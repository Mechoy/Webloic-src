package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class FileRealmMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = FileRealmMBean.class;

   public FileRealmMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public FileRealmMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = FileRealmMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "7.0.0.0 ");
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean holds the configuration properties for the File realm. This MBean is associated with a RealmMBean.  Deprecated in WebLogic Server version 7.0. Replaced by the new Security architecture that includes Authentication, Authorization, and Auditing providers. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.FileRealmMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("MaxACLs")) {
         var3 = "getMaxACLs";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxACLs";
         }

         var2 = new PropertyDescriptor("MaxACLs", FileRealmMBean.class, var3, var4);
         var1.put("MaxACLs", var2);
         var2.setValue("description", "<p>The maximum number of positive access control lists (ACLs) supported by the File realm. A warning is issued when this number is reached.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1000));
         var2.setValue("legalMax", new Integer(10000));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxGroups")) {
         var3 = "getMaxGroups";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxGroups";
         }

         var2 = new PropertyDescriptor("MaxGroups", FileRealmMBean.class, var3, var4);
         var1.put("MaxGroups", var2);
         var2.setValue("description", "<p>The maximum number of groups supported by the File realm.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1000));
         var2.setValue("legalMax", new Integer(10000));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxUsers")) {
         var3 = "getMaxUsers";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxUsers";
         }

         var2 = new PropertyDescriptor("MaxUsers", FileRealmMBean.class, var3, var4);
         var1.put("MaxUsers", var2);
         var2.setValue("description", "<p>The maximum number of users supported by the File realm.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1000));
         var2.setValue("legalMax", new Integer(10000));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.FALSE);
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
