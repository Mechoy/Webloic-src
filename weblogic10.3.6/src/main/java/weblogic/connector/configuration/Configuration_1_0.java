package weblogic.connector.configuration;

import com.bea.connector.monitoring1Dot0.ActivationspecDocument;
import com.bea.connector.monitoring1Dot0.AdminObjectGroupDocument;
import com.bea.connector.monitoring1Dot0.AdminObjectInstanceDocument;
import com.bea.connector.monitoring1Dot0.AdminObjectsDocument;
import com.bea.connector.monitoring1Dot0.ConnectionInstanceDocument;
import com.bea.connector.monitoring1Dot0.ConnectionPoolParamsType;
import com.bea.connector.monitoring1Dot0.ConnectorDocument;
import com.bea.connector.monitoring1Dot0.InboundDocument;
import com.bea.connector.monitoring1Dot0.MessagelistenerDocument;
import com.bea.connector.monitoring1Dot0.OutboundDocument;
import com.bea.connector.monitoring1Dot0.OutboundGroupDocument;
import com.bea.connector.monitoring1Dot0.RequiredConfigPropertyDocument;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import weblogic.connector.common.Debug;
import weblogic.connector.external.ActivationSpecInfo;
import weblogic.connector.external.AdminObjInfo;
import weblogic.connector.external.ElementNotFoundException;
import weblogic.connector.external.InboundInfo;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.external.RAInfo;
import weblogic.connector.external.RequiredConfigPropInfo;

public class Configuration_1_0 extends Configuration_BaseImpl {
   private final String SCHEMA_LOCATION = "weblogic/connector/extensions/connectorMonitoring_1_0.xsd";
   private final String SCHEMA_VERSION = "1.0";

   public Configuration_1_0(RAInfo var1) {
      super(var1);
   }

   public String getSchema() {
      try {
         InputStream var1 = this.getClass().getClassLoader().getResourceAsStream("weblogic/connector/extensions/connectorMonitoring_1_0.xsd");
         InputStreamReader var7 = new InputStreamReader(var1);
         char[] var3 = new char[1024];
         StringBuffer var4 = new StringBuffer("");
         boolean var5 = false;

         for(int var8 = var7.read(var3, 0, 1024); var8 != -1; var8 = var7.read(var3, 0, 1024)) {
            var4.append(var3, 0, var8);
         }

         return var4.toString();
      } catch (IOException var6) {
         String var2 = Debug.getExceptionMissingSchema();
         throw new MissingResourceException(var2, "weblogic/connector/extensions/connectorMonitoring_1_0.xsd", "weblogic/connector/extensions/connectorMonitoring_1_0.xsd");
      }
   }

   public String getConfigurationVersion() {
      return "1.0";
   }

   public String getConfiguration() {
      ConnectorDocument var1 = ConnectorDocument.Factory.newInstance();
      var1.setConnector(ConnectorDocument.Connector.Factory.newInstance());
      String var2 = this.raInfo.getJndiName();
      if (var2 != null && var2.trim().length() > 0) {
         var1.getConnector().setJndiName(var2.trim());
      }

      String var3 = this.raInfo.getSpecVersion();
      if (var3 != null && var3.trim().length() > 0) {
         var1.getConnector().setVersion(var3.trim());
      }

      String[] var4 = this.raInfo.getRADescriptions();
      if (var4 != null) {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            var1.getConnector().addDescription(var4[var5]);
         }
      }

      String var10 = this.raInfo.getVendorName();
      if (var10 != null && var10.trim().length() > 0) {
         var1.getConnector().setVendorName(var10.trim());
      }

      String var6 = this.raInfo.getEisType();
      if (var6 != null && var6.trim().length() > 0) {
         var1.getConnector().setEisType(var6.trim());
      }

      String var7 = this.raInfo.getRAVersion();
      if (var7 != null && var7.trim().length() > 0) {
         var1.getConnector().setResourceadapterVersion(var7.trim());
      }

      String var8 = this.raInfo.getRAClass();
      if (var8 != null && var8.trim().length() > 0) {
         var1.getConnector().setResourceadapterClass(var8.trim());
      }

