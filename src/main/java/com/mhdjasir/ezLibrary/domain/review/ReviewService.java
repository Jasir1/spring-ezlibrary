package com.mhdjasir.ezLibrary.domain.review;

import com.mhdjasir.ezLibrary.domain.book.BookRepository;
import com.mhdjasir.ezLibrary.domain.book.Status;
import com.mhdjasir.ezLibrary.domain.security.entity.User;
import com.mhdjasir.ezLibrary.domain.security.entity.UserRole;
import com.mhdjasir.ezLibrary.domain.security.repos.UserRepository;
import com.mhdjasir.ezLibrary.domain.user.UserService;
import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import com.mhdjasir.ezLibrary.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ApplicationResponseDTO addReview(Long bookId, ReviewDTO reviewDTO) {

        bookRepository.findByIdAndStatusAndDelete(bookId, Status.ACTIVE,false)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_NOT_FOUND", "Book not found"));

//        if (reviewRepository.findByBookIdAndUserId(bookId, userService.getCurrentUser().getId()).isPresent()){
//            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_ALREADY_RATED", "Product already rated!");
//        }

        Optional<Review> alreadyReviewed = reviewRepository.findByUserIdAndBookId(userService.getCurrentUser().getId(), bookId);

        if (alreadyReviewed.isPresent()){
            Review review = reviewRepository.findByUserIdAndBookId(userService.getCurrentUser().getId(), bookId).get();
            review.setComment(reviewDTO.getComment());
            review.setRatings(reviewDTO.getRatings());
            reviewRepository.save(review);
        }else{
            Review review = new Review();
            review.setBookId(bookId);
            review.setUserId(userService.getCurrentUser().getId());
            review.setComment(reviewDTO.getComment());
            review.setRatings(reviewDTO.getRatings());
            reviewRepository.save(review);
        }

        return new ApplicationResponseDTO(HttpStatus.CREATED, "BOOK_REVIEWED_SUCCESSFULLY", "Book reviewed successfully!");
    }


    public Review getReviewById(Long id) {

        return reviewRepository.findByIdAndUserId(id, userService.getCurrentUser().getId())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_REVIEW_FOUND", "No review found"));
    }

    public List<Review> getReviewByBookId(Long bookId) {

        return reviewRepository.findAllByBookId(bookId);
    }

    public ApplicationResponseDTO deleteReview(Long id) {
        reviewRepository.findById(id)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "REVIEW_NOT_FOUND", "Review not found"));

        User user = userRepository.findById(userService.getCurrentUser().getId()).get();
        if (user.getUserRole().equals(UserRole.ADMIN)){
            Review review = reviewRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "CART_ID_NOT_FOUND", "Cart id not found"));
            reviewRepository.delete(review);
            return new ApplicationResponseDTO(HttpStatus.OK, "BOOK_REVIEW_DELETED_SUCCESSFULLY", "Book review deleted successfully!");
        }else{
            return new ApplicationResponseDTO(HttpStatus.BAD_REQUEST, "BOOK_REVIEW_CANNOT_DELETED", "Book review cannot deleted!");
        }
    }
}
