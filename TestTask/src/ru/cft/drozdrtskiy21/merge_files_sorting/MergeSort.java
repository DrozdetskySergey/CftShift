package ru.cft.drozdrtskiy21.merge_files_sorting;

import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;

public class MergeSort {
    public static void mergeSort(Writer writer, ElementsGetterFromReaders elements, Comparator<Object> comparator) {
        int readersCount = elements.getReadersCount();

        while (readersCount > 0) {
            int nextElementIndex = 0;

            for (int i = 1; i < readersCount; i++) {
                if (comparator.compare(elements.getElement(nextElementIndex), elements.getElement(i)) > 0) {
                    nextElementIndex = i;
                }
            }

            Object writableElement = elements.getElement(nextElementIndex);

            try {
                writer.write(String.format("%s%n", writableElement));
            } catch (IOException e) {
                System.out.println("Problem with Writer!");

                return;
            }

            boolean hasNextElementByIndex = elements.tryUpdateElement(nextElementIndex);

            if (!hasNextElementByIndex) {
                readersCount = elements.getReadersCount(); // = readersCount - 1
            } else {
                while (comparator.compare(writableElement, elements.getElement(nextElementIndex)) > 0) {
                    hasNextElementByIndex = elements.tryUpdateElement(nextElementIndex); // next element is not sorted -> update again

                    if (!hasNextElementByIndex) {
                        readersCount = elements.getReadersCount();

                        break;
                    }
                }
            }
        }
    }
}
