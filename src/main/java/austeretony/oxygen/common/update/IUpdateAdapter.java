package austeretony.oxygen.common.update;

public interface IUpdateAdapter {

    String getModId();

    String getVersion();

    void apply();
}