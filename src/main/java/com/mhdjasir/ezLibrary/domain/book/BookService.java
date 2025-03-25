package com.mhdjasir.ezLibrary.domain.book;

import com.mhdjasir.ezLibrary.domain.bookImage.BookImage;
import com.mhdjasir.ezLibrary.domain.bookImage.BookImageRepository;
import com.mhdjasir.ezLibrary.domain.category.CategoryRepository;
import com.mhdjasir.ezLibrary.domain.order.OrderItemsRepository;
import com.mhdjasir.ezLibrary.domain.order.OrderStatus;
import com.mhdjasir.ezLibrary.domain.security.entity.UserRole;
import com.mhdjasir.ezLibrary.domain.user.UserService;
import com.mhdjasir.ezLibrary.domain.wishlist.Wishlist;
import com.mhdjasir.ezLibrary.domain.wishlist.WishlistRepository;
import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import com.mhdjasir.ezLibrary.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final BookImageRepository bookImageRepository;
    private final WishlistRepository wishlistRepository;
    private final OrderItemsRepository orderItemsRepository;
    private final UserService userService;

    public ApplicationResponseDTO uploadBookImages(Long bookId, List<MultipartFile> files) {
        // Find the book by ID and ensure it exists
        Optional<Book> optionalBook = bookRepository.findByIdAndUserIdAndStatusAndDelete(
                bookId,
                userService.getCurrentUser().getId(),
                Status.ACTIVE,
                false
        );
        if (optionalBook.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "BOOK_NOT_FOUND", "Book not found");
        }

        Book book = optionalBook.get();

        if (files == null || files.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILES_NOT_SELECTED", "No files selected");
        }

        // Validate and process each file
        List<BookImage> bookImages = new ArrayList<>();
        String projectRoot = System.getProperty("user.dir");

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SELECTED", "File not selected");
            }

            try {
                String originalFilename = file.getOriginalFilename();
                if (originalFilename != null) {
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                    // Validate file type
                    if (!(fileExtension.equalsIgnoreCase(".jpg") ||
                            fileExtension.equalsIgnoreCase(".jpeg") ||
                            fileExtension.equalsIgnoreCase(".png"))) {
                        throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE", "Invalid file type. Only JPG, JPEG, and PNG are allowed.");
                    }

                    // Save the file
                    String newFileName = UUID.randomUUID() + fileExtension;
                    String imagePath = "/uploads/book/" + newFileName;
                    Path path = Paths.get(projectRoot + imagePath);
                    file.transferTo(path.toFile());

                    // Create BookImage object
                    BookImage bookImage = BookImage.builder()
                            .imagePath(imagePath)
                            .bookId(book.getId())
                            .build();

                    bookImages.add(bookImage);
                } else {
                    throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORIGINAL_FILE_NAME_NOT_FOUND", "Original file name not found");
                }
            } catch (IOException e) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SAVED", "File not saved");
            }
        }

        // Save all book images
        bookImageRepository.saveAll(bookImages);

        // Optionally update the book with images
        book.getImages().addAll(bookImages);
        bookRepository.save(book);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "BOOK_IMAGES_UPLOADED_SUCCESSFULLY", "Book images uploaded successfully!");

    }

    public ApplicationResponseDTO addBook(BookDTO bookDTO) {
        if (categoryRepository.findByIdAndStatus(bookDTO.getCategoryId(), true).isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CATEGORY_NOT_FOUND", "Category not found");
        }

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setCategoryId(bookDTO.getCategoryId());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublishedDate(bookDTO.getPublishedDate());
        book.setIsbn(bookDTO.getIsbn());

        String language = bookDTO.getLanguage();
        if (language.equals("ENGLISH")) {
            book.setLanguage(Language.ENGLISH);
        } else if (language.equals("SPANISH")) {
            book.setLanguage(Language.SPANISH);
        }

        String condition = bookDTO.getCondition();
        if (condition.equals("NEW")) {
            book.setCondition(Condition.NEW);
        } else if (condition.equals("USED")) {
            book.setCondition(Condition.USED);
        }

        book.setPageCount(bookDTO.getPageCount());
        book.setPrice(bookDTO.getPrice());
        book.setDescription(bookDTO.getDescription());
        book.setStock(bookDTO.getStock());
        book.setUserId(userService.getCurrentUser().getId());

        if (userService.getCurrentUser().getUserRole() == UserRole.ADMIN) {
            book.setStatus(Status.ACTIVE);
        } else if (userService.getCurrentUser().getUserRole() == UserRole.USER) {
            book.setStatus(Status.NOT_APPROVED);
        }

        book.setImages(new ArrayList<>()); // Initialize the image paths
        bookRepository.save(book);

        return new ApplicationResponseDTO(HttpStatus.CREATED, "BOOK_ADDED_SUCCESSFULLY", "Book added successfully!");
    }

    public ApplicationResponseDTO addBookWithImages(BookDTO bookDTO, List<MultipartFile> files) {
        // Validate category
        if (categoryRepository.findByIdAndStatus(bookDTO.getCategoryId(), true).isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CATEGORY_NOT_FOUND", "Category not found");
        }

        // Create a new book instance
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setCategoryId(bookDTO.getCategoryId());
        book.setAuthor(bookDTO.getAuthor());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublishedDate(bookDTO.getPublishedDate());
        book.setIsbn(bookDTO.getIsbn());

        // Set language
        String language = bookDTO.getLanguage();
        if (language.equals("ENGLISH")) {
            book.setLanguage(Language.ENGLISH);
        } else if (language.equals("SPANISH")) {
            book.setLanguage(Language.SPANISH);
        }

        // Set condition
        String condition = bookDTO.getCondition();
        if (condition.equals("NEW")) {
            book.setCondition(Condition.NEW);
        } else if (condition.equals("USED")) {
            book.setCondition(Condition.USED);
        }

        book.setPageCount(bookDTO.getPageCount());
        book.setPrice(bookDTO.getPrice());
        book.setDescription(bookDTO.getDescription());
        book.setStock(bookDTO.getStock());
        book.setUserId(userService.getCurrentUser().getId());

        // Set book status based on user role
        if (userService.getCurrentUser().getUserRole() == UserRole.ADMIN) {
            book.setStatus(Status.ACTIVE);
        } else if (userService.getCurrentUser().getUserRole() == UserRole.USER) {
            book.setStatus(Status.NOT_APPROVED);
        }

        book.setImages(new ArrayList<>()); // Initialize the images list

        // Save the book (required to generate an ID for images)
        bookRepository.save(book);

        // Validate and process files if provided
        if (files != null && !files.isEmpty()) {
            List<BookImage> bookImages = new ArrayList<>();
            String projectRoot = System.getProperty("user.dir");

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SELECTED", "File not selected");
                }

                try {
                    String originalFilename = file.getOriginalFilename();
                    if (originalFilename != null) {
                        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                        // Validate file type
                        if (!(fileExtension.equalsIgnoreCase(".jpg") ||
                                fileExtension.equalsIgnoreCase(".jpeg") ||
                                fileExtension.equalsIgnoreCase(".png"))) {
                            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "INVALID_FILE_TYPE", "Invalid file type. Only JPG, JPEG, and PNG are allowed.");
                        }

                        // Save the file
                        String newFileName = UUID.randomUUID() + fileExtension;
                        String imagePath = "/uploads/book/" + newFileName;
                        Path path = Paths.get(projectRoot + imagePath);
                        file.transferTo(path.toFile());

                        // Create BookImage object
                        BookImage bookImage = BookImage.builder()
                                .imagePath(imagePath)
                                .bookId(book.getId())
                                .build();

                        bookImages.add(bookImage);
                    } else {
                        throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "ORIGINAL_FILE_NAME_NOT_FOUND", "Original file name not found");
                    }
                } catch (IOException e) {
                    throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "FILE_NOT_SAVED", "File not saved");
                }
            }

            // Save all book images
            bookImageRepository.saveAll(bookImages);

            // Associate images with the book
            book.getImages().addAll(bookImages);
            bookRepository.save(book);
        }

        return new ApplicationResponseDTO(HttpStatus.CREATED, "BOOK_ADDED_WITH_IMAGES_SUCCESSFULLY", "Book added with images successfully!");
    }

