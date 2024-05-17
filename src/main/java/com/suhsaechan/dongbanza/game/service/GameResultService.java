package com.suhsaechan.dongbanza.game.service;

import com.suhsaechan.dongbanza.game.domain.entity.GameResult;
import com.suhsaechan.dongbanza.game.dto.request.GameResultRequest;
import com.suhsaechan.dongbanza.game.dto.response.GameResultResponse;
import com.suhsaechan.dongbanza.game.repository.GameResultRepository;
import com.suhsaechan.dongbanza.member.domain.entity.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameResultService {

  private final GameResultRepository gameResultRepository;

  public void saveGameResult(GameResultRequest request, Member member) {
    GameResult gameResult = request.toGameResult(member);
    gameResultRepository.save(gameResult);
  }

  public List<GameResultResponse> getGameResultsByMember(Long memberId) {
    return gameResultRepository.findByMemberId(memberId).stream()
        .map(GameResultResponse::from)
        .collect(Collectors.toList());
  }

  public void deleteGameResult(Long gameResultId, Long memberId) {
    GameResult gameResult = gameResultRepository.findByIdAndMemberId(gameResultId, memberId)
        .orElseThrow(() -> new IllegalArgumentException("Game result not found"));

    gameResultRepository.delete(gameResult);
  }
}