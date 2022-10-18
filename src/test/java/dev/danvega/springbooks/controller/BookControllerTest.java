package dev.danvega.springbooks.controller;

import dev.danvega.springbooks.model.Author;
import dev.danvega.springbooks.model.Book;
import dev.danvega.springbooks.model.Rating;
import dev.danvega.springbooks.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@GraphQlTest(BookController.class)
class BookControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private BookRepository bookRepository;

    private final List<Book> books = new ArrayList<>();

    @Test
    public void contextLoads() {
        assertNotNull(graphQlTester);
        assertNotNull(bookRepository);
    }

    @BeforeEach
    void setUp() {
        var josh = new Author(1,"Josh","Long");
        var mark = new Author(2,"Mark","Heckler");
        var greg = new Author(3,"Greg","Turnquist");
        books.add(new Book(1,"Reactive Spring", 484, Rating.FIVE_STARS, josh));
        books.add(new Book(2,"Spring Boot Up & Running", 328, Rating.FIVE_STARS, mark));
        books.add(new Book(3,"Hacking with Spring Boot 2.3", 392, Rating.FIVE_STARS, greg));
    }

    @Test
    void testFindAllBooksQueryReturnsAllBooks() {
        String document = """
            query {
                allBooks {
                    id
                    title
                }
            }        
        """;

        when(bookRepository.findAll()).thenReturn(books);

        graphQlTester.document(document)
                .execute()
                .path("allBooks")
                .entityList(Book.class)
                .hasSize(3);
    }

}