package com.hdecoded.finman.service;

import com.hdecoded.finman.dto.ExpenseDTO;
import com.hdecoded.finman.entity.CategoryEntity;
import com.hdecoded.finman.entity.ExpenseEntity;
import com.hdecoded.finman.entity.ProfileEntity;
import com.hdecoded.finman.repository.CategoryRepository;
import com.hdecoded.finman.repository.ExpenseRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {

  private final CategoryRepository categoryRepository;
  private final ExpenseRepository expenseRepository;
  private final ProfileService profileService;

  //Adds a new Expense to the Database
  public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {

    ProfileEntity profile = profileService.getCurrentProfile();
    CategoryEntity category = categoryRepository.findById(expenseDTO.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));
    ExpenseEntity newExpense = toEntity(expenseDTO, profile, category);
    newExpense = expenseRepository.save(newExpense);
    return toDTO(newExpense);
  }

  // Retrieve all the expenses based on the start date and end date
  public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {
    ProfileEntity profile = profileService.getCurrentProfile();
    LocalDate now = LocalDate.now();
    LocalDate startDate = now.withDayOfMonth(1);
    LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
    List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(),
        startDate, endDate);
    return list.stream().map(this::toDTO).toList();
  }

  // Get latest 5 Expenses for current user
  public List<ExpenseDTO> getLatest5ExpensesForCurrentUser() {
    ProfileEntity profile = profileService.getCurrentProfile();
    List<ExpenseEntity> list = expenseRepository.findTop5ByProfileIdOrderByDateDesc(
        profile.getId());
    return list.stream().map(this::toDTO).toList();
  }

  // Get total expenses for Current User
  public BigDecimal getTotalExpensesForCurrentUser() {
    ProfileEntity profile = profileService.getCurrentProfile();
    BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profile.getId());
    return total != null ? total : BigDecimal.ZERO;
  }

  // Delete expense by id
  public void deleteExpense(Long expenseId) {
    ProfileEntity profile = profileService.getCurrentProfile();
    ExpenseEntity entity = expenseRepository.findById(expenseId)
        .orElseThrow(() -> new RuntimeException("Expense not found"));
    if (!entity.getProfile().getId().equals(profile.getId())) {
      throw new RuntimeException("Unauthorized to delete expense");
    }
    expenseRepository.delete(entity);
  }

  // Helper Methods
  private ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity,
      CategoryEntity categoryEntity) {
    return ExpenseEntity.builder()
        .name(expenseDTO.getName())
        .memo(expenseDTO.getMemo())
        .imageURL(expenseDTO.getImageURL())
        .date(expenseDTO.getDate())
        .amount(expenseDTO.getAmount())
        .category(categoryEntity)
        .profile(profileEntity)
        .build();
  }

  private ExpenseDTO toDTO(ExpenseEntity expenseEntity) {
    return ExpenseDTO.builder()
        .id(expenseEntity.getId())
        .name(expenseEntity.getName())
        .memo(expenseEntity.getMemo())
        .imageURL(expenseEntity.getImageURL())
        .date(expenseEntity.getDate())
        .amount(expenseEntity.getAmount())
        .categoryName(
            expenseEntity.getCategory() != null ? expenseEntity.getCategory().getCategoryName()
                : "N/A")
        .categoryId(
            expenseEntity.getCategory() != null ? expenseEntity.getCategory().getId() : null)
        .createdAt(expenseEntity.getCreatedDate())
        .updatedAt(expenseEntity.getUpdatedDate())
        .build();
  }
}
