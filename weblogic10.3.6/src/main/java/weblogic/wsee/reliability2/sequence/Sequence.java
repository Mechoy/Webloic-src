package weblogic.wsee.reliability2.sequence;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Message;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import weblogic.wsee.persistence.Storable;
import weblogic.wsee.reliability.MessageRange;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.reliability2.api.SequenceState;
import weblogic.wsee.reliability2.compat.Rpc2WsUtil;

public abstract class Sequence<M extends MessageInfo> implements Storable {
   private static final long serialVersionUID = 1L;
   private static final Logger LOGGER = Logger.getLogger(Sequence.class.getName());
   private static final boolean LOCK_FAIRNESS = false;
   private transient ReentrantReadWriteLock _objectLock;
   private transient boolean _changed;
   private long _lastActivityTime;
   private String _id;
   private String _type;
   private WsrmConstants.RMVersion _rmVersion;
   private transient String _logicalStoreName;
   private transient String _physicalStoreName;
   private transient boolean _active;
   private WsrmSecurityContext _securityCtx;
   @NotNull
   private Duration _expires;
   private long _timestamp;
   @Nullable
   private Duration _idleTimeout;
   @NotNull
   private WsrmConstants.IncompleteSequenceBehavior _incompleteSequenceBehavior;
   @NotNull
   private DeliveryAssurance _deliveryAssurance;
   private transient ReentrantReadWriteLock _messageLock;
   private transient long _maxMessageNum;
   private transient long _finalMessageNum;
   private transient long _unackdCount;
   private transient Map<Long, M> _requests;
   private transient Sequence<M>.RequestListener _requestListener;
   private transient List<M> _pendingRequests;
   private transient ReentrantReadWriteLock _stateLock;
   private transient SequenceState _state;
   private transient SequenceState _previousState;
   private transient ReentrantReadWriteLock _piggybackLock;
   private transient boolean _piggybackAck;
   private transient boolean _piggybackAckRequest;
   @NotNull
   private transient List<WsrmHeader> _piggybackHeaders;
   private transient AddressingVersion _addressingVersion;
   private transient SOAPVersion _soapVersion;
   private boolean _nonBuffered;
   private String _createSeqMsgId;
   private boolean _usingSsl;
   private transient boolean _masterInstance;
   private transient List<PropertyChangeListener> _listeners;

   public abstract M copyMessageInfo(M var1);

