public class ActiveState implements BudgetLifecycleState {
    @Override
    public void addCategory(BudgetMonth context, String parentName, String name, double limit){
        System.out.println("Błąd: Budżet jest aktywny (brak możliwości modyfikacji kategorii).");
    }

    @Override
    public void addTransaction(BudgetMonth context, String categoryName, double amount, String desc){
        context.performAddTransaction(categoryName, amount, desc);
    }

    @Override
    public void nextState(BudgetMonth context){
        context.setLifecycleState(new ClosedState());
        System.out.println("Budżet został zamknięty.");
    }

    @Override
    public String getStateName() {
        return "AKTYWNY BUDŻET";
    }
}
