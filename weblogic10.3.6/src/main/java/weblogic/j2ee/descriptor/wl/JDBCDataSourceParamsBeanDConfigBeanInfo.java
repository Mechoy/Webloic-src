package weblogic.j2ee.descriptor.wl;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;

public class JDBCDataSourceParamsBeanDConfigBeanInfo extends SimpleBeanInfo {
   BeanDescriptor bd = new BeanDescriptor(JDBCDataSourceParamsBeanDConfig.class);
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
            PropertyDescriptor var1 = new PropertyDescriptor("JNDINames", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "getJNDINames", "setJNDINames");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("Scope", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "getScope", "setScope");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RowPrefetch", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "isRowPrefetch", "setRowPrefetch");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("RowPrefetchSize", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "getRowPrefetchSize", "setRowPrefetchSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("StreamChunkSize", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "getStreamChunkSize", "setStreamChunkSize");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("AlgorithmType", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "getAlgorithmType", "setAlgorithmType");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("DataSourceList", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "getDataSourceList", "setDataSourceList");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", true);
            var2.add(var1);
            var1 = new PropertyDescriptor("ConnectionPoolFailoverCallbackHandler", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "getConnectionPoolFailoverCallbackHandler", "setConnectionPoolFailoverCallbackHandler");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("FailoverRequestIfBusy", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "isFailoverRequestIfBusy", "setFailoverRequestIfBusy");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("GlobalTransactionsProtocol", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "getGlobalTransactionsProtocol", "setGlobalTransactionsProtocol");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("KeepConnAfterLocalTx", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "isKeepConnAfterLocalTx", "setKeepConnAfterLocalTx");
            var1.setValue("dependency", false);
            var1.setValue("declaration", false);
            var1.setValue("configurable", true);
            var1.setValue("key", false);
            var1.setValue("dynamic", false);
            var2.add(var1);
            var1 = new PropertyDescriptor("KeepConnAfterGlobalTx", Class.forName("weblogic.j2ee.descriptor.wl.JDBCDataSourceParamsBeanDConfig"), "isKeepConnAfterGlobalTx", "setKeepConnAfterGlobalTx");
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
            throw new AssertionError("Failed to create PropertyDescriptors for JDBCDataSourceParamsBeanDConfigBeanInfo");
         }
      }
   }
}
