package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class KernelMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = KernelMBean.class;

   public KernelMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public KernelMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = KernelMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean represents the configuration of the core message passing kernel on both WebLogic clients and servers.  <p> {@link ServerMBean ServerMBean} extends this bean to represent the configuration of a server.</p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.KernelMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AdministrationProtocol")) {
         var3 = "getAdministrationProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAdministrationProtocol";
         }

         var2 = new PropertyDescriptor("AdministrationProtocol", KernelMBean.class, var3, var4);
         var1.put("AdministrationProtocol", var2);
         var2.setValue("description", "<p>Returns the protocol to be used for administrative connections when none is specified.</p> ");
         var2.setValue("legalValues", new Object[]{"t3s", "https", "iiops", "t3", "http", "iiop"});
         var2.setValue("dynamic", Boolean.FALSE);
      }

      String[] var5;
      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("CompleteCOMMessageTimeout")) {
         var3 = "getCompleteCOMMessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteCOMMessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteCOMMessageTimeout", KernelMBean.class, var3, var4);
         var1.put("CompleteCOMMessageTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds spent waiting for a complete COM message to be received. This setting does not apply to any network channels that you have configured for this server.</p>  <p>This timeout helps guard against a denial of service attack in which a caller indicates that they will be sending a message of a certain size which they never finish sending.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getCompleteMessageTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getCompleteMessageTimeout()} ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("CompleteHTTPMessageTimeout")) {
         var3 = "getCompleteHTTPMessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteHTTPMessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteHTTPMessageTimeout", KernelMBean.class, var3, var4);
         var1.put("CompleteHTTPMessageTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds spent waiting for a complete HTTP message to be received. If you configure network channels for this server, each channel can override this HTTP message timeout.</p>  <p>This timeout helps guard against a denial of service attack in which a caller indicates that it will be sending a message of a certain size which it never finishes sending.</p>  <p>A value of -1 indicates that this value should be obtained from network channels configured for this server.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getCompleteMessageTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getCompleteMessageTimeout()} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CompleteIIOPMessageTimeout")) {
         var3 = "getCompleteIIOPMessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteIIOPMessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteIIOPMessageTimeout", KernelMBean.class, var3, var4);
         var1.put("CompleteIIOPMessageTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds spent waiting for a complete IIOP message to be received. This timeout helps guard against denial of service attacks in which a caller indicates that they will be sending a message of a certain size which they never finish sending.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getCompleteMessageTimeout()} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CompleteMessageTimeout")) {
         var3 = "getCompleteMessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteMessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteMessageTimeout", KernelMBean.class, var3, var4);
         var1.put("CompleteMessageTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds that this server waits for a complete message to be received. If you configure network channels for this server, each channel can override this message timeout.</p>  <p>This timeout helps guard against a denial of service attack in which a caller indicates that it will be sending a message of a certain size which it never finishes sending.</p>  <p>CompleteMessageTimeout affects the HTTP Response, such that if WebLogic Server discovers sockets inactive for longer than the CompleteMessageTimeout, the server will close these sockets.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getCompleteMessageTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("CompleteT3MessageTimeout")) {
         var3 = "getCompleteT3MessageTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setCompleteT3MessageTimeout";
         }

         var2 = new PropertyDescriptor("CompleteT3MessageTimeout", KernelMBean.class, var3, var4);
         var1.put("CompleteT3MessageTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds spent waiting for a complete T3 message to be received. If you configure network channels for this server, each channel can override this T3 message timeout.</p>  <p>This timeout helps guard against a denial of service attack in which a caller indicates that it will be sending a message of a certain size which it never finishes sending.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getCompleteMessageTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMax", new Integer(480));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getCompleteMessageTimeout()} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ConnectTimeout")) {
         var3 = "getConnectTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setConnectTimeout";
         }

         var2 = new PropertyDescriptor("ConnectTimeout", KernelMBean.class, var3, var4);
         var1.put("ConnectTimeout", var2);
         var2.setValue("description", "<p>The amount of time that this server should wait to establish an outbound socket connection before timing out. A value of <code>0</code> disables server connect timeout. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getConnectTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(0));
         var2.setValue("legalMax", new Integer(240));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("DGCIdlePeriodsUntilTimeout")) {
         var3 = "getDGCIdlePeriodsUntilTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDGCIdlePeriodsUntilTimeout";
         }

         var2 = new PropertyDescriptor("DGCIdlePeriodsUntilTimeout", KernelMBean.class, var3, var4);
         var1.put("DGCIdlePeriodsUntilTimeout", var2);
         var2.setValue("description", "<p>The number of idle periods allowed before object is collected.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
      }

      if (!var1.containsKey("DefaultProtocol")) {
         var3 = "getDefaultProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultProtocol";
         }

         var2 = new PropertyDescriptor("DefaultProtocol", KernelMBean.class, var3, var4);
         var1.put("DefaultProtocol", var2);
         var2.setValue("description", "<p>The protocol to use for connections when none is specified.</p> ");
         setPropertyDescriptorDefault(var2, "t3");
         var2.setValue("legalValues", new Object[]{"t3", "t3s", "http", "https", "iiop", "iiops"});
      }

      if (!var1.containsKey("DefaultSecureProtocol")) {
         var3 = "getDefaultSecureProtocol";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDefaultSecureProtocol";
         }

         var2 = new PropertyDescriptor("DefaultSecureProtocol", KernelMBean.class, var3, var4);
         var1.put("DefaultSecureProtocol", var2);
         var2.setValue("description", "<p>The protocol to use for secure connections when none is specified.</p> ");
         setPropertyDescriptorDefault(var2, "t3s");
         var2.setValue("legalValues", new Object[]{"t3s", "https", "iiops"});
      }

      if (!var1.containsKey("ExecuteQueues")) {
         var3 = "getExecuteQueues";
         var4 = null;
         var2 = new PropertyDescriptor("ExecuteQueues", KernelMBean.class, var3, var4);
         var1.put("ExecuteQueues", var2);
         var2.setValue("description", "<p>Returns the execute queues configured for this server.</p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("destroyer", "destroyExecuteQueue");
         var2.setValue("creator", "createExecuteQueue");
      }

      if (!var1.containsKey("IIOP")) {
         var3 = "getIIOP";
         var4 = null;
         var2 = new PropertyDescriptor("IIOP", KernelMBean.class, var3, var4);
         var1.put("IIOP", var2);
         var2.setValue("description", "<p>Returns the kernel's IIOP configuration. An IIOP MBean is always linked to a particular Kernel and cannot be changed. Individual attributes on the IIOP MBean may be changed, as documented, but the MBean itself may not.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("IIOPTxMechanism")) {
         var3 = "getIIOPTxMechanism";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIIOPTxMechanism";
         }

         var2 = new PropertyDescriptor("IIOPTxMechanism", KernelMBean.class, var3, var4);
         var1.put("IIOPTxMechanism", var2);
         var2.setValue("description", "<p>Configures IIOP propagate transactions using either WebLogic-specific JTA or the OMG-specified OTS.</p> <p> It is not possible to use both because it affects the way transactions are negotiated.</p> ");
         setPropertyDescriptorDefault(var2, "ots");
         var2.setValue("legalValues", new Object[]{"ots", "jta"});
         var2.setValue("deprecated", "8.1.0.0 use {@link IIOPMBean#getTxMechanism()} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IdleConnectionTimeout")) {
         var3 = "getIdleConnectionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdleConnectionTimeout";
         }

         var2 = new PropertyDescriptor("IdleConnectionTimeout", KernelMBean.class, var3, var4);
         var1.put("IdleConnectionTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds that a connection is allowed to be idle before it is closed by the server. The T3 and T3S protocols ignore this attribute. If you configure network channels for this server, each channel can override this idle connection message timeout.</p>  <p>This timeout helps guard against server deadlock through too many open connections.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getIdleConnectionTimeout")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(65));
         var2.setValue("secureValue", new Integer(65));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IdleIIOPConnectionTimeout")) {
         var3 = "getIdleIIOPConnectionTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdleIIOPConnectionTimeout";
         }

         var2 = new PropertyDescriptor("IdleIIOPConnectionTimeout", KernelMBean.class, var3, var4);
         var1.put("IdleIIOPConnectionTimeout", var2);
         var2.setValue("description", "<p>The maximum number of seconds an IIOP connection is allowed to be idle before it is closed by the server. This timeout helps guard against server deadlock through too many open connections.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("secureValue", new Integer(60));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getIdleConnectionTimeout()} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("IdlePeriodsUntilTimeout")) {
         var3 = "getIdlePeriodsUntilTimeout";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setIdlePeriodsUntilTimeout";
         }

         var2 = new PropertyDescriptor("IdlePeriodsUntilTimeout", KernelMBean.class, var3, var4);
         var1.put("IdlePeriodsUntilTimeout", var2);
         var2.setValue("description", "<p>The number of idle periods until peer is considered unreachable</p> ");
         setPropertyDescriptorDefault(var2, new Integer(4));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(4));
      }

      if (!var1.containsKey("JMSThreadPoolSize")) {
         var3 = "getJMSThreadPoolSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setJMSThreadPoolSize";
         }

         var2 = new PropertyDescriptor("JMSThreadPoolSize", KernelMBean.class, var3, var4);
         var1.put("JMSThreadPoolSize", var2);
         var2.setValue("description", "<p>The size of the JMS execute thread pool.</p>  <p><b>Note</b>: Incoming RMI calls execute in the JMS execute queue/thread pool, if one exists; otherwise, they execute in the default execute queue.</p>  <p>Additional executes (work that cannot be completed in the initial RMI thread) are executed in the default execute queue.</p>  <p>The difference in setting up a JMS-specific thread pool is that JMS will not be starved by other execute threads and vice versa.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(15));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("KernelDebug")) {
         var3 = "getKernelDebug";
         var4 = null;
         var2 = new PropertyDescriptor("KernelDebug", KernelMBean.class, var3, var4);
         var1.put("KernelDebug", var2);
         var2.setValue("description", "<p>Get the debug flags for this kernel (will return a KernelDebugMBean if this is a KernelMBean) or the server (will return a ServerDebugMBean if this is a ServerMBean)</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Log")) {
         var3 = "getLog";
         var4 = null;
         var2 = new PropertyDescriptor("Log", KernelMBean.class, var3, var4);
         var1.put("Log", var2);
         var2.setValue("description", "<p>Returns the Log settings for this Kernel. An Log MBean is always linked to a particular Kernel and cannot be changed. Individual attributes on the Log MBean may be changed, as documented, but the MBean itself may not.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("7.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MaxCOMMessageSize")) {
         var3 = "getMaxCOMMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxCOMMessageSize";
         }

         var2 = new PropertyDescriptor("MaxCOMMessageSize", KernelMBean.class, var3, var4);
         var1.put("MaxCOMMessageSize", var2);
         var2.setValue("description", "<p>The maximum number of bytes allowed in messages that are received over the COM protocol. If you configure custom network channels for this server, each channel can override this maximum message size.</p>  <p>This maximum message size helps guard against a denial of service attack in which a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests.</p>  <p>A value of -1 causes the COM protocol to use the maximums that are specified elsewhere along the order of precedence.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("KernelMBean#getMaxMessageSize"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getMaxMessageSize")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getMaxMessageSize()} ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("since", "7.0.0.0");
      }

      if (!var1.containsKey("MaxHTTPMessageSize")) {
         var3 = "getMaxHTTPMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxHTTPMessageSize";
         }

         var2 = new PropertyDescriptor("MaxHTTPMessageSize", KernelMBean.class, var3, var4);
         var1.put("MaxHTTPMessageSize", var2);
         var2.setValue("description", "<p>The maximum number of bytes allowed in messages that are received over the HTTP protocol. If you configure custom network channels for this server, each channel can override this maximum message size.</p>  <p>This maximum message size helps guard against a denial of service attack in which a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests.</p>  <p>A value of -1 causes the HTTP protocol to use the maximums that are specified elsewhere along the order of precedence.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("KernelMBean#getMaxMessageSize"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getMaxMessageSize")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getMaxMessageSize()} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxIIOPMessageSize")) {
         var3 = "getMaxIIOPMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxIIOPMessageSize";
         }

         var2 = new PropertyDescriptor("MaxIIOPMessageSize", KernelMBean.class, var3, var4);
         var1.put("MaxIIOPMessageSize", var2);
         var2.setValue("description", "<p>The maximum number of bytes allowed in messages that are received over the IIOP protocol. If you configure custom network channels for this server, each channel can override this maximum message size.</p>  <p>This maximum message size helps guard against a denial of service attack in which a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests.</p>  <p>A value of -1 causes the IIOP protocol to use the maximums that are specified elsewhere along the order of precedence.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("KernelMBean#getMaxMessageSize"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getMaxMessageSize")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getMaxMessageSize()} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxMessageSize")) {
         var3 = "getMaxMessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxMessageSize";
         }

         var2 = new PropertyDescriptor("MaxMessageSize", KernelMBean.class, var3, var4);
         var1.put("MaxMessageSize", var2);
         var2.setValue("description", "<p>The maximum number of bytes allowed in messages that are received over all supported protocols, unless overridden by a protocol-specific setting or a custom channel setting.</p>  <p>The order of precedence for setting message size maximums is as follows:</p>  <ol> <li> <p>A channel-wide maximum in a custom network channel.</p> </li>  <li> <p>A protocol-specific setting in the default network channel. <br/>See<ul> <li>{@link #MaxCOMMessageSize MaxCOMMessageSize}</li> <li>{@link #MaxHTTPMessageSize MaxHTTPMessageSize}</li> <li>{@link #MaxIIOPMessageSize MaxIIOPessageSize}</li> <li>{@link #MaxT3MessageSize MaxT3MessageSize}</li> </ul></p> </li>  <li> <p>The message maximum in this attribute.</p> </li> </ol>  <p>This maximum message size helps guard against a denial of service attack in which a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("weblogic.management.configuration.NetworkAccessPointMBean")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(10000000));
         var2.setValue("secureValue", new Integer(10000000));
         var2.setValue("legalMax", new Integer(2000000000));
         var2.setValue("legalMin", new Integer(4096));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxOpenSockCount")) {
         var3 = "getMaxOpenSockCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxOpenSockCount";
         }

         var2 = new PropertyDescriptor("MaxOpenSockCount", KernelMBean.class, var3, var4);
         var1.put("MaxOpenSockCount", var2);
         var2.setValue("description", "<p>The maximum number of open sockets allowed in server at a given point of time.</p>  <p>When the maximum threshold is reached, the server stops accepting new requests until the number of sockets drops below the threshold.</p>  <p>A value less than <code>0</code> indicates an unlimited size.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxT3MessageSize")) {
         var3 = "getMaxT3MessageSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxT3MessageSize";
         }

         var2 = new PropertyDescriptor("MaxT3MessageSize", KernelMBean.class, var3, var4);
         var1.put("MaxT3MessageSize", var2);
         var2.setValue("description", "<p>The maximum number of bytes allowed in messages that are received over the T3 protocol. If you configure custom network channels for this server, each channel can override this maximum message size.</p>  <p>This maximum message size helps guard against a denial of service attack in which a caller attempts to force the server to allocate more memory than is available thereby keeping the server from responding quickly to other requests.</p>  <p>A value of -1 causes the T3 protocol to use the maximums that are specified elsewhere along the order of precedence.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("KernelMBean#getMaxMessageSize"), BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#getMaxMessageSize")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("deprecated", "8.1.0.0 use {@link NetworkAccessPointMBean#getMaxMessageSize()} ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MessagingBridgeThreadPoolSize")) {
         var3 = "getMessagingBridgeThreadPoolSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMessagingBridgeThreadPoolSize";
         }

         var2 = new PropertyDescriptor("MessagingBridgeThreadPoolSize", KernelMBean.class, var3, var4);
         var1.put("MessagingBridgeThreadPoolSize", var2);
         var2.setValue("description", "<p>Returns the size of the messaging bridge execute thread pool.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(-1));
         var2.setValue("deprecated", "9.0.0.0 replaced by a Work Manager named weblogic.jms.MessagingBridge ");
      }

      if (!var1.containsKey("MuxerClass")) {
         var3 = "getMuxerClass";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMuxerClass";
         }

         var2 = new PropertyDescriptor("MuxerClass", KernelMBean.class, var3, var4);
         var1.put("MuxerClass", var2);
         var2.setValue("description", "<p>To enable non-blocking IO, enter <code>weblogic.socket.NIOSocketMuxer</code> in the <code>Muxer Class</code> field. </p> <p>The default value is null (not enabled). However, if <code>ExalogicOptimizationsEnabled</code> is <code>true</code>, the value is reset to <code>weblogic.socket.NIOSocketMuxer</code>. </p> <p>The Certicom SSL implementation is not supported with NIOSocketMuxer. If you need to secure internet communication, Oracle recommends implementing JSSE (Java Secure Socket Extension).</p> ");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setName";
         }

         var2 = new PropertyDescriptor("Name", KernelMBean.class, var3, var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>The user-specified name of this MBean instance.</p>  <p>This name is included as one of the key properties in the MBean's <code>javax.management.ObjectName</code>:<br clear=\"none\" /> <code>Name=<i>user-specified-name</i></code></p> ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("key", Boolean.TRUE);
      }

      if (!var1.containsKey("PeriodLength")) {
         var3 = "getPeriodLength";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPeriodLength";
         }

         var2 = new PropertyDescriptor("PeriodLength", KernelMBean.class, var3, var4);
         var1.put("PeriodLength", var2);
         var2.setValue("description", "<p>The time interval in milliseconds of the heartbeat period. A value of 0 indicates that heartbeats are turned off.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60000));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("SSL")) {
         var3 = "getSSL";
         var4 = null;
         var2 = new PropertyDescriptor("SSL", KernelMBean.class, var3, var4);
         var1.put("SSL", var2);
         var2.setValue("description", "<p>Returns the kernel's SSL configuration. An SSL MBean is always linked to a particular Kernel and cannot be changed. Individual attributes on the SSL MBean may be changed, as documented, but the MBean itself may not.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("SelfTuningThreadPoolSizeMax")) {
         var3 = "getSelfTuningThreadPoolSizeMax";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSelfTuningThreadPoolSizeMax";
         }

         var2 = new PropertyDescriptor("SelfTuningThreadPoolSizeMax", KernelMBean.class, var3, var4);
         var1.put("SelfTuningThreadPoolSizeMax", var2);
         var2.setValue("description", "<p>Sets the maximum thread pool size of the self-tuning thread pool.</p> <p> The self-tuning thread pool starts with the default size of 1. It grows and shrinks automatically as required. Setting this attribute changes the default max pool size. The active thread count will never increase beyond this value. This value defines the maximum number of threads permitted in the server. Note that the server will add threads only if it improves throughput. Measurements are taken every 2 seconds and the decision to increase or decrease the thread count is based on the current throughput measurement versus past values. </p> <p> This attribute is used only when {@link #setUse81StyleExecuteQueues} is turned off which is the default. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(400));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SelfTuningThreadPoolSizeMin")) {
         var3 = "getSelfTuningThreadPoolSizeMin";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSelfTuningThreadPoolSizeMin";
         }

         var2 = new PropertyDescriptor("SelfTuningThreadPoolSizeMin", KernelMBean.class, var3, var4);
         var1.put("SelfTuningThreadPoolSizeMin", var2);
         var2.setValue("description", "<p>Get the minimum thread pool size of the self-tuning thread pool.</p> <p> The self-tuning thread pool starts with the default size of 1. It grows and shrinks automatically as required. Setting this attribute changes the default min pool size. The thread count will never shrink below this value. It can add threads to improve throughput but will never decrease below the set minimum. </p> <p> This attribute is used only when {@link #setUse81StyleExecuteQueues} is turned off which is the default. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(1));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("SocketReaders")) {
         var3 = "getSocketReaders";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSocketReaders";
         }

         var2 = new PropertyDescriptor("SocketReaders", KernelMBean.class, var3, var4);
         var1.put("SocketReaders", var2);
         var2.setValue("description", "<p>The number of socket reader threads</p> ");
         setPropertyDescriptorDefault(var2, new Integer(-1));
         var2.setValue("legalMax", new Integer(65534));
         var2.setValue("legalMin", new Integer(-1));
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutFormat")) {
         var3 = "getStdoutFormat";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutFormat";
         }

         var2 = new PropertyDescriptor("StdoutFormat", KernelMBean.class, var3, var4);
         var1.put("StdoutFormat", var2);
         var2.setValue("description", "<p>The output format to use when logging to the console.</p> ");
         setPropertyDescriptorDefault(var2, "standard");
         var2.setValue("legalValues", new Object[]{"standard", "noid"});
         var2.setValue("deprecated", " ");
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutSeverityLevel")) {
         var3 = "getStdoutSeverityLevel";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutSeverityLevel";
         }

         var2 = new PropertyDescriptor("StdoutSeverityLevel", KernelMBean.class, var3, var4);
         var1.put("StdoutSeverityLevel", var2);
         var2.setValue("description", "<p>The minimum severity of a message that the server sends to standard out. (Requires you to enable sending messages to standard out.)</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isStdoutEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(32));
         var2.setValue("secureValue", new Integer(32));
         var2.setValue("legalValues", new Object[]{new Integer(256), new Integer(128), new Integer(64), new Integer(16), new Integer(8), new Integer(32), new Integer(4), new Integer(2), new Integer(1), new Integer(0)});
         var2.setValue("deprecated", "9.0.0.0 Replaced by LogMBean.StdoutSeverity.  For backward compatibility the changes to this attribute will be  propagated to the LogMBean. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (!var1.containsKey("StuckThreadMaxTime")) {
         var3 = "getStuckThreadMaxTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStuckThreadMaxTime";
         }

         var2 = new PropertyDescriptor("StuckThreadMaxTime", KernelMBean.class, var3, var4);
         var1.put("StuckThreadMaxTime", var2);
         var2.setValue("description", "<p>The number of seconds that a thread must be continually working before this server considers the thread stuck.</p>  <p>For example, if you set this to 600 seconds, WebLogic Server considers a thread to be \"stuck\" after 600 seconds of continuous use.</p>  <p>In Web Logic Server 9.x and later, it is recommended that you use the ServerFailureTriggerMBean in the OverloadProtectionMBean. The ServerFailureTriggerMBean transitions the server to a FAILED state after the specified number of stuck threads are detected. The OverloadProtectionMBean has options to suspend or shutdown a failed server. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(600));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("deprecated", "9.0.0.0 replaced by ServerFailureTriggerMBean.getMaxStuckThreadTime() ");
      }

      if (!var1.containsKey("StuckThreadTimerInterval")) {
         var3 = "getStuckThreadTimerInterval";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStuckThreadTimerInterval";
         }

         var2 = new PropertyDescriptor("StuckThreadTimerInterval", KernelMBean.class, var3, var4);
         var1.put("StuckThreadTimerInterval", var2);
         var2.setValue("description", "<p>The number of seconds after which WebLogic Server periodically scans threads to see if they have been continually working for the configured maximum length of time.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getStuckThreadMaxTime")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
      }

      if (!var1.containsKey("ThreadPoolPercentSocketReaders")) {
         var3 = "getThreadPoolPercentSocketReaders";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setThreadPoolPercentSocketReaders";
         }

         var2 = new PropertyDescriptor("ThreadPoolPercentSocketReaders", KernelMBean.class, var3, var4);
         var1.put("ThreadPoolPercentSocketReaders", var2);
         var2.setValue("description", "<p>The percentage of execute threads from the default queue that can be used as socket readers.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(33));
         var2.setValue("legalMax", new Integer(99));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TimedOutRefIsolationTime")) {
         var3 = "getTimedOutRefIsolationTime";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTimedOutRefIsolationTime";
         }

         var2 = new PropertyDescriptor("TimedOutRefIsolationTime", KernelMBean.class, var3, var4);
         var1.put("TimedOutRefIsolationTime", var2);
         var2.setValue("description", "<p>The amount of time in milli seconds a reference should not be used after a request timed out. The clusterable ref avoids using this remote ref for the period specified.</p> ");
         setPropertyDescriptorDefault(var2, new Long(0L));
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("exclude", Boolean.TRUE);
      }

      if (!var1.containsKey("Use81StyleExecuteQueues")) {
         var3 = "getUse81StyleExecuteQueues";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUse81StyleExecuteQueues";
         }

         var2 = new PropertyDescriptor("Use81StyleExecuteQueues", KernelMBean.class, var3, var4);
         var1.put("Use81StyleExecuteQueues", var2);
         var2.setValue("description", "<p>Backward compatibility mode to switch to 8.1 execute queues instead of WorkManagers. Each of the WorkManagers is converted to an individual execute queue. Setting this attribute requires a server restart. </p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getUse81StyleExecuteQueues")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.4.0", (String)null, this.targetVersion) && !var1.containsKey("AddWorkManagerThreadsByCpuCount")) {
         var3 = "isAddWorkManagerThreadsByCpuCount";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAddWorkManagerThreadsByCpuCount";
         }

         var2 = new PropertyDescriptor("AddWorkManagerThreadsByCpuCount", KernelMBean.class, var3, var4);
         var1.put("AddWorkManagerThreadsByCpuCount", var2);
         var2.setValue("description", "Enables increased efficiency of the self-tuning thread pool by aligning it with the Exalogic processor architecture threading capabilities. Enabling this attribute increases efficiency during I/O in environments with high network throughput and should be used only when configuring a WebLogic domain for Oracle Exalogic. For more information, see \"Enabling Exalogic-Specific Enhancements in Oracle WebLogic Server 11g Release 1 (10.3.4)\" in the Oracle Exalogic Deployment Guide. ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3.4.0");
      }

      if (!var1.containsKey("GatheredWritesEnabled")) {
         var3 = "isGatheredWritesEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setGatheredWritesEnabled";
         }

         var2 = new PropertyDescriptor("GatheredWritesEnabled", KernelMBean.class, var3, var4);
         var1.put("GatheredWritesEnabled", var2);
         var2.setValue("description", "Enables gathered writes over NIO socket channels. Enabling this attribute increases efficiency during I/O in environments with high network throughput and should be used only when configuring a WebLogic domain for Oracle Exalogic. For more information, see \"Enabling Exalogic-Specific Enhancements in Oracle WebLogic Server 11g Release 1 (10.3.4)\" in the Oracle Exalogic Deployment Guide. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("InstrumentStackTraceEnabled")) {
         var3 = "isInstrumentStackTraceEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setInstrumentStackTraceEnabled";
         }

         var2 = new PropertyDescriptor("InstrumentStackTraceEnabled", KernelMBean.class, var3, var4);
         var1.put("InstrumentStackTraceEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the server returns stack traces for RMI calls that generate exceptions.</p>  <p>With RMI stack tracking enabled, if a client issues an RMI call to a server subsystem or to a module running within the server, and if the subsystem or module generates an exception that includes a stack trace, the server will return the exception as well as the stack trace. With it disabled, the server will return the exception without the stack trace details.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("LogRemoteExceptionsEnabled")) {
         var3 = "isLogRemoteExceptionsEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setLogRemoteExceptionsEnabled";
         }

         var2 = new PropertyDescriptor("LogRemoteExceptionsEnabled", KernelMBean.class, var3, var4);
         var1.put("LogRemoteExceptionsEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the server message log includes exceptions that are raised in remote systems.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(true));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("NativeIOEnabled")) {
         var3 = "isNativeIOEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNativeIOEnabled";
         }

         var2 = new PropertyDescriptor("NativeIOEnabled", KernelMBean.class, var3, var4);
         var1.put("NativeIOEnabled", var2);
         var2.setValue("description", "<p>Specifies whether native I/O is enabled for the server.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#getSocketReaderTimeoutMaxMillis")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
      }

      if (!var1.containsKey("OutboundEnabled")) {
         var3 = "isOutboundEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOutboundEnabled";
         }

         var2 = new PropertyDescriptor("OutboundEnabled", KernelMBean.class, var3, var4);
         var1.put("OutboundEnabled", var2);
         var2.setValue("description", "<p>Specifies whether new server-to-server connections may consider the default server channel when initiating a connection. This is only relevant if the connection needs to be bound to the default listen address. This will only work for binary protocols that support both outbound and inbound traffic.</p>  <p>When this feature is not enabled, connections are initiated using a local address selected by the underlying hardware. For the default channel this is usually what is wanted for IP-routing to be effective. Note that since the default is false, other outbound channels will be considered in preference to the default channel.</p>  <p>Default administration channels, created when the domain-wide administration port is turned on, are always considered and bound when initiating an administrative connection. To allow IP-routing for administration traffic create custom admin with {@link NetworkAccessPointMBean#isOutboundEnabled isOutboundEnabled} set to false instead of enabling the domain-wide ADMIN port.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("NetworkAccessPointMBean#isOutboundEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("OutboundPrivateKeyEnabled")) {
         var3 = "isOutboundPrivateKeyEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setOutboundPrivateKeyEnabled";
         }

         var2 = new PropertyDescriptor("OutboundPrivateKeyEnabled", KernelMBean.class, var3, var4);
         var1.put("OutboundPrivateKeyEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the SSL identity specified by {@link SSLMBean#ServerPrivateKeyAlias SSLMBean#ServerPrivateKeyAlias} for this server should be used for outbound SSL connections on the default server channel. In normal circumstances the outbound identity is determined by the caller's environment. ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isOutboundEnabled")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ReverseDNSAllowed")) {
         var3 = "isReverseDNSAllowed";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReverseDNSAllowed";
         }

         var2 = new PropertyDescriptor("ReverseDNSAllowed", KernelMBean.class, var3, var4);
         var1.put("ReverseDNSAllowed", var2);
         var2.setValue("description", "<p>Specifies whether the kernel is allowed to perform reverse DNS lookups.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ScatteredReadsEnabled")) {
         var3 = "isScatteredReadsEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setScatteredReadsEnabled";
         }

         var2 = new PropertyDescriptor("ScatteredReadsEnabled", KernelMBean.class, var3, var4);
         var1.put("ScatteredReadsEnabled", var2);
         var2.setValue("description", "Enables scattered reads over NIO socket channels. Enabling this attribute increases efficiency during I/O in environments with high network throughput and should be used only when configuring a WebLogic domain for Oracle Exalogic. For more information, see \"Enabling Exalogic-Specific Enhancements in Oracle WebLogic Server 11g Release 1 (10.3.4)\" in the Oracle Exalogic Deployment Guide. ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SocketBufferSizeAsChunkSize")) {
         var3 = "isSocketBufferSizeAsChunkSize";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSocketBufferSizeAsChunkSize";
         }

         var2 = new PropertyDescriptor("SocketBufferSizeAsChunkSize", KernelMBean.class, var3, var4);
         var1.put("SocketBufferSizeAsChunkSize", var2);
         var2.setValue("description", "<p>Specifies whether the server's buffer size for sending or receiving data through a raw socket should be set to 4KB. </p> <p>Otherwise, the server does not impose a limit to the buffer size and defers to the operating system. This option is useful only on some operating systems for improving performance. It should be disabled in most environments.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("configurable", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutDebugEnabled")) {
         var3 = "isStdoutDebugEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutDebugEnabled";
         }

         var2 = new PropertyDescriptor("StdoutDebugEnabled", KernelMBean.class, var3, var4);
         var1.put("StdoutDebugEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the server sends messages of the <code>DEBUG</code> severity to standard out in addition to the log file. (Requires you to enable sending messages to standard out.)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
         var2.setValue("secureValue", new Boolean(false));
         var2.setValue("deprecated", "9.0.0.0 replaced by LogMBean.StdoutSeverity For backward compatibility the changes to this attribute will be propagated to the LogMBean. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutEnabled")) {
         var3 = "isStdoutEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutEnabled";
         }

         var2 = new PropertyDescriptor("StdoutEnabled", KernelMBean.class, var3, var4);
         var1.put("StdoutEnabled", var2);
         var2.setValue("description", "<p>Specifies whether the server sends messages to standard out in addition to the log file.</p>  <p>Other settings configure the minimum severity of a message that the server sends to standard out.</p> ");
         var5 = new String[]{BeanInfoHelper.encodeEntities("#isStdoutDebugEnabled"), BeanInfoHelper.encodeEntities("#getStdoutSeverityLevel")};
         var2.setValue("see", var5);
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("deprecated", "9.0.0.0 replaced by LogMBean.StdoutSeverity, for backward compatibility the changes to this attribute will be propagated to the LogMBean. ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant((String)null, "9.0.0.0", this.targetVersion) && !var1.containsKey("StdoutLogStack")) {
         var3 = "isStdoutLogStack";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setStdoutLogStack";
         }

         var2 = new PropertyDescriptor("StdoutLogStack", KernelMBean.class, var3, var4);
         var1.put("StdoutLogStack", var2);
         var2.setValue("description", "<p>Specifies whether to dump stack traces to the console when included in logged message.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(true));
         var2.setValue("deprecated", "9.0.0.0 ");
         var2.setValue("dynamic", Boolean.TRUE);
         var2.setValue("obsolete", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.6.0", (String)null, this.targetVersion) && !var1.containsKey("UseConcurrentQueueForRequestManager")) {
         var3 = "isUseConcurrentQueueForRequestManager";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setUseConcurrentQueueForRequestManager";
         }

         var2 = new PropertyDescriptor("UseConcurrentQueueForRequestManager", KernelMBean.class, var3, var4);
         var1.put("UseConcurrentQueueForRequestManager", var2);
         var2.setValue("description", "<p>Reduces lock contention by using concurrent buffer queue to park incoming requests. Enabling this attribute increases throughput as requests are scheduled with out acquiring any locks. </p> ");
         var2.setValue("dynamic", Boolean.FALSE);
         var2.setValue("since", "10.3.6.0");
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = KernelMBean.class.getMethod("createExecuteQueue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "of the new queue ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Create a new execute queue</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ExecuteQueues");
      }

      var3 = KernelMBean.class.getMethod("destroyExecuteQueue", ExecuteQueueMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("queue", "to destroy ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Destroy execute queue</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "factory");
         var2.setValue("property", "ExecuteQueues");
      }

   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = KernelMBean.class.getMethod("lookupExecuteQueue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         MethodDescriptor var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "finder");
         var2.setValue("property", "ExecuteQueues");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = KernelMBean.class.getMethod("freezeCurrentValue", String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has not been set explicitly, and if the attribute has a default value, this operation forces the MBean to persist the default value.</p>  <p>Unless you use this operation, the default value is not saved and is subject to change if you update to a newer release of WebLogic Server. Invoking this operation isolates this MBean from the effects of such changes.</p>  <dl> <dt>Note:</dt>  <dd> <p>To insure that you are freezing the default value, invoke the <code>restoreDefaultValue</code> operation before you invoke this.</p> </dd> </dl>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute for which some other value has been set.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = KernelMBean.class.getMethod("restoreDefaultValue", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("attributeName", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5) && !this.readOnly) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "9.0.0.0 ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>If the specified attribute has a default value, this operation removes any value that has been set explicitly and causes the attribute to use the default value.</p>  <p>Default values are subject to change if you update to a newer release of WebLogic Server. To prevent the value from changing if you update to a newer release, invoke the <code>freezeCurrentValue</code> operation.</p>  <p>This operation has no effect if you invoke it on an attribute that does not provide a default value or on an attribute that is already using the default.</p> ");
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
