package ru.codenisst.listeners;

import ru.codenisst.models.dao.Dao;

import java.io.BufferedReader;
import java.io.IOException;

public interface Listener {

    void start() throws IOException;
    default void invalidCommand() {System.out.println("Invalid command!");}

    Listener setDao(Dao dao);
    Listener setReader(BufferedReader reader);
}
