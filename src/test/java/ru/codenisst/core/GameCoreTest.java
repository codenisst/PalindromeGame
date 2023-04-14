package ru.codenisst.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.codenisst.listeners.Listener;
import ru.codenisst.listeners.PalindromeListener;
import ru.codenisst.models.dao.Dao;
import ru.codenisst.models.dao.UserDaoFromFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameCoreTest {

    private String fileDbName;
    private Dao daoUsers;
    private BufferedReader reader;
    private Map<String, Listener> listeners;
    private PrintStream originalOut;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void init() {
        fileDbName = "src/main/resources/testDb.txt";
        daoUsers = new UserDaoFromFile(fileDbName);

        Listener palindrome = new PalindromeListener();
        palindrome.setReader(reader).setDao(daoUsers);

        Map<String, Listener> listenersMap = new HashMap<>();
        listenersMap.put("PalindromeListener", palindrome);

        listeners = listenersMap;

        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void deleteDbFile() throws IOException {
        reader.close();
        File file = new File(fileDbName);
        file.delete();
        System.setOut(originalOut);
    }

    @Test
    void checkingTheCoreIsRunningAndStopped() throws Exception {
        String command = "/exit";
        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(command.getBytes())));

        assertTrue(new GameCore(
                daoUsers,
                listeners,
                reader)
                .started());

    }

    @Test
    void checkingTheSuccessfulLaunchOfTheGame() throws IOException {
        String commands = "/start_palindrome\n/stop\n/exit";
        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(commands.getBytes())));

        assertTrue(new GameCore(
                daoUsers,
                listeners,
                reader)
                .started());
    }

    @Test
    void checkingTheCorrectOutputOfTheScoringTable() throws IOException {
        String commands = "/score\n/exit";
        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(commands.getBytes())));

        new GameCore(
                daoUsers,
                listeners,
                reader)
                .started();

        assertTrue(outContent.toString().contains("Score board is empty!"));

        commands = "/start_palindrome\n12 21\n/stop\n123\n/score\n/exit";

        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(commands.getBytes())));

        new GameCore(
                daoUsers,
                listeners,
                reader)
                .started();

        assertTrue(outContent.toString().contains("Score page: \n"));
    }
}