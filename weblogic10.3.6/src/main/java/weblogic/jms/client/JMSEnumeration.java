package weblogic.jms.client;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import javax.jms.JMSException;
import javax.jms.Queue;
import weblogic.jms.common.JMSEnumerationNextElementResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.frontend.FEEnumerationNextElementRequest;
import weblogic.messaging.dispatcher.Response;

final class JMSEnumeration implements Enumeration {
   private final Queue queue;
   private final JMSQueueBrowser browser;
   private JMSID enumerationId;
   private final JMSDispatcher frontEndDispatcher;
   private MessageImpl message;

   public JMSEnumeration(Queue var1, JMSQueueBrowser var2, JMSID var3, JMSDispatcher var4) throws JMSException {
      this.queue = var1;
      this.browser = var2;
      this.enumerationId = var3;
      this.frontEndDispatcher = var4;
   }

   public boolean hasMoreElements() {
      if (this.message != null) {
         return true;
      } else {
         if (!this.isClosed()) {
            this.message = this.getNextMessage();
         }

         return this.message != null;
      }
   }

   public Object nextElement() {
      if (this.message == null) {
         if (!this.isClosed()) {
            this.message = this.getNextMessage();
         }

         if (this.message == null) {
            throw new NoSuchElementException();
         }
      }

      MessageImpl var1 = this.message;
      this.message = null;

      try {
         var1.setJMSDestination(this.queue);
         if (this.isClosed() || this.browser.getSession().getConnection().isLocal()) {
            boolean var2 = var1.isOldMessage();
            var1 = var1.copy();
            var1.setOldMessage(var2);
         }

         var1.includeJMSXDeliveryCount(true);
         return var1;
      } catch (JMSException var3) {
         throw new AssertionError();
      }
   }

   private boolean isClosed() {
      if (this.enumerationId == null) {
         return true;
      } else {
         if (this.browser.isClosed()) {
            this.close();
         }

         return this.enumerationId == null;
      }
   }

   private void close() {
      this.enumerationId = null;
   }

   private MessageImpl getNextMessage() {
      MessageImpl var1 = null;

      try {
         Response var2 = this.frontEndDispatcher.dispatchSync(new FEEnumerationNextElementRequest(this.enumerationId));
         var1 = ((JMSEnumerationNextElementResponse)var2).getMessage();
      } catch (JMSException var3) {
         return null;
      }

      if (var1 == null) {
         this.close();
      }

      return var1;
   }
}
