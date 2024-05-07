package weblogic.ejb.container.internal;

import java.lang.reflect.Proxy;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EJBException;
import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.interfaces.BeanManager;
import weblogic.ejb.container.interfaces.Ejb3RemoteHome;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3StatefulHome;
import weblogic.ejb.container.manager.ReplicatedStatefulSessionManager;
import weblogic.ejb.container.manager.StatefulSessionManager;
import weblogic.ejb.container.replication.ReplicatedBeanManager;
import weblogic.ejb.spi.BusinessObject;
import weblogic.ejb20.internal.HomeHandleImpl;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.rmi.extensions.server.ServerHelper;
import weblogic.utils.collections.SoftHashMap;

public class StatefulEJBHomeImpl extends StatefulEJBHome implements Ejb3StatefulHome, Ejb3RemoteHome {
   private final Map opaqueReferenceMap = new HashMap();
   private final Map ifaceNameToIface = new HashMap();
   private Map bosMap = Collections.synchronizedMap(new SoftHashMap());

   public StatefulEJBHomeImpl() {
      super((Class)null);
   }

   public StatefulEJBHomeImpl(Class var1) {
      super(var1);
   }

   public EJBMetaData getEJBMetaData() throws RemoteException {
      throw new IllegalStateException();
   }

   public HomeHandle getHomeHandle() throws RemoteException {
      return new HomeHandleImpl(this, this.getJNDIName(), URLDelegateProvider.getURLDelegate(this.isHomeClusterable()));
   }

   public void remove(Object var1) throws RemoteException, RemoveException {
      throw new IllegalStateException();
   }

   public void remove(Handle var1) throws RemoteException, RemoveException {
      throw new IllegalStateException();
   }

   public void prepare() {
      Ejb3SessionBeanInfo var1 = (Ejb3SessionBeanInfo)this.beanInfo;
      Iterator var2 = var1.getBusinessRemotes().iterator();

      while(var2.hasNext()) {
         Class var3 = (Class)var2.next();
         Class var4 = var1.getGeneratedRemoteBusinessImplClass(var3);
         Class var5 = var1.getGeneratedRemoteBusinessIntfClass(var3);
         EJBBusinessActivator var6 = new EJBBusinessActivator(this, var4, var3);
         OpaqueReferenceImpl var7 = new OpaqueReferenceImpl(this, var4, var6, var3, var5);
         this.opaqueReferenceMap.put(var3.getName(), var7);
         this.ifaceNameToIface.put(var3.getName(), var3);
      }

   }

   public Object getBindableImpl(Class var1) {
      return this.opaqueReferenceMap.get(var1.getName());
   }

   public Object getComponentImpl(Object var1) throws RemoteException {
      if (this.beanManager instanceof ReplicatedStatefulSessionManager) {
         try {
            return ((ReplicatedStatefulSessionManager)this.beanManager).registerReplicatedObject(var1);
         } catch (InternalException var3) {
            throw new RemoteException("Remote Exception: ", var3);
         }
      } else {
         return null;
      }
   }

   public boolean needToConsiderReplicationService() throws RemoteException {
      Ejb3SessionBeanInfo var1 = (Ejb3SessionBeanInfo)this.getBeanInfo();
      BeanManager var2 = this.getBeanManager();
      if (var2 instanceof ReplicatedStatefulSessionManager) {
         boolean var3 = ((ReplicatedStatefulSessionManager)var2).isInCluster();
         if (var3 && var1.hasDeclaredRemoteHome()) {
            return true;
         }
      }

      return false;
   }

   public Object getBusinessImpl(Object var1, String var2) throws RemoteException {
      Class var3 = (Class)this.ifaceNameToIface.get(var2);
      return this.getBusinessImpl(var1, var3);
   }

   public Object getBusinessImpl(Object var1, Class var2) throws RemoteException {
      Ejb3SessionBeanInfo var3 = (Ejb3SessionBeanInfo)this.beanInfo;
      Class var4 = var3.getGeneratedRemoteBusinessImplClass(var2);
      if (var4 == null) {
         return null;
      } else {
         OpaqueReferenceImpl var5 = (OpaqueReferenceImpl)this.opaqueReferenceMap.get(var2.getName());
         EJBBusinessActivator var6 = (EJBBusinessActivator)var5.getActivator();

         try {
            Remote var7 = (Remote)((StatefulSessionManager)this.beanManager).remoteCreateForBI(var1, var4, var6, var2);
            return this.getProxyForRemoteBO((StatefulRemoteObject)var7, var2, var5.getGeneratedRemoteInterface());
         } catch (InternalException var8) {
            throw new RemoteException("Remote Exception: ", var8);
         }
      }
   }

