package weblogic.sca.runtime.impl;

import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SCASpringComponentRuntimeMBean;
import weblogic.management.runtime.WorkManagerRuntimeMBean;

public class SCASpringComponentRuntimeImpl extends RuntimeMBeanDelegate implements SCASpringComponentRuntimeMBean {
   private static final WorkManagerRuntimeMBean[] EMPTY = new WorkManagerRuntimeMBean[0];
   private final String name;
   private int state;

   public SCASpringComponentRuntimeImpl(String var1) throws ManagementException {
      super(var1, true);
      this.name = var1;
   }

   public String getModuleId() {
      return this.name;
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
