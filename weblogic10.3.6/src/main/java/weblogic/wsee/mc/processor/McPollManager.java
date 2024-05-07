package weblogic.wsee.mc.processor;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Messages;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.Fiber;
import com.sun.xml.ws.fault.SOAPFaultBuilder;
import com.sun.xml.ws.message.StringHeader;
import com.sun.xml.ws.model.CheckedExceptionImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;
import weblogic.management.configuration.WebServiceLogicalStoreMBean;
import weblogic.management.configuration.WebServiceMBean;
import weblogic.management.configuration.WebServicePersistenceMBean;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.spi.ClientInstance;
import weblogic.wsee.mc.exception.McException;
import weblogic.wsee.mc.exception.McMsgException;
import weblogic.wsee.mc.messages.McMsg;
import weblogic.wsee.mc.tube.McInitiatorDispatchFactory;
import weblogic.wsee.mc.tube.McSender;
import weblogic.wsee.mc.utils.McConstants;
import weblogic.wsee.mc.utils.McProtocolUtils;
import weblogic.wsee.persistence.LogicalStore;
import weblogic.wsee.persistence.LogicalStoreListChangeListener;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.security.wssc.sct.SCCredential;

public class McPollManager {
   private static final Logger LOGGER = Logger.getLogger(McPollManager.class.getName());
   private static McPollManager _instance;
   private final TimerManager _timerMgr;
   private final Map<McPoll, TimerInfo> _pollToTimerInfo;
   private final Map<McPoll, McSender> _pollToSender;
   private final Map<McPoll, ClientInstanceListener> _pollToListener;
   private final List<PollStore> _stores = new ArrayList();
   private final ReentrantReadWriteLock _storesLock = new ReentrantReadWriteLock(false);

   public static McPollManager getInstance() {
      return _instance;
   }

   public McPollManager() throws StoreException {
      TimerManagerFactory var1 = TimerManagerFactory.getTimerManagerFactory();
      this._timerMgr = var1.getDefaultTimerManager();
      this._pollToTimerInfo = new ConcurrentHashMap();
      this._pollToSender = new ConcurrentHashMap();
      this._pollToListener = new ConcurrentHashMap();
   }

   private void recover() throws StoreException {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Recovering McPoll instances in McPollManager");
      }

      WebServiceMBean var1 = WebServiceMBeanFactory.getInstance();
      WebServicePersistenceMBean var2 = var1.getWebServicePersistence();
      WebServiceLogicalStoreMBean[] var3 = var2.getWebServiceLogicalStores();
      WebServiceLogicalStoreMBean[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         WebServiceLogicalStoreMBean var7 = var4[var6];
         this.handleLogicalStoreAdded(var7.getName());
      }

