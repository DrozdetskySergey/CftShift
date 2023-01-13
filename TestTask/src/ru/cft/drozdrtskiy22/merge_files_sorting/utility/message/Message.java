package ru.cft.drozdrtskiy22.merge_files_sorting.utility.message;

public enum Message {

    INFO("Программа сортирует несколько файлов слиянием. Входные файлы содержат данные одного из двух видов: " +
            "String или Integer. Данные записаны в столбик (каждая строка файла – новый элемент). Строки могут " +
            "содержать любые не пробельные символы, строки с пробелами считаются ошибочными. Также считается, что " +
            "файлы предварительно отсортированы."),
    KEYS("-a  сортировка по возростанию. -d  сортировка по убыванию. " +
            "-s данные в виде String. -i данные в виде Integer."),
    PARAMS("Параметры: (-a | -d) [-s | -i] [имя файла с результатом] [имя входного файла №1]..");

    private final String text;

    Message(String text) {
        this.text = text;
    }

    public void show() {
        System.out.println(text);
    }
}
