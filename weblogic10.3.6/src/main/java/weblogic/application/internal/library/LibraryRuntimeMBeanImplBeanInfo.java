package weblogic.application.internal.library;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.LibraryRuntimeMBean;

public class LibraryRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = LibraryRuntimeMBean.class;

   public LibraryRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public LibraryRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = LibraryRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.application.internal.library");
      String var3 = (new String("<h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.LibraryRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Components")) {
         var3 = "getComponents";
         var4 = null;
         var2 = new PropertyDescriptor("Components", LibraryRuntimeMBean.class, var3, (String)var4);
         var1.put("Components", var2);
         var2.setValue("description", "<p>Returns the component mbeans for this Library</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ImplementationVersion")) {
         var3 = "getImplementationVersion";
         var4 = null;
         var2 = new PropertyDescriptor("ImplementationVersion", LibraryRuntimeMBean.class, var3, (String)var4);
         var1.put("ImplementationVersion", var2);
         var2.setValue("description", "<p>Returns this Library's Implementation Version, null if none is set</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("LibraryIdentifier")) {
         var3 = "getLibraryIdentifier";
         var4 = null;
         var2 = new PropertyDescriptor("LibraryIdentifier", LibraryRuntimeMBean.class, var3, (String)var4);
         var1.put("LibraryIdentifier", var2);
         var2.setValue("description", "<p>Returns this Library's Identifier</p>  <p>The library identifier uniquely identifies this library version across all versions of all deployed applications and deployed libraries. If the library is not versioned, the library identifier is the same as the library name.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("LibraryName")) {
         var3 = "getLibraryName";
         var4 = null;
         var2 = new PropertyDescriptor("LibraryName", LibraryRuntimeMBean.class, var3, (String)var4);
         var1.put("LibraryName", var2);
         var2.setValue("description", "<p>Returns this Library's Name</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ReferencingNames")) {
         var3 = "getReferencingNames";
         var4 = null;
         var2 = new PropertyDescriptor("ReferencingNames", LibraryRuntimeMBean.class, var3, (String)var4);
         var1.put("ReferencingNames", var2);
         var2.setValue("description", "<p>Returns the names of all deployed applications that reference this Library</p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ReferencingRuntimes")) {
         var3 = "getReferencingRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("ReferencingRuntimes", LibraryRuntimeMBean.class, var3, (String)var4);
         var1.put("ReferencingRuntimes", var2);
         var2.setValue("description", "<p>Returns the RuntimeMBeans of current referencers of this Library. Typically a Library Referencer is a deployed application</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("SpecificationVersion")) {
         var3 = "getSpecificationVersion";
         var4 = null;
         var2 = new PropertyDescriptor("SpecificationVersion", LibraryRuntimeMBean.class, var3, (String)var4);
         var1.put("SpecificationVersion", var2);
         var2.setValue("description", "<p>Returns this Library's Specification Version, null if none is set</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("Referenced")) {
         var3 = "isReferenced";
         var4 = null;
         var2 = new PropertyDescriptor("Referenced", LibraryRuntimeMBean.class, var3, (String)var4);
         var1.put("Referenced", var2);
         var2.setValue("description", "<p>Returns true if this Library is referenced by one or more referencers. Typically a Library Referencer is a deployed application</p> ");
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
      Method var3 = LibraryRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
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
