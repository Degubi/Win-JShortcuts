package jshortcuts.model.linkinfo;

import static jshortcuts.utils.ReaderUtils.*;

import java.util.*;

public final class VolumeID {

    public final DriveType driveType;
    public final int driveSerialNumber;
    public final String volumeLabel;

    public VolumeID(byte[] lnkData, int volumeIDOffset) {
        assert read4Bytes(lnkData, volumeIDOffset) >= 0x00000010 : "Volume ID Size Mismatch!";

        this.driveType = getDriveType(read4Bytes(lnkData, volumeIDOffset + 4));
        this.driveSerialNumber = read4Bytes(lnkData, volumeIDOffset + 8);

        var volumeLabelOffset = read4Bytes(lnkData, volumeIDOffset + 12);

        this.volumeLabel = volumeLabelOffset == 0x00000014 ? readUnicodeString(lnkData, read4Bytes(lnkData, volumeIDOffset + 16))
                                                           : readNullTerminatedString(lnkData, volumeLabelOffset);
    }

    @Override
    public String toString() {
        return "VolumeID {\n" +
               "    driveType: " + driveType + "\n" +
               "    driveSerialNumber: 0x" + Integer.toHexString(driveSerialNumber) + "\n" +
               "    volumeLabel: '" + volumeLabel + "'\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof VolumeID volID &&
               volID.driveType == driveType &&
               volID.driveSerialNumber == driveSerialNumber &&
               volumeLabel.equals(volumeLabel);
    }

    @SuppressWarnings("boxing")
    @Override
    public int hashCode() {
        return Objects.hash(driveType, driveSerialNumber, volumeLabel);
    }


    private static DriveType getDriveType(int type) {
        return switch(type) {
            case 0x00000001 -> DriveType.NO_ROOT_DIR;
            case 0x00000002 -> DriveType.REMOVABLE;
            case 0x00000003 -> DriveType.FIXED;
            case 0x00000004 -> DriveType.REMOTE;
            case 0x00000005 -> DriveType.CDROM;
            case 0x00000006 -> DriveType.RAMDISK;
            default -> DriveType.UNKNOWN;
        };
    }
}