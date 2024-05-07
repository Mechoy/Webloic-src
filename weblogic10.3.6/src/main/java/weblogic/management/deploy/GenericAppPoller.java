package weblogic.management.deploy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.management.deploy.internal.ApplicationPollerLogger;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.Debug;
import weblogic.utils.StringUtils;
import weblogic.work.WorkManagerFactory;

public abstract class GenericAppPoller {
   protected static boolean debug = false;
   protected static boolean methodTrace = false;
   protected boolean verbose;
   private boolean searchRecursively;
   private File startDir;
   private HashMap currentRunFileMap;
   private HashMap lastRunFileMap;
   private ArrayList activateFileList;
   private ArrayList deactivateFileList;
   private long pollInterval;
   private boolean runOnce;
   private Timer pollerTimer;
   private String lastRunFilename;
   protected boolean startDirFound;

   public GenericAppPoller(File var1, boolean var2, long var3) {
      this(var1, var2, var3, (String)null);
   }

   public GenericAppPoller(File var1, boolean var2, long var3, String var5) {
      this.verbose = false;
      this.lastRunFilename = null;
      this.startDirFound = true;
      if (methodTrace) {
         Debug.say("<init>");
      }

      this.searchRecursively = var2;
      this.startDir = var1;
      if (!var1.exists()) {
         this.startDirFound = false;
      }

      if (var3 == 0L) {
         this.runOnce = true;
      } else {
         this.runOnce = false;
      }

      this.pollInterval = var3;
      this.lastRunFilename = var5;
   }

   public abstract void doActivate();

   public abstract void doDeactivate();

   public final void runInSameThread() {
      if (methodTrace) {
         Debug.say("run");
      }

      if (!this.runOnce) {
         if (this.verbose) {
            Debug.say("running in polling mode");
         }

         while(true) {
            while(true) {
               try {
                  if (this.verbose) {
                     Debug.say("evaluating " + this.startDir.getName());
                  }

                  this.doit();
                  if (this.verbose) {
                     Debug.say("done evaluating " + this.startDir.getName());
                  }

                  Thread.sleep(this.pollInterval);
               } catch (InterruptedException var2) {
               } catch (Throwable var3) {
                  ApplicationPollerLogger.logUncaughtThrowable(var3);
               }
            }
         }
      }

      if (this.verbose) {
         Debug.say("running in runOnce mode");
      }

      this.doit();
   }

   public void start() {
      if (methodTrace) {
         Debug.say("run");
      }

      if (this.runOnce) {
         Runnable var1 = new Runnable() {
            public void run() {
               if (GenericAppPoller.this.verbose) {
                  Debug.say("running in runOnce mode");
               }

               GenericAppPoller.this.doit();
            }
         };
         WorkManagerFactory.getInstance().getSystem().schedule(var1);
      } else {
         if (this.verbose) {
            Debug.say("running in polling mode");
         }

         this.startPollerTimerListener();
      }

   }

   public void setSleepInterval(long var1) {
      this.pollInterval = var1;
   }

   protected boolean shouldActivate(File var1) {
      boolean var2 = false;
      String var3 = var1.getAbsolutePath();

      try {
         var1 = var1.getCanonicalFile();
         var3 = var1.getCanonicalPath();
      } catch (IOException var6) {
         ApplicationPollerLogger.logIOException(var6);
      }

      if (debug) {
         Debug.say("SHOULD ACTIVATE: " + var3);
      }

      if (this.lastRunFileMap.containsKey(var3)) {
         long var4 = (Long)this.lastRunFileMap.get(var3);
         if (var1.lastModified() > var4) {
            var2 = true;
         }
      } else {
         var2 = true;
      }

      return var2;
   }

   protected boolean shouldDeactivate(File var1) {
      String var2 = var1.getAbsolutePath();

      try {
         var1 = var1.getCanonicalFile();
         var2 = var1.getCanonicalPath();
      } catch (IOException var4) {
         ApplicationPollerLogger.logIOException(var4);
      }

      if (debug) {
         Debug.say("SHOULD DEACTIVATE: " + var1);
      }

      return !this.currentRunFileMap.containsKey(var2);
   }

   protected final ArrayList getActivateFileList() {
      if (methodTrace) {
         Debug.say("getActivateFileList");
      }

      return this.activateFileList;
   }

