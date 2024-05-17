package com.suhsaechan.dongbanza.member.dto.response;

import com.suhsaechan.dongbanza.member.domain.entity.Member;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberDto {
  private Long id;

  private String email;

  private String nickname;

  private String role;

  private String status;

  private String profileImageUrl;

  private Integer score;

  private LocalDate birthDate;

  private String gender;

  private String mbti;

  private Integer regressionCount;

  private String gameProgress;

  private String refreshToken;

  public static MemberDto from(Member member){
    return MemberDto.builder()
        .id(member.getId())
        .email(member.getEmail())
        .nickname(member.getNickname())
        .role(String.valueOf(member.getRole()))
        .status(String.valueOf(member.getStatus()))
        .profileImageUrl(member.getProfileImageUrl())
        .score(member.getScore())
        .birthDate(member.getBirthDate())
        .gender(member.getGender())
        .mbti(member.getMbti())
        .regressionCount(member.getRegressionCount())
        .gameProgress(member.getGameProgress())
        .refreshToken(member.getRefreshToken())
        .build();
  }
}
