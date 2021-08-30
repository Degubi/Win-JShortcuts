package jshortcuts.model.extradata;

import static jshortcuts.utils.ReaderUtils.*;

import java.util.*;
import jshortcuts.model.extradata.console.*;

public final class ConsoleDataBlock {
    public static final int SIGNATURE = 0xA0000002;
    public static final int SIZE = 0x000000CC;

    public final EnumSet<FillAttribute> fillAttributes;
    public final EnumSet<FillAttribute> popupFillAttributes;
    public final int screenBufferSizeX;
    public final int screenBufferSizeY;
    public final int windowSizeX;
    public final int windowSizeY;
    public final int windowOriginX;
    public final int windowOriginY;
    public final int fontSize;
    public final FontFamily fontFamily;
    public final FontPitch fontPitch;
    public final int fontWeight;
    public final String faceName;
    public final int cursorSize;
    public final boolean fullScreen;
    public final boolean quickEdit;
    public final boolean insertMode;
    public final boolean autoPosition;
    public final int historyBufferSize;
    public final int numberOfHistoryBuffers;
    public final boolean historyNoDup;
    public final int[] colorTable;

    public ConsoleDataBlock(byte[] lnkData, int afterSigOffset, int blockSize) {
        assert blockSize == SIZE : "Console Data Block Size Mismatch!";

        this.fillAttributes = getFillAttributes(read2Bytes(lnkData, afterSigOffset));
        this.popupFillAttributes = getFillAttributes(read2Bytes(lnkData, afterSigOffset + 2));
        this.screenBufferSizeX = read2Bytes(lnkData, afterSigOffset + 4);
        this.screenBufferSizeY = read2Bytes(lnkData, afterSigOffset + 6);
        this.windowSizeX = read2Bytes(lnkData, afterSigOffset + 8);
        this.windowSizeY = read2Bytes(lnkData, afterSigOffset + 10);
        this.windowOriginX = read2Bytes(lnkData, afterSigOffset + 12);
        this.windowOriginY = read2Bytes(lnkData, afterSigOffset + 14);
        this.fontSize = read4Bytes(lnkData, afterSigOffset + 24);

        var fontFamilyBits = read4Bytes(lnkData, afterSigOffset + 28);

        this.fontFamily = getFontFamily(fontFamilyBits);
        this.fontPitch = getFontPitch(fontFamilyBits);
        this.fontWeight = read4Bytes(lnkData, afterSigOffset + 32);
        this.faceName = readUnicodeString(lnkData, afterSigOffset + 36, 32);
        this.cursorSize = read4Bytes(lnkData, afterSigOffset + 100);
        this.fullScreen = read4Bytes(lnkData, afterSigOffset + 104) != 0;
        this.quickEdit = read4Bytes(lnkData, afterSigOffset + 108) != 0;
        this.insertMode = read4Bytes(lnkData, afterSigOffset + 112) != 0;
        this.autoPosition = read4Bytes(lnkData, afterSigOffset + 116) != 0;
        this.historyBufferSize = read4Bytes(lnkData, afterSigOffset + 120);
        this.numberOfHistoryBuffers = read4Bytes(lnkData, afterSigOffset + 124);
        this.historyNoDup = read4Bytes(lnkData, afterSigOffset + 128) != 0;
        this.colorTable = getColorTable(lnkData, afterSigOffset + 132);
    }

