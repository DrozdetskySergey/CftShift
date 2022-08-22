package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.FileElementSupplier;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.SupplierFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MergeFilesSort implements AutoCloseable {

    private final Comparator<FileElement> comparator;
    private final SupplierFactory supplierFactory;
    private final Path outputFile;
    private final List<Path> inputFiles;
    private final List<Path> tempFiles;

    public MergeFilesSort(Comparator<FileElement> comparator, SupplierFactory supplierFactory, Path outputFile, List<Path> inputFiles) {
        this.comparator = comparator;
        this.supplierFactory = supplierFactory;
        this.outputFile = outputFile;
        this.inputFiles = inputFiles;
        tempFiles = new ArrayList<>();
    }

    public void sort() throws Exception {
        Path alfaFile = inputFiles.get(0);

        if (inputFiles.size() == 1) {
            try (FileElementSupplier supplier = supplierFactory.get(alfaFile);
                 BufferedWriter fileWriter = Files.newBufferedWriter(outputFile)) {

                for (FileElement fileElement = supplier.next(); fileElement != null; fileElement = supplier.next()) {
                    fileWriter.write(fileElement.toWritableFormat());
                }
            }

            return;
        }

        for (int i = 1; i < inputFiles.size(); i++) {
            try (FileElementSupplier alfaSupplier = supplierFactory.get(alfaFile);
                 FileElementSupplier betaSupplier = supplierFactory.get(inputFiles.get(i))) {

                alfaFile = sortTwoFilesWithMerge(alfaSupplier, betaSupplier);
            }
        }

        Files.copy(alfaFile, outputFile, StandardCopyOption.REPLACE_EXISTING);
    }

    private Path sortTwoFilesWithMerge(FileElementSupplier alfaSupplier, FileElementSupplier betaSupplier) throws IOException {
        Path tempFile = Paths.get(UUID.randomUUID().toString() + ".tmp");
        tempFiles.add(tempFile);

        try (BufferedWriter fileWriter = Files.newBufferedWriter(tempFile)) {

            FileElement alfaSupplierElement = alfaSupplier.next();
            FileElement betaSupplierElement = betaSupplier.next();

            while (alfaSupplierElement != null || betaSupplierElement != null) {
                if (alfaSupplierElement == null) {
                    fileWriter.write(betaSupplierElement.toWritableFormat());
                    betaSupplierElement = betaSupplier.next();
                } else if (betaSupplierElement == null) {
                    fileWriter.write(alfaSupplierElement.toWritableFormat());
                    alfaSupplierElement = alfaSupplier.next();
                } else if (comparator.compare(alfaSupplierElement, betaSupplierElement) < 0) {
                    fileWriter.write(alfaSupplierElement.toWritableFormat());
                    alfaSupplierElement = alfaSupplier.next();
                } else {
                    fileWriter.write(betaSupplierElement.toWritableFormat());
                    betaSupplierElement = betaSupplier.next();
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