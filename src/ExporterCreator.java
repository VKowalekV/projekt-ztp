abstract class ExporterCreator {

    protected abstract FileExporter createExporter();

    public void performExport(BudgetComponent root) {
        FileExporter exporter = createExporter();

        exporter.export(root);
    }
}
