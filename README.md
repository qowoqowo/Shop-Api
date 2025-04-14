쇼핑몰 REST API 프로젝트

Spring Boot 기반의 쇼핑몰 REST API를 Docker로 컨테이너화하여 구현한 프로젝트입니다. 이 API는 사용자 관리, 상품 조회, 장바구니 등 기본적인 쇼핑몰 기능을 제공합니다. JWT (JSON Web Token)를 사용하여 인증 및 인가 기능을 구현하였고, Docker로 실행 가능하며, MySQL을 데이터베이스로 사용합니다.

 프로젝트 실행 방법

1. Docker 이미지 빌드
프로젝트 루트 디렉터리에서 아래 명령어를 통해 Docker 이미지를 빌드합니다.

```bash
docker build -t shoppingmall-api .
```
2. Docker 컨테이너 실행
이미지 빌드 후, 아래 명령어로 컨테이너를 실행합니다.
```bash
docker run -d -p 8080:8080 --name shoppingmall-api-container shoppingmall-api
```
3. API 테스트
API가 성공적으로 실행되면, Swagger UI를 통해 API를 테스트할 수 있습니다.
```bash
- Swagger UI: http://localhost:8080/swagger-ui/index.html
```
사용 기술
- Backend: Spring Boot
- Database: MySQL
- API 문서화: Swagger
- Authentication: JWT (JSON Web Token)
- Containerization: Docker

주요 기능
1. 회원 관리
- 회원가입: 사용자는 POST /api/auth/signup API를 통해 회원가입을 할 수 있습니다.
- 로그인: 사용자는 POST /api/auth/login API를 통해 로그인하고 JWT 토큰을 발급받습니다.
- 내 정보 조회: 로그인한 사용자는 GET /api/user/me API로 자신의 정보를 조회할 수 있습니다.
- 비밀번호 변경: 사용자는 PUT /api/user/update-password API를 통해 비밀번호를 변경할 수 있습니다.

2. 장바구니 관리
- 장바구니 담기: 사용자는 POST /api/cart/add API를 통해 장바구니에 상품을 추가할 수 있습니다.
- 장바구니 조회: 사용자는 GET /api/cart API로 장바구니의 모든 상품을 조회할 수 있습니다.
- 장바구니 상품 삭제: 사용자는 DELETE /api/cart/{productId} API로 장바구니에서 상품을 삭제할 수 있습니다.
- 장바구니 수량 변경: 사용자는 PATCH /api/cart/update API로 장바구니에 담긴 상품의 수량을 변경할 수 있습니다.

3. 주문 관리
- 주문 생성: 사용자는 POST /api/orders/create API를 통해 주문을 생성할 수 있습니다.
- 내 주문 목록 조회: 사용자는 GET /api/orders API로 자신의 주문 목록을 조회할 수 있습니다.
- 주문 상세 조회: 사용자는 GET /api/orders/{orderId} API로 특정 주문의 상세 내역을 조회할 수 있습니다.
- 전체 주문 조회 (관리자): 관리자는 GET /api/orders/admin API로 모든 주문을 조회할 수 있습니다.
- 주문 상태 변경 (관리자): 관리자는 PATCH /api/orders/{orderId}/status API를 통해 주문 상태를 변경할 수 있습니다.

4. 결제 처리
- 결제 처리: 사용자는 POST /api/orders/payment API를 통해 결제를 처리할 수 있습니다.

JWT 인증
1. 로그인
- 사용자는 로그인 시 JWT 토큰을 발급받으며, 이 토큰을 통해 인증된 사용자만 접근할 수 있는 API를 호출할 수 있습니다.
- 로그인 요청: 사용자 이름과 비밀번호를 제공하여 로그인 요청을 보냅니다.
- JWT 발급: 인증이 성공하면 JWT 토큰을 반환받습니다.
- API 요청 시 JWT 사용: 로그인 후, API 요청 시 JWT 토큰을 Authorization 헤더에 Bearer <token> 형식으로 전달해야 합니다.

2. JWT 생성 및 검증
- JWT 생성: 로그인 후, 서버는 JWT 토큰을 생성하여 클라이언트에 반환합니다. 이 토큰은 비밀번호 대신 사용되어 인증을 처리합니다.
- JWT 검증: 클라이언트는 이후의 모든 요청에 대해 Authorization 헤더에 JWT를 첨부하여 보냅니다. 서버는 이 토큰을 검증하여 유효한 사용자만 접근할 수 있도록 합니다.

환경설정
application-prod.yml
```yml
server:
  port: 8080
  servlet:
    context-path: /
    encoding:
      charset: UTF-8
      enabled: true
      force: true

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://host.docker.internal:3306/shop?serverTimezone=Asia/Seoul
    username: cos
    password: cos1234

  jpa:
    hibernate:
      ddl-auto: validate  
    open-in-view: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true

```


