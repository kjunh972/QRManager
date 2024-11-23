package com.kjunh972.QRManager.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kjunh972.QRManager.model.QuizScore;
import com.kjunh972.QRManager.repository.QuizScoreRepository;

@Controller
public class QuizController {

    // 퀴즈 점수 저장소 주입
    @Autowired
    private QuizScoreRepository quizScoreRepository;

    // 퀴즈 페이지 보여주기
    @GetMapping("/QuizJun")
    public String showQuizPage(Model model) {
        List<QuizScore> topScores = quizScoreRepository.findTop10ByOrderByScoreDesc();
        model.addAttribute("leaderboard", topScores);
        return "QuizJun";
    }

    // 퀴즈 점수 저장 API
    @PostMapping("/api/quiz/score")
    @ResponseBody
    public ResponseEntity<?> saveScore(@RequestBody QuizScore quizScore) {
        quizScore.setCreatedAt(LocalDateTime.now().withNano(0));
        quizScoreRepository.save(quizScore);
        return ResponseEntity.ok().build();
    }

    // 리더보드 조회 API
    @GetMapping("/api/quiz/leaderboard")
    @ResponseBody
    public List<QuizScore> getLeaderboard() {
        return quizScoreRepository.findTop10ByOrderByScoreDesc();
    }
}