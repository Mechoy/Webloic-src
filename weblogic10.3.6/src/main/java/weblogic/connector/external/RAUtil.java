package weblogic.connector.external;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.resource.spi.ResourceAdapter;
import weblogic.connector.common.RACollectionManager;
import weblogic.connector.common.RAInstanceManager;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.SecurityServiceManager;

public class RAUtil {
   public static final String CONNECTOR_LOG = "ConnectorLog";

   public static RAInfo getRAInfo(ResourceAdapter var0) {
      RAInfo var2 = null;
      if (var0 != null) {
         RAInstanceManager var1 = RACollectionManager.getRAInstanceManager(var0);
         if (var1 != null) {
            var2 = var1.getRAInfo();
         }
      }

      return var2;
   }

   public static RAInfo getRAInfo(String var0) {
      RAInstanceManager var1 = null;
      RAInfo var2 = null;
      Iterator var3 = RACollectionManager.getRAs();

      while(var3.hasNext()) {
         var1 = (RAInstanceManager)var3.next();
         if (var0.equals(var1.getRAInfo().getJndiName())) {
            var2 = var1.getRAInfo();
            break;
         }

         if (var1.getRAInfo().getOutboundInfos().size() > 0) {
            Iterator var4 = var1.getRAInfo().getOutboundInfos().iterator();
            OutboundInfo var5 = null;

            while(var4.hasNext()) {
               var5 = (OutboundInfo)var4.next();
               if (var5.getKey().equals(var0)) {
                  var2 = var1.getRAInfo();
                  break;
               }
            }
         }
      }

      return var2;
   }

   public static Set getAvailableConnectorLogNames() {
      HashSet var0 = new HashSet();
      Iterator var1 = RACollectionManager.getRAs();

      while(var1.hasNext()) {
         RAInstanceManager var2 = (RAInstanceManager)var1.next();
         RAInfo var3 = var2.getRAInfo();
         Iterator var4 = var3.getOutboundInfos().iterator();

         while(var4.hasNext()) {
            OutboundInfo var5 = (OutboundInfo)var4.next();
            String var6 = var5.getLogFilename();
            if (var6 != null && var6.length() > 0) {
               var0.add("ConnectorLog/" + var5.getKey());
            }
         }
      }

      return var0;
   }

   public static String getLogFileName(String var0) {
      OutboundInfo var2 = getOutboundInfo(var0);
      String var1;
      if (var2 != null) {
         var1 = var2.getLogFilename();
      } else {
         var1 = null;
      }

      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      return var1;
   }

   public static String getLogFileRotationDir(String var0) {
      OutboundInfo var2 = getOutboundInfo(var0);
      String var1;
      if (var2 != null) {
         var1 = var2.getLogFileRotationDir();
      } else {
         var1 = null;
      }

      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      return var1;
   }

   private static OutboundInfo getOutboundInfo(String var0) {
      Iterator var1 = RACollectionManager.getRAs();

      while(var1.hasNext()) {
         RAInstanceManager var2 = (RAInstanceManager)var1.next();
         RAInfo var3 = var2.getRAInfo();
         Iterator var4 = var3.getOutboundInfos().iterator();

         while(var4.hasNext()) {
            OutboundInfo var5 = (OutboundInfo)var4.next();
            if (var5.getKey().equals(var0)) {
               return var5;
            }
         }
      }

      return null;
   }

   public static void testLogUtils(AuthenticatedSubject var0) {
      if (!SecurityServiceManager.isKernelIdentity(var0)) {
         throw new SecurityException("KernelId is required to call RAUtils.testLogUtils, Subject '" + (var0 == null ? "<null>" : var0.toString()) + "' is not the kernel identity");
      } else {
         System.out.println("^^^^^ TESTLOGUTILS STARTING ^^^^^^^");
         Set var1 = getAvailableConnectorLogNames();
         int var2 = "ConnectorLog".length() + 1;
         Iterator var3 = var1.iterator();
         int var4 = 0;

         while(var3.hasNext()) {
            ++var4;
            String var5 = (String)var3.next();
            String var6 = var5.substring(var2);
            String var7 = getLogFileName(var6);
            String var8 = getLogFileRotationDir(var6);
            System.out.println("CONNECTOR LOG #" + var4);
            System.out.println("================================================");
            System.out.println("  LogicalName:         " + var5);
            System.out.println("  KeyName:             " + var6);
            System.out.println("  LogFilename:         " + var7);
            System.out.println("  LogFileRotationDir:  " + var8);
         }

         System.out.println("^^^^^ TESTLOGUTILS COMPLETE ^^^^^^^");
      }
   }
}
