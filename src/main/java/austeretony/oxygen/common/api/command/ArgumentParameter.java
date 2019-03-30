package austeretony.oxygen.common.api.command;

import austeretony.oxygen.common.command.IArgumentParameter;

public class ArgumentParameter implements IArgumentParameter {

    public final String baseName, name;

    private String value;

    public final boolean hasValue;

    public ArgumentParameter(String name, boolean hasValue) {
        this.baseName = name;
        this.name = hasValue ? "-" + name : "--" + name;
        this.hasValue = hasValue;
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
