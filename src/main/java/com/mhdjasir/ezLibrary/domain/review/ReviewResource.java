package com.mhdjasir.ezLibrary.domain.review;

import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
@SecurityRequirement(name = "ezLibrary")
public class ReviewResource {

    private final ReviewService reviewService;

    @PostMapping("/add/{book-id}")
    public ResponseEntity<ApplicationResponseDTO> addReview(@PathVariable("book-id") Long productId, @Valid @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.addReview(productId, reviewDTO));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/get/book/{book-id}")
    public ResponseEntity<List<Review>> getReviewByBookId(@PathVariable("book-id") Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewByBookId(bookId));
    }

    @DeleteMapping("/delete/{id}") // for admin
    public ResponseEntity<ApplicationResponseDTO> deleteReview(@PathVariable("id") Long id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}
