package weblogic.management.mbeans.custom;

import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSQueueMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public class JMSDistributedQueueMember extends JMSDistributedDestinationMember {
   private static final String DQ_MEMBER = "JMSQueue";
   private static final String QUEUE_PROP = "PhysicalDestinationName";
   private DomainMBean domain;
   private DistributedDestinationMemberBean delegate;

   public JMSDistributedQueueMember(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DomainMBean var1, DistributedDestinationMemberBean var2) {
      this.domain = var1;
      this.delegate = var2;
      super.useDelegates(this.delegate);
   }

   public JMSQueueMBean getJMSQueue() {
      if (this.delegate == null) {
         return (JMSQueueMBean)this.getValue("JMSQueue");
      } else if (!this.delegate.isSet("PhysicalDestinationName")) {
         return null;
      } else {
         String var1 = this.delegate.getPhysicalDestinationName();
         if (var1 == null) {
            return null;
         } else {
            JMSBean var2 = JMSBeanHelper.getInteropJMSBean(this.domain);
            if (var2 == null) {
               return null;
            } else {
               QueueBean var3 = var2.lookupQueue(var1);
               if (var3 == null) {
                  return null;
               } else {
                  String var4 = var3.getSubDeploymentName();
                  JMSServerMBean var5 = this.domain.lookupJMSServer(var4);
                  return var5 == null ? null : var5.lookupJMSQueue(var1);
               }
            }
         }
      }
   }

   public void setJMSQueue(JMSQueueMBean var1) {
      if (this.delegate == null) {
         this.putValue("JMSQueue", var1);
      } else {
         if (var1 == null) {
            this.delegate.unSet("PhysicalDestinationName");
         } else {
            this.delegate.setPhysicalDestinationName(var1.getName());
         }

      }
   }
}
