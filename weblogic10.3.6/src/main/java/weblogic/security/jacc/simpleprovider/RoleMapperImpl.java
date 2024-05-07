package weblogic.security.jacc.simpleprovider;

import java.security.SecurityPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.security.SecurityLogger;
import weblogic.security.jacc.RoleMapper;

public class RoleMapperImpl implements RoleMapper {
   private static DebugLogger jaccDebugLogger = DebugLogger.getDebugLogger("DebugSecurityJACCNonPolicy");
   private RoleMapperFactoryImpl rmFactoryImpl;
   private String contextId;
   private String contextIdNoNPE;
   private Map rolesToPrincipalNames;
   private boolean policyChanged = false;

   public RoleMapperImpl(String var1, RoleMapperFactoryImpl var2) {
      this.rmFactoryImpl = var2;
      this.contextId = var1;
      this.rolesToPrincipalNames = new HashMap();
      this.contextIdNoNPE = this.contextId == null ? "null" : this.contextId;
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("RoleMapperImpl constructor contextId: " + this.contextIdNoNPE);
      }

   }

   public void addAppRolesToPrincipalMap(Map var1) throws IllegalArgumentException {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null) {
         var2.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (var1 != null && !var1.isEmpty()) {
         try {
            synchronized(this.rolesToPrincipalNames) {
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("RoleMapperImpl: addAppRolesToPrincipalMap contextId: " + this.contextIdNoNPE + " map has " + var1.size() + " roles in it.");
               }

               if (this.rolesToPrincipalNames != null && !this.rolesToPrincipalNames.isEmpty()) {
                  this.rolesToPrincipalNames = mergeMap(this.rolesToPrincipalNames, var1);
                  if (jaccDebugLogger.isDebugEnabled()) {
                     jaccDebugLogger.debug("RoleMapperImpl: addAppRolesToPrincipalMap contextId: " + this.contextIdNoNPE + " map merged with existing role to pricipal names map.");
                  }
               } else {
                  this.rolesToPrincipalNames = new HashMap(var1);
                  if (jaccDebugLogger.isDebugEnabled()) {
                     jaccDebugLogger.debug("RoleMapperImpl: addAppRolesToPrincipalMap contextId: " + this.contextIdNoNPE + " created new map.");
                  }
               }

               this.policyChanged = true;
            }

            Set var3 = this.rolesToPrincipalNames.keySet();
            Iterator var4 = var3.iterator();

            while(true) {
               while(var4.hasNext()) {
                  String var5 = (String)var4.next();
                  String var6 = var5 == null ? "null" : var5;
                  if (jaccDebugLogger.isDebugEnabled()) {
                     jaccDebugLogger.debug("RoleMapperImpl: addAppRolesToPrincipalMap roleName: " + var6);
                  }

                  String[] var7 = (String[])((String[])this.rolesToPrincipalNames.get(var5));
                  if (var7 != null && var7.length != 0) {
                     for(int var8 = 0; var8 < var7.length; ++var8) {
                        if (jaccDebugLogger.isDebugEnabled()) {
                           jaccDebugLogger.debug("RoleMapperImpl: addAppRolesToPrincipalMap contextId: " + this.contextIdNoNPE + " roleName: " + var5 + " user/group name: " + (var7[var8] != null ? var7[var8] : "null"));
                        }
                     }
                  } else if (jaccDebugLogger.isDebugEnabled()) {
                     jaccDebugLogger.debug("RoleMapperImpl: addAppRolesToPrincipalMap contextId: " + this.contextIdNoNPE + " roleName: " + var5 + " no user or group names");
                  }
               }

               return;
            }
         } catch (Exception var10) {
            throw new IllegalArgumentException(SecurityLogger.getBadRoleToPrincipalMap(var10, this.contextId));
         }
      } else {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("RoleMapperImpl: addAppRolesToPrincipalMap contextId: " + this.contextIdNoNPE + " map is null or empty.");
         }

      }
   }

   public synchronized Map getRolesToPrincipalNames() {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("RoleMapperImpl: getRolesToPrincipalNames contextId: " + this.contextIdNoNPE + " map has " + this.rolesToPrincipalNames.size() + " roles in it.");
      }

      return new HashMap(this.rolesToPrincipalNames);
   }

   synchronized void clear() {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new SecurityPermission("setPolicy"));
      }

      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("RoleMapperImpl: clear contextId: " + this.contextIdNoNPE);
      }

      this.rolesToPrincipalNames = new HashMap();
   }

   private static HashMap mergeMap(Map var0, Map var1) {
      Set var2 = var1.keySet();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (!var0.containsKey(var4)) {
            var0.put(var4, var1.get(var4));
         } else {
            String[] var5 = (String[])((String[])var0.get(var4));
            String[] var6 = (String[])((String[])var1.get(var4));
            String[] var7 = mergeArray(var6, var5);
            var0.put(var4, var7);
         }
      }

      return new HashMap(var0);
   }

   private static String[] mergeArray(String[] var0, String[] var1) {
      int var2 = 0;
      int var3 = 0;
      ArrayList var4 = new ArrayList(var0.length + var1.length);

      int var5;
      for(var5 = 0; var5 < var0.length; ++var5) {
         if (var0[var5] != null) {
            ++var2;
            var4.add(var0[var5]);
         }
      }

      for(var5 = 0; var5 < var1.length; ++var5) {
         if (var1[var5] != null) {
            ++var3;
            var4.add(var1[var5]);
         }
      }

      var4.trimToSize();
      TreeSet var7 = new TreeSet(var4);
      String[] var6 = (String[])((String[])var7.toArray(new String[var7.size()]));
      return var6;
   }
}
