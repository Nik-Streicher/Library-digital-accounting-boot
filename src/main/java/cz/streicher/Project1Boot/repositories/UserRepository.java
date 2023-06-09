package cz.streicher.Project1Boot.repositories;

import cz.streicher.Project1Boot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByFullName(String fullName);
}
