package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFImageNotificationBeanImplBeanInfo extends WLDFNotificationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFImageNotificationBean.class;

   public WLDFImageNotificationBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFImageNotificationBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFImageNotificationBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>Use this interface to configure an image notification, which will be sent when a diagnostic watch evaluates to <code>true</code>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFImageNotificationBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ImageDirectory")) {
         var3 = "getImageDirectory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setImageDirectory";
         }

         var2 = new PropertyDescriptor("ImageDirectory", WLDFImageNotificationBean.class, var3, var4);
         var1.put("ImageDirectory", var2);
         var2.setValue("description", "<p>The directory where diagnostic images are stored. The default directory, relative to a server's root directory, is <code>./logs/diagnostic_images</code>.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ImageLockout")) {
         var3 = "getImageLockout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setImageLockout";
         }

         var2 = new PropertyDescriptor("ImageLockout", WLDFImageNotificationBean.class, var3, var4);
         var1.put("ImageLockout", var2);
         var2.setValue("description", "<p>The length of time, in minutes, during which no diagnostic images requests will be accepted; that is, the minimum amount of time between image capture requests.</p> ");
         var2.setValue("legalMin", new Integer(0));
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
