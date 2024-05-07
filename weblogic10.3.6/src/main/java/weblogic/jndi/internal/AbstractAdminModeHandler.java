package weblogic.jndi.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.application.utils.ApplicationVersionUtils;

class AbstractAdminModeHandler {
   protected final ServerNamingNode node;
   private HashSet adminModeSet = new HashSet();
   private final Object adminModeSetLock = new String("AdminModeSetLock");
   protected final boolean debug = NamingDebugLogger.isDebugEnabled();

   AbstractAdminModeHandler(ServerNamingNode var1) {
      this.node = var1;
   }

   private boolean hasAdminModeBindings() {
      HashSet var1 = null;
      synchronized(this.adminModeSetLock) {
         var1 = this.adminModeSet;
      }

      return var1.size() > 0;
   }

   protected boolean isAdminMode(String var1) {
      HashSet var2 = null;
      synchronized(this.adminModeSetLock) {
         var2 = this.adminModeSet;
      }

      return var2.size() == 0 ? false : var2.contains(var1);
   }

   protected void setAdminMode(String var1) {
      synchronized(this.adminModeSetLock) {
         HashSet var3 = new HashSet(this.adminModeSet);
         if (this.debug) {
            NamingDebugLogger.debug("+++ AdminMode.setAdminMode name=" + var1 + ", ns=" + this.node.getNameInNamespace() + ", this=" + this);
         }

         var3.add(var1);
         this.adminModeSet = var3;
      }
   }

   protected void unsetAdminMode(String var1) {
      synchronized(this.adminModeSetLock) {
         if (this.adminModeSet.size() != 0) {
            HashSet var3 = new HashSet(this.adminModeSet);
            if (this.debug) {
               NamingDebugLogger.debug("+++ AdminMode.unsetAdminMode name=" + var1 + ", ns=" + this.node.getNameInNamespace() + ", this=" + this);
            }

            var3.remove(var1);
            this.adminModeSet = var3;
         }
      }
   }

   Set getAccessibleBindings(Set var1) {
      if (NamingDebugLogger.isDebugEnabled()) {
         NamingDebugLogger.debug("+++ AdminMode.getAccessibleBindings hasAdminModeBindings=" + this.hasAdminModeBindings() + ", adminRequest=" + ApplicationVersionUtils.isAdminModeRequest() + ", this=" + this + ", ns=" + this.node.getNameInNamespace());
      }

      if (this.hasAdminModeBindings() && !ApplicationVersionUtils.isAdminModeRequest()) {
         HashMap var2 = new HashMap();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Map.Entry var4 = (Map.Entry)var3.next();
            if (!this.isAdminMode((String)var4.getKey())) {
               if (NamingDebugLogger.isDebugEnabled()) {
                  NamingDebugLogger.debug("++++ non-admin binding: " + var4.getKey());
               }

               var2.put(var4.getKey(), var4.getValue());
            } else if (NamingDebugLogger.isDebugEnabled()) {
               NamingDebugLogger.debug("++++ admin binding: " + var4.getKey());
            }
         }

         return var2.entrySet();
      } else {
         return var1;
      }
   }
}
