package weblogic.jms.backend;

import java.util.Iterator;
import javax.jms.JMSException;
import weblogic.jms.JMSService;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDMember;
import weblogic.jms.dd.DDStatusListener;
import weblogic.timers.StopTimerListener;
import weblogic.timers.Timer;

public final class QueueForwardingManager implements StopTimerListener, DDStatusListener, ForwardingStatusListener {
   private long forwardingDelay;
   private Timer queueForwardingTimer;
   private DDMember forwardTo = null;
   private BEForwardingConsumer forwardingConsumer;
   private boolean forwardingTimerExpired = false;
   DDHandler ddHandler;
   DDMember member;

   public QueueForwardingManager(DDHandler var1, DDMember var2) {
      this.ddHandler = var1;
      this.member = var2;
      var2.setIsForwardingUp(true);
      var1.addStatusListener(this, 7);
   }

   public synchronized void statusChange() {
      boolean var1 = this.member.isDestinationUp() && !this.member.isConsumptionPaused() && !this.member.hasConsumers() && this.ddHandler.getForwardDelay() >= 0;
      if (var1) {
         if (this.forwardTo == null) {
            this.start();
         } else if (!this.forwardTo.hasConsumers() && !this.pickAndForward()) {
            this.start();
         }
      } else if (!var1 && this.forwardingConsumer != null) {
         this.stop();
      }

   }

   private synchronized boolean pickForwardTo() {
      this.forwardTo = null;
      Iterator var1 = this.ddHandler.memberCloneIterator();

      while(var1.hasNext()) {
         DDMember var2 = (DDMember)var1.next();
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("QueueForwardingManager.pickForwardTo() " + var2.getName() + " hasConsumer " + var2.hasConsumers() + " is up? " + var2.isUp());
         }

         if (!var2.getName().equals(this.member.getName()) && var2.hasConsumers() && !var2.isConsumptionPaused() && var2.isUp()) {
            if (var2.isLocal() && var2.isPersistent()) {
               this.forwardTo = var2;
               break;
            }

            if (this.forwardTo == null) {
               this.forwardTo = var2;
            } else if (!this.forwardTo.isPersistent() && (var2.isPersistent() || var2.isLocal())) {
               this.forwardTo = var2;
            }
         }
      }

      return this.forwardTo != null;
   }

   private synchronized void start() {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("QueueForwardingManager.start() forwardingConsumer " + this.forwardingConsumer + " ddHandler " + this.ddHandler + " resetDeliveryCount " + this.ddHandler.getResetDeliveryCountOnForward() + " queueForwardDelay " + this.ddHandler.getForwardDelay());
      }

      if (this.forwardingConsumer == null) {
         JMSID var1 = JMSService.getJMSService().getNextId();
         this.forwardingConsumer = new BEForwardingConsumer(this.member.getDestination().getBackEnd(), var1.toString(), var1, ((BEQueueImpl)this.member.getDestination()).getKernelQueue(), this.ddHandler.getResetDeliveryCountOnForward());
         this.forwardingConsumer.setStatusListener(this);
      }

      this.forwardingDelay = (long)this.ddHandler.getForwardDelay() * 1000L;
      if (this.forwardingTimerExpired) {
         this.pickAndForward();
      } else if (this.forwardingDelay == 0L) {
         this.forwardingTimerExpired = true;
         this.pickAndForward();
      } else {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Started dist queue forwarding timer for " + this.member.getName() + " to " + this.forwardingDelay);
         }

         this.queueForwardingTimer = this.member.getDestination().getBackEnd().getTimerManager().schedule(this, this.forwardingDelay, this.forwardingDelay);
      }
   }

   private synchronized boolean pickAndForward() {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Trying to find candidate for forwarding for " + this.member.getName());
      }

      if (this.pickForwardTo()) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Forwarding to " + this.forwardTo.getName() + " from " + this.member.getName());
         }

         try {
            this.forwardingConsumer.start(this.forwardTo.getDDImpl(), this.forwardTo.getName(), this.forwardTo.getRemoteSecurityMode());
            return true;
         } catch (JMSException var2) {
            this.forwardingFailed(this.forwardingConsumer);
            this.forwardTo = null;
            return false;
         }
      } else {
         return false;
      }
   }

   synchronized void stop() {
      this.forwardingTimerExpired = false;
      if (this.forwardingConsumer != null) {
         if (this.forwardTo != null) {
            this.forwardTo = null;
         }

         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Stopping dist queue forwarding for " + this.member.getName());
         }

         if (this.queueForwardingTimer != null) {
            this.queueForwardingTimer.cancel();
            this.queueForwardingTimer = null;
         }

         if (this.forwardingConsumer != null) {
            this.forwardingConsumer.stop();
            this.forwardingConsumer = null;
         }

      }
   }

   public synchronized void forwardingFailed(BEForwardingConsumer var1) {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("QueueForwardingManager.forwardingFailed() reschedule forwarder " + var1);
      }

      if (this.queueForwardingTimer != null) {
         this.queueForwardingTimer.cancel();
      }

      this.queueForwardingTimer = this.member.getDestination().getBackEnd().getTimerManager().schedule(this, this.forwardingDelay, this.forwardingDelay);
   }

   public synchronized void timerExpired(Timer var1) {
      if (this.forwardingConsumer != null) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Queue forwarding timer expired for " + this.member.getName());
         }

         this.forwardingTimerExpired = true;
         if (var1 != null) {
            var1.cancel();
         }

         this.pickAndForward();
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("No forwarding candidate found for " + this.member.getName() + ", an event will need to occur");
         }

      }
   }

   public void timerStopped(Timer var1) {
      this.stop();
   }

   public void statusChangeNotification(DDHandler var1, int var2) {
      if (var1.findMemberByName(this.member.getName()) == null) {
         var1.removeStatusListener(this);
         this.stop();
      } else {
         this.statusChange();
      }

   }

   public String toString() {
      return "QueueForwardingManager: " + this.member.getName() + " within " + this.ddHandler.getName() + ", hash: " + this.hashCode();
   }
}
