package weblogic.j2ee;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WorkManagerRuntimeMBean;

public class ComponentRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ComponentRuntimeMBean {
   private final String moduleId;
   protected int state = 3;
   private Set workManagerRuntimes = Collections.synchronizedSet(new HashSet());

   public ComponentRuntimeMBeanImpl(String var1, String var2, boolean var3) throws ManagementException {
      super(var1, var3, "ComponentRuntimes");
      this.moduleId = var2;
   }

   public ComponentRuntimeMBeanImpl(String var1, String var2, RuntimeMBean var3, boolean var4) throws ManagementException {
      super(var1, var3, var4, "ComponentRuntimes");
      this.moduleId = var2;
   }

   public ComponentRuntimeMBeanImpl(String var1, String var2, RuntimeMBean var3, boolean var4, DescriptorBean var5) throws ManagementException {
      super(var1, var3, var4, var5, "ComponentRuntimes");
      this.moduleId = var2;
   }

   public ComponentRuntimeMBeanImpl(String var1, String var2, RuntimeMBean var3) throws ManagementException {
      super(var1, var3);
      this.moduleId = var2;
   }

   public String getModuleId() {
      return this.moduleId;
   }

   public int getDeploymentState() {
      return this.state;
   }

   public void setDeploymentState(int var1) {
      this.state = var1;
   }

   public boolean addWorkManagerRuntime(WorkManagerRuntimeMBean var1) {
      return this.workManagerRuntimes.add(var1);
   }

   public WorkManagerRuntimeMBean[] getWorkManagerRuntimes() {
      int var1 = this.workManagerRuntimes.size();
      return (WorkManagerRuntimeMBean[])((WorkManagerRuntimeMBean[])this.workManagerRuntimes.toArray(new WorkManagerRuntimeMBean[var1]));
   }

   public void unregister() throws ManagementException {
      super.unregister();
   }
}
