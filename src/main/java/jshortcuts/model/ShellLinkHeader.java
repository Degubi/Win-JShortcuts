package jshortcuts.model;

import static jshortcuts.utils.ReaderUtils.*;

import java.time.*;
import java.util.*;
import jshortcuts.model.shellinkheader.*;

public final class ShellLinkHeader {
    private static final String LINK_CLSID = "00021401-0000-0000-c000-000000000046";
    private static final LinkFlag[] LINK_FLAGS = LinkFlag.values();
    private static final FileAttribute[] FILE_ATTRIBUTES = FileAttribute.values();
    private static final HotKeyModifier[] HOTKEY_MODIFIERS = HotKeyModifier.values();

    public final EnumSet<LinkFlag> linkFlags;
    public final EnumSet<FileAttribute> fileAttributes;
    public final Instant creationTime;
    public final Instant accessTime;
    public final Instant writeTime;
    public final int fileSize;
    public final int iconIndex;
    public final ShowCommand showCommand;
    public final HotKey hotKey;
    public final EnumSet<HotKeyModifier> hotKeyModifiers;

    public ShellLinkHeader(byte[] lnkData) {
        assert read4Bytes(lnkData, 0x0000) == 0x0000004c : "ShellLinkHeader HeaderSize mismatch!";
        assert readGUID(lnkData, 0x0004).equals(LINK_CLSID) : "ShellLinkHeader linkCLSID mismatch!";
        assert read2Bytes(lnkData, 0x0042) == 0 : "ShellLinkHeader Reserved1 mismatch!";
        assert read4Bytes(lnkData, 0x0044) == 0 : "ShellLinkHeader Reserved2 mismatch!";
        assert read4Bytes(lnkData, 0x0048) == 0 : "ShellLinkHeader Reserved3 mismatch!";

        this.linkFlags = readEnumValuesFromBitFlag(read4Bytes(lnkData, 0x0014), LinkFlag.class, LINK_FLAGS);
        this.fileAttributes = readEnumValuesFromBitFlag(read4Bytes(lnkData, 0x0018), FileAttribute.class, FILE_ATTRIBUTES);
        this.creationTime = readFileTime(lnkData, 0x001C);
        this.accessTime = readFileTime(lnkData, 0x0024);
        this.writeTime = readFileTime(lnkData, 0x002C);
        this.fileSize = read4Bytes(lnkData, 0x0034);
        this.iconIndex = read4Bytes(lnkData, 0x0038);
        this.showCommand = getShowCommand(read4Bytes(lnkData, 0x003C));
        this.hotKey = getHotkey(read1Byte(lnkData, 0x0040));
        this.hotKeyModifiers = readEnumValuesFromBitFlag(read1Byte(lnkData, 0x0041), HotKeyModifier.class, HOTKEY_MODIFIERS);
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
               "    hotKey: " + hotKey + "\n" +
               "    hotKeyModifiers: " + hotKeyModifiers + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ShellLinkHeader header &&
               header.accessTime.equals(accessTime) &&
               header.creationTime.equals(creationTime) &&
               header.fileAttributes.equals(fileAttributes) &&
               header.fileSize == fileSize &&
               header.hotKey == hotKey &&
               header.hotKeyModifiers.equals(hotKeyModifiers) &&
               header.iconIndex == iconIndex &&
               header.linkFlags.equals(linkFlags) &&
               header.showCommand == showCommand &&
               header.writeTime.equals(writeTime);
    }

    @SuppressWarnings("boxing")
    @Override
    public int hashCode() {
        return Objects.hash(accessTime, creationTime, fileAttributes, fileSize, hotKey, hotKeyModifiers, iconIndex, linkFlags, showCommand, writeTime);
    }



    private static ShowCommand getShowCommand(int showCommand) {
        return showCommand == 0x00000007 ? ShowCommand.MINNO_ACTIVE :
               showCommand == 0x00000003 ? ShowCommand.MAXIMIZED : ShowCommand.NORMAL;
    }

    private static HotKey getHotkey(int hotkeyLowerByte) {
        return switch(hotkeyLowerByte) {
            case 0x00 -> HotKey.UNASSIGNED;
            case 0x30 -> HotKey.KEY_0;
            case 0x31 -> HotKey.KEY_1;
            case 0x32 -> HotKey.KEY_2;
            case 0x33 -> HotKey.KEY_3;
            case 0x34 -> HotKey.KEY_4;
            case 0x35 -> HotKey.KEY_5;
            case 0x36 -> HotKey.KEY_6;
            case 0x37 -> HotKey.KEY_7;
            case 0x38 -> HotKey.KEY_8;
            case 0x39 -> HotKey.KEY_9;
            case 0x41 -> HotKey.KEY_A;
            case 0x42 -> HotKey.KEY_B;
            case 0x43 -> HotKey.KEY_C;
            case 0x44 -> HotKey.KEY_D;
            case 0x45 -> HotKey.KEY_E;
            case 0x46 -> HotKey.KEY_F;
            case 0x47 -> HotKey.KEY_G;
            case 0x48 -> HotKey.KEY_H;
            case 0x49 -> HotKey.KEY_I;
            case 0x4A -> HotKey.KEY_J;
            case 0x4B -> HotKey.KEY_K;
            case 0x4C -> HotKey.KEY_L;
            case 0x4D -> HotKey.KEY_M;
            case 0x4E -> HotKey.KEY_N;
            case 0x4F -> HotKey.KEY_O;
            case 0x50 -> HotKey.KEY_P;
            case 0x51 -> HotKey.KEY_Q;
            case 0x52 -> HotKey.KEY_R;
            case 0x53 -> HotKey.KEY_S;
            case 0x54 -> HotKey.KEY_T;
            case 0x55 -> HotKey.KEY_U;
            case 0x56 -> HotKey.KEY_V;
            case 0x57 -> HotKey.KEY_W;
            case 0x58 -> HotKey.KEY_X;
            case 0x59 -> HotKey.KEY_Y;
            case 0x5A -> HotKey.KEY_Z;
            case 0x70 -> HotKey.KEY_F1;
            case 0x71 -> HotKey.KEY_F2;
            case 0x72 -> HotKey.KEY_F3;
            case 0x73 -> HotKey.KEY_F4;
            case 0x74 -> HotKey.KEY_F5;
            case 0x75 -> HotKey.KEY_F6;
            case 0x76 -> HotKey.KEY_F7;
            case 0x77 -> HotKey.KEY_F8;
            case 0x78 -> HotKey.KEY_F9;
            case 0x79 -> HotKey.KEY_F10;
            case 0x7A -> HotKey.KEY_F11;
            case 0x7B -> HotKey.KEY_F12;
            case 0x7C -> HotKey.KEY_F13;
            case 0x7D -> HotKey.KEY_F14;
            case 0x7E -> HotKey.KEY_F15;
            case 0x7F -> HotKey.KEY_F16;
            case 0x80 -> HotKey.KEY_F17;
            case 0x81 -> HotKey.KEY_F18;
            case 0x82 -> HotKey.KEY_F19;
            case 0x83 -> HotKey.KEY_F20;
            case 0x84 -> HotKey.KEY_F21;
            case 0x85 -> HotKey.KEY_F22;
            case 0x86 -> HotKey.KEY_F23;
            case 0x87 -> HotKey.KEY_F24;
            case 0x90 -> HotKey.KEY_NUMLOCK;
            case 0x91 -> HotKey.KEY_SCROLLLOCK;
            default -> throw new IllegalArgumentException("Unknown hotkey value: " + hotkeyLowerByte);
        };
    }
}