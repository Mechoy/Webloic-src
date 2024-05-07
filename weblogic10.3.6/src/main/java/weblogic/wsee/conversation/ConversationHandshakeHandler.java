package weblogic.wsee.conversation;

import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.addressing.ReplyToHeader;
import weblogic.wsee.cluster.ClusterDispatcherRemote;
import weblogic.wsee.cluster.ClusterRoutingUtil;
import weblogic.wsee.cluster.ClusterServiceException;
import weblogic.wsee.cluster.CorrelationHeader;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.reliability.ReliableConversationEPR;
import weblogic.wsee.reliability.SAFServerHeader;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.Verbose;

public class ConversationHandshakeHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(ConversationHandshakeHandler.class);

   public QName[] getHeaders() {
      return ConversationConstants.CONV_HEADERS;
   }

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      CorrelationHeader var3 = (CorrelationHeader)var2.getHeaders().getHeader(CorrelationHeader.TYPE);
      if (var3 == null) {
         return true;
      } else {
         WsStorage var4 = WsStorageFactory.getStorage("weblogic.wsee.conversation.store", new ConversationInvokeStateObjectHandler());

         ConversationInvokeState var5;
         try {
            var5 = (ConversationInvokeState)var4.persistentGet(var3.getCorrelationId());
            if (var5 == null) {
               return true;
            }
         } catch (PersistentStoreException var14) {
            if (verbose) {
               Verbose.logException(var14);
            }

            throw new JAXRPCException(var14);
         }

         EndpointReference var6 = null;
         ReplyToHeader var7 = (ReplyToHeader)var2.getHeaders().getHeader(ReplyToHeader.TYPE);
         if (var7 != null) {
            var6 = var7.getReference();
            if (var6 != null) {
               var5.setEpr(var6);
            }
         }

         boolean var8 = var1.containsProperty("weblogic.wsee.enable.rm");

         try {
            if (var8) {
               String var9 = (String)var1.getProperty("weblogic.wsee.reliability.RequestMessageSeqID");
               if (var9 != null) {
                  if (var5.getSeqId() != null && !var5.getSeqId().equals("<UNKNOWN>")) {
                     if (!var9.equals(var5.getSeqId())) {
                        throw new IllegalStateException("Found a different RM seq id than was already set on ConversationInvokeState for correlation id " + var3.getCorrelationId() + ". Old: " + var5.getSeqId() + " New: " + var9);
                     }
                  } else {
                     if (verbose) {
                        Verbose.log((Object)("Setting sequence ID " + var9 + " onto ConversationInvokeState at correlation id " + var3.getCorrelationId()));
                     }

                     var5.setSeqId(var9);
                  }
               } else if (verbose) {
                  Verbose.log((Object)("!! No sequence ID " + var9 + " found on message with Correlation header, for ConversationInfoState with correlation id " + var3.getCorrelationId()));
               }

               var4.persistentPut(var3.getCorrelationId(), var5);
            } else {
               var4.put(var3.getCorrelationId(), var5);
            }
         } catch (PersistentStoreException var17) {
            if (verbose) {
               Verbose.logException(var17);
            }

            throw new JAXRPCException(var17);
         }

         if (var6 == null) {
            return true;
         } else {
            SAFServerHeader var18 = (SAFServerHeader)var2.getHeaders().getHeader(SAFServerHeader.TYPE);
            if (var18 != null) {
               ContinueHeader var10 = (ContinueHeader)var6.getReferenceParameters().getHeader(ContinueHeader.TYPE);
               if (var10 == null) {
                  return true;
               }

               String var11 = var10.getConversationId();
               String var12 = var18.getServerName();

               try {
                  if (verbose) {
                     Verbose.log((Object)("Dispatch Conversation (ID, Key) (" + var11 + ", " + var18.getConversationKey() + ") for " + "weblogic.wsee.reliability.conversation.msg.cluster.service" + " to " + var12));
                  }

                  ClusterDispatcherRemote var13 = ClusterRoutingUtil.getClusterDispatcher(var12, "weblogic.wsee.reliability.conversation.msg.cluster.service");
                  if (var13 != null) {
                     var13.dispatch("weblogic.wsee.reliability.conversation.msg.cluster.service", new ReliableConversationEPR(var18.getConversationKey(), var6, var5.getSeqId()));
                  }
               } catch (ClusterServiceException var15) {
                  if (verbose) {
                     Verbose.logException(var15);
                  }
               } catch (RemoteException var16) {
                  if (verbose) {
                     Verbose.logException(var16);
                  }
               }
            }

            return true;
         }
      }
   }
}
