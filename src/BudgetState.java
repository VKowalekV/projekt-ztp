public interface BudgetState {
    void handleState(Category context);
    String getStatusName();
    String getColorCode();
}
