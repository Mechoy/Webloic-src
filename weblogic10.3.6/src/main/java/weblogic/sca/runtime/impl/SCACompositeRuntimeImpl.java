package weblogic.sca.runtime.impl;

import java.util.Iterator;
import java.util.Vector;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SCACompositeRuntimeMBean;

public class SCACompositeRuntimeImpl extends RuntimeMBeanDelegate implements SCACompositeRuntimeMBean {
   private final Vector<ComponentRuntimeMBean> components = new Vector();
   private final String compositeName;
   private final String descriptor;

   public SCACompositeRuntimeImpl(String var1, String var2) throws ManagementException {
      super(var1, true);
      this.compositeName = var1;
      this.descriptor = var2;
   }

   public String getCompositeName() {
      return this.compositeName;
   }

   public String getDescriptor() {
      return this.descriptor;
   }

   public ComponentRuntimeMBean[] getComponentRuntimes() {
      ComponentRuntimeMBean[] var1 = new ComponentRuntimeMBean[this.components.size()];
      return (ComponentRuntimeMBean[])this.components.toArray(var1);
   }

   public void addComponentRuntimeMBean(ComponentRuntimeMBean var1) {
      this.components.add(var1);
   }

   public void changeComponentState(int var1) {
      Iterator var2 = this.components.iterator();

      while(var2.hasNext()) {
         ComponentRuntimeMBean var3 = (ComponentRuntimeMBean)var2.next();
         var3.setDeploymentState(var1);
      }

   }
}
