package weblogic.jms.backend;

import javax.jms.JMSException;
import javax.naming.Context;
import weblogic.application.ModuleException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.ModuleName;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.management.ManagementException;
import weblogic.management.utils.GenericBeanListener;

public final class BETopicRuntimeDelegate extends BEDestinationRuntimeDelegate {
   private BETopicImpl managedTopic;
   private GenericBeanListener multicastListener;

   public BETopicRuntimeDelegate(EntityName var1, BackEnd var2, Context var3, boolean var4, ModuleName var5, JMSBean var6, TopicBean var7) {
      super(var1, var3, var2, var6, var7, var4, var5);
   }

   protected void initialize(int var1) throws ModuleException {
      try {
         this.managedTopic = new BETopicImpl((TopicBean)this.specificBean, this.backEnd, this.entityName.toString(), this.temporary, new BEDestinationSecurityImpl(this.entityName, "topic"));
         this.setManagedDestination(this.managedTopic);
         TopicBean var2 = (TopicBean)this.specificBean;
         DescriptorBean var3 = (DescriptorBean)var2.getMulticast();
         this.multicastListener = new GenericBeanListener(var3, this.managedTopic, JMSBeanHelper.multicastBeanSignatures, false);
         this.multicastListener.initialize();
         super.initialize(var1);
      } catch (JMSException var4) {
         throw new ModuleException(var4);
      } catch (ManagementException var5) {
         throw new ModuleException(var5);
      }
   }

   public void activate(JMSBean var1) throws ModuleException {
      BEDurableSubscriptionStore var2 = this.managedTopic.getBackEnd().getDurableSubscriptionStore();
      if (var2 != null) {
         try {
            var2.restoreSubscriptions(this.managedTopic);
         } catch (JMSException var5) {
            throw new ModuleException(var5.getMessage(), var5);
         }
      }

      super.activate(var1);
      if (var1 != null) {
         TopicBean var3 = var1.lookupTopic(this.getEntityName());
         DescriptorBean var4 = (DescriptorBean)var3.getMulticast();
         this.multicastListener.close();
         this.multicastListener = new GenericBeanListener(var4, this.managedTopic, JMSBeanHelper.multicastBeanSignatures);
      }

   }

   public void deactivate() throws ModuleException {
      this.multicastListener.close();
      super.deactivate();
   }
}
