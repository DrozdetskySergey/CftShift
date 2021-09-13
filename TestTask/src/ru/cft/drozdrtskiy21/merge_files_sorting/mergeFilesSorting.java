package ru.cft.drozdrtskiy21.merge_files_sorting;

import com.sun.jdi.InvalidTypeException;

import java.io.*;
import java.util.*;

public class mergeFilesSorting {
    public static void main(String[] args) {
        //TODO read args

        boolean isAscend = true;
        ElementType type = ElementType.INTEGER;

        String outputFileName = "out.txt";

        List<String> inputFilesNames = new ArrayList<>();
        inputFilesNames.add("in1.txt");
        inputFilesNames.add("in2.txt");
        inputFilesNames.add("in3.txt");

        List<BufferedReader> readers = new ArrayList<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String s : inputFilesNames) {
                readers.add(new BufferedReader(new FileReader(s)));
            }

            mergeSort(writer, new ElementReader(new ArrayList<>(readers), type), getComparator(isAscend, type));

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Problem with input or output file!");
        } catch (InvalidTypeException e) {
            System.out.println("Unknown type of elements!");
        } finally {
            try {
                for (BufferedReader reader : readers) {
                    if (reader != null) {
                        reader.close();
                    }
                }
            } catch (IOException ignored) {
            }
        }
    }

    private static void mergeSort(BufferedWriter writer, ElementReader elements, Comparator<Object> comparator) throws InvalidTypeException {
        int inputFilesCount = elements.getSize();

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
                System.out.println("Problem with output file!");

                return;
            }

            boolean hasNextElementForIndex = elements.isUpdatedElement(writableElementIndex);

            if (!hasNextElementForIndex) {
                inputFilesCount = elements.getSize();
            } else {
                while (comparator.compare(writableElement, elements.getElement(writableElementIndex)) > 0) {
                    hasNextElementForIndex = elements.isUpdatedElement(writableElementIndex);

                    if (!hasNextElementForIndex) {
                        inputFilesCount = elements.getSize();

                        break;
                    }
                }
            }
        }
    }

    private static Comparator<Object> getComparator(boolean isAscend, ElementType type) throws InvalidTypeException {
        if (isAscend) {
            switch (type) {
                case INTEGER -> {
                    return Comparator.comparing(o -> ((Integer) o));
                }
                case STRING -> {
                    return Comparator.comparing(o -> ((String) o));
                }
                default -> throw new InvalidTypeException();
            }
        } else {
            switch (type) {
                case INTEGER -> {
                    return (o1, o2) -> ((Integer) o2).compareTo((Integer) o1);
                }
                case STRING -> {
                    return (o1, o2) -> ((String) o2).compareTo((String) o1);
                }
                default -> throw new InvalidTypeException();
            }
        }
    }
}
