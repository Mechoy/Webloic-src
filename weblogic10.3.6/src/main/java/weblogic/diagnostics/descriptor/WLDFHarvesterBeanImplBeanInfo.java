package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFHarvesterBeanImplBeanInfo extends WLDFBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFHarvesterBean.class;

   public WLDFHarvesterBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFHarvesterBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFHarvesterBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>Configures the behavior of the Harvester component of the WebLogic Diagnostic Framework (WLDF).</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFHarvesterBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("HarvestedTypes")) {
         var3 = "getHarvestedTypes";
         var4 = null;
         var2 = new PropertyDescriptor("HarvestedTypes", WLDFHarvesterBean.class, var3, var4);
         var1.put("HarvestedTypes", var2);
         var2.setValue("description", "<p>The list of MBeans representing the harvested types.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createHarvestedType");
         var2.setValue("destroyer", "destroyHarvestedType");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SamplePeriod")) {
         var3 = "getSamplePeriod";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSamplePeriod";
         }

         var2 = new PropertyDescriptor("SamplePeriod", WLDFHarvesterBean.class, var3, var4);
         var1.put("SamplePeriod", var2);
         var2.setValue("description", "<p>The interval, in milliseconds, between samples.</p> ");
         setPropertyDescriptorDefault(var2, new Long(300000L));
         var2.setValue("legalMin", new Long(1000L));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", WLDFHarvesterBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>Specifies whether the Harvester component is enabled.</p> <p>If <code>true</code>, all types that are both configured and enabled are harvested. If <code>false</code>, nothing is harvested.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFHarvesterBean.class.getMethod("createHarvestedType", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "the name of the entity being created ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates a harvested type.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "HarvestedTypes");
      }

      var3 = WLDFHarvesterBean.class.getMethod("destroyHarvestedType", WLDFHarvestedTypeBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("entry", "the entry to be deleted ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes an entry from the list of harvested types.</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "HarvestedTypes");
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
