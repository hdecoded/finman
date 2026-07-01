package com.hdecoded.finman.service;

import com.hdecoded.finman.dto.IncomeDTO;
import com.hdecoded.finman.entity.CategoryEntity;
import com.hdecoded.finman.entity.IncomeEntity;
import com.hdecoded.finman.entity.ProfileEntity;
import com.hdecoded.finman.repository.CategoryRepository;
import com.hdecoded.finman.repository.IncomeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IncomeService {

  private final CategoryRepository categoryRepository;
  private final IncomeRepository incomeRepository;
  private final ProfileService profileService;

  //Adds a new Income to the Database
  public IncomeDTO addIncome(IncomeDTO incomeDTO) {

    ProfileEntity profile = profileService.getCurrentProfile();
    CategoryEntity category = categoryRepository.findById(incomeDTO.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    IncomeEntity newIncome = toEntity(incomeDTO, profile, category);
    newIncome = incomeRepository.save(newIncome);
    return toDTO(newIncome);
  }

  // Delete income by id
  public void deleteIncome(Long incomeId) {
    ProfileEntity profile = profileService.getCurrentProfile();
    IncomeEntity entity = incomeRepository.findById(incomeId)
        .orElseThrow(() -> new RuntimeException("Income not found"));
    if (!entity.getProfile().getId().equals(profile.getId())) {
      throw new RuntimeException("Unauthorized to delete income");
    }
    incomeRepository.delete(entity);
  }

  // Retrieve all the incomes based on the start date and end date
  public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser() {
    ProfileEntity profile = profileService.getCurrentProfile();
    LocalDate now = LocalDate.now();
    LocalDate startDate = now.withDayOfMonth(1);
    LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
    List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(),
        startDate, endDate);
    return list.stream().map(this::toDTO).toList();
  }

  // Get latest 5 i for current user
  public List<IncomeDTO> getLatest5IncomesForCurrentUser() {
    ProfileEntity profile = profileService.getCurrentProfile();
    List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(
        profile.getId());
    return list.stream().map(this::toDTO).toList();
  }

  // Get total income for Current User
  public BigDecimal getTotalIncomesForCurrentUser() {
    ProfileEntity profile = profileService.getCurrentProfile();
    BigDecimal total = incomeRepository.findTotalIncomeByProfileId(profile.getId());
    return total != null ? total : BigDecimal.ZERO;
  }

  // Helper Methods
  private IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profileEntity,
      CategoryEntity categoryEntity) {
    return IncomeEntity.builder()
        .name(incomeDTO.getName())
        .memo(incomeDTO.getMemo())
        .imageURL(incomeDTO.getImageURL())
        .date(incomeDTO.getDate())
        .amount(incomeDTO.getAmount())
        .category(categoryEntity)
        .profile(profileEntity)
        .build();
  }

  private IncomeDTO toDTO(IncomeEntity incomeEntity) {
    return IncomeDTO.builder()
        .id(incomeEntity.getId())
        .name(incomeEntity.getName())
        .memo(incomeEntity.getMemo())
        .imageURL(incomeEntity.getImageURL())
        .date(incomeEntity.getDate())
        .amount(incomeEntity.getAmount())
        .categoryName(
            incomeEntity.getCategory() != null ? incomeEntity.getCategory().getCategoryName()
                : "N/A")
        .categoryId(
            incomeEntity.getCategory() != null ? incomeEntity.getCategory().getId() : null)
        .createdAt(incomeEntity.getCreatedDate())
        .updatedAt(incomeEntity.getUpdatedDate())
        .build();
  }
}
