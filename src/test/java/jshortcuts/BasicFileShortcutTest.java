package jshortcuts;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.*;
import org.junit.jupiter.api.*;

public class BasicFileShortcutTest implements ShortcutTest {

    private static final Shortcut shortcut = Shortcut.parse(Path.of("src/test/resources/BasicFile.lnk"));

    @Test
    @Override
    public void testHeader() {
        fail();
    }

    @Test
    @Override
    public void testLinkTargetIDList() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testLinkInfo() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testStringData() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testConsoleDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testConsoleFEDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testDarwinDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testEnvironmentVariableDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testIconEnvironmentDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testKnownFolderDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testPropertyStoreDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testShimpDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testSpecialFolderDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testTrackerDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testVistaAndAboveIDListDataBlock() {
        // TODO Auto-generated method stub

    }

    @Test
    @Override
    public void testToString() {
        // TODO Auto-generated method stub

    }
}