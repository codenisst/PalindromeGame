package ru.codenisst.models.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.codenisst.models.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDaoFromFileTest {

    private UserDaoFromFile daoUsers;
    private final String fileDbName = "src/main/resources/testDb.txt";

    @BeforeEach
    public void init() {
        daoUsers = new UserDaoFromFile(fileDbName);
    }

    @AfterEach
    public void deleteDbFile() {
        File file = new File(fileDbName);
        file.delete();
    }

    @Test
    void saveIfDbFileNotCreated() throws IOException {
        User user = new User("TestNickname", 10);
        daoUsers.save(user);
        File file = new File(fileDbName);
        assertTrue(file.exists());
    }

    @Test
    void saveIfDbIsEmpty() throws IOException {
        User user = new User("TestNickname", 10);
        daoUsers.save(user);
        List<User> users = daoUsers.getAll();
        assertTrue(users.size() == 1 && users.contains(user));
    }

    @Test
    void saveIfTheDatabaseContains5UsersAndTheirResultsAreGreaterThanTheSaved() throws IOException {
        System.out.println("Загрузка изначально присутствующих в бд данных");
        User user1 = new User("user1", 100);
        User user2 = new User("user2", 200);
        User user3 = new User("user3", 300);
        User user4 = new User("user4", 400);
        User user5 = new User("user5", 500);

        daoUsers.save(user1);
        daoUsers.save(user2);
        daoUsers.save(user3);
        daoUsers.save(user4);
        daoUsers.save(user5);

        System.out.println(daoUsers.getAll());

        User user6 = new User("user6", 10);

        System.out.println("Запись нового юзера. Поскольку бд уже имеет 5 записей, результаты " +
                "которых больше того, что имеет новый юзер - его результат в бд не попадет.");
        daoUsers.save(user6);

        List<User> users = daoUsers.getAll();

        System.out.println(daoUsers.getAll());

        assertFalse(users.contains(user6));
    }

    @Test
    void checkingTheCorrectUpdateOfTheUser() throws IOException {
        User user = new User("nickname", 1);
        daoUsers.save(user);

        User updatedUser = new User("nickname", 3);
        daoUsers.save(updatedUser);

        List<User> users = daoUsers.getAll();
        User userFromDb = users.stream().filter(u -> u.getUsername().equals("nickname")).findFirst().get();

        assertEquals(1, users.size());
        assertEquals(3, userFromDb.getScore());
    }

    @Test
    void getAllUsersStoredInTheDatabase() throws IOException {
        System.out.println("Загрузка изначально присутствующих в бд данных");
        User user1 = new User("user1", 100);
        User user2 = new User("user2", 200);
        User user3 = new User("user3", 300);
        User user4 = new User("user4", 400);

        List<User> initUsers = new ArrayList<>();

        initUsers.add(user1);
        initUsers.add(user3);
        initUsers.add(user2);
        initUsers.add(user4);

        daoUsers.save(user1);
        daoUsers.save(user2);
        daoUsers.save(user3);
        daoUsers.save(user4);

        List<User> receivedUsers = daoUsers.getAll();

        System.out.println(initUsers + "\n" + receivedUsers);

        boolean flag = true;

        for (User user : initUsers) {
            if (!receivedUsers.contains(user)) {
                flag = false;
                break;
            }
        }

        assertTrue(flag);
    }
}