package ru.codenisst.core;

import ru.codenisst.listeners.Listener;
import ru.codenisst.models.User;
import ru.codenisst.models.dao.Dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public final class GameCore {

    private final Map<String, Listener> listeners;
    private final Dao userDao;

    public GameCore(Dao dao, Map<String, Listener> listeners) {
        this.userDao = dao;
        this.listeners = listeners;
    }

    // Основной метод, из которого осуществляется выполнение команд/запуск "слушателей". При расширении функционала,
    // например добавлении новых игр, потребуется модифицировать switch-конструкцию, подобно ниже описанному примеру.
    public void started() throws IOException {
        System.out.println("Available commands: \n1) /start_palindrome\n2) /score\n3) /exit");

        BufferedReader listener = new BufferedReader(new InputStreamReader(System.in));
        String command = listener.readLine();

        while (!command.equals("/exit")) {

            if (!command.startsWith("/")) {
                invalidCommand();
                command = listener.readLine();
                continue;
            }

            switch (command) {
                case "/start_palindrome": {
                    startTheGamePalindrome(listener);
                    break;
                }
                case "/score": {
                    printScopeBoard();
                    break;
                }
                 /*
                case "/new_game": {
                вызов отдельного приватного метода, который будет дергать "слушателя" из списка listeners. Список формируется через рефлексию.
                    startNewGame();
                    break;
                }
                */
                default: {
                    invalidCommand();
                    break;
                }
            }

            command = listener.readLine();
        }

        listener.close();
    }

    private void startTheGamePalindrome(BufferedReader reader) throws IOException {
        listeners.get("PalindromeListener")
                .setDao(userDao)
                .setReader(reader)
                .start();
    }

    private void printScopeBoard() {
        List<User> userList = userDao.getAll();

        if (!userList.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("Score page: \n");

            for (User user : userList) {
                builder.append(user).append("\n");
            }

            System.out.println(builder);
            return;
        }

        System.out.println("Score board is empty!");
    }

    private void invalidCommand() {
        System.out.println("Invalid command!");
    }
}
