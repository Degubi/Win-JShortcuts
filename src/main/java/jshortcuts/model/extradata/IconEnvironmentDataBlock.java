package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

public final class IconEnvironmentDataBlock {
    public static final int SIGNATURE = 0xA0000007;
    public static final int SIZE = 0x00000314;

    public final String target;

    public IconEnvironmentDataBlock(byte[] lnkData, int afterSigOffset) {
        assert readBlockSize(lnkData, afterSigOffset) == SIZE : "Icon Environment Data Block Size Mismatch!";

        this.target = readNullTerminatedString(lnkData, afterSigOffset);
    }

    @Override
    public String toString() {
        return "IconEnvironmentDataBlock {\n" +
               "    target: '" + target + "'\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IconEnvironmentDataBlock block && block.target.equals(target);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }
}