package weblogic.wsee.jaxws.spi;

import com.sun.xml.ws.Closeable;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.developer.WSBindingProvider;
import java.io.Serializable;
import java.lang.ref.PhantomReference;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.jaxws.persistence.ClientInstanceProperties;

public class ClientInstance<T> {
   private static final Logger LOGGER = Logger.getLogger(ClientInstance.class.getName());
   private ClientInstanceIdentity _id;
   private CreationInfo _creationInfo;
   private boolean _durable;
   private T _instance;
   private boolean _active;
   private Map<String, Serializable> _props;
   private String _logicalStoreName;
   private Collection<Listener> _listeners = null;
   private PhantomReference<?> _ref;
   private InstanceReleaser _releaser;

   public ClientInstance(ClientInstanceIdentity var1, InstanceReleaser var2, CreationInfo var3, T var4) {
      if (var4 instanceof ClientInstanceProxy) {
         throw new IllegalArgumentException("Error, constructed a ClientInstance using the proxy, instead of the underlying client instance (Port/Dispatch instance)");
      } else {
         this._id = var1;
         this._creationInfo = var3;
         this._instance = var4;
         this._active = false;
         this._durable = false;
         this._ref = null;
         this._releaser = var2;
      }
   }

   public boolean isDurable() {
      return this._durable;
   }

   public void setDurable(boolean var1, String var2) {
      this._durable = var1;
      this._logicalStoreName = var2;
   }

   public Map<String, Serializable> getProps() {
      if (this._props == null) {
         this._props = new HashMap();
      }

      return this._props;
   }

   public void setProps(Map<String, Serializable> var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Props cannot be null");
      } else {
         this._props = var1;
      }
   }

   public void saveProps() {
      if (this._durable) {
         ClientInstanceProperties var1 = (ClientInstanceProperties)ClientInstanceProperties.getStoreMap(this._logicalStoreName).get(this.getId());
         if (var1 == null) {
            var1 = new ClientInstanceProperties(this.getId());
         }

         if (var1.getPropertyMap() != this._props) {
            var1.getPropertyMap().putAll(this._props);
         }

         ClientInstanceProperties.getStoreMap(this._logicalStoreName).put(this.getId(), var1);
      }
   }

   public T createProxyInstance(ReferenceHolderFactory var1) {
      InstanceCloseable var2 = new InstanceCloseable(this);
      ClientInstanceInvocationHandler var3 = new ClientInstanceInvocationHandler(this, var2);
      this._ref = var1.create(var3, var2);
      return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{this._creationInfo.getMode() != null ? Dispatch.class : this._creationInfo.getInstanceType(), WSBindingProvider.class, Closeable.class, ClientInstanceProxy.class}, var3);
   }

   public synchronized void addClientInstanceListener(Listener<T> var1) {
      if (this._listeners == null) {
         this._listeners = new ArrayList();
      }

      if (!this._listeners.contains(var1)) {
         this._listeners.add(var1);
      }

   }

   public synchronized void removeClientInstanceListener(Listener<T> var1) {
      if (this._listeners != null) {
         this._listeners.remove(var1);
      }
   }

   public boolean isActive() {
      return this._active;
   }

   public void activate() {
      if (this._active) {
         throw new IllegalStateException("ClientInstance already active, can't activate again: " + this._id);
      } else {
         this._active = true;
      }
   }

   public void deactivate() {
      if (this._active) {
         this._active = false;
      }
   }

   public ClientInstanceIdentity getId() {
      return this._id;
   }

   public CreationInfo getCreationInfo() {
      return this._creationInfo;
   }

   public T getInstance() {
      return this._instance;
   }

   void close() {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Closing client instance: " + this);
      }

      this._ref = null;
      synchronized(this) {
         if (this._listeners != null) {
            ArrayList var2 = new ArrayList(this._listeners);
            Iterator var3 = var2.iterator();

            Listener var4;
            while(var3.hasNext()) {
               var4 = (Listener)var3.next();
               var4.clientInstanceClosing(this);
            }

            if (this.getId().getType() == ClientInstanceIdentity.Type.POOLED) {
               var3 = var2.iterator();

               while(var3.hasNext()) {
                  var4 = (Listener)var3.next();
                  var4.clientInstanceRecycled(this);
               }
            }

            this._listeners = null;
         }
      }

      this._releaser.release(this);
   }

   public int hashCode() {
      return this._id.hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof ClientInstance && this._id.equals(((ClientInstance)var1)._id);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer(this._id.toString());
      var1.append(" - ").append(this._instance.toString());
      return var1.toString();
   }

   interface InstanceReleaser<T> {
      void release(ClientInstance<T> var1);
   }

   public interface Listener<T> {
      void clientInstanceClosing(ClientInstance<T> var1);

      void clientInstanceRecycled(ClientInstance<T> var1);
   }

   public static class CreationInfo {
      private QName _portName;
      private WSEndpointReference _wsepr;
      private Class _aClass;
      private JAXBContext _jaxbContext;
      private Service.Mode _mode;
      private WebServiceFeatureList _features;

      public CreationInfo(QName var1, WSEndpointReference var2, Class var3, JAXBContext var4, Service.Mode var5, WebServiceFeatureList var6) {
         this._portName = var1;
         this._wsepr = var2;
         this._aClass = var3;
         this._jaxbContext = var4;
         this._mode = var5;
         this._features = new WebServiceFeatureList(var6);
      }

      public CreationInfo(CreationInfo var1, Class var2, Service.Mode var3, WebServiceFeatureList var4) {
         this._portName = var1._portName;
         this._wsepr = var1._wsepr;
         this._aClass = var2;
         this._jaxbContext = var1._jaxbContext;
         this._mode = var3;
         this._features = new WebServiceFeatureList(var4);
      }

      public boolean isPortInstance() {
         return this._mode == null;
      }

      public boolean isDispatchInstance() {
         return this._mode != null;
      }

      public QName getPortName() {
         return this._portName;
      }

      public WSEndpointReference getWsepr() {
         return this._wsepr;
      }

      public Class getInstanceType() {
         return this._aClass;
      }

      public JAXBContext getJaxbContext() {
         return this._jaxbContext;
      }

      public Service.Mode getMode() {
         return this._mode;
      }

      public WebServiceFeatureList getFeatures() {
         return new WebServiceFeatureList(this._features);
      }
   }

   public interface ReferenceHolderFactory {
      PhantomReference<?> create(Object var1, Closeable var2);
   }

   private static class InstanceCloseable implements Closeable {
      private ClientInstance<?> instance;

      public InstanceCloseable(ClientInstance<?> var1) {
         this.instance = var1;
      }

      public synchronized void close() throws WebServiceException {
         if (this.instance != null) {
            ClientInstance var1 = this.instance;
            this.instance = null;
            var1.close();
         }

      }
   }
}
