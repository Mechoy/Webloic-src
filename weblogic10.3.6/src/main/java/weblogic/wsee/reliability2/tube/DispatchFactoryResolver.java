package weblogic.wsee.reliability2.tube;

import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.server.WSEndpointImpl;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.WseeRmLogger;
import weblogic.wsee.jaxws.spi.ClientIdentityRegistry;
import weblogic.wsee.jaxws.tubeline.TubelineSpliceFactory;

public class DispatchFactoryResolver {
   private static final ReentrantReadWriteLock _clientDispatchFactoryRegistryLock = new ReentrantReadWriteLock(false);
   private static final Map<String, SpliceDispatchFactoryHandle<TubelineSpliceFactory.ClientDispatchFactory>> _clientDispatchFactoryRegistry = new HashMap();
   private static final ReentrantReadWriteLock _serverDispatchFactoryRegistryLock = new ReentrantReadWriteLock(false);
   private static final Map<String, SpliceDispatchFactoryHandle<TubelineSpliceFactory.DispatchFactory>> _serverDispatchFactoryRegistry = new HashMap();
   private static final Logger LOGGER = Logger.getLogger(DispatchFactoryResolver.class.getName());

   public static void registerClientDispatchFactory(String var0, TubelineSpliceFactory.ClientDispatchFactory var1) {
      try {
         _clientDispatchFactoryRegistryLock.writeLock().lock();
         SpliceDispatchFactoryHandle var2 = (SpliceDispatchFactoryHandle)_clientDispatchFactoryRegistry.get(var0);
         if (var2 == null) {
            var2 = new SpliceDispatchFactoryHandle(var0);
         }

         var2.registerFactory(var1);
         _clientDispatchFactoryRegistry.put(var0, var2);
      } finally {
         _clientDispatchFactoryRegistryLock.writeLock().unlock();
      }

   }

   public static TubelineSpliceFactory.ClientDispatchFactory getClientDispatchFactory(String var0) {
      TubelineSpliceFactory.ClientDispatchFactory var2;
      try {
         _clientDispatchFactoryRegistryLock.readLock().lock();
         SpliceDispatchFactoryHandle var1 = (SpliceDispatchFactoryHandle)_clientDispatchFactoryRegistry.get(var0);
         var2 = var1 != null ? (TubelineSpliceFactory.ClientDispatchFactory)var1.getFactory() : null;
      } finally {
         _clientDispatchFactoryRegistryLock.readLock().unlock();
      }

      return var2;
   }

   public static TubelineSpliceFactory.ClientDispatchFactory unregisterClientDispatchFactory(String var0) {
      TubelineSpliceFactory.ClientDispatchFactory var2;
      try {
         _clientDispatchFactoryRegistryLock.writeLock().lock();
         SpliceDispatchFactoryHandle var1 = (SpliceDispatchFactoryHandle)_clientDispatchFactoryRegistry.get(var0);
         if (var1 != null) {
            if (var1.unregisterFactory()) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Un-registered ClientDispatchFactory for client '" + var0 + "' resulting in 0 refCount. Physically removing this DispatchFactory");
               }

               _clientDispatchFactoryRegistry.remove(var0);
            }

            var2 = (TubelineSpliceFactory.ClientDispatchFactory)var1.getFactory();
            return var2;
         }

