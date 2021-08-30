package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

import java.util.*;

public final class KnownFolderDataBlock {
    public static final int SIGNATURE = 0xA000000B;
    public static final int SIZE = 0x0000001C;

    public final String knownFolderID;
    public final int offset;

    public KnownFolderDataBlock(byte[] lnkData, int afterSigOffset, int blockSize) {
        assert blockSize == SIZE : "Known Folder Data Block Size Mismatch!";

        this.knownFolderID = readGUID(lnkData, afterSigOffset);
        this.offset = read4Bytes(lnkData, afterSigOffset + 16);
    }

    @Override
    public String toString() {
        return "KnownFolderDataBlock {\n" +
               "    knownFolderID: " + knownFolderID + "\n" +
               "    offset: " + offset + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KnownFolderDataBlock block &&
               block.knownFolderID.equals(knownFolderID) &&
               block.offset == offset;
    }

    @SuppressWarnings("boxing")
    @Override
    public int hashCode() {
        return Objects.hash(knownFolderID, offset);
    }
}