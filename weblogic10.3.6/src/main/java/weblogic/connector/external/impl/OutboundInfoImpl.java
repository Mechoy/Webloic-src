package weblogic.connector.external.impl;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import weblogic.connector.common.Debug;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.external.RAInfo;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.AuthenticationMechanismBean;
import weblogic.j2ee.descriptor.ConfigPropertyBean;
import weblogic.j2ee.descriptor.ConnectionDefinitionBean;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.OutboundResourceAdapterBean;
import weblogic.j2ee.descriptor.ResourceAdapterBean;
import weblogic.j2ee.descriptor.wl.ConnectionDefinitionPropertiesBean;
import weblogic.j2ee.descriptor.wl.ConnectionInstanceBean;
import weblogic.j2ee.descriptor.wl.LoggingBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorExtensionBean;

public class OutboundInfoImpl implements OutboundInfo {
   private RAInfo raInfo = null;
   private ConnectorBean connBean = null;
   private WeblogicConnectorBean wlConnBean = null;
   private ConnectionDefinitionBean connDefn = null;
   private weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean wlConnDefnGroup = null;
   private OutboundInfo baseOutboundInfo = null;
   private ConnectionDefinitionPropertiesBean defOutRADefnProps = null;
   private ConnectionDefinitionPropertiesBean defGroupDefnProps = null;
   private ConnectionInstanceBean wlConnInstance = null;
   private ConnectionDefinitionPropertiesBean connInstanceDefnProps = null;
   private ResourceAdapterBean raBean = null;
   private OutboundResourceAdapterBean outboundRABean = null;
   String jndiName;
   List authenticationMechanisms;

   public OutboundInfoImpl(RAInfo var1, ConnectorBean var2, WeblogicConnectorBean var3, ConnectionDefinitionBean var4, weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var5, ConnectionInstanceBean var6) {
      this.raInfo = var1;
      this.connBean = var2;
      this.wlConnBean = var3;
      this.connDefn = var4;
      this.wlConnDefnGroup = var5;
      this.wlConnInstance = var6;
      if (var3.getOutboundResourceAdapter() == null) {
         Debug.throwAssertionError("wlConnBean.getOutboundResourceAdapter() == null");
      }

      this.initProperties();
      this.getJndiName();
      this.getAuthenticationMechanisms();
   }

   private void initProperties() {
      if (this.wlConnBean.getOutboundResourceAdapter().getDefaultConnectionProperties() == null) {
         this.wlConnBean.getOutboundResourceAdapter().createDefaultConnectionProperties();
      }

      if (this.wlConnDefnGroup.getDefaultConnectionProperties() == null) {
         this.wlConnDefnGroup.createDefaultConnectionProperties();
      }

      if (this.wlConnInstance.getConnectionProperties() == null) {
         this.wlConnInstance.createConnectionProperties();
      }

      this.defOutRADefnProps = this.wlConnBean.getOutboundResourceAdapter().getDefaultConnectionProperties();
      this.defGroupDefnProps = this.wlConnDefnGroup.getDefaultConnectionProperties();
      this.connInstanceDefnProps = this.wlConnInstance.getConnectionProperties();
      if (this.defOutRADefnProps.getPoolParams() == null) {
         this.defOutRADefnProps.createPoolParams();
      }

      if (this.defOutRADefnProps.getLogging() == null) {
         this.defOutRADefnProps.createLogging();
      }

      if (this.defGroupDefnProps.getPoolParams() == null) {
         this.defGroupDefnProps.createPoolParams();
      }

      if (this.defGroupDefnProps.getLogging() == null) {
         this.defGroupDefnProps.createLogging();
      }

      if (this.connInstanceDefnProps.getPoolParams() == null) {
         this.connInstanceDefnProps.createPoolParams();
      }

      if (this.connInstanceDefnProps.getLogging() == null) {
         this.connInstanceDefnProps.createLogging();
      }

   }

