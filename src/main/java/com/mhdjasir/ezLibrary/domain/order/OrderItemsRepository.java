package com.mhdjasir.ezLibrary.domain.order;

import com.mhdjasir.ezLibrary.domain.book.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    // get all trendy book with pageable
    @Query("SELECT oi.book FROM OrderItems oi " +
            "JOIN oi.order o " +
            "WHERE o.status != :status " +
            "GROUP BY oi.book.id " +
            "HAVING COUNT(DISTINCT o.user.id) >= 3 " +
            "ORDER BY MAX(o.paidAt) DESC")
    List<Book> findBooksBoughtByThreeOrMoreUsers(@Param("status") OrderStatus status, Pageable pageable);

    // get trendy books without pageable
    @Query("SELECT oi.bookId " +
            "FROM OrderItems oi " +
            "JOIN Order o ON oi.order.id = o.id " +
            "GROUP BY oi.bookId " +
            //"HAVING COUNT(DISTINCT o.userId) >= :minUsers") // different 3 users
            "HAVING COUNT(o.userId) >= :minUsers") // 3 users
    List<Long> findBooksBoughtByMinUsers(@Param("minUsers") int minUsers);
}