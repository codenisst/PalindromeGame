package ru.codenisst.listeners;

import ru.codenisst.models.User;
import ru.codenisst.models.dao.Dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class PalindromeListener implements Listener{

    private Dao userDao;
    private int score;
    private BufferedReader gameListener;
    private final List<String> enteredWordsOrString = new ArrayList<>();

    @Override
    public void start() throws IOException {
        System.out.println("The game has begun! \nTo end the game type /stop");

        String potentialPalindrome = gameListener.readLine();

        while (!potentialPalindrome.equals("/stop")) {

            if (potentialPalindrome.startsWith("/")) {
                invalidCommand();
                potentialPalindrome = gameListener.readLine();
                continue;
            }

            potentialPalindrome = potentialPalindrome.toLowerCase();

            String compareStr1 = potentialPalindrome.replace(" ", "");
            String compareStr2 = new StringBuilder(compareStr1).reverse().toString();

            if (!enteredWordsOrString.contains(potentialPalindrome)) {
                enteredWordsOrString.add(potentialPalindrome);
                if (compareStr2.equals(compareStr1)) {
                    score += potentialPalindrome.length();
                    System.out.println("This string or word is a palindrome! \nYou have " + score + " point(-s)!");
                } else {
                    System.out.println("This string or word is not a palindrome.");
                }
            } else {
                System.out.println("You have already entered this word or string!");
            }

            potentialPalindrome = gameListener.readLine();
        }

        if (score != 0) {
            System.out.println("You have " + score + " point(-s)! \nLet's write down the result! \nWrite your name:");
            writeResult(gameListener.readLine());
            enteredWordsOrString.clear();
            score = 0;
        }
    }

    @Override
    public Listener setDao(Dao dao) {
        this.userDao = dao;
        return this;
    }

    @Override
    public Listener setReader(BufferedReader reader) {
        this.gameListener = reader;
        return this;
    }

    private void writeResult(String nickname) throws IOException {
        userDao.save(new User(nickname, score));
    }
}
