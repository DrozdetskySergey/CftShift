package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.FileElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.FileElementSupplierFactory;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.Args;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.SortDirection;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MergeFilesSort implements AutoCloseable {

    private final Comparator<FileElement> comparator;
    private final FileElementSupplierFactory supplierFactory;
    private final Path outputFile;
    private final List<Path> inputFiles;
    private final List<Path> tempFiles;

    public static MergeFilesSort withArguments(Args arguments) {
        return new MergeFilesSort(arguments);
    }

    private MergeFilesSort(Args arguments) {
        this.comparator = arguments.getSortDirection() == SortDirection.DESC ?
                Comparator.reverseOrder() :
                Comparator.naturalOrder();
        this.supplierFactory = FileElementSupplierFactory.forElementType(arguments.getElementType());
        this.outputFile = arguments.getOutputFile();
        this.inputFiles = arguments.getInputFiles();
        tempFiles = new ArrayList<>();
    }

    public void sort() throws Exception {
        if (inputFiles.size() == 1) {
            saveFirstInputFileToOutputFile();

            return;
        }

        Path resultFile = inputFiles.get(0);

        for (int i = 1; i < inputFiles.size(); i++) {
            resultFile = sortTwoFilesWithMergeAndSaveToFile(resultFile, inputFiles.get(i));
        }

        Files.copy(resultFile, outputFile, StandardCopyOption.REPLACE_EXISTING);
    }

    private void saveFirstInputFileToOutputFile() throws Exception {
        try (FileElementSupplier supplier = supplierFactory.get(inputFiles.get(0));
             BufferedWriter fileWriter = Files.newBufferedWriter(outputFile)) {

            for (FileElement fileElement = supplier.next(); fileElement != null; fileElement = supplier.next()) {
                fileWriter.write(fileElement.toWritableFormat());
            }
        }
    }

    private Path sortTwoFilesWithMergeAndSaveToFile(Path firstFile, Path secondFile) throws Exception {
        Path tempFile = Paths.get(UUID.randomUUID().toString() + ".tmp");
        tempFiles.add(tempFile);

        try (BufferedWriter fileWriter = Files.newBufferedWriter(tempFile);
             FileElementSupplier firstSupplier = supplierFactory.get(firstFile);
             FileElementSupplier secondSupplier = supplierFactory.get(secondFile)) {

            FileElement alfa = firstSupplier.next();
            FileElement beta = secondSupplier.next();

            while (alfa != null || beta != null) {
                if (alfa == null) {
                    fileWriter.write(beta.toWritableFormat());
                    beta = secondSupplier.next();
                } else if (beta == null || comparator.compare(alfa, beta) < 0) {
                    fileWriter.write(alfa.toWritableFormat());
                    alfa = firstSupplier.next();
                } else {
                    fileWriter.write(beta.toWritableFormat());
                    beta = secondSupplier.next();
                }
            }
        }

        return tempFile;
    }

    @Override
    public void close() throws IOException {
        for (Path p : tempFiles) {
            Files.deleteIfExists(p);
        }
    }
}