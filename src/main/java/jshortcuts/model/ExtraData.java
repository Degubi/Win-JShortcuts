package jshortcuts.model;

import static jshortcuts.utils.ReaderUtils.*;
import static jshortcuts.utils.WriterUtils.*;

import java.util.*;
import jshortcuts.model.extradata.*;

public final class ExtraData {

    public final ConsoleDataBlock consoleDataBlock;
    public final ConsoleFEDataBlock consoleFEDataBlock;
    public final DarwinDataBlock darwinDataBlock;
    public final EnvironmentVariableDataBlock environmentVariableDataBlock;
    public final IconEnvironmentDataBlock iconEnvironmentDataBlock;
    public final KnownFolderDataBlock knownFolderDataBlock;
    public final PropertyStoreDataBlock propertyStoreDataBlock;
    public final ShimpDataBlock shimpDataBlock;
    public final SpecialFolderDataBlock specialFolderDataBlock;
    public final TrackerDataBlock trackerDataBlock;
    public final VistaAndAboveIDListDataBlock vistaAndAboveIDListDataBlock;

    public ExtraData(byte[] lnkData, int extraDataOffset) {
        var blockSizeOffset = extraDataOffset;
        var blockSize = read4Bytes(lnkData, blockSizeOffset);

        var consoleBlock = (ConsoleDataBlock) null;
        var consoleFEBlock = (ConsoleFEDataBlock) null;
        var darwinBlock = (DarwinDataBlock) null;
        var envVarBlock = (EnvironmentVariableDataBlock) null;
        var iconEnvBlock = (IconEnvironmentDataBlock) null;
        var knownFolderBlock = (KnownFolderDataBlock) null;
        var propStoreBlock = (PropertyStoreDataBlock) null;
        var shimBlock = (ShimpDataBlock) null;
        var specFolderBlock = (SpecialFolderDataBlock) null;
        var trackerBlock = (TrackerDataBlock) null;
        var vistaIDListBlock = (VistaAndAboveIDListDataBlock) null;

        while(blockSize >= 0x00000004) {
            var dataBlockOffset = blockSizeOffset + 8;
            var blockSignature = read4Bytes(lnkData, blockSizeOffset + 4);

            switch(blockSignature) {
                case ConsoleDataBlock.SIGNATURE             -> consoleBlock = new ConsoleDataBlock(lnkData, dataBlockOffset);
                case ConsoleFEDataBlock.SIGNATURE           -> consoleFEBlock = new ConsoleFEDataBlock(lnkData, dataBlockOffset);
                case DarwinDataBlock.SIGNATURE              -> darwinBlock = new DarwinDataBlock(lnkData, dataBlockOffset);
                case EnvironmentVariableDataBlock.SIGNATURE -> envVarBlock = new EnvironmentVariableDataBlock(lnkData, dataBlockOffset);
                case IconEnvironmentDataBlock.SIGNATURE     -> iconEnvBlock = new IconEnvironmentDataBlock(lnkData, dataBlockOffset);
                case KnownFolderDataBlock.SIGNATURE         -> knownFolderBlock = new KnownFolderDataBlock(lnkData, dataBlockOffset);
                case PropertyStoreDataBlock.SIGNATURE       -> propStoreBlock = new PropertyStoreDataBlock(lnkData, dataBlockOffset);
                case ShimpDataBlock.SIGNATURE               -> shimBlock = new ShimpDataBlock(lnkData, dataBlockOffset);
                case SpecialFolderDataBlock.SIGNATURE       -> specFolderBlock = new SpecialFolderDataBlock(lnkData, dataBlockOffset);
                case TrackerDataBlock.SIGNATURE             -> trackerBlock = new TrackerDataBlock(lnkData, dataBlockOffset);
                case VistaAndAboveIDListDataBlock.SIGNATURE -> vistaIDListBlock = new VistaAndAboveIDListDataBlock(lnkData, dataBlockOffset);
            }

            blockSizeOffset += blockSize;
            blockSize = read4Bytes(lnkData, blockSizeOffset);
        }

        this.consoleDataBlock = consoleBlock;
        this.consoleFEDataBlock = consoleFEBlock;
        this.darwinDataBlock = darwinBlock;
        this.environmentVariableDataBlock = envVarBlock;
        this.iconEnvironmentDataBlock = iconEnvBlock;
        this.knownFolderDataBlock = knownFolderBlock;
        this.propertyStoreDataBlock = propStoreBlock;
        this.shimpDataBlock = shimBlock;
        this.specialFolderDataBlock = specFolderBlock;
        this.trackerDataBlock = trackerBlock;
        this.vistaAndAboveIDListDataBlock = vistaIDListBlock;
    }

    @Override
    public String toString() {
        return "ExtraData {\n" +
               "    consoleDataBlock: " + formatEmbeddedStruct(consoleDataBlock) + "\n" +
               "    consoleFEDataBlock: " + formatEmbeddedStruct(consoleFEDataBlock) + "\n" +
               "    darwinDataBlock: " + formatEmbeddedStruct(darwinDataBlock) + "\n" +
               "    environmentVariableDataBlock: " + formatEmbeddedStruct(environmentVariableDataBlock) + "\n" +
               "    iconEnvironmentDataBlock: " + formatEmbeddedStruct(iconEnvironmentDataBlock) + "\n" +
               "    knownFolderDataBlock: " + formatEmbeddedStruct(knownFolderDataBlock) + "\n" +
               "    propertyStoreDataBlock: " + formatEmbeddedStruct(propertyStoreDataBlock) + "\n" +
               "    shimpDataBlock: " + formatEmbeddedStruct(shimpDataBlock) + "\n" +
               "    specialFolderDataBlock: " + formatEmbeddedStruct(specialFolderDataBlock) + "\n" +
               "    trackerDataBlock: " + formatEmbeddedStruct(trackerDataBlock) + "\n" +
               "    vistaAndAboveIDListDataBlock: " + formatEmbeddedStruct(vistaAndAboveIDListDataBlock) + "\n" +
               "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ExtraData data &&
               Objects.equals(data.consoleDataBlock, consoleDataBlock) &&
               Objects.equals(data.consoleFEDataBlock, consoleFEDataBlock) &&
               Objects.equals(data.darwinDataBlock, darwinDataBlock) &&
               Objects.equals(data.environmentVariableDataBlock, environmentVariableDataBlock) &&
               Objects.equals(data.iconEnvironmentDataBlock, iconEnvironmentDataBlock) &&
               Objects.equals(data.knownFolderDataBlock, knownFolderDataBlock) &&
               Objects.equals(data.propertyStoreDataBlock, propertyStoreDataBlock) &&
               Objects.equals(data.shimpDataBlock, shimpDataBlock) &&
               Objects.equals(data.specialFolderDataBlock, specialFolderDataBlock) &&
               Objects.equals(data.trackerDataBlock, trackerDataBlock) &&
               Objects.equals(data.vistaAndAboveIDListDataBlock, vistaAndAboveIDListDataBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(consoleDataBlock, consoleFEDataBlock, darwinDataBlock, environmentVariableDataBlock, iconEnvironmentDataBlock,
                            knownFolderDataBlock, propertyStoreDataBlock, shimpDataBlock, specialFolderDataBlock, trackerDataBlock, vistaAndAboveIDListDataBlock);
    }
}