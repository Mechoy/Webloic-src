package weblogic.jms.frontend;

import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import weblogic.jms.backend.BEBrowserCloseRequest;
import weblogic.jms.backend.BEBrowserGetEnumerationRequest;
import weblogic.jms.common.JMSBrowserGetEnumerationResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPushExceptionRequest;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;

public final class FEBrowser implements Invocable {
   private final JMSID browserId;
   private final JMSDispatcher dispatcher;
   private final FEConnection connection;
   private final FESession session;
   private final InvocableMonitor invocableMonitor;
   private final HashMap enumerations = new HashMap();

   public FEBrowser(FEConnection var1, FESession var2, JMSID var3, JMSDispatcher var4) {
      this.connection = var1;
      this.session = var2;
      this.browserId = var3;
      this.dispatcher = var4;
      this.invocableMonitor = var2.getInvocableMonitor();
   }

   public JMSID getJMSID() {
      return this.browserId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   FEConnection getConnection() {
      return this.connection;
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   private int close(FEBrowserCloseRequest var1) throws JMSException {
      JMSException var2 = null;
      switch (var1.getState()) {
         case 0:
            BEBrowserCloseRequest var3 = new BEBrowserCloseRequest(this.browserId);
            synchronized(var1) {
               var1.rememberChild(var3);
               var1.setState(1);
            }

            try {
               var1.dispatchAsync(this.dispatcher, var3);
            } catch (DispatcherException var9) {
               throw new weblogic.jms.common.JMSException("Error closing browser", var9);
            }

            return var1.getState();
         default:
            try {
               var1.useChildResult(VoidResponse.class);
            } catch (JMSException var13) {
               if (var2 == null) {
                  var2 = var13;
               }
            }

            synchronized(this) {
               Iterator var5 = ((HashMap)this.enumerations.clone()).values().iterator();

               while(true) {
                  if (!var5.hasNext()) {
                     break;
                  }

                  FEEnumeration var6 = (FEEnumeration)var5.next();

                  try {
                     this.enumerationRemove(var6.getJMSID());
                  } catch (JMSException var11) {
                     if (var2 == null) {
                        var2 = var11;
                     }
                  }
               }
            }

            if (this.session == null) {
               this.connection.browserRemove(this.browserId);
            } else {
               this.session.browserRemove(this.browserId);
            }

            if (var2 != null) {
               throw var2;
            } else {
               return Integer.MAX_VALUE;
            }
      }
   }

   private synchronized void enumerationAdd(FEEnumeration var1) throws JMSException {
      if (this.enumerations.put(var1.getJMSID(), var1) == null) {
         InvocableManagerDelegate.delegate.invocableAdd(12, var1);
      }

   }

   synchronized void enumerationRemove(JMSID var1) throws JMSException {
      if (this.enumerations.remove(var1) == null) {
         throw new weblogic.jms.common.JMSException("Enumeration not found, " + var1);
      } else {
         InvocableManagerDelegate.delegate.invocableRemove(12, var1);
      }
   }

   private int enumerate(Request var1) throws JMSException {
      FEBrowserGetEnumerationRequest var2 = (FEBrowserGetEnumerationRequest)var1;
      switch (var2.getState()) {
         case 0:
            BEBrowserGetEnumerationRequest var3 = new BEBrowserGetEnumerationRequest(this.browserId);
            synchronized(var2) {
               var2.rememberChild(var3);
               var2.setState(1);
            }

            try {
               var2.dispatchAsync(this.dispatcher, var3);
            } catch (DispatcherException var6) {
               throw new weblogic.jms.common.JMSException("Error getting enumeration", var6);
            }

            return var2.getState();
         case 1:
         default:
            JMSBrowserGetEnumerationResponse var4 = (JMSBrowserGetEnumerationResponse)var2.useChildResult(JMSBrowserGetEnumerationResponse.class);
            this.enumerationAdd(new FEEnumeration(this, var4.getEnumerationId(), this.dispatcher));
            return Integer.MAX_VALUE;
      }
   }

   private int pushException(Request var1) throws JMSException {
      JMSPushExceptionRequest var2 = (JMSPushExceptionRequest)var1;
      JMSPushExceptionRequest var3 = new JMSPushExceptionRequest(22, this.browserId, var2.getException());
      if (this.session != null) {
         JMSServerUtilities.anonDispatchNoReply(var3, this.session.getConnection().getClientDispatcher());
      } else if (this.connection != null) {
         JMSServerUtilities.anonDispatchNoReply(var3, this.connection.getClientDispatcher());
      }

      synchronized(this) {
         Iterator var5 = ((HashMap)this.enumerations.clone()).values().iterator();

         while(true) {
            if (!var5.hasNext()) {
               break;
            }

            FEEnumeration var6 = (FEEnumeration)var5.next();
            this.enumerationRemove(var6.getJMSID());
         }
      }

      if (this.session == null) {
         this.connection.browserRemove(this.browserId);
      } else {
         this.session.browserRemove(this.browserId);
      }

      var2.setState(Integer.MAX_VALUE);
      return var2.getState();
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 267:
            return this.close((FEBrowserCloseRequest)var1);
         case 779:
            return this.enumerate(var1);
         case 15371:
            return this.pushException(var1);
         default:
            throw new weblogic.jms.common.JMSException("No such method " + this.getClass().getName() + "." + var1.getMethodId());
      }
   }
}
