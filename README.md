#  🚀 Meet U, Career - Backend API Server

**Spring Boot 기반의 커리어 매칭 플랫폼 'Meet U, Career' 백엔드 API 서버입니다.**  

이 프로젝트는 실제 서비스 수준의 커리어 매칭 플랫폼 구현을 목표로 기획 및 개발되었습니다.  

총 6명의 팀원 모두 백엔드는 물론, 프론트엔드까지 함께 개발하며 전반적인 웹 애플리케이션 개발 전반을 경험했습니다.

  

---


## 📌 프로젝트 개요

| 항목    | 설명                                                                    |
| ----- | --------------------------------------------------------------------- |
| 프로젝트명 | Meet U, Career                                                        |
| 개발 기간 | 2025.03 ~ 2025.04                                                     |
| 팀 구성  | 총 6인 (팀 하이파이브)                                                        |
| 개발 방식 | 백엔드 / 프론트엔드 분리 개발 (각 레포지토리 관리)                                        |
| 주요 기능 | 이력서 관리, 채용 공고 조회 및 지원, 기업 회원 관리, 관리자 대시보드 등                           |
| 기술 스택 | Spring Boot, JPA, MySQL, QueryDSL, JWT, Docker, AWS, GitHub Actions 등 |
  


---


## ⚙️ 기술 스택 및 아키텍처

### 🔧 사용 기술

- **Backend Framework**: Spring Boot 3.x

- **ORM**: JPA / Hibernate + QueryDSL

- **Database**: MySQL (Oracle Cloud)

- **Build Tool**: Gradle

- **API 인증/인가**: JWT 기반

- **CI/CD**: GitHub Actions + Docker + AWS EC2

- **문서화**: Swagger

- **IDE**: IntelliJ IDEA (Backend), VS Code (Frontend)

- **협업 도구**: Jira, Confluence, Slack, Figma



| 분류                   | 기술 스택                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
| -------------------- |-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **프론트엔드**            | <img src="https://img.shields.io/badge/React-61DAFB?style=flat&logo=react&logoColor=white"/> <img src="https://img.shields.io/badge/Next.js-000000?style=flat&logo=nextdotjs&logoColor=white"/> <img src="https://img.shields.io/badge/TypeScript-3178C6?style=flat&logo=typescript&logoColor=white"/> <img src="https://img.shields.io/badge/TailwindCSS-06B6D4?style=flat&logo=tailwindcss&logoColor=white"/>                                                                                                                                                                                                                                                                               |
| **백엔드**              | <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat&logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/Java-007396?style=flat&logo=openjdk&logoColor=white"/> <img src="https://img.shields.io/badge/Gradle-02303A?style=flat&logo=gradle&logoColor=white"/> <img src="https://img.shields.io/badge/JPA-59666C?style=flat&logo=hibernate&logoColor=white"/> <img src="https://img.shields.io/badge/QueryDSL-00B4B6?style=flat"/> <img src="https://img.shields.io/badge/JWT-black?style=flat&logo=jsonwebtokens&logoColor=white"/> <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat&logo=springsecurity&logoColor=white"/> |
| **데이터베이스**           | <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white"/>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| **API 문서화 / 테스트 도구** | <img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat&logo=swagger&logoColor=white"/> <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white"/>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| **인프라 / 배포**         | <img src="https://img.shields.io/badge/Oracle%20Cloud-F80000?style=flat&logo=oracle&logoColor=white" /> <img src="https://img.shields.io/badge/GitHub%20Actions-2088FF?style=flat&logo=githubactions&logoColor=white" /> <img src="https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white" /> <img src="https://img.shields.io/badge/AWS%20EC2-FF9900?style=flat&logo=amazonaws&logoColor=white" /> <img src="https://img.shields.io/badge/Nginx-009639?style=flat&logo=nginx&logoColor=white" /> <img src="https://img.shields.io/badge/Ubuntu-E95420?style=flat&logo=ubuntu&logoColor=white" />                                                       |
| **개발 환경**            | <img src="https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=flat&logo=intellijidea&logoColor=white"/> <img src="https://img.shields.io/badge/VS%20Code-007ACC?style=flat&logo=visualstudiocode&logoColor=white"/>                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **협업 도구**            | <img src="https://img.shields.io/badge/Jira-0052CC?style=flat&logo=jira&logoColor=white"/> <img src="https://img.shields.io/badge/Confluence-172B4D?style=flat&logo=confluence&logoColor=white"/> <img src="https://img.shields.io/badge/Slack-4A154B?style=flat&logo=slack&logoColor=white"/> <img src="https://img.shields.io/badge/Figma-F24E1E?style=flat&logo=figma&logoColor=white"/>                                                                                                                                                                                                                                                                                                   |



