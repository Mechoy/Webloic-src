package weblogic.jms.extensions;

import javax.jms.JMSException;
import javax.jms.Message;
import org.w3c.dom.Document;

public interface WLMessage extends Message, WLAcknowledgeInfo {
   /** @deprecated */
   long getJMSDeliveryTime() throws JMSException;

   /** @deprecated */
   void setJMSDeliveryTime(long var1) throws JMSException;

   /** @deprecated */
   int getJMSRedeliveryLimit() throws JMSException;

   /** @deprecated */
   void setJMSRedeliveryLimit(int var1) throws JMSException;

   Document getJMSMessageDocument() throws JMSException;

   boolean getDDForwarded();

   String getUnitOfOrder();

   void setSAFSequenceName(String var1);

   String getSAFSequenceName();

   void setSAFSeqNumber(long var1);

   long getSAFSeqNumber();
}
