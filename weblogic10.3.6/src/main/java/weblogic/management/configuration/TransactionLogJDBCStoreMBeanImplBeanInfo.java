package weblogic.management.configuration;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Map;

public class TransactionLogJDBCStoreMBeanImplBeanInfo extends JDBCStoreMBeanImplBeanInfo {
   public static Class INTERFACE_CLASS = TransactionLogJDBCStoreMBean.class;

   public TransactionLogJDBCStoreMBeanImplBeanInfo(boolean var1, String var2) throws IntrospectionException {
      super(var1, var2);
   }

   public TransactionLogJDBCStoreMBeanImplBeanInfo() throws IntrospectionException {
   }

   protected BeanDescriptor buildBeanDescriptor() {
      Class var1 = null;

      try {
         var1 = TransactionLogJDBCStoreMBeanImpl.class;
      } catch (Throwable var4) {
         var1 = INTERFACE_CLASS;
      }

      BeanDescriptor var2 = new BeanDescriptor(var1, (Class)null);
      var2.setValue("since", "10.3.6.0");
      var2.setValue("package", "weblogic.management.configuration");
      String var3 = (new String("<p>This class represents a Transaction Log JDBC Store configuration.</p> ")).intern();
      var2.setShortDescription(var3);
      var2.setValue("description", var3);
      var2.setValue("interfaceclassname", "weblogic.management.configuration.TransactionLogJDBCStoreMBean");
      var2.setValue("generatedByWLSInfoBinder", Boolean.TRUE);
      return var2;
   }

   protected void buildPropertyDescriptors(Map var1) throws IntrospectionException {
      PropertyDescriptor var2 = null;
      String var3;
      String var4;
      if (!var1.containsKey("MaxRetrySecondsBeforeTLOGFail")) {
         var3 = "getMaxRetrySecondsBeforeTLOGFail";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxRetrySecondsBeforeTLOGFail";
         }

         var2 = new PropertyDescriptor("MaxRetrySecondsBeforeTLOGFail", TransactionLogJDBCStoreMBean.class, var3, var4);
         var1.put("MaxRetrySecondsBeforeTLOGFail", var2);
         var2.setValue("description", "<p>The maximum amount of time, in seconds, WebLogic Server tries to recover from a JDBC TLog store failure. If store remains unusable after this period, WebLogic Server set the health state to <code>HEALTH_FAILED</code>. A value of 0 indicates WebLogic Server does not conduct a retry and and immediately sets the health state as <code>HEALTH_FAILED</code>. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(300));
         var2.setValue("legalMax", new Integer(Integer.MAX_VALUE));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("MaxRetrySecondsBeforeTXException")) {
         var3 = "getMaxRetrySecondsBeforeTXException";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setMaxRetrySecondsBeforeTXException";
         }

         var2 = new PropertyDescriptor("MaxRetrySecondsBeforeTXException", TransactionLogJDBCStoreMBean.class, var3, var4);
         var1.put("MaxRetrySecondsBeforeTXException", var2);
         var2.setValue("description", "<p>The maximum amount of time, in seconds, WebLogic Server waits before trying to recover from a JDBC TLog store failure while processing a transaction. If store remains unusable after this amount of time, WebLogic Server throws an exception the affected transaction. A value of 0 indicates WebLogic Server does not conduct a retry and an exception will thrown immediately. The practical maximum value is a value less than the current value of <code>MaxRetrySecondsBeforeTLogFail</code>.</p> ");
         setPropertyDescriptorDefault(var2, new Integer(60));
         var2.setValue("legalMax", new Integer(300));
         var2.setValue("legalMin", new Integer(0));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("PrefixName")) {
         var3 = "getPrefixName";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setPrefixName";
         }

         var2 = new PropertyDescriptor("PrefixName", TransactionLogJDBCStoreMBean.class, var3, var4);
         var1.put("PrefixName", var2);
         var2.setValue("description", "<p>When using multiple TLOG JDBC stores, use this attribute to create a label ending in \"_\"  that is prepended to the name of the server hosting the JDBC TLOG store and ends in \"_\" to form a unique JDBC TLOG store name for each configured JDBC TLOG store. </p> The default prefix name is \"TLOG_\" . For example, a valid JDBC TLOG store name using the default Prefix Name is <code>TLOG_MyServer_ </code> where TLOG_ is the Prefix Name and MyServer is the name of the server hosting the JDBC TLOG store. ");
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("RetryIntervalSeconds")) {
         var3 = "getRetryIntervalSeconds";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setRetryIntervalSeconds";
         }

         var2 = new PropertyDescriptor("RetryIntervalSeconds", TransactionLogJDBCStoreMBean.class, var3, var4);
         var1.put("RetryIntervalSeconds", var2);
         var2.setValue("description", "<p>The amount of time, in seconds, WebLogic Server waits before attempting to verify the health of the TLOG store after a store failure has occurred. </p> ");
         setPropertyDescriptorDefault(var2, new Integer(5));
         var2.setValue("legalMax", new Integer(60));
         var2.setValue("legalMin", new Integer(1));
         var2.setValue("configurable", Boolean.TRUE);
         var2.setValue("dynamic", Boolean.TRUE);
      }

      if (!var1.containsKey("Enabled")) {
         var3 = "isEnabled";
         var4 = null;
         if (!this.readOnly) {
            var4 = "setEnabled";
         }

         var2 = new PropertyDescriptor("Enabled", TransactionLogJDBCStoreMBean.class, var3, var4);
         var1.put("Enabled", var2);
         var2.setValue("description", "<p>When true, TLOGs are logged to a TLOG JDBC Store; otherwise, TLOGs are logged to the server's default store.</p> <p>When using the Administration Console, select <b>JDBC</b> to enable logging of TLOGs to a JDBC store; select <b>Default Store</b> to enable logging of TLOGs to the server's default store. </p> ");
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
