package ru.cft.drozdrtskiy21.merge_files_sorting;

public class StringFormatException extends IllegalArgumentException {
    @Override
    public void printStackTrace() {
        System.err.println("Invalid characters.");
    }
}
