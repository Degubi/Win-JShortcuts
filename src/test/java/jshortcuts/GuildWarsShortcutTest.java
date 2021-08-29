package jshortcuts;

import static jshortcuts.model.shellinkheader.LinkFlag.*;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.*;
import java.time.*;
import java.util.*;
import jshortcuts.model.extradata.propertystore.*;
import jshortcuts.model.linkinfo.*;
import jshortcuts.model.shellinkheader.*;
import org.junit.jupiter.api.*;

public class GuildWarsShortcutTest implements ShortcutTest {

    private static final Shortcut shortcut = Shortcut.parse(Path.of("src/test/resources/GuildWars.lnk"));

    @Test
    @Override
    public void testHeader() {
        var header = shortcut.header;

        assertEquals(EnumSet.of(HAS_LINK_TARGET_ID_LIST, HAS_LINK_INFO, HAS_NAME, HAS_RELATIVE_PATH, HAS_WORKING_DIR, HAS_ARGUMENTS), header.linkFlags);
        assertEquals(EnumSet.of(FileAttribute.ARCHIVE), header.fileAttributes);
        assertEquals(Instant.parse("2021-05-06T07:48:23.178Z"), header.creationTime);
        assertEquals(Instant.parse("2021-05-06T08:42:44.387Z"), header.accessTime);
        assertEquals(Instant.parse("2021-05-06T07:48:23.701Z"), header.writeTime);
        assertEquals(8492776, header.fileSize);
        assertEquals(0, header.iconIndex);
        assertEquals(ShowCommand.MAXIMIZED, header.showCommand);
        assertEquals(HotKey.KEY_P, header.hotKey);
        assertEquals(EnumSet.of(HotKeyModifier.CTRL, HotKeyModifier.ALT), header.hotKeyModifiers);
    }

    @Test
    @Override
    public void testLinkTargetIDList() {
        assertArrayEquals(new String[] { "C:\\", "PROGRA~2", "GUILDW~1", "Gw.exe" }, shortcut.linkTargetIDList);
    }

    @Test
    @Override
    public void testLinkInfo() {
        var linkInfo = shortcut.linkInfo;

        assertEquals(LinkInfoFlag.VOLUME_ID_AND_LOCAL_BASE_PATH, linkInfo.linkInfoFlag);
        assertEquals("C:\\Program Files (x86)\\Guild Wars\\Gw.exe", linkInfo.localBasePath);
        assertEquals("", linkInfo.commonPathSuffix);

        var volumeID = linkInfo.volumeID;

        assertEquals(volumeID.driveType, DriveType.FIXED);
        assertEquals(volumeID.driveSerialNumber, 0x26b83ac);
        assertEquals(volumeID.volumeLabel, "");
    }

    @Test
    @Override
    public void testStringData() {
        var stringData = shortcut.stringData;

        assertEquals("Hihi", stringData.name);
        assertEquals(stringData.relativePath, "..\\..\\..\\..\\..\\..\\..\\Program Files (x86)\\Guild Wars\\Gw.exe");
        assertEquals(stringData.workingDir, "C:\\Program Files (x86)\\Guild Wars");
        assertEquals("-email \"mailPlaceholder\" -password \"pwPlaceholder\" -character \"Mr Assassin Aragorn\"", stringData.cmdArguments);
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
        var knownFolderBlock = shortcut.extraData.knownFolderDataBlock;

        assertEquals("7c5a40ef-a0fb-4bfc-874a-c0f2e0b9fa8e", knownFolderBlock.knownFolderID);
        assertEquals(197, knownFolderBlock.offset);
    }

    @Test
    @Override
    public void testPropertyStoreDataBlock() {
        var propStoreBlock = shortcut.extraData.propertyStoreDataBlock;
        var propValues = propStoreBlock.propertyValues;

        assertEquals("dabd30ed-0043-4789-a7f8-d013a4736622", propStoreBlock.formatID);
        assertEquals(1, propValues.length);

        var propVal = propValues[0];

        assertEquals(100, propVal.ID);
        assertEquals(PropertyValueType.LPWSTR, propVal.type);
        assertEquals("Guild Wars (C:\\Programfájlok (x86))", propVal.value);
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

        assertEquals("degubi", trackerBlock.machineID);
        assertEquals("bc2cfde2-a191-4165-ab15-76ce28ed84ee", trackerBlock.droid1);
        assertEquals("bc2cfde2-a191-4165-ab15-76ce28ed84ee", trackerBlock.droidBirth1);
        assertEquals("95398e43-ae2f-11eb-ba46-f832e4cd580b", trackerBlock.droid2);
        assertEquals("95398e43-ae2f-11eb-ba46-f832e4cd580b", trackerBlock.droidBirth2);
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
                           linkFlags: [HAS_LINK_TARGET_ID_LIST, HAS_LINK_INFO, HAS_NAME, HAS_RELATIVE_PATH, HAS_WORKING_DIR, HAS_ARGUMENTS]
                           fileAttributes: [ARCHIVE]
                           creationTime: 2021-05-06T07:48:23.178Z
                           accessTime: 2021-05-06T08:42:44.387Z
                           writeTime: 2021-05-06T07:48:23.701Z
                           fileSize: 8492776
                           iconIndex: 0
                           showCommand: MAXIMIZED
                           hotKey: KEY_P
                           hotKeyModifiers: [CTRL, ALT]
                       }
                       LinkTargetIDList [
                           C:\\
                           PROGRA~2
                           GUILDW~1
                           Gw.exe
                       ]
                       LinkInfo {
                           linkInfoFlag: VOLUME_ID_AND_LOCAL_BASE_PATH
                           localBasePath: 'C:\\Program Files (x86)\\Guild Wars\\Gw.exe'
                           commonPathSuffix: ''
                           volumeID: {
                               driveType: FIXED
                               driveSerialNumber: 0x26b83ac
                               volumeLabel: ''
                           }
                       }
                       StringData {
                           name: Hihi
                           relativePath: ..\\..\\..\\..\\..\\..\\..\\Program Files (x86)\\Guild Wars\\Gw.exe
                           workingDir: C:\\Program Files (x86)\\Guild Wars
                           cmdArguments: -email "mailPlaceholder" -password "pwPlaceholder" -character "Mr Assassin Aragorn"
                           iconLocation: null
                       }
                       ExtraData {
                           consoleDataBlock: {}
                           consoleFEDataBlock: {}
                           darwinDataBlock: {}
                           environmentVariableDataBlock: {}
                           iconEnvironmentDataBlock: {}
                           knownFolderDataBlock: {
                               knownFolderID: 7c5a40ef-a0fb-4bfc-874a-c0f2e0b9fa8e
                               offset: 197
                           }
                           propertyStoreDataBlock: {
                               formatID: dabd30ed-0043-4789-a7f8-d013a4736622
                               propertyValues: [
                                   {
                                       ID: 100
                                       type: LPWSTR
                                       value: Guild Wars (C:\\Programfájlok (x86))
                                   }
                               ]
                           }
                           shimpDataBlock: {}
                           specialFolderDataBlock: {
                           }
                           trackerDataBlock: {
                               machineID: degubi
                               droid1: bc2cfde2-a191-4165-ab15-76ce28ed84ee
                               droid2: 95398e43-ae2f-11eb-ba46-f832e4cd580b
                               droidBirth1: bc2cfde2-a191-4165-ab15-76ce28ed84ee
                               droidBirth2: 95398e43-ae2f-11eb-ba46-f832e4cd580b
                           }
                           vistaAndAboveIDListDataBlock: {}
                       }""";

        assertEquals(expected, shortcut.toString());
    }
}