### 🧩 아키텍처 개요

```
[Frontend (Next.js)] → [Backend (Spring Boot)]
                          ↳ DB: MySQL (Oracle Cloud)
                          ↳ CI/CD: Docker, GitHub Actions
                          ↳ 배포: AWS EC2

```



---


## 👥 팀원


> 모든 팀원이 GitHub를 통해 브랜치 전략(git flow)을 적용하며 협업했습니다.



---


## 💬 기술적 의사결정 사유

| 사용 기술              | 선택 사유 및 기술 설명                                                                                                                          |
| ------------------ | -------------------------------------------------------------------------------------------------------------------------------------- |
| **QueryDSL**       | 다양한 조건으로 공고, 게시글 등을 필터링하는 기능이 많기 때문에 **복잡한 WHERE 조건**과 **동적 쿼리 생성**이 필요한 상황에 적합합니다. IDE의 자동완성과 컴파일 타임 에러 감지도 가능한 **QueryDSL**을 사용했습니다. |
| **Presigned URL**  | **S3 버킷에 직접 업로드**하거나 조회할 수 있도록, 서버에서 생성한 인증된 URL을 클라이언트에 제공하는 방식입니다. 인증, 권한 체크 등은 서버에서 처리하므로 보안도 확보됩니다.                                |
| **RDS (MySQL)**    | **관계형 데이터 구조**에 적합하며, AWS RDS를 사용해 **서버가 중지되더라도 안정적으로 데이터 보존 및 관리**가 가능한 MySQL을 선택했습니다.                                                |
| **GitHub Actions** | 기존 Jenkins, AWS CodePipeline 등과 비교해 **설정이 간편하고**, GitHub 기반의 워크플로우를 활용해 **자동 배포(CI/CD)**를 구현하기 위해 선택했습니다. 비용 효율성도 장점입니다.               |
| **WebSocket**      | HTTP 기반의 일반 요청-응답 방식으로는 실시간 알림/채팅이 어렵기 때문에, **양방향 통신이 가능한 WebSocket**으로 실시간 채팅 기능을 구현했습니다.                                             |
| **JWT**            | 로그인 인증에 있어 세션을 서버에 저장하지 않고, **Access + Refresh 토큰** 구조로 구현하여 보안성과 확장성을 모두 고려했습니다.                                                      |
| **OAuth2**         | 회원가입 절차의 편의성과 사용자 인증의 신뢰도를 높이기 위해 **카카오 소셜로그인(OAuth2)**을 도입했습니다.                                                                       |



---


## 📂 폴더 구조

<details>
  <summary>폴더 구조 확인하기</summary>

