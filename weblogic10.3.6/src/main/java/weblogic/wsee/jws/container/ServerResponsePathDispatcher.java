package weblogic.wsee.jws.container;

import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.Handler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.addressing.AddressingHelper;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.async.AsyncInvokeState;
import weblogic.wsee.async.AsyncInvokeStateObjectHandler;
import weblogic.wsee.async.AsyncPostCallContextImpl;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.cluster.ClusterUtil;
import weblogic.wsee.conversation.LockManager;
import weblogic.wsee.handler.HandlerIterator;
import weblogic.wsee.handler.HandlerListImpl;
import weblogic.wsee.jws.JwsContext;
import weblogic.wsee.jws.conversation.Store;
import weblogic.wsee.jws.conversation.StoreConfig;
import weblogic.wsee.jws.conversation.StoreManager;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.reliability.WsrmSAFManagerFactory;
import weblogic.wsee.reliability.WsrmSAFReceivingManager;
import weblogic.wsee.reliability.WsrmSequenceContext;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsException;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsPort;
import weblogic.wsee.ws.WsReturnType;
import weblogic.wsee.ws.dispatch.client.ClientDispatcher;
import weblogic.wsee.ws.dispatch.server.JaxrpcChainHandler;
import weblogic.wsee.ws.dispatch.server.ServerDispatcher;

public class ServerResponsePathDispatcher {
   public static final String ASYNC_TX_INVOKE_PROP_NAME = "weblogic.jws.AsyncTransactionalInvoke";
   public static final String ASYNC_TX_INVOKE_IN_PROGRESS_PROP_NAME = "weblogic.jws.AsyncTransactionalInvokeInProgress";
   public static final String ASYNC_TX_INVOKE_SAVED_REQUEST_STATE_KEY_PROP_NAME = "weblogic.jws.AsyncTransactionalInvokeSavedRequestStateKey";
   public static final String PERSISTENT = "persistent";
   public static final String NON_PERSISTENT = "non-persistent";
   private static final boolean verbose = Verbose.isVerbose(ServerResponsePathDispatcher.class);
   private static final String SAVED_REQUEST_STATE_KEY_SUFFIX = "SavedRequestStateKey";

   public static WlMessageContext getWlMessageContext(JwsContext var0) {
      WlMessageContext var1 = ((Container)var0).getUnfilteredMessageContext();
      return var1;
   }

   public static String getRequestMessageId(WlMessageContext var0) {
      MessageIdHeader var1 = (MessageIdHeader)var0.getHeaders().getHeader(MessageIdHeader.TYPE);

      assert var1 != null;

      return var1.getMessageId();
   }

   public static void saveContextForResponsePath(WlMessageContext var0) throws WsException {
      MessageIdHeader var2 = (MessageIdHeader)var0.getHeaders().getHeader(MessageIdHeader.TYPE);
      String var1;
      if (var2 != null) {
         var1 = var2.getMessageId();
      } else {
         var1 = (String)var0.getProperty("weblogic.wsee.addressing.MessageId");
      }

      assert var1 != null;

      boolean var3 = var0.getProperty("weblogic.wsee.enable.rm") != null;
      String var4 = (String)var0.getProperty("weblogic.jws.AsyncTransactionalInvoke");
      boolean var5 = var4 != null && var4.equals("persistent");
      if (verbose) {
         Verbose.say("Saving request path state for message " + var1 + ". RM=" + var3 + " persistent=" + var5);
      }

      var0.removeProperty("weblogic.jws.AsyncTransactionalInvoke");
      var0.setProperty("weblogic.jws.AsyncTransactionalInvokeInProgress", "true");
      WsStorage var6 = WsStorageFactory.getStorage("weblogic.wsee.async.store", new AsyncInvokeStateObjectHandler());
      AsyncInvokeState var7 = new AsyncInvokeState();
      var7.setAsyncPostCallContext(new AsyncPostCallContextImpl());
      var7.setMessageContext(var0);
      var7.setSubject(ClusterUtil.getSubject((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction())));
      var7.setDispatcher((ClientDispatcher)null);
      String var8 = (String)var7.getMessageContext().getProperty("weblogic.wsee.enclosing.jws.serviceuri");
      if (var8 == null) {
         var8 = var7.getMessageContext().getDispatcher().getConnection().getTransport().getServiceURI();
         var7.getMessageContext().setProperty("weblogic.wsee.enclosing.jws.serviceuri", var8);
      }

      AsyncUtil.setApplicationVersionIdIntoContexts(var7.getMessageContext(), var7.getAsyncPostCallContext());
      String var9 = var1 + "SavedRequestStateKey";
      if (var3) {
         removeNonSerializablePropertiesFromContext(var0);

         try {
            var0.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", PolicyContext.getResponseEffectivePolicy(var0));
            WssPolicyContext var10 = (WssPolicyContext)var0.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
            if (var10 == null) {
               var10 = var0.getDispatcher().getWsPort().getEndpoint().getService().getWssPolicyContext();
               var0.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", var10);
            }
         } catch (PolicyException var12) {
            throw new JAXRPCException(var12);
         }

         try {
            if (var5) {
               var6.persistentPut(var9, var7);
            } else {
               var6.put(var9, var7);
            }
         } catch (PersistentStoreException var11) {
            throw new JAXRPCException(var11);
         }
      } else {
         var6.put(var9, var7);
      }

   }

