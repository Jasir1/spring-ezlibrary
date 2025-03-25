package com.mhdjasir.ezLibrary.domain.wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    Optional<Wishlist> findByBookIdAndUserId(Long productId,Long userId);
    Optional<Wishlist> findByIdAndUserId(Long id,Long userId);
    List<Wishlist> findByUserIdAndBook_TitleContainsIgnoreCase(Long userId,String product);
    List<Wishlist> findAllByUserId(Long userId);
    @Query("SELECT w.bookId FROM Wishlist w GROUP BY w.bookId HAVING COUNT(w.bookId) >= 5")
    List<Long> findPopularBookIds();
}
