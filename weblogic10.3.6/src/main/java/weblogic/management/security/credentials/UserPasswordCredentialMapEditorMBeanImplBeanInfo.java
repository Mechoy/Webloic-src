package weblogic.management.security.credentials;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class UserPasswordCredentialMapEditorMBeanImplBeanInfo extends UserPasswordCredentialMapReaderMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = UserPasswordCredentialMapEditorMBean.class;

   public UserPasswordCredentialMapEditorMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public UserPasswordCredentialMapEditorMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = UserPasswordCredentialMapEditorMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.credentials");
      String var3 = (new String("Provides a set of methods for creating, editing, and removing a credential map that matches WebLogic users to remote usernames and their corresponding passwords. A Credential Mapping-provider MBean can optionally extend this MBean. The WebLogic Server Administration Console detects when a Credential Mapping provider extends this MBean and automatically provides a GUI for using these methods.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.credentials.UserPasswordCredentialMapEditorMBean");
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
      Method var3 = UserPasswordCredentialMapEditorMBean.class.getMethod("setUserPasswordCredential", String.class, String.class, String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the credential is created. "), createParameterDescriptor("remoteUserName", "- The username for the credential. "), createParameterDescriptor("remotePassword", "- The password for the credential. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Sets a remote user-password credential for the specified resource. If a new new username is specified, this method creates a new user-password credential. If you specify an existing username, this method replaces the user's password. ");
         var2.setValue("role", "operation");
         var2.setValue("wls:auditProtectedArgs", "3");
      }

      var3 = UserPasswordCredentialMapEditorMBean.class.getMethod("setUserPasswordCredentialMapping", String.class, String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the credential mapping is created. "), createParameterDescriptor("wlsUserName", "- The user name of the webLogic user. "), createParameterDescriptor("remoteUserName", "- The name of remote user the mapping is being created for. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a mapping from a webLogic username to a remote username-password credential for the specified resource. The credential for the remoteusername for the specified resource should be created before this mapping is created. ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapEditorMBean.class.getMethod("removeUserPasswordCredential", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the credential is to be removed. "), createParameterDescriptor("remoteUserName", "- The name of remote user. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes the credential that is created on the specified resource and remote username. If you created a credential map that specifies this username, the map becomes invalid. Users must remove any credential mappings created for the credential and the specified resource before removing the credential. ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapEditorMBean.class.getMethod("removeUserPasswordCredentialMapping", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the credential mapping is removed. "), createParameterDescriptor("wlsUserName", "- The user name of the webLogic user. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes the mapping from a webLogic username to a remote username-password credential for the specified resource. ");
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
