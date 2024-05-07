package weblogic.jms.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSBrowserGetEnumerationResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPushExceptionRequest;
import weblogic.jms.common.JMSSQLExpression;
import weblogic.jms.common.JMSSecurityException;
import weblogic.jms.common.JMSSecurityHelper;
import weblogic.jms.common.JMSServerUtilities;
import weblogic.jms.common.TimedSecurityParticipant;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.jms.extensions.ConsumerClosedException;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.messaging.kernel.Expression;
import weblogic.messaging.kernel.InvalidExpressionException;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.Queue;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.work.WorkManagerFactory;

final class BEBrowserImpl implements BEBrowser, TimedSecurityParticipant {
   private final JMSID browserId;
   private final BESession session;
   private BEQueueImpl destination;
   private Queue queue;
   private Expression expression;
   private InvocableMonitor invocableMonitor;
   private boolean isClosed;
   private AuthenticatedSubject authenticatedSubject;
   private static final boolean debug = false;
   private final Map<JMSID, BEEnumerationImpl> enumerations = new HashMap();

   BEBrowserImpl(BESession var1, BEQueueImpl var2, Queue var3, String var4) throws JMSException {
      var2.getJMSDestinationSecurity().checkBrowsePermission();
      this.session = var1;
      this.browserId = JMSService.getJMSService().getNextId();
      this.destination = var2;
      this.queue = var3;
      this.authenticatedSubject = JMSSecurityHelper.getCurrentSubject();
      if (var1 == null) {
         this.invocableMonitor = var2.getBackEnd().getInvocableMonitor();
      } else {
         this.invocableMonitor = var1.getInvocableMonitor();
      }

      try {
         JMSSQLExpression var5 = new JMSSQLExpression(var4);
         this.expression = var3.getFilter().createExpression(var5);
      } catch (InvalidExpressionException var6) {
         throw new InvalidSelectorException(var6.toString());
      } catch (KernelException var7) {
         throw new weblogic.jms.common.JMSException(var7);
      }
   }

   public JMSID getJMSID() {
      return this.browserId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   BESession getSession() {
      return this.session;
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public synchronized boolean isClosed() {
      return this.isClosed;
   }

   private int close(Request var1) {
      this.close();
      var1.setResult(VoidResponse.THE_ONE);
      var1.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   public void close() {
      ArrayList var1;
      synchronized(this) {
         if (this.isClosed) {
            return;
         }

         this.isClosed = true;
         var1 = new ArrayList(this.enumerations.values());
         this.enumerations.clear();
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ((BEEnumerationImpl)var2.next()).close();
      }

      if (this.session == null) {
         InvocableManagerDelegate.delegate.invocableRemove(18, this.browserId);
      } else {
         this.session.browserRemove(this.browserId);
      }

      this.destination.removeBrowser(this.getJMSID());
   }

   private int enumerate(BEBrowserGetEnumerationRequest var1) throws JMSException {
      this.checkShutdownOrSuspended("enumerate browser");
      this.checkPermission();
      JMSID var2 = JMSService.getJMSService().getNextId();
      BEEnumerationImpl var3 = new BEEnumerationImpl(this, var2, this.queue, this.expression);
      this.enumerationAdd(var3);
      var1.setState(Integer.MAX_VALUE);
      var1.setResult(new JMSBrowserGetEnumerationResponse(var2));
      return Integer.MAX_VALUE;
   }

   private synchronized void enumerationAdd(BEEnumerationImpl var1) throws JMSException {
      InvocableManagerDelegate.delegate.invocableAdd(19, var1);
      this.enumerations.put(var1.getJMSID(), var1);
   }

   synchronized void enumerationRemove(JMSID var1) {
      this.enumerations.remove(var1);
      InvocableManagerDelegate.delegate.invocableRemove(19, var1);
   }

   void checkShutdownOrSuspended(String var1) throws JMSException {
      if (this.isClosed()) {
         throw new weblogic.jms.common.JMSException("The browser is closed");
      } else {
         this.destination.checkShutdownOrSuspendedNeedLock(var1);
      }
   }

   public void securityLapsed() {
      BESession var1 = this.getSession();
      this.close();
      if (var1 != null) {
         BEConnection var2 = var1.getConnection();
         if (var2 != null) {
            try {
               JMSServerUtilities.anonDispatchNoReply(new JMSPushExceptionRequest(11, this.browserId, new ConsumerClosedException((MessageConsumer)null, "ERROR: Security has lapsed for this consumer")), var2.getDispatcher());
            } catch (JMSException var4) {
               System.out.println("ERROR: Could not push security exception to queue browser: " + var4);
            }
         }
      }

   }

   public HashSet<?> getAcceptedDestinations() {
      return null;
   }

   private void checkPermission() throws JMSSecurityException {
      AuthenticatedSubject var1 = JMSSecurityHelper.getCurrentSubject();
      if (!JMSService.getJMSService().isSecurityCheckerStop()) {
         if (this.authenticatedSubject != var1 && (this.authenticatedSubject == null || !this.authenticatedSubject.equals(var1))) {
            this.destination.getJMSDestinationSecurity().checkBrowsePermission(var1);
            this.setSubject(var1);
         }
      } else {
         try {
            this.destination.getJMSDestinationSecurity().checkBrowsePermission(this.authenticatedSubject);
         } catch (JMSSecurityException var3) {
            WorkManagerFactory.getInstance().getSystem().schedule(new BrowserCloseThread());
            throw var3;
         }
      }

   }

   private void checkPermission(boolean var1) throws JMSSecurityException {
      this.checkPermission();
      if (var1) {
         WorkManagerFactory.getInstance().getSystem().schedule(new BrowserCloseThread());
         throw new JMSSecurityException("security check simulation negative result");
      }
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 8210:
            return this.close(var1);
         case 8722:
            return this.enumerate((BEBrowserGetEnumerationRequest)var1);
         default:
            throw new weblogic.jms.common.JMSException("No such method " + this.getClass().getName() + "." + var1.getMethodId());
      }
   }

   private synchronized void setSubject(AuthenticatedSubject var1) {
      if (var1 != null) {
         this.authenticatedSubject = var1;
      }

   }

   public synchronized AuthenticatedSubject getSubject() {
      return this.authenticatedSubject;
   }

   private class BrowserCloseThread implements Runnable {
      private BrowserCloseThread() {
      }

      public void run() {
         BEBrowserImpl.this.securityLapsed();
      }

      // $FF: synthetic method
      BrowserCloseThread(Object var2) {
         this();
      }
   }
}
