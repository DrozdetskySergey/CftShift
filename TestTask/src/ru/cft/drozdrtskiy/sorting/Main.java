package ru.cft.drozdrtskiy.sorting;

import ru.cft.drozdrtskiy.sorting.argument.ArgsException;
import ru.cft.drozdrtskiy.sorting.argument.file.FileSorterArguments;
import ru.cft.drozdrtskiy.sorting.message.Message;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.file.merge.FileSorterByMerge;

public final class Main {

    public static void main(String[] args) {
        args = new String[]{"", "-w", "", "-i", "out.txt", "in1.txt", "in2.txt", "in3.txt"};

        if (args.length <= 2) {
            Message.FILE_SORT_INFO.show();
            Message.FILE_SORT_PARAMS.show();
            Message.FILE_SORT_KEYS.show();

            return;
        }

        FileSorterArguments fileSorterArguments;

        try {
            fileSorterArguments = FileSorterArguments.from(args);
        } catch (ArgsException e) {
            System.out.println(e.getMessage());
            Message.FILE_SORT_PARAMS.show();

            return;
        }

        Sorter sorter = FileSorterByMerge.from(fileSorterArguments);

        try {
            sorter.sort();
        } catch (Exception e) {
            System.out.printf("Что-то пошло не так. %s%n", e.getMessage());
        }
    }
}
