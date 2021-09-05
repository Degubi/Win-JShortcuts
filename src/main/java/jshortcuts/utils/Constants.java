package jshortcuts.utils;

import jshortcuts.model.*;

public final class Constants {

    public static final int LINK_HEADER_SIZE_OFFSET = 0x0000;
    public static final int LINK_HEADER_CLSID_OFFSET = 0x0004;
    public static final int LINK_HEADER_LINKFLAGS_OFFSET = 0x0014;
    public static final int LINK_HEADER_FILEATTRIBUTES_OFFSET = 0x0018;
    public static final int LINK_HEADER_CREATION_TIME_OFFSET = 0x001C;
    public static final int LINK_HEADER_ACCESS_TIME_OFFSET = 0x0024;
    public static final int LINK_HEADER_WRITE_TIME_OFFSET = 0x002C;
    public static final int LINK_HEADER_FILE_SIZE_OFFSET = 0x0034;
    public static final int LINK_HEADER_ICON_INDEX_OFFSET = 0x0038;
    public static final int LINK_HEADER_SHOW_COMMAND_OFFSET = 0x003C;
    public static final int LINK_HEADER_HOTKEY_OFFSET = 0x0040;
    public static final int LINK_HEADER_HOTKEY_MODIFIERS_OFFSET = 0x0041;
    public static final int LINK_HEADER_RESERVED1_OFFSET = 0x0042;
    public static final int LINK_HEADER_RESERVED2_OFFSET = 0x0044;
    public static final int LINK_HEADER_RESERVED3_OFFSET = 0x0048;

    public static final String MY_COMPUTER_GUID = "20d04fe0-3aea-1069-a2d8-08002b30309d";

    public static final int LINK_TARGET_ID_LIST_SIZE_OFFSET = ShellLinkHeader.SIZE;
    public static final int FIRST_LINK_TARGET_LIST_ITEM_ID_SIZE_OFFSET = ShellLinkHeader.SIZE + 2;
    public static final int LINK_INFO_OFFSET_WITHOUT_LINK_TARGED_ID_LIST = ShellLinkHeader.SIZE;

    public static final int LINK_INFO_SIZE_RELATIVE_OFFSET = 0;
    public static final int LINK_INFO_HEADER_SIZE_RELATIVE_OFFSET = 4;
    public static final int LINK_INFO_FLAGS_RELATIVE_OFFSET = 8;
    public static final int LINK_INFO_VOLUMEID_OFFSET_RELATIVE_OFFSET = 12;
    public static final int LINK_INFO_LOCAL_BASE_PATH_OFFSET_RELATIVE_OFFSET = 16;
    public static final int LINK_INFO_COMMON_NETWORK_RELATIVE_LINK_OFFSET_RELATIVE_OFFSET = 20;
    public static final int LINK_INFO_COMMON_PATH_SUFFIX_OFFSET_RELATIVE_OFFSET = 24;

    public static final int VOLUME_ID_SIZE_RELATIVE_OFFSET = 0;
    public static final int VOLUME_ID_DRIVE_TYPE_RELATIVE_OFFSET = 4;
    public static final int VOLUME_ID_DRIVE_SERIAL_NUMBER_RELATIVE_OFFSET = 8;
    public static final int VOLUME_LABEL_OFFSET_RELATIVE_OFFSET = 12;
    public static final int VOLUME_LABEL_UNICODE_OFFSET_RELATIVE_OFFSET = 16;

    private Constants() {}
}