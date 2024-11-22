package com.kjunh972.QRManager.controller;

import com.kjunh972.QRManager.model.Content;
import com.kjunh972.QRManager.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

// 컨텐츠 관리를 위한 컨트롤러 클래스
@Controller
public class ContentController {

    // ContentService 의존성 주입
    @Autowired
    private ContentService contentService;

    // 메인 페이지 요청 처리
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 컨텐츠 업로드 및 QR코드 생성 처리
    @PostMapping("/upload")
    public String uploadContent(@RequestParam("type") String type,
            @RequestParam(value = "data", required = false) String data,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "latitude", required = false) String latitude,
            @RequestParam(value = "longitude", required = false) String longitude,
            Model model) {
        try {
            String qrCode;
            String url;
            switch (type) {
                // 퀴즈 QR코드 생성
                case "quiz":
                    url = contentService.getServerUrl() + "/QuizJun";
                    qrCode = contentService.generateQRCode(url);
                    break;
                // 주소 QR코드 생성    
                case "address":
                    qrCode = contentService.generateQRCode(address);
                    break;
                // 연락처 QR코드 생성    
                case "vcard":
                    qrCode = contentService.generateVCardQRCode(name, phone, email, address);
                    break;
                // 위치 QR코드 생성    
                case "location":
                    if (latitude == null || longitude == null) {
                        model.addAttribute("error", "위치 정보가 올바르지 않습니다.");
                        return "index";
                    }
                    // 구글 지도 앱으로 바로 연결되는 URL 생성
                    String locationUrl = String.format("geo:%s,%s?q=%s,%s",
                            latitude, longitude, latitude, longitude);
                    qrCode = contentService.generateQRCode(locationUrl);
                    break;
                // 이미지/비디오 QR코드 생성    
                case "image":
                case "video":
                    if (file == null || file.isEmpty()) {
                        model.addAttribute("error", "파일을 선택해주세요.");
                        return "index";
                    }
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename == null || originalFilename.isEmpty()) {
                        model.addAttribute("error", "유효하지 않은 파일명입니다.");
                        return "index";
                    }
                    Content content = contentService.saveContent(type, null, file);
                    url = contentService.getServerUrl() + "/view/" + content.getId();
                    qrCode = contentService.generateQRCode(url);
                    break;
                // 텍스트 QR코드 생성    
                case "text":
                    content = contentService.saveContent(type, data, null);
                    url = contentService.getServerUrl() + "/view/" + content.getId();
                    qrCode = contentService.generateQRCode(url);
                    break;
                default:
                    throw new RuntimeException("지원하지 않는 컨텐츠 타입입니다.");
            }
            model.addAttribute("qrCode", qrCode);
            model.addAttribute("message", "QR 코드가 생성되었습니다.");
        } catch (Exception e) {
            model.addAttribute("error", "오류 발생: " + e.getMessage());
        }
        return "index";
    }

    // ID로 컨텐츠 조회
    @GetMapping("/view/{id}")
    public String viewContent(@PathVariable Long id, Model model) {
        Content content = contentService.getContent(id);
        if (content != null) {
            model.addAttribute("content", content);
        } else {
            model.addAttribute("error", "컨텐츠를 찾을 수 없습니다.");
        }
        return "view";
    }

    // 업로드된 파일 제공
    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get("uploads/" + filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.parseMediaType(contentService.getContentType(filename)))
                        .body(resource);
            } else {
                throw new RuntimeException("파일을 읽을 수 없습니다!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 서버 주소 설정
    @PostMapping("/set-address")
    public String setServerAddress(@RequestParam("address") String address, Model model) {
        contentService.setServerAddress(address);
        model.addAttribute("message", "서버 주소가 설정되었습니다: " + address);
        return "index";
    }
}