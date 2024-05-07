package weblogic.messaging.path.helper;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.transaction.Transaction;
import weblogic.cache.lld.ChangeListener;
import weblogic.cache.lld.LLDFactory;
import weblogic.cache.utils.ExpiredMap;
import weblogic.cluster.ClusterService;
import weblogic.common.CompletionListener;
import weblogic.common.CompletionRequest;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.health.LowMemoryNotificationService;
import weblogic.health.MemoryEvent;
import weblogic.health.MemoryListener;
import weblogic.jms.extensions.JMSOrderException;
import weblogic.jndi.Environment;
import weblogic.messaging.path.AsyncMap;
import weblogic.messaging.path.AsyncMapRemote;
import weblogic.messaging.path.ExceptionAdapter;
import weblogic.messaging.path.Key;
import weblogic.messaging.path.Member;
import weblogic.rjvm.PeerGoneException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.transaction.TransactionManager;
import weblogic.transaction.TxHelper;
import weblogic.utils.collections.ConcurrentHashMap;

public class PathHelper {
   public static final DebugLogger PathSvc = DebugLogger.getDebugLogger("DebugPathSvc");
   public static final DebugLogger PathSvcVerbose = DebugLogger.getDebugLogger("DebugPathSvcVerbose");
   public static boolean retired = false;
   private static Object lock = new Object();
   private HashMap servers = new HashMap();
   private static ServerInfo clusterServerInfo;
   public static final int QOS_CLEAR_CACHE = 8;
   public static final int QOS_OWNED_CACHE = 64;
   public static final int QOS_DIRTY_CACHE = 512;
   public static final int QOS_STORE = 32768;
   public static final int QOS_CACHE_ON_EQUAL = 16384;
   private static final int QOS_local = 584;
   private static PathHelper THE_ONE = new PathHelper();
   public static String DEFAULT_PATH_SERVICE_JNDI = "weblogic.PathService.default";
   private static final JMSOrderExceptionAdapter jmsOrderExceptionAdapter = new JMSOrderExceptionAdapter();
   private TransactionManager tranManager = TxHelper.getTransactionManager();
   private static final int Retry_Put_If_Absent = 0;
   private static final int Retry_Remove = 1;
   private static final int Retry_Put = 2;
   private static final int Retry_Get = 3;
   private static final String PATH_SERVICE = "BEA.PathService";

   private PathHelper() {
   }

   public static PathHelper manager() {
      return THE_ONE;
   }

   public final void cachedGet(String var1, Key var2, int var3, CompletionRequest var4) throws NamingException {
      Transaction var5 = this.tranManager.forceSuspend();

      try {
         this.cachedGetNoTx(var1, var2, var3, var4);
      } finally {
         this.tranManager.forceResume(var5);
      }

   }

   private void cachedGetNoTx(String var1, Key var2, int var3, CompletionRequest var4) throws NamingException {
      ServerInfo var5 = this.createServerInfo(var1);
      if ((584 & var3) != 0) {
         Member var6 = this.useCache(var5, var2, var3);
         if (var6 != null) {
            var4.setResult(var6);
            return;
         }
      }

      if (('耀' & var3) != 0) {
         AsyncMap var9 = this.lookupAsyncMap(var1, var5);
         int var7 = var3 & -32777;
         CompletionRequest var8;
         if (var7 != 0) {
            var8 = this.updateCacheCR(var5, var2, (Member)null, var7, var4);
         } else {
            var8 = var4;
         }

         var9.get(var2, new RetryOnce(3, var1, var2, (Member)null, var9, var8));
      } else {
         var4.setResult((Object)null);
      }

   }

   public final Member cachedGet(String var1, Key var2, int var3) throws Throwable {
      ServerInfo var4;
      try {
         var4 = this.createServerInfo(var1);
      } catch (NamingException var8) {
         throw var8;
      }

      if ((584 & var3) != 0) {
         Member var5 = this.useCache(var4, var2, var3);
         if (var5 != null) {
            return var5;
         }
      }

      if (('耀' & var3) != 0) {
         AsyncMap var9 = this.lookupAsyncMap(var1, var4);
         int var6 = var3 & -32777;
         CompletionRequest var7;
         if (var6 != 0) {
            var7 = this.updateCacheCR(var4, var2, (Member)null, var6, (CompletionRequest)null);
         } else {
            var7 = new CompletionRequest();
         }

         var9.get(var2, new RetryOnce(3, var1, var2, (Member)null, var9, var7));
         return (Member)var7.getResult();
      } else {
         return null;
      }
   }

