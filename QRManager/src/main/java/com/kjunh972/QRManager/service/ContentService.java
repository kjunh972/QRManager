package com.kjunh972.QRManager.service;

import com.kjunh972.QRManager.model.Content;
import com.kjunh972.QRManager.repository.ContentRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private Environment environment;

    // 파일 업로드 최대 크기 설정
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    // 서버 주소 저장 변수
    private String serverAddress;

    // 서버 주소 설정 메서드
    public void setServerAddress(String address) {
        this.serverAddress = address;
    }

    // 안전한 파일명 생성 메서드
    private String sanitizeFileName(String originalFilename) {
        try {
            // 원본 파일명이 null이거나 비어있는 경우 처리
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                return "file_" + System.currentTimeMillis() + ".tmp";
            }

            // 파일 확장자 분리
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex == -1) {
                // 확장자가 없는 경우
                return originalFilename;
            }

            String nameWithoutExt = originalFilename.substring(0, lastDotIndex);
            String extension = originalFilename.substring(lastDotIndex).toLowerCase();

            // 파일명이 비어있거나 언더스코어로만 이루어진 경우 처리
            if (nameWithoutExt.trim().isEmpty() || nameWithoutExt.matches("^_+$")) {
                nameWithoutExt = "file_" + System.currentTimeMillis();
            }

            // Windows에서 사용할 수 없는 특수문자만 제거
            nameWithoutExt = nameWithoutExt.replaceAll("[\\\\/:*?\"<>|]", "");

            return nameWithoutExt + extension;
        } catch (Exception e) {
            // 예외 발생 시 타임스탬프를 이용한 기본 파일명 생성
            return "file_" + System.currentTimeMillis() +
                    (originalFilename != null && originalFilename.contains(".")
                            ? originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase()
                            : ".tmp");
        }
    }

    // 파일명 중복 처리 메서드
    private String getUniqueFileName(String originalFilename, Path uploadPath) throws IOException {
        String fileName = sanitizeFileName(originalFilename);

        if (!Files.exists(uploadPath.resolve(fileName))) {
            return fileName;
        }

        int lastDotIndex = fileName.lastIndexOf(".");
        String nameWithoutExt = lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : fileName;
        String extension = lastDotIndex > 0 ? fileName.substring(lastDotIndex) : "";

        int counter = 1;
        String newFileName;
        do {
            newFileName = nameWithoutExt + "_" + counter + extension;
            counter++;
        } while (Files.exists(uploadPath.resolve(newFileName)));

        return newFileName;
    }

    // 컨텐츠 저장 메서드
    public Content saveContent(String type, String data, MultipartFile file) throws IOException {
        Content content = new Content();
        content.setType(type);
        content.setCreatedAt(LocalDateTime.now().withNano(0)); // 나노초를 0으로 설정하여 깔끔한 시간 포맷 사용

        if (file != null && !file.isEmpty()) {
            // 파일 크기 체크
            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("파일 크기는 50MB를 초과할 수 없습니다.");
            }

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalArgumentException("올바르지 않은 파일명입니다.");
            }

            // 확장자 추출 및 검증
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            if (type.equals("image") && !isValidImageExtension(extension)) {
                throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다.");
            } else if (type.equals("video") && !isValidVideoExtension(extension)) {
                throw new IllegalArgumentException("지원하지 않는 비디오 형식입니다.");
            }

            // 업로드 디렉토리 생성
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // getUniqueFileName 메서드 사용
            String fileName = getUniqueFileName(originalFilename, uploadPath);

            // 파일 저장
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            content.setData(fileName);
        } else {
            content.setData(data);
        }

        return contentRepository.save(content);
    }

    // 이미지 확장자 검증
    private boolean isValidImageExtension(String extension) {
        return Arrays.asList(".jpg", ".jpeg", ".png", ".gif").contains(extension);
    }

    // 비디오 확장자 검증
    private boolean isValidVideoExtension(String extension) {
        return Arrays.asList(".mp4", ".avi", ".mov", ".wmv").contains(extension);
    }

    // ID로 컨텐츠 조회
    public Content getContent(Long id) {
        return contentRepository.findById(id).orElse(null);
    }

    // vCard QR 코드 생성
    public String generateVCardQRCode(String name, String phone, String email, String address)
            throws WriterException, IOException {
        // 주소 필수 체크
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("주소를 입력해주세요.");
        }

        // 주소 데이터 정제
        address = address.trim();
        if (address.startsWith(",")) {
            address = address.substring(1).trim();
        }
        if (address.endsWith(",")) {
            address = address.substring(0, address.length() - 1).trim();
        }

        // vCard 데이터 생성
        StringBuilder vCard = new StringBuilder();
        vCard.append("BEGIN:VCARD\r\n")
                .append("VERSION:3.0\r\n");

        // 선택적 필드 추가
        if (name != null && !name.isEmpty()) {
            vCard.append("FN;CHARSET=UTF-8:").append(name).append("\r\n");
        }
        if (phone != null && !phone.isEmpty()) {
            vCard.append("TEL:").append(phone).append("\r\n");
        }
        if (email != null && !email.isEmpty()) {
            vCard.append("EMAIL:").append(email).append("\r\n");
        }
        if (!address.isEmpty()) {
            vCard.append("ADR;CHARSET=UTF-8:;;").append(address).append("\r\n");
        }

        vCard.append("END:VCARD");

        System.out.println("Generated vCard: " + vCard);
        return generateQRCode(
                new String(vCard.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
    }

    // QR 코드 생성 메서드
    public String generateQRCode(String data) throws WriterException, IOException {
        // 데이터 정제
        if (data != null && data.endsWith(",")) {
            data = data.substring(0, data.length() - 1);
        }

        // QR 코드 생성
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);

        // PNG 이미지로 변환
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(pngData);
    }

    // 서버 URL 조회
    public String getServerUrl() {
        if (serverAddress != null && !serverAddress.isEmpty()) {
            return serverAddress;
        }
        String port = environment.getProperty("server.port");
        String internalIp = getInternalIpAddress();
        return "http://" + internalIp + ":" + port;
    }

    // 내부 IP 주소 조회 (IPv4 또는 WiFi)
    private String getInternalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // 루프백이 아니고 활성화된 인터페이스만 확인
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    // IPv4 주소이고 내부 네트워크 주소인 경우
                    if (addr instanceof Inet4Address && addr.isSiteLocalAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "localhost";
    }

    // 컨텐츠 타입 조회
    public String getContentType(String filename) throws IOException {
        Path path = Paths.get("uploads", filename);
        return Files.probeContentType(path);
    }

    // 퀴즈 QR 코드 생성
    public String generateQuizQRCode() throws WriterException, IOException {
        // 퀴즈 페이지 URL 생성
        String quizUrl = getServerUrl() + "/QuizJun";
        System.out.println("Generated Quiz URL: " + quizUrl);
        return generateQRCode(quizUrl);
    }
}