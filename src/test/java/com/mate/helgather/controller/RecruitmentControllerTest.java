package com.mate.helgather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.helgather.service.RecruitmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc // -> webAppContextSetup(webApplicationContext)
@AutoConfigureRestDocs // -> apply(documentationConfiguration(restDocumentation))
@WebMvcTest(RecruitmentController.class)
class RecruitmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecruitmentService recruitmentService;

    @Test
    @DisplayName("게시글 생성 테스트")
    void recruitment_test() {
        String requestBody = "{\"memberId\": 1, \"location\": 2, \"subLocation\": 1, \"title\": \"같이운동하실분!!\", \"description\": \"같이운동하실분구합니다@@!!\"}";
        ObjectMapper objectMapper;
        Object correctRequest;
//        mockMvc.
//        this.mockMvc.perform(post("/recruitments")
//                        .header(sf)
//                        .conte
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .("{\"memberId\":1,\"location\":2,\"subLocation\":1,\"title\":\"같이운동하실분!!\",\"description\":\"같이운동하실분구합니다@@!!\"}")

    }
}