   protected final ArrayList getDeactivateFileList() {
      if (methodTrace) {
         Debug.say("getDeactivateFileList");
      }

      return this.deactivateFileList;
   }

   protected final void doit() {
      if (methodTrace) {
         Debug.say("doit");
      }

      try {
         this.getLastRunFileMap();
         this.activateFileList = new ArrayList();
         this.deactivateFileList = new ArrayList();
         this.currentRunFileMap = new HashMap();
         if (debug) {
            Debug.say("START DIR " + this.startDir);
         }

         this.setCurrentRunMap(this.startDir);
      } catch (IOException var4) {
         ApplicationPollerLogger.logIOException(var4);
      }

      this.generateActivateFileList();
      this.generateDeactivateFileList();
      if (debug) {
         this.dumpHashMap("CURRENT RUN FILELIST", this.currentRunFileMap);
         this.dumpArrayList("ACTIVATE FILE LIST: ", this.activateFileList);
         this.dumpHashMap("LAST RUN FILELIST", this.lastRunFileMap);
         this.dumpArrayList("DEACTIVATE FILE LIST: ", this.deactivateFileList);
      }

      try {
         this.doActivate();
         this.doDeactivate();
      } catch (Throwable var3) {
         ApplicationPollerLogger.logUncaughtThrowable(var3);
      }

      try {
         this.setLastRunFileMap();
      } catch (IOException var2) {
         ApplicationPollerLogger.logIOException(var2);
      }

   }

   protected File getStartDir() {
      return this.startDir;
   }

