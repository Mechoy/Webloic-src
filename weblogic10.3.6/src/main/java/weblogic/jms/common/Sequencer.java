package weblogic.jms.common;

import weblogic.jms.JMSService;
import weblogic.jms.dispatcher.Invocable;
import weblogic.jms.dispatcher.JMSDispatcher;
import weblogic.messaging.ID;
import weblogic.messaging.dispatcher.InvocableMonitor;
import weblogic.messaging.dispatcher.Request;

public final class Sequencer implements Invocable {
   private final PushTarget pushTarget;
   private final JMSDispatcher dispatcher;
   private final JMSID sequencerId;
   private final InvocableMonitor invocableMonitor;
   private long expectedSequenceNumber = 1L;
   private boolean running;
   private JMSPushRequest firstRequest;
   private JMSPushRequest lastRequest;

   public Sequencer(PushTarget var1, JMSDispatcher var2) {
      this.pushTarget = var1;
      this.dispatcher = var2;
      this.sequencerId = JMSService.getJMSService().getNextId();
      this.invocableMonitor = JMSService.getJMSService().getInvocableMonitor();
   }

   public Sequencer(PushTarget var1) {
      this.pushTarget = var1;
      this.dispatcher = null;
      this.sequencerId = null;
      this.invocableMonitor = null;
   }

   public JMSDispatcher getDispatcher() {
      return this.dispatcher;
   }

   public void changeExpectedSequenceNumberCanHaveRemainder(long var1) {
      synchronized(this) {
         if (this.expectedSequenceNumber >= var1) {
            return;
         }

         this.expectedSequenceNumber = var1;
         this.expungeBeforeSequenceNumber();
         if (this.running) {
            return;
         }

         this.running = true;
      }

      this.deliverMessages();
   }

   private void expungeBeforeSequenceNumber() {
      assert Thread.holdsLock(this);

      while(this.firstRequest != null) {
         if (this.firstRequest.getBackEndSequenceNumber() >= this.expectedSequenceNumber) {
            return;
         }

         JMSPushRequest var1 = this.firstRequest;
         this.firstRequest = (JMSPushRequest)var1.getNext();
         var1.setNext((Request)null);
      }

      if (this.firstRequest == null) {
         this.lastRequest = null;
      }

   }

   public final void pushMessage(JMSPushRequest var1) {
      synchronized(this) {
         JMSPushRequest var3 = var1;

         while(var3 != null) {
            JMSPushEntry var4 = var3.getFirstPushEntry().getNext();

            JMSPushEntry var5;
            JMSPushRequest var6;
            while(var4 != null) {
               var5 = var4;
               var4 = var4.getNext();
               var5.setNext((JMSPushEntry)null);
               var6 = new JMSPushRequest(13, this.pushTarget.getJMSID(), var3.getMessage(), var5);
               this.sequenceRequest(var6);
            }

            var5 = var3.getFirstPushEntry();
            var5.setNext((JMSPushEntry)null);
            var3.setLastPushEntry(var5);
            var3.setInvocableId(this.pushTarget.getJMSID());
            var6 = var3;
            var3 = (JMSPushRequest)var3.getNext();
            var6.setNext((Request)null);
            this.sequenceRequest(var6);
         }

         if (this.running) {
            return;
         }

         this.running = true;
      }

      this.deliverMessages();
   }

   private void sequenceRequest(JMSPushRequest var1) {
      assert Thread.holdsLock(this);

      assert var1.getNext() == null;

      assert var1.getFirstPushEntry().getNext() == null;

      long var2 = var1.getBackEndSequenceNumber();
      if (var2 >= this.expectedSequenceNumber) {
         if (this.firstRequest == null) {
            this.firstRequest = this.lastRequest = var1;
         } else if (this.lastRequest != null && var2 > this.lastRequest.getBackEndSequenceNumber()) {
            this.lastRequest.setNext(var1);
            this.lastRequest = var1;
         } else {
            JMSPushRequest var4 = this.firstRequest;

            JMSPushRequest var5;
            for(var5 = null; var4 != null && var2 >= var4.getBackEndSequenceNumber(); var4 = (JMSPushRequest)var4.getNext()) {
               var5 = var4;
            }

            if (var4 != null) {
               if (var4.getBackEndSequenceNumber() == var2) {
                  return;
               }

               var1.setNext(var4);
               if (var5 != null) {
                  var5.setNext(var1);
               } else {
                  this.firstRequest = var1;
               }
            } else {
               var5.setNext(var1);
               this.lastRequest = var1;
            }

         }
      }
   }

   private void deliverMessages() {
      JMSPushRequest var1 = null;

      while(true) {
         synchronized(this) {
            var1 = this.firstRequest;
            JMSPushRequest var3 = null;

            while(true) {
               if (this.firstRequest == null || this.firstRequest.getBackEndSequenceNumber() != this.expectedSequenceNumber) {
                  if (var3 == null) {
                     this.running = false;
                     return;
                  }

                  if (this.firstRequest == null) {
                     this.lastRequest = null;
                  } else {
                     var3.setNext((Request)null);
                  }
                  break;
               }

               this.firstRequest.getFirstPushEntry().setDispatcher(this.dispatcher);
               ++this.expectedSequenceNumber;
               var3 = this.firstRequest;
               this.firstRequest = (JMSPushRequest)this.firstRequest.getNext();
            }
         }

         this.pushTarget.pushMessage(var1);
      }
   }

   public JMSID getJMSID() {
      return this.sequencerId;
   }

   public ID getId() {
      return this.getJMSID();
   }

   public InvocableMonitor getInvocableMonitor() {
      return this.invocableMonitor;
   }

   public int invoke(Request var1) throws javax.jms.JMSException {
      switch (var1.getMethodId()) {
         case 15629:
            this.pushMessage((JMSPushRequest)var1);
            return Integer.MAX_VALUE;
         default:
            throw new JMSException("No such method " + var1.getMethodId());
      }
   }

   public synchronized String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append("[ ");
      if (this.firstRequest != null) {
         var1.append(" first=");
         var1.append(this.firstRequest.getBackEndSequenceNumber());
      }

      if (this.lastRequest != null) {
         var1.append(" last=");
         var1.append(this.lastRequest.getBackEndSequenceNumber());
      }

      int var2 = 0;

      for(JMSPushRequest var3 = this.firstRequest; var3 != null; var3 = (JMSPushRequest)var3.getNext()) {
         var1.append(' ');
         var1.append(var3.getBackEndSequenceNumber());
         var1.append(' ');
         ++var2;
      }

      var1.append(" pending=");
      var1.append(var2);
      var1.append(" expected=");
      var1.append(this.expectedSequenceNumber);
      var1.append(" ]");
      return var1.toString();
   }
}
