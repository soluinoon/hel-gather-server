package com.mate.helgather.controller;

import com.mate.helgather.domain.Member;
import com.mate.helgather.domain.Recruitment;
import com.mate.helgather.domain.status.MemberStatus;
import com.mate.helgather.domain.status.RecruitmentStatus;
import com.mate.helgather.dto.RecruitmentListResponseDto;
import com.mate.helgather.dto.RecruitmentOptions;
import com.mate.helgather.dto.RecruitmentResponseDto;
import com.mate.helgather.service.RecruitmentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc // -> webAppContextSetup(webApplicationContext)
@AutoConfigureRestDocs // -> apply(documentationConfiguration(restDocumentation))
@WebMvcTest(RecruitmentController.class)
@MockBean(JpaMetamodelMappingContext.class)
class RecruitmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecruitmentService recruitmentService;

    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjb3JlTmljazciLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjg4NDcyMzA3fQ.bBWrIpULfrZsIUAF-MrhPa100k-b0d6LF84Iyr-Gjss";

    @Test
    @DisplayName("게시글 생성 테스트")
    @WithMockUser(username = "김씨") // 오류사항1
    void create() throws Exception {
        this.mockMvc.perform(post("/recruitments")
                        .header("Authorization", "Bearer " + token)
                        .content("{\"memberId\":1,\"location\":2,\"subLocation\":1,\"title\":\"같이운동하실분!!\",\"description\":\"같이운동하실분구합니다@@!!\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())) // 오류사항2
                .andExpect(status().isOk())
                .andDo(document("게시물 생성 API",
                        requestFields(
                                fieldWithPath("memberId").description("멤버 id"),
                                fieldWithPath("location").description("로케이션 넘버"),
                                fieldWithPath("subLocation").description("서브 로케이션 넘버"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("description").description("글 내용"))

                ));
    }

    @Test
    @DisplayName("게시글 id 조회 테스트")
    @WithMockUser(username = "김씨") // 오류사항1
    void findById() throws Exception {
        // given
        final Member member = Member.builder()
                .nickname("스님")
                .phone("010-2323-2232")
                .status(MemberStatus.ACTIVE)
                .birthDate(LocalDate.of(2004, 5, 31))
                .password("kksf")
                .build();
        final RecruitmentResponseDto recruitmentResponseDto = new RecruitmentResponseDto(Recruitment.builder()
                .id(1L)
                .member(member)
                .subLocation(2L)
                .location(2L)
                .title("같이 운동하실분")
                .description("내일쯤 예상하고 있습니다.")
                .status(RecruitmentStatus.ACTIVE)
                .build());
        //when
        when(recruitmentService.findById(anyLong())).thenReturn(recruitmentResponseDto);

        this.mockMvc.perform(get("/recruitments/{id}", recruitmentResponseDto.getRecruitmentId())
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(csrf())) // 오류사항2

                // then
                .andExpect(status().isOk())
                .andDo(document("게시물 ID 조회 API",
                        pathParameters(
                                parameterWithName("id").description("게시물 id")
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("성공여부"),
                                fieldWithPath("code").description("코드"),
                                fieldWithPath("message").description("설명"),
                                fieldWithPath("result").description("결과"),
                                fieldWithPath("result.recruitmentId").description("게시글 id"),
                                fieldWithPath("result.memberId").description("멤버 id"),
                                fieldWithPath("result.nickname").description("멤버 닉네임"),
                                fieldWithPath("result.title").description("제목"),
                                fieldWithPath("result.description").description("글 내용"),
                                fieldWithPath("result.createdAt").description("작성일")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 전체 조회 테스트")
    @WithMockUser(username = "김씨") // 오류사항1
    void findAll() throws Exception {
        // given
        final Member member = Member.builder()
                .nickname("스님")
                .phone("010-2323-2232")
                .status(MemberStatus.ACTIVE)
                .birthDate(LocalDate.of(2004, 5, 31))
                .password("kksf")
                .build();
        final List<RecruitmentListResponseDto> recruitmentResponseDtos = List.of(new RecruitmentListResponseDto(Recruitment.builder()
                    .id(1L)
                    .member(member)
                    .subLocation(2L)
                    .location(2L)
                    .title("같이 운동하실분")
                    .description("내일쯤 예상하고 있습니다.")
                    .status(RecruitmentStatus.ACTIVE)
                    .build()),
                new RecruitmentListResponseDto(Recruitment.builder()
                    .id(2L)
                    .member(member)
                    .subLocation(4L)
                    .location(2L)
                    .title("상암 헬스장에서 운동하실분 구합니다.")
                    .description("3~6시 아무때나 연락주세요!!")
                    .status(RecruitmentStatus.ACTIVE)
                    .build()));
        System.out.println("in tc : " + recruitmentResponseDtos);
        //when
        when(recruitmentService.findAll()).thenReturn(recruitmentResponseDtos);

        ResultActions resultActions = this.mockMvc.perform(get("/recruitments/all")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()));
        resultActions // 오류사항2

                // then
                .andExpect(status().isOk())
                .andDo(document("게시물 전체 조회 API",
                        responseFields(
                                fieldWithPath("isSuccess").description("성공여부"),
                                fieldWithPath("code").description("코드"),
                                fieldWithPath("message").description("설명"),
                                fieldWithPath("result").description("결과"),
                                fieldWithPath("result[].recruitmentId").description("게시글 id"),
                                fieldWithPath("result[].title").description("제목"),
                                fieldWithPath("result[].nickname").description("멤버 닉네임"),
                                fieldWithPath("result[].createdAt").description("작성일")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 필터링 조회 테스트")
    @WithMockUser(username = "김씨") // 오류사항1
    void findByOptions() throws Exception {
        // given
        final Member member = Member.builder()
                .nickname("스님")
                .phone("010-2323-2232")
                .status(MemberStatus.ACTIVE)
                .birthDate(LocalDate.of(2004, 5, 31))
                .password("kksf")
                .build();
        final List<RecruitmentListResponseDto> recruitmentResponseDtos = List.of(new RecruitmentListResponseDto(Recruitment.builder()
                        .id(1L)
                        .member(member)
                        .subLocation(1L)
                        .location(2L)
                        .title("같이 운동하실분")
                        .description("내일쯤 예상하고 있습니다.")
                        .status(RecruitmentStatus.ACTIVE)
                        .build()),
                new RecruitmentListResponseDto(Recruitment.builder()
                        .id(2L)
                        .member(member)
                        .subLocation(4L)
                        .location(2L)
                        .title("상암 헬스장에서 운동하실분 구합니다.")
                        .description("3~6시 아무때나 연락주세요!!")
                        .status(RecruitmentStatus.ACTIVE)
                        .build()));
        RecruitmentOptions recruitmentOptions = new RecruitmentOptions(2L, 1L, 80,
                100, 80, 120, 80, 110);
        //when
        when(recruitmentService.findByOptions(any())).thenReturn(recruitmentResponseDtos);
        this.mockMvc.perform(get("/recruitments")
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()) // 오류사항2
                .queryParam("location", "2")
                .queryParam("subLocation", "1")
                .queryParam("maxDeadLift", "100")
                .queryParam("minDeadLift", "80")
                .queryParam("maxSquat", "120")
                .queryParam("minSquat", "80")
                .queryParam("maxBenchPress", "110")
                .queryParam("minBenchPress", "90"))
                .andExpect(status().isOk())

                .andDo(document("게시물 필터링 조회 API",
                        requestParameters(
                                parameterWithName("location").description("지역 번호"),
                                parameterWithName("subLocation").description("서브지역 번호"),
                                parameterWithName("maxDeadLift").description("최대 데드리프트 수치"),
                                parameterWithName("minDeadLift").description("최소 데드리프트 수치"),
                                parameterWithName("maxSquat").description("최대 스쿼트 수치"),
                                parameterWithName("minSquat").description("최소 스쿼트 수치"),
                                parameterWithName("maxBenchPress").description("최대 벤치 프레스 수치"),
                                parameterWithName("minBenchPress").description("최소 벤치 프레스 수치"),
                                parameterWithName("_csrf").description("csrf").ignored()
                        ),
                        responseFields(
                                fieldWithPath("isSuccess").description("성공여부"),
                                fieldWithPath("code").description("코드"),
                                fieldWithPath("message").description("설명"),
                                fieldWithPath("result").description("결과"),
                                fieldWithPath("result[].recruitmentId").description("게시글 id"),
                                fieldWithPath("result[].title").description("제목"),
                                fieldWithPath("result[].nickname").description("멤버 닉네임"),
                                fieldWithPath("result[].createdAt").description("작성일")
                        )
                ));
    }
}