   public final void cachedRemove(String var1, Key var2, Member var3, int var4, CompletionRequest var5) throws NamingException {
      ServerInfo var6 = this.createServerInfo(var1);
      if (('耀' & var4) != 0) {
         var4 |= 584;
      }

      Member var7 = this.useCache(var6, var2, var4);
      if (('耀' & var4) == 0) {
         var5.setResult(var7);
      } else {
         AsyncMap var8 = this.lookupAsyncMap(var1, var6);
         var8.remove(var2, var3, new RetryOnce(1, var1, var2, var3, var8, var5));
      }
   }

   public final boolean cachedRemove(String var1, Key var2, Member var3, int var4) throws NamingException, PathServiceException {
      ServerInfo var5;
      try {
         var5 = this.createServerInfo(var1);
      } catch (NamingException var13) {
         throw var13;
      }

      if (('耀' & var4) != 0) {
         var4 |= 584;
      }

      Member var6 = this.useCache(var5, var2, var4);
      if (('耀' & var4) == 0) {
         return var6 != null;
      } else {
         CompletionRequest var7 = new CompletionRequest();

         try {
            AsyncMap var8 = this.lookupAsyncMap(var1, var5);
            var8.remove(var2, var3, new RetryOnce(1, var1, var2, var3, var8, var7));
         } catch (NamingException var12) {
            throw var12;
         }

         try {
            return Boolean.TRUE == var7.getResult();
         } catch (RuntimeException var9) {
            throw var9;
         } catch (Error var10) {
            throw var10;
         } catch (Throwable var11) {
            throw new PathServiceException(var11);
         }
      }
   }

   public final void cachedPutIfAbsent(String var1, Key var2, Member var3, int var4, CompletionRequest var5) throws NamingException {
      Transaction var6 = this.tranManager.forceSuspend();

      try {
         this.cachedPutIfAbsentNoTx(var1, var2, var3, var4, var5);
      } finally {
         this.tranManager.forceResume(var6);
      }

   }

   private void cachedPutIfAbsentNoTx(String var1, Key var2, Member var3, int var4, CompletionRequest var5) throws NamingException {
      ServerInfo var6;
      try {
         var6 = this.createServerInfo(var1);
      } catch (NamingException var10) {
         throw var10;
      }

      if ((584 & var4) != 0) {
         Member var7 = this.useCache(var6, var2, var4);
         if (('耀' & var4) == 0) {
            if ((512 & var4) != 0) {
               var5.setResult(var6.dirtyPutIfAbsent(var2, var3));
            } else if ((64 & var4) != 0) {
               var5.setResult(var6.ownedPutIfAbsent(var2, var3));
            }

            return;
         }

         if (var7 != null) {
            var5.setResult(var7);
            return;
         }
      }

      if (('耀' & var4) != 0) {
         AsyncMap var11 = this.lookupAsyncMap(var1, var6);
         int var8 = var4 & -32777;
         CompletionRequest var9;
         if (var8 != 0) {
            var9 = this.updateCacheCR(var6, var2, var3, var8, var5);
         } else {
            var9 = var5;
         }

         var11.putIfAbsent(var2, var3, new RetryOnce(0, var1, var2, var3, var11, var9));
      } else {
         var5.setResult((Object)null);
      }

   }

   public final void update(String var1, Key var2, Member var3, CompletionRequest var4) throws NamingException {
      Transaction var5 = this.tranManager.forceSuspend();

      try {
         this.updateNoTx(var1, var2, var3, var4);
      } finally {
         this.tranManager.forceResume(var5);
      }

   }

   private void updateNoTx(String var1, Key var2, Member var3, CompletionRequest var4) throws NamingException {
      ServerInfo var5 = this.createServerInfo(var1);
      this.useCache(var5, var2, 584);
      AsyncMap var6 = this.lookupAsyncMap(var1, var5);
      var6.put(var2, var3, new RetryOnce(2, var1, var2, var3, var6, var4));
   }

