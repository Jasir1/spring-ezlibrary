package com.mhdjasir.ezLibrary.domain.wishlist;

import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlist")
@SecurityRequirement(name = "ezLibrary")
public class WishlistResource {

    private final WishlistService wishlistService;

    @PostMapping("/add/{book-id}")
    public ResponseEntity<ApplicationResponseDTO> addToWishlist(@PathVariable("book-id") Long bookId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(bookId));
    }
    @GetMapping("/get")
    public ResponseEntity<List<Wishlist>> getAllFromWishlist() {
        return ResponseEntity.ok(wishlistService.getAllFromWishlist());
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(wishlistService.getWishlistById(id));
    }

    @GetMapping("/get/book/title/{title}")
    public ResponseEntity<List<Wishlist>> getWishlistByBookTitle(@PathVariable("title") String title) {
        return ResponseEntity.ok(wishlistService.getWishlistByBookTitle(title));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApplicationResponseDTO> deleteBookFromWishlist(@PathVariable("id") Long id) {
        return ResponseEntity.ok(wishlistService.deleteBookFromWishlist(id));
    }

}
