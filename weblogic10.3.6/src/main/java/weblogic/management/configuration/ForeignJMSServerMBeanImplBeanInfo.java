package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class ForeignJMSServerMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ForeignJMSServerMBean.class;

   public ForeignJMSServerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ForeignJMSServerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ForeignJMSServerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("obsolete", "9.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by the ForeignServerBean type in the new JMS module. ");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This class represents a JNDI provider that is outside the WebLogic JMS server. It is a parent element of the ForeignJMSConnectionFactory and ForeignJMSDestination MBeans. It contains information that allows WebLogic Server to reach the remote JNDI provider. This way, a number of connection factory and destination objects can be defined on one JNDI directory. <p/> <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3> <p/> <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.ForeignJMSServerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ConnectionFactories")) {
         var3 = "getConnectionFactories";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionFactories";
         }

         var2 = new PropertyDescriptor("ConnectionFactories", ForeignJMSServerMBean.class, var3, var4);
         var1.put("ConnectionFactories", var2);
         var2.setValue("description", "<p>The remote connection factories.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addConnectionFactory");
         var2.setValue("remover", "removeConnectionFactory");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("ConnectionURL")) {
         var3 = "getConnectionURL";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectionURL";
         }

         var2 = new PropertyDescriptor("ConnectionURL", ForeignJMSServerMBean.class, var3, var4);
         var1.put("ConnectionURL", var2);
         var2.setValue("description", "<p>The URL that WebLogic Server will use to contact the JNDI provider. The syntax of this URL depends on which JNDI provider is being used. For WebLogic JMS, leave this field blank if you are referencing WebLogic JMS objects within the same cluster. This value corresponds to the standard JNDI property, <tt>java.naming.provider.url</tt>.</p> <p/> <p><i>Note:</i> If this value is not specified, look-ups will be performed on the JNDI server within the WebLogic Server instance where this connection factory is deployed.</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Destinations")) {
         var3 = "getDestinations";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDestinations";
         }

         var2 = new PropertyDescriptor("Destinations", ForeignJMSServerMBean.class, var3, var4);
         var1.put("Destinations", var2);
         var2.setValue("description", "<p>The remote destinations.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addDestination");
         var2.setValue("remover", "removeDestination");
         var2.setValue("deprecated", "9.0.0.0 Replaced by the ForeignServerBean type in the new JMS module. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("ForeignJMSConnectionFactories")) {
         var3 = "getForeignJMSConnectionFactories";
         var4 = null;
         var2 = new PropertyDescriptor("ForeignJMSConnectionFactories", ForeignJMSServerMBean.class, var3, var4);
         var1.put("ForeignJMSConnectionFactories", var2);
         var2.setValue("description", "Get all the Members ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createForeignJMSConnectionFactory");
         var2.setValue("destroyer", "destroyForeignJMSConnectionFactory");
         var2.setValue("creator", "createForeignJMSConnectionFactory");
         var2.setValue("deprecated", "9.0.0.0 Replaced by the ForeignServerBean type in the new JMS module. ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("ForeignJMSDestinations")) {
         var3 = "getForeignJMSDestinations";
         var4 = null;
         var2 = new PropertyDescriptor("ForeignJMSDestinations", ForeignJMSServerMBean.class, var3, var4);
         var1.put("ForeignJMSDestinations", var2);
         var2.setValue("description", "Get all the Members ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyForeignJMSDestination");
         var2.setValue("creator", "createForeignJMSDestination");
         var2.setValue("creator", "createForeignJMSDestination");
         var2.setValue("deprecated", "9.0.0.0 Replaced by the ForeignServerBean type in the new JMS module. ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("InitialContextFactory")) {
         var3 = "getInitialContextFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInitialContextFactory";
         }

         var2 = new PropertyDescriptor("InitialContextFactory", ForeignJMSServerMBean.class, var3, var4);
         var1.put("InitialContextFactory", var2);
         var2.setValue("description", "<p>The name of the class that must be instantiated to access the JNDI provider. This class name depends on the JNDI provider and the vendor that are being used. This value corresponds to the standard JNDI property, <tt>java.naming.factory.initial</tt>.</p> <p/> <p><i>Note:</i> This value defaults to <tt>weblogic.jndi.WLInitialContextFactory</tt>, which is the correct value for WebLogic Server.</p> ");
         setPropertyDescriptorDefault(var2, "weblogic.jndi.WLInitialContextFactory");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JNDIProperties")) {
         var3 = "getJNDIProperties";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIProperties";
         }

         var2 = new PropertyDescriptor("JNDIProperties", ForeignJMSServerMBean.class, var3, var4);
         var1.put("JNDIProperties", var2);
         var2.setValue("description", "<p>Any additional properties that must be set for the JNDI provider. These properties will be passed directly to the constructor for the JNDI provider's <tt>InitialContext</tt> class.</p> <p/> <p><i>Note:</i> This value must be filled in using a <tt>name=value&lt;return&gt;name=value</tt> format.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("JNDIPropertiesCredential")) {
         var3 = "getJNDIPropertiesCredential";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIPropertiesCredential";
         }

         var2 = new PropertyDescriptor("JNDIPropertiesCredential", ForeignJMSServerMBean.class, var3, var4);
         var1.put("JNDIPropertiesCredential", var2);
         var2.setValue("description", "<p>The encrypted value of the value set via java.naming.security.credentials property of the JNDIProperties attribute. set via <code> setJNDIPropertiesCredential </code>, ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.EncryptionHelper")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("JNDIPropertiesCredentialEncrypted")) {
         var3 = "getJNDIPropertiesCredentialEncrypted";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJNDIPropertiesCredentialEncrypted";
         }

         var2 = new PropertyDescriptor("JNDIPropertiesCredentialEncrypted", ForeignJMSServerMBean.class, var3, var4);
         var1.put("JNDIPropertiesCredentialEncrypted", var2);
         var2.setValue("description", "<p>The encrypted value of the value set via java.naming.security.credentials property of the JNDIProperties attribute. set via <code> setJNDIPropertiesCredentialEncrypted </code>, ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.EncryptionHelper")};
         var2.setValue("see", var5);
         var2.setValue("encrypted", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", ForeignJMSServerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", ForeignJMSServerMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ForeignJMSServerMBean.class.getMethod("createForeignJMSConnectionFactory", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Create a new diagnostic deployment that can be targeted to a server</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSConnectionFactories");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("destroyForeignJMSConnectionFactory", ForeignJMSConnectionFactoryMBean.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Delete a diagnostic deployment configuration from the domain.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSConnectionFactories");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("createForeignJMSConnectionFactory", String.class, ForeignJMSConnectionFactoryMBean.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSConnectionFactories");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("createForeignJMSDestination", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Create a new diagnostic deployment that can be targeted to a server</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSDestinations");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("createForeignJMSDestination", String.class, ForeignJMSDestinationMBean.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSDestinations");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("destroyForeignJMSDestination", ForeignJMSDestinationMBean.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Delete a diagnostic deployment configuration from the domain.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ForeignJMSDestinations");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ForeignJMSServerMBean.class.getMethod("addDestination", ForeignJMSDestinationMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("destination", "The feature to be added to the Destination attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by the ForeignServerBean type in the new JMS module. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a destination.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Destinations");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("addTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the Target attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>You can add a target to specify additional servers on which the deployment can be deployed. The targets must be either clusters or servers.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("removeDestination", ForeignJMSDestinationMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("destination", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by the ForeignServerBean type in the new JMS module. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a destination.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Destinations");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes the value of the addTarget attribute.</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#addTarget")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("addConnectionFactory", ForeignJMSConnectionFactoryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("factory", "The feature to be added to the ConnectionFactory attribute ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by the ForeignServerBean type in the new JMS module. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a destination.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "ConnectionFactories");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("removeConnectionFactory", ForeignJMSConnectionFactoryMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("factory", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 Replaced by the ForeignServerBean type in the new JMS module. ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a destination.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "ConnectionFactories");
      }

   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ForeignJMSServerMBean.class.getMethod("lookupForeignJMSConnectionFactory", String.class);
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "ForeignJMSConnectionFactories");
      }

      var3 = ForeignJMSServerMBean.class.getMethod("lookupForeignJMSDestination", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "finder");
         var2.setValue("property", "ForeignJMSDestinations");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ForeignJMSServerMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = ForeignJMSServerMBean.class.getMethod("restoreDefaultValue", String.class);
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
