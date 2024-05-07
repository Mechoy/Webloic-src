package weblogic.ejb.container.internal;

import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.j2ee.ComponentRuntimeMBeanImpl;
import weblogic.kodo.monitoring.KodoPersistenceUnitParent;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.CoherenceClusterRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.EJBRuntimeMBean;
import weblogic.management.runtime.KodoPersistenceUnitRuntimeMBean;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SpringRuntimeMBean;
import weblogic.management.runtime.WseeClientConfigurationRuntimeMBean;
import weblogic.management.runtime.WseeClientRuntimeMBean;
import weblogic.management.runtime.WseeV2RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class EJBComponentRuntimeMBeanImpl extends ComponentRuntimeMBeanImpl implements EJBComponentRuntimeMBean, KodoPersistenceUnitParent {
   private Map m_runtimeMBeans = new HashMap();
   private Map m_runtimePersistenUnit = new HashMap();
   private String applicationName = null;
   private SpringRuntimeMBean springRuntimeMBean;
   private CoherenceClusterRuntimeMBean coherenceClusterRuntimeMBean;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final HashSet<WseeClientRuntimeMBean> wseeClientRuntimes = new HashSet();
   private final HashSet<WseeV2RuntimeMBean> wseeV2Runtimes = new HashSet();
   private final Set<WseeClientConfigurationRuntimeMBean> wseeClientConfigurationRuntimes = new HashSet();

   public EJBComponentRuntimeMBeanImpl(String var1, String var2, RuntimeMBean var3, String var4) throws ManagementException {
      super(var1, var2, var3, true);
      this.applicationName = var4;
   }

   /** @deprecated */
   public int getStatus() {
      return 0;
   }

   public EJBRuntimeMBean[] getEJBRuntimes() {
      EJBRuntimeMBean[] var1 = new EJBRuntimeMBean[this.m_runtimeMBeans.size()];
      var1 = (EJBRuntimeMBean[])((EJBRuntimeMBean[])this.m_runtimeMBeans.values().toArray(var1));
      return var1;
   }

   public EJBRuntimeMBean getEJBRuntime(String var1) {
      return (EJBRuntimeMBean)this.m_runtimeMBeans.get(var1);
   }

   public void addEJBRuntimeMBean(EJBRuntimeMBean var1) {
      if (var1 instanceof MessageDrivenEJBRuntimeMBean) {
         MessageDrivenEJBRuntimeMBean var2 = (MessageDrivenEJBRuntimeMBean)var1;
         String var3 = var2.getEJBName();
         String var4 = var2.getEJBName() + "_" + var2.getDestination();
         if (this.m_runtimeMBeans.containsKey(var3)) {
            this.m_runtimeMBeans.remove(var3);
         }

         if (this.m_runtimeMBeans.containsKey(var4)) {
            this.m_runtimeMBeans.remove(var4);
         }

         this.m_runtimeMBeans.put(var1.getName(), var1);
      } else {
         this.m_runtimeMBeans.put(var1.getEJBName(), var1);
      }

   }

   public void removeEJBRuntimeMBean(EJBRuntimeMBean var1) {
      if (var1 instanceof MessageDrivenEJBRuntimeMBean) {
         this.m_runtimeMBeans.remove(var1.getName());
      } else {
         this.m_runtimeMBeans.remove(var1.getEJBName());
      }

   }

   public EJBComponentMBean getEJBComponent() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      ApplicationMBean var2 = var1.lookupApplication(this.applicationName);
      return var2 == null ? null : var2.lookupEJBComponent(this.getName());
   }

   public String toString() {
      return "EJBComponentRuntimeMBean: name=" + this.getName();
   }

   public void unregisterDependents() throws ManagementException {
      Iterator var1 = this.m_runtimeMBeans.values().iterator();

      while(var1.hasNext()) {
         RuntimeMBeanDelegate var2 = (RuntimeMBeanDelegate)var1.next();
         var2.unregister();
      }

      this.m_runtimeMBeans.clear();
   }

   public void addKodoPersistenceUnit(KodoPersistenceUnitRuntimeMBean var1) {
      this.m_runtimePersistenUnit.put(var1.getPersistenceUnitName(), var1);
   }

   public KodoPersistenceUnitRuntimeMBean[] getKodoPersistenceUnitRuntimes() {
      KodoPersistenceUnitRuntimeMBean[] var1 = new KodoPersistenceUnitRuntimeMBean[this.m_runtimePersistenUnit.size()];
      var1 = (KodoPersistenceUnitRuntimeMBean[])((KodoPersistenceUnitRuntimeMBean[])this.m_runtimePersistenUnit.values().toArray(var1));
      return var1;
   }

   public KodoPersistenceUnitRuntimeMBean getKodoPersistenceUnitRuntime(String var1) {
      return (KodoPersistenceUnitRuntimeMBean)this.m_runtimePersistenUnit.get(var1);
   }

   public SpringRuntimeMBean getSpringRuntimeMBean() {
      return this.springRuntimeMBean;
   }

   public void setSpringRuntimeMBean(SpringRuntimeMBean var1) {
      this.springRuntimeMBean = var1;
   }

   public CoherenceClusterRuntimeMBean getCoherenceClusterRuntime() {
      return this.coherenceClusterRuntimeMBean;
   }

   public void setCoherenceClusterRuntime(CoherenceClusterRuntimeMBean var1) {
      this.coherenceClusterRuntimeMBean = var1;
   }

   public WseeClientRuntimeMBean[] getWseeClientRuntimes() {
      synchronized(this.wseeClientRuntimes) {
         int var2 = this.wseeClientRuntimes.size();
         return (WseeClientRuntimeMBean[])this.wseeClientRuntimes.toArray(new WseeClientRuntimeMBean[var2]);
      }
   }

   public WseeClientRuntimeMBean lookupWseeClientRuntime(String var1) {
      WseeClientRuntimeMBean var2 = null;
      synchronized(this.wseeClientRuntimes) {
         Iterator var4 = this.wseeClientRuntimes.iterator();

         while(var4.hasNext()) {
            WseeClientRuntimeMBean var5 = (WseeClientRuntimeMBean)var4.next();
            if (var5.getName().equals(var1)) {
               var2 = var5;
               break;
            }
         }

         return var2;
      }
   }

   public void addWseeClientRuntime(WseeClientRuntimeMBean var1) {
      synchronized(this.wseeClientRuntimes) {
         this.wseeClientRuntimes.add(var1);
      }
   }

   public void removeWseeClientRuntime(WseeClientRuntimeMBean var1) {
      synchronized(this.wseeClientRuntimes) {
         this.wseeClientRuntimes.remove(var1);
      }
   }

   public WseeV2RuntimeMBean[] getWseeV2Runtimes() {
      synchronized(this.wseeV2Runtimes) {
         int var2 = this.wseeV2Runtimes.size();
         return (WseeV2RuntimeMBean[])this.wseeV2Runtimes.toArray(new WseeV2RuntimeMBean[var2]);
      }
   }

   public WseeV2RuntimeMBean lookupWseeV2Runtime(String var1) {
      WseeV2RuntimeMBean var2 = null;
      synchronized(this.wseeV2Runtimes) {
         Iterator var4 = this.wseeV2Runtimes.iterator();

         while(var4.hasNext()) {
            WseeV2RuntimeMBean var5 = (WseeV2RuntimeMBean)var4.next();
            if (var5.getName().equals(var1)) {
               var2 = var5;
               break;
            }
         }

         return var2;
      }
   }

   public void addWseeV2Runtime(WseeV2RuntimeMBean var1) {
      synchronized(this.wseeV2Runtimes) {
         this.wseeV2Runtimes.add(var1);
      }
   }

   public void removeWseeV2Runtime(WseeV2RuntimeMBean var1) {
      synchronized(this.wseeV2Runtimes) {
         this.wseeV2Runtimes.remove(var1);
      }
   }

   public WseeClientConfigurationRuntimeMBean[] getWseeClientConfigurationRuntimes() {
      synchronized(this.wseeClientConfigurationRuntimes) {
         return (WseeClientConfigurationRuntimeMBean[])this.wseeClientConfigurationRuntimes.toArray(new WseeClientConfigurationRuntimeMBean[this.wseeClientConfigurationRuntimes.size()]);
      }
   }

   public WseeClientConfigurationRuntimeMBean lookupWseeClientConfigurationRuntime(String var1) {
      WseeClientConfigurationRuntimeMBean var2 = null;
      synchronized(this.wseeClientConfigurationRuntimes) {
         Iterator var4 = this.wseeClientConfigurationRuntimes.iterator();

         while(var4.hasNext()) {
            WseeClientConfigurationRuntimeMBean var5 = (WseeClientConfigurationRuntimeMBean)var4.next();
            if (var5.getName().equals(var1)) {
               var2 = var5;
               break;
            }
         }

         return var2;
      }
   }

   public void addWseeClientConfigurationRuntime(WseeClientConfigurationRuntimeMBean var1) {
      synchronized(this.wseeClientConfigurationRuntimes) {
         this.wseeClientConfigurationRuntimes.add(var1);
      }
   }

   public void removeWseeClientConfigurationRuntime(WseeClientConfigurationRuntimeMBean var1) {
      synchronized(this.wseeClientConfigurationRuntimes) {
         this.wseeClientConfigurationRuntimes.remove(var1);
      }
   }
}
