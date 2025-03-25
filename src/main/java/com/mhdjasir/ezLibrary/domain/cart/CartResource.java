package com.mhdjasir.ezLibrary.domain.cart;

import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@SecurityRequirement(name = "ezLibrary")
public class CartResource {

    private final CartService cartService;

    @PostMapping("/add/{book-id}")
    public ResponseEntity<ApplicationResponseDTO> addToCart(@PathVariable("book-id") Long bookId, @Valid @RequestBody CartDTO cartDTO) {
        return ResponseEntity.ok(cartService.addToCart(bookId,cartDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<Cart>> getAllFromCart() {
        return ResponseEntity.ok(cartService.getAllFromCart());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @GetMapping("/get/book/{id}")
    public ResponseEntity<List<Cart>> getCartByBookId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cartService.getCartByBookId(id));
    }

    @GetMapping("/get/book/title/{title}")
    public ResponseEntity<List<Cart>> getCartByBookTitle(@PathVariable("title") String title) {
        return ResponseEntity.ok(cartService.getCartByBookTitle(title));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApplicationResponseDTO> deleteBookFromCart(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cartService.deleteBookFromCart(id));
    }

}
