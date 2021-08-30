package jshortcuts.model;

import static jshortcuts.utils.Constants.*;
import static jshortcuts.utils.ReaderUtils.*;

import java.time.*;
import java.util.*;
import jshortcuts.model.shellinkheader.*;

public final class ShellLinkHeader {
    public static final String LINK_CLSID = "00021401-0000-0000-c000-000000000046";
    public static final int SIZE = 0x0000004c;

    public final EnumSet<LinkFlag> linkFlags;
    public final EnumSet<FileAttribute> fileAttributes;
    public final Instant creationTime;
    public final Instant accessTime;
    public final Instant writeTime;
    public final int fileSize;
    public final int iconIndex;
    public final ShowCommand showCommand;
    public final Hotkey hotkey;
    public final EnumSet<HotkeyModifier> hotkeyModifiers;

    public ShellLinkHeader(byte[] lnkData) {
        assert read4Bytes(lnkData, LINK_HEADER_SIZE_OFFSET) == SIZE : "ShellLinkHeader HeaderSize mismatch!";
        assert readGUID(lnkData, LINK_HEADER_CLSID_OFFSET).equals(LINK_CLSID) : "ShellLinkHeader linkCLSID mismatch!";
        assert read2Bytes(lnkData, LINK_HEADER_RESERVED1_OFFSET) == 0 : "ShellLinkHeader Reserved1 mismatch!";
        assert read4Bytes(lnkData, LINK_HEADER_RESERVED2_OFFSET) == 0 : "ShellLinkHeader Reserved2 mismatch!";
        assert read4Bytes(lnkData, LINK_HEADER_RESERVED3_OFFSET) == 0 : "ShellLinkHeader Reserved3 mismatch!";

        this.linkFlags = getEnumOptionsFromBitFlags(read4Bytes(lnkData, LINK_HEADER_LINKFLAGS_OFFSET), LinkFlag.class, LINK_FLAGS);
        this.fileAttributes = getEnumOptionsFromBitFlags(read4Bytes(lnkData, LINK_HEADER_FILEATTRIBUTES_OFFSET), FileAttribute.class, FILE_ATTRIBUTES);
        this.creationTime = readFileTime(lnkData, LINK_HEADER_CREATION_TIME_OFFSET);
        this.accessTime = readFileTime(lnkData, LINK_HEADER_ACCESS_TIME_OFFSET);
        this.writeTime = readFileTime(lnkData, LINK_HEADER_WRITE_TIME_OFFSET);
        this.fileSize = read4Bytes(lnkData, LINK_HEADER_FILE_SIZE_OFFSET);
        this.iconIndex = read4Bytes(lnkData, LINK_HEADER_ICON_INDEX_OFFSET);
        this.showCommand = getShowCommand(read4Bytes(lnkData, LINK_HEADER_SHOW_COMMAND_OFFSET));
        this.hotkey = getHotkey(read1Byte(lnkData, LINK_HEADER_HOTKEY_OFFSET));
        this.hotkeyModifiers = getEnumOptionsFromBitFlags(read1Byte(lnkData, LINK_HEADER_HOTKEY_MODIFIERS_OFFSET), HotkeyModifier.class, HOTKEY_MODIFIERS);
    }

    @Override
    public String toString() {
        return "ShellLinkHeader {\n" +
               "    linkFlags: " + linkFlags + "\n" +
               "    fileAttributes: " + fileAttributes + "\n" +
               "    creationTime: " + creationTime + "\n" +
               "    accessTime: " + accessTime + "\n" +
               "    writeTime: " + writeTime + "\n" +
               "    fileSize: " + fileSize + "\n" +
               "    iconIndex: " + iconIndex + "\n" +
               "    showCommand: " + showCommand + "\n" +
               "    hotKey: " + hotkey + "\n" +
               "    hotKeyModifiers: " + hotkeyModifiers + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ShellLinkHeader header &&
               header.accessTime.equals(accessTime) &&
               header.creationTime.equals(creationTime) &&
               header.fileAttributes.equals(fileAttributes) &&
               header.fileSize == fileSize &&
               header.hotkey == hotkey &&
               header.hotkeyModifiers.equals(hotkeyModifiers) &&
               header.iconIndex == iconIndex &&
               header.linkFlags.equals(linkFlags) &&
               header.showCommand == showCommand &&
               header.writeTime.equals(writeTime);
    }

