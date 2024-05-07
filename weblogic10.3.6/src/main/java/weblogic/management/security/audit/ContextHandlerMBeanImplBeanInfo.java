package weblogic.management.security.audit;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.commo.AbstractCommoConfigurationBeanImplBeanInfo;

public class ContextHandlerMBeanImplBeanInfo extends AbstractCommoConfigurationBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ContextHandlerMBean.class;

   public ContextHandlerMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ContextHandlerMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ContextHandlerMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("abstract", Boolean.TRUE);
      var2.setValue("package", "weblogic.management.security.audit");
      String var3 = (new String("Provides a set of attributes for ContextHandler support. An Auditor provider MBean can optionally implement this MBean. The WebLogic Server Administration Console detects when an Auditor provider implements this MBean and automatically provides a tab for using these attributes.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">In addition to being used as a base class that provides functionality to security provider MBeans, JMX applications can use this class directly as a type-safe interface. When used as a type-safe interface, a JMX application imports this class and accesses it through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, JMX applications that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. </p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.security.audit.ContextHandlerMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("ActiveContextHandlerEntries")) {
         var3 = "getActiveContextHandlerEntries";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setActiveContextHandlerEntries";
         }

         var2 = new PropertyDescriptor("ActiveContextHandlerEntries", ContextHandlerMBean.class, var3, var4);
         var1.put("ActiveContextHandlerEntries", var2);
         var2.setValue("description", "Returns the ContextHandler entries that the Audit provider is currently configured to process. ");
         var2.setValue("dynamic", Boolean.FALSE);
      }

      if (!var1.containsKey("SupportedContextHandlerEntries")) {
         var3 = "getSupportedContextHandlerEntries";
         var4 = null;
         var2 = new PropertyDescriptor("SupportedContextHandlerEntries", ContextHandlerMBean.class, var3, var4);
         var1.put("SupportedContextHandlerEntries", var2);
         var2.setValue("description", "Returns the list of all ContextHandler entries supported by the auditor. ");
         var2.setValue("transient", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.FALSE);
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
