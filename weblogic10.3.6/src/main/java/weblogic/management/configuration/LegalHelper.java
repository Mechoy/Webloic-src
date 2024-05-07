package weblogic.management.configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.ReflectionException;
import weblogic.common.internal.VersionInfo;
import weblogic.logging.Loggable;
import weblogic.management.WebLogicMBean;
import weblogic.management.internal.ManagementTextTextFormatter;
import weblogic.security.SecurityLogger;

public class LegalHelper {
   public static boolean serverMBeanSetNameLegalCheck(String var0, Object var1) throws InvalidAttributeValueException {
      ServerMBean var2 = (ServerMBean)var1;
      DomainMBean var3 = (DomainMBean)var2.getParent();
      return var3 == null || var3.getName() == null || !var3.getName().equals(var0);
   }

   static boolean exists(Set var0, BooleanFun1Arg var1) {
      Iterator var2 = var0.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!var1.eval((WebLogicMBean)var2.next()));

      return true;
   }

   static boolean forall(Set var0, BooleanFun1Arg var1) {
      Iterator var2 = var0.iterator();

      do {
         if (!var2.hasNext()) {
            return true;
         }
      } while(var1.eval((WebLogicMBean)var2.next()));

      return false;
   }

   static boolean in(final WebLogicMBean var0, WebLogicMBean[] var1) {
      return exists(new HashSet(Arrays.asList((Object[])var1)), new BooleanFun1Arg() {
         public boolean eval(WebLogicMBean var1) {
            return var1.getName().equals(var0.getName());
         }
      });
   }

   static boolean subset(WebLogicMBean[] var0, final WebLogicMBean[] var1) {
      return forall(new HashSet(Arrays.asList((Object[])var0)), new BooleanFun1Arg() {
         public boolean eval(WebLogicMBean var1x) {
            return LegalHelper.in(var1x, var1);
         }
      });
   }

   static final void validateListenPorts(ServerMBean var0) {
      SSLMBean var1 = var0.getSSL();
      Loggable var2;
      if (var1.isEnabled() && var1.getListenPort() == var0.getListenPort()) {
         var2 = SecurityLogger.logSSLListenPortSameAsServerListenPortLoggable(var0.getListenPort() + "");
         var2.log();
         throw new IllegalArgumentException(var2.getMessage());
      } else if (var0.isAdministrationPortEnabled() && var1.isEnabled() && var1.getListenPort() == var0.getAdministrationPort()) {
         var2 = SecurityLogger.logSSLListenPortSameAsAdministrationPortLoggable(var1.getListenPort() + "");
         var2.log();
         throw new IllegalArgumentException(var2.getMessage());
      }
   }

   public static void validateJavaHome(String var0) throws IllegalArgumentException {
      if (var0 != null) {
         if (var0.indexOf(34) != -1) {
            throw new IllegalArgumentException("JavaHome may not contain '\"'");
         }
      }
   }

   public static void validateClasspath(String var0) throws IllegalArgumentException {
      if (var0 != null) {
         if (var0.indexOf(34) != -1) {
            throw new IllegalArgumentException("Classpath may not contain '\"'");
         }
      }
   }

   public static void validateBeaHome(String var0) throws IllegalArgumentException {
      if (var0 != null) {
         if (var0.indexOf(34) != -1) {
            throw new IllegalArgumentException("BeaHome may not contain '\"'");
         }
      }
   }

   public static void validateRootDirectory(String var0) throws IllegalArgumentException {
      if (var0 != null) {
         if (var0.indexOf(34) != -1) {
            throw new IllegalArgumentException("RootDirectory may not contain '\"'");
         }
      }
   }

   public static void validateSecurityPolicyFile(String var0) throws IllegalArgumentException {
      if (var0 != null) {
         if (var0.indexOf(34) != -1) {
            throw new IllegalArgumentException("SecurityPolicyFile may not contain '\"'");
         }
      }
   }

   public static void validateIsSetIfTargetted(DeploymentMBean var0, String var1) throws IllegalArgumentException {
      TargetMBean[] var2 = var0.getTargets();
      if (var2 != null && var2.length > 0) {
         Object var3;
         try {
            var3 = var0.getAttribute(var1);
         } catch (AttributeNotFoundException var5) {
            var3 = null;
         } catch (ReflectionException var6) {
            var3 = null;
         } catch (MBeanException var7) {
            var3 = null;
         }

         if (var3 == null || var3 instanceof String && ((String)var3).length() == 0) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getUnsetOnTargetted(var0.getName(), var1));
         }
      }

   }

   public static void validateNotNullable(String var0, String var1) throws IllegalArgumentException {
      if (var1 != null && "".equals(var1.trim())) {
         throw new IllegalArgumentException("value may not be unset.");
      } else if (var1 == null && var0 != null) {
         throw new IllegalArgumentException("value may not be set to empty string.");
      }
   }

   public static String checkClassName(String var0) {
      String var1 = var0;
      if (var0 != null && var0.endsWith(".class")) {
         int var2 = var0.length();
         var1 = var0.substring(0, var2 - 6);
      }

      return var1;
   }

   public static void validateArguments(String var0) throws IllegalArgumentException {
      if (var0 != null) {
         if (var0.indexOf(34) != -1 && !Boolean.getBoolean("weblogic.serverStart.allowQuotes")) {
            throw new IllegalArgumentException("Arguments may not contain '\"'");
         }
      }
   }

   public static boolean versionEarlierThan(String var0, String var1) throws IllegalArgumentException {
      if (var0 != null && var1 != null) {
         VersionInfo var2 = new VersionInfo(var0);
         VersionInfo var3 = new VersionInfo(var1);
         return var2.earlierThan(var3);
      } else {
         return false;
      }
   }

   interface BooleanFun1Arg {
      boolean eval(WebLogicMBean var1);
   }
}
