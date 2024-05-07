package weblogic.management.deploy.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.deploy.DeploymentData;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.DeployerRuntimeMBean;

public class DeployerRuntimeImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = DeployerRuntimeMBean.class;

   public DeployerRuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public DeployerRuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = DeployerRuntimeImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.deploy.api.spi.WebLogicDeploymentManager} ");
      var2.setValue("package", "weblogic.management.deploy.internal");
      String var3 = (new String("<p>This MBean is the user API for initiating deployment requests and exists only on an Admin Server. To access this MBean use {@link weblogic.management.deploy.DeployerRuntime#getDeployerRuntime()}.</p>  <p>The deployment methods in this MBean provide access to the 2 phase deployment protocol. This protocol is only supported on WLS 7.x servers and later. If no target servers in an activate request are known to be pre-release 7 servers, then the 2 phase protocol is used. Otherwise the deployment will use the WLS 6.x deployment protocol, even if some target servers are at release 7.</p>  <p>The deployment process involves a number of state changes: start&lt;--&gt;staged&lt;--&gt;prepared&lt;--&gt;active. The methods in this MBean provide the means for changing the state of an application, as follows:</p>  <p>activate: places application in active state from any other state</p>  <p>deactivate: places application in prepared state from active state</p>  <p>unprepare: places application in staged state from active and prepared state</p>  <p>remove: places application in start state from any other state</p>  <p>Activating An Application</p>  <p>The basic process of deploying an application is shown in the following example:</p>  <p /> <pre xml:space=\"preserve\"> <tt>DeployerRuntimeMBean deployer = getDeployerRuntime(userName, password, adminURL); try { DeploymentTaskRuntimeMBean task = deployer.activate(sourceFile, appName, staging, info, id); } catch (ManagementException me) { System.out.println(\"Deployment failed: \"+me.getMessage()); } </tt> </pre>  <p>In this example, <tt>sourceFile</tt> is the path to the application. If the application is an EAR, then <tt>sourceFile</tt> would name the EAR archive or the root directory if it is not archived. Similarly, if the application is not an EAR, but a standalone module (web app or EJB), the <tt>sourceFile</tt> argument would be the path to the module archive or directory.</p>  <p>The <tt>sourceFile</tt> argument can be null, indicating that this is a redeployment and that the source is unchanged from a previous deployment.</p>  <p>The <tt>appName</tt> argument is the name to be given to the application. If this is a new deployment, an {@link weblogic.management.configuration.ApplicationMBean} is created. For redeployments, an existing ApplicationMBean with <tt>appName</tt> is used as the application's configured state.</p>  <p>The <tt>info</tt> argument is a {@link weblogic.management.deploy.DeploymentData} object which is used to qualify the deployment in terms of targets of the deployment and an optional list of files which are to be refreshed during a redeploy.</p>  <p>The staging argument is used to specify whether the application is to be staged to the target servers. This argument may be null (use {@link weblogic.management.configuration.ServerMBean#getStagingMode}), \"stage\", or \"nostage\". Staging is the process of uploading the application file to the target servers' staging area, defined in {@link weblogic.management.configuration.ServerMBean#getStagingDirectoryName}.</p>  <p>The <tt>id</tt> argument allows the caller to name the deployment task. Care should be taken here as the tag must be unique. The recommendation is to generally use null for this argument, allowing the system to generate a unique tag.</p>  <p>The deployment process runs asynchronously to the invoker; it will initiate the task then return the {@link weblogic.management.runtime.DeploymentTaskRuntimeMBean} object representing the task to the client. This object may be used to track status of the task. If the client wants to wait until the task completes then the following is a basic method for doing this.</p>  <p /> <pre xml:space=\"preserve\"> <tt>while (task.isRunning()) { try { Thread.sleep(oneSecond); } catch (InterruptedException ie) {} } </tt> </pre>  <p>Cancelling A Deployment</p>  <p>Note that a task will not complete until all targets have either completed the deployment or failed. If one of the targets is inactive, the task will remain active until the server starts or the task is cancelled. Cancelling a deployment task is accomplished as follows:</p>  <p /> <pre xml:space=\"preserve\"> <tt>if (task.isRunning()) { try { task.cancel(); } catch (Exception e) {} } </tt> </pre>  <p>Targeting Specific Servers</p>  <p>The folowing examples show how to be more specific when targeting a deployment.</p>  <p /> <pre xml:space=\"preserve\"> <tt>DeploymentData info = new DeploymentData(); info.addTarget(server1,null); // adds server1 as target for all modules // in app String[] mods = { \"web-module\",\"ejb\" }; info.addTarget(server2,mods); // adds server2 as target for modules // web-module and ejb deployer.activate(sourceFile, appName, info, null, null);   // refreshes the hello.jsp file on all currently targeted servers. // The \"jsps\" directory is // relative to the root of the application. String[] files = { \"jsps/hello.jsp\" }; DeploymentData info = new DeploymentData(files); deployer.activate(null, appName, null, info, null);  </tt> </pre>  <p>Deactivating An Application</p>  <p>To deactivate an application is to suspend it. The application files remain staged on the target servers, and can be reactivated without restaging. It should be noted that deactivating an application does not unload any of its classes. To do so requires an <tt>unprepare</tt> operation (see below). The following example show appName being deactivated, then subsequently reactivated on all configured servers.</p>  <pre xml:space=\"preserve\"> <tt>deployer.deactivate(appName, null, null); . . . deployer.activate(null, appName, null, null, null);  </tt> </pre>  <p>Unpreparing An Application</p>  <p>To unprepare an application is to suspend and unload it. The application files remain staged on the target servers, and any relevant classes are unloaded. If the application is to be reactivated with new class files, unprepare is the correct approach, rather than deactivate. The following example show appName being unprepared, then subsequently reactivated on all configured servers.</p>  <pre xml:space=\"preserve\"> <tt>deployer.unprepare(appName, null, null); . . . deployer.activate(sourceFile, appName, null, null, null);  </tt> </pre>  <p>Removing An Application</p>  <p>Removing an application involves deactivation, unstaging and possible removal of the application. After removing an application from a managed server it is deconfigured from that server. If no servers remain targeted by the application, the entire configuration of the application is removed. Removal does not touch the application source, but will remove staged copies of the application.</p>  <pre xml:space=\"preserve\"> <tt>// this completely removes an application from the domain configuration deployer.remove(appName, null, null); </tt> </pre>  <p>Tracking Deployment Status</p>  <p>Once initiated, a deployment task can be monitored via notifications or polling. Use of notifications relies on JMX Notifications on the relevant ApplicationMBean and is accomplished as follows:</p>  <pre xml:space=\"preserve\"> <tt>package examples.deploy;  import java.io.Serializable; import javax.management.MBeanServer; import javax.management.Notification; import javax.management.NotificationFilter; import weblogic.management.DeploymentNotification; import weblogic.management.Helper; import weblogic.management.MBeanHome; import weblogic.management.RemoteNotificationListener; import weblogic.management.configuration.ApplicationMBean; import weblogic.management.deploy.DeploymentData; import weblogic.management.deploy.DeployerRuntime; import weblogic.management.runtime.DeployerRuntimeMBean; import weblogic.management.runtime.DeploymentTaskRuntimeMBean;  // // // This example activates and application and prints the resulting // notifications generated during the processing of the deployment. // The args passed to this // program are: // arg1: userid // arg2: password // arg3: admin URL // arg4: app name // arg5: app source // arg6: target server // //  public class Activater implements Serializable {  private static String userid; private static String password; private static String url; private static String name; private static String source; private static String server;  void deploy() { try { // Get access to MBeanHome MBeanHome home = Helper.getAdminMBeanHome(userid, password, url); // Get the deployer DeployerRuntimeMBean deployer = DeployerRuntime.getDeployerRuntime(home); // Build the DeploymentData object DeploymentData info = new DeploymentData(); info.addTarget(server, null); // Create the deployment task. Last arg indicates to just // create the task, but not initiate it DeploymentTaskRuntimeMBean task = deployer.activate(source,name,null,info,null,false); // Register for notifications ApplicationMBean app = task.getDeploymentObject(); MBeanServer mBeanServer = home.getMBeanServer(); mBeanServer.addNotificationListener( app.getObjectName(), new DeployListener(), new DeployFilter(), null ); // Start the task task.start(); System.out.println(task.getDescription()); // wait until finished while (task.isRunning()) { try { Thread.sleep(1000); } catch (InterruptedException ie) { System.out.println(task.getStatus()); } } } catch (Exception e) { System.out.println(e.getMessage()); } }   public static void main(String[] argv) throws Exception { if (argv.length == 6) { userid = argv[0]; password = argv[1]; url = argv[2]; name = argv[3]; source = argv[4]; server = argv[5]; Activater activater = new Activater(); activater.deploy(); System.exit(0); } }  // Inner classes for handling notifications class DeployListener implements RemoteNotificationListener {  public void handleNotification(Notification notification, java.lang.Object handback) { System.out.println( notification.getMessage() ); } };   // inner class for filtering notifications class DeployFilter implements NotificationFilter, Serializable {  public boolean isNotificationEnabled( Notification n ) { return ( n instanceof DeploymentNotification ); }  } }  </tt> </pre>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.DeployerRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      if (!var1.containsKey("DeploymentTaskRuntimes")) {
         String var3 = "getDeploymentTaskRuntimes";
         Object var4 = null;
         var2 = new PropertyDescriptor("DeploymentTaskRuntimes", DeployerRuntimeMBean.class, var3, (String)var4);
         var1.put("DeploymentTaskRuntimes", var2);
         var2.setValue("description", "Return the deployment task runtime mbeans. ");
         var2.setValue("relationship", "containment");
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
      Method var3 = DeployerRuntimeMBean.class.getMethod("activate", String.class, String.class, String.class, DeploymentData.class, String.class);
      ParameterDescriptor[] var4 = new ParameterDescriptor[]{createParameterDescriptor("source", "is the path to the application. If null, the configured path is used. "), createParameterDescriptor("name", "is the configured name of the application. "), createParameterDescriptor("stagingMode", "the value that will be set on the ApplicationMBean for this deployment &quot;stage&quot;, &quot;external_stage&quot;,&quot;nostage&quot;, or null which implies use the servers stagingMode value. "), createParameterDescriptor("info", "describes the details of the deployment. "), createParameterDescriptor("id", "to use for tracking. Use null to allow system to generate. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      String[] var6;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link #deploy} or {@link #redeploy} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Activate a deployment. The deployment is extended to the relevant J2EE containers and subsystems. An ApplicationMBean is created if necessary and initialized prior to initiating the deployment for new deployments. If the application is already configured it is expected to be correctly defined. Activate covers new deployments, redeployments and refreshes.</p>  <p>If the source argument is a valid path to an EAR or module, the application files will be distributed as necessary, to the target servers. If the source argument is null, then the application must already be configured with a valid path.</p>  <p>The name argument must always be specified (not null). If there is already an application configured with this name, then the deployment will be based on that application. Otherwise, this is a new deployment and an ApplicationMBean will be created and fully configured based on the application descriptors found in the archive or directory named by the source argument. If this is a new deployment, the source argument cannot be null.</p>  <p>The stagingMode argument can be used to override the staging attribute of the targeted servers. If this argument is null, the application will be staged to each server if that server is configured to be staged. If stagingMode is \"stage\" or \"nostage\" then the application will be staged or not staged, respectively, to each server, regardless of the server's configuration. If the staging mode is \"external_stage\", the application files are not staged by the server, rather the user is expected to place them in the staging area.</p>  <p>The info argument is used to qualify the deployment. If null, the deployment will apply to the application's configured target servers. If info is not null, then it names a list of servers, each of which can be further qualified by module names. If a named target is not already configured for this application, it will be added as a target to the appropriate components.</p>  <p>The info argument can also specify a list of files and directories. This supports application refreshes. When a file list is defined in the info object, the deployment will cause those files to be redistributed to the target servers. The file paths must be relative to the application source. If the application is an archive, the entire archive is redistributed, otherwise only the named files are distributed. Note that if the application targets release 6.x servers, there is no guarantee that only the files listed are redeployed.</p>  <p>The id argument is used to specify the identifier for the resulting task. If null, the system will generate the id. If not null, then the value must be unique across all existing deployment tasks.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("preDeregister");
      String var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("activate", String.class, String.class, String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("source", (String)null), createParameterDescriptor("name", (String)null), createParameterDescriptor("stagingMode", (String)null), createParameterDescriptor("info", (String)null), createParameterDescriptor("id", (String)null), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link #deploy} or {@link #redeploy} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Same functionality as {@link #activate(String, String, String, DeploymentData, String)} except that control is given back to caller without actually initiating the task, when startTask is false. The client must then invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the activation process. This is most useful when the client is interested in receiving notifications of the task's progress.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("deactivate", String.class, DeploymentData.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "is the configured name of the application. "), createParameterDescriptor("info", "describes the details of the deployment. Null interpreted               to deactivate the application on all servers, retaining               targets. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link #stop} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deactivate a deployment. This suspends the services offered by the deployment and removes it from the relevant J2EE containers. The deployment will remain in the staging area and prepared following deactivation.</p>  <p>The info parameter is used to define the specific targets the deactivation applies to. If any targets are specified in the info object, they will be removed from the application configuration. If info object does not specify any targets then the deactivation will apply to all targets configured for the application. In this scenario the configured targets are not removed from the configuration. Rather, the application is configured as undeployed.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("deactivate", String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("info", (String)null), createParameterDescriptor("id", (String)null), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link #stop} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Same functionality as {@link #deactivate(String, DeploymentData, String)} except that control is given back to caller without actually initiating the task, when startTask is false. The client must invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the deactivation process. This is most useful when the client is interested in receiving notifications of the task's progress.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("remove", String.class, DeploymentData.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "is the configured name of the application to remove "), createParameterDescriptor("info", "describes the details of the deployment. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link #undeploy} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Removes a deployment. This results in the application being deactivated and deconfigured from the target servers. Staged files are removed from the target server if they were staged when first deployed. If no targets are specified in the info object, or if the info object is null, the application is removed entirely from the domain.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("remove", String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("info", (String)null), createParameterDescriptor("id", (String)null), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link #undeploy} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Same functionality as {@link #remove(String, DeploymentData, String)} except that control is given back to caller without actually initiating the task, when startTask is false. The client must invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the remove process. This is most useful when the client is interested in receiving notifications of the task's progress.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("unprepare", String.class, DeploymentData.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "is the configured name of the application to unprepare "), createParameterDescriptor("info", "describes the details of the deployment. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link #stop} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deactivate and unload a deployment. This results in the application being deactivated and classes are unloaded from the target servers. Staged files are not removed from the target server if they were staged when first deployed. If no targets are specified in the info object, or if the info object is null, the application is removed entirely from the domain.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("unprepare", String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("info", (String)null), createParameterDescriptor("id", (String)null), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var2.setValue("deprecated", "8.1.0.0 Replaced by {@link #stop} ");
         var1.put(var5, var2);
         var2.setValue("description", "<p>Same functionality as {@link #unprepare(String, DeploymentData, String)} except that control is given back to caller without actually initiating the task, when startTask is false. The client must invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the process. This is most useful when the client is interested in receiving notifications of the task's progress.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("distribute", String.class, String.class, DeploymentData.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("source", "is the path to the application files "), createParameterDescriptor("name", "is the configured name of the application to distribute "), createParameterDescriptor("info", "contains target information. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Distributes application files on targets. This results in the application being copied to the staging area of a target</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("distribute", String.class, String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("source", (String)null), createParameterDescriptor("name", (String)null), createParameterDescriptor("info", "contains target information. "), createParameterDescriptor("id", (String)null), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Same functionality as {@link #distribute(String source, String, DeploymentData, String)} except that control is given back to caller without actually initiating the task, when startTask is false. The client must invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the process. This is most useful when the client is interested in receiving notifications of the task's progress.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("start", String.class, DeploymentData.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "is the configured name of the application to start "), createParameterDescriptor("info", "contains target information. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      String[] var7;
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Starts an already distributed application on target(s). This results in the application being prepared and activated on target(s)</p> ");
         var2.setValue("role", "operation");
         var7 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var7);
      }

      var3 = DeployerRuntimeMBean.class.getMethod("start", String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("info", "contains target information. "), createParameterDescriptor("id", (String)null), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Same functionality as {@link #start(String, DeploymentData, String)} except that control is given back to caller without actually initiating the task, when startTask is false. The client must invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the process. This is most useful when the client is interested in receiving notifications of the task's progress.</p> ");
         var2.setValue("role", "operation");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = DeployerRuntimeMBean.class.getMethod("stop", String.class, DeploymentData.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "is the configured name of the application to start "), createParameterDescriptor("info", "contains target information. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Stops application on target(s). This results in an application becoming unavailable for the clients</p> ");
         var2.setValue("role", "operation");
         var7 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var7);
      }

      var3 = DeployerRuntimeMBean.class.getMethod("stop", String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("info", "contains target information. "), createParameterDescriptor("id", (String)null), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Same functionality as {@link #stop(String, DeploymentData, String)} except that control is given back to caller without actually initiating the task, when startTask is false. The client must invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the process. This is most useful when the client is interested in receiving notifications of the task's progress.</p> ");
         var2.setValue("role", "operation");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = DeployerRuntimeMBean.class.getMethod("deploy", String.class, String.class, String.class, DeploymentData.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("source", "is the path to the application files "), createParameterDescriptor("name", "is the configured name of the application to distribute "), createParameterDescriptor("info", "contains target information. It names a list of target servers, each of which can be further  qualified by module names. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. "), createParameterDescriptor("stagingMode", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Deploys application on target(s). This results in the application being distributed, prepared and activated on specified target(s). The name argument must always be specified (not null). If there is already an application configured with this name, then the deployment will be based on that application. Otherwise, this is a new deployment and an ApplicationMBean will be created and fully configured based on the application descriptors found in the archive or directory named by the source argument. If this is a new deployment, the source argument cannot be null.</p>  <p>The stagingMode argument can be used to override the staging attribute of the targeted servers. If this argument is null, the application will be staged to each server if that server is configured to be staged. If stagingMode is \"stage\" or \"nostage\" then the application will be staged or not staged, respectively, to each server, regardless of the server's configuration. If the staging mode is \"external_stage\", the application files are not staged by the server, rather the user is expected to place them in the staging area.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("deploy", String.class, String.class, String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("source", (String)null), createParameterDescriptor("name", (String)null), createParameterDescriptor("stagingMode", (String)null), createParameterDescriptor("info", (String)null), createParameterDescriptor("id", (String)null), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Same functionality as {@link #deploy(java.lang.String, java.lang.String, java.lang.String, weblogic.management.deploy.DeploymentData, java.lang.String)} except that control is given back to caller without actually initiating the task, when startTask is false. The client must invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the process. This is most useful when the client is interested in receiving notifications of the task's progress.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("redeploy", String.class, DeploymentData.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "is the configured name of the application to distribute "), createParameterDescriptor("info", "contains target information. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Redeploys application on target(s). This results in redeploy of entire application on target(s). Redeploy results in redistribution of the application if staging mode is \"stage\". Redeploy can also be used for partial redeployment by specifying a list of files.</p>  <p>The info argument is used to qualify the deployment. If null, the deployment will apply to the application's configured target servers. If info is not null, then it names a list of servers, each of which can be further qualified by module names. If a named target is not already configured for this application, it will be added as a target to the appropriate components.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("redeploy", String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", (String)null), createParameterDescriptor("info", (String)null), createParameterDescriptor("id", (String)null), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Same functionality as {@link #redeploy(String, DeploymentData, String)} except that control is given back to caller without actually initiating the task, when startTask is false. The client must invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the process. This is most useful when the client is interested in receiving notifications of the task's progress.</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("redeploy", String.class, String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "Redeploy a new version of an application. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("update", String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "is the name of the deployed application "), createParameterDescriptor("info", "contains target information. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. "), createParameterDescriptor("startTask", "indcates whether to automatically start the operation ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Updates an application's configuration ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("undeploy", String.class, DeploymentData.class, String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "is the configured name of the application to distribute "), createParameterDescriptor("info", "contains target information. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>undeploys application on target(s). This results in deactivating and removing an application on target(s). This is the exact reverse of deploy().</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("undeploy", String.class, DeploymentData.class, String.class, Boolean.TYPE);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("name", "is the configured name of the application to distribute "), createParameterDescriptor("info", "describes the details of the deployment. "), createParameterDescriptor("id", "to use for tracking. If null, a system generated id is used. "), createParameterDescriptor("startTask", (String)null)};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var6 = new String[]{BeanInfoHelper.encodeEntities("ManagementException if the request is rejected.")};
         var2.setValue("throws", var6);
         var1.put(var5, var2);
         var2.setValue("description", "Same functionality as {@link #undeploy(String, DeploymentData, String)} except that  control is given back to caller without actually initiating the task, when startTask is false. The client must invoke the {@link DeploymentTaskRuntimeMBean#start} method to complete the process. This is most useful when the client is interested in receiving notifications of the task's progress. ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("query", String.class);
      var4 = new ParameterDescriptor[]{createParameterDescriptor("id", "is the id to query ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var4);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Locates a deployment task based on the deployment id.</p> ");
         var2.setValue("role", "operation");
         var6 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
         var2.setValue("rolesAllowed", var6);
      }

      var3 = DeployerRuntimeMBean.class.getMethod("list");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Return array of all known deployment tasks</p> ");
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("removeTask", String.class);
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Return array of all known deployment tasks</p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("getDeployments", TargetMBean.class);
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "Get all of the DeploymentMBeans that are associated with a specific target. ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      var3 = DeployerRuntimeMBean.class.getMethod("purgeRetiredTasks");
      var8 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var8)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var8, var2);
         var2.setValue("description", "<p>Flush out all retired tasks after completion and returns their ids back so that DeployerTool reports a message. </p> ");
         var2.setValue("exclude", Boolean.TRUE);
         var2.setValue("role", "operation");
      }

      if (BeanInfoHelper.isVersionCompliant("8.1.4.0", (String)null, this.targetVersion)) {
         var3 = DeployerRuntimeMBean.class.getMethod("getAvailabilityStatusForApplication", String.class, Boolean.TYPE);
         var8 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var8)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "8.1.4.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.AppRuntimeStateRuntimeMBean} ");
            var1.put(var8, var2);
            var2.setValue("description", "Provides a map consisting of Component names of the application and map of availability status for each target of that component including any virtual host. The map corresponding to each component name consist of the component target name and weblogic.management.TargetAvailabilityStatus object corresponding to that component target. ");
            var2.setValue("role", "operation");
            var2.setValue("since", "8.1.4.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("8.1.4.0", (String)null, this.targetVersion)) {
         var3 = DeployerRuntimeMBean.class.getMethod("getAvailabilityStatusForComponent", ComponentMBean.class, Boolean.TYPE);
         var8 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var8)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "8.1.4.0");
            var2.setValue("deprecated", "9.0.0.0 Replaced by {@link weblogic.management.runtime.AppRuntimeStateRuntimeMBean} ");
            var1.put(var8, var2);
            var2.setValue("description", "Provides a map of availability status for each target of that component including any virtual host. The map consist of the component target name and TargetAvailabilityStatus object corresponding to that component target. ");
            var2.setValue("role", "operation");
            var2.setValue("since", "8.1.4.0");
         }
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
