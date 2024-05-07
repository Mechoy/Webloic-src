package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;
import weblogic.management.internal.mbean.BeanInfoHelper;

public class WTCResourcesMBeanImplBeanInfo extends ConfigurationMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = WTCResourcesMBean.class;

   public WTCResourcesMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public WTCResourcesMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = WTCResourcesMBeanImpl.class;
      } catch (Throwable var5) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "7.0.0.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("This interface provides access to the WTC resources configuration attributes. The methods defined herein are applicable for WTC configuration at the WLS domain level.  <h3 class=\"TypeSafeDeprecation\">Deprecation of MBeanHome and Type-Safe Interfaces</h3>  <p class=\"TypeSafeDeprecation\">This is a type-safe interface for a WebLogic Server MBean, which you can import into your client classes and access through <code>weblogic.management.MBeanHome</code>. As of 9.0, the <code>MBeanHome</code> interface and all type-safe interfaces for WebLogic Server MBeans are deprecated. Instead, client classes that interact with WebLogic Server MBeans should use standard JMX design patterns in which clients use the <code>javax.management.MBeanServerConnection</code> interface to discover MBeans, attributes, and attribute types at runtime. For more information, see \"Developing Manageable Applications with JMX.\"</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      String[] var4 = new String[]{BeanInfoHelper.encodeEntities("Deployer")};
      var2.setValue("rolesAllowed", var4);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.WTCResourcesMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("AppPassword")) {
         var3 = "getAppPassword";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAppPassword";
         }

         var2 = new PropertyDescriptor("AppPassword", WTCResourcesMBean.class, var3, var4);
         var1.put("AppPassword", var2);
         var2.setValue("description", "<p>The application password as returned from the <code>genpasswd</code> utility.</p>  <p><i>Note:</i> This Tuxedo application password is the encrypted password used to authenticate connections.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("AppPasswordIV")) {
         var3 = "getAppPasswordIV";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setAppPasswordIV";
         }

         var2 = new PropertyDescriptor("AppPasswordIV", WTCResourcesMBean.class, var3, var4);
         var1.put("AppPasswordIV", var2);
         var2.setValue("description", "<p>The initialization vector used to encrypt the <tt>AppPassword</tt>.</p>  <p><i>Note:</i> This value is returned from the <code>genpasswd</code> utility with the AppPassword.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FldTbl16Classes")) {
         var3 = "getFldTbl16Classes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFldTbl16Classes";
         }

         var2 = new PropertyDescriptor("FldTbl16Classes", WTCResourcesMBean.class, var3, var4);
         var1.put("FldTbl16Classes", var2);
         var2.setValue("description", "<p>The names of <tt>FldTbl16Classes</tt> that are loaded via a class loader and added to a <tt>FldTbl</tt> array.</p>  <p style=\"font-weight: bold\">Value Requirements:</p>  <ul> <li>Used fully qualified names of the desired classes. </li>  <li>Use a comma-separated list to enter multiple classes. </li> </ul> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("FldTbl32Classes")) {
         var3 = "getFldTbl32Classes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setFldTbl32Classes";
         }

         var2 = new PropertyDescriptor("FldTbl32Classes", WTCResourcesMBean.class, var3, var4);
         var1.put("FldTbl32Classes", var2);
         var2.setValue("description", "<p>The names of <tt>FldTbl32Classes</tt> that are loaded via a class loader and added to a <tt>FldTbl</tt> array.</p>  <p style=\"font-weight: bold\">Value Requirements:</p>  <ul> <li>Used fully qualified names of the desired classes. </li>  <li>Use a comma-separated list to enter multiple classes </li> </ul> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MBEncodingMapFile")) {
         var3 = "getMBEncodingMapFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMBEncodingMapFile";
         }

         var2 = new PropertyDescriptor("MBEncodingMapFile", WTCResourcesMBean.class, var3, var4);
         var1.put("MBEncodingMapFile", var2);
         var2.setValue("description", "<p>The encoding name map file between Java and Tuxedo MBSTRING.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RemoteMBEncoding")) {
         var3 = "getRemoteMBEncoding";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRemoteMBEncoding";
         }

         var2 = new PropertyDescriptor("RemoteMBEncoding", WTCResourcesMBean.class, var3, var4);
         var1.put("RemoteMBEncoding", var2);
         var2.setValue("description", "<p>The default encoding name of sending MBSTRING data.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("TpUsrFile")) {
         var3 = "getTpUsrFile";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setTpUsrFile";
         }

         var2 = new PropertyDescriptor("TpUsrFile", WTCResourcesMBean.class, var3, var4);
         var1.put("TpUsrFile", var2);
         var2.setValue("description", "<p>The full path to the <tt>TPUSR</tt> file, which contains Tuxedo UID/GID information.</p>  <p><i>Note:</i> This file is generated by the Tuxedo <code>tpusradd</code> utility on the remote Tuxedo domain.</p> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ViewTbl16Classes")) {
         var3 = "getViewTbl16Classes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setViewTbl16Classes";
         }

         var2 = new PropertyDescriptor("ViewTbl16Classes", WTCResourcesMBean.class, var3, var4);
         var1.put("ViewTbl16Classes", var2);
         var2.setValue("description", "<p>The names of <tt>ViewTbl16Classes</tt> that are loaded via a class loader and added to a <tt>ViewTbl</tt> array.</p>  <p style=\"font-weight: bold\">Value Requirements:</p>  <ul> <li>Used fully qualified names of the desired classes. </li>  <li>Use a comma-separated list to enter multiple classes. </li> </ul> ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("ViewTbl32Classes")) {
         var3 = "getViewTbl32Classes";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setViewTbl32Classes";
         }

         var2 = new PropertyDescriptor("ViewTbl32Classes", WTCResourcesMBean.class, var3, var4);
         var1.put("ViewTbl32Classes", var2);
         var2.setValue("description", "<p>The names of <tt>ViewTbl32Classes</tt> that are loaded via a class loader and added to a <tt>ViewTbl</tt> array.</p>  <p style=\"font-weight: bold\">Value Requirements:</p>  <ul> <li>Used fully qualified names of the desired classes. </li>  <li>Use a comma-separated list to enter multiple classes. </li> </ul> ");
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
