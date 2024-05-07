package weblogic.wsee.jaxws.spi;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.Closeable;
import com.sun.xml.ws.client.ResponseContext;
import com.sun.xml.ws.client.Stub;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceFeature;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.jaxws.persistence.ClientInstancePoolFeature;
import weblogic.wsee.jaxws.persistence.ClientInstanceProperties;
import weblogic.wsee.jaxws.persistence.ClientInstancePropertiesStore;
import weblogic.wsee.jaxws.persistence.PersistenceConfig;
import weblogic.wsee.persistence.StoreException;

public class ClientInstancePool<T> implements ClientInstance.InstanceReleaser<T> {
   private static final Logger LOGGER = Logger.getLogger(ClientInstancePool.class.getName());
   private ClientIdentityFeature _parentFeature;
   private boolean _durable;
   private boolean _initialized;
   private PortInfoBean _portInfoBean;
   private InstanceFactory _factory;
   private Class<T> _instanceType;
   private ClientInstancePoolFeature _poolFeature;
   private int _capacity;
   private ReentrantReadWriteLock _poolLock = new ReentrantReadWriteLock(false);
   private Condition _poolWriteLockFreeClientsCondition;
   private int _pooledClientTakeCount;
   private int _conversationalClientTakeCount;
   private int _simpleClientCreateCount;
   private List<ClientInstance<T>> _freeClients;
   private List<ClientInstance<T>> _takenClients;
   private Map<ClientInstanceIdentity, ClientInstanceProperties> _nonDurableConvClients;
   private ClientInstancePropertiesStore _durableConvClients;

   public ClientInstancePool(ClientIdentityFeature var1, @Nullable PortInfoBean var2, @NotNull ClientInstancePoolFeature var3, @NotNull InstanceFactory var4, @NotNull Class<T> var5) throws StoreException {
      this._parentFeature = var1;
      this._initialized = false;
      this._portInfoBean = var2;
      this._poolFeature = var3;
      this._factory = var4;
      this._instanceType = var5;
      this._poolWriteLockFreeClientsCondition = this._poolLock.writeLock().newCondition();
      this.initialize();
   }

   private void initialize() throws StoreException {
      try {
         this._poolLock.writeLock().lock();
         if (!this._initialized) {
            if (this._factory != null && this._instanceType != null && this._poolFeature != null) {
               this._capacity = this._poolFeature.getCapacity();
               this._durable = this._poolFeature.isDurable();
               if (this._durable) {
                  PersistenceConfig.Client var1 = PersistenceConfig.getClientConfig(this._portInfoBean);
                  String var2 = var1.getLogicalStoreName();
                  if (var2 == null) {
                     return;
                  }

                  this._durableConvClients = ClientInstancePropertiesStore.getStore(var2);
               } else {
                  this._nonDurableConvClients = new HashMap();
               }

               this._freeClients = new ArrayList(this._capacity);
               this._takenClients = new ArrayList(this._capacity);
               this.fillPool();
               this._initialized = true;
               return;
            }

            throw new IllegalArgumentException("Missing required args (factory, instanceType or poolFeature");
         }
      } finally {
         this._poolLock.writeLock().unlock();
      }

   }

   private void fillPool() {
      try {
         this._poolLock.writeLock().lock();
         if (this._factory == null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("DurableClient pool not initialized because it is not yet required. Client ID: " + this._parentFeature.getClientId());
            }

            return;
         }

         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Client instance pool is being initialized (capacity=" + this._capacity + "). Client ID: " + this._parentFeature.getClientId());
         }

