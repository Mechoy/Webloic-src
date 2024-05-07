package weblogic.console.internal;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import weblogic.management.WebLogicMBeanImplBeanInfo;
import weblogic.management.internal.mbean.BeanInfoHelper;
import weblogic.management.runtime.ConsoleRuntimeMBean;

public class ConsoleRuntimeImplBeanInfo extends WebLogicMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = ConsoleRuntimeMBean.class;

   public ConsoleRuntimeImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public ConsoleRuntimeImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = ConsoleRuntimeImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.1.0");
      var2.setValue("package", "weblogic.console.internal");
      String var3 = (new String("<p>Runtime services for the console.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.runtime.ConsoleRuntimeMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      Object var4;
      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("HomePageURL")) {
         var3 = "getHomePageURL";
         var4 = null;
         var2 = new PropertyDescriptor("HomePageURL", ConsoleRuntimeMBean.class, var3, (String)var4);
         var1.put("HomePageURL", var2);
         var2.setValue("description", "Gets the URL for the console's home page ");
         var2.setValue("since", "10.3.1.0");
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion) && !var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         var2 = new PropertyDescriptor("Enabled", ConsoleRuntimeMBean.class, var3, (String)var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "Is the console enabled? ");
         var2.setValue("since", "10.3.1.0");
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
      Method var3 = ConsoleRuntimeMBean.class.getMethod("preDeregister");
      String var4 = BeanInfoHelper.buildMethodKey(var3);
      MethodDescriptor var2;
      if (!var1.containsKey(var4)) {
         var2 = new MethodDescriptor(var3, (ParameterDescriptor[])null);
         var1.put(var4, var2);
         var2.setValue("description", " ");
         var2.setValue("role", "operation");
      }

      String var5;
      String[] var6;
      String[] var7;
      ParameterDescriptor[] var8;
      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion)) {
         var3 = ConsoleRuntimeMBean.class.getMethod("getDefaultPageURL", String[].class, String.class);
         var8 = new ParameterDescriptor[]{createParameterDescriptor("context", "Identifies the object the page should display or manage.  Most pages display or manage WLS mbeans (such as a server or cluster).  For them, call ‘getObjectNameContext’, passing in the JMX object name, to get the context to pass to this method.  See the console programmers’ guide for more information on contexts. "), createParameterDescriptor("perspective", "Specifies which kind of default page to return (e.g. configuration or monitoring).  Pass in null to get the default page for the object.  See the console programmers’ guide for more information on perspectives. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var8);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if context is null or empty")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.3.1.0");
            var1.put(var5, var2);
            var2.setValue("description", "Gets the URL for the default page of an object.  You can optionally specify the kind of default page (e.g. configuration or monitoring).  For example, use this method to get a WLS cluster's default page’s URL.  Use this method when possible since it isolates the caller from the specific console page labels.  This method does not check whether the page exists. To find out, use the URL and see if it works. ");
            var7 = new String[]{BeanInfoHelper.encodeEntities("getObjectNameContext")};
            var2.setValue("see", var7);
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.1.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.5.0", (String)null, this.targetVersion)) {
         var3 = ConsoleRuntimeMBean.class.getMethod("getDefaultPageURLs", String[][].class, String.class);
         var8 = new ParameterDescriptor[]{createParameterDescriptor("contexts", "An array of contexts identifying the object each page should display or manage. "), createParameterDescriptor("perspective", "Specifies which kind of default page to return for all of the pages. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var8);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if contexts is null, or if there  is a problem with the information for any of the pages  (e.g. contexts[2] is null or an empty string).   The returned array of URLs parallels the contexts array.  For example, return[1] contains the result for contexts[1].")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.3.5.0");
            var1.put(var5, var2);
            var2.setValue("description", "Gets the URLs for the default pages for a set of objects.  This method works exactly like getDefaultPageURL, except that it lets you get the URLS for a set of pages in one JMX call (to increase network performance). ");
            var7 = new String[]{BeanInfoHelper.encodeEntities("getDefaultPageURL")};
            var2.setValue("see", var7);
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.5.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.5.0", (String)null, this.targetVersion)) {
         var3 = ConsoleRuntimeMBean.class.getMethod("getDefaultPageURLs", String[][].class, String[].class);
         var8 = new ParameterDescriptor[]{createParameterDescriptor("contexts", "An array of contexts identifying the object each page should display or manage. "), createParameterDescriptor("perspectives", "An array of perspectives identifying which kind of default page to return for each object. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var8);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if contexts or perspectives is null,  contexts and perspectives don't have the same length, or if there is a  problem with the information for any of the pages  (e.g. contexts[2] is null or an empty string).   The contexts and perspecitives arrays are parallel arrays specifying  each desired page.  They must be the same length.  For example, contexts[1] and perspectives[1] are used to indicate  the information needed to compute the URL for the second page.   Similarly, the returned array of URLs parallels the contexts and  perspectives arrays.  For example, return[1] contains the result  for contexts[1]/perspectives[1].")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.3.5.0");
            var1.put(var5, var2);
            var2.setValue("description", "Gets the URLs for the default pages for a set of objects.  This method works exactly like getDefaultPageURL, except that it lets you get the URLS for a set of pages in one JMX call (to increase network performance). ");
            var7 = new String[]{BeanInfoHelper.encodeEntities("getDefaultPageURL")};
            var2.setValue("see", var7);
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.5.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion)) {
         var3 = ConsoleRuntimeMBean.class.getMethod("getSpecificPageURL", String.class, String[].class);
         var8 = new ParameterDescriptor[]{createParameterDescriptor("page", "The portal page label of the desired console page. "), createParameterDescriptor("context", "Identifies the object the page should display or manage.  Most pages display or manage WLS mbeans (such as a server or cluster).  For them, call ‘getObjectNameContext’, passing in the JMX object name, to get the context to pass to this method.  See the console programmers’ guide for more information on contexts. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var8);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if page is null or empty")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.3.1.0");
            var1.put(var5, var2);
            var2.setValue("description", "Gets the URL for a specific console page.  For example, use this method to get a WLS server's SSL configuration page's URL or the servers table page's URL.  Note: WLS reserves the right to change its portal page names in future releases.  Therefore, customers are advised to use ‘getDefaultPageURL’ if possible so that they’ll be isolated from these kinds of changes.  This method does not check whether the page exists. To find out, use the URL and see if it works. ");
            var7 = new String[]{BeanInfoHelper.encodeEntities("getJMXHandleContext"), BeanInfoHelper.encodeEntities("getDefaultPageURL")};
            var2.setValue("see", var7);
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.1.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.5.0", (String)null, this.targetVersion)) {
         var3 = ConsoleRuntimeMBean.class.getMethod("getSpecificPageURLs", String.class, String[][].class);
         var8 = new ParameterDescriptor[]{createParameterDescriptor("page", "The portal page label of the desired console pages. "), createParameterDescriptor("contexts", "An array of contexts identifying the object each page should display or manage. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var8);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if page is null or empty,  if contexts is null, or if there is a problem with the information  for any of the pages (e.g. pages[2] is null or an empty string).   The returned array of URLs parallels the contexts array.  For  example, return[1] contains the result for pages[1]/contexts[1].")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.3.5.0");
            var1.put(var5, var2);
            var2.setValue("description", "Gets the URLs for a set of specific console page.  This method works exactly like getSpecificPageURL, except that it lets you get the URLS for a set of pages in one JMX call (to increase network performance). ");
            var7 = new String[]{BeanInfoHelper.encodeEntities("getSpecificPageURL")};
            var2.setValue("see", var7);
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.5.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.5.0", (String)null, this.targetVersion)) {
         var3 = ConsoleRuntimeMBean.class.getMethod("getSpecificPageURLs", String[].class, String[][].class);
         var8 = new ParameterDescriptor[]{createParameterDescriptor("pages", "An array of portal page labels of the desired console pages. "), createParameterDescriptor("contexts", "An array of contexts identifying the object each page should display or manage. ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var8);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if pages or contexts is null,  pages and contexts don't have the same length, or if there is a  problem with the information for any of the pages  (e.g. pages[2] is null or an empty string).   The pages and contexts arrays are parallel arrays specifying  each desired page.  They must be the same length.  For example, pages[1] and contexts[1] are used to indicate  the information needed to compute the URL for the second page.   Similarly, the returned array of URLs parallels the pages  and contexts and pages arrays.  For example, return[1] contains  the result for pages[1]/contexts[1].")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.3.5.0");
            var1.put(var5, var2);
            var2.setValue("description", "Gets the URLs for a set of specific console pages.  This method works exactly like getSpecificPageURL, except that it lets you get the URLS for a set of pages in one JMX call (to increase network performance). ");
            var7 = new String[]{BeanInfoHelper.encodeEntities("getSpecificPageURL")};
            var2.setValue("see", var7);
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.5.0");
         }
      }

      if (BeanInfoHelper.isVersionCompliant("10.3.1.0", (String)null, this.targetVersion)) {
         var3 = ConsoleRuntimeMBean.class.getMethod("getObjectNameContext", String.class);
         var8 = new ParameterDescriptor[]{createParameterDescriptor("objectName", "the JMX object name of a WLS mbean ")};
         var5 = BeanInfoHelper.buildMethodKey(var3);
         if (!var1.containsKey(var5)) {
            var2 = new MethodDescriptor(var3, var8);
            var6 = new String[]{BeanInfoHelper.encodeEntities("IllegalArgumentException if objectName is null or empty")};
            var2.setValue("throws", var6);
            var2.setValue("since", "10.3.1.0");
            var1.put(var5, var2);
            var2.setValue("description", "Gets a context for a WLS mbean.  This method does not check whether the mbean exists. The results of this method are usually passed into getDefaultPageURL or getSpecificPageURL. ");
            var7 = new String[]{BeanInfoHelper.encodeEntities("getDefaultPageURL"), BeanInfoHelper.encodeEntities("getSpecificPageURL")};
            var2.setValue("see", var7);
            var2.setValue("role", "operation");
            var2.setValue("rolePermitAll", Boolean.TRUE);
            var2.setValue("since", "10.3.1.0");
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
