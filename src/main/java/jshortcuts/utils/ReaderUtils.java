package jshortcuts.utils;

import static jshortcuts.utils.WriterUtils.*;

import java.time.*;
import java.util.*;

public final class ReaderUtils {

    public static long read8Bytes(byte[] data, int begin) {
        return ((long) data[begin] & 0xFF) | (((long) data[begin + 1] & 0xFF) << 8) | (((long) data[begin + 2] & 0xFF) << 16) |
               (((long) data[begin + 3] & 0xFF) << 24) | ((long) (data[begin + 4] & 0xFF) << 32) | (((long) data[begin + 5] & 0xFF) << 40) |
               (((long) data[begin + 6] & 0xFF) << 48) | (((long) data[begin + 7] & 0xFF) << 56);
    }

    public static int read4Bytes(byte[] data, int begin) {
        return (data[begin] & 0xFF) | ((data[begin + 1] & 0xFF) << 8) | ((data[begin + 2] & 0xFF) << 16) | ((data[begin + 3] & 0xFF) << 24);
    }

    public static int read2Bytes(byte[] data, int begin) {
        return (data[begin] & 0xFF) | ((data[begin + 1] & 0xFF) << 8);
    }

    public static int read1Byte(byte[] data, int begin) {
        return (data[begin] & 0xFF);
    }

    public static<T extends Enum<T>> EnumSet<T> readEnumValuesFromBitFlag(int flags, Class<T> enumType, T[] enumOptions) {
        var resultOptions = EnumSet.noneOf(enumType);
        var enumOptionIndex = 0;

        for(var i = 1; i < 128; i <<= 1) {
            if((flags & i) != 0) {
                resultOptions.add(enumOptions[enumOptionIndex]);
            }

            ++enumOptionIndex;
        }

        return resultOptions;
    }

    public static String readNullTerminatedString(byte[] data, int offset) {
        for(var i = 0;; ++i) {
            if((data[offset + i] & 0xFF) == 0) {
                return new String(data, offset, i);
            }
        }
    }

    public static String readNullTerminatedStringWithLimit(byte[] data, int offset, int limit) {
        for(var i = 0; i < limit; ++i) {
            var value = data[offset + i] & 0xFF;

            if(value == 0) {
                return new String(data, offset, i);
            }
        }

        return new String(data, offset, limit);
    }

    public static String readStringData(byte[] data, int offset) {
        return readUnicodeString(data, offset + 2, read2Bytes(data, offset));
    }

    public static String readUnicodeString(byte[] data, int offset) {
        return readUnicodeString(data, offset + 4, read4Bytes(data, offset) - 1);
    }

    public static String readUnicodeString(byte[] data, int offset, int length) {
        var stringData = new char[length];

        for(int dataPos = 0, insertIndex = 0; insertIndex < length; dataPos += 2, ++insertIndex) {
            stringData[insertIndex] = (char) read2Bytes(data, offset + dataPos);
        }

        return new String(stringData);
    }

    public static Instant readFileTime(byte[] data, int offset) {
        return Instant.ofEpochMilli((read8Bytes(data, offset) / 10000) - 11644473600000L);
    }

    public static String readGUID(byte[] data, int offset) {
        return toZeroPaddedHexString(read4BytesLong(data, offset), 8) + "-" +
               toZeroPaddedHexString(read2Bytes(data, offset + 4), 4) + "-" +
               toZeroPaddedHexString(read2Bytes(data, offset + 6), 4) + "-" +
               toZeroPaddedHexString(read2BytesReversed(data, offset + 8), 4) + "-" +
               toZeroPaddedHexString(read6BytesReversed(data, offset + 10), 12);
    }

    public static int readBlockSize(byte[] data, int dataBlockOffset) {
        return read4Bytes(data, dataBlockOffset - 8);
    }


    private static int read2BytesReversed(byte[] data, int begin) {
        return (data[begin + 1] & 0xFF) | ((data[begin] & 0xFF) << 8);
    }

    private static long read4BytesLong(byte[] data, int begin) {
        return ((long) data[begin] & 0xFF) | (((long) data[begin + 1] & 0xFF) << 8) | (((long) data[begin + 2] & 0xFF) << 16) | (((long) data[begin + 3] & 0xFF) << 24);
    }

    private static long read6BytesReversed(byte[] data, int begin) {
        return ((long) data[begin + 5] & 0xFF) | (((long) data[begin + 4] & 0xFF) << 8) | (((long) data[begin + 3] & 0xFF) << 16) |
               (((long) data[begin + 2] & 0xFF) << 24) | ((long) (data[begin + 1] & 0xFF) << 32) | (((long) data[begin] & 0xFF) << 40);
    }
}