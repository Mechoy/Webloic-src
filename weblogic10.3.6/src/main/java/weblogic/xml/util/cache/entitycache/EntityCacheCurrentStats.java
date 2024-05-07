package weblogic.xml.util.cache.entitycache;

import java.io.Serializable;
import java.security.AccessController;
import java.util.Enumeration;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.EntityCacheCurrentStateRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class EntityCacheCurrentStats extends EntityCacheStats implements EntityCacheCurrentStateRuntimeMBean {
   private static final long serialVersionUID = 3556632666101847510L;

   Statistics getStats() {
      this.cache.currentStats.clear();
      Enumeration var1 = this.cache.entries.keys();

      while(var1.hasMoreElements()) {
         Serializable var2 = (Serializable)var1.nextElement();
         CacheEntry var3 = (CacheEntry)this.cache.entries.get(var2);
         this.cache.currentStats.addEntry(var3);
         if (var3.isPersisted()) {
            this.cache.currentStats.writeEntry(var3);
         }
      }

      return this.cache.currentStats;
   }

   boolean changesMade() {
      return this.cache.statsCurrentModification;
   }

   public EntityCacheCurrentStats(EntityCache var1) throws ManagementException {
      super("EntityCacheCurrentState_" + ManagementService.getRuntimeAccess((AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction())).getServerName(), (RuntimeMBean)null, var1);
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      ManagementService.getRuntimeAccess(var2).getServerRuntime().setEntityCacheCurrentStateRuntime(this);
   }

   public synchronized long getMemoryUsage() {
      return this.getStats().getTotalEntries();
   }

   public synchronized long getDiskUsage() {
      return this.getStats().getTotalEntries();
   }
}
