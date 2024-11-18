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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Enumeration;

@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private Environment environment;

    private String serverAddress;

    public void setServerAddress(String address) {
        this.serverAddress = address;
    }

    public Content saveContent(String type, String data, MultipartFile file) throws IOException {
        Content content = new Content();
        content.setType(type);
        content.setCreatedAt(LocalDateTime.now());

        if ("text".equals(type)) {
            content.setData(data);
        } else {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            content.setData(path.toString());
        }

        return contentRepository.save(content);
    }

    public Content getContent(Long id) {
        return contentRepository.findById(id).orElse(null);
    }

    public String generateVCardQRCode(String name, String phone, String email, String address) throws WriterException, IOException {
        // 주소에서 앞뒤 공백을 제거하고, 맨 뒤의 쉼표가 있다면 제거합니다.
        address = address != null ? address.trim() : "";
        if (address.endsWith(",")) {
            address = address.substring(0, address.length() - 1).trim();
        }

        StringBuilder vCard = new StringBuilder();
        vCard.append("BEGIN:VCARD\r\n")
             .append("VERSION:3.0\r\n");
        
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
        
        System.out.println("Generated vCard: " + vCard); // 디버깅용 로그
        return generateQRCode(new String(vCard.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
    }

    public String generateQRCode(String data) throws WriterException, IOException {
        // URL에서 마지막 쉼표 제거
        if (data != null && data.endsWith(",")) {
            data = data.substring(0, data.length() - 1);
        }
        
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(pngData);
    }

    public String getServerUrl() {
        if (serverAddress != null && !serverAddress.isEmpty()) {
            return serverAddress;
        }
        String port = environment.getProperty("server.port");
        String internalIp = getInternalIpAddress();
        return "http://" + internalIp + ":" + port;
    }

    private String getInternalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.isSiteLocalAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "localhost"; // 내부 IP를 찾지 못한 경우 localhost 반환
    }

    public String getContentType(String filename) throws IOException {
        Path path = Paths.get("uploads", filename);
        return Files.probeContentType(path);
    }

    public String generateQuizQRCode() throws WriterException, IOException {
        // 직접 QuizJun 페이지로 이동하는 URL 생성
        String quizUrl = getServerUrl() + "/QuizJun";
        System.out.println("Generated Quiz URL: " + quizUrl); 
        return generateQRCode(quizUrl);
    }
}