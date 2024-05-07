package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFResourceBeanImplBeanInfo extends WLDFBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFResourceBean.class;

   public WLDFResourceBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFResourceBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFResourceBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>The top of the WebLogic Diagnostic Framework (WLDF) module bean tree.</p>  <p>All WLDF modules have a WLDFResourceBean as their root bean (a bean with no parent). The schema namespace that corresponds to this bean is: <code>http://www.bea.com/ns/weblogic/90</code>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFResourceBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("Harvester")) {
         var3 = "getHarvester";
         var4 = null;
         var2 = new PropertyDescriptor("Harvester", WLDFResourceBean.class, var3, (String)var4);
         var1.put("Harvester", var2);
         var2.setValue("description", "<p>The Diagnostic Harvester configuration for this deployment.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Instrumentation")) {
         var3 = "getInstrumentation";
         var4 = null;
         var2 = new PropertyDescriptor("Instrumentation", WLDFResourceBean.class, var3, (String)var4);
         var1.put("Instrumentation", var2);
         var2.setValue("description", "<p>The Diagnostic Instrumentation configuration for this deployment.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("WatchNotification")) {
         var3 = "getWatchNotification";
         var4 = null;
         var2 = new PropertyDescriptor("WatchNotification", WLDFResourceBean.class, var3, (String)var4);
         var1.put("WatchNotification", var2);
         var2.setValue("description", "<p>The Diagnostic Watch and Notification configuration for this deployment.</p> ");
         var2.setValue("relationship", "containment");
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
