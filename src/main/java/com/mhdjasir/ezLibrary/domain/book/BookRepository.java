package com.mhdjasir.ezLibrary.domain.book;

import com.mhdjasir.ezLibrary.domain.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> , JpaSpecificationExecutor<Book> {
    Optional<Book> findByTitle(String title);
    Optional<Book> findByIdAndStatusAndDelete(Long id, Status status, Boolean delete);
    List<Book> findByTitleContainsIgnoreCaseAndStatusAndDelete(String title, Status status, Boolean delete);
    List<Book> findAllByStatusAndDelete(Status status, Boolean delete);
    List<Book> findAllByStatusAndDeleteAndUserId(Status status, Boolean delete,Long userId);
    List<Book> findAllByCategoryAndStatusAndDelete(Category category, Status status, Boolean delete);

    List<Book> findByStatusAndDeleteAndCategoryId(Status status, Boolean delete, Long id);

    List<Book> findByStatusAndDeleteAndCondition(Status status, boolean delete, Condition condition);
    List<Book> findByStatusAndDeleteAndLanguage(Status status, boolean delete, Language language);

    Optional<Book> findByIdAndUserIdAndStatusAndDelete(Long id, Long userId, Status status, boolean delete);

    Optional<Book> findByIdAndUserIdAndDelete(Long id, Long userId, boolean delete);
    Optional<Book> findByIdAndUserIdNotAndDelete(Long id, Long userId, boolean delete);

    List<Book> findAllByStatusAndDeleteOrderByCreatedAtDesc(Status status, boolean delete);
    List<Book> findAllByStatusAndDeleteOrderByRatingsDesc(Status status, boolean delete);
    List<Book> findAllByStatusAndDeleteOrderByPriceAsc(Status status, boolean delete);
    List<Book> findAllByStatusAndDeleteOrderByPriceDesc(Status status, boolean delete);

    List<Book> findFirstByUserIdAndDeleteOrderByIdDesc(Long id, boolean delete);

    List<Book> findAllByDeleteAndUserId(boolean delete, Long userId);

    List<Book> findByTitleContainsIgnoreCaseAndDeleteAndUserId(String title, boolean delete, Long id);
    Page<Book> findAllByStatusAndDelete(Status status, boolean delete, Pageable pageable);
    List<Book> findAllByIdInAndStatusAndDeleteOrderByRatingsDesc(
            List<Long> ids, Status status, boolean delete
    );
    List<Book> findAllByStatusAndDeleteAndRatingsGreaterThanEqualOrderByRatingsDesc(Status status, Boolean delete, Double ratings);
    List<Book> findTop8ByIdInAndStatusAndDeleteOrderByRatingsDesc(List<Long> ids, Status status, Boolean delete);

}
