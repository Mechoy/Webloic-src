package weblogic.nodemanager.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import weblogic.nodemanager.NodeManagerTextTextFormatter;
import weblogic.nodemanager.common.StartupConfig;
import weblogic.nodemanager.common.StateInfo;
import weblogic.nodemanager.system.NodeManagerSystem;
import weblogic.nodemanager.util.ConcurrentFile;

abstract class AbstractServerMonitor implements ServerMonitorI, Runnable {
   private final ServerManagerI serverMgr;
   private final DomainManager domainMgr;
   private final ServerDir serverDir;
   private final ConcurrentFile lockFile;
   private final ConcurrentFile stateFile;
   private final StateInfo stateInfo;
   private final WLSProcessBuilder builder;
   private final StartupConfig conf;
   private final LogFormatter formatter;
   private WLSProcess proc;
   private boolean started;
   private boolean killing;
   private boolean finished;
   private int stateCheckCount;
   private boolean killed;
   private boolean startupAborted;
   private long lastBaseStartTime;
   private List<WLSProcess.ExecuteCallbackHook> preStartHooks = new ArrayList();
   private List<WLSProcess.ExecuteCallbackHook> postStopHooks = new ArrayList();
   private int stateCheckInterval = 500;
   private static final NodeManagerTextTextFormatter nmText = NodeManagerTextTextFormatter.getInstance();
   private static final String EOL = System.getProperty("line.separator");

   public AbstractServerMonitor(ServerManagerI var1, StartupConfig var2) {
      this.serverMgr = var1;
      this.domainMgr = var1.getDomainManager();
      this.serverDir = var1.getServerDir();
      this.lockFile = this.serverDir.getLockFile();
      this.stateFile = this.serverDir.getStateFile();
      this.stateInfo = new StateInfo();
      this.stateCheckInterval = this.domainMgr.getNMServer().getConfig().getStateCheckInterval();
      this.conf = var2;
      this.formatter = new LogFormatter();
      this.builder = this.createWLSProcessBuilder(var1, var2);
   }

   protected abstract WLSProcessBuilder createWLSProcessBuilder(ServerManagerI var1, StartupConfig var2);

   public void setPreStartHooks(List<WLSProcess.ExecuteCallbackHook> var1) {
      this.preStartHooks = var1;
   }

   public void setPostStopHooks(List<WLSProcess.ExecuteCallbackHook> var1) {
      this.postStopHooks = var1;
   }

   public synchronized Thread startMonitor(WLSProcess var1) throws IOException {
      this.setProcess(var1);
      Thread var2 = new Thread(this, "server monitor");
      var2.start();
      return var2;
   }

   public synchronized Thread start() throws IOException {
      WLSProcess var1 = this.startWLSProcess();
      return this.startMonitor(var1);
   }

   public synchronized Thread start(String var1) throws IOException {
      WLSProcess var2 = this.builder.createProcess(var1);
      if (this.lastBaseStartTime == 0L) {
         this.lastBaseStartTime = System.currentTimeMillis() / 1000L;
      }

      if (this.postStopHooks != null) {
         Iterator var3 = this.postStopHooks.iterator();

         while(var3.hasNext()) {
            WLSProcess.ExecuteCallbackHook var4 = (WLSProcess.ExecuteCallbackHook)var3.next();
            var2.addPostStopHook(var4);
         }
      }

      WLSProcess.ExecuteCallbackHook var5 = this.createStopScriptAsPostHook();
      if (var5 != null) {
         var2.addPostStopHook(var5);
      }

      return this.startMonitor(var2);
   }

   public synchronized boolean isFinished() {
      return this.finished;
   }

   public synchronized boolean isStarted() {
      return this.started;
   }

   public synchronized boolean isKilled() {
      return this.killed;
   }

