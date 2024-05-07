package weblogic.jms.backend;

import javax.jms.JMSException;
import javax.naming.Context;
import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.ModuleName;
import weblogic.management.ManagementException;

public final class BEQueueRuntimeDelegate extends BEDestinationRuntimeDelegate {
   public BEQueueRuntimeDelegate(EntityName var1, BackEnd var2, Context var3, boolean var4, ModuleName var5, JMSBean var6, QueueBean var7) {
      super(var1, var3, var2, var6, var7, var4, var5);
   }

   protected void initialize(int var1) throws ModuleException {
      try {
         BEQueueImpl var2 = new BEQueueImpl(this.backEnd, this.entityName.toString(), this.temporary, new BEDestinationSecurityImpl(this.entityName, "queue"));
         this.setManagedDestination(var2);
         super.initialize(var1);
         this.getRuntimeMBean().setMessageManagementDelegate(new BEMessageManagementImpl(this.entityName.toString(), var2.getKernelQueue(), var2, this.getRuntimeMBean()));
      } catch (JMSException var3) {
         throw new ModuleException(var3);
      } catch (ManagementException var4) {
         throw new ModuleException(var4);
      }
   }
}
