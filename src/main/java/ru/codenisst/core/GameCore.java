package ru.codenisst.core;

import ru.codenisst.listeners.Listener;
import ru.codenisst.models.User;
import ru.codenisst.models.dao.Dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class GameCore {

    private final Map<String, Listener> listeners;
    private final Dao userDao;
    private final BufferedReader reader;

    public GameCore(Dao dao, Map<String, Listener> listeners, BufferedReader reader) {
        this.userDao = dao;
        this.listeners = listeners;
        this.reader = reader;
    }

    /**
     * Основной метод, из которого осуществляется выполнение команд/запуск "слушателей".
     * При расширении функционала, например добавлении новых игр, потребуется модифицировать switch-конструкцию, подобно ниже описанному примеру,
     * а так же описать соответствующий listener в пакете {@link ru.codenisst.listeners} , с соответствующей логикой.
     * <p>
     * Пример:<br>
     * startNewGame() - вызов отдельного приватного метода, который будет дергать "слушателя" из списка listeners. Список формируется через рефлексию.
     * <pre>
     * {@code
     * case "/new_game": {
     *      startNewGame();
     *      break;
     * }
     * </pre>
     */
    public boolean started() throws IOException {
        System.out.println("Available commands: \n1) /start_palindrome\n2) /score\n3) /exit");

        String command = reader.readLine();

        while (!command.equals("/exit")) {

            if (!command.startsWith("/")) {
                invalidCommand();
                command = reader.readLine();
                continue;
            }

            switch (command) {
                case "/start_palindrome": {
                    startTheGamePalindrome(reader);
                    break;
                }
                case "/score": {
                    printScopeBoard();
                    break;
                }
                default: {
                    invalidCommand();
                    break;
                }
            }

            command = reader.readLine();
        }

        reader.close();

        return true;
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
