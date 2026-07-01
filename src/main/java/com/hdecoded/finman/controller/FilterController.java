package com.hdecoded.finman.controller;

import com.hdecoded.finman.dto.ExpenseDTO;
import com.hdecoded.finman.dto.FilterDTO;
import com.hdecoded.finman.dto.IncomeDTO;
import com.hdecoded.finman.service.ExpenseService;
import com.hdecoded.finman.service.IncomeService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(
        @RequestBody FilterDTO filter) {

        // preparing the data or validation
        // MySQL DATE only spans 1000-01-01..9999-12-31, so LocalDate.MIN/MAX
        // fall outside the supported range and match no rows.
        LocalDate startDate =
            filter.getStartDate() != null ? filter.getStartDate() : LocalDate.of(1000, 1, 1);
        LocalDate endDate =
            filter.getEndDate() != null ? filter.getEndDate() : LocalDate.of(9999, 12, 31);
        String keyword = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField = filter.getSortField() != null ? filter.getSortField() : "date";
        Sort.Direction direction =
            "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        if ("income".equals(filter.getType())) {
            List<IncomeDTO> incomes = incomeService.filterIncomes(startDate, endDate, keyword,
                sort);
            return ResponseEntity.ok(incomes);
        } else if ("expense".equals(filter.getType())) {
            List<ExpenseDTO> expenses = expenseService.filterExpenses(startDate, endDate, keyword,
                sort);
            return ResponseEntity.ok(expenses);
        } else {
            return ResponseEntity.badRequest().body("INVALID TYPE, Must be 'income' or 'expense'");
        }
    }
}
