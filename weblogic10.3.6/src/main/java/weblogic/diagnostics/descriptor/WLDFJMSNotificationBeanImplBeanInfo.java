package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFJMSNotificationBeanImplBeanInfo extends WLDFNotificationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFJMSNotificationBean.class;

   public WLDFJMSNotificationBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFJMSNotificationBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFJMSNotificationBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>Use this interface to define a JMS notification, which is sent when a diagnostic watch evaluates to <code>true</code>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFJMSNotificationBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ConnectionFactoryJNDIName")) {
         var3 = "getConnectionFactoryJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionFactoryJNDIName";
         }

         var2 = new PropertyDescriptor("ConnectionFactoryJNDIName", WLDFJMSNotificationBean.class, var3, var4);
         var1.put("ConnectionFactoryJNDIName", var2);
         var2.setValue("description", "<p>The JNDI name of the JMS connection factory. If a name has not been specified, the default JMS connection factory is used.</p> ");
         setPropertyDescriptorDefault(var2, "weblogic.jms.ConnectionFactory");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DestinationJNDIName")) {
         var3 = "getDestinationJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDestinationJNDIName";
         }

         var2 = new PropertyDescriptor("DestinationJNDIName", WLDFJMSNotificationBean.class, var3, var4);
         var1.put("DestinationJNDIName", var2);
         var2.setValue("description", "<p>The JNDI name of the JMS destination.</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
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
