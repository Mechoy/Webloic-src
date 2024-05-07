package weblogic.diagnostics.debug;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoImpl;

public class DebugScopeBeanImplBeanInfo extends BeanInfoImpl {
   public static Class INTERFACE_CLASS = DebugScopeBean.class;

   public DebugScopeBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DebugScopeBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DebugScopeBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.diagnostics.debug");
      String var3 = (new String("This interface captures whether the enabled state of a debug scope. The name of a debug scope is the fully qualified scope name for example weblogic.security.SSL ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.debug.DebugScopeBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", DebugScopeBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The name of this configuration. WebLogic Server uses an MBean to implement and persist the configuration.</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", DebugScopeBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "Gets the enabled state of the debug scope mbean. By default a debug scope is disabled. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
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
