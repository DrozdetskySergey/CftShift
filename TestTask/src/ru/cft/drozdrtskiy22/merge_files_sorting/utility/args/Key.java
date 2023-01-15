package ru.cft.drozdrtskiy22.merge_files_sorting.utility.args;

enum Key {

    ASCENDING_ORDER("-a"),
    DESCENDING_ORDER("-d"),
    STRING_TYPE("-s"),
    INTEGER_TYPE("-i");

    private final String sign;

    Key(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
