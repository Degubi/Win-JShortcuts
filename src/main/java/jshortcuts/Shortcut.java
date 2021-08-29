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
        var linkInfoOffset = linkFlags.contains(LinkFlag.HAS_LINK_TARGET_ID_LIST) ? fillItemIDList(lnkData, itemIDListBuffer) : 0x004E;
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
        var sizeOffset = 0x004E;
        var readDataSize = read2Bytes(lnkData, sizeOffset);

        while(readDataSize != 0) {
            var maxDataSize = readDataSize - 2;
            var typeOffset = sizeOffset + 2;
            var dataOffset = sizeOffset + 3;
            var type = lnkData[typeOffset];

            switch(type) {
                case '/' -> itemIDListBuffer.add(readNullTerminatedStringWithLimit(lnkData, dataOffset, maxDataSize));
                case '1', '2' -> {
                    var stringDataOffset = dataOffset + 11;

                    itemIDListBuffer.add(readNullTerminatedStringWithLimit(lnkData, stringDataOffset, maxDataSize));
                }
            };

            sizeOffset += readDataSize;
            readDataSize = read2Bytes(lnkData, sizeOffset);
        }

        return sizeOffset + 2;
    }
}