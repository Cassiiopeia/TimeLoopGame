package com.suhsaechan.dongbanza.member.service;

import com.suhsaechan.dongbanza.common.exception.ErrorCode;
import com.suhsaechan.dongbanza.common.exception.api.MemberException;
import com.suhsaechan.dongbanza.common.jwt.dto.CustomUserDetails;
import com.suhsaechan.dongbanza.common.jwt.service.JwtUtil;
import com.suhsaechan.dongbanza.common.jwt.service.TokenService;
import com.suhsaechan.dongbanza.member.domain.constants.MemberRole;
import com.suhsaechan.dongbanza.member.domain.constants.MemberStatus;
import com.suhsaechan.dongbanza.member.domain.entity.Member;
import com.suhsaechan.dongbanza.member.dto.request.MemberLoginForm;
import com.suhsaechan.dongbanza.member.dto.request.MemberSignUpForm;
import com.suhsaechan.dongbanza.member.dto.response.MemberDto;
import com.suhsaechan.dongbanza.member.dto.response.MemberLoginResponse;
import com.suhsaechan.dongbanza.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final TokenService tokenService;

  public MemberDto save(MemberSignUpForm form) {
    // 이메일 중복 확인
    if (memberRepository.findByEmail(form.getEmail()).isPresent()) {
      throw new MemberException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    // 닉네임 중복 확인
    if (memberRepository.findByCharacterName(form.getNickname()).isPresent()) {
      throw new MemberException(ErrorCode.CHARACTER_NAME_ALREADY_EXISTS);
    }

    return MemberDto.from(
        memberRepository.save(Member.builder()
            .email(form.getEmail())
            .password(passwordEncoder.encode(form.getPassword()))
            .characterName(form.getNickname())
            .role(MemberRole.USER)
            .status(MemberStatus.ACTIVE)
            .profileImageUrl(null)
            .totalScore(0) // 초기 호감도는 0으로 설정
            .birthDate(form.getBirthDate())
            .gender(form.getGender())
            .mbti(form.getMbti())
            .totalRegressionCount(0) // 초기 회귀 횟수는 0으로 설정
            .gameProgress("NOT_STARTED") // 초기 게임 진행 상태는 'NOT_STARTED'로 설정
            .refreshToken(null)
            .build()
        )
    );
  }

  public MemberLoginResponse login(MemberLoginForm form) {
    // 아이디 검증
    Member member = memberRepository.findByEmail(form.getEmail())
        .orElseThrow(() -> new MemberException(ErrorCode.EMAIL_NOT_FOUND));

    // 비밀번호 검증
    if (!passwordEncoder.matches(form.getPassword(), member.getPassword())) {
      throw new MemberException(ErrorCode.INVALID_PASSWORD);
    }

    String accessToken = jwtUtil.createAccessToken(
        CustomUserDetails.builder()
            .member(member)
            .build());
    String refreshToken = tokenService.createRefreshToken(member);

    return MemberLoginResponse.from(member, accessToken, refreshToken);
  }

  public MemberDto getMemberById(Long id) {
    Member member = memberRepository.findById(id)
        .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
    return MemberDto.from(member);
  }
}
