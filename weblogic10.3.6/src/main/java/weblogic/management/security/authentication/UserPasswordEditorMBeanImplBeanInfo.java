package weblogic.management.security.authentication;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.commo.AbstractCommoConfigurationBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class UserPasswordEditorMBeanImplBeanInfo extends AbstractCommoConfigurationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = UserPasswordEditorMBean.class;

   public UserPasswordEditorMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public UserPasswordEditorMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = UserPasswordEditorMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.authentication");
      String var3 = (new String("Provides two methods for changing a user's password. An Authentication provider MBean can optionally implement this MBean. The WebLogic Server Administration Console detects when an Authentication provider implements this MBean and automatically provides a tab for using these methods.<p>  CSS v4 introduced a new Password Validaton Service to check password against a set of rules when doing changing password operations with authentication provider MBeans such as <b>createUser</b>, <b>changeUserPassword</b> and <b>resetUserPassword</b>. The rules can be specified through configuring Password Validation Provider into the security realm, for further information, see <code>weblogic.management.security.RealmMBean</code>.<p>  All OOTB authentication providers in CSS will automatically call the Password Validation Service if their MBeans inherit <code>UserPasswordEditorMBean</code> interface. The service is also available for all those customized authentication providers whose MBeans inherit <code>UserPasswordEditorMBean</code>, to introduce the Password Validation Service into a customized authentication proivder, the following approach must be met:<p> 1. In the <b>initialize</b> method of a customized provider implementation, must retrieve the Password Validation Service and register the service into a helper class such as <code>weblogic.security.provider.authentication.AuthenticationSecurityHelper</code>, the code might like as below: <blockquote><pre> import com.bea.common.security.service.PasswordValidationService; import com.bea.common.security.legacy.ExtendedSecurityServices; import com.bea.common.security.internal.legacy.helper.PasswordValidationServiceConfigHelper; import weblogic.security.provider.authentication.AuthenticationSecurityHelper; ...... ExtendedSecurityServices extendedSecurityServices = (ExtendedSecurityServices)securityServices; PasswordValidationService serivce = (PasswordValidationService)extendedSecurityServices.getServices(). getService(PasswordValidationServiceConfigHelper.getServiceName(providerMBean.getRealm())); AuthenticationSecurityHelper.getInstance(providerMBean).registerPasswordValidationService(service); ...... </pre></blockquote><p> 2. In the <b>createUser</b>, <b>changeUserPassword</b> and(or) <b>resetUserPassword</b> methods of a customized authentication provider MBean, call the helper class to validate the new password to determine if the new password is valid. The code might be:<br> <blockquote><pre> import weblogic.security.provider.authentication.AuthenticationSecurityHelper; ..... AuthenticationSecurityHelper.getInstance(providerMBean).validatePassword(userName,password); ..... </pre></blockquote>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.authentication.UserPasswordEditorMBean");
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
      Method var3 = UserPasswordEditorMBean.class.getMethod("changeUserPassword", String.class, String.class, String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- The name of an existing user. "), createParameterDescriptor("oldPassword", "- The current password for the user. "), createParameterDescriptor("newPassword", "- The new password for the user. The Authentication provider determines the syntax requirements for passwords. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Used by a user to change his or her password. ");
         var2.setValue("role", "operation");
         var2.setValue("wls:auditProtectedArgs", "2,3");
      }

      var3 = UserPasswordEditorMBean.class.getMethod("resetUserPassword", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("userName", "- The name of an existing user. "), createParameterDescriptor("newPassword", "- The new password for the user. The Authentication provider determines the syntax requirements for passwords. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Used by an administrator to change a user's password. ");
         var2.setValue("role", "operation");
         var2.setValue("wls:auditProtectedArgs", "2");
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
