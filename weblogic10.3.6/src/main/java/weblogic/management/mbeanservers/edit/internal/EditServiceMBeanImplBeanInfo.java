package weblogic.management.mbeanservers.edit.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.mbeanservers.edit.EditServiceMBean;
import weblogic.management.mbeanservers.internal.ServiceImplBeanInfo;

public class EditServiceMBeanImplBeanInfo extends ServiceImplBeanInfo {
   public static Class INTERFACE_CLASS = EditServiceMBean.class;

   public EditServiceMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public EditServiceMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = EditServiceMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "9.0.0.0");
      var2.setValue("package", "weblogic.management.mbeanservers.edit.internal");
      String var3 = (new String("<p>Provides the entry point for managing the configuration of the current WebLogic Server domain.</p>  <p>This MBean is available only on the Administration Server.</p>  <p>The <code>javax.management.ObjectName</code> of this MBean is \"<code>com.bea:Name=EditService,Type=weblogic.management.mbeanservers.edit.EditServiceMBean</code>\".</p>  <p>This is the only object name that a JMX client needs to navigate and edit the hierarchy of editiable WebLogic Server MBeans. To start editing MBean attributes, a JMX client invokes the <code>javax.management.MBeanServerConnection.getAttribute()</code> method and passes the following as parameters:</p>  <ul> <li> <p>The <code>ObjectName</code> of this service MBean</p> </li>  <li> <p><code>\"ConfigurationManager\"</code>, which is the attribute that contains the <code>ConfigurationManagerMBean</code>. The <code>ConfigurationManagerMBean</code> contains attributes and operations to start/stop edit sessions, and save, undo, and activate configuration changes.</p> </li> </ul>  <p>This method call returns the <code>ObjectName</code> of the <code>ConfigurationManagerMBean</code>.</p>  <p>For example:<br clear=\"none\" /> <code>ObjectName es = new ObjectName(\"com.bea:Name=EditService,Type=weblogic.management.mbeanservers.edit.EditServiceMBean\");</code> <br clear=\"none\" /> <code>// Get the ObjectName of the domain's ConfigurationManagerMBean by getting the value<br clear=\"none\" /> // of the EditServiceMBean ConfigurationManager attribute<br clear=\"none\" /> ObjectName cfg =<br clear=\"none\" /> (ObjectName) MBeanServerConnection.getAttribute(es, \"ConfigurationManager\");<br clear=\"none\" /> </code></p>  <p>After getting this <code>ObjectName</code>, the client invokes the <code>ConfigurationManagerMBean</code> <code>startEdit()</code> operation, which returns the <code>ObjectName</code> of the editable <code>DomainMBean</code>. Clients can change the values of <code>DomainMBean</code> attributes or navigate the hierarchy of MBeans below <code>DomainMBean</code> and change their attribute values.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.mbeanservers.edit.EditServiceMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ConfigurationManager")) {
         var3 = "getConfigurationManager";
         var4 = null;
         var2 = new PropertyDescriptor("ConfigurationManager", EditServiceMBean.class, var3, (String)var4);
         var1.put("ConfigurationManager", var2);
         var2.setValue("description", "<p>Contains the <code>ConfigurationManagerMBean</code> for this domain, which has attributes and operations to start/stop edit sessions, navigate the pending hierarchy of configuration MBeans, and save, undo, and activate configuration changes..</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("DomainConfiguration")) {
         var3 = "getDomainConfiguration";
         var4 = null;
         var2 = new PropertyDescriptor("DomainConfiguration", EditServiceMBean.class, var3, (String)var4);
         var1.put("DomainConfiguration", var2);
         var2.setValue("description", "<p>Contains the pending <code>DomainMBean</code>, which is the root of the pending configuration MBean hierarchy. You cannot use this MBean to modify a domain's configuration unless you have already started an edit session using the {@link ConfigurationManagerMBean#startEdit startEdit} operation in the <code>ConfigurationManagerMBean</code>.</p>  <p>If you have already started an edit session, you can use this attribute to get the editable <code>DomainMBean</code> and navigate its hierarchy, however the process of starting an edit session returns the editable <code>DomainMBean</code> (making it unnecessary get the value of this attribute, because you already have the <code>DomainMBean</code>).</p>  <p>The <code>ConfigurationManagerMBean</code> provides this attribute mostly to enable JMX clients to view the in-memory state of the pending configuration MBean hierarchy without having to start an edit session. For example, if userA starts an edit session and changes the value of an MBean attribute, userB can get <code>DomainMBean</code> from this (<code>ConfigurationManagerMBean DomainConfiguration</code>) attribute and see the changes from userA, even if userA's edit session is still active.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Name")) {
         var3 = "getName";
         var4 = null;
         var2 = new PropertyDescriptor("Name", EditServiceMBean.class, var3, (String)var4);
         var1.put("Name", var2);
         var2.setValue("description", "<p>A unique key that WebLogic Server generates to identify the current instance of this MBean type.</p>  <p>For a singleton, such as <code>DomainRuntimeServiceMBean</code>, this key is often just the bean's short class name.</p> ");
      }

      if (!var1.containsKey("ParentAttribute")) {
         var3 = "getParentAttribute";
         var4 = null;
         var2 = new PropertyDescriptor("ParentAttribute", EditServiceMBean.class, var3, (String)var4);
         var1.put("ParentAttribute", var2);
         var2.setValue("description", "<p>The name of the attribute of the parent that refers to this bean</p> ");
      }

      if (!var1.containsKey("ParentService")) {
         var3 = "getParentService";
         var4 = null;
         var2 = new PropertyDescriptor("ParentService", EditServiceMBean.class, var3, (String)var4);
         var1.put("ParentService", var2);
         var2.setValue("description", "<p>The MBean that created the current MBean instance.</p>  <p>In the data model for WebLogic Server MBeans, an MBean that creates another MBean is called a <i>parent</i>. MBeans at the top of the hierarchy have no parents.</p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("Path")) {
         var3 = "getPath";
         var4 = null;
         var2 = new PropertyDescriptor("Path", EditServiceMBean.class, var3, (String)var4);
         var1.put("Path", var2);
         var2.setValue("description", "<p>Returns the path to the bean relative to the reoot of the heirarchy of services</p> ");
      }

      if (!var1.containsKey("RecordingManager")) {
         var3 = "getRecordingManager";
         var4 = null;
         var2 = new PropertyDescriptor("RecordingManager", EditServiceMBean.class, var3, (String)var4);
         var1.put("RecordingManager", var2);
         var2.setValue("description", "<p>Contains the <code>RecordingManagerMBean</code> for this domain, which has attributes and operations to start/stop WLST recording sessions and write scripts/comments to the recording file.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("Type")) {
         var3 = "getType";
         var4 = null;
         var2 = new PropertyDescriptor("Type", EditServiceMBean.class, var3, (String)var4);
         var1.put("Type", var2);
         var2.setValue("description", "<p>The MBean type for this instance. This is useful for MBean types that support multiple intances, such as <code>ActivationTaskMBean</code>.</p> ");
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
