package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class MaxThreadsConstraintMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MaxThreadsConstraintMBean.class;

   public MaxThreadsConstraintMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MaxThreadsConstraintMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MaxThreadsConstraintMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean defines the max number of concurrent threads that can execute requests sharing this max constraint. <p> MaxThreadsConstraint can be used to tell the server that the requests are constrained by an external resource like a database and allocating more threads that the external resource limit is not going to help since the extra threads are just going to wait. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.MaxThreadsConstraintMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ConnectionPoolName")) {
         var3 = "getConnectionPoolName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionPoolName";
         }

         var2 = new PropertyDescriptor("ConnectionPoolName", MaxThreadsConstraintMBean.class, var3, var4);
         var1.put("ConnectionPoolName", var2);
         var2.setValue("description", "Name of the connection pool whose size is taken as the max constraint. <p> This can be the name of a JDBC data source. The max capacity of the data source is used as the constraint. </p> ");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("Count")) {
         var3 = "getCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCount";
         }

         var2 = new PropertyDescriptor("Count", MaxThreadsConstraintMBean.class, var3, var4);
         var1.put("Count", var2);
         var2.setValue("description", "Maximum number of concurrent threads that can execute requests sharing this constraint. <p> A count of 0 or -1 is treated as if the constraint is not present. This means that no constraint is enforced for these two values. A count > 0 can be dynamically changed to 0 to indicate that constraint enforcement is no longer needed. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
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
