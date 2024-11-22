package com.kjunh972.QRManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kjunh972.QRManager.model.QuizScore;

import java.util.List;

@Repository
public interface QuizScoreRepository extends JpaRepository<QuizScore, Long> {
    // 상위 10개의 점수를 점수 내림차순으로 조회
    List<QuizScore> findTop10ByOrderByScoreDesc();
    
    // 특정 닉네임의 모든 점수 조회 
    List<QuizScore> findByNicknameOrderByScoreDesc(String nickname);
    
    // 전체 평균 점수보다 높은 점수들을 조회 
    @Query("SELECT qs FROM QuizScore qs WHERE qs.score > (SELECT AVG(score) FROM QuizScore)")
    List<QuizScore> findScoresAboveAverage();
}