**STEP 2. Source Code Exploration & Analysis**

**① Source Code Analysis — 질문 8개**README에 자신의 말로 각 2~4문장 작성하고 관련 **소스 파일과 메서드명**을 명시합니다.

**Project Structure & JPA**

1. BookController, BookService, BookRepository의 역할과 요청 처리 흐름을 설명하세요.
   사용자가 요청을 보내고, 그 요청을 http 로 controller가 받고, 요청을 해석한후에 bookservice에게 그 요청에 필요한 비즈니스 로직을 가지고잇는 코드에게 주고, service가 repository에게 db에게 접근할 수 있도록 repository에게 전송한다. repository는 jpa 로 db에 접근해서 테이블을 생성하고, 데이터 crud를 진행한다.
2. JpaRepository<Book, Long>만 선언해도 findAll(), save()를 사용할 수 있는 이유를 설명하세요.
   repository는 jpa를 통해 db에 접근해서 데이터를 조작하는데, jpa에 그런 데이터들을 직접 접근가능하고 그런 접근에 필요한 명령어들을 가지고있기 때문입니다.
   **DTO & HTTP Response**

 3. 기존 API가 Book Entity를 직접 요청·응답에 사용할 때 발생할 수 있는 문제를 설명하세요.  
DB구조와 api 구조가 서로 직접적으로 연결되어 사용자에게 보여지지 않아야하는 부분까지 접근가능하게 해서 보안적인 문제 뿐만 아니라, DB 테이블 구조나 바뀐다면 api 응답에도 영향을 준다.  
 4. Request DTO와 Response DTO를 분리하는 이유와 ResponseEntity가 필요한 상황을 설명하세요.
request Dto 는 서버가 받는 요청 형식을 말하고, response dto는 서버가 클라이언트에게 보내는 응답 형식을 말하기 때문에 서로 사용처가 다르기 때문에 분리하고, responseEntity는 controller가 유저한테 http 응답을 할떄, 상태코드까지 보내고싶을때 사용한다.
**Validation & Exception Handling**

 5. @Valid, @NotBlank, @Min은 각각 어느 시점에 어떤 동작을 수행하나요?  
dto 요청을 받았을때, 유효성 검사를 진행할때 사용하고, NotBlank는 문자열의 공백을 허용하지 않을때 사용하고, Min은 비교햇을대 최소값일 경우에 유효성 검사를 실패하게 한다.
 6. @RestControllerAdvice와 @ExceptionHandler를 사용하면 예외 처리 흐름이 어떻게 바뀌나요?
예외 상황을 한곳에서 관리가능해진다.

**Pagination & OpenAPI**

7. Pageable과 Page<T>는 각각 어떤 역할을 하며 sort=price,desc는 어떻게 처리되나요?
Pageable은 page,size,sort 요청을 받는 객체, Page<T>는 데이터와 페이징 정보를 돌려주는 결과 객체이고, sort=price,desc는 Sort.by(DESC, "price")로 바뀌어서, ORDER BY price DESC로 실행된다.
8. /v3/api-docs와 /swagger-ui.html의 역할을 구분해서 설명하세요.
/v3/api-docs는 BookController의 /api/books CRUD 엔드포인트를 분석해 OpenAPI 명세 JSON으로 반환하는 데이터 엔드포인트이고, /swagger-ui.html은 그 JSON을 읽어 사용자가 브라우저에서 API 목록을 보고 직접 요청을 보내 테스트할 수 있게 렌더링하는 UI 페이지이다

**② Line-by-Line Code Comments — 핵심 메서드 3개**개선된 Java 소스에 직접 줄 단위 설명 주석을 작성하세요.

1. BookController.create() — @Valid, DTO, ResponseEntity, 201 응답
2. BookService.findById() — Repository 조회, Optional, 404 예외 발생
3. BookController.list() 또는 BookService.findAll() — Pageable, Page, DTO 변환

- 주요 실행 코드의 **목적과 데이터 흐름**을 설명합니다. import, 중괄호 등 단순 문법은 제외 가능합니다.
- 기존 CRUD는 정상 동작해야 합니다.
- **README 분석 답변 및 Java 코드 설명 주석에는 생성형 AI를 사용하지 않습니다.**

---

**STEP 3. DTO & HTTP Response Refactoring — 변경 전/후 API 비교**

변경 전에는 Controller가 Book 엔티티를 그대로 받고 그대로 돌려줬다. 변경 후에는 요청은 BookRequest, 응답은 BookResponse로 받고 보낸다.

| API | 변경 전 | 변경 후 
| POST /api/books | 200, Book 엔티티 반환 | 201 + Location 헤더, BookResponse 반환
| GET /api/books | 200 | 200, Page&lt;BookResponse&gt; 
| GET /api/books/{id} | 200, Book 엔티티 반환 | 200, BookResponse 반환 
| PUT /api/books/{id} | 200, Book 엔티티 반환 | 200, BookResponse 반환 
| DELETE /api/books/{id} | 200 | 204 No Content 


