package weblogic.diagnostics.image;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WLDFImageRuntimeMBean;

public class ImageRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFImageRuntimeMBean.class;

   public ImageRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ImageRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ImageRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.image");
      String var3 = (new String("<p>This interface controls diagnostic image creation, and provides access to run-time information about past and current diagnostic image capture requests.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WLDFImageRuntimeMBean");
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
      Method var3 = WLDFImageRuntimeMBean.class.getMethod("captureImage");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Creates a diagnostic image in the configured destination directory.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFImageRuntimeMBean.class.getMethod("preDeregister");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFImageRuntimeMBean.class.getMethod("captureImage", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("destination", "absolute or relative directory path ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates a diagnostic image in the specified destination directory, which can be specified either as a relative or absolute pathname. If relative, the path is relative to the server's <code>logs</code> directory.</p>  <p>If the directory does not exist, it is created. If the directory exists, it must be writable in order for image creation to proceed.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFImageRuntimeMBean.class.getMethod("captureImage", Integer.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("lockoutMinutes", "number of minutes before the next image capture request will be accepted ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates a diagnostic image in the configured destination directory.</p>  <p>No additional image capture requests will be accepted until the specified lockout period has expired.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFImageRuntimeMBean.class.getMethod("captureImage", String.class, Integer.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("destination", "absolute or relative path "), createParameterDescriptor("lockoutMinutes", "number of minutes before next image capture request will be accepted ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Creates a diagnostic image in the specified destination directory, which can be specified either as a relative or absolute pathname. If relative, the path is relative to the server's <code>logs</code> directory.</p>  <p>If the directory does not exist, it is created.  If the directory exists, it must be writable in order for image creation to proceed.</p>  <p>No additional image capture requests will be accepted until the specified lockout period has expired.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFImageRuntimeMBean.class.getMethod("listImageCaptureTasks");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>The list of all initiated image capture tasks.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFImageRuntimeMBean.class.getMethod("clearCompletedImageCaptureTasks");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Removes all completed image capture tasks.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = WLDFImageRuntimeMBean.class.getMethod("resetImageLockout");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Reset the lockout period, thus allowing image capture requests to be accepted.</p> ");
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
