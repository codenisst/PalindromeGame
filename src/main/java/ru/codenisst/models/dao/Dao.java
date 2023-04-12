package ru.codenisst.models.dao;

import ru.codenisst.models.User;

import java.io.IOException;
import java.util.List;

public interface Dao {

    void save(User user) throws IOException;
    List<User> getAll();

}
