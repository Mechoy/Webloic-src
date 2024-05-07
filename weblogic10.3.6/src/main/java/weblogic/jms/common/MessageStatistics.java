package weblogic.jms.common;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;

public final class MessageStatistics {
   private long messagesPendingCount;
   private long messagesSentCount;
   private long messagesReceivedCount;
   private long bytesPendingCount;
   private long bytesSentCount;
   private long bytesReceivedCount;

   public final synchronized long getMessagesPendingCount() {
      return this.messagesPendingCount;
   }

   public final synchronized long getMessagesReceivedCount() {
      return this.messagesReceivedCount;
   }

   public final synchronized long getMessagesSentCount() {
      return this.messagesSentCount;
   }

   public final synchronized long getBytesPendingCount() {
      return this.bytesPendingCount;
   }

   public final synchronized long getBytesReceivedCount() {
      return this.bytesReceivedCount;
   }

   public final synchronized long getBytesSentCount() {
      return this.bytesSentCount;
   }

   public final synchronized void incrementPendingCount(long var1) {
      ++this.messagesPendingCount;
      this.bytesPendingCount += var1;
   }

   public final void incrementPendingCount(MessageImpl var1) {
      long var2 = var1.getPayloadSize() + (long)var1.getUserPropertySize();
      synchronized(this) {
         ++this.messagesPendingCount;
         this.bytesPendingCount += var2;
      }
   }

   public final void decrementPendingCount(long var1) {
      synchronized(this) {
         --this.messagesPendingCount;
         this.bytesPendingCount -= var1;
      }
   }

   public final synchronized void incrementReceivedCount(long var1) {
      ++this.messagesReceivedCount;
      this.bytesReceivedCount += var1;
   }

   public final synchronized void incrementReceivedCount(MessageImpl var1) {
      ++this.messagesReceivedCount;
      this.bytesReceivedCount += var1.getPayloadSize();
      this.bytesReceivedCount += (long)var1.getUserPropertySize();
   }

   public final synchronized void incrementSentCount(long var1) {
      ++this.messagesSentCount;
      this.bytesSentCount += var1;
   }

   public final synchronized void incrementSentCount(MessageImpl var1) {
      ++this.messagesSentCount;
      this.bytesSentCount += var1.getPayloadSize();
      this.bytesSentCount += (long)var1.getUserPropertySize();
   }

   public void dump(JMSDiagnosticImageSource var1, XMLStreamWriter var2) throws XMLStreamException, DiagnosticImageTimeoutException {
      var1.checkTimeout();
      var2.writeStartElement("Statistics");
      var2.writeAttribute("messagesPendingCount", String.valueOf(this.messagesPendingCount));
      var2.writeAttribute("messagesSentCount", String.valueOf(this.messagesSentCount));
      var2.writeAttribute("messagesReceivedCount", String.valueOf(this.messagesReceivedCount));
      var2.writeAttribute("bytesPendingCount", String.valueOf(this.bytesPendingCount));
      var2.writeAttribute("bytesSentCount", String.valueOf(this.bytesSentCount));
      var2.writeAttribute("bytesReceivedCount", String.valueOf(this.bytesReceivedCount));
      var2.writeEndElement();
   }
}
