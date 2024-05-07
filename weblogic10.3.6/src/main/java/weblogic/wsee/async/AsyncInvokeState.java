package weblogic.wsee.async;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaders;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.server.StateExpiration;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.dispatch.client.ClientDispatcher;

public class AsyncInvokeState implements StateExpiration {
   private AsyncPostCallContextImpl asyncPostCtx;
   private WlMessageContext wlmc;
   private ClientDispatcher dispatcher;
   private String serviceClassName;
   private STATE state;
   private String msgId;
   private static final boolean verbose = Verbose.isVerbose(AsyncInvokeState.class);
   private static final boolean verboseProps = Verbose.isVerbose(AsyncInvokeState.class.getName() + "Prop");
   private AuthenticatedSubject subject;

   public AsyncInvokeState() {
      this.state = AsyncInvokeState.STATE.NEW;
      this.subject = null;
   }

   public String getServiceClassName() {
      return this.serviceClassName;
   }

   public void setServiceClassName(String var1) {
      this.serviceClassName = var1;
   }

   public ClientDispatcher getDispatcher() {
      return this.dispatcher;
   }

   public void setDispatcher(ClientDispatcher var1) {
      this.dispatcher = var1;
   }

   public void setSubject(AuthenticatedSubject var1) {
      this.subject = var1;
   }

   public AuthenticatedSubject getSubject() {
      return this.subject;
   }

   public void setState(STATE var1) {
      if (verbose) {
         Verbose.log((Object)("AIS: In setPendingReliableResponse(" + var1 + ") for msgId " + this.msgId));
      }

      this.state = var1;
   }

   public STATE getState() {
      return this.state;
   }

   public boolean isExpired() {
      boolean var1 = this.wlmc != null && this.wlmc.getProperty("weblogic.wsee.enable.rm") != null;
      if (verbose) {
         Verbose.say("AIS:isExpired called for msg id: " + this.msgId + ", serviceClass=" + this.serviceClassName + ", asString:\t" + this.asString());
      }

      if (verboseProps) {
         this.dumpMessageContextProps(this.msgId);
      }

      if (var1 && this.isExpiredRMState(this.msgId)) {
         return true;
      } else {
         if (this.asyncPostCtx != null) {
            long var2 = this.asyncPostCtx.getAbsTimeout();
            if (var2 > 0L && var2 < System.currentTimeMillis()) {
               if (verbose) {
                  Verbose.say("AIS: Returning expired on timeout for msg id: " + this.msgId);
               }

               return true;
            }
         }

         if (verbose) {
            Verbose.say("AIS: Returning not expired for msg id: " + this.msgId);
         }

         return false;
      }
   }

   private void dumpMessageContextProps(String var1) {
      if (this.wlmc != null) {
         Verbose.say("AIS: Dumping wlmc for message id: " + var1);
         Verbose.say("\tProperties");
         Iterator var2 = this.wlmc.getPropertyNames();

         while(true) {
            while(true) {
               while(var2.hasNext()) {
                  String var3 = (String)var2.next();
                  if ("weblogic.wsee.invoke_properties".equals(var3)) {
                     Verbose.say("Invoke Properties:");
                     Map var4 = (Map)this.wlmc.getProperty(var3);
                     if (var4 == null) {
                        Verbose.say("\tNULL");
                     } else {
                        Iterator var5 = var4.keySet().iterator();

                        while(var5.hasNext()) {
                           Object var6 = var5.next();
                           Verbose.say("\t\t" + var6 + "=" + var4.get(var6));
                        }
                     }
                  } else {
                     Verbose.say("\t" + var3 + "=" + this.wlmc.getProperty(var3));
                  }
               }

               Verbose.say("Msg Headers");
               MsgHeaders var7 = this.wlmc.getHeaders();
               var2 = null;
               if (var7 != null) {
                  var2 = var7.listHeaders();
               }

               if (var2 != null) {
                  while(var2.hasNext()) {
                     MsgHeader var8 = (MsgHeader)var2.next();
                     Verbose.say(var8.toString());
                  }
               }

               return;
            }
         }
      }
   }

