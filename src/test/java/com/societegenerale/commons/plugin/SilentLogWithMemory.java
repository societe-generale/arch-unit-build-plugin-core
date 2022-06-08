package com.societegenerale.commons.plugin;

import java.util.ArrayList;
import java.util.List;

public class SilentLogWithMemory extends SilentLog {

    List<String> infoLogs=new ArrayList();

    List<String> debugLogs=new ArrayList();

    @Override
    public void info(String s) {
        infoLogs.add(s);
    }

    public List<String> getInfoLogs() {
        return infoLogs;
    }

    @Override
    public void debug(String s) {
        debugLogs.add(s);
    }

    public List<String> getDebugLogs() {
        return debugLogs;
    }
}
