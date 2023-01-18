package ru.cft.drozdrtskiy.sorting.argument.file;

enum Key {

    ASCENDING_ORDER("-a"),
    DESCENDING_ORDER("-d"),
    STRING_TYPE("-s"),
    INTEGER_TYPE("-i"),
    OVERWRITE_OUTPUT_FILE("-w");

    private final String notation;

    Key(String notation) {
        this.notation = notation;
    }

    public String notation() {
        return notation;
    }
}
