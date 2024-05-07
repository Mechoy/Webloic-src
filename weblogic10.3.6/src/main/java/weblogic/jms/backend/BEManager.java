package weblogic.jms.backend;

import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import weblogic.jms.JMSService;
import weblogic.jms.common.DurableSubscription;
import weblogic.jms.common.InvalidDestinationException;
import weblogic.jms.common.JMSBrowserCreateResponse;
import weblogic.jms.common.JMSConnectionConsumerCreateResponse;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;

public final class BEManager implements Invocable {
   private static BEManager beManager;
   private final InvocableMonitor invocableMonitor;

   public BEManager(InvocableMonitor var1) {
      Class var2 = BEManager.class;
      synchronized(BEManager.class) {
         if (beManager == null) {
            beManager = this;
         }

         this.invocableMonitor = var1;
      }
   }

   public static BEConnection connectionFindOrCreate(JMSID var0, JMSDispatcher var1, boolean var2, long var3, String var5) {
      BEConnection var6;
      do {
         try {
            var6 = (BEConnection)InvocableManagerDelegate.delegate.invocableFind(15, var0);
            if (var6.getDispatcher() != var1 && var6.getDispatcher() != null) {
               var6.setDispatcher(var1);
            }

            if (var3 > var6.getStartStopSequenceNumber()) {
               if (var2) {
                  var6.stop(var3, false);
               } else {
                  var6.start(var3);
               }
            }
         } catch (JMSException var10) {
            var6 = new BEConnection(var1, var0, var2, var5);
            var6.setStartStopSequenceNumber(var3);

            try {
               InvocableManagerDelegate.delegate.invocableAdd(15, var6);
            } catch (JMSException var9) {
               var6 = null;
            }
         }
      } while(var6 == null);

      return var6;
   }

   private static int browserCreate(Request var0) throws JMSException {
      BEBrowserCreateRequest var1 = (BEBrowserCreateRequest)var0;
      BEDestinationImpl var2 = (BEDestinationImpl)InvocableManagerDelegate.delegate.invocableFind(20, var1.getDestinationId());
      var2.checkShutdownOrSuspendedNeedLock("create browser");
      BEBrowser var3 = var2.createBrowser((BESession)null, var1.getSelector());
      InvocableManagerDelegate.delegate.invocableAdd(18, var3);
      var1.setResult(new JMSBrowserCreateResponse(var3.getJMSID()));
      var1.setState(Integer.MAX_VALUE);
      return var1.getState();
   }

   private void sessionCreate(Request var1) throws JMSException {
      BESessionCreateRequest var2 = (BESessionCreateRequest)var1;
      JMSID var3 = var2.getSessionId();

      JMSDispatcher var4;
      try {
         var4 = JMSDispatcherManager.dispatcherFindOrCreate(var2.getFEDispatcherId());
      } catch (DispatcherException var9) {
         throw new weblogic.jms.common.JMSException(var9.getMessage(), var9);
      }

      BEConnection var5 = connectionFindOrCreate(var2.getConnectionId(), var4, var2.getIsStopped(), var2.getStartStopSequenceNumber(), var2.getConnectionAddress());
      var5.checkShutdownOrSuspendedNeedLock("create session");

      try {
         BESession var10 = (BESession)InvocableManagerDelegate.delegate.invocableFind(16, var3);
      } catch (JMSException var8) {
         BESessionImpl var6 = new BESessionImpl(var5, var3, var2.getSequencerId(), var2.getTransacted(), var2.getXASession(), var2.getAcknowledgeMode(), var2.getClientVersion(), var2.getPushWorkManager());
         var5.sessionAdd(var6);
      }

   }

