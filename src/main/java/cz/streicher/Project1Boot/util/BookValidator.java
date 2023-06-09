package cz.streicher.Project1Boot.util;

import cz.streicher.Project1Boot.models.Book;
import cz.streicher.Project1Boot.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

    private final BookService bookService;

    @Autowired
    public BookValidator(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        if (bookService.getBook(book.getTitle()).isPresent())
            errors.rejectValue("title", "", "Title already exists");
    }
}
