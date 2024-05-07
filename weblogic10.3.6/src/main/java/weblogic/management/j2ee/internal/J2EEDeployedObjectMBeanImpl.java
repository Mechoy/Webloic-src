package weblogic.management.j2ee.internal;

import weblogic.management.j2ee.J2EEDeployedObjectMBean;

public class J2EEDeployedObjectMBeanImpl extends J2EEManagedObjectMBeanImpl implements J2EEDeployedObjectMBean {
   protected final String serverName;
   protected ApplicationInfo info;

   J2EEDeployedObjectMBeanImpl(String var1, String var2, ApplicationInfo var3) {
      super(var1);
      this.serverName = var2;
      this.info = var3;
   }

   public String getdeploymentDescriptor() {
      return this.info.getDescriptor();
   }

   public String getserver() {
      return this.serverName;
   }

   public String getproductSpecificDeploymentDescriptor() {
      return this.info.getWebLogicDescriptor();
   }
}
