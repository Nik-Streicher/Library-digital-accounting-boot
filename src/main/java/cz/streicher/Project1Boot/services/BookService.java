package cz.streicher.Project1Boot.services;

import cz.streicher.Project1Boot.models.Book;
import cz.streicher.Project1Boot.models.User;
import cz.streicher.Project1Boot.repositories.BookRepository;
import cz.streicher.Project1Boot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;


    @Autowired
    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<Book> index() {
        return bookRepository.findAll();
    }

    public List<Book> index(boolean flag) {
        return bookRepository.findAll(Sort.by("releaseYear"));
    }

    public List<Book> index(int page, int booksPerPage) {
        return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> index(int page, int booksPerPage, boolean flag) {
        return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("releaseYear"))).getContent();
    }


    public Book get(int bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    @Transactional
    public void add(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void edit(int bookId, Book updatedBook) {
        updatedBook.setBookId(bookId);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int bookId) {
        bookRepository.deleteById(bookId);
    }


    public User getLoan(int bookId) {
        return bookRepository.findById(bookId).map(Book::getUser).orElse(null);
    }

    @Transactional
    public void deleteUser(int bookId) {
        Book book = get(bookId);
        book.setUser(null);
        book.setBorrowingDate(null);
        bookRepository.save(book);
    }

    @Transactional
    public void appoint(int userId, int bookId) {
        Book book = get(bookId);
        book.setUser(userRepository.findById(userId).orElse(null));
        book.setBorrowingDate(new Date());
        bookRepository.save(book);
    }

    public Optional<Book> getBook(String title) {
        return bookRepository.findByTitle(title);
    }

    public List<Book> search(String query) {
        return query.equals("") ? null : bookRepository.findAllByTitleStartingWith(query);
    }


    public boolean checkExpires(int bookId) {
        Book book = get(bookId);
        Date date = new Date();
        long diff = date.getTime() - book.getBorrowingDate().getTime();
        return diff >= 864000000;
    }


}
