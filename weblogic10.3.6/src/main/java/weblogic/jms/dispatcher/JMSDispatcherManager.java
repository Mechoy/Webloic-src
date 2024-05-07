package weblogic.jms.dispatcher;

import java.lang.reflect.Method;
import javax.naming.Context;
import weblogic.jms.JMSEnvironment;
import weblogic.kernel.KernelStatus;
import weblogic.messaging.dispatcher.Dispatcher;
import weblogic.messaging.dispatcher.DispatcherId;
import weblogic.messaging.dispatcher.DispatcherManager;
import weblogic.work.WorkManager;
import weblogic.work.WorkManagerFactory;

public class JMSDispatcherManager {
   private static final String JMS_REQUEST_QUEUE = "JmsDispatcher";
   private static final String JMS_ASYNC_REQUEST_QUEUE = "JmsAsyncQueue";
   public static final String PREFIX_SERVER_NAME = "weblogic.jms.S:";
   private static final String PREFIX_CLIENT_NAME = "weblogic.jms.C:";
   private static final String SERVER_OBJECT_HANDLER_CLASS_NAME = "weblogic.jms.dispatcher.DispatcherObjectHandler";
   private static final String CLIENT_OBJECT_HANDLER_CLASS_NAME = "weblogic.jms.dispatcher.ClientDispatcherObjectHandler";
   private static DispatcherAdapter localDispatcher;
   private static int exportCount;
   private static WorkManager jmsDispatcherWorkManager;
   private static WorkManager oneWayWorkManager;

   public static void initialize(String var0, String var1, boolean var2) {
      if (localDispatcher == null) {
         int var3;
         if (KernelStatus.isServer()) {
            if (var2) {
               var3 = getJMSThreadPoolSize();
               if (var3 < 5) {
                  var3 = 5;
               }
            } else {
               var3 = 1;
            }
         } else if (JMSEnvironment.getJMSEnvironment().isThinClient()) {
            var3 = 0;
            String var4 = null;

            try {
               var4 = System.getProperty("weblogic.JMSThreadPoolSize");
            } catch (SecurityException var7) {
            }

            if (var4 != null) {
               try {
                  var3 = Integer.parseInt(var4);
               } catch (NumberFormatException var6) {
               }

               if (var3 < 5 && var3 > 0) {
                  var3 = 5;
               }
            }
         } else {
            var3 = getJMSThreadPoolSize();
         }

         if (var2) {
            jmsDispatcherWorkManager = WorkManagerFactory.getInstance().findOrCreate("JmsDispatcher", -1, var3, -1);
         } else {
            jmsDispatcherWorkManager = WorkManagerFactory.getInstance().findOrCreate("JmsDispatcher", 100, var3, -1);
         }

         oneWayWorkManager = WorkManagerFactory.getInstance().findOrCreate("JmsAsyncQueue", 100, 1, -1);
         Dispatcher var8 = DispatcherManager.createLocalDispatcher(var0, var1, jmsDispatcherWorkManager, oneWayWorkManager, JMSEnvironment.getJMSEnvironment().isServer() ? "weblogic.jms.dispatcher.DispatcherObjectHandler" : "weblogic.jms.dispatcher.ClientDispatcherObjectHandler");
         localDispatcher = new DispatcherAdapter(var8);
      }

   }

   private static int getJMSThreadPoolSize() {
      try {
         Class var0 = Class.forName("weblogic.kernel.Kernel");
         Method var1 = var0.getMethod("ensureInitialized");
         var1.invoke((Object)null);
         Method var2 = var0.getMethod("getConfig");
         Object var3 = var2.invoke((Object)null);
         Class var4 = Class.forName("weblogic.management.configuration.KernelMBean");
         Method var5 = var4.getMethod("getJMSThreadPoolSize");
         Object var6 = var5.invoke(var3);
         return (Integer)var6;
      } catch (Throwable var7) {
         throw new Error("Call BEA Support ", var7);
      }
   }

   public static String generateDispatcherName() {
      return "weblogic.jms.C:" + DispatcherManager.getHostAddress() + ":" + Long.toString(System.currentTimeMillis() & 65535L, 36) + ":" + Long.toString(DispatcherManager.generateRandomLong(), 36);
   }

   static WorkManager getWorkManager() {
      return jmsDispatcherWorkManager;
   }

   public static JMSDispatcher getLocalDispatcher() {
      if (localDispatcher != null) {
         return localDispatcher;
      } else {
         String var0 = generateDispatcherName();
         initialize(var0, (String)null, false);
         return localDispatcher;
      }
   }

   public static JMSDispatcher addDispatcherReference(DispatcherWrapper var0) throws weblogic.messaging.dispatcher.DispatcherException {
      Dispatcher var1 = DispatcherManager.addDispatcherReference(var0);
      return new DispatcherAdapter(var1);
   }

   public static JMSDispatcher dispatcherFind(DispatcherId var0) throws weblogic.messaging.dispatcher.DispatcherException {
      Dispatcher var1 = DispatcherManager.dispatcherFind(var0);
      return new DispatcherAdapter(var1);
   }

   public static void removeDispatcherReference(JMSDispatcher var0) {
      Dispatcher var1 = var0.getDelegate();
      DispatcherManager.removeDispatcherReference(var1, false);
   }

   public static JMSDispatcher dispatcherFindOrCreate(Context var0, DispatcherId var1) throws weblogic.messaging.dispatcher.DispatcherException {
      Dispatcher var2 = DispatcherManager.dispatcherFindOrCreate(var0, var1);
      return new DispatcherAdapter(var2);
   }

   public static JMSDispatcher dispatcherFindOrCreate(DispatcherId var0) throws weblogic.messaging.dispatcher.DispatcherException {
      Dispatcher var1 = DispatcherManager.dispatcherFindOrCreate(var0);
      return new DispatcherAdapter(var1);
   }

   public static DispatcherWrapper getLocalDispatcherWrapper() {
      return new DispatcherWrapper((weblogic.messaging.dispatcher.DispatcherImpl)getLocalDispatcher().getDelegate());
   }

   public static synchronized void exportLocalDispatcher() {
      ++exportCount;
      if (exportCount == 1) {
         DispatcherManager.export(getLocalDispatcher().getDelegate());
      }

   }

   public static synchronized void unexportLocalDispatcher() {
      --exportCount;
      if (exportCount == 0) {
         DispatcherManager.unexport(getLocalDispatcher().getDelegate());
      }

   }

   public static String getDispatcherJNDIName(DispatcherId var0) {
      return "weblogic.messaging.dispatcher.S:" + var0;
   }
}
