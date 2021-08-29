package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

public final class EnvironmentVariableDataBlock {
    public static final int SIGNATURE = 0xA0000001;
    public static final int SIZE = 0x00000314;

    public final String target;

    public EnvironmentVariableDataBlock(byte[] lnkData, int afterSigOffset) {
        assert readBlockSize(lnkData, afterSigOffset) == SIZE : "Environment Variable Data Block Size Mismatch!";

        this.target = readNullTerminatedString(lnkData, afterSigOffset);
    }

    @Override
    public String toString() {
        return "EnvironmentVariableDataBlock {\n" +
               "    target: '" + target + "'\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EnvironmentVariableDataBlock block && block.target.equals(target);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }
}