   private void initTransients() {
      this._objectLock = new ReentrantReadWriteLock(false);
      this._changed = false;
      this._messageLock = new ReentrantReadWriteLock(false);
      this._stateLock = new ReentrantReadWriteLock(false);
      this._requestListener = new RequestListener();
      this._pendingRequests = new ArrayList(1);
      this._piggybackLock = new ReentrantReadWriteLock(false);
      this._piggybackHeaders = new ArrayList();
      this._addressingVersion = AddressingVersion.W3C;
      this._soapVersion = SOAPVersion.SOAP_11;
      this._listeners = new ArrayList();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      var1.writeObject(this._addressingVersion.nsUri);
      var1.writeObject(this._soapVersion.nsUri);

      try {
         this._objectLock.writeLock().lock();

         try {
            this._messageLock.readLock().lock();
            var1.writeLong(this._lastActivityTime);
            var1.writeLong(this._maxMessageNum);
            var1.writeLong(this._finalMessageNum);
            var1.writeLong(this._unackdCount);
            var1.writeObject(this._requests);
         } finally {
            this._messageLock.readLock().unlock();
         }

         try {
            this._stateLock.readLock().lock();
            var1.writeObject(this._state);
            var1.writeObject(this._previousState);
         } finally {
            this._stateLock.readLock().unlock();
         }

         try {
            this._piggybackLock.readLock().lock();
            var1.writeBoolean(this._piggybackAck);
            var1.writeBoolean(this._piggybackAckRequest);
            ArrayList var2 = new ArrayList(this._piggybackHeaders);
            Rpc2WsUtil.serializeHeaderList(var2, var1);
         } finally {
            this._piggybackLock.readLock().unlock();
         }

         this._changed = false;
      } finally {
         this._objectLock.writeLock().unlock();
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
      this._lastActivityTime = var1.readLong();

      try {
         this._messageLock.writeLock().lock();
         this._maxMessageNum = var1.readLong();
         this._finalMessageNum = var1.readLong();
         this._unackdCount = var1.readLong();
         this._requests = (Map)var1.readObject();
         Iterator var4 = this._requests.values().iterator();

         while(var4.hasNext()) {
            MessageInfo var5 = (MessageInfo)var4.next();
            var5.addPropertyChangeListener(this._requestListener);
         }
      } finally {
         this._messageLock.writeLock().unlock();
      }

      try {
         this._stateLock.writeLock().lock();
         this._state = (SequenceState)var1.readObject();
         this._previousState = (SequenceState)var1.readObject();
      } finally {
         this._stateLock.writeLock().unlock();
      }

      try {
         this._piggybackLock.writeLock().lock();
         this._piggybackAck = var1.readBoolean();
         this._piggybackAckRequest = var1.readBoolean();
         List var21 = Rpc2WsUtil.deserializeHeaderList(var1, this._soapVersion);
         Iterator var22 = var21.iterator();

         while(var22.hasNext()) {
            Header var6 = (Header)var22.next();
            if (var6 instanceof WsrmHeader) {
               this._piggybackHeaders.add((WsrmHeader)var6);
            }
         }
      } finally {
         this._piggybackLock.writeLock().unlock();
      }

      var1.defaultReadObject();
   }

   protected Sequence(String var1, String var2, WsrmConstants.RMVersion var3, AddressingVersion var4, SOAPVersion var5, WsrmSecurityContext var6, boolean var7) {
      this.initTransients();
      this._logicalStoreName = var2;
      this._id = var1;
      this.parseType();
      this._rmVersion = var3;
      this._addressingVersion = var4;
      this._soapVersion = var5;
      this._securityCtx = var6;

      try {
         this._expires = DatatypeFactory.newInstance().newDuration("P1D");
         this._timestamp = System.currentTimeMillis();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

      this._incompleteSequenceBehavior = WsrmConstants.IncompleteSequenceBehavior.NoDiscard;
      this._deliveryAssurance = new DeliveryAssurance();
      this._state = SequenceState.NEW;
      this._requests = new TreeMap();
      this._finalMessageNum = -1L;
      this._unackdCount = 0L;
      this._nonBuffered = var7;
      this._changed = true;
      this._lastActivityTime = this._timestamp;
   }

   private void parseType() {
      String var1 = this.getClass().getName();
      int var2 = var1.lastIndexOf(46);
      this._type = var1.substring(var2 + 1);
   }

   public boolean isChanged() {
      boolean var1;
      try {
         this._objectLock.readLock().lock();
         var1 = this._changed;
      } finally {
         this._objectLock.readLock().unlock();
      }

      return var1;
   }

   protected void markChanged() {
      boolean var1 = false;

      try {
         if (this._messageLock.isWriteLocked() && this._messageLock.isWriteLockedByCurrentThread() || this._stateLock.isWriteLocked() && this._stateLock.isWriteLockedByCurrentThread() || this._piggybackLock.isWriteLocked() && this._piggybackLock.isWriteLockedByCurrentThread()) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Bypassing markChanged for " + this + " because one of our other sub-locks is already locked for write");
            }

            return;
         }

         this._objectLock.writeLock().lock();
         var1 = true;
         this._changed = true;
         this._lastActivityTime = System.currentTimeMillis();
      } finally {
         if (var1) {
            this._objectLock.writeLock().unlock();
         }

      }

   }

   public long getLastActivityTime() {
      long var1;
      try {
         this._objectLock.readLock().lock();
         var1 = this._lastActivityTime;
      } finally {
         this._objectLock.readLock().unlock();
      }

      return var1;
   }

   public String getId() {
      return this._id;
   }

   public Serializable getObjectId() {
      return this.getId();
   }

   public boolean isExpired() {
      return this._expires != null && this._expires.getTimeInMillis(new Date(this._timestamp)) > System.currentTimeMillis();
   }

   public boolean hasExplicitExpiration() {
      return this._expires != null;
   }

   public String getPhysicalStoreName() {
      return this._physicalStoreName;
   }

   public void setPhysicalStoreName(String var1) {
      this._physicalStoreName = var1;
   }

   public Long getCreationTime() {
      return this._timestamp;
   }

   public Long getLastUpdatedTime() {
      return this._lastActivityTime;
   }

