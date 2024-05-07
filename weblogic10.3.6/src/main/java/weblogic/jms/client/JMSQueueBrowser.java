package weblogic.jms.client;

import java.util.Enumeration;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import weblogic.jms.JMSClientExceptionLogger;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.IllegalStateException;
import weblogic.jms.common.JMSBrowserGetEnumerationResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPushExceptionRequest;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.frontend.FEBrowserGetEnumerationRequest;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.dispatcher.Response;

public final class JMSQueueBrowser implements QueueBrowser, Invocable {
   private final JMSDispatcher frontEndDispatcher;
   private JMSID browserId;
   private final Queue queue;
   private final String selector;
   private final JMSSession session;

   public JMSQueueBrowser(Queue var1, String var2, JMSSession var3) throws JMSException {
      this.queue = var1;
      this.selector = var2;
      this.session = var3;
      this.frontEndDispatcher = var3.getConnection().getFrontEndDispatcher();
      this.browserId = var3.createBackEndBrowser((DestinationImpl)var1, var2);
   }

   void setId(JMSID var1) {
      this.browserId = var1;
   }

   public JMSID getJMSID() {
      return this.browserId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return null;
   }

   public Queue getQueue() throws JMSException {
      this.checkClosed();
      return this.queue;
   }

   public String getMessageSelector() throws JMSException {
      this.checkClosed();
      return this.selector;
   }

   public Enumeration getEnumeration() throws JMSException {
      this.checkClosed();
      Response var1 = this.frontEndDispatcher.dispatchSync(new FEBrowserGetEnumerationRequest(this.browserId));
      return new JMSEnumeration(this.queue, this, ((JMSBrowserGetEnumerationResponse)var1).getEnumerationId(), this.frontEndDispatcher);
   }

   public JMSSession getSession() throws JMSException {
      this.checkClosed();
      return this.session;
   }

   boolean isClosed() {
      return this.browserId == null;
   }

   private void checkClosed() throws JMSException {
      if (this.isClosed()) {
         throw new IllegalStateException(JMSClientExceptionLogger.logClosedBrowserLoggable());
      }
   }

   public void close() throws JMSException {
      JMSID var1;
      synchronized(this) {
         if (this.isClosed()) {
            return;
         }

         var1 = this.browserId;
         this.browserId = null;
      }

      this.session.closeBrowser(var1, false);
   }

   private int pushException(Request var1) throws JMSException {
      JMSPushExceptionRequest var2 = (JMSPushExceptionRequest)var1;
      JMSID var3;
      synchronized(this) {
         var3 = this.browserId;
         this.browserId = null;
      }

      if (var3 != null) {
         this.session.closeBrowser(var3, true);
      }

      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 15382:
            return this.pushException(var1);
         default:
            throw new weblogic.jms.common.JMSException(JMSClientExceptionLogger.logNoSuchMethod3Loggable(var1.getMethodId(), this.getClass().getName()));
      }
   }
}
