package weblogic.diagnostics.accessor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WLDFAccessRuntimeMBean;

public class DiagnosticAccessRuntimeBeanInfo extends AccessRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = WLDFAccessRuntimeMBean.class;

   public DiagnosticAccessRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DiagnosticAccessRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DiagnosticAccessRuntime.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.accessor");
      String var3 = (new String("<p>Use this interface to access the different types of diagnostic data generated by a server.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer"), BeanInfoHelper.encodeEntities("Monitor"), BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WLDFAccessRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("AvailableDiagnosticDataAccessorNames")) {
         var3 = "getAvailableDiagnosticDataAccessorNames";
         var4 = null;
         var2 = new PropertyDescriptor("AvailableDiagnosticDataAccessorNames", WLDFAccessRuntimeMBean.class, var3, (String)var4);
         var1.put("AvailableDiagnosticDataAccessorNames", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("DataAccessRuntimes")) {
         var3 = "getDataAccessRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("DataAccessRuntimes", WLDFAccessRuntimeMBean.class, var3, (String)var4);
         var1.put("DataAccessRuntimes", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WLDFDataAccessRuntimes")) {
         var3 = "getWLDFDataAccessRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFDataAccessRuntimes", WLDFAccessRuntimeMBean.class, var3, (String)var4);
         var1.put("WLDFDataAccessRuntimes", var2);
         var2.setValue("description", "<p>An array containing all known instances of the WLDFDataAccessRuntimeMBean MBeans on this server.</p> ");
         var2.setValue("relationship", "containment");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFAccessRuntimeMBean.class.getMethod("lookupDataAccessRuntime", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "DataAccessRuntimes");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFAccessRuntimeMBean.class.getMethod("lookupWLDFDataAccessRuntime", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>An instance of a WLDFDataAccessMBean defined by its logical name. The name is constructed from the specific type of log data requested.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFAccessRuntimeMBean.class.getMethod("preDeregister");
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