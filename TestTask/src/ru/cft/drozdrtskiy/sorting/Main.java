package ru.cft.drozdrtskiy.sorting;

import ru.cft.drozdrtskiy.sorting.argument.ArgsException;
import ru.cft.drozdrtskiy.sorting.argument.file.FileSorterArguments;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.impl.FileSorterByMerge;
import ru.cft.drozdrtskiy.sorting.util.*;

public final class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            MessagePrinter.print(Message.FILE_SORT_INFO);
            MessagePrinter.print(Message.FILE_SORT_PARAMS);
            MessagePrinter.print(Message.FILE_SORT_KEYS);

            return;
        }

        try {
            FileSorterArguments fileSorterArguments = new FileSorterArguments(args);
            Sorter sorter = new FileSorterByMerge(fileSorterArguments.getDTO());
            sorter.sort();
        } catch (ArgsException e) {
            MessagePrinter.print(e.getMessage());
            MessagePrinter.print(Message.FILE_SORT_PARAMS);
        }
    }
}
