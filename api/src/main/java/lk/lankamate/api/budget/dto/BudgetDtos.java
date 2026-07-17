package lk.lankamate.api.budget.dto;

import java.math.BigDecimal;
import java.util.List;

public final class BudgetDtos {

    private BudgetDtos() {
    }

    public record BudgetSummary(
            BigDecimal totalBudget,
            String currency,
            BigDecimal estimatedTotal,
            BigDecimal remaining,
            int activityCount,
            List<CategoryBreakdown> byCategory,
            List<DayBreakdown> byDay
    ) {
    }

    public record CategoryBreakdown(
            String category,
            BigDecimal amount,
            int count
    ) {
    }

    public record DayBreakdown(
            int dayNumber,
            String title,
            BigDecimal amount
    ) {
    }
}