package com.mhdjasir.ezLibrary.domain.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import com.mhdjasir.ezLibrary.exception.ApplicationCustomException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/book")
@SecurityRequirement(name = "ezLibrary")
public class BookResource {
    private final BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<ApplicationResponseDTO> addBook(@Valid @RequestBody BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.addBook(bookDTO));
    }
    @PostMapping("/upload-image/{id}")
    public ResponseEntity<ApplicationResponseDTO> uploadBookImages(@PathVariable("id") Long id, List<MultipartFile> files) {
        return ResponseEntity.ok(bookService.uploadBookImages(id,files));
    }

    @PostMapping(value = "/add-book-with-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationResponseDTO> addBookWithImages(
            @RequestPart("bookDTO") String bookDTOJson,
            @RequestPart("files") List<MultipartFile> files
    ) {
        // Parse bookDTO JSON
        ObjectMapper objectMapper = new ObjectMapper();
        BookDTO bookDTO;
        try {
            bookDTO = objectMapper.readValue(bookDTOJson, BookDTO.class);
        } catch (JsonProcessingException e) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_JSON", "Invalid JSON in bookDTO");
        }
        return ResponseEntity.ok(bookService.addBookWithImages(bookDTO,files));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, bookUpdateDTO));
    }

    @PutMapping("/status-change/{id}")
    public ResponseEntity<ApplicationResponseDTO> statusChange(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookService.statusChange(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApplicationResponseDTO> deleteBook(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookService.deleteBook(id));
    }

    //get common
//    @GetMapping("/get")
//    public ResponseEntity<List<Book>> getBooks() {
//        return ResponseEntity.ok(bookService.getBooks());
//    }
    @GetMapping("/get")
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        return ResponseEntity.ok(bookService.getBooks(page, size));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/get/filter")
    public ResponseEntity<List<Book>> getFilteredBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Boolean delete,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Double priceGreaterThan,
            @RequestParam(required = false) Double priceLessThan,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection
    ) {
        List<Book> books = bookService.getFilteredBooks(
                title, status, delete, category, condition, language,
                priceGreaterThan, priceLessThan, sortBy, sortDirection
        );
        return ResponseEntity.ok(books);
    }

    @GetMapping("/get/latest")
    public ResponseEntity<List<Book>> getLatestBooks() {
        return ResponseEntity.ok(bookService.getLatestBooks());
    }

    @GetMapping("/get/featured")
    public ResponseEntity<List<Book>> getFeaturedBooks() {
        return ResponseEntity.ok(bookService.getFeaturedBooks());
    }
    @GetMapping("/get/favourite-reads")
    public ResponseEntity<List<Book>> getFavouriteReadBooks() {
        return ResponseEntity.ok(bookService.getFavouriteReadBooks());
    }
    @GetMapping("/get/trendy-books")
    public ResponseEntity<List<Book>> getTrendyBooks() {
        return ResponseEntity.ok(bookService.getTrendyBooks());
    }

    //get own
    @GetMapping("/get-own")
    public ResponseEntity<List<Book>> getOwnBooks() {
        return ResponseEntity.ok(bookService.getOwnBooks());
    }
    @GetMapping("/get-own/{id}")
    public ResponseEntity<Book> getOwnBookById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(bookService.getOwnBookById(id));
    }
    @GetMapping("/get-own/filter")
    public ResponseEntity<List<Book>> getOwnFilteredBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Boolean delete,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Double priceGreaterThan,
            @RequestParam(required = false) Double priceLessThan,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection
    ) {
        List<Book> books = bookService.getOwnFilteredBooks(title, status, delete, category, condition, language, priceGreaterThan, priceLessThan, sortBy, sortDirection);
        return ResponseEntity.ok(books);
    }


    @GetMapping("/get-own/last-added-book")
    public ResponseEntity<Book> getOwnLastAddedBook() {
        return ResponseEntity.ok(bookService.getOwnLastAddedBook());
    }

    @GetMapping("/get-own/last-added")
    public ResponseEntity<Long> getOwnLastAddedBookId() {
        return ResponseEntity.ok(bookService.getOwnLastAddedBookId());
    }

}
