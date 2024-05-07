package weblogic.management.mbeans.custom;

import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.JMSTopicMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public class JMSDistributedTopicMember extends JMSDistributedDestinationMember {
   private static final String DT_MEMBER = "JMSTopic";
   private static final String TOPIC_PROP = "PhysicalDestinationName";
   private DistributedDestinationMemberBean delegate;
   private DomainMBean domain;

   public JMSDistributedTopicMember(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DomainMBean var1, DistributedDestinationMemberBean var2) {
      this.domain = var1;
      this.delegate = var2;
      super.useDelegates(this.delegate);
   }

   public JMSTopicMBean getJMSTopic() {
      if (this.delegate == null) {
         return (JMSTopicMBean)this.getValue("JMSTopic");
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
               TopicBean var3 = var2.lookupTopic(var1);
               if (var3 == null) {
                  return null;
               } else {
                  String var4 = var3.getSubDeploymentName();
                  JMSServerMBean var5 = this.domain.lookupJMSServer(var4);
                  return var5 == null ? null : var5.lookupJMSTopic(var1);
               }
            }
         }
      }
   }

   public void setJMSTopic(JMSTopicMBean var1) {
      if (this.delegate == null) {
         this.putValue("JMSTopic", var1);
      } else {
         if (var1 == null) {
            this.delegate.unSet("PhysicalDestinationName");
         } else {
            this.delegate.setPhysicalDestinationName(var1.getName());
         }

      }
   }
}