      LogicalStore.addLogicalStoreListChangeListener(new LogicalStoreListChangeListener() {
         public void logicalStoreAdded(String var1) {
            try {
               McPollManager.this.handleLogicalStoreAdded(var1);
            } catch (Exception var3) {
               WseeMCLogger.logUnexpectedException(var3.toString(), var3);
            }

         }

         public void logicalStorePreRemoval(String var1) {
            try {
               McPollManager.this.handleLogicalStorePreRemoval(var1);
            } catch (Exception var3) {
               WseeMCLogger.logUnexpectedException(var3.toString(), var3);
            }

         }

         public void logicalStoreRemoved(String var1) {
            try {
               McPollManager.this.handleLogicalStoreRemoved(var1);
            } catch (Exception var3) {
               WseeMCLogger.logUnexpectedException(var3.toString(), var3);
            }

         }
      });
   }

   private void handleLogicalStoreAdded(String var1) throws StoreException {
      try {
         this._storesLock.writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Handling newly added logical store: " + var1);
         }

         PollStore var2 = this.getPollStore(var1);
         this._stores.add(var2);
         if (LOGGER.isLoggable(Level.FINE)) {
            this.dumpLogicalStoreNames("Added");
            this.dumpPolls();
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

   }

   private void handleLogicalStorePreRemoval(String var1) throws StoreException {
      PollStore var2 = null;

      try {
         this._storesLock.readLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Handling pre-removal of logical store: " + var1);
         }

         Iterator var3 = this._stores.iterator();

         while(var3.hasNext()) {
            PollStore var4 = (PollStore)var3.next();
            if (var4.getName().equals(var1)) {
               var2 = var4;
               break;
            }
         }
      } finally {
         this._storesLock.readLock().unlock();
      }

      if (var2 != null) {
         var2.stopPollsInStore(true);
      }

   }

   private void handleLogicalStoreRemoved(String var1) throws StoreException {
      try {
         this._storesLock.writeLock().lock();
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Handling newly removed logical store: " + var1);
         }

         PollStore var2 = null;
         Iterator var3 = this._stores.iterator();

         while(var3.hasNext()) {
            PollStore var4 = (PollStore)var3.next();
            if (var4.getName().equals(var1)) {
               var2 = var4;
               break;
            }
         }

         if (var2 != null) {
            this._stores.remove(var2);
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            this.dumpLogicalStoreNames("Removed");
            this.dumpPolls();
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

   }

   private void dumpLogicalStoreNames(String var1) {
      Set var2 = this.getPollStoreNames();
      StringBuffer var3 = new StringBuffer();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var3.append(var5).append(", ");
      }

      LOGGER.fine("These logical stores exist after one was " + var1 + ": " + var3.toString());
   }

   private void dumpPolls() {
      if (LOGGER.isLoggable(Level.FINER)) {
         Set var1 = this.keySet();
         StringBuffer var2 = new StringBuffer();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            McPoll var5 = this.get(var4);
            if (var5 != null) {
               var2.append("   ").append(var5.getClass().getSimpleName()).append(": ").append(var4).append(" - ").append(var5.getLogicalStoreName()).append("\n");
            }
         }

         LOGGER.finer("Current Polls:\n" + var2.toString());
      }

   }

   public void finalize() throws Throwable {
      Exception var1 = null;

      try {
         this._storesLock.writeLock().lock();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PollStore var3 = (PollStore)var2.next();

            try {
               var3.close();
            } catch (Exception var8) {
               var1 = var8;
            }
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

      super.finalize();
      if (var1 != null) {
         throw var1;
      }
   }

   private PollStore getPollStore(String var1) throws StoreException {
      return PollStore.getStore(var1);
   }

   private Set<String> getPollStoreNames() {
      try {
         this._storesLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PollStore var3 = (PollStore)var2.next();
            var1.add(var3.getName());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   private PollStore getPollStoreForPoll(McPoll var1) {
      String var2 = var1.getLogicalStoreName();
      if (var2 == null) {
         throw new IllegalStateException("Poll " + var1.getId() + " doesn't have a logical store name assigned to it!");
      } else {
         Iterator var3 = this._stores.iterator();

         PollStore var4;
         do {
            if (!var3.hasNext()) {
               throw new IllegalStateException("Poll " + var1.getId() + " refers to a logical store name for which there is no associated physical store: " + var2);
            }

            var4 = (PollStore)var3.next();
         } while(!var4.getName().equals(var2));

         return var4;
      }
   }

   public void addPoll(McPoll var1, ClientInstance var2) {
      synchronized(var1) {
         String var4 = var1.getId();
         if (!this.containsKey(var4)) {
            this.put(var4, var1);
            if (var2 != null) {
               ClientInstanceListener var5 = new ClientInstanceListener(var1);
               this._pollToListener.put(var1, var5);
               var2.addClientInstanceListener(var5);
            }

            TimerInfo var8 = new TimerInfo(var1);
            this._pollToTimerInfo.put(var1, var8);
            this.schedulePoll(var1);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Added poll " + var4 + " and started poll timers. Current poll list size: " + this.size());
            }
         } else {
            this.put(var4, var1);
         }

      }
   }

   public void updatePoll(McPoll var1, ClientInstance var2) {
      synchronized(var1) {
         String var4 = var1.getId();
         if (!this.containsKey(var4)) {
            this.addPoll(var1, var2);
         } else {
            this.put(var4, var1);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Updated poll " + var4);
            }

         }
      }
   }

   public McPoll getPoll(String var1) {
      return this.get(var1);
   }

   public Collection<McPoll> values() {
      try {
         this._storesLock.readLock().lock();
         LinkedList var1 = new LinkedList();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PollStore var3 = (PollStore)var2.next();
            var1.addAll(var3.values());
         }

         LinkedList var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public int size() {
      int var1 = 0;

      try {
         this._storesLock.readLock().lock();

         PollStore var3;
         for(Iterator var2 = this._stores.iterator(); var2.hasNext(); var1 += var3.size()) {
            var3 = (PollStore)var2.next();
         }

         int var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public boolean isEmpty() {
      boolean var3;
      try {
         this._storesLock.readLock().lock();
         Iterator var1 = this._stores.iterator();

         PollStore var2;
         do {
            if (!var1.hasNext()) {
               boolean var7 = false;
               return var7;
            }

            var2 = (PollStore)var1.next();
         } while(!var2.isEmpty());

         var3 = true;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var3;
   }

   public boolean containsKey(Object var1) {
      boolean var9;
      try {
         this._storesLock.readLock().lock();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PollStore var3 = (PollStore)var2.next();
            String var4 = (String)var1;
            if (var3.containsKey(var4)) {
               boolean var5 = true;
               return var5;
            }
         }

         var9 = false;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var9;
   }

   public boolean containsValue(Object var1) {
      boolean var9;
      try {
         this._storesLock.readLock().lock();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PollStore var3 = (PollStore)var2.next();
            McPoll var4 = (McPoll)var1;
            if (var3.containsValue(var4)) {
               boolean var5 = true;
               return var5;
            }
         }

         var9 = false;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var9;
   }

   public McPoll get(String var1) {
      Iterator var2;
      try {
         this._storesLock.readLock().lock();
         var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PollStore var3 = (PollStore)var2.next();
            if (var3.containsKey(var1)) {
               McPoll var4 = (McPoll)var3.get(var1);
               return var4;
            }
         }

         var2 = null;
      } finally {
         this._storesLock.readLock().unlock();
      }

      return var2;
   }

   public McPoll put(String var1, McPoll var2) {
      McPoll var4;
      try {
         this._storesLock.writeLock().lock();
         PollStore var3 = this.getPollStoreForPoll(var2);
         var4 = (McPoll)var3.put(var1, var2);
      } finally {
         this._storesLock.writeLock().unlock();
      }

      return var4;
   }

   public McPoll remove(String var1) {
      McPoll var4;
      try {
         this._storesLock.writeLock().lock();
         Iterator var2 = this._stores.iterator();

         PollStore var3;
         do {
            if (!var2.hasNext()) {
               var2 = null;
               return var2;
            }

            var3 = (PollStore)var2.next();
         } while(!var3.containsKey(var1));

         var4 = (McPoll)var3.remove(var1);
      } finally {
         this._storesLock.writeLock().unlock();
      }

      return var4;
   }

   public void putAll(Map<String, McPoll> var1) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         McPoll var4 = (McPoll)var1.get(var3);
         this.put(var3, var4);
      }

   }

   public void clear() {
      try {
         this._storesLock.writeLock().lock();
         Iterator var1 = this._stores.iterator();

         while(var1.hasNext()) {
            PollStore var2 = (PollStore)var1.next();
            var2.clear();
         }
      } finally {
         this._storesLock.writeLock().unlock();
      }

   }

   public Set<String> keySet() {
      try {
         this._storesLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PollStore var3 = (PollStore)var2.next();
            var1.addAll(var3.keySet());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   public Set<Map.Entry<String, McPoll>> entrySet() {
      try {
         this._storesLock.readLock().lock();
         HashSet var1 = new HashSet();
         Iterator var2 = this._stores.iterator();

         while(var2.hasNext()) {
            PollStore var3 = (PollStore)var2.next();
            var1.addAll(var3.entrySet());
         }

         HashSet var7 = var1;
         return var7;
      } finally {
         this._storesLock.readLock().unlock();
      }
   }

   void startPoll(McPoll var1) {
      synchronized(var1) {
         TimerInfo var3 = new TimerInfo(var1);
         this._pollToTimerInfo.put(var1, var3);
         this.schedulePoll(var1);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Started poll timers for poll " + var1.getId());
         }

      }
   }

   public void terminatePolling(String var1) {
      this.stopPoll(var1, true);
   }

   private void removePoll(McPoll var1) {
      synchronized(var1) {
         String var3 = var1.getId();
         this.remove(var3);
         TimerInfo var4 = (TimerInfo)this._pollToTimerInfo.remove(var1);
         if (var4 != null) {
            var4.cancelAll();
         }

         this._pollToSender.remove(var1);
         ClientInstanceListener var5 = (ClientInstanceListener)this._pollToListener.remove(var1);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Poll " + var3 + " removed");
         }

      }
   }

   void stopPoll(String var1, boolean var2) {
      McPoll var3;
      if (var2) {
         var3 = this.remove(var1);
      } else {
         var3 = this.get(var1);
      }

      if (var3 != null) {
         TimerInfo var4 = (TimerInfo)this._pollToTimerInfo.remove(var3);
         if (var4 != null) {
            var4.cancelAll();
         }

         this._pollToSender.remove(var3);
         this._pollToListener.remove(var3);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Polling terminated for poll " + var1);
         }
      }

   }

   private void schedulePoll(McPoll var1) {
      synchronized(var1) {
         switch (var1.getState()) {
            case ENABLED:
            case POLLING:
               TimerInfo var3 = (TimerInfo)this._pollToTimerInfo.get(var1);
               if (var3 != null) {
                  var3.resetIntervalTimer();
               }

               var1.setState(McPollState.ENABLED);
               return;
            default:
               throw new IllegalStateException(WseeMCLogger.logIllegalPollStateLoggable(var1.getState().toString()).getMessage());
         }
      }
   }

   private void intervalTimeout(McPoll var1) {
      String var2 = var1.getId();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Polling interval expiration for poll " + var2);
      }

      if (this.get(var2) != null) {
         if (var1.getState() != McPollState.ENABLED) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Polling interval expiration for poll " + var2 + " However, poll is in state " + var1.getState() + " so no poll will be performed");
            }
         } else if (var1.getSuspendedFiberCount() <= 0 && var1.getPollCount() != -1 && var1.getPersistentRequestCount() <= 0) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Polling interval expiration for poll " + var2 + " However, there are no persistent requests or suspended fibers so no poll will be performed");
            }
         } else {
            var1.setState(McPollState.POLLING);
            var1.incrementPollCount();

            try {
               this.sendMcMessage(var1);
            } catch (Exception var15) {
               Exception var3 = var15;
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Exception during polling for poll " + var2 + var15.getMessage());
               }

               WseeMCLogger.logUnexpectedException(var15.toString(), var15);
               if (var15 instanceof SOAPException || var15 instanceof SOAPFaultException || var15 instanceof McException || var15 instanceof McMsgException) {
                  var1.setState(McPollState.TERMINATED);
                  HashSet var4 = new HashSet(var1.getFiberKeySet());
                  SOAPVersion var5 = var1.getSoapVersion();
                  Iterator var6 = var4.iterator();

                  while(var6.hasNext()) {
                     String var7 = (String)var6.next();
                     FiberBox var8 = var1.getFiber(var7);

                     try {
                        Fiber var9 = var8.get();
                        var1.decrementSuspendedFiberCount();
                        Message var10 = SOAPFaultBuilder.createSOAPFaultMessage(var5, (CheckedExceptionImpl)null, var3);
                        Packet var11 = var9.getPacket().createClientResponse(var10);
                        var9.resume(var11);
                     } catch (InterruptedException var13) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                           LOGGER.log(Level.FINE, "IntervalTimeout failed for poll " + var2 + " " + var13.toString(), var13);
                        }

                        WseeMCLogger.logUnexpectedException(var3.toString(), var13);
                     }
                  }

                  var1.clearPersistentRequests();
                  this.removePoll(var1);
                  return;
               }
            }
         }

         synchronized(var1) {
            if (var1.getFiberCount() <= 0 && var1.getPollCount() != -1 && var1.getPersistentRequestCount() <= 0) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("No fibers or persistent requests added to poll; terminating polling for " + var2);
               }

               this.removePoll(var1);
            } else {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Scheduling poll for " + var2 + ", poll count = " + var1.getPollCount());
               }

               this.schedulePoll(var1);
            }

         }
      }
   }

   private void expirationTimeout(McPoll var1) {
      String var2 = var1.getId();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Polling lifetime expiration for poll " + var2);
      }

      if (this.get(var2) != null) {
         var1.setState(McPollState.EXPIRED);
         HashSet var3 = new HashSet(var1.getFiberKeySet());
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            FiberBox var6 = var1.getFiber(var5);

            try {
               Fiber var7 = var6.get();
               var1.decrementSuspendedFiberCount();
               Packet var8 = var7.getPacket().createClientResponse((Message)null);
               var7.resume(var8);
            } catch (InterruptedException var9) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.log(Level.FINE, "ExpirationTimeout failed for poll " + var2 + " " + var9.toString(), var9);
               }

               WseeMCLogger.logUnexpectedException(var9.toString(), var9);
            }
         }

         var1.clearPersistentRequests();
         this.removePoll(var1);
      }
   }

   private void sendMcMessage(McPoll var1) throws Exception {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Sending MakeConnection message for poll " + var1.getId() + ", poll count is " + var1.getPollCount() + ", fiber count = " + var1.getFiberCount() + ", suspended fiber count = " + var1.getSuspendedFiberCount());
      }

      McMsg var2 = new McMsg();
      String var3 = McConstants.getAnonymousURITemplate(McConstants.McVersion.MC_11) + McProtocolUtils.encodeId(var1.getId());
      var2.setAddress(var3);
      String var4 = var1.getSoapVersion() == SOAPVersion.SOAP_12 ? "SOAP 1.2 Protocol" : "SOAP 1.1 Protocol";
      MessageFactory var5 = MessageFactory.newInstance(var4);
      SOAPMessage var6 = var5.createMessage();
      var2.writeIntoSOAPMsg(var6);
      Message var7 = Messages.create(var6);
      String var8 = McConstants.Action.MC.getActionURI(McConstants.McVersion.MC_11);
      StringHeader var9 = new StringHeader(var1.getAddressingVersion().actionTag, var8);
      var7.getHeaders().addOrReplace(var9);
      McSender var10 = this.getSender(var1);
      if (var10 != null) {
         HashMap var11 = new HashMap();
         SCCredential var12 = var1.getCredential();
         if (var12 != null) {
            var11.put("weblogic.wsee.wssc.sct", var12);
         }

         var10.send(var7, var8, var1.getEndpointReference(), (WSEndpointReference)null, (WSEndpointReference)null, var11);
      } else {
         throw new McException("Could not create sender for poll " + var1.getId());
      }
   }

   private McSender getSender(McPoll var1) {
      McSender var2 = (McSender)this._pollToSender.get(var1);
      if (var2 == null) {
         McInitiatorDispatchFactory var3 = new McInitiatorDispatchFactory(var1.getClientId());
         var2 = new McSender(var3);
         this._pollToSender.put(var1, var2);
      }

      return var2;
   }

   static {
      try {
         _instance = new McPollManager();
         _instance.recover();
      } catch (Exception var1) {
         WseeMCLogger.logUnexpectedException(var1.toString(), var1);
         throw new RuntimeException(var1.toString(), var1);
      }
   }

   private class ClientInstanceListener implements ClientInstance.Listener {
      private final McPoll _poll;

      private ClientInstanceListener(McPoll var2) {
         this._poll = var2;
      }

      public void clientInstanceClosing(ClientInstance var1) {
         synchronized(this._poll) {
            TimerInfo var3 = (TimerInfo)McPollManager.this._pollToTimerInfo.get(this._poll);
            if (var3 != null) {
               try {
                  Duration var4 = DatatypeFactory.newInstance().newDuration("PT0S");
                  this._poll.setExpires(var4);
                  var3.setExpirationTimer();
               } catch (Exception var6) {
                  WseeMCLogger.logUnexpectedException(var6.toString(), var6);
               }
            }

         }
      }

      public void clientInstanceRecycled(ClientInstance var1) {
      }

      // $FF: synthetic method
      ClientInstanceListener(McPoll var2, Object var3) {
         this(var2);
      }
   }

   public static class FiberBox {
      private Semaphore lock = new Semaphore(0);
      private final Fiber fiber;
      private final String name;

      public FiberBox(Fiber var1) {
         this.fiber = var1;
         this.name = this.fiber.toString();
      }

      public void open() {
         if (this.lock != null) {
            this.lock.release();
         }

      }

      public Fiber get() throws InterruptedException {
         if (this.lock != null) {
            this.lock.acquire();
            this.lock = null;
         }

         return this.fiber;
      }

      public String getName() {
         return this.name;
      }
   }

   private class TimerInfo {
      private final McPoll _poll;
      private Timer _expirationTimer;
      private Timer _intervalTimer;

      private TimerInfo(McPoll var2) {
         this._poll = var2;
         this.setExpirationTimer();
      }

      private void setExpirationTimer() {
         if (this._expirationTimer != null) {
            this._expirationTimer.cancel();
         }

         long var1 = this._poll.getStartTime();
         long var3 = System.currentTimeMillis();
         long var5 = var3 - var1;
         long var7 = this._poll.getExpires().getTimeInMillis(new Date());
         var7 -= var5;
         if (var7 < 0L) {
            var7 = 0L;
         }

         this._expirationTimer = McPollManager.this._timerMgr.schedule(new TimerListener() {
            public void timerExpired(Timer var1) {
               synchronized(TimerInfo.this) {
                  McPollManager.this.expirationTimeout(TimerInfo.this._poll);
               }
            }
         }, var7);
      }

      private void resetIntervalTimer() {
         if (this._intervalTimer != null) {
            this._intervalTimer.cancel();
         }

         int var3 = this._poll.getPollCount();
         long var1;
         if (var3 == -1) {
            var1 = 0L;
         } else {
            var1 = this._poll.getInterval().getTimeInMillis(new Date());
            if (this._poll.isUseExponentialBackoff() && var3 > 0) {
               var1 = (long)(Math.random() * Math.pow(2.0, (double)var3) * (double)var1);
            }
         }

         this._intervalTimer = McPollManager.this._timerMgr.schedule(new TimerListener() {
            public void timerExpired(Timer var1) {
               synchronized(TimerInfo.this) {
                  McPollManager.this.intervalTimeout(TimerInfo.this._poll);
               }
            }
         }, var1);
         if (McPollManager.LOGGER.isLoggable(Level.FINE)) {
            McPollManager.LOGGER.fine("Scheduled interval timer " + var1 + " ms into the future");
         }

      }

      private synchronized void cancelAll() {
         if (this._expirationTimer != null) {
            this._expirationTimer.cancel();
            this._expirationTimer = null;
         }

         if (this._intervalTimer != null) {
            this._intervalTimer.cancel();
            this._intervalTimer = null;
         }

      }

      // $FF: synthetic method
      TimerInfo(McPoll var2, Object var3) {
         this(var2);
      }
   }
}
