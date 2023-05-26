package com.mate.helgather.service;

import com.mate.helgather.domain.Member;
import com.mate.helgather.dto.MemberRequestDto;
import com.mate.helgather.dto.MemberResponseDto;
import com.mate.helgather.dto.TokenDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.MemberRepository;
import com.mate.helgather.util.JwtTokenProvider;
import com.mate.helgather.dto.MemberLoginResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) throws BaseException {
        validateMemberRequest(memberRequestDto);

        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);

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
                    .orElseThrow(() -> new BaseException(ErrorCode.NO_SUCH_MEMBER_ERROR));

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
