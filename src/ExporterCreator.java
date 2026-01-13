abstract class ExporterCreator {

    protected abstract FileExporter createExporter();

    public void performExport(BudgetComponent root, BudgetMonth currentMonth) {
        FileExporter exporter = createExporter();

        exporter.export(root, currentMonth);
    }
}
