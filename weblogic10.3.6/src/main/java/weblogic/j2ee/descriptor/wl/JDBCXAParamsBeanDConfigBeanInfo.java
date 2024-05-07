package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class JDBCXAParamsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(JDBCXAParamsBeanDConfig.class);
   static PropertyDescriptor[] pds = null;

   public BeanDescriptor getBeanDescriptor() {
      return this.bd;
   }

   public PropertyDescriptor[] getPropertyDescriptors() {
      if (pds != null) {
         return pds;
      } else {
         ArrayList var2 = new ArrayList();

         try {
            PropertyDescriptor var1 = new PropertyDescriptor("KeepXaConnTillTxComplete", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "isKeepXaConnTillTxComplete", "setKeepXaConnTillTxComplete");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("NeedTxCtxOnClose", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "isNeedTxCtxOnClose", "setNeedTxCtxOnClose");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("XaEndOnlyOnce", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "isXaEndOnlyOnce", "setXaEndOnlyOnce");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("NewXaConnForCommit", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "isNewXaConnForCommit", "setNewXaConnForCommit");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("KeepLogicalConnOpenOnRelease", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "isKeepLogicalConnOpenOnRelease", "setKeepLogicalConnOpenOnRelease");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ResourceHealthMonitoring", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "isResourceHealthMonitoring", "setResourceHealthMonitoring");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RecoverOnlyOnce", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "isRecoverOnlyOnce", "setRecoverOnlyOnce");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("XaSetTransactionTimeout", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "isXaSetTransactionTimeout", "setXaSetTransactionTimeout");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("XaTransactionTimeout", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "getXaTransactionTimeout", "setXaTransactionTimeout");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RollbackLocalTxUponConnClose", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "isRollbackLocalTxUponConnClose", "setRollbackLocalTxUponConnClose");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("XaRetryDurationSeconds", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "getXaRetryDurationSeconds", "setXaRetryDurationSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("XaRetryIntervalSeconds", Class.forName("weblogic.j2ee.descriptor.wl.JDBCXAParamsBeanDConfig"), "getXaRetryIntervalSeconds", "setXaRetryIntervalSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for JDBCXAParamsBeanDConfigBeanInfo");
         }
      }
   }
}
