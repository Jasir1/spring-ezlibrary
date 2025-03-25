package com.mhdjasir.ezLibrary.domain.category;

import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
@SecurityRequirement(name = "ezLibrary")
public class CategoryResource {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApplicationResponseDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
    }

    @GetMapping("/get-all") // for admin
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/get")
    public ResponseEntity<List<Category>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<List<Category>> getCategoryByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApplicationResponseDTO> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id,categoryDTO));
    }

    @PutMapping("/status-change/{id}")
    public ResponseEntity<ApplicationResponseDTO> statusChange(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.statusChange(id));
    }
}
