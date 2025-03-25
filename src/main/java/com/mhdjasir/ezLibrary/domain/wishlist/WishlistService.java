package com.mhdjasir.ezLibrary.domain.wishlist;

import com.mhdjasir.ezLibrary.domain.book.BookRepository;
import com.mhdjasir.ezLibrary.domain.book.Status;
import com.mhdjasir.ezLibrary.domain.security.repos.UserRepository;
import com.mhdjasir.ezLibrary.domain.user.UserService;
import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import com.mhdjasir.ezLibrary.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public ApplicationResponseDTO addToWishlist(Long bookId) {
        bookRepository.findByIdAndStatusAndDelete(bookId, Status.ACTIVE,false)
                .orElseThrow(() -> new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_NOT_FOUND", "Book not found"));

        if (wishlistRepository.findByBookIdAndUserId(bookId, userService.getCurrentUser().getId()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_ALREADY_ADDED_TO_WISHLIST", "Book already added to wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setBookId(bookId);
        wishlist.setUserId(userService.getCurrentUser().getId());
        wishlistRepository.save(wishlist);
        return new ApplicationResponseDTO(HttpStatus.CREATED, "BOOK_ADDED_TO_WISHLIST_SUCCESSFULLY", "Book added to wishlist successfully!");
    }

    public List<Wishlist> getAllFromWishlist() {
        return wishlistRepository.findAllByUserId(userService.getCurrentUser().getId());
    }

    public Wishlist getWishlistById(Long id) {
        return wishlistRepository.findByIdAndUserId(id, userService.getCurrentUser().getId()).
                orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "WISHLIST_ID_NOT_FOUND", "Wishlist id not found"));
    }

    public List<Wishlist> getWishlistByBookTitle(String title) {
        List<Wishlist> wishlistList = wishlistRepository.findByUserIdAndBook_TitleContainsIgnoreCase(userService.getCurrentUser().getId(),title);
        if (wishlistList.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "NO_BOOK_FOUND_ON_WISHLIST", "No book found on wishlist");
        }
        return wishlistList;
    }

    public ApplicationResponseDTO deleteBookFromWishlist(Long id) {

        Wishlist wishlist = wishlistRepository.findByIdAndUserId(id, userService.getCurrentUser().getId()).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "WISHLIST_ID_NOT_FOUND", "Wishlist id not found"));
        wishlistRepository.delete(wishlist);
        return new ApplicationResponseDTO(HttpStatus.OK, "BOOK_DELETED_FROM_WISHLIST_SUCCESSFULLY", "Book deleted from wishlist successfully!");
    }

}
