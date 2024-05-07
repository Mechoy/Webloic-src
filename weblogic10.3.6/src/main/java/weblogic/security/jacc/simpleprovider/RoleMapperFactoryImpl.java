package weblogic.security.jacc.simpleprovider;

import java.security.SecurityPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.security.jacc.RoleMapper;
import weblogic.security.jacc.RoleMapperFactory;

public class RoleMapperFactoryImpl extends RoleMapperFactory {
   private static Map rmMap = new HashMap();
   private static Map acMap = new HashMap();
   private static Map caMap = new HashMap();
   private static DebugLogger jaccDebugLogger = DebugLogger.getDebugLogger("DebugSecurityJACCNonPolicy");

   public RoleMapperFactoryImpl() {
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("RoleMapperFactoryImpl noarg constructor");
      }

   }

   public RoleMapper getRoleMapper(String var1, boolean var2) {
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("RoleMapperFactoryImpl.getRoleMapper appID: " + (var1 == null ? "null" : var1) + " remove: " + var2);
      }

      RoleMapperImpl var3 = null;
      synchronized(rmMap) {
         var3 = (RoleMapperImpl)rmMap.get(var1);
         if (var3 == null) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("creating a new RoleMapperImpl");
            }

            var3 = new RoleMapperImpl(var1, this);
            rmMap.put(var1, var3);
         } else if (var2) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("calling delete on the role mapper");
            }

            var3.clear();
         }

         return var3;
      }
   }

   public RoleMapper getRoleMapper(String var1, String var2, boolean var3) {
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("RoleMapperFactoryImpl.getRoleMapper appID: " + (var1 == null ? "null" : var1) + " contextID: " + (var2 == null ? "null" : var2) + " remove: " + var3);
      }

      RoleMapperImpl var4 = null;
      synchronized(rmMap) {
         var4 = (RoleMapperImpl)rmMap.get(var1);
         if (var4 == null) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("creating a new RoleMapperImpl");
            }

            var4 = new RoleMapperImpl(var1, this);
            rmMap.put(var1, var4);
         } else if (var3) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("calling delete on the role mapper");
            }

            var4.clear();
         }

         if (var2 != null && !var2.equals("")) {
            caMap.put(var2, var1);
            ArrayList var6 = (ArrayList)acMap.get(var1);
            if (var6 == null) {
               var6 = new ArrayList(1);
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("creating a new appIDtoContextID map for appID: " + (var1 == null ? "null" : var1) + " and adding contextID: " + (var2 == null ? "null" : var2));
               }

               var6.add(var2);
               acMap.put(var1, var6);
            } else if (!var6.contains(var2)) {
               jaccDebugLogger.debug("adding contextID: " + (var2 == null ? "null" : var2) + " to appID: " + (var1 == null ? "null" : var1));
               var6.add(var2);
               acMap.put(var1, var6);
            }
         }

         return var4;
      }
   }

   public RoleMapper getRoleMapperForContextID(String var1) {
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("RoleMapperFactoryImpl.getRoleMapperForContextID contextID: " + (var1 == null ? "null" : var1));
      }

      RoleMapperImpl var2 = null;
      synchronized(rmMap) {
         String var4 = (String)caMap.get(var1);
         if (var4 == null) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("RoleMapperImpl for appID: " + (var4 == null ? "null" : var4) + " does not have a map from ContextID: " + (var1 == null ? "null" : var1));
            }
         } else {
            var2 = (RoleMapperImpl)rmMap.get(var4);
            if (var2 == null && jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("RoleMapperImpl for appID: " + (var4 == null ? "null" : var4) + " does not exist");
            }
         }

         return var2;
      }
   }

   public void removeRoleMapper(String var1) {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null) {
         var2.checkPermission(new SecurityPermission("setPolicy"));
      }

      synchronized(rmMap) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("RoleMapperFactoryImpl.getRoleMapper removing the RoleMapper for appID: " + (var1 == null ? "null" : var1));
         }

         rmMap.remove(var1);
         String[] var4 = (String[])((String[])((ArrayList)acMap.get(var1)).toArray());
         if (jaccDebugLogger.isDebugEnabled() && var4.length > 0) {
            jaccDebugLogger.debug("RoleMapperFactoryImpl.getRoleMapper removing mapping of contextIDs to appID: " + (var1 == null ? "null" : var1));
         }

         for(int var5 = 0; var5 < var4.length; ++var5) {
            caMap.remove(var4[var5]);
         }

         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("RoleMapperFactoryImpl.getRoleMapper removing the mapping from appID: " + (var1 == null ? "null" : var1) + " to contextIDs");
         }

         acMap.remove(var1);
      }
   }

   protected String getAppId(String var1) {
      String var2 = null;
      synchronized(caMap) {
         var2 = (String)caMap.get(var1);
      }

      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("RoleMapperFactoryImpl.getAppID contextID: " + (var1 == null ? "null" : var1) + " maps to appID: " + (var2 == null ? "null" : var2));
      }

      return var2;
   }
}
