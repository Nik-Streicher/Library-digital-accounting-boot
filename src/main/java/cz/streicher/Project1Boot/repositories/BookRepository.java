package cz.streicher.Project1Boot.repositories;


import cz.streicher.Project1Boot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByTitle(String title);
    List<Book> findAllByTitleStartingWith(String tittle);

}
