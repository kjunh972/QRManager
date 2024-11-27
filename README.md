# QR Manager

QR 코드를 통한 다양한 컨텐츠 관리 시스템

## 주요 기능

### 1. 다양한 컨텐츠 타입 지원
- 텍스트
- 이미지 
- 비디오
- 주소/URL
- 연락처(vCard)
- 위치 정보
- 미니게임(퀴즈)

### 2. QR 코드 생성
- 각 컨텐츠에 대한 고유 QR 코드 자동 생성
- Base64 인코딩 지원
- 안전한 파일명 처리

### 3. 미니게임(퀴즈) 기능
- 다양한 주제의 퀴즈 제공
- 실시간 점수 계산
- 상위 10위까지 랭킹 시스템
- 모바일 친화적 UI

## 기술 스택

### Backend
- Java 17
- Spring Boot 3.3.4
- Spring Data JPA
- H2 Database

### Frontend
- Thymeleaf
- HTML5/CSS3
- JavaScript
- SweetAlert2

## 시작하기

### 요구사항
- JDK 17 이상
- Maven
- H2 Database

### 설치 및 실행

1. 저장소 클론
```bash
git clone https://github.com/kjunh972/QRManager.git
```

2. 프로젝트 디렉토리로 이동
```bash
cd QRManager
```

3. Maven으로 빌드
```bash
./mvnw clean package
```

4. 애플리케이션 실행
```bash
java -jar target/QRManager-0.0.1-SNAPSHOT.jar
```

5. 브라우저에서 접속
```bash
http://localhost:8056
```

## 주요 API

### 컨텐츠 관리
- `POST /upload` - 새로운 컨텐츠 업로드 및 QR 코드 생성
- `GET /view/{id}` - 컨텐츠 조회
- `GET /uploads/{filename}` - 업로드된 파일 조회

### 퀴즈 게임
- `GET /QuizJun` - 퀴즈 게임 페이지
- `POST /api/quiz/score` - 퀴즈 점수 저장
- `GET /api/quiz/leaderboard` - 랭킹 조회

## 보안 기능
- 파일 업로드 크기 제한 (최대 50MB)
- 안전한 파일명 처리
- MIME 타입 검증
- XSS 방지

## 기여하기
1. Fork the Project
2. Create your Feature Branch
3. Commit your Changes
4. Push to the Branch
5. Open a Pull Request
