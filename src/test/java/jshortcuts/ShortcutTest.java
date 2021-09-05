package jshortcuts;

import java.io.*;

public interface ShortcutTest {
    public static final String MY_COMPUTER_GUID = "20d04fe0-3aea-1069-a2d8-08002b30309d";

    void testHeader();
    void testLinkTargetIDList();
    void testLinkInfo();
    void testStringData();
    void testConsoleDataBlock();
    void testConsoleFEDataBlock();
    void testDarwinDataBlock();
    void testEnvironmentVariableDataBlock();
    void testIconEnvironmentDataBlock();
    void testKnownFolderDataBlock();
    void testPropertyStoreDataBlock();
    void testShimpDataBlock();
    void testSpecialFolderDataBlock();
    void testTrackerDataBlock();
    void testVistaAndAboveIDListDataBlock();
    void testToString();
    void testShortcutWrite() throws IOException;
}