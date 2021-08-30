package jshortcuts.utils;

import static java.lang.Character.*;

import java.time.*;
import java.util.*;

public final class WriterUtils {

    public static int write8Bytes(byte[] data, long value, int offset) {
        data[offset] = (byte) ((value & 0x00000000000000FFL));
        data[offset + 1] = (byte) ((value & 0x000000000000FF00L) >> 8);
        data[offset + 2] = (byte) ((value & 0x0000000000FF0000L) >> 16);
        data[offset + 3] = (byte) ((value & 0x00000000FF000000L) >> 24);
        data[offset + 4] = (byte) ((value & 0x000000FF00000000L) >> 32);
        data[offset + 5] = (byte) ((value & 0x0000FF0000000000L) >> 40);
        data[offset + 6] = (byte) ((value & 0x00FF000000000000L) >> 48);
        data[offset + 7] = (byte) ((value & 0xFF00000000000000L) >> 56);

        return offset + 4;
    }

    public static int write4Bytes(byte[] data, int value, int offset) {
        data[offset] = (byte) ((value & 0x000000FF));
        data[offset + 1] = (byte) ((value & 0x0000FF00) >> 8);
        data[offset + 2] = (byte) ((value & 0x00FF0000) >> 16);
        data[offset + 3] = (byte) ((value & 0xFF000000) >> 24);

        return offset + 4;
    }

    public static int write2Bytes(byte[] data, int value, int offset) {
        data[offset] = (byte) ((value & 0x000000FF));
        data[offset + 1] = (byte) ((value & 0x0000FF00) >> 8);

        return offset + 4;
    }

    public static int write1Byte(byte[] data, int value, int offset) {
        data[offset] = (byte) ((value & 0x000000FF));

        return offset + 4;
    }

    public static<T extends Enum<T>> int getBitFlagsFromEnumOptions(EnumSet<T> options, T[] enumOptions) {
        var result = 0;
        var enumOptionsLength = enumOptions.length;

        for(var option : options) {
            for(var i = 0; i < enumOptionsLength; ++i) {
                if(enumOptions[i] == option) {
                    result |= 1 << i;
                    break;
                }
            }
        }

        return result;
    }

    public static int writeFileTime(byte[] data, Instant value, int offset) {
        write8Bytes(data, value.toEpochMilli() * 10000 + 11644473600000L, offset);

        return offset + 8;
    }

    public static int writeGUID(byte[] data, String guid, int offset) {
        var guidData = guid.getBytes();

        writeGUIDBytesToBuffer(data, offset, guidData, 0, 8);
        writeGUIDBytesToBuffer(data, offset + 4, guidData, 9, 13);
        writeGUIDBytesToBuffer(data, offset + 6, guidData, 14, 18);
        writeReversedGUIDBytesToBuffer(data, offset + 8, guidData, 19, 23);
        writeReversedGUIDBytesToBuffer(data, offset + 10, guidData, 24, 36);

        return offset + 16;
    }


    public static String formatEmbeddedStruct(Object obj) {
        if(obj == null) {
            return "{}";
        }

        var result = new StringBuilder("{\n");

        writeStructEmbeddedForm(obj, "    ", result);

        return result.append("    }").toString();
    }

    public static String formatEmbeddedStructs(Object[] objs) {
        if(objs.length == 0) {
            return "[]";
        }

        var result = new StringBuilder("[\n");
        var objCount = objs.length;

        for(var i = 0; i < objCount; ++i) {
            result.append("        {\n");
            writeStructEmbeddedForm(objs[i], "        ", result);
            result.append("        }");

            if(i != objCount - 1) {
                result.append(',');
            }

            result.append('\n');
        }

        return result.append("    ]").toString();
    }

    public static String toZeroPaddedHexString(long value, int charCount) {
        var hexForm = Long.toHexString(value);

        return "0".repeat(charCount - hexForm.length()) + hexForm;
    }


    private static void writeStructEmbeddedForm(Object obj, String padding, StringBuilder builder) {
        var lines = obj.toString().split("\n");

        for(var i = 1; i < lines.length - 1; ++i) {
            builder.append(padding + lines[i] + '\n');
        }
    }

    private static void writeGUIDBytesToBuffer(byte[] outData, int outOffset, byte[] inData, int begin, int end) {
        for(int insert = 0, i = end - 1; i >= begin; i -= 2, ++insert) {
            outData[outOffset + insert] = (byte) ((digit(inData[i], 16)) | (((digit(inData[i - 1], 16)) << 4)));
        }
    }

    private static void writeReversedGUIDBytesToBuffer(byte[] outData, int outOffset, byte[] inData, int begin, int end) {
        for(int insert = (end - begin) / 2 - 1, i = end - 1; i >= begin; i -= 2, --insert) {
            outData[outOffset + insert] = (byte) ((digit(inData[i], 16)) | ((digit(inData[i - 1], 16) << 4)));
        }
    }
}