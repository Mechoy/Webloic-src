package weblogic.connector.external.impl;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import weblogic.connector.common.Debug;
import weblogic.connector.external.AdminObjInfo;
import weblogic.connector.external.AuthMechInfo;
import weblogic.connector.external.ElementNotFoundException;
import weblogic.connector.external.LoggingInfo;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.external.PoolInfo;
import weblogic.connector.external.RAInfo;
import weblogic.connector.external.SecurityIdentityInfo;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.AdminObjectBean;
import weblogic.j2ee.descriptor.AuthenticationMechanismBean;
import weblogic.j2ee.descriptor.ConnectionDefinitionBean;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.IconBean;
import weblogic.j2ee.descriptor.InboundResourceAdapterBean;
import weblogic.j2ee.descriptor.MessageAdapterBean;
import weblogic.j2ee.descriptor.MessageListenerBean;
import weblogic.j2ee.descriptor.OutboundResourceAdapterBean;
import weblogic.j2ee.descriptor.ResourceAdapterBean;
import weblogic.j2ee.descriptor.SecurityPermissionBean;
import weblogic.j2ee.descriptor.wl.AdminObjectGroupBean;
import weblogic.j2ee.descriptor.wl.AdminObjectInstanceBean;
import weblogic.j2ee.descriptor.wl.AdminObjectsBean;
import weblogic.j2ee.descriptor.wl.ConfigPropertyBean;
import weblogic.j2ee.descriptor.wl.ConnectionDefinitionPropertiesBean;
import weblogic.j2ee.descriptor.wl.ConnectionInstanceBean;
import weblogic.j2ee.descriptor.wl.ResourceAdapterSecurityBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorExtensionBean;
import weblogic.j2ee.descriptor.wl.WorkManagerBean;

public class RAInfoImpl implements RAInfo {
   private static String DEFAULT_NATIVE_LIBDIR = "/temp/nativelibs/";
   private ConnectorBean connBean;
   private WeblogicConnectorBean wlConnBean;
   private RAInfo baseRA = null;
   private ResourceAdapterSecurityBean raSecurityBean;
   private WorkManagerBean workManagerBean;
   private URL url;
   private String moduleName;
   public static final RAInfo factoryHelper = new RAInfoImpl();
   private Vector inboundInfos = null;
   private ResourceAdapterBean raBean = null;
   private OutboundResourceAdapterBean outboundRABean = null;
   private weblogic.j2ee.descriptor.wl.OutboundResourceAdapterBean wlOutboundRABean = null;
   private InboundResourceAdapterBean inboundRABean = null;
   private MessageListenerBean[] msgListeners = null;

   public RAInfo createRAInfo(ConnectorBean var1, WeblogicConnectorBean var2, URL var3, String var4) {
      return new RAInfoImpl(var1, var2, var3, var4);
   }

   private RAInfoImpl() {
   }

   private RAInfoImpl(ConnectorBean var1, WeblogicConnectorBean var2, URL var3, String var4) {
      Debug.println(this, "( moduleName = '" + var4 + "' )");
      if (var2 == null) {
         Debug.throwAssertionError("WeblogicConnectorBean == null");
      }

      if (var3 == null) {
         Debug.throwAssertionError("URL == null");
      }

      if (var4 == null || var4.trim().equals("")) {
         Debug.throwAssertionError("ModuleName == null or the empty string");
      }

      if (var1 == null) {
         Debug.parsing("connectorBean == null");
      }

      this.moduleName = var4;
      this.url = var3;
      this.setConnBean(var1);
      this.setWeblogicConnectorBean(var2);
      this.dump();
   }

   public void dump() {
      if (Debug.isParsingEnabled()) {
         try {
            ByteArrayOutputStream var1;
            if (this.connBean != null) {
               var1 = new ByteArrayOutputStream();
               ((DescriptorBean)this.connBean).getDescriptor().toXML(var1);
               Debug.parsing("ConnectorBean = \n" + var1.toString());
            }

            if (this.wlConnBean != null) {
               var1 = new ByteArrayOutputStream();
               ((DescriptorBean)this.wlConnBean).getDescriptor().toXML(var1);
               Debug.parsing("WeblogicConnectorBean = \n" + var1.toString());
            }
         } catch (Exception var2) {
            var2.printStackTrace();
         }

      }
   }

   public void setBaseRA(RAInfo var1) {
      Debug.println(this, ".setBaseRA()");
      if (var1 == null) {
         Debug.throwAssertionError("RAInfo == null");
      }

      this.baseRA = var1;
      ConnectorBean var2 = ((RAInfoImpl)this.baseRA).getConnectorBean();
      if (var2 == null) {
         Debug.throwAssertionError("RAInfo.getConnBean() == null");
      }

      this.setConnBean(var2);
      if (this.getOutboundRABean() == null) {
         Debug.throwAssertionError("RAInfo.getOutboundRABean() == null");
      }

      ConnectionDefinitionBean[] var3 = this.getOutboundRABean().getConnectionDefinitions();
      if (var3.length == 0) {
         Debug.throwAssertionError("connDefns.length == 0");
      }

      String var4 = var3[0].getConnectionFactoryInterface();
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean[] var5 = this.getWeblogicOutboundRABean().getConnectionDefinitionGroups();
      var5[0].setConnectionFactoryInterface(var4);
   }

