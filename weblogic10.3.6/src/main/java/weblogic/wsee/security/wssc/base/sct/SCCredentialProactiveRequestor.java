package weblogic.wsee.security.wssc.base.sct;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.xml.rpc.handler.MessageContext;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;
import weblogic.wsee.security.wssc.sct.SCCredential;
import weblogic.wsee.security.wssc.sct.SCTStore;
import weblogic.wsee.security.wssc.v200502.sct.SCTokenHandler;
import weblogic.wsee.security.wst.framework.WSTContext;
import weblogic.wsee.util.Verbose;

public class SCCredentialProactiveRequestor {
   private static final boolean verbose = Verbose.isVerbose(SCCredentialProactiveRequestor.class);
   public static final String ENABLE_SC_CREDENTIAL_PROACTIVE_REQUESTOR = "weblogic.wsee.security.wssc.sct.enableSCCredentialProactiveRequestor";
   public static final String SC_CREDENTIAL_PROACTIVE_REQUESTOR = "weblogic.wsee.security.wssc.sct.scCredentialProactiveRequestor";
   private MessageContext context = null;
   private Semaphore lock = new Semaphore(1);
   private Semaphore proactiveRenewalSignal = new Semaphore(1);
   private long avgTimePeriod = -1L;
   private TimerManager tm = null;

   private SCCredentialProactiveRequestor(MessageContext var1) {
      this.context = var1;
   }

   public static SCCredentialProactiveRequestor getProactiveRequestor(MessageContext var0) {
      SCCredentialProactiveRequestor var1 = (SCCredentialProactiveRequestor)var0.getProperty("weblogic.wsee.security.wssc.sct.scCredentialProactiveRequestor");
      if (var1 == null) {
         var1 = new SCCredentialProactiveRequestor(var0);
         var0.setProperty("weblogic.wsee.security.wssc.sct.scCredentialProactiveRequestor", var1);
         Object var2 = (Map)var0.getProperty("weblogic.wsee.invoke_properties");
         if (var2 == null) {
            var2 = new ConcurrentHashMap();
            var0.setProperty("weblogic.wsee.invoke_properties", var2);
         }

         ((Map)var2).put("weblogic.wsee.security.wssc.sct.scCredentialProactiveRequestor", var1);
      }

      return var1;
   }

   public boolean verify(MessageContext var1) {
      return this.context == var1;
   }

   public void lock() {
      try {
         this.lock.acquire();
         if (verbose) {
            Verbose.log((Object)"Lock to exchange SC credential");
         }
      } catch (InterruptedException var2) {
         var2.printStackTrace();
      }

   }

   public void unlock() {
      this.lock.release();
      if (verbose) {
         Verbose.log((Object)"Unlock after exchanging SC credential");
      }

   }

   public Measure createAndStartMeasure() {
      return new Measure();
   }

   public void asyncRenewNext(SCCredential var1) {
      this.asyncRenewNext(var1, new RenewTimerListener(var1));
   }

   private void asyncRenewNext(SCCredential var1, TimerListener var2) {
      if (this.tm == null) {
         this.tm = TimerManagerFactory.getTimerManagerFactory().getTimerManager("sc_credential_proactive_requestor-" + var1.getIdentifier(), WorkManagerFactory.getInstance().getSystem());
      }

      Calendar var3 = (Calendar)var1.getExpires().clone();
      var3.add(14, (int)this.avgTimePeriod * -2);
      this.tm.schedule(var2, var3.getTime());
      if (verbose) {
         Verbose.log((Object)("Schedules to async renew SC credential at " + var3.getTime()));
      }

   }

   public void waitOutProactiveRenewal() {
      boolean var1 = false;

      try {
         WSTContext var2 = WSTContext.getWSTContext(this.context);
         long var3 = this.avgTimePeriod == -1L ? var2.getLifetimePeriod() : this.avgTimePeriod * 5L;
         var1 = this.proactiveRenewalSignal.tryAcquire(var3, TimeUnit.MILLISECONDS);
         if (verbose) {
            if (var1) {
               Verbose.log((Object)"Waits out proactive async renewal");
            } else {
               Verbose.log((Object)("Proactive nenewal not return in " + var3 / 1000L + " seconds, don't continue to wait it out"));
            }
         }
      } catch (InterruptedException var8) {
         var8.printStackTrace();
      } finally {
         if (var1) {
            this.proactiveRenewalSignal.release();
         }

      }

   }

   public static void dispose(MessageContext var0) {
      SCCredentialProactiveRequestor var1 = (SCCredentialProactiveRequestor)var0.getProperty("weblogic.wsee.security.wssc.sct.scCredentialProactiveRequestor");
      if (var1 != null) {
         var1.dispose();
      }
   }

   public void dispose() {
      if (this.tm != null) {
         try {
            this.tm.stop();
            this.tm = null;
         } catch (Throwable var2) {
            var2.printStackTrace();
            if (verbose) {
               Verbose.log((Object)"Fails to stop the thread for proactive async renewal");
               Verbose.logException(var2);
            }
         }

         if (verbose) {
            Verbose.log((Object)"SC credential proactive requestor is disposed");
         }

      }
   }

   public class Measure {
      private long startTime;

      private Measure() {
         this.startTime = -1L;
         this.startTime = System.currentTimeMillis();
      }

      public void terminate() {
         long var1 = System.currentTimeMillis() - this.startTime;
         SCCredentialProactiveRequestor.this.avgTimePeriod = SCCredentialProactiveRequestor.this.avgTimePeriod == -1L ? var1 : (SCCredentialProactiveRequestor.this.avgTimePeriod + var1) / 2L;
         if (SCCredentialProactiveRequestor.verbose) {
            Verbose.log((Object)("Proactive requestor calculates the average time period of exchanging SC: " + SCCredentialProactiveRequestor.this.avgTimePeriod / 1000L + " seconds, the current time period is: " + var1 / 1000L + " seconds."));
         }

      }

      // $FF: synthetic method
      Measure(Object var2) {
         this();
      }
   }

   private class RenewTimerListener implements TimerListener {
      private SCCredential sc = null;

      public RenewTimerListener(SCCredential var2) {
         this.sc = var2;
      }

      public void timerExpired(Timer var1) {
         boolean var2 = false;

         try {
            SCCredentialProactiveRequestor.this.proactiveRenewalSignal.acquire();
            var2 = true;
            WSTContext var3 = WSTContext.getWSTContext(SCCredentialProactiveRequestor.this.context);
            SCTokenHandler var4 = new SCTokenHandler();
            ClientSCCredentialProviderBase._renewCredential(SCCredentialProactiveRequestor.this, this.sc, var3, var4);
            synchronized(SCCredentialProactiveRequestor.this) {
               SCCredentialProviderBase.setSCToContext(SCCredentialProactiveRequestor.this.context, this.sc);
               String var6 = SCCredentialProviderBase.getPhysicalStoreNameFromMessageContext(var3.getMessageContext());
               SCTStore.addToClient(this.sc, !var3.isSessionPersisted(), var6);
            }

            if (SCCredentialProactiveRequestor.verbose) {
               Verbose.log((Object)"Proactive async renewal has completed this once");
            }
         } catch (InterruptedException var13) {
            var13.printStackTrace();
         } finally {
            if (var2) {
               SCCredentialProactiveRequestor.this.proactiveRenewalSignal.release();
            }

            SCCredentialProactiveRequestor.this.asyncRenewNext(this.sc, this);
         }

      }
   }
}
