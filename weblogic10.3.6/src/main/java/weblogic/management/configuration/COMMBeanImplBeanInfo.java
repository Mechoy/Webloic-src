package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class COMMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = COMMBean.class;

   public COMMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public COMMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = COMMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This bean represents the server-wide configuration of COM  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.COMMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("NTAuthHost")) {
         var3 = "getNTAuthHost";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNTAuthHost";
         }

         var2 = new PropertyDescriptor("NTAuthHost", COMMBean.class, var3, var4);
         var1.put("NTAuthHost", var2);
         var2.setValue("description", "<p>The address of the primary domain controller this server uses for authenticating clients. (If not specified, COM clients will not be authenticated.)</p> ");
      }

      if (!var1.containsKey("ApartmentThreaded")) {
         var3 = "isApartmentThreaded";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setApartmentThreaded";
         }

         var2 = new PropertyDescriptor("ApartmentThreaded", COMMBean.class, var3, var4);
         var1.put("ApartmentThreaded", var2);
         var2.setValue("description", "<p>Controls the flag that is used to initialize COM in native mode.</p>  <p>By default, when jCOM initializes COM in native mode, it starts COM with the <code>COINIT_MULTITHREADED</code>. This causes COM to use Multi-Threaded Apartment (MTA) thread model. In the MTA model, calls to an object are not synchronized by COM. Multiple clients can concurrently call an object that supports this model on different threads, and the object must provide synchronization in its interface/method implementations using synchronization objects such as events, mutexes, semaphores, etc. MTA objects can receive concurrent calls from multiple out-of-process clients through a pool of COM-created threads belonging to the object's process.</p>  <p>If the server logs a Class Not Registered Message when starting COM in native mode, try setting this property. This will cause jCOM to start COM in native mode, using <code>COINIT_APARTMENTTHREADED</code> option instead of the <code>COINIT_MULTITHREADED</code> option. In a component that is marked as Apartment Threaded, each method of that component will execute on a thread that is associated with that component. This separates the methods into their own \"Apartments\", with each instance of a component corresponding to one apartment. While there is only one thread inside of a component, each instance of that component will have its own thread apartment.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("MemoryLoggingEnabled")) {
         var3 = "isMemoryLoggingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMemoryLoggingEnabled";
         }

         var2 = new PropertyDescriptor("MemoryLoggingEnabled", COMMBean.class, var3, var4);
         var1.put("MemoryLoggingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this server should log memory usage.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("NativeModeEnabled")) {
         var3 = "isNativeModeEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setNativeModeEnabled";
         }

         var2 = new PropertyDescriptor("NativeModeEnabled", COMMBean.class, var3, var4);
         var1.put("NativeModeEnabled", var2);
         var2.setValue("description", "<p>Specifies whether this server should use native DLLs to allow Java objects to interact with COM objects. (Supported on Windows only.)</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
      }

      if (!var1.containsKey("PrefetchEnums")) {
         var3 = "isPrefetchEnums";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrefetchEnums";
         }

         var2 = new PropertyDescriptor("PrefetchEnums", COMMBean.class, var3, var4);
         var1.put("PrefetchEnums", var2);
         var2.setValue("description", "<p>Specifies whether this server should prefetch the next element in a <tt>java.lang.Enumeration</tt> (that had been improperly converted from a COM <tt>VariantEnumeration</tt> type) so the correct value is returned when the <tt>hasMoreElements()</tt> method is called.</p>  <p>Some COM methods return a COM VariantEnumeration type. The java2com tool automatically converts the returned type into a java.lang.Enumeration. This is not a perfect match since COM enumerations have no equivalent to the <code>hasMoreElements()</code> call. The client must continue to call <code>nextElement</code> until a <code>NoSuchElementException</code> occurs. Setting this property will cause jCOM to prefetch the next element in behind the scenes and return the correct value when hasMoreElements is called.</p> ");
      }

      if (!var1.containsKey("VerboseLoggingEnabled")) {
         var3 = "isVerboseLoggingEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setVerboseLoggingEnabled";
         }

         var2 = new PropertyDescriptor("VerboseLoggingEnabled", COMMBean.class, var3, var4);
         var1.put("VerboseLoggingEnabled", var2);
         var2.setValue("description", "<p>Specifies whether verbose logging is enabled.</p> ");
         setPropertyDescriptorDefault(var2, new Boolean(false));
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
