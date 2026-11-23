package com.example.demo.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class BookService {
  private final BookRepository repository;

  public BookService(BookRepository repository) {
    this.repository = repository;
  }

  // 읽기 전용 트랜잭션: 변경 감지(dirty checking)를 생략해 조회 성능을 높인다
  @Transactional(readOnly = true)
  // Pageable(page·size·sort)을 받아 해당 페이지의 DTO 목록과 페이지 메타정보를 담은 Page를 반환한다
  public Page<BookResponse> findAll(Pageable pageable) {
    // repository.findAll(pageable)이 LIMIT/OFFSET·ORDER BY 쿼리와 전체 개수 COUNT 쿼리를 실행해 Page<Book>을 만든다
    return repository.findAll(pageable)
        // Page.map()으로 각 Book 엔티티를 BookResponse DTO로 변환한다 (totalElements 등 메타정보는 그대로 유지)
        .map(BookResponse::from);
  }

  // 읽기 전용 트랜잭션 안에서 단건을 조회한다
  @Transactional(readOnly = true)
  // id로 엔티티를 찾아 반환하고, 없으면 404 예외를 던진다
  public Book findById(Long id) {
    // JpaRepository.findById()는 null 대신 결과가 없을 수도 있음을 나타내는 Optional<Book>을 반환한다
    return repository.findById(id)
        // Optional이 비어 있으면 404 NOT_FOUND 상태와 메시지를 담은 ResponseStatusException을 던진다
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found: id=" + id));
  }

  @Transactional
  public Book create(BookRequest request) {
    return repository.save(request.toEntity());
  }

  @Transactional
  public Book update(Long id, BookRequest request) {
    Book book = findById(id);
    book.update(request.title(), request.author(), request.price());
    return book; // 트랜잭션 커밋 시 변경 감지로 UPDATE 실행
  }

  @Transactional
  public void delete(Long id) {
    repository.delete(findById(id));
  }
}