   private void setConnBean(ConnectorBean var1) {
      this.connBean = var1;
      this.inboundInfos = null;
      this.raBean = null;
      this.outboundRABean = null;
      this.inboundRABean = null;
      this.msgListeners = null;
      if (this.connBean != null && this.connBean.getLicense() == null) {
         this.connBean.createLicense();
      }

   }

   private void setWeblogicConnectorBean(WeblogicConnectorBean var1) {
      this.wlConnBean = var1;
      if (this.wlConnBean.getSecurity() == null) {
         this.raSecurityBean = this.wlConnBean.createSecurity();
      } else {
         this.raSecurityBean = this.wlConnBean.getSecurity();
      }

      this.workManagerBean = this.wlConnBean.getWorkManager();
   }

   public WeblogicConnectorBean getWeblogicConnectorBean() {
      return this.wlConnBean;
   }

   public List getOutboundInfos() {
      Vector var1 = new Vector();
      ConnectionDefinitionBean[] var2 = null;
      OutboundInfo var4 = null;
      if (this.baseRA != null) {
         var4 = (OutboundInfo)this.baseRA.getOutboundInfos().get(0);
      }

      if (this.getOutboundRABean() != null) {
         var2 = this.getOutboundRABean().getConnectionDefinitions();
         Debug.println(this, ".getOutboundInfos() found " + (var2 == null ? 0 : var2.length) + " connection definitions.");
      }

      for(int var7 = 0; var2 != null && var7 < var2.length; ++var7) {
         weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var3 = this.getConnectionDefnRef(var2[var7], this.connBean, this.wlConnBean);
         if (var3 != null && var3.getConnectionInstances() != null && var3.getConnectionInstances().length != 0) {
            ConnectionInstanceBean[] var5 = var3.getConnectionInstances();

            for(int var8 = 0; var8 < var3.getConnectionInstances().length; ++var8) {
               OutboundInfoImpl var6 = new OutboundInfoImpl(this, this.connBean, this.wlConnBean, var2[var7], var3, var5[var8]);
               var1.add(var6);
               if (var4 != null) {
                  var6.setBaseOutboundInfo(var4);
               }
            }
         }
      }

      return var1;
   }

   public OutboundInfo getOutboundInfo(String var1) {
      OutboundInfo var2 = null;
      List var3 = this.getOutboundInfos();
      Iterator var4 = var3.iterator();
      OutboundInfo var5 = null;

      while(var4.hasNext()) {
         var5 = (OutboundInfo)var4.next();
         if (var5.getKey().equals(var1)) {
            var2 = var5;
            break;
         }
      }

      return var2;
   }

   public List getInboundInfos() throws ElementNotFoundException {
      Debug.println(this, ".getInboundInfos()");
      if (this.inboundInfos == null) {
         Debug.println(this, ".getInboundInfos() computing/caching InboundInfos");
         MessageListenerBean[] var2 = this.getMessageListeners();
         this.inboundInfos = new Vector();
         Debug.println(this, ".getInboundInfos() found " + var2.length + " MessageListeners");

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Debug.println(this, ".getInboundInfos() creating InboundInfoImpl for listener: '" + this.msgListeners[var3] + "'");
            InboundInfoImpl var1 = new InboundInfoImpl(this, this.msgListeners[var3]);
            this.inboundInfos.add(var1);
         }
      }