   public Object getBusinessImpl(String var1) throws RemoteException {
      OpaqueReferenceImpl var2 = (OpaqueReferenceImpl)this.opaqueReferenceMap.get(var1);
      return this.getBusinessImpl(var2.getBusinessImplClass(), var2.getActivator(), var2.getBusinessIntfClass(), var2.getGeneratedRemoteInterface());
   }

   public Object getBusinessImpl(Class var1, Activator var2, Class var3, Class var4) throws RemoteException {
      try {
         Remote var5 = (Remote)((StatefulSessionManager)this.beanManager).remoteCreateForBI((Object)null, var1, var2, var3);
         return this.getProxyForRemoteBO((StatefulRemoteObject)var5, var3, var4);
      } catch (InternalException var7) {
         RemoteException var6 = new RemoteException("Remote Exception: ", var7.detail);
         throw var6;
      }
   }

   public Remote allocateBI(Object var1, Class var2, Class var3, Activator var4) {
      if (this.getIsInMemoryReplication()) {
         Object var9 = (Map)this.bosMap.get(var1);
         if (var9 == null) {
            var9 = new HashMap();
            this.bosMap.put(var1, var9);
         }

         StatefulRemoteObject var6 = (StatefulRemoteObject)((Map)var9).get(var3.getName());
         if (var6 == null) {
            var6 = this.createNewBO(var1, var2);

            try {
               ServerHelper.exportObject((Remote)var6, "");
            } catch (RemoteException var8) {
               throw new EJBException(var8);
            }

            ((Map)var9).put(var3.getName(), var6);
         }

         return (Remote)var6;
      } else {
         StatefulRemoteObject var5 = this.createNewBO(var1, var2);
         var5.setActivator(var4);
         return (Remote)var5;
      }
   }

   private StatefulRemoteObject createNewBO(Object var1, Class var2) {
      StatefulRemoteObject var3;
      try {
         var3 = (StatefulRemoteObject)var2.newInstance();
      } catch (InstantiationException var5) {
         throw new AssertionError(var5);
      } catch (IllegalAccessException var6) {
         throw new AssertionError(var6);
      }

      var3.setEJBHome(this);
      var3.setBeanManager(this.getBeanManager());
      var3.setBeanInfo(this.getBeanInfo());
      var3.setPrimaryKey(var1);
      return var3;
   }

   public void cleanup() {
      Iterator var1 = this.opaqueReferenceMap.values().iterator();

      while(var1.hasNext()) {
         OpaqueReferenceImpl var2 = (OpaqueReferenceImpl)var1.next();
         this.unexportEJBActivator(var2.getActivator(), var2.getBusinessImplClass());
      }

   }

   private Object getProxyForRemoteBO(StatefulRemoteObject var1, Class var2, Class var3) {
      if (Remote.class.isAssignableFrom(var2)) {
         return var1;
      } else if (var3 == null) {
         throw new AssertionError();
      } else {
         var1.setIsImplementsRemote(false);
         RemoteBusinessIntfProxy var4 = new RemoteBusinessIntfProxy(var1, this.deploymentInfo.getApplicationName(), var2.getName(), var3.getName());
         return Proxy.newProxyInstance(var2.getClassLoader(), new Class[]{var2, BusinessObject.class}, var4);
      }
   }

   public Object createSecondaryForBI(Object var1, String var2) throws RemoteException {
      try {
         Class var3 = this.loadClassForBI(var2);
         return ((ReplicatedBeanManager)this.beanManager).createSecondaryForBI(var1, var3);
      } catch (ClassNotFoundException var4) {
         throw new RemoteException("encounter a remote exception, the nested exception is: ", var4);
      }
   }

   private Class loadClassForBI(String var1) throws ClassNotFoundException {
      Ejb3SessionBeanInfo var2 = (Ejb3SessionBeanInfo)this.beanInfo;
      Class var3 = var2.getBeanClass().getClassLoader().loadClass(var1);
      return var3;
   }

   public void removeSecondary(Object var1) throws RemoteException {
      super.removeSecondary(var1);
      Map var2 = (Map)this.bosMap.remove(var1);
      if (var2 != null) {
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            StatefulRemoteObject var5 = (StatefulRemoteObject)var2.get(var4);
            if (var5 != null) {
               this.unexportBO(var5, false);
            }
         }
      }

   }

   private void unexportBO(StatefulRemoteObject var1, boolean var2) {
      try {
         ServerHelper.unexportObject(var1, true, var2);
      } catch (NoSuchObjectException var4) {
      }

   }

   public void releaseBOs(Object var1) {
      Map var2 = (Map)this.bosMap.remove(var1);
      if (this.getIsInMemoryReplication() && var2 != null) {
         Iterator var3 = var2.keySet().iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            StatefulRemoteObject var5 = (StatefulRemoteObject)var2.get(var4);
            if (var5 != null) {
               this.unexportBO(var5, false);
            }
         }
      }

   }
}
