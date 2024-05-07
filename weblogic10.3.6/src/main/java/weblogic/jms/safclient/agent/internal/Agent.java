package weblogic.jms.safclient.agent.internal;

import java.io.File;
import java.util.HashMap;
import javax.jms.JMSException;
import weblogic.jms.safclient.MessageMigrator;
import weblogic.jms.safclient.agent.AgentManager;
import weblogic.jms.safclient.store.StoreUtils;
import weblogic.messaging.kernel.Kernel;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.Queue;
import weblogic.messaging.kernel.Quota;
import weblogic.messaging.kernel.QuotaPolicy;
import weblogic.messaging.kernel.internal.KernelImpl;
import weblogic.store.xa.PersistentStoreXA;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class Agent {
   public static final String AGENT_WM_PREFIX = "client.SAF.";
   public static final String NORMAL_WM_NAME = ".System";
   public static final String LIMITED_WM_NAME = ".Limited";
   private static final String ASYNC_WM_NAME = ".AsyncPush";
   public static final int LIMITED_WM_NUM_THREADS = 8;
   private static final String QUOTA_NAME = "client.SAF.Quota";
   public static final String AGENT_TM_PREFIX = "client.SAF.";
   public static final String DIRECT_TIMER_EXT = ".direct";
   private String name;
   private File rootDirectory;
   private WorkManager workManager;
   private WorkManager limitedWorkManager;
   private WorkManager asyncPushWorkManager;
   private String limitedTimerManagerName;
   private String directTimerManagerName;
   private long bytesMaximum = -1L;
   private int messagesMaximum = -1;
   private long messageBufferSize = -1L;
   private int maximumMessageSize = Integer.MAX_VALUE;
   private long defaultRetryDelayBase = 20000L;
   private long defaultRetryDelayMaximum = 180000L;
   private double defaultRetryDelayMultiplier = 1.0;
   private int windowSize = 10;
   private boolean loggingEnabled = true;
   private int windowInterval = 0;
   private HashMap destinations = new HashMap();
   private Kernel kernel;
   private Quota quota;

   public Agent(String var1, File var2) {
      this.name = var1;
      this.rootDirectory = var2;
   }

   public String getName() {
      return this.name;
   }

   public void setBytesMaximum(long var1) {
      this.bytesMaximum = var1;
   }

   public void setMessagesMaximum(long var1) {
      this.messagesMaximum = (int)var1;
   }

   public void setMessageBufferSize(long var1) {
      this.messageBufferSize = var1;
   }

   public void setMaximumMessageSize(int var1) {
      this.maximumMessageSize = var1;
   }

   public WorkManager getAsyncPushWorkManager() {
      return this.asyncPushWorkManager;
   }

   public long getDefaultRetryDelayBase() {
      return this.defaultRetryDelayBase;
   }

   public void setDefaultRetryDelayBase(long var1) {
      this.defaultRetryDelayBase = var1;
   }

   public long getDefaultRetryDelayMaximum() {
      return this.defaultRetryDelayMaximum;
   }

   public void setDefaultRetryDelayMaximum(long var1) {
      this.defaultRetryDelayMaximum = var1;
   }

   public double getDefaultRetryDelayMultiplier() {
      return this.defaultRetryDelayMultiplier;
   }

   public void setDefaultRetryDelayMultiplier(double var1) {
      this.defaultRetryDelayMultiplier = var1;
   }

   public void setLoggingEnabled(boolean var1) {
      this.loggingEnabled = var1;
   }

   public boolean isLoggingEnabled() {
      return this.loggingEnabled;
   }

   public int getWindowSize() {
      return this.windowSize;
   }

   public void setWindowSize(int var1) {
      this.windowSize = var1;
   }

   public int getWindowInterval() {
      return this.windowInterval;
   }

   public void setWindowInterval(int var1) {
      this.windowInterval = var1;
   }

   public void initialize() throws JMSException {
      File var1 = new File(this.rootDirectory, "paging");
      if (!var1.exists() && !var1.mkdirs()) {
         throw new JMSException("Unable to create paging directory " + var1.getAbsolutePath());
      } else if (!var1.isDirectory()) {
         throw new JMSException("The file " + var1.getAbsolutePath() + " must be a directory, it will be used for the paging store");
      } else {
         PersistentStoreXA var2 = StoreUtils.getStore(this.rootDirectory);
         if (var2 == null) {
            throw new JMSException("Could not find default store");
         } else {
            HashMap var3 = new HashMap(5);
            WorkManagerFactory var4 = WorkManagerFactory.getInstance();
            if (MessageMigrator.revertBugFix) {
               this.workManager = var4.findOrCreate("client.SAF." + this.name + ".System", 100, 1, -1);
               this.limitedWorkManager = var4.findOrCreate("client.SAF." + this.name + ".Limited", 1, 8);
               this.asyncPushWorkManager = WorkManagerFactory.getInstance().findOrCreate("client.SAF." + this.name + ".AsyncPush", 100, 1, -1);
            } else {
               String var5 = AgentManager.getManagerSequence();
               this.workManager = var4.findOrCreate("client.SAF." + this.name + var5 + ".System", 100, 1, -1);
               this.limitedWorkManager = var4.findOrCreate("client.SAF." + this.name + var5 + ".Limited", 1, 8);
               this.asyncPushWorkManager = WorkManagerFactory.getInstance().findOrCreate("client.SAF." + this.name + var5 + ".AsyncPush", 100, 1, -1);
               this.limitedTimerManagerName = "client.SAF." + this.name + var5;
               this.directTimerManagerName = "client.SAF." + this.name + var5 + ".direct";
               var3.put("LimitedTimerManagerName", this.limitedTimerManagerName);
               var3.put("DirectTimerManagerName", this.directTimerManagerName);
            }

            var3.put("WorkManager", this.workManager);
            var3.put("LimitedWorkManager", this.limitedWorkManager);
            var3.put("PagingDirectory", var1.getAbsolutePath());

            try {
               this.kernel = new KernelImpl(this.name, var3);
               this.kernel.setProperty("Store", var2);
               this.kernel.open();
            } catch (KernelException var8) {
               throw new weblogic.jms.common.JMSException(var8);
            }

            try {
               this.kernel.setProperty("MessageBufferSize", new Long(this.messageBufferSize < 0L ? Long.MAX_VALUE : this.messageBufferSize));
               this.kernel.setProperty("MaximumMessageSize", new Integer(this.maximumMessageSize < 0 ? Integer.MAX_VALUE : this.maximumMessageSize));
            } catch (KernelException var7) {
               throw new weblogic.jms.common.JMSException(var7);
            }

            try {
               this.quota = this.kernel.createQuota("client.SAF.Quota");
            } catch (KernelException var6) {
               throw new weblogic.jms.common.JMSException(var6);
            }

            this.quota.setPolicy(QuotaPolicy.FIFO);
            this.quota.setBytesMaximum(this.bytesMaximum < 0L ? Long.MAX_VALUE : this.bytesMaximum);
            this.quota.setMessagesMaximum(this.messagesMaximum < 0 ? Integer.MAX_VALUE : this.messagesMaximum);
         }
      }
   }

   public PersistentStoreXA getPersistentStore() {
      return (PersistentStoreXA)this.kernel.getProperty("Store");
   }

   public Queue addConfiguredDestination(String var1) throws weblogic.jms.common.JMSException {
      Queue var2 = null;
      HashMap var3 = new HashMap();
      var3.put("Durable", new Boolean(true));
      var3.put("Quota", this.quota);
      var3.put("MaximumMessageSize", new Integer(this.maximumMessageSize < 0 ? Integer.MAX_VALUE : this.maximumMessageSize));
      boolean var15 = false;

      try {
         var15 = true;
         var2 = this.kernel.findQueue(var1);
         if (var2 == null) {
            try {
               var2 = this.kernel.createQueue(var1, var3);
            } catch (KernelException var20) {
               throw new weblogic.jms.common.JMSException(var20);
            }
         } else {
            try {
               var2.setProperties(var3);
            } catch (KernelException var19) {
               throw new weblogic.jms.common.JMSException(var19);
            }
         }

         try {
            var2.resume(16384);
            var15 = false;
         } catch (KernelException var18) {
            var2 = null;
            throw new weblogic.jms.common.JMSException(var18);
         }
      } finally {
         if (var15) {
            if (var2 != null) {
               synchronized(this.destinations) {
                  this.destinations.put(var1, var2);
               }
            }

         }
      }

      if (var2 != null) {
         synchronized(this.destinations) {
            this.destinations.put(var1, var2);
         }
      }

      return var2;
   }

   public synchronized void shutdown() throws JMSException {
      try {
         if (this.kernel != null) {
            this.kernel.close();
         }

      } catch (KernelException var2) {
         throw new weblogic.jms.common.JMSException(var2);
      }
   }
}
