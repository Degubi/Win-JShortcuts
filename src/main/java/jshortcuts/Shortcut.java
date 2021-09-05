package jshortcuts;

import static jshortcuts.utils.Constants.*;
import static jshortcuts.model.shellinkheader.LinkFlag.*;
import static jshortcuts.utils.ReaderUtils.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import jshortcuts.model.*;
import jshortcuts.model.shellinkheader.*;

public final class Shortcut {

    public final ShellLinkHeader header;
    public final String[] linkTargetIDList;
    public final LinkInfo linkInfo;
    public final StringData stringData;
    public final ExtraData extraData;

    private Shortcut(byte[] lnkData) {
        var header = new ShellLinkHeader(lnkData);

        var itemIDListBuffer = new ArrayList<String>();
        var linkFlags = header.linkFlags;
        var hasLinkTargetIDList = linkFlags.contains(LinkFlag.HAS_LINK_TARGET_ID_LIST);
        var linkInfoOffset = hasLinkTargetIDList ? readItemIDList(lnkData, FIRST_LINK_TARGET_LIST_ITEM_ID_SIZE_OFFSET, itemIDListBuffer)
                                                 : LINK_INFO_OFFSET_WITHOUT_LINK_TARGED_ID_LIST;
        var lastReadEndOffset = new int[1];
        var hasLinkInfo = linkFlags.contains(LinkFlag.HAS_LINK_INFO);
        var linkInfo = hasLinkInfo ? new LinkInfo(lnkData, linkInfoOffset, lastReadEndOffset) : null;
        var stringData = new StringData(lnkData, hasLinkInfo ? lastReadEndOffset[0] : linkInfoOffset, lastReadEndOffset, linkFlags);
        var linkTargetIDList = itemIDListBuffer.toArray(String[]::new);

        assert !hasLinkTargetIDList || linkTargetIDList[0].equals(MY_COMPUTER_GUID);

        this.header = header;
        this.linkTargetIDList = linkTargetIDList;
        this.linkInfo = linkInfo;
        this.stringData = stringData;
        this.extraData = new ExtraData(lnkData, lastReadEndOffset[0]);
    }

    @Override
    public String toString() {
        return header + "\n" +
               "LinkTargetIDList [\n    " + String.join("\n    ", linkTargetIDList) + "\n]\n" +
               linkInfo + "\n" +
               stringData + "\n" +
               extraData;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Shortcut shortcut &&
               shortcut.header.equals(header) &&
               Arrays.equals(shortcut.linkTargetIDList, linkTargetIDList) &&
               Objects.equals(shortcut.linkInfo, linkInfo) &&
               shortcut.stringData.equals(stringData) &&
               shortcut.extraData.equals(extraData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, linkTargetIDList, linkInfo, stringData, extraData);
    }


    public static ShortcutBuilder newBuilder() {
        return new ShortcutBuilder();
    }

    public static void create(Path target, Path lnkPath) {
        if(Files.isDirectory(target)) {
            Shortcut.newBuilder()
                    .withTarget(target)
                    .withLinkFlags(HAS_LINK_TARGET_ID_LIST, HAS_LINK_INFO, HAS_RELATIVE_PATH, IS_UNICODE)
                    .withFileAttributes(FileAttribute.DIRECTORY)
                    .save(lnkPath);
        }else{
            Shortcut.newBuilder()
                    .withTarget(target)
                    .withLinkFlags(HAS_LINK_TARGET_ID_LIST, HAS_LINK_INFO, HAS_RELATIVE_PATH, HAS_WORKING_DIR, IS_UNICODE)
                    .save(lnkPath);
        }
    }

    public static void create(String target, String lnkPath) {
        create(Path.of(target), Path.of(lnkPath));
    }

    public static Shortcut parse(byte[] data) {
        return new Shortcut(data);
    }

    public static Shortcut parse(Path filePath) {
        try {
            return new Shortcut(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}