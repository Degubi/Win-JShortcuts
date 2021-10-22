package jshortcuts;

import static jshortcuts.model.shellinkheader.LinkFlag.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import jshortcuts.model.linkinfo.*;
import jshortcuts.model.shellinkheader.*;
import org.junit.jupiter.api.*;

public class BasicDirectoryShortcutTest implements ShortcutTest {

    private static final Shortcut shortcut = Shortcut.parse(Path.of("src/test/resources/BasicDirectory.lnk"));

    @Test
    @Override
    public void testHeader() {
        var header = shortcut.header;
        var time = Instant.parse("1601-01-16T08:44:40.088Z");

        assertEquals(EnumSet.of(HAS_LINK_TARGET_ID_LIST, HAS_LINK_INFO, HAS_RELATIVE_PATH, IS_UNICODE), header.linkFlags);
        assertEquals(EnumSet.of(FileAttribute.DIRECTORY), header.fileAttributes);
        assertEquals(time, header.creationTime);
        assertEquals(time, header.accessTime);
        assertEquals(time, header.writeTime);
        assertEquals(0, header.fileSize);
        assertEquals(0, header.iconIndex);
        assertEquals(ShowCommand.NORMAL, header.showCommand);
        assertEquals(Hotkey.UNASSIGNED, header.hotkey);
        assertEquals(EnumSet.noneOf(HotkeyModifier.class), header.hotkeyModifiers);
    }

    @Test
    @Override
    public void testLinkTargetIDList() {
        assertArrayEquals(new String[] { ShortcutTest.MY_COMPUTER_GUID, "C:\\", "Users" }, shortcut.linkTargetIDList);
    }

    @Test
    @Override
    public void testLinkInfo() {
        var info = shortcut.linkInfo;

        assertEquals(LinkInfoFlag.VOLUME_ID_AND_LOCAL_BASE_PATH, info.linkInfoFlag);
        assertEquals("C:\\Users", info.localBasePath);
        assertEquals("", info.commonPathSuffix);

        var volumeID = info.volumeID;

        assertEquals(DriveType.FIXED, volumeID.driveType);
        assertEquals(0x0, volumeID.driveSerialNumber);
        assertEquals("", volumeID.volumeLabel);
    }

    @Test
    @Override
    public void testStringData() {
        var strData = shortcut.stringData;

        assertNull(strData.name);
        assertEquals("..\\..\\..", strData.relativePath);
        assertNull(strData.workingDir);
        assertNull(strData.cmdArguments);
        assertNull(strData.iconLocation);
    }

    @Test
    @Override
    public void testConsoleDataBlock() {
        assertNull(shortcut.extraData.consoleDataBlock);
    }

    @Test
    @Override
    public void testConsoleFEDataBlock() {
        assertNull(shortcut.extraData.consoleFEDataBlock);
    }

    @Test
    @Override
    public void testDarwinDataBlock() {
        assertNull(shortcut.extraData.darwinDataBlock);
    }

    @Test
    @Override
    public void testEnvironmentVariableDataBlock() {
        assertNull(shortcut.extraData.environmentVariableDataBlock);
    }

    @Test
    @Override
    public void testIconEnvironmentDataBlock() {
        assertNull(shortcut.extraData.iconEnvironmentDataBlock);
    }

    @Test
    @Override
    public void testKnownFolderDataBlock() {
        assertNull(shortcut.extraData.knownFolderDataBlock);
    }

    @Test
    @Override
    public void testPropertyStoreDataBlock() {
        assertNull(shortcut.extraData.propertyStoreDataBlock);
    }

    @Test
    @Override
    public void testShimpDataBlock() {
        assertNull(shortcut.extraData.shimpDataBlock);
    }

    @Test
    @Override
    public void testSpecialFolderDataBlock() {
        assertNull(shortcut.extraData.specialFolderDataBlock);
    }

    @Test
    @Override
    public void testTrackerDataBlock() {
        assertNull(shortcut.extraData.trackerDataBlock);
    }

    @Test
    @Override
    public void testVistaAndAboveIDListDataBlock() {
        assertNull(shortcut.extraData.vistaAndAboveIDListDataBlock);
    }

    @Test
    @Override
    public void testToString() {
        var expected = """
                       ShellLinkHeader {
                           linkFlags: [HAS_LINK_TARGET_ID_LIST, HAS_LINK_INFO, HAS_RELATIVE_PATH, IS_UNICODE]
                           fileAttributes: [DIRECTORY]
                           creationTime: 1601-01-16T08:44:40.088Z
                           accessTime: 1601-01-16T08:44:40.088Z
                           writeTime: 1601-01-16T08:44:40.088Z
                           fileSize: 0
                           iconIndex: 0
                           showCommand: NORMAL
                           hotKey: UNASSIGNED
                           hotKeyModifiers: []
                       }
                       LinkTargetIDList [
                           MY_COMPUTER_GUID_PLACEHOLDER
                           C:\\
                           Users
                       ]
                       LinkInfo {
                           linkInfoFlag: VOLUME_ID_AND_LOCAL_BASE_PATH
                           localBasePath: 'C:\\Users'
                           commonPathSuffix: ''
                           volumeID: {
                               driveType: FIXED
                               driveSerialNumber: 0x0
                               volumeLabel: ''
                           }
                       }
                       StringData {
                           name: null
                           relativePath: ..\\..\\..
                           workingDir: null
                           cmdArguments: null
                           iconLocation: null
                       }
                       ExtraData {
                           consoleDataBlock: {}
                           consoleFEDataBlock: {}
                           darwinDataBlock: {}
                           environmentVariableDataBlock: {}
                           iconEnvironmentDataBlock: {}
                           knownFolderDataBlock: {}
                           propertyStoreDataBlock: {}
                           shimpDataBlock: {}
                           specialFolderDataBlock: {}
                           trackerDataBlock: {}
                           vistaAndAboveIDListDataBlock: {}
                       }""".replaceFirst("MY_COMPUTER_GUID_PLACEHOLDER", ShortcutTest.MY_COMPUTER_GUID);

        assertEquals(expected, shortcut.toString());
    }

    @Test
    @Override
    public void testShortcutWrite() throws IOException {
        var shortcutPath = Path.of("basicDirectory.lnk");
        var time = Instant.parse("1601-01-16T08:44:40.088Z");

        Shortcut.newBuilder()
                .withTarget(Path.of("C:\\Users"))
                .withLinkFlags(HAS_LINK_TARGET_ID_LIST, HAS_LINK_INFO, HAS_RELATIVE_PATH, IS_UNICODE)
                .withFileAttributes(FileAttribute.DIRECTORY)
                .withAccessTime(time)
                .withCreationTime(time)
                .withWriteTime(time)
                .save(shortcutPath);

        assertEquals(shortcut, Shortcut.parse(shortcutPath));

        Files.delete(shortcutPath);
    }
}