package com.example.demo.book;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Book", description = "도서 CRUD API")
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "도서 목록 조회", description = "page, size, sort 파라미터로 페이징/정렬 (예: ?page=0&size=5&sort=price,desc)")
    public Page<BookResponse> list(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "도서 단건 조회", description = "없는 id면 404")
    public BookResponse get(@PathVariable Long id) {
        return BookResponse.from(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "도서 등록", description = "성공 시 201과 Location 헤더 반환")
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        Book saved = service.create(request);
        BookResponse body = BookResponse.from(saved);
        return ResponseEntity.created(URI.create("/api/books/" + saved.getId()))
                .body(body);
    }

    @PutMapping("/{id}")
    @Operation(summary = "도서 수정")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return BookResponse.from(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "도서 삭제", description = "성공 시 204")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
