package weblogic.jms.saf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import weblogic.application.ApplicationContext;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.SAFDestinationBean;
import weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBean;
import weblogic.j2ee.descriptor.wl.SAFQueueBean;
import weblogic.j2ee.descriptor.wl.SAFRemoteContextBean;
import weblogic.j2ee.descriptor.wl.SAFTopicBean;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.JMSTargetsListener;
import weblogic.jms.module.JMSBeanHelper;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SAFAgentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.utils.GenericBeanListener;

public class ImportedDestinationGroup implements JMSModuleManagedEntity, JMSTargetsListener {
   private final String name;
   private SAFImportedDestinationsBean idGroupBean;
   private GenericBeanListener groupBeanListener;
   private static HashMap groupBeanAddRemoveSignatures = new HashMap();
   private static HashMap groupBeanSignatures = new HashMap();
   private final Map importedDestinations = new HashMap();
   private final ApplicationContext appCtx;
   private List localTargets;
   private List preparedTargets;
   private EntityName entityName;
   private boolean defaultTargetingEnabled;
   private static Map localSAFAgents = Collections.synchronizedMap(new HashMap());
   private static LinkedList instances = new LinkedList();

   ImportedDestinationGroup(String var1, ApplicationContext var2, EntityName var3, SAFImportedDestinationsBean var4, List var5, DomainMBean var6) throws ModuleException {
      this.name = var1;
      this.idGroupBean = var4;
      this.entityName = var3;
      this.appCtx = var2;
      this.localTargets = var5;
      this.defaultTargetingEnabled = var4.isDefaultTargetingEnabled();
      SAFQueueBean[] var7 = var4.getSAFQueues();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         this.importedDestinations.put(var7[var8].getName(), new IDBeanHandler(this, var2, var3, var7[var8], var5, var6));
         synchronized(instances) {
            instances.add(this.importedDestinations.get(var7[var8].getName()));
         }
      }

      SAFTopicBean[] var14 = var4.getSAFTopics();

