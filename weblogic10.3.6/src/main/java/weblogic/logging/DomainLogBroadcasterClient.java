package weblogic.logging;

import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.Environment;
import weblogic.management.configuration.LogMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.logging.DomainLogHandler;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.URLManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class DomainLogBroadcasterClient {
   private static final int MAX_BUFFER_SIZE = 100;
   private static final DebugLogger DEBUG = DebugLogger.getDebugLogger("DebugDomainLogHandler");
   private static DomainLogBroadcasterClient singleton = null;
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(SubjectManager.getKernelIdentityAction());
   private static final int MIN_RETRY_COUNT = 1;
   private static final int MAX_RETRY_COUNT = 10;
   private static final long RETRY_SLEEP_INTERVAL = 5000L;
   private ArrayList logBuffer;
   private LogMBean logConfig;
   private DomainLogHandler domainLogHandler = null;
   private long messagesBroadcastedToDomain = 0L;
   boolean closed = false;
   private WorkManager workManager;
   private Object logBufferLock = new Object();

   public static synchronized DomainLogBroadcasterClient getInstance() {
      if (singleton == null) {
         singleton = new DomainLogBroadcasterClient();
      }

      return singleton;
   }

   private DomainLogBroadcasterClient() {
      ServerMBean var1 = ManagementService.getRuntimeAccess(KERNEL_ID).getServer();
      this.logConfig = var1.getLog();
      this.logBuffer = new ArrayList(100);
      this.workManager = WorkManagerFactory.getInstance().findOrCreate("weblogic.logging.DomainLogBroadcasterClient", 1, -1);
   }

   public void initDomainLogHandler(final boolean var1) {
      this.workManager.schedule(new Runnable() {
         public void run() {
            DomainLogBroadcasterClient.this.initDomainLogHandlerAsync(var1);
         }
      });
   }

   public DomainLogHandler getDomainLogHandler() {
      return this.domainLogHandler;
   }

   private synchronized void initDomainLogHandlerAsync(boolean var1) {
      byte var2 = 1;
      if (var1) {
         var2 = 10;
      }

      if (this.domainLogHandler != null && var1) {
         try {
            this.domainLogHandler.ping();
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Ping worked, no need to lookup again");
            }

            return;
         } catch (RemoteException var22) {
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug("Remote reference is bad");
            }

            this.domainLogHandler = null;
         }
      }

      Exception var3 = null;
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Entering loop with retryCount=" + var2);
      }

      for(int var4 = 0; var4 < var2; ++var4) {
         Context var5 = null;

         try {
            try {
               Environment var6 = new Environment();
               var6.setProviderUrl(URLManager.findAdministrationURL(ManagementService.getRuntimeAccess(KERNEL_ID).getAdminServerName()));
               var5 = var6.getInitialContext();
               this.domainLogHandler = (DomainLogHandler)var5.lookup("weblogic.logging.DomainLogHandler");
               LogMgmtLogger.logDomainLogHandlerInitialized();
               break;
            } catch (Exception var20) {
               var3 = var20;
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Got exception while ctx lookup, retryCount=" + var4, var20);
               }
            }

            try {
               Thread.currentThread();
               Thread.sleep(5000L);
            } catch (InterruptedException var19) {
               if (DEBUG.isDebugEnabled()) {
                  DEBUG.debug("Got exception while sleeping", var19);
               }
            }
         } finally {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (NamingException var18) {
               }
            }

         }
      }

      if (this.domainLogHandler == null) {
         LogMgmtLogger.logCannotGetDomainLogHandler(var3);
      } else {
         this.flush();
      }

   }

   public void broadcast(LogEntry var1) {
      ArrayList var2;
      synchronized(this.logBufferLock) {
         if (this.closed) {
            return;
         }

         this.logBuffer.add(var1);
         int var4 = this.logConfig.getDomainLogBroadcasterBufferSize();
         if (this.domainLogHandler == null) {
            var4 = 100;
         }

         if (this.logBuffer.size() >= var4) {
            if (this.domainLogHandler != null) {
               var2 = this.logBuffer;
               this.logBuffer = new ArrayList(this.logConfig.getDomainLogBroadcasterBufferSize());
            } else {
               if (this.logBuffer.size() > var4) {
                  this.logBuffer.remove(0);
               }

               var2 = null;
            }
         } else {
            var2 = null;
         }
      }

      if (var2 != null) {
         this.scheduleLogBroadcast(var2);
      }

   }

   public void flush() {
      ArrayList var1;
      synchronized(this.logBufferLock) {
         int var3 = this.logBuffer.size();
         if (var3 == 0) {
            return;
         }

         var1 = this.logBuffer;
         this.logBuffer = new ArrayList(this.logConfig.getDomainLogBroadcasterBufferSize());
      }

      this.scheduleLogBroadcast(var1);
   }

   private void scheduleLogBroadcast(final ArrayList var1) {
      this.workManager.schedule(new Runnable() {
         public void run() {
            DomainLogHandler var1x = DomainLogBroadcasterClient.this.domainLogHandler;
            LogEntry[] var2 = new LogEntry[var1.size()];
            var1.toArray(var2);

            try {
               if (var1x != null) {
                  var1x.publishLogEntries(var2);
               }
            } catch (RemoteException var8) {
               try {
                  if (var1x != null) {
                     var1x.publishLogEntries(var2);
                  }
               } catch (RemoteException var7) {
                  DomainLogBroadcasterClient.this.domainLogHandler = null;
                  LogMgmtLogger.logCannotGetDomainLogHandler(var8);
                  if (DomainLogBroadcasterClient.DEBUG.isDebugEnabled()) {
                     StringBuilder var5 = new StringBuilder();

                     for(int var6 = 0; var6 < var2.length; ++var6) {
                        var5.append('[');
                        var5.append(var2[var6].getLogMessage());
                        var5.append(']');
                     }

                     DomainLogBroadcasterClient.DEBUG.debug("Failed to send messages to the domain: " + var5.toString(), var8);
                  }
               }
            }

         }
      });
   }

   public void close() {
      synchronized(this.logBufferLock) {
         this.closed = true;
         this.logBuffer.clear();
      }
   }

   public void sendALAlertTrap(final String var1, final String var2, final String var3, final String var4, final String var5, final String var6, final String var7, final String var8, final String var9, final String var10, final String var11, final String var12) throws Exception {
      try {
         final DomainLogHandler var13 = this.domainLogHandler;
         if (var13 != null) {
            this.workManager.schedule(new Runnable() {
               public void run() {
                  try {
                     var13.sendALAlertTrap(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
                  } catch (RemoteException var2x) {
                     if (DomainLogBroadcasterClient.DEBUG.isDebugEnabled()) {
                        DomainLogBroadcasterClient.DEBUG.debug("Failed to send AquaLogic Alert trap to the domain", var2x);
                     }
                  }

               }
            });
         } else {
            Loggable var14 = LogMgmtLogger.logDomainLogHandlerNotAvailableForTrapLoggable();
            if (DEBUG.isDebugEnabled()) {
               DEBUG.debug(var14.getMessageBody());
            }

            throw new Exception(var14.getMessageBody());
         }
      } catch (RemoteException var15) {
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug("Failed to send AquaLogic Alert trap to the domain", var15);
         }

         throw new Exception(var15.getMessage());
      }
   }

   public void sendTrap(final String var1, final List<Object[]> var2) throws RemoteException {
      final DomainLogHandler var3 = this.domainLogHandler;
      if (var3 != null) {
         this.workManager.schedule(new Runnable() {
            public void run() {
               try {
                  if (DomainLogBroadcasterClient.DEBUG.isDebugEnabled()) {
                     DomainLogBroadcasterClient.DEBUG.debug("Sending trap " + var1);
                  }

                  var3.sendTrap(var1, var2);
               } catch (RemoteException var2x) {
                  if (DomainLogBroadcasterClient.DEBUG.isDebugEnabled()) {
                     DomainLogBroadcasterClient.DEBUG.debug("Failed to send trap to the domain", var2x);
                  }
               }

            }
         });
      } else {
         Loggable var4 = LogMgmtLogger.logDomainLogHandlerNotAvailableForTrapLoggable();
         if (DEBUG.isDebugEnabled()) {
            DEBUG.debug(var4.getMessageBody());
         }

         throw new RemoteException(var4.getMessageBody());
      }
   }
}
