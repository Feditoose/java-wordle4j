package ru.yandex.practicum;

public class WordNotFoundInDictionary extends Throwable {
    public WordNotFoundInDictionary(String s) {
        super(s);
    }
}
