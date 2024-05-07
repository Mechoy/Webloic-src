package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class XMLEntitySpecRegistryEntryMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = XMLEntitySpecRegistryEntryMBean.class;

   public XMLEntitySpecRegistryEntryMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public XMLEntitySpecRegistryEntryMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = XMLEntitySpecRegistryEntryMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>An Entity Spec Entry specifies how to resolve an external entity based its public and system IDs.  <p>When WebLogic Server is parsing an XML document and it encounters the specified external entity, WebLogic Server resolves the entity according to the values entered for this entry.  You can specify that the external entity resolve to a particular resource whose location is either a pathname reachable by the Administration Server or a URI to a local repository.  <p>An Entity Spec entry is part of an XML Registry.  <p>For this type of registry entry the document type is identified by either or both of:  1) a public ID (e.g, \"-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN\" 2) a system ID (e.g, \"http://java.sun.com/j2ee/dtds/ejb-jar_2_0.dtd\")  This configuration information is used by the WebLogic JAXP implementation to set up SAX EntityResolvers.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.XMLEntitySpecRegistryEntryMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CacheTimeoutInterval")) {
         var3 = "getCacheTimeoutInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCacheTimeoutInterval";
         }

         var2 = new PropertyDescriptor("CacheTimeoutInterval", XMLEntitySpecRegistryEntryMBean.class, var3, var4);
         var1.put("CacheTimeoutInterval", var2);
         var2.setValue("description", "<p>Specifies the default timeout interval (in seconds) of the external entity cache.  <p>A value of <tt>-1</tt> means that the entity cache timeout interval defers to the one specified for the XML registry of which this Entity Spec entry is a part. ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("EntityURI")) {
         var3 = "getEntityURI";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEntityURI";
         }

         var2 = new PropertyDescriptor("EntityURI", XMLEntitySpecRegistryEntryMBean.class, var3, var4);
         var1.put("EntityURI", var2);
         var2.setValue("description", "<p>The location of the external entity, either a pathname or URI.  <p>The location of the external entity can be either a pathname relative to the XML Registry directories, reachable by the Administration Server, or a URI of the entity location in some local repository.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("HandleEntityInvalidation")) {
         var3 = "getHandleEntityInvalidation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHandleEntityInvalidation";
         }

         var2 = new PropertyDescriptor("HandleEntityInvalidation", XMLEntitySpecRegistryEntryMBean.class, var3, var4);
         var1.put("HandleEntityInvalidation", var2);
         var2.setValue("description", "<p>Whether cached DTD/schema is invalidated when parsing error is encountered. ");
         setPropertyDescriptorDefault(var2, "defer-to-registry-setting");
         var2.setValue("legalValues", new Object[]{"true", "false", "defer-to-registry-setting"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PublicId")) {
         var3 = "getPublicId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPublicId";
         }

         var2 = new PropertyDescriptor("PublicId", XMLEntitySpecRegistryEntryMBean.class, var3, var4);
         var1.put("PublicId", var2);
         var2.setValue("description", "<p>The public ID of the external entity.  <p>When WebLogic Server is parsing an XML document and it encounters an external entity with this public ID, WebLogic Server resolves the entity (to either a local file or a URL resource) according to the values set for this Entity spec entry. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SystemId")) {
         var3 = "getSystemId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemId";
         }

         var2 = new PropertyDescriptor("SystemId", XMLEntitySpecRegistryEntryMBean.class, var3, var4);
         var1.put("SystemId", var2);
         var2.setValue("description", "<p>The system ID of the external entity.  <p>When WebLogic Server is parsing an XML document and it encounters an external entity with this system ID, WebLogic Server resolves the entity (to either a local file or a URL resource) according to the values set for this Entity spec entry. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WhenToCache")) {
         var3 = "getWhenToCache";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWhenToCache";
         }

         var2 = new PropertyDescriptor("WhenToCache", XMLEntitySpecRegistryEntryMBean.class, var3, var4);
         var1.put("WhenToCache", var2);
         var2.setValue("description", "<p>Specifies when WebLogic Server should cache the external entities it retrieves from the Web.  <p>WebLogic Server can cache the entities when they are referenced, as soon as possible (that is, on initialization) or never. Additionally, WebLogic Server can defer to the XML registry's cache setting.</p> ");
         setPropertyDescriptorDefault(var2, "defer-to-registry-setting");
         var2.setValue("legalValues", new Object[]{"cache-on-reference", "cache-at-initialization", "cache-never", "defer-to-registry-setting"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   protected void buildMethodDescriptors(Map var1) throws IntrospectionException, NoSuchMethodException {
      this.fillinFinderMethodInfos(var1);
      if (!this.readOnly) {
         this.fillinCollectionMethodInfos(var1);
         this.fillinFactoryMethodInfos(var1);
      }

      this.fillinOperationMethodInfos(var1);
      super.buildMethodDescriptors(var1);
   }

   protected void buildEventSetDescriptors(Map var1) throws IntrospectionException {
   }
}
