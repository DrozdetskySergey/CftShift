Программа сортирует несколько файлов слиянием. Входные файлы содержат данные одного из двух видов String или Integer.
Данные записаны в столбик, каждая строка файла – новый элемент.
Строки могут содержать любые не пробельные символы, строки с пробелами считаются ошибочными.
Также считается, что файлы предварительно отсортированы.
По умолчанию, элементы из исходного файла нарушающие сортировку сохраняются в том же порядке (как в исходном файле). При параметре “-n” такие данные пропускаются.
По умолчанию, если файл с результатом уже существует, то данные не перезаписываются. Параметр “-w” разрешает перезаписывать файл.


Консольная программа. При запуске без параметров выдаёт описание.

Параметры программы (задаются через аргументы):
1. “-n” пропускать элементы нарушающие сортировку в исходном файле. Необязательный.
2. “-w” можно перезаписывать выходной файл. Необязательный.
3. Режим сортировки “-a” по возрастанию или “-d” по убыванию. Необязательный, по умолчанию сортировка по возрастанию.
4. Тип данных “-s” строки или “-i” целые числа. Обязательный.
5. Имя выходного файла. Обязательный.
6. Остальные параметры – имена исходных файлов, не менее одного.

Версия Java: 11

Сторонняя библиотека: apache.commons.io v2.11.0 https://commons.apache.org/proper/commons-io/download_io.cgi
