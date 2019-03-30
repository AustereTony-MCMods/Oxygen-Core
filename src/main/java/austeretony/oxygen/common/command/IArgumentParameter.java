package austeretony.oxygen.common.command;

public interface IArgumentParameter {

    String getBaseName();

    String getParameterName();

    boolean hasValue();

    String getValue();

    void setValue(String value);
}
