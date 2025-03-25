package com.mhdjasir.ezLibrary.domain.image;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/images")
@SecurityRequirement(name = "ezLibrary")
@Slf4j
public class ImageResource {

    private final Path bookImagePath = Paths.get("uploads/book");
    private final Path userImagePath = Paths.get("uploads/user");

    @GetMapping("/book/{filename:.+}")
    public ResponseEntity<Resource> getProductImage(@PathVariable String filename) {
        try {
            Path file = bookImagePath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("File not found " + filename, e);
        }
//        try {
//            Path file = bookImagePath.resolve(filename).normalize();
//            if (!file.startsWith(bookImagePath)) {
//                throw new RuntimeException("Unauthorized access to file: " + filename);
//            }
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
//                        .body(resource);
//            } else {
//                throw new RuntimeException("File not found " + filename);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("File not found " + filename, e);
//        }
    }

    @GetMapping("/user/{filename:.+}")
    public ResponseEntity<Resource> getUserImage(@PathVariable String filename) {
        try {
            Path file = userImagePath.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("File not found " + filename, e);
        }
    }
}
