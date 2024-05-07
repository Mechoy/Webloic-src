package weblogic.wsee.reliability2.store;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.sender.api.ConversationStatusCallback;
import weblogic.wsee.sender.api.Sender;
import weblogic.wsee.sender.api.SenderFactory;
import weblogic.wsee.sender.api.SendingServiceException;

public class SourceSequenceSenderFactory implements SenderFactory {
   private static final long serialVersionUID = 1L;
   private static final Logger LOGGER = Logger.getLogger(SourceSequenceSenderFactory.class.getName());
   private transient Map<String, SourceSequenceSender> _seqIdToSenderMap;
   private transient SourceSequenceMap _parentMap;
   private final List<String> _seqIdList;
   private final Object _seqMonitor = "SeqLock";

   private void writeObject(ObjectOutputStream var1) throws IOException {
      synchronized(this._seqMonitor) {
         var1.defaultWriteObject();
      }
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      this._seqIdToSenderMap = new HashMap();
      Iterator var2 = this._seqIdList.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         SourceSequence var4 = (SourceSequence)this._parentMap.get(var3);
         SourceSequenceSender var5 = new SourceSequenceSender(this, var4);
         this._seqIdToSenderMap.put(var3, var5);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SourceSequenceSenderFactory recreated sender for seq: " + var3 + ". Current sender count: " + this._seqIdToSenderMap.size());
         }
      }

   }

   SourceSequenceSenderFactory(SourceSequenceMap var1) {
      this._parentMap = var1;
      this._seqIdList = new LinkedList();
      this._seqIdToSenderMap = new HashMap();
   }

   void senderClosed(String var1) {
      synchronized(this._seqMonitor) {
         if (this._seqIdToSenderMap.remove(var1) != null && LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SourceSequenceSenderFactory removed sender for seq: " + var1 + ". Remaining sender count: " + this._seqIdToSenderMap.size());
         }

         this._seqIdList.remove(var1);
      }
   }

   void sequenceCreated(String var1) {
      SourceSequenceSender var2;
      synchronized(this._seqMonitor) {
         var2 = (SourceSequenceSender)this._seqIdToSenderMap.get(var1);
      }

      if (var2 != null) {
         var2.notifySequenceCreated();
      }

   }

   public Sender createSender(String var1) throws SendingServiceException {
      SourceSequenceSender var2;
      synchronized(this._seqMonitor) {
         var2 = (SourceSequenceSender)this._seqIdToSenderMap.get(var1);
      }

      if (var2 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SourceSequenceSenderFactory returning existing sender for seq: " + var1);
         }

         return var2;
      } else {
         SourceSequence var3 = (SourceSequence)this._parentMap.get(var1);
         var2 = new SourceSequenceSender(this, var3);
         synchronized(this._seqMonitor) {
            this._seqIdToSenderMap.put(var1, var2);
            this._seqIdList.add(var1);
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SourceSequenceSenderFactory created new sender for seq: " + var1);
         }

         return var2;
      }
   }

   public ConversationStatusCallback getStatusCallback(String var1) throws SendingServiceException {
      SourceSequenceSender var2;
      synchronized(this._seqMonitor) {
         var2 = (SourceSequenceSender)this._seqIdToSenderMap.get(var1);
      }

      return var2 != null ? var2.getConversationStatusCallback() : null;
   }
}
