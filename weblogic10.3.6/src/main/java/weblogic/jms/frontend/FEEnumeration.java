package weblogic.jms.frontend;

import javax.jms.JMSException;
import weblogic.jms.backend.BEEnumerationNextElementRequest;
import weblogic.jms.common.JMSEnumerationNextElementResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;

public final class FEEnumeration implements Invocable {
   private final FEBrowser browser;
   private final JMSID enumerationId;
   private final JMSDispatcher dispatcher;
   private final InvocableMonitor invocableMonitor;

   public FEEnumeration(FEBrowser var1, JMSID var2, JMSDispatcher var3) {
      this.browser = var1;
      this.enumerationId = var2;
      this.dispatcher = var3;
      this.invocableMonitor = var1.getInvocableMonitor();
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

   private int nextElement(Request var1) throws JMSException {
      FEEnumerationNextElementRequest var2 = (FEEnumerationNextElementRequest)var1;
      switch (var2.getState()) {
         case 0:
            BEEnumerationNextElementRequest var3 = new BEEnumerationNextElementRequest(this.enumerationId);
            synchronized(var2) {
               var2.rememberChild(var3);
               var2.setState(1);
            }

            try {
               var2.dispatchAsync(this.dispatcher, var3);
            } catch (DispatcherException var6) {
               throw new weblogic.jms.common.JMSException("Error getting next element", var6);
            }

            return var2.getState();
         case 1:
            JMSEnumerationNextElementResponse var4 = (JMSEnumerationNextElementResponse)var2.useChildResult(JMSEnumerationNextElementResponse.class);
            if (var4.getMessage() == null) {
               this.browser.enumerationRemove(this.enumerationId);
            }

            MessageImpl var5 = var4.getMessage();
            if (var5 != null) {
               var4.setCompressionThreshold(this.browser.getConnection().getCompressionThreshold());
            }

            var2.setResult(var4);
            return Integer.MAX_VALUE;
         default:
            return var2.getState();
      }
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 4108:
            return this.nextElement(var1);
         default:
            throw new weblogic.jms.common.JMSException("No such method " + this.getClass().getName() + "." + var1.getMethodId());
      }
   }
}