//    public List<Book> getBooks() {
//        return bookRepository.findAllByStatusAndDelete(Status.ACTIVE, false);
//    }

    public Page<Book> getBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findAllByStatusAndDelete(Status.ACTIVE, false, pageable);
    }


    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findByIdAndStatusAndDelete(id, Status.ACTIVE, false);
        if (book.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "Book not found");
        }
        return book.get();
    }

    public List<Book> getFilteredBooks(
            String title,
            Status status,
            Boolean delete,
            Long category,
            String condition,
            String language,
            Double priceGreaterThan,
            Double priceLessThan,
            String sortBy,
            String sortDirection) {

        Specification<Book> specification = BookSpecification.filterByAttributes(
                title, status, delete, category, condition, language, priceGreaterThan, priceLessThan
        );
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return bookRepository.findAll(specification, sort);
    }

    public ApplicationResponseDTO updateBook(Long id, BookUpdateDTO bookUpdateDTO) {

        Book book = bookRepository.findByIdAndUserIdAndDelete(id, userService.getCurrentUser().getId(), false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "Book not found"));
//        Product product = productRepository.findByIdAndUserIdAndDelete(id, userService.getCurrentUser().getId(), false).get();

        book.setTitle(bookUpdateDTO.getTitle());

        if (bookUpdateDTO.getAuthor() != null) {
            book.setAuthor(bookUpdateDTO.getAuthor());
        }
        if (bookUpdateDTO.getCategoryId() != null) {
            book.setCategoryId(bookUpdateDTO.getCategoryId());
        }
        if (bookUpdateDTO.getPublisher() != null) {
            book.setPublisher(bookUpdateDTO.getPublisher());
        }
        if (bookUpdateDTO.getPublishedDate() != null) {
            book.setPublishedDate(bookUpdateDTO.getPublishedDate());
        }
        if (bookUpdateDTO.getIsbn() != null) {
            book.setIsbn(bookUpdateDTO.getIsbn());
        }
        String language = bookUpdateDTO.getLanguage();
        if (language != null) {
            if (language.equals("ENGLISH")) {
                book.setLanguage(Language.ENGLISH);
            } else if (language.equals("SPANISH")) {
                book.setLanguage(Language.SPANISH);
            }
        }

        String condition = bookUpdateDTO.getCondition();
        if (condition != null) {
            if (condition.equals("NEW")) {
                book.setCondition(Condition.NEW);
            } else if (condition.equals("USED")) {
                book.setCondition(Condition.USED);
            }
        }

        if (bookUpdateDTO.getPageCount() != null) {
            book.setPageCount(bookUpdateDTO.getPageCount());
        }
        if (bookUpdateDTO.getPrice() != null) {
            book.setPrice(bookUpdateDTO.getPrice());
        }
        if (bookUpdateDTO.getDescription() != null) {
            book.setDescription(bookUpdateDTO.getDescription());
        }
        if (bookUpdateDTO.getStock() != null) {
            book.setStock(bookUpdateDTO.getStock());
        }
        book.setUserId(userService.getCurrentUser().getId());
        bookRepository.save(book);

        return new ApplicationResponseDTO(HttpStatus.OK, "BOOK_UPDATED_SUCCESSFULLY", "Book updated successfully!");

    }

    public ApplicationResponseDTO statusChange(Long id) {

        Book book = bookRepository.findByIdAndUserIdAndDelete(id, userService.getCurrentUser().getId(), false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "Book not found"));
        if (book.getStatus() == Status.INACTIVE) {
            book.setStatus(Status.ACTIVE);
        } else if (book.getStatus() == Status.ACTIVE) {
            book.setStatus(Status.INACTIVE);
        }
        bookRepository.save(book);
        return new ApplicationResponseDTO(HttpStatus.OK, "BOOK_STATUS_CHANGED_SUCCESSFULLY", "Book status changed successfully!");
    }

    public ApplicationResponseDTO deleteBook(Long id) {
        Book book = bookRepository.findByIdAndUserIdAndDelete(id, userService.getCurrentUser().getId(), false).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "Book not found"));
        book.setDeletedAt(LocalDateTime.now());
        book.setDelete(true);
        bookRepository.save(book);
        return new ApplicationResponseDTO(HttpStatus.OK, "BOOK_DELETED_SUCCESSFULLY", "Book deleted successfully!");
    }

    public List<Book> getLatestBooks() {
        return bookRepository.findAllByStatusAndDeleteOrderByCreatedAtDesc(Status.ACTIVE, false);

    }

    public List<Book> getFeaturedBooks() {
//        return bookRepository.findAllByStatusAndDeleteOrderByRatingsDesc(Status.ACTIVE, false);
        return bookRepository.findAllByStatusAndDeleteAndRatingsGreaterThanEqualOrderByRatingsDesc(Status.ACTIVE, false, 3.0);
    }

    public List<Book> getFavouriteReadBooks() {
        List<Long> popularBookIds = wishlistRepository.findPopularBookIds();
        return bookRepository.findAllByIdInAndStatusAndDeleteOrderByRatingsDesc(
                popularBookIds, Status.ACTIVE, false
        );
    }

    public List<Book> getTrendyBooks() {
//        // get all trendy book with pageable
//        Pageable pageable = PageRequest.of(0, 8); // Fetch top 8 books
//        return orderItemsRepository.findBooksBoughtByThreeOrMoreUsers(OrderStatus.CANCELLED, pageable);

        List<Long> bookIds = orderItemsRepository.findBooksBoughtByMinUsers(3);
        return bookRepository.findTop8ByIdInAndStatusAndDeleteOrderByRatingsDesc(bookIds, Status.ACTIVE, false);

    }

    public List<Book> getOwnBooks() {
        return bookRepository.findAllByDeleteAndUserId(false, userService.getCurrentUser().getId());
    }

    public Book getOwnBookById(Long id) {
        Optional<Book> book = bookRepository.findByIdAndUserIdAndDelete(id, userService.getCurrentUser().getId(), false);
        if (book.isEmpty()) {
            throw new ApplicationCustomException(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "Book not found");
        }
        return book.get();
    }

    public List<Book> getOwnFilteredBooks(
            String title,
            Status status,
            Boolean delete,
            Long category,
            String condition,
            String language,
            Double priceGreaterThan,
            Double priceLessThan,
            String sortBy,
            String sortDirection) {
        Specification<Book> specification = BookSpecification.filterByAttributes(title, status, delete, category, condition, language, priceGreaterThan, priceLessThan)
                .and((root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("user").get("id"), userService.getCurrentUser().getId())
                );

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return bookRepository.findAll(specification, sort);
    }


    public Book getOwnLastAddedBook() {
        return bookRepository.findFirstByUserIdAndDeleteOrderByIdDesc(userService.getCurrentUser().getId(), false).getFirst();
    }

    public Long getOwnLastAddedBookId() {
        return bookRepository.findFirstByUserIdAndDeleteOrderByIdDesc(userService.getCurrentUser().getId(), false).getFirst().getId();
    }

}
