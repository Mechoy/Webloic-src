package weblogic.management.security.credentials;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.utils.ListerMBeanImplBeanInfo;

public class PKICredentialMapReaderMBeanImplBeanInfo extends ListerMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = PKICredentialMapReaderMBean.class;

   public PKICredentialMapReaderMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public PKICredentialMapReaderMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = PKICredentialMapReaderMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.credentials");
      String var3 = (new String("Provides a set of methods for reading a credential map that matches users and resources to aliases and their corresponding passwords that can then be used to retrieve key information or public certificate information from the configured keystores. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.credentials.PKICredentialMapReaderMBean");
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
      Method var3 = PKICredentialMapReaderMBean.class.getMethod("getKeystoreAlias", String.class, String.class, Boolean.TYPE, String.class, String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- The resource id that is used to map user names to keystore alias and password. A resource object such as <code>weblogic.security.service.ResourceManager</code> assigns IDs to external sources. "), createParameterDescriptor("userName", "- The username that is mapped to the alias and password. "), createParameterDescriptor("isInitiatorUserName", "- Set true if the initiator name passed in is the username. False otherwise. "), createParameterDescriptor("credAction", "- The credential action for which the mapping is created for. "), createParameterDescriptor("credType", "- The credential type. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("NotFoundException - This exception is thrown if                             the keystore alias is not found.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Gets the keystore alias that is mapped to a username for a particular resource and credential action. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("getCurrentInitiatorName", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code>listMappings</code> method or the <code>listMappingsByPattern</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets an username from a list that has been returned from the <code>listMappings</code> or the <code>listMappingsByPattern()</code> method. This method returns the username that corresponds to current location in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("isInitiatorUserName", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code>listMappings</code> method or the <code>listMappingsByPattern</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Returns whether the initiator name from a list that has been returned from the <code>listMappings</code> or the <code>listMappingsByPattern()</code>method is a user name or a group name. Method returns true if the username returned by the getCurrentInitiatorName is a user name. If the initiator name returned is a Group name this method returns false. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("getCurrentCredAction", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code>listMappings</code> method or the <code>listMappingsByPattern</code>. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets an credential action from a list that has been returned from the <code>listMappings</code> or the <code>listMappingsByPattern()</code> method. This method returns the credential action that corresponds to current location in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("getCurrentCredential", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code>listMappings</code> or the <code>listMappingsByPattern</code> method. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets the keystore alias from a credentials map that has been returned from the <code>listMappings</code> or the <code>listMappingsByPattern()</code> method. This method returns the keystore alias that corresponds to current location in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("listMappings", String.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceId", "- the resource id that the credential mappings are created for. A resource object such as <code>weblogic.security.service.ResourceManager</code> assigns IDs to external sources. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets a list of usernames, credential actions, keystore aliases and their passwords. Returns  a cursor as a string. Use the <code>getCurrentUserName()</code>,<code>getCurrentCredential()</code>, <code>getCurrentCredAction()</code> methods to get the username, keystore alias and credential action for the current item in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("getCurrentResourceId", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("cursor", "- The cursor that has been returned from the <code> listMappingsByPattern</code> method. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets the current resource id from a list that has been returned from the and <code>listMappingsByPattern()</code> method. This method returns the resource id that corresponds to current location in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("listMappingsByPattern", String.class, Integer.TYPE, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("resourceIdPattern", "- the resource id pattern to filter the records to be returned. If you pass null or * the method will not filter and return all records. "), createParameterDescriptor("maxToReturn", "- The maximum number of records to return "), createParameterDescriptor("credType", "- The credential type ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "Gets a list of all the configured credential mappings. Returns a cursor as a string. Use the <code>getCurrentUserName()</code>,<code> getCurrentCredAction()</code>, <code> getCurrentResourceId()</code>, <code> getCurrentCredential()</code> methods to get the username, credential action, resource id and keystore alias for the current item in the list. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("listAllCertEntryAliases");
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "Returns a list of all the public certificate aliases currently configured in the keystore. Console can call this method to display a list of all possible certificate aliases. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("listAllKeypairEntryAliases");
      var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var7, var2);
         var2.setValue("description", "Returns a list of all the key pair aliases that currently configured in the keystore. Console can call this method to display a list of all possible keypair aliases. ");
         var2.setValue("role", "operation");
      }

      var3 = PKICredentialMapReaderMBean.class.getMethod("getCertificate", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("keystoreAlias", "- The keystore alias. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "For a certificate entry this method will return the certificate corresponding to the alias. For a keyentry it will return the first Certificate entry in a CertificateChain. ");
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