```
src/
└── main/
    └── java/
        └── com.highfive.meetu/  # 메인 패키지: 회사명.프로젝트명 형식의 루트 패키지
            ├── domain/  # 도메인 계층: 비즈니스 기능별로 모듈화된 패키지 모음
            │   ├── application/  # 지원서 관련 도메인
            │   │   ├── business/  # 기업 관점의 지원서 비즈니스 로직
            │   │   ├── common/  # 공통 컴포넌트
            │   │   │   └── entity/  # 지원서 관련 엔티티 클래스
            │   │   │       ├── Application  # 지원서 엔티티
            │   │   │       └── InterviewReview  # 면접 후기 엔티티
            │   │   ├── repository/  # 지원서 관련 데이터 접근 계층
            │   │   ├── type/  # 지원서 관련 타입 정의
            │   │   └── personal/  # 개인 사용자 관점의 지원서 관련 기능
            │   │
            │   ├── auth/  # 인증 관련 도메인
            │   │   ├── admin/  # 관리자 인증
            │   │   ├── business/  # 기업 사용자 인증
            │   │   ├── common/  # 공통 인증 컴포넌트
            │   │   └── personal/  # 개인 사용자 인증
            │   │
            │   ├── calendar/  # 캘린더 관련 도메인
            │   │   ├── business/  # 기업용 캘린더 기능
            │   │   ├── common/  # 공통 캘린더 컴포넌트
            │   │   │   └── entity/  # 캘린더 관련 엔티티
            │   │   │       └── CalendarEvent  # 캘린더 이벤트 엔티티
            │   │   ├── repository/  # 캘린더 데이터 접근 계층
            │   │   ├── type/  # 캘린더 관련 타입 정의
            │   │   └── personal/  # 개인 사용자용 캘린더 기능
            │   │
            │   ├── chat/  # 채팅 관련 도메인
            │   │   ├── business/  # 기업용 채팅 기능
            │   │   ├── common/  # 공통 채팅 컴포넌트
            │   │   │   └── entity/  # 채팅 관련 엔티티
            │   │   │       ├── ChatMessage  # 채팅 메시지 엔티티
            │   │   │       └── ChatRoom  # 채팅방 엔티티
            │   │   ├── repository/  # 채팅 데이터 접근 계층
            │   │   ├── type/  # 채팅 관련 타입 정의
            │   │   └── personal/  # 개인 사용자용 채팅 기능
            │   │
            │   ├── community/  # 커뮤니티 관련 도메인
            │   │   ├── admin/  # 커뮤니티 관리 기능
            │   │   ├── common/  # 공통 커뮤니티 컴포넌트
            │   │   │   └── entity/  # 커뮤니티 관련 엔티티
            │   │   │       ├── CommunityComment  # 커뮤니티 댓글 엔티티
            │   │   │       ├── CommunityLike  # 좋아요 엔티티
            │   │   │       ├── CommunityPost  # 게시글 엔티티
            │   │   │       └── CommunityTag  # 태그 엔티티
            │   │   ├── repository/  # 커뮤니티 데이터 접근 계층
            │   │   ├── type/  # 커뮤니티 관련 타입 정의
            │   │   └── personal/  # 개인 사용자용 커뮤니티 기능
            │   │
            │   ├── company/  # 기업 관련 도메인
            │   │   ├── admin/  # 기업 관리 기능
            │   │   ├── business/  # 기업 계정용 비즈니스 로직
            │   │   ├── common/  # 공통 기업 컴포넌트
            │   │   │   └── entity/  # 기업 관련 엔티티
            │   │   │       ├── Company  # 기업 정보 엔티티
            │   │   │       └── CompanyFollow  # 기업 팔로우 엔티티
            │   │   ├── repository/  # 기업 데이터 접근 계층
            │   │   ├── type/  # 기업 관련 타입 정의
            │   │   └── personal/  # 개인 사용자의 기업 관련 기능
            │   │
            │   ├── coverletter/  # 자기소개서 관련 도메인
            │   │   ├── admin/  # 자기소개서 관리 기능
            │   │   ├── business/  # 기업 관점의 자기소개서 관련 기능
            │   │   ├── common/  # 공통 자기소개서 컴포넌트
            │   │   │   └── entity/  # 자기소개서 관련 엔티티
            │   │   │       ├── CoverLetter  # 자기소개서 엔티티
            │   │   │       └── CoverLetterContent  # 자기소개서 내용 엔티티
            │   │   ├── repository/  # 자기소개서 데이터 접근 계층
            │   │   ├── type/  # 자기소개서 관련 타입 정의
            │   │   └── personal/  # 개인 사용자의 자기소개서 관련 기능
            │   │
            │   ├── cs/  # 고객 지원 관련 도메인
            │   │   ├── admin/  # 고객 지원 관리 기능
            │   │   ├── business/  # 기업 관점의 고객 지원 기능
            │   │   ├── common/  # 공통 고객 지원 컴포넌트
            │   │   │   └── entity/  # 고객 지원 관련 엔티티
            │   │   │       └── CustomerSupport  # 고객 지원 요청 엔티티
            │   │   ├── repository/  # 고객 지원 데이터 접근 계층
            │   │   ├── type/  # 고객 지원 관련 타입 정의
            │   │   └── personal/  # 개인 사용자의 고객 지원 관련 기능
            │   │
            │   ├── dashboard/  # 대시보드 관련 도메인
            │   │   ├── admin/  # 관리자용 대시보드
            │   │   ├── business/  # 기업용 대시보드
            │   │   └── personal/  # 개인 사용자용 대시보드
            │   │
            │   └── job/  # 채용 공고 관련 도메인
            │       ├── admin/  # 채용 공고 관리 기능
            │       │   ├── controller/  # 관리자용 컨트롤러
            │       │   ├── dto/  # 관리자용 데이터 전송 객체
            │       │   └── service/  # 관리자용 서비스 로직
            │       ├── business/  # 기업용 채용 공고 관련 기능
            │       ├── common/  # 공통 채용 공고 컴포넌트
            │       │   ├── entity/  # 채용 공고 관련 엔티티
            │       │   │   ├── Bookmark  # 북마크 엔티티
            │       │   │   ├── JobCategory  # 직무 카테고리 엔티티
            │       │   │   ├── JobPosting  # 채용 공고 엔티티
            │       │   │   ├── JobPostingJobCategory  # 채용 공고-카테고리 연결 엔티티
            │       │   │   ├── JobPostingViewLog  # 채용 공고 조회 기록 엔티티
            │       │   │   └── Location  # 근무지 위치 엔티티
            │       │   ├── repository/  # 채용 공고 데이터 접근 계층
            │       │   └── type/  # 채용 공고 관련 타입 정의
            │       │       └── JobPostingTypes  # 채용 공고 타입 정의
            │       ├── personal/  # 개인 사용자의 채용 공고 관련 기능
            │       ├── notification/  # 채용 알림 관련 기능
            │       ├── offer/  # 채용 제안 관련 기능
            │       ├── payment/  # 결제 관련 기능
            │       ├── portal/  # 채용 포털 관련 기능
            │       ├── resume/  # 이력서 관련 기능
            │       ├── system/  # 시스템 관련 기능
            │       ├── user/  # 사용자 관련 기능
            │       └── global/  # 글로벌 공통 컴포넌트
            │           └── common/  # 공통 유틸리티 및 기본 클래스
            │               ├── converter/  # 데이터 변환 관련 클래스
            │               ├── entity/  # 기본 엔티티 클래스
            │               │   └── BaseEntity  # 모든 엔티티의 기본이 되는 클래스
            │               └── response/  # 응답 관련 클래스
            │                   └── ResultData  # API 응답 포맷 클래스
            │
            ├── config/  # 애플리케이션 설정 관련 패키지
            │   ├── QuerydslConfig  # Querydsl 설정 클래스
            │   ├── SecurityConfig  # 보안 설정 클래스
            │   ├── security/  # 추가 보안 설정 관련 패키지
            │   └── util/  # 유틸리티 클래스 모음
            │
            └── MeetUBackendApplication  # 스프링 부트 애플리케이션 진입점
    └── resources/  # 리소스 파일 디렉토리
        ├── application.yml  # 기본 애플리케이션 설정 파일
        ├── application-dev.yml  # 개발 환경 설정 파일
        └── application-secret.yml  # 보안 정보 설정 파일
```
</details>

