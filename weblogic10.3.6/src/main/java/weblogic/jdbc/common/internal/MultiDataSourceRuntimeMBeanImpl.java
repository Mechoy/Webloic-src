package weblogic.jdbc.common.internal;

import weblogic.descriptor.DescriptorBean;
import weblogic.management.ManagementException;
import weblogic.management.runtime.JDBCMultiDataSourceRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WorkManagerRuntimeMBean;

public final class MultiDataSourceRuntimeMBeanImpl extends RuntimeMBeanDelegate implements JDBCMultiDataSourceRuntimeMBean {
   private MultiPool multipool;
   private int state = 1;

   public MultiDataSourceRuntimeMBeanImpl(MultiPool var1, String var2, RuntimeMBean var3, DescriptorBean var4) throws ManagementException {
      super(var2, var3, true, var4);
      this.multipool = var1;
   }

   public void setDeploymentState(int var1) {
      this.state = var1;
   }

   public int getDeploymentState() {
      return this.state;
   }

   public boolean addWorkManagerRuntime(WorkManagerRuntimeMBean var1) {
      return true;
   }

   public WorkManagerRuntimeMBean[] getWorkManagerRuntimes() {
      return null;
   }

   public String getModuleId() {
      return this.getName();
   }

   public int getMaxCapacity() {
      return this.multipool.getMaxCapacity();
   }
}
