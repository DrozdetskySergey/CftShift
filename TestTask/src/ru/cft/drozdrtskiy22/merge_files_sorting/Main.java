package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.argument.Args;
import ru.cft.drozdrtskiy22.merge_files_sorting.argument.ArgsException;
import ru.cft.drozdrtskiy22.merge_files_sorting.message.Message;

public final class Main {

    public static void main(String[] args) {
        args = new String[]{"-w", "-i", "out.txt", "in1.txt", "in2.txt", "in3.txt"};

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

        try (SorterFilesByMerge sorter = SorterFilesByMerge.withArguments(arguments)) {
            sorter.sortFiles();
            System.out.printf("Результат в файле: %s%n", arguments.getOutputFile().toAbsolutePath());
        } catch (Exception e) {
            System.out.printf("Что-то пошло не так. %s%n", e.getMessage());
        }
    }
}
