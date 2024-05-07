package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class XMLRegistryEntryMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = XMLRegistryEntryMBean.class;

   public XMLRegistryEntryMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public XMLRegistryEntryMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = XMLRegistryEntryMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "7.0.0.0 replaced by {@link weblogic.management.configuration.XMLRegistryMBean}. ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This is an entry in the XML registry. An XML registry entry is configuration information associated with a particular XML document type. The document type is identified by one or more of the following:  1) a public ID (e.g, \"-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN\" 2) a system ID (e.g, \"http://java.sun.com/j2ee/dtds/ejb-jar_2_0.dtd\") 3) a document root tag name (e.g., \"ejb-jar\")  In Version 6.0 this configuration information is used by the WebLogic JAXP implementation to choose the appropriate parser factories and parsers and to set up SAX EntityResolvers.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.XMLRegistryEntryMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DocumentBuilderFactory")) {
         var3 = "getDocumentBuilderFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDocumentBuilderFactory";
         }

         var2 = new PropertyDescriptor("DocumentBuilderFactory", XMLRegistryEntryMBean.class, var3, var4);
         var1.put("DocumentBuilderFactory", var2);
         var2.setValue("description", "<p>Provides the class name of the DocumentBuilderFactory that is associated with the registry entry.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("EntityPath")) {
         var3 = "getEntityPath";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEntityPath";
         }

         var2 = new PropertyDescriptor("EntityPath", XMLRegistryEntryMBean.class, var3, var4);
         var1.put("EntityPath", var2);
         var2.setValue("description", "<p>Provides the path name to a local copy of an external entity (e.g., a DTD) that is associated with this registry entry.</p>  <p>Return the path name to a local copy of an external entity (e.g., a DTD) that is associated with this registry entry. This pathname is relative to one of the XML registry directories of the installation.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ParserClassName")) {
         var3 = "getParserClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setParserClassName";
         }

         var2 = new PropertyDescriptor("ParserClassName", XMLRegistryEntryMBean.class, var3, var4);
         var1.put("ParserClassName", var2);
         var2.setValue("description", "<p>Provides the class name of any custom XML parser that is associated with the registry entry.</p>  <p>Return class name of any custom XML parser that is associated with the registry entry.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PublicId")) {
         var3 = "getPublicId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPublicId";
         }

         var2 = new PropertyDescriptor("PublicId", XMLRegistryEntryMBean.class, var3, var4);
         var1.put("PublicId", var2);
         var2.setValue("description", "<p>Provides the public id of the document type represented by this registry entry.</p>  <p>Get the public id of the document type represented by this registry entry.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RootElementTag")) {
         var3 = "getRootElementTag";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRootElementTag";
         }

         var2 = new PropertyDescriptor("RootElementTag", XMLRegistryEntryMBean.class, var3, var4);
         var1.put("RootElementTag", var2);
         var2.setValue("description", "<p>Provides the tag name of the document root element of the document type represented by this registry entry.</p>  <p>Get the tag name of the document root element of the document type represented by this registry entry.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SAXParserFactory")) {
         var3 = "getSAXParserFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSAXParserFactory";
         }

         var2 = new PropertyDescriptor("SAXParserFactory", XMLRegistryEntryMBean.class, var3, var4);
         var1.put("SAXParserFactory", var2);
         var2.setValue("description", "<p>Provides the class name of the SAXParserFactory that is associated with the registry entry.</p>  <p>Return the class name of the SAXParserFactory that is associated with the registry entry.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SystemId")) {
         var3 = "getSystemId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemId";
         }

         var2 = new PropertyDescriptor("SystemId", XMLRegistryEntryMBean.class, var3, var4);
         var1.put("SystemId", var2);
         var2.setValue("description", "<p>Provides the system id of the document type represented by this registry entry.</p>  <p>Get the system id of the document type represented by this registry entry.</p> ");
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
