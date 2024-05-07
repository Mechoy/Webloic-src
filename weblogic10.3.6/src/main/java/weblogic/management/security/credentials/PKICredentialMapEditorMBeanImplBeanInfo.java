package weblogic.management.security.credentials;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class PKICredentialMapEditorMBeanImplBeanInfo extends PKICredentialMapReaderMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = PKICredentialMapEditorMBean.class;

   public PKICredentialMapEditorMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public PKICredentialMapEditorMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = PKICredentialMapEditorMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.credentials");
      String var3 = (new String("Provides a set of methods for creating, editing, and removing a credential map that matches users, resources and credential action to keystore aliases and the corresponding passwords. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.credentials.PKICredentialMapEditorMBean");
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
      Method var3 = PKICredentialMapEditorMBean.class.getMethod("setKeypairCredential", String.class, String.class, Boolean.TYPE, String.class, String.class, char[].class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the user name is mapped to the keystore alias and password. "), createParameterDescriptor("principalName", "- \tThe principalName used in the credential mapping. "), createParameterDescriptor("isInitiatorUserName", "- True if the initiator name is a user name. False if it is a  group. "), createParameterDescriptor("credAction", "- The credential action. "), createParameterDescriptor("keystoreAlias", "- The keystore alias. "), createParameterDescriptor("password", "- The password for the keystore entry. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException - Exception thrown if the keystore alias                               does not point to a keypair entry or if the                               password supplied here is not correct.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a keypair mapping between the principalName, resourceid and credential action to the keystore alias and the corresponding password. ");
         var2.setValue("role", "operation");
         var2.setValue("wls:auditProtectedArgs", "6");
      }

      var3 = PKICredentialMapEditorMBean.class.getMethod("setCertificateCredential", String.class, String.class, Boolean.TYPE, String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the user name is mapped to the keystore alias. "), createParameterDescriptor("principalName", "- \tThe principalName used in the credential mapping. "), createParameterDescriptor("isInitiatorUserName", "- True if the initiator name is a user name. False if it is a  group. "), createParameterDescriptor("credAction", "- The credential action. "), createParameterDescriptor("keystoreAlias", "- The keystore alias. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("CreateException -    Exception thrown if the keystore alias                               does not point to a certificate entry.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Creates a public certificate mapping between the principalName, resourceid and credential action to the keystore alias. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapEditorMBean.class.getMethod("removePKICredentialMapping", String.class, String.class, Boolean.TYPE, String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource for which the user name is mapped to the keystore alias and password. "), createParameterDescriptor("principalName", "- The principalName used in the credential mapping. "), createParameterDescriptor("isInitiatorUserName", "- True if the initiator name is a user name. False if it is a  group. "), createParameterDescriptor("credAction", "- The credential action. "), createParameterDescriptor("credType", "- The credential type. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Removes the mapping between the principalName, resourceid and credential action to the keystore alias. ");
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
