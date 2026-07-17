package lk.lankamate.api.budget;

import lk.lankamate.api.auth.CurrentUser;
import lk.lankamate.api.budget.dto.BudgetDtos.BudgetSummary;
import lk.lankamate.api.common.response.ApiResponse;
import lk.lankamate.api.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trips/{tripId}/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public ApiResponse<BudgetSummary> getBudget(@PathVariable UUID tripId,
                                                @CurrentUser User me) {
        return ApiResponse.ok(budgetService.getBudget(tripId, me.getId()));
    }
}