package weblogic.management.mbeans.custom;

import java.util.Properties;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;
import weblogic.nodemanager.mbean.NodeManagerRuntime;

public class ServerStart extends ConfigurationMBeanCustomizer {
   public ServerStart(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   private ServerMBean getServer() {
      return (ServerMBean)this.getMbean().getParent();
   }

   private Properties errorProps(String var1) {
      Properties var2 = new Properties();
      var2.setProperty("Error", var1);
      return var2;
   }

   public Properties getBootProperties() {
      String var1 = this.getServer().getName();
      if (var1 != null && !var1.equals("")) {
         NodeManagerRuntime var3 = NodeManagerRuntime.getInstance(this.getServer());
         return var3.getBootProperties(this.getServer());
      } else {
         String var2 = "Could not get boot properties for server: Server name is not set";
         return this.errorProps(var2);
      }
   }

   public Properties getStartupProperties() {
      String var1 = this.getServer().getName();
      if (var1 != null && !var1.equals("")) {
         NodeManagerRuntime var3 = NodeManagerRuntime.getInstance(this.getServer());
         return var3.getStartupProperties(this.getServer());
      } else {
         String var2 = "Could not get startup properties for server: Server name is not set";
         return this.errorProps(var2);
      }
   }
}
