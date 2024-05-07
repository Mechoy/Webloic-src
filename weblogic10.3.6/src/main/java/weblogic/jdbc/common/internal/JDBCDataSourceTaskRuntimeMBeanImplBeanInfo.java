package weblogic.jdbc.common.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.JDBCDataSourceTaskRuntimeMBean;
import weblogic.management.runtime.TaskRuntimeMBeanImplBeanInfo;

public class JDBCDataSourceTaskRuntimeMBeanImplBeanInfo extends TaskRuntimeMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JDBCDataSourceTaskRuntimeMBean.class;

   public JDBCDataSourceTaskRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JDBCDataSourceTaskRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JDBCDataSourceTaskRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.jdbc.common.internal");
      String var3 = (new String(" ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.JDBCDataSourceTaskRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BeginTime")) {
         var3 = "getBeginTime";
         var4 = null;
         var2 = new PropertyDescriptor("BeginTime", JDBCDataSourceTaskRuntimeMBean.class, var3, var4);
         var1.put("BeginTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         var2 = new PropertyDescriptor("Description", JDBCDataSourceTaskRuntimeMBean.class, var3, var4);
         var1.put("Description", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("EndTime")) {
         var3 = "getEndTime";
         var4 = null;
         var2 = new PropertyDescriptor("EndTime", JDBCDataSourceTaskRuntimeMBean.class, var3, var4);
         var1.put("EndTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Error")) {
         var3 = "getError";
         var4 = null;
         var2 = new PropertyDescriptor("Error", JDBCDataSourceTaskRuntimeMBean.class, var3, var4);
         var1.put("Error", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("ParentTask")) {
         var3 = "getParentTask";
         var4 = null;
         var2 = new PropertyDescriptor("ParentTask", JDBCDataSourceTaskRuntimeMBean.class, var3, var4);
         var1.put("ParentTask", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Status")) {
         var3 = "getStatus";
         var4 = null;
         var2 = new PropertyDescriptor("Status", JDBCDataSourceTaskRuntimeMBean.class, var3, var4);
         var1.put("Status", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("SubTasks")) {
         var3 = "getSubTasks";
         var4 = null;
         var2 = new PropertyDescriptor("SubTasks", JDBCDataSourceTaskRuntimeMBean.class, var3, var4);
         var1.put("SubTasks", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Running")) {
         var3 = "isRunning";
         var4 = null;
         var2 = new PropertyDescriptor("Running", JDBCDataSourceTaskRuntimeMBean.class, var3, var4);
         var1.put("Running", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("SystemTask")) {
         var3 = "isSystemTask";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSystemTask";
         }

         var2 = new PropertyDescriptor("SystemTask", JDBCDataSourceTaskRuntimeMBean.class, var3, var4);
         var1.put("SystemTask", var2);
         var2.setValue("description", " ");
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
      Method var3 = JDBCDataSourceTaskRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCDataSourceTaskRuntimeMBean.class.getMethod("cancel");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = JDBCDataSourceTaskRuntimeMBean.class.getMethod("printLog", PrintWriter.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
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
