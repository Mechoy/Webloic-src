package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class MailSessionMBeanImplBeanInfo extends RMCFactoryMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = MailSessionMBean.class;

   public MailSessionMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public MailSessionMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = MailSessionMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>Facilitates using the JavaMail APIs, which provide applications and other J2EE modules with access to Internet Message Access Protocol (IMAP)- and Simple Mail Transfer Protocol (SMTP)-capable mail servers on your network or the Internet.</p>  <p>In the reference implementation of JavaMail, applications must instantiate <code>javax.mail.Session</code> objects, which designate mail hosts, transport and store protocols, and a default mail user for connecting to a mail server. In WebLogic Server, you create a mail session, which configures a <code>javax.mail.Session</code> object and registers it in the WebLogic Server JNDI tree. Applications access the mail session through JNDI instead of instantiating their own javax.mail.Session object.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.MailSessionMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      if (!var1.containsKey("Properties")) {
         String var3 = "getProperties";
         String var4 = null;
         if (!this.readOnly) {
            var4 = "setProperties";
         }

         var2 = new PropertyDescriptor("Properties", MailSessionMBean.class, var3, var4);
         var1.put("Properties", var2);
         var2.setValue("description", "<p>The configuration options and user authentication data that this mail session uses to interact with a mail server. Each property that you specify overrides the default property value as defined by the JavaMail API Design Specification.</p> <p>Include only the properties defined by the JavaMail API Design Specification.</p> <p>If you do not specify any properties, this mail session will use the JavaMail default property values.</p> <p>Express each property as a <code><i>name</i>=<i>value</i></code> pair. Separate multiple properties with a semicolon (<code>;</code>).</p> ");
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
