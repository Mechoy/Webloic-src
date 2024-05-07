package weblogic.wsee.mc.processor;

import com.sun.istack.NotNull;
import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.jaxws.framework.WsUtil;
import weblogic.wsee.jaxws.spi.ClientInstanceIdentity;
import weblogic.wsee.persistence.Storable;
import weblogic.wsee.security.wssc.sct.SCCredential;

public class McPoll implements Storable {
   private static final long serialVersionUID = 1L;
   private static final Logger LOGGER = Logger.getLogger(McPoll.class.getName());
   private ClientInstanceIdentity _id;
   private Duration _expires;
   private Duration _interval;
   private boolean _backoff;
   private int _pollCount;
   private long _startTime;
   private transient ReentrantReadWriteLock _stateLock;
   private McPollState _state;
   private transient SCCredential _credential;
   private transient AddressingVersion _addressingVersion;
   private transient SOAPVersion _soapVersion;
   private transient WSEndpointReference _endpointRef;
   private transient String _logicalStoreName;
   private transient String _physicalStoreName;
   private transient ReentrantReadWriteLock _fiberLock;
   private transient Map<String, McPollManager.FiberBox> _fibers;
   private transient int _suspendedCount;
   private transient ReentrantReadWriteLock _persistentRequestsLock;
   private transient Map<String, String> _persistentRequests;

   public McPoll() {
   }

   private void initTransients() {
      this._stateLock = new ReentrantReadWriteLock();
      this._fiberLock = new ReentrantReadWriteLock();
      this._addressingVersion = AddressingVersion.W3C;
      this._soapVersion = SOAPVersion.SOAP_11;
      this._endpointRef = null;
      this._fibers = new HashMap();
      this._suspendedCount = 0;
      this._persistentRequestsLock = new ReentrantReadWriteLock();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.writeObject("10.3.6");
      var1.writeObject(this._addressingVersion.nsUri);
      var1.writeObject(this._soapVersion.nsUri);
      WsUtil.serializeWSEndpointReference(this._endpointRef, var1);

      try {
         this._stateLock.readLock().lock();
         var1.writeObject(this._state);
      } finally {
         this._stateLock.readLock().unlock();
      }

      try {
         this._persistentRequestsLock.writeLock().lock();
         var1.writeObject(this._persistentRequests);
      } finally {
         this._persistentRequestsLock.writeLock().unlock();
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
      this._endpointRef = WsUtil.deserializeWSEndpointReference(var1, this.getAddressingVersion());

      try {
         this._stateLock.writeLock().lock();
         this._state = (McPollState)var1.readObject();
      } finally {
         this._stateLock.writeLock().unlock();
      }

      try {
         this._persistentRequestsLock.writeLock().lock();
         this._persistentRequests = (Map)var1.readObject();
      } finally {
         this._persistentRequestsLock.writeLock().unlock();
      }

      var1.defaultReadObject();
   }

   public McPoll(@NotNull ClientInstanceIdentity var1, @NotNull String var2, @NotNull AddressingVersion var3, @NotNull SOAPVersion var4) {
      this.initTransients();
      this._id = var1;
      this._logicalStoreName = var2;
      this._addressingVersion = var3;
      this._soapVersion = var4;

      try {
         this._expires = DatatypeFactory.newInstance().newDuration("P1D");
         this._interval = DatatypeFactory.newInstance().newDuration("PT10S");
         this._backoff = false;
         this._pollCount = 0;
         this._startTime = System.currentTimeMillis();
      } catch (Exception var6) {
         WseeMCLogger.logUnexpectedException(var6.toString(), var6);
      }

      this._state = McPollState.ENABLED;
      this._persistentRequests = new HashMap();
   }

   public String getId() {
      return this._id.getId();
   }

   public String getClientId() {
      return this._id.getClientId();
   }

   public Duration getExpires() {
      return this._expires;
   }

   public long getStartTime() {
      return this._startTime;
   }

   public Duration getInterval() {
      return this._interval;
   }

   public boolean isUseExponentialBackoff() {
      return this._backoff;
   }

   public boolean isExpired() {
      return false;
   }

   private ReentrantReadWriteLock getStateLock() {
      return this._stateLock;
   }

   public McPollState getState() {
      McPollState var1;
      try {
         this.getStateLock().readLock().lock();
         var1 = this._state;
      } finally {
         this.getStateLock().readLock().unlock();
      }

      return var1;
   }

   public boolean setState(McPollState var1) {
      boolean var2;
      try {
         this.getStateLock().writeLock().lock();
         if (this._state.isValidTransition(var1)) {
            this._state = var1;
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("MakeConnection poll " + this.getId() + " transitioned to state " + this._state);
            }

            var2 = true;
            return var2;
         }

         var2 = false;
      } finally {
         this.getStateLock().writeLock().unlock();
      }

      return var2;
   }

   public void setExpires(Duration var1) {
      this._expires = var1;
   }

   public void setInterval(Duration var1) {
      this._interval = var1;
   }

   public void setExponentialBackoff(boolean var1) {
      this._backoff = var1;
   }

   public void incrementPollCount() {
      ++this._pollCount;
   }

   public int getPollCount() {
      return this._pollCount;
   }

   public void resetPollCount(boolean var1) {
      if (var1) {
         this._pollCount = -1;
      } else {
         this._pollCount = 0;
      }

   }

   public AddressingVersion getAddressingVersion() {
      return this._addressingVersion;
   }

   public void setAddressingVersion(AddressingVersion var1) {
      this._addressingVersion = var1;
   }

   public SOAPVersion getSoapVersion() {
      return this._soapVersion;
   }

   public void setSoapVersion(SOAPVersion var1) {
      this._soapVersion = var1;
   }

