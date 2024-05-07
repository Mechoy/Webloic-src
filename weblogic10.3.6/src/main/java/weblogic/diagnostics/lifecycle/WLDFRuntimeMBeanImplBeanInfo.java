package weblogic.diagnostics.lifecycle;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WLDFRuntimeMBean;

public class WLDFRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFRuntimeMBean.class;

   public WLDFRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.lifecycle");
      String var3 = (new String("<p>This interface provides access to all the runtime MBeans for the WebLogic Diagnostic Framework (WLDF). ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WLDFRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("WLDFAccessRuntime")) {
         var3 = "getWLDFAccessRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFAccessRuntime", WLDFRuntimeMBean.class, var3, (String)var4);
         var1.put("WLDFAccessRuntime", var2);
         var2.setValue("description", "<p>The MBean that represents this server's view of its diagnostic accessor.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WLDFArchiveRuntimes")) {
         var3 = "getWLDFArchiveRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFArchiveRuntimes", WLDFRuntimeMBean.class, var3, (String)var4);
         var1.put("WLDFArchiveRuntimes", var2);
         var2.setValue("description", "<p>The MBeans that represent this server's view of its diagnostic archives.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WLDFHarvesterRuntime")) {
         var3 = "getWLDFHarvesterRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFHarvesterRuntime", WLDFRuntimeMBean.class, var3, (String)var4);
         var1.put("WLDFHarvesterRuntime", var2);
         var2.setValue("description", "<p>The MBean that represents this server's view of its diagnostic harvester.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WLDFImageRuntime")) {
         var3 = "getWLDFImageRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFImageRuntime", WLDFRuntimeMBean.class, var3, (String)var4);
         var1.put("WLDFImageRuntime", var2);
         var2.setValue("description", "<p>The MBean that represents this server's view of its diagnostic image source.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WLDFInstrumentationRuntimes")) {
         var3 = "getWLDFInstrumentationRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFInstrumentationRuntimes", WLDFRuntimeMBean.class, var3, (String)var4);
         var1.put("WLDFInstrumentationRuntimes", var2);
         var2.setValue("description", "<p>The MBeans that represent this server's view of its diagnostic server instrumentation.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("WLDFWatchNotificationRuntime")) {
         var3 = "getWLDFWatchNotificationRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFWatchNotificationRuntime", WLDFRuntimeMBean.class, var3, (String)var4);
         var1.put("WLDFWatchNotificationRuntime", var2);
         var2.setValue("description", "<p>The MBean that represents this server's view of its diagnostic Watch and Notification component.</p> ");
         var2.setValue("relationship", "reference");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFRuntimeMBean.class.getMethod("lookupWLDFInstrumentationRuntime", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Looks up the WLDFInstrumentationRuntimeMBean with the given name. ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WLDFInstrumentationRuntimes");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFRuntimeMBean.class.getMethod("preDeregister");
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
