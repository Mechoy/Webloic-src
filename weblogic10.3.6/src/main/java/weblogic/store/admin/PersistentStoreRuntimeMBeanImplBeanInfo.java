package weblogic.store.admin;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.PersistentStoreRuntimeMBean;

public class PersistentStoreRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = PersistentStoreRuntimeMBean.class;

   public PersistentStoreRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public PersistentStoreRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = PersistentStoreRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.store.admin");
      String var3 = (new String("<h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.PersistentStoreRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AllocatedIoBufferBytes")) {
         var3 = "getAllocatedIoBufferBytes";
         var4 = null;
         var2 = new PropertyDescriptor("AllocatedIoBufferBytes", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("AllocatedIoBufferBytes", var2);
         var2.setValue("description", "<p>The amount of off-heap (native) memory, in bytes, reserved for file store use. When applicable, this is a multiple of the file store configurable attribute <code>IOBufferSize</code>. Applies to synchronous write policies <code>Direct-Write</code> and <code>Cache-Flush policies</code>.</p> ");
      }

      if (!var1.containsKey("AllocatedWindowBufferBytes")) {
         var3 = "getAllocatedWindowBufferBytes";
         var4 = null;
         var2 = new PropertyDescriptor("AllocatedWindowBufferBytes", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("AllocatedWindowBufferBytes", var2);
         var2.setValue("description", "<p>The amount of off-heap (native) memory, in bytes, reserved for file store window buffer use.</p> <p>Applies to synchronous write policies <code>Direct-Write-With-Cache</code> and <code>Disabled</code> but only when the native <code>wlfileio</code> library is loaded. See file store configurable attribute <code>MaxWindowBufferSize</code> for more information. ");
      }

      if (!var1.containsKey("Connections")) {
         var3 = "getConnections";
         var4 = null;
         var2 = new PropertyDescriptor("Connections", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("Connections", var2);
         var2.setValue("description", "<p>The connections contained in the store.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("CreateCount")) {
         var3 = "getCreateCount";
         var4 = null;
         var2 = new PropertyDescriptor("CreateCount", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("CreateCount", var2);
         var2.setValue("description", "<p>Number of create requests issued by this store.</p> ");
      }

      if (!var1.containsKey("DeleteCount")) {
         var3 = "getDeleteCount";
         var4 = null;
         var2 = new PropertyDescriptor("DeleteCount", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("DeleteCount", var2);
         var2.setValue("description", "<p>Number of delete requests issued by this store.</p> ");
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "Implement the HealthFeedback interface by returning the health state of this store. ");
      }

      if (!var1.containsKey("ObjectCount")) {
         var3 = "getObjectCount";
         var4 = null;
         var2 = new PropertyDescriptor("ObjectCount", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("ObjectCount", var2);
         var2.setValue("description", "<p>Number of objects contained in the store.</p> ");
      }

      if (!var1.containsKey("PhysicalWriteCount")) {
         var3 = "getPhysicalWriteCount";
         var4 = null;
         var2 = new PropertyDescriptor("PhysicalWriteCount", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("PhysicalWriteCount", var2);
         var2.setValue("description", "<p>Number of times the store flushed its data to durable storage.</p> ");
      }

      if (!var1.containsKey("ReadCount")) {
         var3 = "getReadCount";
         var4 = null;
         var2 = new PropertyDescriptor("ReadCount", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("ReadCount", var2);
         var2.setValue("description", "<p>Number of read requests issued by this store, including requests that occur during store initialization.</p> ");
      }

      if (!var1.containsKey("UpdateCount")) {
         var3 = "getUpdateCount";
         var4 = null;
         var2 = new PropertyDescriptor("UpdateCount", PersistentStoreRuntimeMBean.class, var3, (String)var4);
         var1.put("UpdateCount", var2);
         var2.setValue("description", "<p>Number of update requests issued by this store.</p> ");
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
      Method var3 = PersistentStoreRuntimeMBean.class.getMethod("preDeregister");
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
