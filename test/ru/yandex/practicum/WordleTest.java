package ru.yandex.practicum;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    WordleDictionary wd;
    WordleGame wordle;
    PrintWriter log;

    @BeforeEach
    public void makeAllForWork() throws IOException {
        log = new PrintWriter(new FileWriter("log.txt"));
        wd = new WordleDictionaryLoader("test.txt", log).readFromDictionary();
        wordle = new WordleGame(wd, log);
    }

    @AfterEach
    public void closeALL() {
        log.close();
    }

    @Test
    void testSteps() {
        assertEquals(0, wordle.getSteps());
        wordle.addSteps();
        assertEquals(1, wordle.getSteps());
        wordle.addSteps();
        assertEquals(2, wordle.getSteps());
    }

    @Test
    void testUserAnswers() {
        wordle.addAnswer("герой");
        wordle.addAnswer("город");
        assertEquals(2, wordle.getUserAnswers().size());
        assertEquals("герой", wordle.getUserAnswers().get(0));
        assertEquals("город", wordle.getUserAnswers().get(1));
    }

    @Test
    void testDictionaryLoaded() {
        assertNotNull(wd);
        assertTrue(wd.getWords().size() > 0);
        assertTrue(wd.getWords().contains("герой"));
        assertTrue(wd.getWords().contains("город"));
    }

    @Test
    void testCompareFullMatch() {
        String correct = wordle.getCorrectAnswer();
        assertEquals("+++++", wordle.compareWithAnswer(correct));
    }

    @Test
    void testGetAdviceExcludesWrongLetters() {
        WordleGame testGame = new WordleGame(wd, log, "герой");
        testGame.addAnswer("город");
        testGame.addAnswer("гроза");

        String advice = testGame.getAdvice();

        assertFalse(advice.contains("д"));
        assertFalse(advice.contains("з"));
    }

    @Test
    void testGetAdviceContainsWrongPositionLetters() {
        WordleGame testGame = new WordleGame(wd, log, "герой");
        testGame.addAnswer("еройг");

        String advice = testGame.getAdvice();
        assertTrue(advice.contains("о"));
    }

    @Test
    void testGetAdviceNoAttempts() {
        String advice = wordle.getAdvice();
        assertNotNull(advice);
        assertTrue(wd.getWords().contains(advice));
        assertTrue(wordle.getUserAnswers().isEmpty());
    }

    @Test
    void testGetAdviceWithAttempt() {
        wordle.addAnswer("город");
        String advice = wordle.getAdvice();
        assertNotNull(advice);
        assertTrue(wd.getWords().contains(advice));
    }

}
