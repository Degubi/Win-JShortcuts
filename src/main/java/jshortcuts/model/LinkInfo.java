package jshortcuts.model;

import static jshortcuts.utils.Constants.*;
import static jshortcuts.utils.ReaderUtils.*;
import static jshortcuts.utils.WriterUtils.*;

import jshortcuts.model.linkinfo.*;

public final class LinkInfo {

    public final LinkInfoFlag linkInfoFlag;
    public final VolumeID volumeID;
    public final String localBasePath;
    public final String commonPathSuffix;

    public LinkInfo(byte[] lnkData, int linkInfoOffset, int[] outLastReadEndOffset) {
        var infoHeaderSize = read4Bytes(lnkData, linkInfoOffset + LINK_INFO_HEADER_SIZE_RELATIVE_OFFSET);

        assert infoHeaderSize == 0x0000001C : "LinkInfo with optional fields are not implemented!";

        var linkInfoFlags = read4Bytes(lnkData, linkInfoOffset + LINK_INFO_FLAGS_RELATIVE_OFFSET);
        var isVolumeAndLocalBasePath = linkInfoFlags == 1;

        var volumeIDOffset = read4Bytes(lnkData, linkInfoOffset + LINK_INFO_VOLUMEID_OFFSET_RELATIVE_OFFSET);
        var localBasePathOffset = read4Bytes(lnkData, linkInfoOffset + LINK_INFO_LOCAL_BASE_PATH_OFFSET_RELATIVE_OFFSET);
        var commonNetworkRelativeLinkOffset = read4Bytes(lnkData, linkInfoOffset + LINK_INFO_COMMON_NETWORK_RELATIVE_LINK_OFFSET_RELATIVE_OFFSET);
        var commonPathSuffixOffset = read4Bytes(lnkData, linkInfoOffset + LINK_INFO_COMMON_PATH_SUFFIX_OFFSET_RELATIVE_OFFSET);
        // TODO: LocalBasePathOffsetUnicode, CommonPathSuffixOffsetUnicode

        assert volumeIDOffset == 0 || isVolumeAndLocalBasePath;
        assert localBasePathOffset == 0 || isVolumeAndLocalBasePath;
        assert commonNetworkRelativeLinkOffset == 0 || !isVolumeAndLocalBasePath;

        this.linkInfoFlag = isVolumeAndLocalBasePath ? LinkInfoFlag.VOLUME_ID_AND_LOCAL_BASE_PATH : LinkInfoFlag.COMMON_NETWORK_RELATIVE_LINK_AND_PATH_SUFFIX;
        this.volumeID = isVolumeAndLocalBasePath ? new VolumeID(lnkData, linkInfoOffset + volumeIDOffset) : null;
        this.localBasePath = isVolumeAndLocalBasePath ? readNullTerminatedString(lnkData, linkInfoOffset + localBasePathOffset) : null;
        this.commonPathSuffix = readNullTerminatedString(lnkData, linkInfoOffset + commonPathSuffixOffset);

        // TODO: This won't make sense later tho
        outLastReadEndOffset[0] = linkInfoOffset + commonPathSuffixOffset + commonPathSuffix.length() + 1;
    }

    @Override
    public String toString() {
        return "LinkInfo {\n" +
               "    linkInfoFlag: " + linkInfoFlag + "\n" +
               "    localBasePath: '" + localBasePath + "'\n" +
               "    commonPathSuffix: '" + commonPathSuffix + "'\n" +
               "    volumeID: " + formatEmbeddedStruct(volumeID) + "\n" +
               "}";
    }
}