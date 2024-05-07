package weblogic.jms.backend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jms.JMSException;
import weblogic.jms.JMSService;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSID;
import weblogic.jms.common.JMSSQLExpression;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDManager;
import weblogic.jms.dd.DDMember;
import weblogic.jms.dd.DDStatusListener;
import weblogic.messaging.kernel.Event;
import weblogic.messaging.kernel.EventListener;
import weblogic.messaging.kernel.GroupAddEvent;
import weblogic.messaging.kernel.GroupEvent;
import weblogic.messaging.kernel.GroupRemoveEvent;
import weblogic.messaging.kernel.Queue;
import weblogic.work.WorkManagerFactory;

public class TopicForwardingManager implements EventListener, DDStatusListener, ForwardingStatusListener {
   private Map forwardGroupCounters = new HashMap();
   private Map forwarders;
   private static final String SYSTEM_DIST_SUBSCRIBER_CLIENT = "WeblogicJmsDistributedTopic";
   DDHandler ddHandler;
   DDMember member;
   BEUOOTopicState beUOOTopicState;

   public TopicForwardingManager(DDHandler var1, DDMember var2, BEDestinationImpl var3) {
      this.ddHandler = var1;
      this.member = var2;
      var2.setIsForwardingUp(true);
      var1.addStatusListener(this, 19);
      if ("PathService".equals(var1.getUnitOfOrderRouting())) {
         this.beUOOTopicState = new BEUOOTopicState(var3, var1);
      }

      var3.setExtension(this.beUOOTopicState);
   }

   public void statusChangeNotification(DDHandler var1, int var2) {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("TopicForwardingManager.statusChangeNotification() " + var1.getName() + " events " + var2);
      }

