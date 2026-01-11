public class WarningState implements BudgetState {
    @Override
    public void handleState(Category context) {
        double limit = context.getLimit();
        double ratio = context.getAmount() / limit;
        if (ratio >= 1.0) {
            context.setState(new ExceededState());
        } else if (ratio < 0.8) {
            context.setState(new NormalState());
        }
    }

    @Override
    public String getStatusName() {
        return "OSTRZEŻENIE (wydano 80% z założonego budrzetu)!";
    }

    @Override
    public String getColorCode() {
        return "\u001B[33m";
    }

}
