package cz.streicher.Project1Boot.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;


@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @NotEmpty(message = "Name should be not empty")
    @Pattern(regexp = "^[\\p{Lu}\\p{Lt}][\\p{L}\\p{M}\\s'-]+ [\\p{Lu}\\p{Lt}][\\p{L}\\p{M}\\s'-]+$",
            message = "Name should be in format: First_name Optional[Patronymic] Second_name")
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @OneToMany(mappedBy = "user")
    private List<Book> books;


    public User(String fullName, int yearOfBirth, int userId) {
        this.userId = userId;
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
    }

    public User() {
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getFullName() {
        return fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public int getUserId() {
        return userId;
    }



}
