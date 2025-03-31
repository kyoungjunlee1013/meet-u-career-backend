# 🪐 meet-u-career-backend

**Spring Boot 기반의 커리어 매칭 플랫폼 'Meet U! Career' 백엔드 API 서버입니다.**  

이 프로젝트는 실제 서비스 수준의 커리어 매칭 플랫폼 구현을 목표로 기획 및 개발되었습니다.  

총 6명의 팀원 모두 백엔드는 물론, 프론트엔드까지 함께 개발하며 전반적인 웹 애플리케이션 개발 전반을 경험했습니다.

  

---


## 📌 프로젝트 개요

| 항목    | 설명                                                                    |
| ----- |-----------------------------------------------------------------------|
| 프로젝트명 | Meet U! Career                                                        |
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





---

  
## 📑 API 명세

> Swagger를 통해 전체 API 명세를 자동 문서화했습니다.  

`http://[배포서버 주소]/swagger-ui/index.html` 에서 확인 가능합니다.

[Meet U - REST API 설계서](https://docs.google.com/spreadsheets/d/1h937tTBRhGLpmx_gNGGCkRQki3vEbtW5z_Nd3Rswu7s/edit?usp=sharing)



---

  
## 🧪 실행 방법

1. `.env` 또는 `application.yml`에 다음 정보를 설정합니다.

```yaml
spring:
  datasource:
    url: jdbc:mysql://[DB_URL]:3306/meetu_db
    username: hf_meetu_user
    password: [비밀번호]
  jpa:
    hibernate:
      ddl-auto: update
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

