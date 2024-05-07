package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class XMLParserSelectRegistryEntryMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = XMLParserSelectRegistryEntryMBean.class;

   public XMLParserSelectRegistryEntryMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public XMLParserSelectRegistryEntryMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = XMLParserSelectRegistryEntryMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>A Parser Select Entry specifies the SAX/DOM parser factory implementation classes for a particular document type.  <p>By default, WebLogic server uses either the built-in (out-of-the-box) or default SAX/DOM parser factory implementation classes when it parses an XML document.  However, you can specify that particular XML documents, based on their public IDs, system IDs, or root elements, use different parser factory implementation classes than the default.  You do this by first creating an XML Registry and then creating a Parser Select entry and specifying how to identify the document and then the desired implementation classes.   The XML document type is identified by one or more of the following:  1) a public ID (e.g, \"-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN\" 2) a system ID (e.g, \"http://java.sun.com/j2ee/dtds/ejb-jar_2_0.dtd\") 3) a document root tag name (e.g., \"ejb-jar\")  This configuration information is used by the WebLogic JAXP implementation to choose the appropriate parser factories (SAX and DOM).  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.XMLParserSelectRegistryEntryMBean");
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

         var2 = new PropertyDescriptor("DocumentBuilderFactory", XMLParserSelectRegistryEntryMBean.class, var3, var4);
         var1.put("DocumentBuilderFactory", var2);
         var2.setValue("description", "<p>Specifies the fully qualified name of the class that implements the <tt>DocumentBuilderFactory</tt> API.  <p>When WebLogic Server begins to parse an XML document identified by either the public ID, system ID, or root element specified in this entry, and the applications specifies it wants a DOM parser, then WebLogic Server uses this implementation class when using the javax.xml.parsers.DocumentBuilderFactory to obtain the DOM parser. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ParserClassName")) {
         var3 = "getParserClassName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setParserClassName";
         }

         var2 = new PropertyDescriptor("ParserClassName", XMLParserSelectRegistryEntryMBean.class, var3, var4);
         var1.put("ParserClassName", var2);
         var2.setValue("description", "<p>Provides the class name of any custom XML parser that is associated with this parser select entry.</p>  <p>Return class name of any custom XML parser that is associated with the registry entry.</p> ");
         var2.setValue("deprecated", " ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PublicId")) {
         var3 = "getPublicId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPublicId";
         }

         var2 = new PropertyDescriptor("PublicId", XMLParserSelectRegistryEntryMBean.class, var3, var4);
         var1.put("PublicId", var2);
         var2.setValue("description", "<p>Specifies the public ID of the XML document type for which this XML registry entry is being configured.  <p>When WebLogic Server starts to parse an XML document that is identified with this public ID, it uses the SAX or DOM parser factory implementation class specified by this registry entry, rather than the built-in or default, when obtaining a SAX or DOM parser. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RootElementTag")) {
         var3 = "getRootElementTag";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRootElementTag";
         }

         var2 = new PropertyDescriptor("RootElementTag", XMLParserSelectRegistryEntryMBean.class, var3, var4);
         var1.put("RootElementTag", var2);
         var2.setValue("description", "<p>Specifies the root element of the XML document type for which this XML registry entry is being configured.  <p>When WebLogic Server starts to parse an XML document that is identified with this root element, it uses the SAX or DOM parser factory implementation class specified by this registry entry, rather than the built-in or default, when obtaining a SAX or DOM parser. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SAXParserFactory")) {
         var3 = "getSAXParserFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSAXParserFactory";
         }

         var2 = new PropertyDescriptor("SAXParserFactory", XMLParserSelectRegistryEntryMBean.class, var3, var4);
         var1.put("SAXParserFactory", var2);
         var2.setValue("description", "<p>Specifies the fully qualified name of the class that implements the <tt>SAXParserFactory</tt> API.  <p>When WebLogic Server begins to parse an XML document identified by either the public ID, system ID, or root element specified in this entry, and the applications specifies it wants a SAX parser, then WebLogic Server uses this implementation class when using the javax.xml.parsers.SAXParserFactory to obtain the SAX parser. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SystemId")) {
         var3 = "getSystemId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemId";
         }

         var2 = new PropertyDescriptor("SystemId", XMLParserSelectRegistryEntryMBean.class, var3, var4);
         var1.put("SystemId", var2);
         var2.setValue("description", "<p>Specifies the system ID of the XML document type for which this XML registry entry is being configured.  <p>When WebLogic Server starts to parse an XML document that is identified with this system ID, it uses the SAX or DOM parser factory implementation class specified by this registry entry, rather than the built-in or default, when obtaining a SAX or DOM parser. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TransformerFactory")) {
         var3 = "getTransformerFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransformerFactory";
         }

         var2 = new PropertyDescriptor("TransformerFactory", XMLParserSelectRegistryEntryMBean.class, var3, var4);
         var1.put("TransformerFactory", var2);
         var2.setValue("description", "<p>Specifies the fully qualified name of the class that implements the <tt>TransformerFactory</tt> API.  <p>When WebLogic Server begins to transform an XML document identified by either the public ID, system ID, or root element specified in this entry, then WebLogic Server uses this implementation class when using the javax.xml.transform.TranformerFactory factory to obtain a Transformer object. ");
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
