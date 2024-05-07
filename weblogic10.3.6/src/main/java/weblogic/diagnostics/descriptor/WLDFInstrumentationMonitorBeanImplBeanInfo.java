package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFInstrumentationMonitorBeanImplBeanInfo extends WLDFBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFInstrumentationMonitorBean.class;

   public WLDFInstrumentationMonitorBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFInstrumentationMonitorBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFInstrumentationMonitorBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>This interface defines a diagnostic monitor, which is applied at the specified locations within the included classes in an instrumentation scope.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFInstrumentationMonitorBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Actions")) {
         var3 = "getActions";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setActions";
         }

         var2 = new PropertyDescriptor("Actions", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("Actions", var2);
         var2.setValue("description", "<p>The diagnostic actions attached to this monitor. Actions are relevant only for delegating and custom monitors. Valid actions are: <code>TraceAction</code>, <code>DisplayArgumentsAction</code>, <code>MethodInvocationStatisticsAction</code>, <code>MethodMemoryAllocationStatisticsAction</code>, <code>StackDumpAction</code>, <code>ThreadDumpAction</code>, <code>TraceElapsedTimeAction</code>, and <code>TraceMemoryAllocationAction</code>.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDescription";
         }

         var2 = new PropertyDescriptor("Description", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("Description", var2);
         var2.setValue("description", "<p>Optional description of this monitor.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DyeMask")) {
         var3 = "getDyeMask";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDyeMask";
         }

         var2 = new PropertyDescriptor("DyeMask", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("DyeMask", var2);
         var2.setValue("description", "<p>The dye mask for all diagnostic actions associated with this monitor.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("Excludes")) {
         var3 = "getExcludes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setExcludes";
         }

         var2 = new PropertyDescriptor("Excludes", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("Excludes", var2);
         var2.setValue("description", "<p>Pattern expressions for classes that will be excluded for this instrumentation monitor. If specified, classes matching given patterns will not be instrumented with this monitor.</p> ");
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

         var2 = new PropertyDescriptor("Includes", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("Includes", var2);
         var2.setValue("description", "<p>Pattern expressions for classes that are included for this instrumentation monitor. If specified, only included classes will be instrumented with this monitor. If not specified, all classes loaded within the application and which are not explicitly excluded are eligible for instrumentation with this monitor.</p> <p>A pattern can end with an asterisk (<code>*</code>), in which case it will match with all classes whose fully qualified classname starts with the prefix of the pattern. For example, <code>weblogic.rmi.*</code> will match with all classes in <code>weblogic.rmi</code> and its subpackages.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getExcludes")};
         var2.setValue("see", var5);
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LocationType")) {
         var3 = "getLocationType";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLocationType";
         }

         var2 = new PropertyDescriptor("LocationType", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("LocationType", var2);
         var2.setValue("description", "<p>Attached actions are applied at selected locations: <code>before</code>, <code>after</code>, or <code>around</code> pointcuts. This is relevant only for custom monitors. (A location where diagnostic code is added is called a diagnostic joinpoint. A set of joinpoints, identified by an expression, is called a pointcut.)</p> <p>Once a location type is set, it cannot be changed.</p> ");
         setPropertyDescriptorDefault(var2, "before");
         var2.setValue("legalValues", new Object[]{"before", "after", "around"});
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("Pointcut")) {
         var3 = "getPointcut";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPointcut";
         }

         var2 = new PropertyDescriptor("Pointcut", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("Pointcut", var2);
         var2.setValue("description", "<p>The pointcut expression for this monitor. (A location where diagnostic code is added is called a diagnostic joinpoint. A set of joinpoints, identified by an expression, is called a pointcut.)</p> <p>Setting a pointcut expression is relevant only for custom monitors; for standard and delegating monitors, this definition is implicitly defined by WLDF.<p> <p>Once a pointcut expression is set, it cannot be changed.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("Properties")) {
         var3 = "getProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setProperties";
         }

         var2 = new PropertyDescriptor("Properties", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("Properties", var2);
         var2.setValue("description", "<p>Properties for this monitor. Properties are name=value pairs, one pair per line. For example, <code>USER1=foo\nADDR1=127.0.0.1</code>.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DyeFilteringEnabled")) {
         var3 = "isDyeFilteringEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDyeFilteringEnabled";
         }

         var2 = new PropertyDescriptor("DyeFilteringEnabled", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("DyeFilteringEnabled", var2);
         var2.setValue("description", "<p>Specifies whether dye filtering is enabled for the diagnostic actions associated with this monitor.</p> ");
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", WLDFInstrumentationMonitorBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>Specifies whether the monitor and its associated diagnostics actions are enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
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
