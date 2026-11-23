package com.example.demo.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
  private final BookRepository repository;

  public BookService(BookRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  public Page<BookResponse> findAll(Pageable pageable) {
    return repository.findAll(pageable)
        .map(BookResponse::from);
  }

  @Transactional(readOnly = true)
  public Book findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new BookNotFoundException(id));
  }

  @Transactional
  public Book create(BookRequest request) {
    return repository.save(request.toEntity());
  }

  @Transactional
  public Book update(Long id, BookRequest request) {
    Book book = findById(id);
    book.update(request.title(), request.author(), request.price());
    return book;
  }

  @Transactional
  public void delete(Long id) {
    repository.delete(findById(id));
  }
}
