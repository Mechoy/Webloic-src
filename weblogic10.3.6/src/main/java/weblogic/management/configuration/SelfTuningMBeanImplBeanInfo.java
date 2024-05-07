package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SelfTuningMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SelfTuningMBean.class;

   public SelfTuningMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SelfTuningMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SelfTuningMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("SelfTuningMBean holds global work manager component mbeans. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SelfTuningMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("Capacities")) {
         var3 = "getCapacities";
         var4 = null;
         var2 = new PropertyDescriptor("Capacities", SelfTuningMBean.class, var3, (String)var4);
         var1.put("Capacities", var2);
         var2.setValue("description", "Get all the capacity definitions ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyCapacity");
         var2.setValue("creator", "createCapacity");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ContextRequestClasses")) {
         var3 = "getContextRequestClasses";
         var4 = null;
         var2 = new PropertyDescriptor("ContextRequestClasses", SelfTuningMBean.class, var3, (String)var4);
         var1.put("ContextRequestClasses", var2);
         var2.setValue("description", "Get all the context request classes ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createContextRequestClass");
         var2.setValue("destroyer", "destroyContextRequestClass");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("FairShareRequestClasses")) {
         var3 = "getFairShareRequestClasses";
         var4 = null;
         var2 = new PropertyDescriptor("FairShareRequestClasses", SelfTuningMBean.class, var3, (String)var4);
         var1.put("FairShareRequestClasses", var2);
         var2.setValue("description", "Get all the fair share request classes ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createFairShareRequestClass");
         var2.setValue("destroyer", "destroyFairShareRequestClass");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MaxThreadsConstraints")) {
         var3 = "getMaxThreadsConstraints";
         var4 = null;
         var2 = new PropertyDescriptor("MaxThreadsConstraints", SelfTuningMBean.class, var3, (String)var4);
         var1.put("MaxThreadsConstraints", var2);
         var2.setValue("description", "Get all the max threads constraint ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyMaxThreadsConstraint");
         var2.setValue("creator", "createMaxThreadsConstraint");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MinThreadsConstraints")) {
         var3 = "getMinThreadsConstraints";
         var4 = null;
         var2 = new PropertyDescriptor("MinThreadsConstraints", SelfTuningMBean.class, var3, (String)var4);
         var1.put("MinThreadsConstraints", var2);
         var2.setValue("description", "Get all the min threads constraint ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyMinThreadsConstraint");
         var2.setValue("creator", "createMinThreadsConstraint");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("ResponseTimeRequestClasses")) {
         var3 = "getResponseTimeRequestClasses";
         var4 = null;
         var2 = new PropertyDescriptor("ResponseTimeRequestClasses", SelfTuningMBean.class, var3, (String)var4);
         var1.put("ResponseTimeRequestClasses", var2);
         var2.setValue("description", "Get all the response time request classes ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createResponseTimeRequestClass");
         var2.setValue("destroyer", "destroyResponseTimeRequestClass");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WorkManagers")) {
         var3 = "getWorkManagers";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagers", SelfTuningMBean.class, var3, (String)var4);
         var1.put("WorkManagers", var2);
         var2.setValue("description", "Get all the defined work managers. <p> A note about dynamic additions and deletions of WorkManager in a running server. Only applications or modules deployed or re-deployed after the changes are made can pick up the newly added WorkManagers. Existing production applications resolve their dispatch-policies to work managers during deployment time and once the application is exported and in production mode, the server does not swap work manager's midway. The tight binding helps performance and also avoids issues like what happens to inflight work in the old work manager. This attribute is marked as dynamic so that new applications (re)deployed can pick up the WorkManager changes. Please note that this applies only to resolving dispatch-policies to WorkManagers. Existing attributes within a work manager like fair-share and constraints can be modified without requiring a redeploy. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWorkManager");
         var2.setValue("creator", "createWorkManager");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "9.0.0.0");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("createFairShareRequestClass", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "This is the factory method for FairShareRequestClasses ");
            var2.setValue("role", "factory");
            var2.setValue("property", "FairShareRequestClasses");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("destroyFairShareRequestClass", FairShareRequestClassMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroys and removes a FairShareRequestClass which with the specified short name . ");
            var2.setValue("role", "factory");
            var2.setValue("property", "FairShareRequestClasses");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("createResponseTimeRequestClass", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "This is the factory method for ResponseTimeRequestClasss ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ResponseTimeRequestClasses");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("destroyResponseTimeRequestClass", ResponseTimeRequestClassMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroys and removes a ResponseTimeRequestClass which with the specified short name . ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ResponseTimeRequestClasses");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("createContextRequestClass", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "This is the factory method for ContextRequestClasss ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ContextRequestClasses");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("destroyContextRequestClass", ContextRequestClassMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroys and removes a ContextRequestClass which with the specified short name . ");
            var2.setValue("role", "factory");
            var2.setValue("property", "ContextRequestClasses");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("createMinThreadsConstraint", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "This is the factory method for MinThreadsConstraints ");
            var2.setValue("role", "factory");
            var2.setValue("property", "MinThreadsConstraints");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("destroyMinThreadsConstraint", MinThreadsConstraintMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroys and removes a MinThreadsConstraint which with the specified short name . ");
            var2.setValue("role", "factory");
            var2.setValue("property", "MinThreadsConstraints");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("createMaxThreadsConstraint", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "This is the factory method for MaxThreadsConstraints ");
            var2.setValue("role", "factory");
            var2.setValue("property", "MaxThreadsConstraints");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("destroyMaxThreadsConstraint", MaxThreadsConstraintMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroys and removes a MaxThreadsConstraint which with the specified short name . ");
            var2.setValue("role", "factory");
            var2.setValue("property", "MaxThreadsConstraints");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("createCapacity", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "This is the factory method for Capacitys ");
            var2.setValue("role", "factory");
            var2.setValue("property", "Capacities");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("destroyCapacity", CapacityMBean.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("bean", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Destroys and removes a Capacity which with the specified short name . ");
            var2.setValue("role", "factory");
            var2.setValue("property", "Capacities");
            var2.setValue("since", "9.0.0.0");
         }
      }

      String var6;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("createWorkManager", String.class);
         var6 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var6)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var6, var2);
            var2.setValue("description", "Create a new work manager ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WorkManagers");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("destroyWorkManager", WorkManagerMBean.class);
         var6 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var6)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var6, var2);
            var2.setValue("description", "Destroy work manager ");
            var2.setValue("role", "factory");
            var2.setValue("property", "WorkManagers");
            var2.setValue("since", "9.0.0.0");
         }
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      ParameterDescriptor[] var4;
      String var5;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("lookupFairShareRequestClass", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Lookup a particular FairShareRequestClass from the list. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "FairShareRequestClasses");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("lookupResponseTimeRequestClass", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Lookup a particular ResponseTimeRequestClass from the list. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "ResponseTimeRequestClasses");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("lookupContextRequestClass", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Lookup a particular ContextRequestClass from the list. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "ContextRequestClasses");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("lookupMinThreadsConstraint", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Lookup a particular MinThreadsConstraint from the list. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "MinThreadsConstraints");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("lookupMaxThreadsConstraint", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Lookup a particular MaxThreadsConstraint from the list. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "MaxThreadsConstraints");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("lookupCapacity", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "9.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Lookup a particular Capacity from the list. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "Capacities");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.0.0.0", (String)null, this.targetVersion)) {
         var3 = SelfTuningMBean.class.getMethod("lookupWorkManager", String.class);
         var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var4);
            var2.setValue("since", "10.0.0.0");
            var1.put(var5, var2);
            var2.setValue("description", "Lookup a particular work manager from the list. ");
            var2.setValue("role", "finder");
            var2.setValue("property", "WorkManagers");
            var2.setValue("since", "10.0.0.0");
         }
      }

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
