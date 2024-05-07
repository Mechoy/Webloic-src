package weblogic.security.jacc.simpleprovider;

import java.security.SecurityPermission;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyConfigurationFactory;
import javax.security.jacc.PolicyContextException;
import weblogic.diagnostics.debug.DebugLogger;

public class PolicyConfigurationFactoryImpl extends PolicyConfigurationFactory {
   private static Map polConfMap = new HashMap();
   private static DebugLogger jaccDebugLogger = DebugLogger.getDebugLogger("DebugSecurityJACCNonPolicy");

   public PolicyConfigurationFactoryImpl() {
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("PolicyConfigurationFactoryImpl noarg constructor");
      }

   }

   public PolicyConfiguration getPolicyConfiguration(String var1, boolean var2) throws PolicyContextException {
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("PolicyConfigurationFactoryImpl.getPolicyConfiguration contextID: " + (var1 == null ? "null" : var1) + " remove: " + var2);
      }

      SecurityManager var3 = System.getSecurityManager();
      if (var3 != null) {
         var3.checkPermission(new SecurityPermission("setPolicy"));
      }

      Object var4 = null;
      synchronized(polConfMap) {
         var4 = (PolicyConfiguration)polConfMap.get(var1);
         if (var4 == null) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("creating a new PolicyConfigurationImpl");
            }

            var4 = new PolicyConfigurationImpl(var1, this);
            polConfMap.put(var1, var4);
         } else if (var2) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("calling delete on the policy configuration");
            }

            ((PolicyConfiguration)var4).delete();
         } else {
            ((PolicyConfigurationImpl)var4).setStateOpen();
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("set the state to open on the policy configuration");
            }
         }

         return (PolicyConfiguration)var4;
      }
   }

   protected static PolicyConfigurationImpl getPolicyConfigurationImpl(String var0) {
      return (PolicyConfigurationImpl)polConfMap.get(var0);
   }

   protected static Collection getPolicyConfigurationImpls() {
      return polConfMap.values();
   }

   public boolean inService(String var1) throws PolicyContextException {
      SecurityManager var2 = System.getSecurityManager();
      if (var2 != null) {
         var2.checkPermission(new SecurityPermission("setPolicy"));
      }

      PolicyConfiguration var3 = (PolicyConfiguration)polConfMap.get(var1);
      if (var3 == null) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("inService didn't find the pc");
         }

         return false;
      } else {
         return var3.inService();
      }
   }

   protected static void removePolicyConfiguration(String var0) {
      SecurityManager var1 = System.getSecurityManager();
      if (var1 != null) {
         var1.checkPermission(new SecurityPermission("setPolicy"));
      }

      synchronized(polConfMap) {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("removing the PolicyConfiguration for contextID: " + (var0 == null ? "null" : var0));
         }

         polConfMap.remove(var0);
      }
   }
}
