package weblogic.deploy.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import weblogic.deploy.common.Debug;
import weblogic.deploy.service.Deployment;
import weblogic.deploy.service.DeploymentFailureHandler;
import weblogic.deploy.service.DeploymentRequest;
import weblogic.management.runtime.DeploymentRequestTaskRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManagerFactory;
import weblogic.utils.StackTraceUtils;
import weblogic.work.WorkManagerFactory;

public class RequestImpl implements DeploymentRequest {
   protected List deployments = new ArrayList();
   protected long identifier;
   private long timeoutInterval = 120000L;
   private boolean callConfigurationProviderLast;
   private AuthenticatedSubject initiator;
   private transient Timer timeoutMonitor;
   private transient String timeoutIdentifier;
   private transient boolean startControl;
   private transient boolean isAControlRequest;
   private final transient HashSet failureListeners = new HashSet();

   protected RequestImpl() {
   }

   protected static final void debug(String var0) {
      Debug.serviceDebug(var0);
   }

   protected static final boolean isDebugEnabled() {
      return Debug.isServiceDebugEnabled();
   }

   public final long getId() {
      return this.identifier;
   }

   public final void addDeployment(Deployment var1) {
      synchronized(this.deployments) {
         if (var1 != null) {
            this.deployments.add(var1);
         } else if (isDebugEnabled()) {
            Debug.serviceDebug("Attempt to add an empty deployment " + StackTraceUtils.throwable2StackTrace((new Throwable()).fillInStackTrace()));
         }

      }
   }

   public Iterator getDeployments() {
      List var1;
      synchronized(this.deployments) {
         var1 = (List)((ArrayList)this.deployments).clone();
      }

      return var1.iterator();
   }

   public Iterator getDeployments(String var1) {
      ArrayList var2 = new ArrayList();
      synchronized(this.deployments) {
         Iterator var4 = this.deployments.iterator();

         while(var4.hasNext()) {
            Deployment var5 = (Deployment)var4.next();
            if (var1.equals(var5.getCallbackHandlerId())) {
               var2.add(var5);
            }
         }

         return var2.iterator();
      }
   }

   public DeploymentRequestTaskRuntimeMBean getTaskRuntime() {
      return null;
   }

   public final boolean isConfigurationProviderCalledLast() {
      return this.callConfigurationProviderLast;
   }

   public final void setCallConfigurationProviderLast() {
      this.callConfigurationProviderLast = true;
   }

   public final boolean isStartControlEnabled() {
      return this.startControl;
   }

   public final void setStartControl(boolean var1) {
      this.startControl = var1;
   }

   public boolean isControlRequest() {
      return this.isAControlRequest;
   }

   public void setControlRequest(boolean var1) {
      this.isAControlRequest = var1;
   }

   public final void registerFailureListener(DeploymentFailureHandler var1) {
      synchronized(this.failureListeners) {
         this.failureListeners.add(var1);
      }
   }

   public final Set getRegisteredFailureListeners() {
      synchronized(this.failureListeners) {
         return this.failureListeners;
      }
   }

   public final void setInitiator(AuthenticatedSubject var1) {
      if (this.initiator == null) {
         this.initiator = var1;
      }

   }

   public final AuthenticatedSubject getInitiator() {
      return this.initiator;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return var1 instanceof RequestImpl && this.identifier != 0L && ((RequestImpl)var1).getId() == this.identifier;
      }
   }

   public int hashCode() {
      return (int)this.identifier;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("DeploymentRequest: id: " + this.getId());
      Iterator var2 = this.getDeployments();
      int var3 = 1;

      while(var2.hasNext()) {
         Deployment var4 = (Deployment)var2.next();
         var1.append(" Deployment[");
         var1.append(var3);
         ++var3;
         var1.append("]: type: ");
         var1.append(var4.getCallbackHandlerId());
         var1.append(" , proposedVersion: ");
         var1.append(var4.getProposedVersion());
         var1.append(" , targets: ");
         String[] var5 = var4.getTargets();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            var1.append(var5[var6]);
            var1.append(" ");
         }
      }

      return var1.toString();
   }

   public final void setTimeoutInterval(long var1) {
      if (var1 > 120000L) {
         if (this.timeoutInterval <= 120000L) {
            this.timeoutInterval = var1;
         }
      }
   }

   public final long getTimeoutInterval() {
      return this.timeoutInterval;
   }

   public final void startTimeoutMonitor(String var1) {
      if (this.timeoutMonitor == null) {
         this.timeoutIdentifier = var1;
         this.timeoutMonitor = TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.deploy.RequestTimeout", WorkManagerFactory.getInstance().getSystem()).schedule(new TimeoutMonitor(), this.timeoutInterval);
         if (isDebugEnabled()) {
            debug("Starting timer '" + var1 + "' to expire in '" + this.timeoutInterval + "' ms at '" + new Date(System.currentTimeMillis() + this.timeoutInterval) + "'");
         }

      }
   }

   public final void cancelTimeoutMonitor() {
      try {
         if (this.timeoutMonitor != null) {
            if (isDebugEnabled()) {
               debug("Cancelling timeout monitor for '" + this.timeoutIdentifier + "'");
            }

            this.timeoutMonitor.cancel();
            this.timeoutMonitor = null;
         }
      } catch (Throwable var2) {
         if (isDebugEnabled()) {
            Debug.serviceDebug(var2.getMessage() + " " + StackTraceUtils.throwable2StackTrace(var2));
         }
      }

   }

   public void requestTimedout() {
      if (isDebugEnabled()) {
         debug(this.timeoutIdentifier + " timed out");
      }

   }

   private final class TimeoutMonitor implements TimerListener {
      private TimeoutMonitor() {
      }

      public final void timerExpired(Timer var1) {
         try {
            RequestImpl.this.requestTimedout();
         } catch (Throwable var3) {
            if (RequestImpl.isDebugEnabled()) {
               RequestImpl.debug("Time out for request id: " + RequestImpl.this.timeoutIdentifier);
            }
         }

      }

      // $FF: synthetic method
      TimeoutMonitor(Object var2) {
         this();
      }
   }
}
