package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.element.FileElement;
import ru.cft.drozdrtskiy22.merge_files_sorting.supplier.FileElementSupplier;
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

    private final Comparator<FileElement> comparator;
    private final FileElementSupplierFactory fileElementSupplierFactory;
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
        this.fileElementSupplierFactory = FileElementSupplierFactory.withElementType(arguments.getElementType());
        this.outputFile = arguments.getOutputFile();
        this.inputFiles = arguments.getInputFiles();
        tempFiles = new ArrayList<>();
    }

    public void sortFiles() throws Exception {
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
        try (FileElementSupplier supplier = fileElementSupplierFactory.createForFile(inputFiles.get(0));
             BufferedWriter fileWriter = Files.newBufferedWriter(outputFile)) {

            for (FileElement e = supplier.next(); e != null; e = supplier.next()) {
                fileWriter.write(e.toWritableFormat());
            }
        }
    }

    private Path sortTwoFilesWithMergeAndSaveResultToFile(Path firstFile, Path secondFile) throws Exception {
        Path tempFile = Paths.get(UUID.randomUUID().toString() + ".tmp");
        tempFiles.add(tempFile);

        try (BufferedWriter fileWriter = Files.newBufferedWriter(tempFile);
             FileElementSupplier firstSupplier = fileElementSupplierFactory.createForFile(firstFile);
             FileElementSupplier secondSupplier = fileElementSupplierFactory.createForFile(secondFile)) {

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