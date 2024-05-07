package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class XMLRegistryMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = XMLRegistryMBean.class;

   public XMLRegistryMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public XMLRegistryMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = XMLRegistryMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Configure the behavior of JAXP (Java API for XML Parsing) in the server.  <p>You configure this behavior by creating XML Registries that specify the default DOM and Sax factory implementation class, transformer factory implementation class, external entity resolution and caching.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.XMLRegistryMBean");
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

         var2 = new PropertyDescriptor("DocumentBuilderFactory", XMLRegistryMBean.class, var3, var4);
         var1.put("DocumentBuilderFactory", var2);
         var2.setValue("description", "<p>The fully qualified name of the class that implements the </tt>DocumentBuilderFactory</tt> interface.  <p>The <tt>javax.xml.parsers.DocumentBuilderFactory</tt> factory API enables applications deployed to WebLogic Server to obtain an XML parser that produces DOM object trees from XML documents.  <p>The built-in WebLogic Server DOM factory implementation class is <tt>com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl</tt>. This is the factory class applications deployed to WebLogic Server get by default when they request a DOM parser.  You can change this default by updating this value.  <p>Return the class name of the default DocumentBuilderFactory</p> ");
         setPropertyDescriptorDefault(var2, "weblogic.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("EntitySpecRegistryEntries")) {
         var3 = "getEntitySpecRegistryEntries";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEntitySpecRegistryEntries";
         }

         var2 = new PropertyDescriptor("EntitySpecRegistryEntries", XMLRegistryMBean.class, var3, var4);
         var1.put("EntitySpecRegistryEntries", var2);
         var2.setValue("description", "<p>Provides a list of EntitySpec registry entries.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("remover", "removeEntitySpecRegistryEntry");
         var2.setValue("adder", "addEntitySpecRegistryEntry");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", XMLRegistryMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("ParserSelectRegistryEntries")) {
         var3 = "getParserSelectRegistryEntries";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setParserSelectRegistryEntries";
         }

         var2 = new PropertyDescriptor("ParserSelectRegistryEntries", XMLRegistryMBean.class, var3, var4);
         var1.put("ParserSelectRegistryEntries", var2);
         var2.setValue("description", "<dp>Provides a list of the set of ParserSelect registry entries.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("remover", "removeParserSelectRegistryEntry");
         var2.setValue("adder", "addParserSelectRegistryEntry");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RegistryEntries")) {
         var3 = "getRegistryEntries";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRegistryEntries";
         }

         var2 = new PropertyDescriptor("RegistryEntries", XMLRegistryMBean.class, var3, var4);
         var1.put("RegistryEntries", var2);
         var2.setValue("description", "<p>Provides a list of the set of pre-Silversword style registry entries.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addRegistryEntry");
         var2.setValue("deprecated", "7.0.0.0 replaced by {@link weblogic.management.configuration.XMLRegistryMBean} ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SAXParserFactory")) {
         var3 = "getSAXParserFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSAXParserFactory";
         }

         var2 = new PropertyDescriptor("SAXParserFactory", XMLRegistryMBean.class, var3, var4);
         var1.put("SAXParserFactory", var2);
         var2.setValue("description", "<p>The fully qualified name of the class that implements the </tt>SAXParserFactory</tt> interface.  <p>The <tt>javax.xml.parsers.SAXParserFactory</tt> factory API enables applications deployed to WebLogic Server to configure and obtain a SAX-based XML parser to parse XML documents.  <p>The built-in WebLogic Server SAX factory implementation class is <tt>com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl</tt>. This is the factory class applications deployed to WebLogic Server get by default when they request a SAX parser.  You can change this default by updating this value.  <p>Return the class name of the default SAXParserFactory</p> ");
         setPropertyDescriptorDefault(var2, "weblogic.apache.xerces.jaxp.SAXParserFactoryImpl");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TransformerFactory")) {
         var3 = "getTransformerFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransformerFactory";
         }

         var2 = new PropertyDescriptor("TransformerFactory", XMLRegistryMBean.class, var3, var4);
         var1.put("TransformerFactory", var2);
         var2.setValue("description", "<p>The fully qualified name of the class that implements the </tt>TransformerFactory</tt> interface.  <p>The <tt>javax.xml.transform.TransformerFactory</tt> factory API enables applications deployed to WebLogic Server to configure and obtain a <tt>Transformer</tt> object used to transform XML data into another format.  <p>The built-in WebLogic Server Transformer factory implementation class is <tt>com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryIml</tt>. This is the factory class applications deployed to WebLogic Server get by default when they request a Transformer object.  You can change this default by updating this value.   <p>Return the class name of the default TransformerFactory</p> ");
         setPropertyDescriptorDefault(var2, "weblogic.xml.jaxp.WebLogicTransformerFactory");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WhenToCache")) {
         var3 = "getWhenToCache";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWhenToCache";
         }

         var2 = new PropertyDescriptor("WhenToCache", XMLRegistryMBean.class, var3, var4);
         var1.put("WhenToCache", var2);
         var2.setValue("description", "<p>Specifies when WebLogic Server should cache external entities that it retrieves from the Web.  <p>When WebLogic Server resolves an external entity within an XML file and retrieves the entity from the Web, you can specify that WebLogic Server cache this entity only when the entity is first referenced, when WebLogic Server first starts up, or not at all. ");
         setPropertyDescriptorDefault(var2, "cache-on-reference");
         var2.setValue("legalValues", new Object[]{"cache-on-reference", "cache-at-initialization", "cache-never"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("XMLEntitySpecRegistryEntries")) {
         var3 = "getXMLEntitySpecRegistryEntries";
         var4 = null;
         var2 = new PropertyDescriptor("XMLEntitySpecRegistryEntries", XMLRegistryMBean.class, var3, var4);
         var1.put("XMLEntitySpecRegistryEntries", var2);
         var2.setValue("description", "<p>Provides a list of EntitySpec registry entries.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyXMLEntitySpecRegistryEntry");
         var2.setValue("creator", "createXMLEntitySpecRegistryEntry");
      }

      if (!var1.containsKey("XMLParserSelectRegistryEntries")) {
         var3 = "getXMLParserSelectRegistryEntries";
         var4 = null;
         var2 = new PropertyDescriptor("XMLParserSelectRegistryEntries", XMLRegistryMBean.class, var3, var4);
         var1.put("XMLParserSelectRegistryEntries", var2);
         var2.setValue("description", "<p>Provides a list of the set of ParserSelect registry entries.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyXMLParserSelectRegistryEntry");
         var2.setValue("creator", "createXMLParserSelectRegistryEntry");
      }

      if (!var1.containsKey("HandleEntityInvalidation")) {
         var3 = "isHandleEntityInvalidation";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setHandleEntityInvalidation";
         }

         var2 = new PropertyDescriptor("HandleEntityInvalidation", XMLRegistryMBean.class, var3, var4);
         var1.put("HandleEntityInvalidation", var2);
         var2.setValue("description", "<p>Whether cached DTD/schema is invalidated when parsing error is encountered. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = XMLRegistryMBean.class.getMethod("createXMLParserSelectRegistryEntry", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "creates an XMLParserSelectRegistryEntryMBean object ");
         var2.setValue("role", "factory");
         var2.setValue("property", "XMLParserSelectRegistryEntries");
      }

      var3 = XMLRegistryMBean.class.getMethod("destroyXMLParserSelectRegistryEntry", XMLParserSelectRegistryEntryMBean.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("XMLParserSelectRegistryEntry", "object ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "factory");
         var2.setValue("property", "XMLParserSelectRegistryEntries");
      }

      var3 = XMLRegistryMBean.class.getMethod("createXMLEntitySpecRegistryEntry", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "factory");
         var2.setValue("property", "XMLEntitySpecRegistryEntries");
      }

      var3 = XMLRegistryMBean.class.getMethod("destroyXMLEntitySpecRegistryEntry", XMLEntitySpecRegistryEntryMBean.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("XMLEntitySpecRegistryEntry", "object ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "factory");
         var2.setValue("property", "XMLEntitySpecRegistryEntries");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = XMLRegistryMBean.class.getMethod("addRegistryEntry", XMLRegistryEntryMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("entry", "The feature to be added to the RegistryEntry attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "7.0.0.0 replaced by {@link weblogic.management.configuration.XMLRegistryMBean} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a new pre-Silversword style registry entry</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "collection");
         var2.setValue("property", "RegistryEntries");
      }

      var3 = XMLRegistryMBean.class.getMethod("addParserSelectRegistryEntry", XMLParserSelectRegistryEntryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("entry", "The feature to be added to the ParserSelectRegistryEntry attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a new ParserSelect registry entry</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "collection");
         var2.setValue("property", "ParserSelectRegistryEntries");
      }

      var3 = XMLRegistryMBean.class.getMethod("removeParserSelectRegistryEntry", XMLParserSelectRegistryEntryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("entry", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a ParserSelect registry entry</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "collection");
         var2.setValue("property", "ParserSelectRegistryEntries");
      }

      var3 = XMLRegistryMBean.class.getMethod("addEntitySpecRegistryEntry", XMLEntitySpecRegistryEntryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("entry", "The feature to be added to the EntitySpecRegistryEntry attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a new EntitySpec registry entry</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "collection");
         var2.setValue("property", "EntitySpecRegistryEntries");
      }

      var3 = XMLRegistryMBean.class.getMethod("removeEntitySpecRegistryEntry", XMLEntitySpecRegistryEntryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("entry", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a EntitySpec registry entry</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "collection");
         var2.setValue("property", "EntitySpecRegistryEntries");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = XMLRegistryMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = XMLRegistryMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
      }

      var3 = XMLRegistryMBean.class.getMethod("findParserSelectMBeanByKey", String.class, String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("publicID", (String)null), createParameterDescriptor("systemID", (String)null), createParameterDescriptor("rootTag", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns an parser select registry entry given the entry's key.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = XMLRegistryMBean.class.getMethod("findEntitySpecMBeanByKey", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("publicID", (String)null), createParameterDescriptor("systemID", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns an entity spec registry entry given the entry's key.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

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
