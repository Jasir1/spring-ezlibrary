package com.mhdjasir.ezLibrary.domain.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Optional<Review> findByBookIdAndUserId(Long productId, Long userId);
    Optional<Review> findByUserIdAndBookId(Long userId, Long productId);
    List<Review> findAllByUserId(Long userId);
    List<Review> findAllByBookId(Long productId);
    Optional<Review> findByIdAndUserId(Long id, Long userId);
}
