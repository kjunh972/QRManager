package com.kjunh972.QRManager.repository;

import com.kjunh972.QRManager.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    // 타입별 컨텐츠 조회
    List<Content> findByType(String type);
    
    // 특정 기간 내 생성된 컨텐츠 조회
    List<Content> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // 타입과 생성일로 컨텐츠 조회
    List<Content> findByTypeAndCreatedAtBetween(String type, LocalDateTime start, LocalDateTime end);
    
    // 데이터로 컨텐츠 검색
    @Query("SELECT c FROM Content c WHERE c.data LIKE %:keyword%")
    List<Content> searchByData(@Param("keyword") String keyword);
    
    // 최근 생성된 컨텐츠 조회
    List<Content> findTop10ByOrderByCreatedAtDesc();
    
    // 타입별 컨텐츠 수 카운트
    Long countByType(String type);
}