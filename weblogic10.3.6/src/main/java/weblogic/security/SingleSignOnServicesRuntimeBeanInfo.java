package weblogic.security;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.SingleSignOnServicesRuntimeMBean;

public class SingleSignOnServicesRuntimeBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SingleSignOnServicesRuntimeMBean.class;

   public SingleSignOnServicesRuntimeBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SingleSignOnServicesRuntimeBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SingleSignOnServicesRuntime.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.5.0.0");
      var2.setValue("package", "weblogic.security");
      String var3 = (new String("<p>This interface is used to publish SAML 2.0 local site meta-data for single sign-on services.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.SingleSignOnServicesRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      Object var2 = null;
      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SingleSignOnServicesRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = SingleSignOnServicesRuntimeMBean.class.getMethod("publish", String.class);
      ParameterDescriptor[] var7 = new ParameterDescriptor[]{createParameterDescriptor("fileName", "Name of file to create; will silently overwrite existing file, if present ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("InvalidParameterException thrown if a file with the specified name cannot be created")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Publishes SAML 2.0 meta-data for Web Browser SSO Profile ");
         var2.setValue("role", "operation");
      }

      var3 = SingleSignOnServicesRuntimeMBean.class.getMethod("publish", String.class, Boolean.TYPE);
      var7 = new ParameterDescriptor[]{createParameterDescriptor("fileName", "Name of file to create "), createParameterDescriptor("prohibitOverwirte", "If true, prohibits overwrite if the file already exists ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var7);
         var6 = new String[]{BeanInfoHelper.encodeEntities("InvalidParameterException fileName parameter is null or empty, or the file cannot be written"), BeanInfoHelper.encodeEntities("CreateException if the metadata can't be generated"), BeanInfoHelper.encodeEntities("AlreadyExistsException if the file already exists and prohibitOverwrite is true")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Publishes SAML 2.0 meta-data for Web Browser SSO Profile ");
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
