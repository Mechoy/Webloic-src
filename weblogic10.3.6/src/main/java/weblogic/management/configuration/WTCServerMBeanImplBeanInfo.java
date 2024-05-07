package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WTCServerMBeanImplBeanInfo extends DeploymentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCServerMBean.class;

   public WTCServerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCServerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCServerMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean defines a WTC Server.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WTCServerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Exports")) {
         var3 = "getExports";
         var4 = null;
         var2 = new PropertyDescriptor("Exports", WTCServerMBean.class, var3, var4);
         var1.put("Exports", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("Imports")) {
         var3 = "getImports";
         var4 = null;
         var2 = new PropertyDescriptor("Imports", WTCServerMBean.class, var3, var4);
         var1.put("Imports", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("LocalTuxDoms")) {
         var3 = "getLocalTuxDoms";
         var4 = null;
         var2 = new PropertyDescriptor("LocalTuxDoms", WTCServerMBean.class, var3, var4);
         var1.put("LocalTuxDoms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", WTCServerMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("Passwords")) {
         var3 = "getPasswords";
         var4 = null;
         var2 = new PropertyDescriptor("Passwords", WTCServerMBean.class, var3, var4);
         var1.put("Passwords", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("RemoteTuxDoms")) {
         var3 = "getRemoteTuxDoms";
         var4 = null;
         var2 = new PropertyDescriptor("RemoteTuxDoms", WTCServerMBean.class, var3, var4);
         var1.put("RemoteTuxDoms", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      String[] var5;
      if (!var1.containsKey("Resource")) {
         var3 = "getResource";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setResource";
         }

         var2 = new PropertyDescriptor("Resource", WTCServerMBean.class, var3, var4);
         var1.put("Resource", var2);
         var2.setValue("description", "<p>Specifies global field table classes, view table classes, and application passwords for domains. Defines your Resources when configured using the Administration Console.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#createResource")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", " ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("Resources")) {
         var3 = "getResources";
         var4 = null;
         var2 = new PropertyDescriptor("Resources", WTCServerMBean.class, var3, var4);
         var1.put("Resources", var2);
         var2.setValue("description", " ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("WTCExports")) {
         var3 = "getWTCExports";
         var4 = null;
         var2 = new PropertyDescriptor("WTCExports", WTCServerMBean.class, var3, var4);
         var1.put("WTCExports", var2);
         var2.setValue("description", "<p>Provides information on services exported by a local Tuxedo access point. Defines your Exported Services when configured using the Administration Console.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWTCExport");
         var2.setValue("creator", "createWTCExport");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WTCImports")) {
         var3 = "getWTCImports";
         var4 = null;
         var2 = new PropertyDescriptor("WTCImports", WTCServerMBean.class, var3, var4);
         var1.put("WTCImports", var2);
         var2.setValue("description", "<p>Provides information on services imported and available on remote domains. Defines your Imported Services when configured using the Administration Console.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWTCImport");
         var2.setValue("creator", "createWTCImport");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WTCLocalTuxDoms")) {
         var3 = "getWTCLocalTuxDoms";
         var4 = null;
         var2 = new PropertyDescriptor("WTCLocalTuxDoms", WTCServerMBean.class, var3, var4);
         var1.put("WTCLocalTuxDoms", var2);
         var2.setValue("description", "<p>The local Tuxedo domains defined for this WTC Server.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWTCLocalTuxDom");
         var2.setValue("creator", "createWTCLocalTuxDom");
      }

      if (!var1.containsKey("WTCPasswords")) {
         var3 = "getWTCPasswords";
         var4 = null;
         var2 = new PropertyDescriptor("WTCPasswords", WTCServerMBean.class, var3, var4);
         var1.put("WTCPasswords", var2);
         var2.setValue("description", "<p>Specifies the configuration information for inter-domain authentication. Defines your Passwords when configured using the Administration Console.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWTCPassword");
         var2.setValue("creator", "createWTCPassword");
      }

      if (!var1.containsKey("WTCRemoteTuxDoms")) {
         var3 = "getWTCRemoteTuxDoms";
         var4 = null;
         var2 = new PropertyDescriptor("WTCRemoteTuxDoms", WTCServerMBean.class, var3, var4);
         var1.put("WTCRemoteTuxDoms", var2);
         var2.setValue("description", "<p>The remote Tuxedo domains defined for this WTC Server.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWTCRemoteTuxDom");
         var2.setValue("destroyer", "destroyWTCRemoteTuxDom");
      }

      if (!var1.containsKey("WTCResources")) {
         var3 = "getWTCResources";
         var4 = null;
         var2 = new PropertyDescriptor("WTCResources", WTCServerMBean.class, var3, var4);
         var1.put("WTCResources", var2);
         var2.setValue("description", "<p>Specifies global field table classes, view table classes, and application passwords for domains. Defines your Resources when configured using the Administration Console.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#createWTCResources")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyWTCResources");
         var2.setValue("creator", "createWTCResources");
      }

      if (!var1.containsKey("WTCtBridgeGlobal")) {
         var3 = "getWTCtBridgeGlobal";
         var4 = null;
         var2 = new PropertyDescriptor("WTCtBridgeGlobal", WTCServerMBean.class, var3, var4);
         var1.put("WTCtBridgeGlobal", var2);
         var2.setValue("description", "<p>Specifies global configuration information for the transfer of messages between WebLogic Server and Tuxedo. Defines your Tuxedo Queuing Bridge when configured using the Administration Console.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#createWTCtBridgeGlobal")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWTCtBridgeGlobal");
         var2.setValue("destroyer", "destroyWTCtBridgeGlobal");
      }

      if (!var1.containsKey("WTCtBridgeRedirects")) {
         var3 = "getWTCtBridgeRedirects";
         var4 = null;
         var2 = new PropertyDescriptor("WTCtBridgeRedirects", WTCServerMBean.class, var3, var4);
         var1.put("WTCtBridgeRedirects", var2);
         var2.setValue("description", "gets all WTCtBridgeRedirect objects ");
         var2.setValue("relationship", "containment");
         var2.setValue("creator", "createWTCtBridgeRedirect");
         var2.setValue("destroyer", "destroyWTCtBridgeRedirect");
      }

      if (!var1.containsKey("tBridgeGlobal")) {
         var3 = "gettBridgeGlobal";
         var4 = null;
         if (!this.readOnly) {
            var4 = "settBridgeGlobal";
         }

         var2 = new PropertyDescriptor("tBridgeGlobal", WTCServerMBean.class, var3, var4);
         var1.put("tBridgeGlobal", var2);
         var2.setValue("description", "<p>Specifies global configuration information for the transfer of messages between WebLogic Server and Tuxedo. Defines your Tuxedo Queuing Bridge when configured using the Administration Console.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#createtBridgeGlobal")};
         var2.setValue("see", var5);
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", " ");
         var2.setValue("transient", Boolean.TRUE);
      }

      if (!var1.containsKey("tBridgeRedirects")) {
         var3 = "gettBridgeRedirects";
         var4 = null;
         var2 = new PropertyDescriptor("tBridgeRedirects", WTCServerMBean.class, var3, var4);
         var1.put("tBridgeRedirects", var2);
         var2.setValue("description", "<p>Specifies the source, target, direction, and transport of messages between WebLogic Server and Tuxedo. Defines your Tuxedo Queuing Bridge Redirects when configured using the Administration Console.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("transient", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WTCServerMBean.class.getMethod("createWTCLocalTuxDom", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create a WTCLocalTuxDomMBean object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCLocalTuxDoms");
      }

      var3 = WTCServerMBean.class.getMethod("destroyWTCLocalTuxDom", WTCLocalTuxDomMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("locTuxDomName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a WTCLocalTuxDomMBean from this WTCServer</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCLocalTuxDoms");
      }

      var3 = WTCServerMBean.class.getMethod("createWTCRemoteTuxDom", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create a WTCRemoteTuxDomMBean object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCRemoteTuxDoms");
      }

      var3 = WTCServerMBean.class.getMethod("destroyWTCRemoteTuxDom", WTCRemoteTuxDomMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("remTuxDomName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a WTCRemoteTuxDomMBean from this WTCServer</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCRemoteTuxDoms");
      }

      var3 = WTCServerMBean.class.getMethod("createWTCExport", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create a WTCExportMBean object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCExports");
      }

      var3 = WTCServerMBean.class.getMethod("destroyWTCExport", WTCExportMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("expName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a WTCExportMBean from this WTCServer</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCExports");
      }

      var3 = WTCServerMBean.class.getMethod("createWTCImport", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create a WTCImportMBean object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCImports");
      }

      var3 = WTCServerMBean.class.getMethod("destroyWTCImport", WTCImportMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("impName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a WTCImportMBean from this WTCServer</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCImports");
      }

      var3 = WTCServerMBean.class.getMethod("createWTCPassword", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create a WTCPasswordMBean object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCPasswords");
      }

      var3 = WTCServerMBean.class.getMethod("destroyWTCPassword", WTCPasswordMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("passwdName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a WTCPasswordMBean from this WTCServer</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCPasswords");
      }

      var3 = WTCServerMBean.class.getMethod("createWTCResources", String.class);
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      String[] var8;
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var8 = new String[]{BeanInfoHelper.encodeEntities("InstanceAlreadyExistsException if Resource exists")};
         var2.setValue("throws", var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Factory method to create a WTCResourcesMBean object</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#destroyWTCResources")};
         var2.setValue("see", var6);
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCResources");
      }

      var3 = WTCServerMBean.class.getMethod("destroyWTCResources", WTCResourcesMBean.class);
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Removes this WTCResourcesMBean from this WTCServer</p> ");
         var8 = new String[]{BeanInfoHelper.encodeEntities("#destroys WTCResource object")};
         var2.setValue("see", var8);
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCResources");
      }

      var3 = WTCServerMBean.class.getMethod("createWTCtBridgeGlobal");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var8 = new String[]{BeanInfoHelper.encodeEntities("InstanceAlreadyExistsException if tBridgeGlobal exists")};
         var2.setValue("throws", var8);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Factory method to create a WTCtBridgeGlobalMBean object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCtBridgeGlobal");
      }

      var3 = WTCServerMBean.class.getMethod("destroyWTCtBridgeGlobal");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Removes this WTCtBridgeGlobalMBean from this WTCServer</p> ");
         var8 = new String[]{BeanInfoHelper.encodeEntities("destorys WTCtBridgeGlobal object")};
         var2.setValue("see", var8);
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCtBridgeGlobal");
      }

      var3 = WTCServerMBean.class.getMethod("createWTCtBridgeRedirect", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Factory method to create a WTCtBridgeRedirectMBean object</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCtBridgeRedirects");
      }

      var3 = WTCServerMBean.class.getMethod("destroyWTCtBridgeRedirect", WTCtBridgeRedirectMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("tBredirect", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a WTCtBridgeRedirectMBean from this WTCServer</p> ");
         var2.setValue("role", "factory");
         var2.setValue("property", "WTCtBridgeRedirects");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WTCServerMBean.class.getMethod("lookupWTCLocalTuxDom", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WTCLocalTuxDoms");
      }

      var3 = WTCServerMBean.class.getMethod("lookupWTCRemoteTuxDom", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WTCRemoteTuxDoms");
      }

      var3 = WTCServerMBean.class.getMethod("lookupWTCExport", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WTCExports");
      }

      var3 = WTCServerMBean.class.getMethod("lookupWTCImport", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WTCImports");
      }

      var3 = WTCServerMBean.class.getMethod("lookupWTCPassword", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WTCPasswords");
      }

      var3 = WTCServerMBean.class.getMethod("lookupWTCtBridgeRedirect", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WTCtBridgeRedirects");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WTCServerMBean.class.getMethod("freezeCurrentValue", String.class);
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

      var3 = WTCServerMBean.class.getMethod("restoreDefaultValue", String.class);
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
