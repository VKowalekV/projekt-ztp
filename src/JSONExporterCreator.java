public class JSONExporterCreator extends ExporterCreator {
    @Override
    protected FileExporter createExporter() {
        return new JSONExporter();
    }
}
