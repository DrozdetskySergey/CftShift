package ru.cft.drozdrtskiy.sorting;

import ru.cft.drozdrtskiy.sorting.args.ArgsException;
import ru.cft.drozdrtskiy.sorting.args.file.FileElementSorterArgs;
import ru.cft.drozdrtskiy.sorting.sorter.Sorter;
import ru.cft.drozdrtskiy.sorting.sorter.file.FileSorterByMerge;

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
            FileElementSorterArgs fileElementSorterArgs = new FileElementSorterArgs(args);
            Sorter sorter = new FileSorterByMerge(fileElementSorterArgs.getDTO());
            sorter.sort();
        } catch (ArgsException e) {
            print(e.getMessage());
            print(Message.FILE_SORT_PARAMS);
        }
    }
}
