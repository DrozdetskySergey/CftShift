package ru.cft.drozdrtskiy.sorting.argument.file;

enum Key {

    ASCENDING_ORDER("-a"),
    DESCENDING_ORDER("-d"),
    STRING_TYPE("-s"),
    INTEGER_TYPE("-i"),
    OVERWRITE_OUTPUT_FILE("-w"),
    IGNORE_UNSORTED("-n");

    final String key;

    Key(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
