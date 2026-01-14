import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemLogger implements BudgetObserver{
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void update(double totalExpenses, double totalIncome){
        double balance = totalIncome - totalExpenses;
        String now = LocalDateTime.now().format(FMT);
        System.out.println(String.format("[%s] System zaktualizowany. Przychody: %.2f, Wydatki: %.2f, Saldo: %.2f", now, totalIncome, totalExpenses, balance));
    }
}