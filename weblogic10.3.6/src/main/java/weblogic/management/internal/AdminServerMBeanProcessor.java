package weblogic.management.internal;

import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.AccessCallback;

public class AdminServerMBeanProcessor implements AccessCallback {
   public void accessed(DomainMBean var1) {
      var1.createAdminServerMBean();
   }

   public void shutdown() {
   }
}
