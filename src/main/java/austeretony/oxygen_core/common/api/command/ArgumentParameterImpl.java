package austeretony.oxygen_core.common.api.command;

import austeretony.oxygen_core.common.command.ArgumentParameter;

public class ArgumentParameterImpl implements ArgumentParameter {

    public final String baseName, name;

    private String value;

    public final boolean hasValue;

    public ArgumentParameterImpl(String name, boolean hasValue) {
        this.baseName = name;
        this.name = hasValue ? "-" + name : "--" + name;
        this.hasValue = hasValue;
    }

    public ArgumentParameterImpl(String name) {
        this(name, false);
    }

    @Override
    public String getBaseName() {
        return this.baseName;
    }

    @Override
    public String getParameterName() {
        return this.name;
    }

    @Override
    public boolean hasValue() {
        return this.hasValue;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
