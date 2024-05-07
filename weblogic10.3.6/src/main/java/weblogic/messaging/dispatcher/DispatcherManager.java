package weblogic.messaging.dispatcher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.HashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.kernel.KernelStatus;
import weblogic.work.WorkManager;

public class DispatcherManager {
   public static final String SERVER_PREFIX = "weblogic.messaging.dispatcher.S:";
   private static final HashMap dispatchers = new HashMap();
   private static Dispatcher localDispatcher;

   private DispatcherManager() {
   }

   public static Dispatcher createLocalDispatcher(String var0, String var1, WorkManager var2, WorkManager var3, String var4) {
      DispatcherId var5 = new DispatcherId(var0, var1);
      DispatcherImpl var6 = new DispatcherImpl(var0, var5, var2, var3, var4);
      Class var7 = DispatcherManager.class;
      synchronized(DispatcherManager.class) {
         localDispatcher = var6;
         dispatchers.put(var5, var6);
         return var6;
      }
   }

   public static Dispatcher dispatcherFindOrCreate(DispatcherId var0) throws DispatcherException {
      try {
         return dispatcherFind(var0);
      } catch (DispatcherException var5) {
         InitialContext var2 = null;

         try {
            var2 = new InitialContext();
         } catch (NamingException var4) {
            throw new DispatcherException(var4.getMessage(), var4);
         }

         return dispatcherCreate(var2, var0, true);
      }
   }

   public static Dispatcher dispatcherFindOrCreate(Context var0, DispatcherId var1) throws DispatcherException {
      return dispatcherCreate(var0, var1, false);
   }

   public static void export(Dispatcher var0) {
      if (var0 instanceof DispatcherImpl) {
         ((DispatcherImpl)var0).export();
      }

   }

   public static void unexport(Dispatcher var0) {
      if (!KernelStatus.isServer()) {
         if (var0 instanceof DispatcherImpl) {
            ((DispatcherImpl)var0).unexport();
         }

      }
   }

   public static String getHostAddress() {
      try {
         InetAddress var0 = InetAddress.getLocalHost();
         return var0.getHostName();
      } catch (UnknownHostException var1) {
         return "UNKNOWN";
      }
   }

   public static long generateRandomLong() {
      SecureRandom var0 = new SecureRandom();
      return var0.nextLong();
   }

   public static Dispatcher addDispatcherReference(DispatcherWrapper var0) throws DispatcherException {
      if (KernelStatus.isServer() && !CrossDomainManager.getCrossDomainUtil().isRemoteDomain(var0)) {
         try {
            Dispatcher var3 = dispatcherFind(var0.getId());
            if (var3 instanceof DispatcherWrapperState) {
               ((DispatcherWrapperState)var3).addRefCount();
            }

            return var3;
         } catch (DispatcherException var2) {
            return dispatcherAdd(var0, true);
         }
      } else {
         DispatcherWrapperState var1 = new DispatcherWrapperState(var0);
         return var1;
      }
   }

   public static boolean isLocal(DispatcherWrapper var0) {
      if (CrossDomainManager.getCrossDomainUtil().isRemoteDomain(var0)) {
         return false;
      } else {
         DispatcherId var1 = var0.getId();
         return var1.getId() == null && localDispatcher.getId().getName().equals(var1.getName()) || localDispatcher.getId().equals(var1);
      }
   }

   public static synchronized void removeDispatcherReference(Dispatcher var0, boolean var1) {
      if (var0 instanceof DispatcherWrapperState) {
         DispatcherWrapperState var2 = (DispatcherWrapperState)var0;
         if (var2.removeRefCount() && !var1) {
            return;
         }

         if (localDispatcher.getId().equals(var0.getId())) {
            return;
         }

         var2.deleteNotify();
         Class var3 = DispatcherManager.class;
         synchronized(DispatcherManager.class) {
            dispatchers.remove(var0.getId());
         }
      }

   }

   public static Dispatcher dispatcherFind(DispatcherId var0) throws DispatcherException {
      Class var2 = DispatcherManager.class;
      Dispatcher var1;
      synchronized(DispatcherManager.class) {
         if (var0 == null) {
            throw new DispatcherException("Dispatcher not found: " + var0);
         }

         if (var0.getId() == null && localDispatcher.getId().getName().equals(var0.getName()) || localDispatcher.getId().equals(var0)) {
            return localDispatcher;
         }

         var1 = (Dispatcher)dispatchers.get(var0);
      }

      if (var1 == null) {
         throw new DispatcherException("Dispatcher not found: " + var0);
      } else {
         return var1;
      }
   }

   private static Dispatcher dispatcherCreate(Context var0, DispatcherId var1, boolean var2) throws DispatcherException {
      DispatcherWrapper var3;
      try {
         var3 = (DispatcherWrapper)var0.lookup("weblogic.messaging.dispatcher.S:" + var1);
      } catch (NamingException var6) {
         DispatcherException var5 = new DispatcherException("could not find Server " + var1);
         var5.initCause(var6);
         throw var5;
      }

      return dispatcherAdd(var3, var2);
   }

   private static Dispatcher dispatcherAdd(DispatcherWrapper var0, boolean var1) throws DispatcherException {
      if (isLocal(var0)) {
         return localDispatcher;
      } else {
         DispatcherWrapperState var2 = new DispatcherWrapperState(var0);
         if (var1) {
            Class var3 = DispatcherManager.class;
            synchronized(DispatcherManager.class) {
               dispatchers.put(var2.getId(), var2);
            }
         }

         return var2;
      }
   }
}
