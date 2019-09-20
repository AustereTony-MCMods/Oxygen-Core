package austeretony.oxygen_core.common.api.update;

import austeretony.oxygen_core.common.update.UpdateAdapter;

public abstract class AbstractUpdateAdapter implements UpdateAdapter {

    private boolean apply;

    private String oldVersion, newVersion;

    public void validate(String oldVersion, String newVersion) {
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
        if (this.getVersion().equals(newVersion))
            this.apply = true;
    }

    public void applyChanges() {
        if (this.apply)
            this.apply();
    }

    public String getOldVersion() {
        return this.oldVersion;
    }

    public String getNewVersion() {
        return this.newVersion;
    }
}