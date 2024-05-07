package weblogic.wsee.async;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.protocol.LocalServerIdentity;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.store.PersistentStoreException;
import weblogic.utils.collections.ConcurrentHashMap;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.addressing.ReplyToHeader;
import weblogic.wsee.cluster.ClusterUtil;
import weblogic.wsee.cluster.ServiceIdentityHeader;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMsgHeaders;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.policy.runtime.PolicyContext;
import weblogic.wsee.security.policy.WssPolicyContext;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.AddressingUtil;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.dispatch.client.ClientDispatcher;
import weblogic.wsee.wsdl.WsdlOperation;

public class AsyncClientHandler extends AsyncHandler implements WLHandler {
   private static final boolean verbose = Verbose.isVerbose(AsyncClientHandler.class);

   public boolean handleRequest(MessageContext var1) {
      if (var1 == null) {
         return true;
      } else if (!var1.containsProperty("weblogic.wsee.async.invoke")) {
         return true;
      } else if (!var1.containsProperty("weblogic.wsee.async.res.epr")) {
         throw new JAXRPCException("Asynchronous response endpoint reference is required to execute an async method. Async methods can only be invoked from stubs with the ServiceClient annotation");
      } else {
         if (!var1.containsProperty("weblogic.wsee.async.invokeNonJws")) {
            this.checkImplementedHandleResponseAndFault(var1);
         }

         WlMessageContext var2 = WlMessageContext.narrow(var1);
         WsdlOperation var3 = var2.getDispatcher().getOperation();
         if (var3 != null && var3.getType() == 1) {
            if (verbose) {
               Verbose.log((Object)("Operation " + var3.getName() + " is one-way, so no AsyncInvokeState will be stored for it"));
            }

            return true;
         } else {
            AsyncPostCallContextImpl var4 = this.propagateCallContext(var1);
            this.constructReplyTo(var2);
            this.saveAsyncInvokeState(var4, var2);
            return true;
         }
      }
   }

   private AsyncPostCallContextImpl propagateCallContext(MessageContext var1) {
      AsyncPreCallContextImpl var2 = (AsyncPreCallContextImpl)var1.getProperty("weblogic.wsee.async.pre.call.context");
      AsyncPostCallContextImpl var3 = (AsyncPostCallContextImpl)AsyncCallContextFactory.getAsyncPostCallContext();
      if (var2 != null) {
         HashMap var4 = var2.getProperties();
         if (var4.size() > 0) {
            var3.setProperties(var4);
         }

         var3.setStubName((String)var1.getProperty("weblogic.wsee.stub.name"));
         if (var2.getTimeout() > 0L) {
            var3.setAbsTimeout(var2.getTimeout() * 1000L + System.currentTimeMillis());
         }
      }

      var1.removeProperty("weblogic.wsee.async.pre.call.context");
      AsyncUtil.setApplicationVersionIdIntoContexts(var1, var3);
      return var3;
   }

   private void saveAsyncInvokeState(AsyncPostCallContextImpl var1, WlMessageContext var2) {
      MessageIdHeader var3 = (MessageIdHeader)var2.getHeaders().getHeader(MessageIdHeader.TYPE);

      assert var3 != null;

      boolean var4 = var2.getProperty("weblogic.wsee.enable.rm") != null;
      if (var4) {
         Object var5 = (Map)var2.getProperty("weblogic.wsee.invoke_properties");
         if (var5 == null) {
            var5 = new ConcurrentHashMap();
            var2.setProperty("weblogic.wsee.invoke_properties", var5);
         }

         String var6 = (String)((Map)var5).get("weblogic.wsee.sequenceid");
         if (var6 == null || var6.equals("PendingSeqId")) {
            ((Map)var5).put("weblogic.wsee.sequenceid", "PendingSeqId");
         }

         String var7 = (String)((Map)var5).get("weblogic.wsee.offer.sequence.id");
         if (var7 == null || var7.equals("PendingOfferSeqId")) {
            ((Map)var5).put("weblogic.wsee.offer.sequence.id", "PendingOfferSeqId");
         }
      }

      WsStorage var10 = WsStorageFactory.getStorage("weblogic.wsee.async.store", new AsyncInvokeStateObjectHandler());
      AsyncInvokeState var11 = new AsyncInvokeState();
      var11.setAsyncPostCallContext(var1);
      var11.setMessageContext(var2);
      var11.setSubject(ClusterUtil.getSubject((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction())));
      var11.setDispatcher((ClientDispatcher)var2.getDispatcher());
      if (var4) {
         try {
            var2.setProperty("weblogic.wsee.policy.effectiveResponsePolicy", PolicyContext.getResponseEffectivePolicy(var2));
            WssPolicyContext var12 = (WssPolicyContext)var2.getProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx");
            if (var12 == null) {
               var12 = var2.getDispatcher().getWsPort().getEndpoint().getService().getWssPolicyContext();
               var2.setProperty("weblogic.weblogic.wsee.security.policy.WssPolicyCtx", var12);
            }
         } catch (PolicyException var9) {
            throw new JAXRPCException(var9);
         }

         try {
            var10.persistentPut(var3.getMessageId(), var11);
         } catch (PersistentStoreException var8) {
            throw new JAXRPCException(var8);
         }
      } else {
         var10.put(var3.getMessageId(), var11);
      }

   }

