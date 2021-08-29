package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

public final class ShimpDataBlock {
    public static final int SIGNATURE = 0xA0000008;
    public static final int SIZE = 0x00000088;

    public final String layerName;

    public ShimpDataBlock(byte[] lnkData, int afterSigOffset) {
        assert readBlockSize(lnkData, afterSigOffset) >= SIZE : "Shimp Data Block Size Mismatch!";

        this.layerName = readUnicodeString(lnkData, afterSigOffset);
    }

    @Override
    public String toString() {
        return "ShimpDataBlock {\n" +
               "    layerName: " + layerName + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ShimpDataBlock block && block.layerName.equals(layerName);
    }

    @Override
    public int hashCode() {
        return layerName.hashCode();
    }
}