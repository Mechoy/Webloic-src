package weblogic.jms.module;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import weblogic.application.AdminModeCompletionBarrier;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.ModuleException;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorUpdateFailedException;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.descriptor.utils.DescriptorUtils;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.j2ee.descriptor.wl.TargetableBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSLogger;
import weblogic.jms.JMSService;
import weblogic.jms.backend.BEDDEntityProvider;
import weblogic.jms.backend.BEUDDEntityProvider;
import weblogic.jms.backend.DestinationEntityProvider;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.foreign.ForeignJMSEntityProvider;
import weblogic.jms.frontend.JmsConnectionFactoryEntityProvider;
import weblogic.jms.module.validators.JMSModuleValidator;
import weblogic.jms.saf.ErrorHandlingProvider;
import weblogic.jms.saf.JMSSAFImportedDestinationsEntityProvider;
import weblogic.jms.saf.RemoteContextProvider;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.BasicDeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetInfoMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.utils.StackTraceUtils;

public class JMSModule extends ModuleCoordinator {
   private static final JMSModuleManagedEntityProvider DESTINATION_PROVIDER = new DestinationEntityProvider();
   private static final JMSModuleManagedEntityProvider DD_PROVIDER = new BEDDEntityProvider();
   private static final JMSModuleManagedEntityProvider UDD_PROVIDER = new BEUDDEntityProvider();
   private static final JMSModuleManagedEntityProvider SAF_PROVIDER = new JMSSAFImportedDestinationsEntityProvider();
   private static final JMSModuleManagedEntityProvider CONN_PROVIDER = new JmsConnectionFactoryEntityProvider();
   private static final JMSModuleManagedEntityProvider FOREIGN_PROVIDER = new ForeignJMSEntityProvider();
   private static final ErrorHandlingProvider ERROR_HANDLING_PROVIDER = new ErrorHandlingProvider();
   private static final RemoteContextProvider REMOTE_CONTEXT_PROVIDER = new RemoteContextProvider();
   private static final int NONEXISTENT_TYPE = 0;
   private static final int TARGETED_TYPE = 1;
   private static final int UBIQUITOUS_TARGETED_TYPE = 2;
   private static final int UBIQUITOUS_NONTARGETED_TYPE = 3;
   private static final int QUEUE_TYPE = 0;
   private static final int TOPIC_TYPE = 1;
   private static final int DISTRIBUTED_QUEUE_TYPE = 2;
   private static final int DISTRIBUTED_TOPIC_TYPE = 3;
   private static final int UNIFORM_DISTRIBUTED_QUEUE_TYPE = 4;
   private static final int UNIFORM_DISTRIBUTED_TOPIC_TYPE = 5;
   private static final int REMOTE_CONTEXT_TYPE = 6;
   private static final int JMS_SAF_IMPORTED_DESTINATIONS_TYPE = 7;
   private static final int ERROR_HANDLING_TYPE = 8;
   private static final int JMS_CONNECTION_FACTORY_TYPE = 9;
   private static final int FOREIGN_TYPE = 10;
   static final int MAX_TYPE = 11;
   private static final String QUOTA_STRING = "Quotas";
   private static final String QUEUE_STRING = "Queues";
   private static final String TOPIC_STRING = "Topics";
   private static final String FOREIGN_STRING = "ForeignServers";
   private static final String DISTRIBUTED_QUEUE_STRING = "DistributedQueues";
   private static final String DISTRIBUTED_TOPIC_STRING = "DistributedTopics";
   private static final String JMS_CONNECTION_FACTORY_STRING = "ConnectionFactories";
   private static final String SAF_IMPORTED_DESTINATIONS_STRING = "SAFImportedDestinations";
   private static final String SAF_REMOTE_CONTEXT_STRING = "SAFRemoteContexts";
   private static final String SAF_ERROR_HANDLING_STRING = "SAFErrorHandlings";
   private static final String UNIFORM_DISTRIBUTED_QUEUE_STRING = "UniformDistributedQueues";
   private static final String UNIFORM_DISTRIBUTED_TOPIC_STRING = "UniformDistributedTopics";
   private static final String TEMPLATE_STRING = "Templates";
   private static final String DESTINATION_KEY_STRING = "DestinationKeys";
   private static final int INIT_STATE = 0;
   private static final int PREP_STATE = 1;
   private static final int ACTI_STATE = 2;
   private static final int DONE_STATE = 3;
   private static final int DEAD_STATE = 4;
   private static final int CHNG_STATE = 5;
   private LinkedList[] allEntities;
   private JMSBean wholeModule;
   private Context applicationNamingContext;
   private JMSModuleListener moduleListener;
   private static final HashMap indexInfo = new HashMap();

   JMSModule(String var1, String var2) {
      super(var2, var1);
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:constructor paramUri: " + var1 + " paramModule: " + var2);
      }

      this.allEntities = new LinkedList[11];

