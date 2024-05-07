package weblogic.diagnostics.accessor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WLDFDataAccessRuntimeMBean;

public class DiagnosticDataAccessRuntimeBeanInfo extends DataAccessRuntimeBeanInfo {
   public static Class INTERFACE_CLASS = WLDFDataAccessRuntimeMBean.class;

   public DiagnosticDataAccessRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DiagnosticDataAccessRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DiagnosticDataAccessRuntime.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.accessor");
      String var3 = (new String("<p>Use this interface to access the specific type of diagnostic data from an underlying log for which this instance is created.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer"), BeanInfoHelper.encodeEntities("Monitor"), BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WLDFDataAccessRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ColumnIndexMap")) {
         var3 = "getColumnIndexMap";
         var4 = null;
         var2 = new PropertyDescriptor("ColumnIndexMap", WLDFDataAccessRuntimeMBean.class, var3, var4);
         var1.put("ColumnIndexMap", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("ColumnInfoMap")) {
         var3 = "getColumnInfoMap";
         var4 = null;
         var2 = new PropertyDescriptor("ColumnInfoMap", WLDFDataAccessRuntimeMBean.class, var3, var4);
         var1.put("ColumnInfoMap", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("ColumnTypeMap")) {
         var3 = "getColumnTypeMap";
         var4 = null;
         var2 = new PropertyDescriptor("ColumnTypeMap", WLDFDataAccessRuntimeMBean.class, var3, var4);
         var1.put("ColumnTypeMap", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("Columns")) {
         var3 = "getColumns";
         var4 = null;
         var2 = new PropertyDescriptor("Columns", WLDFDataAccessRuntimeMBean.class, var3, var4);
         var1.put("Columns", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("DataArchiveParameters")) {
         var3 = "getDataArchiveParameters";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDataArchiveParameters";
         }

         var2 = new PropertyDescriptor("DataArchiveParameters", WLDFDataAccessRuntimeMBean.class, var3, var4);
         var1.put("DataArchiveParameters", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("EarliestAvailableTimestamp")) {
         var3 = "getEarliestAvailableTimestamp";
         var4 = null;
         var2 = new PropertyDescriptor("EarliestAvailableTimestamp", WLDFDataAccessRuntimeMBean.class, var3, var4);
         var1.put("EarliestAvailableTimestamp", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("LatestAvailableTimestamp")) {
         var3 = "getLatestAvailableTimestamp";
         var4 = null;
         var2 = new PropertyDescriptor("LatestAvailableTimestamp", WLDFDataAccessRuntimeMBean.class, var3, var4);
         var1.put("LatestAvailableTimestamp", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("LatestRecordId")) {
         var3 = "getLatestRecordId";
         var4 = null;
         var2 = new PropertyDescriptor("LatestRecordId", WLDFDataAccessRuntimeMBean.class, var3, var4);
         var1.put("LatestRecordId", var2);
         var2.setValue("description", " ");
      }

      if (!var1.containsKey("TimestampAvailable")) {
         var3 = "isTimestampAvailable";
         var4 = null;
         var2 = new PropertyDescriptor("TimestampAvailable", WLDFDataAccessRuntimeMBean.class, var3, var4);
         var1.put("TimestampAvailable", var2);
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
      Method var3 = WLDFDataAccessRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("retrieveDataRecords", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("retrieveDataRecords", Long.TYPE, Long.TYPE, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("retrieveDataRecords", Long.TYPE, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("retrieveDataRecords", Long.TYPE, Long.TYPE, Long.TYPE, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("openCursor", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("openCursor", String.class, Long.TYPE);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("openCursor", Long.TYPE, Long.TYPE, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("openCursor", Long.TYPE, Long.TYPE, String.class, Long.TYPE);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("openCursor", Long.TYPE, Long.TYPE, Long.TYPE, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("openCursor", Long.TYPE, Long.TYPE, Long.TYPE, String.class, Long.TYPE);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("getDataRecordCount", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("getDataRecordCount", Long.TYPE, Long.TYPE, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("getDataRecordCount", Long.TYPE, Long.TYPE, Long.TYPE, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("hasMoreData", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("fetch", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("fetch", String.class, Integer.TYPE);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("closeCursor", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("deleteDataRecords", Long.TYPE, Long.TYPE, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFDataAccessRuntimeMBean.class.getMethod("closeArchive");
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
