public class DraftState implements BudgetLifecycleState {
    @Override
    public void addCategory(BudgetMonth context, String parentName, String name, double limit) {
        context.performAddSubCategory(parentName, name, limit);
    }

    @Override
    public void addTransaction(BudgetMonth context, String categoryName, double amount, String desc) {
        System.out.println("Błąd: Bużdet jest w procesie planowania (bark możliwości dodania nowych tranzakcji).");
    }

    @Override
    public void exportData(BudgetMonth context, ExporterCreator creator) {
        System.out.println("Błąd: Budżet jest w trakcie planowania (eksport możliwy tylko po zamknięciu miesiąca).");
    }

    @Override
    public void nextState(BudgetMonth context) {
        context.setLifecycleState(new ActiveState());
        System.out.println("Budżet przechodzi w stan aktywny.");
    }

    @Override
    public String getStateName() {
        return "PLANOWANIE BUDŻETU";
    }
}
