package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"profile"})
    List<User> findAll(Sort sort);

    @EntityGraph(attributePaths = {"profile"})
    User findByEmail(String email);
}
