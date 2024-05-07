package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSDistributedQueueMBean;
import weblogic.management.configuration.JMSDistributedQueueMemberMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.utils.ArrayUtils;

public class JMSDistributedQueue extends JMSDistributedDestination {
   static final long serialVersionUID = -5251076212640474307L;
   private DistributedQueueBean delegate;

   public JMSDistributedQueue(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DistributedQueueBean var1, SubDeploymentMBean var2) {
      this.delegate = var1;
      super.useDelegates(this.delegate, var2);
   }

   public JMSDistributedQueueMemberMBean createJMSDistributedQueueMember(String var1, JMSDistributedQueueMemberMBean var2) {
      try {
         return (JMSDistributedQueueMemberMBean)((JMSDistributedQueueMemberMBean)this.getMbean().createChildCopy("JMSDistributedQueueMember", var2));
      } catch (IllegalArgumentException var4) {
         throw new Error(var4);
      }
   }

   public void destroyJMSDistributedQueueMember(String var1, JMSDistributedQueueMemberMBean var2) {
      this.removeMember(var2);
   }

   public JMSDistributedQueueMemberMBean[] getMembers() {
      return ((JMSDistributedQueueMBean)this.getMbean()).getJMSDistributedQueueMembers();
   }

   /** @deprecated */
   public void setMembers(JMSDistributedQueueMemberMBean[] var1) {
      ArrayUtils.computeDiff(this.getMembers(), var1, new ArrayUtils.DiffHandler() {
         public void addObject(Object var1) {
            JMSDistributedQueue.this.addMember((JMSDistributedQueueMemberMBean)var1);
         }

         public void removeObject(Object var1) {
            JMSDistributedQueue.this.removeMember((JMSDistributedQueueMemberMBean)var1);
         }
      });
   }

   /** @deprecated */
   public boolean addMember(JMSDistributedQueueMemberMBean var1) {
      JMSDistributedQueueMBean var2 = (JMSDistributedQueueMBean)this.getMbean();
      if (var2.lookupJMSDistributedQueueMember(var1.getName()) != null) {
         return true;
      } else {
         JMSDistributedQueueMemberMBean var10000 = (JMSDistributedQueueMemberMBean)this.getMbean().createChildCopy("JMSDistributedQueueMember", var1);
         DomainMBean var4 = (DomainMBean)var2.getParentBean();
         var4.destroyJMSDistributedQueueMember(var1);
         return true;
      }
   }

   /** @deprecated */
   public boolean removeMember(JMSDistributedQueueMemberMBean var1) {
      JMSDistributedQueueMBean var2 = (JMSDistributedQueueMBean)this.getMbean();
      DomainMBean var3 = (DomainMBean)var2.getParentBean();
      if (var2.lookupJMSDistributedQueueMember(var1.getName()) == null) {
         return true;
      } else {
         var3.createJMSDistributedQueueMember(var1.getName(), var1);
         var2.destroyJMSDistributedQueueMember(var1);
         return true;
      }
   }

   public int getForwardDelay() {
      if (this.delegate == null) {
         Object var1 = this.getValue("ForwardDelay");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : -1;
      } else {
         return this.delegate.getForwardDelay();
      }
   }

   public void setForwardDelay(int var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("ForwardDelay", new Integer(var1));
      } else {
         this.delegate.setForwardDelay(var1);
      }

   }

   public boolean getResetDeliveryCountOnFoward() {
      if (this.delegate == null) {
         Object var1 = this.getValue("ResetDeliveryCountOnForward");
         return var1 != null && var1 instanceof Boolean ? (Boolean)var1 : true;
      } else {
         return this.delegate.getResetDeliveryCountOnForward();
      }
   }

   public void setResetDeliveryCountOnForward(boolean var1) {
      if (this.delegate == null) {
         this.putValue("ResetDeliveryCountOnForward", new Boolean(var1));
      } else {
         this.delegate.setResetDeliveryCountOnForward(var1);
      }

   }
}
