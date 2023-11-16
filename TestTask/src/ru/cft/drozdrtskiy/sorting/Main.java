package ru.cft.drozdrtskiy.sorting;

import ru.cft.drozdrtskiy.sorting.args.ArgsException;
import ru.cft.drozdrtskiy.sorting.args.file.FileElementSorterArgs;
import ru.cft.drozdrtskiy.sorting.sorter.ElementSorter;
import ru.cft.drozdrtskiy.sorting.sorter.file.FileElementSorter;

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
            ElementSorter elementSorter = new FileElementSorter(fileElementSorterArgs.getDTO());
            elementSorter.sort();
        } catch (ArgsException e) {
            print(e.getMessage());
            print(Message.FILE_SORT_PARAMS);
        }
    }
}