      for(int var9 = 0; var9 < var14.length; ++var9) {
         this.importedDestinations.put(var14[var9].getName(), new IDBeanHandler(this, var2, var3, var14[var9], var5, var6));
         synchronized(instances) {
            instances.add(this.importedDestinations.get(var14[var9].getName()));
         }
      }

   }

   String getName() {
      return this.name;
   }

   public void prepare() throws ModuleException {
      Iterator var1 = this.importedDestinations.values().iterator();

      while(var1.hasNext()) {
         IDBeanHandler var2 = (IDBeanHandler)var1.next();
         var2.prepare();
      }

   }

   SAFRemoteContextBean getRemoteContextBean() {
      return this.idGroupBean.getSAFRemoteContext();
   }

   String getRemoteSAFContextFullyQualifiedName() {
      SAFRemoteContextBean var1 = this.getRemoteContextBean();
      return var1 != null ? JMSBeanHelper.getDecoratedName(this.entityName.getFullyQualifiedModuleName(), var1.getName()) : JMSBeanHelper.getDecoratedName(this.entityName.getFullyQualifiedModuleName(), "#LOCAL SERVER CONTEXT#");
   }

   SAFImportedDestinationsBean getBean() {
      return this.idGroupBean;
   }

   public void activate(JMSBean var1) throws ModuleException {
      this.idGroupBean = var1.lookupSAFImportedDestinations(this.getEntityName());
      this.unregisterBeanUpdateListeners();
      this.registerBeanUpdateListeners();
      SAFQueueBean[] var2 = this.idGroupBean.getSAFQueues();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         IDBeanHandler var4 = (IDBeanHandler)this.importedDestinations.get(var2[var3].getName());
         var4.activate(var2[var3]);
      }

      SAFTopicBean[] var6 = this.idGroupBean.getSAFTopics();

      for(int var7 = 0; var7 < var6.length; ++var7) {
         IDBeanHandler var5 = (IDBeanHandler)this.importedDestinations.get(var6[var7].getName());
         var5.activate(var6[var7]);
      }

   }

   public void deactivate() throws ModuleException {
      this.unregisterBeanUpdateListeners();
      Iterator var1 = this.importedDestinations.values().iterator();

      while(var1.hasNext()) {
         IDBeanHandler var2 = (IDBeanHandler)var1.next();
         var2.deactivate();
      }

   }

   public void unprepare() throws ModuleException {
      Iterator var1 = this.importedDestinations.values().iterator();

      while(var1.hasNext()) {
         IDBeanHandler var2 = (IDBeanHandler)var1.next();
         var2.unprepare();
      }

      this.importedDestinations.clear();
      synchronized(instances) {
         Iterator var3 = instances.iterator();

         while(var3.hasNext()) {
            var3.next();
            var3.remove();
         }

      }
   }

   public void destroy() throws ModuleException {
      Iterator var1 = this.importedDestinations.values().iterator();

      while(var1.hasNext()) {
         IDBeanHandler var2 = (IDBeanHandler)var1.next();
         var2.destroy();
      }

   }

   public void remove() throws ModuleException {
      Iterator var1 = this.importedDestinations.values().iterator();

      while(var1.hasNext()) {
         IDBeanHandler var2 = (IDBeanHandler)var1.next();
         var2.remove();
      }

   }

   public String getEntityName() {
      return this.idGroupBean.getName();
   }

   public void prepareChangeOfTargets(List var1, DomainMBean var2) throws ModuleException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.prepareChangeOfTargets: targets=" + var1);
      }

      this.preparedTargets = var1;
      Iterator var3 = this.importedDestinations.values().iterator();

      while(var3.hasNext()) {
         IDBeanHandler var4 = (IDBeanHandler)var3.next();
         var4.prepareChangeOfTargets(var1, var2);
      }

   }

   public void activateChangeOfTargets() throws ModuleException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.activateChangeOfTargets ");
      }

      this.localTargets = this.preparedTargets;
      Iterator var1 = this.importedDestinations.values().iterator();

      while(var1.hasNext()) {
         IDBeanHandler var2 = (IDBeanHandler)var1.next();
         var2.activateChangeOfTargets();
      }

   }

   public void rollbackChangeOfTargets() {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.rollbackChangeOfTargets ");
      }

      Iterator var1 = this.importedDestinations.values().iterator();

      while(var1.hasNext()) {
         IDBeanHandler var2 = (IDBeanHandler)var1.next();
         var2.rollbackChangeOfTargets();
      }

   }

   public void prepareUpdate(DomainMBean var1, TargetMBean var2, int var3, boolean var4) throws BeanUpdateRejectedException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.prepareUpdate: target = " + var2 + " action = " + var3);
      }

      Iterator var5 = this.importedDestinations.values().iterator();

      while(var5.hasNext()) {
         IDBeanHandler var6 = (IDBeanHandler)var5.next();
         var6.prepareUpdate(var1, var3);
      }

   }

   public void rollbackUpdate() {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.rollbackUpdate ");
      }

      Iterator var1 = this.importedDestinations.values().iterator();

      while(var1.hasNext()) {
         IDBeanHandler var2 = (IDBeanHandler)var1.next();
         var2.rollbackUpdate();
      }

   }

   public void activateUpdate() {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.activateUpdate ");
      }

      Iterator var1 = this.importedDestinations.values().iterator();

      while(var1.hasNext()) {
         IDBeanHandler var2 = (IDBeanHandler)var1.next();
         var2.activateUpdate();
      }

   }

   public static void prepareLocalSAFAgent(SAFAgentMBean var0) throws DeploymentException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.prepareLocalSAFAgent: " + var0.getName());
      }

      localSAFAgents.put(var0.getName(), var0.getName());
      broadcastChangeToAllIDs(JMSBeanHelper.getDomain(var0), true);
   }

   public static void activateLocalSAFAgent(SAFAgentMBean var0) throws DeploymentException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.activateLocalSAFAgent: " + var0.getName());
      }

      localSAFAgents.put(var0.getName(), var0.getName());
      broadcastChangeToAllIDs(JMSBeanHelper.getDomain(var0), true);
      broadcastChangeToAllIDs(JMSBeanHelper.getDomain(var0), false);
   }

   public static void deactivateLocalSAFAgent(SAFAgentMBean var0) throws UndeploymentException {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.deactivateLocalSAFAgent: " + var0.getName());
      }

      localSAFAgents.remove(var0.getName());

      try {
         broadcastChangeToAllIDs(JMSBeanHelper.getDomain(var0), true);
         broadcastChangeToAllIDs(JMSBeanHelper.getDomain(var0), false);
      } catch (DeploymentException var2) {
         throw new UndeploymentException(var2);
      }
   }

   public static void unprepareLocalSAFAgent(SAFAgentMBean var0) {
      if (JMSDebug.JMSSAF.isDebugEnabled()) {
         JMSDebug.JMSSAF.debug("IDG.unprepareLocalSAFAgent: " + var0.getName());
      }

   }

   public static void broadcastChangeToAllIDs(DomainMBean var0, boolean var1) throws DeploymentException {
      boolean var2 = false;
      IDBeanHandler var3 = null;
      synchronized(instances) {
         try {
            Iterator var5 = instances.iterator();

            while(var5.hasNext()) {
               var3 = (IDBeanHandler)var5.next();

               try {
                  if (var1) {
                     var3.reconcileTargets(var0);
                  } else {
                     var3.activateTargetUpdates(true);
                  }
               } catch (BeanUpdateRejectedException var19) {
                  throw new DeploymentException("Rejected bean update", var19);
               }
            }

            var2 = true;
         } finally {
            if (!var2 && var1) {
               ListIterator var9 = instances.listIterator();

               while(var9.hasNext()) {
                  IDBeanHandler var10 = (IDBeanHandler)var9.next();
                  if (var10 == var3) {
                     break;
                  }

                  try {
                     var10.activateTargetUpdates(false);
                  } catch (BeanUpdateRejectedException var18) {
                  }
               }
            }

         }
      }
   }

   private void startAddDest(SAFDestinationBean var1) throws BeanUpdateRejectedException {
      if (var1 != null) {
         try {
            IDBeanHandler var2 = new IDBeanHandler(this, this.appCtx, this.entityName, var1, this.localTargets, (DomainMBean)null);
            var2.prepare();
            this.importedDestinations.put(var1.getName(), var2);
            synchronized(instances) {
               instances.add(var2);
            }
         } catch (ModuleException var6) {
            throw new BeanUpdateRejectedException("Adding destination", var6);
         }
      }
   }

   private void finishAddDest(SAFDestinationBean var1, boolean var2) throws BeanUpdateRejectedException {
      IDBeanHandler var3;
      if (!var2) {
         var3 = (IDBeanHandler)this.importedDestinations.remove(var1.getName());
         synchronized(instances) {
            instances.remove(var3);
         }
      } else {
         var3 = (IDBeanHandler)this.importedDestinations.get(var1.getName());

         try {
            var3.activate(var1);
         } catch (ModuleException var6) {
            throw new BeanUpdateRejectedException("Adding destination", var6);
         }
      }

   }

   public void startAddSAFQueues(SAFQueueBean var1) throws BeanUpdateRejectedException {
      this.startAddDest(var1);
   }

   public void finishAddSAFQueues(SAFQueueBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.finishAddDest(var1, var2);
   }

   public void startAddSAFTopics(SAFTopicBean var1) throws BeanUpdateRejectedException {
      this.startAddDest(var1);
   }

   public void finishAddSAFTopics(SAFTopicBean var1, boolean var2) throws BeanUpdateRejectedException {
      this.finishAddDest(var1, var2);
   }

   public void startRemoveDest(SAFDestinationBean var1) {
   }

   public void finishRemoveDest(SAFDestinationBean var1, boolean var2) {
      if (var2) {
         IDBeanHandler var3 = (IDBeanHandler)this.importedDestinations.remove(var1.getName());
         synchronized(instances) {
            instances.remove(var3);
         }

         try {
            var3.deactivate();
            var3.unprepare();
         } catch (ModuleException var6) {
         }

      }
   }

   public void startRemoveSAFQueues(SAFQueueBean var1) {
      this.startRemoveDest(var1);
   }

   public void finishRemoveSAFQueues(SAFQueueBean var1, boolean var2) {
      this.finishRemoveDest(var1, var2);
   }

   public void startRemoveSAFTopics(SAFTopicBean var1) {
      this.startRemoveDest(var1);
   }

   public void finishRemoveSAFTopics(SAFTopicBean var1, boolean var2) {
      this.finishRemoveDest(var1, var2);
   }

   private void registerBeanUpdateListeners() {
      SAFService.getSAFService().getDeployer().addSAFAgentListener(this);
      this.groupBeanListener = new GenericBeanListener((DescriptorBean)this.idGroupBean, this, groupBeanSignatures, groupBeanAddRemoveSignatures);
   }

   private void unregisterBeanUpdateListeners() {
      SAFService.getSAFService().getDeployer().removeSAFAgentListener(this);
      if (this.groupBeanListener != null) {
         this.groupBeanListener.close();
         this.groupBeanListener = null;
      }

   }

   public void setJNDIPrefix(String var1) {
      Iterator var2 = this.importedDestinations.values().iterator();

      while(var2.hasNext()) {
         IDBeanHandler var3 = (IDBeanHandler)var2.next();
         var3.setJNDIPrefix(var1);
      }

   }

   public void setSAFRemoteContext(SAFRemoteContextBean var1) {
      Iterator var2 = this.importedDestinations.values().iterator();

      while(var2.hasNext()) {
         IDBeanHandler var3 = (IDBeanHandler)var2.next();
         var3.remoteContextChanged();
      }

   }

   public void setDefaultTargetingEnabled(boolean var1) {
      this.defaultTargetingEnabled = var1;
   }

   public boolean isDefaultTargetingEnabled() {
      return this.defaultTargetingEnabled;
   }

   public static Map getLocalSAFAgents() {
      return localSAFAgents;
   }

   public Map getImportedDestinations() {
      return this.importedDestinations;
   }

   static {
      groupBeanAddRemoveSignatures.put("SAFQueues", SAFQueueBean.class);
      groupBeanAddRemoveSignatures.put("SAFTopics", SAFTopicBean.class);
      groupBeanSignatures.put("JNDIPrefix", String.class);
      groupBeanSignatures.put("SAFRemoteContext", SAFRemoteContextBean.class);
      groupBeanSignatures.put("DefaultTargetingEnabled", Boolean.TYPE);
   }
}
