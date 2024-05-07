package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class AdminConsoleMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = AdminConsoleMBean.class;

   public AdminConsoleMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public AdminConsoleMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = AdminConsoleMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("AdminConsoleMBean is a console specific MBean to configure weblogic administration console attributes. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.AdminConsoleMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CookieName")) {
         var3 = "getCookieName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCookieName";
         }

         var2 = new PropertyDescriptor("CookieName", AdminConsoleMBean.class, var3, var4);
         var1.put("CookieName", var2);
         var2.setValue("description", "<p>Returns the Cookie Name used by the Administration Console. </p> ");
         setPropertyDescriptorDefault(var2, "ADMINCONSOLESESSION");
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (!var1.containsKey("SessionTimeout")) {
         var3 = "getSessionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSessionTimeout";
         }

         var2 = new PropertyDescriptor("SessionTimeout", AdminConsoleMBean.class, var3, var4);
         var1.put("SessionTimeout", var2);
         var2.setValue("description", "<p>Returns Session Timeout value (in seconds) for Administration Console. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(3600));
         var2.setValue("configurable", Boolean.TRUE);
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
