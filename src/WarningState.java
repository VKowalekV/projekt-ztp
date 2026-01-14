public class WarningState implements BudgetState {
    @Override
    public void handleState(Category context) {
        double limit = context.getLimit();
        double ratio = context.getAmount() / limit;
        if (ratio > 1.0) {
            context.setState(new ExceededState());
        } else if (ratio < 0.8) {
            context.setState(new NormalState());
        } else if (ratio == 1.0) {
            context.setState(new LimitReachedState());
        }
    }

    @Override
    public String getStatusName() {
        return "Wydano ponad 80% z założonego limitu";
    }

    @Override
    public String getColorCode() {
        return "\u001B[33m";
    }

}