   private static void removeNonSerializablePropertiesFromContext(WlMessageContext var0) throws WsException {
      var0.removeProperty("weblogic.wsee.handler.jaxrpcHandlerChain");
      Container var1 = getContainer(var0);
      ConversationalContainer var2 = null;
      if (var1 instanceof ConversationalContainer) {
         var2 = (ConversationalContainer)var1;
      }

      var0.removeProperty("weblogic.wsee.jws.container");
      LockManager.Lock var3 = (LockManager.Lock)var0.getProperty("weblogic.wsee.conversation.Lock");
      if (var3 != null) {
         var3.release();

         try {
            StoreConfig var4 = (StoreConfig)var0.getProperty("weblogic.wsee.conversation.StoreConfig");
            if (var4 != null && var2 != null) {
               Store var5 = StoreManager.getStore(var4);
               var5.update(var2);
            }
         } catch (Exception var6) {
            throw new WsException(var6.toString(), var6);
         }
      }

      var0.removeProperty("weblogic.wsee.conversation.Lock");
   }

   private static Container getContainer(MessageContext var0) {
      Container var1 = ContainerFactory.getContainer(var0);
      return var1;
   }

   public static void dispatchResponsePath(String var0, SOAPMessage var1) throws WsException {
      if (verbose) {
         Verbose.say("ServerResponsePathDispatcher dispatching result: " + var1);
      }

      FinalInvokeConfig var2 = loadSavedPathState(var0, (JwsContext)null);
      doFinalInvoke(var2, var2.context, var2.subject, var1);
      if (verbose) {
         Verbose.say("ServerResponsePathDispatcher done");
      }

   }

   public static void dispatchResponsePath(JwsContext var0, String var1, Object var2) throws WsException {
      System.out.println("ServerResponsePathDispatcher dispatching result: " + var2);
      FinalInvokeConfig var3 = loadSavedPathState(var1, var0);
      doFinalInvoke(var3, var3.context, var3.subject, var2);
      System.out.println("ServerResponsePathDispatcher done");
   }

   private static FinalInvokeConfig createFinalInvokeConfig(JwsContext var0, AsyncInvokeState var1) {
      FinalInvokeConfig var2 = new FinalInvokeConfig();
      if (var0 != null) {
         var2.container = (Container)var0;
         var2.dispatcher = (ServerDispatcher)var2.container.messageContext.getDispatcher();
      } else {
         var2.container = null;
         var2.dispatcher = new ServerDispatcher(var1.getMessageContext());
         AsyncUtil.SavedServiceInfo var3 = AsyncUtil.getSavedServiceInfo(var1, verbose);
         WsPort var4 = var3.wsPort;
         var2.dispatcher.setWsPort(var4);
         var2.dispatcher.setHandlerChain(new HandlerIterator(var2.dispatcher.getWsPort().getInternalHandlerList()));
      }

      return var2;
   }

