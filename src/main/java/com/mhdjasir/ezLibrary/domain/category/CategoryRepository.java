package com.mhdjasir.ezLibrary.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByStatusAndNameIgnoreCase(boolean status,String name);
    Optional<Category> findByNameIgnoreCase(String name);
    Optional<Category> findByIdAndStatus(Long id, boolean status);
    List<Category> findByStatusAndNameContainsIgnoreCase(boolean status,String name);
    List<Category> findAllByStatus(boolean status);
}
