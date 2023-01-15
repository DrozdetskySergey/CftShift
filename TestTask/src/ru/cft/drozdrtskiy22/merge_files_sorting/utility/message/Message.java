package ru.cft.drozdrtskiy22.merge_files_sorting.utility.message;

public enum Message {

    INFO(String.format(
            "Программа сортирует несколько файлов слиянием. Входные файлы содержат данные одного из двух видов:%n" +
            "String или Integer. Данные записаны в столбик (каждая строка файла – новый элемент).%n" +
            "Строки могут содержать любые не пробельные символы, строки с пробелами считаются ошибочными.%n" +
            "Также считается, что файлы предварительно отсортированы.%n")),
    KEYS(String.format(
            "    -a  сортировка по возростанию (по умолчанию).%n" +
            "    -d  сортировка по убыванию.%n" +
            "    -s  данные в виде String.%n" +
            "    -i  данные в виде Integer.%n" +
            "    -r  перезаписывать файл с результатом.%n")),
    PARAMS("Параметры: (-r) (-a | -d) [-s | -i] [имя файла для результата] [имя входного файла №1]..");

    private final String text;

    Message(String text) {
        this.text = text;
    }

    public void show() {
        System.out.println(text);
    }
}
