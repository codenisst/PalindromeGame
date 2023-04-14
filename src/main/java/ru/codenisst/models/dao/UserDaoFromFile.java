package ru.codenisst.models.dao;

import ru.codenisst.models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UserDaoFromFile implements Dao {

    private final String filename;

    public UserDaoFromFile(String filename) {
        this.filename = filename;
    }

    /**
     * Сохранение данных в txt-файл ("Список лидеров"). Хранится 5 наибольших результатов.
     */
    @Override
    public void save(User user) throws IOException {

        List<User> users = getAll();

        if (!users.isEmpty()) {
            User userUpd = users.stream().filter(u -> u.getUsername().equals(user.getUsername())).findFirst().orElse(null);
            if (userUpd != null) {
                users.stream().filter(u -> u.getUsername().equals(user.getUsername())).findFirst().ifPresent(u -> u.setScore(user.getScore()));
                users = sorted(users);
                writeList(users, true);
                return;
            }
        }

        users.add(user);
        users = sorted(users);

        if (users.size() > 5) {
            users = users.subList(0, 5);
        }

        writeList(users, false);
    }

    /**
     * Возвращает всех хранящихся в бд юзеров
     *
     * @return List&lt;{@link User}&gt;
     */
    @Override
    public List<User> getAll() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            List<User> usersFromFIle = new ArrayList<>();

            while (reader.ready()) {
                String[] entity = reader.readLine().split(" - ");
                usersFromFIle.add(new User(entity[0], Integer.parseInt(entity[1])));
            }

            reader.close();
            return usersFromFIle;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private void writeList(List<User> users, boolean update) throws IOException {
        StringBuilder builder = new StringBuilder();

        for (User u : users) {
            builder.append(u.getUsername()).append(" - ").append(u.getScore()).append("\n");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(builder.toString());
        writer.close();

        if (update) {
            System.out.println("Result updated!");
        } else {
            System.out.println("Result added!");
        }
    }

    private List<User> sorted(List<User> users) {
        users = users.stream().sorted(Comparator.comparing(User::getScore))
                .collect(Collectors.toList());
        Collections.reverse(users);
        return users;
    }
}
