package weblogic.wsee.reliability;

import java.io.Externalizable;
import javax.xml.rpc.JAXRPCException;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFEndpoint;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.async.AsyncInvokeState;
import weblogic.wsee.async.AsyncInvokeStateObjectHandler;
import weblogic.wsee.async.SOAPInvokeState;
import weblogic.wsee.buffer.BufferManager;
import weblogic.wsee.server.WsStorage;
import weblogic.wsee.server.WsStorageFactory;
import weblogic.wsee.util.Verbose;

public final class WsrmSAFEndpoint implements SAFEndpoint {
   private final String targetURI;
   private static final boolean verbose = Verbose.isVerbose(WsrmSAFEndpoint.class);
   private static final boolean soapMessageVerbose = Verbose.isVerbose(WsrmSAFEndpoint.class.getName() + "Message");
   private static final int DEFAULT_RETRY_COUNT = 3;
   private static final long DEFAULT_RETRY_DELAY = 5L;

   public WsrmSAFEndpoint(String var1) {
      if (verbose) {
         Verbose.log((Object)("Created WsrmSAFEndpoint for targetURI: " + var1));
      }

      this.targetURI = var1;
   }

   public void deliver(SAFConversationInfo var1, SAFRequest var2) throws SAFException {
      try {
         if (verbose) {
            Verbose.log((Object)("[WsrmSAFEndpoint.deliver()] sequence id " + var1.getConversationName() + ": " + var2.getSequenceNumber()));
         }

         WsrmPayloadContext var3 = (WsrmPayloadContext)var2.getPayloadContext();
         if (var3.isEmptyLastMessage() && verbose) {
            Verbose.log((Object)("Empty last message on sequence id " + var1.getConversationName()));
         }

         Externalizable var4 = var2.getPayload();
         if (var4 instanceof SOAPInvokeState) {
            SOAPInvokeState var5 = (SOAPInvokeState)var4;
            String var6 = (String)var5.getMessageContextProperties().get("weblogic.wsee.addressing.MessageId");
            int var7 = var3.getRetryCount();
            if (var7 < 0) {
               var7 = 3;
            }

            long var8 = var3.getRetryDelay();
            if (var8 < 0L) {
               var8 = 5L;
            }

            if (soapMessageVerbose) {
               try {
                  Verbose.log((Object)"WsrmSAFEndpoint delivers:");
                  Verbose.getOut().println("\n-------------------------------\n");
                  var5.getSOAPMessage().writeTo(Verbose.getOut());
                  Verbose.log((Object)("Retry count: " + var7 + " Retry delay: " + var8));
                  Verbose.getOut().println("\n-------------------------------\n");
               } catch (Exception var20) {
               }
            }

            if (verbose) {
               Verbose.log((Object)("Conversation name is " + var1.getConversationName() + " targetURI=" + this.targetURI + " retryCount=" + var7 + " retryDelay=" + var8));
            }

            String var10 = null;
            if (verbose) {
               var10 = "Unknown";
               if (var5.getMessageContextProperties().containsKey("weblogic.wsee.addressing.Action")) {
                  var10 = (String)var5.getMessageContextProperties().get("weblogic.wsee.addressing.Action");
               }
            }

            String var11 = (String)var5.getMessageContextProperties().get("weblogic.wsee.reliability.RequestMessageSeqNumber");
            long var12 = var11 != null ? Long.parseLong(var11) : -1L;
            if (var11 != null) {
               SAFConversationInfo var14 = var1.getConversationOffer();
               if (var14 != null) {
                  String var15 = var14.getConversationName();
                  WsrmSequenceContext var16 = (WsrmSequenceContext)var14.getContext();
                  if (var16.hasRequestSeqNumBeenMappedToResponseSeqNum(var12)) {
                     if (verbose) {
                        Verbose.say("*** Already had mapping of requestSeqNum " + var12 + " action (" + var10 + ") on request sequence " + var1.getConversationName() + " to response seq num on offer sequence " + var15 + ". This indicates a duplicate message in the endpoint. Leaving the existing mapping in place.");
                     }
                  } else {
                     if (verbose) {
                        Verbose.say("*** Doing initial mapping of requestSeqNum " + var12 + " action (" + var10 + ") on request sequence " + var1.getConversationName() + " to -2 on offer sequence " + var15);
                     }

                     if (var3.isEmptyLastMessage()) {
                        var16.mapRequestSeqNumToResponseSeqNum(var12, 0L);
                     } else {
                        var16.mapRequestSeqNumToResponseSeqNum(var12, -2L);
                     }

                     SAFManagerImpl.getManager().storeConversationContextOnSendingSide(var15, var16);
                  }
               }
            }

            if (var5.getMessageContextProperties().containsKey("weblogic.wsee.addressing.RelatesTo")) {
               String var23 = (String)var5.getMessageContextProperties().get("weblogic.wsee.addressing.RelatesTo");
               if (verbose) {
                  Verbose.log((Object)("WsrmSAFEndpoint is processing async response msgId: " + var6 + " related to msgId: " + var23 + " requestSeqNum " + var12 + " action (" + var10 + ") on request sequence " + var1.getConversationName()));
               }

               WsStorage var24 = WsStorageFactory.getStorage("weblogic.wsee.async.store", new AsyncInvokeStateObjectHandler());

               AsyncInvokeState var25;
               try {
                  var25 = (AsyncInvokeState)var24.persistentGet(var23);
                  if (var25 == null) {
                     if (verbose) {
                        Verbose.log((Object)("Cannot find AsyncInvokeState for request message: " + var23));
                     }

                     throw new JAXRPCException("Cannot retrieve request information for message " + var23);
                  }
               } catch (PersistentStoreException var21) {
                  if (verbose) {
                     Verbose.logException(var21);
                  }

                  throw new JAXRPCException(var21);
               }

               synchronized(var25) {
                  if (var25.getState() == AsyncInvokeState.STATE.NEW) {
                     var25.setState(AsyncInvokeState.STATE.PENDING_RESPONSE);
                     if (verbose) {
                        Verbose.log((Object)("WsrmSAFEndpoint set 'pending reliable response' flag on AsyncInvokeState msgId: " + var6 + " related to msgId: " + var23 + " requestSeqNum " + var12 + " action (" + var10 + ") on request sequence " + var1.getConversationName()));
                     }
                  } else if (verbose) {
                     Verbose.log((Object)("WsrmSAFEndpoint sees AsyncInvokeState is not 'new' for msgId: " + var6 + " related to msgId: " + var23 + " requestSeqNum " + var12 + " action (" + var10 + ") on request sequence " + var1.getConversationName() + ". We won't set any pending response flag here."));
                  }
               }

               var24.persistentPut(var23, var25);
            }

            BufferManager.instance().bufferMessageUOO(this.targetURI, var5, var7, var8, var1.getConversationName(), var2.getSequenceNumber());
            if (verbose) {
               Verbose.log((Object)("WsrmSAFEndpoint done buffering msgId: " + var6 + " requestSeqNum " + var12 + " action (" + var10 + ") on request sequence " + var1.getConversationName() + ". Will now proceed to ack this message back to the sender."));
            }
         }

      } catch (Throwable var22) {
         if (var22 instanceof SAFException) {
            throw (SAFException)var22;
         } else {
            throw new SAFException(var22);
         }
      }
   }

   public String getTargetQueue() {
      return BufferManager.instance().getTargetQueue(this.targetURI).getQueueName();
   }

   public boolean isAvailable() {
      boolean var1 = true;
      return var1;
   }
}
