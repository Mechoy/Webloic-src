package weblogic.j2ee;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ApplicationRuntimeMBean;

public class J2EEApplicationRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ApplicationRuntimeMBean.class;

   public J2EEApplicationRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public J2EEApplicationRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = J2EEApplicationRuntimeMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.j2ee");
      String var3 = (new String("<p>An application represents a J2EE Enterprise application packaged in an EAR file or EAR exploded directory. The EAR file or directory contains a set of components such as WAR, EJB, and RAR connector components, each of which can be deployed on one or more targets. A target is a server or a cluster. Modules in the application can have one of the following states: <br> <ul> <li>UNPREPARED - Indicates that none of the  modules in this application are currently prepared or active.</li> <li>PREPARED -  Indicates that none of the  modules in this application are currently prepared or active.</li> <li>ACTIVATED - Indicates at least one module in this application is currently active.</li></ul>  ApplicationRuntimeMBean encapsulates runtime information about a deployed Enterprise application. </p>  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Operator")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ApplicationRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("ActiveVersionState")) {
         var3 = "getActiveVersionState";
         var4 = null;
         var2 = new PropertyDescriptor("ActiveVersionState", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("ActiveVersionState", var2);
         var2.setValue("description", "<p>Specifies whether this application version is the currently active version.</p>  <p>An application can be the only version currently deployed, or it can have more than one version currently deployed, using the side-by-side deployment feature. If more than one version is deployed, only one version can be active. This attribute specifies the state in which the current application version is in.</p>  <p>An application can be in an INACTIVE state, which means that it has not been activated yet, or that there is more than one version of the application deployed (using side-by-side deployment) and this one is retiring.</p>  <p>An application can be in ACTIVE_ADMIN state, which means that it is the currently active version for administrative channel requests.</p>  <p>An application can be in ACTIVE state, which means that it is the currently active version for normal (non-administrative) channel requests.</p>  See <a href=../../e13941/weblogic/deploy/version/package-summary.html >weblogic.deploy.version.AppActiveVersionState</a> for state values. ");
      }

      if (!var1.containsKey("ApplicationName")) {
         var3 = "getApplicationName";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationName", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("ApplicationName", var2);
         var2.setValue("description", "<p>The name of the Enterprise application.</p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (!var1.containsKey("ApplicationVersion")) {
         var3 = "getApplicationVersion";
         var4 = null;
         var2 = new PropertyDescriptor("ApplicationVersion", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("ApplicationVersion", var2);
         var2.setValue("description", "<p>The Enterprise application's version identifier.</p>  <p>This is particularly useful, when using the side-by-side deployment feature, to differentiate between two different versions of the same application that are deployed at the same time. </p> ");
         var2.setValue("unharvestable", Boolean.TRUE);
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.0.0", (String)null, this.targetVersion) && !var1.containsKey("ClassRedefinitionRuntime")) {
         var3 = "getClassRedefinitionRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("ClassRedefinitionRuntime", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("ClassRedefinitionRuntime", var2);
         var2.setValue("description", "<p>If class fast-swap feature is enabled for the application, return the runtime mbean to monitor and control the class fast-swap within the application. </p> ");
         var2.setValue("relationship", "containment");
         var2.setValue("since", "10.3.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.3.0", (String)null, this.targetVersion) && !var1.containsKey("CoherenceClusterRuntime")) {
         var3 = "getCoherenceClusterRuntime";
         var4 = null;
         var2 = new PropertyDescriptor("CoherenceClusterRuntime", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("CoherenceClusterRuntime", var2);
         var2.setValue("description", "<p>Returns the Coherence Cluster related runtime mbean </p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "10.3.3.0");
      }

      if (!var1.containsKey("ComponentRuntimes")) {
         var3 = "getComponentRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("ComponentRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("ComponentRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of component runtime instances for each J2EE component (such as an EJB or a Web application) that is contained in this Enterprise application. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("HealthState")) {
         var3 = "getHealthState";
         var4 = null;
         var2 = new PropertyDescriptor("HealthState", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("HealthState", var2);
         var2.setValue("description", "<p>The HealthState mbean for the application. </p>  See <a href=../../e13941/weblogic/health/HealthState.html>weblogic.health.HealthState</a> for state values. ");
      }

      if (!var1.containsKey("KodoPersistenceUnitRuntimes")) {
         var3 = "getKodoPersistenceUnitRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("KodoPersistenceUnitRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("KodoPersistenceUnitRuntimes", var2);
         var2.setValue("description", "<p>Provides an array of KodoPersistenceUnitRuntimeMBean objects for this EJB module. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("LibraryRuntimes")) {
         var3 = "getLibraryRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("LibraryRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("LibraryRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of library runtime instances for each J2EE library that is contained in this Enterprise application. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MaxThreadsConstraintRuntimes")) {
         var3 = "getMaxThreadsConstraintRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("MaxThreadsConstraintRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("MaxThreadsConstraintRuntimes", var2);
         var2.setValue("description", "<p>Get the runtime mbeans for all MaxThreadsConstraints defined at the app-level</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("MinThreadsConstraintRuntimes")) {
         var3 = "getMinThreadsConstraintRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("MinThreadsConstraintRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("MinThreadsConstraintRuntimes", var2);
         var2.setValue("description", "<p>Get the runtime mbeans for all MinThreadsConstraints defined at the app-level</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("OptionalPackageRuntimes")) {
         var3 = "getOptionalPackageRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("OptionalPackageRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("OptionalPackageRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of optional package runtime instances for each J2EE optional package that is contained in this Enterprise application. </p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("QueryCacheRuntimes")) {
         var3 = "getQueryCacheRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("QueryCacheRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("QueryCacheRuntimes", var2);
         var2.setValue("description", "<p>Returns a list of QueryCacheRuntimeMBeans configured for this application. </p> ");
         var2.setValue("relationship", "reference");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("RequestClassRuntimes")) {
         var3 = "getRequestClassRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("RequestClassRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("RequestClassRuntimes", var2);
         var2.setValue("description", "<p>Get the runtime mbeans for all request classes defined at the app-level</p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion) && !var1.containsKey("WorkManagerRuntimes")) {
         var3 = "getWorkManagerRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WorkManagerRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("WorkManagerRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of work manager runtime instances for each application-scoped work manager that is associated with this Enterprise application. </p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("since", "9.0.0.0");
      }

      if (!var1.containsKey("WseeRuntimes")) {
         var3 = "getWseeRuntimes";
         var4 = null;
         var2 = new PropertyDescriptor("WseeRuntimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("WseeRuntimes", var2);
         var2.setValue("description", "<p>Returns the list of Web Service runtime instances that are contained in this Enterprise application. </p> ");
         var2.setValue("relationship", "reference");
         var2.setValue("deprecated", "Use getWseeV2Runtimes from the web app or EJB component instead ");
      }

      if (!var1.containsKey("WseeV2Runtimes")) {
         var3 = "getWseeV2Runtimes";
         var4 = null;
         var2 = new PropertyDescriptor("WseeV2Runtimes", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("WseeV2Runtimes", var2);
         var2.setValue("description", "<p>Returns the list of Web Service runtime instances that are contained at the application scope of this Enterprise application. This can happen when javax.xml.ws.Endpoint.publish() is called from within an application lifecycle listener </p> ");
         var2.setValue("relationship", "reference");
      }

      if (!var1.containsKey("EAR")) {
         var3 = "isEAR";
         var4 = null;
         var2 = new PropertyDescriptor("EAR", ApplicationRuntimeMBean.class, var3, (String)var4);
         var1.put("EAR", var2);
         var2.setValue("description", "<p>Returns true if the application deployment unit is an EAR file.  It returns false for WAR/JAR/RAR etc deployments  </p> ");
         var2.setValue("exclude", Boolean.TRUE);
      }

      super.buildPropertyDescriptors(var1);
   }

   private void fillinFactoryMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinCollectionMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
   }

   private void fillinFinderMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      MethodDescriptor var2;
      Method var3;
      String var4;
      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationRuntimeMBean.class.getMethod("lookupWorkManagerRuntime", String.class, String.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>Lookup the WorkManagerRuntimeMBean given the component name and work manager name. If the component name is null then the WorkManagerRuntime is retrieved from the application itself.</p> ");
            var2.setValue("role", "finder");
            var2.setValue("property", "WorkManagerRuntimes");
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = ApplicationRuntimeMBean.class.getMethod("lookupWseeV2Runtime", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("name", "The web service description name of the web service to look up. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "<p>Returns a named Web Service runtime instance that is contained at application scope of this Enterprise application. </p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "WseeV2Runtimes");
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationRuntimeMBean.class.getMethod("lookupMinThreadsConstraintRuntime", String.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>Lookup MinThreadsConstraintRuntime given its name</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "finder");
            var2.setValue("property", "MinThreadsConstraintRuntimes");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationRuntimeMBean.class.getMethod("lookupRequestClassRuntime", String.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>Lookup RequestClassRuntime given its name</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "finder");
            var2.setValue("property", "RequestClassRuntimes");
            var2.setValue("since", "9.0.0.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("9.0.0.0", (String)null, this.targetVersion)) {
         var3 = ApplicationRuntimeMBean.class.getMethod("lookupMaxThreadsConstraintRuntime", String.class);
         var4 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var4)) {
            var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
            var2.setValue("since", "9.0.0.0");
            var1.put(var4, var2);
            var2.setValue("description", "<p>Lookup MaxThreadsConstraintRuntime given its name</p> ");
            var2.setValue("exclude", Boolean.TRUE);
            var2.setValue("role", "finder");
            var2.setValue("property", "MaxThreadsConstraintRuntimes");
            var2.setValue("since", "9.0.0.0");
         }
      }

      var3 = ApplicationRuntimeMBean.class.getMethod("lookupQueryCacheRuntime", String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Returns a QueryCacheRuntimeMBean for the app-scoped query-cache with name 'cacheName'. </p> ");
         var2.setValue("role", "finder");
         var2.setValue("property", "QueryCacheRuntimes");
      }

   }

   private void fillinOperationMethodInfos(Map var1) throws IntrospectionException, NoSuchMethodException {
      Method var3 = ApplicationRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = ApplicationRuntimeMBean.class.getMethod("lookupComponents");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var2.setValue("deprecated", " ");
         var1.put(var4, var2);
         var2.setValue("description", "<p>The ComponentRuntimeMBean's contained in this application</p> ");
         var2.setValue("role", "operation");
         String[] var5 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
         var2.setValue("rolesAllowed", var5);
      }

      var3 = ApplicationRuntimeMBean.class.getMethod("hasApplicationCache");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>returns True if the application has an (EJB) Application Level Cache ");
         var2.setValue("role", "operation");
      }

      var3 = ApplicationRuntimeMBean.class.getMethod("reInitializeApplicationCachesAndPools");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>If the application has an (EJB) Application Level Cache then this method will reinitialize the cache and any of its associated pools to their startup time states if possible. ");
         var2.setValue("role", "operation");
      }

      var3 = ApplicationRuntimeMBean.class.getMethod("getKodoPersistenceUnitRuntime", String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("unitName", (String)null)};
      String var7 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var7)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var7, var2);
         var2.setValue("description", "<p>Provides the KodoPersistenceUnitRuntimeMBean for the EJB with the specified name.</p> ");
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
