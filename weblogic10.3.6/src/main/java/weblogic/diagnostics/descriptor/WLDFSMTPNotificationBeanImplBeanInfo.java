package weblogic.diagnostics.descriptor;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WLDFSMTPNotificationBeanImplBeanInfo extends WLDFNotificationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WLDFSMTPNotificationBean.class;

   public WLDFSMTPNotificationBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WLDFSMTPNotificationBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WLDFSMTPNotificationBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.diagnostics.descriptor");
      String var3 = (new String("<p>Use this interface to define a SMTP notification, which is sent when a diagnostic watch evaluates to <code>true</code>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.diagnostics.descriptor.WLDFSMTPNotificationBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Body")) {
         var3 = "getBody";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setBody";
         }

         var2 = new PropertyDescriptor("Body", WLDFSMTPNotificationBean.class, var3, var4);
         var1.put("Body", var2);
         var2.setValue("description", "<p>The body for the mail message. If the body is not specified, a body is created from the watch notification information.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MailSessionJNDIName")) {
         var3 = "getMailSessionJNDIName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMailSessionJNDIName";
         }

         var2 = new PropertyDescriptor("MailSessionJNDIName", WLDFSMTPNotificationBean.class, var3, var4);
         var1.put("MailSessionJNDIName", var2);
         var2.setValue("description", "<p>The JNDI name of the mail session. This name must match the attribute in the corresponding MailSessionMBean.</p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Recipients")) {
         var3 = "getRecipients";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRecipients";
         }

         var2 = new PropertyDescriptor("Recipients", WLDFSMTPNotificationBean.class, var3, var4);
         var1.put("Recipients", var2);
         var2.setValue("description", "<p>The address of the recipient or recipients of the SMTP notification mail. The address uses the syntax defined in RFC822. Typical address syntax is of the form <code><i>user</i>@<i>host</i>.<i>domain</code> or <code><i>Personal Name</i></code>. An address can include multiple recipients, separated by commas or spaces.</p>  <p>For more information, refer to the javax.mail.internet.InternetAddress.parse method.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Subject")) {
         var3 = "getSubject";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSubject";
         }

         var2 = new PropertyDescriptor("Subject", WLDFSMTPNotificationBean.class, var3, var4);
         var1.put("Subject", var2);
         var2.setValue("description", "<p>The subject for the mail message. If the subject is not specified, a subject is created from the watch notification information.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = WLDFSMTPNotificationBean.class.getMethod("addRecipient", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("recipient", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Adds a recipient to the list of e-mail addresses that will receive this notification.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Recipients");
      }

      var3 = WLDFSMTPNotificationBean.class.getMethod("removeRecipient", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("recipient", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a recipient from the list of e-mail addresses that will receive this notification.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Recipients");
      }

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
