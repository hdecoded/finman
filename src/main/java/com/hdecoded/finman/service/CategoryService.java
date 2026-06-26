package com.hdecoded.finman.service;

import com.hdecoded.finman.dto.CategoryDTO;
import com.hdecoded.finman.entity.CategoryEntity;
import com.hdecoded.finman.entity.ProfileEntity;
import com.hdecoded.finman.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    // Save Category
    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        if (categoryRepository.existsByCategoryNameAndProfileId(categoryDTO.getCategoryName(), profileEntity.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category with this name already exists");
        }

        CategoryEntity newCategory = toEntity(categoryDTO, profileEntity);
        newCategory = categoryRepository.save(newCategory);
        return toDTO(newCategory);
    }

    //helper methods
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .categoryName(categoryDTO.getCategoryName())
                .icon(categoryDTO.getIcon())
                .profile(profileEntity)
                .type(categoryDTO.getType())
                .build();
    }

    private CategoryDTO toDTO(CategoryEntity categoryEntity) {
        return CategoryDTO.builder()
                .id(categoryEntity.getId())
                .profileId(categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
                .categoryName(categoryEntity.getCategoryName())
                .icon(categoryEntity.getIcon())
                .type(categoryEntity.getType())
                .createdAt(categoryEntity.getCreatedAt())
                .updatedAt(categoryEntity.getUpdatedAt())
                .description(categoryEntity.getDescription())
                .build();
    }
}
