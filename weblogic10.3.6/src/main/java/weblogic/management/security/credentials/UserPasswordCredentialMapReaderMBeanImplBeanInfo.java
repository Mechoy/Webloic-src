package weblogic.management.security.credentials;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.utils.ListerMBeanImplBeanInfo;

public class UserPasswordCredentialMapReaderMBeanImplBeanInfo extends ListerMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = UserPasswordCredentialMapReaderMBean.class;

   public UserPasswordCredentialMapReaderMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public UserPasswordCredentialMapReaderMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = UserPasswordCredentialMapReaderMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.credentials");
      String var3 = (new String("Provides a set of methods for reading credentials and credential mappings. Credential mappings match WebLogic users to remote usernames and passwords.   A Credential Mapping-provider MBean can optionally extend this MBean. The WebLogic Server Administration Console detects when a Credential Mapping provider extends this MBean and automatically provides a GUI for using these methods.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.credentials.UserPasswordCredentialMapReaderMBean");
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
      Method var3 = UserPasswordCredentialMapReaderMBean.class.getMethod("getRemoteUserName", String.class, String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource on which the mapping between the local weblogic user and the remote user was created. "), createParameterDescriptor("wlsUserName", "- The local weblogic username. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets the external username that is mapped to a local webLogic username for the specified resource. ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapReaderMBean.class.getMethod("getRemotePassword", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the credential was created. "), createParameterDescriptor("remoteUserName", "- The external username. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "Gets the remote password corresponding to the remote username in the credential created for the specified resource. Deprecated in WLS 9.0 ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapReaderMBean.class.getMethod("listCredentials", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the credentials are to be returned. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets a list of credentials mapped to the resource. Returns a cursor as a string. Use the <code>getCurrentCredentialRemoteUserName</code> and <code>getCurrentCredentialRemotePassword</code> methods to get the username and password for the current item in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapReaderMBean.class.getMethod("getCurrentCredentialRemoteUserName", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code>listCredentials</code> method. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets a remote username from a list that has been returned from the <code>listCredentials</code> method. This method returns the remote username that corresponds to current location in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapReaderMBean.class.getMethod("getCurrentCredentialRemotePassword", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code>listCredentials</code> method. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "Gets a password from a list that has been returned from the <code>listCredentials</code> method. This method returns the password that corresponds to current location in the list. Deprecated in WLS 9.0 ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapReaderMBean.class.getMethod("listMappings", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the credential mappings are to be returned. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets a list of credential mappings created for the given resource id. Returns a cursor as a string. Use the <code>getCurrentMappingWLSUserName</code> and <code>getCurrentMappingRemoteUserName</code> methods to return the webLogic username and remote user name for the current item in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapReaderMBean.class.getMethod("getCurrentMappingWLSUserName", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code>listMappings</code> method. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets a webLogic username from a credentials mapping that has been returned from the <code>listMappings</code> method. This method returns the local webLogic username that corresponds to current location in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapReaderMBean.class.getMethod("getCurrentMappingRemoteUserName", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code>listMappings</code> method. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets an remote username from a credentials mapping that has been returned from the <code>listMappings</code> method. This method returns the remote username that corresponds to current location in the list. ");
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
