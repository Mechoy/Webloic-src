package weblogic.wsee.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.webservice.WebServiceLogger;
import weblogic.wsee.buffer.BufferManager;
import weblogic.wsee.cluster.ClusterDispatcher;
import weblogic.wsee.cluster.ForwardingHandler;
import weblogic.wsee.conversation.ConversationHandshakeHandler;
import weblogic.wsee.conversation.ConversationMsgClusterService;
import weblogic.wsee.handler.HandlerException;
import weblogic.wsee.handler.HandlerList;
import weblogic.wsee.reliability.ReliableConversationMsgClusterService;
import weblogic.wsee.reliability.WsrmEndpointManager;
import weblogic.wsee.reliability.WsrmSAFEndpoint;
import weblogic.wsee.reliability.WsrmServerHandler;
import weblogic.wsee.reliability.handshake.WsrmServerHandshakeHandler;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.server.jms.WsDispatchMessageListener;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsService;
import weblogic.wsee.ws.dispatch.server.SoapFaultHandler;
import weblogic.wsee.ws.init.WsDeploymentContext;
import weblogic.wsee.ws.init.WsDeploymentException;
import weblogic.wsee.ws.init.WsDeploymentListener;

public class AsyncResponseServiceDeploymentListener implements WsDeploymentListener {
   private static final boolean verbose = Verbose.isVerbose(AsyncResponseServiceDeploymentListener.class);

   public void process(WsDeploymentContext var1) throws WsDeploymentException {
      if (verbose) {
         Verbose.say("AsyncResponseServiceDeploymentListener.process");
      }

      WsService var2 = var1.getWsService();
      Iterator var3 = var2.getPorts();

      while(var3.hasNext()) {
         WsPort var4 = (WsPort)var3.next();
         HandlerList var5 = var4.getInternalHandlerList();
         HandlerInfo var6 = new HandlerInfo(AsyncResponseHandler.class, new HashMap(), (QName[])null);
         HandlerInfo var7 = new HandlerInfo(ForwardingHandler.class, new HashMap(), (QName[])null);
         HandlerInfo var8 = new HandlerInfo(ConversationHandshakeHandler.class, new HashMap(), (QName[])null);
         HandlerInfo var9 = new HandlerInfo(WsrmServerHandshakeHandler.class, new HashMap(), (QName[])null);
         HandlerInfo var10 = new HandlerInfo(WsrmServerHandler.class, new HashMap(), (QName[])null);
         HandlerInfo var11 = new HandlerInfo(AsyncResponseWsrmWsscHandler.class, new HashMap(), (QName[])null);
         ArrayList var12 = new ArrayList();
         ArrayList var13 = new ArrayList();
         var13.add("ADDRESSING_HANDLER");
         var13.add("SECURITY_HANDLER");
         var12.add("OPERATION_LOOKUP_HANDLER");
         ArrayList var14 = new ArrayList();
         ArrayList var15 = new ArrayList();
         var15.add("CONNECTION_HANDLER");
         var14.add("ADDRESSING_HANDLER");
         var14.add("SECURITY_HANDLER");

         try {
            int var16 = var5.lenientInsert("WSRM_HANDSHAKE_HANDLER", var9, var13, var12);
            var5.insert("RELIABILITY_HANDLER", var16 + 1, var10);
            var5.insert("CONVERSATION_HANDSHAKE_HANDLER", var16 + 2, var8);
            var5.insert("ASYNC_RESPONSE_HANDLER", var16 + 3, var6);
            var5.remove("CODEC_HANDLER");
            var16 = var5.lenientInsert("FORWARDING_HANDLER", var7, var15, var14);
            var5.insert("SOAP_FAULT_HANDLER", var16 + 1, new HandlerInfo(SoapFaultHandler.class, new HashMap(), (QName[])null));
            var5.insert("ASYNC_RESPONSE_WSRM_WSSC_HANDLER", var16 + 2, var11);
         } catch (HandlerException var17) {
            throw new WsDeploymentException("Failed to register handler", var17);
         }

         this.setupReliability(var4, var1);
         this.setupClusterService();
      }

   }

   private void setupClusterService() {
      if (verbose) {
         Verbose.say("AsyncResponseServiceDeploymentListener.setupClusterService starting");
      }

      ClusterDispatcher var1 = ClusterDispatcher.getInstance();
      var1.registerClusterService(new ConversationMsgClusterService());
      var1.registerClusterService(new ReliableConversationMsgClusterService());
      if (verbose) {
         Verbose.say("AsyncResponseServiceDeploymentListener.setupClusterService done");
      }

   }

