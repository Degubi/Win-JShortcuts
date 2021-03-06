package jshortcuts;

import static jshortcuts.utils.Constants.*;
import static jshortcuts.utils.WriterUtils.*;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import java.util.regex.*;
import jshortcuts.model.*;
import jshortcuts.model.shellinkheader.*;
import jshortcuts.utils.*;

public final class ShortcutBuilder {
    private static final Pattern PATH_SEPARATOR_SPLITTER = Pattern.compile("(?<=\\\\)");

    private EnumSet<LinkFlag> linkFlags;
    private EnumSet<FileAttribute> fileAttributes;
    private Instant creationTime;
    private Instant accessTime;
    private Instant writeTime;
    private int fileSize;
    private int iconIndex;
    private ShowCommand showCommand;
    private Hotkey hotkey;
    private EnumSet<HotkeyModifier> hotkeyModifiers;
    private String volumeLabel = "";
    private Path workingDir;

    private Path target;

    ShortcutBuilder() {}

    public ShortcutBuilder withLinkFlags(LinkFlag... flags) {
        linkFlags = setOf(LinkFlag.class, flags);
        return this;
    }

    public ShortcutBuilder withFileAttributes(FileAttribute... attributes) {
        fileAttributes = setOf(FileAttribute.class, attributes);
        return this;
    }

    public ShortcutBuilder withCreationTime(Instant time) {
        creationTime = time;
        return this;
    }

    public ShortcutBuilder withAccessTime(Instant time) {
        accessTime = time;
        return this;
    }

    public ShortcutBuilder withWriteTime(Instant time) {
        writeTime = time;
        return this;
    }

    public ShortcutBuilder withShowCommand(ShowCommand command) {
        showCommand = command;
        return this;
    }

    public ShortcutBuilder withHotkey(Hotkey key) {
        hotkey = key;
        return this;
    }

    public ShortcutBuilder withHotkeyModifiers(HotkeyModifier... modifiers) {
        hotkeyModifiers = setOf(HotkeyModifier.class, modifiers);
        return this;
    }

    public ShortcutBuilder withTarget(Path target) {
        this.target = target;
        return this;
    }

    public ShortcutBuilder withTarget(String target) {
        return withTarget(Path.of(target));
    }

    public ShortcutBuilder withWorkingDirectory(Path workingDir) {
        this.workingDir = workingDir;
        return this;
    }


    public void save(String lnkPath) {
        save(Path.of(lnkPath));
    }