   private static int connectionConsumerCreate(BEConnectionConsumerCreateRequest var0) throws JMSException {
      BackEnd var1 = JMSService.getJMSService().getBEDeployer().findBackEnd(var0.getBackEndId());
      var1.checkShutdownNeedLock("create connection consumer");
      BEDestinationImpl var3 = (BEDestinationImpl)InvocableManagerDelegate.delegate.invocableFind(20, var0.getDestinationId());
      JMSID var4 = JMSService.getJMSService().getNextId();
      boolean var5 = true;
      if (var0.getConnection() != null && var0.getConnection().isStopped()) {
         var5 = false;
      }

      BEConnectionConsumerImpl var2 = var3.createConnectionConsumer(var4, var0.getServerSessionPool(), (String)null, (String)null, var0.getMessageSelector(), false, var0.getMessagesMaximum(), -1L, var0.isDurable(), var5);
      BEConnection var6 = connectionFindOrCreate(var0.getConnectionId(), JMSDispatcherManager.getLocalDispatcher(), var0.isStopped(), var0.getStartStopSequenceNumber(), (String)null);
      if (var0.isDurable()) {
         DurableSubscription var7 = var1.getDurableSubscription(var2.getName());
         var7.addSubscriber(var2);
         var1.addDurableSubscription(var2.getName(), var7);
      }

      var6.connectionConsumerAdd(var2);
      var0.setResult(new JMSConnectionConsumerCreateResponse(var2));
      var0.setState(Integer.MAX_VALUE);
      return Integer.MAX_VALUE;
   }

   private static int removeSubscription(Request var0) throws JMSException {
      BERemoveSubscriptionRequest var1 = (BERemoveSubscriptionRequest)var0;
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("BEManager removes Subscription: " + var1.getName() + " client id = " + var1.getClientId());
      }

      BackEnd var2 = JMSService.getJMSService().getBEDeployer().findBackEnd(var1.getBackEndId());
      if (var2 == null) {
         throw new InvalidDestinationException("JMS Destination referenced by the request is not found");
      } else {
         var2.checkShutdownOrSuspendedNeedLock("remove subscription");
         String var3 = BEConsumerImpl.clientIdPlusName(var1.getClientId(), var1.getName(), var1.getClientIdPolicy(), var1.getDestinationName(), var2.getName());
         DurableSubscription var4 = var2.getDurableSubscription(var3);
         if (var4 == null) {
            throw new InvalidDestinationException("Subscription " + var3 + " not found");
         } else {
            var4.getConsumer().delete(false, true, false);
            var1.setResult(new VoidResponse());
            var1.setState(Integer.MAX_VALUE);
            return var1.getState();
         }
      }
   }

   private static int completeUpdateParentRequest(Request var0) {
      BEOrderUpdateParentRequest var1 = (BEOrderUpdateParentRequest)var0;

      try {
         var1.setResult(var1.getOrderUpdate().getResult());
         var1.setState(Integer.MAX_VALUE);
      } catch (Throwable var3) {
         var1.getCompletionRequest().setResult(var3);
         return Integer.MAX_VALUE;
      }

      var1.getCompletionRequest().setResult(Boolean.TRUE);
      return Integer.MAX_VALUE;
   }

   public static BEManager getBEManager() {
      return beManager;
   }

   public JMSID getJMSID() {
      return null;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 8450:
            return browserCreate(var1);
         case 9218:
            return connectionConsumerCreate((BEConnectionConsumerCreateRequest)var1);
         case 13570:
            this.sessionCreate(var1);
            var1.setResult(new VoidResponse());
            var1.setState(Integer.MAX_VALUE);
            return Integer.MAX_VALUE;
         case 14850:
            return removeSubscription(var1);
         case 18178:
            return completeUpdateParentRequest(var1);
         default:
            throw new weblogic.jms.common.JMSException("No such method " + var1.getMethodId());
      }
   }

   public static BEConnection[] getBEConnections() {
      HashMap var0 = InvocableManagerDelegate.delegate.getInvocableMap(15);
      synchronized(var0) {
         BEConnection[] var2 = new BEConnection[var0.size()];
         Iterator var3 = var0.values().iterator();

         for(int var4 = 0; var3.hasNext(); var2[var4++] = (BEConnection)var3.next()) {
         }

         return var2;
      }
   }
}
