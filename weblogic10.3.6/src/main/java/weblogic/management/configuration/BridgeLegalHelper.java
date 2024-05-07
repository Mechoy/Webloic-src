package weblogic.management.configuration;

import java.util.Enumeration;
import java.util.Properties;
import weblogic.management.internal.ManagementTextTextFormatter;

public final class BridgeLegalHelper {
   private static final boolean debug = false;

   public static void validateBridgeDestinations(MessagingBridgeMBean var0) throws IllegalArgumentException {
      BridgeDestinationCommonMBean var1 = var0.getSourceDestination();
      BridgeDestinationCommonMBean var2 = var0.getTargetDestination();
      if (var1 != null && var2 != null) {
         if (var1.getName().equals(var2.getName())) {
            throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getSameSourceTargetException(var0.getName()));
         } else {
            Properties var3 = null;
            if (var1 instanceof JMSBridgeDestinationMBean) {
               var3 = createProperties(((JMSBridgeDestinationMBean)var1).getConnectionURL(), ((JMSBridgeDestinationMBean)var1).getInitialContextFactory(), ((JMSBridgeDestinationMBean)var1).getConnectionFactoryJNDIName(), ((JMSBridgeDestinationMBean)var1).getDestinationJNDIName(), ((JMSBridgeDestinationMBean)var1).getDestinationType());
            } else {
               var3 = ((BridgeDestinationMBean)var1).getProperties();
            }

            Properties var4 = null;
            if (var2 instanceof JMSBridgeDestinationMBean) {
               var4 = createProperties(((JMSBridgeDestinationMBean)var2).getConnectionURL(), ((JMSBridgeDestinationMBean)var2).getInitialContextFactory(), ((JMSBridgeDestinationMBean)var2).getConnectionFactoryJNDIName(), ((JMSBridgeDestinationMBean)var2).getDestinationJNDIName(), ((JMSBridgeDestinationMBean)var2).getDestinationType());
            } else {
               var4 = ((BridgeDestinationMBean)var2).getProperties();
            }

            if (!notSameDestinations(var3, var4)) {
               throw new IllegalArgumentException(ManagementTextTextFormatter.getInstance().getSameSourceTargetException(var0.getName()));
            }
         }
      }
   }

   public static boolean notSameDestinations(Properties var0, Properties var1) {
      if (var0 != null && var0.size() != 0) {
         if (var1 != null && var1.size() != 0) {
            Enumeration var2 = var0.propertyNames();
            Properties var3 = (Properties)var1.clone();

            String var4;
            while(var2.hasMoreElements()) {
               var4 = (String)var2.nextElement();
               String var5 = var0.getProperty(var4);
               String var6 = var1.getProperty(var4);
               if (var5 == null) {
                  if (var6 != null) {
                     return true;
                  }
               } else {
                  if (var6 == null) {
                     return true;
                  }

                  if (!var5.equals(var6)) {
                     return true;
                  }

                  var3.remove(var4);
               }
            }

            var2 = var3.propertyNames();

            do {
               if (!var2.hasMoreElements()) {
                  return false;
               }

               var4 = (String)var2.nextElement();
            } while(var3.getProperty(var4) == null);

            return true;
         } else {
            return true;
         }
      } else {
         return var1 != null && var1.size() != 0;
      }
   }

   public static Properties createProperties(String var0, String var1, String var2, String var3, String var4) {
      Properties var5 = new Properties();
      if (var0 != null && var0.length() > 0) {
         var5.put("ConnectionURL", var0);
      }

      if (var1 != null) {
         var5.put("InitialContextFactory", var1);
      } else {
         var5.put("InitialContextFactory", "weblogic.jndi.WLInitialContextFactory");
      }

      if (var2 != null) {
         var5.put("ConnectionFactoryJNDIName", var2);
      }

      if (var3 != null) {
         var5.put("DestinationJNDIName", var3);
      }

      if (var4 != null) {
         var5.put("DestinationType", var4);
      }

      return var5;
   }
}
