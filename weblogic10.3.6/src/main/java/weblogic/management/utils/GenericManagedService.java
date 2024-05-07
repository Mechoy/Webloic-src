package weblogic.management.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.cluster.migration.Migratable;
import weblogic.cluster.migration.MigrationException;
import weblogic.cluster.migration.MigrationManager;
import weblogic.jms.deployer.BEAdminHandler;
import weblogic.jms.saf.SAFAgentDeployer;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.FileStoreMBean;
import weblogic.management.configuration.JMSServerMBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.server.ServiceFailureException;

public class GenericManagedService {
   private Class mBeanClass;
   private String mBeanName;
   private Class handlerClass;
   private final Map handlers = new HashMap();
   private volatile boolean started;
   private boolean handleMigration;
   private static final boolean debug = System.getProperty("weblogic.management.utils.debug") != null;

   GenericManagedService(Class var1, Class var2, boolean var3) {
      this.mBeanClass = var1;
      this.mBeanName = var1.getName();
      this.handlerClass = var2;
      this.handleMigration = var3;
   }

   Class getMBeanClass() {
      return this.mBeanClass;
   }

   Class getHandlerClass() {
      return this.handlerClass;
   }

   public void start() throws ServiceFailureException {
      if (!this.started) {
         this.started = true;
         if (debug) {
            this.logDebug("Subsystem called start");
         }

         ArrayList var1;
         synchronized(this) {
            var1 = new ArrayList(this.handlers.values());
         }

         DeploymentException var2 = null;
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            ManagedDeployment var4 = (ManagedDeployment)var3.next();
            synchronized(var4) {
               if (var4.activated && var4.migrated && !var4.active) {
                  try {
                     if (debug) {
                        this.logDebug("Calling activate for " + var4);
                     }

                     var4.handler.activate(var4.bean);
                     var4.active = true;
                  } catch (DeploymentException var8) {
                     var2 = var8;
                  }
               }
            }
         }

         if (var2 != null) {
            throw new ServiceFailureException(var2);
         }
      }
   }

   public void stop() throws ServiceFailureException {
      if (this.started) {
         this.started = false;
         if (debug) {
            this.logDebug("Subsystem called stop");
         }

         ArrayList var1;
         synchronized(this) {
            var1 = new ArrayList(this.handlers.values());
         }

         UndeploymentException var2 = null;
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            ManagedDeployment var4 = (ManagedDeployment)var3.next();
            synchronized(var4) {
               if (var4.active) {
                  try {
                     if (debug) {
                        this.logDebug("Calling deactivate for " + var4);
                     }

                     var4.handler.deactivate(var4.bean);
                     var4.active = false;
                  } catch (UndeploymentException var8) {
                     var2 = var8;
                  }
               }
            }
         }

         if (var2 != null) {
            throw new ServiceFailureException(var2);
         }
      }
   }

   void prepareDeployment(DeploymentMBean var1) throws DeploymentException {
      if (debug) {
         this.logDebug("Got prepareDeployment for " + var1);
      }

      ManagedDeployment var2 = this.findDeployment(var1);
      if (var2 == null) {
         var2 = new ManagedDeployment(var1);
         this.addDeployment(var1, var2);
      }

      synchronized(var2) {
         boolean var4 = false;
         var2.bean = var1;
         if (this.handleMigration) {
            TargetMBean[] var5 = var1.getTargets();

            for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
               if (var5[var6] instanceof MigratableTargetMBean) {
                  if (debug) {
                     this.logDebug("Calling registerMigratableTarget for " + var2);
                  }

                  var2.registerMigratableTarget((MigratableTargetMBean)var5[var6]);
                  var4 = true;
               }
            }
         }

         if (!var4) {
            if (debug) {
               this.logDebug("Calling prepare for " + var2);
            }

            var2.handler.prepare(var1);
         }

      }
   }

   void activateDeployment(DeploymentMBean var1) throws DeploymentException {
      if (debug) {
         this.logDebug("Got activateDeployment for " + var1);
      }

      ManagedDeployment var2 = this.findDeployment(var1);
      if (var2 == null) {
         throw new DeploymentException("Deployment " + var1 + " was never prepared");
      } else {
         synchronized(var2) {
            if (debug) {
               this.logDebug("Deployment for " + var2 + " activated=" + var2.activated + " active=" + var2.active + "migrated=" + var2.migrated + "started=" + this.started);
            }

            var2.bean = var1;
            var2.activated = true;
            if (this.started && var2.migrated && !var2.active) {
               if (debug) {
                  this.logDebug("Calling activate for " + var2);
               }

               var2.handler.activate(var1);
               var2.active = true;
            }

         }
      }
   }

   void deactivateDeployment(DeploymentMBean var1) throws UndeploymentException {
      if (debug) {
         this.logDebug("Got deactivateDeployment for " + var1);
      }

      ManagedDeployment var2 = this.findDeployment(var1);
      if (var2 == null) {
         throw new UndeploymentException("Deployment " + var1 + " was never prepared");
      } else {
         synchronized(var2) {
            var2.bean = var1;
            var2.activated = false;
            if (var2.active) {
               if (debug) {
                  this.logDebug("Calling deactivate for " + var2);
               }

               var2.handler.deactivate(var1);
               var2.active = false;
            }

         }
      }
   }

   void unprepareDeployment(DeploymentMBean var1) throws UndeploymentException {
      if (debug) {
         this.logDebug("Got unprepareDeployment for " + var1);
      }

      ManagedDeployment var2 = this.findDeployment(var1);
      if (var2 == null) {
         throw new UndeploymentException("Deployment " + var1 + " was never prepared");
      } else {
         synchronized(var2) {
            var2.bean = var1;
            if (this.handleMigration) {
               synchronized(var2) {
                  TargetMBean[] var5 = var1.getTargets();

                  for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
                     if (var5[var6] instanceof MigratableTargetMBean) {
                        if (debug) {
                           this.logDebug("Unregistering migratableTarget for " + var2);
                        }

                        var2.unregisterMigratableTarget((MigratableTargetMBean)var5[var6]);
                     }
                  }
               }
            }

            if (var2.migrated) {
               if (debug) {
                  this.logDebug("Calling unprepare for " + var2);
               }

               var2.handler.unprepare(var1);
            }
         }

         this.removeDeployment(var1);
      }
   }

   private synchronized ManagedDeployment findDeployment(DeploymentMBean var1) {
      return (ManagedDeployment)this.handlers.get(var1.getName());
   }

   private synchronized void addDeployment(DeploymentMBean var1, ManagedDeployment var2) {
      this.handlers.put(var1.getName(), var2);
   }

   private synchronized void removeDeployment(DeploymentMBean var1) {
      this.handlers.remove(var1.getName());
   }

   private void logDebug(String var1) {
      System.out.println("GenericManagedService " + this.mBeanName + ": " + var1);
   }

   private final class ManagedDeployment implements Migratable {
      private DeploymentMBean bean;
      private GenericAdminHandler handler;
      private boolean activated;
      private boolean migrated;
      private boolean active;
      private int skew = 0;

      ManagedDeployment(DeploymentMBean var2) throws DeploymentException {
         this.bean = var2;
         if (var2 instanceof FileStoreMBean) {
            this.skew = -100;
         }

         if (var2 instanceof JMSServerMBean) {
            this.skew = 100;
         }

         try {
            this.handler = (GenericAdminHandler)GenericManagedService.this.handlerClass.newInstance();
            this.migrated = true;
         } catch (Exception var4) {
            throw new DeploymentException("Can't instantiate handler class: " + var4, var4);
         }
      }

      public String getName() {
         return this.bean.getName();
      }

      synchronized void registerMigratableTarget(MigratableTargetMBean var1) throws DeploymentException {
         this.migrated = false;

         try {
            MigrationManager.singleton().register(this, var1);
         } catch (MigrationException var3) {
            throw new DeploymentException(var3);
         }
      }

      void unregisterMigratableTarget(MigratableTargetMBean var1) throws UndeploymentException {
         try {
            MigrationManager.singleton().unregister(this, var1);
         } catch (MigrationException var3) {
            throw new UndeploymentException(var3);
         }
      }

      public void migratableInitialize() {
      }

      public synchronized void migratableActivate() throws MigrationException {
         if (GenericManagedService.debug) {
            GenericManagedService.this.logDebug("Got migratableActivate for " + this);
         }

         this.migrated = true;

         try {
            if (GenericManagedService.debug) {
               GenericManagedService.this.logDebug("Calling prepare for " + this);
            }

            this.setMigrationStatus(true);
            this.handler.prepare(this.bean);
            if (this.activated && GenericManagedService.this.started && !this.active) {
               if (GenericManagedService.debug) {
                  GenericManagedService.this.logDebug("Calling activate for " + this);
               }

               this.handler.activate(this.bean);
               this.active = true;
            }
         } catch (DeploymentException var6) {
            throw new MigrationException(var6);
         } finally {
            this.setMigrationStatus(false);
         }

      }

      public synchronized void migratableDeactivate() throws MigrationException {
         if (GenericManagedService.debug) {
            GenericManagedService.this.logDebug("Got migratableDeactivate for " + this);
         }

         this.migrated = false;

         try {
            this.setMigrationStatus(true);
            if (this.active) {
               if (GenericManagedService.debug) {
                  GenericManagedService.this.logDebug("Calling deactivate for " + this);
               }

               this.handler.deactivate(this.bean);
               this.active = false;
            }

            if (GenericManagedService.debug) {
               GenericManagedService.this.logDebug("Calling unprepare for " + this);
            }

            this.handler.unprepare(this.bean);
         } catch (UndeploymentException var6) {
            throw new MigrationException(var6);
         } finally {
            this.setMigrationStatus(false);
         }

      }

      private void setMigrationStatus(boolean var1) {
         if (this.handler instanceof BEAdminHandler) {
            ((BEAdminHandler)this.handler).setMigrationInProgress(var1);
         } else if (this.handler instanceof SAFAgentDeployer) {
            ((SAFAgentDeployer)this.handler).setMigrationInProgress(var1);
         }

      }

      public String toString() {
         return "[Handler for " + this.bean.getType() + " " + this.bean.getName() + "]";
      }

      public int getOrder() {
         return -1900 + this.skew;
      }
   }
}
