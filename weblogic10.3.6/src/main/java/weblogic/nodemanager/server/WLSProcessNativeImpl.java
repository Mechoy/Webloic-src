package weblogic.nodemanager.server;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import weblogic.logging.WLLevel;
import weblogic.nodemanager.util.ProcessControl;

public class WLSProcessNativeImpl extends WLSProcess {
   private ProcessControl pc;
   private String pid;
   private final boolean isAnExistingPID;
   private static final int PROCESS_CHECK_INTERVAL = 500;

   public WLSProcessNativeImpl(ProcessControl var1, String[] var2, Map var3, File var4, File var5) {
      super(var2, var3, var4, var5);
      this.pc = var1;
      this.isAnExistingPID = false;
      this.setLogger(NMServer.nmLog);
      this.setErrorLevel(WLLevel.WARNING);
   }

   public WLSProcessNativeImpl(ProcessControl var1, String var2) {
      super((String[])null, (Map)null, (File)null, (File)null);
      this.pc = var1;
      this.pid = var2;
      this.isAnExistingPID = true;
      this.setLogger(NMServer.nmLog);
      this.setErrorLevel(WLLevel.WARNING);
   }

   protected final void start() throws IOException {
      if (!this.isAnExistingPID) {
         synchronized(this.pc) {
            this.pid = this.pc.createProcess(this.getCommand(), this.getEnv(), this.getDir(), this.getOutFile());
         }
      }

   }

   public String getProcessId() {
      return this.pid;
   }

   public boolean isAlive() {
      return this.pc.isProcessAlive(this.pid);
   }

   public void destroy() {
      this.pc.killProcess(this.pid);
   }

   protected final void waitFor() throws InterruptedException {
      while(this.pc.isProcessAlive(this.pid)) {
         Thread.sleep(500L);
      }

   }
}
