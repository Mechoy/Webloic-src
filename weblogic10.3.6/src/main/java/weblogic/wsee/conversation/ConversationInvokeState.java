package weblogic.wsee.conversation;

import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.server.StateExpiration;
import weblogic.wsee.util.Verbose;

public class ConversationInvokeState implements StateExpiration {
   private EndpointReference epr;
   private long absTimeout = -1L;
   private String seqId = "<UNKNOWN>";
   private boolean rmState = false;
   private static final boolean verbose = Verbose.isVerbose(ConversationInvokeState.class);
   public static final String UNKNOWN_SEQID = "<UNKNOWN>";
   private String appVersion;
   private String appName;
   private String storeConfig;

   public void setSeqId(String var1) {
      this.seqId = var1;
   }

   public void setAppVersion(String var1) {
      this.appVersion = var1;
   }

   public void setAppName(String var1) {
      this.appName = var1;
   }

   public void setStoreConfig(String var1) {
      this.storeConfig = var1;
   }

   public String getSeqId() {
      return this.seqId;
   }

   public String getAppVersion() {
      return this.appVersion;
   }

   public String getAppName() {
      return this.appName;
   }

   public String getStoreConfig() {
      return this.storeConfig;
   }

   public void setRmState(boolean var1) {
      this.rmState = var1;
   }

   public boolean isRmState() {
      return this.rmState;
   }

   public boolean isExpired() {
      if (verbose) {
         Verbose.say("CIS:isExpired called for:\n\t" + this.asString());
      }

      if (this.rmState) {
         if (verbose) {
            Verbose.say("CIS: Dealing with RM State");
         }

         if (!this.seqId.equals("<UNKNOWN>")) {
            try {
               if (verbose) {
                  Verbose.say("CIS RM State: offer seqID =  " + this.seqId);
               }

               SAFConversationInfo var1 = SAFManagerImpl.getManager().getConversationInfoOnReceivingSide(this.seqId);
               if (var1 == null) {
                  if (verbose) {
                     Verbose.say("CIS RM State: SAFConversationInfo for offer is null - returning true");
                  }

                  return true;
               }
            } catch (Throwable var2) {
               if (verbose) {
                  Verbose.say("CIS RM State: Caught exception " + var2 + " when geting conversation info for offer.\nReturning not expired");
               }

               return false;
            }

            if (verbose) {
               Verbose.say("CIS RM State: Returning not expired");
            }

            return false;
         } else {
            if (verbose) {
               Verbose.say("CIS: RM state with no seqId: Returning not expired");
            }

            if (verbose) {
               Verbose.say("CIS RM State: Returning not expired");
            }

            return false;
         }
      } else if (this.absTimeout > 0L && this.absTimeout < System.currentTimeMillis()) {
         if (verbose) {
            Verbose.say("Non RM State: Timed out - returning expired");
         }

         return true;
      } else {
         if (verbose) {
            Verbose.say("Non RM State: Returning not expired");
         }

         return false;
      }
   }

   public synchronized EndpointReference getEpr() {
      return this.epr;
   }

   public long getAbsTimeout() {
      return this.absTimeout;
   }

   public void setAbsTimeout(long var1) {
      this.absTimeout = var1;
   }

   public synchronized EndpointReference getEpr(int var1) {
      if (this.epr != null) {
         return this.epr;
      } else {
         try {
            this.wait((long)(var1 * 1000));
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }

         return this.epr;
      }
   }

   public synchronized void setEpr(EndpointReference var1) {
      this.epr = var1;
      this.notify();
   }

   public String asString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("ConvInvokeState: " + this);
      if (this.epr != null) {
         var1.append("\n\tEPR = " + this.epr);
      } else {
         var1.append("\n\tEPR = null");
      }

      var1.append("\n\tAbs timeout = " + this.absTimeout);
      var1.append("\n\tSequence ID = " + this.seqId);
      var1.append("\n\tRM CIS = " + this.rmState);
      var1.append("\n\tApp version = " + this.appVersion);
      return var1.toString();
   }
}
