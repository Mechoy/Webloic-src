package weblogic.jms.module.observers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import weblogic.j2ee.descriptor.wl.DistributedDestinationMemberBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.MBeanConverter;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JMSDistributedTopicMBean;
import weblogic.management.configuration.JMSDistributedTopicMemberMBean;
import weblogic.management.configuration.JMSInteropModuleMBean;
import weblogic.management.configuration.JMSSystemResourceMBean;
import weblogic.management.provider.UpdateException;
import weblogic.utils.ArrayUtils;

public class JMSDistributedTopicObserver implements PropertyChangeListener, ArrayUtils.DiffHandler {
   private static final String MEMBERS_STRING = "JMSDistributedTopicMembers";
   private static final String[] handledProperties = new String[]{"JMSDistributedTopicMembers"};
   private static final int UNHANDLED = -1;
   private static final int MEMBER = 0;
   private static final int MAX_PROPERTIES = 1;
   private JMSObserver domainObserver;
   private JMSDistributedTopicMBean distributedTopic;
   private int currentType = -1;

   public JMSDistributedTopicObserver(JMSObserver var1, JMSDistributedTopicMBean var2) {
      this.domainObserver = var1;
      this.distributedTopic = var2;
   }

   public synchronized void propertyChange(PropertyChangeEvent var1) {
      this.currentType = this.getType(var1.getPropertyName());
      if (this.currentType != -1) {
         Object[] var2 = (Object[])((Object[])var1.getOldValue());
         Object[] var3 = (Object[])((Object[])var1.getNewValue());
         ArrayUtils.computeDiff(var2, var3, this, this.domainObserver);
         this.currentType = -1;
      }
   }

   public JMSDistributedTopicMBean getJMSDistributedTopic() {
      return this.distributedTopic;
   }

   private int getType(String var1) {
      if (var1 == null) {
         return -1;
      } else {
         for(int var2 = 0; var2 < 1; ++var2) {
            String var3 = handledProperties[var2];
            if (var3.equals(var1)) {
               return var2;
            }
         }

         return -1;
      }
   }

   private void addMember(JMSDistributedTopicMemberMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSSystemResourceMBean var3 = JMSBeanHelper.addInteropApplication(var2);
      JMSBean var4 = var3.getJMSResource();
      DistributedTopicBean var5 = var4.lookupDistributedTopic(this.distributedTopic.getName());
      if (var5 != null) {
         DistributedDestinationMemberBean var6 = null;

         try {
            var6 = MBeanConverter.addDistributedTopicMember(var4, var5, var1);
         } catch (UpdateException var8) {
            this.domainObserver.logUpdateException(var1.getName(), var8);
            return;
         }

         var1.useDelegates(var2, var6);
      }
   }

   private void removeMember(JMSDistributedTopicMemberMBean var1) {
      DomainMBean var2 = this.domainObserver.getDomain();
      JMSInteropModuleMBean var3 = JMSBeanHelper.getJMSInteropModule(var2);
      if (var3 != null) {
         JMSBean var4 = var3.getJMSResource();
         DistributedTopicBean var5 = var4.lookupDistributedTopic(this.distributedTopic.getName());
         if (var5 != null) {
            DistributedDestinationMemberBean var6 = var5.lookupDistributedTopicMember(var1.getName());
            var5.destroyDistributedTopicMember(var6);
         }
      }
   }

   public void addObject(Object var1) {
      switch (this.currentType) {
         case 0:
            this.addMember((JMSDistributedTopicMemberMBean)var1);
            return;
         default:
            throw new AssertionError("ERROR: Unknown current type: " + this.currentType);
      }
   }

   public void removeObject(Object var1) {
      switch (this.currentType) {
         case 0:
            this.removeMember((JMSDistributedTopicMemberMBean)var1);
            return;
         default:
            throw new AssertionError("ERROR: Unknown current type: " + this.currentType);
      }
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof JMSDistributedTopicObserver) {
         JMSDistributedTopicObserver var2 = (JMSDistributedTopicObserver)var1;
         return this.distributedTopic == var2.distributedTopic;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.distributedTopic.hashCode();
   }
}
