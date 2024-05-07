package weblogic.ejb.container.manager;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EntityBean;
import javax.naming.Context;
import javax.transaction.Transaction;
import weblogic.cluster.ClusterService;
import weblogic.cluster.GroupMessage;
import weblogic.cluster.MulticastSession;
import weblogic.cluster.RecoverListener;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.InternalException;
import weblogic.ejb.container.cache.CacheKey;
import weblogic.ejb.container.cache.PassivationListener;
import weblogic.ejb.container.interfaces.BaseEJBLocalHomeIntf;
import weblogic.ejb.container.interfaces.BaseEJBRemoteHomeIntf;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.ejb.container.interfaces.EntityBeanInfo;
import weblogic.ejb.container.interfaces.InvalidationBeanManager;
import weblogic.ejb.container.interfaces.ReadOnlyManager;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.spi.EJBCache;
import weblogic.ejb.spi.InvalidationMessage;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.kernel.Kernel;
import weblogic.logging.Loggable;
import weblogic.management.configuration.ServerMBean;

public final class ReadOnlyEntityManager extends ExclusiveEntityManager implements PassivationListener, InvalidationBeanManager, RecoverListener, ReadOnlyManager {
   private long readTimeoutMS;
   private Map lastReadMap = Collections.synchronizedMap(new HashMap());
   private boolean inCluster;
   private MulticastSession multicastSession;

   public ReadOnlyEntityManager(EJBComponentRuntimeMBeanImpl var1) {
      super(var1);
   }

   public void setup(BaseEJBRemoteHomeIntf var1, BaseEJBLocalHomeIntf var2, BeanInfo var3, Context var4, EJBCache var5) throws WLDeploymentException {
      super.setup(var1, var2, var3, var4, var5);
      EntityBeanInfo var6 = (EntityBeanInfo)var3;
      this.readTimeoutMS = (long)var6.getCachingDescriptor().getReadTimeoutSeconds() * 1000L;
      this.inCluster = ((ServerMBean)Kernel.getConfig()).getCluster() != null;
      if (this.inCluster) {
         this.multicastSession = ClusterService.getServices().createMulticastSession(this, -1);
      }

   }

   public void passivated(Object var1) {
      this.lastReadMap.remove(var1);
   }

   public void remove(InvocationWrapper var1) throws InternalException {
      this.lastReadMap.remove(var1.getPrimaryKey());
      super.remove(var1);
   }

   protected EntityBean alreadyCached(Transaction var1, Object var2) {
      return (EntityBean)this.cache.get(new CacheKey(var2, this));
   }

   protected boolean shouldLoad(Object var1, boolean var2, boolean var3) {
      if (var3) {
         return false;
      } else {
         long var4 = System.currentTimeMillis();
         Long var6 = (Long)this.lastReadMap.get(var1);
         if (var6 != null) {
            if (this.readTimeoutMS == 0L) {
               return false;
            }

            if (Math.abs(var6 - var4) < this.readTimeoutMS) {
               return false;
            }
         }

         this.lastReadMap.put(var1, new Long(var4));
         return true;
      }
   }

   protected void initLastRead(Object var1) {
      long var2 = System.currentTimeMillis();
      this.lastReadMap.put(var1, new Long(var2));
   }

   protected boolean shouldStoreAfterMethod(InvocationWrapper var1) {
      return false;
   }

   protected boolean shouldStore(EntityBean var1) {
      return false;
   }

   public GroupMessage createRecoverMessage() {
      return new InvalidationMessage(this.info.getDeploymentInfo().getApplicationName(), this.info.getDeploymentInfo().getEJBComponentName(), this.info.getEJBName());
   }

   private void sendInvalidate(Object var1) throws InternalException {
      InvalidationMessage var2;
      if (var1 == null) {
         var2 = new InvalidationMessage(this.info.getDeploymentInfo().getApplicationName(), this.info.getDeploymentInfo().getEJBComponentName(), this.info.getEJBName());
      } else if (var1 instanceof Collection) {
         var2 = new InvalidationMessage(this.info.getDeploymentInfo().getApplicationName(), this.info.getDeploymentInfo().getEJBComponentName(), this.info.getEJBName(), (Collection)var1);
      } else {
         var2 = new InvalidationMessage(this.info.getDeploymentInfo().getApplicationName(), this.info.getDeploymentInfo().getEJBComponentName(), this.info.getEJBName(), var1);
      }

      try {
         this.multicastSession.send(var2);
      } catch (IOException var5) {
         Loggable var4 = EJBLogger.logErrorWhileMulticastingInvalidationLoggable(this.ejbHome.getDisplayName(), var5);
         throw new InternalException(var4.getMessage(), var5);
      }
   }

   public void invalidate(Object var1, Object var2) throws InternalException {
      this.lastReadMap.remove(var2);
      if (this.inCluster) {
         this.sendInvalidate(var2);
      }

   }

   public void invalidate(Object var1, Collection var2) throws InternalException {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         this.lastReadMap.remove(var4);
      }

      if (this.inCluster) {
         this.sendInvalidate(var2);
      }

   }

   public void invalidateAll(Object var1) throws InternalException {
      this.lastReadMap.clear();
      if (this.inCluster) {
         this.sendInvalidate((Object)null);
      }

   }

   public void invalidateLocalServer(Object var1, Object var2) {
      this.lastReadMap.remove(var2);
   }

   public void invalidateLocalServer(Object var1, Collection var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         this.lastReadMap.remove(var3.next());
      }

   }

   public void invalidateAllLocalServer(Object var1) {
      this.lastReadMap.clear();
   }

   public void updateReadTimeoutSeconds(int var1) {
      this.readTimeoutMS = (long)var1 * 1000L;
   }
}
