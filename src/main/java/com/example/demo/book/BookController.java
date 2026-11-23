package com.example.demo.book;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    // GET /api/books?page=0&size=10&sort=title,asc 요청을 처리한다
    @GetMapping
    // 쿼리 파라미터가 없을 때 기본값(10개씩, id 오름차순)으로 Pageable을 만들어 주입받는다
    public Page<BookResponse> list(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        // 서비스가 반환한 Page<BookResponse>를 그대로 응답하면 content와 page(size·number·totalElements·totalPages)가 JSON으로 직렬화된다
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public BookResponse get(@PathVariable Long id) {
        return BookResponse.from(service.findById(id));
    }

    // POST /api/books 요청을 처리한다
    @PostMapping
    // @RequestBody로 JSON을 BookRequest DTO로 변환하고, @Valid로 제약조건(@NotBlank 등)을 검사해 실패하면 400을 반환한다
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        // 검증을 통과한 DTO를 서비스에 넘겨 엔티티로 변환·저장하고, DB가 생성한 id가 채워진 엔티티를 받는다
        Book saved = service.create(request);
        // 엔티티를 외부에 노출하지 않도록 응답용 DTO로 변환한다
        BookResponse body = BookResponse.from(saved);
        // ResponseEntity.created()로 상태코드 201 Created와 새 리소스 위치를 알려주는 Location 헤더를 함께 설정한다
        return ResponseEntity.created(URI.create("/api/books/" + saved.getId()))
                // 응답 본문에 생성된 도서 DTO를 담아 반환한다
                .body(body);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return BookResponse.from(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
