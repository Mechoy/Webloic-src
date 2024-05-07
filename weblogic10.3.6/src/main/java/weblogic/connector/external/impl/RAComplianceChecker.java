package weblogic.connector.external.impl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import weblogic.connector.common.Debug;
import weblogic.connector.common.Utils;
import weblogic.connector.compliance.RAComplianceTextFormatter;
import weblogic.connector.configuration.DDUtil;
import weblogic.connector.deploy.DeployerUtil;
import weblogic.connector.exception.RACommonException;
import weblogic.connector.exception.RAConfigurationException;
import weblogic.connector.exception.WLRAConfigurationException;
import weblogic.connector.external.PropSetterTable;
import weblogic.connector.external.RAComplianceException;
import weblogic.connector.external.RAInfo;
import weblogic.connector.external.RAValidationInfo;
import weblogic.descriptor.DescriptorException;
import weblogic.j2ee.descriptor.AdminObjectBean;
import weblogic.j2ee.descriptor.ConfigPropertyBean;
import weblogic.j2ee.descriptor.ConnectionDefinitionBean;
import weblogic.j2ee.descriptor.ConnectorBean;
import weblogic.j2ee.descriptor.IconBean;
import weblogic.j2ee.descriptor.InboundResourceAdapterBean;
import weblogic.j2ee.descriptor.MessageAdapterBean;
import weblogic.j2ee.descriptor.MessageListenerBean;
import weblogic.j2ee.descriptor.OutboundResourceAdapterBean;
import weblogic.j2ee.descriptor.ResourceAdapterBean;
import weblogic.j2ee.descriptor.wl.AdminObjectGroupBean;
import weblogic.j2ee.descriptor.wl.AdminObjectInstanceBean;
import weblogic.j2ee.descriptor.wl.AdminObjectsBean;
import weblogic.j2ee.descriptor.wl.ConfigPropertiesBean;
import weblogic.j2ee.descriptor.wl.ConnectionDefinitionPropertiesBean;
import weblogic.j2ee.descriptor.wl.ConnectionInstanceBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicConnectorBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public final class RAComplianceChecker implements weblogic.connector.external.RAComplianceChecker {
   private static RAComplianceTextFormatter fmt = new RAComplianceTextFormatter();
   private static Class JAVA_LANG_OBJECT_CLASS = (new Object()).getClass();
   private static final Class[] PRIMITIVE_CLASSES;
   private static final String[] PRIMITIVE_TYPES;
   private static final String[] PRIMITIVE_OBJ_CLASSES;
   private static final int NUM_PRIMS;
   private static final int NUM_OBJ_PRIMS;
   private List classFinders = new Vector(1);
   private Vector raErrors = new Vector();
   private Vector raWarnings = new Vector();
   private RAValidationInfo raValidationInfo = new RAValidationInfoImpl();
   private GenericClassLoader raClassLoader = null;
   private ConnectorBean raConnectorBean = null;
   private WeblogicConnectorBean wlraConnectorBean = null;
   public static final weblogic.connector.external.RAComplianceChecker factoryHelper;
   private Set<Class> introspectedClasses = new HashSet();
   private boolean clearIntrospectorCacheNeeded = false;

   private RAComplianceChecker() {
   }

   public weblogic.connector.external.RAComplianceChecker createChecker() {
      return new RAComplianceChecker();
   }

   public boolean validate(GenericClassLoader var1, VirtualJarFile var2, File var3, File var4, DeploymentPlanBean var5) throws RAComplianceException {
      boolean var6 = false;

      try {
         String var7 = DDUtil.getModuleName(var2.getName());
         RAInfo var8 = DDUtil.getRAInfo(var2, var3, var7, (AppDeploymentMBean)null, var5);
         DeployerUtil.updateClassFinder(var1, var2, false, this.classFinders);
         this.setClearIntrospectorCacheNeeded();
         this.raValidationInfo = this.validate(var2.getName(), var8, var1);
         var6 = this.raValidationInfo.isCompliant();
      } catch (WLRAConfigurationException var14) {
         Debug.logComplianceWLRAConfigurationException(var14.getMessage());
      } catch (RAConfigurationException var15) {
         this.handleRAException(var15);
      } finally {
         this.clearIntrospectorCache();
      }

      return var6;
   }

   public RAValidationInfo validate(String var1, RAInfo var2, GenericClassLoader var3) throws RAComplianceException {
      ((RAValidationInfoImpl)this.raValidationInfo).linkRef = var2.getLinkref();
      ((RAValidationInfoImpl)this.raValidationInfo).isLinkRef = this.raValidationInfo.getLinkRef() != null;
      this.raConnectorBean = var2.getConnectorBean();
      this.wlraConnectorBean = var2.getWeblogicConnectorBean();
      this.raClassLoader = var3;
      this.validate(var1);
      return this.raValidationInfo;
   }

   private void validate(String var1) throws RAComplianceException {
      RAComplianceException var2 = new RAComplianceException();
      ((RAValidationInfoImpl)this.raValidationInfo).moduleName = var1;
      this.validateRADD();
      this.validateWLRADD();
      int var3 = this.raErrors.size();
      int var4 = this.raWarnings.size();
      if (var3 == 0 && var4 == 0) {
         Debug.logNoComplianceErrors(var1);
      } else {
         Debug.logNumComplianceErrorsAndWarnings(var1, var3, var4);
      }

      Iterator var5;
      if (var3 > 0) {
         var5 = this.raErrors.iterator();

         while(var5.hasNext()) {
            var2.addMessage((String)var5.next());
         }
      }

      if (var4 > 0) {
         var5 = this.raWarnings.iterator();
         RAComplianceException var6 = new RAComplianceException();

         while(var5.hasNext()) {
            var6.addMessage((String)var5.next());
         }

         Debug.logComplianceWarnings(var1, var6.getMessage());
      }

      if (var3 > 0) {
         ((RAValidationInfoImpl)this.raValidationInfo).isCompliant = false;
         throw var2;
      } else {
         ((RAValidationInfoImpl)this.raValidationInfo).isCompliant = var3 == 0;
      }
   }

   private RAValidationInfo validateRADD() {
      if (this.raConnectorBean != null) {
         ((RAValidationInfoImpl)this.raValidationInfo).hasRAxml = !this.raValidationInfo.isLinkRef();
         this.validateIconFiles();
         this.validateResourceAdapter();
      } else if (this.raValidationInfo.isLinkRef()) {
         Debug.logComplianceIsLinkRef(this.raValidationInfo.getLinkRef());
      } else if (!this.raValidationInfo.isLinkRef() && this.raConnectorBean == null) {
         this.raErrors.addElement(fmt.MISSING_RA_XML());
      }

      return this.raValidationInfo;
   }

   private void validateIconFiles() {
      IconBean[] var1 = this.raConnectorBean.getIcons();
      if (var1 != null && var1.length > 0) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            String var3 = var1[var2].getLargeIcon();
            String var4 = var1[var2].getSmallIcon();
            boolean var5;
            if (var4 != null) {
               var5 = this.raClassLoader.getResource(var4) != null;
               if (!var5) {
                  this.raWarnings.addElement(fmt.FILE_NOT_FOUND("ra.xml", "<small-icon>", var4));
               }
            }

            if (var3 != null) {
               var5 = this.raClassLoader.getResource(var3) != null;
               if (!var5) {
                  this.raWarnings.addElement(fmt.FILE_NOT_FOUND("ra.xml", "<large-icon>", var3));
               }
            }
         }
      }

   }

   private void validateResourceAdapter() {
      ResourceAdapterBean var1 = this.raConnectorBean.getResourceAdapter();
      Class var2 = null;
      if (var1 != null) {
         OutboundResourceAdapterBean var3 = var1.getOutboundResourceAdapter();
         AdminObjectBean[] var4 = var1.getAdminObjects();
         var2 = this.validateResourceAdapterBean();
         this.validateAdminObjects(var4);
         this.validateOutboundResourceAdapter(var3);
         this.validateMessageListeners();
         if (this.raValidationInfo.isInbound() && !this.raValidationInfo.hasRAbean()) {
            this.raWarnings.addElement(fmt.MISSING_RA_BEAN_FOR_INBOUND());
         }

      }
   }

   private void validateWLRADD() {
      AdminObjectsBean var1 = this.wlraConnectorBean.getAdminObjects();
      String var2 = this.wlraConnectorBean.getJNDIName();
      weblogic.j2ee.descriptor.wl.OutboundResourceAdapterBean var3 = this.wlraConnectorBean.getOutboundResourceAdapter();
      if (this.raValidationInfo.isInbound() && this.raValidationInfo.hasRAbean() && var2 == null) {
         this.raWarnings.addElement(fmt.MISSING_JNDI_NAME());
      }

      if (var2 != null && !this.raValidationInfo.hasRAbean()) {
         this.raErrors.addElement(fmt.MISSING_RA_BEAN());
      }

      this.validateWLProps("<weblogic-connector><properties>", this.wlraConnectorBean.getProperties(), this.raValidationInfo.getRAPropSetterTable());
      this.validateWLAdminObjects(var1);
      this.validateWLOutboundResourceAdapter(var3);
   }

   private void validateWLAdminObjects(AdminObjectsBean var1) {
      if (var1 != null) {
         ConfigPropertiesBean var2 = var1.getDefaultProperties();
         this.validateWLGroupProps("<admin-objects><default-properties>", var2, this.raValidationInfo.getAllAdminPropSetters());
         AdminObjectGroupBean[] var3 = var1.getAdminObjectGroups();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            AdminObjectGroupBean var6 = var3[var5];
            String var7 = var6.getAdminObjectInterface();
            if (!this.hasMatchingAdminInterfaceInRA(var7)) {
               this.raErrors.addElement(fmt.NO_MATCHING_ADMIN_INTERFACE(var7));
            } else {
               ConfigPropertiesBean var8 = var6.getDefaultProperties();
               PropSetterTable var9 = this.raValidationInfo.getAdminPropSetterTable(var7);
               this.validateWLProps("<admin-objects><admin-object-group>[admin-object-interface = " + var7 + "]", var8, var9);
               AdminObjectInstanceBean[] var10 = var6.getAdminObjectInstances();
               int var11 = var10.length;

               for(int var12 = 0; var12 < var11; ++var12) {
                  AdminObjectInstanceBean var13 = var10[var12];
                  ConfigPropertiesBean var14 = var13.getProperties();
                  this.validateWLProps("<admin-objects><admin-object-group>[admin-object-interface = " + var7 + "]<admin-object-instance>[jndi-name = " + var13.getJNDIName() + "]", var14, var9);
               }
            }
         }

      }
   }

   private boolean hasMatchingAdminInterfaceInRA(String var1) {
      boolean var2 = false;
      if (this.raConnectorBean != null && this.raConnectorBean.getResourceAdapter() != null) {
         AdminObjectBean[] var3 = this.raConnectorBean.getResourceAdapter().getAdminObjects();

         for(int var4 = 0; var4 < var3.length && !var2; ++var4) {
            AdminObjectBean var5 = var3[var4];
            String var6 = var5.getAdminObjectInterface();
            var2 = var1.equals(var6);
         }
      }

      return var2;
   }

   private Class validateResourceAdapterBean() {
      String[] var1 = new String[]{"javax.resource.spi.ResourceAdapter"};
      String[] var2 = new String[]{"java.io.Serializable"};
      String var3 = this.raConnectorBean.getResourceAdapter().getResourceAdapterClass();
      Class var4 = null;
      if (var3 == null) {
         return null;
      } else {
         ((RAValidationInfoImpl)this.raValidationInfo).hasRAbean = true;
         var4 = this.checkClass("<resourceadapter-class>", "ra.xml", var3, var1, var2, true, (Boolean)null);
         if (var4 != null) {
            this.validateProperties(var4, this.raConnectorBean.getResourceAdapter().getConfigProperties(), this.raValidationInfo.getRAPropSetterTable(), "<resourceadapter>", "ra.xml");
         }

         return var4;
      }
   }

   private void validateOutboundResourceAdapter(OutboundResourceAdapterBean var1) {
      if (var1 != null) {
         ConnectionDefinitionBean[] var2 = var1.getConnectionDefinitions();
         if (var2 != null && var2.length != 0) {
            for(int var3 = 0; var3 < var2.length; ++var3) {
               ConnectionDefinitionBean var4 = var2[var3];
               this.validateConnectionDefinition(var4);
            }

         }
      }
   }

   private void validateWLOutboundResourceAdapter(weblogic.j2ee.descriptor.wl.OutboundResourceAdapterBean var1) {
      if (var1 != null && (!this.raValidationInfo.isLinkRef() || this.raConnectorBean != null)) {
         ConnectionDefinitionPropertiesBean var2 = var1.getDefaultConnectionProperties();
         ConfigPropertiesBean var3 = null;
         if (var2 != null) {
            var3 = var2.getProperties();
            this.validateWLGroupProps("<outbound-resource-adapter><default-connection-properties><properties>", var3, this.raValidationInfo.getAllConnectionFactoryPropSetters());
         }

         weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean[] var4 = var1.getConnectionDefinitionGroups();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            weblogic.j2ee.descriptor.wl.ConnectionDefinitionBean var6 = var4[var5];
            String var7 = var6.getConnectionFactoryInterface();
            if (!this.hasMatchingConnFactoryInterfaceInRA(var7)) {
               this.raErrors.addElement(fmt.NO_MATCHING_CONN_FACTORY_INTERFACE(var7));
            } else {
               PropSetterTable var8 = this.raValidationInfo.getConnectionFactoryPropSetterTable(var7);
               ConnectionDefinitionPropertiesBean var9 = var6.getDefaultConnectionProperties();
               ConfigPropertiesBean var10 = null;
               if (var9 != null) {
                  var10 = var9.getProperties();
                  this.validateWLProps("<outbound-resource-adapter><connection-definition-group>[connection-factory-interface = " + var7 + "]<default-connection-properties>", var10, var8);
               }

               ConnectionInstanceBean[] var11 = var6.getConnectionInstances();
               int var12 = var11.length;

               for(int var13 = 0; var13 < var12; ++var13) {
                  ConnectionInstanceBean var14 = var11[var13];
                  ConnectionDefinitionPropertiesBean var15 = var14.getConnectionProperties();
                  ConfigPropertiesBean var16 = null;
                  if (var15 != null) {
                     var16 = var15.getProperties();
                     this.validateWLProps("<outbound-resource-adapter><connection-definition-group>[connection-factory-interface = " + var7 + "]<connection-instance>[ jndi-name = " + var14.getJNDIName() + "]", var16, var8);
                  }
               }
            }
         }

      }
   }

   private boolean hasMatchingConnFactoryInterfaceInRA(String var1) {
      boolean var2 = false;
      if (this.raConnectorBean != null && this.raConnectorBean.getResourceAdapter() != null) {
         OutboundResourceAdapterBean var3 = this.raConnectorBean.getResourceAdapter().getOutboundResourceAdapter();
         ConnectionDefinitionBean[] var4 = var3.getConnectionDefinitions();
         if (var4 != null && var4.length > 0) {
            for(int var5 = 0; var5 < var4.length && !var2; ++var5) {
               ConnectionDefinitionBean var6 = var4[var5];
               var2 = var1.equals(var6.getConnectionFactoryInterface());
            }
         }
      }

      return var2;
   }

   private void validateConnectionDefinition(ConnectionDefinitionBean var1) {
      String[] var2 = new String[]{"javax.resource.spi.ManagedConnectionFactory"};
      String[] var3 = new String[]{"java.io.Serializable", "javax.resource.Referenceable", "<connectionfactory-interface>"};
      String[] var4 = new String[]{"<connection-interface>"};
      String var5 = var1.getManagedConnectionFactoryClass();
      String var6 = var1.getConnectionFactoryImplClass();
      String var7 = var1.getConnectionFactoryInterface();
      var3[2] = var7;
      String var8 = var1.getConnectionImplClass();
      String var9 = var1.getConnectionInterface();
      var4[0] = var9;
      ConfigPropertyBean[] var10 = var1.getConfigProperties();
      Class var11 = null;
      this.checkClass("<connection-interface>", "ra.xml", var9, (String[])null, (String[])null, false);
      this.checkClass("<connection-impl-class>", "ra.xml", var8, var4, (String[])null, false);
      var11 = this.checkClass("<managedconnectionfactory-class>", "ra.xml", var5, var2, (String[])null, false, Boolean.TRUE);
      this.checkClass("<connectionfactory-interface>", "ra.xml", var7, (String[])null, (String[])null, false);
      this.checkClass("<connectionfactory-impl-class>", "ra.xml", var6, var3, (String[])null, false);
      this.validateProperties(var11, var10, this.raValidationInfo.getConnectionFactoryPropSetterTable(var7), "<connection-definition>", "ra.xml");
   }

   private void validateMessageListeners() {
      if (this.raConnectorBean == null) {
         this.raErrors.addElement(fmt.ELEMENT_IS_EMPTY("ra.xml", "<connector>"));
      } else {
         InboundResourceAdapterBean var1 = this.raConnectorBean.getResourceAdapter().getInboundResourceAdapter();
         if (var1 != null) {
            MessageAdapterBean var2 = var1.getMessageAdapter();
            if (var2 != null) {
               MessageListenerBean[] var3 = var2.getMessageListeners();
               if (var3 != null && var3.length != 0) {
                  ((RAValidationInfoImpl)this.raValidationInfo).isInbound = true;

                  for(int var4 = 0; var4 < var3.length; ++var4) {
                     this.validateMessageListener(var3[var4]);
                  }

               }
            }
         }
      }
   }

   private void validateMessageListener(MessageListenerBean var1) {
      String[] var2 = new String[]{"javax.resource.spi.ActivationSpec", "java.io.Serializable"};
      if (var1 == null) {
         this.raErrors.addElement(fmt.ELEMENT_IS_EMPTY("ra.xml", "<messagelistener-type>"));
      } else {
         String var3 = var1.getMessageListenerType();
         String var4 = var1.getActivationSpec().getActivationSpecClass();
         this.checkClass("<messagelistener-type>", "ra.xml", var3, (String[])null, (String[])null, false);
         this.checkClass("<activationspec-class>", "ra.xml", var4, var2, (String[])null, true, Boolean.FALSE);
      }
   }

   private void validateAdminObjects(AdminObjectBean[] var1) {
      String[] var2 = new String[]{"<adminobject-interface>"};
      String[] var3 = new String[]{"java.io.Serializable"};
      Class var4 = null;
      if (var1 != null && var1.length > 0) {
         for(int var5 = 0; var5 < var1.length; ++var5) {
            AdminObjectBean var6 = var1[var5];
            String var7 = var6.getAdminObjectInterface();
            this.checkClass("<adminobject-interface>", "ra.xml", var7, (String[])null, (String[])null, false);
            var2[0] = var7;
            String var8 = var6.getAdminObjectClass();
            var4 = this.checkClass("<adminobject-class>", "ra.xml", var8, var2, var3, true);
            if (var4 != null) {
               this.validateProperties(var4, var6.getConfigProperties(), this.raValidationInfo.getAdminPropSetterTable(var7), "<adminobject>", "ra.xml");
            }
         }
      }

   }

   private Class checkClass(String var1, String var2, String var3, String[] var4, String[] var5, boolean var6, Boolean var7) {
      boolean var8 = true;
      Class var9 = null;
      if (var3 != null && var3.length() != 0) {
         try {
            var9 = Class.forName(var3, false, this.raClassLoader);
         } catch (ClassNotFoundException var12) {
            this.raErrors.addElement(fmt.CLASS_NOT_FOUND(var2, var1, var3));
            return var9;
         } catch (Throwable var13) {
            this.raErrors.addElement(fmt.COULDNT_LOAD_CLASS(var2, var1, var3, var13.toString()));
            return var9;
         }

         Class[] var10 = Utils.getInterfaces(var9);
         if (var4 != null) {
            this.validateImplements(var2, true, var1, var3, var10, var4);
         }

         if (var5 != null) {
            this.validateImplements(var2, false, var1, var3, var10, var5);
         }

         if (var6) {
            this.checkJavaBeanCompliance(var2, var1, var9);
         }

         if (var7 != null) {
            boolean var11 = var7;
            var8 = checkOverrides(var9, var11);
            if (!var8) {
               if (var11) {
                  this.raErrors.addElement(fmt.MUST_OVERRIDE(var2, var1, var3));
               } else {
                  this.raWarnings.addElement(fmt.SHOULD_NOT_OVERRIDE(var2, var1, var3));
               }
            }
         }

         return var9;
      } else {
         this.raErrors.addElement(fmt.ZERO_LENGTH_NAME(var2, var1, var3));
         return var9;
      }
   }

   private Class checkClass(String var1, String var2, String var3, String[] var4, String[] var5, boolean var6) {
      return this.checkClass(var1, var2, var3, var4, var5, var6, (Boolean)null);
   }

   private void validateImplements(String var1, boolean var2, String var3, String var4, Class[] var5, String[] var6) {
      boolean var7 = var5 != null && var5.length > 0;
      if (var6 != null && var6.length != 0) {
         for(int var8 = 0; var8 < var6.length; ++var8) {
            boolean var9 = false;
            if (var7) {
               for(int var10 = 0; !var9 && var10 < var5.length; ++var10) {
                  if (var5[var10].getName().equals(var6[var8])) {
                     var9 = true;
                  }
               }
            }

            if (!var9) {
               this.reportMissingInterface(var1, var2, var3, var4, var6[var8]);
            }
         }

      }
   }

   private void reportMissingInterface(String var1, boolean var2, String var3, String var4, String var5) {
      if (var2) {
         this.raErrors.addElement(fmt.MUST_IMPLEMENT_INTERFACE(var1, var3, var4, var5));
      } else {
         this.raWarnings.addElement(fmt.SHOULD_IMPLEMENT_INTERFACE(var1, var3, var4, var5));
      }

   }

   private void checkJavaBeanCompliance(String var1, String var2, Class var3) {
      BeanInfo var4 = null;
      if (var3 != null) {
         try {
            Introspector.flushFromCaches(var3);
            var4 = Introspector.getBeanInfo(var3);
         } catch (IntrospectionException var6) {
            this.raWarnings.addElement(fmt.NOT_A_JAVA_BEAN(var1, var2, var3.getName(), var6.getMessage()));
         }

      }
   }

   private static boolean checkOverrides(Class var0, boolean var1) throws SecurityException {
      boolean var2 = false;
      boolean var3 = false;
      Method[] var4 = var0.getMethods();
      if (var4 != null && var4.length > 0) {
         for(int var5 = 0; var5 < var4.length && (!var2 || !var3); ++var5) {
            Method var6 = var4[var5];
            if (!var2) {
               var2 = !var6.getDeclaringClass().equals(JAVA_LANG_OBJECT_CLASS) && var6.getName().equals("equals") && var6.getReturnType().equals(Boolean.TYPE) && var6.getParameterTypes().length == 1 && var6.getParameterTypes()[0].getName().equals("java.lang.Object");
            }

            if (!var3) {
               var3 = !var6.getDeclaringClass().equals(JAVA_LANG_OBJECT_CLASS) && var6.getName().equals("hashCode") && var6.getReturnType().equals(Integer.TYPE) && var6.getParameterTypes().length == 0;
            }
         }
      }

      return var1 && var2 && var3 || !var1 && !var2 && !var3;
   }

   private void handleRAException(RAConfigurationException var1) {
      Throwable var3 = var1.getCause();
      String var2;
      if (var3 == null) {
         var2 = var1.getMessage();
      } else {
         Throwable var4 = var3.getCause();
         if (var3 instanceof DescriptorException) {
            var2 = "DescriptorException: ";
            if (var4 != null) {
               var2 = var2 + var4.toString();
            } else {
               var2 = var2 + var3.toString();
            }
         } else if (var4 != null) {
            var2 = var4.getClass().getName() + ": " + var4.toString();
         } else {
            var2 = var3.getClass().getName() + ": " + var3.toString();
         }
      }

      Debug.logComplianceRAConfigurationException(var2);
   }

   private void setClearIntrospectorCacheNeeded() {
      this.clearIntrospectorCacheNeeded = true;
   }

   private void clearIntrospectorCache() {
      Debug.println("Clear the BeanInfo cache for RA Classes");
      Iterator var1 = this.introspectedClasses.iterator();

      while(var1.hasNext()) {
         Class var2 = (Class)var1.next();
         Introspector.flushFromCaches(var2);
      }

      this.introspectedClasses.clear();
   }

   private void validateProperties(Class var1, ConfigPropertyBean[] var2, PropSetterTable var3, String var4, String var5) {
      Debug.enter("weblogic.connector.external.impl.RAComplianceChecker", ".validateProperties( " + (var1 == null ? "missing class" : var1.getName()) + " )");

      try {
         BeanInfo var7 = null;
         PropertyDescriptor[] var8 = null;
         MethodDescriptor[] var9 = null;
         Hashtable var10 = new Hashtable();
         Method var11 = null;

         try {
            if (var1 != null) {
               Debug.println("Get the BeanInfo object for the obj");
               Introspector.flushFromCaches(var1);
               var7 = Introspector.getBeanInfo(var1);
               if (this.clearIntrospectorCacheNeeded) {
                  this.introspectedClasses.add(var1);
               }

               Debug.println("Get all the methods & properties of the class");
               var8 = var7.getPropertyDescriptors();
               var9 = var7.getMethodDescriptors();
            }

            Debug.println("Iterate through all the config properties and (optionally) set them in the obj");
            String var12 = "";

            for(int var20 = 0; var20 < var2.length; ++var20) {
               ConfigPropertyBean var6 = var2[var20];
               var11 = null;
               this.validateConfigProperty(var4, var6);
               if (var1 != null) {
                  var11 = this.findWriteMethod(var4, var6, var8, var9, var1);
                  if (var11 == null) {
                     Debug.println("For property " + var6.getConfigPropertyName() + " no write method was found.");
                  }
               }

               this.checkForDuplicateProperty(var4, var6, var10);
               registerWriteMethod(var6, var11, var3);
            }
         } catch (IntrospectionException var18) {
            String var13 = Debug.getExceptionIntrospectProperties(var1.getName());
            Debug.println("Utils.validateProperties() error: " + var13);
            this.raErrors.addElement(var13);
         }
      } finally {
         Debug.exit("weblogic.connector.external.impl.RAComplianceChecker", "validateProperties() ");
      }

   }

   private void validateConfigProperty(String var1, ConfigPropertyBean var2) {
      String var3 = var2.getConfigPropertyName();
      String var4 = var2.getConfigPropertyValue();
      String var5 = var2.getConfigPropertyType();
      Debug.println("Validate the config property Name = '" + var3 + "' Type = '" + var5 + "' value = '" + var4 + "'");
      if (var3 == null || var3.length() == 0) {
         this.raWarnings.addElement(fmt.NULL_PROPERTY_NAME(var1, var5, var4));
      }

      if (var4 != null) {
         try {
            Utils.getValueByType(var4, var5);
         } catch (NumberFormatException var7) {
            this.raWarnings.addElement(fmt.PROPERTY_TYPE_VALUE_MISMATCH("ra.xml", var1, var3, var5, var4, var7.toString()));
         }
      }

   }

   private Method findWriteMethod(String var1, ConfigPropertyBean var2, PropertyDescriptor[] var3, MethodDescriptor[] var4, Class var5) {
      Debug.println("Find the write method");
      String var6 = var2.getConfigPropertyName();
      String var7 = var2.getConfigPropertyValue();
      String var8 = var2.getConfigPropertyType();
      Method var9 = null;
      MethodDescriptor var10 = null;

      try {
         var10 = this.findMethod(var1, var6, var8, var4);
      } catch (RACommonException var14) {
         return null;
      }

      if (var10 != null) {
         var9 = var10.getMethod();
         if (var9 != null) {
            Debug.println("In findWriteMethod(): Found a method whose name matches set" + var6);
            return var9;
         }

         Debug.println("In findWriteMethod(): Failed to find a method that matches set" + var6);
      }

      Debug.println("Utils.findWriteMethod() failed to find setter method. Looking for a PropertyDescriptor for a match");

      try {
         var9 = this.findPropertyWriteMethod(var1, var6, var8, var3);
      } catch (RACommonException var13) {
         return null;
      }

      if (var9 != null) {
         Debug.println("In findWriteMethod(): Found a method whose name matches set" + var6);
         return var9;
      } else {
         Debug.println("Utils.findWriteMethod() couldn't find property write method.  Looking for properties with a different type to help user out");
         PropertyDescriptor[] var11 = findProperties(var6, var3);
         if (var3 != null && var11 != null) {
            for(int var12 = 0; var12 < var11.length; ++var12) {
               var9 = this.checkPropByTypeAndGetWriteMethodOrReport(var1, var11[var12], var6, var8, var7, var5.getName());
            }
         }

         if (var9 != null) {
            Debug.println("In findWriteMethod(): Found a method whose name matches set" + var6);
            return var9;
         } else {
            Debug.println("In findWriteMethod(): Unable to find a method whose name matches set" + var6);
            this.raWarnings.addElement(fmt.NO_SET_METHOD_FOR_PROPERTY("ra.xml", var1, var2.getConfigPropertyName(), var2.getConfigPropertyType(), var5.getName()));
            return null;
         }
      }
   }

   private void checkForDuplicateProperty(String var1, ConfigPropertyBean var2, Hashtable var3) {
      String var4 = var2.getConfigPropertyName();
      ConfigPropertyBean var5 = (ConfigPropertyBean)var3.get(var4);
      if (var5 == null) {
         var3.put(var4, var2);
      } else {
         String var6;
         if (var5.getConfigPropertyType().equals(var2.getConfigPropertyType())) {
            var6 = fmt.DUPLICATE_RA_PROPERTY(var1, var4, var2.getConfigPropertyType(), var2.getConfigPropertyValue());
            this.raWarnings.addElement(var6);
         } else {
            var6 = fmt.DUPLICATE_RA_PROPERTY_MULTI_TYPES(var1, var4, var2.getConfigPropertyType(), var2.getConfigPropertyValue(), var5.getConfigPropertyType());
            this.raWarnings.addElement(var6);
         }
      }

   }

   private static void registerWriteMethod(ConfigPropertyBean var0, Method var1, PropSetterTable var2) {
      Debug.println("Utils.registerWriteMethod( " + var0 + ", " + var1 + " )");
      if (var2.hasRAProperty(var0)) {
         Debug.println("Utils.registerWriteMethod( table already contains prop = " + var0);
      }

      var2.setMethod(var0, var1);
   }

   private MethodDescriptor findMethod(String var1, String var2, String var3, MethodDescriptor[] var4) throws RACommonException {
      String var7 = "set" + var2;
      Class[] var8 = new Class[2];

      try {
         var8[0] = getClass(var3);
         String var9 = switchType(var3);
         if (var9 != null && !var3.equals(var9)) {
            var8[1] = getClass(var9);
         } else {
            var8[1] = null;
         }
      } catch (ClassNotFoundException var11) {
         String var10 = fmt.INVALID_PROPERTY_TYPE("ra.xml", var1, var3, var2, var11.toString());
         this.raWarnings.addElement(var10);
         throw new RACommonException();
      }

      for(int var12 = 0; var12 < var8.length; ++var12) {
         if (var8[var12] != null) {
            for(int var13 = 0; var13 < var4.length; ++var13) {
               MethodDescriptor var5 = var4[var13];
               String var6 = var5.getName();
               if (var6 != null && var6.equalsIgnoreCase(var7) && var5.getMethod().getParameterTypes().length == 1 && var5.getMethod().getParameterTypes()[0].equals(var8[var12])) {
                  return var5;
               }
            }
         }
      }

      return null;
   }

   private Method findPropertyWriteMethod(String var1, String var2, String var3, PropertyDescriptor[] var4) throws RACommonException {
      Class[] var8 = new Class[2];

      try {
         var8[0] = getClass(var3);
         String var9 = switchType(var3);
         if (var9 != null && !var3.equals(var9)) {
            var8[1] = getClass(var9);
         } else {
            var8[1] = null;
         }
      } catch (ClassNotFoundException var11) {
         String var10 = fmt.INVALID_PROPERTY_TYPE("ra.xml", var1, var3, var2, var11.toString());
         this.raWarnings.addElement(var10);
         throw new RACommonException();
      }

      for(int var12 = 0; var12 < var8.length; ++var12) {
         if (var8[var12] != null) {
            for(int var13 = 0; var13 < var4.length; ++var13) {
               PropertyDescriptor var5 = var4[var13];
               String var7 = var5.getName();
               Method var6 = var5.getWriteMethod();
               if (var7 != null && var7.equalsIgnoreCase(var2) && var6 != null && var6.getParameterTypes().length == 1 && var6.getParameterTypes()[0].equals(var8[var12])) {
                  return var6;
               }
            }
         }
      }

      return null;
   }

   private static PropertyDescriptor[] findProperties(String var0, PropertyDescriptor[] var1) {
      Vector var3 = new Vector();

      for(int var5 = 0; var5 < var1.length; ++var5) {
         PropertyDescriptor var2 = var1[var5];
         String var4 = var2.getName();
         if (var4 != null && var4.equalsIgnoreCase(var0)) {
            var3.addElement(var2);
         }
      }

      if (var3.size() > 0) {
         return (PropertyDescriptor[])((PropertyDescriptor[])var3.toArray());
      } else {
         return null;
      }
   }

   private Method checkPropByTypeAndGetWriteMethodOrReport(String var1, PropertyDescriptor var2, String var3, String var4, String var5, String var6) {
      Debug.println("In setProperty(): Found a property whose name matches " + var3);
      Class var7 = var2.getPropertyType();
      if (var7 != null) {
         boolean var8 = var4.equals(var7.getName());
         boolean var9 = var7.isPrimitive() && var4.equals(primitiveToObject(var7.getName()));
         boolean var10 = isPrimitive(var4) && var7.getName().equals(primitiveToObject(var4));
         if (Debug.getVerbose()) {
            Debug.println("For config prop with name '" + var3 + "' and type '" + var4 + "', ");
            Debug.println("\tthe object property found " + (var8 ? "had" : "did not have") + " the same type");
            if (var8) {
               Method var11 = var2.getWriteMethod();
               if (var11 != null) {
                  return var11;
               }
            } else if (var9) {
               Debug.println("\tthe object property type is primitive, and the config prop has the corresponding object type");
            } else if (var10) {
               Debug.println("\tthe config property type is primitive, and the object property type has the corresponding object type");
            } else {
               Debug.println("\tthe config property type and the object property type do not correspond");
            }
         }

         if (!var8 && !var9 && !var10) {
            this.raWarnings.addElement(fmt.PROPERTY_TYEPE_MISMATCH("ra.xml", var1, var3, var4, var5, var6));
         }
      } else {
         this.raWarnings.addElement(fmt.NULL_PROPERTYDESCRIPTOR_TYPE("ra.xml", var1, var3, var4, var2.getName(), var6));
      }

      return null;
   }

   private static Class getClass(String var0) throws ClassNotFoundException {
      if (var0 != null && !var0.equals("")) {
         for(int var1 = 0; var1 < NUM_PRIMS; ++var1) {
            if (var0.equals(PRIMITIVE_TYPES[var1])) {
               return PRIMITIVE_CLASSES[var1];
            }
         }

         return Class.forName(var0);
      } else {
         throw new ClassNotFoundException(var0);
      }
   }

   private static String switchType(String var0) {
      String var1 = null;
      if (isPrimitive(var0)) {
         var1 = primitiveToObject(var0);
      } else if (isObjectTypeOfPrimitive(var0)) {
         var1 = objectToPrimitive(var0);
      }

      return var1;
   }

   private static String primitiveToObject(String var0) {
      for(int var1 = 0; var1 < NUM_PRIMS; ++var1) {
         if (var0.equals(PRIMITIVE_TYPES[var1])) {
            return PRIMITIVE_OBJ_CLASSES[var1];
         }
      }

      return null;
   }

   private static String objectToPrimitive(String var0) {
      for(int var1 = 0; var1 < NUM_PRIMS; ++var1) {
         if (var0.equals(PRIMITIVE_OBJ_CLASSES[var1])) {
            return PRIMITIVE_TYPES[var1];
         }
      }

      return null;
   }

   private static boolean isPrimitive(String var0) {
      for(int var1 = 0; var1 < NUM_PRIMS; ++var1) {
         if (var0.equals(PRIMITIVE_TYPES[var1])) {
            return true;
         }
      }

      return false;
   }

   private static boolean isObjectTypeOfPrimitive(String var0) {
      for(int var1 = 0; var1 < NUM_OBJ_PRIMS; ++var1) {
         if (var0.equals(PRIMITIVE_OBJ_CLASSES[var1])) {
            return true;
         }
      }

      return false;
   }

   private void validateWLProps(String var1, ConfigPropertiesBean var2, PropSetterTable var3) {
      if (var2 != null) {
         weblogic.j2ee.descriptor.wl.ConfigPropertyBean[] var4 = var2.getProperties();

         for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
            weblogic.j2ee.descriptor.wl.ConfigPropertyBean var6 = var4[var5];
            ConfigPropertyBean var7 = var3.getRAProperty(var6.getName());
            if (var7 != null) {
               String var8 = var6.getValue();
               String var9 = var7.getConfigPropertyType();

               try {
                  Utils.getValueByType(var8, var9);
               } catch (NumberFormatException var11) {
                  this.raWarnings.addElement(fmt.PROPERTY_TYPE_VALUE_MISMATCH("weblogic-ra.xml", var1, var6.getName(), var9, var8, var11.toString()));
               }
            } else {
               this.raWarnings.addElement(fmt.MISSING_RA_PROPERTY(var1, var6.getName(), var6.getValue()));
            }
         }

      }
   }

   private void validateWLGroupProps(String var1, ConfigPropertiesBean var2, Collection var3) {
      if (var2 != null) {
         weblogic.j2ee.descriptor.wl.ConfigPropertyBean[] var4 = var2.getProperties();

         for(int var5 = 0; var4 != null && var5 < var4.length; ++var5) {
            weblogic.j2ee.descriptor.wl.ConfigPropertyBean var6 = var4[var5];
            ConfigPropertyBean var7 = this.raValidationInfo.getProperty(var6.getName(), var3);
            if (var7 != null) {
               String var8 = var6.getValue();
               String var9 = var7.getConfigPropertyType();

               try {
                  Utils.getValueByType(var8, var9);
               } catch (NumberFormatException var11) {
                  this.raWarnings.addElement(fmt.PROPERTY_TYPE_VALUE_MISMATCH("weblogic-ra.xml", var1, var6.getName(), var9, var8, var11.toString()));
               }
            } else {
               this.raWarnings.addElement(fmt.MISSING_RA_PROPERTY(var1, var6.getName(), var6.getValue()));
            }
         }

      }
   }

   static {
      PRIMITIVE_CLASSES = new Class[]{Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
      PRIMITIVE_TYPES = new String[]{"boolean", "byte", "char", "short", "int", "long", "float", "double"};
      PRIMITIVE_OBJ_CLASSES = new String[]{"java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Float", "java.lang.Double"};
      NUM_PRIMS = PRIMITIVE_TYPES.length;
      NUM_OBJ_PRIMS = PRIMITIVE_OBJ_CLASSES.length;
      factoryHelper = new RAComplianceChecker();
   }
}
