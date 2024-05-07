package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.mbean.typing.MBeanCategorizer;
import javax.management.MBeanServerConnection;

class JMXHarvesterConfig {
   private String harvesterName;
   private String namespace;
   private MBeanCategorizer categorizer;
   private boolean polling;
   private MBeanServerConnection mbeanServer;

   public JMXHarvesterConfig(String var1, String var2, MBeanCategorizer var3, boolean var4, MBeanServerConnection var5) {
      this.harvesterName = var1;
      this.namespace = var2;
      this.categorizer = var3;
      this.polling = var4;
      this.mbeanServer = var5;
   }

   public String getHarvesterName() {
      return this.harvesterName;
   }

   public void setHarvesterName(String var1) {
      this.harvesterName = var1;
   }

   public String getNamespace() {
      return this.namespace;
   }

   public void setNamespace(String var1) {
      this.namespace = var1;
   }

   public MBeanCategorizer getCategorizer() {
      return this.categorizer;
   }

   public void setCategorizer(MBeanCategorizer var1) {
      this.categorizer = var1;
   }

   public boolean isPolling() {
      return this.polling;
   }

   public void setPolling(boolean var1) {
      this.polling = var1;
   }

   public MBeanServerConnection getMbeanServer() {
      return this.mbeanServer;
   }

   protected void setMbeanServer(MBeanServerConnection var1) {
      this.mbeanServer = var1;
   }
}
