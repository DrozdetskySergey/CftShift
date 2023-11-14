package ru.cft.drozdrtskiy.sorting;

import ru.cft.drozdrtskiy.sorting.argument.ArgsException;
import ru.cft.drozdrtskiy.sorting.argument.file.FileSorterArguments;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.impl.FileSorterByMerge;

import static ru.cft.drozdrtskiy.sorting.util.MessagePrinter.print;

public final class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            print(Message.FILE_SORT_INFO);
            print(Message.FILE_SORT_PARAMS);
            print(Message.FILE_SORT_KEYS);

            return;
        }

        try {
            FileSorterArguments fileSorterArguments = new FileSorterArguments(args);
            Sorter sorter = new FileSorterByMerge(fileSorterArguments.getDTO());
            sorter.sort();
        } catch (ArgsException e) {
            print(e.getMessage());
            print(Message.FILE_SORT_PARAMS);
        }
    }
}
