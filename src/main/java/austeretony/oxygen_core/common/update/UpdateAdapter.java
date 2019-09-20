package austeretony.oxygen_core.common.update;

public interface UpdateAdapter {

    String getModId();

    String getVersion();

    void apply();
}