      if ((var2 & 16) != 0) {
         this.deactivate();
      } else if ((var2 & 1) != 0 && var1.findMemberByName(this.member.getName()) == null) {
         this.deactivate();
      } else {
         this.refreshMembers();
         this.member.setIsForwardingUp(true);
      }
   }

   private void deactivate() {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Deactivate on topic forwarder for " + this.member.getName() + " within " + this.ddHandler.getName());
      }

      this.ddHandler.removeStatusListener(this);
      if (this.forwarders != null) {
         Iterator var1 = this.forwarders.values().iterator();

         while(var1.hasNext()) {
            Forwarder var2 = (Forwarder)var1.next();
            var2.deactivate();
         }

      }
   }

   private void refreshMembers() {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("Refreshing members for " + this.ddHandler.getName() + " with respect to " + this.member.getName() + ": this is " + this);
      }

      if (this.forwarders == null) {
         this.forwarders = new HashMap();
      }

      Iterator var1 = this.forwarders.values().iterator();

      while(var1.hasNext()) {
         Forwarder var2 = (Forwarder)var1.next();
         DDMember var3 = this.ddHandler.findMemberByName(var2.getName());
         if (var3 == null) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug(var2.getName() + " is no longer a member of " + this.ddHandler.getName() + " so I won't forward from " + this.member.getName());
            }

            var2.deactivate();
            var1.remove();
         }
      }

      var1 = this.ddHandler.memberCloneIterator();

      while(true) {
         while(true) {
            DDMember var6;
            Forwarder var7;
            do {
               do {
                  if (!var1.hasNext()) {
                     return;
                  }

                  var6 = (DDMember)var1.next();
                  if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
                     JMSDebug.JMSDistTopic.debug("Looking for match between forwardTo " + var6.getName() + " and member " + this.member.getName());
                  }
               } while(var6.getName().equals(this.member.getName()));

               var7 = (Forwarder)this.forwarders.get(var6.getName());
               if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
                  JMSDebug.JMSDistTopic.debug(" Found match, forwarder " + var7);
               }

               if (var7 != null) {
                  break;
               }

               try {
                  if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
                     JMSDebug.JMSDistTopic.debug("Creating a forwarder to " + var6.getName() + " from " + this.member.getName() + " within " + this.ddHandler.getName());
                  }

                  var7 = new Forwarder(var6);
                  this.forwarders.put(var6.getName(), var7);
               } catch (JMSException var5) {
               }
            } while(var7 == null);

            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug(" Forwarder info, isStarted " + var7.isStarted() + ", isUp " + var6.isUp());
            }

            if (!var7.isStarted() && var6.isUp()) {
               var7.start(var6);
            } else if (var7.isStarted() && !var6.isUp()) {
               var7.stop();
            }
         }
      }
   }

   public void onEvent(Event var1) {
      if (var1 instanceof GroupEvent) {
         boolean var2 = var1 instanceof GroupAddEvent;

         assert var2 ^ var1 instanceof GroupRemoveEvent;

         String var3 = ((GroupEvent)var1).getGroup().getName();
         synchronized(this) {
            Counter var5 = (Counter)this.forwardGroupCounters.get(var3);
            if (var2) {
               if (var5 != null) {
                  var5.increment();
                  return;
               }

               this.forwardGroupCounters.put(var3, new Counter(1));
            } else {
               if (var5 == null || var5.decrement() != 0) {
                  return;
               }

               this.forwardGroupCounters.remove(var3);
            }
         }

         if (this.beUOOTopicState != null) {
            if (var2) {
               this.beUOOTopicState.groupAddEvent(var3);
            } else {
               this.beUOOTopicState.groupRemoveEvent(var3);
            }
         }

      }
   }

   public String toString() {
      return "TopicForwardingManager: " + this.member.getName() + " within " + this.ddHandler.getName() + ", hash: " + this.hashCode();
   }

   private static String systemSubscriberName(String var0, String var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append("WeblogicJmsDistributedTopic");
      var2.append("@Remote@");
      var2.append(var0);
      var2.append("@Local@");
      var2.append(var1);
      return var2.toString();
   }

   public synchronized void forwardingFailed(BEForwardingConsumer var1) {
      if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
         JMSDebug.JMSDistTopic.debug("TopicForwardingManager.forwardingFailed() consumer " + var1);
      }

      WorkManagerFactory.getInstance().getSystem().schedule(new RefreshMembersThread());
   }

   protected final class Forwarder {
      private String name;
      private Queue subscriberQueue;
      private BEForwardingConsumer forwardingConsumer;
      private BETopicImpl topicImpl;

      Forwarder(DDMember var2) throws JMSException {
         this.name = var2.getName();
         String var3 = TopicForwardingManager.systemSubscriberName(this.name, TopicForwardingManager.this.member.getName());
         JMSSQLExpression var4 = new JMSSQLExpression((String)null, false, (JMSID)null, true);
         this.topicImpl = (BETopicImpl)TopicForwardingManager.this.member.getDestination();
         if (this.topicImpl == null) {
            this.stop();
            throw new JMSException("Error while creating a system subscriber, member has no valid destination");
         } else {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Creating new system subscriber queue " + var3 + " for forwarding to " + this.name + " from " + TopicForwardingManager.this.member.getName());
            }

            try {
               this.subscriberQueue = this.topicImpl.createSubscriptionQueue(var3, true);
               this.topicImpl.activateSubscriptionQueue(this.subscriberQueue, (BEConsumerImpl)null, var4, false, true);
            } catch (JMSException var6) {
               if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
                  JMSDebug.JMSDistTopic.debug("Exception while creating a system subscriber: " + var3, var6);
               }

               this.stop();
               throw var6;
            }

            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Created new system subscriber queue " + var3 + " for forwarding to " + this.name + " from " + TopicForwardingManager.this.member.getName());
            }

            JMSID var5 = JMSService.getJMSService().getNextId();
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Creating system subscriber " + this.name + " beUOOTopicState " + TopicForwardingManager.this.beUOOTopicState);
            }

            this.forwardingConsumer = new BEForwardingConsumer(TopicForwardingManager.this.member.getDestination().getBackEnd(), this.name, var5, this.subscriberQueue);
            if (TopicForwardingManager.this.beUOOTopicState != null) {
               BEDestinationImpl.addPropertyFlags(this.forwardingConsumer.getQueue(), "Logging", 16);
               this.forwardingConsumer.getQueue().addListener(TopicForwardingManager.this);
            } else {
               this.forwardingConsumer.setStatusListener(TopicForwardingManager.this);
            }

         }
      }

      boolean isStarted() {
         return this.forwardingConsumer.isStarted();
      }

      void start(DDMember var1) {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("Starting system subscriber to " + this.subscriberQueue.getName() + " for " + this.name + " from " + TopicForwardingManager.this.member.getName());
         }

         DistributedDestinationImpl var2 = DDManager.findDDImplByMemberName(this.name);
         if (var2 == null) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Cannot find the DDImpl for " + this.name + " to accept forwarding from " + TopicForwardingManager.this.member.getName());
            }

         } else {
            try {
               this.forwardingConsumer.start(var2, var2.getMemberName(), var1.getRemoteSecurityMode());
            } catch (JMSException var4) {
            }

         }
      }

      String getName() {
         return this.name;
      }

      synchronized void stop() {
         if (this.forwardingConsumer != null) {
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Stopping system subscriber to " + this.subscriberQueue.getName() + " for " + this.name + " from " + TopicForwardingManager.this.member.getName());
            }

            this.forwardingConsumer.stop();
         }

      }

      synchronized void deactivate() {
         this.stop();
         if (this.subscriberQueue != null) {
            DDMember var1 = TopicForwardingManager.this.member;
            if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
               JMSDebug.JMSDistTopic.debug("Permanently removing system subscriber to " + this.subscriberQueue.getName() + " for " + this.name + " from " + var1.getName());
            }

            try {
               this.topicImpl.unsubscribe(this.subscriberQueue, false);
            } catch (JMSException var3) {
               if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
                  JMSDebug.JMSDistTopic.debug("Exception while shutting down forwarder", var3);
               }
            }

            this.subscriberQueue = null;
         }

      }
   }

   private class RefreshMembersThread implements Runnable {
      private RefreshMembersThread() {
      }

      public void run() {
         if (JMSDebug.JMSDistTopic.isDebugEnabled()) {
            JMSDebug.JMSDistTopic.debug("TopicForwardingManager.RefreshMembersThread()");
         }

         TopicForwardingManager.this.refreshMembers();
         TopicForwardingManager.this.member.setIsForwardingUp(true);
      }

      // $FF: synthetic method
      RefreshMembersThread(Object var2) {
         this();
      }
   }

   public static final class Counter {
      private int value;

      public Counter(int var1) {
         this.value = var1;
      }

      public int increment() {
         return ++this.value;
      }

      public int decrement() {
         return --this.value;
      }
   }
}
