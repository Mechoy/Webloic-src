package weblogic.security.jacc.simpleprovider;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import javax.security.jacc.PolicyContext;
import sun.security.provider.PolicyFile;
import weblogic.diagnostics.debug.DebugLogger;

public final class SimpleJACCPolicy extends Policy {
   private PolicyFile defaultPolicy;
   private static DebugLogger jaccDebugLogger = DebugLogger.getDebugLogger("DebugSecurityJACCPolicy");

   public SimpleJACCPolicy() {
      if (jaccDebugLogger.isDebugEnabled()) {
         this.log("SimpleJACCPolicy no arg constructor");
      }

      this.defaultPolicy = new PolicyFile();
   }

   public PermissionCollection getPermissions(CodeSource var1) {
      PermissionCollection var2 = null;
      PolicyFile var3 = null;
      String var4 = PolicyContext.getContextID();
      var3 = this.getPolicyConfigurationPolicyForContext(var4);
      PermissionCollection var5 = var3.getPermissions(var1);
      var2 = this.removeExcludedPermissions(var4, var5);
      return var2;
   }

   public PermissionCollection getPermissions(ProtectionDomain var1) {
      PermissionCollection var2 = null;
      PolicyFile var3 = null;
      String var4 = PolicyContext.getContextID();
      var3 = this.getPolicyConfigurationPolicyForContext(var4);
      PermissionCollection var5 = var3.getPermissions(var1);
      var2 = this.removeExcludedPermissions(var4, var5);
      return var2;
   }

   public boolean implies(ProtectionDomain var1, Permission var2) {
      if (jaccDebugLogger.isDebugEnabled()) {
         this.log("SimpleJACCPolicy.implies " + var2);
      }

      String var3 = PolicyContext.getContextID();
      PolicyFile var4 = this.getPolicyConfigurationPolicyForContext(var3);
      Permissions var5 = this.getExcludedPermissionsForContext(var3);
      boolean var6 = var4.implies(var1, var2);
      if (jaccDebugLogger.isDebugEnabled()) {
         this.log("SimpleJACCPolicy.implies " + (!var6 ? "denied " : "granted") + " policy: " + (var4 == this.defaultPolicy ? "default" : (var3 == null ? " null" : var3)) + " " + var2);
      }

      if (var6 && var5 != null) {
         if (this.shouldExclude(var2, var5)) {
            var6 = false;
         }

         if (jaccDebugLogger.isDebugEnabled()) {
            this.log("SimpleJACCPolicy.implies " + (!var6 ? "denied " : "granted") + var2 + " after applying excluded Permissions");
         }
      }

      return var6;
   }

   public void refresh() {
      if (jaccDebugLogger.isDebugEnabled()) {
         this.log("SimpleJACCPolicy.refresh");
      }

      this.defaultPolicy.refresh();
      Collection var1 = PolicyConfigurationFactoryImpl.getPolicyConfigurationImpls();
      if (var1 != null) {
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            PolicyConfigurationImpl var3 = (PolicyConfigurationImpl)var2.next();
            if (var3 != null) {
               var3.refresh();
            }
         }
      }

   }

   private PolicyFile getPolicyConfigurationPolicyForContext(String var1) {
      PolicyFile var2 = null;
      if (var1 != null) {
         PolicyConfigurationImpl var3 = PolicyConfigurationFactoryImpl.getPolicyConfigurationImpl(var1);
         if (var3 != null) {
            var2 = var3.getPolicy();
         }
      }

      if (var2 == null) {
         var2 = this.defaultPolicy;
      }

      return var2;
   }

   private Permissions getExcludedPermissionsForContext(String var1) {
      Permissions var2 = null;
      if (var1 != null) {
         PolicyConfigurationImpl var3 = PolicyConfigurationFactoryImpl.getPolicyConfigurationImpl(var1);
         if (var3 != null) {
            var2 = var3.getExcludedPermissions();
         }
      }

      return var2;
   }

   private PermissionCollection removeExcludedPermissions(String var1, PermissionCollection var2) {
      boolean var3 = false;
      Permissions var4 = this.getExcludedPermissionsForContext(var1);
      Object var5 = null;
      Permission var6 = null;
      if (var4 != null && var4.elements().hasMoreElements()) {
         Enumeration var7 = var2.elements();

         while(var7.hasMoreElements()) {
            var6 = (Permission)var7.nextElement();
            if (!this.shouldExclude(var6, var4)) {
               if (var5 == null) {
                  var5 = new Permissions();
               }

               ((PermissionCollection)var5).add(var6);
            } else {
               var3 = true;
            }
         }
      }

      if (!var3) {
         var5 = var2;
      }

      return (PermissionCollection)var5;
   }

   private boolean shouldExclude(Permission var1, Permissions var2) {
      boolean var3 = false;
      if (var2 != null && var2.elements().hasMoreElements()) {
         if (!var2.implies(var1)) {
            Enumeration var4 = var2.elements();

            while(var4.hasMoreElements() || var3) {
               Permission var5 = (Permission)var4.nextElement();
               if (var1.implies(var5)) {
                  var3 = true;
                  this.log("SimpleJACCPolicy excluding granted: " + var1 + " implies: " + var5);
                  break;
               }
            }
         } else {
            var3 = true;
            this.log("SimpleJACCPolicy excluding excludedPermissions implies: " + var1);
         }
      }

      return var3;
   }

   private void log(String var1) {
      System.out.println(var1);
   }
}