         var2 = null;
      } finally {
         _clientDispatchFactoryRegistryLock.writeLock().unlock();
      }

      return var2;
   }

   public static void registerServerDispatchFactory(String var0, TubelineSpliceFactory.DispatchFactory var1) {
      try {
         _serverDispatchFactoryRegistryLock.writeLock().lock();
         SpliceDispatchFactoryHandle var2 = (SpliceDispatchFactoryHandle)_serverDispatchFactoryRegistry.get(var0);
         if (var2 == null) {
            var2 = new SpliceDispatchFactoryHandle(var0);
         }

         var2.registerFactory(var1);
         _serverDispatchFactoryRegistry.put(var0, var2);
      } finally {
         _serverDispatchFactoryRegistryLock.writeLock().unlock();
      }

   }

   public static TubelineSpliceFactory.DispatchFactory getServerDispatchFactory(String var0) {
      TubelineSpliceFactory.DispatchFactory var2;
      try {
         _serverDispatchFactoryRegistryLock.readLock().lock();
         SpliceDispatchFactoryHandle var1 = (SpliceDispatchFactoryHandle)_serverDispatchFactoryRegistry.get(var0);
         var2 = var1 != null ? var1.getFactory() : null;
      } finally {
         _serverDispatchFactoryRegistryLock.readLock().unlock();
      }

      return var2;
   }

   public static TubelineSpliceFactory.DispatchFactory unregisterServerDispatchFactory(String var0) {
      TubelineSpliceFactory.DispatchFactory var2;
      try {
         _serverDispatchFactoryRegistryLock.writeLock().lock();
         SpliceDispatchFactoryHandle var1 = (SpliceDispatchFactoryHandle)_serverDispatchFactoryRegistry.get(var0);
         if (var1 == null) {
            var2 = null;
            return var2;
         }

         if (var1.unregisterFactory()) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Un-registered DispatchFactory for endpoint '" + var0 + "' resulting in 0 refCount. Physically removing this DispatchFactory");
            }

            _serverDispatchFactoryRegistry.remove(var0);
         }

         var2 = var1.getFactory();
      } finally {
         _serverDispatchFactoryRegistryLock.writeLock().unlock();
      }

      return var2;
   }

   public static class ServerSideKey extends BaseKey {
      private static final long serialVersionUID = 1L;

      public ServerSideKey() {
      }

      public ServerSideKey(String var1) {
         super(var1);
      }

      protected void startListening() {
      }

      protected void stopListening() {
      }

      public DispatchFactory resolve() {
         WSEndpointImpl var1 = this.resolveEndpoint();
         WsrmServerDispatchFactory var2 = new WsrmServerDispatchFactory(var1);
         return var2;
      }

      public TubelineSpliceFactory.DispatchFactory resolveForSplice() {
         this.resolveEndpoint();
         return DispatchFactoryResolver.getServerDispatchFactory((String)this._id);
      }

      private WSEndpointImpl resolveEndpoint() {
         WSEndpointImpl var1 = (WSEndpointImpl)WSEndpoint.getEndpoint((String)this._id);
         if (var1 == null) {
            throw new IllegalStateException("No WSEndpoint with id: " + this._id);
         } else {
            return var1;
         }
      }
   }

   public static class ClientSideKey extends BaseKey {
      private static final long serialVersionUID = 1L;
      private transient ClientIdentityRegistry.Listener _clientIdListener;

      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         var1.defaultReadObject();
         this.initTransients();
      }

      public ClientSideKey() {
      }

      public ClientSideKey(String var1) {
         super(var1);
         this.initTransients();
      }

      private void initTransients() {
         this._clientIdListener = new ClientIdentityRegistry.Listener() {
            public void clientIdentityRegistered(String var1) {
            }

            public void clientIdentityBeforeUnregister(String var1) {
               ClientSideKey.this.notifyDispose();
            }
         };
      }

      protected void startListening() {
         ClientIdentityRegistry.addClientIdentityListener((String)this._id, this._clientIdListener);
      }

      protected void stopListening() {
         ClientIdentityRegistry.removeClientIdentityListener((String)this._id, this._clientIdListener);
      }

      public DispatchFactory resolve() {
         this.verifyClientIdentity();
         WsrmClientDispatchFactory var1 = new WsrmClientDispatchFactory((String)this._id);
         return var1;
      }

      public TubelineSpliceFactory.ClientDispatchFactory resolveForSplice() {
         this.verifyClientIdentity();
         TubelineSpliceFactory.ClientDispatchFactory var1 = DispatchFactoryResolver.getClientDispatchFactory((String)this._id);
         return var1;
      }

      private void verifyClientIdentity() {
         ClientIdentityRegistry.ClientInfo var1 = ClientIdentityRegistry.getClientInfo((String)this._id);
         if (var1 == null) {
            throw new DispatchFactoryNotReadyException("No client with client identity '" + this._id + "' has been registered yet");
         } else if (!var1.isInitialized()) {
            throw new DispatchFactoryNotReadyException("Client identity '" + this._id + "' has been registered, but is not yet fully initialized");
         }
      }
   }

   private abstract static class BaseKey implements Key {
      private static final long serialVersionUID = 1L;
      private transient List<LifecycleListener> _listeners;
      protected transient String _serializableVersion;
      protected Serializable _id;

      private void writeObject(ObjectOutputStream var1) throws IOException {
         this._serializableVersion = "10.3.6";
         var1.writeObject(this._serializableVersion);
         var1.defaultWriteObject();
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         this._serializableVersion = (String)var1.readObject();
         var1.defaultReadObject();
      }

      public BaseKey() {
      }

      protected BaseKey(Serializable var1) {
         this._id = var1;
      }

      public Serializable getId() {
         return this._id;
      }

      protected abstract void startListening();

      protected abstract void stopListening();

      public void addLifecycleListener(LifecycleListener var1) {
         synchronized(this) {
            if (this._listeners == null) {
               this._listeners = new ArrayList();
            }

            if (!this._listeners.contains(var1)) {
               this._listeners.add(var1);
               if (DispatchFactoryResolver.LOGGER.isLoggable(Level.FINE)) {
                  DispatchFactoryResolver.LOGGER.fine("DispatchFactory.Key " + this + " added LifecycleListener " + var1 + ". Current listener count: " + this._listeners.size());
               }

               if (this._listeners.size() == 1) {
                  this.startListening();
               }
            }

         }
      }

      public void removeLifecycleListener(LifecycleListener var1) {
         synchronized(this) {
            if (this._listeners != null) {
               this._listeners.remove(var1);
               if (DispatchFactoryResolver.LOGGER.isLoggable(Level.FINE)) {
                  DispatchFactoryResolver.LOGGER.fine("DispatchFactory.Key " + this + " removed LifecycleListener " + var1 + ". Remaining listener count: " + this._listeners.size());
               }

               if (this._listeners.size() < 1) {
                  this.stopListening();
                  this._listeners = null;
               }

            }
         }
      }

      protected void notifyDispose() {
         LifecycleListener[] var1;
         synchronized(this) {
            if (this._listeners == null || this._listeners.size() < 1) {
               return;
            }

            var1 = (LifecycleListener[])this._listeners.toArray(new LifecycleListener[this._listeners.size()]);
         }

         DispatchFactory var2;
         try {
            var2 = this.resolve();
         } catch (Exception var9) {
            WseeRmLogger.logUnexpectedException(var9.toString(), var9);
            return;
         }

         LifecycleListener[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            LifecycleListener var6 = var3[var5];

            try {
               var6.onDispatchFactoryDispose(var2);
            } catch (Exception var8) {
               WseeRmLogger.logUnexpectedException(var8.toString(), var8);
            }
         }

      }

      public String toString() {
         StringBuffer var1 = new StringBuffer(super.toString());
         var1.append(": ").append(this._id);
         return var1.toString();
      }
   }

   public interface Key extends Serializable {
      Serializable getId();

      DispatchFactory resolve();

      TubelineSpliceFactory.DispatchFactory resolveForSplice();

      void addLifecycleListener(LifecycleListener var1);

      void removeLifecycleListener(LifecycleListener var1);
   }

   public interface LifecycleListener {
      void onDispatchFactoryDispose(DispatchFactory var1);
   }

   private static class SpliceDispatchFactoryHandle<T extends TubelineSpliceFactory.DispatchFactory> {
      private String _endpointId;
      private int _refCount;
      private T _factory;

      public SpliceDispatchFactoryHandle(String var1) {
         this._endpointId = var1;
         this._refCount = 0;
      }

      public T registerFactory(T var1) {
         TubelineSpliceFactory.DispatchFactory var2 = this._factory;
         this._factory = var1;
         ++this._refCount;
         if (DispatchFactoryResolver.LOGGER.isLoggable(Level.FINE)) {
            DispatchFactoryResolver.LOGGER.fine("Registered " + this.getFactoryTypeDesc() + " for ID '" + this._endpointId + "' resulting in refCount=" + this._refCount);
         }

         return var2;
      }

      public boolean unregisterFactory() {
         --this._refCount;
         if (DispatchFactoryResolver.LOGGER.isLoggable(Level.FINE)) {
            DispatchFactoryResolver.LOGGER.fine("Un-registered " + this.getFactoryTypeDesc() + " for ID '" + this._endpointId + "' resulting in refCount=" + this._refCount);
         }

         return this._refCount < 1;
      }

      public T getFactory() {
         return this._factory;
      }

      public int getRefCount() {
         return this._refCount;
      }

      public String getFactoryTypeDesc() {
         return this._factory.getClass().getSimpleName();
      }
   }
}