   public boolean isCleanupAfterCrashNeeded() throws IOException {
      if (this.stateInfo == null || this.stateInfo.getState() == null) {
         this.loadStateInfo();
      }

      return this.stateInfo != null && this.stateInfo.getState() != null && this.stateInfo.getState().equals("RUNNING") && this.proc == null;
   }

   public void cleanup(List<WLSProcess.ExecuteCallbackHook> var1) {
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            WLSProcess.ExecuteCallbackHook var3 = (WLSProcess.ExecuteCallbackHook)var2.next();

            try {
               var3.execute();
            } catch (IOException var5) {
               this.log(Level.FINEST, "The server cleanup failed.", var5);
            }
         }
      }

      this.serverDir.getLockFile().delete();
      this.serverDir.getStateFile().delete();
      this.serverDir.getPidFile().delete();
      this.serverDir.getURLFile().delete();
   }

   public synchronized boolean isStartupAborted() {
      return this.startupAborted;
   }

   public StateInfo getStateInfo() {
      return this.stateInfo;
   }

   public synchronized boolean kill() throws InterruptedException {
      if (this.proc == null) {
         return false;
      } else {
         if (!this.builder.isNative()) {
            this.fine("Killing non native process");
            this.proc.destroy();
            this.killing = true;
         } else if (this.proc.isAlive()) {
            ConcurrentFile var1 = this.serverDir.getPidFile();

            String var2;
            try {
               var2 = var1.readLine();
               this.fine("Read process id of " + var2);
            } catch (IOException var4) {
               return false;
            }

            this.fine("Calling kill on the process control for " + var2);
            if (!this.builder.getProcessControl().killProcess(var2)) {
               this.killing = true;
               this.fine("Process control killProcess return false");
               return false;
            }
         }

         this.killed = true;
         this.fine("Waiting for server to be killed");

         while(!this.finished) {
            this.wait();
         }

         this.killing = false;
         this.fine("Finished killing process");
         return true;
      }
   }

   public void run() {
      try {
         this.runMonitor();
      } catch (Throwable var4) {
         this.severe(nmText.msgErrorUnexpected(), var4);
      }

      synchronized(this) {
         this.fine("runMonitor returned, setting finished=true and notifying waiters");
         this.finished = true;
         this.notifyAll();
      }
   }

   private void runMonitor() throws IOException, InterruptedException {
      WLSProcess var1;
      synchronized(this) {
         var1 = this.proc;
      }

      int var2 = 0;

      do {
         String var3 = var1.getProcessId();
         if (var3 != null) {
            this.lockFile.writeLine(var3);
         }

         this.fine("Wrote process id " + var3);

         while(var1.isAlive()) {
            Thread.sleep((long)this.stateCheckInterval);
            this.loadStateInfo();
         }

         this.fine("Waiting for the process to die: " + var3);
         var1.waitForProcessDeath();
         this.loadStateInfo();
         this.lockFile.delete();
         if (this.isKilled()) {
            this.fine("Process is killed " + var3 + " with " + nmText.msgKilled());
            this.info(nmText.msgKilled());
            this.stateInfo.setState("SHUTDOWN");
            break;
         }

         if (!this.stateInfo.isStarted()) {
            this.stateInfo.setState("FAILED_NOT_RESTARTABLE");
            this.info(nmText.getStartupFailedNotRestartable());
            break;
         }

         boolean var4 = "SHUTTING_DOWN".equals(this.stateInfo.getState()) || "FORCE_SHUTTING_DOWN".equals(this.stateInfo.getState());
         if (var4 && !this.stateInfo.isFailed()) {
            this.stateInfo.setState("SHUTDOWN");
            this.info(nmText.msgShutDown());
            break;
         }

         this.stateInfo.setState("FAILED");
         if (!this.conf.isAutoRestart()) {
            this.info(nmText.msgWarnIgnoreFailed());
            this.stateInfo.setState("FAILED_NOT_RESTARTABLE");
            break;
         }

         long var5 = System.currentTimeMillis() / 1000L;
         if (var5 - this.lastBaseStartTime > (long)this.conf.getRestartInterval()) {
            var2 = 0;
            this.lastBaseStartTime = 0L;
         }

         ++var2;
         if (var2 > this.conf.getRestartMax()) {
            this.info(nmText.msgWarnRestartMax());
            this.stateInfo.setState("FAILED_NOT_RESTARTABLE");
            break;
         }

         this.info(nmText.msgInfoRestarting(var2));
         if (this.conf.getRestartDelaySeconds() > 0) {
            this.info(nmText.getSleepForRestartDelay(this.conf.getRestartDelaySeconds()));
            this.stateInfo.setState("FAILED");
            Thread.sleep((long)(this.conf.getRestartDelaySeconds() * 1000));
            if (this.isKilled()) {
               break;
            }
         }

         var1 = this.startWLSProcess();
      } while(var1 != null);

      this.stateInfo.save(this.stateFile);
      this.setProcess((WLSProcess)null);
   }

   private void setProcess(WLSProcess var1) {
      synchronized(this) {
         this.proc = var1;
      }
   }

   private synchronized WLSProcess startWLSProcess() throws IOException {
      try {
         return this.startWLSProcess_inner();
      } catch (IOException var2) {
         this.finished = true;
         this.stateInfo.set("FAILED_NOT_RESTARTABLE", false, true);
         this.stateInfo.save(this.stateFile);
         throw var2;
      }
   }

   private synchronized WLSProcess startWLSProcess_inner() throws IOException {
      if (this.killed) {
         return null;
      } else {
         String[] var1 = this.builder.getCommandLine();
         StringBuilder var2 = new StringBuilder(1024);
         String[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            if (var6.matches("[ \t\n]")) {
               var2.append("\"").append(var6).append("\" ");
            } else {
               var2.append(var6).append(' ');
            }
         }

         this.info(this.getStartString(nmText, var2));
         Map var7 = this.builder.getEnvironment();
         if (var7 != null) {
            Iterator var8 = var7.entrySet().iterator();

            while(var8.hasNext()) {
               Map.Entry var10 = (Map.Entry)var8.next();
               Map.Entry var12 = (Map.Entry)var10;
               this.fine("Environment: " + var12.getKey() + "=" + var12.getValue());
            }
         }

         this.info(nmText.getWorkingDirectory(this.builder.getDirectory().toString()));
         LogFileRotationUtil.rotateServerFiles(this.serverMgr, this.conf);
         this.info(nmText.getOutFile(this.serverDir.getOutFile().getCanonicalPath()));
         this.stateInfo.set("STARTING", false, false);
         this.stateInfo.save(this.stateFile);
         WLSProcess var9 = this.builder.createProcess();
         Iterator var11;
         WLSProcess.ExecuteCallbackHook var14;
         if (this.preStartHooks != null) {
            var11 = this.preStartHooks.iterator();

            while(var11.hasNext()) {
               var14 = (WLSProcess.ExecuteCallbackHook)var11.next();
               var9.addPreStartHook(var14);
            }
         }

         if (this.postStopHooks != null) {
            var11 = this.postStopHooks.iterator();

            while(var11.hasNext()) {
               var14 = (WLSProcess.ExecuteCallbackHook)var11.next();
               var9.addPostStopHook(var14);
            }
         }

         WLSProcess.ExecuteCallbackHook var13 = this.createStopScriptAsPostHook();
         if (var13 != null) {
            var9.addPostStopHook(var13);
         }

         var9.startProcess();
         if (this.lastBaseStartTime == 0L) {
            this.lastBaseStartTime = System.currentTimeMillis() / 1000L;
         }

         this.setProcess(var9);
         this.notifyAll();
         return var9;
      }
   }

   protected String getStartString(NodeManagerTextTextFormatter var1, StringBuilder var2) {
      return var1.msgStarting(var2.toString());
   }

   private Properties toProps(Map<String, String> var1) {
      Properties var2 = new Properties();
      Iterator var3 = var1.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         var2.put(var4.getKey(), var4.getValue());
      }

      return var2;
   }

   private WLSProcess.ExecuteCallbackHook createStopScriptAsPostHook() {
      NMServerConfig var1 = this.domainMgr.getNMServer().getConfig();
      final String var2 = var1.getStopScriptName();
      final long var3 = var1.getExecScriptTimeout();
      WLSProcess.ExecuteCallbackHook var5 = null;
      if (var1.isStopScriptEnabled() && var2 != null) {
         var5 = new WLSProcess.ExecuteCallbackHook() {
            public void execute() {
               try {
                  WLSProcessBuilder var1 = new WLSProcessBuilder(AbstractServerMonitor.this.serverMgr, AbstractServerMonitor.this.conf, true);
                  String[] var2x = var1.getCommandLine();
                  Map var3x = var1.getEnvironment();
                  Properties var4 = var3x != null ? AbstractServerMonitor.this.toProps(var3x) : null;
                  File var5 = var1.getDirectory();
                  int var6 = NodeManagerSystem.getInstance().executeScript(var2x, var4, var5, var3);
                  if (var6 != 0) {
                     if (var6 == -101) {
                        NMServer.nmLog.warning(AbstractServerMonitor.nmText.cmdTimedOut(var2, AbstractServerMonitor.this.serverMgr.getServerName(), var3));
                     } else {
                        NMServer.nmLog.warning(AbstractServerMonitor.nmText.cmdFailedSvr(var2, AbstractServerMonitor.this.serverMgr.getServerName()));
                     }
                  }
               } catch (Exception var7) {
                  NMServer.nmLog.warning(AbstractServerMonitor.nmText.cmdFailedSvrReason(var2, AbstractServerMonitor.this.serverMgr.getServerName(), var7.toString()));
               }

            }
         };
      }

      return var5;
   }

   private synchronized void loadStateInfo() throws IOException {
      try {
         this.stateInfo.load(this.stateFile);
         ++this.stateCheckCount;
      } catch (FileNotFoundException var2) {
         return;
      }

      if (this.killing && this.stateCheckCount * this.stateCheckInterval % 30000 == 0) {
         this.fine("Server being killed, last state is " + this.stateInfo);
      }

      if (this.stateInfo.isStartupAborted()) {
         this.startupAborted = true;
      }

      if (!this.started && this.stateInfo.isStarted()) {
         this.started = true;
         this.notifyAll();
      }

   }

   private void log(Level var1, String var2, Throwable var3) {
      this.serverMgr.log(var1, var2, var3);
      synchronized(this) {
         if (this.proc != null && this.proc.isAlive()) {
            return;
         }
      }

      LogRecord var4 = new LogRecord(var1, var2);
      var4.setParameters(new String[]{"NodeManager"});
      if (var3 != null) {
         var4.setThrown(var3);
      }

      String var5;
      synchronized(this.formatter) {
         var5 = this.formatter.format(var4);
      }

      if (var5.endsWith(EOL)) {
         var5 = var5.substring(0, var5.length() - EOL.length());
      }

      File var6 = this.serverDir.getOutFile();

      try {
         FileWriter var7 = new FileWriter(var6, true);
         var7.write(var5);
         var7.write(EOL);
         var7.close();
      } catch (IOException var8) {
         this.serverMgr.log(Level.WARNING, nmText.msgErrorFileWrite(var6.toString()), var8);
      }

   }

   private void log(Level var1, String var2) {
      this.log(var1, var2, (Throwable)null);
   }

   private void info(String var1) {
      this.log(Level.INFO, var1);
   }

   private void fine(String var1) {
      this.log(Level.FINEST, var1);
   }

   private void severe(String var1, Throwable var2) {
      this.log(Level.SEVERE, var1, var2);
   }
}
