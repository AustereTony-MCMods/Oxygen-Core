package austeretony.oxygen_core.common.command;

public interface ArgumentParameter {

    String getBaseName();

    String getParameterName();

    boolean hasValue();

    String getValue();

    void setValue(String value);
}