### MeetU 프로젝트 패키지 구조 특징

#### 1. 도메인 중심 구조 (Domain-Driven)

프로젝트가 `application`, `auth`, `calendar`, `chat`, `community` 등 비즈니스 도메인별로 명확하게 분리되어 있습니다. 이를 통해 관련 기능들을 논리적으로 그룹화하고 응집도를 높였습니다.

#### 2. 계층별 패키지 구성

각 도메인 내부는 다음과 같은 일관된 구조를 가집니다:

##### common 패키지

모든 도메인의 공통 컴포넌트를 포함합니다:

- `entity`: 도메인 엔티티 클래스
- `repository`: 데이터 접근 계층
- `type`: 상수, Enum 등 타입 정의

##### 사용자 역할별 패키지

각 역할에 맞는 기능을 분리하여 관리합니다:

- `personal`: 개인 사용자 관련 기능
    - `dto`: 데이터 전송 객체
    - `service`: 비즈니스 로직
    - `controller`: API 엔드포인트
- `business`: 기업 사용자 관련 기능
    - `dto`: 데이터 전송 객체
    - `service`: 비즈니스 로직
    - `controller`: API 엔드포인트
- `admin`: 관리자 관련 기능
    - `dto`: 데이터 전송 객체
    - `service`: 비즈니스 로직
    - `controller`: API 엔드포인트

#### 3. 계층 구조 (Layered Architecture)

전체적으로 계층형 아키텍처를 따르며, 각 도메인 내에서도 계층이 명확히 구분됩니다:

