package weblogic.management.mbeans.custom;

import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSDistributedTopicMBean;
import weblogic.management.configuration.JMSDistributedTopicMemberMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.utils.ArrayUtils;

public class JMSDistributedTopic extends JMSDistributedDestination {
   static final long serialVersionUID = 175403281312376358L;
   private DistributedTopicBean delegate;

   public JMSDistributedTopic(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(DistributedTopicBean var1, SubDeploymentMBean var2) {
      this.delegate = var1;
      super.useDelegates(this.delegate, var2);
   }

   public JMSDistributedTopicMemberMBean createJMSDistributedTopicMember(String var1, JMSDistributedTopicMemberMBean var2) {
      try {
         return (JMSDistributedTopicMemberMBean)this.getMbean().createChildCopyIncludingObsolete("JMSDistributedTopicMember", var2);
      } catch (IllegalArgumentException var4) {
         throw new Error(var4);
      }
   }

   public void destroyJMSDistributedTopicMember(String var1, JMSDistributedTopicMemberMBean var2) {
      this.removeMember(var2);
   }

   public JMSDistributedTopicMemberMBean[] getMembers() {
      return ((JMSDistributedTopicMBean)this.getMbean()).getJMSDistributedTopicMembers();
   }

   /** @deprecated */
   public void setMembers(JMSDistributedTopicMemberMBean[] var1) {
      ArrayUtils.computeDiff(this.getMembers(), var1, new ArrayUtils.DiffHandler() {
         public void addObject(Object var1) {
            JMSDistributedTopic.this.addMember((JMSDistributedTopicMemberMBean)var1);
         }

         public void removeObject(Object var1) {
            JMSDistributedTopic.this.removeMember((JMSDistributedTopicMemberMBean)var1);
         }
      });
   }

   /** @deprecated */
   public boolean addMember(JMSDistributedTopicMemberMBean var1) {
      JMSDistributedTopicMBean var2 = (JMSDistributedTopicMBean)this.getMbean();
      if (var2.lookupJMSDistributedTopicMember(var1.getName()) != null) {
         return true;
      } else {
         JMSDistributedTopicMemberMBean var10000 = (JMSDistributedTopicMemberMBean)this.getMbean().createChildCopy("JMSDistributedTopicMember", var1);
         DomainMBean var4 = (DomainMBean)var2.getParentBean();
         var4.destroyJMSDistributedTopicMember(var1);
         return true;
      }
   }

   /** @deprecated */
   public boolean removeMember(JMSDistributedTopicMemberMBean var1) {
      JMSDistributedTopicMBean var2 = (JMSDistributedTopicMBean)this.getMbean();
      DomainMBean var3 = (DomainMBean)var2.getParentBean();
      if (var2.lookupJMSDistributedTopicMember(var1.getName()) == null) {
         return true;
      } else {
         var3.createJMSDistributedTopicMember(var1.getName(), var1);
         var2.destroyJMSDistributedTopicMember(var1);
         return true;
      }
   }
}
