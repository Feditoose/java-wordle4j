package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    private List<String> words;

    public WordleDictionary(List<String> words, PrintWriter log) {
        this.words = words;
    }

    public void printWords() {
        for (String word : words) {
            System.out.println(word);
        }
    }

    public boolean compareWords(String a, String b) {
        return (a.equals(b));
    }

    public boolean compareChar(char a, char b) {
        return (a == b);
    }

    public List<String> getWords() {
        return words;
    }
}
