package com.raion.snapventure.Data;

public class DataStage {

    private String _name;
    private boolean _unlocked;

    public DataStage() {
    }

    public DataStage(String _name, boolean _unlocked) {
        this._name = _name;
        this._unlocked = _unlocked;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public boolean is_unlocked() {
        return _unlocked;
    }

    public void set_unlocked(boolean _unlocked) {
        this._unlocked = _unlocked;
    }
}
