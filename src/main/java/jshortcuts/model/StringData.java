package jshortcuts.model;

import static jshortcuts.utils.ReaderUtils.*;

import java.util.*;
import jshortcuts.model.shellinkheader.*;

public final class StringData {

    public final String name;
    public final String relativePath;
    public final String workingDir;
    public final String cmdArguments;
    public final String iconLocation;

    public StringData(byte[] lnkData, int stringDataOffset, int[] outLastReadEndOffset, EnumSet<LinkFlag> linkFlags) {
        var sizeOffset = stringDataOffset;

        var name = linkFlags.contains(LinkFlag.HAS_NAME) ? readStringData(lnkData, sizeOffset) : null;
        sizeOffset += name == null ? 0 : name.length() * 2 + 2;

        var relativePath = linkFlags.contains(LinkFlag.HAS_RELATIVE_PATH) ? readStringData(lnkData, sizeOffset) : null;
        sizeOffset += relativePath == null ? 0 : relativePath.length() * 2 + 2;

        var workingDir = linkFlags.contains(LinkFlag.HAS_WORKING_DIR) ? readStringData(lnkData, sizeOffset) : null;
        sizeOffset += workingDir == null ? 0 : workingDir.length() * 2 + 2;

        var cmdArguments = linkFlags.contains(LinkFlag.HAS_ARGUMENTS) ? readStringData(lnkData, sizeOffset) : null;
        sizeOffset += cmdArguments == null ? 0 : cmdArguments.length() * 2 + 2;

        var iconLocation = linkFlags.contains(LinkFlag.HAS_ICON_LOCATION) ? readStringData(lnkData, sizeOffset) : null;
        outLastReadEndOffset[0] = sizeOffset + (iconLocation == null ? 0 : iconLocation.length() * 2 + 2);

        this.name = name;
        this.relativePath = relativePath;
        this.workingDir = workingDir;
        this.cmdArguments = cmdArguments;
        this.iconLocation = iconLocation;
    }

    @Override
    public String toString() {
        return "StringData {\n" +
               "    name: " + name + "\n" +
               "    relativePath: " + relativePath + "\n" +
               "    workingDir: " + workingDir + "\n" +
               "    cmdArguments: " + cmdArguments + "\n" +
               "    iconLocation: " + iconLocation + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringData strData &&
               Objects.equals(strData.cmdArguments, cmdArguments) &&
               Objects.equals(strData.iconLocation, iconLocation) &&
               Objects.equals(strData.name, name) &&
               Objects.equals(strData.relativePath, relativePath) &&
               Objects.equals(strData.workingDir, workingDir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, relativePath, workingDir, cmdArguments, iconLocation);
    }
}