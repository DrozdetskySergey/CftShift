package ru.cft.drozdrtskiy.sorting.args.file;

enum Key {

    ASCENDING_ORDER("a"),
    DESCENDING_ORDER("d"),
    STRING_TYPE("s"),
    INTEGER_TYPE("i"),
    OVERWRITE_OUTPUT_FILE("-OOF"),
    IGNORE_UNSORTED("-IU");

    final String key;

    Key(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
