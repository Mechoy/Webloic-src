package weblogic.wtc.gwt;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WTCRuntimeMBean;

public class WTCServiceBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCRuntimeMBean.class;

   public WTCServiceBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCServiceBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCService.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.wtc.gwt");
      String var3 = (new String("This class is used to 1. query, stop, and start WTC connections. 2. query, suspend, and resume WTC imported and exported services.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX\" on <a href=\"http://www.oracle.com/technology/products/weblogic/index.html\" shape=\"rect\">http://www.oracle.com/technology/products/weblogic/index.html</a>.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WTCRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      if (!var1.containsKey("ServiceStatus")) {
         String var3 = "getServiceStatus";
         Object var4 = null;
         var2 = new PropertyDescriptor("ServiceStatus", WTCRuntimeMBean.class, var3, (String)var4);
         var1.put("ServiceStatus", var2);
         var2.setValue("description", "Returns status of all the Import or Export services/resources configured for the targeted WTC server with the specified service name.  This service name is the resource name of the WTCImport and WTCExport. ");
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
      Method var3 = WTCRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("startConnection", String.class, String.class);
      ParameterDescriptor[] var6 = new ParameterDescriptor[]{createParameterDescriptor("LDomAccessPointId", "The local domain access point id. "), createParameterDescriptor("RDomAccessPointId", "The remote domain access point id. ")};
      String var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Attempt to start a connection between the specified local and remote domain access points. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("startConnection", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("LDomAccessPointId", "The local domain access point id. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Attempt to start connections between the specified local domain access point and all remote end points defined for the given local domain domain access point. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("stopConnection", String.class, String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("LDomAccessPointId", "The local domain access point id. "), createParameterDescriptor("RDomAccessPointId", "The remote domain access point id. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Attempt to stop the connection between the specified local and remote domain access points. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("stopConnection", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("LDomAccessPointId", "The local domain access point id. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Attempt to stop all remote connections configured for the given local access point id. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("listConnectionsConfigured");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Provide a list of the connections configured for this WTCService. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("suspendService", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Suspend all the Import and Export services with the specified service name.  This service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("suspendService", String.class, Boolean.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. "), createParameterDescriptor("isImport", "The type of service indicate whether it is import or export. If true, then it is imported service, if false it is targeted for exported service. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Suspend all the Import or Export services with the specified service name.  This service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("suspendService", String.class, String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("localAccessPoint", "The local access point name. "), createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Suspend all the Import and Export services with the specified service name configured for the specified local access point. The service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("suspendService", String.class, String.class, Boolean.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("localAccessPoint", "The local access point name. "), createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. "), createParameterDescriptor("isImport", "The type of service indicate whether it is import or export. If true, then it is imported service, if false it is targeted for exported service. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Suspend all the Import or Export services with the specified service name configured for the specified local access point. The service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("suspendService", String.class, String.class, String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("localAccessPoint", "The local access point name. "), createParameterDescriptor("remoteAccessPointList", "The comma separated remote access point names. "), createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Suspend a specific Import service with the specified service name configured for the specified local access point and remote access point list. The service name is the resource name of the WTCImport and WTCExport. The remote access point list is a comma separated list; for instance, \"TDOM1,TDOM2\". ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("resumeService", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Resume all the Import and Export services with the specified service name.  This service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("resumeService", String.class, Boolean.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. "), createParameterDescriptor("isImport", "The type of service indicate whether it is import or export. If true, then it is imported service, if false it is targeted for exported service. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Resume all the Import or Export services with the specified service name.  This service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("resumeService", String.class, String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("localAccessPoint", "The local access point name. "), createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Resume all the Import and Export services with the specified service name configured for the specified local access point. The service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("resumeService", String.class, String.class, Boolean.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("localAccessPoint", "The local access point name. "), createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. "), createParameterDescriptor("isImport", "The type of service indicate whether it is import or export. If true, then it is imported service, if false it is targeted for exported service. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Resume all the Import or Export services with the specified service name configured for the specified local access point. The service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("resumeService", String.class, String.class, String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("localAccessPoint", "The local access point name. "), createParameterDescriptor("remoteAccessPointList", "The comma separated remote access point names. "), createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Resume a specific Import service with the specified service name configured for the specified local access point and remote access point list. The service name is the resource name of the WTCImport and WTCExport. The remote access point list is a comma separated list; for instance, \"TDOM1,TDOM2\". ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("getServiceStatus", String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Returns status of the Import and Export service/resource configured for the targeted WTC server with the specified service name.  This service name is the resource name of the WTCImport and WTCExport.  As long as one of the directions is available the returned satatus will be available. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("getServiceStatus", String.class, Boolean.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. "), createParameterDescriptor("isImport", "The type of service indicate whether it is import or export. If true, then it is imported service, if false it is targeted for exported service. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Returns status of the Import or Export service/resource configured for the targeted WTC server with the specified service name.  This service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("getServiceStatus", String.class, String.class);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("localAccessPoint", "The local access point name. "), createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Returns status of the imported and exported service of the specified service name and provided by the specified local access point. The service name is the resource name of the WTCImport and WTCExport. As long as one of the directions is available then the returned status will be available. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("getServiceStatus", String.class, String.class, Boolean.TYPE);
      var6 = new ParameterDescriptor[]{createParameterDescriptor("localAccessPoint", "The local access point name. "), createParameterDescriptor("svcName", "The resource name of imported or exported service/resource name. "), createParameterDescriptor("isImport", "The type of service indicate whether it is import or export. If true, then it is imported service, if false it is targeted for exported service. ")};
      var5 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var5)) {
         var2 = new MethodDescriptor(var3, var6);
         var1.put(var5, var2);
         var2.setValue("description", "Returns status of the imported or exported service of the specified service name and provided by the specified local access point. The service name is the resource name of the WTCImport and WTCExport. ");
         var2.setValue("role", "operation");
      }

      var3 = WTCRuntimeMBean.class.getMethod("getServiceStatus", String.class, String.class, String.class);
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "Returns status of a specific imported service provided by the specified local access point and remote access point list. The service name is the resource name of the WTCImport and WTCExport. The remote access point list is a comma separated list; for instance, \"TDOM1,TDOM2\". ");
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
