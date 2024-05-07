package weblogic.servlet.internal;

import weblogic.management.DeploymentException;
import weblogic.servlet.utils.ServletMapping;

public final class OnDemandManager {
   private ServletMapping contextTable = new ServletMapping();

   public synchronized void registerOnDemandContextPaths(String[] var1, OnDemandListener var2, String var3, boolean var4) {
      synchronized(this.contextTable) {
         ServletMapping var6 = (ServletMapping)this.contextTable.clone();

         for(int var7 = 0; var7 < var1.length; ++var7) {
            String var8 = var1[var7];
            Object var9 = this.contextTable.get(var8);
            if (var9 != null) {
               throw new IllegalArgumentException("Context path " + var8 + " is already registered");
            }

            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Registering OnDemandContext with context-root: '" + var8 + "'");
            }

            OnDemandContext var10 = new OnDemandContext(var8, var2, var3, var4);
            var6.put(toPattern(var8), var10);
         }

         this.contextTable = var6;
      }
   }

   public synchronized void unregisterOnDemandContextPaths(String[] var1) {
      synchronized(this.contextTable) {
         ServletMapping var3 = (ServletMapping)this.contextTable.clone();

         for(int var4 = 0; var4 < var1.length; ++var4) {
            String var5 = var1[var4];
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Unregistering OnDemandContext with context-root: '" + var5 + "'");
            }

            OnDemandContext var6 = (OnDemandContext)this.contextTable.get(toPattern(var5));
            if (var6 != null) {
               var3.remove(toPattern(var5));
            }
         }

         this.contextTable = var3;
      }
   }

   OnDemandContext lookupOnDemandContext(String var1) {
      return var1 == null ? null : (OnDemandContext)this.contextTable.get(var1);
   }

   void loadOnDemandURI(OnDemandContext var1, boolean var2) throws DeploymentException {
      if (var1 != null) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Loading on demand context: " + var1.getAppName() + " load async: " + var2);
         }

         OnDemandListener var3 = var1.getListener();
         var3.OnDemandURIAccessed(var1.getContextPath(), var1.getAppName(), var2);
      }
   }

   private static String toPattern(String var0) {
      if (!var0.equals("") && !var0.equals("/")) {
         if (!var0.startsWith("/") && !var0.startsWith("*.")) {
            var0 = "/" + var0;
         }

         return var0.endsWith("/") ? var0 + "*" : var0 + "/*";
      } else {
         return "/";
      }
   }
}