   public WSEndpointReference getEndpointReference() {
      return this._endpointRef;
   }

   public void setEndpointReference(WSEndpointReference var1) {
      this._endpointRef = var1;
   }

   public SCCredential getCredential() {
      return this._credential;
   }

   public void setCredential(SCCredential var1) {
      this._credential = var1;
   }

   public boolean hasExplicitExpiration() {
      return false;
   }

   public String getPhysicalStoreName() {
      return this._physicalStoreName;
   }

   public String getLogicalStoreName() {
      return this._logicalStoreName;
   }

   public void setLogicalStoreName(String var1) {
      this._logicalStoreName = var1;
   }

   public void setPhysicalStoreName(String var1) {
      this._physicalStoreName = var1;
   }

   public Long getCreationTime() {
      return this._startTime;
   }

   public Long getLastUpdatedTime() {
      return this._startTime;
   }

   public void touch() {
   }

   public Serializable getObjectId() {
      return this.getId();
   }

   private ReentrantReadWriteLock getFiberLock() {
      return this._fiberLock;
   }

   public void addFiber(String var1, McPollManager.FiberBox var2) {
      try {
         this.getFiberLock().writeLock().lock();
         this._fibers.put(var1, var2);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("MakeConnection poll " + this.getId() + " added fiber " + var2.getName() + ", id " + var1 + " fiber count = " + this._fibers.size() + ", suspended fiber count = " + this.getSuspendedFiberCount());
         }
      } finally {
         this.getFiberLock().writeLock().unlock();
      }

   }

   public Set<String> getFiberKeySet() {
      Set var1;
      try {
         this.getFiberLock().readLock().lock();
         var1 = this._fibers.keySet();
      } finally {
         this.getFiberLock().readLock().unlock();
      }

      return var1;
   }

   public McPollManager.FiberBox removeFiber(String var1) {
      McPollManager.FiberBox var3;
      try {
         this.getFiberLock().writeLock().lock();
         McPollManager.FiberBox var2 = (McPollManager.FiberBox)this._fibers.remove(var1);
         if (var2 != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("MakeConnection poll " + this.getId() + " removed fiber " + var2.getName() + " fiber count = " + this._fibers.size() + ", suspended fiber count = " + this.getSuspendedFiberCount());
            }
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("MakeConnection poll " + this.getId() + " no fiber with id " + var1 + " fiber count = " + this._fibers.size());
         }

         var3 = var2;
      } finally {
         this.getFiberLock().writeLock().unlock();
      }

      return var3;
   }

   public McPollManager.FiberBox getFiber(String var1) {
      McPollManager.FiberBox var2;
      try {
         this.getFiberLock().readLock().lock();
         var2 = (McPollManager.FiberBox)this._fibers.get(var1);
      } finally {
         this.getFiberLock().readLock().unlock();
      }

      return var2;
   }

   public int getFiberCount() {
      int var1;
      try {
         this.getFiberLock().readLock().lock();
         var1 = this._fibers.size();
      } finally {
         this.getFiberLock().readLock().unlock();
      }

      return var1;
   }

   public void incrementSuspendedFiberCount() {
      ++this._suspendedCount;
   }

   public void decrementSuspendedFiberCount() {
      --this._suspendedCount;
   }

   public int getSuspendedFiberCount() {
      return this._suspendedCount;
   }

   public void addPersistentRequest(String var1, String var2) {
      try {
         this._persistentRequestsLock.writeLock().lock();
         if (!this._persistentRequests.containsKey(var1)) {
            this._persistentRequests.put(var1, var2);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("MakeConnection poll " + this.getId() + " added request msg ID " + var1 + " and action '" + var2 + "''. PersistentRequest count = " + this._persistentRequests.size());
            }
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("MakeConnection poll " + this.getId() + " bypassed adding request msg ID " + var1 + " because it already existed in this McPoll instance");
         }
      } finally {
         this._persistentRequestsLock.writeLock().unlock();
      }

   }

   public String getPersistentRequestAction(String var1) {
      String var2;
      try {
         this._persistentRequestsLock.readLock().lock();
         var2 = (String)this._persistentRequests.get(var1);
      } finally {
         this._persistentRequestsLock.readLock().unlock();
      }

      return var2;
   }

   public int getPersistentRequestCount() {
      int var1;
      try {
         this._persistentRequestsLock.readLock().lock();
         var1 = this._persistentRequests.size();
      } finally {
         this._persistentRequestsLock.readLock().unlock();
      }

      return var1;
   }

   public boolean removePersistentRequest(String var1) {
      boolean var3;
      try {
         this._persistentRequestsLock.writeLock().lock();
         String var2 = (String)this._persistentRequests.remove(var1);
         if (var2 != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("MakeConnection poll " + this.getId() + " removed PersistentRequest msg ID " + var1 + " PersistentRequest count = " + this._persistentRequests.size());
            }
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("MakeConnection poll " + this.getId() + " found NO PersistentRequest request with msg ID " + var1);
         }

         var3 = var2 != null;
      } finally {
         this._persistentRequestsLock.writeLock().unlock();
      }

      return var3;
   }

   public boolean containsPersistentRequest(String var1) {
      boolean var2;
      try {
         this._persistentRequestsLock.readLock().lock();
         var2 = this._persistentRequests.containsKey(var1);
      } finally {
         this._persistentRequestsLock.readLock().unlock();
      }

      return var2;
   }

   void clearPersistentRequests() {
      try {
         this._persistentRequestsLock.writeLock().lock();
         this._persistentRequests.clear();
      } finally {
         this._persistentRequestsLock.writeLock().unlock();
      }

   }
}
