package weblogic.wsee.reliability2.saf;

import com.sun.istack.Nullable;
import com.sun.xml.ws.api.message.Packet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import weblogic.kernel.KernelStatus;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFRequest;
import weblogic.messaging.saf.common.SAFRequestImpl;
import weblogic.messaging.saf.internal.SAFManagerImpl;
import weblogic.store.PersistentStoreException;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.reliability2.sequence.MessageInfo;
import weblogic.wsee.reliability2.sequence.Sequence;

public abstract class SequenceSAFMap<S extends Sequence, M extends MessageInfo> implements Map<String, S> {
   private static final Logger LOGGER = Logger.getLogger(SequenceSAFMap.class.getName());
   protected SAFManagerImpl _safManager;

   @Nullable
   static <V extends Sequence> V getSequenceFromConversationInfo(SAFConversationInfo var0, Class<V> var1) {
      return getSequenceFromConversationInfo(var0, var1, true);
   }

   @Nullable
   static <V extends Sequence> V getSequenceFromConversationInfo(SAFConversationInfo var0, Class<V> var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else {
         try {
            Externalizable var3 = var0.getContext();
            if (var3 instanceof SequenceExternalizable) {
               SequenceExternalizable var4 = (SequenceExternalizable)var3;
               Sequence var5 = (Sequence)var4.getObj();
               if (var5.getPhysicalStoreName() == null) {
                  var5.setPhysicalStoreName(var4.getPhysicalStoreName());
               }

               if (var5.getLogicalStoreName() == null) {
                  var5.setLogicalStoreName(var4.getLogicalStoreName());
               }

               return var5;
            } else if (var2) {
               return null;
            } else {
               throw new RuntimeException("Programming error: SAF has called the JAX-WS RM SAF Transport with an invalid SAFConversationInfo containing context object: " + var0.getContext() + ". Expected object of class ExternalizableWrapper<" + var1 + ">");
            }
         } catch (Exception var6) {
            throw new RuntimeException(var6.toString(), var6);
         }
      }
   }

   static Serializable getPayloadFromSAFRequest(SAFRequest var0) {
      try {
         return ((ExternalizableWrapper)var0.getPayload()).getObj();
      } catch (Exception var2) {
         throw new RuntimeException(var2.toString(), var2);
      }
   }

   static MessageInfo getMessageInfoFromSAFRequest(SAFRequest var0) {
      try {
         return (MessageInfo)((ExternalizableWrapper)var0.getPayloadContext()).getObj();
      } catch (Exception var2) {
         throw new RuntimeException(var2.toString(), var2);
      }
   }

   static ReadWriteTimings getPersistentMessageTimingsFromSAFRequest(SAFRequest var0) {
      ExternalizableWrapper var1 = (ExternalizableWrapper)var0.getPayload();
      ReadWriteTimings var2 = new ReadWriteTimings();
      var2.millisSinceLastReadObject = var1.millisSinceLastReadObject();
      var2.millisSinceLastWriteObject = var1.millisSinceLastWriteObject();
      return var2;
   }

   protected SequenceSAFMap() throws NamingException, PersistentStoreException {
      this.initSAFManager();
   }

   private void initSAFManager() throws NamingException, PersistentStoreException {
      if (KernelStatus.isServer()) {
         this._safManager = (SAFManagerImpl)SAFManagerImpl.getManager();
      } else {
         this._safManager = null;
      }

   }

   public void recover() throws StoreException {
   }

   protected SAFRequest createSAFRequest(S var1, M var2) throws IOException {
      Packet var3 = var2.getRequestPacket();
      if (var3 == null) {
         return null;
      } else {
         SAFRequestImpl var4 = new SAFRequestImpl();
         var4.setConversationName(var1.getId());
         var4.setSequenceNumber(var2.getMessageNum());
         var4.setDeliveryMode(2);
         var4.setTimeToLive(0L);
         var4.setTimestamp(System.currentTimeMillis());
         var4.setEndOfConversation(false);
         if (var2.getMessageId() != null) {
            var4.setMessageId(var2.getMessageId());
         }

         Externalizable var5 = this.createSAFRequestPayload(var3, var2);
         var4.setPayload(var5);
         ExternalizableWrapper var6 = new ExternalizableWrapper(var2, false);
         var4.setPayloadContext(var6);
         return var4;
      }
   }

   protected abstract Externalizable createSAFRequestPayload(Packet var1, M var2);

   protected static class ExternalizableWrapper<T extends Serializable> implements Externalizable {
      private static final long serialVersionUID = 1L;
      private T _obj;
      private byte[] _bytes;
      private boolean _repeatSerializeAllowed;
      private transient long _lastReadObjectTime;
      private transient long _lastWriteObjectTime;

      public ExternalizableWrapper() {
         if (SequenceSAFMap.LOGGER.isLoggable(Level.FINE)) {
            SequenceSAFMap.LOGGER.fine("Deserialized ExternalizableWrapper: " + this);
         }

      }

      public ExternalizableWrapper(T var1, boolean var2) {
         this._obj = var1;
         this._repeatSerializeAllowed = var2;
         if (SequenceSAFMap.LOGGER.isLoggable(Level.FINE)) {
            SequenceSAFMap.LOGGER.fine("Created new ExternalizableWrapper " + this);
         }

      }

