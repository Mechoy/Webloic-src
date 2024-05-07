package weblogic.jms.bridge.internal;

import java.util.Enumeration;
import java.util.Properties;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanRemoveRejectedException;
import weblogic.jms.JMSLogger;
import weblogic.management.ManagementException;
import weblogic.management.configuration.BridgeDestinationCommonMBean;
import weblogic.management.configuration.BridgeDestinationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSBridgeDestinationMBean;
import weblogic.management.configuration.MessagingBridgeMBean;
import weblogic.management.provider.ConfigurationProcessor;
import weblogic.management.provider.UpdateException;

public class BridgeConfigUpdater implements ConfigurationProcessor {
   public void updateConfiguration(DomainMBean var1) throws UpdateException {
      try {
         MessagingBridgeMBean[] var3 = var1.getMessagingBridges();

         int var2;
         for(var2 = 0; var3 != null && var2 < var3.length; ++var2) {
            this.updateMessagingBridge(var1, var3[var2]);
         }

         BridgeDestinationMBean[] var4 = var1.getBridgeDestinations();

         for(var2 = 0; var4 != null && var2 < var4.length; ++var2) {
            this.updateBridgeDestination(var1, var4[var2]);
         }

      } catch (InvalidAttributeValueException var5) {
         throw new UpdateException(var5);
      } catch (ManagementException var6) {
         throw new UpdateException(var6);
      }
   }

   private void updateMessagingBridge(DomainMBean var1, MessagingBridgeMBean var2) throws InvalidAttributeValueException, ManagementException {
      BridgeDestinationCommonMBean var3 = var2.getSourceDestination();
      if (var3 != null && var3 instanceof BridgeDestinationMBean) {
         var2.setSourceDestination((BridgeDestinationCommonMBean)null);
         this.updateBridgeDestination(var1, (BridgeDestinationMBean)var3);
         var2.setSourceDestination(this.updateBridgeDestination(var1, (BridgeDestinationMBean)var3));
      }

      BridgeDestinationCommonMBean var4 = var2.getTargetDestination();
      if (var4 != null && var4 instanceof BridgeDestinationMBean) {
         var2.setTargetDestination((BridgeDestinationCommonMBean)null);
         this.updateBridgeDestination(var1, (BridgeDestinationMBean)var4);
         var2.setTargetDestination(this.updateBridgeDestination(var1, (BridgeDestinationMBean)var4));
      }

   }

   private BridgeDestinationCommonMBean updateBridgeDestination(DomainMBean var1, BridgeDestinationMBean var2) throws InvalidAttributeValueException, ManagementException {
      JMSBridgeDestinationMBean var3 = var1.lookupJMSBridgeDestination(var2.getName());
      if (var3 != null) {
         this.destroyOldBean(var1, var2);
         return var3;
      } else {
         JMSLogger.logReplacingBridgeDestinationMBean(var2.getName());
         var3 = var1.createJMSBridgeDestination(var2.getName());
         if (var2.getAdapterJNDIName() != null) {
            var3.setAdapterJNDIName(var2.getAdapterJNDIName());
         }

         if (var2.getUserName() != null) {
            var3.setUserName(var2.getUserName());
         }

         if (var2.getUserPassword() != null) {
            var3.setUserPassword(var2.getUserPassword());
         }

         if (var2.getClasspath() != null) {
            var3.setClasspath(var2.getClasspath());
         }

         Properties var4 = var2.getProperties();
         int var5 = 0;
         if (var4 != null && var4.size() != 0) {
            Enumeration var6 = var4.propertyNames();

            label59:
            while(true) {
               while(true) {
                  while(true) {
                     if (!var6.hasMoreElements()) {
                        break label59;
                     }

                     String var7 = (String)var6.nextElement();
                     String var8 = var4.getProperty(var7);
                     if (!var7.equalsIgnoreCase("ConnectionFactoryJNDI") && !var7.equalsIgnoreCase("ConnectionFactoryJNDIName")) {
                        if (var7.equalsIgnoreCase("ConnectionURL")) {
                           var3.setConnectionURL(var8);
                        } else if (var7.equalsIgnoreCase("InitialContextFactory")) {
                           var3.setInitialContextFactory(var8);
                        } else if (!var7.equalsIgnoreCase("DestinationJNDI") && !var7.equalsIgnoreCase("DestinationJNDIName")) {
                           if (var7.equalsIgnoreCase("DestinationType")) {
                              var3.setDestinationType(var8);
                           }
                        } else {
                           var3.setDestinationJNDIName(var8);
                           ++var5;
                        }
                     } else {
                        var3.setConnectionFactoryJNDIName(var8);
                        ++var5;
                     }
                  }
               }
            }
         }

         if (var5 < 2) {
            var1.destroyJMSBridgeDestination(var3);
            return var2;
         } else {
            this.destroyOldBean(var1, var2);
            return var3;
         }
      }
   }

   private void destroyOldBean(DomainMBean var1, BridgeDestinationMBean var2) {
      try {
         var1.destroyBridgeDestination(var2);
      } catch (BeanRemoveRejectedException var4) {
      }

   }
}
