package weblogic.messaging.saf.store;

import java.util.HashMap;
import java.util.Iterator;
import weblogic.common.CompletionRequest;
import weblogic.messaging.saf.SAFConversationInfo;
import weblogic.messaging.saf.SAFException;
import weblogic.messaging.saf.common.SAFDebug;
import weblogic.messaging.saf.internal.Agent;
import weblogic.messaging.saf.internal.ReceivingAgent;
import weblogic.messaging.saf.internal.ReceivingAgentImpl;
import weblogic.messaging.saf.internal.SendingAgent;
import weblogic.messaging.saf.internal.SendingAgentImpl;
import weblogic.store.PersistentHandle;
import weblogic.store.PersistentStore;
import weblogic.store.PersistentStoreConnection;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreManager;
import weblogic.store.PersistentStoreRecord;
import weblogic.store.PersistentStoreTransaction;

public final class SAFStore {
   private PersistentHandle SAStoreHandle;
   private PersistentHandle RAStoreHandle;
   private final HashMap conversationStoreHandles = new HashMap();
   private final HashMap conversationInfos = new HashMap();
   private SendingAgent sendingAgent;
   private ReceivingAgent receivingAgent;
   private final String agentName;
   private PersistentStore store;
   private final String storeName;
   private boolean recovered;
   private static final int NO_FLAGS = 0;
   private static final String CONN_NAME_PREFIX = "weblogic.messaging.";
   private static final SAFObjectHandler SAF_OBJECT_HANDLER = new SAFObjectHandler();
   private PersistentStoreConnection storeConnectionNotForMaps;

   SAFStore(String var1, String var2, boolean var3) throws SAFException {
      this.storeName = var1;
      this.agentName = var2;
      this.initPersistentStore();
      this.open(var3);
      if (SAFDebug.SAFStore.isDebugEnabled()) {
         SAFDebug.SAFStore.debug(" == SAFStore Created for Agent " + var2);
      }

   }

   private void initPersistentStore() throws SAFException {
      PersistentStoreManager var1 = PersistentStoreManager.getManager();
      if (this.storeName != null) {
         this.store = var1.getStore(this.storeName);
         if (this.store == null) {
            throw new AssertionError("Persistent Store Service should have already created a PersistentStore  for Name = <" + this.storeName + " > before SAFService is booted");
         }
      } else {
         this.store = var1.getDefaultStore();
         if (this.store == null) {
            throw new SAFException("The default persistent store does not exist");
         }

         if (SAFDebug.SAFStore.isDebugEnabled()) {
            SAFDebug.SAFStore.debug("Agent using the server's default store");
         }
      }

   }

   public String getStoreName() {
      return this.storeName;
   }

   public String getEffectiveStoreName() {
      if (this.storeName != null) {
         return this.storeName;
      } else {
         return this.store != null ? this.store.getName() : null;
      }
   }

   public String toString() {
      return "<SAFStore> : storeName = " + this.storeName + " agentName =" + this.agentName;
   }

   public synchronized SendingAgent getSendingAgent() {
      return this.sendingAgent;
   }

   public synchronized ReceivingAgent getReceivingAgent() {
      return this.receivingAgent;
   }

   private void open(boolean var1) throws SAFStoreException {
      try {
         this.storeConnectionNotForMaps = this.store.createConnection("weblogic.messaging." + this.agentName, SAF_OBJECT_HANDLER);
      } catch (PersistentStoreException var6) {
         throw new SAFStoreException(this, var6);
      }

      SAFStoreRecord var3;
      for(SAFStoreRecord var2 = this.recover(); var2 != null; var2 = var3) {
         var3 = var2.getNext();
         var2.setNext((SAFStoreRecord)null);

         Object var4;
         try {
            var4 = var2.getStoreObject();
         } catch (PersistentStoreException var7) {
            continue;
         }

         if (SAFDebug.SAFStore.isDebugEnabled()) {
            SAFDebug.SAFStore.debug("*********** ***** <SAFStoreRecord> state= class=" + var4.getClass().getName() + " code=" + SAF_OBJECT_HANDLER.getCode(var4) + " obj=" + var4);
         }

         switch (SAF_OBJECT_HANDLER.getCode(var4)) {
            case 15:
               this.sendingAgent = (SendingAgent)var4;
               break;
            case 16:
               this.receivingAgent = (ReceivingAgent)var4;
               break;
            case 17:
               SAFConversationInfo var5 = (SAFConversationInfo)var4;
               this.conversationInfos.put(var5.getConversationName(), var5);
               this.conversationStoreHandles.put(var5, var2.getHandle());
         }
      }

      if (this.sendingAgent != null) {
         ((SendingAgentImpl)this.sendingAgent).setConversationInfosFromStore(this.conversationInfos);
      }

      if (this.receivingAgent != null) {
         ((ReceivingAgentImpl)this.receivingAgent).setConversationInfosFromStore(this.conversationInfos);
      }

   }

