package weblogic.management.security.credentials;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class UserPasswordCredentialMapExtendedReaderMBeanImplBeanInfo extends UserPasswordCredentialMapReaderMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = UserPasswordCredentialMapExtendedReaderMBean.class;

   public UserPasswordCredentialMapExtendedReaderMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public UserPasswordCredentialMapExtendedReaderMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = UserPasswordCredentialMapExtendedReaderMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.credentials");
      String var3 = (new String("Provides a set of methods for reading the credential mappings. Credential mappings match WebLogic users to remote usernames and passwords.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.credentials.UserPasswordCredentialMapExtendedReaderMBean");
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
      Method var3 = UserPasswordCredentialMapExtendedReaderMBean.class.getMethod("listMappingsByPattern", String.class, Integer.TYPE);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceIdPattern", "- Resource Id string pattern, that can contain '*' - wild card, and '' - escape character. "), createParameterDescriptor("maxToReturn", "- Maximum number of returned mappings ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets a list of all credential mappings with the resource id matching the specified resource id pattern. Returns a cursor as a string. Use the <code>getCurrentMappingWLSUserName</code> and <code>getCurrentMappingRemoteUserName()</code> and <code>getCurrentMappingResourceID()</code> methods to return the WebLogic username and external user name and the current resource ID for the current item in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = UserPasswordCredentialMapExtendedReaderMBean.class.getMethod("getCurrentMappingResourceID", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code>listMappings</code> method and the <code>listMappingsByPattern</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets an resource id from a credentials map that has been returned from the <code>listMappingsByPattern</code> method. This method returns the resource id that corresponds to current location in the list. ");
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
