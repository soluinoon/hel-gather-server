package com.mate.helgather.repository;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AmazonS3Repository {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    // 키는 /{dir}/fileName.{extention} 형태
    /**
     * 아마존 s3에 저장과 삭제를 담당하는 레포지터리이다.
     * 이건 SBD 때문에 따로 작성되어있다. 추후 리팩터링 예정
     * @param file
     * @param fullPath
     * @return
     * @throws AmazonS3Exception 전역오류 처리
     */
    public String save(File file, String fullPath) throws AmazonS3Exception {
        try {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fullPath, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, fullPath).toString();
        } finally {
            // 임시파일 무조건 삭제하도록 함.
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * s3에 업로드 한다.
     * @param multipartFile 입력으로 받은 멀티파트 파일
     * @param baseDir 베이스 url ex) videos, thumbnails, images 등등
     * @return 저장된 url
     * @throws Exception 전역 관리
     */
    public String saveV2(MultipartFile multipartFile, String baseDir) throws Exception {
        // 랜덤 uuid 생성
        String fileId = UUID.randomUUID().toString();
        String path = createPath(multipartFile.getContentType(), baseDir, fileId);
        File file = new File(getLocalHomeDirectory(), path);

        try {
            multipartFile.transferTo(file);
            amazonS3Client.putObject(new PutObjectRequest(bucket, path, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, path).toString();
        } finally {
            // 임시파일 무조건 삭제하도록 함.
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * S3에 있는 객체를 삭제한다.
     *
     * @param path s3에 존재하는 상대경로(key)
     * @throws AmazonS3Exception
     */
    public void delete(String path) throws AmazonS3Exception {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, path));
    }

    public String getLocalHomeDirectory() {
        // 로컬
//        return System.getProperty("user.home");
        // 서버
        return "/home/ubuntu/";
    }

    /**
     *
     * @param contentType 컨텐츠 타입에서 포멧구함 ex) jpg
     * @param baseDir 베이스 url ex) dir/
     * @param fileId 랜익 파일 아이디
     * @return
     */
    public String createPath(String contentType, String baseDir, String fileId) {
        String format = null;

        if (StringUtils.hasText(contentType)) {
            format = contentType.substring(contentType.lastIndexOf('/') + 1);
        }

        return String.format("%s/%s.%s", baseDir, fileId, format);
    }
}
