package com.example.demo.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank @Size(max = 150) String title,
        @NotBlank @Size(max = 100) String author,
        @PositiveOrZero int price
) {
    public Book toEntity() {
        return new Book(title, author, price);
    }
}
