package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class SNMPAgentDeploymentMBeanImplBeanInfo extends SNMPAgentMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = SNMPAgentDeploymentMBean.class;

   public SNMPAgentDeploymentMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public SNMPAgentDeploymentMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = SNMPAgentDeploymentMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This MBean is an SNMP agent that can be targeted to instances of WebLogic Server. </p> <p>With SNMP, you configure <b>agents</b> to gather and send data about managed resources in response to a request from <b>managers</b>. You can also configure agents to issue unsolicited reports to managers when they detect predefined thresholds or conditions on a managed resource.</p> <p>In a WebLogic Server domain, you can choose a centralized or de-centralized model for SNMP monitoring and communication:</p> <ul><li>In a centralized model, you create an instance of this MBean and target it only to the Administration Server. This agent communicates with all Managed Servers in the domain. SNMP managers communicate only with the SNMP agent on the Administration Server. This model is convenient but introduces performance overhead in WebLogic Server. In addition, if the Administration Server is unavailable, you cannot monitor the domain through SNMP.</li> <li>In a de-centralized model, you create an instance of this MBean and target it to each Managed Server that you want to monitor. (Alternatively, you can create multiple instances of this MBean and target each instance to an individual Managed Server.) Your SNMP manager must communicate with the agents on individual Managed Servers.</li></ul> <p>To support domains that were created with WebLogic Server release 9.2 and earlier, a domain also hosts a singleton SNMP agent whose scope is the entire domain (see {@link SNMPAgentMBean}). SNMPAgentMBean offers the same features as an instance of this MBean (SNMPAgentDeploymentMBean) that you have targeted as described in the centralized model above. However, the underlying implementation of SNMPAgentMBean is different and it will eventually be deprecated. SNMPAgentMBean is overridden if you target an instance of this MBean to the Administration Server.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.SNMPAgentDeploymentMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("DeploymentOrder")) {
         var3 = "getDeploymentOrder";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setDeploymentOrder";
         }

         var2 = new PropertyDescriptor("DeploymentOrder", SNMPAgentDeploymentMBean.class, var3, var4);
         var1.put("DeploymentOrder", var2);
         var2.setValue("description", "<p>A priority that the server uses to determine when it deploys an item. The priority is relative to other deployable items of the same type.</p>  <p>For example, the server prioritizes and deploys all EJBs before it prioritizes and deploys startup classes.</p>  <p>Items with the lowest Deployment Order value are deployed first. There is no guarantee on the order of deployments with equal Deployment Order values. There is no guarantee of ordering across clusters.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(1000));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Targets")) {
         var3 = "getTargets";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTargets";
         }

         var2 = new PropertyDescriptor("Targets", SNMPAgentDeploymentMBean.class, var3, var4);
         var1.put("Targets", var2);
         var2.setValue("description", "<p>You must select a target on which an MBean will be deployed from this list of the targets in the current domain on which this item can be deployed. Targets must be either servers or clusters. The deployment will only occur once if deployments overlap.</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("adder", "addTarget");
         var2.setValue("remover", "removeTarget");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = SNMPAgentDeploymentMBean.class.getMethod("addTarget", TargetMBean.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("target", "The feature to be added to the Target attribute ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>You can add a target to specify additional servers on which the deployment can be deployed. The targets must be either clusters or servers.</p> ");
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
      }

      var3 = SNMPAgentDeploymentMBean.class.getMethod("removeTarget", TargetMBean.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("target", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes the value of the addTarget attribute.</p> ");
         String[] var6 = new String[]{BeanInfoHelper.encodeEntities("#addTarget")};
         var2.setValue("see", var6);
         var2.setValue("role", "collection");
         var2.setValue("property", "Targets");
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
