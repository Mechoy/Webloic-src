package weblogic.jms.frontend;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEConsumerImpl;
import weblogic.jms.backend.BEDestinationCreateRequest;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.backend.BERemoveSubscriptionRequest;
import weblogic.jms.backend.BEServerSessionPool;
import weblogic.jms.backend.BackEnd;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.DurableSubscription;
import weblogic.jms.common.InvalidDestinationException;
import weblogic.jms.common.JMSDestinationCreateResponse;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSPushEntry;
import weblogic.jms.common.JMSPushRequest;
import weblogic.jms.common.JMSServerId;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.Sequencer;
import weblogic.jms.dd.DDManager;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.InvocableManagerDelegate;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.jms.dispatcher.JMSDispatcherManager;
import weblogic.jms.dispatcher.VoidResponse;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.JMSConnectionRuntimeMBean;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.DispatcherException;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class FEManager implements Invocable {
   private static FETempDestinationFactory feTemporaryDestinationFactory;
   private final InvocableMonitor invocableMonitor;

   public FEManager(InvocableMonitor var1) {
      this.invocableMonitor = var1;
   }

   public static JMSConnectionRuntimeMBean[] getConnections() {
      HashMap var0 = InvocableManagerDelegate.delegate.getInvocableMap(7);
      synchronized(var0) {
         JMSConnectionRuntimeMBean[] var2 = new JMSConnectionRuntimeMBean[var0.size()];
         Iterator var3 = var0.values().iterator();

         for(int var4 = 0; var3.hasNext(); var2[var4++] = ((FEConnection)var3.next()).getRuntimeDelegate()) {
         }

         return var2;
      }
   }

   public static long getConnectionsCurrentCount() {
      return (long)InvocableManagerDelegate.delegate.getInvocablesCurrentCount(7);
   }

   public static long getConnectionsHighCount() {
      return (long)InvocableManagerDelegate.delegate.getInvocablesHighCount(7);
   }

   public static long getConnectionsTotalCount() {
      return (long)InvocableManagerDelegate.delegate.getInvocablesTotalCount(7);
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

   static FETempDestinationFactory getTemporaryDestinationFactory() throws JMSException {
      Class var0 = FEManager.class;
      synchronized(FEManager.class) {
         if (feTemporaryDestinationFactory == null) {
            try {
               feTemporaryDestinationFactory = (FETempDestinationFactory)JMSService.getContext().lookup("weblogic.jms.TempDestinationFactory");
            } catch (NamingException var3) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logNoTemporaryTemplatesLoggable(), var3);
            }
         }
      }

      return feTemporaryDestinationFactory;
   }

   private void serverSessionPoolClose(FEServerSessionPoolCloseRequest var1) throws JMSException {
      JMSService var2 = JMSService.getJMSService();
      BackEnd var3 = var2.getBEDeployer().findBackEnd(var1.getBackEndId());
      var3.checkShutdownOrSuspended("close server session pool");
      BEServerSessionPool var4 = var3.serverSessionPoolFind(var1.getServerSessionPoolId());
      if (var4 != null) {
         var3.serverSessionPoolRemove(var4);
         var4.close();
      } else {
         throw new JMSException("Server session pool not found");
      }
   }

   private int destinationCreate(Request var1) throws JMSException {
      FEDestinationCreateRequest var2 = (FEDestinationCreateRequest)var1;
      JMSService var3 = JMSService.getJMSService();
      DistributedDestinationImpl var4 = null;
      switch (var2.getState()) {
         case 0:
            String var6 = var2.getDestinationName();
            if (var6 != null && var6.length() > 0) {
               var4 = DDManager.findDDImplByDDName(var6);
               if (var4 != null) {
                  this.checkAndProcessCreateDestination(var4, var2);
                  break;
               }

               int var5;
               if ((var5 = var6.indexOf(47)) != -1 && var5 != var6.length() - 1) {
                  String var7 = var6.startsWith("./") ? null : var6.substring(0, var5);
                  String var8 = "weblogic.jms.backend." + var7;
                  FrontEnd var9 = var3.getFEDeployer().getFrontEnd();
                  var6 = var6.substring(var5 + 1, var6.length());
                  if (var7 == null) {
                     var9.checkShutdownOrSuspended();
                     BackEnd[] var10 = var3.getBEDeployer().getBackEnds();

                     for(int var11 = 0; var10 != null && var11 < var10.length; ++var11) {
                        assert !var10[var11].getName().startsWith("!weblogicreserved!");

                        try {
                           var10[var11].checkShutdownOrSuspendedNeedLock("create destination");
                        } catch (JMSException var18) {
                           continue;
                        }

                        BEDestinationImpl var12 = var10[var11].findDestinationByCreateName(var6);
                        if (var12 != null && var12.getDestinationImpl().getType() == var2.getDestType() && var12.isStarted() && var12.isAvailableForCreateDestination()) {
                           var2.setResult(new JMSDestinationCreateResponse(var12.getDestinationImpl()));
                           var2.setState(Integer.MAX_VALUE);
                           break;
                        }
                     }

                     if (var2.getState() != Integer.MAX_VALUE) {
                        AuthenticatedSubject var21 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
                        throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logDestinationNotFoundLoggable(var6, ManagementService.getRuntimeAccess(var21).getServerName()));
                     }
                     break;
                  } else {
                     Object var22;
                     try {
                        var22 = JMSService.getContext().lookup(var8);
                     } catch (NamingException var17) {
                        throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logBackEndUnreachableLoggable(), var17);
                     }

                     if (!(var22 instanceof JMSServerId)) {
                        throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logBackEndUnknownLoggable());
                     }

                     JMSServerId var20 = (JMSServerId)var22;
                     var9.checkShutdownOrSuspended();
                     BEDestinationCreateRequest var23 = new BEDestinationCreateRequest(var20.getId(), var6, var2.getDestType(), true);
                     var2.setBackEndId(var20);
                     var2.setDestinationName(var6);
                     synchronized(var2) {
                        var2.rememberChild(var23);
                        var2.setState(1);
                     }

                     try {
                        JMSDispatcher var13 = JMSDispatcherManager.dispatcherFindOrCreate(var20.getDispatcherId());
                        var2.dispatchAsync(var13, var23);
                        break;
                     } catch (DispatcherException var15) {
                        throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logFindFailedLoggable(), var15);
                     }
                  }
               }

               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logInvalidDestinationFormatLoggable(var6));
            }

            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logNoDestinationNameLoggable());
         case 1:
         default:
            JMSDestinationCreateResponse var24;
            if (var2.getChild() != null) {
               var24 = (JMSDestinationCreateResponse)var2.useChildResult(JMSDestinationCreateResponse.class);
            } else {
               var24 = (JMSDestinationCreateResponse)var2.getResult();
            }

            var2.setState(Integer.MAX_VALUE);
            DestinationImpl var19 = var24.getDestination();
      }

      return var2.getState();
   }

   private void checkAndProcessCreateDestination(DestinationImpl var1, FEDestinationCreateRequest var2) throws weblogic.jms.common.JMSException {
      if (var1.getType() != var2.getDestType()) {
         throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logInvalidDestinationTypeLoggable(var2.getDestinationName(), var2.getDestType() == 2 ? "topic" : "queue"));
      } else {
         var2.setResult(new JMSDestinationCreateResponse(var1));
         var2.setState(Integer.MAX_VALUE);
      }
   }

   private int pushMessage(Request var1) {
      JMSPushRequest var2 = (JMSPushRequest)var1;
      MessageImpl var3 = var2.getMessage();
      JMSPushEntry var4 = null;
      JMSPushEntry var5 = var2.getFirstPushEntry();
      JMSID var6 = var5.getSequencerId();

      do {
         if (!var6.equals(var5.getSequencerId())) {
            try {
               var4.setNext((JMSPushEntry)null);
               var2.setLastPushEntry(var4);
               ((Sequencer)InvocableManagerDelegate.delegate.invocableFind(13, var6)).pushMessage(var2);
            } catch (JMSException var9) {
            }

            var2 = new JMSPushRequest(0, (JMSID)null, var3);
            var2.setFirstPushEntry(var5);
            var6 = var5.getSequencerId();
         }

         var4 = var5;
      } while((var5 = var5.getNext()) != null);

      try {
         var4.setNext((JMSPushEntry)null);
         var2.setLastPushEntry(var4);
         ((Sequencer)InvocableManagerDelegate.delegate.invocableFind(13, var6)).pushMessage(var2);
      } catch (JMSException var8) {
      }

      return Integer.MAX_VALUE;
   }

   private int removeSubscription(Request var1) throws JMSException {
      FERemoveSubscriptionRequest var2 = (FERemoveSubscriptionRequest)var1;
      switch (var2.getState()) {
         case 0:
            JMSServerId var3 = var2.getBackEndId();
            if (var3 == null) {
               if (var2.getClientIdPolicy() == 1) {
                  throw new InvalidDestinationException(JMSExceptionLogger.logInvalidUnrestrictedUnsubscribeLoggable(var2.getName(), var2.getClientId()));
               }

               try {
                  Context var4 = JMSService.getContext();
                  String var5 = BEConsumerImpl.JNDINameForSubscription(BEConsumerImpl.clientIdPlusName(var2.getClientId(), var2.getName()));
                  var3 = ((DurableSubscription)var4.lookup(var5)).getBackEndId();
               } catch (NameNotFoundException var9) {
                  throw new InvalidDestinationException("Subscription " + var2.getName() + " not found.");
               } catch (NamingException var10) {
                  throw new InvalidDestinationException("Subscription " + var2.getName() + " not found", var10);
               }
            }

            BERemoveSubscriptionRequest var11 = new BERemoveSubscriptionRequest(var3, var2.getDestinationName(), var2.getClientId(), var2.getClientIdPolicy(), var2.getName());
            synchronized(var2) {
               var2.rememberChild(var11);
               var2.setState(1);
            }

            try {
               JMSDispatcher var12 = JMSDispatcherManager.dispatcherFindOrCreate(var3.getDispatcherId());
               var2.dispatchAsync(var12, var11);
            } catch (DispatcherException var7) {
               throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logErrorRemovingSubscriptionLoggable(), var7);
            }

            return var2.getState();
         case 1:
         default:
            var2.useChildResult(VoidResponse.class);
            return Integer.MAX_VALUE;
      }
   }

   public static FEConnection[] getFEConnections() {
      HashMap var0 = InvocableManagerDelegate.delegate.getInvocableMap(7);
      synchronized(var0) {
         FEConnection[] var2 = new FEConnection[var0.size()];
         return (FEConnection[])((FEConnection[])var0.values().toArray(var2));
      }
   }

   public int invoke(Request var1) throws JMSException {
      switch (var1.getMethodId()) {
         case 3841:
            return this.destinationCreate(var1);
         case 5377:
            FERemoveSubscriptionRequest var2 = (FERemoveSubscriptionRequest)var1;
            DestinationImpl var3 = var2.getDestination();
            if (var3 != null) {
               JMSServerId var4 = refreshBackEndId(var3.getServerName());
               var3.setBackEndID(var4);
               var2.setBackEndId(var4);
            }

            return this.removeSubscription(var2);
         case 5633:
            this.serverSessionPoolClose((FEServerSessionPoolCloseRequest)var1);
            var1.setResult(new VoidResponse());
            var1.setState(Integer.MAX_VALUE);
            return Integer.MAX_VALUE;
         case 15617:
            return this.pushMessage(var1);
         default:
            throw new weblogic.jms.common.JMSException(JMSExceptionLogger.logNoMethodLoggable(var1.getMethodId()));
      }
   }

   static JMSServerId refreshBackEndId(String var0) throws JMSException {
      JMSServerId var1 = null;
      String var2 = "weblogic.jms.backend." + var0;

      try {
         JMSService.getService();
         Object var3 = JMSService.getContext().lookup(var2);

         try {
            var1 = (JMSServerId)var3;
            return var1;
         } catch (ClassCastException var7) {
            String var5 = debugClassCastException(var2, var0, var3);
            if (var7.getCause() == null) {
               var7.initCause(new ClassCastException(var5));
               throw var7;
            } else {
               ClassCastException var6 = new ClassCastException(var5);
               var6.initCause(var7);
               throw var6;
            }
         }
      } catch (NamingException var8) {
         throw new weblogic.jms.common.JMSException(var8);
      } catch (ManagementException var9) {
         throw new weblogic.jms.common.JMSException(var9);
      }
   }

   private static String debugClassCastException(String var0, String var1, Object var2) {
      String var3 = "destination for jndi " + var0 + " has backend " + var1 + " found object <";
      if (var2 == null) {
         var3 = var3 + var2 + ">";
      } else {
         var3 = var3 + var2.getClass().getName() + " " + var2 + ">";
      }

      return var3;
   }

   static boolean isStaleDestEx(JMSException var0) {
      Object var1 = null;

      for(var1 = var0; var1 != null; var1 = ((Throwable)var1).getCause()) {
         if (var1 instanceof InvalidDestinationException) {
            return true;
         }
      }

      return false;
   }
}