   private List convertAuthMechArrayToVector(AuthenticationMechanismBean[] var1) {
      Vector var2 = new Vector();
      if (var1 != null) {
         for(int var4 = 0; var4 < var1.length; ++var4) {
            AuthMechInfoImpl var3 = new AuthMechInfoImpl(var1[var4]);
            var2.add(var3);
         }
      }

      return var2;
   }

   public void setBaseOutboundInfo(OutboundInfo var1) {
      this.baseOutboundInfo = var1;
   }

   public String getRADescription() {
      return (this.raInfo.getRADescription() == null || this.raInfo.getRADescription().length() == 0) && this.baseOutboundInfo != null ? this.baseOutboundInfo.getRADescription() : this.raInfo.getRADescription();
   }

   public String getDisplayName() {
      return (this.raInfo.getDisplayName() == null || this.raInfo.getDisplayName().length() == 0) && this.baseOutboundInfo != null ? this.baseOutboundInfo.getDisplayName() : this.raInfo.getDisplayName();
   }

   public String getVendorName() {
      return (this.raInfo.getVendorName() == null || this.raInfo.getVendorName().length() == 0) && this.baseOutboundInfo != null ? this.baseOutboundInfo.getVendorName() : this.raInfo.getVendorName();
   }

   public String getEisType() {
      return (this.raInfo.getEisType() == null || this.raInfo.getEisType().length() == 0) && this.baseOutboundInfo != null ? this.baseOutboundInfo.getEisType() : this.raInfo.getEisType();
   }

   public String getTransactionSupport() {
      String var1 = null;
      if (this.connInstanceDefnProps.isSet("TransactionSupport") && this.baseOutboundInfo != null) {
         var1 = this.baseOutboundInfo.getTransactionSupport();
      } else if (this.connInstanceDefnProps.isSet("TransactionSupport")) {
         var1 = this.connInstanceDefnProps.getTransactionSupport();
      } else if (this.defGroupDefnProps.isSet("TransactionSupport")) {
         var1 = this.defGroupDefnProps.getTransactionSupport();
      } else if (this.defOutRADefnProps.isSet("TransactionSupport")) {
         var1 = this.defOutRADefnProps.getTransactionSupport();
      } else {
         var1 = this.getOutboundRABean().getTransactionSupport();
      }

      return var1;
   }

   public List getAuthenticationMechanisms() {
      if (this.authenticationMechanisms != null) {
         return this.authenticationMechanisms;
      } else if ((this.connInstanceDefnProps.getAuthenticationMechanisms() == null || this.connInstanceDefnProps.getAuthenticationMechanisms().length == 0) && this.baseOutboundInfo != null) {
         return this.baseOutboundInfo.getAuthenticationMechanisms();
      } else {
         AuthenticationMechanismBean[] var1 = this.getOutboundRABean().getAuthenticationMechanisms();
         if (this.defOutRADefnProps.getAuthenticationMechanisms() != null && this.defOutRADefnProps.getAuthenticationMechanisms().length > 0) {
            var1 = this.defOutRADefnProps.getAuthenticationMechanisms();
         }

         if (this.defGroupDefnProps.getAuthenticationMechanisms() != null && this.defGroupDefnProps.getAuthenticationMechanisms().length > 0) {
            var1 = this.defGroupDefnProps.getAuthenticationMechanisms();
         }

         if (this.connInstanceDefnProps.getAuthenticationMechanisms() != null && this.connInstanceDefnProps.getAuthenticationMechanisms().length > 0) {
            var1 = this.connInstanceDefnProps.getAuthenticationMechanisms();
         }

         this.authenticationMechanisms = var1 == null ? null : this.convertAuthMechArrayToVector(var1);
         return this.authenticationMechanisms;
      }
   }

