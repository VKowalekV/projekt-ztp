public class BudgetManager {
    private ExporterCreator exporterFactory;
    private BudgetComponent rootCategory;

    public void exportData(ExporterCreator creator){
        creator.performExport(rootCategory);
    }
}
