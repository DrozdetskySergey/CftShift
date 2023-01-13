package ru.cft.drozdrtskiy22.merge_files_sorting;

import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.Args;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.args.ArgsException;
import ru.cft.drozdrtskiy22.merge_files_sorting.utility.message.Message;

public final class Main {

    public static void main(String[] args) {
        args = new String[]{"-a", "-i", "out.txt", "in1.txt", "in2.txt", "in3.txt"};

        if (args.length < 3) {
            Message.INFO.show();
            Message.PARAMS.show();
            Message.KEYS.show();

            return;
        }

        Args arguments;

        try {
            arguments = Args.fromArray(args);
            checkExistenceFiles();
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            Message.PARAMS.show();

            return;
        }

        try (MergeFilesSort mergeFilesSort = MergeFilesSort.withArguments(arguments)) {
            mergeFilesSort.sort();
            System.out.printf("Результат в файле: \"%s\"%n", arguments.getOutputFile().toAbsolutePath());
        } catch (Exception e) {
            System.out.println("Работа с файлами. Что-то пошло не так." + e.getMessage());
        }
    }

    private static void checkExistenceFiles() throws ArgsException {
        //TODO check
    }
}
