package ru.codenisst.models.dao;

import ru.codenisst.models.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserDaoFromFile implements Dao {

    // Сохранение данных в txt-файл ("Список лидеров"). Хранится 5 наибольших результатов.

    @Override
    public void save(User user) throws IOException {


        Map<String, Integer> mapUsersScore = new HashMap<>();

        try {
            mapUsersScore = getAll().stream().collect(Collectors.toMap(User::getUsername, User::getScore));
        } catch (NullPointerException e) {
        }

        if (mapUsersScore.containsKey(user.getUsername())) {
            System.out.println("Result updated!");
        } else {
            System.out.println("Result added!");
        }

        mapUsersScore.put(user.getUsername(), user.getScore());

        if (mapUsersScore.size() > 5) {
            Map.Entry<String, Integer> minEntry = null;
            for (Map.Entry<String, Integer> result : mapUsersScore.entrySet()) {
                if (minEntry == null || result.getValue() < minEntry.getValue()) {
                    minEntry = result;
                }
            }

            if (minEntry != null) {
                mapUsersScore.remove(minEntry.getKey());
            }
        }

        StringBuilder builder = new StringBuilder();

        for (String username : mapUsersScore.keySet()) {
            builder.append(username).append(" - ").append(mapUsersScore.get(username)).append("\n");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("usersScore.txt"));
        writer.write(builder.toString());
        writer.close();
    }

    @Override
    public List<User> getAll() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("usersScore.txt"));
            List<User> usersFromFIle = new ArrayList<>();

            while (reader.ready()) {
                String[] entity = reader.readLine().split(" - ");
                usersFromFIle.add(new User(entity[0], Integer.parseInt(entity[1])));
            }

            reader.close();
            return usersFromFIle;
        } catch (Exception e) {
            return null;
        }
    }
}
