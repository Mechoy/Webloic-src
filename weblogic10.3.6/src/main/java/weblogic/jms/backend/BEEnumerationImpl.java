package weblogic.jms.backend;

import javax.jms.JMSException;
import weblogic.jms.common.JMSEnumerationNextElementResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.Invocable;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.kernel.Cursor;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.MessageElement;
import weblogic.messaging.kernel.Queue;

final class BEEnumerationImpl implements Invocable {
   private boolean closed;
   private BEBrowserImpl browser;
   private JMSID enumerationId;
   private InvocableMonitor invocableMonitor;
   private Cursor cursor;

   BEEnumerationImpl(BEBrowserImpl var1, JMSID var2, Queue var3, Expression var4) throws JMSException {
      this.browser = var1;
      this.enumerationId = var2;
      this.invocableMonitor = var1.getInvocableMonitor();

      try {
         this.cursor = var3.createCursor(true, var4, 1);
      } catch (KernelException var6) {
         throw new weblogic.jms.common.JMSException(var6);
      }
   }

   void close() {
      synchronized(this) {
         if (this.closed) {
            return;
         }

         this.closed = true;
      }

      this.cursor.close();
      this.browser.enumerationRemove(this.enumerationId);
   }

   private int nextElement(BEEnumerationNextElementRequest var1) throws JMSException {
      this.browser.checkShutdownOrSuspended("next element");

      MessageElement var2;
      try {
         var2 = this.cursor.next();
      } catch (KernelException var4) {
         throw new weblogic.jms.common.JMSException(var4);
      }

      MessageImpl var3 = (MessageImpl)(var2 == null ? null : var2.getMessage());
      if (var3 != null) {
         if (var2.getDeliveryCount() > 0) {
            var3 = var3.cloneit();
            var3.setDeliveryCount(var2.getDeliveryCount());
         }
      } else {
         this.close();
      }

      var1.setResult(new JMSEnumerationNextElementResponse(var3));
      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   public JMSID getJMSID() {
      return this.enumerationId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public int invoke(Request var1) throws JMSException {
      if (var1.getMethodId() == 11795) {
         return this.nextElement((BEEnumerationNextElementRequest)var1);
      } else {
         throw new weblogic.jms.common.JMSException("No such method " + this.getClass().getName() + "." + var1.getMethodId());
      }
   }
}
