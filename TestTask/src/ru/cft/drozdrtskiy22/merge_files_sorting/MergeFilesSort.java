package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.FileElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.IntegerFileElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.StringFileElementSupplier;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MergeFilesSort implements AutoCloseable {

    private final Comparator<FileElement> comparator;
    private final ElementType elementType;
    private final Path outputFile;
    private final List<Path> inputFiles;
    private final List<Path> tempFiles;

    public MergeFilesSort(SortDirection sortDirection, ElementType elementType, Path outputFile, List<Path> inputFiles) {
        comparator = sortDirection == SortDirection.DESC ? Comparator.reverseOrder() : Comparator.naturalOrder();

        this.elementType = elementType;
        this.outputFile = outputFile;
        this.inputFiles = inputFiles;
        tempFiles = new ArrayList<>();
    }

    private FileElementSupplier getSupplier(Path path) throws IOException {
        if (elementType == ElementType.INTEGER) {

            return new IntegerFileElementSupplier(path, comparator);
        }

        return new StringFileElementSupplier(path, comparator);
    }

    public void sort() throws Exception {
        if (inputFiles.size() == 1) {
            Files.copy(inputFiles.get(0), outputFile);

            return;
        }

        FileElementSupplier AlfaSupplier = getSupplier(inputFiles.get(0));

        for (int i = 1; i < inputFiles.size(); i++) {
            FileElementSupplier BetaSupplier = getSupplier(inputFiles.get(i));
            Path tempFile = sortTwoFilesWithMerge(AlfaSupplier, BetaSupplier);
            AlfaSupplier.close();
            BetaSupplier.close();
            AlfaSupplier = getSupplier(tempFile);
        }

        AlfaSupplier.close();

        Files.copy(tempFiles.get(tempFiles.size() - 1), outputFile, StandardCopyOption.REPLACE_EXISTING);
    }

    private Path sortTwoFilesWithMerge(FileElementSupplier AlfaSupplier, FileElementSupplier BetaSupplier) throws IOException {
        Path tempFile = Paths.get(UUID.randomUUID().toString() + ".tmp");
        tempFiles.add(tempFile);

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {

            FileElement AlfaElement = AlfaSupplier.next();
            FileElement BetaElement = BetaSupplier.next();

            while (AlfaElement != null || BetaElement != null) {
                if (AlfaElement == null) {
                    writer.write(BetaElement.toWritableFormat());
                    BetaElement = BetaSupplier.next();
                } else if (BetaElement == null) {
                    writer.write(AlfaElement.toWritableFormat());
                    AlfaElement = AlfaSupplier.next();
                } else if (comparator.compare(AlfaElement, BetaElement) < 0) {
                    writer.write(AlfaElement.toWritableFormat());
                    AlfaElement = AlfaSupplier.next();
                } else {
                    writer.write(BetaElement.toWritableFormat());
                    BetaElement = BetaSupplier.next();
                }
            }
        }

        return tempFile;
    }

    @Override
    public void close() throws IOException {
        for (Path f : tempFiles) {
            Files.deleteIfExists(f);
        }
    }
}