    @Override
    public String toString() {
        return "ConsoleDataBlock {\n" +
               "    fillAttributes: " + fillAttributes + "\n" +
               "    popupFillAttributes: " + popupFillAttributes + "\n" +
               "    screenBufferSizeX: " + screenBufferSizeX + "\n" +
               "    screenBufferSizeY: " + screenBufferSizeY + "\n" +
               "    windowSizeX: " + windowSizeX + "\n" +
               "    windowSizeY: " + windowSizeY + "\n" +
               "    windowOriginX: " + windowOriginX + "\n" +
               "    windowOriginY: " + windowOriginY + "\n" +
               "    fontSize: " + fontSize + "\n" +
               "    fontFamily: " + fontFamily + "\n" +
               "    fontPitch: " + fontPitch + "\n" +
               "    fontWeight: " + fontWeight + "\n" +
               "    faceName: " + faceName + "\n" +
               "    cursorSize: " + cursorSize + "\n" +
               "    fullScreen: " + fullScreen + "\n" +
               "    quickEdit: " + quickEdit + "\n" +
               "    insertMode: " + insertMode + "\n" +
               "    autoPosition: " + autoPosition + "\n" +
               "    historyBufferSize: " + historyBufferSize + "\n" +
               "    numberOfHistoryBuffers: " + numberOfHistoryBuffers + "\n" +
               "    historyNoDup: " + historyNoDup + "\n" +
               "    colorTable: " + Arrays.toString(colorTable) + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ConsoleDataBlock block &&
                block.fillAttributes.equals(fillAttributes) &&
                block.popupFillAttributes.equals(popupFillAttributes) &&
                block.screenBufferSizeX == screenBufferSizeX &&
                block.screenBufferSizeY == screenBufferSizeY &&
                block.windowSizeX == windowSizeX &&
                block.windowSizeY == windowSizeY &&
                block.windowOriginX == windowOriginX &&
                block.windowOriginY == windowOriginY &&
                block.fontSize == fontSize &&
                block.fontFamily == fontFamily &&
                block.fontPitch == fontPitch &&
                block.fontWeight == fontWeight &&
                block.faceName.equals(faceName) &&
                block.cursorSize == cursorSize &&
                block.fullScreen == fullScreen &&
                block.quickEdit == quickEdit &&
                block.insertMode == insertMode &&
                block.autoPosition == autoPosition &&
                block.historyBufferSize == historyBufferSize &&
                block.numberOfHistoryBuffers == numberOfHistoryBuffers &&
                block.historyNoDup == historyNoDup &&
                Arrays.equals(block.colorTable, colorTable);
    }

    @SuppressWarnings("boxing")
    @Override
    public int hashCode() {
        return Objects.hash(fillAttributes, popupFillAttributes, screenBufferSizeX, screenBufferSizeY, windowSizeX, windowSizeY,
                            windowOriginX, windowOriginY, fontSize, fontFamily, fontPitch, fontWeight, faceName, cursorSize,
                            fullScreen, quickEdit, insertMode, autoPosition, historyBufferSize, numberOfHistoryBuffers, historyNoDup, colorTable);
    }


    private static EnumSet<FillAttribute> getFillAttributes(int attributes) {
        var result = EnumSet.noneOf(FillAttribute.class);

        if((attributes & 0x0001) != 0) result.add(FillAttribute.FOREGROUND_BLUE);
        if((attributes & 0x0002) != 0) result.add(FillAttribute.FOREGROUND_GREEN);
        if((attributes & 0x0004) != 0) result.add(FillAttribute.FOREGROUND_RED);
        if((attributes & 0x0008) != 0) result.add(FillAttribute.FOREGROUND_INTENSITY);
        if((attributes & 0x0010) != 0) result.add(FillAttribute.BACKGROUND_BLUE);
        if((attributes & 0x0020) != 0) result.add(FillAttribute.BACKGROUND_GREEN);
        if((attributes & 0x0040) != 0) result.add(FillAttribute.BACKGROUND_RED);
        if((attributes & 0x0080) != 0) result.add(FillAttribute.BACKGROUND_INTENSITY);

        return result;
    }

    private static FontFamily getFontFamily(int fontFamilyBits) {
        return (fontFamilyBits & 0x0010) != 0 ? FontFamily.ROMAN :
               (fontFamilyBits & 0x0020) != 0 ? FontFamily.SWISS :
               (fontFamilyBits & 0x0030) != 0 ? FontFamily.MODERN :
               (fontFamilyBits & 0x0040) != 0 ? FontFamily.SCRIPT :
               (fontFamilyBits & 0x0050) != 0 ? FontFamily.DECORATIVE : FontFamily.DONTCARE;
    }

    private static FontPitch getFontPitch(int fontFamilyBits) {
        return (fontFamilyBits & 0x0001) != 0 ? FontPitch.FIXED :
               (fontFamilyBits & 0x0002) != 0 ? FontPitch.VECTOR :
               (fontFamilyBits & 0x0004) != 0 ? FontPitch.TRUETYPE :
               (fontFamilyBits & 0x0008) != 0 ? FontPitch.DEVICE : FontPitch.NONE;
    }

    private static int[] getColorTable(byte[] lnkData, int colorTableOffset) {
        var result = new int[16];

        for(var i = 0; i < 16; ++i) {
            result[i] = read4Bytes(lnkData, colorTableOffset + (i * 4));
        }

        return result;
    }
}