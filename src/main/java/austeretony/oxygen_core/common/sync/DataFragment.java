package austeretony.oxygen_core.common.sync;

public class DataFragment {

    public final int entriesAmount;

    public final byte[] rawData;

    public DataFragment(int entriesAmount, byte[] rawData) {
        this.entriesAmount = entriesAmount;
        this.rawData = rawData;
    }
}