    @SuppressWarnings("boxing")
    @Override
    public int hashCode() {
        return Objects.hash(accessTime, creationTime, fileAttributes, fileSize, hotkey, hotkeyModifiers, iconIndex, linkFlags, showCommand, writeTime);
    }



    private static ShowCommand getShowCommand(int showCommand) {
        return showCommand == 0x00000007 ? ShowCommand.MINNO_ACTIVE :
               showCommand == 0x00000003 ? ShowCommand.MAXIMIZED : ShowCommand.NORMAL;
    }

    private static Hotkey getHotkey(int hotkeyLowerByte) {
        return switch(hotkeyLowerByte) {
            case 0x00 -> Hotkey.UNASSIGNED;
            case 0x30 -> Hotkey.KEY_0;
            case 0x31 -> Hotkey.KEY_1;
            case 0x32 -> Hotkey.KEY_2;
            case 0x33 -> Hotkey.KEY_3;
            case 0x34 -> Hotkey.KEY_4;
            case 0x35 -> Hotkey.KEY_5;
            case 0x36 -> Hotkey.KEY_6;
            case 0x37 -> Hotkey.KEY_7;
            case 0x38 -> Hotkey.KEY_8;
            case 0x39 -> Hotkey.KEY_9;
            case 0x41 -> Hotkey.KEY_A;
            case 0x42 -> Hotkey.KEY_B;
            case 0x43 -> Hotkey.KEY_C;
            case 0x44 -> Hotkey.KEY_D;
            case 0x45 -> Hotkey.KEY_E;
            case 0x46 -> Hotkey.KEY_F;
            case 0x47 -> Hotkey.KEY_G;
            case 0x48 -> Hotkey.KEY_H;
            case 0x49 -> Hotkey.KEY_I;
            case 0x4A -> Hotkey.KEY_J;
            case 0x4B -> Hotkey.KEY_K;
            case 0x4C -> Hotkey.KEY_L;
            case 0x4D -> Hotkey.KEY_M;
            case 0x4E -> Hotkey.KEY_N;
            case 0x4F -> Hotkey.KEY_O;
            case 0x50 -> Hotkey.KEY_P;
            case 0x51 -> Hotkey.KEY_Q;
            case 0x52 -> Hotkey.KEY_R;
            case 0x53 -> Hotkey.KEY_S;
            case 0x54 -> Hotkey.KEY_T;
            case 0x55 -> Hotkey.KEY_U;
            case 0x56 -> Hotkey.KEY_V;
            case 0x57 -> Hotkey.KEY_W;
            case 0x58 -> Hotkey.KEY_X;
            case 0x59 -> Hotkey.KEY_Y;
            case 0x5A -> Hotkey.KEY_Z;
            case 0x70 -> Hotkey.KEY_F1;
            case 0x71 -> Hotkey.KEY_F2;
            case 0x72 -> Hotkey.KEY_F3;
            case 0x73 -> Hotkey.KEY_F4;
            case 0x74 -> Hotkey.KEY_F5;
            case 0x75 -> Hotkey.KEY_F6;
            case 0x76 -> Hotkey.KEY_F7;
            case 0x77 -> Hotkey.KEY_F8;
            case 0x78 -> Hotkey.KEY_F9;
            case 0x79 -> Hotkey.KEY_F10;
            case 0x7A -> Hotkey.KEY_F11;
            case 0x7B -> Hotkey.KEY_F12;
            case 0x7C -> Hotkey.KEY_F13;
            case 0x7D -> Hotkey.KEY_F14;
            case 0x7E -> Hotkey.KEY_F15;
            case 0x7F -> Hotkey.KEY_F16;
            case 0x80 -> Hotkey.KEY_F17;
            case 0x81 -> Hotkey.KEY_F18;
            case 0x82 -> Hotkey.KEY_F19;
            case 0x83 -> Hotkey.KEY_F20;
            case 0x84 -> Hotkey.KEY_F21;
            case 0x85 -> Hotkey.KEY_F22;
            case 0x86 -> Hotkey.KEY_F23;
            case 0x87 -> Hotkey.KEY_F24;
            case 0x90 -> Hotkey.KEY_NUMLOCK;
            case 0x91 -> Hotkey.KEY_SCROLLLOCK;
            default -> throw new IllegalArgumentException("Unknown hotkey value: " + hotkeyLowerByte);
        };
    }
}