   private boolean isExpiredRMState(String var1) {
      if (verbose) {
         Verbose.say("AIS: Dealing with RM State for msg id: " + var1);
      }

      if (this.state == AsyncInvokeState.STATE.PENDING_RESPONSE) {
         if (verbose) {
            Verbose.say("AIS: Have pending reliable response for msg id " + var1 + ". Returning not-expired");
         }

         return false;
      } else {
         Map var4 = (Map)this.wlmc.getProperty("weblogic.wsee.invoke_properties");
         String var2;
         String var3;
         if (var4 == null) {
            if (verbose) {
               Verbose.say("AIS: invokeProperties is null - looking for explicit server-side properties. Msg id: " + var1);
            }

            var2 = (String)this.wlmc.getProperty("weblogic.wsee.reliability.RequestMessageSeqID");
            var3 = (String)this.wlmc.getProperty("weblogic.wsee.reliability.RequestMessageOfferSeqID");
         } else {
            if (verbose) {
               Verbose.say("AIS: invokeProperties is not null - searching it for sequence and offer sequence ID values. Msg id: " + var1);
            }

            var2 = (String)var4.get("weblogic.wsee.sequenceid");
            var3 = (String)var4.get("weblogic.wsee.offer.sequence.id");
         }

         if (var2 == null && var3 == null && var4 == null) {
            if (verbose) {
               Verbose.say("AIS: invokeProperties is null - returning expired for msg id: " + var1);
            }

            return true;
         } else if (var2 != null && var2.equals("PendingSeqId") || var3 != null && var3.equals("PendingOfferSeqId")) {
            if (verbose) {
               Verbose.say("AIS: seqId is null and oSeqId is null - returning not expired for msg id: " + var1);
            }

            return false;
         } else {
            if (verbose) {
               Verbose.say("AIS: Found sequence ID as:  " + var2 + " for msg id: " + var1);
               Verbose.say("AIS: Found offer seq ID as: " + var3 + " for msg id: " + var1);
            }

            SAFConversationInfo var5 = null;
            SAFConversationInfo var6 = null;
            String var7 = "Sending";

            try {
               if (var3 != null) {
                  if (var4 != null) {
                     var6 = SAFManagerImpl.getManager().getConversationInfoOnReceivingSide(var3);
                     var7 = "Receiving";
                  } else {
                     var6 = SAFManagerImpl.getManager().getConversationInfoOnSendingSide(var3);
                     var7 = "Sending";
                  }

                  if (verbose) {
                     Verbose.say("AIS: SAFConversationInfo for offer seqid (on " + var7 + " side) " + (var6 == null ? "is null" : "was found OK") + " for msg id: " + var1);
                  }
               }

               if (var2 != null) {
                  var5 = SAFManagerImpl.getManager().getConversationInfoOnSendingSide(var2);
                  if (var5 == null) {
                     if (verbose) {
                        Verbose.say("AIS: SAFConversationInfo for seqid " + (var5 == null ? "is null" : "was found OK") + " for msg id: " + var1);
                     }
                  } else if (var6 == null) {
                     String var8 = var5.getConversationOffer().getConversationName();
                     if (var4 != null) {
                        var6 = SAFManagerImpl.getManager().getConversationInfoOnReceivingSide(var8);
                        var7 = "Receiving";
                     } else {
                        var6 = SAFManagerImpl.getManager().getConversationInfoOnSendingSide(var8);
                        var7 = "Sending";
                     }
                  }
               }
            } catch (Throwable var9) {
               if (verbose) {
                  Verbose.say("Caught exception " + var9 + "when geting conversation info\nReturning not expired");
               }

               return false;
            }

            if (var5 == null && var6 == null) {
               if (verbose) {
                  Verbose.say("AIS: Conversation info for seqid and oseqid (" + var7 + " side) are null - returning expired for msg id: " + var1);
               }

               return true;
            } else {
               if (verbose) {
                  Verbose.say("AIS: Either sequence or Offer sequence (" + var7 + " side) not terminated - now checking for non-RM timeout on msg id: " + var1);
               }

               return false;
            }
         }
      }
   }

   public AsyncPostCallContextImpl getAsyncPostCallContext() {
      return this.asyncPostCtx;
   }

   public WlMessageContext getMessageContext() {
      return this.wlmc;
   }

   public void setAsyncPostCallContext(AsyncPostCallContextImpl var1) {
      this.asyncPostCtx = var1;
   }

   public void setMessageContext(WlMessageContext var1) {
      this.wlmc = var1;
      String var2 = null;
      if (this.wlmc != null) {
         var2 = (String)this.wlmc.getProperty("weblogic.wsee.addressing.MessageId");
         if (var2 == null) {
            MsgHeaders var3 = this.wlmc.getHeaders();
            if (var3 != null) {
               MessageIdHeader var4 = (MessageIdHeader)var3.getHeader(MessageIdHeader.TYPE);
               if (var4 != null) {
                  var2 = var4.getMessageId();
               }
            }
         }
      }

      if (verbose && this.msgId != null) {
         Verbose.log((Object)("AIS: Resetting msgId in AsyncInvokeState. Was " + this.msgId + ", now " + var2));
      }

      this.msgId = var2;
   }

   public String getMessageId() {
      return this.msgId;
   }

   public void setMessageId(String var1) {
      this.msgId = var1;
   }

   public String asString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("AsyncInvokeState: " + this);
      if (this.asyncPostCtx != null) {
         var1.append("\n\tExpiration timeout is = " + this.asyncPostCtx.getAbsTimeout() + "\n");
         HashMap var2 = this.asyncPostCtx.getProperties();
         var1.append("\tAsyncPostContextProperties\n");
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Object var5 = var2.get(var4);
            var1.append("\n\t" + var4 + "=" + var5 + "\n");
         }
      }

      return var1.toString();
   }

   public static enum STATE {
      NEW,
      PENDING_RESPONSE,
      COMPLETE;
   }
}
