package ru.codenisst;

import ru.codenisst.core.GameCore;
import ru.codenisst.models.dao.Dao;
import ru.codenisst.models.dao.UserDaoFromFile;
import ru.codenisst.service.PackageScanner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {

    // Подгружается конфиг для подключения к бд.
    // Если url для коннекта не указан - данные хранятся в памяти (отдельный txt файл),
    // так же понадобится реализовать отдельный Dao-класс под требуемую бд с соответствующей логикой.
    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("database.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Dao dao = new UserDaoFromFile();
        String dbUrl = properties.getProperty("db.url");

        if (!dbUrl.isEmpty()) {
            // присвоение ссылке dao инстанса соответствующего Dao-класса под требуемую бд.
        }

        new GameCore(
                dao,
                new PackageScanner().getMapListenersInPackageListeners())
                .started();
    }
}