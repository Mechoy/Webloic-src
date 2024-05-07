package weblogic.sca.runtime.impl;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SCAPojoComponentRuntimeMBean;
import weblogic.management.runtime.WorkManagerRuntimeMBean;

public class SCAPojoComponentRuntimeImpl extends RuntimeMBeanDelegate implements SCAPojoComponentRuntimeMBean {
   private static final WorkManagerRuntimeMBean[] EMPTY = new WorkManagerRuntimeMBean[0];
   private final String componentName;
   private int state;

   public SCAPojoComponentRuntimeImpl(String var1) throws ManagementException {
      super(var1, true);
      this.componentName = var1;
   }

   public String getModuleId() {
      return this.componentName;
   }

   public int getDeploymentState() {
      return this.state;
   }

   public void setDeploymentState(int var1) {
      this.state = var1;
   }

   public boolean addWorkManagerRuntime(WorkManagerRuntimeMBean var1) {
      return false;
   }

   public WorkManagerRuntimeMBean[] getWorkManagerRuntimes() {
      return EMPTY;
   }
}