   private static void doFinalInvoke(FinalInvokeConfig var0, WlMessageContext var1, AuthenticatedSubject var2, Object var3) throws WsException {
      addBackNonSerializablePropertiesToContext(var0.container, var0.dispatcher, var1);
      AuthorizedInvoke var4 = new AuthorizedInvoke(var1, var0.dispatcher, var3);
      if (var2 != null) {
         try {
            SecurityServiceManager.runAs((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction()), var2, var4);
         } catch (PrivilegedActionException var8) {
            if (var8.getException() instanceof WsException) {
               throw (WsException)var8.getException();
            }

            throw new UndeclaredThrowableException(var8.getException());
         }
      } else {
         try {
            var4.run();
         } catch (WsException var6) {
            throw var6;
         } catch (Exception var7) {
            throw new WsException(var7.toString(), var7);
         }
      }

   }

   private static FinalInvokeConfig loadSavedPathState(String var0, JwsContext var1) throws WsException {
      if (var0 == null) {
         throw new WsException("Null message ID passed to dispatchResponsePath");
      } else {
         if (verbose) {
            Verbose.say("Loading request path state for message " + var0);
         }

         String var2 = var0 + "SavedRequestStateKey";
         WsStorage var3 = WsStorageFactory.getStorage("weblogic.wsee.async.store", new AsyncInvokeStateObjectHandler());

         AsyncInvokeState var4;
         try {
            var4 = (AsyncInvokeState)var3.persistentGet(var2);
            if (var4 == null) {
               throw new WsException("Cannot retrieve 'saved state' from request path (key='" + var2 + "') in dispatchResponsePath");
            }
         } catch (PersistentStoreException var10) {
            if (verbose) {
               Verbose.logException(var10);
            }

            throw new WsException(var10.toString(), var10);
         }

         synchronized(var4) {
            var4 = (AsyncInvokeState)var3.get(var2);
            if (var4 == null) {
               throw new WsException("Cannot retrieve 'saved state' from request path (key='" + var2 + "') in dispatchResponsePath");
            } else {
               try {
                  var3.persistentRemove(var2);
               } catch (Exception var8) {
                  throw new WsException(var8.toString(), var8);
               }

               FinalInvokeConfig var6 = createFinalInvokeConfig(var1, var4);
               var6.context = var4.getMessageContext();
               var6.subject = var4.getSubject();
               return var6;
            }
         }
      }
   }

