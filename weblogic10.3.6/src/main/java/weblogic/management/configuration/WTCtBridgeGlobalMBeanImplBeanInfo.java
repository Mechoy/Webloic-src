package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WTCtBridgeGlobalMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCtBridgeGlobalMBean.class;

   public WTCtBridgeGlobalMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCtBridgeGlobalMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCtBridgeGlobalMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This interface provides access to the WTC tBridge Global configuration attributes. The methods defined herein are applicable for tBridge configuration at the WLS domain level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WTCtBridgeGlobalMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AllowNonStandardTypes")) {
         var3 = "getAllowNonStandardTypes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAllowNonStandardTypes";
         }

         var2 = new PropertyDescriptor("AllowNonStandardTypes", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("AllowNonStandardTypes", var2);
         var2.setValue("description", "<p>Specifies whether non-standard data types are allowed to pass through this Tuxedo queuing bridge.</p>  <p>A value of <code>NO</code> means that non standard types are rejected and placed onto a specified error location; a value of <code>YES</code> means that non-standard types are placed on the target location as BLOBs with a tag indicating the original type.</p>  <p><i>Note:</i> Standard types are: ASCII text (TextMessage, STRING), or BLOB (BytesMessage, CARRAY).</p> ");
         setPropertyDescriptorDefault(var2, "NO");
         var2.setValue("legalValues", new Object[]{"Yes", "No"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DefaultReplyDeliveryMode")) {
         var3 = "getDefaultReplyDeliveryMode";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultReplyDeliveryMode";
         }

         var2 = new PropertyDescriptor("DefaultReplyDeliveryMode", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("DefaultReplyDeliveryMode", var2);
         var2.setValue("description", "<p>The reply delivery mode to associate with a message when placing messages onto the target location.</p>  <p style=\"font-weight: bold\">Usage Notes:</p>  <ul> <li>Use when messages are being redirected to Tuxedo/Q from JMS and the <code>JMS_BEA_TuxGtway_Tuxedo_ReplyDeliveryMode</code> property is not set for a message. </li>  <li>If the <code>defaultReplyDeliveryMode</code> and <code>JMS_BEA_TuxGtway_Tuxedo_ReplyDeliveryMode</code> are not set, the default semantics defined for Tuxedo are enforced by the Tuxedo/Q subsystem. </li> </ul> ");
         setPropertyDescriptorDefault(var2, "DEFAULT");
         var2.setValue("legalValues", new Object[]{"PERSIST", "NONPERSIST", "DEFAULT"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DeliveryModeOverride")) {
         var3 = "getDeliveryModeOverride";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeliveryModeOverride";
         }

         var2 = new PropertyDescriptor("DeliveryModeOverride", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("DeliveryModeOverride", var2);
         var2.setValue("description", "<p>The delivery mode to use when placing messages onto the target location.</p>  <p>If this value is not specified, the message is placed on the target location with the same delivery mode specified from the source location.</p>  <p><i>Note:</i> This value overrides any delivery mode associated with a message.</p> ");
         setPropertyDescriptorDefault(var2, "NONPERSIST");
         var2.setValue("legalValues", new Object[]{"PERSIST", "NONPERSIST"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JmsFactory")) {
         var3 = "getJmsFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJmsFactory";
         }

         var2 = new PropertyDescriptor("JmsFactory", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("JmsFactory", var2);
         var2.setValue("description", "<p>The name of the JMS connection factory.</p>  <p><b>Example:</b> <code>weblogic.jms.ConnectionFactory</code></p> ");
         setPropertyDescriptorDefault(var2, "weblogic.jms.XAConnectionFactory");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JmsToTuxPriorityMap")) {
         var3 = "getJmsToTuxPriorityMap";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJmsToTuxPriorityMap";
         }

         var2 = new PropertyDescriptor("JmsToTuxPriorityMap", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("JmsToTuxPriorityMap", var2);
         var2.setValue("description", "<p>The mapping of priorities from JMS to Tuxedo. The default JMS To Tux Priority Map is: <tt>0:1 | 1:12 | 2:23 | 3:34 | 4:45 |5:56 | 6:67 | 7:78 | 8:89 | 9:100.</tt></p>  <p><b>Examples:</b> <code>0:1 | 1:12 | 2:23 | 3:34 | 4:45 | 5:56 | 6:67 | 7:78 | 8:89 | 9:100 or 0:1-10|1:11-20|2:21-30|3:31-40|4:41-50|5:51-60|6:61-70|7:71-80|8:81-90|9:91-100</code></p>  <p><i>Note:</i> The are 10 possible JMS priorities(0=&gt;9) which can be paired to 100 possible Tuxedo priorities(1=&gt;100). A mapping consists of a \"|\" separated list of value-to-range pairs (jmsvalue:tuxrange) where pairs are separated by \":\" and ranges are separated by \"-\".</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("JndiFactory")) {
         var3 = "getJndiFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJndiFactory";
         }

         var2 = new PropertyDescriptor("JndiFactory", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("JndiFactory", var2);
         var2.setValue("description", "<p>The name of the JNDI lookup factory.</p>  <p><b>Example:</b> <code>weblogic.jndi.WLInitialContextFactory</code></p> ");
         setPropertyDescriptorDefault(var2, "weblogic.jndi.WLInitialContextFactory");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Retries")) {
         var3 = "getRetries";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetries";
         }

         var2 = new PropertyDescriptor("Retries", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("Retries", var2);
         var2.setValue("description", "<p>The number of attempts to redirect a message before this Tuxedo queuing bridge places the message in the specified error location and logs an error.</p>  <p><b>Range of Values:</b> Between 0 and a positive 32-bit integer.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RetryDelay")) {
         var3 = "getRetryDelay";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetryDelay";
         }

         var2 = new PropertyDescriptor("RetryDelay", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("RetryDelay", var2);
         var2.setValue("description", "<p>The minimum number of milliseconds this Tuxedo queuing bridge waits before redirecting a message after a failure.</p>  <p><i>Note:</i> During this waiting period, no other messages are redirected from the thread. Other threads may continue to redirect messages.</p>  <p><b>Range of Values:</b> Between <code><code>0</code></code> and a positive 32-bit integer.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(10));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Timeout")) {
         var3 = "getTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTimeout";
         }

         var2 = new PropertyDescriptor("Timeout", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("Timeout", var2);
         var2.setValue("description", "<p>The number of timeout seconds for an entire redirection when this Tuxedo queuing bridge places a message on the target location. A value of <code>0</code> specifies an infinite wait.</p>  <p><b>Range of Values:</b> Between <code>0</code> and a positive 32-bit integer.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Transactional")) {
         var3 = "getTransactional";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTransactional";
         }

         var2 = new PropertyDescriptor("Transactional", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("Transactional", var2);
         var2.setValue("description", "<p>Specifies whether this Tuxedo queuing bridge should use transactions when retrieving messages from a source location and when placing messages on a target location.</p>  <p>A value of <code>YES</code> means that transactions are used for both operations; a value of <code>NO</code> means that transactions are not used for either operation.</p>  <p><i>Note:</i> Transactional is not supported in this release.</p> ");
         setPropertyDescriptorDefault(var2, "NO");
         var2.setValue("legalValues", new Object[]{"Yes", "No"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TuxErrorQueue")) {
         var3 = "getTuxErrorQueue";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTuxErrorQueue";
         }

         var2 = new PropertyDescriptor("TuxErrorQueue", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("TuxErrorQueue", var2);
         var2.setValue("description", "<p>The name of the Tuxedo queue used to store a message that cannot be redirected to a Tuxedo/Q source queue.</p>  <p>If not specified, all messages not redirected are lost. If the message cannot be placed into the <code>TuxErrorQueue</code>, an error is logged and the message is lost.</p>  <p><i>Note:</i> This queue is in the same queue space as the source queue.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TuxFactory")) {
         var3 = "getTuxFactory";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTuxFactory";
         }

         var2 = new PropertyDescriptor("TuxFactory", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("TuxFactory", var2);
         var2.setValue("description", "<p>The name of the Tuxedo connection factory.</p>  <p><b>Example:</b> <code>tuxedo.services.TuxedoConnection</code></p> ");
         setPropertyDescriptorDefault(var2, "tuxedo.services.TuxedoConnection");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TuxToJmsPriorityMap")) {
         var3 = "getTuxToJmsPriorityMap";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTuxToJmsPriorityMap";
         }

         var2 = new PropertyDescriptor("TuxToJmsPriorityMap", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("TuxToJmsPriorityMap", var2);
         var2.setValue("description", "<p>The mapping of priorities to map from Tuxedo to JMS. The default JMS To Tux Priority Map is: <tt>1-10:0 | 11-20:1 | 21-30:2 | 31-40:3| 41-50:4| 51-60:5 | 61-70:6 | 71-80:7 | 81-90:8 | 91-100:9<code>.</code></tt></p>  <p><b>Examples:</b> <code>1:0 | 12:1 | 23:2 | 34:3 | 45:4 | 56:5 | 67:6 | 78:7 | 89:8 | 100:9 or 20:0-1 | 40:2-3 | 60:4-5 | 80:6-7 | 100:8-9</code></p>  <p><i>Note:</i> The are 100 possible Tuxedo priorities(1=&gt;100) which can be paired to 10 possible JMS priorities(0=&gt;9). A mapping consists of a \"|\" separated list of value-to-range pairs (tuxvalue:jmsrange) where pairs are separated by \":\" and ranges are separated by \"-\".</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("UserId")) {
         var3 = "getUserId";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUserId";
         }

         var2 = new PropertyDescriptor("UserId", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("UserId", var2);
         var2.setValue("description", "<p>The user identity for all messages handled by this Tuxedo queuing bridge for ACL checks when security is configured.</p>  <p>All messages assume this identity until the security/authentication contexts are passed between the subsystems. Until the security contexts are passed, there is no secure method to identify who generated a message received from the source location.</p>  <p><i>Note:</i> The <code>user</code> argument may be specified as either a user name or a user identification number (uid).</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("WlsErrorDestination")) {
         var3 = "getWlsErrorDestination";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setWlsErrorDestination";
         }

         var2 = new PropertyDescriptor("WlsErrorDestination", WTCtBridgeGlobalMBean.class, var3, var4);
         var1.put("WlsErrorDestination", var2);
         var2.setValue("description", "<p>The name of the location used to store WebLogic Server JMS messages when a message cannot be redirected.</p>  <p>If not specified, all messages not redirected are lost. If the message cannot be placed into <code>WlsErrorDestination</code> for any reason, an error is logged and the message is lost.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
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