    public void save(Path lnkPath) {
        var defaultTime = Instant.now();

        var saveDir = lnkPath.toAbsolutePath().getParent();
        var relativePath = saveDir.relativize(target).toString();

        var linkFlags = this.linkFlags == null ? EnumSet.noneOf(LinkFlag.class) : this.linkFlags;
        var fileAttributes = this.fileAttributes == null ? EnumSet.noneOf(FileAttribute.class) : this.fileAttributes;
        var creationTime = this.creationTime == null ? defaultTime : this.creationTime;
        var accessTime = this.accessTime == null ? defaultTime : this.accessTime;
        var writeTime = this.writeTime == null ? defaultTime : this.writeTime;
        var showCommand = this.showCommand == null ? ShowCommand.NORMAL : this.showCommand;
        var hotkey = this.hotkey == null ? Hotkey.UNASSIGNED : this.hotkey;
        var hotkeyModifiers = this.hotkeyModifiers == null ? EnumSet.noneOf(HotkeyModifier.class) : this.hotkeyModifiers;
        var targetPath = target.toAbsolutePath().toString();
        var workingDir = this.workingDir.toAbsolutePath().toString();

        var hasLinkTargetIDList = linkFlags.contains(LinkFlag.HAS_LINK_TARGET_ID_LIST);
        var linkTargetIDList = hasLinkTargetIDList ? createLinkTargetIDList(target) : null;

        var linkTargetIDListSize = calculateItemIDSize(linkTargetIDList);
        var volumeIDSize = 16 + calculateNullTerminatedStringSize(volumeLabel);
        var linkInfoSize = LinkInfo.HEADER_SIZE + volumeIDSize + calculateNullTerminatedStringSize(targetPath) + 1; // + 1 is unknown, it is after writing the target path, before the string data
        var workingDirPathSize = linkFlags.contains(LinkFlag.HAS_WORKING_DIR) ? calculateStringDataStringSize(workingDir) : 0;
        var relativePathSize = linkFlags.contains(LinkFlag.HAS_RELATIVE_PATH) ? calculateStringDataStringSize(relativePath) : 0;
        var data = new byte[ShellLinkHeader.SIZE + linkTargetIDListSize + linkInfoSize + relativePathSize + workingDirPathSize + 4 + 2]; // 2 bytes are getting lost somewhere

        write4Bytes(data, ShellLinkHeader.SIZE, LINK_HEADER_SIZE_OFFSET);
        writeGUID(data, ShellLinkHeader.LINK_CLSID, LINK_HEADER_CLSID_OFFSET);
        write4Bytes(data, getBitFlagsFromEnumOptions(linkFlags, ReaderUtils.LINK_FLAGS), LINK_HEADER_LINKFLAGS_OFFSET);
        write4Bytes(data, getBitFlagsFromEnumOptions(fileAttributes, ReaderUtils.FILE_ATTRIBUTES), LINK_HEADER_FILEATTRIBUTES_OFFSET);
        writeFileTime(data, creationTime, LINK_HEADER_CREATION_TIME_OFFSET);
        writeFileTime(data, accessTime, LINK_HEADER_ACCESS_TIME_OFFSET);
        writeFileTime(data, writeTime, LINK_HEADER_WRITE_TIME_OFFSET);
        write4Bytes(data, fileSize, LINK_HEADER_FILE_SIZE_OFFSET);
        write4Bytes(data, iconIndex, LINK_HEADER_ICON_INDEX_OFFSET);
        write4Bytes(data, getShowCommandValue(showCommand), LINK_HEADER_SHOW_COMMAND_OFFSET);
        write1Byte(data, getHotkeyValue(hotkey), LINK_HEADER_HOTKEY_OFFSET);
        write1Byte(data, getBitFlagsFromEnumOptions(hotkeyModifiers, ReaderUtils.HOTKEY_MODIFIERS), LINK_HEADER_HOTKEY_OFFSET);

        writeItemIDList(data, LINK_TARGET_ID_LIST_SIZE_OFFSET, linkTargetIDListSize, linkTargetIDList);

        var linkInfoOffset = LINK_TARGET_ID_LIST_SIZE_OFFSET + linkTargetIDListSize + (hasLinkTargetIDList ? 2 : 0);
        write4Bytes(data, linkInfoSize, linkInfoOffset + LINK_INFO_SIZE_RELATIVE_OFFSET);
        write4Bytes(data, LinkInfo.HEADER_SIZE, linkInfoOffset + LINK_INFO_HEADER_SIZE_RELATIVE_OFFSET);
        write4Bytes(data, 1, linkInfoOffset + LINK_INFO_FLAGS_RELATIVE_OFFSET);
        write4Bytes(data, 28, linkInfoOffset + LINK_INFO_VOLUMEID_OFFSET_RELATIVE_OFFSET);
        write4Bytes(data, 45, linkInfoOffset + LINK_INFO_LOCAL_BASE_PATH_OFFSET_RELATIVE_OFFSET);
        var volumeIDOffset = write4Bytes(data, 54, linkInfoOffset + LINK_INFO_COMMON_PATH_SUFFIX_OFFSET_RELATIVE_OFFSET);

        write4Bytes(data, volumeIDSize, volumeIDOffset + VOLUME_ID_SIZE_RELATIVE_OFFSET);
        write4Bytes(data, 0x00000003, volumeIDOffset + VOLUME_ID_DRIVE_TYPE_RELATIVE_OFFSET);
        write4Bytes(data, 0, volumeIDOffset + VOLUME_ID_DRIVE_SERIAL_NUMBER_RELATIVE_OFFSET);
        write4Bytes(data, 16, volumeIDOffset + VOLUME_LABEL_OFFSET_RELATIVE_OFFSET);

        var volumeLabelOffset = writeNullTerminatedString(data, volumeLabel, volumeIDOffset + 16);
        var targetPathOffset = writeNullTerminatedString(data, targetPath, volumeLabelOffset);

        var relativePathOffset = writeStringData(data, relativePath, targetPathOffset + 1);
        var workingDirPathOffset = writeStringData(data, workingDir, relativePathOffset);

        try {
            Files.write(lnkPath, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String[] createLinkTargetIDList(Path target) {
        var pathSubparts = PATH_SEPARATOR_SPLITTER.split(target.toAbsolutePath().toString());
        var linkTargetIDList = new String[pathSubparts.length + 1];

        linkTargetIDList[0] = MY_COMPUTER_GUID;

        for(var i = 0; i < pathSubparts.length; ++i) {
            var path = pathSubparts[i];
            var pathLength = path.length();
            var isSlashTerminated = path.charAt(pathLength - 1) == '\\';
            var toAppend = isSlashTerminated ? (path.charAt(pathLength - 2) == ':' ? path : path.substring(0, pathLength - 1)) : path;

            linkTargetIDList[i + 1] = toAppend;
        }

        return linkTargetIDList;
    }

    private static int getShowCommandValue(ShowCommand command) {
        return command == ShowCommand.MINNO_ACTIVE ? 0x00000007 :
               command == ShowCommand.MAXIMIZED ? 0x00000003 : 0x00000001;
    }

    private static int getHotkeyValue(Hotkey key) {
        return switch(key) {
            case UNASSIGNED     -> 0x00;
            case KEY_0          -> 0x30;
            case KEY_1          -> 0x31;
            case KEY_2          -> 0x32;
            case KEY_3          -> 0x33;
            case KEY_4          -> 0x34;
            case KEY_5          -> 0x35;
            case KEY_6          -> 0x36;
            case KEY_7          -> 0x37;
            case KEY_8          -> 0x38;
            case KEY_9          -> 0x39;
            case KEY_A          -> 0x41;
            case KEY_B          -> 0x42;
            case KEY_C          -> 0x43;
            case KEY_D          -> 0x44;
            case KEY_E          -> 0x45;
            case KEY_F          -> 0x46;
            case KEY_G          -> 0x47;
            case KEY_H          -> 0x48;
            case KEY_I          -> 0x49;
            case KEY_J          -> 0x4A;
            case KEY_K          -> 0x4B;
            case KEY_L          -> 0x4C;
            case KEY_M          -> 0x4D;
            case KEY_N          -> 0x4E;
            case KEY_O          -> 0x4F;
            case KEY_P          -> 0x50;
            case KEY_Q          -> 0x51;
            case KEY_R          -> 0x52;
            case KEY_S          -> 0x53;
            case KEY_T          -> 0x54;
            case KEY_U          -> 0x55;
            case KEY_V          -> 0x56;
            case KEY_W          -> 0x57;
            case KEY_X          -> 0x58;
            case KEY_Y          -> 0x59;
            case KEY_Z          -> 0x5A;
            case KEY_F1         -> 0x70;
            case KEY_F2         -> 0x71;
            case KEY_F3         -> 0x72;
            case KEY_F4         -> 0x73;
            case KEY_F5         -> 0x74;
            case KEY_F6         -> 0x75;
            case KEY_F7         -> 0x76;
            case KEY_F8         -> 0x77;
            case KEY_F9         -> 0x78;
            case KEY_F10        -> 0x79;
            case KEY_F11        -> 0x7A;
            case KEY_F12        -> 0x7B;
            case KEY_F13        -> 0x7C;
            case KEY_F14        -> 0x7D;
            case KEY_F15        -> 0x7E;
            case KEY_F16        -> 0x7F;
            case KEY_F17        -> 0x80;
            case KEY_F18        -> 0x81;
            case KEY_F19        -> 0x82;
            case KEY_F20        -> 0x83;
            case KEY_F21        -> 0x84;
            case KEY_F22        -> 0x85;
            case KEY_F23        -> 0x86;
            case KEY_F24        -> 0x87;
            case KEY_NUMLOCK    -> 0x90;
            case KEY_SCROLLLOCK -> 0x91;
        };
    }

    @SafeVarargs
    private static<T extends Enum<T>> EnumSet<T> setOf(Class<T> type, T... elements) {
        var result = EnumSet.noneOf(type);

        for(var element : elements) {
            result.add(element);
        }

        return result;
    }
}