      public T getObj() {
         if (this._obj == null) {
            try {
               return this.getObjFromBytes();
            } catch (Exception var2) {
               throw new RuntimeException(var2.toString(), var2);
            }
         } else {
            return this._obj;
         }
      }

      private T getObjFromBytes() throws ClassNotFoundException, IOException {
         if (SequenceSAFMap.LOGGER.isLoggable(Level.FINE)) {
            SequenceSAFMap.LOGGER.fine("Getting object from bytes in ExternalizableWrapper " + this);
         }

         try {
            ByteArrayInputStream var1 = new ByteArrayInputStream(this._bytes);
            ObjectInputStream var2 = new ObjectInputStream(var1);
            Serializable var3 = (Serializable)var2.readObject();
            if (this._repeatSerializeAllowed) {
               this._bytes = null;
               this._obj = var3;
            }

            this._lastReadObjectTime = System.currentTimeMillis();
            return var3;
         } catch (IOException var4) {
            if (SequenceSAFMap.LOGGER.isLoggable(Level.SEVERE)) {
               SequenceSAFMap.LOGGER.log(Level.SEVERE, var4.toString(), var4);
            }

            throw var4;
         }
      }

      public long millisSinceLastReadObject() {
         return System.currentTimeMillis() - this._lastReadObjectTime;
      }

      public long millisSinceLastWriteObject() {
         return System.currentTimeMillis() - this._lastWriteObjectTime;
      }

      public byte[] getBytes() throws IOException {
         if (this._bytes != null) {
            return this._bytes;
         } else {
            try {
               ByteArrayOutputStream var1 = new ByteArrayOutputStream();
               ObjectOutputStream var2 = new ObjectOutputStream(var1);
               var2.writeObject(this._obj);
               var2.flush();
               var1.flush();
               byte[] var3 = var1.toByteArray();
               if (!this._repeatSerializeAllowed) {
                  this._obj = null;
                  this._bytes = var3;
               }

               this._lastWriteObjectTime = System.currentTimeMillis();
               return var3;
            } catch (IOException var4) {
               if (SequenceSAFMap.LOGGER.isLoggable(Level.SEVERE)) {
                  SequenceSAFMap.LOGGER.log(Level.SEVERE, var4.toString(), var4);
               }

               throw var4;
            }
         }
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         var1.writeBoolean(this._repeatSerializeAllowed);
         byte[] var2 = this.getBytes();
         var1.writeInt(var2.length);
         var1.write(var2);
         if (SequenceSAFMap.LOGGER.isLoggable(Level.FINE)) {
            SequenceSAFMap.LOGGER.fine("Serialized ExternalizableWrapper " + this);
         }

      }

      public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         this._repeatSerializeAllowed = var1.readBoolean();
         int var2 = var1.readInt();
         this._bytes = new byte[var2];
         var1.readFully(this._bytes);
         if (SequenceSAFMap.LOGGER.isLoggable(Level.FINE)) {
            SequenceSAFMap.LOGGER.fine("Deserialized ExternalizableWrapper " + this);
         }

      }

      public String toString() {
         StringBuffer var1 = new StringBuffer(super.toString());
         var1.append(" bytes[").append(this._bytes != null ? this._bytes.length : 0).append("]");
         var1.append(" obj: ").append(this._obj);
         var1.append(", repeatSerializeAllowed=").append(this._repeatSerializeAllowed);
         return var1.toString();
      }
   }

   protected static class SequenceExternalizable<T extends Sequence> extends ExternalizableWrapper<T> {
      private static final long serialVersionUID = 1L;
      private String _seqId;
      private String _physicalStoreName;
      private String _logicalStoreName;

      public SequenceExternalizable() {
      }

      public SequenceExternalizable(T var1, boolean var2) {
         super(var1, var2);
      }

      public String getSeqId() {
         return this._seqId;
      }

      public String getPhysicalStoreName() {
         return this._physicalStoreName;
      }

      public String getLogicalStoreName() {
         return this._logicalStoreName;
      }

      private void grabDataFromSeq(T var1) {
         if (var1.getId() == null) {
            this._seqId = "Standin-" + WsUtil.generateUUID();
         } else {
            this._seqId = var1.getId();
         }

         if (var1.getPhysicalStoreName() != null) {
            this._physicalStoreName = var1.getPhysicalStoreName();
         }

         if (var1.getLogicalStoreName() != null) {
            this._logicalStoreName = var1.getLogicalStoreName();
         }

      }

      public String toString() {
         StringBuffer var1 = new StringBuffer(super.toString());
         var1.append(" - ").append(this._seqId);
         return var1.toString();
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         if (this._seqId == null && SequenceSAFMap.LOGGER.isLoggable(Level.FINE)) {
            SequenceSAFMap.LOGGER.fine("ExternalizableWrapper grabbing seq ID: " + this);
         }

         this.grabDataFromSeq((Sequence)this.getObj());
         var1.writeObject(this._seqId);
         var1.writeObject(this._physicalStoreName);
         var1.writeObject(this._logicalStoreName);
         super.writeExternal(var1);
      }

      public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         this._seqId = (String)var1.readObject();
         this._physicalStoreName = (String)var1.readObject();
         this._logicalStoreName = (String)var1.readObject();
         super.readExternal(var1);
      }
   }

   static class ReadWriteTimings {
      long millisSinceLastReadObject;
      long millisSinceLastWriteObject;
   }
}
