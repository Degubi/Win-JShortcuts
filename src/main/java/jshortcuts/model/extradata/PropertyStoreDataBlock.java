package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;
import static jshortcuts.utils.WriterUtils.*;

import java.util.*;
import jshortcuts.model.extradata.propertystore.*;

public final class PropertyStoreDataBlock {
    public static final int SIGNATURE = 0xA0000009;
    public static final int SIZE = 0x0000000C;

    public final String formatID;
    public final PropertyValue[] propertyValues;

    public PropertyStoreDataBlock(byte[] lnkData, int afterSigOffset, int blockSize) {
        assert blockSize >= SIZE : "Property Store Data Block Size Mismatch!";
        assert read4Bytes(lnkData, afterSigOffset + 4) == 0x53505331 : "Serialized Property Storage Version Mismatch!";

        this.formatID = readGUID(lnkData, afterSigOffset + 8);

        var propVals = new ArrayList<PropertyValue>();
        var propertyValueSizeOffset = afterSigOffset + 24;
        var propertyValueSize = read4Bytes(lnkData, propertyValueSizeOffset);

        while(propertyValueSize != 0x00000000) {
            propVals.add(new PropertyValue(lnkData, propertyValueSizeOffset + 4));

            propertyValueSizeOffset += propertyValueSize;
            propertyValueSize = read4Bytes(lnkData, propertyValueSizeOffset);
        }

        this.propertyValues = propVals.toArray(PropertyValue[]::new);
    }

    @Override
    public String toString() {
        return "PropertyStoreDataBlock {\n" +
               "    formatID: " + formatID + "\n" +
               "    propertyValues: " + formatEmbeddedStructs(propertyValues) + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PropertyStoreDataBlock block &&
               block.formatID.equals(formatID) &&
               Arrays.equals(block.propertyValues, propertyValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formatID, propertyValues);
    }
}