package weblogic.wsee.mc.processor;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.jaxws.persistence.PersistentMessage;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.mc.utils.McProtocolUtils;
import weblogic.wsee.persistence.Storable;

public class McPending implements Storable {
   private static final long serialVersionUID = 1L;
   private static final Logger LOGGER = Logger.getLogger(McPending.class.getName());
   private String _id;
   private String _address;
   private long _creationTimestamp;
   private transient ReentrantReadWriteLock _messageLock;
   private transient List<PersistentMessage> _messages;
   private List<Long> _timestamps;
   private transient AddressingVersion _addressingVersion;
   private transient SOAPVersion _soapVersion;
   private transient int _removalAttempts;
   private transient int _successfulRemovalAttempts;
   private boolean _expired;
   private transient String _logicalStoreName;
   private transient String _physicalStoreName;

   public McPending() {
   }

   private void initTransients() {
      this._messageLock = new ReentrantReadWriteLock();
      this._addressingVersion = AddressingVersion.W3C;
      this._soapVersion = SOAPVersion.SOAP_11;
      this._removalAttempts = 0;
      this._successfulRemovalAttempts = 0;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      var1.writeObject(this._addressingVersion.nsUri);
      var1.writeObject(this._soapVersion.nsUri);

      try {
         this._messageLock.writeLock().lock();
         var1.writeObject(this._messages);
         var1.writeObject(this._timestamps);
      } finally {
         this._messageLock.writeLock().unlock();
      }

      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.initTransients();
      var1.readObject();
      String var2 = (String)var1.readObject();
      this._addressingVersion = AddressingVersion.fromNsUri(var2);
      String var3 = (String)var1.readObject();
      this._soapVersion = SOAPVersion.fromNsUri(var3);

      try {
         this._messageLock.writeLock().lock();
         this._messages = (List)var1.readObject();
         this._timestamps = (List)var1.readObject();
      } finally {
         this._messageLock.writeLock().unlock();
      }

      var1.defaultReadObject();
   }

   public McPending(String var1, String var2, AddressingVersion var3, SOAPVersion var4) {
      this.initTransients();
      this._id = var1;
      this._logicalStoreName = var2;
      this._address = null;
      this._creationTimestamp = System.currentTimeMillis();
      this._addressingVersion = var3;
      this._soapVersion = var4;
      this._messages = new ArrayList();
      this._timestamps = new ArrayList();
      this._expired = false;
   }

   public String getId() {
      return this._id;
   }

   private ReentrantReadWriteLock getMessageLock() {
      return this._messageLock;
   }

   public int getRemovalAttempts() {
      return this._removalAttempts;
   }

   public int getSuccessfulRemovalAttempts() {
      return this._successfulRemovalAttempts;
   }

   public long getOldestTimestamp() {
      long var2;
      try {
         this.getMessageLock().readLock().lock();
         int var1 = this._timestamps.size();
         if (var1 == 0) {
            var2 = -1L;
            return var2;
         }

         var2 = (Long)this._timestamps.get(0);
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var2;
   }

   public long getNewestTimestamp() {
      long var2;
      try {
         this.getMessageLock().readLock().lock();
         int var1 = this._timestamps.size();
         if (var1 == 0) {
            var2 = -1L;
            return var2;
         }

         var2 = (Long)this._timestamps.get(var1 - 1);
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var2;
   }

   public Packet removeMessage(Packet var1) {
      Message var5;
      try {
         ++this._removalAttempts;
         PersistentMessageFactory var2 = PersistentMessageFactory.getInstance();
         this.getMessageLock().writeLock().lock();

         try {
            String var4;
            if (this._messages.isEmpty()) {
               var4 = null;
               return var4;
            }

            Packet var3 = var1.copy(false);
            var2.setMessageIntoPacket((PersistentMessage)this._messages.remove(0), var3);
            this._timestamps.remove(0);
            ++this._successfulRemovalAttempts;
            if (LOGGER.isLoggable(Level.FINE)) {
               var4 = null;
               var5 = var3.getMessage();
               if (var5 != null) {
                  var4 = var5.getID(this._addressingVersion, this._soapVersion);
               }

               LOGGER.fine("Message with id " + var4 + " removed from pending list " + McProtocolUtils.decodeId(this.getId()) + ", " + this._messages.size() + " message(s) now pending");
            }

            if (this.isEmpty()) {
               this._expired = true;
            }

            Packet var11 = var3;
            return var11;
         } catch (IndexOutOfBoundsException var9) {
            var5 = null;
         }
      } finally {
         this.getMessageLock().writeLock().unlock();
      }

      return var5;
   }

   public int size() {
      int var1;
      try {
         this.getMessageLock().readLock().lock();
         var1 = this._messages.size();
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var1;
   }

   public boolean isEmpty() {
      boolean var1;
      try {
         this.getMessageLock().readLock().lock();
         var1 = this._messages.isEmpty();
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var1;
   }

   public void addMessage(Packet var1) {
      this._expired = false;

      try {
         this.getMessageLock().writeLock().lock();
         String var2 = null;
         Message var3 = var1.getMessage();
         if (var3 != null) {
            var2 = var3.getID(this._addressingVersion, this._soapVersion);
         }

         Iterator var4 = this._messages.iterator();

         String var6;
         do {
            if (!var4.hasNext()) {
               this._messages.add(PersistentMessageFactory.getInstance().createMessageFromPacket(var2, var1));
               this._timestamps.add(System.currentTimeMillis());
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Message with id " + var2 + " added to pending list " + McProtocolUtils.decodeId(this.getId()) + ", " + this._messages.size() + " message(s) now pending");
               }

               return;
            }

            PersistentMessage var5 = (PersistentMessage)var4.next();
            var6 = var5.getMessage().getID(this._addressingVersion, this._soapVersion);
         } while(!var6.equals(var2));

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Message with id " + var2 + " already added to pending list " + McProtocolUtils.decodeId(this.getId()));
         }
      } finally {
         this.getMessageLock().writeLock().unlock();
      }

   }

   public String getAddress() {
      return this._address;
   }

   public void setAddress(String var1) {
      this._address = var1;
   }

   public long getCreationTimestamp() {
      return this._creationTimestamp;
   }

   public boolean isExpired() {
      return this._expired;
   }

   public boolean hasExplicitExpiration() {
      return true;
   }

   public String getLogicalStoreName() {
      return this._logicalStoreName;
   }

   public void setLogicalStoreName(String var1) {
      this._logicalStoreName = var1;
   }

   public String getPhysicalStoreName() {
      return this._physicalStoreName;
   }

   public void setPhysicalStoreName(String var1) {
      this._physicalStoreName = var1;
   }

   public Long getCreationTime() {
      return this._creationTimestamp;
   }

   public Long getLastUpdatedTime() {
      return this.getNewestTimestamp() > 0L ? this.getNewestTimestamp() : this.getCreationTimestamp();
   }

   public void touch() {
   }

   public Serializable getObjectId() {
      return this.getId();
   }
}