   private void setupReliability(WsPort var1, WsDeploymentContext var2) throws WsDeploymentException {
      if (verbose) {
         Verbose.say("AsyncResponseServiceDeploymentListener.setupReliability: " + var1.getWsdlPort().getName());
      }

      WsrmEndpointManager var3 = (WsrmEndpointManager)SAFManagerImpl.getManager().getEndpointManager(2);

      assert var3 != null;

      if (var3 == null) {
         if (verbose) {
            Verbose.log((Object)"SAFManager not initialized. Async response service cannot initialize");
         }

      } else {
         StringBuffer var4 = new StringBuffer();
         WsDispatchMessageListener var5 = new WsDispatchMessageListener(var1);
         ServerUtil.QueueInfo var6 = ServerUtil.getMessagingQueueInfo();
         boolean var7 = true;
         String var8 = null;

         for(int var9 = 0; var9 < var2.getServiceURIs().length; ++var9) {
            String var10 = var2.getContextPath();
            String var11 = var2.getServiceURIs()[var9];
            var8 = var11;
            String var12 = AsyncUtil.calculateServiceTargetURI(var10, var11);
            BufferManager var13 = BufferManager.instance();
            synchronized(var13) {
               if (var13.getMessageListener(var12) != null) {
                  var7 = false;
                  continue;
               }

               var13.addMessageListener(var12, var5);
            }

            var13.setTargetQueue(var12, var6);
            var2.addBufferTargetURI(var12);
            WsrmSAFEndpoint var14 = new WsrmSAFEndpoint(var12);
            if (verbose) {
               Verbose.getOut().println("*********************************");
               Verbose.log((Object)("Adding endpoint at " + var12));
               Verbose.getOut().println("*********************************");
            }

            var3.addEndpoint(var12, var14);
            if (var4.length() == 0) {
               var4.append(AsyncUtil.getAsyncSelector(var12));
            } else {
               var4.append(" OR (" + AsyncUtil.getAsyncSelector(var12) + ")");
            }
         }

         if (var7) {
            if (verbose) {
               Verbose.log((Object)("Set up dynamic MDB to queue: " + var6.getQueueName()));
            }

            boolean var17 = checkQueueExists(var6.getQueueName());
            SetupDynamicMDBArgs var18 = new SetupDynamicMDBArgs(var8, var2, var4.toString(), var6.getQueueName(), var6.getMdbRunAsPrincipalName(), "weblogic.wsee.server.jms.MdbWS", 180);
            if (var17) {
               if (verbose) {
                  Verbose.log((Object)("Found queue, continue set up dynamic MDB to queue: " + var6.getQueueName()));
               }

               AsyncUtil.setupDynamicMDB(var18.context, var18.messageSelector, var18.queueName, var18.runAsPrincipalName, var18.beanName, var18.txnTimeout);
            } else {
               if (verbose) {
                  Verbose.log((Object)("Didn't find queue, defer MDB deployment waiting for queue: " + var6.getQueueName()));
               }

               var18.logMessage = true;
               scheduleSetupDynamicMDBRetry(var18);
            }
         }

      }
   }

   private static boolean checkQueueExists(String var0) {
      boolean var1;
      if (System.getProperty("weblogic.wsee.forceDeploy.async.response") != null) {
         var1 = true;
      } else {
         try {
            InitialContext var2 = new InitialContext();
            var2.lookup(var0);
            var1 = true;
         } catch (Exception var3) {
            var1 = false;
         }
      }

      return var1;
   }

   private static void scheduleSetupDynamicMDBRetry(SetupDynamicMDBArgs var0) {
      if (var0.logMessage) {
         WebServiceLogger.logAsyncResponseServiceNotDeployed(var0.serviceUri, var0.queueName);
      }

      long var1 = 60000L;
      TimerManager var3 = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager();
      MyTimerListener var4 = new MyTimerListener(var0);
      Timer var5 = var3.schedule(var4, var1);
      var4.setTimer(var5);
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }

   private static class MyTimerListener implements TimerListener {
      private SetupDynamicMDBArgs _args;
      private Timer _timer;

      public MyTimerListener(SetupDynamicMDBArgs var1) {
         this._args = var1;
      }

      public void setTimer(Timer var1) {
         this._timer = var1;
      }

      public void timerExpired(Timer var1) {
         if (AsyncResponseServiceDeploymentListener.verbose) {
            Verbose.log((Object)("Dynamic MDB timer popped. Looking for dynamic MDB queue: " + this._args.queueName));
         }

         try {
            boolean var2 = AsyncResponseServiceDeploymentListener.checkQueueExists(this._args.queueName);
            this._timer.cancel();
            if (var2) {
               if (AsyncResponseServiceDeploymentListener.verbose) {
                  Verbose.log((Object)("Found queue, continue set up dynamic MDB to queue: " + this._args.queueName));
               }

               WebServiceLogger.logAsyncResponseServiceDeployed(this._args.serviceUri, this._args.queueName);
               AsyncUtil.setupDynamicMDB(this._args.context, this._args.messageSelector, this._args.queueName, this._args.runAsPrincipalName, this._args.beanName, this._args.txnTimeout);
            } else {
               this._args.logMessage = false;
               AsyncResponseServiceDeploymentListener.scheduleSetupDynamicMDBRetry(this._args);
            }
         } catch (Exception var3) {
         }

      }
   }

   private static class SetupDynamicMDBArgs {
      String serviceUri;
      WsDeploymentContext context;
      String messageSelector;
      String queueName;
      String runAsPrincipalName;
      String beanName;
      int txnTimeout;
      boolean logMessage;

      public SetupDynamicMDBArgs(String var1, WsDeploymentContext var2, String var3, String var4, String var5, String var6, int var7) {
         this.serviceUri = var1;
         this.context = var2;
         this.messageSelector = var3;
         this.queueName = var4;
         this.runAsPrincipalName = var5;
         this.beanName = var6;
         this.txnTimeout = var7;
      }
   }
}