   private void constructReplyTo(WlMessageContext var1) {
      ReplyToHeader var2 = (ReplyToHeader)var1.getHeaders().getHeader(ReplyToHeader.TYPE);

      assert var2 != null;

      EndpointReference var3 = (EndpointReference)var1.getProperty("weblogic.wsee.async.res.epr");

      assert var3 != null;

      if (AddressingUtil.isAnonymous(var1, var2.getReference())) {
         var2.getReference().setAddress(var3.getAddress());
      }

      FreeStandingMsgHeaders var4 = (FreeStandingMsgHeaders)var2.getReference().getReferenceParameters();
      FreeStandingMsgHeaders var5 = (FreeStandingMsgHeaders)var3.getReferenceParameters();
      var4.merge(var5);
      if (var4.getHeader(ServiceIdentityHeader.TYPE) == null) {
         ServiceIdentityHeader var6 = new ServiceIdentityHeader();
         var6.setServerName(LocalServerIdentity.getIdentity().getServerName());
         var6.setServiceName("weblogic.wsee.conversation.msg.cluster.service");
         var4.addHeader(var6);
      }

      FreeStandingMsgHeaders var8 = (FreeStandingMsgHeaders)var2.getReference().getReferenceProperties();
      FreeStandingMsgHeaders var7 = (FreeStandingMsgHeaders)var3.getReferenceProperties();
      var8.merge(var7);
      ((SoapMsgHeaders)var1.getHeaders()).replaceHeader(AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var1).createReplyToHeader(var2.getReference()));
   }

   private void checkImplementedHandleResponseAndFault(MessageContext var1) {
      String var2 = AsyncUtil.getAsyncResponseMethodName(var1);
      String var3 = AsyncUtil.getAsyncFailureMethodName(var1);
      Class[] var4 = AsyncUtil.getAsyncResponseMethodParams(var1);
      Class[] var5 = AsyncUtil.getAsyncFailureMethodParams();
      String var6 = (String)var1.getProperty("weblogic.wsee.enclosing.classname");

      Class var7;
      try {
         var7 = Thread.currentThread().getContextClassLoader().loadClass(var6);
      } catch (ClassNotFoundException var11) {
         throw new JAXRPCException("No class found for " + var6);
      }

      try {
         var7.getMethod(var2, var4);
      } catch (NoSuchMethodException var10) {
         throw new JAXRPCException("No onAsyncResponse method '" + var2 + "' found for asynchronous invoke of " + var1.getProperty("weblogic.wsee.method.name"));
      }

      try {
         var7.getMethod(var3, var5);
      } catch (NoSuchMethodException var9) {
         throw new JAXRPCException("No onAsyncFailure method '" + var3 + "' found for asynchronous invoke of " + var1.getProperty("weblogic.wsee.method.name"));
      }
   }

   public boolean handleClosure(MessageContext var1) {
      if (!var1.containsProperty("weblogic.wsee.async.invoke")) {
         return true;
      } else {
         if (verbose) {
            Verbose.say("AsyncClientHandler: handleClosure");
         }

         if (!(var1 instanceof SOAPMessageContext)) {
            if (verbose) {
               Verbose.say("AsyncClientHandler: handleClosure: mc not soapmc");
            }

            return true;
         } else {
            WlMessageContext var2 = WlMessageContext.narrow(var1);
            MessageIdHeader var3 = (MessageIdHeader)var2.getHeaders().getHeader(MessageIdHeader.TYPE);

            assert var3 != null;

            String var4 = (String)var2.getProperty("weblogic.wsee.offer.sequence.id");
            if (var4 == null) {
               if (verbose) {
                  Verbose.say("AsyncClientHandler: handleClosure: offer sequence id is null!!!");
               }

               return true;
            } else {
               WsStorage var5 = WsStorageFactory.getStorage("weblogic.wsee.async.store", new AsyncInvokeStateObjectHandler());

               try {
                  AsyncInvokeState var6 = (AsyncInvokeState)var5.persistentGet(var3.getMessageId());
                  if (var6 == null) {
                     if (verbose) {
                        Verbose.say("AsyncClientHandler: handleClosure: AIS is not stored for messageid " + var3.getMessageId());
                     }

                     return true;
                  }

                  WlMessageContext var7 = var6.getMessageContext();
                  Map var8 = (Map)var2.getProperty("weblogic.wsee.invoke_properties");

                  assert var8 != null;

                  Map var9 = (Map)var7.getProperty("weblogic.wsee.invoke_properties");

                  assert var9 != null;

                  var9.put("weblogic.wsee.offer.sequence.id", var2.getProperty("weblogic.wsee.offer.sequence.id"));
                  if (verbose) {
                     Verbose.say("AsyncClientHandler: handleClosure: ais after new seqid = " + var6.asString());
                  }

                  var5.persistentPut(var3.getMessageId(), var6);
               } catch (PersistentStoreException var10) {
                  if (verbose) {
                     Verbose.say("AsyncClientHandler: handleClosure: caught exception: " + var10);
                  }

                  throw new JAXRPCException(var10);
               }

               if (verbose) {
                  Verbose.say("handleClosure: returning true");
               }

               return true;
            }
         }
      }
   }
}
