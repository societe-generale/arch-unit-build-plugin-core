package com.societegenerale.commons.plugin;

public interface Log {
    
    boolean isInfoEnabled();

    boolean isDebugEnabled();

    void info(String s);

    void debug(String s);

    void warn(String toString);

    void warn(String s, Throwable e);

    void debug(String s, Throwable e);
}
