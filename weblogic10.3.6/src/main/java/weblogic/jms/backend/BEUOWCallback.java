package weblogic.jms.backend;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import weblogic.common.internal.PeerInfo;
import weblogic.jms.JMSService;
import weblogic.jms.common.BadSequenceNumberException;
import weblogic.jms.common.DuplicateSequenceNumberException;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.ObjectMessageImpl;
import weblogic.jms.common.OutOfSequenceRangeException;
import weblogic.messaging.Message;
import weblogic.messaging.kernel.KernelException;
import weblogic.messaging.kernel.UOWCallback;
import weblogic.messaging.kernel.UOWCallbackCaller;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.work.WorkManagerFactory;

public class BEUOWCallback implements UOWCallback, TimerListener {
   private ArrayList messages = new ArrayList();
   private int lastSequenceNumber = Integer.MAX_VALUE;
   private int numberVisible = 0;
   private ObjectMessageImpl oneBigMessage = null;
   private BEDestinationImpl dest = null;
   private UOWCallbackCaller caller;
   private Timer expirationTimer;
   private String name;
   private int oneBigMessageNumber = -1;
   private Set adminDeletedMessages = new HashSet();

   public BEUOWCallback(UOWCallbackCaller var1, String var2) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Creating BEUOWCallback(UOW) of name " + var1 + " on queue " + var2);
      }

      this.caller = var1;
      this.name = var2;
   }

   public synchronized Message newVisibleMessage(Message var1) {
      ++this.numberVisible;
      this.removeMarkerFromMessage(var1);
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Number visible in UOW " + this.caller + " is now " + this.numberVisible);
      }

      if (this.numberVisible != this.lastSequenceNumber + 1) {
         return null;
      } else {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("UOW complete");
         }

         MessageImpl var2 = (MessageImpl)this.messages.get(this.lastSequenceNumber);
         this.oneBigMessage = new ObjectMessageImpl();

         try {
            this.oneBigMessage.initializeFromMessage(var2);
            this.oneBigMessage.setObject(this.messages, PeerInfo.VERSION_920);
            this.oneBigMessage.setJMSDeliveryMode(1);
            this.oneBigMessage.setId(var2.getId());
         } catch (JMSException var4) {
            var4.printStackTrace();
         }

         return this.oneBigMessage;
      }
   }

   public void checkReplacement(Message var1, Message var2) {
      try {
         MessageImpl var3 = (MessageImpl)var1;
         MessageImpl var4 = (MessageImpl)var2;
         if (var4 instanceof TextMessage) {
            try {
               ((TextMessage)var4).getText();
            } catch (JMSException var7) {
            }
         }

         if (var3 instanceof TextMessage) {
            try {
               ((TextMessage)var3).getText();
            } catch (JMSException var6) {
            }
         }

         if (!var4.propertyExists("JMS_BEA_DeliveryFailureReason")) {
            return;
         }

         var3.setPropertiesWritable(true);
         var3.setIntProperty("JMS_BEA_DeliveryFailureReason", var4.getIntProperty("JMS_BEA_DeliveryFailureReason"));
         var3.setPropertiesWritable(false);
      } catch (JMSException var8) {
      }

   }

   public Message getOneBigMessageReplacee() {
      return this.oneBigMessageNumber == -1 ? null : (Message)this.messages.get(this.oneBigMessageNumber);
   }

   private void clean() {
      this.messages = new ArrayList();
      this.lastSequenceNumber = Integer.MAX_VALUE;
      this.numberVisible = 0;
      this.oneBigMessageNumber = -1;
      this.oneBigMessage = null;
      this.dest = null;
      this.caller = null;
      if (this.expirationTimer != null) {
         this.expirationTimer.cancel();
      }

      this.expirationTimer = null;
      this.adminDeletedMessages = new HashSet();
   }

   public synchronized void adminDeletedMessage(Message var1) {
      if (var1 instanceof TextMessage) {
         try {
            ((TextMessage)var1).getText();
         } catch (JMSException var3) {
         }
      }

      this.adminDeletedMessages.add(var1);
   }

   public synchronized boolean removeMessage(Message var1) {
      if (this.caller == null) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Caller is null, we've already cleaned up");
         }

         return true;
      } else {
         MessageImpl var2 = (MessageImpl)var1;
         if (this.adminDeletedMessages.contains(var2)) {
            this.adminDeletedMessages.remove(var2);
         } else if (var2 == this.getOneBigMessageReplacee()) {
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               if (var2 instanceof TextMessage) {
                  try {
                     ((TextMessage)var2).getText();
                  } catch (JMSException var4) {
                  }
               }

               JMSDebug.JMSBackEnd.debug("Removing one big message: " + var2 + " within UOW: " + this.caller + " on " + this.name);
            }

            this.clean();
            return true;
         }

         try {
            if (!var2.propertyExists("JMS_BEA_DeliveryFailureReason")) {
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  if (var2 instanceof TextMessage) {
                     ((TextMessage)var2).getText();
                  }

                  JMSDebug.JMSBackEnd.debug("No delivery failure reason on " + var2 + " within UOW: " + this.caller + " on " + this.name);
               }

               int var3 = var2.getIntProperty("JMS_BEA_UnitOfWorkSequenceNumber") - 1;
               --this.numberVisible;
               this.messages.set(var3, (Object)null);
               if (this.numberVisible == 0) {
                  if (this.expirationTimer != null) {
                     if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                        JMSDebug.JMSBackEnd.debug("Last message gone, cleaning up expiration timer");
                     }

                     this.expirationTimer.cancel();
                     this.expirationTimer = null;
                  }

                  this.clean();
                  return true;
               }

               if (this.expirationTimer == null) {
                  this.setWorkExpirationTimerIfNecessary();
               }

               this.oneBigMessageNumber = -1;
               this.oneBigMessage = null;
               return false;
            }
         } catch (JMSException var5) {
         }

         this.clean();
         return true;
      }
   }

   public synchronized void timerExpired(Timer var1) {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Incomplete work expiration timer fired for work: " + this.caller + " on " + this.name);
      }

      ListIterator var2 = this.messages.listIterator();

      while(var2.hasNext()) {
         MessageImpl var3 = (MessageImpl)var2.next();

         try {
            if (var3 != null) {
               var3.setPropertiesWritable(true);
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  if (var3 instanceof TextMessage) {
                     ((TextMessage)var3).getText();
                  }

                  JMSDebug.JMSBackEnd.debug("Setting delivery failure reason to WORK_EXPIRED on message: " + var3 + " within UOW: " + this.caller + " on " + this.name);
               }

               var3.setIntProperty("JMS_BEA_DeliveryFailureReason", 1);
               var3.setPropertiesWritable(false);
            }
         } catch (JMSException var5) {
         }
      }

      this.caller.expireAll();
   }

   private TimerManager getTimerManager() {
      return TimerManagerFactory.getTimerManagerFactory().getTimerManager("weblogic.jms.backend.BEUOWCallback", WorkManagerFactory.getInstance().getSystem());
   }

   private boolean allMessagesHaveArrived() {
      if (this.lastSequenceNumber == Integer.MAX_VALUE) {
         return false;
      } else {
         ListIterator var1 = this.messages.listIterator();

         Object var2;
         do {
            if (!var1.hasNext()) {
               return true;
            }

            var2 = var1.next();
         } while(var2 != null);

         return false;
      }
   }

   private void setWorkExpirationTimerIfNecessary() {
      if (this.dest == null) {
         this.dest = JMSService.getJMSService().getBEDeployer().findBEDestination(this.name);
      }

      if (this.dest == null) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Cannot find destination: " + this.name + " assuming that we're going down");
         }

      } else {
         if (this.dest.getIncompleteWorkExpirationTime() > 0) {
            long var1 = System.currentTimeMillis() + (long)this.dest.getIncompleteWorkExpirationTime();
            this.caller.setUserData(new Expiration(var1));
            this.expirationTimer = this.getTimerManager().schedule(this, (long)this.dest.getIncompleteWorkExpirationTime());
         }

      }
   }

   private void removeMarkerFromMessage(Message var1) {
      try {
         MessageImpl var2 = (MessageImpl)var1;
         if (!var2.propertyExists("JMS_BEA_OneBigMessageNumber")) {
            return;
         }

         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            if (var2 instanceof TextMessage) {
               ((TextMessage)var2).getText();
            }

            JMSDebug.JMSBackEnd.debug("Removing marker from in-memory copy of message");
         }

         var2.removeProperty("JMS_BEA_OneBigMessageNumber");
      } catch (JMSException var3) {
         var3.printStackTrace();
      }

   }

   private void makeMarkerMessage(MessageImpl var1, int var2) {
      if (this.lastSequenceNumber == 0) {
         this.oneBigMessageNumber = 0;
      } else {
         if (var2 == 0) {
            this.oneBigMessageNumber = 1;
         } else {
            this.oneBigMessageNumber = 0;
         }

         var1.setPropertiesWritable(true);

         try {
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               if (var1 instanceof TextMessage) {
                  ((TextMessage)var1).getText();
               }

               JMSDebug.JMSBackEnd.debug("Setting one big message to message number " + this.oneBigMessageNumber + ", the message is " + this.getOneBigMessageReplacee() + " within UOW: " + this.caller + " on " + this.name);
            }

            var1.setIntProperty("JMS_BEA_OneBigMessageNumber", this.oneBigMessageNumber);
         } catch (JMSException var4) {
            var4.printStackTrace();
         }

         var1.setPropertiesWritable(false);
      }
   }

   public synchronized boolean sendMessage(Message var1) throws KernelException {
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Message sent in UOW: " + var1);
      }

      MessageImpl var2 = (MessageImpl)var1;
      if (var2 == this.oneBigMessage) {
         return false;
      } else {
         try {
            int var3 = var2.getIntProperty("JMS_BEA_UnitOfWorkSequenceNumber") - 1;
            if (var3 < 0) {
               throw new KernelException("Dummy", new BadSequenceNumberException("A sequence number must be greater than or equal to zero"));
            }

            if (var3 > this.lastSequenceNumber) {
               throw new KernelException("Dummy", new OutOfSequenceRangeException("Normal message out of range"));
            }

            boolean var4 = var2.propertyExists("JMS_BEA_IsUnitOfWorkEnd") && var2.getBooleanProperty("JMS_BEA_IsUnitOfWorkEnd");
            if (var3 < this.messages.size() && this.messages.get(var3) != null) {
               if (var2.getSAFSequenceName() != null) {
                  return true;
               }

               throw new KernelException("Dummy", new DuplicateSequenceNumberException("Duplicate message"));
            }

            if (var4) {
               if (this.lastSequenceNumber != Integer.MAX_VALUE) {
                  throw new KernelException("Dummy", new DuplicateSequenceNumberException("Can't send two last messages"));
               }

               if (var3 < this.messages.size()) {
                  throw new KernelException("Dummy", new OutOfSequenceRangeException("End is too low"));
               }
            }

            while(this.messages.size() <= var3) {
               this.messages.add((Object)null);
            }

            this.messages.set(var3, var2);
            if (this.messages.size() == 1 && !this.allMessagesHaveArrived()) {
               this.setWorkExpirationTimerIfNecessary();
            }

            if (var4) {
               this.lastSequenceNumber = var3;
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("lastSequenceNumber set to " + this.lastSequenceNumber);
               }
            }

            if (this.expirationTimer != null && this.allMessagesHaveArrived()) {
               this.expirationTimer.cancel();
               this.expirationTimer = null;
            }

            if (this.allMessagesHaveArrived()) {
               this.makeMarkerMessage(var2, var3);
            }
         } catch (JMSException var5) {
            var5.printStackTrace();
         }

         return false;
      }
   }

   public synchronized void recoverMessage(Message var1) throws KernelException {
      MessageImpl var2 = (MessageImpl)var1;
      if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
         JMSDebug.JMSBackEnd.debug("Message recovered in UOW: " + var1);
      }

      try {
         int var3 = var2.getIntProperty("JMS_BEA_UnitOfWorkSequenceNumber") - 1;
         boolean var4 = var2.propertyExists("JMS_BEA_IsUnitOfWorkEnd") && var2.getBooleanProperty("JMS_BEA_IsUnitOfWorkEnd");

         while(this.messages.size() <= var3) {
            this.messages.add((Object)null);
         }

         this.messages.set(var3, var2);
         if (var4) {
            this.lastSequenceNumber = var3;
            if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("End message: lastSequenceNumber set to " + this.lastSequenceNumber);
            }
         }

         if (this.allMessagesHaveArrived()) {
            this.makeMarkerMessage(var2, var3);
         }

         if (var2.propertyExists("JMS_BEA_OneBigMessageNumber")) {
            this.oneBigMessageNumber = var2.getIntProperty("JMS_BEA_OneBigMessageNumber");
         }
      } catch (JMSException var5) {
         var5.printStackTrace();
      }

   }

   public void recoveryComplete() {
      if (this.caller.getUserData() != null) {
         if (!this.allMessagesHaveArrived()) {
            if (this.oneBigMessageNumber != -1) {
               this.caller.deleteAll();
            } else {
               Expiration var1 = (Expiration)this.caller.getUserData();
               long var2 = var1.getTime();
               long var4 = System.currentTimeMillis();
               if (var4 > var2) {
                  this.timerExpired((Timer)null);
               } else {
                  this.expirationTimer = this.getTimerManager().schedule(this, var2 - var4);
               }
            }
         }
      }
   }

   public static class Expiration implements Externalizable {
      long expirationTime;

      public Expiration() {
      }

      public Expiration(long var1) {
         this.expirationTime = var1;
      }

      public long getTime() {
         return this.expirationTime;
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         var1.writeLong(this.expirationTime);
      }

      public void readExternal(ObjectInput var1) throws IOException {
         this.expirationTime = var1.readLong();
      }
   }
}