   public boolean getReauthenticationSupport() {
      if (!((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps)).isSet("ReauthenticationSupport") && this.baseOutboundInfo != null) {
         return this.baseOutboundInfo.getReauthenticationSupport();
      } else {
         boolean var1 = this.getOutboundRABean().isReauthenticationSupport();
         if (((DescriptorBean)((DescriptorBean)this.defOutRADefnProps)).isSet("ReauthenticationSupport")) {
            var1 = this.defOutRADefnProps.isReauthenticationSupport();
         }

         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps)).isSet("ReauthenticationSupport")) {
            var1 = this.defGroupDefnProps.isReauthenticationSupport();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps)).isSet("ReauthenticationSupport")) {
            var1 = this.connInstanceDefnProps.isReauthenticationSupport();
         }

         return var1;
      }
   }

   public String getResAuth() {
      if (!((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps)).isSet("ResAuth") && this.baseOutboundInfo != null) {
         return this.baseOutboundInfo.getResAuth();
      } else {
         String var1 = this.defOutRADefnProps.getResAuth();
         if (((DescriptorBean)this.defGroupDefnProps).isSet("ResAuth")) {
            var1 = this.defGroupDefnProps.getResAuth();
         }

         if (((DescriptorBean)this.connInstanceDefnProps).isSet("ResAuth")) {
            var1 = this.connInstanceDefnProps.getResAuth();
         }

         return var1;
      }
   }

   public String getMCFClass() {
      return (this.connDefn.getManagedConnectionFactoryClass() == null || this.connDefn.getManagedConnectionFactoryClass().length() == 0) && this.baseOutboundInfo != null ? this.baseOutboundInfo.getMCFClass() : this.connDefn.getManagedConnectionFactoryClass();
   }

   public Hashtable getMCFProps() {
      if (this.connInstanceDefnProps.getProperties() == null && this.baseOutboundInfo != null) {
         return this.baseOutboundInfo.getMCFProps();
      } else {
         Hashtable var1 = new Hashtable();
         ConfigPropertyBean[] var3 = this.connDefn.getConfigProperties();

         for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
            ConfigPropInfoImpl var2 = new ConfigPropInfoImpl(var3[var4], var3[var4].getConfigPropertyValue());
            var2 = DDLayerUtils.getConfigPropInfoWithOverrides(var2, this.wlConnBean.getProperties());
            if (this.defOutRADefnProps != null) {
               var2 = DDLayerUtils.getConfigPropInfoWithOverrides(var2, this.defOutRADefnProps.getProperties());
            }

            if (this.defGroupDefnProps != null) {
               var2 = DDLayerUtils.getConfigPropInfoWithOverrides(var2, this.defGroupDefnProps.getProperties());
            }

            if (this.connInstanceDefnProps.getProperties() != null) {
               var2 = DDLayerUtils.getConfigPropInfoWithOverrides(var2, this.connInstanceDefnProps.getProperties());
            }

            if (var2.getName() != null) {
               var1.put(var2.getName(), var2);
            }
         }

         return var1;
      }
   }

   public String getCFInterface() {
      return (this.connDefn.getConnectionFactoryInterface() == null || this.connDefn.getConnectionFactoryInterface().length() == 0) && this.baseOutboundInfo != null ? this.baseOutboundInfo.getCFInterface() : this.connDefn.getConnectionFactoryInterface();
   }

   public String getCFImpl() {
      return (this.connDefn.getConnectionFactoryImplClass() == null || this.connDefn.getConnectionFactoryImplClass().length() == 0) && this.baseOutboundInfo != null ? this.baseOutboundInfo.getCFImpl() : this.connDefn.getConnectionFactoryImplClass();
   }

   public String getConnectionInterface() {
      return (this.connDefn.getConnectionInterface() == null || this.connDefn.getConnectionInterface().length() == 0) && this.baseOutboundInfo != null ? this.baseOutboundInfo.getConnectionInterface() : this.connDefn.getConnectionInterface();
   }

   public String getConnectionImpl() {
      return (this.connDefn.getConnectionImplClass() == null || this.connDefn.getConnectionImplClass().length() == 0) && this.baseOutboundInfo != null ? this.baseOutboundInfo.getConnectionImpl() : this.connDefn.getConnectionImplClass();
   }

   public String getConnectionFactoryName() {
      String var1 = null;
      if (this.isPreDiabloRA() && ((WeblogicConnectorExtensionBean)this.wlConnBean).getLinkRef() != null) {
         var1 = ((WeblogicConnectorExtensionBean)this.wlConnBean).getLinkRef().getConnectionFactoryName();
      }

      return var1;
   }

   public String getJndiName() {
      if (this.jndiName != null) {
         return this.jndiName;
      } else {
         if (this.wlConnInstance != null) {
            this.jndiName = this.wlConnInstance.getJNDIName();
         }

         return this.jndiName;
      }
   }

   public String getResourceLink() {
      return this.wlConnInstance.getResourceLink();
   }

   public int getInitialCapacity() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getInitialCapacity();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getInitialCapacity();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("InitialCapacity")) {
            var1 = this.defGroupDefnProps.getPoolParams().getInitialCapacity();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("InitialCapacity")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getInitialCapacity();
         }

         return var1;
      }
   }

   public int getMaxCapacity() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getMaxCapacity();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getMaxCapacity();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("MaxCapacity")) {
            var1 = this.defGroupDefnProps.getPoolParams().getMaxCapacity();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("MaxCapacity")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getMaxCapacity();
         }

         return var1;
      }
   }

   public int getCapacityIncrement() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getCapacityIncrement();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getCapacityIncrement();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("CapacityIncrement")) {
            var1 = this.defGroupDefnProps.getPoolParams().getCapacityIncrement();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("CapacityIncrement")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getCapacityIncrement();
         }

         return var1;
      }
   }

   public boolean isShrinkingEnabled() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.isShrinkingEnabled();
      } else {
         boolean var1 = this.defOutRADefnProps.getPoolParams().isShrinkingEnabled();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("ShrinkingEnabled")) {
            var1 = this.defGroupDefnProps.getPoolParams().isShrinkingEnabled();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("ShrinkingEnabled")) {
            var1 = this.connInstanceDefnProps.getPoolParams().isShrinkingEnabled();
         }

         return var1;
      }
   }

   public int getShrinkFrequencySeconds() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getShrinkFrequencySeconds();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getShrinkFrequencySeconds();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("ShrinkFrequencySeconds")) {
            var1 = this.defGroupDefnProps.getPoolParams().getShrinkFrequencySeconds();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("ShrinkFrequencySeconds")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getShrinkFrequencySeconds();
         }

         return var1;
      }
   }

   public int getInactiveConnectionTimeoutSeconds() {
      int var1 = 0;
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getInactiveConnectionTimeoutSeconds();
      } else {
         if (this.isPreDiabloRA() && ((WeblogicConnectorExtensionBean)this.wlConnBean).getProxy() != null) {
            var1 = ((WeblogicConnectorExtensionBean)this.wlConnBean).getProxy().getInactiveConnectionTimeoutSeconds();
         }

         return var1;
      }
   }

   public boolean getConnectionProfilingEnabled() {
      boolean var1 = false;
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getConnectionProfilingEnabled();
      } else {
         if (this.isPreDiabloRA() && ((WeblogicConnectorExtensionBean)this.wlConnBean).getProxy() != null) {
            var1 = ((WeblogicConnectorExtensionBean)this.wlConnBean).getProxy().isConnectionProfilingEnabled();
         }

         return var1;
      }
   }

   public int getHighestNumWaiters() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getHighestNumWaiters();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getHighestNumWaiters();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("HighestNumWaiters")) {
            var1 = this.defGroupDefnProps.getPoolParams().getHighestNumWaiters();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("HighestNumWaiters")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getHighestNumWaiters();
         }

         return var1;
      }
   }

   public int getHighestNumUnavailable() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getHighestNumUnavailable();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getHighestNumUnavailable();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("HighestNumUnavailable")) {
            var1 = this.defGroupDefnProps.getPoolParams().getHighestNumUnavailable();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("HighestNumUnavailable")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getHighestNumUnavailable();
         }

         return var1;
      }
   }

   public int getConnectionCreationRetryFrequencySeconds() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getConnectionCreationRetryFrequencySeconds();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getConnectionCreationRetryFrequencySeconds();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("ConnectionCreationRetryFrequencySeconds")) {
            var1 = this.defGroupDefnProps.getPoolParams().getConnectionCreationRetryFrequencySeconds();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("ConnectionCreationRetryFrequencySeconds")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getConnectionCreationRetryFrequencySeconds();
         }

         return var1;
      }
   }

   public int getConnectionReserveTimeoutSeconds() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getConnectionReserveTimeoutSeconds();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getConnectionReserveTimeoutSeconds();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("ConnectionReserveTimeoutSeconds")) {
            var1 = this.defGroupDefnProps.getPoolParams().getConnectionReserveTimeoutSeconds();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("ConnectionReserveTimeoutSeconds")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getConnectionReserveTimeoutSeconds();
         }

         return var1;
      }
   }

   public int getTestFrequencySeconds() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getTestFrequencySeconds();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getTestFrequencySeconds();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("TestFrequencySeconds")) {
            var1 = this.defGroupDefnProps.getPoolParams().getTestFrequencySeconds();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("TestFrequencySeconds")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getTestFrequencySeconds();
         }

         return var1;
      }
   }

   public boolean isTestConnectionsOnCreate() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.isTestConnectionsOnCreate();
      } else {
         boolean var1 = this.defOutRADefnProps.getPoolParams().isTestConnectionsOnCreate();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("TestConnectionsOnCreate")) {
            var1 = this.defGroupDefnProps.getPoolParams().isTestConnectionsOnCreate();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("TestConnectionsOnCreate")) {
            var1 = this.connInstanceDefnProps.getPoolParams().isTestConnectionsOnCreate();
         }

         return var1;
      }
   }

   public boolean isTestConnectionsOnRelease() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.isTestConnectionsOnRelease();
      } else {
         boolean var1 = this.defOutRADefnProps.getPoolParams().isTestConnectionsOnRelease();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("TestConnectionsOnRelease")) {
            var1 = this.defGroupDefnProps.getPoolParams().isTestConnectionsOnRelease();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("TestConnectionsOnRelease")) {
            var1 = this.connInstanceDefnProps.getPoolParams().isTestConnectionsOnRelease();
         }

         return var1;
      }
   }

   public boolean isTestConnectionsOnReserve() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.isTestConnectionsOnReserve();
      } else {
         boolean var1 = this.defOutRADefnProps.getPoolParams().isTestConnectionsOnReserve();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("TestConnectionsOnReserve")) {
            var1 = this.defGroupDefnProps.getPoolParams().isTestConnectionsOnReserve();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("TestConnectionsOnReserve")) {
            var1 = this.connInstanceDefnProps.getPoolParams().isTestConnectionsOnReserve();
         }

         return var1;
      }
   }

   public Boolean getUseConnectionProxies() {
      Boolean var1 = null;
      if (!this.isPreDiabloRA()) {
         var1 = new Boolean(false);
      } else if (this.isLinkRefOverridingPoolParams()) {
         var1 = this.baseOutboundInfo.getUseConnectionProxies();
      } else if (((WeblogicConnectorExtensionBean)this.wlConnBean).getProxy() != null) {
         String var2 = ((WeblogicConnectorExtensionBean)this.wlConnBean).getProxy().getUseConnectionProxies();
         if (var2 != null) {
            var1 = new Boolean(var2);
         }
      }

      return var1;
   }

   public RAInfo getRAInfo() {
      return this.raInfo;
   }

   public String getRaLinkRef() {
      String var1 = null;
      if (this.isPreDiabloRA() && ((WeblogicConnectorExtensionBean)this.wlConnBean).getLinkRef() != null) {
         var1 = ((WeblogicConnectorExtensionBean)this.wlConnBean).getLinkRef().getRaLinkRef();
      }

      return var1;
   }

   public LoggingBean getLoggingBean() {
      return this.connInstanceDefnProps.getLogging();
   }

   public boolean isMatchConnectionsSupported() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.isMatchConnectionsSupported();
      } else {
         boolean var1 = this.defOutRADefnProps.getPoolParams().isMatchConnectionsSupported();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("MatchConnectionsSupported")) {
            var1 = this.defGroupDefnProps.getPoolParams().isMatchConnectionsSupported();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("MatchConnectionsSupported")) {
            var1 = this.connInstanceDefnProps.getPoolParams().isMatchConnectionsSupported();
         }

         return var1;
      }
   }

   public boolean isUseFirstAvailable() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.isUseFirstAvailable();
      } else {
         boolean var1 = this.defOutRADefnProps.getPoolParams().isUseFirstAvailable();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("UseFirstAvailable")) {
            var1 = this.defGroupDefnProps.getPoolParams().isUseFirstAvailable();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("UseFirstAvailable")) {
            var1 = this.connInstanceDefnProps.getPoolParams().isUseFirstAvailable();
         }

         return var1;
      }
   }

   private boolean isLinkRefOverridingPoolParams() {
      if (this.connInstanceDefnProps == null) {
         Debug.throwAssertionError("connInstanceDefnProps == null");
      }

      if (this.connInstanceDefnProps.getPoolParams() == null) {
         Debug.throwAssertionError("connInstanceDefnProps.getPoolParams() == null");
      }

      return this.connInstanceDefnProps.getPoolParams().getMaxCapacity() == 0;
   }

   private boolean isPreDiabloRA() {
      return this.wlConnBean instanceof WeblogicConnectorExtensionBean;
   }

   private ResourceAdapterBean getRABean() {
      Debug.println(this, ".getRABean()");
      if (this.raBean == null) {
         this.raBean = this.connBean.getResourceAdapter();
      }

      Debug.println(this, ".getRABean() returning " + (this.raBean != null ? "non-null" : "null"));
      if (this.raBean != null) {
         Debug.println(this, ".getRABean().getResourceAdapterClass() = " + this.raBean.getResourceAdapterClass());
      }

      return this.raBean;
   }

   private OutboundResourceAdapterBean getOutboundRABean() {
      if (this.outboundRABean == null) {
         this.outboundRABean = this.getRABean().getOutboundResourceAdapter();
      }

      return this.outboundRABean;
   }

   public int getProfileHarvestFrequencySeconds() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.getProfileHarvestFrequencySeconds();
      } else {
         int var1 = this.defOutRADefnProps.getPoolParams().getProfileHarvestFrequencySeconds();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("ProfileHarvestFrequencySeconds")) {
            var1 = this.defGroupDefnProps.getPoolParams().getProfileHarvestFrequencySeconds();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("ProfileHarvestFrequencySeconds")) {
            var1 = this.connInstanceDefnProps.getPoolParams().getProfileHarvestFrequencySeconds();
         }

         return var1;
      }
   }

   public boolean isIgnoreInUseConnectionsEnabled() {
      if (this.isLinkRefOverridingPoolParams()) {
         return this.baseOutboundInfo.isIgnoreInUseConnectionsEnabled();
      } else {
         boolean var1 = this.defOutRADefnProps.getPoolParams().isIgnoreInUseConnectionsEnabled();
         if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getPoolParams())).isSet("IgnoreInUseConnectionsEnabled")) {
            var1 = this.defGroupDefnProps.getPoolParams().isIgnoreInUseConnectionsEnabled();
         }

         if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getPoolParams())).isSet("IgnoreInUseConnectionsEnabled")) {
            var1 = this.connInstanceDefnProps.getPoolParams().isIgnoreInUseConnectionsEnabled();
         }

         return var1;
      }
   }

   public String getKey() {
      String var1 = null;
      String var2 = this.getJndiName();
      if (var2 != null && var2.length() != 0) {
         var1 = var2;
      } else {
         var1 = this.getResourceLink();
      }

      return var1;
   }

   public String getLogFilename() {
      String var1 = this.defOutRADefnProps.getLogging().getLogFilename();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("LogFilename")) {
         var1 = this.defGroupDefnProps.getLogging().getLogFilename();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("LogFilename")) {
         var1 = this.connInstanceDefnProps.getLogging().getLogFilename();
      }

      return var1;
   }

   public boolean isLoggingEnabled() {
      boolean var1 = this.defOutRADefnProps.getLogging().isLoggingEnabled();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("LoggingEnabled")) {
         var1 = this.defGroupDefnProps.getLogging().isLoggingEnabled();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("LoggingEnabled")) {
         var1 = this.connInstanceDefnProps.getLogging().isLoggingEnabled();
      }

      return var1;
   }

   public String getRotationType() {
      String var1 = this.defOutRADefnProps.getLogging().getRotationType();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("RotationType")) {
         var1 = this.defGroupDefnProps.getLogging().getRotationType();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("RotationType")) {
         var1 = this.connInstanceDefnProps.getLogging().getRotationType();
      }

      return var1;
   }

   public String getRotationTime() {
      String var1 = this.defOutRADefnProps.getLogging().getRotationTime();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("RotationTime")) {
         var1 = this.defGroupDefnProps.getLogging().getRotationTime();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("RotationTime")) {
         var1 = this.connInstanceDefnProps.getLogging().getRotationTime();
      }

      return var1;
   }

   public boolean isNumberOfFilesLimited() {
      boolean var1 = this.defOutRADefnProps.getLogging().isNumberOfFilesLimited();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("NumberOfFilesLimited")) {
         var1 = this.defGroupDefnProps.getLogging().isNumberOfFilesLimited();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("NumberOfFilesLimited")) {
         var1 = this.connInstanceDefnProps.getLogging().isNumberOfFilesLimited();
      }

      return var1;
   }

   public int getFileCount() {
      int var1 = this.defOutRADefnProps.getLogging().getFileCount();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("FileCount")) {
         var1 = this.defGroupDefnProps.getLogging().getFileCount();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("FileCount")) {
         var1 = this.connInstanceDefnProps.getLogging().getFileCount();
      }

      return var1;
   }

   public int getFileSizeLimit() {
      int var1 = this.defOutRADefnProps.getLogging().getFileSizeLimit();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("FileSizeLimit")) {
         var1 = this.defGroupDefnProps.getLogging().getFileSizeLimit();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("FileSizeLimit")) {
         var1 = this.connInstanceDefnProps.getLogging().getFileSizeLimit();
      }

      return var1;
   }

   public int getFileTimeSpan() {
      int var1 = this.defOutRADefnProps.getLogging().getFileTimeSpan();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("FileTimeSpan")) {
         var1 = this.defGroupDefnProps.getLogging().getFileTimeSpan();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("FileTimeSpan")) {
         var1 = this.connInstanceDefnProps.getLogging().getFileTimeSpan();
      }

      return var1;
   }

   public boolean isRotateLogOnStartup() {
      boolean var1 = this.defOutRADefnProps.getLogging().isRotateLogOnStartup();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("RotateLogOnStartup")) {
         var1 = this.defGroupDefnProps.getLogging().isRotateLogOnStartup();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("RotateLogOnStartup")) {
         var1 = this.connInstanceDefnProps.getLogging().isRotateLogOnStartup();
      }

      return var1;
   }

   public String getLogFileRotationDir() {
      String var1 = this.defOutRADefnProps.getLogging().getLogFileRotationDir();
      if (((DescriptorBean)((DescriptorBean)this.defGroupDefnProps.getLogging())).isSet("LogFileRotationDir")) {
         var1 = this.defGroupDefnProps.getLogging().getLogFileRotationDir();
      }

      if (((DescriptorBean)((DescriptorBean)this.connInstanceDefnProps.getLogging())).isSet("LogFileRotationDir")) {
         var1 = this.connInstanceDefnProps.getLogging().getLogFileRotationDir();
      }

      return var1;
   }

   public String getDescription() {
      return this.wlConnInstance.getDescription();
   }
}
