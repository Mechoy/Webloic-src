package weblogic.jms.backend;

import java.util.HashMap;
import javax.jms.JMSException;
import weblogic.management.configuration.JMSConnectionConsumerMBean;
import weblogic.management.utils.GenericBeanListener;

public class BEConnectionConsumerRuntimeDelegate {
   private JMSConnectionConsumerMBean mbean;
   private BEConnectionConsumerCommon consumer;
   private static final HashMap connectionConsumerSignatures = new HashMap();
   private GenericBeanListener ccListener;

   BEConnectionConsumerRuntimeDelegate(BEConnectionConsumerCommon var1, JMSConnectionConsumerMBean var2) throws JMSException {
      this.mbean = var2;
      this.consumer = var1;
      var1.setName(var2.getName());
      this.ccListener = new GenericBeanListener(var2, var1, connectionConsumerSignatures);
   }

   void close() {
      if (this.ccListener != null) {
         this.ccListener.close();
      }

   }

   static {
      connectionConsumerSignatures.put("MessagesMaximum", Integer.TYPE);
   }
}