   public void touch() {
      this.markChanged();
   }

   public void setId(String var1) {
      this._id = var1;
   }

   public WsrmConstants.RMVersion getRmVersion() {
      return this._rmVersion;
   }

   public String getLogicalStoreName() {
      return this._logicalStoreName;
   }

   public void setLogicalStoreName(String var1) {
      this._logicalStoreName = var1;
   }

   public String getCreateSequenceMsgId() {
      return this._createSeqMsgId;
   }

   public void setCreateSequenceMsgId(String var1) {
      this._createSeqMsgId = var1;
      this.markChanged();
   }

   public boolean isUsingSsl() {
      return this._usingSsl;
   }

   public void setUsingSsl(boolean var1) {
      this._usingSsl = var1;
   }

   protected abstract WSEndpointReference getPiggybackEpr();

   public WsrmSecurityContext getSecurityContext() {
      return this._securityCtx;
   }

   void setSecurityContext(WsrmSecurityContext var1) {
      this._securityCtx = var1;
   }

   public Duration getExpires() {
      return this._expires;
   }

   public long getTimestamp() {
      return this._timestamp;
   }

   public Duration getIdleTimeout() {
      return this._idleTimeout;
   }

   public WsrmConstants.IncompleteSequenceBehavior getIncompleteSequenceBehavior() {
      return this._incompleteSequenceBehavior;
   }

   public DeliveryAssurance getDeliveryAssurance() {
      return this._deliveryAssurance;
   }

   protected ReentrantReadWriteLock getMessageLock() {
      return this._messageLock;
   }

