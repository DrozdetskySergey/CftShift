package ru.cft.drozdrtskiy22.merge_files_sorting.argument;

enum KeyList {

    ASCENDING_ORDER("-a"),
    DESCENDING_ORDER("-d"),
    STRING_TYPE("-s"),
    INTEGER_TYPE("-i"),
    OVERWRITE_OUTPUT_FILE("-w");

    private final String key;

    KeyList(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
