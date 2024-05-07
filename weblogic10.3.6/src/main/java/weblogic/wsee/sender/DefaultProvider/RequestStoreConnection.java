package weblogic.wsee.sender.DefaultProvider;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.persistence.Storable;
import weblogic.wsee.persistence.StoreConnection;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.sender.api.SendRequest;

public final class RequestStoreConnection extends StoreConnection<String, SendRequest> {
   private static final Logger LOGGER = Logger.getLogger(RequestStoreConnection.class.getName());
   private Map<String, List<SendRequest>> _requestsByConvName;
   private ReentrantReadWriteLock _requestLock;
   private Map<String, TreeSet<SendRequest>> _recoveredRequestsByConvName;

   RequestStoreConnection(String var1, String var2) throws StoreException {
      super(var1, var2);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == RequestStoreConnection being created for " + var1 + " and connection " + var2);
      }

      this._requestsByConvName = new HashMap();
      this._requestLock = new ReentrantReadWriteLock(false);
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine(" == RequestStoreConnection Created for " + var1 + " and connection " + var2);
      }

   }

   protected void recoveryStarting() {
      this._recoveredRequestsByConvName = new HashMap();
   }

   protected void recoverValue(SendRequest var1) {
      super.recoverValue(var1);
      TreeSet var2 = (TreeSet)this._recoveredRequestsByConvName.get(var1.getConversationName());
      if (var2 == null) {
         var2 = new TreeSet();
         this._recoveredRequestsByConvName.put(var1.getConversationName(), var2);
      }

      var2.add(var1);
   }

   protected void recoveryComplete() {
      try {
         this._requestLock.writeLock().lock();

         String var2;
         TreeSet var3;
         for(Iterator var1 = this._recoveredRequestsByConvName.keySet().iterator(); var1.hasNext(); this._requestsByConvName.put(var2, new LinkedList(var3))) {
            var2 = (String)var1.next();
            var3 = (TreeSet)this._recoveredRequestsByConvName.get(var2);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("RequestStoreConnection recovered " + var3.size() + " requests for conversation: " + var2);
            }
         }
      } finally {
         this._requestLock.writeLock().unlock();
      }

      this._recoveredRequestsByConvName = null;
   }

   public SendRequest put(String var1, SendRequest var2) {
      SendRequest var4;
      try {
         this._requestLock.writeLock().lock();
         SendRequest var3 = (SendRequest)super.put((Serializable)var1, (Storable)var2);
         this.internalAddRequest(var2);
         var4 = var3;
      } finally {
         this._requestLock.writeLock().unlock();
      }

      return var4;
   }

   public SendRequest put(SendRequest var1) throws StoreException {
      return this.put(var1.getMessageId(), var1);
   }

   private void internalAddRequest(SendRequest var1) {
      Object var2 = (List)this._requestsByConvName.get(var1.getConversationName());
      if (var2 == null) {
         var2 = new LinkedList();
         this._requestsByConvName.put(var1.getConversationName(), var2);
      }

      ((List)var2).add(var1);
   }

   public SendRequest get(String var1, long var2) {
      SendRequest var7;
      try {
         this._requestLock.readLock().lock();
         List var4 = (List)this._requestsByConvName.get(var1);
         Iterator var5;
         if (var4 == null) {
            var5 = null;
            return var5;
         }

         var5 = var4.iterator();

         SendRequest var6;
         do {
            if (!var5.hasNext()) {
               var5 = null;
               return var5;
            }

            var6 = (SendRequest)var5.next();
         } while(var6.getSequenceNumber() != var2);

         var7 = var6;
      } finally {
         this._requestLock.readLock().unlock();
      }

      return var7;
   }

   @NotNull
   public List<SendRequest> getPendingRequests(String var1) {
      ArrayList var3;
      try {
         this._requestLock.readLock().lock();
         List var2 = (List)this._requestsByConvName.get(var1);
         if (var2 == null) {
            var3 = new ArrayList();
            return var3;
         }

         var3 = new ArrayList(var2);
      } finally {
         this._requestLock.readLock().unlock();
      }

      return var3;
   }

   public SendRequest remove(Object var1) {
      SendRequest var2 = (SendRequest)super.remove(var1);
      if (var2 != null) {
         this.internalRemove(var2);
         return var2;
      } else {
         return null;
      }
   }

   public boolean removeAllPendingRequests(String var1) throws StoreException {
      try {
         this._requestLock.writeLock().lock();
         List var2 = (List)this._requestsByConvName.get(var1);
         boolean var10;
         if (var2 == null) {
            var10 = false;
            return var10;
         } else {
            ArrayList var9 = new ArrayList(var2);
            Iterator var3 = var9.iterator();

            while(var3.hasNext()) {
               SendRequest var4 = (SendRequest)var3.next();
               this.remove(var4);
            }

            var10 = true;
            return var10;
         }
      } finally {
         this._requestLock.writeLock().unlock();
      }
   }

   public boolean remove(SendRequest var1) throws StoreException {
      boolean var2;
      try {
         this._requestLock.writeLock().lock();
         var2 = super.remove(var1.getMessageId()) != null && this.internalRemove(var1);
      } finally {
         this._requestLock.writeLock().unlock();
      }

      return var2;
   }

   private boolean internalRemove(SendRequest var1) {
      List var2 = (List)this._requestsByConvName.get(var1.getConversationName());
      if (var2 == null) {
         return false;
      } else {
         var2.remove(var1);
         if (var2.isEmpty()) {
            this._requestsByConvName.remove(var1.getConversationName());
         }

         return true;
      }
   }
}
