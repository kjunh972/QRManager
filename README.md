# QR Manager

QR 코드를 통한 다양한 컨텐츠 관리 시스템

## 프로젝트 소개
QR Manager는 다양한 형태의 컨텐츠를 QR 코드로 손쉽게 관리할 수 있는 웹 기반 시스템입니다. 

사용자는 텍스트, 이미지, 비디오, 주소, 연락처, 미니게임, 현재위치 등 다양한 형태의 컨텐츠를 업로드하고 이에 대한 고유한 QR 코드를 자동으로 생성받을 수 있습니다.

특히 미니게임(퀴즈) 기능을 통해 교육용 컨텐츠 제작이 가능하며 실시간 랭킹 시스템으로 사용자들의 참여를 유도할 수 있습니다.


## 목차
- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [기능 미리보기](#기능-미리보기)
- [시작하기](#시작하기)
- [주요 API](#주요-api)
- [보안 기능](#보안-기능)
- [지원](#지원)

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

![Java](https://img.shields.io/badge/Java-%23ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-%236DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-%236DB33F?style=for-the-badge&logo=spring&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2%20Database-%230072b1?style=for-the-badge&logo=h2&logoColor=white)

### Frontend

![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%2330c58e?style=for-the-badge&logo=thymeleaf&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-%23E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-%231572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-%23F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![SweetAlert2](https://img.shields.io/badge/SweetAlert2-%236f42c1?style=for-the-badge&logo=javascript&logoColor=white)

## 기능 미리보기
1. QR 생성

- 주소 QR 생성
![주소](https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EC%A3%BC%EC%86%8C_QR%EC%83%9D%EC%84%B1.gif)

- 텍스트 QR 생성
![주소](https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%ED%85%8D%EC%8A%A4%ED%8A%B8_QR%EC%83%9D%EC%84%B1.gif)

- 이미지 QR 생성
![주소](https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EC%9D%B4%EB%AF%B8%EC%A7%80_QR%EC%83%9D%EC%84%B1.gif)

- 동영상 QR 생성
![주소](https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EB%B9%84%EB%94%94%EC%98%A4_QR%EC%83%9D%EC%84%B1.gif)

- 연락처 QR 생성
![주소](https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EC%97%B0%EB%9D%BD%EC%B2%98_QR%EC%83%9D%EC%84%B1.gif)

- 미니게임 QR 생성
![주소](https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EB%AF%B8%EB%8B%88%EA%B2%8C%EC%9E%84_QR%EC%83%9D%EC%84%B1.gif)

- 현재 위치 QR 생성
![주소](https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%ED%98%84%EC%9E%AC%EC%9C%84%EC%B9%98_QR%EC%83%9D%EC%84%B1.gif)


2. QR 스캔

| 주소 | 텍스트 | 연락처 |
| :-: | :-: | :-: |
| <img src="https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EC%A3%BC%EC%86%8C_%20%EB%AA%A8%EB%B0%94%EC%9D%BC.gif" width="200" height="420"/> | <img src="https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%ED%85%8D%EC%8A%A4%ED%8A%B8_%20%EB%AA%A8%EB%B0%94%EC%9D%BC.gif" width="200" height="420"/> | <img src="https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EC%97%B0%EB%9D%BD%EC%B2%98_%20%EB%AA%A8%EB%B0%94%EC%9D%BC.gif" width="200" height="420"/> |

| 이미지 | 동영상 |
| :-: | :-: |
| <img src="https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EC%9D%B4%EB%AF%B8%EC%A7%80_%20%EB%AA%A8%EB%B0%94%EC%9D%BC.gif" width="200" height="420"/> | <img src="https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EB%B9%84%EB%94%94%EC%98%A4_%EB%AA%A8%EB%B0%94%EC%9D%BC.gif" width="200" height="420"/> |

| 미니게임 | 현재 위치 |
| :-: | :-: |
| <img src="https://github.com/kjunh972/QRManager/blob/main/QRManager/uploads/%EB%AF%B8%EB%8B%88%EA%B2%8C%EC%9E%84_%EB%AA%A8%EB%B0%94%EC%9D%BC.gif" width="200" height="420"/> | |

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

## 지원

[![Gmail Badge](https://img.shields.io/badge/Gmail-d14836?style=for-the-badge&logo=Gmail&logoColor=white&link=mailto:kjunh972@gmail.com)](mailto:kjunh972@gmail.com)
[![Tistory](https://img.shields.io/badge/Tistory-%23FF5A4A?style=for-the-badge&logo=tistory&logoColor=white)](https://kjunh972.tistory.com)
