# Capstone Spring Boot Server

안드로이드 앱에서 사용하던 PHP API를 대체하기 위한 Spring Boot + MySQL 서버 스켈레톤입니다.

## 실행 준비

1. `server/src/main/resources/application.yml`에서 MySQL 접속 정보를 실제 환경에 맞게 수정합니다.
2. 루트 디렉터리에서 `./gradlew :server:bootRun`으로 로컬 실행합니다.
   - JDK 17 이상이 필요합니다.
3. 최초 실행 시 `spring.jpa.hibernate.ddl-auto=update` 설정에 의해 스키마가 자동 생성됩니다.

## 제공 REST API (요약)

| 기능 | 메소드 | 경로 | 설명 |
| --- | --- | --- | --- |
| 회원가입 | POST | `/api/auth/register` | `userId`, `password`, `name`, `age` 전달 |
| 로그인 | POST | `/api/auth/login` | 가입된 사용자 인증 |
| 재료 조회 | GET | `/api/ingredients` | 냉장/냉동 재료 전체 조회 |
| 재료 추가 | POST | `/api/ingredients` | 재료 정보 JSON으로 등록 |
| 재료 수정 | PUT | `/api/ingredients/{id}` | 재료 정보 수정 |
| 재료 삭제 | DELETE | `/api/ingredients/{id}` | ID 기준 삭제 |
| 재료 삭제(이름) | DELETE | `/api/ingredients?name=` | 이름 기준 삭제 |
| 장바구니 조회 | GET | `/api/shopping-items` | 장바구니 전체 조회 |
| 장바구니 추가 | POST | `/api/shopping-items` | 품목 등록 |
| 장바구니 수정 | PUT | `/api/shopping-items/{id}` | 품목 수정 |
| 장바구니 삭제 | DELETE | `/api/shopping-items/{id}` | 품목 삭제 |
| 장바구니 → 냉장고 | POST | `/api/shopping-items/move-to-fridge` | 선택 품목을 재료로 이동 |
| 레시피 조회 | GET | `/api/recipes?keyword=` | 키워드(optional) 기반 레시피 검색 |
| 레시피 등록 | POST | `/api/recipes` | 커스텀 레시피 저장 |

요청/응답 JSON 구조는 `server/src/main/java/.../dto` 패키지를 참고하세요.

## 안드로이드 연동 가이드

- 기존 PHP 엔드포인트와 동일한 기능을 제공하되, RESTful JSON 응답으로 통일했습니다.
- `ApiRequest`, `LoginRequest`, `RegisterRequest` 등의 URL을 위 엔드포인트로 교체하고, 응답 필드를 DTO에 맞게 업데이트하면 됩니다.
- 장바구니 → 냉장고 이동은 `/api/shopping-items/move-to-fridge`에 `itemIds` 목록과 기본 보관 정보를 전달하면 됩니다.
- 인증 고도화(예: JWT) 및 외부 레시피 API 연동은 이후 단계에서 추가할 수 있도록 서비스 레이어를 분리했습니다.

## 다음 작업 제안

- AES/BCrypt 등을 사용해 사용자 비밀번호 암호화.
- Validation 메시지 국제화, 공통 응답 포맷 정리.
- Swagger/OpenAPI 문서화(`springdoc-openapi`)
- 배포 환경 분리를 위한 `application-prod.yml` 작성.
- 안드로이드 네트워크 모듈(예: Retrofit)에서 DTO 매핑을 위한 데이터 클래스 정의.
