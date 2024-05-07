package weblogic.wsee.sender.DefaultProvider;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.WseeSenderLogger;
import weblogic.wsee.sender.api.ConversationNotFoundException;
import weblogic.wsee.sender.api.Preferences;
import weblogic.wsee.sender.api.Resources;
import weblogic.wsee.sender.api.SendException;
import weblogic.wsee.sender.api.SendRequest;
import weblogic.wsee.sender.api.SendingServiceException;
import weblogic.wsee.sender.spi.SendingServiceProvider;

public class DefaultSendingServiceProvider implements SendingServiceProvider {
   private static final Logger LOGGER = Logger.getLogger(DefaultSendingServiceProvider.class.getName());
   private Preferences _prefs;
   private String _logicalStoreName;
   private RequestStore _store;
   private Map<String, Conversation> _convMap;
   private final ReentrantReadWriteLock _convMapLock = new ReentrantReadWriteLock(false);

   public DefaultSendingServiceProvider(Preferences var1) throws SendingServiceException {
      this._prefs = var1;
      if (this._prefs != null && this._prefs.getLogicalStoreName() != null) {
         this._logicalStoreName = this._prefs.getLogicalStoreName();
      } else {
         this._logicalStoreName = null;
      }

      this.initStore();
   }

   private void initStore() throws SendingServiceException {
      this._convMap = new HashMap();

      try {
         this._store = RequestStore.getStore(this._logicalStoreName);
      } catch (Exception var2) {
         throw new SendingServiceException(var2.toString(), var2);
      }
   }

   public void addConversation(String var1, Resources var2) throws SendingServiceException {
      this.internalAddConversation(var1, var2);
   }

   public void internalAddConversation(String var1, Resources var2) throws SendingServiceException {
      try {
         this._convMapLock.writeLock().lock();
         if (this._convMap.containsKey(var1)) {
            throw new ConversationNotFoundException(WseeSenderLogger.logConversationExistsLoggable(var1).getMessage());
         }

         Conversation var3 = new Conversation(var1, this._store, var2);
         this._convMap.put(var1, var3);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("DefaultSendingServiceProvider new conversation count: " + this._convMap.size());
            this.dumpConversations();
         }

         var3.startSending();
      } finally {
         this._convMapLock.writeLock().unlock();
      }

   }

   public void continueConversation(String var1, Resources var2) throws SendingServiceException {
      try {
         this._convMapLock.writeLock().lock();
         if (this._convMap.containsKey(var1)) {
            Conversation var3 = (Conversation)this._convMap.get(var1);
            var3.stop();
            this._convMap.remove(var1);
         }

         this.internalAddConversation(var1, var2);
      } catch (SendException var8) {
         throw new SendingServiceException(var8.toString(), var8);
      } finally {
         this._convMapLock.writeLock().unlock();
      }

   }

   public void cancelConversation(String var1) throws SendingServiceException {
      try {
         Conversation var2 = this.getConversation(var1);
         var2.cancelConversation();
      } catch (Exception var3) {
         throw new SendingServiceException(var3.toString(), var3);
      }

      this.removeConversationFromMap(var1);
   }

   private void removeConversationFromMap(String var1) {
      try {
         this._convMapLock.writeLock().lock();
         this._convMap.remove(var1);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("DefaultSendingServiceProvider remaining conversation count: " + this._convMap.size());
            this.dumpConversations();
         }
      } finally {
         this._convMapLock.writeLock().unlock();
      }

   }

   public boolean conversationExists(String var1) throws SendingServiceException {
      boolean var2;
      try {
         this._convMapLock.readLock().lock();
         var2 = this._convMap.containsKey(var1);
      } finally {
         this._convMapLock.readLock().unlock();
      }

      return var2;
   }

   public void addRequest(String var1, SendRequest var2) throws SendingServiceException {
      Conversation var3 = this.getConversation(var1);
      var3.addRequest(var2);
   }

   private void dumpConversations() {
      if (LOGGER.isLoggable(Level.FINER)) {
         Set var1 = this._convMap.keySet();
         StringBuffer var2 = new StringBuffer();
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Conversation var5 = (Conversation)this._convMap.get(var4);
            if (var5 != null) {
               var2.append("   ").append(var4).append(": ").append(var5.getState()).append("\n");
            }
         }

         LOGGER.finer("Current Conversations:\n" + var2.toString());
      }

   }

   public Conversation getConversation(String var1) throws SendingServiceException {
      Conversation var3;
      try {
         this._convMapLock.readLock().lock();
         Conversation var2 = (Conversation)this._convMap.get(var1);
         if (var2 == null) {
            throw new ConversationNotFoundException(WseeSenderLogger.logConversationNotFoundLoggable(var1).getMessage());
         }

         var3 = var2;
      } finally {
         this._convMapLock.readLock().unlock();
      }

      return var3;
   }

   public List<Long> getPendingRequests(String var1) throws SendingServiceException {
      Conversation var2 = this.getConversation(var1);
      return var2.getPendingRequestSeqNums();
   }

   public SendRequest getRequestBySequenceNumber(String var1, long var2) throws SendingServiceException {
      Conversation var4 = this.getConversation(var1);
      return var4.getRequestBySequenceNumber(var2);
   }

   public SendRequest getRequestByMessageID(String var1) throws SendingServiceException {
      try {
         SendRequest var2 = (SendRequest)this._store.get(var1);
         if (var2 == null) {
            throw new SendingServiceException(WseeSenderLogger.logSendRequestNotFoundLoggable(var1).getMessage());
         } else {
            return var2;
         }
      } catch (Exception var3) {
         throw new SendingServiceException(var3.toString(), var3);
      }
   }

   public void acknowledgeRequests(String var1, long var2, long var4) throws SendingServiceException {
      Conversation var6 = this.getConversation(var1);
      var6.acknowledgeRequests(var2, var4);
   }

   public void closeConversation(String var1) throws SendingServiceException {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SendingService closing conversation: " + var1);
         }

         Conversation var2 = this.getConversation(var1);
         var2.closeConversation();
      } catch (SendingServiceException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new SendingServiceException(var4.toString(), var4);
      }

      this.removeConversationFromMap(var1);
   }

   public void stopConversation(String var1) throws SendingServiceException {
      try {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("SendingService stopping conversation: " + var1);
         }

         Conversation var2 = this.getConversation(var1);
         var2.stop();
      } catch (SendingServiceException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new SendingServiceException(var4.toString(), var4);
      }

      this.removeConversationFromMap(var1);
   }

   public void stop() throws SendingServiceException {
      try {
         this._convMapLock.readLock().lock();
         Iterator var1 = this._convMap.values().iterator();

         while(var1.hasNext()) {
            Conversation var2 = (Conversation)var1.next();
            var2.stop();
         }
      } catch (Exception var7) {
         throw new SendingServiceException(var7.toString(), var7);
      } finally {
         this._convMapLock.readLock().unlock();
      }

   }

   public Preferences getPreferences() {
      return this._prefs;
   }
}
