package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFSystemResourceMBeanImplBeanInfo extends SystemResourceMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFSystemResourceMBean.class;

   public WLDFSystemResourceMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFSystemResourceMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFSystemResourceMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This bean defines a system-level WebLogic Diagnostic Framework (WLDF) resource.  It links a separate descriptor that specifies the definition.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WLDFSystemResourceMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Description")) {
         var3 = "getDescription";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDescription";
         }

         var2 = new PropertyDescriptor("Description", WLDFSystemResourceMBean.class, var3, var4);
         var1.put("Description", var2);
         var2.setValue("description", "<p>Optional short description of this WLDFSystemResource. If provided, the WebLogic Server Administration Console will display the resource description.</p> ");
         var2.setValue("defaultValueNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DescriptorFileName")) {
         var3 = "getDescriptorFileName";
         var4 = null;
         var2 = new PropertyDescriptor("DescriptorFileName", WLDFSystemResourceMBean.class, var3, var4);
         var1.put("DescriptorFileName", var2);
         var2.setValue("description", "<p>The name of the descriptor file that contains the XML configuration information for this system-level resource. The location of this file is a relative path rooted at <code><i>DOMAIN_DIR</i>/config</code>.  By default the file resides in the <code><i>DOMAIN_DIR</i>/config/diagnostics</code> directory, and derives its name from the bean name using the following pattern:</p>  <p><code><i><beanName></i>.xml</code></p>  <p>Note that the filename is a read-only property, which is set when the WLDF resource is created.</p> ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", WLDFSystemResourceMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "Unique identifier for this bean instance. ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("Resource")) {
         var3 = "getResource";
         var4 = null;
         var2 = new PropertyDescriptor("Resource", WLDFSystemResourceMBean.class, var3, var4);
         var1.put("Resource", var2);
         var2.setValue("description", "<p>Return the Descriptor for the system resource. This should be overridden by the derived system resources. ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("WLDFResource")) {
         var3 = "getWLDFResource";
         var4 = null;
         var2 = new PropertyDescriptor("WLDFResource", WLDFSystemResourceMBean.class, var3, var4);
         var1.put("WLDFResource", var2);
         var2.setValue("description", "<p>A WLDF resource defines a system-level or an application-level diagnostic resource.<p>  <p>A system-level WLDF resource is a diagnostic resource whose scope is system-wide; its descriptor file, by default, resides in the <code><i>DOMAIN_NAME</i>/config/diagnostics</code> directory.</p>  <p>An application-level WLDF resource is a diagnostic resource whose scope is application-wide; its descriptor file, <code>weblogic-diagnostics.xml</code> is contained within the application archive.</p>  <p>For more information, see \"Configuring and Using the WebLogic Diagnostics Framework\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("transient", Boolean.TRUE);
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
      Method var3 = WLDFSystemResourceMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFSystemResourceMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("role", "operation");
         var2.setValue("impact", "action");
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