   private static void addBackNonSerializablePropertiesToContext(JwsContext var0, ServerDispatcher var1, WlMessageContext var2) {
      var2.setProperty("weblogic.wsee.jws.container", var0);
      HandlerListImpl var3 = (HandlerListImpl)var1.getWsPort().getInternalHandlerList();

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         Handler var5 = var3.get(var4);
         if (var5 instanceof JaxrpcChainHandler) {
            ((JaxrpcChainHandler)var5).prepareForFirstHandleResponse(var2);
         }
      }

   }

   public static void handleRMEndProcessing(WlMessageContext var0) {
      try {
         String var1 = (String)var0.getProperty("weblogic.wsee.reliability.RequestMessageSeqID");
         String var2 = (String)var0.getProperty("weblogic.wsee.reliability.RequestMessageSeqNumber");
         String var3 = (String)var0.getProperty("weblogic.wsee.reliability.RequestMessageAction");
         if (var1 != null) {
            long var4 = Long.parseLong(var2);
            String var6 = (String)var0.getProperty("weblogic.wsee.reliability.RequestMessageOfferSeqID");
            if (var6 != null) {
               SAFConversationInfo var7 = SAFManagerImpl.getManager().getConversationInfoOnSendingSide(var6);
               if (var7 != null) {
                  WsrmSequenceContext var8 = (WsrmSequenceContext)var7.getContext();
                  if (var8 != null) {
                     if (var8.getResponseSeqNumFromRequestSeqNum(var4) == -2L) {
                        if (!var0.containsProperty("weblogic.jws.AsyncTransactionalInvokeInProgress")) {
                           if (verbose) {
                              Verbose.say("*** Doing no-response mapping of requestSeqNum " + var4 + " action(" + var3 + ")" + " to 0 on offer sequence " + var6);
                           }

                           var8.mapRequestSeqNumToResponseSeqNum(var4, 0L);
                           SAFManagerImpl.getManager().storeConversationContextOnSendingSide(var6, var8);
                           EndpointReference var9 = (EndpointReference)var0.getProperty("weblogic.wsee.addressing.ReplyTo");
                           if (var9 == null || AddressingHelper.isAnonymousEndpointReference(var0, var9)) {
                              var9 = new EndpointReference(var7.getDestinationURL());
                           }

                           WsrmSAFReceivingManager var10 = WsrmSAFManagerFactory.getWsrmSAFReceivingManager();
                           var10.checkForAutoTerminateOnOfferSequence(var6, var8, var9);
                        } else if (verbose) {
                           Verbose.say("*** Deferred mapping of requestSeqNum " + var4 + " action(" + var3 + ")" + " on offer sequence " + var6 + " until response path is dispatched at a later date");
                        }
                     } else if (verbose) {
                        Verbose.say("*** Found that someone started processing and mapped requestSeqNum " + var4 + " action(" + var3 + ")" + " already on offer sequence " + var6);
                     }
                  }
               } else if (verbose) {
                  Verbose.say("*** Didn't find conversation info for offer sequence " + var6 + " in order to map requestSeqNum " + var4 + " action(" + var3 + ")" + " to response on offer sequence");
               }
            } else if (verbose) {
               Verbose.say("*** Didn't find a saved offer sequence ID on message context needed to process a sequence message from request sequence " + var1 + " in order to map requestSeqNum " + var4 + " action(" + var3 + ")" + " to response on offer sequence");
            }
         }
      } catch (Exception var11) {
         if (verbose) {
            Verbose.logException(var11);
         }
      }

   }

   private static void ensureWsMethodOnDispatcher(ServerDispatcher var0) throws WsException {
      WlMessageContext var1 = var0.getContext();
      if (var0.getWsMethod() == null) {
         QName var2 = (QName)var1.getProperty("weblogic.wsee.ws.server.OperationName");
         if (var2 == null) {
            throw new WsException("No operation name on MessageContext");
         }

         WsMethod var3 = var0.getWsPort().getEndpoint().getMethod(var2.getLocalPart());
         if (var3 == null) {
            throw new WsException("Unable to find method with name:" + var2 + " method available are -- " + var0.getWsPort().getEndpoint());
         }

         var0.setWsMethod(var3);
      }

   }

   private static class AuthorizedInvoke implements PrivilegedExceptionAction {
      private WlMessageContext savedMc;
      private ServerDispatcher dispatcher;
      private Object result;

      AuthorizedInvoke(WlMessageContext var1, ServerDispatcher var2, Object var3) {
         this.savedMc = var1;
         this.dispatcher = var2;
         this.result = var3;
      }

      public Object run() throws WsException {
         this.dispatcher.setContext(this.savedMc);
         ServerResponsePathDispatcher.ensureWsMethodOnDispatcher(this.dispatcher);
         if (this.result instanceof SOAPMessage) {
            SOAPMessage var1 = (SOAPMessage)this.result;
            SOAPMessageContext var2 = (SOAPMessageContext)this.dispatcher.getContext();
            var2.setMessage(var1);

            try {
               if (var1.getSOAPBody().hasFault()) {
                  SOAPFault var3 = var1.getSOAPBody().getFault();
                  SOAPFaultException var4 = new SOAPFaultException(var3.getFaultCodeAsQName(), var3.getFaultString(), var3.getFaultActor(), var3.getDetail());
                  var4.fillInStackTrace();
                  this.dispatcher.getContext().setFault(var4);
               }
            } catch (Exception var9) {
               throw new WsException(var9.toString(), var9);
            }
         } else if (this.result instanceof Throwable) {
            this.dispatcher.getContext().setFault((Throwable)this.result);
         } else {
            WsReturnType var10 = this.dispatcher.getWsMethod().getReturnType();
            if (var10 != null) {
               this.dispatcher.getOutParams().put(var10.getName(), this.result);
            }
         }

         try {
            this.dispatcher.dispatchResponsePath();
         } finally {
            ServerResponsePathDispatcher.handleRMEndProcessing(this.savedMc);
         }

         return null;
      }
   }

   private static class FinalInvokeConfig {
      public WlMessageContext context;
      public AuthenticatedSubject subject;
      public Container container;
      public ServerDispatcher dispatcher;

      private FinalInvokeConfig() {
      }

      // $FF: synthetic method
      FinalInvokeConfig(Object var1) {
         this();
      }
   }
}
