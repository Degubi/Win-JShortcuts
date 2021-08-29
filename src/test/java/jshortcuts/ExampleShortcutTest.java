package jshortcuts;

import static jshortcuts.model.shellinkheader.LinkFlag.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.*;
import java.time.*;
import java.util.*;
import jshortcuts.model.linkinfo.*;
import jshortcuts.model.shellinkheader.*;
import org.junit.jupiter.api.*;

public class ExampleShortcutTest implements ShortcutTest {

    private static final Shortcut shortcut = Shortcut.parse(Path.of("src/test/resources/ShellinkExample.lnk"));

    @Test
    @Override
    public void testHeader() {
        var header = shortcut.header;
        var time = Instant.parse("2008-09-12T20:27:17.101Z");

        assertEquals(EnumSet.of(HAS_LINK_TARGET_ID_LIST, HAS_LINK_INFO, HAS_RELATIVE_PATH, HAS_WORKING_DIR), header.linkFlags);
        assertEquals(EnumSet.of(FileAttribute.ARCHIVE), header.fileAttributes);
        assertEquals(time, header.creationTime);
        assertEquals(time, header.accessTime);
        assertEquals(time, header.writeTime);
        assertEquals(0, header.fileSize);
        assertEquals(0, header.iconIndex);
        assertEquals(ShowCommand.NORMAL, header.showCommand);
        assertEquals(HotKey.UNASSIGNED, header.hotKey);
        assertEquals(EnumSet.noneOf(HotKeyModifier.class), header.hotKeyModifiers);
    }

    @Test
    @Override
    public void testLinkTargetIDList() {
        assertArrayEquals(new String[] { "C:\\", "test", "a.txt" }, shortcut.linkTargetIDList);
    }

    @Test
    @Override
    public void testLinkInfo() {
        var linkInfo = shortcut.linkInfo;

        assertEquals(LinkInfoFlag.VOLUME_ID_AND_LOCAL_BASE_PATH, linkInfo.linkInfoFlag);
        assertEquals("C:\\test\\a.txt", linkInfo.localBasePath);
        assertEquals("", linkInfo.commonPathSuffix);

        var volumeID = linkInfo.volumeID;

        assertEquals(volumeID.driveType, DriveType.FIXED);
        assertEquals(volumeID.driveSerialNumber, 0x307A8A81);
        assertEquals(volumeID.volumeLabel, "");
    }

    @Test
    @Override
    public void testStringData() {
        var stringData = shortcut.stringData;

        assertNull(stringData.name);
        assertEquals(stringData.relativePath, ".\\a.txt");
        assertEquals(stringData.workingDir, "C:\\test");
        assertNull(stringData.cmdArguments);
        assertNull(stringData.iconLocation);
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
        var trackerBlock = shortcut.extraData.trackerDataBlock;

        assertEquals("chris-xps", trackerBlock.machineID);
        assertEquals("94c77840-fa47-46c7-b356-5c2dc6b6d115", trackerBlock.droid1);
        assertEquals("94c77840-fa47-46c7-b356-5c2dc6b6d115", trackerBlock.droidBirth1);
        assertEquals("7bcd46ec-7f22-11dd-9499-00137216874a", trackerBlock.droid2);
        assertEquals("7bcd46ec-7f22-11dd-9499-00137216874a", trackerBlock.droidBirth2);
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
                           linkFlags: [HAS_LINK_TARGET_ID_LIST, HAS_LINK_INFO, HAS_RELATIVE_PATH, HAS_WORKING_DIR]
                           fileAttributes: [ARCHIVE]
                           creationTime: 2008-09-12T20:27:17.101Z
                           accessTime: 2008-09-12T20:27:17.101Z
                           writeTime: 2008-09-12T20:27:17.101Z
                           fileSize: 0
                           iconIndex: 0
                           showCommand: NORMAL
                           hotKey: UNASSIGNED
                           hotKeyModifiers: []
                       }
                       LinkTargetIDList [
                           C:\\
                           test
                           a.txt
                       ]
                       LinkInfo {
                           linkInfoFlag: VOLUME_ID_AND_LOCAL_BASE_PATH
                           localBasePath: 'C:\\test\\a.txt'
                           commonPathSuffix: ''
                           volumeID: {
                               driveType: FIXED
                               driveSerialNumber: 0x307a8a81
                               volumeLabel: ''
                           }
                       }
                       StringData {
                           name: null
                           relativePath: .\\a.txt
                           workingDir: C:\\test
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
                           trackerDataBlock: {
                               machineID: chris-xps
                               droid1: 94c77840-fa47-46c7-b356-5c2dc6b6d115
                               droid2: 7bcd46ec-7f22-11dd-9499-00137216874a
                               droidBirth1: 94c77840-fa47-46c7-b356-5c2dc6b6d115
                               droidBirth2: 7bcd46ec-7f22-11dd-9499-00137216874a
                           }
                           vistaAndAboveIDListDataBlock: {}
                       }""";

        assertEquals(expected, shortcut.toString());
    }
}