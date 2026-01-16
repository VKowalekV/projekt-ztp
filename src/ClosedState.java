public class ClosedState implements BudgetLifecycleState {
    @Override
    public void addCategory(BudgetMonth context, String parentName, String name, double limit) {
        System.out.println("Błąd: Budżet jest zamknięty (brak możliwości modyfikacji kategorii).");
    }

    @Override
    public void addTransaction(BudgetMonth context, String categoryName, double amount, String desc) {
        System.out.println("Błąd: Bużdet jest w zamknięty (bark możliwości dodania nowych tranzakcji).");
    }

    @Override
    public void exportData(BudgetMonth context, ExporterCreator creator) {
        creator.performExport(context.getRootCategory(), context);
    }

    @Override
    public void nextState(BudgetMonth context) {
        System.out.println("Budżet został zamknięty.");
    }

    @Override
    public String getStateName() {
        return "ZAMKNIĘTY BUDŻET";
    }
}
