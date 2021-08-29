package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

public final class VistaAndAboveIDListDataBlock {
    public static final int SIGNATURE = 0xA000000C;
    public static final int SIZE = 0x0000000A;

    public VistaAndAboveIDListDataBlock(byte[] lnkData, int afterSigOffset) {
        assert readBlockSize(lnkData, afterSigOffset) >= SIZE;

        throw new IllegalArgumentException("Vista And... Data Block Unimplemented!");
    }
}