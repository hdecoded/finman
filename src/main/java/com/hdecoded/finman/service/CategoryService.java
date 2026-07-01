package com.hdecoded.finman.service;

import com.hdecoded.finman.dto.CategoryDTO;
import com.hdecoded.finman.entity.CategoryEntity;
import com.hdecoded.finman.entity.ProfileEntity;
import com.hdecoded.finman.repository.CategoryRepository;
import java.util.List;
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
        if (categoryRepository.existsByCategoryNameAndProfileId(categoryDTO.getCategoryName(),
            profileEntity.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Category with this name already exists");
        }

        CategoryEntity newCategory = toEntity(categoryDTO, profileEntity);
        newCategory = categoryRepository.save(newCategory);
        return toDTO(newCategory);
    }

    //get categories for current user
    public List<CategoryDTO> getCategoriesForCurrentUser() {
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profileEntity.getId());
        return categories.stream().map(this::toDTO).toList();
    }

    // get categories by type for current user
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> entities = categoryRepository.findByTypeAndProfileId(
            type,
            profile.getId());
        return entities.stream().map(this::toDTO).toList();
    }

    public CategoryDTO updateCategory(Long categoryId, CategoryDTO dto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity existingCategory = categoryRepository.findByIdAndProfileId(categoryId,
                profile.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existingCategory.setCategoryName(dto.getCategoryName());
        existingCategory.setIcon(dto.getIcon());
        existingCategory.setDescription(dto.getDescription());
        existingCategory.setType(dto.getType());
        existingCategory = categoryRepository.save(existingCategory);
        return toDTO(existingCategory);
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
            .profileId(
                categoryEntity.getProfile() != null ? categoryEntity.getProfile().getId() : null)
            .categoryName(categoryEntity.getCategoryName())
            .icon(categoryEntity.getIcon())
            .type(categoryEntity.getType())
            .createdAt(categoryEntity.getCreatedAt())
            .updatedAt(categoryEntity.getUpdatedAt())
            .description(categoryEntity.getDescription())
            .build();
    }
}
