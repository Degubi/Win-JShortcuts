package jshortcuts;

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
        var linkInfoOffset = linkFlags.contains(LinkFlag.HAS_LINK_TARGET_ID_LIST) ? fillItemIDList(lnkData, itemIDListBuffer) : LINK_INFO_OFFSET_NO_LINK_TARGED_ID_LIST;
        var hasLinkInfo = linkFlags.contains(LinkFlag.HAS_LINK_INFO);
        var lastReadEndOffset = new int[1];
        var linkInfo = hasLinkInfo ? new LinkInfo(lnkData, linkInfoOffset, lastReadEndOffset) : null;
        var stringData = new StringData(lnkData, hasLinkInfo ? lastReadEndOffset[0] : linkInfoOffset, lastReadEndOffset, linkFlags);

        this.header = header;
        this.linkTargetIDList = itemIDListBuffer.toArray(String[]::new);
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


    private static int fillItemIDList(byte[] lnkData, ArrayList<String> itemIDListBuffer) {
        var itemIDSizeOffset = FIRST_ITEM_ID_SIZE_OFFSET;
        var itemIDSize = read2Bytes(lnkData, itemIDSizeOffset);

        while(itemIDSize != 0) {
            var itemIDMaxDataSize = itemIDSize - 2;
            var itemIDDataOffset = itemIDSizeOffset + 3;
            var itemIDType = lnkData[itemIDSizeOffset + 2];

            switch(itemIDType) {
                case '/' -> itemIDListBuffer.add(readNullTerminatedStringWithLimit(lnkData, itemIDDataOffset, itemIDMaxDataSize));
                case '1', '2' -> {
                    var stringDataOffset = itemIDDataOffset + 11;

                    itemIDListBuffer.add(readNullTerminatedStringWithLimit(lnkData, stringDataOffset, itemIDMaxDataSize));
                }
            };

            itemIDSizeOffset += itemIDSize;
            itemIDSize = read2Bytes(lnkData, itemIDSizeOffset);
        }

        return itemIDSizeOffset + 2;
    }


    private static final int FIRST_ITEM_ID_SIZE_OFFSET = ShellLinkHeader.SIZE + 2;
    private static final int LINK_INFO_OFFSET_NO_LINK_TARGED_ID_LIST = ShellLinkHeader.SIZE + 2;
}