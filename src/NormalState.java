public class NormalState implements BudgetState {
    @Override
    public void handleState(Category context) {
        double limit = context.getLimit();
        if (limit <= 0) {
            return;
        }
        double ratio = context.getAmount() / limit;
        if (ratio > 1.0) {
            context.setState(new ExceededState());
        } else if (ratio > 0.8) {
            context.setState(new WarningState());
        } else if (ratio == 1.0) {
            context.setState(new LimitReachedState());
        }
    }

    @Override
    public String getStatusName() {
        return "Nie przekroczono limitu";
    }

    @Override
    public String getColorCode() {
        return "\u001B[32m";
    }
}