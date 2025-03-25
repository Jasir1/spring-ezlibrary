package com.mhdjasir.ezLibrary.domain.cart;

import com.mhdjasir.ezLibrary.domain.book.Book;
import com.mhdjasir.ezLibrary.domain.book.BookRepository;
import com.mhdjasir.ezLibrary.domain.book.Status;
import com.mhdjasir.ezLibrary.domain.security.entity.User;
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
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ApplicationResponseDTO addToCart(Long bookId, CartDTO cartDTO) {

        Book book = bookRepository.findByIdAndStatusAndDelete(bookId, Status.ACTIVE,false)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_NOT_FOUND", "Book not found"));
        Integer stockAvailability = book.getStock();

        if (stockAvailability < cartDTO.getQty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_OUT_OF_STOCK", "Book out of stock");
        }

        Optional<Cart> optionalCart = cartRepository.findByBookIdAndUserId(bookId, userService.getCurrentUser().getId());

        if (optionalCart.isPresent()) {
            // already have
            Cart cart = optionalCart.get();
            int newQty = cartDTO.getQty();
            if (stockAvailability < newQty) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_OUT_OF_STOCK", "Book out of stock");
            }
            cart.setQty(newQty);
            cartRepository.save(cart);
            return new ApplicationResponseDTO(HttpStatus.OK, "BOOK_QTY_UPDATED_TO_CART_SUCCESSFULLY", "Book qty updated to cart successfully!");
        } else {
            // not in cart
            Cart cart = new Cart();
            cart.setBookId(bookId);
            cart.setQty(cartDTO.getQty());
            cart.setUserId(userService.getCurrentUser().getId());
            cartRepository.save(cart);
            return new ApplicationResponseDTO(HttpStatus.OK, "BOOK_ADDED_TO_CART_SUCCESSFULLY", "Book added to cart successfully!");
        }

    }
    public List<Cart> getAllFromCart() {
        return cartRepository.findAllByUserId(userService.getCurrentUser().getId());
    }

    public Cart getCartById(Long id) {
        return cartRepository.findByIdAndUserId(id, userService.getCurrentUser().getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "CART_ID_NOT_FOUND", "Cart id not found"));
    }

    public List<Cart> getCartByBookId(Long bookId) {
        return cartRepository.findByUserIdAndBookId(userService.getCurrentUser().getId(),bookId);
    }

    public List<Cart> getCartByBookTitle(String title) {
        return cartRepository.findByUserIdAndBook_TitleContainsIgnoreCase(userService.getCurrentUser().getId(),title);
    }

    public ApplicationResponseDTO deleteBookFromCart(Long id) {
        User user = userRepository.findByEmail(userService.getCurrentUser().getEmail())
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", "User not found"));
        Cart cart = cartRepository.findByIdAndUserId(id, user.getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "CART_ID_NOT_FOUND", "Cart id not found"));
        cartRepository.delete(cart);
        return new ApplicationResponseDTO(HttpStatus.OK, "BOOK_DELETED_FROM_CART_SUCCESSFULLY", "Book deleted from cart successfully!");
    }

}