   private void setCurrentRunMap(File var1) throws IOException {
      if (methodTrace) {
         Debug.say("setCurrentRunMap");
      }

      File[] var2 = var1.listFiles();

      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         File var4 = var2[var3];
         String var5 = var4.getAbsolutePath();
         var4 = var4.getCanonicalFile();
         var5 = var4.getCanonicalPath();
         if (this.searchRecursively && var4.isDirectory()) {
            this.setCurrentRunMap(var4);
         }

         if (!this.ignoreFile(var4)) {
            long var6 = var4.lastModified();
            this.currentRunFileMap.put(var5, new Long(var6));
         }
      }

   }

   void removeFileFromMap(File var1) {
      String var2 = var1.getAbsolutePath();

      try {
         var1 = var1.getCanonicalFile();
         var2 = var1.getCanonicalPath();
      } catch (IOException var4) {
         if (debug) {
            Debug.say("Problem getting canonical file and path for file '" + var1 + "'\nwill try to remove: " + var2 + "\nexception message: " + var4.getMessage());
         }
      }

      Object var3 = this.currentRunFileMap.remove(var2);
      if (var3 == null && debug) {
         this.dumpHashMap("Current run file map does not contain key: " + var2, this.currentRunFileMap);
      }

   }

   public long getPollInterval() {
      return this.pollInterval;
   }

   private void generateActivateFileList() {
      if (methodTrace) {
         Debug.say("generateActivateFileList");
      }

      Set var1 = this.currentRunFileMap.keySet();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (this.shouldActivate(new File(var3))) {
            this.activateFileList.add(var3);
         }
      }

   }

   private void generateDeactivateFileList() {
      if (methodTrace) {
         Debug.say("generateDeactivateFileList");
      }

      String var1 = null;
      Set var2 = this.lastRunFileMap.keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         var1 = (String)var3.next();
         if (this.shouldDeactivate(new File(var1))) {
            this.deactivateFileList.add(var1);
         }
      }

   }

   private boolean ignoreFile(File var1) {
      if (methodTrace) {
         Debug.say("ignoreFile");
      }

      return var1.getName().equals(this.lastRunFilename);
   }

   private void getLastRunFileMap() throws IOException {
      if (methodTrace) {
         Debug.say("getLastRunFileMap");
      }

      if (this.lastRunFileMap == null && this.lastRunFilename != null) {
         this.lastRunFileMap = new HashMap();
         File var1 = new File(this.lastRunFilename);
         if (!var1.exists()) {
            if (debug) {
               Debug.say("no previous delete file.");
            }
         } else {
            if (debug) {
               Debug.say("reading file " + this.lastRunFilename);
            }

            BufferedReader var2 = null;

            try {
               var2 = new BufferedReader(new FileReader(new File(this.lastRunFilename)));
               String var3 = null;
               boolean var4 = false;

               while(!var4) {
                  var3 = var2.readLine();
                  if (var3 == null) {
                     var4 = true;
                  } else {
                     String[] var5 = StringUtils.split(var3, '\t');
                     String var6 = new String(var5[0]);
                     long var7 = Long.parseLong(var5[1]);
                     this.lastRunFileMap.put(var6, new Long(var7));
                  }
               }
            } finally {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Exception var15) {
                  }
               }

            }
         }
      }

   }

   private void setLastRunFileMap() throws IOException {
      if (methodTrace) {
         Debug.say("setLastRunFileMap");
      }

      if (!this.lastRunFileMap.equals(this.currentRunFileMap)) {
         this.lastRunFileMap = this.currentRunFileMap;
         if (this.lastRunFilename != null) {
            if (debug) {
               Debug.say("writing file " + this.lastRunFilename);
            }

            BufferedWriter var1 = new BufferedWriter(new FileWriter(new File(this.lastRunFilename)));
            String var2 = new String();
            Set var3 = this.lastRunFileMap.keySet();

            for(Iterator var4 = var3.iterator(); var4.hasNext(); var2 = var2 + "\n") {
               String var5 = (String)var4.next();
               Long var6 = (Long)this.lastRunFileMap.get(var5);
               var2 = var2 + var5;
               var2 = var2 + "\t";
               var2 = var2 + var6.toString();
            }

            var1.write(var2);
            var1.flush();
            var1.close();
         }
      }

   }

   private void startPollerTimerListener() {
      if (this.pollInterval > 0L) {
         this.pollerTimer = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager().schedule(new PollerTimerListener(), this.pollInterval, this.pollInterval);
      }
   }

   private void cancelPollerTimerListener() {
      if (this.pollerTimer != null) {
         this.pollerTimer.cancel();
      }

   }

   protected final void setCheckPoint(File var1, long var2) {
      this.currentRunFileMap.put(var1.getPath(), new Long(var2));
   }

   protected final Long getLastCheckPoint(File var1) {
      return this.lastRunFileMap != null ? (Long)this.lastRunFileMap.get(var1.getPath()) : null;
   }

   protected final void dumpArrayList(String var1, ArrayList var2) {
      StringBuffer var3 = null;
      if (var1 == null) {
         var3 = new StringBuffer("DUMPING ARRAY LIST\n");
      } else {
         var3 = new StringBuffer(var1 + "\n");
      }

      var3.append("ARRAY LIST HAS: " + var2.size() + " ELEMENTS\n");
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         var3.append(var5.toString());
         var3.append("\n");
      }

      Debug.say(var3.toString());
   }

   protected final void dumpHashMap(String var1, Map var2) {
      StringBuffer var3 = null;
      if (var1 == null) {
         var3 = new StringBuffer("DUMPING HASH SET\n");
      } else {
         var3 = new StringBuffer(var1 + "\n");
      }

      var3.append("HASH SET HAS: " + var2.size() + " ELEMENTS\n");
      Set var4 = var2.keySet();
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         var3.append(var6.toString());
         var3.append(": ");
         var3.append(var2.get(var6));
         var3.append("\n");
      }

      Debug.say(var3.toString());
   }

   protected final void dumpStringArray(String var1, String[] var2) {
      StringBuffer var3 = null;
      if (var1 == null) {
         var3 = new StringBuffer("DUMPING STRING ARRAY\n");
      } else {
         var3 = new StringBuffer(var1 + "\n");
      }

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3.append(" ");
         var3.append(var2[var4]);
         if (var4 + 1 < var2.length) {
            var3.append("\n");
         }
      }

      Debug.say(var3.toString());
   }

   private class PollerTimerListener implements TimerListener {
      private PollerTimerListener() {
      }

      public final void timerExpired(Timer var1) {
         try {
            if (GenericAppPoller.this.verbose) {
               Debug.say("evaluating " + GenericAppPoller.this.startDir.getName());
            }

            GenericAppPoller.this.doit();
            if (GenericAppPoller.this.verbose) {
               Debug.say("done evaluating " + GenericAppPoller.this.startDir.getName());
            }
         } catch (Throwable var3) {
            ApplicationPollerLogger.logUncaughtThrowable(var3);
         }

      }

      // $FF: synthetic method
      PollerTimerListener(Object var2) {
         this();
      }
   }
}
