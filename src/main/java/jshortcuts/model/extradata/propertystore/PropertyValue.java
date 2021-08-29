package jshortcuts.model.extradata.propertystore;

import static jshortcuts.model.extradata.propertystore.PropertyValueType.*;
import static jshortcuts.utils.ReaderUtils.*;

import java.util.*;

public final class PropertyValue {

    public final int ID;
    public final PropertyValueType type;
    public final Object value;

    public PropertyValue(byte[] lnkData, int offset) {
        assert read1Byte(lnkData, offset + 4) == 0x00 : "Serialized Property Value Reserved Mismatch!";
        assert read2Bytes(lnkData, offset + 7) == 0 : "Typed Property Value Padding Mismatch!";

        var type = getType(read2Bytes(lnkData, offset + 5));

        this.ID = read4Bytes(lnkData, offset);
        this.type = type;
        this.value = getValue(type, lnkData, offset + 9);
    }

    @Override
    public String toString() {
        return "PropertyValue {\n" +
               "    ID: " + ID + "\n" +
               "    type: " + type + "\n" +
               "    value: " + value + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PropertyValue prop &&
               prop.ID == ID &&
               prop.type == type &&
               prop.value.equals(value);
    }

    @SuppressWarnings("boxing")
    @Override
    public int hashCode() {
        return Objects.hash(ID, type, value);
    }


    private static Object getValue(PropertyValueType type, byte[] data, int offset) {
        return switch(type) {
            case LPWSTR -> readUnicodeString(data, offset);
            default -> throw new IllegalArgumentException("Unimplemented property type: " + type);
        };
    }

    private static PropertyValueType getType(int type) {
        return switch(type) {
            case 0x0000 -> EMPTY;
            case 0x0001 -> NULL;
            case 0x0010 -> I1;
            case 0x0002 -> I2;
            case 0x0003 -> I4;
            case 0x0014 -> I8;
            case 0x0011 -> UI1;
            case 0x0012 -> UI2;
            case 0x0013 -> UI4;
            case 0x0015 -> UI8;
            case 0x0016 -> INT;
            case 0x0017 -> UINT;
            case 0x001F -> LPWSTR;
            default -> throw new IllegalArgumentException("Unknown property value type: " + Integer.toHexString(type));
        };
    }
}