package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class RealmMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = RealmMBean.class;

   public RealmMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public RealmMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = RealmMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("deprecated", "7.0.0.0 ");
      var2.setValue("exclude", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This MBean represents the properties of the security realm.  Deprecated in WebLogic Server version 7.0. Replaced by the new Security architecture that includes Authentication, Authorization, and Auditing providers.  It includes the following: <ul> <li> Configuration parameters for the File realm.</li> <li> Configuration parameters for the Caching realm.</li> <li> Methods to manage users, groups, ACLs, and permissions.</li> </ul> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.RealmMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("CachingRealm")) {
         var3 = "getCachingRealm";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCachingRealm";
         }

         var2 = new PropertyDescriptor("CachingRealm", RealmMBean.class, var3, var4);
         var1.put("CachingRealm", var2);
         var2.setValue("description", "<p>The name of an alternate security realm to be used in this WebLogic server domain. A value of <tt>(none)</tt> means that only the File realm can be used.</p>  <p>If a realm other than the File realm is used, the realm is specified by attaching the name of the realm to the CachingRealm MBean. The CachingRealm MBean then attaches to the Realm MBean.</p>  <p>If the attribute has a value, an alternate security realm is used. If the attribute is null, only the File Realm can be used.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("FileRealm")) {
         var3 = "getFileRealm";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFileRealm";
         }

         var2 = new PropertyDescriptor("FileRealm", RealmMBean.class, var3, var4);
         var1.put("FileRealm", var2);
         var2.setValue("description", "<p>Creates a File realm.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", RealmMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("ResultsBatchSize")) {
         var3 = "getResultsBatchSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setResultsBatchSize";
         }

         var2 = new PropertyDescriptor("ResultsBatchSize", RealmMBean.class, var3, var4);
         var1.put("ResultsBatchSize", var2);
         var2.setValue("description", "<p>The number of users, groups, and ACLs to return per Remote Procedure Call (RPC).</p>  <p>Specifies the batch size (number of users, groups, and ACLs to return per rpc) for returning users, groups, and ACLs. The purpose is to avoid having either one rpc per user, group, or ACL or one very large rpc that causes an overfill of memory.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(200));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("EnumerationAllowed")) {
         var3 = "isEnumerationAllowed";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnumerationAllowed";
         }

         var2 = new PropertyDescriptor("EnumerationAllowed", RealmMBean.class, var3, var4);
         var1.put("EnumerationAllowed", var2);
         var2.setValue("description", "<p>Specifies ability to enumerate users, groups, and memberships to prevent possible Denial Of Service attacks (if there are many users or groups).</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
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
      Method var3 = RealmMBean.class.getMethod("refresh");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Propagate changes to users, groups, and ACLs to the realms in the managed servers.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = RealmMBean.class.getMethod("manager");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Get the object that manages the realm (list/create/modify/delete users/groups/acls).</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = RealmMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = RealmMBean.class.getMethod("restoreDefaultValue", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var6);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
         var2.setValue("exclude", Boolean.TRUE);
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
