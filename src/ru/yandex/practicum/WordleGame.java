package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
*/
public class WordleGame {
    private String correctAnswer;
    private int steps;
    private WordleDictionary dictionary;
    private List<String> userAnswers;
    private PrintWriter log;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        Random random = new Random();

        this.dictionary = dictionary;
        List<String> words = dictionary.getWords();
        int index = random.nextInt(words.size());
        Collections.shuffle(words);
        correctAnswer = words.get(index);

        steps = 0;
        userAnswers = new ArrayList<>();
        this.log = log;
    }

    public WordleGame(WordleDictionary dictionary, PrintWriter log, String correctAnswer) {
        this.dictionary = dictionary;
        this.log = log;
        this.correctAnswer = correctAnswer;
        this.steps = 0;
        this.userAnswers = new ArrayList<>();
    }

    public String compareWithAnswer(String answer) {
        StringBuilder result = new StringBuilder();
        char[] corrAnsw = correctAnswer.trim().toLowerCase().toCharArray();
        char[] userAnsw = answer.trim().toLowerCase().toCharArray();

        for (int i = 0; i <= 4; i++) {
            if (corrAnsw[i] == userAnsw[i]) {
                result.append("+");
            } else {
                boolean flag = false;
                for (int j = 0; j <= 4; j++) {
                    if (userAnsw[i] == corrAnsw[j]) {
                        result.append("^");
                        flag = true;
                        break;
                    }
                }

                if (flag == false) {
                    result.append("-");
                }
            }
        }

        return result.toString();
    }

    public String getAdvice() {
        Random random = new Random();

        if (userAnswers.isEmpty()) {
            int index = random.nextInt(dictionary.getWords().size());

            return dictionary.getWords().get(index);
        }

        Map<Integer, Character> corLettersFromAnsw = new LinkedHashMap<>();
        List<String> suitableWords = new ArrayList<>();
        List<Integer> corrPosition = new ArrayList<>();
        List<Character> wrongLetters = new ArrayList<>();

        for (String ans : userAnswers) {
            char[] answer = ans.toCharArray();
            char[] hint = compareWithAnswer(ans).toCharArray();
            int index = 0;
            for (char a : hint) {
                if (a == '+') {
                    corLettersFromAnsw.put(index, answer[index]);
                    corrPosition.add(index);
                }
                else if (a == '^') {
                    corLettersFromAnsw.put(index + 5, answer[index]);
                } else if (a == '-') {
                    wrongLetters.add(answer[index]);
                }

                index++;
            }
        }

        for (String word : dictionary.getWords()) {
            int currReqLetters = 0;
            char[] nowWord = word.toCharArray();

            int i = 0;
            Map<Integer, Character> cpCorrLettersAnsw = corLettersFromAnsw;
            for (char a : nowWord) {
                if (wrongLetters.contains(a)) {
                    i++;
                    continue;
                }
                if (corLettersFromAnsw.get(i) != null) {
                    if (corLettersFromAnsw.get(i) == a) {
                        currReqLetters++;
                        i++;
                        cpCorrLettersAnsw.remove(i);
                        continue;
                    }
                }
                for (Map.Entry<Integer, Character> entry : cpCorrLettersAnsw.entrySet()) {
                    if (entry.getKey() <= 4) {
                        continue;
                    } else if (entry.getKey() >= 5) {
                        if (a == entry.getValue() && i != (entry.getKey()) - 5) {
                            currReqLetters++;
                            cpCorrLettersAnsw.remove(entry.getKey());
                            break;
                        }
                    }
                }

                i++;
            }

            if (currReqLetters == corLettersFromAnsw.size()) {
                suitableWords.add(word);
            }
        }

        String hintWord = "";
        if (suitableWords.size() != 0) {
            hintWord = suitableWords.get(random.nextInt(suitableWords.size()));
        }
        else {
            hintWord = dictionary.getWords().get(random.nextInt(dictionary.getWords().size()));
        }

        return hintWord;
    }

    public void addSteps() {
        steps++;
    }

    public int getSteps() {
        return steps;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void addAnswer(String answer) {
        userAnswers.add(answer);
    }

    public List<String> getUserAnswers() {
        return userAnswers;
    }

}
