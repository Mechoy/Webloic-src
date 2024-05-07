package weblogic.jms.saf;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import weblogic.application.ApplicationContext;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.MessageLoggingParamsBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.j2ee.descriptor.wl.SAFDestinationBean;
import weblogic.j2ee.descriptor.wl.SAFErrorHandlingBean;
import weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBean;
import weblogic.j2ee.descriptor.wl.SAFQueueBean;
import weblogic.jms.backend.BEDestinationImpl;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.dd.DDConfig;
import weblogic.jms.dd.DDConstants;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDManager;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.TargetListSave;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.SystemResourceMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.utils.GenericBeanListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class IDBeanHandler implements DDConfig, DDConstants {
   private final String name;
   private SAFImportedDestinationsBean idGroupBean;
   private ImportedDestinationGroup idGroup;
   private SAFDestinationBean destBean;
   private ApplicationContextInternal appCtx;
   private String earModuleName;
   private DDHandler ddHandler;
   private static final HashMap targetSignatures;
   private List lotsOfListeners = new LinkedList();
   private HashMap destinations = new HashMap();
   private HashMap activeTargetedServers = new HashMap();
   private HashMap preparedTargetedServers = new HashMap();
   private LinkedList addedLocalDestinations = null;
   private LinkedList preparedForRemovalTargetedServers = new LinkedList();
   private static HashMap idBeanSignatures = new HashMap();
   private static HashMap messageLoggingSignatures;
   private static HashMap domainBeanSignatures;
   private String moduleName;
   private EntityName entityName;
   private String messageLoggingFormat;
   private boolean messageLoggingEnabled = false;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private boolean reconciled = false;
   private TargetListSave savedTargets = null;
   private static final String AT_SIGN = "@";
   private static final String PERSISTENT_QOS = "weblogic.jms.saf.persistent.qos";
   static boolean TODOREMOVEDEBUG;

   IDBeanHandler(ImportedDestinationGroup var1, ApplicationContext var2, EntityName var3, SAFDestinationBean var4, List var5, DomainMBean var6) throws ModuleException {
      this.idGroup = var1;
      this.idGroupBean = var1.getBean();
      this.entityName = var3;
      this.name = JMSBeanHelper.getDecoratedName(var3.getFullyQualifiedModuleName(), JMSBeanHelper.getDecoratedName(this.idGroupBean.getName(), var4.getName()));
      this.destBean = var4;
      this.appCtx = (ApplicationContextInternal)var2;
      this.moduleName = var3.getFullyQualifiedModuleName();
      this.earModuleName = var3.getEARModuleName();
      if (TODOREMOVEDEBUG || JMSDebug.JMSSAF.isDebugEnabled()) {
         TODOREMOVEdebug("Constructing IDBeanHandler: " + this.name + ": moduleName=" + this.moduleName + " earModuleName = " + this.earModuleName + " applicationid = " + (var2 != null ? var2.getApplicationId() : null));
      }

      this.savedTargets = new TargetListSave(var5);
      if (var5 != null) {
         this.makeDD(var6, var5);
      } else {
         this.makeDD(var6, (List)null);
      }

   }

   private BasicDeploymentMBean getBasicDeployment(DomainMBean var1) {
      AppDeploymentMBean var2 = null;
      String var3;
      if ((var2 = this.appCtx.getAppDeploymentMBean()) != null) {
         var3 = var2.getName();
         if ((var2 = var1.lookupAppDeployment(var3)) == null) {
            throw new AssertionError("Cannot find my deployment");
         } else {
            return var2;
         }
      } else {
         SystemResourceMBean var4;
         if ((var4 = this.appCtx.getSystemResourceMBean()) == null) {
            throw new AssertionError("Cannot find my resource");
         } else {
            var3 = var4.getName();
            return var1.lookupJMSSystemResource(var3);
         }
      }
   }

   private static void fillWithSAFAgents(String var0, HashMap var1, DomainMBean var2, ServerMBean[] var3) {
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            fillWithSAFAgents(var0, var1, var2, var3[var4]);
         }

      }
   }

   private static void checkAndAdd(String var0, HashMap var1, String var2) {
      if (var1.put(var2, var2) != null) {
         throw new IllegalArgumentException("Targets of imported destination " + var0 + " overlap");
      }
   }

   private static boolean fillFromSAFAgentMBean(String var0, HashMap var1, SAFAgentMBean var2) {
      if (var2.getServiceType().equals("Receiving-only")) {
         return false;
      } else {
         TargetMBean[] var3 = var2.getTargets();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            TargetMBean var5 = var3[var4];
            if (!(var5 instanceof ServerMBean) && !(var5 instanceof MigratableTargetMBean)) {
               assert var5 instanceof ClusterMBean;

               ServerMBean[] var6 = ((ClusterMBean)var5).getServers();

               for(int var7 = 0; var7 < var6.length; ++var7) {
                  checkAndAdd(var0, var1, getAgentAtServerName(var2.getName(), var6[var7].getName()));
               }
            } else {
               checkAndAdd(var0, var1, getAgentAtServerName(var2.getName(), var5.getName()));
            }
         }

         return true;
      }
   }

   public static String getAgentAtServerName(String var0, String var1) {
      String var2 = var0 + "@" + var1;
      if (TODOREMOVEDEBUG) {
         debugServerPart(var1, var2);
         debugAgentPart(var0, var2);
      }

      return var2;
   }

   private static void debugServerPart(String var0, String var1) {
      String var2;
      if (var0.contains("SendingAgent")) {
         var2 = "PROBLEM sending agent sever wrong order, server=" + var0 + ", for " + var1;
         TODOREMOVEdebugException(var2);
      } else if (var0.contains("@")) {
         var2 = "PROBLEM @ is unexpectedly in sever=" + var0 + ", for " + var1;
         TODOREMOVEdebugException(var2);
      } else {
         TODOREMOVEdebug("target portion of " + var1 + " is " + var0);
      }

   }

   private static void TODOREMOVEdebug(String var0) {
      if (TODOREMOVEDEBUG) {
         System.err.println(var0);
      }

      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(var0);
      }

   }

   private static void TODOREMOVEdebugException(String var0) {
      if (TODOREMOVEDEBUG) {
         (new Exception(var0)).printStackTrace();
      }

      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug(var0, new Exception(var0));
      }

   }

   private static void debugAgentPart(String var0, String var1) {
      String var2;
      if (var0.contains("D1C")) {
         var2 = "PROBLEM sending agentPart sever wrong order, agentPart=" + var0 + ", for " + var1;
         TODOREMOVEdebugException(var2);
      } else if (var0.contains("@")) {
         var2 = "PROBLEM @ is unexpectedly in agentPart=" + var0 + ", for " + var1;
         TODOREMOVEdebugException(var2);
      } else {
         TODOREMOVEdebug("agent bean name  portion of " + var1 + " is " + var0);
      }

   }

   public static boolean hasSafTarget(String var0) {
      return var0.lastIndexOf("@") >= 0;
   }

   public static String extractTargetPart(String var0) {
      String var1 = var0.substring(var0.lastIndexOf("@") + 1);
      if (TODOREMOVEDEBUG) {
         debugServerPart(var1, var0);
      }

      return var1;
   }

   private String extractAgent(String var1) {
      int var2 = var1.lastIndexOf("@");
      if (var2 < 0) {
         AssertionError var7 = new AssertionError("SAF expected @ in " + var1);
         if (TODOREMOVEDEBUG || JMSDebug.JMSSAF.isDebugEnabled()) {
            TODOREMOVEdebugException(var7.getMessage());
         }

         throw var7;
      } else {
         String var3 = var1.substring(0, var2);
         int var4 = var3.lastIndexOf(64);
         if (var4 > -1) {
            String var5 = var3.substring(var4);
            if (TODOREMOVEDEBUG || JMSDebug.JMSSAF.isDebugEnabled()) {
               String var6 = "debug changing agent=" + var3 + ", to " + var5;
               if (TODOREMOVEDEBUG) {
                  TODOREMOVEdebugException(var6);
               } else {
                  TODOREMOVEdebug(var6);
               }
            }

            var3 = var5;
         }

         if (TODOREMOVEDEBUG) {
            debugAgentPart(var3, var1);
         }

         return var3;
      }
   }

   private static boolean isServerLocal(String var0) {
      return ManagementService.getRuntimeAccess(kernelId) == null || ManagementService.getRuntimeAccess(kernelId).getServerName().equals(var0);
   }

   private static void fillWithSAFAgents(String var0, HashMap var1, DomainMBean var2, ServerMBean var3) {
      if (var3 != null) {
         SAFAgentMBean[] var4 = var2.getSAFAgents();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (!var4[var5].getServiceType().equals("Receiving-only")) {
               TargetMBean[] var6 = var4[var5].getTargets();
               if (var6 != null) {
                  for(int var7 = 0; var7 < var6.length; ++var7) {
                     TargetMBean var8 = var6[var7];
                     if (var8 instanceof ServerMBean) {
                        if (var8.getName().equals(var3.getName())) {
                           checkAndAdd(var0, var1, getAgentAtServerName(var4[var5].getName(), var3.getName()));
                        }
                     } else {
                        ClusterMBean var9 = var3.getCluster();
                        if (var9 != null) {
                           if (var9.getName().equals(var8.getName()) && isServerLocal(var3.getName())) {
                              checkAndAdd(var0, var1, getAgentAtServerName(var4[var5].getName(), var3.getName()));
                           }

                           if (var8 instanceof MigratableTargetMBean) {
                              ClusterMBean var10 = ((MigratableTargetMBean)var8).getCluster();
                              if (var9.getName().equals(var10.getName()) && var1.get(getAgentAtServerName(var4[var5].getName(), var8.getName())) == null) {
                                 fillFromSAFAgentMBean(var0, var1, var4[var5]);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

      }
   }

   public static void fillWithMyTargets(String var0, HashMap var1, DomainMBean var2, TargetMBean[] var3) {
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.length; ++var4) {
            TargetMBean var5 = var3[var4];
            if (var5 instanceof ClusterMBean) {
               ServerMBean[] var6 = ((ClusterMBean)var5).getServers();
               fillWithSAFAgents(var0, var1, var2, var6);
            } else if (var5 instanceof ServerMBean) {
               fillWithSAFAgents(var0, var1, var2, (ServerMBean)var5);
            } else {
               if (!(var5 instanceof SAFAgentMBean)) {
                  throw new IllegalArgumentException("The imported destination " + var0 + " has been targeted to an invalid target: " + var5.getName());
               }

               if (!fillFromSAFAgentMBean(var0, var1, (SAFAgentMBean)var5)) {
                  throw new IllegalArgumentException("The imported destination " + var0 + " has been targeted to a SAF Agent that only supports receiving: " + var5.getName());
               }
            }
         }

      }
   }

   private void fillWithMyTargets(HashMap var1, DomainMBean var2, List var3) {
      if (var3 != null) {
         fillWithMyTargets(this.name, var1, var2, (TargetMBean[])((TargetMBean[])var3.toArray(new TargetMBean[0])));
      }

   }

   private void makeDestination(String var1) throws BeanUpdateRejectedException {
      String var2 = this.extractAgent(var1);

      try {
         ImportedDestination var3 = new ImportedDestination(this, this.idGroup, this.appCtx != null ? this.appCtx.getApplicationId() : null, new EntityName(this.entityName.getApplicationName(), this.entityName.getEARModuleName(), this.destBean.getName()), this.name, var2, this.getTimeToLiveDefault(), this.isUseSAFTimeToLiveDefault(), this.destBean instanceof SAFQueueBean ? "queue" : "topic", this.getNonPersistentQos(), this.destBean.getMessageLoggingParams().getMessageLoggingFormat(), this.destBean.getMessageLoggingParams().isMessageLoggingEnabled(), this.getPersistentQos());
         var3.setRemoteJNDIName(this.destBean.getRemoteJNDIName());
         if (TODOREMOVEDEBUG || JMSDebug.JMSSAF.isDebugEnabled()) {
            TODOREMOVEdebug("Putting " + var1 + " in destinations");
         }

         this.destinations.put(var1, var3);
      } catch (ModuleException var4) {
         throw new BeanUpdateRejectedException("Failed to create Destination", var4);
      }
   }

   public String getEntityName() {
      return this.name;
   }

   private void addMember(String var1, DomainMBean var2) throws BeanUpdateRejectedException {
      if (this.isTargetLocal(var1)) {
         this.makeDestination(var1);
         if (JMSDebug.JMSSAF.isDebugEnabled()) {
            JMSDebug.JMSSAF.debug("IDBeanHandler added Member for " + var1);
         }
      }

   }

   private void removeMember(String var1) {
      this.destinations.remove(var1);
   }

   private void makeDD(DomainMBean var1, List var2) throws ModuleException {
      if (var1 == null) {
         if (var2 != null) {
            var1 = JMSBeanHelper.getDomain((TargetMBean)var2.get(0));
         } else {
            var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
         }
      }

      this.fillWithMyTargets(this.preparedTargetedServers, var1, var2);
      Iterator var3 = this.preparedTargetedServers.values().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();

         try {
            this.addMember(var4, var1);
         } catch (BeanUpdateRejectedException var6) {
            throw new ModuleException("Could not create Uniform Distributed Destination", var6);
         }
      }

      this.ddHandler = new DDHandler(this, false);
   }

   private boolean isTargetLocal(String var1) {
      return this.isSAFAgentLocal(this.extractAgent(var1));
   }

   private Iterator makeDestinationsIterator(HashMap var1) {
      LinkedList var2 = new LinkedList();
      if (var1 != null) {
         Iterator var3 = var1.values().iterator();

         while(true) {
            while(var3.hasNext()) {
               String var4 = (String)var3.next();
               if (TODOREMOVEDEBUG || JMSDebug.JMSSAF.isDebugEnabled()) {
                  TODOREMOVEdebug("Trying to find " + var4 + " destinations");
               }

               ImportedDestination var5 = (ImportedDestination)this.destinations.get(var4);
               if (var5 != null) {
                  var2.add(var5);
               } else if (TODOREMOVEDEBUG || JMSDebug.JMSSAF.isDebugEnabled()) {
                  TODOREMOVEdebug("did not find " + var4 + " in destinations, keys " + this.destinations.keySet());
               }
            }

            return var2.listIterator();
         }
      } else {
         return var2.listIterator();
      }
   }

   private Iterator preparedDestinationsIterator() {
      return this.makeDestinationsIterator(this.preparedTargetedServers);
   }

   private Iterator activeDestinationsIterator() {
      return this.makeDestinationsIterator(this.activeTargetedServers);
   }

   private ImportedDestination findDestination(String var1) {
      return (ImportedDestination)this.destinations.get(var1);
   }

   private void reregisterBeanUpdateListeners() {
      this.unregisterBeanUpdateListeners();
      this.registerBeanUpdateListeners();
   }

   public void activate(NamedEntityBean var1) throws ModuleException {
      this.destBean = (SAFDestinationBean)var1;
      Iterator var2 = this.preparedDestinationsIterator();

      while(var2.hasNext()) {
         ImportedDestination var3 = (ImportedDestination)var2.next();
         var3.activate();
      }

      this.reregisterBeanUpdateListeners();
      if (this.ddHandler != null) {
         this.ddHandler = DDManager.activateOrUpdate(this.ddHandler);
      }

      String var10 = ManagementService.getRuntimeAccess(kernelId).getServer().getName();
      ClusterMBean var11 = ManagementService.getRuntimeAccess(kernelId).getServer().getCluster();
      Iterator var4 = this.preparedTargetedServers.values().iterator();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      while(var4.hasNext()) {
         String var7 = (String)var4.next();
         String var8 = var7.substring(var7.lastIndexOf("@") + 1);
         ImportedDestination var9 = this.findDestination(var7);
         if (var11 == null) {
            if (var10.equalsIgnoreCase(var8)) {
               var5.add(getAgentAtServerName(this.name, var7));
               var6.add(var9 == null ? null : var9.getManagedDestination());
            }
         } else {
            var5.add(getAgentAtServerName(this.name, var7));
            var6.add(var9 == null ? null : var9.getManagedDestination());
         }
      }

      this.ddHandler.addMembers((String[])var5.toArray(new String[0]), (BEDestinationImpl[])var6.toArray(new BEDestinationImpl[0]));
      this.activeTargetedServers = this.preparedTargetedServers;
      this.preparedTargetedServers = new HashMap();
   }

   private void registerBeanUpdateListeners() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.destBean, this, idBeanSignatures, (Map)null));
      this.lotsOfListeners.add(new GenericBeanListener((DescriptorBean)this.destBean.getMessageLoggingParams(), this, messageLoggingSignatures, (Map)null));
      this.lotsOfListeners.add(new GenericBeanListener(var1, this, (Map)null, domainBeanSignatures));
      SAFAgentMBean[] var2 = var1.getSAFAgents();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.lotsOfListeners.add(new GenericBeanListener(var2[var3], this, (Map)null, targetSignatures));
      }

   }

   private void unregisterBeanUpdateListeners() {
      ListIterator var1 = this.lotsOfListeners.listIterator();

      while(var1.hasNext()) {
         ((GenericBeanListener)var1.next()).close();
      }

      this.lotsOfListeners.clear();
   }

   public void deactivate() throws ModuleException {
      this.unregisterBeanUpdateListeners();
      Iterator var1 = this.activeDestinationsIterator();

      while(var1.hasNext()) {
         ImportedDestination var2 = (ImportedDestination)var1.next();
         var2.deactivate();
      }

      if (this.ddHandler != null) {
         this.ddHandler.deactivate();
      }

   }

   public void destroy() throws ModuleException {
      Iterator var1 = this.activeDestinationsIterator();

      while(var1.hasNext()) {
         ImportedDestination var2 = (ImportedDestination)var1.next();
         var2.destroy();
      }

   }

   private boolean isSAFAgentLocal(String var1) {
      return ImportedDestinationGroup.getLocalSAFAgents().get(var1) != null;
   }

   private boolean onlyRemoteNewSAFAgents(DomainMBean var1) {
      HashMap var2 = new HashMap();
      this.fillWithMyTargets(var2, var1, this.savedTargets.restoreTargets(var1));
      boolean var3 = false;
      Iterator var4 = var2.values().iterator();

      String var5;
      while(var4.hasNext()) {
         var5 = (String)var4.next();
         if (!this.activeTargetedServers.containsKey(var5)) {
            var3 = true;
            if (this.isSAFAgentLocal(var5)) {
               return false;
            }
         }
      }

      var4 = this.activeTargetedServers.values().iterator();

      while(var4.hasNext()) {
         var5 = (String)var4.next();
         if (!var2.containsKey(var5)) {
            var3 = true;
         }
      }

      return var3;
   }

   public void prepareUpdate(DomainMBean var1, int var2) throws BeanUpdateRejectedException {
      if (this.onlyRemoteNewSAFAgents(var1)) {
         this.reconcileTargets(var1);
      }
   }

   public void rollbackUpdate() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (this.onlyRemoteNewSAFAgents(var1)) {
         try {
            this.activateTargetUpdates(false);
         } catch (BeanUpdateRejectedException var3) {
            throw new AssertionError("activateTargetUpdates failed");
         }
      }
   }

   public void activateUpdate() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      if (this.onlyRemoteNewSAFAgents(var1)) {
         try {
            this.activateTargetUpdates(true);
         } catch (BeanUpdateRejectedException var3) {
            throw new AssertionError("activateTargetUpdates failed");
         }
      }
   }

   public void prepareChangeOfTargets(List var1, DomainMBean var2) throws ModuleException {
      TargetListSave var3 = this.savedTargets;
      this.savedTargets = new TargetListSave(var1);
      if (var3 == null && this.savedTargets != null || var3 != null && this.savedTargets == null || var3.size() != this.savedTargets.size()) {
         synchronized(this) {
            this.reconciled = false;
         }
      }

      if (var2 == null) {
         throw new AssertionError("Cannot find domain!");
      } else {
         try {
            this.reconcileTargets(var2);
         } catch (BeanUpdateRejectedException var6) {
            throw new ModuleException("Rejected targeting change", var6);
         }
      }
   }

   public void activateChangeOfTargets() throws ModuleException {
      try {
         this.activateTargetUpdates(true);
      } catch (BeanUpdateRejectedException var2) {
         throw new ModuleException("Rejected targeting change", var2);
      }
   }

   public void rollbackChangeOfTargets() {
      try {
         this.activateTargetUpdates(false);
      } catch (BeanUpdateRejectedException var2) {
         throw new AssertionError("Rejected targeting change" + var2);
      }
   }

   public void prepare() throws ModuleException {
      Iterator var1 = this.preparedDestinationsIterator();

      while(var1.hasNext()) {
         ImportedDestination var2 = (ImportedDestination)var1.next();
         var2.prepare();
      }

   }

   public void remove() throws ModuleException {
      Iterator var1 = this.activeDestinationsIterator();

      while(var1.hasNext()) {
         ImportedDestination var2 = (ImportedDestination)var1.next();
         var2.remove();
      }

   }

   public void unprepare() throws ModuleException {
      Iterator var1 = this.preparedDestinationsIterator();

      ImportedDestination var2;
      while(var1.hasNext()) {
         var2 = (ImportedDestination)var1.next();
         var2.unprepare();
      }

      var1 = this.activeDestinationsIterator();

      while(var1.hasNext()) {
         var2 = (ImportedDestination)var1.next();
         var2.unprepare();
      }

   }

   public void setLocalJNDIName(String var1) {
      this.ddHandler.setJNDIName(this.getJNDIName());
   }

   private void reconcileAddedLocalDestinations(HashMap var1, DomainMBean var2) throws BeanUpdateRejectedException {
      this.addedLocalDestinations = new LinkedList();
      Iterator var3 = var1.values().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (!this.destinations.containsKey(var4) && this.isTargetLocal(var4)) {
            this.addedLocalDestinations.add(var4);
            this.makeDestination(var4);
            ImportedDestination var5 = this.findDestination(var4);

            try {
               var5.prepare();
            } catch (ModuleException var7) {
               throw new BeanUpdateRejectedException("Cannot prepare destination; ", var7);
            }
         }
      }

   }

   private void reconcileAddedMembers(HashMap var1, DomainMBean var2) throws BeanUpdateRejectedException {
      this.preparedTargetedServers = new HashMap();
      Iterator var3 = var1.values().iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (!this.activeTargetedServers.containsKey(var4)) {
            this.addMember(var4, var2);
            ImportedDestination var5 = this.findDestination(var4);
            if (var5 != null) {
               try {
                  var5.prepare();
               } catch (ModuleException var7) {
                  throw new BeanUpdateRejectedException("Cannot prepare destination; ", var7);
               }
            }

            this.preparedTargetedServers.put(var4, var4);
         }
      }

   }

   private void reconcileSubtractedMembers(HashMap var1) {
      this.preparedForRemovalTargetedServers = new LinkedList();
      Iterator var2 = this.activeTargetedServers.values().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (!var1.containsKey(var3)) {
            this.preparedForRemovalTargetedServers.add(var3);
         }
      }

   }

   public void reconcileTargets(DomainMBean var1) throws BeanUpdateRejectedException {
      HashMap var2 = new HashMap();
      synchronized(this) {
         if (this.reconciled) {
            return;
         }

         this.reconciled = true;
      }

      this.fillWithMyTargets(var2, var1, this.savedTargets.restoreTargets(var1));
      this.reconcileAddedMembers(var2, var1);
      this.reconcileAddedLocalDestinations(var2, var1);
      this.reconcileSubtractedMembers(var2);
   }

   private void activateAddedLocalDestinations(boolean var1) throws BeanUpdateRejectedException {
      if (this.addedLocalDestinations != null) {
         ListIterator var2 = this.addedLocalDestinations.listIterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (!var1) {
               this.destinations.remove(var3);
            } else {
               ImportedDestination var4 = this.findDestination(var3);

               try {
                  var4.activate();
               } catch (ModuleException var6) {
                  throw new BeanUpdateRejectedException("activate failed", var6);
               }
            }
         }

         this.addedLocalDestinations = null;
      }
   }

   private void activateAddedMembers(boolean var1) throws BeanUpdateRejectedException {
      if (this.preparedTargetedServers != null) {
         String var3;
         ImportedDestination var4;
         for(Iterator var2 = this.preparedTargetedServers.values().iterator(); var2.hasNext(); this.ddHandler.addMember(getAgentAtServerName(this.name, var3), var4 == null ? null : var4.getManagedDestination())) {
            var3 = (String)var2.next();
            var4 = this.findDestination(var3);
            if (!var1) {
               this.removeMember(var3);
               if (var4 != null) {
                  try {
                     var4.unprepare();
                  } catch (ModuleException var6) {
                     throw new AssertionError("Unprepare failed");
                  }
               }
            } else {
               if (var4 != null) {
                  try {
                     var4.activate();
                  } catch (ModuleException var7) {
                     throw new BeanUpdateRejectedException("activate failed", var7);
                  }
               }

               this.activeTargetedServers.put(var3, var3);
            }
         }

         this.preparedTargetedServers = null;
      }
   }

   private void activateSubtractedMembers(boolean var1) throws BeanUpdateRejectedException {
      if (this.preparedForRemovalTargetedServers != null) {
         String var3;
         for(ListIterator var2 = this.preparedForRemovalTargetedServers.listIterator(); var2.hasNext(); this.ddHandler.removeMember(getAgentAtServerName(this.name, var3))) {
            var3 = (String)var2.next();
            if (var1) {
               ImportedDestination var4 = this.findDestination(var3);
               this.removeMember(var3);
               if (var4 != null) {
                  try {
                     var4.deactivate();
                     var4.destroy();
                     DomainMBean var5 = ManagementService.getRuntimeAccess(kernelId).getDomain();
                     if (var5.lookupSAFAgent(this.extractAgent(var3)) != null && var5.lookupTarget(extractTargetPart(var3)) != null) {
                        var4.unprepare();
                     } else {
                        var4.remove();
                     }
                  } catch (ModuleException var6) {
                     throw new BeanUpdateRejectedException("Cannot bring down member for " + var3, var6);
                  }
               }

               this.activeTargetedServers.remove(var3);
            }
         }

         this.preparedForRemovalTargetedServers = null;
      }
   }

   private void activateSubtractedLocalDestinations() throws BeanUpdateRejectedException {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      Iterator var2 = this.destinations.keySet().iterator();

      while(true) {
         String var3;
         ImportedDestination var4;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (String)var2.next();
            var4 = this.findDestination(var3);
         } while(this.isTargetLocal(var3));

         try {
            var4.deactivate();
            var4.destroy();
            if (var1.lookupSAFAgent(this.extractAgent(var3)) != null && var1.lookupTarget(extractTargetPart(var3)) != null) {
               var4.unprepare();
            } else {
               var4.remove();
            }
         } catch (ModuleException var6) {
            throw new BeanUpdateRejectedException("destroy failed", var6);
         }

         var2.remove();
      }
   }

   public synchronized void activateTargetUpdates(boolean var1) throws BeanUpdateRejectedException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDBeanHandler.activatetargetUpdates: isActive?" + var1 + " conciled = " + this.reconciled);
      }

      if (this.reconciled) {
         this.reconciled = false;
         this.activateAddedLocalDestinations(var1);
         this.activateAddedMembers(var1);
         this.activateSubtractedMembers(var1);
         this.activateSubtractedLocalDestinations();
      }
   }

   public void startAny() throws BeanUpdateRejectedException {
      ListIterator var1 = this.lotsOfListeners.listIterator();
      DomainMBean var2 = null;

      while(var1.hasNext()) {
         GenericBeanListener var3 = (GenericBeanListener)var1.next();
         if (var3.getCurrentEvent() != null) {
            var2 = JMSBeanHelper.getDomain((WebLogicMBean)var3.getCurrentEvent().getProposedBean());
         }
      }

      assert var2 != null;

      this.reconcileTargets(var2);
   }

   public void finishAny(boolean var1) throws BeanUpdateRejectedException {
      this.activateTargetUpdates(var1);
      if (var1) {
         this.reregisterBeanUpdateListeners();
      }

   }

   public void startAddTargets(TargetMBean var1) throws BeanUpdateRejectedException {
      this.startAny();
   }

   public void finishAddTargets(TargetMBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.finishAny(var2);
   }

   public void startRemoveTargets(TargetMBean var1) throws BeanUpdateRejectedException {
      this.startAny();
   }

   public void finishRemoveTargets(TargetMBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.finishAny(var2);
   }

   public void startAddSAFAgents(SAFAgentMBean var1) throws BeanUpdateRejectedException {
      this.startAny();
   }

   public void finishAddSAFAgents(SAFAgentMBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.finishAny(var2);
   }

   public void startRemoveSAFAgents(SAFAgentMBean var1) throws BeanUpdateRejectedException {
      this.startAny();
   }

   public void finishRemoveSAFAgents(SAFAgentMBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.finishAny(var2);
   }

   public void startAddServers(ServerMBean var1) throws BeanUpdateRejectedException {
      this.startAny();
   }

   public void finishAddServers(ServerMBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.finishAny(var2);
   }

   public void startRemoveServers(ServerMBean var1) throws BeanUpdateRejectedException {
      this.startAny();
   }

   public void finishRemoveServers(ServerMBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.finishAny(var2);
   }

   public String getApplicationName() {
      return this.appCtx != null ? this.appCtx.getApplicationId() : null;
   }

   public String getEARModuleName() {
      return this.earModuleName;
   }

   public String getReferenceName() {
      String var1 = null;
      var1 = this.destBean.getRemoteJNDIName();
      if (var1 != null) {
         var1 = var1.replace('/', '.');
      }

      return this.idGroup.getRemoteSAFContextFullyQualifiedName() + "@@" + var1;
   }

   public int getForwardDelay() {
      return 0;
   }

   public boolean getResetDeliveryCountOnForward() {
      return true;
   }

   public String getJNDIName() {
      String var1 = this.idGroupBean.getJNDIPrefix();
      if (var1 == null) {
         var1 = "";
      }

      String var2;
      if (this.destBean.getLocalJNDIName() != null) {
         var2 = this.destBean.getLocalJNDIName();
      } else {
         var2 = this.destBean.getRemoteJNDIName();
      }

      return var1 + var2;
   }

   public int getLoadBalancingPolicyAsInt() {
      return 2;
   }

   public String getName() {
      return this.name;
   }

   public String getSAFExportPolicy() {
      return null;
   }

   public int getType() {
      return this.destBean instanceof SAFQueueBean ? 0 : 1;
   }

   public String getUnitOfOrderRouting() {
      return this.destBean.getUnitOfOrderRouting();
   }

   public boolean isDefaultUnitOfOrder() {
      return false;
   }

   public void setRemoteJNDIName(String var1) {
      Iterator var2 = this.destinations.values().iterator();

      while(var2.hasNext()) {
         ImportedDestination var3 = (ImportedDestination)var2.next();
         var3.setRemoteJNDIName(var1);
      }

   }

   public void setMessageLoggingEnabled(boolean var1) {
      Iterator var2 = this.destinations.values().iterator();

      while(var2.hasNext()) {
         ImportedDestination var3 = (ImportedDestination)var2.next();
         var3.getManagedDestination().setMessageLoggingEnabled(var1);
      }

      this.messageLoggingEnabled = var1;
   }

   public boolean isMessageLoggingEnabled() {
      return this.messageLoggingEnabled;
   }

   public void setMessageLoggingFormat(String var1) {
      Iterator var2 = this.destinations.values().iterator();

      while(var2.hasNext()) {
         ImportedDestination var3 = (ImportedDestination)var2.next();
         var3.getManagedDestination().setMessageLoggingFormat(var1);
      }

      this.messageLoggingFormat = var1;
   }

   public String getMessageLoggingFormat() {
      return this.messageLoggingFormat;
   }

   public void setNonPersistentQos(String var1) {
      Iterator var2 = this.destinations.values().iterator();

      while(var2.hasNext()) {
         ImportedDestination var3 = (ImportedDestination)var2.next();
         var3.setNonPersistentQos(var1);
      }

   }

   public String getNonPersistentQos() {
      return this.destBean.getNonPersistentQos();
   }

   public String getPersistentQos() {
      String var1 = "Exactly-Once";
      String var2 = System.getProperty("weblogic.jms.saf.persistent.qos");
      if (var2 != null) {
         if (var2.toUpperCase().equals("At-Least-Once".toUpperCase())) {
            var1 = "At-Least-Once";
         } else if (var2.toUpperCase().equals("At-Most-Once".toUpperCase())) {
            var1 = "At-Most-Once";
         }
      } else {
         var1 = this.destBean.getPersistentQos();
      }

      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDBeanHandler.getPersistentQos()  " + var1);
      }

      return var1;
   }

   public void setSAFErrorHandling(SAFErrorHandlingBean var1) {
      Iterator var2 = this.destinations.values().iterator();

      while(var2.hasNext()) {
         ImportedDestination var3 = (ImportedDestination)var2.next();
         if (var1 == null) {
            var3.setSAFErrorHandling(this.idGroupBean.getSAFErrorHandling());
         } else {
            var3.setSAFErrorHandling(var1);
         }
      }

   }

   public void setTimeToLiveDefault(long var1) {
      Iterator var3 = this.destinations.values().iterator();

      while(var3.hasNext()) {
         ImportedDestination var4 = (ImportedDestination)var3.next();
         var4.setTimeToLiveDefault(var1);
      }

   }

   public long getTimeToLiveDefault() {
      return this.destBean.getTimeToLiveDefault();
   }

   public boolean isUseSAFTimeToLiveDefault() {
      return this.destBean.isUseSAFTimeToLiveDefault();
   }

   public void setUseSAFTimeToLiveDefault(boolean var1) {
      Iterator var2 = this.destinations.values().iterator();

      while(var2.hasNext()) {
         ImportedDestination var3 = (ImportedDestination)var2.next();
         var3.setUseSAFTimeToLiveDefault(var1);
      }

   }

   public void setJNDIPrefix(String var1) {
      this.setLocalJNDIName(this.destBean.getLocalJNDIName());
   }

   public void remoteContextChanged() {
      Iterator var1 = this.destinations.values().iterator();

      while(var1.hasNext()) {
         ImportedDestination var2 = (ImportedDestination)var1.next();
         var2.remoteContextChanged();
      }

   }

   SAFErrorHandlingBean getSafErrorHandling() {
      return this.destBean.getSAFErrorHandling();
   }

   MessageLoggingParamsBean getMessageLoggingParamsBean() {
      return this.destBean.getMessageLoggingParams();
   }

   static {
      idBeanSignatures.put("RemoteJNDIName", String.class);
      idBeanSignatures.put("LocalJNDIName", String.class);
      idBeanSignatures.put("NonPersistentQos", String.class);
      idBeanSignatures.put("SAFErrorHandling", SAFErrorHandlingBean.class);
      idBeanSignatures.put("TimeToLiveDefault", Long.TYPE);
      idBeanSignatures.put("UseSAFTimeToLiveDefault", Boolean.TYPE);
      messageLoggingSignatures = new HashMap();
      messageLoggingSignatures.put("MessageLoggingFormat", String.class);
      messageLoggingSignatures.put("MessageLoggingEnabled", Boolean.TYPE);
      domainBeanSignatures = new HashMap();
      domainBeanSignatures.put("SAFAgents", SAFAgentMBean.class);
      domainBeanSignatures.put("Servers", ServerMBean.class);
      targetSignatures = new HashMap();
      targetSignatures.put("Targets", TargetMBean.class);
      TODOREMOVEDEBUG = false;
   }
}
