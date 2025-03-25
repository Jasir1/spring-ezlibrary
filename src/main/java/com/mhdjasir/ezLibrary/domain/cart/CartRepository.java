package com.mhdjasir.ezLibrary.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByBookIdAndUserId(Long bookId, Long userId);
    List<Cart> findByUserIdAndBook_TitleContainsIgnoreCase(Long userId, String book);
    List<Cart> findByUserIdAndBookId(Long userId, Long bookId);
    List<Cart> findAllByUserId(Long userId);
    Optional<Cart> findByIdAndUserId(Long id,Long userId);
}
