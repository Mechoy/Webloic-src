package weblogic.management.mbeanservers.runtime.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanAttributeInfo;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.QueryExp;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.diagnostics.image.descriptor.InstanceMetricBean;
import weblogic.diagnostics.image.descriptor.JMXDomainStatisticsBean;
import weblogic.diagnostics.image.descriptor.ManagementRuntimeImageBean;
import weblogic.diagnostics.image.descriptor.MetricBean;
import weblogic.diagnostics.image.descriptor.ServerRuntimeStateBean;
import weblogic.diagnostics.image.descriptor.ServerRuntimeStatisticsBean;
import weblogic.management.jmx.mbeanserver.WLSMBeanServer;

public class ManagementRuntimeImageSource implements ImageSource {
   private static final String[][] METRICS_TABLE = new String[][]{{"com.bea:Type=WorkManagerRuntime,*", "StuckThreadCount"}, {"com.bea:Type=JRockitRuntime,*", "HeapFreePercent", "JvmProcessorLoad"}, {"com.bea:Type=JVMRuntime,*", "HeapFreePercent"}, {"com.bea:Type=ThreadPoolRuntime,*", "HoggingThreadCount", "PendingUserRequestCount", "Throughput"}, {"com.bea:Type=JDBCDataSourceRuntime,*", "LeakedConnectionCount", "ActiveConnectionsCurrentCount", "ConnectionDelayTime", "LeakedConnectionCount", "NumAvailable", "ReserveRequestCount"}, {"com.bea:Type=JTARuntime,*", "ActiveTransactionsTotalCount", "SecondsActiveTotalCount"}, {"com.bea:Type=LogBroadcasterRuntime,*", "MessagesLogged"}, {"com.bea:Type=ServerRuntime,*", "OpenSocketsCurrentCount", "State"}, {"com.bea:Type=WebAppComponentRuntime,*", "OpenSessionsCurrentCount"}};
   private WLSMBeanServer mbs;
   private boolean timedOut;

   public ManagementRuntimeImageSource(WLSMBeanServer var1) {
      this.mbs = var1;
   }

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      try {
         DescriptorManager var2 = new DescriptorManager();
         Descriptor var3 = var2.createDescriptorRoot(ManagementRuntimeImageBean.class);
         ManagementRuntimeImageBean var4 = (ManagementRuntimeImageBean)var3.getRootBean();
         ServerRuntimeStatisticsBean var5 = var4.getServerRuntimeStatistics();
         var5.setTotalRegisteredMBeansCount(this.mbs.getMBeanCount());
         String[] var6 = this.mbs.getDomains();
         String[] var7 = var6;
         int var8 = var6.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String var10 = var7[var9];
            if (this.timedOut) {
               break;
            }

            Set var11 = this.mbs.queryMBeans(new ObjectName(var10 + ":*"), (QueryExp)null);
            JMXDomainStatisticsBean var12 = var5.createJMXDomainStatistic();
            var12.setDomainName(var10);
            var12.setCount(var11.size());
         }

         this.populateMBeanStats(var4);
         this.writeImageSourceBean(var1, var2, var3);
      } catch (Exception var13) {
         throw new ImageSourceCreationException(var13);
      }
   }

   private void writeImageSourceBean(OutputStream var1, DescriptorManager var2, Descriptor var3) throws ImageSourceCreationException {
      if (!this.timedOut) {
         try {
            var2.writeDescriptorAsXML(var3, var1);
         } catch (IOException var5) {
            throw new ImageSourceCreationException(var5);
         }
      }
   }

   private void populateMBeanStats(ManagementRuntimeImageBean var1) throws Exception {
      if (!this.timedOut) {
         ServerRuntimeStateBean var2 = var1.getServerRuntimeState();
         String[][] var3 = METRICS_TABLE;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String[] var6 = var3[var5];
            if (this.timedOut) {
               break;
            }

            this.queryValues(var2, var6);
         }

      }
   }

   public void timeoutImageCreation() {
      this.timedOut = true;
   }

   private void queryValues(ServerRuntimeStateBean var1, String[] var2) throws Exception {
      ObjectName var3 = new ObjectName(var2[0]);
      Set var4 = this.mbs.queryMBeans(var3, (QueryExp)null);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         Object var6 = var5.next();
         if (this.timedOut) {
            break;
         }

         ObjectInstance var7 = (ObjectInstance)var6;
         InstanceMetricBean var8 = var1.createInstanceMetric();
         var8.setInstanceName(var7.getObjectName().getCanonicalName());
         String[] var9 = this.copyAttributes(var2);
         AttributeList var10 = this.mbs.getAttributes(var7.getObjectName(), var9);
         Iterator var11 = var10.iterator();

         while(var11.hasNext()) {
            Object var12 = var11.next();
            Attribute var13 = (Attribute)var12;
            String var14 = this.getAttributeType(var7.getObjectName(), var13.getName());
            MetricBean var15 = var8.createMBeanMetric();
            var15.setAttributeName(var13.getName());
            var15.setAttributeType(var14);
            var15.setAttributeValue(var13.getValue().toString());
         }
      }

   }

   private String[] copyAttributes(String[] var1) {
      String[] var2 = new String[var1.length - 1];
      System.arraycopy(var1, 1, var2, 0, Math.min(var1.length - 1, var2.length));
      return var2;
   }

   private String getAttributeType(ObjectName var1, String var2) throws Exception {
      MBeanAttributeInfo[] var3 = this.mbs.getMBeanInfo(var1).getAttributes();
      MBeanAttributeInfo[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         MBeanAttributeInfo var7 = var4[var6];
         if (var7.getName().equals(var2)) {
            return var7.getType();
         }
      }

      return String.class.getName();
   }
}