   private SAFStoreRecord recover() throws SAFStoreException {
      if (this.recovered) {
         return null;
      } else {
         this.recovered = true;
         SAFStoreRecord var1 = null;
         SAFStoreRecord var2 = null;

         try {
            PersistentStoreConnection.Cursor var3 = this.storeConnectionNotForMaps.createCursor(0);

            PersistentStoreRecord var7;
            while((var7 = var3.next()) != null) {
               SAFStoreRecord var5 = new SAFStoreRecord(var7);
               if (var2 == null) {
                  var2 = var5;
                  var1 = var5;
               } else {
                  var2.setNext(var5);
                  var2 = var5;
               }
            }

            return var1;
         } catch (PersistentStoreException var6) {
            SAFStoreException var4 = new SAFStoreException(this, var6);
            var4.initCause(var6);
            throw var4;
         }
      }
   }

   public void close() {
      synchronized(this.conversationInfos) {
         this.conversationInfos.clear();
         this.conversationStoreHandles.clear();
      }

      if (this.storeConnectionNotForMaps != null) {
         this.storeConnectionNotForMaps.close();
      }

   }

   public void clean() throws SAFStoreException {
      this.delete(this.SAStoreHandle);
      this.delete(this.RAStoreHandle);
      synchronized(this.conversationInfos) {
         Iterator var2 = this.conversationStoreHandles.values().iterator();

         while(var2.hasNext()) {
            PersistentHandle var3 = (PersistentHandle)var2.next();
            this.delete(var3);
         }

      }
   }

   public void addAgent(Agent var1) throws SAFStoreException {
      if (SAFDebug.SAFStore.isDebugEnabled()) {
         SAFDebug.SAFStore.debug(" == BEFORE ADDING Agent " + var1.getName() + " and store's agentName = " + this.agentName);
      }

      PersistentHandle var2 = this.storeAgent(var1);
      if (var1 instanceof SendingAgent) {
         this.sendingAgent = (SendingAgent)var1;
         this.SAStoreHandle = var2;
      } else if (var1 instanceof ReceivingAgent) {
         this.receivingAgent = (ReceivingAgent)var1;
         this.RAStoreHandle = var2;
      }

      if (SAFDebug.SAFStore.isDebugEnabled()) {
         SAFDebug.SAFStore.debug(" == AFTER ADDING Agent " + var1);
      }

   }

   public void addConversationInfo(SAFConversationInfo var1) throws SAFStoreException {
      synchronized(this.conversationInfos) {
         PersistentHandle var2;
         if (this.conversationInfos.get(var1.getConversationName()) == null) {
            var2 = this.storeSync(var1);
            this.conversationInfos.put(var1.getConversationName(), var1);
            this.conversationStoreHandles.put(var1, var2);
         } else {
            var2 = (PersistentHandle)this.conversationStoreHandles.get(var1);
            this.updateSync(var2, var1);
         }

      }
   }

   public void removeConversationInfo(SAFConversationInfo var1) throws SAFStoreException {
      synchronized(this.conversationInfos) {
         if (this.conversationInfos.remove(var1.getConversationName()) != null) {
            this.delete((PersistentHandle)this.conversationStoreHandles.remove(var1));
         }

      }
   }

   private PersistentHandle storeAgent(Agent var1) throws SAFStoreException {
      return this.storeSync(var1);
   }

   private PersistentHandle storeInternal(Object var1, CompletionRequest var2) {
      PersistentStoreTransaction var3 = this.store.begin();
      PersistentHandle var4 = this.storeConnectionNotForMaps.create(var3, var1, 0);
      var3.commit(var2);
      return var4;
   }

   private void updateInternal(PersistentHandle var1, Object var2, CompletionRequest var3) {
      PersistentStoreTransaction var4 = this.store.begin();
      this.storeConnectionNotForMaps.update(var4, var1, var2, 0);
      var4.commit(var3);
   }

   private PersistentHandle storeSync(Object var1) throws SAFStoreException {
      CompletionRequest var2 = new CompletionRequest();
      PersistentHandle var3 = this.storeInternal(var1, var2);

      try {
         var2.getResult();
         return var3;
      } catch (Throwable var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof Error) {
            throw (Error)var5;
         } else {
            throw new SAFStoreException(this, var5);
         }
      }
   }

   private void updateSync(PersistentHandle var1, Object var2) throws SAFStoreException {
      CompletionRequest var3 = new CompletionRequest();
      this.updateInternal(var1, var2, var3);

      try {
         var3.getResult();
      } catch (Throwable var5) {
         if (var5 instanceof RuntimeException) {
            throw (RuntimeException)var5;
         } else if (var5 instanceof Error) {
            throw (Error)var5;
         } else {
            throw new SAFStoreException(this, var5);
         }
      }
   }

   private void delete(PersistentHandle var1) throws SAFStoreException {
      PersistentStoreTransaction var2 = this.store.begin();
      this.storeConnectionNotForMaps.delete(var2, var1, 0);

      try {
         var2.commit();
      } catch (PersistentStoreException var4) {
         throw new SAFStoreException(this, var4);
      }
   }
}
