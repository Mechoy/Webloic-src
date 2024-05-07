package weblogic.wsee.jaxws.spi;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.EndpointAddress;
import com.sun.xml.ws.api.WSBinding;
import com.sun.xml.ws.api.WSService;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.model.SEIModel;
import com.sun.xml.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.ServerTubeAssemblerContext;
import com.sun.xml.ws.api.server.Container;
import com.sun.xml.ws.binding.WebServiceFeatureList;
import com.sun.xml.ws.client.WSServiceDelegate;
import com.sun.xml.ws.model.wsdl.WSDLServiceImpl;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import org.xml.sax.Locator;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.jws.jaxws.WLSWebServiceFeature;
import weblogic.jws.jaxws.client.ClientIdentityFeature;
import weblogic.jws.jaxws.client.async.AsyncClientTransportFeature;
import weblogic.kernel.KernelStatus;
import weblogic.management.WebLogicMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;
import weblogic.management.runtime.WseeClientRuntimeMBean;
import weblogic.management.runtime.WseePortConfigurationRuntimeMBean;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.WseePersistLogger;
import weblogic.wsee.deploy.DeployInfo;
import weblogic.wsee.jaxws.persistence.ClientInstancePoolFeature;
import weblogic.wsee.jaxws.tubeline.standard.ClientContainerUtil;
import weblogic.wsee.monitoring.WseeClientConfigurationRuntimeMBeanImpl;
import weblogic.wsee.monitoring.WseeClientPortRuntimeMBeanImpl;
import weblogic.wsee.monitoring.WseeClientRuntimeMBeanImpl;
import weblogic.wsee.monitoring.WseeRuntimeMBeanManager;
import weblogic.wsee.persistence.StoreException;
import weblogic.wsee.util.Guid;
import weblogic.wsee.util.HashCodeUtil;
import weblogic.wsee.util.Pair;

public class ClientIdentityRegistry {
   private static ClientIdentityFeatureListener _listener = new ClientIdentityFeatureListener();
   private static ClientInstancePoolFeatureListener _clientInstancePoolListener = new ClientInstancePoolFeatureListener();
   private static final Logger LOGGER = Logger.getLogger(ClientIdentityRegistry.class.getName());
   private static final ReentrantReadWriteLock _clientRegistryInternalLock = new ReentrantReadWriteLock(false);
   private static final Map<String, ClientInfo> _clientRegistryInternal;
   private static final ClientInstanceParentIdCalculator _parentIdCalculator = new ClientInstanceParentIdCalculator();
   private static final ReentrantReadWriteLock _clientIdListenersLock = new ReentrantReadWriteLock(false);
   private static final Map<String, List<Listener>> _clientIdListeners;

   public static ClientIdentityFeature getClientIdentityFeature(String var0) {
      ClientInfo var1 = getClientInfo(var0);
      return var1 != null ? var1.getClientIdentityFeature() : null;
   }

   public static WseeClientRuntimeMBeanImpl getClientRuntimeMBean(String var0) {
      ClientInfo var1 = getClientInfo(var0);
      return var1 != null ? var1.getClientRuntimeMBean() : null;
   }

   public static ClientInfo getClientInfo(String var0) {
      ClientInfo var1;
      try {
         _clientRegistryInternalLock.readLock().lock();
         if (!_clientRegistryInternal.containsKey(var0)) {
            var1 = null;
            return var1;
         }

         var1 = (ClientInfo)_clientRegistryInternal.get(var0);
      } finally {
         _clientRegistryInternalLock.readLock().unlock();
      }

      return var1;
   }

   public static ClientInfo getRequiredClientInfo(String var0) {
      ClientInfo var1 = getClientInfo(var0);
      if (var1 == null) {
         throw new IllegalStateException("No client with identity '" + var0 + "' could be found");
      } else {
         return var1;
      }
   }

   public static <T2> Dispatch<T2> getEquivalentDispatch(String var0, Class<T2> var1, Service.Mode var2, WSService var3, WSEndpointReference var4, WebServiceFeature... var5) {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Creating equivalent dispatch for client identity: " + var0);
      }

      ClientInfo var6 = getRequiredClientInfo(var0);
      ClientInfo.Config var7 = var6.getOriginalConfig();
      WebServiceFeature[] var9 = (WebServiceFeature[])var7.getFeatures().values().toArray(new WebServiceFeature[var7.getFeatures().size()]);
      WebServiceFeature[] var8;
      if (var5 != null && var5.length != 0) {
         var8 = new WebServiceFeature[var9.length + var5.length];
         System.arraycopy(var9, 0, var8, 0, var9.length);
         System.arraycopy(var5, 0, var8, var9.length, var5.length);
      } else {
         var8 = var9;
      }

      Dispatch var10 = var3.createDispatch(var7.getPortName(), var7.getWsepr(), var1, var2, var8);
      if (var4 != null) {
         var10.getRequestContext().put("javax.xml.ws.service.endpoint.address", var4.getAddress());
      }

