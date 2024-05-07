package weblogic.diagnostics.archive;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WLDFEditableArchiveRuntimeMBean;

public class WLDFDiagnosticEditableArchiveRuntimeBeanInfo extends WLDFDiagnosticArchiveRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = WLDFEditableArchiveRuntimeMBean.class;

   public WLDFDiagnosticEditableArchiveRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFDiagnosticEditableArchiveRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFDiagnosticEditableArchiveRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("since", "10.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.archive");
      String var3 = (new String("<p>Use this interface to collect statistical information about the editable archives maintained by WLDF, such as JDBC based and weblogic.store based WLDF archives</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WLDFEditableArchiveRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("DataRetirementCycles")) {
         var3 = "getDataRetirementCycles";
         var4 = null;
         var2 = new PropertyDescriptor("DataRetirementCycles", WLDFEditableArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("DataRetirementCycles", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("DataRetirementTotalTime")) {
         var3 = "getDataRetirementTotalTime";
         var4 = null;
         var2 = new PropertyDescriptor("DataRetirementTotalTime", WLDFEditableArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("DataRetirementTotalTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("LastDataRetirementStartTime")) {
         var3 = "getLastDataRetirementStartTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastDataRetirementStartTime", WLDFEditableArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("LastDataRetirementStartTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("LastDataRetirementTime")) {
         var3 = "getLastDataRetirementTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastDataRetirementTime", WLDFEditableArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("LastDataRetirementTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RecordRetrievalTime")) {
         var3 = "getRecordRetrievalTime";
         var4 = null;
         var2 = new PropertyDescriptor("RecordRetrievalTime", WLDFEditableArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RecordRetrievalTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RecordSeekCount")) {
         var3 = "getRecordSeekCount";
         var4 = null;
         var2 = new PropertyDescriptor("RecordSeekCount", WLDFEditableArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RecordSeekCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RecordSeekTime")) {
         var3 = "getRecordSeekTime";
         var4 = null;
         var2 = new PropertyDescriptor("RecordSeekTime", WLDFEditableArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RecordSeekTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RetiredRecordCount")) {
         var3 = "getRetiredRecordCount";
         var4 = null;
         var2 = new PropertyDescriptor("RetiredRecordCount", WLDFEditableArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RetiredRecordCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RetrievedRecordCount")) {
         var3 = "getRetrievedRecordCount";
         var4 = null;
         var2 = new PropertyDescriptor("RetrievedRecordCount", WLDFEditableArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RetrievedRecordCount", var2);
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
      Method var3 = WLDFEditableArchiveRuntimeMBean.class.getMethod("performDataRetirement");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Perform data retirement on demand, and delete records older than specified age in the retirement policy. </p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFEditableArchiveRuntimeMBean.class.getMethod("preDeregister");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFEditableArchiveRuntimeMBean.class.getMethod("performRetirement");
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