- 프레젠테이션 계층: `controller`
- 비즈니스 계층: `service`
- 데이터 접근 계층: `repository`
- 도메인 모델: `entity`

#### 4. 공통 기능 분리

여러 도메인에서 공유되는 공통 기능은 `global/common`에 위치하며, 기본 엔티티 클래스(`BaseEntity`)와 응답 포맷(`ResultData`) 등을 포함합니다.

#### 5. 설정 분리

애플리케이션 설정은 `config` 패키지에 분리되어 있으며, 환경별 설정은 resources 디렉토리의 `application-*.yml` 파일에서 관리합니다.

#### 6. 확장성을 고려한 구조

새로운 도메인이나 기능이 추가될 때 기존 구조에 자연스럽게 통합될 수 있도록 설계되었습니다. 각 도메인은 독립적으로 확장 가능합니다.



---

  
## 📑 API 명세

> Swagger를 통해 전체 API 명세를 자동 문서화했습니다.  

`http://[배포서버 주소]/swagger-ui/index.html` 에서 확인 가능합니다.

[Meet U - REST API 설계서](https://docs.google.com/spreadsheets/d/1h937tTBRhGLpmx_gNGGCkRQki3vEbtW5z_Nd3Rswu7s/edit?usp=sharing)



---

  
## 🧪 실행 방법

1. `.env (프로젝트 루트 디렉토리)`, `application-secret.yml (src/main/resources/)`에 다음과 같이 정보를 설정합니다.
```
# 데이터베이스 설정  
DB_HOST=[DB 퍼블릭 IP]  
DB_PORT=3306  
DB_NAME=meetu_db
DB_USER=hf_meetu_user
DB_PASSWORD=[DB 비밀번호]
  
# JPA 설정  
JPA_HIBERNATE_DDL_AUTO=update
```

```yaml
# MySQL DB
spring:  
  datasource:  
    url: jdbc:mysql://[DB 퍼블릭 IP]:3306/meetu_db?serverTimezone=Asia/Seoul  
    username: hf_meetu_user  
    password: [DB 비밀번호]

# OPEN API  
api:  
  saramin:  # 사람인  
    key: [OPEN API KEY]
```


2. 아래 명령어로 실행합니다.

```bash
./gradlew build
java -jar build/libs/meetu-career-backend.jar
```



---


## ✨ 커밋 메시지 형식 (Conventional Commits)

|             |                        |                              |
| ----------- | ---------------------- |------------------------------|
| 타입          | 설명                     | 예시                           |
| `feat:`     | 새로운 기능 추가              | `feat: CAR-12 회원가입 API 추가`   |
| `fix:`      | 버그 수정                  | `fix: CAR-18 포인트 반환 오류 해결`   |
| `refactor:` | 리팩토링 (기능 변경 X)         | `refactor: 중복 유틸 제거`         |
| `docs:`     | 문서 수정                  | `docs: README 업데이트`          |
| `style:`    | 코드 스타일 변경 (공백, 세미콜론 등) | `style: 불필요한 공백 제거`          |
| `test:`     | 테스트 코드 추가              | `test: 회원가입 API 유닛 테스트`      |
| `chore:`     | 빌드, 설정, 패키지 등 초기 환경설정 및 기타 작업              | `chore: 프로젝트 초기 설정 및 패키지 구성` |
| `WIP:`      | 아직 작업 중인 커밋            | `WIP: CAR-21 공고 필터 작업 중 (2025-03-27)`    |


## ✅ WIP 커밋 예시 (날짜 포함)

|                        |                                         |
| ---------------------- | --------------------------------------- |
| 형식                     | 예시                                      |
| WIP: {이슈키} {작업내용} {날짜} | WIP: CAR-71 채용 공고 필터링 작업 중 (2025-03-27) |



---

  
## 🚀 배포 방식

- GitHub Actions를 통해 `main` 브랜치에 푸시 시 자동으로 Docker 이미지 빌드 및 EC2 배포가 이루어집니다.

- Docker Hub를 통해 이미지 관리

  

---


## 🤝 협업 툴 및 관리 방식

- GitHub Projects: 칸반 보드로 이슈 및 태스크 관리

- Figma: 화면 설계 (프론트엔드)

- Notion: 회의록, 기획서, API 명세서 공유

  

---

  
## 📎 기타

- [meet-u-career-frontend 레포지토리](https://github.com/dpdlcl01/meet-u-career-frontend)

