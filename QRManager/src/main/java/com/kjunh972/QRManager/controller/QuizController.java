package com.kjunh972.QRManager.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kjunh972.QRManager.model.QuizMessages;
import com.kjunh972.QRManager.model.QuizScore;
import com.kjunh972.QRManager.repository.QuizScoreRepository;

@Controller
public class QuizController {
    @Autowired
    private QuizScoreRepository quizScoreRepository;
    private final QuizMessages quizMessages = new QuizMessages();

    // 퀴즈 문제 리스트 정의
    private final List<Map<String, Object>> questions = Arrays.asList(
        createQuestion("대한민국의 수도는?", 
            Arrays.asList("서울", "부산", "대구", "인천"), 0),
        createQuestion("1 + 1 = ?", 
            Arrays.asList("1", "2", "3", "4"), 1),
        createQuestion("고양이가 세상을 지배한다면, 가장 먼저 금지할 행동은?", 
            Arrays.asList("개를 산책시키기", "책 읽기", "TV 보기", "청소하기"), 0),
        createQuestion("태양계에서 가장 큰 행성은?",
            Arrays.asList("지구", "화성", "목성", "토성"), 2),
        createQuestion("1년은 몇 개월인가요?",
            Arrays.asList("10개월", "11개월", "12개월", "13개월"), 2),
        createQuestion("한글날은 몇 월 며칠인가요?",
            Arrays.asList("9월 9일", "10월 9일", "11월 9일", "12월 9일"), 1),
        createQuestion("파이의 값은 소수점 둘째 자리까지 얼마인가요?",
            Arrays.asList("3.12", "3.13", "3.14", "3.15"), 2),
        createQuestion("토끼는 당근을 먹을 때 무슨 생각을 할까요?",
            Arrays.asList("이건 내꺼다!", "더 주세요!", "이걸 왜 먹지?", "당근은 진리다!"), 1),
        createQuestion("외계인이 지구를 방문한다면 가장 먼저 할 일은?",
            Arrays.asList("핸드폰 사기", "피자 먹기", "유튜브 보기", "SNS에 인증샷 올리기"), 3),
        createQuestion("지구는 몇 개의 위성을 가지고 있나요?",
            Arrays.asList("0개", "1개", "2개", "3개"), 1)
    );

    // 문제 생성 헬퍼 메서드
    private Map<String, Object> createQuestion(String question, List<String> options, int correct) {
        Map<String, Object> questionMap = new HashMap<>();
        questionMap.put("question", question);
        questionMap.put("options", options);
        questionMap.put("correct", correct);
        return questionMap;
    }

    @GetMapping("/QuizJun")
    public String showQuizPage(Model model) {
        List<QuizScore> topScores = quizScoreRepository.findTop10ByOrderByScoreDesc();
        model.addAttribute("leaderboard", topScores);
        model.addAttribute("questions", questions);
        model.addAttribute("msg", quizMessages);  // msg라는 이름으로 전달
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