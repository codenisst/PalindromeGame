package ru.codenisst.service;

import org.junit.jupiter.api.Test;
import ru.codenisst.listeners.Listener;

import static org.junit.jupiter.api.Assertions.*;

class PackageScannerTest {

    private final PackageScanner packageScanner = new PackageScanner();

    @Test
    void checkingTheMethodReturnsListOfObjectsThatImplementTheListenerInterface () throws Exception {

        for (Object object:packageScanner.getMapListenersInPackageListeners().values()) {
            assertTrue(object instanceof Listener);
        }
    }
}