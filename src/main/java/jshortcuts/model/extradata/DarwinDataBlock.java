package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

public final class DarwinDataBlock {
    public static final int SIGNATURE = 0xA0000006;
    public static final int SIZE = 0x00000314;

    public DarwinDataBlock(byte[] lnkData, int afterSigOffset) {
        assert readBlockSize(lnkData, afterSigOffset) == SIZE : "Darwin Data Block Size Mismatch!";

        throw new IllegalStateException("Darwing Data Block Unimplemented!");
    }
}