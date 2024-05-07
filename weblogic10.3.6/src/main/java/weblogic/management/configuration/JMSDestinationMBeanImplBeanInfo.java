package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class JMSDestinationMBeanImplBeanInfo extends JMSDestCommonMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = JMSDestinationMBean.class;

   public JMSDestinationMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public JMSDestinationMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = JMSDestinationMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.j2ee.descriptor.wl.DestinationBean} ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents a JMS destination, which identifies a queue (Point-To-Point) or a topic (Pub/Sub) for a JMS server.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.JMSDestinationMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("BytesPagingEnabled")) {
         var3 = "getBytesPagingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBytesPagingEnabled";
         }

         var2 = new PropertyDescriptor("BytesPagingEnabled", JMSDestinationMBean.class, var3, var4);
         var1.put("BytesPagingEnabled", var2);
         var2.setValue("description", "<p>This parameter has been deprecated. Paging is always enabled. The \"MessageBufferSize\" parameter on JMSServerMBean controls how much memory is used before paging kicks in.</p> ");
         setPropertyDescriptorDefault(var2, "default");
         var2.setValue("legalValues", new Object[]{"default", "false", "true"});
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.JMSServerMBean#MessageBufferSize} ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("JNDIName")) {
         var3 = "getJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIName";
         }

         var2 = new PropertyDescriptor("JNDIName", JMSDestinationMBean.class, var3, var4);
         var1.put("JNDIName", var2);
         var2.setValue("description", "<p>The JNDI name used to look up the destination within the JNDI namespace. If not specified, the destination name is not advertised through the JNDI namespace and cannot be looked up and used.</p>  <p><i>Note:</i> This attribute is not dynamically configurable.</p> ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("MessagesPagingEnabled")) {
         var3 = "getMessagesPagingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagesPagingEnabled";
         }

         var2 = new PropertyDescriptor("MessagesPagingEnabled", JMSDestinationMBean.class, var3, var4);
         var1.put("MessagesPagingEnabled", var2);
         var2.setValue("description", "<p>This parameter has been deprecated. Paging is always enabled. The \"MessageBufferSize\" parameter on JMSServerMBean controls how much memory is used before paging kicks in.</p> ");
         setPropertyDescriptorDefault(var2, "default");
         var2.setValue("legalValues", new Object[]{"default", "false", "true"});
         var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.configuration.JMSServerMBean#MessageBufferSize} ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("StoreEnabled")) {
         var3 = "getStoreEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStoreEnabled";
         }

         var2 = new PropertyDescriptor("StoreEnabled", JMSDestinationMBean.class, var3, var4);
         var1.put("StoreEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the destination supports persistent messaging by using the JMS store specified by the JMS server.</p>  <p>Supported values are:</p>  <ul> <li><b>default</b> <p>- The destination uses the JMS store defined for the JMS server, if one is defined, and supports persistent messaging. However, if a JMS store is not defined for the JMS server, then persistent messages are automatically downgraded to non-persistent.</p> </li>  <li><b>false</b> <p>- The destination does not support persistent messaging.</p> </li>  <li><b>true</b> <p>- The destination does support persistent messaging. However, if a JMS store is not defined for the JMS server, then the configuration will fail and the JMS server will not boot.</p> </li> </ul>  <p><i>Note:</i> This attribute is not dynamically configurable.</p> ");
         setPropertyDescriptorDefault(var2, "default");
         var2.setValue("legalValues", new Object[]{"default", "false", "true"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("Template")) {
         var3 = "getTemplate";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTemplate";
         }

         var2 = new PropertyDescriptor("Template", JMSDestinationMBean.class, var3, var4);
         var1.put("Template", var2);
         var2.setValue("description", "<p>The JMS template from which the destination is derived. If a JMS template is specified, destination attributes that are set to their default values will inherit their values from the JMS template at run time. However, if this attribute is not defined, then the attributes for the destination must be specified as part of the destination.</p>  <p><i>Note:</i> The Template attribute setting per destination is static. The JMS template's attributes, however, can be modified dynamically.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("JNDINameReplicated")) {
         var3 = "isJNDINameReplicated";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDINameReplicated";
         }

         var2 = new PropertyDescriptor("JNDINameReplicated", JMSDestinationMBean.class, var3, var4);
         var1.put("JNDINameReplicated", var2);
         var2.setValue("description", "<p>Indicates whether the JNDI name is replicated across the cluster. If JNDINameReplicated is set to true, then the JNDI name for the destination (if present) is replicated across the cluster. If JNDINameReplicated is set to false, then the JNDI name for the destination (if present) is only visible from the server of which this destination is a part.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("dynamic", Boolean.FALSE);
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
