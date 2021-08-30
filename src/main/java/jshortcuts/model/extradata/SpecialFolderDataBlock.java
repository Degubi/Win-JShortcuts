package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

public final class SpecialFolderDataBlock {
    public static final int SIGNATURE = 0xA0000005;
    public static final int SIZE = 0x00000010;

    public final int specialFolderID;

    public SpecialFolderDataBlock(byte[] lnkData, int afterSigOffset, int blockSize) {
        assert blockSize == SIZE : "Special Folder Data Block Size Mismatch!";

        this.specialFolderID = read4Bytes(lnkData, afterSigOffset);

        var offset = read4Bytes(lnkData, afterSigOffset + 4);

        // TODO Finish this
    }
}