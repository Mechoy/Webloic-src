package weblogic.management.logging;

import java.security.AccessController;
import java.util.Iterator;
import java.util.List;
import javax.management.MBeanException;
import weblogic.logging.LogEntry;
import weblogic.management.ManagementException;
import weblogic.management.jmx.modelmbean.NotificationGenerator;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.LogBroadcasterRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.transaction.TxHelper;
import weblogic.utils.collections.CopyOnWriteArrayList;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public final class LogBroadcaster extends RuntimeMBeanDelegate implements LogBroadcasterRuntimeMBean {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static RuntimeAccess runtimeAccess;
   static final String BASE_TYPE = "weblogic.log.";
   private static final long serialVersionUID = 7795992271907801150L;
   private static final boolean DEBUG = false;
   private long messagesLogged;
   private List generators;
   private WorkManager workManager;

   public static LogBroadcaster getLogBroadcaster() throws ManagementException {
      return LogBroadcaster.SingletonFactory.getSingleton();
   }

   private LogBroadcaster() throws ManagementException {
      super("TheLogBroadcaster");
      this.generators = new CopyOnWriteArrayList();
      this.workManager = WorkManagerFactory.getInstance().findOrCreate("weblogic.logging.LogBroadcaster", 1, -1);
   }

   void addNotificationGenerator(NotificationGenerator var1) {
      this.generators.add(var1);
   }

   public long getMessagesLogged() {
      return this.messagesLogged;
   }

   public void translateLogEntry(final LogEntry var1) {
      if (var1.getSeverity() < 128) {
         if (this.isTranslationNeeded()) {
            String var2 = var1.getUserId();
            if (var2 == null) {
               var2 = "";
            }

            String var3 = TxHelper.getTransactionId();
            if (var3 == null) {
               var3 = "";
            }

            final String var4 = "weblogic.log." + var1.getSubsystem() + "." + var1.getId();
            ++this.messagesLogged;
            final Iterator var5 = this.generators.iterator();
            this.workManager.schedule(new Runnable() {
               public void run() {
                  while(var5.hasNext()) {
                     NotificationGenerator var1x = (NotificationGenerator)var5.next();
                     if (var1x.isSubscribed()) {
                        WebLogicLogNotification var2 = new WebLogicLogNotification(var4, LogBroadcaster.this.messagesLogged, var1x.getObjectName(), var1);

                        try {
                           var1x.sendNotification(var2);
                           var1x.incrementSequenceNumber();
                        } catch (MBeanException var4x) {
                        }
                     }
                  }

               }
            });
         }
      }
   }

   private boolean isTranslationNeeded() {
      boolean var1 = false;
      Iterator var2 = this.generators.iterator();

      while(var2.hasNext()) {
         NotificationGenerator var3 = (NotificationGenerator)var2.next();
         if (var3.isSubscribed()) {
            var1 = true;
         }
      }

      return var1;
   }

   // $FF: synthetic method
   LogBroadcaster(Object var1) throws ManagementException {
      this();
   }

   static {
      runtimeAccess = ManagementService.getRuntimeAccess(kernelId);
   }

   private static final class SingletonFactory {
      private static LogBroadcaster singleton = null;

      private static LogBroadcaster getSingleton() throws ManagementException {
         if (singleton == null) {
            singleton = new LogBroadcaster();
         }

         return singleton;
      }
   }
}