      for(int var3 = 0; var3 < 11; ++var3) {
         this.allEntities[var3] = new LinkedList();
      }

   }

   JMSModule(String var1) {
      this(var1, (String)null);
   }

   protected DescriptorBean getModuleDescriptor() {
      return (DescriptorBean)this.wholeModule;
   }

   protected void initializeModule(ApplicationContextInternal var1, DomainMBean var2) throws ModuleException {
      BasicDeploymentMBean var3 = this.getBasicDeployment(var2);
      this.wholeModule = JMSParser.createJMSDescriptor(var1, var3, this.getId());
      AppDeploymentMBean var4 = var1.getAppDeploymentMBean();
      if (var4 != null) {
         try {
            JMSModuleValidator.validateTargeting(this.wholeModule, var4, this.getTargetingBean(var2));
         } catch (IllegalArgumentException var6) {
            throw new ModuleException(var6.getMessage(), var6);
         }
      }

      this.targeter = new TargetingHelper(this, var2, this.moduleName.getEARModuleName(), var1.getApplicationId());
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("=== JMSModule:internalInit(): from application " + this.getId() + " Tree === ");
         DescriptorUtils.writeAsXML((DescriptorBean)this.wholeModule);
      }

      this.processEntities((JMSBean)null, (UpdateInformation)null, var2);
   }

   public void prepare(DomainMBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:prepare() called in " + this.moduleName);
      }

      LinkedList var2 = new LinkedList();
      boolean var3 = false;

      try {
         synchronized(this.allEntities) {
            int var5 = 0;

            while(true) {
               if (var5 >= 11) {
                  break;
               }

               Iterator var6 = this.allEntities[var5].iterator();

               while(var6.hasNext()) {
                  EntityState var7 = (EntityState)var6.next();
                  var7.setState(1);
                  var2.addFirst(var7);
               }

               ++var5;
            }
         }

         var3 = true;
      } finally {
         if (!var3) {
            Iterator var11 = var2.iterator();

            while(var11.hasNext()) {
               EntityState var12 = (EntityState)var11.next();

               try {
                  var12.setState(0);
               } catch (ModuleException var18) {
                  JMSLogger.logUnprepareFailedInPrepare(var12.getName(), this.moduleName.toString(), var18.toString());
               }
            }
         }

      }

   }

   public void activate(DomainMBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:activate() called in " + this.moduleName);
      }

      this.targeter.activate();
      LinkedList var2 = new LinkedList();
      boolean var3 = false;

      try {
         synchronized(this.allEntities) {
            int var5 = 0;

            while(true) {
               if (var5 >= 11) {
                  break;
               }

               Iterator var6 = this.allEntities[var5].iterator();

               while(var6.hasNext()) {
                  EntityState var7 = (EntityState)var6.next();
                  var7.setState(2);
                  var2.addFirst(var7);
               }

               ++var5;
            }
         }

         this.moduleListener = new JMSModuleListener();
         DescriptorBean var4 = (DescriptorBean)this.wholeModule;
         var4.addBeanUpdateListener(this.moduleListener);
         var3 = true;
      } finally {
         if (!var3) {
            Iterator var11 = var2.iterator();

            while(var11.hasNext()) {
               EntityState var12 = (EntityState)var11.next();

               try {
                  var12.setState(1);
               } catch (ModuleException var18) {
                  JMSLogger.logDeactivateFailedInActivate(var12.getName(), this.moduleName.toString(), var18.toString());
               }
            }
         }

      }

   }

   public void adminToProduction() {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:adminToProduction() called in " + this.moduleName);
      }

      this.targeter.adminToProduction();
   }

   public void gracefulProductionToAdmin(AdminModeCompletionBarrier var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:gracefulProductionToAdmin() called in " + this.moduleName);
      }

      this.targeter.productionToAdmin();
   }

   public void forceProductionToAdmin() throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:forceProductionToAdmin() called in " + this.moduleName);
      }

      this.targeter.productionToAdmin();
   }

   public void deactivate(DomainMBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:deactivate() called in " + this.moduleName);
      }

      this.targeter.deactivate();
      if (this.moduleListener != null) {
         DescriptorBean var2 = (DescriptorBean)this.wholeModule;
         var2.removeBeanUpdateListener(this.moduleListener);
         this.moduleListener = null;
      }

      ModuleException var11 = null;
      synchronized(this.allEntities) {
         for(int var4 = 10; var4 >= 0; --var4) {
            ListIterator var5 = this.allEntities[var4].listIterator(this.allEntities[var4].size());

            while(var5.hasPrevious()) {
               EntityState var6 = (EntityState)var5.previous();

               try {
                  var6.setState(1);
               } catch (ModuleException var9) {
                  if (var11 == null) {
                     var11 = var9;
                  }
               }
            }
         }
      }

      if (var11 != null) {
         throw var11;
      }
   }

   public void unprepare(DomainMBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:unprepare() called in " + this.moduleName);
      }

      ModuleException var2 = null;
      this.targeter.unprepare();
      synchronized(this.allEntities) {
         int var4 = 10;

         while(true) {
            if (var4 < 0) {
               break;
            }

            ListIterator var5 = this.allEntities[var4].listIterator(this.allEntities[var4].size());

            while(var5.hasPrevious()) {
               EntityState var6 = (EntityState)var5.previous();

               try {
                  var6.setState(0);
               } catch (ModuleException var9) {
                  if (var2 == null) {
                     var2 = var9;
                  }
               }
            }

            --var4;
         }
      }

      if (var2 != null) {
         throw var2;
      }
   }

   public void destroy(DomainMBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:destroy() called in " + this.moduleName);
      }

      ModuleException var2 = null;
      synchronized(this.allEntities) {
         int var4 = 10;

         while(true) {
            if (var4 < 0) {
               break;
            }

            ListIterator var5 = this.allEntities[var4].listIterator(this.allEntities[var4].size());

            while(var5.hasPrevious()) {
               EntityState var6 = (EntityState)var5.previous();

               try {
                  var6.setState(3);
               } catch (ModuleException var9) {
                  if (var2 == null) {
                     var2 = var9;
                  }
               }
            }

            --var4;
         }
      }

      if (var2 != null) {
         throw var2;
      }
   }

   public void remove(DomainMBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:remove() called in " + this.moduleName);
      }

      ModuleException var2 = null;
      synchronized(this.allEntities) {
         int var4 = 10;

         while(true) {
            if (var4 < 0) {
               break;
            }

            ListIterator var5 = this.allEntities[var4].listIterator(this.allEntities[var4].size());

            while(var5.hasPrevious()) {
               EntityState var6 = (EntityState)var5.previous();

               try {
                  var6.setState(4);
               } catch (ModuleException var9) {
                  if (var2 == null) {
                     var2 = var9;
                  }
               }
            }

            this.allEntities[var4].clear();
            --var4;
         }
      }

      if (var2 != null) {
         throw var2;
      }
   }

   protected Object prepareUpdate(DomainMBean var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule: prepareUpdate called in " + this.moduleName);
      }

      TargetInfoMBean var2 = this.getTargetingBean(var1);
      TargetMBean[] var3 = null;
      if (var2 != null) {
         var3 = var2.getTargets();
      }

      UpdateInformation var4 = new UpdateInformation(11);
      var4.setDefaultTargets(var3);
      BasicDeploymentMBean var5 = this.getBasicDeployment(var1);
      JMSBean var6 = JMSParser.createJMSDescriptor(this.getAppCtx(), var5, this.getId());
      if (this.getModuleType() == 0) {
         try {
            JMSModuleValidator.validateTargeting(var6, var5, this.getTargetingBean(var1));
         } catch (IllegalArgumentException var12) {
            throw new ModuleException(var12.getMessage(), var12);
         }
      }

      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("=== Module " + this.getId() + " Tree === ");
         DescriptorUtils.writeAsXML((DescriptorBean)var6);
      }

      Descriptor var7 = ((DescriptorBean)var6).getDescriptor();
      this.targeter.prepareUpdate(var5, var4, this.getAppCtx());
      var4.setProposedDomain(var1);
      DescriptorBean var8 = (DescriptorBean)this.wholeModule;
      Descriptor var9 = var8.getDescriptor();
      this.moduleListener.setInfo(var4);

      try {
         var9.prepareUpdate(var7, false);
      } catch (DescriptorUpdateRejectedException var11) {
         this.moduleListener.setInfo((UpdateInformation)null);
         throw new ModuleException(var11.getMessage(), var11);
      }

      TargetingHelper var10000 = this.targeter;
      if (TargetingHelper.hasTargetingChanged(var4)) {
         this.processEntities(var6, var4, var1);
      }

      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule: prepareUpdate finished in " + this.moduleName);
      }

      return var4;
   }

   protected void rollbackUpdate(DomainMBean var1, Object var2) {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule: rollbackUpdate called in " + this.moduleName);
      }

      UpdateInformation var3 = (UpdateInformation)var2;
      this.targeter.rollbackUpdate(var3);
      DescriptorBean var4 = (DescriptorBean)this.wholeModule;
      Descriptor var5 = var4.getDescriptor();
      var5.rollbackUpdate();
      this.moduleListener.setInfo((UpdateInformation)null);
      LinkedList[] var6 = var3.getAddedEntities();
      LinkedList[] var7 = var3.getChangedEntities();

      for(int var8 = 10; var8 >= 0; --var8) {
         ListIterator var9 = var6[var8].listIterator(var6[var8].size());

         EntityState var10;
         while(var9.hasPrevious()) {
            var10 = (EntityState)var9.previous();

            try {
               var10.takeDown();
            } catch (ModuleException var12) {
               JMSLogger.logDeactivateFailedInRollbackUpdate(var10.getName(), this.moduleName.toString(), var12.toString());
            }
         }

         var9 = var7[var8].listIterator(var7[var8].size());

         while(var9.hasPrevious()) {
            var10 = (EntityState)var9.previous();

            try {
               var10.setState(2, (List)null, (DomainMBean)null, false);
            } catch (ModuleException var13) {
               JMSLogger.logRollbackChangedFailedInRollbackUpdate(var10.getName(), this.moduleName.toString(), var13.toString());
            }
         }
      }

      var3.close();
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule: rollbackUpdate finished in " + this.moduleName);
      }

   }

   private static void printThrowable(String var0, Throwable var1) {
      int var2 = 0;

      for(Throwable var3 = var1; var3 != null; var3 = var3.getCause()) {
         JMSDebug.JMSModule.debug(var0 + " level=" + var2);
         JMSDebug.JMSModule.debug(StackTraceUtils.throwable2StackTrace(var3));
         ++var2;
      }

   }

   private static void printDUFE(String var0, DescriptorUpdateFailedException var1) {
      printThrowable("ERROR in " + var0, var1);
      Exception[] var2 = var1.getExceptionList();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            printThrowable("ERROR in " + var0 + " inner cause=" + var3, var2[var3]);
         }

      }
   }

   protected void activateUpdate(DomainMBean var1, Object var2) throws ModuleException {
      ModuleException var3 = null;
      UpdateInformation var4 = (UpdateInformation)var2;
      this.targeter.activateUpdate(var4);
      DescriptorBean var5 = (DescriptorBean)this.wholeModule;
      Descriptor var6 = var5.getDescriptor();

      try {
         var6.activateUpdate();
      } catch (DescriptorUpdateFailedException var21) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            printDUFE(this.moduleName.toString(), var21);
         }

         var3 = new ModuleException(var21.getMessage(), var21);
      }

      this.moduleListener.setInfo((UpdateInformation)null);
      LinkedList[] var7 = var4.getAddedEntities();
      LinkedList[] var8 = var4.getDeletedEntities();
      LinkedList[] var9 = var4.getChangedEntities();
      synchronized(this.allEntities) {
         int var11;
         Iterator var12;
         EntityState var13;
         for(var11 = 0; var11 < 11; ++var11) {
            var12 = var7[var11].iterator();

            while(var12.hasNext()) {
               var13 = (EntityState)var12.next();
               var13.setState(2);
               this.allEntities[var11].add(var13);
            }

            var12 = var9[var11].iterator();

            while(var12.hasNext()) {
               var13 = (EntityState)var12.next();
               var13.setState(2, (List)null, (DomainMBean)null, true);
            }
         }

         for(var11 = 10; var11 >= 0; --var11) {
            var12 = var8[var11].iterator();

            while(var12.hasNext()) {
               var13 = (EntityState)var12.next();
               String var14 = var13.getName();
               Iterator var15 = this.allEntities[var11].iterator();

               while(var15.hasNext()) {
                  EntityState var16 = (EntityState)var15.next();
                  if (var16.getName().equals(var14)) {
                     var16.setDoRemove(var13.isDoRemove());

                     try {
                        var16.takeDown();
                     } catch (ModuleException var19) {
                        if (var3 == null) {
                           var3 = var19;
                        }

                        JMSLogger.logDeactivateFailedInActivateUpdate(var16.getName(), this.moduleName.toString(), var19.toString());
                     }

                     var15.remove();
                  }
               }
            }

            var8[var11].clear();
         }
      }

      var4.close();
      if (var3 != null) {
         throw var3;
      }
   }

   private void addEntity(BeanUpdateEvent.PropertyUpdate var1, JMSBean var2, UpdateInformation var3) throws BeanUpdateRejectedException {
      Object var4 = var1.getAddedObject();
      String var5 = var1.getPropertyName();
      Stuff var6 = (Stuff)indexInfo.get(var5);
      if (var6 == null) {
         throw new BeanUpdateRejectedException(JMSExceptionLogger.logAddUnknownTypeLoggable(this.moduleName.toString(), var5).getMessage());
      } else if (var6.getType() != 0) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            NamedEntityBean var7 = (NamedEntityBean)var4;
            JMSDebug.JMSModule.debug("Adding a named entity " + var7.getName() + " of type " + var5 + " in module " + this.moduleName);
         }

         this.addEntity(var5, (NamedEntityBean)var4, var2, var3, (EntityState)null, var3.getProposedDomain());
      }
   }

   private void addEntity(String var1, NamedEntityBean var2, JMSBean var3, UpdateInformation var4, EntityState var5, DomainMBean var6) throws BeanUpdateRejectedException {
      JMSModuleManagedEntityProvider var7 = null;
      EntityState var8 = var5;
      LinkedList[] var9 = var4.getAddedEntities();
      HashMap[] var10 = var4.getAddedEntitiesHash();
      Stuff var11 = (Stuff)indexInfo.get(var1);
      if (var11 == null) {
         throw new BeanUpdateRejectedException(JMSExceptionLogger.logAddUnknownTypeLoggable(this.moduleName.toString(), var1).getMessage());
      } else {
         var7 = var11.getProvider();
         int var12 = var11.getType();
         if (var7 != null && var12 != 0) {
            LinkedList var13 = var9[var11.getIndex()];
            HashMap var14 = var10[var11.getIndex()];
            if (var5 == null) {
               List var15 = null;
               TargetableBean var16;
               switch (var12) {
                  case 1:
                     var16 = (TargetableBean)var2;
                     var15 = this.targeter.getTarget(this.getTargetingBean(var6), var16, var16.getSubDeploymentName(), var4, false);
                     break;
                  case 2:
                     var16 = (TargetableBean)var2;
                     var15 = this.targeter.getTarget(this.getTargetingBean(var6), var16, var16.getSubDeploymentName(), var4, true);
               }

               if (var12 == 1 && (var15 == null || var15.size() <= 0)) {
                  if (JMSDebug.JMSModule.isDebugEnabled()) {
                     JMSDebug.JMSModule.debug("Entity " + var2.getName() + " of type " + var1 + " in module " + this.moduleName + " with subdeployment " + ((TargetableBean)var2).getSubDeploymentName() + " is not targeted locally: " + var15);
                  }

                  return;
               }

               JMSModuleManagedEntity var17;
               try {
                  EntityName var18 = new EntityName(this.moduleName, var2.getName());
                  var17 = var7.createEntity(this.getAppCtx(), var18, this.applicationNamingContext, var3, var2, var15, var6);
               } catch (ModuleException var21) {
                  throw new BeanUpdateRejectedException(JMSExceptionLogger.logErrorAddingTypeLoggable(this.moduleName.toString(), var1, var2.getName()).getMessage(), var21);
               }

               var8 = new EntityState(var17);
            }

            try {
               var8.setState(1);
            } catch (ModuleException var20) {
               try {
                  var8.takeDown();
               } catch (ModuleException var19) {
                  JMSLogger.logDestroyFailedInAdd(var8.getName(), this.moduleName.toString(), var19.toString());
               }

               throw new BeanUpdateRejectedException(var20.getMessage(), var20);
            }

            var13.add(var8);
            var14.put(var8.getName(), var8);
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Entity " + var2.getName() + " of type " + var1 + " in module " + this.moduleName + " was succesfully added");
            }

         } else {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("Entity " + var2.getName() + " of type " + var1 + " in module " + this.moduleName + " has no associated factory, no need to add");
            }

         }
      }
   }

   private void removeEntity(NamedEntityBean var1, String var2, UpdateInformation var3, boolean var4) throws BeanUpdateRejectedException {
      String var5 = var1.getName();
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("Removing a named entity named " + var5);
      }

      Stuff var6 = (Stuff)indexInfo.get(var2);
      if (var6 == null) {
         throw new BeanUpdateRejectedException(JMSExceptionLogger.logDeleteUnknownTypeLoggable(this.moduleName.toString(), var2).getMessage());
      } else {
         int var7 = var6.getIndex();
         if (var7 >= 0) {
            LinkedList[] var8 = var3.getDeletedEntities();
            LinkedList var9 = var8[var7];
            HashMap[] var10 = var3.getDeletedEntitiesHash();
            HashMap var11 = var10[var7];
            if (!var11.containsKey(var5)) {
               EntityState var12 = new EntityState(var5);
               var12.setDoRemove(var4);
               var9.add(var12);
               var11.put(var5, var12);
            }
         }
      }
   }

   private void changeEntity(TargetableBean var1, int var2, String var3, int var4, DomainMBean var5, UpdateInformation var6) throws ModuleException {
      String var7 = var1.getName();
      LinkedList var8 = null;
      LinkedList[] var9 = null;
      switch (var4) {
         case 1:
            var9 = var6.getAddedEntities();
            break;
         case 2:
            var9 = var6.getChangedEntities();
            break;
         case 3:
            var9 = var6.getChangedEntities();
            break;
         default:
            throw new AssertionError("realChangeState is UNCHANGED for " + var1.getName() + " type " + var3 + " change state=" + var4);
      }

      Stuff var10 = (Stuff)indexInfo.get(var3);
      if (var10 == null) {
         throw new ModuleException(JMSExceptionLogger.logDeleteUnknownTypeLoggable(this.moduleName.toString(), var3).getMessage());
      } else {
         int var11 = var10.getIndex();
         if (var11 >= 0) {
            var8 = var9[var11];
            LinkedList var12 = this.allEntities[var11];
            Iterator var13 = var12.iterator();
            EntityState var14 = null;

            while(var13.hasNext()) {
               EntityState var15 = (EntityState)var13.next();
               if (var15.getName().equals(var7)) {
                  var14 = var15;
                  break;
               }
            }

            if (var14 == null) {
               throw new AssertionError("A changed entity must have an entity.  Module=" + this.moduleName + " entity=" + var7);
            } else {
               boolean var17 = var2 == 2;
               List var16 = var4 == 2 ? null : this.targeter.getTarget(this.getTargetingBean(var5), var1, var1.getSubDeploymentName(), var6, var17);
               var14.setState(5, var16, var5, true);
               var8.add(var14);
            }
         }
      }
   }

   private void processEntities(JMSBean var1, UpdateInformation var2, DomainMBean var3) throws ModuleException {
      boolean var4 = false;
      LinkedList[] var5 = new LinkedList[11];

      for(int var6 = 0; var6 < 11; ++var6) {
         var5[var6] = new LinkedList();
      }

      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModule:processEntities called in " + this.moduleName + (var1 == null ? " for initialization" : " for update"));
      }

      HashMap[] var51 = var1 != null ? var2.getAddedEntitiesHash() : null;
      HashMap[] var7 = var1 != null ? var2.getDeletedEntitiesHash() : null;
      JMSBean var8;
      if (var1 == null) {
         this.initializeNamingContext();
         var8 = this.wholeModule;
      } else {
         var8 = var1;
      }

      Object var9 = null;
      LinkedList var10 = null;
      String var11 = null;
      TargetInfoMBean var12 = var3 == null ? null : this.getTargetingBean(var3);
      boolean var13 = var12 == null ? false : var12 instanceof BasicDeploymentMBean;
      HashSet var14 = null;

      try {
         for(int var15 = 0; var15 < 11; ++var15) {
            var10 = var5[var15];
            switch (var15) {
               case 0:
                  var9 = var8.getQueues();
                  var11 = "Queues";
                  break;
               case 1:
                  var9 = var8.getTopics();
                  var11 = "Topics";
                  break;
               case 2:
                  var9 = var8.getDistributedQueues();
                  var11 = "DistributedQueues";
                  break;
               case 3:
                  var9 = var8.getDistributedTopics();
                  var11 = "DistributedTopics";
                  break;
               case 4:
                  var9 = var8.getUniformDistributedQueues();
                  var11 = "UniformDistributedQueues";
                  break;
               case 5:
                  var9 = var8.getUniformDistributedTopics();
                  var11 = "UniformDistributedTopics";
                  break;
               case 6:
                  var9 = var8.getSAFRemoteContexts();
                  var11 = "SAFRemoteContexts";
                  break;
               case 7:
                  var9 = var8.getSAFImportedDestinations();
                  var11 = "SAFImportedDestinations";
                  break;
               case 8:
                  var9 = var8.getSAFErrorHandlings();
                  var11 = "SAFErrorHandlings";
                  break;
               case 9:
                  var9 = var8.getConnectionFactories();
                  var11 = "ConnectionFactories";
                  break;
               case 10:
                  var9 = var8.getForeignServers();
                  var11 = "ForeignServers";
                  break;
               default:
                  throw new AssertionError("ERROR: processEntities in module " + this.moduleName + " got unknown type " + var15);
            }

            Stuff var16 = (Stuff)indexInfo.get(var11);

            for(int var17 = 0; var17 < ((Object[])var9).length; ++var17) {
               Object var18;
               int var19;
               int var20;
               List var21;
               int var22;
               var18 = ((Object[])var9)[var17];
               var21 = null;
               var22 = var16.getType();
               label592:
               switch (var22) {
                  case 1:
                  case 2:
                     TargetableBean var23 = (TargetableBean)var18;
                     String var24 = var23.getSubDeploymentName();
                     if (var12 != null) {
                        if (var14 == null) {
                           var14 = new HashSet();
                        }

                        if (!var14.contains(var24)) {
                           var14.add(var24);
                           SubDeploymentMBean var25;
                           if (var13) {
                              BasicDeploymentMBean var26 = (BasicDeploymentMBean)var12;
                              var25 = var26.lookupSubDeployment(var24);
                           } else {
                              SubDeploymentMBean var55 = (SubDeploymentMBean)var12;
                              var25 = var55.lookupSubDeployment(var24);
                           }

                           if (var25 == null && (!var16.isDefaultTargetingEnabled() || !var23.isDefaultTargetingEnabled())) {
                              JMSLogger.logInvalidJMSModuleSubDeploymentConfiguration(this.moduleName.toString(), var24, ((NamedEntityBean)var18).getName(), var11);
                           }
                        }
                     }

                     HashMap var54;
                     if (var22 == 2) {
                        var20 = var19 = this.targeter.getGroupTargetChangeStatus(var23, var24, var2, true);
                        var21 = this.targeter.getTarget(var12, var23, var24, var2, true);
                        switch (var20) {
                           case 0:
                              if (var1 == null) {
                                 var19 = 1;
                              }
                              break label592;
                           case 1:
                              if (var1 != null) {
                                 var54 = var51[var16.getIndex()];
                                 if (var54.containsKey(((NamedEntityBean)var18).getName())) {
                                    var19 = 0;
                                 } else {
                                    var19 = 3;
                                 }
                              }
                              break label592;
                           case 2:
                              var54 = var7[var16.getIndex()];
                              if (!var54.containsKey(((NamedEntityBean)var18).getName())) {
                                 var19 = 3;
                              }
                              break label592;
                           case 3:
                              var54 = var51[var16.getIndex()];
                              if (var54.containsKey(((NamedEntityBean)var18).getName())) {
                                 var19 = 0;
                              }
                              break label592;
                           default:
                              throw new AssertionError("Unknown change status: " + var20);
                        }
                     } else {
                        var20 = var19 = this.targeter.getGroupTargetChangeStatus(var23, var24, var2, false);
                        var21 = this.targeter.getTarget(var12, var23, var24, var2, false);
                        switch (var20) {
                           case 1:
                              if (var1 != null) {
                                 var54 = var51[var16.getIndex()];
                                 if (var54.containsKey(((NamedEntityBean)var18).getName())) {
                                    var19 = 0;
                                 }
                              }
                              break label592;
                           case 2:
                              var54 = var7[var16.getIndex()];
                              if (var54.containsKey(((NamedEntityBean)var18).getName())) {
                                 var19 = 0;
                              }
                              break label592;
                        }
                     }
                  case 3:
                     if (var1 != null) {
                        var19 = 0;
                        var20 = 0;
                     } else {
                        var19 = 1;
                        var20 = 1;
                     }
                     break;
                  default:
                     throw new AssertionError("An entity of this type " + var11 + " should not have type " + var22);
               }

               switch (var19) {
                  case 0:
                     break;
                  case 1:
                     JMSModuleManagedEntityProvider var52 = var16.getProvider();
                     EntityName var53 = new EntityName(this.moduleName, ((NamedEntityBean)var18).getName());
                     JMSModuleManagedEntity var57 = var52.createEntity(this.getAppCtx(), var53, this.applicationNamingContext, var8, (NamedEntityBean)var18, var21, var3);
                     EntityState var56 = new EntityState(var57);
                     var10.add(var56);
                     if (var1 != null) {
                        try {
                           this.addEntity(var11, (NamedEntityBean)var18, var1, var2, var56, var3);
                        } catch (BeanUpdateRejectedException var49) {
                           Throwable var28 = var49.getCause();
                           if (var28 != null && var28 instanceof ModuleException) {
                              throw (ModuleException)var28;
                           }

                           throw new ModuleException(JMSExceptionLogger.logAddFailedLoggable(this.moduleName.toString(), var56.getName()).getMessage(), var49);
                        }

                        HashMap var27 = var51[var16.getIndex()];
                        var27.put(var56.getName(), var56);
                     }
                     break;
                  case 2:
                     try {
                        this.removeEntity((NamedEntityBean)var18, var11, var2, false);
                        break;
                     } catch (BeanUpdateRejectedException var48) {
                        throw new ModuleException(JMSExceptionLogger.logRemoveFailedLoggable(this.moduleName.toString(), ((NamedEntityBean)var18).getName()).getMessage(), var48);
                     }
                  case 3:
                     if (!(var18 instanceof TargetableBean)) {
                        throw new AssertionError("The named entity " + ((NamedEntityBean)var18).getName() + " of type " + var11 + " must be targeteable to have its targets changed in " + this.moduleName + ".  It is of class " + var18.getClass().getName());
                     }

                     this.changeEntity((TargetableBean)var18, var22, var11, var20, var3, var2);
                     break;
                  default:
                     throw new AssertionError("ERROR: processEntities in module " + this.moduleName + " got unknown change state " + var19);
               }
            }
         }

         var4 = true;
      } finally {
         if (var4) {
            if (var1 == null) {
               synchronized(this.allEntities) {
                  this.allEntities = var5;
               }
            }
         } else {
            int var31;
            if (var1 != null) {
               for(var31 = 0; var31 < 11; ++var31) {
                  var51[var31].clear();
               }

               LinkedList[] var58 = var2.getChangedEntities();

               for(int var32 = 10; var32 >= 0; --var32) {
                  ListIterator var33 = var58[var32].listIterator(var58[var32].size());

                  while(var33.hasPrevious()) {
                     EntityState var34 = (EntityState)var33.previous();

                     try {
                        var34.setState(2, (List)null, (DomainMBean)null, false);
                     } catch (ModuleException var46) {
                        JMSLogger.logRollbackChangeFailedInInit(var34.getName(), this.moduleName.toString(), var46.toString());
                     }
                  }
               }
            }

            for(var31 = 10; var31 >= 0; --var31) {
               ListIterator var59 = var5[var31].listIterator(var5[var31].size());

               while(var59.hasPrevious()) {
                  EntityState var60 = (EntityState)var59.previous();

                  try {
                     var60.takeDown();
                  } catch (ModuleException var45) {
                     JMSLogger.logDeactivateFailedInInit(var60.getName(), this.moduleName.toString(), var45.toString());
                  }
               }
            }
         }

      }
   }

   private void initializeNamingContext() throws ModuleException {
      Context var1 = JMSService.getContext(true);
      Context var2 = null;

      try {
         var2 = (Context)var1.lookup("weblogic");
      } catch (NamingException var6) {
         throw new ModuleException("ERROR: Could not lookup the weblogic context in module " + this.moduleName + " of application " + this.getAppCtx().getApplicationId(), var6);
      }

      try {
         this.applicationNamingContext = var2.createSubcontext(this.getAppCtx().getApplicationId());
      } catch (NameAlreadyBoundException var4) {
      } catch (NamingException var5) {
         throw new ModuleException("ERROR: Could not create the application context in module " + this.moduleName + " of application " + this.getAppCtx().getApplicationId(), var5);
      }

   }

   static {
      indexInfo.put("Queues", new Stuff(DESTINATION_PROVIDER, 0, 1, false));
      indexInfo.put("Topics", new Stuff(DESTINATION_PROVIDER, 1, 1, false));
      indexInfo.put("ForeignServers", new Stuff(FOREIGN_PROVIDER, 10, 1, true));
      indexInfo.put("DistributedQueues", new Stuff(DD_PROVIDER, 2, 3, false));
      indexInfo.put("DistributedTopics", new Stuff(DD_PROVIDER, 3, 3, false));
      indexInfo.put("ConnectionFactories", new Stuff(CONN_PROVIDER, 9, 1, true));
      indexInfo.put("SAFImportedDestinations", new Stuff(SAF_PROVIDER, 7, 2, true));
      indexInfo.put("SAFRemoteContexts", new Stuff(REMOTE_CONTEXT_PROVIDER, 6, 3, false));
      indexInfo.put("SAFErrorHandlings", new Stuff(ERROR_HANDLING_PROVIDER, 8, 3, false));
      indexInfo.put("UniformDistributedQueues", new Stuff(UDD_PROVIDER, 4, 2, true));
      indexInfo.put("UniformDistributedTopics", new Stuff(UDD_PROVIDER, 5, 2, true));
      indexInfo.put("Templates", new Stuff((JMSModuleManagedEntityProvider)null, -1, 0, false));
      indexInfo.put("DestinationKeys", new Stuff((JMSModuleManagedEntityProvider)null, -1, 0, false));
      indexInfo.put("Quotas", new Stuff((JMSModuleManagedEntityProvider)null, -1, 0, false));
   }

   private class EntityState {
      private JMSModuleManagedEntity entity;
      private String name;
      private int state;
      private boolean doRemove;

      private EntityState(String var2) {
         this.state = 0;
         this.doRemove = false;
         this.name = var2;
      }

      private EntityState(JMSModuleManagedEntity var2) {
         this.state = 0;
         this.doRemove = false;
         this.entity = var2;
         this.name = this.entity.getEntityName();
      }

      private String stateToString(int var1) {
         switch (var1) {
            case 0:
               return "INITIALIZED";
            case 1:
               return "PREPARED";
            case 2:
               return "ACTIVE";
            case 3:
               return "FINISHED";
            case 4:
               return "DEAD";
            case 5:
               return "CHANGED";
            default:
               return "UNKNOWN STATE: " + var1;
         }
      }

      private void invalidStateTransition(int var1, int var2) {
         throw new AssertionError("ERROR: An invalid state transition was requested in module " + JMSModule.this.moduleName + " for entity " + this.name + ".  The transition requested was from " + this.stateToString(var1) + " to " + this.stateToString(var2));
      }

      private void setState(int var1) throws ModuleException {
         this.setState(var1, (List)null, (DomainMBean)null, true);
      }

      private void setState(int var1, List var2, DomainMBean var3, boolean var4) throws ModuleException {
         boolean var5 = false;

         try {
            label54:
            switch (var1) {
               case 0:
                  switch (this.state) {
                     case 1:
                        var5 = true;
                        this.entity.unprepare();
                        break label54;
                     default:
                        this.invalidStateTransition(this.state, var1);
                        break label54;
                  }
               case 1:
                  switch (this.state) {
                     case 0:
                        this.entity.prepare();
                        break label54;
                     case 2:
                        var5 = true;
                        this.entity.deactivate();
                        break label54;
                     default:
                        this.invalidStateTransition(this.state, var1);
                        break label54;
                  }
               case 2:
                  switch (this.state) {
                     case 1:
                        this.entity.activate(JMSModule.this.wholeModule);
                        break label54;
                     case 5:
                        var5 = true;
                        if (var4) {
                           this.entity.activateChangeOfTargets();
                        } else {
                           this.entity.rollbackChangeOfTargets();
                        }
                        break label54;
                     default:
                        this.invalidStateTransition(this.state, var1);
                        break label54;
                  }
               case 3:
                  switch (this.state) {
                     case 0:
                        var5 = true;
                        this.entity.destroy();
                        break label54;
                     default:
                        this.invalidStateTransition(this.state, var1);
                        break label54;
                  }
               case 4:
                  switch (this.state) {
                     case 3:
                        var5 = true;
                        this.entity.remove();
                        break label54;
                     default:
                        this.invalidStateTransition(this.state, var1);
                        break label54;
                  }
               case 5:
                  switch (this.state) {
                     case 2:
                        this.entity.prepareChangeOfTargets(var2, var3);
                     case 5:
                        break label54;
                     default:
                        this.invalidStateTransition(this.state, var1);
                        break label54;
                  }
               default:
                  this.invalidStateTransition(this.state, var1);
            }
         } catch (ModuleException var7) {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("ERROR: Entity " + this.entity.getEntityName() + " in module " + JMSModule.this.moduleName + "failed to go from state " + this.stateToString(this.state) + " to state " + this.stateToString(var1) + " due to " + var7.getMessage());
               var7.printStackTrace();
            }

            if (var5) {
               this.state = var1;
            }

            throw var7;
         }

         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: Entity " + this.entity.getEntityName() + " in module " + JMSModule.this.moduleName + " changed from state " + this.stateToString(this.state) + " to state " + this.stateToString(var1));
         }

         this.state = var1;
      }

      private void takeDown() throws ModuleException {
         ModuleException var1 = null;
         if (this.state == 5) {
            try {
               this.setState(2, (List)null, (DomainMBean)null, false);
            } catch (ModuleException var3) {
               var1 = var3;
            }
         }

         if (this.state == 2) {
            try {
               this.setState(1);
            } catch (ModuleException var7) {
               if (var1 != null) {
                  var1 = var7;
               }
            }
         }

         if (this.state == 1) {
            try {
               this.setState(0);
            } catch (ModuleException var6) {
               if (var1 != null) {
                  var1 = var6;
               }
            }
         }

         if (this.state == 0) {
            try {
               this.setState(3);
            } catch (ModuleException var5) {
               if (var1 != null) {
                  var1 = var5;
               }
            }
         }

         if (this.doRemove && this.state == 3) {
            try {
               this.setState(4);
            } catch (ModuleException var4) {
               if (var1 != null) {
                  var1 = var4;
               }
            }
         }

         if (var1 != null) {
            throw var1;
         }
      }

      private String getName() {
         return this.name;
      }

      private void setDoRemove(boolean var1) {
         this.doRemove = var1;
      }

      private boolean isDoRemove() {
         return this.doRemove;
      }

      public int hashCode() {
         return this.name.hashCode();
      }

      public boolean equals(Object var1) {
         if (var1 != null && var1 instanceof EntityState) {
            EntityState var2 = (EntityState)var1;
            return this.name.equals(var2.name);
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      EntityState(JMSModuleManagedEntity var2, Object var3) {
         this((JMSModuleManagedEntity)var2);
      }

      // $FF: synthetic method
      EntityState(String var2, Object var3) {
         this((String)var2);
      }
   }

   private static class Stuff {
      private JMSModuleManagedEntityProvider myProvider;
      private int myIndex;
      private int myType;
      private boolean myDefaultTargetingEnabled;

      private Stuff(JMSModuleManagedEntityProvider var1, int var2, int var3, boolean var4) {
         this.myProvider = var1;
         this.myIndex = var2;
         this.myType = var3;
         this.myDefaultTargetingEnabled = var4;
      }

      private JMSModuleManagedEntityProvider getProvider() {
         return this.myProvider;
      }

      private int getIndex() {
         return this.myIndex;
      }

      private int getType() {
         return this.myType;
      }

      private boolean isDefaultTargetingEnabled() {
         return this.myDefaultTargetingEnabled;
      }

      // $FF: synthetic method
      Stuff(JMSModuleManagedEntityProvider var1, int var2, int var3, boolean var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }

   private class JMSModuleListener implements BeanUpdateListener {
      private UpdateInformation info;

      private JMSModuleListener() {
      }

      private void setInfo(UpdateInformation var1) {
         this.info = var1;
      }

      public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
         boolean var2 = false;

         try {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("INFO: Got module listener prepareUpdate in " + JMSModule.this.moduleName);
            }

            if (this.info == null) {
               return;
            }

            BeanUpdateEvent.PropertyUpdate[] var3 = var1.getUpdateList();
            if (var3 != null) {
               JMSBean var4 = (JMSBean)var1.getProposedBean();

               for(int var5 = 0; var5 < var3.length; ++var5) {
                  switch (var3[var5].getUpdateType()) {
                     case 2:
                        JMSModule.this.addEntity(var3[var5], var4, this.info);
                        break;
                     case 3:
                        JMSModule.this.removeEntity((NamedEntityBean)var3[var5].getRemovedObject(), var3[var5].getPropertyName(), this.info, true);
                  }
               }

               if (JMSDebug.JMSModule.isDebugEnabled()) {
                  JMSDebug.JMSModule.debug("INFO: module listener prepareUpdate exits normally in " + JMSModule.this.moduleName);
               }

               var2 = true;
               return;
            }

            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("INFO: module listener prepareUpdate exits with no events in " + JMSModule.this.moduleName);
            }
         } finally {
            if (!var2) {
               this.info = null;
            }

         }

      }

      public void rollbackUpdate(BeanUpdateEvent var1) {
         try {
            if (JMSDebug.JMSModule.isDebugEnabled()) {
               JMSDebug.JMSModule.debug("INFO: Got module listener rollback event: " + var1 + " in " + JMSModule.this.moduleName);
            }
         } finally {
            this.info = null;
         }

      }

      public void activateUpdate(BeanUpdateEvent var1) {
         this.info = null;
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("INFO: Got module listener activateUpdate, nothing to do in" + JMSModule.this.moduleName);
         }

      }

      // $FF: synthetic method
      JMSModuleListener(Object var2) {
         this();
      }
   }
}
