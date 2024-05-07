package weblogic.webservice.monitoring;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.WebServiceRuntimeMBean;

public class WSRuntimeMBeanImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WebServiceRuntimeMBean.class;

   public WSRuntimeMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WSRuntimeMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WSRuntimeMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("package", "weblogic.webservice.monitoring");
      String var3 = (new String("Describes the state of a single webservice. ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.WebServiceRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (!var1.containsKey("HomePageHitCount")) {
         var3 = "getHomePageHitCount";
         var4 = null;
         var2 = new PropertyDescriptor("HomePageHitCount", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("HomePageHitCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of times that this service's home page has been visited since this webservice was deployed.</p> ");
      }

      if (!var1.containsKey("HomePageURL")) {
         var3 = "getHomePageURL";
         var4 = null;
         var2 = new PropertyDescriptor("HomePageURL", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("HomePageURL", var2);
         var2.setValue("description", "<p>Provides the URL from which the home page for this webservice can be retrieved.</p> ");
      }

      if (!var1.containsKey("LastMalformedRequestError")) {
         var3 = "getLastMalformedRequestError";
         var4 = null;
         var2 = new PropertyDescriptor("LastMalformedRequestError", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("LastMalformedRequestError", var2);
         var2.setValue("description", "<p>Provides the exception that was thrown during the most recently-processed malformed requrest, or null if no malformed request has been encountered.</p> ");
      }

      if (!var1.containsKey("LastResetTime")) {
         var3 = "getLastResetTime";
         var4 = null;
         var2 = new PropertyDescriptor("LastResetTime", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("LastResetTime", var2);
         var2.setValue("description", "<p>Provides the Date on which <code>reset()</code> was last called for this mbean, or null if it has never been called.</p> ");
      }

      if (!var1.containsKey("MalformedRequestCount")) {
         var3 = "getMalformedRequestCount";
         var4 = null;
         var2 = new PropertyDescriptor("MalformedRequestCount", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("MalformedRequestCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of times that this service has received a malformed or otherwise invalid request.</p> ");
      }

      if (!var1.containsKey("Operations")) {
         var3 = "getOperations";
         var4 = null;
         var2 = new PropertyDescriptor("Operations", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("Operations", var2);
         var2.setValue("description", "<p>Returns an array of MBeans representing each of the operations exposed by this WebService.</p> ");
         var2.setValue("relationship", "containment");
      }

      if (!var1.containsKey("ServiceName")) {
         var3 = "getServiceName";
         var4 = null;
         var2 = new PropertyDescriptor("ServiceName", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("ServiceName", var2);
         var2.setValue("description", "<p>Provides the name of this service.</p> ");
      }

      if (!var1.containsKey("URI")) {
         var3 = "getURI";
         var4 = null;
         var2 = new PropertyDescriptor("URI", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("URI", var2);
         var2.setValue("description", "<p>Provides the URI of this service.</p> ");
      }

      if (!var1.containsKey("WSDLHitCount")) {
         var3 = "getWSDLHitCount";
         var4 = null;
         var2 = new PropertyDescriptor("WSDLHitCount", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("WSDLHitCount", var2);
         var2.setValue("description", "<p>Provides a count of the number of times that this service's WSDL has been retrieved since this webservice was deployed.</p> ");
      }

      if (!var1.containsKey("WSDLUrl")) {
         var3 = "getWSDLUrl";
         var4 = null;
         var2 = new PropertyDescriptor("WSDLUrl", WebServiceRuntimeMBean.class, var3, (String)var4);
         var1.put("WSDLUrl", var2);
         var2.setValue("description", "<p>Provides the URL from which the WSDL for this webservice can be retrieved.</p> ");
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
      Method var3 = WebServiceRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      var3 = WebServiceRuntimeMBean.class.getMethod("reset");
      var4 = BeanInfoHelper.buildMethodKey(var3);
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", "<p>Resets all of the failure statistics that have been gathered in this mbean.</p> ");
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
