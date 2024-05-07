package weblogic.jms.client;

import javax.jms.JMSException;
import javax.jms.XAQueueSession;
import javax.jms.XASession;
import javax.jms.XATopicSession;

public final class XAConnectionInternalImpl extends WLConnectionImpl implements XAConnectionInternal {
   public XAConnectionInternalImpl(JMSXAConnectionFactory var1, JMSXAConnection var2) throws JMSException {
      super(var1, var2);
   }

   public XAQueueSession createXAQueueSession() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSConnection var3 = this.getPhysicalJMSConnection();

      try {
         return ((JMSXAConnection)var3).createXAQueueSession();
      } catch (weblogic.jms.common.JMSException var5) {
         return ((JMSXAConnection)this.computeJMSConnection(var1, var3, var5)).createXAQueueSession();
      }
   }

   public XATopicSession createXATopicSession() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSConnection var3 = this.getPhysicalJMSConnection();

      try {
         return ((JMSXAConnection)var3).createXATopicSession();
      } catch (weblogic.jms.common.JMSException var5) {
         return ((JMSXAConnection)this.computeJMSConnection(var1, var3, var5)).createXATopicSession();
      }
   }

   public XASession createXASession() throws JMSException {
      long var1 = System.currentTimeMillis();
      JMSConnection var3 = this.getPhysicalJMSConnection();

      try {
         return ((JMSXAConnection)var3).createXASession();
      } catch (weblogic.jms.common.JMSException var5) {
         return ((JMSXAConnection)this.computeJMSConnection(var1, var3, var5)).createXASession();
      }
   }
}
