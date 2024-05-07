package weblogic.wsee.conversation;

import java.util.Map;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.LocalServerIdentity;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.addressing.AddressingProvider;
import weblogic.wsee.addressing.AddressingProviderFactory;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.ReplyToHeader;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.cluster.ClusterUtil;
import weblogic.wsee.cluster.CorrelationHeader;
import weblogic.wsee.cluster.ServiceIdentityHeader;
import weblogic.wsee.connection.soap.SoapClientConnection;
import weblogic.wsee.message.FreeStandingMsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.message.soap.SoapMsgHeaders;
import weblogic.wsee.server.ServerUtil;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.AddressingUtil;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.Verbose;

public class ConversationClientHandler extends ConversationHandler {
   public static final boolean verbose = Verbose.isVerbose(ConversationClientHandler.class);

   public boolean handleRequest(MessageContext var1) {
      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         ConversationPhase var3 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
         if (var3 != null && var3 != ConversationPhase.NONE) {
            if (var3 == ConversationPhase.START && var1.getProperty("weblogic.wsee.conversation.started") != null) {
               throw new JAXRPCException("Cannot invoke a start method when conversation has already started. Conv id=" + var1.getProperty("weblogic.wsee.conversation.ConversationId"));
            } else {
               int var4 = this.getConversationMajorVersion(var2);
               String var5 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");
               if (var4 == 2 && var3 == ConversationPhase.START && var5 != null) {
                  throw new JAXRPCException("In Diablo style conversation, a start method invoke should not include a client assigned conversation ID: " + var5);
               } else {
                  boolean var6 = KernelStatus.isServer();
                  Map var7 = (Map)var1.getProperty("weblogic.wsee.invoke_properties");

                  assert var7 != null;

                  if (var5 != null) {
                     if (var4 == 1) {
                        String var11 = (String)var1.getProperty("weblogic.wsee.callback.loc");
                        if (var3 == ConversationPhase.START) {
                           var2.getHeaders().addHeader(new StartHeader(var5, var11));
                        } else {
                           var2.getHeaders().addHeader(new ContinueHeader(var5));
                        }
                     } else if (var1.getProperty("weblogic.wsee.conversation.key") != null) {
                        var1.removeProperty("weblogic.wsee.conversation.key");
                     }
                  } else if (var4 == 1) {
                     if (var3 != ConversationPhase.START) {
                        throw new JAXRPCException("Conversation id does not exist for operation marked " + var3);
                     }

                     var5 = Guid.generateGuid();
                     var1.setProperty("weblogic.wsee.conversation.ConversationId", var5);
                     var7.put("weblogic.wsee.conversation.ConversationId", var5);
                     var2.getHeaders().addHeader(new StartHeader(var5));
                  } else {
                     if (var4 != 2) {
                        throw new JAXRPCException("Unsupported conversation version " + var4);
                     }

                     if (var3 == ConversationPhase.START) {
                        boolean var8 = var2.getDispatcher().getOperation().getType() != 0;
                        ReplyToHeader var9 = (ReplyToHeader)var2.getHeaders().getHeader(ReplyToHeader.TYPE);

                        assert var9 != null;

                        boolean var10 = !AddressingUtil.isAnonymous(var1, var9.getReference()) || var1.getProperty("weblogic.wsee.async.invoke") != null;
                        if (!var6) {
                           if (var8 || var10) {
                              throw new JAXRPCException("Invoking a one way conversational start method requires to be on a server.");
                           }
                        } else if (var8 || var10) {
                           this.handleAsyncConvId(var1, var10, var9, var7);
                        }
                     }
                  }

                  var7.put("weblogic.wsee.conversation.started", "true");
                  return true;
               }
            }
         } else {
            return true;
         }
      }
   }

   private void handleAsyncConvId(MessageContext var1, boolean var2, ReplyToHeader var3, Map var4) {
      EndpointReference var5 = null;
      AddressingProvider var6 = AddressingProviderFactory.getInstance().getAddressingProvider(var1);
      String var7;
      if (var2) {
         var5 = var6.createEndpointReference(var3.getReference().getAddress());
      } else {
         var7 = SoapClientConnection.getDestinationAddress(var1);
         String var8 = ClusterUtil.getProtocol(var7);
         if (var8 == null) {
            var8 = "http";
         }

         String var9 = ServerUtil.getServerURL(var8) + AsyncUtil.getAsyncUri(AsyncUtil.isSoap12(var1), var8);
         if ("jms".equals(var8)) {
            var9 = var9 + "?URI=" + ServerUtil.getMessagingQueue() + "&FACTORY=" + ServerUtil.getJmsConnectionFactory();
         }

         var5 = var6.createEndpointReference(var9);
      }

      var7 = Guid.generateGuid();
      this.mergeReplyToHeader(var3, var5, (WlMessageContext)var1, var7);
      this.saveConversationInvokeState(var1, var7);
      var4.put("weblogic.wsee.conversation.correlation.id", var7);
      var4.put("weblogic.wsee.conversation.epr.set", "false");
      var4.put("weblogic.wsee.conversation.key", Guid.generateGuid());
   }

   private void saveConversationInvokeState(MessageContext var1, String var2) {
      boolean var3 = var1.getProperty("weblogic.wsee.enable.rm") != null;
      WsStorage var4 = WsStorageFactory.getStorage("weblogic.wsee.conversation.store", new ConversationInvokeStateObjectHandler());
      ConversationInvokeState var5 = new ConversationInvokeState();
      Integer var6 = (Integer)var1.getProperty("weblogic.wsee.conversation.method.block.timeout");
      var5.setAbsTimeout(System.currentTimeMillis() + (long)(var6 == null ? 120000 : var6 * 1000));
      var5.setRmState(var3);
      String var7 = (String)var1.getProperty("weblogic.wsee.version.appversion.id");
      var5.setAppVersion(var7);

      try {
         if (var3) {
            if (verbose) {
               Verbose.log((Object)("Storing ConversationInvokeState RM=true with NO seq ID under correlation ID " + var2 + ". Will not get cleaned up till explicitly removed, seqId set and seq is terminated, or main timeout expires"));
            }

            var4.persistentPut(var2, var5);
         } else {
            var4.put(var2, var5);
         }

      } catch (PersistentStoreException var9) {
         throw new JAXRPCException(var9);
      }
   }

   private void mergeReplyToHeader(ReplyToHeader var1, EndpointReference var2, WlMessageContext var3, String var4) {
      FreeStandingMsgHeaders var5 = new FreeStandingMsgHeaders();
      CorrelationHeader var6 = new CorrelationHeader();
      var6.setCorrelationId(var4);
      var5.addHeader(var6);
      ServiceIdentityHeader var7 = new ServiceIdentityHeader();
      var7.setServerName(LocalServerIdentity.getIdentity().getServerName());
      var7.setServiceName("weblogic.wsee.conversation.msg.cluster.service");
      var5.addHeader(var7);
      FreeStandingMsgHeaders var8 = (FreeStandingMsgHeaders)var1.getReference().getReferenceParameters();
      var5.merge(var8);
      var2.setReferenceParameters(var5);
      var2.setReferenceProperties((FreeStandingMsgHeaders)var1.getReference().getReferenceProperties());
      AddressingProvider var9 = AddressingProviderFactory.getInstance().getAddressingProvider((MessageContext)var3);
      ((SoapMsgHeaders)var3.getHeaders()).replaceHeader(var9.createReplyToHeader(var2));
   }
}
