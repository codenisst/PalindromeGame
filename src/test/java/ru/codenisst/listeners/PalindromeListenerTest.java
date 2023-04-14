package ru.codenisst.listeners;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.codenisst.models.User;
import ru.codenisst.models.dao.Dao;
import ru.codenisst.models.dao.UserDaoFromFile;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PalindromeListenerTest {

    private Dao daoUsers;
    private String fileDbName;
    private PalindromeListener palindromeListener;
    private BufferedReader reader;

    @BeforeEach
    public void init() {
        fileDbName = "src/main/resources/testDb.txt";
        daoUsers = new UserDaoFromFile(fileDbName);
        palindromeListener = new PalindromeListener();
        palindromeListener.setDao(daoUsers);
    }

    @AfterEach
    public void deleteDbFile() throws IOException {
        reader.close();
        File file = new File(fileDbName);
        file.delete();
    }

    @Test
    void checkingTheListenerIsRunningAndStopped() throws IOException {
        String command = "/stop";
        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(command.getBytes())));
        palindromeListener.setReader(reader);
        assertTrue(palindromeListener.start());
    }

    @Test
    void checkingIfTheUserIsSavedCorrectly () throws IOException {
        String wordsSet = "ada\nHello\n/stop\nnickname";

        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(wordsSet.getBytes())));
        palindromeListener.setReader(reader);
        palindromeListener.start();

        List<User> users = daoUsers.getAll();
        User userFromDb = users.stream().filter(u -> u.getUsername().equals("nickname")).findFirst().get();

        assertEquals(1, users.size());
        assertEquals(new User("nickname", 3), userFromDb);

        String wordsSet2 = "ada\nTenet\nan asd\n/stop\nnickname2";

        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(wordsSet2.getBytes())));
        palindromeListener.setReader(reader);
        palindromeListener.start();

        List<User> users2 = daoUsers.getAll();
        User userFromDb2 = users2.stream().filter(u -> u.getUsername().equals("nickname2")).findFirst().get();

        assertEquals(2, users2.size());
        assertEquals(new User("nickname2", 8), userFromDb2);
    }

    @Test
    void checkingTheCorrectUpdateOfTheUser() throws IOException {
        User user = new User("nickname", 1);
        daoUsers.save(user);

        String wordsSet = "ada\nHello\n/stop\nnickname";

        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(wordsSet.getBytes())));
        palindromeListener.setReader(reader);
        palindromeListener.start();

        List<User> users = daoUsers.getAll();
        User userFromDb = users.stream().filter(u -> u.getUsername().equals("nickname")).findFirst().get();

        assertEquals(1, users.size());
        assertEquals(3, userFromDb.getScore());
    }

    @Test
    void checkingCorrectScoring() throws IOException {
        String wordsSet = "ada\nHello\n/stop\nnickname";

        reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(wordsSet.getBytes())));
        palindromeListener.setReader(reader);
        palindromeListener.start();

        List<User> users = daoUsers.getAll();
        User userFromDb = users.stream().filter(u -> u.getUsername().equals("nickname")).findFirst().get();

        assertEquals(3, userFromDb.getScore());
    }
}