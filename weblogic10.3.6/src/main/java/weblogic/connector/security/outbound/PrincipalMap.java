package weblogic.connector.security.outbound;

import java.util.Hashtable;
import java.util.Iterator;

public final class PrincipalMap {
   private static String defaultIPName = "*";
   private Hashtable rpMap = new Hashtable();
   private boolean isEmptyMap = true;
   private boolean hasDefaultResourcePrincipal = false;
   private ResourcePrincipal defaultResourcePrincipal = null;

   public PrincipalMap(Hashtable var1) {
      if (!var1.isEmpty()) {
         this.isEmptyMap = false;
         Iterator var7 = var1.keySet().iterator();

         while(var7.hasNext()) {
            String var3 = (String)var7.next();
            Hashtable var2 = (Hashtable)var1.get(var3);
            String var4 = (String)var2.get("resource-username");
            String var5 = (String)var2.get("resource-password");
            ResourcePrincipal var6 = new ResourcePrincipal(var4, var5);
            this.rpMap.put(var3, var6);
         }

         this.defaultResourcePrincipal = (ResourcePrincipal)this.rpMap.get(defaultIPName);
         if (this.defaultResourcePrincipal != null) {
            this.hasDefaultResourcePrincipal = true;
         }
      }

   }

   public ResourcePrincipal getDefaultResourcePrincipal() {
      return this.defaultResourcePrincipal;
   }

   public ResourcePrincipal getResourcePrincipal(String var1) {
      return this.rpMap.containsKey(var1) ? (ResourcePrincipal)this.rpMap.get(var1) : this.defaultResourcePrincipal;
   }

   public boolean isEmptyMap() {
      return this.isEmptyMap;
   }
}
