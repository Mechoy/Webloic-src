package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class XAParamsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(XAParamsBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("DebugLevel", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "getDebugLevel", "setDebugLevel");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("KeepConnUntilTxCompleteEnabled", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isKeepConnUntilTxCompleteEnabled", "setKeepConnUntilTxCompleteEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("EndOnlyOnceEnabled", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isEndOnlyOnceEnabled", "setEndOnlyOnceEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RecoverOnlyOnceEnabled", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isRecoverOnlyOnceEnabled", "setRecoverOnlyOnceEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("TxContextOnCloseNeeded", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isTxContextOnCloseNeeded", "setTxContextOnCloseNeeded");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("NewConnForCommitEnabled", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isNewConnForCommitEnabled", "setNewConnForCommitEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("PreparedStatementCacheSize", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "getPreparedStatementCacheSize", "setPreparedStatementCacheSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("KeepLogicalConnOpenOnRelease", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isKeepLogicalConnOpenOnRelease", "setKeepLogicalConnOpenOnRelease");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("LocalTransactionSupported", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isLocalTransactionSupported", "setLocalTransactionSupported");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("ResourceHealthMonitoringEnabled", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isResourceHealthMonitoringEnabled", "setResourceHealthMonitoringEnabled");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("XaSetTransactionTimeout", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isXaSetTransactionTimeout", "setXaSetTransactionTimeout");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("XaTransactionTimeout", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "getXaTransactionTimeout", "setXaTransactionTimeout");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RollbackLocaltxUponConnclose", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "isRollbackLocaltxUponConnclose", "setRollbackLocaltxUponConnclose");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("XaRetryDurationSeconds", Class.forName("weblogic.j2ee.descriptor.wl.XAParamsBeanDConfig"), "getXaRetryDurationSeconds", "setXaRetryDurationSeconds");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", false);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            pds = (PropertyDescriptor[])((PropertyDescriptor[])var2.toArray(new PropertyDescriptor[0]));
            return pds;
         } catch (Throwable var4) {
            var4.printStackTrace();
            throw new AssertionError("Failed to create PropertyDescriptors for XAParamsBeanDConfigBeanInfo");
         }
      }
   }
}
