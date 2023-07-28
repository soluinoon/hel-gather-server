package com.mate.helgather.controller;

import com.mate.helgather.dto.TodayExerciseResponseDto;
import com.mate.helgather.service.ExerciseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc // -> webAppContextSetup(webApplicationContext)
@AutoConfigureRestDocs // -> apply(documentationConfiguration(restDocumentation))
@WebMvcTest(ExerciseController.class)
@MockBean(JpaMetamodelMappingContext.class)
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseService exerciseService;

    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjb3JlTmljazciLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjg4NDcyMzA3fQ.bBWrIpULfrZsIUAF-MrhPa100k-b0d6LF84Iyr-Gjss";
    private String imageUrl1 = "https://s3.bucket.image3.com";
    private String httpImageUrl1 = "http://s3.bucket.image3.com";
    private String imageUrl2 = "https://s3.bucket.image2.com";
    private String imageUrl3 = "https://s3.bucket.image1.com";

    @Test
    @DisplayName("운동 인증 테스트")
    @WithMockUser(username = "김씨")
        // 오류사항1
    void create() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "file".getBytes(StandardCharsets.UTF_8));

        when(exerciseService.save(anyLong(), any(MockMultipartFile.class)))
                .thenReturn(new TodayExerciseResponseDto(imageUrl1));

        this.mockMvc.perform(RestDocumentationRequestBuilders.multipart("/exercises")
                        .file(multipartFile)
                        .header("Authorization", "Bearer " + token)
                        .queryParam("member", "1")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())) // 오류사항2
                .andExpect(status().isOk())
                .andDo(document("exercise_post_API",
                        requestParameters(
                                parameterWithName("member").description("멤버 id"),
                                parameterWithName("_csrf").description("csrf").ignored() // requestParam 사용시 에러처리
                        ),
                        requestParts(
                                partWithName("file").description("동영상 파일")
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("성공여부"),
                                fieldWithPath("code").description("코드"),
                                fieldWithPath("message").description("설명"),
                                fieldWithPath("result").description("결과"),
                                fieldWithPath("result.imageUrl").description("이미지 URL")
                        )

                ));
    }

    @Test
    @DisplayName("게시글 id 조회 테스트")
    @WithMockUser(username = "김씨")
        // 오류사항1
    void findAllByMemberId() throws Exception {
        // given
        List<TodayExerciseResponseDto> todayExerciseResponseDtos = new ArrayList<>();
        todayExerciseResponseDtos.add(new TodayExerciseResponseDto(imageUrl1));
        todayExerciseResponseDtos.add(new TodayExerciseResponseDto(imageUrl2));
        todayExerciseResponseDtos.add(new TodayExerciseResponseDto(imageUrl3));

        //when
        when(exerciseService.findAllByMemberId(anyLong())).thenReturn(todayExerciseResponseDtos);

        this.mockMvc.perform(get("/exercises")
                        .header("Authorization", "Bearer " + token)
                        .queryParam("member", "1")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())) // 오류사항2

                // then
                .andExpect(status().isOk())
                .andDo(document("운동 게시물 전체조회 API",
                        requestParameters(
                                parameterWithName("member").description("멤버 id"),
                                parameterWithName("_csrf").description("csrf").ignored() // requestParam 사용시 에러처리
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("성공여부"),
                                fieldWithPath("code").description("코드"),
                                fieldWithPath("message").description("설명"),
                                fieldWithPath("result").description("결과"),
                                fieldWithPath("result[].imageUrl").description("이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 id 조회 테스트")
    @WithMockUser(username = "김씨")
    void deleteV1() throws Exception {
        // given
        // when

        this.mockMvc.perform(delete("/exercises")
                        .header("Authorization", "Bearer " + token)
                        .queryParam("member", "1")
                        .queryParam("imageUrl", "http://sff.sl")
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())) // 오류사항2
                // then
                .andExpect(status().isOk())
                .andDo(document("운동 게시물 삭제 API",
                        requestParameters(
                                parameterWithName("member").description("멤버 id"),
                                parameterWithName("imageUrl").description("삭제할 이미지 URL(HTTP)"),
                                parameterWithName("_csrf").description("csrf").ignored() // requestParam 사용시 에러처리
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("성공여부"),
                                fieldWithPath("code").description("코드"),
                                fieldWithPath("message").description("설명"),
                                fieldWithPath("result").description("결과")
                        )
                ));
    }
}