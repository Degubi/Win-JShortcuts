package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

public final class ConsoleFEDataBlock {
    public static final int SIGNATURE = 0xA0000004;
    public static final int SIZE = 0x0000000C;

    public final int codePage;

    public ConsoleFEDataBlock(byte[] lnkData, int afterSigOffset) {
        assert readBlockSize(lnkData, afterSigOffset) == SIZE : "ConsoleFE Data Block Size Mismatch!";

        this.codePage = read4Bytes(lnkData, afterSigOffset);
    }

    @Override
    public String toString() {
        return "ConsoleFEDataBlock {\n" +
               "    codePage: " + codePage + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ConsoleFEDataBlock block && block.codePage == codePage;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(codePage);
    }
}