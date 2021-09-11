package ru.cft.drozdrtskiy21.merge_files_sorting;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class mergeFilesSorting {
    public static void main(String[] args) {
        //TODO read args

        boolean isAscend = true;
        boolean isInteger = true;

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

            List<BufferedReader> noEmptyReaders = new ArrayList<>(readers);

            if (isAscend) {
                if (isInteger) {
                    mergeSort(writer, noEmptyReaders, Comparator.comparing(o -> ((Integer) o)), true);
                } else {
                    mergeSort(writer, noEmptyReaders, Comparator.comparing(o -> ((String) o)), false);
                }
            } else {
                if (isInteger) {
                    mergeSort(writer, noEmptyReaders, (o1, o2) -> ((Integer) o2).compareTo((Integer) o1), true);
                } else {
                    mergeSort(writer, noEmptyReaders, (o1, o2) -> ((String) o2).compareTo((String) o1), false);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Problem with input or output file!");
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

    private static void mergeSort(BufferedWriter writer, List<BufferedReader> readers, Comparator<Object> comparator, boolean isInteger) {
        List<Object> elements = new ArrayList<>();
        int readersCount = readers.size();
        boolean isHasNull = false;

        for (BufferedReader r : readers) {
            try {
                String nextLine = r.readLine();

                if (nextLine == null) {
                    isHasNull = true;
                }

                if (isInteger && nextLine != null) {
                    elements.add(Integer.valueOf(nextLine));
                } else {
                    elements.add(nextLine);
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

            Object writableElement = elements.get(writableElementIndex);

            try {
                writer.write(String.format("%s%n", writableElement));
            } catch (IOException e) {
                System.out.println("Problem with output file!");
                return;
            }

            Object nextReadableElement;

            try {
                String nextLine = readers.get(writableElementIndex).readLine();

                if (isInteger && nextLine != null) {
                    nextReadableElement = Integer.valueOf(nextLine);
                } else {
                    nextReadableElement = nextLine;
                }
            } catch (Exception e) {
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
