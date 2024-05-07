package weblogic.connector.common;

import java.util.HashSet;
import weblogic.j2ee.dd.xml.JCAConnectionFactoryProvider;
import weblogic.j2ee.descriptor.ConnectionDefinitionBean;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.OutboundResourceAdapterBean;
import weblogic.j2ee.descriptor.ResourceAdapterBean;

public class JCAConnectionFactoryRegistry extends JCAConnectionFactoryProvider {
   private HashSet<String> connectionFactorySet = new HashSet();
   private static JCAConnectionFactoryRegistry instance = null;

   private JCAConnectionFactoryRegistry() {
   }

   public static synchronized JCAConnectionFactoryRegistry getInstance() {
      if (instance == null) {
         createInstane();
      }

      return instance;
   }

   public synchronized boolean isAdapterConnectionFactory(String var1) {
      return this.connectionFactorySet.contains(var1);
   }

   private void registerConnectionFactory(String var1) {
      if (!this.connectionFactorySet.contains(var1)) {
         this.connectionFactorySet.add(var1);
         if (Debug.isDeploymentEnabled()) {
            Debug.deployment("Registering JCA Connection Factory: " + instance);
         }
      }

   }

   public void registerConnectionFactory(ConnectorBean var1) {
      if (var1 != null) {
         ResourceAdapterBean var2 = var1.getResourceAdapter();
         if (var2 != null) {
            OutboundResourceAdapterBean var3 = var2.getOutboundResourceAdapter();
            if (var3 != null) {
               ConnectionDefinitionBean[] var4 = var3.getConnectionDefinitions();
               if (var4 != null) {
                  synchronized(this) {
                     ConnectionDefinitionBean[] var6 = var4;
                     int var7 = var4.length;

                     for(int var8 = 0; var8 < var7; ++var8) {
                        ConnectionDefinitionBean var9 = var6[var8];
                        String var10 = var9.getConnectionFactoryInterface();
                        if (var10 != null) {
                           this.registerConnectionFactory(var10.trim());
                        }
                     }
                  }
               }
            }
         }
      }

   }

   private static void createInstane() {
      instance = new JCAConnectionFactoryRegistry();
      if (Debug.isDeploymentEnabled()) {
         Debug.deployment("Registering JCAConnectionFactoryProvider: " + instance);
      }

      JCAConnectionFactoryProvider.set(instance);
   }
}
