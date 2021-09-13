package ru.cft.drozdrtskiy21.merge_files_sorting;

import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;

public class MergeSort {
    public static void sort(Writer writer, ElementsStreamFromReaders elements, Comparator<Object> comparator) {
        int inputFilesCount = elements.getReadersCount();

        while (inputFilesCount > 0) {
            int writableElementIndex = 0;

            for (int i = 1; i < inputFilesCount; i++) {
                if (comparator.compare(elements.getElement(writableElementIndex), elements.getElement(i)) > 0) {
                    writableElementIndex = i;
                }
            }

            Object writableElement = elements.getElement(writableElementIndex);

            try {
                writer.write(String.format("%s%n", writableElement));
            } catch (IOException e) {
                System.out.println("Problem with Writer!");

                return;
            }

            boolean hasNextElementByIndex = elements.isUpdatedElement(writableElementIndex);

            if (!hasNextElementByIndex) {
                inputFilesCount = elements.getReadersCount();
            } else {
                while (comparator.compare(writableElement, elements.getElement(writableElementIndex)) > 0) {
                    hasNextElementByIndex = elements.isUpdatedElement(writableElementIndex);

                    if (!hasNextElementByIndex) {
                        inputFilesCount = elements.getReadersCount();

                        break;
                    }
                }
            }
        }
    }
}
