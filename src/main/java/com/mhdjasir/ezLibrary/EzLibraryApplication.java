package com.mhdjasir.ezLibrary;

import com.mhdjasir.ezLibrary.domain.book.*;
import com.mhdjasir.ezLibrary.domain.category.Category;
import com.mhdjasir.ezLibrary.domain.category.CategoryRepository;
import com.mhdjasir.ezLibrary.domain.security.entity.User;
import com.mhdjasir.ezLibrary.domain.security.entity.UserRole;
import com.mhdjasir.ezLibrary.domain.security.repos.UserRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "ezLibrary API", version = "2.0", description = "ezLibrary"))
@SecurityScheme(name = "ezLibrary", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)

public class EzLibraryApplication {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public static void main(String[] args) {
        SpringApplication.run(EzLibraryApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // user
        if (userRepository.findByEmail("mhdjasir4565@gmail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .name("Jasir4565")
                    .email("mhdjasir4565@gmail.com")
                    .mobile("0762684595")
                    .password(passwordEncoder.encode("1234"))
                    .status(true)
                    .delete(false)
                    .userRole(UserRole.ADMIN)
                    .build());
        }
        if (userRepository.findByEmail("monika@gmail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .name("Monika")
                    .email("monika@gmail.com")
                    .mobile("0762684595")
                    .password(passwordEncoder.encode("1234"))
                    .status(true)
                    .delete(false)
                    .userRole(UserRole.USER)
                    .build());
        }

        // category
        if (categoryRepository.findByNameIgnoreCase("Art & Music").isEmpty()) {
            categoryRepository.save(Category.builder()
                    .name("Art & Music")
                    .status(true)
                    .build());
        }
        if (categoryRepository.findByNameIgnoreCase("Business").isEmpty()) {
            categoryRepository.save(Category.builder()
                    .name("Business")
                    .status(true)
                    .build());
        }
        if (categoryRepository.findByNameIgnoreCase("Computer & Technology").isEmpty()) {
            categoryRepository.save(Category.builder()
                    .name("Computer & Technology")
                    .status(true)
                    .build());
        }
        if (categoryRepository.findByNameIgnoreCase("History").isEmpty()) {
            categoryRepository.save(Category.builder()
                    .name("History")
                    .status(true)
                    .build());
        }
        if (categoryRepository.findByNameIgnoreCase("Horror").isEmpty()) {
            categoryRepository.save(Category.builder()
                    .name("Horror")
                    .status(true)
                    .build());
        }
        if (categoryRepository.findByNameIgnoreCase("Kids").isEmpty()) {
            categoryRepository.save(Category.builder()
                    .name("Kids")
                    .status(true)
                    .build());
        }
        if (categoryRepository.findByNameIgnoreCase("Mystery").isEmpty()) {
            categoryRepository.save(Category.builder()
                    .name("Mystery")
                    .status(true)
                    .build());
        }
        if (categoryRepository.findByNameIgnoreCase("Religion").isEmpty()) {
            categoryRepository.save(Category.builder()
                    .name("Religion")
                    .status(true)
                    .build());
        }
        if (categoryRepository.findByNameIgnoreCase("Romance").isEmpty()) {
            categoryRepository.save(Category.builder()
                    .name("Romance")
                    .status(true)
                    .build());
        }

        //book
//        bookRepository.save(Book.builder().title("Modern Calligraphy for Kids").categoryId(10002L).author("none").publisher("Callisto Media Inc").publishedDate("August 2019").isbn("1641523816").language(Language.ENGLISH).condition(Condition.NEW).pageCount(120).price(2400.00).description("none").stock(5).userId(10000L).delete(false).status(Status.ACTIVE).ratings(0.0).numOfReviews(0).imagePath("Modern Calligraphy for Kids.jpg").build());
//        bookRepository.save(Book.builder().title("The Ultimate Brush Lettering Guide").categoryId(10002L).author("Peggy Dean").publisher("Potter/Ten Speed/Harmony/Rodale").publishedDate("July 2018").isbn("0399582177").language(Language.ENGLISH).condition(Condition.NEW).pageCount(160).price(1000.00).description("-").stock(3).userId(10000L).delete(false).status(Status.ACTIVE).ratings(5.0).numOfReviews(0).imagePath("The Ultimate Brush Lettering Guide.jpg").build());
//        bookRepository.save(Book.builder().title("Who Was Claude Monet?").categoryId(10002L).author("Ann Waldron").publisher("Penguin Young Readers Group").publishedDate("July 2009").isbn("0448449854").language(Language.ENGLISH).condition(Condition.NEW).pageCount(112).price(980.00).description("-").stock(8).userId(10000L).delete(false).status(Status.INACTIVE).ratings(1.6).numOfReviews(0).imagePath("Who Was Claude Monet.jpg").build());
//        bookRepository.save(Book.builder().title("Rock Art Handbook: Techniques and Projects for Painting, Coloring, and Transforming Stones").categoryId(10002L).author("Samantha Sarles").publisher("Fox Chapel Publishing Company, Incorporated").publishedDate("August 2018").isbn("1565239458").language(Language.ENGLISH).condition(Condition.NEW).pageCount(160).price(2560.00).description("-").stock(5).userId(10000L).delete(false).status(Status.INACTIVE).ratings(4.2).numOfReviews(0).imagePath("Rock Art Handbook.jpg").build());
//        bookRepository.save(Book.builder().title("The World for Sale").categoryId(10003L).author("Javier Blas, Jack Farchy").publisher("Cornerstone").publishedDate("February 2021").isbn("9781473572188").language(Language.ENGLISH).condition(Condition.NEW).pageCount(416).price(11000.00).description("-").stock(5).userId(10000L).delete(false).status(Status.ACTIVE).ratings(1.5).numOfReviews(0).imagePath("The World for Sale.jpg").build());
//        bookRepository.save(Book.builder().title("Driven to Delight: Delivering World-Class Customer Experience the Mercedes-Benz Way").categoryId(10003L).author("Joseph A. Michelli").publisher("McGraw-Hill Education - Europe").publishedDate("December 2015").isbn("9780071812276").language(Language.ENGLISH).condition(Condition.NEW).pageCount(304).price(700000.00).description("-").stock(10).userId(10000L).delete(false).status(Status.ACTIVE).ratings(0.0).numOfReviews(0).imagePath("Driven to Delight.jpg").build());
//        bookRepository.save(Book.builder().title("Superintelligence").categoryId(10004L).author("Nick Bostrom").publisher("OUP Oxford").publishedDate("July 2014").isbn("9780191666827").language(Language.ENGLISH).condition(Condition.NEW).pageCount(432).price(3790.00).description("-").stock(9).userId(10000L).delete(false).status(Status.ACTIVE).ratings(3.6).numOfReviews(0).imagePath("Superintelligence.jpg").build());
//        bookRepository.save(Book.builder().title("Cybersecurity Program Development for Business").categoryId(10004L).author("Chris Moschovitis").publisher("John Wiley & Sons Inc").publishedDate("April 2018").isbn("9781119430001").language(Language.ENGLISH).condition(Condition.NEW).pageCount(224).price(8200.00).description("-").stock(16).userId(10000L).delete(false).status(Status.ACTIVE).ratings(3.5).numOfReviews(0).imagePath("Cybersecurity Program Development for Business.jpg").build());
//        bookRepository.save(Book.builder().title("Learn Latin from the Romans").categoryId(10005L).author("Eleanor Dickey").publisher("Cambridge University Press").publishedDate("June 2018").isbn("9781316728673").language(Language.ENGLISH).condition(Condition.NEW).pageCount(526).price(12570.00).description("-").stock(16).userId(10000L).delete(false).status(Status.ACTIVE).ratings(3.5).numOfReviews(0).imagePath("Learn Latin from the Romans.jpg").build());
//        bookRepository.save(Book.builder().title("True Ghost Stories: Real Haunted Castles and Fortresses").categoryId(10006L).author("Zachery Knowles").publisher("CreateSpace Independent Publishing Platform").publishedDate("October 2017").isbn("1979306125").language(Language.ENGLISH).condition(Condition.NEW).pageCount(106).price(5900.00).description("-").stock(16).userId(10000L).delete(false).status(Status.ACTIVE).ratings(0.0).numOfReviews(0).imagePath("True Ghost Stories.jpg").build());
//        bookRepository.save(Book.builder().title("Vampires: A Hunters Guide").categoryId(10006L).author("Steve White and Mark McKenzie-Ray").publisher("Bloomsbury Publishing Plc").publishedDate("August 2014").isbn("1472804244").language(Language.ENGLISH).condition(Condition.NEW).pageCount(80).price(6740.00).description("-").stock(6).userId(10000L).delete(false).status(Status.ACTIVE).ratings(0.0).numOfReviews(0).imagePath("Vampires.jpg").build());
//        bookRepository.save(Book.builder().title("Percy Jackson and the Olympians, Book Five the Last Olympian").categoryId(10007L).author("Rick Riordan").publisher("Hyperion Books for Children").publishedDate("January 2011").isbn("1423101502").language(Language.ENGLISH).condition(Condition.NEW).pageCount(432).price(3750.00).description("-").stock(3).userId(10000L).delete(false).status(Status.ACTIVE).ratings(0.0).numOfReviews(0).imagePath("Percy Jackson and the Olympians, Book Five the Last Olympian.jpg").build());
//        bookRepository.save(Book.builder().title("The Innocent").categoryId(10008L).author("David Baldacci").publisher("Grand Central Publishing").publishedDate("October 2012").isbn("1455519006").language(Language.ENGLISH).condition(Condition.NEW).pageCount(448).price(2990.00).description("-").stock(6).userId(10000L).delete(false).status(Status.ACTIVE).ratings(0.0).numOfReviews(0).imagePath("The Innocent.jpg").build());
//        bookRepository.save(Book.builder().title("I Dont Have Enough Faith to Be an Atheist").categoryId(10009L).author("Norman L. Geisler and Frank Turek").publisher("Crossway").publishedDate("March 2004").isbn("1581345615").language(Language.ENGLISH).condition(Condition.NEW).pageCount(448).price(3650.00).description("-").stock(2).userId(10000L).delete(false).status(Status.ACTIVE).ratings(0.0).numOfReviews(0).imagePath("I Don_t Have Enough Faith to Be an Atheist.jpg").build());
//        bookRepository.save(Book.builder().title("City of Fallen Angels, 4").categoryId(10010L).author("McElderry Books, Margaret K.").publisher("Hyperion Books for Children").publishedDate("September 2015").isbn("1481455990").language(Language.ENGLISH).condition(Condition.NEW).pageCount(480).price(2690.00).description("-").stock(3).userId(10000L).delete(false).status(Status.ACTIVE).ratings(0.0).numOfReviews(0).imagePath("City of Fallen Angels, 4.jpg").build());

    }
}

