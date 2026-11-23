package com.example.demo.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank(message = "제목은 비어 있을 수 없습니다") @Size(max = 150) String title,
        @NotBlank(message = "저자는 비어 있을 수 없습니다") @Size(max = 100) String author,
        @Min(value = 0, message = "가격은 0 이상이어야 합니다") int price
) {
    public Book toEntity() {
        return new Book(title, author, price);
    }
}
