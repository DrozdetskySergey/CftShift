package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.Element;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.ElementSupplier;
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

public final class SorterFilesByMerge implements AutoCloseable {

    private final Comparator<Element> comparator;
    private final FileElementSupplierDispatcher supplierDispatcher;
    private final Path outputFile;
    private final List<Path> inputFiles;
    private final List<Path> tempFiles;

    public static SorterFilesByMerge withArguments(Args arguments) {
        return new SorterFilesByMerge(arguments);
    }

    private SorterFilesByMerge(Args arguments) {
        this.comparator = arguments.getSortDirection() == SortDirection.DESC ?
                Comparator.reverseOrder() :
                Comparator.naturalOrder();
        this.supplierDispatcher = FileElementSupplierDispatcher.forElementType(arguments.getElementType());
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
            resultFile = sortTwoFilesWithMergeAndSaveResultToFile(resultFile, inputFiles.get(i));
        }

        Files.copy(resultFile, outputFile, StandardCopyOption.REPLACE_EXISTING);
    }

    private void saveFirstInputFileToOutputFile() throws Exception {
        try (ElementSupplier supplier = supplierDispatcher.createWithFile(inputFiles.get(0));
             BufferedWriter fileWriter = Files.newBufferedWriter(outputFile)) {

            for (Element e = supplier.next(); e != null; e = supplier.next()) {
                fileWriter.write(e.toWritableFormat());
            }
        }
    }

    private Path sortTwoFilesWithMergeAndSaveResultToFile(Path firstFile, Path secondFile) throws Exception {
        Path tempFile = Paths.get(UUID.randomUUID().toString() + ".tmp");
        tempFiles.add(tempFile);

        try (BufferedWriter fileWriter = Files.newBufferedWriter(tempFile);
             ElementSupplier firstSupplier = supplierDispatcher.createWithFile(firstFile);
             ElementSupplier secondSupplier = supplierDispatcher.createWithFile(secondFile)) {

            Element alfa = firstSupplier.next();
            Element beta = secondSupplier.next();

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
    public void close() {
        for (Path p : tempFiles) {
            try {
                Files.deleteIfExists(p);
            } catch (IOException e) {
                System.out.printf("Удаление временного файла %s Что-то пошло не так. %s%n", p.getFileName(), e.getMessage());
            }
        }
    }
}