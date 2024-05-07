package weblogic.wsee.reliability2.sequence;

import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Packet;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.WsrmSecurityContext;
import weblogic.wsee.reliability.faults.WsrmFaultMsg;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.property.WsrmInvocationPropertyBag;
import weblogic.wsee.reliability2.store.SequenceMap;

public abstract class SequenceManager<T extends Sequence> {
   private static final Logger LOGGER = Logger.getLogger(SequenceManager.class.getName());
   public static final boolean STRESS_SAFE_DISABLE = false;
   protected SequenceMap<T> _seqIdToSeqMap;
   private Map<String, T> _createSeqMsgIdToSeqMap;
   private ReentrantReadWriteLock _mapLock = new ReentrantReadWriteLock(false);
   private String _type;

   protected SequenceManager(SequenceMap<T> var1) throws WsrmException {
      this._seqIdToSeqMap = var1;
      this.parseType();
      this._createSeqMsgIdToSeqMap = new HashMap();
   }

   protected void recoverMap() throws WsrmException {
      try {
         this._seqIdToSeqMap.setSequenceListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent var1) {
               if (var1.getPropertyName().equals("Active") && var1.getSource() instanceof SourceSequence) {
                  SequenceManager.this.handleSequenceActiveChange((Sequence)var1.getSource(), (Boolean)var1.getOldValue(), (Boolean)var1.getNewValue());
               }

            }
         });
         this._seqIdToSeqMap.recover();
      } catch (StoreException var2) {
         throw new WsrmException(var2.toString(), var2);
      }
   }

   private void parseType() {
      String var1 = this.getClass().getName();
      int var2 = var1.lastIndexOf(46);
      this._type = var1.substring(var2 + 1);
   }

   public abstract void handleRmFault(WsrmFaultMsg var1);

   public void addSequence(T var1) throws WsrmException {
      if (var1.getId() != null && this._seqIdToSeqMap.containsKey(var1.getId())) {
         throw new DuplicateSequenceException(WseeRmLogger.logAddingNullOrDuplicateSequenceLoggable(var1.getId()).getMessage());
      } else {
         this.updateSequenceInternal(var1);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** Added  " + this._type + " sequence " + var1.getId() + ". Current sequence count: " + this._seqIdToSeqMap.size());
            this.dumpSequences();
         }

      }
   }

   public void updateSequence(T var1) throws UnknownSequenceException {
      if (var1 != null && this._seqIdToSeqMap.containsKey(var1.getId())) {
         if (var1.isChanged()) {
            this.updateSequenceInternal(var1);
            if (var1.getCreateSequenceMsgId() != null) {
               try {
                  this._mapLock.writeLock().lock();
                  this._createSeqMsgIdToSeqMap.put(var1.getCreateSequenceMsgId(), var1);
               } finally {
                  this._mapLock.writeLock().unlock();
               }
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("** Updated  " + this._type + " sequence " + var1.getId() + ".");
            }
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("** " + this._type + " sequence " + var1.getId() + " was not changed. Skipping update.");
         }

      } else {
         if (LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.warning("** Sequence has been unavaliable or closed by another thread. seqId: " + (var1 != null ? var1.getId() : "unknown"));
         }

      }
   }

   private void updateSequenceInternal(T var1) {
      this._seqIdToSeqMap.put(var1.getId(), var1);
   }

   public T getSequence(WsrmConstants.RMVersion var1, String var2) {
      try {
         return this.getSequence(var1, var2, true);
      } catch (UnknownSequenceException var4) {
         var4.printStackTrace();
         return null;
      }
   }

   public T getSequence(WsrmConstants.RMVersion var1, String var2, boolean var3) throws UnknownSequenceException {
      if (var2 != null && var2.length() >= 1) {
         Sequence var4 = this._seqIdToSeqMap.get(var2);
         if (var4 == null) {
            try {
               this._mapLock.readLock().lock();
               var4 = (Sequence)this._createSeqMsgIdToSeqMap.get(var2);
            } finally {
               this._mapLock.readLock().unlock();
            }
         }

         if (var4 == null && !var3) {
            throw new UnknownSequenceException(var1, true, var2);
         } else {
            return var4;
         }
      } else {
         throw new UnknownSequenceException(WseeRmLogger.logNullSequenceIDLoggable().getMessage(), var1, true, var2);
      }
   }

   public Iterator<T> listSequences() {
      return this._seqIdToSeqMap.values().iterator();
   }

   public List<T> getSequencesForPiggybackEndpoint(String var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.listSequences();

      while(var3.hasNext()) {
         Sequence var4 = (Sequence)var3.next();
         WSEndpointReference var5 = var4.getPiggybackEpr();
         if (var5 != null && !var5.isAnonymous() && var5.getAddress().equals(var1)) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public void removeSequence(T var1) {
      this._seqIdToSeqMap.remove(var1.getId());
      if (var1.getCreateSequenceMsgId() != null) {
         try {
            this._mapLock.writeLock().lock();
            this._createSeqMsgIdToSeqMap.remove(var1.getCreateSequenceMsgId());
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("** Removed  " + this._type + " sequence " + var1.getId() + "'s CreateSequence Msg ID mapping. Remaining sequence count: " + this._createSeqMsgIdToSeqMap.size());
            }
         } finally {
            this._mapLock.writeLock().unlock();
         }
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("** Removed  " + this._type + " sequence " + var1.getId() + ". Remaining sequence count: " + this._seqIdToSeqMap.size());
         this.dumpSequences();
      }

   }

   private void dumpSequences() {
      if (LOGGER.isLoggable(Level.FINER)) {
         Set var1 = this._seqIdToSeqMap.keySet();
         StringBuffer var2 = new StringBuffer();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Sequence var5 = this.getSequence(WsrmConstants.RMVersion.RM_11, var4);
            if (var5 != null) {
               var2.append("   ").append(this._type).append(": ").append(var4).append(" - ").append(var5.getState()).append("\n");
            }
         }

         LOGGER.finer("Current " + this._type + " Sequences:\n" + var2.toString());
      }

   }

   public Map<String, Object> getSenderInvokeProperties(T var1, Packet var2) {
      HashMap var3 = new HashMap();
      if (var2 == null) {
         var2 = new Packet();
      }

      WsrmInvocationPropertyBag var4 = new WsrmInvocationPropertyBag(var2);
      var3.put(WsrmInvocationPropertyBag.key, var4);
      var4.setSequenceId(var1.getId());
      var4.setWsrmVersion(var1.getRmVersion());
      var4.setMostRecentMsgNum(var1.getMaxMessageNum());
      if (var2.invocationProperties.containsKey(WsrmInvocationPropertyBag.key)) {
         WsrmInvocationPropertyBag var5 = (WsrmInvocationPropertyBag)var2.invocationProperties.get(WsrmInvocationPropertyBag.key);
         var4.setFinalMsgFlag(var5.getFinalMsgFlag());
      }

      var4.setForceWsrm10Client(var1.getRmVersion() == WsrmConstants.RMVersion.RM_10);
      var4.setLogicalStoreName(var1.getLogicalStoreName());
      var3.putAll(this.getPacketInvokeProperties(var1));
      return var3;
   }

   protected Map<String, Object> getPacketInvokeProperties(T var1) {
      HashMap var2 = new HashMap();
      WsrmSecurityContext var3 = var1.getSecurityContext();
      if (var3 != null) {
         Map var4;
         try {
            var4 = var3.newInitializedMap();
         } catch (PolicyException var6) {
            throw new RuntimeException(var6);
         }

         var2.putAll(var4);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Extracted invocationProperties from WsrmSecurityProperties: " + var1);
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("No WsrmSecurityProperty from Sequence: " + var1);
         }
      }

      return var2;
   }

   public void setInvokePropertiesOntoPacket(T var1, Packet var2) {
      Map var3 = this.getPacketInvokeProperties(var1);
      var2.invocationProperties.putAll(var3);
   }

   protected void handleSequenceActiveChange(T var1, boolean var2, boolean var3) {
      if (var2 && !var3) {
         if (var1.getCreateSequenceMsgId() != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Seq Deactivation. Removing mapping of create seq msg ID " + var1.getCreateSequenceMsgId() + " to seq " + var1.getId());
            }

            try {
               this._mapLock.writeLock().lock();
               this._createSeqMsgIdToSeqMap.remove(var1.getCreateSequenceMsgId());
            } finally {
               this._mapLock.writeLock().unlock();
            }
         }
      } else if (!var2 && var3 && var1.getCreateSequenceMsgId() != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Seq Activation. Adding mapping for create seq msg ID " + var1.getCreateSequenceMsgId() + " to seq " + var1.getId());
         }

         try {
            this._mapLock.writeLock().lock();
            this._createSeqMsgIdToSeqMap.put(var1.getCreateSequenceMsgId(), var1);
         } finally {
            this._mapLock.writeLock().unlock();
         }
      }

   }
}
