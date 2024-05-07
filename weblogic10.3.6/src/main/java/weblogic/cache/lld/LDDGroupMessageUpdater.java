package weblogic.cache.lld;

import java.io.IOException;
import java.util.Map;
import weblogic.rmi.spi.HostID;

public class LDDGroupMessageUpdater extends LLDGroupMessageInvalidator {
   private static final boolean VERBOSE = true;

   public LDDGroupMessageUpdater(String var1, Map var2) {
      super(var1, var2);
   }

   public void onUpdate(CacheEntry var1, Object var2) {
      System.out.println("Sending update of: " + var1.getKey());
      this.sendPutMessage(var1.getKey(), var1.getValue());
   }

   public void onCreate(CacheEntry var1) {
      System.out.println("Sending create of: " + var1.getKey());
      this.sendPutMessage(var1.getKey(), var1.getValue());
   }

   private void sendPutMessage(Object var1, Object var2) {
      try {
         PutMessage var3 = new PutMessage(this.cacheName, var1, var2);
         this.multicastSession.send(var3);
      } catch (IOException var4) {
         LLDLogger.logMessageException("" + var1, var4);
      }

   }

   protected static class PutMessage extends LLDGroupMessageInvalidator.RemoveMessage {
      private final Object value;

      PutMessage(String var1, Object var2, Object var3) {
         super(var1, var2);
         this.value = var3;
      }

      public void execute(HostID var1) {
         System.out.println("Putting entry: " + this.key);
         Map var2 = this.getLocalMap();
         System.out.println("got map: " + var2);
         if (var2 != null) {
            var2.put(this.key, this.value);
         }
      }
   }
}
