package weblogic.wsee.reliability2.sequence;

import com.sun.xml.ws.api.message.Packet;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class MessageInfo implements Serializable, Cloneable {
   private static final long serialVersionUID = 1L;
   private transient List<PropertyChangeListener> _listeners;
   private String _seqId;
   private long _msgNum;
   private long _timestamp;
   private boolean _ack;
   private String _msgId;
   private boolean _lastMessage;
   private boolean _emptyLastMessage;
   private String _soapAction;
   private transient Packet _requestPacket;
   private transient ReentrantReadWriteLock _lock;

   private void initTransients() {
      this._lock = new ReentrantReadWriteLock(false);
      this._listeners = new ArrayList();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      try {
         this._lock.readLock().lock();
         var1.writeObject("10.3.6");
         var1.defaultWriteObject();
      } finally {
         this._lock.readLock().unlock();
      }

   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      this.initTransients();

      try {
         this._lock.writeLock().lock();
         var1.readObject();
         var1.defaultReadObject();
      } finally {
         this._lock.writeLock().unlock();
      }

   }

   protected MessageInfo(String var1, String var2, long var3, String var5) {
      this._seqId = var1;
      this._msgId = var2;
      this._msgNum = var3;
      this._soapAction = var5;
      this.commonConstructorCode();
   }

   protected MessageInfo(MessageInfo var1) {
      this._seqId = var1._seqId;
      this._msgNum = var1._msgNum;
      this._timestamp = var1._timestamp;
      this._ack = var1._ack;
      this._msgId = var1._msgId;
      this._soapAction = var1._soapAction;
      this._lastMessage = var1._lastMessage;
      this._requestPacket = var1._requestPacket;
      this.commonConstructorCode();
   }

   private void commonConstructorCode() {
      this.initTransients();
   }

   public Object clone() throws CloneNotSupportedException {
      MessageInfo var1 = (MessageInfo)super.clone();
      var1._listeners.clear();
      return var1;
   }

   protected ReentrantReadWriteLock getLock() {
      return this._lock;
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      if (!this._listeners.contains(var1)) {
         this._listeners.add(var1);
      }

   }

   public boolean removePropertyChangeListener(PropertyChangeListener var1) {
      return this._listeners.remove(var1);
   }

   public String getSequenceId() {
      String var1;
      try {
         this._lock.readLock().lock();
         var1 = this._seqId;
      } finally {
         this._lock.readLock().unlock();
      }

      return var1;
   }

   public long getMessageNum() {
      long var1;
      try {
         this._lock.readLock().lock();
         var1 = this._msgNum;
      } finally {
         this._lock.readLock().unlock();
      }

      return var1;
   }

   public long getTimestamp() {
      return this._timestamp;
   }

   public void setTimestamp(long var1) {
      if (var1 != this._timestamp) {
         PropertyChangeEvent var3 = new PropertyChangeEvent(this, "timestamp", this._timestamp, var1);
         this._timestamp = var1;
         this.fireEvent(var3);
      }

   }

   protected void fireEvent(PropertyChangeEvent var1) {
      PropertyChangeListener[] var2;
      synchronized(this._listeners) {
         var2 = (PropertyChangeListener[])this._listeners.toArray(new PropertyChangeListener[this._listeners.size()]);
      }

      PropertyChangeListener[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PropertyChangeListener var6 = var3[var5];
         var6.propertyChange(var1);
      }

   }

   public String getMessageId() {
      String var1;
      try {
         this._lock.readLock().lock();
         var1 = this._msgId;
      } finally {
         this._lock.readLock().unlock();
      }

      return var1;
   }

   public String getSOAPAction() {
      String var1;
      try {
         this._lock.readLock().lock();
         var1 = this._soapAction;
      } finally {
         this._lock.readLock().unlock();
      }

      return var1;
   }

   public boolean isAck() {
      boolean var1;
      try {
         this._lock.readLock().lock();
         var1 = this._ack;
      } finally {
         this._lock.readLock().unlock();
      }

      return var1;
   }

   public void setAck(boolean var1) {
      PropertyChangeEvent var2 = null;

      try {
         this._lock.writeLock().lock();
         if (this._ack != var1) {
            var2 = new PropertyChangeEvent(this, "ack", this._ack, var1);
            this._ack = var1;
         }
      } finally {
         this._lock.writeLock().unlock();
      }

      if (var2 != null) {
         this.fireEvent(var2);
      }

   }

   public boolean isEmptyLastMessage() {
      boolean var1;
      try {
         this._lock.readLock().lock();
         var1 = this._emptyLastMessage;
      } finally {
         this._lock.readLock().unlock();
      }

      return var1;
   }

   public void setEmptyLastMessage(boolean var1) {
      PropertyChangeEvent var2 = null;

      try {
         this._lock.writeLock().lock();
         if (this._emptyLastMessage != var1) {
            var2 = new PropertyChangeEvent(this, "emptyLastMessage", this._emptyLastMessage, var1);
            this._emptyLastMessage = var1;
         }
      } finally {
         this._lock.writeLock().unlock();
      }

      if (var2 != null) {
         this.fireEvent(var2);
      }

   }

   public boolean isLastMessage() {
      boolean var1;
      try {
         this._lock.readLock().lock();
         var1 = this._lastMessage;
      } finally {
         this._lock.readLock().unlock();
      }

      return var1;
   }

   public void setLastMessage(boolean var1) {
      PropertyChangeEvent var2 = null;

      try {
         this._lock.writeLock().lock();
         if (this._lastMessage != var1) {
            var2 = new PropertyChangeEvent(this, "lastMessage", this._lastMessage, var1);
            this._lastMessage = var1;
         }
      } finally {
         this._lock.writeLock().unlock();
      }

      if (var2 != null) {
         this.fireEvent(var2);
      }

   }

   public Packet getRequestPacket() {
      Packet var1;
      try {
         this._lock.readLock().lock();
         var1 = this._requestPacket;
      } finally {
         this._lock.readLock().unlock();
      }

      return var1;
   }

   public void setRequestPacket(Packet var1) {
      try {
         this._lock.writeLock().lock();
         this._requestPacket = var1 != null ? var1.copy(false) : null;
         if (var1 != null) {
            this._requestPacket.setMessage(var1.getMessage());
         }
      } finally {
         this._lock.writeLock().unlock();
      }

   }
}
