package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.Args;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.ArgsException;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.message.Message;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Main {

    public static void main(String[] args) {
        args = new String[]{"", "-i", "out.txt", "in1.txt", "in2.txt", "in3.txt"};

        if (args.length < 3) {
            Message.INFO.show();
            Message.PARAMS.show();
            Message.KEYS.show();

            return;
        }

        Args arguments;

        try {
            arguments = Args.fromArray(args);
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            Message.PARAMS.show();

            return;
        }

        for (Path p : arguments.getInputFiles()) {
            if (!Files.isReadable(p)) {
                System.out.printf("Входящий файл %s недоступен для чтения.", p.getFileName());

                return;
            }
        }

        if (Files.exists(arguments.getOutputFile())) {
            System.out.printf("Файл %s уже существует.%n", arguments.getOutputFile().getFileName());

            return;
        }

        try (SorterFilesByMerge sorter = SorterFilesByMerge.withArguments(arguments)) {
            sorter.sortFiles();
            System.out.printf("Результат в файле: %s%n", arguments.getOutputFile().toAbsolutePath());
        } catch (Exception e) {
            System.out.printf("Что-то пошло не так. %s%n", e.getMessage());
        }
    }
}
