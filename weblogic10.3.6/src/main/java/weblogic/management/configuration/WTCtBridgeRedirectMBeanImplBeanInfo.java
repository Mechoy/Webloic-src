package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WTCtBridgeRedirectMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCtBridgeRedirectMBean.class;

   public WTCtBridgeRedirectMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCtBridgeRedirectMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCtBridgeRedirectMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This interface provides access to the WTC tBridge Redirect configuration attributes. The methods defined herein are applicable for tBridge configuration at the WLS domain level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WTCtBridgeRedirectMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("Direction")) {
         var3 = "getDirection";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDirection";
         }

         var2 = new PropertyDescriptor("Direction", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("Direction", var2);
         var2.setValue("description", "<p>The direction of data flow. At least one redirection must be specified or the Tuxedo queuing bridge will fail to start and an error will be logged.</p>  <p>Each defined direction is handled by starting a new thread.</p>  <p style=\"font-weight: bold\">Redirection keywords:</p>  <ul> <li><code>JmsQ2TuxQ</code>  <p>- From JMS to TUXEDO /Q</p> </li>  <li><code>TuxQ2JmsQ</code>  <p>- From TUXEDO /Q to JMS</p> </li>  <li><code>JmsQ2TuxS</code>  <p>- From JMS to TUXEDO Service reply to JMS</p> </li>  <li><code>JmsQ2JmsQ</code>  <p>- From JMS to JMS</p> </li> </ul> ");
         setPropertyDescriptorDefault(var2, "JmsQ2TuxQ");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("legalValues", new Object[]{"JmsQ2TuxQ", "TuxQ2JmsQ", "JmsQ2TuxS", "JmsQ2JmsQ"});
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MetaDataFile")) {
         var3 = "getMetaDataFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMetaDataFile";
         }

         var2 = new PropertyDescriptor("MetaDataFile", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("MetaDataFile", var2);
         var2.setValue("description", "<p>The name of the metadata file URL used to pass the call to the XML-to-non-XML WebLogic XML Translator (WLXT).</p>  <p><i>Note:</i> Not supported for this release.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ReplyQ")) {
         var3 = "getReplyQ";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setReplyQ";
         }

         var2 = new PropertyDescriptor("ReplyQ", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("ReplyQ", var2);
         var2.setValue("description", "<p>The name of the JMS queue used specifically for synchronous calls to a Tuxedo service. The response is returned to the JMS ReplyQ.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SourceAccessPoint")) {
         var3 = "getSourceAccessPoint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSourceAccessPoint";
         }

         var2 = new PropertyDescriptor("SourceAccessPoint", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("SourceAccessPoint", var2);
         var2.setValue("description", "<p>The name of the local or remote access point where the source is located.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SourceName")) {
         var3 = "getSourceName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSourceName";
         }

         var2 = new PropertyDescriptor("SourceName", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("SourceName", var2);
         var2.setValue("description", "<p>The name of a source queue or service. Specifies a JMS queue name, a Tuxedo queue name, or the name of a Tuxedo service.</p> ");
         setPropertyDescriptorDefault(var2, "mySource");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("SourceQspace")) {
         var3 = "getSourceQspace";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setSourceQspace";
         }

         var2 = new PropertyDescriptor("SourceQspace", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("SourceQspace", var2);
         var2.setValue("description", "<p>The name of the Qspace for a source location.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TargetAccessPoint")) {
         var3 = "getTargetAccessPoint";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargetAccessPoint";
         }

         var2 = new PropertyDescriptor("TargetAccessPoint", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("TargetAccessPoint", var2);
         var2.setValue("description", "<p>The name of the local or remote access point where the target is located.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TargetName")) {
         var3 = "getTargetName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargetName";
         }

         var2 = new PropertyDescriptor("TargetName", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("TargetName", var2);
         var2.setValue("description", "<p>The name of the target queue or service. Specifies a JMS queue name, a Tuxedo queue name, or the name of a Tuxedo service.</p> ");
         setPropertyDescriptorDefault(var2, "myTarget");
         var2.setValue("legalNull", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TargetQspace")) {
         var3 = "getTargetQspace";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargetQspace";
         }

         var2 = new PropertyDescriptor("TargetQspace", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("TargetQspace", var2);
         var2.setValue("description", "<p>The name of the Qspace for a target location.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TranslateFML")) {
         var3 = "getTranslateFML";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTranslateFML";
         }

         var2 = new PropertyDescriptor("TranslateFML", WTCtBridgeRedirectMBean.class, var3, var4);
         var1.put("TranslateFML", var2);
         var2.setValue("description", "<p>The type of XML/FML translation.</p>  <p><code>NO</code> indicates that no data translation is performed. <code>FLAT</code> indicates that the message payload is transformed using the WebLogic Tuxedo Connector translator. <code>WLXT</code> indicates that translation is performed by the XML-to-non-XML WebLogic XML Translator (WLXT).</p>  <p><i>Note:</i> WLXT is not supported for this release.</p> ");
         setPropertyDescriptorDefault(var2, "NO");
         var2.setValue("legalValues", new Object[]{"NO", "FLAT", "WLXT"});
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
