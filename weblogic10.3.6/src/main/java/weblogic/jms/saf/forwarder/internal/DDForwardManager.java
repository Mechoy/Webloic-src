package weblogic.jms.saf.forwarder.internal;

import weblogic.jms.forwarder.dd.DDMembersCache;

public class DDForwardManager {
   public static DDForwardManager manager = new DDForwardManager();

   private DDForwardManager() {
   }

   public DDMembersCache findOrCreateDDmembersCache(String var1, String var2) {
      synchronized(this) {
         return null;
      }
   }
}
