package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFInstrumentationBeanImplBeanInfo extends WLDFBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFInstrumentationBean.class;

   public WLDFInstrumentationBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFInstrumentationBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFInstrumentationBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>Use this interface to configure server-scope and application-scope instrumentation for diagnostic monitors that will execute diagnostic code at selected locations in server or application code.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFInstrumentationBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      String[] var5;
      if (!var1.containsKey("Excludes")) {
         var3 = "getExcludes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExcludes";
         }

         var2 = new PropertyDescriptor("Excludes", WLDFInstrumentationBean.class, var3, var4);
         var1.put("Excludes", var2);
         var2.setValue("description", "<p>Pattern expressions for classes that will be excluded from this instrumentation scope. If specified, classes matching given patterns will not be instrumented.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getIncludes")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Includes")) {
         var3 = "getIncludes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIncludes";
         }

         var2 = new PropertyDescriptor("Includes", WLDFInstrumentationBean.class, var3, var4);
         var1.put("Includes", var2);
         var2.setValue("description", "<p>Pattern expressions for classes that are included in this instrumentation scope. If specified, only included classes will be instrumented. If not specified, all classes loaded within the application and which are not explicitly excluded are eligible for instrumentation.</p> <p>A pattern can end with an asterisk (<code>*</code>), in which case it will match with all classes whose fully qualified classname starts with the prefix of the pattern. For example, <code>weblogic.rmi.*</code> will match with all classes in <code>weblogic.rmi</code> and its subpackages.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getExcludes")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WLDFInstrumentationMonitors")) {
         var3 = "getWLDFInstrumentationMonitors";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFInstrumentationMonitors", WLDFInstrumentationBean.class, var3, var4);
         var1.put("WLDFInstrumentationMonitors", var2);
         var2.setValue("description", "<p>The diagnostic monitors defined in this instrumentation scope.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWLDFInstrumentationMonitor");
         var2.setValue("destroyer", "destroyWLDFInstrumentationMonitor");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", WLDFInstrumentationBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>The state of the instrumentation behavior for the server or application. If <code>false</code>, there will no weaving (inserting of diagnostic code) in the application or server code during class loading. In addition, if woven classes are already loaded, disabling instrumentation will disable all the monitors in this scope.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFInstrumentationBean.class.getMethod("createWLDFInstrumentationMonitor", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "A unique name to identify the monitor in this scope. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds the specified diagnostic monitor to this instrumentation scope, which could be server or application scope.</p> ");
         var6 = new String[]{BeanInfoHelper.encodeEntities("#getWLDFInstrumentationMonitors")};
         var2.setValue("see", var6);
         var2.setValue("role", "factory");
         var2.setValue("property", "WLDFInstrumentationMonitors");
      }

      var3 = WLDFInstrumentationBean.class.getMethod("destroyWLDFInstrumentationMonitor", WLDFInstrumentationMonitorBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("monitor", "The name of the monitor to remove ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("MonitorNotFoundException The named monitor does not exist in the scope of this instrumentation manager.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes the specified diagnostic monitor from this instrumentation scope.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WLDFInstrumentationMonitors");
      }

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
