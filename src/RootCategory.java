public class RootCategory extends Category {

  public RootCategory() {
    super("calkowity budzet", 0.0);
  }

  @Override
  public double getLimit() {
    double totalLimit = 0;
    for (BudgetComponent child : getChildren()) {
      if (child instanceof Category) {
        totalLimit += ((Category) child).getLimit();
      }
    }
    return totalLimit;
  }

  @Override
  public String getName() {
    return "calkowity budzet";
  }
}
