package ru.cft.drozdrtskiy.sorting.util;

public enum Message {

    FILE_SORT_INFO(String.format(
            "Программа сортирует несколько файлов слиянием. Входные файлы содержат данные одного из двух видов:%n" +
            "String или Integer. Данные записаны в столбик (каждая строка файла – новый элемент).%n" +
            "Строки могут содержать любые не пробельные символы, строки с пробелами считаются ошибочными.%n" +
            "Также считается, что файлы предварительно отсортированы.%n")),
    FILE_SORT_PARAMS("Параметры: (-a | -d) [-s | -i] (-w) (-n) [имя выходного файла] [имя входного файла №1].."),
    FILE_SORT_KEYS(String.format(
            "  -a  сортировка по возростанию (по умолчанию).%n" +
            "  -d  сортировка по убыванию.%n" +
            "  -s  данные в виде String.%n" +
            "  -i  данные в виде Integer.%n" +
            "  -w  можно перезаписывать выходной файл.%n" +
            "  -n  пропускать элементы нарушающие сортировку.%n"));

    private final String text;

    Message(String text) {
        this.text = text;
    }

    public void show() {
        Writer.write(text);
    }
}
