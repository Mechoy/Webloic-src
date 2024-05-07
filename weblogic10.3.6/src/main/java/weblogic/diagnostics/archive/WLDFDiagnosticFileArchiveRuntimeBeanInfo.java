package weblogic.diagnostics.archive;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WLDFFileArchiveRuntimeMBean;

public class WLDFDiagnosticFileArchiveRuntimeBeanInfo extends WLDFDiagnosticArchiveRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = WLDFFileArchiveRuntimeMBean.class;

   public WLDFDiagnosticFileArchiveRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFDiagnosticFileArchiveRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFDiagnosticFileArchiveRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.archive");
      String var3 = (new String("<p>Use this interface to collect statistical information about file-based WLDF archives.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WLDFFileArchiveRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("IncrementalIndexCycleCount")) {
         var3 = "getIncrementalIndexCycleCount";
         var4 = null;
         var2 = new PropertyDescriptor("IncrementalIndexCycleCount", WLDFFileArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("IncrementalIndexCycleCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("IncrementalIndexTime")) {
         var3 = "getIncrementalIndexTime";
         var4 = null;
         var2 = new PropertyDescriptor("IncrementalIndexTime", WLDFFileArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("IncrementalIndexTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("IndexCycleCount")) {
         var3 = "getIndexCycleCount";
         var4 = null;
         var2 = new PropertyDescriptor("IndexCycleCount", WLDFFileArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("IndexCycleCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("IndexTime")) {
         var3 = "getIndexTime";
         var4 = null;
         var2 = new PropertyDescriptor("IndexTime", WLDFFileArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("IndexTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RecordRetrievalTime")) {
         var3 = "getRecordRetrievalTime";
         var4 = null;
         var2 = new PropertyDescriptor("RecordRetrievalTime", WLDFFileArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RecordRetrievalTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RecordSeekCount")) {
         var3 = "getRecordSeekCount";
         var4 = null;
         var2 = new PropertyDescriptor("RecordSeekCount", WLDFFileArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RecordSeekCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RecordSeekTime")) {
         var3 = "getRecordSeekTime";
         var4 = null;
         var2 = new PropertyDescriptor("RecordSeekTime", WLDFFileArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RecordSeekTime", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RetrievedRecordCount")) {
         var3 = "getRetrievedRecordCount";
         var4 = null;
         var2 = new PropertyDescriptor("RetrievedRecordCount", WLDFFileArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RetrievedRecordCount", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("RotatedFilesCount")) {
         var3 = "getRotatedFilesCount";
         var4 = null;
         var2 = new PropertyDescriptor("RotatedFilesCount", WLDFFileArchiveRuntimeMBean.class, var3, (String)var4);
         var1.put("RotatedFilesCount", var2);
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
      Method var3 = WLDFFileArchiveRuntimeMBean.class.getMethod("preDeregister");
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
