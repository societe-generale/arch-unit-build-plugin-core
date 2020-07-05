package com.societegenerale.commons.plugin;

public interface Log {
    
    boolean isInfoEnabled();

    boolean isDebugEnabled();

    void info(String s);

    void debug(String s);

    void warn(String toString);
}