         while(this._freeClients.size() + this._takenClients.size() < this._capacity) {
            int var1 = this._freeClients.size() + this._takenClients.size() + 1;
            ClientInstanceIdentity var2 = new ClientInstanceIdentity(this._parentFeature.getClientId(), ClientInstanceIdentity.Type.POOLED, var1);
            ClientInstance var3 = this._factory.createClientInstance(var2, this._instanceType, this);
            this._freeClients.add(var3);
         }
      } finally {
         this._poolLock.writeLock().unlock();
      }

   }

   public boolean isInitialized() {
      boolean var1;
      try {
         this._poolLock.readLock().lock();
         var1 = this._initialized;
      } finally {
         this._poolLock.readLock().unlock();
      }

      return var1;
   }

   public PoolStats getStats() {
      PoolStats var2;
      try {
         this._poolLock.readLock().lock();
         PoolStats var1 = new PoolStats();
         var1._capacity = this._capacity;
         var1._freeCount = this._freeClients.size();
         var1._takenCount = this._takenClients.size();
         var1._pooledClientTakeCount = this._pooledClientTakeCount;
         var1._conversationalClientTakeCount = this._conversationalClientTakeCount;
         var1._simpleClientCreateCount = this._simpleClientCreateCount;
         var2 = var1;
      } finally {
         this._poolLock.readLock().unlock();
      }

      return var2;
   }

   static Map<Class, WebServiceFeature> getFeatureMap(WebServiceFeature... var0) {
      HashMap var1 = new HashMap();
      WebServiceFeature[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WebServiceFeature var5 = var2[var4];
         var1.put(var5.getClass(), var5);
      }

      return var1;
   }

   static WebServiceFeature[] getFeatureArray(Map<Class, WebServiceFeature> var0) {
      return (WebServiceFeature[])var0.values().toArray(new WebServiceFeature[var0.size()]);
   }

   public ClientInstance take(@Nullable ClientInstanceIdentity var1, ClientInstance.CreationInfo var2) {
      ClientInstance var3;
      if (var1 != null && var1.getType() == ClientInstanceIdentity.Type.CONVERSATIONAL) {
         var3 = this.takeConversationalClientInstance(var1, var2);
      } else {
         boolean var4;
         try {
            this._poolLock.readLock().lock();
            var4 = this._capacity > 0;
         } finally {
            this._poolLock.readLock().unlock();
         }

         if (var4) {
            var3 = this.takePooledClientInstance();
         } else {
            var3 = this.takeSimpleClientInstance(var1, var2);
         }
      }

      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Took ClientInstance of type " + var3.getId().getType() + ": " + var3);
      }

      return var3;
   }

   private ClientInstance takePooledClientInstance() {
      ClientInstance var3;
      try {
         this._poolLock.writeLock().lock();
         boolean var1 = false;

         while(this._freeClients.isEmpty()) {
            var1 = true;
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("ClientIdentityFeature pool (" + this._parentFeature.getClientId() + ") ran out of durable clients instances. Waiting as one of " + this._poolLock.getWaitQueueLength(this._poolWriteLockFreeClientsCondition) + " waiting threads");
            }

            try {
               this._poolWriteLockFreeClientsCondition.await(30000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException var10) {
            }
         }

         if (var1) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("ClientIdentityFeature pool (" + this._parentFeature.getClientId() + ") has a newly available pooled client instance. Waking up to use newly available instance. Available clients=" + this._freeClients.size() + " numWaiters=" + this._poolLock.getWaitQueueLength(this._poolWriteLockFreeClientsCondition));
            }
         } else if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("ClientIdentityFeature pool (" + this._parentFeature.getClientId() + ") had an available pooled client instance. Available clients=" + this._freeClients.size() + " takenClients=" + this._takenClients.size());
         }

         ClientInstance var2 = (ClientInstance)this._freeClients.remove(0);
         synchronized(var2) {
            var2.activate();
         }

         this._takenClients.add(var2);
         ++this._pooledClientTakeCount;
         var3 = var2;
      } finally {
         this._poolLock.writeLock().unlock();
      }

      return var3;
   }

   private ClientInstance takeConversationalClientInstance(@NotNull ClientInstanceIdentity var1, ClientInstance.CreationInfo var2) {
      ClientInstance var6;
      try {
         this._poolLock.writeLock().lock();
         ClientInstanceProperties var3 = null;
         if (this._durable) {
            Map var4 = this._durableConvClients.getByClientId(var1.getClientId());
            if (var4 != null) {
               var3 = (ClientInstanceProperties)var4.get(var1.getExtraId());
            }

            if (var3 == null) {
               var3 = new ClientInstanceProperties(var1);
               this._durableConvClients.put(var1, var3);
            }
         } else {
            var3 = (ClientInstanceProperties)this._nonDurableConvClients.get(var1);
            if (var3 == null) {
               var3 = new ClientInstanceProperties(var1);
               this._nonDurableConvClients.put(var1, var3);
            }
         }

         ClientInstance var12 = var3.getClientInstance();
         if (var12 != null) {
            synchronized(var12) {
               if (!var12.isActive()) {
                  var12.activate();
               }
            }

            ++this._conversationalClientTakeCount;
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Took conversational client instance '" + var1 + "' from pool");
            }

            ClientInstance var13 = var12;
            return var13;
         }

         ClientInstanceIdentity var5 = new ClientInstanceIdentity(this._parentFeature.getClientId(), ClientInstanceIdentity.Type.CONVERSATIONAL, var1.getExtraId());
         var12 = this._factory.createClientInstance(var5, var2.getInstanceType(), this, var2);
         var12.setDurable(this._durable, this._durable ? this._durableConvClients.getName() : null);
         var12.setProps(var3.getPropertyMap());
         var3.setClientInstance(var12);
         var12.activate();
         ++this._conversationalClientTakeCount;
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Took (and created) conversational client instance '" + var1 + "' from pool");
         }

         var6 = var12;
      } finally {
         this._poolLock.writeLock().unlock();
      }

      return var6;
   }

   private ClientInstance takeSimpleClientInstance(@Nullable ClientInstanceIdentity var1, ClientInstance.CreationInfo var2) {
      if (var1 == null) {
         var1 = ClientIdentityRegistry.generateSimpleClientInstanceIdentity(this._parentFeature.getClientId());
      }

      ClientInstance var3 = this._factory.createClientInstance(var1, var2.getInstanceType(), this, var2);
      var3.activate();

      ClientInstance var4;
      try {
         this._poolLock.writeLock().lock();
         ++this._simpleClientCreateCount;
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Took simple client instance '" + var1 + "' from pool: " + this);
         }

         var4 = var3;
      } finally {
         this._poolLock.writeLock().unlock();
      }

      return var4;
   }

   public void release(ClientInstance<T> var1) {
      if (var1 != null) {
         synchronized(var1) {
            if (!var1.isActive()) {
               return;
            }
         }

         try {
            this._poolLock.writeLock().lock();
            synchronized(var1) {
               var1.deactivate();
               BindingProvider var3 = (BindingProvider)var1.getInstance();
               Stub var4 = this.getStubFromProvider(var3);
               var4.resetRequestContext();
               var4.setResponseContext((ResponseContext)null);
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("ClientIdentityFeature pool (" + this._parentFeature.getClientId() + ") releasing client instance " + var1.getId() + " Available pooled clients=" + this._freeClients.size() + " numWaiters=" + this._poolLock.getWaitQueueLength(this._poolWriteLockFreeClientsCondition) + " Current conv client count: " + (this._durable ? this._durableConvClients.size() : this._nonDurableConvClients.size()));
            }

            if (var1.getId().getType() == ClientInstanceIdentity.Type.CONVERSATIONAL) {
               if (this._durable) {
                  this._durableConvClients.remove(var1.getId());
               } else {
                  this._nonDurableConvClients.remove(var1.getId());
               }

               this.closeCloseableInstance(var1.getInstance());
            } else if (var1.getId().getType() == ClientInstanceIdentity.Type.POOLED) {
               this._takenClients.remove(var1);
               this._freeClients.add(var1);
               this._poolWriteLockFreeClientsCondition.signal();
            } else {
               this.closeCloseableInstance(var1.getInstance());
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("ClientIdentityFeature pool (" + this._parentFeature.getClientId() + ") RELEASED client instance " + var1.getId() + " Available pooled clients=" + this._freeClients.size() + " numWaiters=" + this._poolLock.getWaitQueueLength(this._poolWriteLockFreeClientsCondition) + " Current conv client count: " + (this._durable ? this._durableConvClients.size() : this._nonDurableConvClients.size()));
            }
         } finally {
            this._poolLock.writeLock().unlock();
         }

      }
   }

   private Stub getStubFromProvider(BindingProvider var1) {
      return Proxy.isProxyClass(var1.getClass()) ? this.getStubFromProxy(Proxy.getInvocationHandler(var1)) : (Stub)var1;
   }

   private Stub getStubFromProxy(InvocationHandler var1) {
      return Proxy.isProxyClass(var1.getClass()) ? this.getStubFromProxy(Proxy.getInvocationHandler(var1)) : (Stub)var1;
   }

   private void closeCloseableInstance(Object var1) {
      try {
         if (var1 instanceof Closeable) {
            ((Closeable)var1).close();
         } else if (var1 instanceof java.io.Closeable) {
            ((java.io.Closeable)var1).close();
         }
      } catch (Exception var3) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, var3.toString(), var3);
         }

         WseeCoreLogger.logUnexpectedException(var3.toString(), var3);
      }

   }

   public int getCapacity() {
      int var1;
      try {
         this._poolLock.readLock().lock();
         var1 = this._capacity;
      } finally {
         this._poolLock.readLock().unlock();
      }

      return var1;
   }

   public int getFreeSize() {
      int var1;
      try {
         this._poolLock.readLock().lock();
         var1 = this._freeClients.size();
      } finally {
         this._poolLock.readLock().unlock();
      }

      return var1;
   }

   public int getTakenSize() {
      int var1;
      try {
         this._poolLock.readLock().lock();
         var1 = this._freeClients.size();
      } finally {
         this._poolLock.readLock().unlock();
      }

      return var1;
   }

   public void expand(int var1) {
      try {
         this._poolLock.writeLock().lock();
         if (var1 > this._capacity) {
            this._capacity = var1;
            this.fillPool();
            return;
         }
      } finally {
         this._poolLock.writeLock().unlock();
      }

   }

   public void dispose() {
      try {
         this._poolLock.writeLock().lock();
         if (!this._initialized) {
            return;
         }
      } finally {
         this._poolLock.writeLock().unlock();
      }

   }

   public void closeAll() {
      try {
         this._poolLock.writeLock().lock();
         if (!this._initialized) {
            return;
         }

         ClientInstance[] var1 = (ClientInstance[])this._freeClients.toArray(new ClientInstance[this._freeClients.size()]);
         ClientInstance[] var2 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ClientInstance var5 = var2[var4];
            var5.close();
         }
      } finally {
         this._poolLock.writeLock().unlock();
      }

   }

   interface InstanceFactory {
      <T2> ClientInstance<T2> createClientInstance(ClientInstanceIdentity var1, Class<T2> var2, ClientInstance.InstanceReleaser<T2> var3);

      <T2> ClientInstance<T2> createClientInstance(ClientInstanceIdentity var1, Class<T2> var2, ClientInstance.InstanceReleaser<T2> var3, ClientInstance.CreationInfo var4);
   }

   public static class PoolStats {
      private int _capacity;
      private int _freeCount;
      private int _takenCount;
      private int _pooledClientTakeCount;
      private int _conversationalClientTakeCount;
      private int _simpleClientCreateCount;

      public int getCapacity() {
         return this._capacity;
      }

      public int getFreeCount() {
         return this._freeCount;
      }

      public int getTakenCount() {
         return this._takenCount;
      }

      public int getPooledClientTakeCount() {
         return this._pooledClientTakeCount;
      }

      public int getConversationalClientTakeCount() {
         return this._conversationalClientTakeCount;
      }

      public int getSimpleClientCreateCount() {
         return this._simpleClientCreateCount;
      }
   }
}
