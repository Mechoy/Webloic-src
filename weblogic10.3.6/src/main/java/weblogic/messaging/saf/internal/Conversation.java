package weblogic.messaging.saf.internal;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.store.SAFStore;

abstract class Conversation {
   protected boolean running;
   protected boolean poisoned;
   protected boolean ordered;
   protected SAFConversationInfo info;
   protected MessageReference firstMessage;
   protected MessageReference lastMessage;
   protected long timeToLive;
   private boolean seenLastMsg;
   private int msgCount;
   protected SAFStore store;
   protected SAFManagerImpl safManager;

   Conversation(SAFConversationInfo var1, SAFStore var2, SAFManagerImpl var3) {
      this.info = var1;
      this.store = var2;
      this.safManager = var3;
   }

   abstract void close() throws SAFException;

   protected void addMessageToList(MessageReference var1) {
      if (var1 != null) {
         if (this.firstMessage == null) {
            this.firstMessage = var1;
            this.lastMessage = var1;
         } else {
            this.lastMessage.setNext(var1);
            var1.setPrev(this.lastMessage);
            this.lastMessage = var1;
         }

         ++this.msgCount;
      }
   }

   protected void addMessageToListInorder(MessageReference var1) {
      if (var1 != null) {
         ++this.msgCount;
         if (this.firstMessage == null) {
            this.firstMessage = var1;
            this.lastMessage = var1;
         } else {
            MessageReference var2;
            for(var2 = this.firstMessage; var2 != null && var2.getSequenceNumber() <= var1.getSequenceNumber(); var2 = var2.getNext()) {
               if (var2.getSequenceNumber() == var1.getSequenceNumber()) {
                  return;
               }
            }

            if (var2 == null) {
               this.lastMessage.setNext(var1);
               var1.setPrev(this.lastMessage);
               this.lastMessage = var1;
            } else {
               var1.setNext(var2);
               var1.setPrev(var2.getPrev());
               if (var2.getPrev() != null) {
                  var2.getPrev().setNext(var1);
               }

               var2.setPrev(var1);
               if (var2 == this.firstMessage) {
                  this.firstMessage = var1;
               }

            }
         }
      }
   }

   protected void restoreMessages(MessageReference var1) {
      MessageReference var2;
      do {
         var2 = var1.getNext();
         var1.setNext((MessageReference)null);
         var1.setPrev((MessageReference)null);
         this.addMessageToListInorder(var1);
         var1 = var2;
      } while(var2 != null);

   }

   protected void removeMessageFromList(MessageReference var1) {
      if (var1 != null) {
         if (var1 == this.firstMessage) {
            this.firstMessage = var1.getNext();
            if (this.firstMessage != null) {
               this.firstMessage.setPrev((MessageReference)null);
            }

            if (var1 == this.lastMessage) {
               this.lastMessage = this.firstMessage;
            }
         } else {
            if (var1.getNext() != null) {
               var1.getNext().setPrev(var1.getPrev());
            }

            if (var1.getPrev() != null) {
               var1.getPrev().setNext(var1.getNext());
            }
         }

         --this.msgCount;
      }
   }

   final boolean isDone() {
      return this.firstMessage == null && this.seenLastMsg;
   }

   public SAFConversationInfo getInfo() {
      return this.info;
   }

   public String getName() {
      return this.info.getConversationName();
   }

   synchronized void setSeenLastMsg(boolean var1) {
      this.seenLastMsg = var1;
   }

   synchronized boolean hasSeenLastMsg() {
      return this.seenLastMsg;
   }

   public String getConversationName() {
      return this.info.getConversationName();
   }

   protected static boolean isPoisoned(int var0) {
      switch (var0) {
         case 6:
         case 8:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 17:
            return true;
         case 7:
         case 9:
         case 15:
         case 16:
         default:
            return false;
      }
   }

   public void dumpAttributes(SAFDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeAttribute("running", String.valueOf(this.running));
      var2.writeAttribute("poisoned", String.valueOf(this.poisoned));
      var2.writeAttribute("ordered", String.valueOf(this.ordered));
      var2.writeAttribute("timeToLive", String.valueOf(this.timeToLive));
      var2.writeAttribute("seenLastMessage", String.valueOf(this.seenLastMsg));
      var2.writeAttribute("msgCount", String.valueOf(this.msgCount));
      var2.writeAttribute("storeName", this.store != null ? this.store.getStoreName() : "");
   }
}