   private CompletionRequest updateCacheCR(final ServerInfo var1, final Key var2, final Member var3, final int var4, CompletionRequest var5) {
      return new UpdateCache(var5) {
         public void onException(CompletionRequest var1x, Throwable var2x) {
            if (this.userCompletionRequest != null) {
               this.userCompletionRequest.setResult(var2x);
            }

         }

         public void onCompletion(CompletionRequest var1x, Object var2x) {
            if ((512 & var4) != 0) {
               if ((var2x != null || var3 != null) && ((16384 & var4) == 0 || var3.equals(var2x))) {
                  var1.dirtyPut(var2, var2x == null ? var3 : (Member)var2x);
               } else {
                  var1.dirtyRemove(var2);
               }
            } else if ((64 & var4) != 0) {
               if ((var2x != null || var3 != null) && ((16384 & var4) == 0 || var3.equals(var2x))) {
                  var1.ownedPut(var2, var2x == null ? var3 : (Member)var2x);
               } else {
                  var1.ownedRemove(var2);
               }
            }

            if (this.userCompletionRequest != null) {
               boolean var3x = this.userCompletionRequest.runListenersInSetResult(true);

               try {
                  this.userCompletionRequest.setResult(var2x);
               } finally {
                  if (var3x) {
                     return;
                  }

                  this.userCompletionRequest.runListenersInSetResult(false);
               }
            }

         }
      };
   }

   private Member useCache(ServerInfo var1, Key var2, int var3) {
      Member var4;
      if ((64 & var3) != 0) {
         if ((8 & var3) != 0) {
            var4 = var1.ownedRemove(var2);
         } else {
            var4 = var1.ownedGet(var2);
            if (var4 != null) {
               return var4;
            }
         }
      } else {
         var4 = null;
      }

      Member var5;
      if ((512 & var3) != 0) {
         if ((8 & var3) != 0) {
            var5 = var1.dirtyRemove(var2);
         } else {
            var5 = var1.dirtyGet(var2);
            if (var5 != null) {
               return var5;
            }
         }
      } else {
         var5 = null;
      }

      return var4 == null ? var5 : var4;
   }

   private static String broadcastJndiName(String var0) {
      return "BEA.PathService" + var0;
   }

   public ChangeListener getDirtyCacheUpdaterMap(String var1) {
      synchronized(lock) {
         return ((ServerInfo)this.servers.get(var1)).invalidator;
      }
   }

   private ServerInfo findServerInfo(String var1) {
      synchronized(lock) {
         return (ServerInfo)this.servers.get(var1);
      }
   }

   private AsyncMap lookupAsyncMap(String var1, ServerInfo var2) throws NamingException {
      AsyncMap var3 = var2.delegate;
      if (var3 != null) {
         return var3;
      } else {
         synchronized(lock) {
            var3 = var2.delegate;
            if (var3 != null) {
               return var3;
            } else {
               AsyncMapRemoteAdapter var7 = new AsyncMapRemoteAdapter(var1, (AsyncMapRemote)this.jndiLookup(var1), jmsOrderExceptionAdapter());
               var2.delegate = var7;
               return var7;
            }
         }
      }
   }

   private Object jndiLookup(String var1) throws NamingException {
      Environment var2 = new Environment();
      var2.setCreateIntermediateContexts(true);
      var2.setReplicateBindings(true);
      Context var3 = var2.getInitialContext();
      AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      SecurityServiceManager.pushSubject(var4, var4);

      Object var5;
      try {
         var5 = var3.lookup(var1);
      } finally {
         SecurityServiceManager.popSubject(var4);
      }

      return var5;
   }

