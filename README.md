# 🚀 TechUp Challenger Hub

---

## 1. 프로젝트 개요

**TechUp Challenger Hub**는  
부트캠프, 스터디, 학습 과정에서 발생하는 **분산된 커뮤니케이션 문제를 해결하기 위한 통합 커뮤니티 플랫폼**입니다.

과정별 커뮤니티, 게시판, 실시간 채팅을 하나의 서비스로 제공하여  
학습자 간 정보 공유와 협업 효율을 높이는 것을 목표로 합니다.

---

## 2. 프로젝트 소개

부트캠프 및 학습 과정에서는 다음과 같은 문제가 반복적으로 발생합니다.

- 학습 정보가 여러 툴(Slack, Notion, Discord 등)에 흩어짐
- 게시글, 댓글, 실시간 대화가 하나의 흐름으로 이어지지 않음
- 개인이 참여한 활동을 체계적으로 관리하기 어려움

**TechUp Challenger Hub**는  
👉 *과정별 커뮤니티 + 게시판 + 실시간 채팅*을 결합하여  
이러한 문제를 해결하는 **학습 협업 플랫폼**입니다.

---

## 3. 프로젝트 목표

- 과정·주제별 커뮤니티 분리로 정보 탐색 효율 향상
- 게시글 + 댓글 + 좋아요 기반 비동기 소통 지원
- WebSocket 기반 실시간 채팅으로 즉각적인 의견 교환
- Redis Pub/Sub을 활용한 **확장 가능한 실시간 채팅 구조 설계**
- 개인 활동 이력 관리 기능 제공

---

## 4. 주요 기능

### 🔐 사용자 인증
- 회원가입 / 로그인 / 로그아웃
- JWT 기반 인증 및 인가
- HttpOnly 쿠키 사용
- 이메일 인증

### 📝 게시판
- 커뮤니티별 게시글 CRUD
- 게시글 이미지 업로드 (AWS S3)
- 게시글 검색 (제목 / 작성자)
- 정렬 기능
  - 최신순
  - 인기순(좋아요 기준)

### ❤️ 댓글 & 좋아요
- 댓글 작성 / 삭제
- 게시글 좋아요 / 좋아요 취소
- 좋아요 수, 댓글 수 집계

### 👤 마이페이지
- 개인정보 수정
- 내가 작성한 게시글 목록 조회
- 내가 참여중인 과정별 채팅방 조회

### 💬 실시간 채팅
- WebSocket(STOMP) 기반 양방향 채팅
- 채팅 메시지 DB 영속화
- Redis Pub/Sub을 이용한 **멀티 서버 메시지 동기화**
- 참여 중인 채팅방 조회

---

## 5. 팀원 소개

| 이름 | 역할 |
|------|------|
| 김민기 | Backend |
| 최우수 | AWS Infra |
| 황시연 | Frontend / Backend |
| 최태웅 | Frontend |

---

## 6. 기술 스택

### Frontend
- Next.js
- TypeScript
- Tailwind CSS
- STOMP.js / SockJS

### Backend
- Spring Boot 3
- Spring Security
- JWT
- Spring Data JPA
- Spring WebSocket (STOMP)

### Database & Cache
- MySQL (AWS RDS)
- Redis (Pub/Sub)

### Infra
- AWS EC2
- AWS S3
- AWS RDS
- Nginx
- Docker (Redis)

### Collaboration
- GitHub
- Notion

---

## 7. 문서 자료

- 📄 **프로젝트 계획서**  
  👉 [프로젝트 계획서 바로가기](https://file.notion.so/f/f/00e9e3e3-35cc-815b-a12e-0003086879df/6e5f67c4-bb68-4166-9eab-35fd1b87d13a/Spring_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8_%EA%B3%84%ED%9A%8D%EC%84%9C.pdf?table=block&id=2bd9e3e3-35cc-80e3-b729-d0eb39a235f4&spaceId=00e9e3e3-35cc-815b-a12e-0003086879df&expirationTimestamp=1767780000000&signature=6sngT-uTNCkgEQ-MBdOQv9vaVSQumRYSrwLHDGSKjAM&downloadName=%5BSpring%5D+%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8+%EA%B3%84%ED%9A%8D%EC%84%9C.pdf)

- 📄 **중간 발표 자료**  
  👉 [중간 발표 자료 바로가기](https://file.notion.so/f/f/00e9e3e3-35cc-815b-a12e-0003086879df/748bfb39-4eff-44d1-80e0-0f7497015ad0/Spring-_-%EC%A4%91%EA%B0%84%EB%B0%9C%ED%91%9C.pdf?table=block&id=2bd9e3e3-35cc-80e3-b729-d0eb39a235f4&spaceId=00e9e3e3-35cc-815b-a12e-0003086879df&expirationTimestamp=1767780000000&signature=nvN9WcpS8M2nZ2hOFqYJNOVmxZEhPCNOg43Y3gef6-Q&downloadName=Spring-_-%EC%A4%91%EA%B0%84%EB%B0%9C%ED%91%9C.pdf)

- 📄 **최종 발표 자료**  
  👉 [최종 발표 자료 바로가기](https://file.notion.so/f/f/00e9e3e3-35cc-815b-a12e-0003086879df/fa33fcf4-321f-496e-a12a-16823c8de38d/Spring-_-%EC%B5%9C%EC%A2%85%EB%B0%9C%ED%91%9C.pdf?table=block&id=2bd9e3e3-35cc-80e3-b729-d0eb39a235f4&spaceId=00e9e3e3-35cc-815b-a12e-0003086879df&expirationTimestamp=1767787200000&signature=J84E1MKOmnrMTQYVF_MKm-EmEKMi-ZicVh9Bm6XaHsE&downloadName=Spring-_-%EC%B5%9C%EC%A2%85%EB%B0%9C%ED%91%9C.pdf)

- 📊 **ERD**  
<img width="898" height="747" alt="techup_erd" src="https://github.com/user-attachments/assets/51c60a0a-6870-4425-b690-27f3b4a79cd3" />

- 🏗 **시스템 아키텍처 다이어그램**  
<img width="790" height="655" alt="인프라(이메일인증 전)" src="https://github.com/user-attachments/assets/78853426-3309-4c07-91fd-568fe9c000c1" />


- 📼 **시연 영상**  
  👉 [시연 영상_1 바로가기](https://www.youtube.com/watch?v=GC9amu1CNZg) <br>
  👉 [시연 영상_2 바로가기](https://www.youtube.com/watch?v=AZ_Y1cvZjeM)
---

## 8. 실행 가이드

### ✅ 실행 가이드

```bash
#  ✅ Backend 실행
# 프로젝트 루트
./gradlew bootRun
# Redis Pub/Sub 포함 실행
-Dspring.profiles.active=local,redis

#  ✅ Frontend 실행
npm install
npm run start

# ✅ Redis 실행(Docker)
docker run -d -p 6379:6379 redis
