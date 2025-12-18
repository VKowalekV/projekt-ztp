import java.time.LocalDate;

public class Transaction implements BudgetComponent {
    private String name;
    private double amount;
    private LocalDate date;

    public Transaction(double amount, String name, LocalDate date) {
        this.amount = amount;
        this.name = name;
        this.date = date;
    }

    @Override
    public double getAmount() {
        return this.amount;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getType() {
        return "TRANSACTION";
    }

    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public void add(BudgetComponent c) {
        throw new UnsupportedOperationException("Nie można dodać elementu do transakcji.");
    }

    @Override
    public void remove (BudgetComponent c) {
        throw new UnsupportedOperationException("Nie można usunąć elementu z transakcji.");
    }

    @Override
    public BudgetComponent getChild(String name) {
        return null;
    }
}