   private ServerInfo createServerInfo(String var1) throws NamingException {
      synchronized(lock) {
         ServerInfo var2 = (ServerInfo)this.servers.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            AsyncMapRemote var4 = (AsyncMapRemote)this.jndiLookup(var1);
            var2 = new ServerInfo(var1, new AsyncMapRemoteAdapter(var1, var4, jmsOrderExceptionAdapter()));
            ServerInfo var5 = (ServerInfo)this.servers.put(var1, var2);
            if (var5 == null) {
               return var2;
            } else {
               var5.delegate = var2.delegate;
               this.servers.put(var1, var5);
               return var5;
            }
         }
      }
   }

   public static JMSOrderExceptionAdapter jmsOrderExceptionAdapter() {
      return jmsOrderExceptionAdapter;
   }

   public static Throwable wrapExtensionImpl(Throwable var0) {
      return (Throwable)(var0 != null && !(var0 instanceof RuntimeException) && !(var0 instanceof Error) && !(var0 instanceof JMSOrderException) ? new JMSOrderException(var0.getMessage(), var0) : var0);
   }

   public void register(boolean var1, String var2, AsyncMap var3) {
      ServerInfo var4 = null;
      ServerInfo var5;
      ServerInfo var6;
      synchronized(lock) {
         if (var1) {
            var4 = new ServerInfo(var2, var3);
         }

         var5 = clusterServerInfo;
         clusterServerInfo = var4;
         if (var1) {
            var6 = (ServerInfo)this.servers.put(var2, var4);
         } else {
            var6 = (ServerInfo)this.servers.remove(var2);
         }
      }

      if (var1) {
         if (var5 != null || var6 != null) {
            PathSvc.debug("\n\nPathService double registration? remote and local? " + var2, new Exception("debug deploy PathService" + var5 + var6));
         }
      } else if (var5 != null && var5.delegate != var3 || var6 != null && var6.delegate != var3) {
         PathSvc.debug("\n\nPathService double unregistration? remote and local? " + var2, new Exception("debug deploy PathService" + var5 + var6));
      }

   }

   public void unRegister(String var1) {
      synchronized(lock) {
         ServerInfo var3 = (ServerInfo)this.servers.remove(var1);
         if (var3 != null && var3.invalidator != null) {
            var3.dirty.clear();
            var3.invalidator = null;
         }
      }
   }

   void handleException(Exception var1, String var2, AsyncMap var3) {
      if (var3 instanceof AsyncMapRemoteAdapter && var1 instanceof RemoteException) {
         synchronized(lock) {
            ServerInfo var5 = this.findServerInfo(var2);
            if (var5 == null || var5.delegate != var3) {
               return;
            }

            this.unRegister(var2);
         }
      }

   }

   private class LowMemoryForgetfulMap implements Map, MemoryListener {
      private int LOWMEM_SIZE = 4096;
      private int NORMAL_SIZE = 1048576;
      private long LOWMEM_DELAY = 60000L;
      private long NORMAL_DELAY = 480000L;
      private long currentDelay;
      private int currentSize;
      ExpiredMap expiredMap;

      LowMemoryForgetfulMap() {
         LowMemoryNotificationService.addMemoryListener(this);
         this.normalUOOMemory();
         this.allocateNormal();
      }

      private void allocateNormal() {
         this.expiredMap = new ExpiredMap(this.currentSize, new HashMap(16), this.currentDelay);
      }

      private void normalUOOMemory() {
         this.currentSize = this.NORMAL_SIZE;
         this.currentDelay = this.NORMAL_DELAY;
      }

      public void memoryChanged(MemoryEvent var1) {
         if (var1.getEventType() == 1) {
            synchronized(this.expiredMap) {
               this.currentSize = this.LOWMEM_SIZE;
               this.currentDelay = this.LOWMEM_DELAY;
               this.expiredMap.clear();
               this.allocateNormal();
            }
         } else if (var1.getEventType() == 0) {
            synchronized(this.expiredMap) {
               ExpiredMap var3 = this.expiredMap;
               this.normalUOOMemory();
               this.allocateNormal();
               if (var3.size() > 0) {
                  this.expiredMap.putAll(var3);
               }
            }
         }

      }

      public int size() {
         return this.expiredMap.size();
      }

      public boolean isEmpty() {
         return this.expiredMap.isEmpty();
      }

      public boolean containsKey(Object var1) {
         return this.expiredMap.containsKey(var1);
      }

      public boolean equals(Object var1) {
         return this.expiredMap.equals(var1);
      }

      public int hashCode() {
         return this.expiredMap.hashCode();
      }

      public void putAll(Map var1) {
         this.expiredMap.putAll(var1);
      }

      public Object clone() {
         return this.expiredMap.clone();
      }

      public void clear() {
         this.expiredMap.clear();
      }

      public boolean containsValue(Object var1) {
         return this.expiredMap.containsValue(var1);
      }

      public Object get(Object var1) {
         return this.expiredMap.get(var1);
      }

      public Object put(Object var1, Object var2) {
         return this.expiredMap.put(var1, var2);
      }

      public Object putIfAbsent(Object var1, Object var2) {
         return this.expiredMap.putIfAbsent(var1, var2);
      }

      public Object remove(Object var1) {
         return this.expiredMap.remove(var1);
      }

      public Set keySet() {
         return this.expiredMap.keySet();
      }

      public Collection values() {
         return this.expiredMap.values();
      }

      public Set entrySet() {
         return this.expiredMap.entrySet();
      }
   }

   public static class JMSOrderExceptionAdapter implements ExceptionAdapter {
      public Throwable wrapException(Throwable var1) {
         return PathHelper.wrapExtensionImpl(var1);
      }

      public Throwable unwrapException(Throwable var1) {
         while(var1 instanceof JMSOrderException && var1.getCause() != null) {
            var1 = var1.getCause();
         }

         return var1;
      }
   }

   private class ServerInfo {
      private ConcurrentHashMap owned = new ConcurrentHashMap();
      private LowMemoryForgetfulMap dirty = PathHelper.this.new LowMemoryForgetfulMap();
      private String jndiName;
      private AsyncMap delegate;
      private ChangeListener invalidator;

      ServerInfo(String var2, AsyncMap var3) {
         this.jndiName = var2;
         this.delegate = var3;
         if (ClusterService.getServices() != null) {
            this.invalidator = LLDFactory.getInstance().createLLDInvalidator(PathHelper.broadcastJndiName(this.jndiName), this.dirty);
         }

      }

      String getJndiName() {
         return this.jndiName;
      }

      private Member dirtyPut(Key var1, Member var2) {
         Member var3 = (Member)this.dirty.put(var1, var2);
         return var3 == null ? null : var3;
      }

      private Member dirtyPutIfAbsent(Key var1, Member var2) {
         Member var3 = (Member)this.dirty.putIfAbsent(var1, var2);
         return var3 == null ? null : var3;
      }

      private Member dirtyGet(Key var1) {
         Member var2 = (Member)this.dirty.get(var1);
         return var2 == null ? null : var2;
      }

      private Member dirtyRemove(Key var1) {
         Member var2 = (Member)this.dirty.remove(var1);
         return var2 == null ? null : var2;
      }

      private Member ownedPut(Key var1, Member var2) {
         return (Member)this.owned.put(var1, var2);
      }

      private Member ownedPutIfAbsent(Key var1, Member var2) {
         return (Member)this.owned.putIfAbsent(var1, var2);
      }

      private Member ownedGet(Key var1) {
         Member var2 = (Member)this.owned.get(var1);
         if (var2 == null && PathHelper.retired && PathHelper.PathSvcVerbose.isDebugEnabled()) {
            PathHelper.PathSvcVerbose.debug("Missing owned key " + var1 + " from " + this.owned.keySet());
         }

         return var2;
      }

      private Member ownedRemove(Key var1) {
         return (Member)this.owned.remove(var1);
      }

      public String toString() {
         return "jndiName=" + this.jndiName;
      }
   }

   private abstract class UpdateCache extends CompletionRequest implements CompletionListener {
      CompletionRequest userCompletionRequest;

      UpdateCache(CompletionRequest var2) {
         this.userCompletionRequest = var2;
         this.addListener(this);
      }
   }

   private class RetryOnce extends CompletionRequest implements CompletionListener {
      int type;
      String jndiName;
      Key key;
      Member member;
      AsyncMap oldMap;
      CompletionRequest userCompletionRequest;

      RetryOnce(int var2, String var3, Key var4, Member var5, AsyncMap var6, CompletionRequest var7) {
         this.type = var2;
         this.key = var4;
         this.member = var5;
         this.oldMap = var6;
         this.jndiName = var3;
         this.userCompletionRequest = var7;
         this.addListener(this);
      }

      public void onCompletion(CompletionRequest var1, Object var2) {
         if (this.userCompletionRequest != null) {
            boolean var3 = this.userCompletionRequest.runListenersInSetResult(true);

            try {
               this.userCompletionRequest.setResult(var2);
            } finally {
               if (var3) {
                  return;
               }

               this.userCompletionRequest.runListenersInSetResult(false);
            }
         }

      }

      public void onException(CompletionRequest var1, Throwable var2) {
         if (this.userCompletionRequest != null) {
            if (!(var2 instanceof PeerGoneException) && !(var2 instanceof ConnectException)) {
               this.onCompletion(this, var2);
            } else {
               this.doRetry(var2);
            }

         }
      }

      private void doRetry(Throwable var1) {
         AsyncMap var3;
         try {
            ServerInfo var2 = PathHelper.this.createServerInfo(this.jndiName);
            var3 = PathHelper.this.lookupAsyncMap(this.jndiName, var2);
            if (var3 == this.oldMap) {
               PathHelper.PathSvc.debug("somehow retry has same map instance", var1);
               this.onCompletion(this, var1);
               return;
            }
         } catch (NamingException var6) {
            this.onCompletion(this, var1);
            return;
         }

         try {
            switch (this.type) {
               case 0:
                  var3.putIfAbsent(this.key, this.member, this.userCompletionRequest);
                  return;
               case 1:
                  var3.remove(this.key, this.member, this.userCompletionRequest);
                  return;
               case 2:
                  var3.put(this.key, this.member, this.userCompletionRequest);
                  return;
               case 3:
                  var3.get(this.key, this.userCompletionRequest);
                  return;
            }
         } catch (Throwable var5) {
            PathHelper.PathSvc.debug("returning first exception, encountered retry exception:", var5);
            this.onCompletion(this, var1);
         }

      }
   }

   public class PathServiceException extends Exception {
      static final long serialVersionUID = -4564823747310961840L;

      PathServiceException(Throwable var2) {
         super(var2);
      }
   }
}
