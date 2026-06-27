package com.hdecoded.finman.controller;

import com.hdecoded.finman.dto.CategoryDTO;
import com.hdecoded.finman.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  @PostMapping
  public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO) {
    CategoryDTO savedCategoryDTO = categoryService.saveCategory(categoryDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDTO);
  }

}