      return this.inboundInfos;
   }

   public String getRADescription() {
      String var1 = null;
      String[] var2 = this.connBean.getDescriptions();
      if (var2.length != 0) {
         var1 = var2[0];
      }

      return var1;
   }

   public String getDisplayName() {
      String var1 = null;
      String[] var2 = this.connBean.getDisplayNames();
      if (var2.length != 0) {
         var1 = var2[0];
      }

      return var1;
   }

   public String getSmallIcon() {
      String var1 = null;
      IconBean[] var2 = this.connBean.getIcons();
      if (var2.length != 0) {
         var1 = var2[0].getSmallIcon();
      }

      return var1;
   }

   public String getLargeIcon() {
      String var1 = null;
      IconBean[] var2 = this.connBean.getIcons();
      if (var2.length != 0) {
         var1 = var2[0].getLargeIcon();
      }

      return var1;
   }

   public String getVendorName() {
      return this.connBean.getVendorName();
   }

   public String getEisType() {
      return this.connBean != null ? this.connBean.getEisType() : null;
   }

   public String getRAVersion() {
      return this.connBean.getResourceAdapterVersion();
   }

   public String getLicenseDescription() {
      String var1 = null;
      if (this.connBean.getLicense() == null) {
         Debug.throwAssertionError("connBean.getLicense() == null");
      }

      String[] var2 = this.connBean.getLicense().getDescriptions();
      if (var2 != null && var2.length != 0) {
         var1 = var2[0];
      }

      return var1;
   }

   public String[] getLicenseDescriptions() {
      if (this.connBean.getLicense() == null) {
         Debug.throwAssertionError("connBean.getLicense() == null");
      }

      return this.connBean.getLicense().getDescriptions();
   }

   public boolean getLicenseRequired() {
      if (this.connBean.getLicense() == null) {
         Debug.throwAssertionError("connBean.getLicense() == null");
      }

      return this.connBean.getLicense().isLicenseRequired();
   }

   public String getRAClass() {
      return this.getRABean().getResourceAdapterClass();
   }

   public List getAdminObjs() {
      Vector var1 = new Vector();
      AdminObjectBean[] var2 = this.getRABean().getAdminObjects();
      AdminObjectsBean var3 = this.wlConnBean.getAdminObjects();
      if (var3 != null) {
         AdminObjectGroupBean[] var5 = var3.getAdminObjectGroups();

         for(int var8 = 0; var8 < var2.length; ++var8) {
            AdminObjectGroupBean var4 = this.getAdminObjectGroup(var2[var8], var5);
            if (var4 != null && var4.getAdminObjectInstances() != null && var4.getAdminObjectInstances().length != 0) {
               AdminObjectInstanceBean[] var6 = var4.getAdminObjectInstances();

               for(int var9 = 0; var9 < var6.length; ++var9) {
                  AdminObjInfoImpl var7 = new AdminObjInfoImpl(var2[var8], var6[var9], this.wlConnBean, var4);
                  var1.add(var7);
               }
            }
         }
      }

      return var1;
   }

   public String getNativeLibDir() {
      return this.wlConnBean != null ? this.wlConnBean.getNativeLibdir() : this.getDefaultNativeLibDir();
   }

   private Hashtable getWLRAConfigPropertyOverrides() {
      Hashtable var1 = new Hashtable();
      ConfigPropertyBean[] var2 = null;
      if (this.wlConnBean != null && this.wlConnBean.getProperties() != null) {
         var2 = this.wlConnBean.getProperties().getProperties();
      }

      int var4 = var2 != null ? var2.length : 0;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var3 = (new String(var2[var5].getName())).toLowerCase();
         var1.put(var3, var2[var5].getValue());
      }

      return var1;
   }

   public Hashtable getRAConfigProps() {
      weblogic.j2ee.descriptor.ConfigPropertyBean[] var1 = this.getRABean().getConfigProperties();
      Hashtable var2 = this.getWLRAConfigPropertyOverrides();
      if (var1 == null) {
         return null;
      } else {
         Hashtable var3 = new Hashtable();

         for(int var5 = 0; var5 < var1.length; ++var5) {
            String var6 = (new String(var1[var5].getConfigPropertyName())).toLowerCase();
            String var7 = (String)var2.get(var6);
            if (var7 == null) {
               var7 = var1[var5].getConfigPropertyValue();
            }

            ConfigPropInfoImpl var4 = new ConfigPropInfoImpl(var1[var5], var7);
            if (var6 != null) {
               var3.put(var6, var4);
            }
         }

         return var3;
      }
   }

   public String getLinkref() {
      String var1 = null;
      if (this.isPreDiabloRA() && ((WeblogicConnectorExtensionBean)this.wlConnBean).getLinkRef() != null) {
         var1 = ((WeblogicConnectorExtensionBean)this.wlConnBean).getLinkRef().getRaLinkRef();
      }

      return var1;
   }

   public String getConnectionFactoryName() {
      String var1 = null;
      if (this.isPreDiabloRA() && ((WeblogicConnectorExtensionBean)this.wlConnBean).getLinkRef() != null) {
         var1 = ((WeblogicConnectorExtensionBean)this.wlConnBean).getLinkRef().getConnectionFactoryName();
      }

      return var1;
   }

   public String getSpecVersion() {
      if (this.connBean.getVersion() == null) {
         Debug.throwAssertionError("connBean.getVersion() == null");
      }

      return this.connBean.getVersion().toString();
   }

   public String getJndiName() {
      return this.wlConnBean.getJNDIName();
   }

   public List getSecurityPermissions() {
      SecurityPermissionBean[] var1 = this.getRABean().getSecurityPermissions();
      Vector var2 = new Vector();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2.add(new SecurityPermissionInfoImpl(var1[var3]));
         }
      }

      return var2;
   }

   public SecurityIdentityInfo getSecurityIdentityInfo() {
      ResourceAdapterSecurityBean var1 = this.wlConnBean.getSecurity();
      return var1 != null ? new SecurityIdentityInfoImpl(var1) : null;
   }

   public URL getURL() {
      return this.url;
   }

   public boolean isEnableAccessOutsideApp() {
      return this.wlConnBean.isEnableAccessOutsideApp();
   }

   public boolean isEnableGlobalAccessToClasses() {
      return this.wlConnBean.isEnableGlobalAccessToClasses();
   }

   public WorkManagerBean getWorkManager() {
      return this.workManagerBean;
   }

   private AdminObjectGroupBean getAdminObjectGroup(AdminObjectBean var1, AdminObjectGroupBean[] var2) {
      AdminObjectGroupBean var3 = null;

      for(int var4 = 0; var2 != null && var4 < var2.length; ++var4) {
         if (var2[var4].getAdminObjectInterface().equals(var1.getAdminObjectInterface())) {
            var3 = var2[var4];
         }
      }

      return var3;
   }

   private weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean getConnectionDefnRef(ConnectionDefinitionBean var1, ConnectorBean var2, WeblogicConnectorBean var3) {
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var4 = null;
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean[] var5 = null;
      if (var1 == null) {
         Debug.println(this, ".getConnectionDefnRef(): argument ConnectorDefinitionBean (connDefn) is null; returning null ConnectionDefinitionBean");
         return null;
      } else if (var2 == null) {
         Debug.println(this, ".getConnectionDefnRef(): argument ConnectorBean (connBean) == null; returning null ConnectionDefinitionBean");
         return null;
      } else if (var3 == null) {
         Debug.println(this, ".getConnectionDefnRef(): argument WeblogicConnectorBean (wlConnBean) == null; returning null ConnectionDefinitionBean");
         return null;
      } else {
         weblogic.j2ee.descriptor.wl.OutboundResourceAdapterBean var7 = this.getWeblogicOutboundRABean();
         if (var7 == null) {
            Debug.println(this, ".getConnectionDefnRef(): getWeblogicOutboundRABean() is null; returning null ConnectionDefinitionBean");
            return null;
         } else {
            var5 = var7.getConnectionDefinitionGroups();
            if (this.isPreDiabloRA()) {
               if (var5 == null || var5.length == 1) {
                  Debug.println(this, ".getConnectionDefnRef() found " + (var5 == null ? 0 : var5.length) + " connection factory(ies).  There should be exactly 1.  Returning the first one or null.");
                  return var5 == null ? null : var5[0];
               }
            } else {
               String var8 = var1.getConnectionFactoryInterface();
               Debug.println(this, ".getConnectionDefnRef() looking for a WL conn defn with connection factory interface = '" + var8 + "'");
               if (var8 == null || var8.trim().equals("")) {
                  Debug.println(this, ".getConnectionDefnRef() found an null or empty string as the ConnectionFactoryInteface on the connection definition.  Returning a null ConnectionDefinitionBean");
                  return null;
               }

               for(int var9 = 0; var5 != null && var9 < var5.length; ++var9) {
                  String var6 = var5[var9].getConnectionFactoryInterface();
                  if (var6.equals(var8)) {
                     Debug.println(this, ".getConnectionDefnRef() found a WL conn defn with connection factory interface = '" + var8 + "'");
                     var4 = var5[var9];
                     break;
                  }

                  Debug.println(this, ".getConnectionDefnRef() looking for a WL conn defn with connection factory interface = '" + var8 + "' but found one with '" + var6 + "' instead. " + (var9 + 1 < var5.length ? "Still looking." : "Done looking."));
               }
            }

            return var4;
         }
      }
   }

   private String getDefaultNativeLibDir() {
      return DEFAULT_NATIVE_LIBDIR;
   }

   public ConnectorBean getConnectorBean() {
      return this.connBean;
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

   private weblogic.j2ee.descriptor.wl.OutboundResourceAdapterBean getWeblogicOutboundRABean() {
      if (this.wlOutboundRABean == null) {
         this.wlOutboundRABean = this.wlConnBean.getOutboundResourceAdapter();
         if (this.wlOutboundRABean == null) {
            this.wlOutboundRABean = this.wlConnBean.createOutboundResourceAdapter();
         }
      }

      return this.wlOutboundRABean;
   }

   private InboundResourceAdapterBean getInboundRABean() {
      Debug.println(this, ".getInboundRABean()");
      if (this.inboundRABean == null) {
         this.inboundRABean = this.getRABean().getInboundResourceAdapter();
      }

      Debug.println(this, ".getInboundRABean() returning " + (this.inboundRABean != null ? "non-null" : "null"));
      return this.inboundRABean;
   }

   private MessageListenerBean[] getMessageListeners() throws ElementNotFoundException {
      Debug.enter(this, "getMessageListeners()");

      MessageListenerBean[] var8;
      try {
         if (this.msgListeners == null) {
            Debug.println(this, ".getMessageListeners() computing/caching MessageListeners");
            InboundResourceAdapterBean var1 = this.getInboundRABean();
            if (var1 == null) {
               Debug.println(this, ".getMessageListeners() found no InboundResourceAdapter");
               String var9 = Debug.getExceptionNoInboundRAElement();
               throw new ElementNotFoundException(var9);
            }

            MessageAdapterBean var2 = var1.getMessageAdapter();
            String var3;
            if (var2 == null) {
               Debug.println(this, ".getMessageListeners() found no MessageAdapter");
               var3 = Debug.getExceptionNoMessageAdapterElement();
               throw new ElementNotFoundException(var3);
            }

            this.msgListeners = var2.getMessageListeners();
            if (this.msgListeners == null) {
               Debug.println(this, ".getMessageListeners() found no message listeners");
               var3 = Debug.getExceptionNoMessageListenerElement();
               throw new IllegalStateException(var3);
            }
         }

         var8 = this.msgListeners;
      } finally {
         Debug.println(this, ".getMessageListeners() returning " + (this.msgListeners != null ? this.msgListeners.length : 0) + " listeners.");
      }

      return var8;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String[] getRADescriptions() {
      return this.connBean.getDescriptions();
   }

   public Hashtable getConnectionGroupConfigProperties(String var1) {
      Hashtable var2 = this.getRAConnectionDefinitionProperties(var1);
      Hashtable var3 = this.getWLDefaultOutboundProperties();
      Hashtable var4 = this.getWLConnectionDefinitionProperties(var1);
      Hashtable var5 = this.getOverriddenConfigProperties(var2, var3, var4);
      return var5;
   }

   public String getConnectionGroupTransactionSupport(String var1) {
      String var2 = null;
      String var3 = this.getOutboundRABean() != null ? this.getOutboundRABean().getTransactionSupport() : null;
      String var4 = this.getWLDefaultTransactionSupport();
      String var5 = this.getWLGroupTransactionSupport(var1);
      if (var5 != null && var5.length() > 0) {
         var2 = var5;
      } else if (var4 != null && var4.length() > 0) {
         var2 = var4;
      } else {
         var2 = var3;
      }

      return var2;
   }

   public String getConnectionGroupResAuth(String var1) {
      String var2 = null;
      String var3 = this.getWLDefaultResAuth();
      String var4 = this.getWLGroupResAuth(var1);
      if (var4 != null && var4.length() > 0) {
         var2 = var4;
      } else if (var3 != null && var3.length() > 0) {
         var2 = var3;
      }

      return var2;
   }

   public boolean isConnectionGroupReauthenticationSupport(String var1) {
      boolean var3 = this.getOutboundRABean() != null ? this.getOutboundRABean().isReauthenticationSupport() : false;
      String var4 = this.getWLDefaultReauthenticationSupport();
      String var5 = this.getWLGroupReauthenticationSupport(var1);
      boolean var2;
      if (var5 != null && var5.length() > 0) {
         var2 = Boolean.valueOf(var5);
      } else if (var4 != null && var4.length() > 0) {
         var2 = Boolean.valueOf(var4);
      } else {
         var2 = var3;
      }

      return var2;
   }

   public AuthMechInfo[] getConnectionGroupAuthenticationMechanisms(String var1) {
      AuthMechInfo[] var2 = new AuthMechInfo[0];
      AuthMechInfo[] var3 = this.getRAAuthenticationMechanisms();
      AuthMechInfo[] var4 = this.getWLDefaultAuthenticationMechanisms();
      AuthMechInfo[] var5 = this.getWLGroupAuthenticationMechanisms(var1);
      if (var5 != null && var5.length > 0) {
         var2 = var5;
      } else if (var4 != null && var4.length > 0) {
         var2 = var4;
      } else {
         var2 = var3;
      }

      return var2;
   }

   public LoggingInfo getConnectionGroupLoggingProperties(String var1) {
      ConnectionDefinitionPropertiesBean var2 = this.getWeblogicOutboundRABean().getDefaultConnectionProperties();
      if (var2 == null) {
         var2 = this.getWeblogicOutboundRABean().createDefaultConnectionProperties();
      }

      if (var2.getLogging() == null) {
         var2.createLogging();
      }

      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var3 = this.getWLConnectionDefinition(var1);
      ConnectionDefinitionPropertiesBean var4 = null;
      if (var3 != null) {
         var4 = var3.getDefaultConnectionProperties();
      }

      String var5 = var2.getLogging().getLogFilename();
      boolean var6 = var2.getLogging().isLoggingEnabled();
      String var7 = var2.getLogging().getRotationType();
      String var8 = var2.getLogging().getRotationTime();
      boolean var9 = var2.getLogging().isNumberOfFilesLimited();
      int var10 = var2.getLogging().getFileCount();
      int var11 = var2.getLogging().getFileSizeLimit();
      int var12 = var2.getLogging().getFileTimeSpan();
      boolean var13 = var2.getLogging().isRotateLogOnStartup();
      String var14 = var2.getLogging().getLogFileRotationDir();
      if (var4 != null && var4.getLogging() != null) {
         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("LogFilename")) {
            var5 = var4.getLogging().getLogFilename();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("LoggingEnabled")) {
            var6 = var4.getLogging().isLoggingEnabled();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("RotationType")) {
            var7 = var4.getLogging().getRotationType();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("RotationTime")) {
            var8 = var4.getLogging().getRotationTime();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("NumberOfFilesLimited")) {
            var9 = var4.getLogging().isNumberOfFilesLimited();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("FileCount")) {
            var10 = var4.getLogging().getFileCount();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("FileSizeLimit")) {
            var11 = var4.getLogging().getFileSizeLimit();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("FileTimeSpan")) {
            var12 = var4.getLogging().getFileTimeSpan();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("RotateLogOnStartup")) {
            var13 = var4.getLogging().isRotateLogOnStartup();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getLogging())).isSet("LogFileRotationDir")) {
            var14 = var4.getLogging().getLogFileRotationDir();
         }
      }

      return new LoggingInfoImpl(var5, var6, var7, var8, var9, var10, var11, var12, var13, var14);
   }

   public PoolInfo getConnectionGroupPoolProperties(String var1) {
      ConnectionDefinitionPropertiesBean var2 = null;
      var2 = this.getWeblogicOutboundRABean().getDefaultConnectionProperties();
      if (var2 == null) {
         var2 = this.getWeblogicOutboundRABean().createDefaultConnectionProperties();
      }

      if (var2.getPoolParams() == null) {
         var2.createPoolParams();
      }

      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var3 = this.getWLConnectionDefinition(var1);
      ConnectionDefinitionPropertiesBean var4 = null;
      if (var3 != null) {
         var4 = var3.getDefaultConnectionProperties();
      }

      int var5 = var2.getPoolParams().getInitialCapacity();
      int var6 = var2.getPoolParams().getMaxCapacity();
      int var7 = var2.getPoolParams().getCapacityIncrement();
      boolean var8 = var2.getPoolParams().isShrinkingEnabled();
      int var9 = var2.getPoolParams().getShrinkFrequencySeconds();
      int var10 = var2.getPoolParams().getHighestNumWaiters();
      int var11 = var2.getPoolParams().getHighestNumUnavailable();
      int var12 = var2.getPoolParams().getConnectionCreationRetryFrequencySeconds();
      int var13 = var2.getPoolParams().getConnectionReserveTimeoutSeconds();
      int var14 = var2.getPoolParams().getTestFrequencySeconds();
      boolean var15 = var2.getPoolParams().isTestConnectionsOnCreate();
      boolean var16 = var2.getPoolParams().isTestConnectionsOnRelease();
      boolean var17 = var2.getPoolParams().isTestConnectionsOnReserve();
      int var18 = var2.getPoolParams().getProfileHarvestFrequencySeconds();
      boolean var19 = var2.getPoolParams().isIgnoreInUseConnectionsEnabled();
      boolean var20 = var2.getPoolParams().isMatchConnectionsSupported();
      if (var4 != null && var4.getPoolParams() != null) {
         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("InitialCapacity")) {
            var5 = var4.getPoolParams().getInitialCapacity();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("MaxCapacity")) {
            var6 = var4.getPoolParams().getMaxCapacity();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("CapacityIncrement")) {
            var7 = var4.getPoolParams().getCapacityIncrement();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("ShrinkingEnabled")) {
            var8 = var4.getPoolParams().isShrinkingEnabled();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("ShrinkFrequencySeconds")) {
            var9 = var4.getPoolParams().getShrinkFrequencySeconds();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("HighestNumWaiters")) {
            var10 = var4.getPoolParams().getHighestNumWaiters();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("HighestNumUnavailable")) {
            var11 = var4.getPoolParams().getHighestNumUnavailable();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("ConnectionCreationRetryFrequencySeconds")) {
            var12 = var4.getPoolParams().getConnectionCreationRetryFrequencySeconds();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("ConnectionReserveTimeoutSeconds")) {
            var13 = var4.getPoolParams().getConnectionReserveTimeoutSeconds();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("TestFrequencySeconds")) {
            var14 = var4.getPoolParams().getTestFrequencySeconds();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("TestConnectionsOnCreate")) {
            var15 = var4.getPoolParams().isTestConnectionsOnCreate();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("TestConnectionsOnRelease")) {
            var16 = var4.getPoolParams().isTestConnectionsOnRelease();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("TestConnectionsOnReserve")) {
            var17 = var4.getPoolParams().isTestConnectionsOnReserve();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("ProfileHarvestFrequencySeconds")) {
            var18 = var4.getPoolParams().getProfileHarvestFrequencySeconds();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("IgnoreInUseConnectionsEnabled")) {
            var19 = var4.getPoolParams().isIgnoreInUseConnectionsEnabled();
         }

         if (((DescriptorBean)((DescriptorBean)var4.getPoolParams())).isSet("MatchConnectionsSupported")) {
            var20 = var4.getPoolParams().isMatchConnectionsSupported();
         }
      }

      return new PoolInfoImpl(var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19, var20);
   }

   public Hashtable getAdminObjectGroupProperties(String var1) {
      Hashtable var2 = this.getRAAdminProperties(var1);
      if (var2.size() == 0) {
         return var2;
      } else {
         Hashtable var3 = this.getGlobalAdminProperties();
         Hashtable var4 = this.getGroupAdminProperties(var1);
         return this.getOverriddenConfigProperties(var2, var3, var4);
      }
   }

   public AdminObjInfo getAdminObject(String var1) {
      AdminObjectBean[] var2 = this.getRABean().getAdminObjects();
      AdminObjectsBean var3 = this.wlConnBean.getAdminObjects();
      AdminObjInfoImpl var4 = null;
      if (var3 != null) {
         AdminObjectGroupBean[] var6 = var3.getAdminObjectGroups();

         for(int var8 = 0; var8 < var2.length; ++var8) {
            AdminObjectGroupBean var5 = this.getAdminObjectGroup(var2[var8], var6);
            if (var5 != null && var5.getAdminObjectInstances() != null && var5.getAdminObjectInstances().length != 0) {
               AdminObjectInstanceBean[] var7 = var5.getAdminObjectInstances();

               for(int var9 = 0; var9 < var7.length; ++var9) {
                  String var10 = var7[var9].getJNDIName();
                  if (var10 != null && var10.equals(var1)) {
                     var4 = new AdminObjInfoImpl(var2[var8], var7[var9], this.wlConnBean, var5);
                     break;
                  }
               }
            }
         }
      }

      return var4;
   }

   private String getWLDefaultTransactionSupport() {
      String var1 = null;
      ConnectionDefinitionPropertiesBean var2 = this.getWeblogicOutboundRABean().getDefaultConnectionProperties();
      if (var2 != null) {
         var1 = var2.getTransactionSupport();
      }

      return var1;
   }

   private String getWLDefaultResAuth() {
      String var1 = null;
      ConnectionDefinitionPropertiesBean var2 = this.getWeblogicOutboundRABean().getDefaultConnectionProperties();
      if (var2 != null) {
         var1 = var2.getResAuth();
      }

      return var1;
   }

   private String getWLDefaultReauthenticationSupport() {
      String var1 = null;
      ConnectionDefinitionPropertiesBean var2 = this.getWeblogicOutboundRABean().getDefaultConnectionProperties();
      if (var2 != null && ((DescriptorBean)var2).isSet("ReauthenticationSupport")) {
         var1 = String.valueOf(var2.isReauthenticationSupport());
      }

      return var1;
   }

   private String getWLGroupTransactionSupport(String var1) {
      String var2 = null;
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var3 = this.getWLConnectionDefinition(var1);
      if (var3 != null) {
         ConnectionDefinitionPropertiesBean var4 = var3.getDefaultConnectionProperties();
         if (var4 != null) {
            var2 = var4.getTransactionSupport();
         }
      }

      return var2;
   }

   private String getWLGroupResAuth(String var1) {
      String var2 = null;
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var3 = this.getWLConnectionDefinition(var1);
      if (var3 != null) {
         ConnectionDefinitionPropertiesBean var4 = var3.getDefaultConnectionProperties();
         if (var4 != null) {
            var2 = var4.getResAuth();
         }
      }

      return var2;
   }

   private String getWLGroupReauthenticationSupport(String var1) {
      String var2 = null;
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var3 = this.getWLConnectionDefinition(var1);
      if (var3 != null) {
         ConnectionDefinitionPropertiesBean var4 = var3.getDefaultConnectionProperties();
         if (var4 != null && ((DescriptorBean)var4).isSet("ReauthenticationSupport")) {
            var2 = String.valueOf(var4.isReauthenticationSupport());
         }
      }

      return var2;
   }

   private AuthMechInfo[] getRAAuthenticationMechanisms() {
      AuthMechInfo[] var1 = new AuthMechInfo[0];
      if (this.getOutboundRABean() != null) {
         AuthenticationMechanismBean[] var2 = this.getOutboundRABean().getAuthenticationMechanisms();
         var1 = new AuthMechInfo[var2.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            var1[var3] = new AuthMechInfoImpl(var2[var3]);
         }
      }

      return var1;
   }

   private AuthMechInfo[] getWLDefaultAuthenticationMechanisms() {
      AuthMechInfo[] var1 = new AuthMechInfo[0];
      ConnectionDefinitionPropertiesBean var2 = this.getWeblogicOutboundRABean().getDefaultConnectionProperties();
      if (var2 != null) {
         AuthenticationMechanismBean[] var3 = var2.getAuthenticationMechanisms();
         var1 = new AuthMechInfo[var3.length];

         for(int var4 = 0; var4 < var3.length; ++var4) {
            var1[var4] = new AuthMechInfoImpl(var3[var4]);
         }
      }

      return var1;
   }

   private AuthMechInfo[] getWLGroupAuthenticationMechanisms(String var1) {
      AuthMechInfo[] var2 = new AuthMechInfo[0];
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var3 = this.getWLConnectionDefinition(var1);
      if (var3 != null) {
         ConnectionDefinitionPropertiesBean var4 = var3.getDefaultConnectionProperties();
         if (var4 != null) {
            AuthenticationMechanismBean[] var5 = var4.getAuthenticationMechanisms();
            var2 = new AuthMechInfo[var5.length];

            for(int var6 = 0; var6 < var5.length; ++var6) {
               var2[var6] = new AuthMechInfoImpl(var5[var6]);
            }
         }
      }

      return var2;
   }

   private Hashtable getOverriddenConfigProperties(Hashtable var1, Hashtable var2, Hashtable var3) {
      Hashtable var4 = new Hashtable();
      Iterator var5 = var1.values().iterator();

      while(var5.hasNext()) {
         weblogic.j2ee.descriptor.ConfigPropertyBean var6 = (weblogic.j2ee.descriptor.ConfigPropertyBean)var5.next();
         if (var6 != null && var6.getConfigPropertyName() != null && var6.getConfigPropertyName().length() > 0) {
            ConfigPropertyBean var7 = (ConfigPropertyBean)var2.get(var6.getConfigPropertyName());
            ConfigPropertyBean var8 = (ConfigPropertyBean)var3.get(var6.getConfigPropertyName());
            ConfigPropInfoImpl var9;
            if (var8 != null) {
               var9 = new ConfigPropInfoImpl(var6, var8.getValue());
               var4.put(var8.getName(), var9);
            } else if (var7 != null) {
               var9 = new ConfigPropInfoImpl(var6, var7.getValue());
               var4.put(var7.getName(), var9);
            } else {
               var9 = new ConfigPropInfoImpl(var6, var6.getConfigPropertyValue());
               var4.put(var6.getConfigPropertyName(), var9);
            }
         }
      }

      return var4;
   }

   private ConnectionDefinitionBean getRAConnectionDefinition(String var1) {
      ConnectionDefinitionBean var2 = null;
      if (this.getOutboundRABean() != null) {
         ConnectionDefinitionBean[] var3 = this.getOutboundRABean().getConnectionDefinitions();

         for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
            if (var3[var4].getConnectionFactoryInterface().equals(var1)) {
               var2 = var3[var4];
               break;
            }
         }
      }

      return var2;
   }

   private weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean getWLConnectionDefinition(String var1) {
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var2 = null;
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean[] var3 = this.getWeblogicOutboundRABean().getConnectionDefinitionGroups();

      for(int var4 = 0; var3 != null && var4 < var3.length; ++var4) {
         if (var3[var4].getConnectionFactoryInterface().equals(var1)) {
            var2 = var3[var4];
            break;
         }
      }

      return var2;
   }

   private Hashtable getRAConnectionDefinitionProperties(String var1) {
      Hashtable var2 = new Hashtable();
      ConnectionDefinitionBean var3 = null;
      var3 = this.getRAConnectionDefinition(var1);
      if (var3 != null) {
         weblogic.j2ee.descriptor.ConfigPropertyBean[] var4 = var3.getConfigProperties();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getConfigPropertyName() != null && var4[var5].getConfigPropertyName().length() > 0) {
               var2.put(var4[var5].getConfigPropertyName(), var4[var5]);
            }
         }
      }

      return var2;
   }

   private Hashtable getWLDefaultOutboundProperties() {
      Hashtable var1 = new Hashtable();
      ConnectionDefinitionPropertiesBean var2 = this.getWeblogicOutboundRABean().getDefaultConnectionProperties();
      if (var2 != null && var2.getProperties() != null && var2.getProperties().getProperties() != null) {
         ConfigPropertyBean[] var3 = var2.getProperties().getProperties();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].getName() != null && var3[var4].getName().length() > 0) {
               var1.put(var3[var4].getName(), var3[var4]);
            }
         }
      }

      return var1;
   }

   private Hashtable getWLConnectionDefinitionProperties(String var1) {
      Hashtable var2 = new Hashtable();
      weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var3 = null;
      var3 = this.getWLConnectionDefinition(var1);
      if (var3 != null) {
         ConnectionDefinitionPropertiesBean var4 = var3.getDefaultConnectionProperties();
         if (var4 != null && var4.getProperties() != null && var4.getProperties().getProperties() != null) {
            ConfigPropertyBean[] var5 = var4.getProperties().getProperties();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               if (var5[var6].getName() != null && var5[var6].getName().length() > 0) {
                  var2.put(var5[var6].getName(), var5[var6]);
               }
            }
         }
      }

      return var2;
   }

   private Hashtable getGroupAdminProperties(String var1) {
      Hashtable var2 = new Hashtable();
      if (var1 != null && var1.trim().length() > 0 && this.wlConnBean.getAdminObjects() != null && this.wlConnBean.getAdminObjects().getAdminObjectGroups() != null) {
         AdminObjectGroupBean[] var3 = this.wlConnBean.getAdminObjects().getAdminObjectGroups();
         AdminObjectGroupBean var4 = null;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            var4 = var3[var5];
            if (var4 != null && var1.equals(var4.getAdminObjectInterface()) && var4.getDefaultProperties() != null && var4.getDefaultProperties().getProperties() != null) {
               ConfigPropertyBean[] var6 = var4.getDefaultProperties().getProperties();
               ConfigPropertyBean var7 = null;

               for(int var8 = 0; var8 < var6.length; ++var8) {
                  var7 = var6[var8];
                  if (var7 != null) {
                     var2.put(var7.getName(), var7);
                  }
               }
            }
         }
      }

      return var2;
   }

   private Hashtable getGlobalAdminProperties() {
      Hashtable var1 = new Hashtable();
      if (this.wlConnBean.getAdminObjects() != null && this.wlConnBean.getAdminObjects().getDefaultProperties() != null && this.wlConnBean.getAdminObjects().getDefaultProperties().getProperties() != null) {
         ConfigPropertyBean[] var2 = this.wlConnBean.getAdminObjects().getDefaultProperties().getProperties();
         ConfigPropertyBean var3 = null;

         for(int var4 = 0; var4 < var2.length; ++var4) {
            var3 = var2[var4];
            if (var3 != null) {
               var1.put(var3.getName(), var3);
            }
         }
      }

      return var1;
   }

   private Hashtable getRAAdminProperties(String var1) {
      weblogic.j2ee.descriptor.ConfigPropertyBean[] var2 = null;
      Hashtable var3 = new Hashtable();
      if (var1 != null && var1.trim().length() > 0 && this.connBean.getResourceAdapter() != null && this.connBean.getResourceAdapter().getAdminObjects() != null) {
         AdminObjectBean[] var4 = this.connBean.getResourceAdapter().getAdminObjects();
         AdminObjectBean var5 = null;

         for(int var6 = 0; var6 < var4.length; ++var6) {
            var5 = var4[var6];
            if (var5 != null && var1.equals(var5.getAdminObjectInterface()) && var5.getConfigProperties() != null) {
               var2 = var5.getConfigProperties();

               for(int var8 = 0; var8 < var2.length; ++var8) {
                  weblogic.j2ee.descriptor.ConfigPropertyBean var7 = var2[var8];
                  var3.put(var7.getConfigPropertyName(), var7);
               }
            }
         }
      }

      return var3;
   }

   public void copyBaseRA(RAInfoImpl var1) {
      this.baseRA = var1.baseRA;
   }
}
