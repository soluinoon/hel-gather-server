package com.mate.helgather.service;

import com.mate.helgather.domain.Exercise;
import com.mate.helgather.domain.Member;
import com.mate.helgather.domain.status.ExerciseCategory;
import com.mate.helgather.dto.ExerciseRequestDto;
import com.mate.helgather.dto.ExerciseResponseDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.DefaultImageException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.exception.S3NoPathException;
import com.mate.helgather.repository.AmazonS3Repository;
import com.mate.helgather.repository.ExerciseRepository;
import com.mate.helgather.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExerciseService {
    private final AmazonS3Repository amazonS3Repository;
    private final ExerciseRepository exerciseRepository;
    private final MemberRepository memberRepository;
    private static final String VIDEO_BASE_DIR = "videos";
    private static final String THUMBNAIL_BASE_DIR = "thumbnails";
    private static final String THUMBNAIL_EXTENSION = "png";
    private static final String DEFAULT_IMAGE_PATH = "src/main/resources/static/images/default-thumbnail.png";

    public ExerciseResponseDto saveExercise(Long memberId, String category, MultipartFile multipartFile) throws Exception {
        ExerciseCategory exerciseCategory = ExerciseCategory.of(category);
        if (exerciseCategory.equals(ExerciseCategory.TODAY)) {
            return saveToday(exerciseCategory, memberId, multipartFile);
        } else {
            return saveSBD(exerciseCategory, memberId, multipartFile);
        }
    }
    public ExerciseResponseDto saveSBD(ExerciseCategory exerciseCategory, Long memberId, MultipartFile multipartFile) throws Exception {
        String fileId = UUID.randomUUID().toString();
        File videoFile = new File(getLocalHomeDirectory(), createPath(multipartFile.getContentType(), fileId));
        String thumbNailPath = String.format("%s/%s.%s", THUMBNAIL_BASE_DIR, fileId, THUMBNAIL_EXTENSION);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR));

        try {
            multipartFile.transferTo(videoFile);
            File thumbnailFile = extractThumbnail(videoFile, thumbNailPath);
            // 비디오 세이브
            String videoUrl = amazonS3Repository.save(videoFile, createPath(multipartFile.getContentType(), fileId));
            String thumbnailUrl = DEFAULT_IMAGE_PATH;
            // 이미지 추출이 성공했다면 세이브 진행.
            if (!thumbnailFile.getPath().equals(DEFAULT_IMAGE_PATH)) {
                thumbnailUrl = amazonS3Repository.save(thumbnailFile, thumbNailPath);
            }
            // 레포지터리에 운동영상, 썸네일 영상 저장
            Exercise exercise = exerciseRepository.save(Exercise.builder()
                    .member(member)
                    .category(exerciseCategory)
                    .videoUrl(videoUrl)
                    .thumbnailUrl(thumbnailUrl)
                    .build());
            return new ExerciseResponseDto(exercise);
        } catch (IOException e) {
            throw new S3NoPathException();
        }
    }

    public ExerciseResponseDto saveToday(ExerciseCategory exerciseCategory, Long memberId, MultipartFile multipartFile) throws Exception {
        String fileId = UUID.randomUUID().toString();
        String thumbNailPath = String.format("%s/%s.%s", THUMBNAIL_BASE_DIR, fileId, THUMBNAIL_EXTENSION);
        File file = new File(getLocalHomeDirectory(), thumbNailPath);

        try {
            multipartFile.transferTo(file);
            String thumbnailUrl = amazonS3Repository.save(file, thumbNailPath);
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR));
            // 레포지터리에 운동영상, 썸네일 영상 저장
            Exercise exercise = exerciseRepository.save(Exercise.builder()
                    .member(member)
                    .category(exerciseCategory)
                    .videoUrl("")
                    .thumbnailUrl(thumbnailUrl)
                    .build());
            return new ExerciseResponseDto(exercise);
        } catch (IOException e) {
            throw new S3NoPathException();
        }
    }

    public String createPath(String contentType, String fileId) {
        String format = null;

        if (StringUtils.hasText(contentType)) {
            format = contentType.substring(contentType.lastIndexOf('/') + 1);
        }

        return String.format("%s/%s.%s", VIDEO_BASE_DIR, fileId, format);
    }

    public String getLocalHomeDirectory() {
        // 로컬
        return System.getProperty("user.home");
        // 서버
//        return "/home/ubuntu/";
    }

    public String getFormat(String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType.substring(contentType.lastIndexOf('/') + 1);
        }

        return null;
    }

    public File extractThumbnail(File source, String path) throws Exception {
        // 썸네일 파일 생성
        File thumbnail = new File(getLocalHomeDirectory(), path);

        try {
            FrameGrab frameGrab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(source));
            // 첫 프레임의 데이터
            frameGrab.seekToSecondPrecise(0);
            Picture picture = frameGrab.getNativeFrame();
            // 썸네일 파일에 복사
            BufferedImage bi = AWTUtil.toBufferedImage(picture);
            ImageIO.write(bi, THUMBNAIL_EXTENSION, thumbnail);

            return thumbnail;
        } catch (Exception e) {
            try {
                // 실패했을 경우에 기본 이미지를 사용
                e.printStackTrace();
                return new File(DEFAULT_IMAGE_PATH);
            } catch (Exception ex) {
                throw new DefaultImageException();
            }
        }
    }

    public List<ExerciseResponseDto> findExercisesByCategory(Long memberId, String category) throws Exception {
        ExerciseCategory exerciseCategory = ExerciseCategory.of(category);

        if (!memberRepository.existsById(memberId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
        List<Exercise> exercises = exerciseRepository.findAllByMemberIdAndCategory(memberId, exerciseCategory);
        List<ExerciseResponseDto> exerciseResponseDtos = new ArrayList<>();

        for (Exercise exercise : exercises) {
            exerciseResponseDtos.add(new ExerciseResponseDto(exercise));
        }

        return exerciseResponseDtos;
    }

    public List<ExerciseResponseDto> findSBD(Long memberId) throws Exception {
        ExerciseCategory exerciseCategory = ExerciseCategory.TODAY;

        if (!memberRepository.existsById(memberId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }

        List<ExerciseResponseDto> exerciseResponseDtos = new ArrayList<>();

        exerciseResponseDtos.add(new ExerciseResponseDto(exerciseRepository
                .findTopByMemberIdAndCategoryOrderByCreatedAtDesc(memberId, ExerciseCategory.DEAD_LIFT).orElse(Exercise.builder()
                        .category(ExerciseCategory.DEAD_LIFT)
                        .thumbnailUrl("")
                        .videoUrl("")
                        .build())));
        exerciseResponseDtos.add(new ExerciseResponseDto(exerciseRepository
                .findTopByMemberIdAndCategoryOrderByCreatedAtDesc(memberId, ExerciseCategory.SQUAT).orElse(Exercise.builder()
                        .category(ExerciseCategory.SQUAT)
                        .thumbnailUrl("")
                        .videoUrl("")
                        .build())));
        exerciseResponseDtos.add(new ExerciseResponseDto(exerciseRepository
                .findTopByMemberIdAndCategoryOrderByCreatedAtDesc(memberId, ExerciseCategory.BENCH_PRESS).orElse(Exercise.builder()
                        .category(ExerciseCategory.BENCH_PRESS)
                        .thumbnailUrl("")
                        .videoUrl("")
                        .build())));
        return exerciseResponseDtos;
    }

    public void deleteExercise(Long memberId, ExerciseRequestDto exerciseRequestDto) throws Exception {
        if (!memberRepository.existsById(memberId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }
        amazonS3Repository.delete(exerciseRequestDto.getVideoUrl());
        amazonS3Repository.delete(exerciseRequestDto.getThumbNailUrl());
        exerciseRepository.deleteByMemberIdAndVideoUrlAndThumbnailUrl(memberId, exerciseRequestDto.getVideoUrl(),
                                                    exerciseRequestDto.getThumbNailUrl());
    }
}
