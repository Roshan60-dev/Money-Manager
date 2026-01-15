package com.roshan.moneymanager.controller;

import com.roshan.moneymanager.dto.ExpenseDTO;
import com.roshan.moneymanager.dto.FilterDTO;
import com.roshan.moneymanager.dto.IncomeDTO;
import com.roshan.moneymanager.entity.ExpenseEntity;
import com.roshan.moneymanager.service.ExpenseService;
import com.roshan.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filter){
        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate():LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate():LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        String keyword = filter.getKeyword() != null ? filter.getKeyword():"";
        String sortField = filter.getSortField() != null ? filter.getSortField():"date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC:Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortField);
        if ("income".equals(filter.getType())){
           List<IncomeDTO> incomes = incomeService.filterIncomes(startDate,endDate,keyword,sort);
           return ResponseEntity.ok(incomes);
        } else if ("expense".equalsIgnoreCase(filter.getType())) {
            List<ExpenseDTO> expenses = expenseService.filterExpenses(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(expenses);
        }
        else {
            return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expense' ");

        }

    }
}