      String var9 = this.raInfo.getLinkref();
      if (var9 != null && var9.trim().length() > 0) {
         var1.getConnector().setLinkRef(var9.trim());
      }

      var1.getConnector().setEnableAccessOutsideApp(this.raInfo.isEnableAccessOutsideApp());
      this.setupOutbound(var1);
      this.setupAdminObjects(var1);
      this.setupInbound(var1);
      return var1.toString();
   }

   private void setupOutbound(ConnectorDocument var1) {
      List var2 = null;
      Iterator var3 = null;
      OutboundInfo var4 = null;
      var2 = this.raInfo.getOutboundInfos();
      if (var2 != null) {
         var3 = var2.iterator();
         OutboundDocument.Outbound var5 = OutboundDocument.Outbound.Factory.newInstance();
         OutboundGroupDocument.OutboundGroup var6 = null;
         Hashtable var7 = new Hashtable();
         ConnectionInstanceDocument.ConnectionInstance var8 = null;
         ConnectionPoolParamsType var9 = null;
         String var10 = null;
         String var11 = null;
         String var12 = null;

         while(var3.hasNext()) {
            var4 = (OutboundInfo)var3.next();
            var5 = OutboundDocument.Outbound.Factory.newInstance();
            var6 = (OutboundGroupDocument.OutboundGroup)var7.get(var4.getCFInterface());
            if (var6 == null) {
               var6 = OutboundGroupDocument.OutboundGroup.Factory.newInstance();
               var6.setConnectionFactoryInterface(var4.getCFInterface());
               var6.setManagedconnectionfactoryClass(var4.getMCFClass());
               var7.put(var4.getCFInterface(), var6);
            }

            var8 = var6.addNewConnectionInstance();
            var10 = var4.getJndiName();
            var11 = var4.getResourceLink();
            if (var10 != null && var10.trim().length() > 0) {
               var8.setJndiName(var10.trim());
            } else {
               var8.setResourceLink(var11.trim());
            }

            var12 = var4.getDescription();
            if (var12 != null && var12.trim().length() > 0) {
               var8.setDescription(var12.trim());
            }

            var8.setTransactionSupport(var4.getTransactionSupport());
            var9 = ConnectionPoolParamsType.Factory.newInstance();
            var9.setInitialCapacity(BigInteger.valueOf((long)var4.getInitialCapacity()));
            var9.setMaxCapacity(BigInteger.valueOf((long)var4.getMaxCapacity()));
            var9.setCapacityIncrement(BigInteger.valueOf((long)var4.getCapacityIncrement()));
            var9.setShrinkingEnabled(var4.isShrinkingEnabled());
            var9.setShrinkFrequencySeconds(BigInteger.valueOf((long)var4.getShrinkFrequencySeconds()));
            var9.setHighestNumWaiters(BigInteger.valueOf((long)var4.getHighestNumWaiters()));
            var9.setHighestNumUnavailable(BigInteger.valueOf((long)var4.getHighestNumUnavailable()));
            var9.setConnectionCreationRetryFrequencySeconds(BigInteger.valueOf((long)var4.getConnectionCreationRetryFrequencySeconds()));
            var9.setConnectionReserveTimeoutSeconds(BigInteger.valueOf((long)var4.getConnectionReserveTimeoutSeconds()));
            var9.setTestFrequencySeconds(BigInteger.valueOf((long)var4.getTestFrequencySeconds()));
            var9.setTestConnectionsOnCreate(var4.isTestConnectionsOnCreate());
            var9.setTestConnectionsOnRelease(var4.isTestConnectionsOnRelease());
            var9.setTestConnectionsOnReserve(var4.isTestConnectionsOnReserve());
            var9.setProfileHarvestFrequencySeconds(BigInteger.valueOf((long)var4.getProfileHarvestFrequencySeconds()));
            var9.setIgnoreInUseConnectionsEnabled(var4.isIgnoreInUseConnectionsEnabled());
            var9.setMatchConnectionsSupported(var4.isMatchConnectionsSupported());
            var8.setPoolParams(var9);
         }

         Collection var13 = var7.values();
         Iterator var14 = var13.iterator();
         OutboundGroupDocument.OutboundGroup[] var15 = new OutboundGroupDocument.OutboundGroup[var13.size()];

         for(int var16 = 0; var14.hasNext(); ++var16) {
            var15[var16] = (OutboundGroupDocument.OutboundGroup)var14.next();
         }

         var5.setOutboundGroupArray(var15);
         var1.getConnector().setOutbound(var5);
      }

   }

   private void setupAdminObjects(ConnectorDocument var1) {
      List var2 = this.raInfo.getAdminObjs();
      Hashtable var5 = new Hashtable();
      AdminObjectGroupDocument.AdminObjectGroup var6 = null;
      AdminObjectsDocument.AdminObjects var8 = null;
      if (var2 != null && var2.size() > 0) {
         var8 = AdminObjectsDocument.AdminObjects.Factory.newInstance();
         Iterator var4 = var2.iterator();

         while(true) {
            while(var4.hasNext()) {
               AdminObjInfo var3 = (AdminObjInfo)var4.next();
               var6 = (AdminObjectGroupDocument.AdminObjectGroup)var5.get(var3.getInterface());
               if (var6 == null) {
                  var6 = AdminObjectGroupDocument.AdminObjectGroup.Factory.newInstance();
                  var6.setAdminObjectInterface(var3.getInterface());
                  var5.put(var3.getInterface(), var6);
               }

               AdminObjectInstanceDocument.AdminObjectInstance var7 = var6.addNewAdminObjectInstance();
               String var9 = var3.getJndiName();
               String var10 = var3.getResourceLink();
               if (var9 != null && var9.trim().length() > 0) {
                  var7.setJndiName(var9.trim());
               } else {
                  var7.setResourceLink(var10.trim());
               }
            }

            Collection var14 = var5.values();
            Iterator var13 = var14.iterator();
            AdminObjectGroupDocument.AdminObjectGroup[] var11 = new AdminObjectGroupDocument.AdminObjectGroup[var14.size()];

            for(int var12 = 0; var13.hasNext(); ++var12) {
               var11[var12] = (AdminObjectGroupDocument.AdminObjectGroup)var13.next();
            }

            var8.setAdminObjectGroupArray(var11);
            var1.getConnector().setAdminObjects(var8);
            break;
         }
      }

   }

   private void setupInbound(ConnectorDocument var1) {
      List var2 = null;
      Iterator var3 = null;
      InboundDocument.Inbound var4 = null;
      Hashtable var5 = new Hashtable();
      InboundInfo var6 = null;

      try {
         var2 = this.raInfo.getInboundInfos();
         var3 = var2.iterator();
         var4 = InboundDocument.Inbound.Factory.newInstance();
         List var9 = null;
         Iterator var10 = null;
         RequiredConfigPropertyDocument.RequiredConfigProperty var11 = null;
         RequiredConfigPropInfo var12 = null;

         while(true) {
            ActivationspecDocument.Activationspec var8;
            do {
               MessagelistenerDocument.Messagelistener var7;
               ActivationSpecInfo var13;
               do {
                  if (!var3.hasNext()) {
                     var1.getConnector().setInbound(var4);
                     return;
                  }

                  var6 = (InboundInfo)var3.next();
                  var7 = (MessagelistenerDocument.Messagelistener)var5.get(var6.getMsgType());
                  if (var7 == null) {
                     var7 = var4.addNewMessagelistener();
                     var7.setMessagelistenerType(var6.getMsgType());
                     var5.put(var6.getMsgType(), var7);
                  }

                  var13 = var6.getActivationSpec();
               } while(var13 == null);

               var8 = var7.addNewActivationspec();
               var8.setActivationspecClass(var13.getActivationSpecClass());
               var9 = var13.getRequiredProps();
            } while(var9 == null);

            for(var10 = var9.iterator(); var10.hasNext(); var11.setConfigPropertyName(var12.getName())) {
               var12 = (RequiredConfigPropInfo)var10.next();
               String[] var14 = var12.getDescriptions();
               var11 = var8.addNewRequiredConfigProperty();
               if (var14 != null && var14.length > 0) {
                  for(int var15 = 0; var15 < var14.length; ++var15) {
                     var11.addDescription(var14[var15]);
                  }
               }
            }
         }
      } catch (ElementNotFoundException var16) {
      }
   }
}
