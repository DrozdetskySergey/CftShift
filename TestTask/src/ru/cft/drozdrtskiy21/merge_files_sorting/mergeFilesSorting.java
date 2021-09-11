package ru.cft.drozdrtskiy21.merge_files_sorting;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class mergeFilesSorting {
    public static void main(String[] args) {
        //TODO read args

        boolean isAscend = false;
        boolean isNumbers = false;

        String outputFileName = "out.txt";

        List<String> inputFilesNames = new ArrayList<>();
        inputFilesNames.add("in1.txt");
        inputFilesNames.add("in2.txt");
        inputFilesNames.add("in3.txt");

        Comparator<String> stringComparator;
        Comparator<Integer> integerComparator;

        if (isAscend) {
            stringComparator = Comparator.naturalOrder();
            integerComparator = Comparator.naturalOrder();
        } else {
            stringComparator = Comparator.reverseOrder();
            integerComparator = Comparator.reverseOrder();
        }

        List<BufferedReader> readers = new ArrayList<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String s : inputFilesNames) {
                readers.add(new BufferedReader(new FileReader(s)));
            }

            List<BufferedReader> noEmptyReaders = new ArrayList<>(readers);

            if (isNumbers) {
                mergeSort(writer, noEmptyReaders, integerComparator, true);
            } else {
                mergeSort(writer, noEmptyReaders, stringComparator, false);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
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

    private static <T> void mergeSort(BufferedWriter writer, List<BufferedReader> readers, Comparator<T> comparator, boolean isNumber) {
        List<T> elements = new ArrayList<>();
        int readersCount = readers.size();
        boolean isHasNull = false;

        for (BufferedReader r : readers) {
            try {
                String nextLine = r.readLine();

                if (isNumber && nextLine != null) {
                    elements.add((T) Integer.valueOf(nextLine));
                } else {
                    elements.add((T) nextLine);
                }

                if (nextLine == null) {
                    isHasNull = true;
                }
            } catch (Exception e) {
                elements.add(null);
                isHasNull = true;
            }
        }

        if (isHasNull) {
            for (int i = 0; i < readersCount; i++) {
                if (elements.get(i) == null) {
                    readers.set(i, null);
                }
            }
        }

        while (readersCount > 0) {
            if (isHasNull) {
                elements = elements.stream().filter(Objects::nonNull).collect(Collectors.toList());
                readers = readers.stream().filter(Objects::nonNull).collect(Collectors.toList());

                readersCount = readers.size();
                isHasNull = false;

                continue;
            }


            int writableElementIndex = 0;

            for (int i = 1; i < readersCount; i++) {
                if (comparator.compare(elements.get(writableElementIndex), elements.get(i)) > 0) {
                    writableElementIndex = i;
                }
            }

            T writableElement = elements.get(writableElementIndex);

            try {
                writer.write(String.format("%s%n", writableElement));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return;
            }

            T nextReadableElement;

            try {
                String nextLine = readers.get(writableElementIndex).readLine();

                if (isNumber && nextLine != null) {
                    nextReadableElement = (T) Integer.valueOf(nextLine);
                } else {
                    nextReadableElement = (T) nextLine;
                }
            } catch (IOException e) {
                nextReadableElement = null;
            }

            if (nextReadableElement == null || comparator.compare(writableElement, nextReadableElement) > 0) {
                readers.set(writableElementIndex, null);
                elements.set(writableElementIndex, null);
                isHasNull = true;
            } else {
                elements.set(writableElementIndex, nextReadableElement);
            }
        }
    }
}
