package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSDistributedDestinationMemberMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSDistributedDestinationMemberMBean.class;

   public JMSDistributedDestinationMemberMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSDistributedDestinationMemberMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSDistributedDestinationMemberMBeanImpl.class;
      } catch (Throwable var6) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean} ");
      String[] var3 = new String[]{BeanInfoHelper.encodeEntities("weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean")};
      var2.setValue("see", var3);
      var2.setValue("package", "weblogic.management.configuration");
      String var4 = (new String("This class represents a JMS distributed destination member, which represents a physical JMS destination (topic or queue) as a member of a single distributed set of destinations that can be served by multiple WebLogic Server instances within a cluster.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var4);
      var2.setValue("description", var4);
      String[] var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var5);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSDistributedDestinationMemberMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      if (!var1.containsKey("Weight")) {
         String var3 = "getWeight";
         String var4 = null;
         if (!this.readOnly) {
            var4 = "setWeight";
         }

         var2 = new PropertyDescriptor("Weight", JMSDistributedDestinationMemberMBean.class, var3, var4);
         var1.put("Weight", var2);
         var2.setValue("description", "<p>The weight of a distributed destination member is a measure of its ability to handle message load, with respect to the other member destinations in the same distributed set.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMin", new Integer(1));
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
