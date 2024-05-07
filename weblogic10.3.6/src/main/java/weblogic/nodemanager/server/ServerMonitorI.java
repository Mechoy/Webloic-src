package weblogic.nodemanager.server;

import java.io.IOException;
import java.util.List;
import weblogic.nodemanager.common.StateInfo;

public interface ServerMonitorI {
   Thread start() throws IOException;

   Thread start(String var1) throws IOException;

   boolean kill() throws InterruptedException;

   Thread startMonitor(WLSProcess var1) throws IOException;

   void setPreStartHooks(List<WLSProcess.ExecuteCallbackHook> var1);

   void setPostStopHooks(List<WLSProcess.ExecuteCallbackHook> var1);

   void cleanup(List<WLSProcess.ExecuteCallbackHook> var1) throws IOException;

   boolean isStarted();

   boolean isFinished();

   boolean isStartupAborted();

   boolean isCleanupAfterCrashNeeded() throws IOException;

   StateInfo getStateInfo();
}
