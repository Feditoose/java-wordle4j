package ru.yandex.practicum;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private String filename;
    private PrintWriter log;

    public WordleDictionaryLoader(String filename, PrintWriter log) {
        this.filename = filename;
        this.log = log;
    }


    public WordleDictionary readFromDictionary() throws RuntimeException {
        List<String> words = new ArrayList<>();

        try (FileReader fr = new FileReader(filename)) {
            BufferedReader br = new BufferedReader(fr);

            while (br.ready()) {
                String line = br.readLine();
                if (line.length() == 5) {
                    words.add(line.toLowerCase().replace("ё", "е"));
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(log);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new WordleDictionary(words, log);
    }
}
