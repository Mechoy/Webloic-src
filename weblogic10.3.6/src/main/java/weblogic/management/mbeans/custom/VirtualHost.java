package weblogic.management.mbeans.custom;

import java.util.HashSet;
import java.util.Set;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.configuration.VirtualHostMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public final class VirtualHost extends WebServer {
   private static final long serialVersionUID = -1880586135310564482L;

   public VirtualHost(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public Set getServerNames() {
      TargetMBean[] var1 = ((VirtualHostMBean)this.getMbean()).getTargets();
      HashSet var2 = new HashSet();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.addAll(var1[var3].getServerNames());
      }

      return var2;
   }
}
