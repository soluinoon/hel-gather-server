package com.mate.helgather.service;

import com.mate.helgather.domain.Member;
import com.mate.helgather.domain.MemberProfile;
import com.mate.helgather.domain.status.MemberStatus;
import com.mate.helgather.dto.*;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.AmazonS3Repository;
import com.mate.helgather.repository.MemberProfileRepository;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mate.helgather.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberProfileRepository memberProfileRepository;
    private final AmazonS3Repository amazonS3Repository;
    private static final String MEMBER_PROFILE_BASE_DIR = "profiles";
    private static final String DEFAULT_IMAGE_URL = "http://hel-gather.s3.ap-northeast-2.amazonaws.com/profiles/Base_Image.png";

    @Transactional
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) throws BaseException {
        validateMemberRequest(memberRequestDto);

        Member member = memberRequestDto.toEntity();
        member.getRoles().add("USER"); // 주석 해제

        member = memberRepository.save(member);

        MemberProfile profile = MemberProfile.builder()
                .member(member)
                .imageUrl(DEFAULT_IMAGE_URL)
                .build();

        memberProfileRepository.save(profile);

        return new MemberResponseDto(
                member.getId(),
                member.getNickname()
        );
    }

    @Transactional
    public MemberLoginResponseDto loginMember(String nickname, String password) throws BaseException {
        if (!StringUtils.hasText(nickname)) {
            throw new BaseException(ErrorCode.NO_INPUT_NICKNAME);
        }

        if (!StringUtils.hasText(password)) {
            throw new BaseException(ErrorCode.NO_INPUT_PASSWORD);
        }

        // 1. Login ID/PW 기반으로 Authentication 객체 생성
        // 이 때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(nickname, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 가 실행
        try {
            Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 3. Authentication 객체를 통해 Principal 얻기
            Object principal = authenticate.getPrincipal();

            // 4. UserDetails 캐스팅
            UserDetails userDetails = (UserDetails) principal;

            TokenDto tokenDto = jwtTokenProvider.generateToken(authenticate);

            Member member = memberRepository.findByNickname(nickname)
                    .orElseThrow(() -> new BaseException(NO_SUCH_MEMBER_ERROR));

            // 인증 정보를 기반으로 JWT 토큰 생성
            return MemberLoginResponseDto.builder()
                    .memberId(member.getId())
                    .nickname(userDetails.getUsername())
                    .grantType("Bearer")
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getRefreshToken())
                    .build();
        } catch (AuthenticationException e) {
            throw new BaseException(ErrorCode.PASSWORD_CORRECT_ERROR);
        }
    }

    @Transactional
    public MemberProfileResponseDto createProfile(Long memberId, String introduction,
                                                  Integer benchPress, Integer squat, Integer deadlift) {

        //특정 id를 가진 멤버 찾기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(NO_SUCH_MEMBER_ERROR));

        //memberProfile 에 이미 존재하는 회원인지 확인하기
        if (memberProfileRepository.existsByMember_id(memberId)) {
            throw new BaseException(EXIST_MEMBER_PROFILE);
        }

        MemberProfile profile = MemberProfile.builder()
                .member(member)
                .imageUrl(DEFAULT_IMAGE_URL)
                .introduction(introduction)
                .benchPress(benchPress)
                .squat(squat)
                .deadLift(deadlift)
                .build();

        memberProfileRepository.save(profile);

        return MemberProfileResponseDto.builder()
                .memberId(memberId)
                .imageUrl(DEFAULT_IMAGE_URL)
                .introduction(introduction)
                .benchPress(benchPress)
                .squat(squat)
                .deadlift(deadlift)
                .exerciseCount(0)
                .build();
    }

    @Transactional
    public MemberProfileImageResponseDto createProfileImage(Long memberId, MultipartFile multipartFile) throws Exception {

        if (!memberRepository.existsById(memberId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }

        //MemberProfile 데이터에서 특정 데이터 가져오기
        MemberProfile memberProfile = memberProfileRepository.findByMember_id(memberId)
                .orElseThrow(() -> new BaseException(NO_SUCH_MEMBER_PROFILE));

        String imageUrl = amazonS3Repository.saveV2(multipartFile, MEMBER_PROFILE_BASE_DIR);
        String httpImageUrl = imageUrl.substring(0, 4) + imageUrl.substring(5);
        //memberProfile 에 이미지 등록해주기
        memberProfile.setImageUrl(httpImageUrl);
        memberProfileRepository.save(memberProfile);

        return MemberProfileImageResponseDto.builder()
                .imageUrl(httpImageUrl)
                .build();
    }

    @Transactional
    public MemberProfileImageResponseDto updateProfileImage(Long memberId, MultipartFile multipartFile) throws Exception {
        if (!memberRepository.existsById(memberId)) {
            throw new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR);
        }

        //MemberProfile 데이터에서 특정 데이터 가져오기
        MemberProfile memberProfile = memberProfileRepository.findByMember_id(memberId)
                .orElseThrow(() -> new BaseException(NO_SUCH_MEMBER_PROFILE));

        String[] split = memberProfile.getImageUrl().split("/");

        //s3에서 기본 프로필 이미지 외 기존 이미지 정보 지우기
//        if (!split[split.length - 1].equals("Base_Image.png")) {
//            amazonS3Repository.delete(extractKey(memberProfile.getImageUrl(), MEMBER_PROFILE_BASE_DIR));
//        }

        //memberProfile 에 이미지 등록해주기
        String imageUrl = amazonS3Repository.saveV2(multipartFile, MEMBER_PROFILE_BASE_DIR);
        String httpImageUrl = imageUrl.substring(0, 4) + imageUrl.substring(5);
        memberProfile.setImageUrl(httpImageUrl);
        memberProfileRepository.save(memberProfile);

        return MemberProfileImageResponseDto.builder()
                .imageUrl(httpImageUrl)
                .build();
    }

    @Transactional
    public MemberProfileResponseDto updateProfile(Long memberId, String introduction,
                                                  Integer benchPress, Integer squat, Integer deadlift) {
        //특정 id를 가진 멤버 찾기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(NO_SUCH_MEMBER_ERROR));

        MemberProfile profile = memberProfileRepository.findByMember_id(memberId)
                .orElseThrow(() -> new BaseException(NO_SUCH_MEMBER_PROFILE));

        profile.setIntroduction(introduction);
        profile.setBenchPress(benchPress);
        profile.setDeadLift(deadlift);
        profile.setSquat(squat);

        memberProfileRepository.save(profile);

        return MemberProfileResponseDto.builder()
                .memberId(memberId)
                .imageUrl(profile.getImageUrl())
                .introduction(introduction)
                .benchPress(benchPress)
                .squat(squat)
                .deadlift(deadlift)
                .exerciseCount(profile.getExerciseCount())
                .build();
    }

    public MemberProfileResponseDto getProfile(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new BaseException(NO_SUCH_MEMBER_ERROR);
        }

        MemberProfile profile = memberProfileRepository.findByMember_id(memberId)
                .orElseThrow(() -> new BaseException(NO_SUCH_MEMBER_PROFILE));

        return MemberProfileResponseDto.builder()
                .memberId(memberId)
                .imageUrl(profile.getImageUrl())
                .introduction(profile.getIntroduction())
                .benchPress(profile.getBenchPress())
                .squat(profile.getSquat())
                .deadlift(profile.getDeadLift())
                .exerciseCount(profile.getExerciseCount())
                .build();
    }

    public MemberResponseDto deleteMember(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(NO_SUCH_MEMBER_ERROR));

        if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new BaseException(ALREADY_DELETE_MEMBER_ERROR);
        }

        //STATUS 바꾸기
        member.changeStatus(MemberStatus.INACTIVE);

        return MemberResponseDto.builder()
                .id(memberId)
                .nickname(member.getNickname())
                .build();
    }

    private String extractKey(String url, String baseUrl) {
        int index = url.indexOf(baseUrl);
        return url.substring(index);
    }

    private void validateMemberRequest(MemberRequestDto memberRequestDto) throws BaseException {

        String phonePattern = "^\\d{3}-\\d{3,4}-\\d{4}$";
        Matcher phoneMatcher = Pattern.compile(phonePattern).matcher(memberRequestDto.getPhone());

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";
        Matcher passwordMatcher = Pattern.compile(passwordPattern).matcher(memberRequestDto.getPassword());

        if (!StringUtils.hasText(memberRequestDto.getName())) {//이름 입력 안할 시
            throw new BaseException(ErrorCode.NO_INPUT_USERNAME);
        }

        if (!StringUtils.hasText(memberRequestDto.getPhone())) {//전화번호 입력 안할 시
            throw new BaseException(ErrorCode.NO_INPUT_PHONE);
        }

        if (!StringUtils.hasText(memberRequestDto.getNickname())) {//닉네임 입력 안할 시
            throw new BaseException(ErrorCode.NO_INPUT_NICKNAME);
        }

        if (!StringUtils.hasText(memberRequestDto.getPassword())) {//패스워드 입력 안할 시
            throw new BaseException(ErrorCode.NO_INPUT_PASSWORD);
        }

        if (memberRequestDto.getBirthYear() == null) {//생년 입력 안할 시
            throw new BaseException(ErrorCode.NO_INPUT_BIRTH_YEAR);
        }

        if (memberRequestDto.getBirthMonth() == null) {//생월 입력 안할 시
            throw new BaseException(ErrorCode.NO_INPUT_BIRTH_MONTH);
        }

        if (memberRequestDto.getBirthDay() == null) {//생일 입력 안할 시
            throw new BaseException(ErrorCode.NO_INPUT_BIRTH_DAY);
        }

        //전화번호 패턴
        if (!phoneMatcher.matches()) {
            throw new BaseException(ErrorCode.PHONE_PATTERN_ERROR);
        }

        //비밀번호 패턴
        if (!passwordMatcher.matches()) {
            throw new BaseException(ErrorCode.PASSWORD_PATTERN_ERROR);
        }

        //전화번호 중복
        if (memberRepository.existsByPhone(memberRequestDto.getPhone())) {
            throw new BaseException(ErrorCode.EXIST_PHONE_ERROR);
        }

        //닉네임 중복
        if (memberRepository.existsByNickname(memberRequestDto.getNickname())) {
            throw new BaseException(ErrorCode.EXIST_NICKNAME_ERROR);
        }
    }
}
