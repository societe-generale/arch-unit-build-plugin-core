package com.societegenerale.commons.plugin;

public class SilentLog implements Log {

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void info(String s) {

    }

    @Override
    public void debug(String s) {

    }

    @Override
    public void warn(String toString) {

    }

    @Override
    public void warn(String toString, Throwable e) {

    }

    @Override
    public void debug(String s, Throwable e) {

    }
}
