package weblogic.management.mbeans.custom;

import weblogic.management.configuration.JMSConnectionConsumerMBean;
import weblogic.management.configuration.JMSSessionPoolMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class JMSSessionPool extends ConfigurationMBeanCustomizer {
   public JMSSessionPool(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public JMSConnectionConsumerMBean[] getConnectionConsumers() {
      JMSSessionPoolMBean var1 = (JMSSessionPoolMBean)this.getMbean();
      return var1.getJMSConnectionConsumers();
   }

   public JMSConnectionConsumerMBean createJMSConnectionConsumer(String var1, JMSConnectionConsumerMBean var2) {
      return (JMSConnectionConsumerMBean)this.getMbean().createChildCopyIncludingObsolete("JMSConnectionConsumer", var2);
   }

   public void destroyJMSConnectionConsumer(String var1, JMSConnectionConsumerMBean var2) {
      ((JMSSessionPoolMBean)this.getMbean()).destroyJMSConnectionConsumer(var2);
   }
}