   public long getMaxMessageNum() {
      long var1;
      try {
         this.getMessageLock().readLock().lock();
         var1 = this._maxMessageNum;
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var1;
   }

   public boolean hasFinalMessageNum() {
      return this.getFinalMessageNum() > 0L;
   }

   public long getFinalMessageNum() {
      long var1;
      try {
         this.getMessageLock().readLock().lock();
         var1 = this._finalMessageNum;
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var1;
   }

   public long getUnackdCount() {
      long var1;
      try {
         this.getMessageLock().readLock().lock();
         var1 = this._unackdCount;
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var1;
   }

   protected ReentrantReadWriteLock getStateLock() {
      return this._stateLock;
   }

   public SequenceState getState() {
      SequenceState var1;
      try {
         this.getStateLock().readLock().lock();
         var1 = this._state;
      } finally {
         this.getStateLock().readLock().unlock();
      }

      return var1;
   }

   public SequenceState getPreviousState() {
      SequenceState var1;
      try {
         this.getStateLock().readLock().lock();
         var1 = this._previousState;
      } finally {
         this.getStateLock().readLock().unlock();
      }

      return var1;
   }

   public List<M> getAndClearPendingRequests() {
      boolean var1 = false;

      ArrayList var3;
      try {
         this.getMessageLock().writeLock().lock();
         ArrayList var2 = new ArrayList(this._pendingRequests);
         this._pendingRequests.clear();
         if (var2.size() > 0) {
            var1 = true;
         }

         var3 = var2;
      } finally {
         this.getMessageLock().writeLock().unlock();
         if (var1) {
            this.markChanged();
         }

      }

      return var3;
   }

   public Map<Long, M> getRequests() {
      TreeMap var1;
      try {
         this.getMessageLock().readLock().lock();
         var1 = new TreeMap(this._requests);
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var1;
   }

   public M getRequest(long var1) {
      MessageInfo var3;
      try {
         this.getMessageLock().readLock().lock();
         var3 = (MessageInfo)this._requests.get(var1);
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var3;
   }

   public M getRequestByMessageId(String var1) {
      MessageInfo var4;
      try {
         this.getMessageLock().readLock().lock();
         Iterator var2 = this._requests.values().iterator();

         MessageInfo var3;
         do {
            if (!var2.hasNext()) {
               var2 = null;
               return var2;
            }

            var3 = (MessageInfo)var2.next();
         } while(!var3.getMessageId().equals(var1));

         var4 = var3;
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var4;
   }

   public AddResult addRequest(M var1) {
      AddResult var2;
      try {
         this.getMessageLock().writeLock().lock();
         var2 = this.addRequestInternal(var1);
      } finally {
         this.getMessageLock().writeLock().unlock();
         this.markChanged();
      }

      return var2;
   }

   protected AddResult addRequestInternal(M var1) {
      AddResult var2 = new AddResult();
      var2.added = true;
      var2.notAddedReason = null;

      try {
         this.getMessageLock().writeLock().lock();
         var1.setTimestamp(System.currentTimeMillis());
         MessageInfo var3 = (MessageInfo)this._requests.put(var1.getMessageNum(), var1);
         if (var3 != null && !var3.isAck()) {
            this.setUnackedCount(this._unackdCount - 1L);
         }

         if (!var1.isAck()) {
            this.setUnackedCount(this._unackdCount + 1L);
         }

         this._pendingRequests.add(this.copyMessageInfo(var1));
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("*** Message number " + var1.getMessageNum() + (var3 != null ? " replaced within " : " added to ") + this._type + " " + this.getId());
         }

         if (var1.getMessageNum() > this._maxMessageNum) {
            this._maxMessageNum = var1.getMessageNum();
         }
      } finally {
         this.getMessageLock().writeLock().unlock();
         var1.addPropertyChangeListener(this._requestListener);
      }

      return var2;
   }

   protected void setUnackedCount(long var1) {
      this._unackdCount = var1;
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Unack'd count for seq " + this + " has been set to: " + var1);
      }

   }

   protected ReentrantReadWriteLock getPiggybackLock() {
      return this._piggybackLock;
   }

   public boolean hasPiggybackHeaders() {
      boolean var1;
      try {
         this._piggybackLock.readLock().lock();
         var1 = this._piggybackAck || this._piggybackAckRequest || !this._piggybackHeaders.isEmpty();
      } finally {
         this._piggybackLock.readLock().unlock();
      }

      return var1;
   }

   protected boolean flagPiggybackAck() {
      boolean var1 = false;

      try {
         var1 = this.flagPiggybackAckInternal();
      } finally {
         if (var1) {
            this.markChanged();
         }

      }

      return var1;
   }

   protected boolean flagPiggybackAckInternal() {
      boolean var1 = false;

      boolean var2;
      try {
         this._piggybackLock.writeLock().lock();
         if (!this._piggybackAck) {
            this._piggybackAck = true;
            var1 = true;
         }

         var2 = var1;
      } finally {
         this._piggybackLock.writeLock().unlock();
      }

      return var2;
   }

   protected boolean flagPiggybackAckRequest() {
      boolean var1 = false;

      boolean var2;
      try {
         var1 = this.flagPiggybackAckRequestInternal();
         var2 = var1;
      } finally {
         if (var1) {
            this.markChanged();
         }

      }

      return var2;
   }

   protected boolean flagPiggybackAckRequestInternal() {
      boolean var1 = false;

      boolean var2;
      try {
         this._piggybackLock.writeLock().lock();
         if (!this._piggybackAckRequest) {
            this._piggybackAckRequest = true;
            var1 = true;
         }

         var2 = var1;
      } finally {
         this._piggybackLock.writeLock().unlock();
      }

      return var2;
   }

   public void addFinalAckToMessage(Message var1) {
      AcknowledgementHeader var2 = this.createPiggybackAckHeader();
      var2.setFinal(true);
      var1.getHeaders().add(var2);
   }

   @NotNull
   public List<WsrmHeader> getAndClearPiggybackHeaders() {
      List var1 = this.getAndClearPiggybackHeadersInternal();
      if (var1.size() > 0) {
         this.markChanged();
      }

      return var1;
   }

   @NotNull
   protected List<WsrmHeader> getAndClearPiggybackHeadersInternal() {
      ArrayList var2;
      try {
         this._piggybackLock.writeLock().lock();
         ArrayList var1 = new ArrayList(this._piggybackHeaders);
         this._piggybackHeaders.clear();
         if (this._piggybackAck) {
            var1.add(this.createPiggybackAckHeader());
            this._piggybackAck = false;
         }

         if (this._piggybackAckRequest) {
            var1.add(this.createPiggybackAckRequestHeader());
            this._piggybackAckRequest = false;
         }

         if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(this._type + " sequence " + this.getId() + " getting/clearing all piggyback headers for delivery on outgoing message. Piggybacking " + var1.size() + " headers");
         }

         var2 = var1;
      } finally {
         this._piggybackLock.writeLock().unlock();
      }

      return var2;
   }

   @NotNull
   public void putBackUnusedPiggybackHeaders(List<WsrmHeader> var1) {
      if (this.putBackUnusedPiggybackHeadersInternal(var1)) {
         this.markChanged();
      }

   }

   @NotNull
   protected boolean putBackUnusedPiggybackHeadersInternal(List<WsrmHeader> var1) {
      boolean var2 = false;

      try {
         this._piggybackLock.writeLock().lock();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            WsrmHeader var4 = (WsrmHeader)var3.next();
            if (var4 instanceof AcknowledgementHeader) {
               var2 |= this.flagPiggybackAckInternal();
            } else if (var4 instanceof AckRequestedHeader) {
               var2 |= this.flagPiggybackAckRequestInternal();
            }
         }

         if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(this._type + " sequence " + this.getId() + " accepted back " + var1.size() + " unused piggyback headers");
         }
      } finally {
         this._piggybackLock.writeLock().unlock();
      }

      return var2;
   }

   protected AcknowledgementHeader createPiggybackAckHeader() {
      AcknowledgementHeader var1 = new AcknowledgementHeader(this.getRmVersion());
      var1.setSequenceId(this.getId());
      SortedSet var2 = this.getAckRanges();
      var1.setAcknowledgementRanges(var2);
      return var1;
   }

   protected AckRequestedHeader createPiggybackAckRequestHeader() {
      AckRequestedHeader var1 = new AckRequestedHeader(this.getRmVersion());
      var1.setSequenceId(this.getId());
      return var1;
   }

   public void addPiggybackHeader(WsrmHeader var1) {
      boolean var2 = false;

      try {
         this._piggybackLock.writeLock().lock();
         var2 = this.addPiggybackHeaderInternal(var1);
      } finally {
         this._piggybackLock.writeLock().unlock();
         if (var2) {
            this.markChanged();
         }

      }

   }

   protected boolean addPiggybackHeaderInternal(WsrmHeader var1) {
      boolean var2 = false;
      if (WsrmHeader.getQName(AcknowledgementHeader.class, this.getRmVersion()).equals(var1.getName())) {
         if (!this._piggybackAck) {
            this._piggybackAck = true;
            var2 = true;
         }

         return var2;
      } else if (WsrmHeader.getQName(AckRequestedHeader.class, this.getRmVersion()).equals(var1.getName())) {
         if (!this._piggybackAckRequest) {
            this._piggybackAckRequest = true;
            var2 = true;
         }

         return var2;
      } else {
         ArrayList var3 = new ArrayList();
         Iterator var4 = this._piggybackHeaders.iterator();

         WsrmHeader var5;
         while(var4.hasNext()) {
            var5 = (WsrmHeader)var4.next();
            if (var5.getName().equals(var1.getName())) {
               var3.add(var5);
            }
         }

         for(var4 = var3.iterator(); var4.hasNext(); this._piggybackHeaders.remove(var5)) {
            var5 = (WsrmHeader)var4.next();
            if (LOGGER.isLoggable(Level.FINER)) {
               LOGGER.finer(this._type + " sequence " + this.getId() + " removing old piggyback header of type " + var5.getName());
            }
         }

         if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.finer(this._type + " sequence " + this.getId() + " adding new piggyback header of type " + var1.getName() + ". Total piggyback count: " + (this._piggybackHeaders.size() + 1));
         }

         this._piggybackHeaders.add(var1);
         var2 = true;
         return var2;
      }
   }

