package weblogic.ejb.container.internal;

import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EJBMetaData;
import javax.ejb.Handle;
import javax.ejb.HomeHandle;
import javax.ejb.RemoveException;
import weblogic.ejb.container.interfaces.Ejb3RemoteHome;
import weblogic.ejb.container.interfaces.Ejb3SessionBeanInfo;
import weblogic.ejb.container.interfaces.Ejb3SessionHome;
import weblogic.ejb.spi.BusinessObject;
import weblogic.ejb20.internal.HomeHandleImpl;

public class StatelessEJBHomeImpl extends StatelessEJBHome implements Ejb3SessionHome, Ejb3RemoteHome {
   private final Map ifaceToWrapper = new HashMap();
   private final Map ifaceNameToIface = new HashMap();
   private final Map jndiToEO = new HashMap();

   public StatelessEJBHomeImpl() {
      super((Class)null);
   }

   public StatelessEJBHomeImpl(Class var1) {
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

   public Object getBindableImpl(Class var1) {
      return this.ifaceToWrapper.get(var1);
   }

   public Object getBindableEO(String var1) {
      return this.jndiToEO.get(var1);
   }

   public void prepare() {
      Ejb3SessionBeanInfo var1 = (Ejb3SessionBeanInfo)this.beanInfo;
      Map var2 = var1.getRemoteBusinessJNDINames();
      Iterator var3 = var1.getBusinessRemotes().iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         Class var5 = var1.getGeneratedRemoteBusinessImplClass(var4);
         Class var6 = var1.getGeneratedRemoteBusinessIntfClass(var4);
         StatelessRemoteObject var7 = null;

         try {
            var7 = this.allocateBI(var5);
            this.ifaceNameToIface.put(var4.getName(), var4);
            if (Remote.class.isAssignableFrom(var4)) {
               var7.setIsImplementsRemote(true);
               this.ifaceToWrapper.put(var4, var7);
               if (var2.containsKey(var4)) {
                  continue;
               }
            } else {
               if (var6 == null) {
                  throw new AssertionError();
               }

               var7.setIsImplementsRemote(false);
               RemoteBusinessIntfProxy var8 = new RemoteBusinessIntfProxy(var7, this.deploymentInfo.getApplicationName(), var4.getName(), var6.getName());
               Object var9 = Proxy.newProxyInstance(var4.getClassLoader(), new Class[]{var4, BusinessObject.class}, var8);
               this.ifaceToWrapper.put(var4, var9);
            }

            String var11 = this.getIsIdenticalKey().replace('.', '_') + "_" + var4.getSimpleName();
            this.jndiToEO.put(var11, var7);
         } catch (Exception var10) {
            throw new AssertionError(var10);
         }
      }

   }

   private StatelessRemoteObject allocateBI(Class var1) {
      StatelessRemoteObject var2 = null;

      try {
         var2 = (StatelessRemoteObject)var1.newInstance();
         var2.setEJBHome(this);
         var2.setBeanManager(this.getBeanManager());
         var2.setBeanInfo(this.getBeanInfo());
         return var2;
      } catch (Exception var4) {
         throw new AssertionError(var4);
      }
   }

   public Object getBusinessImpl(Object var1, String var2) throws RemoteException {
      Class var3 = (Class)this.ifaceNameToIface.get(var2);
      return this.getBusinessImpl(var1, var3);
   }

   public Object getBusinessImpl(Object var1, Class var2) throws RemoteException {
      return this.ifaceToWrapper.get(var2);
   }

   public Object getComponentImpl(Object var1) throws RemoteException {
      throw new AssertionError("This method should not be invoked for SLSB");
   }

   public boolean needToConsiderReplicationService() throws RemoteException {
      return false;
   }

   public void undeploy() {
      Iterator var1 = this.jndiToEO.keySet().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         this.unexportEO((Remote)this.jndiToEO.get(var2));
      }

      super.undeploy();
   }
}
