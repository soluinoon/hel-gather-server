package com.mate.helgather.service;

import com.mate.helgather.domain.Member;
import com.mate.helgather.dto.MemberLoginRequestDto;
import com.mate.helgather.dto.MemberRequestDto;
import com.mate.helgather.dto.MemberResponseDto;
import com.mate.helgather.exception.BaseException;
import com.mate.helgather.exception.ErrorCode;
import com.mate.helgather.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mate.helgather.exception.ErrorCode.NO_SUCH_MEMBER_ERROR;
import static com.mate.helgather.exception.ErrorCode.PASSWORD_CORRECT_ERROR;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) throws BaseException {
        validateMemberRequest(memberRequestDto);

        Member member = memberRequestDto.toEntity();
        memberRepository.save(member);

        return new MemberResponseDto(
                member.getId(),
                member.getNickname()
        );
    }

    public MemberResponseDto loginMember(MemberLoginRequestDto memberLoginRequestDto) throws BaseException {

        String nickname = memberLoginRequestDto.getNickname();
        String password = memberLoginRequestDto.getPassword();

        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new BaseException(NO_SUCH_MEMBER_ERROR));

        // 비밀번호가 해당 아이디와 일치하는지 확인
        if (password.equals(member.getPassword())) {
            throw new BaseException(PASSWORD_CORRECT_ERROR);
        }

        // jwt 발급


        return new MemberResponseDto(
                member.getId(),
                nickname
        );
    }


    private void validateMemberRequest(MemberRequestDto memberRequestDto) throws BaseException {

        String phonePattern = "^\\d{3}-\\d{3,4}-\\d{4}$";
        Matcher phoneMatcher = Pattern.compile(phonePattern).matcher(memberRequestDto.getPhone());

        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}$";
        Matcher passwordMatcher = Pattern.compile(passwordPattern).matcher(memberRequestDto.getPassword());

        if (!StringUtils.hasText(memberRequestDto.getUserName())) {//이름 입력 안할 시
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
