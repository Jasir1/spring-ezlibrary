package com.mhdjasir.ezLibrary.domain.category;

import com.mhdjasir.ezLibrary.dto.ApplicationResponseDTO;
import com.mhdjasir.ezLibrary.exception.ApplicationCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private static final String CATEGORY_NOT_FOUND_CODE = "CATEGORY_NOT_FOUND";
    private static final String CATEGORY_NOT_FOUND_MSG = "Category not found";
    public ApplicationResponseDTO createCategory(CategoryDTO categoryDTO) {

        if (categoryRepository.findByNameIgnoreCase(categoryDTO.getName()).isPresent()) {
            throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CATEGORY_ALREADY_EXIST", "Category already exist");
        }
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setStatus(true);
        categoryRepository.save(category);
        return new ApplicationResponseDTO(HttpStatus.CREATED, "CATEGORY_CREATED_SUCCESSFULLY", "Category created successfully!");
    }

    public List<Category> getCategories() {
        return categoryRepository.findAllByStatus(true);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findByIdAndStatus(id,true).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG));
    }

    public List<Category> getCategoryByName(String name) {
        return categoryRepository.findByStatusAndNameContainsIgnoreCase(true,name);
    }

    public ApplicationResponseDTO updateCategory(Long id, CategoryDTO categoryDTO) {

        Category category = categoryRepository.findByIdAndStatus(id,true).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG));

        categoryRepository.findByStatusAndNameIgnoreCase(true,categoryDTO.getName()).ifPresent(existingCategory -> {
            if (!category.getName().equals(categoryDTO.getName())) {
                throw new ApplicationCustomException(HttpStatus.BAD_REQUEST, "CATEGORY_NAME_ALREADY_EXISTS", "Category name already exists");
            }
        });

        category.setName(categoryDTO.getName());

        categoryRepository.save(category);

        return new ApplicationResponseDTO(HttpStatus.OK, "CATEGORY_UPDATED_SUCCESSFULLY", "Category updated successfully!");
    }

    public ApplicationResponseDTO statusChange(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ApplicationCustomException(HttpStatus.NOT_FOUND, CATEGORY_NOT_FOUND_CODE, CATEGORY_NOT_FOUND_MSG));
        category.setStatus(!category.getStatus().equals(true));
        categoryRepository.save(category);
        return new ApplicationResponseDTO(HttpStatus.OK, "CATEGORY_STATUS_UPDATED_SUCCESSFULLY", "Category status updated successfully!");
    }

}
