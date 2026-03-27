package ru.yandex.practicum;

import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) throws WordNotFoundInDictionary, IOException {
        PrintWriter log = new PrintWriter(new FileWriter("log.txt"));
        try {
            Scanner scanner = new Scanner(System.in);

            WordleDictionary wd = new WordleDictionaryLoader("words_ru.txt", log).readFromDictionary();
            WordleGame wordle = new WordleGame(wd, log);

            final int MAX_STEPS = 6;
            final int WORD_LENGTH = 5;

            System.out.println("Попробуйте угадать слово");
            while (wordle.getSteps() <= MAX_STEPS) {
                String userAnswer = scanner.nextLine();

                if (!(userAnswer.isEmpty())) {
                    userAnswer = userAnswer.trim().toLowerCase();
                    if (!wd.getWords().contains(userAnswer) && userAnswer.length() == WORD_LENGTH) {
                        log.println(userAnswer);
                        System.out.println("Слова " + userAnswer + " нет в словаре.");
                        continue;
                    } else if (!wd.getWords().contains(userAnswer) && userAnswer.length() != WORD_LENGTH) {
                        log.println(userAnswer);
                        System.out.println("Слово должно быть из 5 букв");
                        continue;
                    }

                    if (wd.compareWords(wordle.getCorrectAnswer(), userAnswer)) {
                        System.out.println("Молодец!!!\nЗагаданное слово - " + wordle.getCorrectAnswer());
                        return;
                    }
                    String hint = wordle.compareWithAnswer(userAnswer);
                    System.out.println(hint);
                    wordle.addAnswer(userAnswer);
                    log.println(wordle.getUserAnswers().size());

                    wordle.addSteps();
                    log.println(wordle.getSteps());
                } else {
                    String advice = wordle.getAdvice();
                    wordle.addAnswer(advice);
                    System.out.println(advice);
                    wordle.addSteps();
                    if (wd.compareWords(advice, wordle.getCorrectAnswer())) {
                        System.out.println("Правильно, загаданное слово - " + wordle.getCorrectAnswer());
                        return;
                    } else {
                        System.out.println(wordle.compareWithAnswer(advice));
                    }
                }
            }

            if (wordle.getSteps() > MAX_STEPS) {
                System.out.println("Загаданное слово: " + wordle.getCorrectAnswer());
                throw new OutOfMaxStepsException("Ходов больше не осталось.");
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace(log);
        } finally {
            log.close();
        }
    }
}
