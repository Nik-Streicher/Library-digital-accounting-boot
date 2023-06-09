package cz.streicher.Project1Boot.controllers;


import cz.streicher.Project1Boot.models.Book;
import cz.streicher.Project1Boot.models.User;
import cz.streicher.Project1Boot.services.BookService;
import cz.streicher.Project1Boot.services.UserService;
import cz.streicher.Project1Boot.util.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final UserService userService;

    private final BookValidator bookValidator;

    @Autowired
    public BookController(BookService bookService, UserService userService, BookValidator bookValidator) {
        this.bookService = bookService;
        this.userService = userService;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sorting) {


        boolean parameters = Objects.isNull(page) && Objects.isNull(booksPerPage);
        List<Book> books;

        if (sorting)
            books = parameters ? bookService.index(true) : bookService.index(page, booksPerPage, true);
        else
            books = parameters ? bookService.index() : bookService.index(page, booksPerPage);


        model.addAttribute("books", books);
        return "/books/index";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") int id, Model model, @ModelAttribute("person") User user) {

        model.addAttribute("book", bookService.get(id));
        model.addAttribute("loan", bookService.getLoan(id));
        model.addAttribute("users", userService.index());
        return "/books/get";
    }

    @RequestMapping("/new")
    public String add(Model model) {
        model.addAttribute("emptyBook", new Book());
        return "/books/create";
    }

    @PostMapping()
    public String create(@ModelAttribute("emptyBook") @Valid Book book, BindingResult bindingResult) {

        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors())
            return "/books/create";
        bookService.add(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.get(id));
        return "/books/edit";
    }


    @PatchMapping("/{id}")
    public String change(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id) {

        // bookValidator.validate(book, bindingResult); Todo

        if (bindingResult.hasErrors())
            return "books/edit";
        bookService.edit(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/release")
    public String release(@PathVariable int id) {
        bookService.deleteUser(id);
        return String.format("redirect:/books/%d", id);
    }

    @PatchMapping("{id}/appoint")
    public String appoint(@PathVariable("id") int bookId, @ModelAttribute("user") User user) {
        bookService.appoint(user.getUserId(), bookId);
        return String.format("redirect:/books/%d", bookId);
    }

    @GetMapping("/search")
    public String search() {
        return "/books/search";
    }

    @PostMapping("/search")
    public String getBooks(@RequestParam("query") String query, Model model) {
        model.addAttribute("books", bookService.search(query));
        return "/books/search";
    }

}
