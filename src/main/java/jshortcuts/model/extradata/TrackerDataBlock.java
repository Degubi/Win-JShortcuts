package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

import java.util.*;

public final class TrackerDataBlock {
    public static final int SIGNATURE = 0xA0000003;
    public static final int SIZE = 0x00000060;

    public final String machineID;
    public final String droid1;
    public final String droid2;
    public final String droidBirth1;
    public final String droidBirth2;

    public TrackerDataBlock(byte[] lnkData, int afterSigOffset) {
        assert readBlockSize(lnkData, afterSigOffset) == SIZE : "Tracker Data Block Size Mismatch!";
        assert read4Bytes(lnkData, afterSigOffset) == 0x00000058 : "Tracker Data Block Length Mismatch!";
        assert read4Bytes(lnkData, afterSigOffset + 4) == 0x00000000 : "Tracker Data Block Version Mismatch!";

        this.machineID = readNullTerminatedStringWithLimit(lnkData, afterSigOffset + 8, 16);
        this.droid1 = readGUID(lnkData, afterSigOffset + 24);
        this.droid2 = readGUID(lnkData, afterSigOffset + 40);
        this.droidBirth1 = readGUID(lnkData, afterSigOffset + 56);
        this.droidBirth2 = readGUID(lnkData, afterSigOffset + 72);
    }

    @Override
    public String toString() {
        return "TrackerDataBlock {\n" +
               "    machineID: " + machineID + "\n" +
               "    droid1: " + droid1 + "\n" +
               "    droid2: " + droid2 + "\n" +
               "    droidBirth1: " + droidBirth1 + "\n" +
               "    droidBirth2: " + droidBirth2 + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TrackerDataBlock block &&
               block.machineID.equals(machineID) &&
               block.droid1.equals(droid1) &&
               block.droid2.equals(droid2) &&
               block.droidBirth1.equals(droidBirth1) &&
               block.droidBirth2.equals(droidBirth2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(machineID, droid1, droid2, droidBirth1, droidBirth2);
    }
}