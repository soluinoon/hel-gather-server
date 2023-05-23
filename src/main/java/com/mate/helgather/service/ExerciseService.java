package com.mate.helgather.service;

import com.mate.helgather.dto.ExerciseResponseDto;
import com.mate.helgather.repository.AmazonS3Repository;
import com.mate.helgather.repository.ExerciseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ExerciseService {
    private final AmazonS3Repository amazonS3Repository;
    private final ExerciseRepository exerciseRepository;
    private static final String BASE_DIR = "images";

    public void saveExercise(MultipartFile multipartFile) {
        File file = new File(getLocalHomeDirectory(), createPath(multipartFile.getContentType()));
        amazonS3Repository.save(multipartFile, file, createPath(multipartFile.getContentType()));
    }

    public String createPath(String contentType) {
        String fildId =  UUID.randomUUID().toString();
        String homeDirectory = System.getProperty("user.home");
        String format = null;

        if (StringUtils.hasText(contentType)) {
            format = contentType.substring(contentType.lastIndexOf('/') + 1);
        }
        return String.format("%s/%s.%s", BASE_DIR, fildId, format);
    }

    public String getLocalHomeDirectory() {
        return System.getProperty("user.home");
    }

    public String getFormat(String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType.substring(contentType.lastIndexOf('/') + 1);
        }
        return null;
    }

}