      return var10;
   }

   public static WseeClientRuntimeMBeanImpl addClientRuntimeMBean(ClientIdentityFeature var0, ClientTubeAssemblerContext var1) {
      return internalAddClientRuntimeMBean(var0, var1.getBinding(), var1.getWsdlModel(), var1.getSEIModel(), var1.getContainer(), (WseeClientConfigurationRuntimeMBeanImpl)var1.getService().getSPI(WseeClientConfigurationRuntimeMBeanImpl.class));
   }

   public static WseeClientRuntimeMBeanImpl addClientRuntimeMBean(ClientIdentityFeature var0, ServerTubeAssemblerContext var1) {
      return internalAddClientRuntimeMBean(var0, var1.getEndpoint().getBinding(), var1.getWsdlModel(), var1.getSEIModel(), var1.getEndpoint().getContainer(), (WseeClientConfigurationRuntimeMBeanImpl)null);
   }

   private static WseeClientRuntimeMBeanImpl internalAddClientRuntimeMBean(ClientIdentityFeature var0, WSBinding var1, WSDLPort var2, SEIModel var3, Container var4, WseeClientConfigurationRuntimeMBeanImpl var5) {
      if (KernelStatus.isServer() && var0 != null) {
         QName var6 = null;
         if (var2 != null) {
            var6 = var2.getName();
         } else if (var3 != null) {
            var6 = var3.getPortName();
         }

         ClientInfo var7 = getRequiredClientInfo(var0.getClientId());
         WseeClientRuntimeMBeanImpl var8 = var7.getClientRuntimeMBean();
         if (var8 != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Returning existing WseeClientRuntimeMBean for client=" + var0.getClientId() + " port=" + var6);
            }

            return var8;
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Didn't find any existing WseeClientRuntimeMBean for client=" + var0.getClientId() + " port=" + var6);
            }

            String var9 = var4.getSPI(DeployInfo.class) == null ? null : ((DeployInfo)var4.getSPI(DeployInfo.class)).getModuleName();
            ComponentRuntimeMBeanImpl var10 = ClientContainerUtil.getContainingComponentRuntimeByModuleName(var9);
            if (var10 == null) {
               if (LOGGER.isLoggable(Level.FINE)) {
                  LOGGER.fine("Cannot create WseeClientRuntimeMBeanImpl for client=" + var0.getClientId() + " port=" + " because we couldn't find a parent runtime MBean for it");
               }

               return null;
            } else {
               WseeClientRuntimeMBeanImpl var12;
               try {
                  var7._lock.writeLock().lock();
                  WseeClientRuntimeMBeanImpl var11 = findWseeClientRuntimeMBeanInComponent(var0, var10);
                  if (var11 == null) {
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Creating WseeClientRuntimeMBeanImpl for client=" + var0.getClientId() + " port=" + " under parent: " + var10);
                     }

                     WseePortConfigurationRuntimeMBean var20 = getPortConfigRuntime(var5, var6);
                     WseeClientPortRuntimeMBeanImpl var13 = WseeRuntimeMBeanManager.createJaxWsClientPort(var6, var1, var2, var9, var20);
                     var11 = new WseeClientRuntimeMBeanImpl(var0.getRawClientId(), var0.getClientId(), var10);
                     var11.setPort(var13);
                     var11.register();
                     var7.mbean = var11;
                     WseeClientRuntimeMBeanImpl var14 = var11;
                     return var14;
                  }

                  if (LOGGER.isLoggable(Level.FINE)) {
                     LOGGER.fine("Re-using already registered WseeClientRuntimeMBeanImpl for client=" + var0.getClientId() + " port=" + " under parent: " + var10);
                  }

                  var12 = var11;
               } catch (Exception var18) {
                  if (LOGGER.isLoggable(Level.SEVERE)) {
                     LOGGER.log(Level.SEVERE, var18.toString(), var18);
                  }

                  throw new IllegalStateException(var18.toString(), var18);
               } finally {
                  var7._lock.writeLock().unlock();
               }

               return var12;
            }
         }
      } else {
         return null;
      }
   }

   private static WseePortConfigurationRuntimeMBean getPortConfigRuntime(WseeClientConfigurationRuntimeMBeanImpl var0, QName var1) {
      WseePortConfigurationRuntimeMBean var2 = null;
      if (var0 != null) {
         for(int var3 = 0; var3 < var0.getPorts().length; ++var3) {
            WseePortConfigurationRuntimeMBean var4 = var0.getPorts()[var3];
            if (var4.getName().equals(var1.getLocalPart())) {
               var2 = var4;
               break;
            }
         }
      }

      return var2;
   }

   private static WseeClientRuntimeMBeanImpl findWseeClientRuntimeMBeanInComponent(ClientIdentityFeature var0, RuntimeMBean var1) {
      WseeClientRuntimeMBeanImpl var2 = null;
      if (var1 instanceof WebAppComponentRuntimeMBean) {
         WseeClientRuntimeMBean[] var3 = ((WebAppComponentRuntimeMBean)var1).getWseeClientRuntimes();
         if (var3 != null) {
            WseeClientRuntimeMBean[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               WseeClientRuntimeMBean var7 = var4[var6];
               if (var7.getName().equals(var0.getRawClientId()) || var7.getClientID().equals(var0.getClientId())) {
                  var2 = (WseeClientRuntimeMBeanImpl)var7;
                  break;
               }
            }
         }
      }

      return var2;
   }

   public static void removeClientRuntimeMBean(String var0) {
      unregisterClientIdentity(var0);
   }

   public static void addClientIdentityListener(String var0, Listener var1) {
      Object var2;
      int var3;
      try {
         _clientIdListenersLock.writeLock().lock();
         var2 = (List)_clientIdListeners.get(var0);
         if (var2 == null) {
            var2 = new ArrayList();
            _clientIdListeners.put(var0, var2);
         }

         var3 = _clientIdListeners.size();
      } finally {
         _clientIdListenersLock.writeLock().unlock();
      }

      synchronized(var2) {
         if (!((List)var2).contains(var1)) {
            ((List)var2).add(var1);
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("ClientIdentityRegistry added listener " + var1 + " for client identity " + var0 + ". Current listener count for client identity: " + ((List)var2).size() + " Current client identities with listeners: " + var3);
            }
         }

      }
   }

   public static void removeClientIdentityListener(String var0, Listener var1) {
      List var2;
      int var3;
      try {
         _clientIdListenersLock.writeLock().lock();
         var2 = (List)_clientIdListeners.get(var0);
         var3 = _clientIdListeners.size();
      } finally {
         _clientIdListenersLock.writeLock().unlock();
      }

      if (var2 != null) {
         synchronized(var2) {
            var2.remove(var1);
            if (var2.size() < 1) {
               synchronized(_clientIdListeners) {
                  _clientIdListeners.remove(var0);
               }

               --var3;
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("ClientIdentityRegistry removed listener " + var1 + " for client identity " + var0 + ". Remaining listener count for client identity: " + var2.size() + " Remaining client identities with listeners: " + var3);
            }

         }
      }
   }

   private static void unregisterClientIdentity(String var0) {
      ClientInfo var1 = getClientInfo(var0);
      if (var1 != null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("ClientIdentityRegistry unregistering client identity " + var0);
         }

         var1.feature.removePropertyChangeListener(_listener);
         Listener[] var2 = null;

         try {
            _clientIdListenersLock.readLock().lock();
            List var3 = (List)_clientIdListeners.get(var0);
            if (var3 != null && !var3.isEmpty()) {
               var2 = (Listener[])var3.toArray(new Listener[var3.size()]);
            }
         } finally {
            _clientIdListenersLock.readLock().unlock();
         }

         if (var2 != null) {
            Listener[] var21 = var2;
            int var4 = var2.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Listener var6 = var21[var5];

               try {
                  var6.clientIdentityBeforeUnregister(var0);
               } catch (Exception var18) {
                  WseeCoreLogger.logUnexpectedException(var18.toString(), var18);
               }
            }
         }

         var1.feature.dispose();

         try {
            _clientRegistryInternalLock.writeLock().lock();
            _clientRegistryInternal.remove(var0);
         } finally {
            _clientRegistryInternalLock.writeLock().unlock();
         }

         ClientInstancePoolFeature var22 = var1.getClientInstancePoolFeature();
         if (var22 != null) {
            var22.removePropertyChangeListener(_clientInstancePoolListener);
         }

         var1.disposeClientInstancePools();
         if (var1.getAsyncResponseEndpointInfo() != null && var1.getAsyncResponseEndpointInfo().getEndpoint() != null) {
            try {
               var1.getAsyncResponseEndpointInfo().getEndpoint().stop();
            } catch (Exception var20) {
               if (LOGGER.isLoggable(Level.WARNING)) {
                  LOGGER.log(Level.WARNING, var20.toString(), var20);
               }

               WseeCoreLogger.logUnexpectedException(var20.toString(), var20);
            }
         }

      }
   }

   public static void registerClientIdentity(@NotNull ClientIdentityFeature var0) {
      String var1 = var0.getClientId();
      ClientInfo var2 = getClientInfo(var1);
      if (var2 == null) {
         try {
            _clientRegistryInternalLock.writeLock().lock();
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("ClientIdentityRegistry registering client identity " + var1);
            }

            var2 = new ClientInfo(var0);
            var0.addPropertyChangeListener(_listener);
            _clientRegistryInternal.put(var1, var2);
         } finally {
            _clientRegistryInternalLock.writeLock().unlock();
         }

         Listener[] var3 = null;

         try {
            _clientIdListenersLock.readLock().lock();
            List var4 = (List)_clientIdListeners.get(var1);
            if (var4 != null && !var4.isEmpty()) {
               var3 = (Listener[])var4.toArray(new Listener[var4.size()]);
            }
         } finally {
            _clientIdListenersLock.readLock().unlock();
         }

         if (var3 != null) {
            Listener[] var18 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               Listener var7 = var18[var6];

               try {
                  var7.clientIdentityRegistered(var1);
               } catch (Exception var15) {
                  WseeCoreLogger.logUnexpectedException(var15.toString(), var15);
               }
            }
         }

      }
   }

   public static ClientInstancePool initClientIdentity(@NotNull ClientIdentityFeature var0, @Nullable ClientInstancePoolFeature var1, @Nullable PortInfoBean var2, @NotNull ClientInstancePool.InstanceFactory var3, boolean var4, @NotNull Class<?> var5) throws StoreException {
      ClientInfo var6 = getRequiredClientInfo(var0.getClientId());
      return var6.init(var2, var1, var3, var4, var5);
   }

   public static ClientInstanceIdentity generateSimpleClientInstanceIdentity(String var0) {
      return new ClientInstanceIdentity(var0, ClientInstanceIdentity.Type.SIMPLE, Guid.generateGuid());
   }

   static PoolKey getPoolKey(Class var0, boolean var1) {
      return new PoolKey(var0, var1);
   }

   private static String genPortNameID(QName var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.getNamespaceURI();
         var1 = var1 == null ? "" : var1;
         return var1 + ":" + var0.getLocalPart();
      }
   }

   public static <T> ClientIdentityFeature initClientIdentityFeature(QName var0, WSServiceDelegate var1, WSEndpointReference var2, Class<T> var3, String var4, WebServiceFeatureList var5) {
      WSDLServiceImpl var6 = var1.getWsdlService();
      String var7 = null;
      if (var6 != null) {
         Locator var8 = var6.getLocation();
         if (var8 != null) {
            var7 = var8.getSystemId();
         }
      } else {
         EndpointAddress var17 = var1.getEndpointAddress(var0);
         if (var17 != null) {
            var7 = var17.toString();
         }
      }

      ClientIdentityFeature var18 = (ClientIdentityFeature)var5.get(ClientIdentityFeature.class);
      boolean var9 = false;
      boolean var10 = true;
      if (var18 == null) {
         var18 = new ClientIdentityFeature();
         var9 = true;
      }

      if (var18.getRawClientId() == null) {
         String var11 = var4;
         if (var4 == null) {
            var11 = genPortNameID(var0);
            if (var11 == null) {
               var11 = var2 != null ? var2.getAddress() : null;
            } else if (var2 != null) {
               var11 = var11 + ":" + var2.getAddress();
            }

            if (var11 == null) {
               var11 = var7;
            } else if (var7 != null) {
               if (!var7.endsWith("?WSDL") && !var7.endsWith("?wsdl")) {
                  var11 = var11 + ":" + var7;
               } else {
                  var11 = var11 + ":" + var7.substring(0, var7.length() - 5);
               }
            }
         }

         if (var11 == null) {
            var11 = var3 != null ? var3.getSimpleName() : null;
         }

         var11 = var11 + ":" + generateRawClientIdFeatureSuffix(var5);
         var10 = !generateDefaultIdIfNeeded(var18, var11);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("rawClientId=" + var11);
         }
      }

      if (var9) {
         var5.add(var18);
      }

      if (var10) {
         registerClientIdentity(var18);
      }

      if (var5.contains(AsyncClientTransportFeature.class)) {
         AsyncClientTransportFeature var20 = (AsyncClientTransportFeature)var5.get(AsyncClientTransportFeature.class);
         var20.preRegisterForSharedEndpoint(var18);
      }

      ClientInfo var21 = getRequiredClientInfo(var18.getClientId());

      ClientIdentityFeature var19;
      try {
         var21._lock.writeLock().lock();
         ClientInfo.Config var12 = var21.getOriginalConfig();
         if (var12 == null) {
            var12 = new ClientInfo.Config(var18, var1, var2, var0, var5);
            var21.setOriginalConfig(var12);
         } else {
            ClientInfo.Config var13 = new ClientInfo.Config(var18, var1, var2, var0, var5);
            var12.verifyAgainst(var13);
         }

         var19 = var18;
      } finally {
         var21._lock.writeLock().unlock();
      }

      return var19;
   }

   private static String generateRawClientIdFeatureSuffix(WebServiceFeatureList var0) {
      StringBuffer var1 = new StringBuffer();
      TreeSet var2 = new TreeSet();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         WebServiceFeature var4 = (WebServiceFeature)var3.next();
         if (var4 instanceof WLSWebServiceFeature) {
            var2.add(((WLSWebServiceFeature)var4).getClientIdComponent());
         } else {
            var2.add(var4.getClass().getSimpleName());
         }
      }

      var3 = var2.iterator();

      while(var3.hasNext()) {
         var1.append((String)var3.next());
         if (var3.hasNext()) {
            var1.append('-');
         }
      }

      return var1.toString();
   }

   private static boolean generateDefaultIdIfNeeded(ClientIdentityFeature var0, String var1) {
      if (var0.getClientId() != null) {
         return false;
      } else {
         if (var1 == null) {
            var1 = "UnknownEndpointAddress";
         }

         var0.setGeneratedRawClientId(var1);
         registerClientIdentity(var0);
         return true;
      }
   }

   public static ClientInstanceIdentityFeature initClientInstanceIdentityFeature(ServerTubeAssemblerContext var0) {
      return initClientInstanceIdentityFeature(var0.getEndpoint().getBinding());
   }

   private static ClientInstanceIdentityFeature initClientInstanceIdentityFeature(WSBinding var0) {
      ClientInstanceIdentityFeature var1 = (ClientInstanceIdentityFeature)var0.getFeature(ClientInstanceIdentityFeature.class);
      if (var1 == null) {
         ClientIdentityFeature var2 = (ClientIdentityFeature)var0.getFeature(ClientIdentityFeature.class);
         ClientInstanceIdentity var3 = generateSimpleClientInstanceIdentity(var2.getClientId());
         var1 = new ClientInstanceIdentityFeature(var3);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Forcing ClientInstanceIdentityFeature onto client binding: " + var3);
         }

         ((WebServiceFeatureList)var0.getFeatures()).add(var1);
      }

      return var1;
   }

   static {
      ClientIdentityFeature.setParentIdCalculator(_parentIdCalculator);
      AsyncClientTransportFeature.setAsyncResponseEndpointRegistry(new ClientIdentityEndpointRegistry());
      _clientIdListeners = new HashMap();
      _clientRegistryInternal = new WeakHashMap();
   }

   private static class ClientInstancePoolFeatureListener implements PropertyChangeListener {
      private ClientInstancePoolFeatureListener() {
      }

      private void durableClientPoolFeatureDisposed(ClientInstancePoolFeature var1) {
         String var2 = var1.getClientIdentityFeature().getClientId();
         ClientInfo var3 = ClientIdentityRegistry.getClientInfo(var2);
         if (var3 != null) {
            var3.disposeClientInstancePools();
         }

      }

      private void allDurableClientsClosed(ClientInstancePoolFeature var1) {
         String var2 = var1.getClientIdentityFeature().getClientId();
         ClientInfo var3 = ClientIdentityRegistry.getClientInfo(var2);
         if (var3 != null) {
            var3.closeAllInClientInstancePools();
         }

      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1.getPropertyName().equals("Disposed")) {
            this.durableClientPoolFeatureDisposed((ClientInstancePoolFeature)var1.getSource());
         } else if (var1.getPropertyName().equals("ClosedAllDurableClients")) {
            this.allDurableClientsClosed((ClientInstancePoolFeature)var1.getSource());
         }

      }

      // $FF: synthetic method
      ClientInstancePoolFeatureListener(Object var1) {
         this();
      }
   }

   private static class ClientIdentityFeatureListener implements PropertyChangeListener {
      private ClientIdentityFeatureListener() {
      }

      private void clientIdentityFeatureDisposed(ClientIdentityFeature var1) {
         String var2 = var1.getClientId();
         WseeClientRuntimeMBeanImpl var3 = ClientIdentityRegistry.getClientRuntimeMBean(var2);
         if (var3 != null) {
            try {
               ((WseeClientRuntimeMBeanImpl)var3).unregister();
            } catch (Exception var5) {
               WseePersistLogger.logUnexpectedException(var5.toString(), var5);
            }
         } else {
            ClientIdentityRegistry.unregisterClientIdentity(var2);
         }

      }

      public void propertyChange(PropertyChangeEvent var1) {
         if (var1.getPropertyName().equals("Disposed")) {
            this.clientIdentityFeatureDisposed((ClientIdentityFeature)var1.getSource());
         }

      }

      // $FF: synthetic method
      ClientIdentityFeatureListener(Object var1) {
         this();
      }
   }

   private static class PoolMap implements Map<PoolKey, ClientInstancePool<?>> {
      private WeakHashMap<Class<?>, Pair<ClientInstancePool<?>, ClientInstancePool<?>>> map;
      private WeakReference<ClientInstancePool<?>> _firstPool;

      private PoolMap() {
         this.map = new WeakHashMap();
      }

      public void clear() {
         this.map.clear();
      }

      public boolean containsKey(Object var1) {
         if (!(var1 instanceof PoolKey)) {
            return false;
         } else {
            Pair var2 = (Pair)this.map.get(((PoolKey)var1).aClass);
            return var2 != null && (var2.getLeft() != null || var2.getRight() != null);
         }
      }

      public boolean containsValue(Object var1) {
         if (!(var1 instanceof ClientInstancePool)) {
            return false;
         } else {
            Iterator var2 = this.map.values().iterator();

            Pair var3;
            do {
               if (!var2.hasNext()) {
                  return false;
               }

               var3 = (Pair)var2.next();
            } while(!var1.equals(var3.getLeft()) && !var1.equals(var3.getRight()));

            return true;
         }
      }

      public Set<Map.Entry<PoolKey, ClientInstancePool<?>>> entrySet() {
         throw new UnsupportedOperationException();
      }

      public ClientInstancePool<?> get(Object var1) {
         if (!(var1 instanceof PoolKey)) {
            return null;
         } else {
            PoolKey var2 = (PoolKey)var1;
            Pair var3 = (Pair)this.map.get(var2.aClass);
            return var3 != null ? (var2.isDispatch ? (ClientInstancePool)var3.getRight() : (ClientInstancePool)var3.getLeft()) : null;
         }
      }

      public boolean isEmpty() {
         return this.map.isEmpty();
      }

      public Set<PoolKey> keySet() {
         throw new UnsupportedOperationException();
      }

      public ClientInstancePool<?> put(PoolKey var1, ClientInstancePool<?> var2) {
         if (this._firstPool == null) {
            this._firstPool = new WeakReference(var2);
         }

         Pair var3 = (Pair)this.map.get(var1.aClass);
         if (var3 == null) {
            if (var2 == null) {
               return null;
            }

            var3 = new Pair();
            this.map.put(var1.aClass, var3);
         }

         ClientInstancePool var4;
         if (var1.isDispatch) {
            if (var2 == null && var3.getLeft() == null) {
               this.map.remove(var1.aClass);
               return (ClientInstancePool)var3.getRight();
            } else {
               var4 = (ClientInstancePool)var3.getRight();
               var3.setRight(var2);
               return var4;
            }
         } else if (var2 == null && var3.getRight() == null) {
            this.map.remove(var1.aClass);
            return (ClientInstancePool)var3.getLeft();
         } else {
            var4 = (ClientInstancePool)var3.getLeft();
            var3.setLeft(var2);
            return var4;
         }
      }

      public ClientInstancePool<?> getFirstPool() {
         if (this._firstPool != null) {
            return (ClientInstancePool)this._firstPool.get();
         } else {
            return this.map.isEmpty() ? null : (ClientInstancePool)this.values().iterator().next();
         }
      }

      public void putAll(Map<? extends PoolKey, ? extends ClientInstancePool<?>> var1) {
         Iterator var2 = var1.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();
            this.put((PoolKey)var3.getKey(), (ClientInstancePool)var3.getValue());
         }

      }

      public ClientInstancePool<?> remove(Object var1) {
         if (!(var1 instanceof PoolKey)) {
            return null;
         } else {
            PoolKey var2 = (PoolKey)var1;
            Pair var3 = (Pair)this.map.get(var2.aClass);
            if (var3 == null) {
               return null;
            } else {
               ClientInstancePool var4;
               if (var2.isDispatch) {
                  if (var3.getLeft() == null) {
                     this.map.remove(var2.aClass);
                  }

                  var4 = (ClientInstancePool)var3.getRight();
                  var3.setRight((Object)null);
                  return var4;
               } else {
                  if (var3.getRight() == null) {
                     this.map.remove(var2.aClass);
                  }

                  var4 = (ClientInstancePool)var3.getLeft();
                  var3.setLeft((Object)null);
                  return var4;
               }
            }
         }
      }

      public int size() {
         throw new UnsupportedOperationException();
      }

      public Collection<ClientInstancePool<?>> values() {
         return new AbstractCollection<ClientInstancePool<?>>() {
            public Iterator<ClientInstancePool<?>> iterator() {
               final Iterator var1 = PoolMap.this.map.values().iterator();
               return new Iterator<ClientInstancePool<?>>() {
                  private ClientInstancePool<?> next = null;

                  public boolean hasNext() {
                     return this.next != null || var1.hasNext();
                  }

                  public ClientInstancePool<?> next() {
                     if (this.next != null) {
                        this.next = null;
                        return this.next;
                     } else {
                        Pair var1x = (Pair)var1.next();
                        if (var1x.getLeft() != null) {
                           this.next = (ClientInstancePool)var1x.getRight();
                           return (ClientInstancePool)var1x.getLeft();
                        } else {
                           return (ClientInstancePool)var1x.getRight();
                        }
                     }
                  }

                  public void remove() {
                     throw new UnsupportedOperationException();
                  }
               };
            }

            public int size() {
               throw new UnsupportedOperationException();
            }
         };
      }

      // $FF: synthetic method
      PoolMap(Object var1) {
         this();
      }
   }

   public static class PoolKey {
      private Class aClass;
      private boolean isDispatch;

      public PoolKey(Class var1, boolean var2) {
         this.aClass = var1;
         this.isDispatch = var2;
      }

      public int hashCode() {
         int var1 = 23;
         var1 = HashCodeUtil.hash(var1, (Object)this.aClass);
         var1 = HashCodeUtil.hash(var1, this.isDispatch);
         return var1;
      }

      public String toString() {
         return (this.isDispatch ? "Dispatch-" : "Port-") + this.aClass.getName();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof PoolKey)) {
            return false;
         } else {
            PoolKey var2 = (PoolKey)var1;
            return this.aClass == var2.aClass && this.isDispatch == var2.isDispatch;
         }
      }
   }

   public interface Listener {
      void clientIdentityRegistered(String var1);

      void clientIdentityBeforeUnregister(String var1);
   }

   public static class ClientInfo {
      private final ReentrantReadWriteLock _lock = new ReentrantReadWriteLock(false);
      ClientIdentityFeature feature;
      WseeClientRuntimeMBeanImpl mbean;
      @Nullable
      ClientInstancePoolFeature _clientInstancePoolFeature;
      PoolMap _clientInstancePools;
      @Nullable
      PortInfoBean portInfoBean;
      @Nullable
      AsyncClientTransportFeature.AsyncResponseEndpointRegistry.EndpointInfo _asyncResponseEndpointInfo;
      @Nullable
      Config _originalConfig;
      boolean _initialized;

      public ClientInfo(ClientIdentityFeature var1) {
         this.feature = var1;
         this._clientInstancePools = new PoolMap();
      }

      public boolean isPoolInitialized(Class var1) {
         boolean var4;
         try {
            this._lock.readLock().lock();
            PoolKey var2 = ClientIdentityRegistry.getPoolKey(var1, false);
            ClientInstancePool var3 = this._clientInstancePools.get(var2);
            var4 = var3 != null && var3.isInitialized();
         } finally {
            this._lock.readLock().unlock();
         }

         return var4;
      }

      public boolean isDispatchPoolInitialized(Class var1) {
         boolean var4;
         try {
            this._lock.readLock().lock();
            PoolKey var2 = ClientIdentityRegistry.getPoolKey(var1, true);
            ClientInstancePool var3 = this._clientInstancePools.get(var2);
            var4 = var3 != null && var3.isInitialized();
         } finally {
            this._lock.readLock().unlock();
         }

         return var4;
      }

      public boolean isInitialized() {
         boolean var1;
         try {
            this._lock.readLock().lock();
            var1 = this._initialized;
         } finally {
            this._lock.readLock().unlock();
         }

         return var1;
      }

      public ClientInstancePool init(@Nullable PortInfoBean var1, @Nullable ClientInstancePoolFeature var2, @Nullable ClientInstancePool.InstanceFactory var3, boolean var4, @Nullable Class<?> var5) throws StoreException {
         if (var5 == null) {
            return null;
         } else {
            PoolKey var6 = ClientIdentityRegistry.getPoolKey(var5, var4);

            ClientInstancePool var7;
            try {
               this._lock.readLock().lock();
               var7 = this._clientInstancePools.get(var6);
            } finally {
               this._lock.readLock().unlock();
            }

            if (var7 != null && var7.isInitialized()) {
               return var7;
            } else {
               ClientInstancePool var8;
               try {
                  this._lock.writeLock().lock();
                  if (!this._clientInstancePools.containsKey(var6)) {
                     this.portInfoBean = var1;
                     if (var2 != null) {
                        var2.addPropertyChangeListener(ClientIdentityRegistry._clientInstancePoolListener);
                     }

                     if (var2 == null) {
                        var2 = new ClientInstancePoolFeature();
                        var2.setClientIdentityFeature(this.feature);
                        var2.setCapacity(0);
                     }

                     this._clientInstancePoolFeature = var2;
                     if (ClientIdentityRegistry.LOGGER.isLoggable(Level.FINE)) {
                        ClientIdentityRegistry.LOGGER.fine("ClientIdentityRegistry requesting initialization of client instance pool for client identity " + this.feature.getClientId() + " and instance type: " + var5.getSimpleName());
                     }

                     var7 = new ClientInstancePool(this.feature, var1, var2, var3, var5);
                     this._clientInstancePools.put(var6, var7);
                     this._initialized = true;
                     var8 = var7;
                     return var8;
                  }

                  var8 = this._clientInstancePools.get(var6);
               } finally {
                  this._lock.writeLock().unlock();
               }

               return var8;
            }
         }
      }

      public ClientIdentityFeature getClientIdentityFeature() {
         ClientIdentityFeature var1;
         try {
            this._lock.readLock().lock();
            var1 = this.feature;
         } finally {
            this._lock.readLock().unlock();
         }

         return var1;
      }

      public ClientInstancePoolFeature getClientInstancePoolFeature() {
         ClientInstancePoolFeature var1;
         try {
            this._lock.readLock().lock();
            var1 = this._clientInstancePoolFeature;
         } finally {
            this._lock.readLock().unlock();
         }

         return var1;
      }

      public WseeClientRuntimeMBeanImpl getClientRuntimeMBean() {
         WseeClientRuntimeMBeanImpl var1;
         try {
            this._lock.readLock().lock();
            var1 = this.mbean;
         } finally {
            this._lock.readLock().unlock();
         }

         return var1;
      }

      public Map<PoolKey, ClientInstancePool<?>> getClientInstancePools() {
         Map var1;
         try {
            this._lock.readLock().lock();
            var1 = Collections.unmodifiableMap(this._clientInstancePools);
         } finally {
            this._lock.readLock().unlock();
         }

         return var1;
      }

      public ClientInstancePool<?> getFirstClientInstancePool() {
         ClientInstancePool var1;
         try {
            this._lock.readLock().lock();
            var1 = this._clientInstancePools.getFirstPool();
         } finally {
            this._lock.readLock().unlock();
         }

         return var1;
      }

      public AsyncClientTransportFeature.AsyncResponseEndpointRegistry.EndpointInfo getOrCreateAsyncResponseEndpointInfo() {
         boolean var1 = false;

         AsyncClientTransportFeature.AsyncResponseEndpointRegistry.EndpointInfo var2;
         try {
            this._lock.readLock().lock();
            var1 = true;
            if (this._asyncResponseEndpointInfo == null) {
               this._lock.readLock().unlock();
               var1 = false;

               try {
                  this._lock.writeLock().lock();
                  this._asyncResponseEndpointInfo = new AsyncClientTransportFeature.AsyncResponseEndpointRegistry.EndpointInfo();
               } finally {
                  this._lock.writeLock().unlock();
               }
            }

            var2 = this._asyncResponseEndpointInfo;
         } finally {
            if (var1) {
               this._lock.readLock().unlock();
            }

         }

         return var2;
      }

      public AsyncClientTransportFeature.AsyncResponseEndpointRegistry.EndpointInfo getAsyncResponseEndpointInfo() {
         AsyncClientTransportFeature.AsyncResponseEndpointRegistry.EndpointInfo var1;
         try {
            this._lock.readLock().lock();
            var1 = this._asyncResponseEndpointInfo;
         } finally {
            this._lock.readLock().unlock();
         }

         return var1;
      }

      public Config getOriginalConfig() {
         Config var1;
         try {
            this._lock.readLock().lock();
            var1 = this._originalConfig;
         } finally {
            this._lock.readLock().unlock();
         }

         return var1;
      }

      public void setOriginalConfig(Config var1) {
         try {
            this._lock.writeLock().lock();
            this._originalConfig = var1;
         } finally {
            this._lock.writeLock().unlock();
         }

      }

      private void disposeClientInstancePools() {
         try {
            this._lock.readLock().lock();
            Iterator var1 = this._clientInstancePools.values().iterator();

            while(var1.hasNext()) {
               ClientInstancePool var2 = (ClientInstancePool)var1.next();

               try {
                  var2.dispose();
               } catch (Exception var13) {
                  if (ClientIdentityRegistry.LOGGER.isLoggable(Level.WARNING)) {
                     ClientIdentityRegistry.LOGGER.log(Level.WARNING, var13.toString(), var13);
                  }

                  WseeCoreLogger.logUnexpectedException(var13.toString(), var13);
               }
            }
         } finally {
            this._lock.readLock().unlock();
         }

         try {
            this._lock.writeLock().lock();
            this._clientInstancePools.clear();
         } finally {
            this._lock.writeLock().unlock();
         }

      }

      private void closeAllInClientInstancePools() {
         try {
            this._lock.readLock().lock();
            Iterator var1 = this._clientInstancePools.values().iterator();

            while(var1.hasNext()) {
               ClientInstancePool var2 = (ClientInstancePool)var1.next();

               try {
                  var2.closeAll();
               } catch (Exception var7) {
                  if (ClientIdentityRegistry.LOGGER.isLoggable(Level.WARNING)) {
                     ClientIdentityRegistry.LOGGER.log(Level.WARNING, var7.toString(), var7);
                  }

                  WseeCoreLogger.logUnexpectedException(var7.toString(), var7);
               }
            }
         } finally {
            this._lock.readLock().unlock();
         }

      }

      public static class Config {
         private String _clientId;
         private boolean _generatedId;
         private boolean _warnedOnce;
         private Throwable _creationContext;
         @Nullable
         private WSServiceDelegate _service;
         @Nullable
         private WSEndpointReference _wsepr;
         @Nullable
         private QName _portName;
         @Nullable
         private WebServiceFeatureList _features;

         public Config(ClientIdentityFeature var1, WSServiceDelegate var2, WSEndpointReference var3, QName var4, WebServiceFeatureList var5) {
            this._clientId = var1.getClientId();
            this._generatedId = var1.isGeneratedRawClientId();
            this._service = var2;
            this._wsepr = var3;
            this._portName = var4;
            this._features = var5;
            this._creationContext = new Throwable();
            this._creationContext.fillInStackTrace();
         }

         public void verifyAgainst(Config var1) {
            try {
               boolean var2 = this.verifyFeatures(var1._features);
               this.verifyService(var1._service, var2);
            } catch (IllegalArgumentException var3) {
               if (!this._warnedOnce && ClientIdentityRegistry.LOGGER.isLoggable(Level.FINE)) {
                  ClientIdentityRegistry.LOGGER.fine("Verification of original vs new ClientInfo.Config FAILED for client identity: " + this._clientId);
                  ClientIdentityRegistry.LOGGER.fine("Original Config Created Here...\n");
                  ClientIdentityRegistry.LOGGER.log(Level.FINE, "Original Config for Client " + this._clientId + " Created Here...\n", this._creationContext);
                  ClientIdentityRegistry.LOGGER.fine("New Config Created Here...\n");
                  ClientIdentityRegistry.LOGGER.log(Level.FINE, "New Config Created for Client " + this._clientId + " Here...\n", var1._creationContext);
               }

               if (!this._generatedId) {
                  throw var3;
               }

               if (!this._warnedOnce) {
                  WseeCoreLogger.logImplicitClientIdReusedImproperly(this._clientId, var3.toString());
               }

               this._warnedOnce = true;
            }

         }

         public WSServiceDelegate getService() {
            return this._service;
         }

         public WSEndpointReference getWsepr() {
            return this._wsepr;
         }

         public QName getPortName() {
            return this._portName;
         }

         public WebServiceFeatureList getFeatures() {
            return this._features;
         }

         private void verifyService(WSServiceDelegate var1, boolean var2) {
            boolean var3 = false;
            if (this._service == null && var1 == null) {
               var3 = true;
            } else if (this._service != null && var1 != null) {
               WSDLServiceImpl var4 = this._service.getWsdlService();
               WSDLServiceImpl var5 = var1.getWsdlService();
               if (var4 == null && var5 == null || var5 != null && var4 != null && var5.getName().equals(var4.getName())) {
                  var3 = true;
               }
            }

            if (!var3) {
               String var6 = "Cannot share ClientIdentityFeature for " + this._clientId + " across Services.";
               if (var2) {
                  throw new WebServiceException(var6);
               } else {
                  throw new IllegalArgumentException(var6);
               }
            }
         }

         private boolean verifyFeatures(WebServiceFeatureList var1) {
            StringBuffer var2 = new StringBuffer();
            boolean var3 = false;
            if (this._features == null && var1 != null || this._features != null && var1 == null) {
               var2.append("One set of features was null and the other was not");
            } else if (this._features != null) {
               var3 = verifyFeatures(this._features, var1, var2);
            }

            if (var2.length() > 0) {
               String var4 = "Feature set mismatch detected across client instances for client " + this._clientId + ":\n" + var2;
               if (var3) {
                  throw new WebServiceException(var4);
               } else {
                  throw new IllegalArgumentException(var4);
               }
            } else {
               return var3;
            }
         }

         private static boolean verifyFeatures(WebServiceFeatureList var0, WebServiceFeatureList var1, StringBuffer var2) {
            WebServiceFeature var4 = var0.get(AsyncClientTransportFeature.class);
            WebServiceFeature var5 = var1.get(AsyncClientTransportFeature.class);
            boolean var3 = var4 != null || var5 != null;
            if (var4 == null && var5 != null || var4 != null && var5 == null) {
               var2.append("AsyncClientTransportFeature used inconsistently across client instances (same have it, some don't)");
            } else if (var4 != null && !var4.equals(var5)) {
               if (var2.length() > 0) {
                  var2.append("\n");
               }

               var2.append("AsyncClientTransportFeature used between client instances differ. Original=[").append(var4).append("] New=[").append(var5).append("]");
            }

            areEquivalent(var0, var1, var2);
            return var3;
         }

         private static boolean areEquivalent(WebServiceFeatureList var0, WebServiceFeatureList var1, StringBuffer var2) {
            WebServiceFeatureList var3 = new WebServiceFeatureList(var0);
            WebServiceFeatureList var4 = new WebServiceFeatureList(var1);
            excludeFeaturesFromEquivalenceCheck(var3);
            excludeFeaturesFromEquivalenceCheck(var4);
            if (!checkMapEquality(var3, var4)) {
               if (var2.length() > 0) {
                  var2.append("\n");
               }

               var2.append("Original Features=[").append(dumpFeatureMap(var3)).append("]");
               var2.append("\n");
               var2.append("New Features=     [").append(dumpFeatureMap(var4)).append("]");
            }

            return var2.length() == 0;
         }

         private static boolean checkMapEquality(WebServiceFeatureList var0, WebServiceFeatureList var1) {
            if (var0.keySet().equals(var1.keySet())) {
               Iterator var2 = var0.keySet().iterator();

               WebServiceFeature var4;
               WebServiceFeature var5;
               do {
                  if (!var2.hasNext()) {
                     return true;
                  }

                  Class var3 = (Class)var2.next();
                  var4 = var0.get(var3);
                  var5 = var1.get(var3);
               } while(WLSWebServiceFeature.featuresAreEqual(var4, var5));

               return false;
            } else {
               return false;
            }
         }

         private static String dumpFeatureMap(WebServiceFeatureList var0) {
            StringBuffer var1 = new StringBuffer();
            Iterator var2 = var0.keySet().iterator();

            while(var2.hasNext()) {
               Class var3 = (Class)var2.next();
               var1.append(var0.get(var3)).append(", ");
            }

            return var1.toString();
         }

         private static void excludeFeaturesFromEquivalenceCheck(WebServiceFeatureList var0) {
            synchronized(var0) {
               Iterator var2 = var0.keySet().iterator();

               while(true) {
                  WebServiceFeature var4;
                  do {
                     label32:
                     do {
                        while(var2.hasNext()) {
                           Class var3 = (Class)var2.next();
                           var4 = var0.get(var3);
                           if (!(var4 instanceof ClientIdentityFeature) && !(var4 instanceof AsyncClientTransportFeature)) {
                              continue label32;
                           }

                           var2.remove();
                        }

                        return;
                     } while(WLSWebServiceFeature.isKnownTubelineImpactingFeature(var4.getClass()));
                  } while(WLSWebServiceFeature.class.isAssignableFrom(var4.getClass()) && ((WLSWebServiceFeature)var4).isTubelineImpact());

                  var2.remove();
               }
            }
         }
      }
   }

   private static class ClientInstanceParentIdCalculator implements ClientIdentityFeature.ParentIdCalculator {
      private ClientInstanceParentIdCalculator() {
      }

      public String calculateParentId(ClientIdentityFeature var1) {
         return this.calculateParentId();
      }

      public String calculateParentId() {
         String var1 = null;
         if (KernelStatus.isServer()) {
            try {
               Object var2 = ClientContainerUtil.getContainingComponentRuntime();
               if (var2 == null) {
                  var2 = ClientContainerUtil.getContainingApplicationRuntime();
               }

               if (var2 != null) {
                  var1 = "";

                  for(Object var3 = var2; var3 != null && !(var3 instanceof ServerRuntimeMBean); var3 = ((WebLogicMBean)var3).getParent()) {
                     String var4 = ((WebLogicMBean)var3).getName();
                     if (var3 instanceof WebAppComponentRuntimeMBean) {
                        int var5 = var4.indexOf("_/");
                        if (var5 >= 0) {
                           var4 = var4.substring(var5 + 2, var4.length());
                        }
                     }

                     if (var1.length() > 0) {
                        var1 = var4 + ":" + var1;
                     } else {
                        var1 = var4;
                     }
                  }
               }
            } catch (Exception var6) {
               if (ClientIdentityRegistry.LOGGER.isLoggable(Level.SEVERE)) {
                  ClientIdentityRegistry.LOGGER.log(Level.SEVERE, var6.toString(), var6);
               }

               var1 = null;
            }
         }

         return var1;
      }

      // $FF: synthetic method
      ClientInstanceParentIdCalculator(Object var1) {
         this();
      }
   }

   private static class ClientIdentityEndpointRegistry implements AsyncClientTransportFeature.AsyncResponseEndpointRegistry {
      private ClientIdentityEndpointRegistry() {
      }

      public AsyncClientTransportFeature.AsyncResponseEndpointRegistry.EndpointInfo getAsyncResponseEndpoint(ClientIdentityFeature var1) {
         ClientInfo var2 = ClientIdentityRegistry.getRequiredClientInfo(var1.getClientId());
         return var2.getOrCreateAsyncResponseEndpointInfo();
      }

      // $FF: synthetic method
      ClientIdentityEndpointRegistry(Object var1) {
         this();
      }
   }
}
