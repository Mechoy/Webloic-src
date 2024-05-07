package weblogic.cache.lld;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import weblogic.cluster.ClusterService;
import weblogic.cluster.ClusterServices;
import weblogic.cluster.GroupMessage;
import weblogic.cluster.MulticastSession;
import weblogic.cluster.RecoverListener;
import weblogic.rmi.spi.HostID;
import weblogic.utils.collections.WeakConcurrentHashMap;

public class LLDGroupMessageInvalidator implements ChangeListener {
   private static final boolean VERBOSE = false;
   private static final WeakConcurrentHashMap caches = new WeakConcurrentHashMap();
   protected final String cacheName;
   protected final MulticastSession multicastSession;

   public LLDGroupMessageInvalidator(String var1, Map var2) {
      this.cacheName = var1;
      caches.put(this.cacheName, var2);
      ClusterServices var3 = ClusterService.getServices();
      if (var3 == null) {
         throw new RuntimeException("This server is not in a cluster.");
      } else {
         this.multicastSession = var3.createMulticastSession(new CacheStateRecoverListener(), -1, false);
      }
   }

   public void onCreate(CacheEntry var1) {
   }

   public void onUpdate(CacheEntry var1, Object var2) {
   }

   public void onDelete(CacheEntry var1) {
      try {
         RemoveMessage var2 = new RemoveMessage(this.cacheName, var1.getKey());
         this.multicastSession.send(var2);
      } catch (IOException var3) {
         LLDLogger.logMessageException("" + var1.getKey(), var3);
      }

   }

   public void onClear() {
      try {
         ClearMessage var1 = new ClearMessage(this.cacheName);
         this.multicastSession.send(var1);
      } catch (IOException var2) {
         LLDLogger.logMessageException("<all keys>", var2);
      }

   }

   protected class CacheStateRecoverListener implements RecoverListener {
      public GroupMessage createRecoverMessage() {
         return new ClearMessage(LLDGroupMessageInvalidator.this.cacheName);
      }
   }

   protected static class ClearMessage extends Message {
      ClearMessage(String var1) {
         super(var1);
      }

      public void execute(HostID var1) {
         Map var2 = this.getLocalMap();
         if (var2 != null) {
            var2.clear();
         }
      }
   }

   protected static class RemoveMessage extends Message {
      protected final Object key;

      RemoveMessage(String var1, Object var2) {
         super(var1);
         this.key = var2;
      }

      public void execute(HostID var1) {
         Map var2 = this.getLocalMap();
         if (var2 != null) {
            var2.remove(this.key);
         }
      }
   }

   protected abstract static class Message implements Serializable, GroupMessage {
      protected final String cacheName;

      protected Message(String var1) {
         this.cacheName = var1;
      }

      protected Map getLocalMap() {
         return (Map)LLDGroupMessageInvalidator.caches.get(this.cacheName);
      }

      public abstract void execute(HostID var1);
   }
}
