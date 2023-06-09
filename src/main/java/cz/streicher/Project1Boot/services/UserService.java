package cz.streicher.Project1Boot.services;


import cz.streicher.Project1Boot.models.Book;
import cz.streicher.Project1Boot.models.User;
import cz.streicher.Project1Boot.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BookService bookService;

    @Autowired
    public UserService(UserRepository userRepository, BookService bookService) {
        this.userRepository = userRepository;
        this.bookService = bookService;
    }

    public List<User> index() {
        return userRepository.findAll();
    }

    public User get(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public void add(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void edit(int userId, User updatedUser) {

        updatedUser.setUserId(userId);
        userRepository.save(updatedUser);
    }


    @Transactional
    public void delete(int userId) {
        userRepository.deleteById(userId);
    }

    public List<Book> getUserBooks(int userId) {

        List<Book> books = userRepository.findById(userId).map(User::getBooks).orElse(null);

        assert books != null;
        for(Book book : books)
            book.setExpired(bookService.checkExpires(book.getBookId()));

        return books;
    }

    public Optional<User> getUser(String fullName) {
        return userRepository.findByFullName(fullName);
    }
}