   public void addPiggybackHeaders(List<WsrmHeader> var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         WsrmHeader var3 = (WsrmHeader)var2.next();
         this.addPiggybackHeader(var3);
      }

   }

   public void setFinalMessageNum(long var1) {
      boolean var3 = false;

      try {
         this.getMessageLock().writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("*** Final message num for " + this._type + " " + this.getId() + " set to " + var1);
         }

         this._finalMessageNum = var1;
         var3 = true;
      } finally {
         this.getMessageLock().writeLock().unlock();
         if (var3) {
            this.markChanged();
         }

      }

   }

   public boolean setState(SequenceState var1) {
      boolean var2 = false;

      boolean var3;
      try {
         if (this._state.isValidTransition(var1)) {
            var2 = this.setStateInternal(var1);
            var3 = true;
            return var3;
         }

         var3 = false;
      } finally {
         if (var2) {
            this.markChanged();
         }

      }

      return var3;
   }

   protected boolean setStateInternal(SequenceState var1) {
      boolean var4 = false;

      SequenceState var2;
      SequenceState var3;
      try {
         this.getStateLock().writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("*** " + this._type + " " + this.getId() + " moving to state " + var1);
         }

         this._previousState = this._state;
         this._state = var1;
         var4 = true;
         var2 = this._state;
         var3 = this._previousState;
      } finally {
         this.getStateLock().writeLock().unlock();
      }

      PropertyChangeEvent var5 = new PropertyChangeEvent(this, "State", var3, var2);
      this.firePropertyChangeEvent(var5);
      return var4;
   }

   public void setExpires(Duration var1) {
      if (this._expires == null) {
         throw new IllegalArgumentException("Null Expires not allowed");
      } else {
         this._expires = var1;
         this.markChanged();
      }
   }

   public void setIdleTimeout(Duration var1) {
      this._idleTimeout = var1;
      this.markChanged();
   }

   public void setIncompleteSequenceBehavior(@NotNull WsrmConstants.IncompleteSequenceBehavior var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Null IncompleteSequenceBehavior not allowed");
      } else {
         this._incompleteSequenceBehavior = var1;
         this.markChanged();
      }
   }

   public void setDeliveryAssurance(@NotNull DeliveryAssurance var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Null DeliveryAssurance not allowed");
      } else {
         this._deliveryAssurance = var1;
         this.markChanged();
      }
   }

   public AddressingVersion getAddressingVersion() {
      return this._addressingVersion;
   }

   public void setAddressingVersion(AddressingVersion var1) {
      this._addressingVersion = var1;
      this.markChanged();
   }

   public SOAPVersion getSoapVersion() {
      return this._soapVersion;
   }

   public void setSoapVersion(SOAPVersion var1) {
      this._soapVersion = var1;
      this.markChanged();
   }

   public SortedSet<MessageRange> getAckRanges() {
      TreeSet var1 = new TreeSet();

      try {
         this.getMessageLock().readLock().lock();
         long var2 = -1L;
         long var4 = -1L;
         Iterator var6 = this._requests.keySet().iterator();

         while(var6.hasNext()) {
            long var7 = (Long)var6.next();
            MessageInfo var9 = (MessageInfo)this._requests.get(var7);
            if (var9.isAck()) {
               if (var2 == -1L) {
                  var2 = var7;
                  var4 = var7;
               } else if (var7 > var4 + 1L) {
                  MessageRange var10 = new MessageRange(var2, var4);
                  var1.add(var10);
                  var2 = var7;
                  var4 = var7;
               } else {
                  var4 = var7;
               }
            }
         }

         if (var2 != -1L) {
            MessageRange var14 = new MessageRange(var2, var4);
            var1.add(var14);
         }
      } finally {
         this.getMessageLock().readLock().unlock();
      }

      return var1;
   }

   public boolean isNonBuffered() {
      return this._nonBuffered;
   }

   public boolean isMasterInstance() {
      return this._masterInstance;
   }

   public void setMasterInstance(boolean var1) {
      boolean var2 = this._masterInstance;
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(this._type + " " + this + " was just made the " + this.getMasterString(var1) + " instance (used to be " + this.getMasterString(var2) + ") for sequence ID: " + this.getId());
      }

      this._masterInstance = var1;
      this.handleMasterInstanceChange(var2, var1);
   }

   private String getMasterString(boolean var1) {
      return var1 ? "master" : "slave";
   }

   protected abstract void handleMasterInstanceChange(boolean var1, boolean var2);

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      if (!this._masterInstance) {
         throw new IllegalStateException("Attempt to add listener to " + this + " when it is not the master instance");
      } else {
         if (!this._listeners.contains(var1)) {
            this._listeners.add(var1);
         }

      }
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      if (!this._masterInstance) {
         throw new IllegalStateException("Attempt to remove listener from " + this + " when it is not the master instance");
      } else {
         this._listeners.remove(var1);
      }
   }

   public void copyPropertyChangeListeners(Sequence var1) {
      if (!this._masterInstance) {
         throw new IllegalStateException("Attempt to add listener to " + this + " when it is not the master instance");
      } else {
         this._listeners.clear();
         this._listeners.addAll(var1._listeners);
      }
   }

   private void firePropertyChangeEvent(PropertyChangeEvent var1) {
      if (!this.objectsEqual(var1.getNewValue(), var1.getOldValue())) {
         PropertyChangeListener[] var2 = (PropertyChangeListener[])this._listeners.toArray(new PropertyChangeListener[this._listeners.size()]);
         PropertyChangeListener[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PropertyChangeListener var6 = var3[var5];

            try {
               var6.propertyChange(var1);
            } catch (Exception var8) {
               if (LOGGER.isLoggable(Level.WARNING)) {
                  LOGGER.log(Level.WARNING, var8.toString(), var8);
               }
            }
         }

      }
   }

   private boolean objectsEqual(Object var1, Object var2) {
      return var1 == null && var2 == null || var2.equals(var1);
   }

   public void startup() {
      PropertyChangeEvent var1 = null;

      try {
         this._stateLock.writeLock().lock();
         boolean var2 = this._active;
         if (!var2) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Starting up " + this._type + " seq " + this._id);
            }

            this._active = true;
            var1 = new PropertyChangeEvent(this, "Active", var2, true);
         }
      } finally {
         this._stateLock.writeLock().unlock();
      }

      if (var1 != null) {
         this.firePropertyChangeEvent(var1);
      }

   }

   public void shutdown() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Shutting down " + this._type + " seq " + this._id);
      }

      PropertyChangeEvent var1 = null;

      try {
         this._stateLock.writeLock().lock();
         boolean var2 = this._active;
         if (var2) {
            this._active = false;
            var1 = new PropertyChangeEvent(this, "Active", var2, false);
         }
      } finally {
         this._stateLock.writeLock().unlock();
      }

      if (var1 != null) {
         this.firePropertyChangeEvent(var1);
      }

   }

   public void destroy() {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Destroying " + this._type + " seq " + this._id);
         }

         this._stateLock.writeLock().lock();
         if (this._active) {
            this.shutdown();
         }
      } finally {
         this._stateLock.writeLock().unlock();
      }

   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(super.toString());
      var1.append(" - ");
      String var2 = this.getId() != null ? this.getId() : "<Unset pending first save>";
      var1.append(var2);
      return var1.toString();
   }

   private class RequestListener implements Serializable, PropertyChangeListener {
      private static final long serialVersionUID = 1L;

      private RequestListener() {
      }

      public void propertyChange(PropertyChangeEvent var1) {
         Sequence.this.markChanged();
         if (Sequence.LOGGER.isLoggable(Level.FINE)) {
            Sequence.LOGGER.fine("*** " + Sequence.this._type + " " + Sequence.this.getId() + " detected change to MessageInfo " + ((MessageInfo)var1.getSource()).getMessageNum() + "/" + var1.getPropertyName() + " and is now marked 'changed'");
         }

         if (var1.getPropertyName().equals("ack")) {
            try {
               Sequence.this._messageLock.writeLock().lock();
               if (!(Boolean)var1.getOldValue() && (Boolean)var1.getNewValue()) {
                  Sequence.this.setUnackedCount(Sequence.this._unackdCount - 1L);
               } else if ((Boolean)var1.getOldValue() && !(Boolean)var1.getNewValue()) {
                  Sequence.this.setUnackedCount(Sequence.this._unackdCount + 1L);
               }

               if (Sequence.LOGGER.isLoggable(Level.FINE)) {
                  Sequence.LOGGER.fine("UnAck'd Request Count in " + Sequence.this._type + " " + Sequence.this.getId() + " is currently: " + Sequence.this._unackdCount);
               }
            } finally {
               Sequence.this._messageLock.writeLock().unlock();
            }
         }

      }

      // $FF: synthetic method
      RequestListener(Object var2) {
         this();
      }
   }

   public static class AddResult {
      public boolean added;
      public NotAddedReason notAddedReason;

      public AddResult() {
      }

      public AddResult(AddResult var1) {
         this.added = var1.added;
         this.notAddedReason = var1.notAddedReason;
      }
   }

   public static enum NotAddedReason {
      DUPLICATE,
      OUT_OF_ORDER;
   }
}
