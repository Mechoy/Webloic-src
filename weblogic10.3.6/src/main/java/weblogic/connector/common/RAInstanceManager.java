package weblogic.connector.common;

import com.bea.connector.diagnostic.AdapterType;
import com.bea.connector.diagnostic.InboundAdapterType;
import com.bea.connector.diagnostic.OutboundAdapterType;
import com.bea.connector.diagnostic.WorkManagerType;
import java.security.AccessController;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.naming.Context;
import javax.resource.ResourceException;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;
import javax.resource.spi.ResourceAdapterInternalException;
import javax.resource.spi.work.WorkException;
import weblogic.application.ApplicationContextInternal;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.configuration.DDUtil;
import weblogic.connector.deploy.DeployerUtil;
import weblogic.connector.deploy.JNDIHandler;
import weblogic.connector.deploy.RarArchive;
import weblogic.connector.exception.RACommonException;
import weblogic.connector.exception.RAException;
import weblogic.connector.extensions.Suspendable;
import weblogic.connector.external.AdminObjInfo;
import weblogic.connector.external.RAComplianceException;
import weblogic.connector.external.RAInfo;
import weblogic.connector.external.RAValidationInfo;
import weblogic.connector.external.impl.RAComplianceChecker;
import weblogic.connector.inbound.RAInboundManager;
import weblogic.connector.lifecycle.BootstrapContext;
import weblogic.connector.monitoring.ConnectorComponentRuntimeMBeanImpl;
import weblogic.connector.monitoring.ServiceRuntimeMBeanImpl;
import weblogic.connector.outbound.RAOutboundManager;
import weblogic.connector.security.SecurityPermissions;
import weblogic.connector.security.layer.AdapterLayer;
import weblogic.connector.work.WorkManager;
import weblogic.j2ee.J2EEApplicationRuntimeMBeanImpl;
import weblogic.management.DeploymentException;
import weblogic.management.ManagementException;
import weblogic.management.UndeploymentException;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ConnectorComponentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.ComponentRuntimeMBean;
import weblogic.management.runtime.ConnectorComponentRuntimeMBean;
import weblogic.management.runtime.WorkManagerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.work.WorkManagerCollection;
import weblogic.work.WorkManagerRuntimeMBeanImpl;
import weblogic.work.WorkManagerService;

public class RAInstanceManager {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final String NEW = Debug.getStringNew();
   private final String INITIALIZED = Debug.getStringInitialized();
   private final String PREPARED = Debug.getStringPrepared();
   private final String ACTIVATED = Debug.getStringActivated();
   private final String SUSPENDED = Debug.getStringSuspended();
   private SuspendState suspendState = new SuspendState();
   ConnectorComponentRuntimeMBeanImpl connectorComponentRuntimeMBean = null;
   WorkManagerRuntimeMBean workManagerRuntime = null;
   private BootstrapContext bootstrapContext = null;
   private ResourceAdapter resourceAdapter = null;
   private RAInboundManager raInboundManager = null;
   private RAOutboundManager raOutboundManager = null;
   private RAInfo raInfo = null;
   private ClassLoader classloader = null;
   private String connectorComponentName = null;
   private String applicationName = null;
   private String componentURI = null;
   private WorkManagerCollection workMgrCollection = null;
   private RarArchive explodedRar;
   private VirtualJarFile vjar;
   private boolean lateDeploy = false;
   private String state;
   private boolean suspendedBeforeDeactivate;
   private ApplicationContextInternal appCtx;
   private Context adminObjectCtx;
   private Hashtable<String, AdminObjVersionId> adminObjects;
   private Context connectionFactoryCtx;
   private String moduleName;
   private String jndiName;
   private AdapterLayer adapterLayer;
   private List classFinders;
   private RAValidationInfo raValidationInfo;
   private volatile boolean waitingStartVersioningComplete;

   public RAInstanceManager(RAInfo var1, ClassLoader var2, String var3, String var4, String var5, RarArchive var6, ApplicationContextInternal var7, Context var8, Context var9, String var10, RAInstanceManager var11, AuthenticatedSubject var12) throws RAException, RAComplianceException {
      this.state = this.NEW;
      this.appCtx = null;
      this.adminObjectCtx = null;
      this.adminObjects = null;
      this.connectionFactoryCtx = null;
      this.waitingStartVersioningComplete = false;
      Debug.enter(this, "Constructor");

      try {
         this.classFinders = new Vector(2);
         this.appCtx = var7;
         this.adminObjectCtx = var8;
         this.connectionFactoryCtx = var9;
         if (var7 == null) {
            Debug.throwAssertionError("appCtx == null");
         }

         if (Debug.isWorkEnabled()) {
            Debug.work("Getting work manager collection for app with appId = " + var7.getApplicationId());
         }

         this.workMgrCollection = var7.getWorkManagerCollection();
         if (this.workMgrCollection == null) {
            Debug.throwAssertionError("appCtx.getWorkManagerCollection() == null ");
         }

         if (var1 == null) {
            Debug.throwAssertionError("RAInfo == null");
         }

         this.raInfo = var1;
         this.classloader = var2;
         this.connectorComponentName = var4;
         this.componentURI = var5;
         this.applicationName = var3;
         this.explodedRar = var6;
         this.vjar = var6.getVirtualJarFile();
         this.moduleName = var10;
         this.jndiName = var1.getJndiName();
         String var13;
         if (this.jndiName != null && JNDIHandler.verifyJNDIName(this.jndiName)) {
            Debug.deployment("Failing deployment with duplicate RA JNDI name of " + this.jndiName + " for module " + var10);
            var13 = Debug.getExceptionJndiNameAlreadyBound(this.jndiName);
            throw new RAException(var13);
         }

         this.adapterLayer = new AdapterLayer(this, var12);
         var13 = this.raInfo.getLinkref();
         if (var13 != null && var13.length() > 0) {
            Debug.deployment("A linkref is being deployed : " + var13);
            Debug.logDeprecatedLinkref(this.getModuleName());
            Debug.println(this, "() Check if the base has been deployed");
            RAInstanceManager var14 = LinkrefManager.getBaseRA(var13);
            if (var14 != null) {
               if (Debug.isDeploymentEnabled()) {
                  Debug.deployment("The base RA for the '" + var13 + "'link-ref is already deployed; base RA module name = '" + var14.getModuleName() + "'");
               }

               this.lateDeploy = false;
               Debug.println(this, "() Update the classloader with the base jar");
               DeployerUtil.updateClassFinder((GenericClassLoader)this.classloader, var14.getJarFile(), this.raInfo.isEnableGlobalAccessToClasses(), this.getClassFinders());
               Debug.println(this, "() Update the RAInfo of this linkref with the base raInfo");
               this.raInfo.setBaseRA(var14.getRAInfo());
            } else {
               Debug.deployment("The base RA for the '" + var13 + "'link-ref has not yet been deployed.");
               this.lateDeploy = true;
               Debug.println(this, "() Add the linkref to the linkref manager for future deployment");
               LinkrefManager.addLinkrefRA(this);
               Debug.logRarMarkedForLateDeployment(this.getModuleName());
            }
         }

         this.setupComponentRuntime();
         if (!this.lateDeploy) {
            Debug.println(this, "() initializing the RA object");
            this.initialize(var11, var12);
         }
      } finally {
         Debug.exit(this, "Constructor");
      }

   }

   public synchronized void prepare() throws RAException {
      Debug.enter(this, "prepare()");

      try {
         if (!this.lateDeploy) {
            if (this.state.equals(this.NEW)) {
               String var1 = Debug.getExceptionPrepareUninitializedRA();
               throw new RAException(var1);
            }

            if (this.state.equals(this.INITIALIZED)) {
               DDUtil.validateRAInfo(this.raInfo);
               Debug.println(this, ".prepare() Set the security permissions for the resource adapter");
               SecurityPermissions.setSecurityPermissions(this.raInfo, this.appCtx);
               Debug.println(this, ".prepare() the outbound manager");
               this.raOutboundManager.prepare();
               this.state = this.PREPARED;
            }
         }
      } finally {
         Debug.exit(this, "prepare");
      }

   }

   public synchronized void activate() throws RAException {
      Debug.enter(this, "activate()");

      try {
         if (!this.lateDeploy) {
            String var6;
            if (this.state.equals(this.NEW) || this.state.equals(this.INITIALIZED)) {
               var6 = Debug.getExceptionActivateUnpreparedRA(this.state.toString());
               throw new RAException(var6);
            }

            if (this.state.equals(this.PREPARED)) {
               if (this.suspendedBeforeDeactivate) {
                  var6 = Debug.getExceptionActivateSuspendedRA(this.state.toString());
                  throw new RAException(var6);
               }

               Debug.println(this, ".activate() Call activate on the outbound manager");
               this.raOutboundManager.activate();
               Debug.println(this, ".activate() Call activate on the inbound manager");
               this.raInboundManager.activate();
               this.bindAdminObjects();
               if (this.resourceAdapter != null) {
                  WorkManager var1 = (WorkManager)((WorkManager)this.bootstrapContext.getWorkManager());
                  var1.acceptDoWorkCalls();
                  this.putRAintoJNDITree(this.jndiName);
               }

               if (this.raInfo.getLinkref() == null || this.raInfo.getLinkref().length() == 0) {
                  Debug.println(this, ".activate() Add the base RA to the linkref manager");
                  LinkrefManager.addBaseRA(this);
                  Debug.println(this, ".activate() Deploy the dependent linkrefs");
                  LinkrefManager.deployDependentLinkrefs(this);
               }

               this.state = this.ACTIVATED;
            }
         }
      } finally {
         Debug.exit(this, "activate");
      }

   }

   public synchronized void deactivate() throws RAException {
      Debug.enter(this, "deactivate()");
      RAException var1 = new RAException();

      try {
         if (!this.lateDeploy && (this.state.equals(this.ACTIVATED) || this.state.equals(this.SUSPENDED))) {
            this.suspendedBeforeDeactivate = this.state.equals(this.SUSPENDED);
            this.removeAdminObjsFromJndi(var1);
            this.removeRAfromJNDITree(var1);
            this.deactivateRAInboundMgr(var1);
            this.deactivateRAOutboundMgr(var1);
            this.state = this.PREPARED;
         }

         if (var1.size() > 0) {
            throw var1;
         }
      } finally {
         Debug.exit(this, "deactivate");
      }

   }

   private void deactivateRAInboundMgr(RAException var1) {
      Debug.println(this, ".deactivateRAInboundMgr() Call deactivate on the inbound manager");

      try {
         if (this.raInboundManager != null) {
            this.raInboundManager.deactivate();
         }
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   private void deactivateRAOutboundMgr(RAException var1) {
      Debug.println(this, ".deactivateRAOutboundMgr() Call deactivate on the outbound manager");

      try {
         if (this.raOutboundManager != null) {
            this.raOutboundManager.deactivate();
         }
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   public synchronized void rollback() throws RAException {
      Debug.enter(this, "rollback()");
      RAException var1 = new RAException();

      try {
         if (this.state.equals(this.ACTIVATED)) {
            String var2 = Debug.getExceptionRollbackActivatedRA();
            throw new RAException(var2);
         }

         if (this.state.equals(this.PREPARED)) {
            if (!this.lateDeploy) {
               this.rollbackRAInboundMgr(var1);
               this.rollbackRAOutboundMgr(var1);
               this.rollbackWorkMgr(var1);
               this.suspendWorkManager(var1);
               this.releaseLongRunningWorks(var1);
               this.unsetRASecurity(var1);
               this.callStopOnRA(var1);
               this.unregisterRAInstance(var1);
            }

            this.lateDeploy = false;
            if (this.raInfo.getLinkref() != null && this.raInfo.getLinkref().length() != 0) {
               LinkrefManager.removeLinkrefRA(this, var1);
            } else {
               LinkrefManager.removeBaseRA(this, var1);
            }

            this.state = this.INITIALIZED;
            if (var1.size() > 0) {
               throw var1;
            }
         }
      } finally {
         Debug.exit(this, "rollback");
         this.closeClassFinders();
      }

   }

   private void rollbackRAInboundMgr(RAException var1) {
      Debug.println(this, ".rollbackRAInboundMgr() Call rollback on the inbound manager");

      try {
         if (this.raInboundManager != null) {
            this.raInboundManager.rollback();
         }
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   private void rollbackRAOutboundMgr(RAException var1) {
      Debug.println(this, ".rollbackRAOutboundMgr() Call rollback on the outbound manager");

      try {
         if (this.raOutboundManager != null) {
            this.raOutboundManager.rollback();
         }
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   private void rollbackWorkMgr(RAException var1) {
      Debug.work("RAInstanceManager.rollbackWorkMgr() Rollback work from the work manager");

      try {
         this.workMgrCollection.removeModuleEntries(this.moduleName);
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   private void unregisterRAInstance(RAException var1) {
      Debug.println(this, ".unregisterRAInstance() Unregister this RAInstanceManager");

      try {
         RACollectionManager.unregister(this);
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   private void unsetRASecurity(RAException var1) {
      Debug.println(this, ".unsetRASecurity() unset the security permissions for the resource adapter");

      try {
         SecurityPermissions.unSetSecurityPermissions(this.raInfo);
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   public void stop() throws RAException {
      Debug.enter(this, "stop()");
      Utils.startManagement();

      try {
         this.shutdownRA();
      } finally {
         Utils.stopManagement();
         Debug.exit(this, "stop()");
      }

   }

   private void shutdownRA() throws RAException {
      RAException var1 = new RAException();
      this.stopRAOutboundMgr(var1);
      this.stopRAInboundMgr(var1);
      this.suspendWorkManager(var1);
      this.releaseLongRunningWorks(var1);
      this.callStopOnRA(var1);
      if (var1.size() > 0) {
         throw var1;
      }
   }

   private void stopRAOutboundMgr(RAException var1) {
      try {
         this.raOutboundManager.stop();
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   private void stopRAInboundMgr(RAException var1) {
      try {
         this.raInboundManager.stop();
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   private void suspendWorkManager(RAException var1) {
      if (this.bootstrapContext != null) {
         try {
            WorkManager var2 = (WorkManager)((WorkManager)this.bootstrapContext.getWorkManager());
            var2.suspend();
         } catch (Throwable var3) {
            var1.addError(var3);
         }
      }

   }

   private void releaseLongRunningWorks(RAException var1) {
      if (this.bootstrapContext != null) {
         try {
            WorkManager var2 = (WorkManager)((WorkManager)this.bootstrapContext.getWorkManager());
            var2.getLongRunningWorkManager().cleanup();
         } catch (Throwable var3) {
            var1.addError(var3);
         }
      }

   }

   private void callStopOnRA(RAException var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (this.resourceAdapter != null) {
         try {
            if (Debug.isRALifecycleEnabled()) {
               Debug.raLifecycle("Calling stop on() the ResouceAdapter JavaBean: " + this.adapterLayer.toString(this.resourceAdapter, var2) + " in module '" + this.moduleName + "' having JNDI name '" + this.jndiName + "'");
            }
         } catch (Throwable var5) {
            var1.addError(var5);
         }

         try {
            this.adapterLayer.stop(this.resourceAdapter, var2);
         } catch (Throwable var4) {
            var1.addError(var4);
         }

      }
   }

   public void halt() throws RAException {
      Debug.enter(this, "halt()");
      Utils.startManagement();

      try {
         this.shutdownRA();
      } finally {
         Utils.stopManagement();
         Debug.exit(this, "halt()");
      }

   }

   public void suspend(int var1, Properties var2) throws RAException {
      Debug.enter(this, "suspend( " + var1 + ", props )");
      Utils.startManagement();

      try {
         RAException var3 = null;

         try {
            Debug.println(this, ".suspend() Suspending the resource adapter");
            this.suspendResourceAdapter(var1, var2);
            this.state = this.SUSPENDED;
         } catch (ResourceException var13) {
            var3 = Utils.consolidateException(var3, var13);
         }

         if (this.matches(var1, 2)) {
            try {
               Debug.println(this, ".suspend() Suspending all outbound");
               if (this.raOutboundManager != null) {
                  this.raOutboundManager.suspend();
                  this.suspendState.suspendOutbound();
               }
            } catch (RAException var12) {
               var3 = Utils.consolidateException(var3, var12);
            }
         }

         if (this.matches(var1, 1)) {
            try {
               Debug.println(this, ".suspend() Suspending all inbound");
               if (this.raInboundManager != null) {
                  this.raInboundManager.suspend(var2);
                  this.suspendState.suspendInbound();
               }
            } catch (RAException var11) {
               var3 = Utils.consolidateException(var3, var11);
            }
         }

         if (this.matches(var1, 4) && this.bootstrapContext != null) {
            ((WorkManager)this.bootstrapContext.getWorkManager()).suspend();
            this.suspendState.suspendWork();
         }

         if (var3 != null) {
            throw var3;
         }
      } finally {
         Utils.stopManagement();
         Debug.exit(this, "suspend( " + var1 + ", props )");
      }

   }

   public void resume(int var1, Properties var2) throws RAException {
      Debug.enter(this, "resume( " + var1 + ", props )");
      Utils.startManagement();

      try {
         RAException var3 = null;

         try {
            this.resumeResourceAdapter(var1, var2);
         } catch (ResourceException var13) {
            var3 = Utils.consolidateException(var3, var13);
         }

         if (this.matches(var1, 2)) {
            try {
               if (this.raOutboundManager != null) {
                  this.raOutboundManager.resume();
                  this.suspendState.resumeOutbound();
               }
            } catch (RAException var12) {
               var3 = Utils.consolidateException(var3, var12);
            }
         }

         if (this.matches(var1, 1)) {
            try {
               if (this.raInboundManager != null) {
                  this.raInboundManager.resume(var2);
                  this.suspendState.resumeInbound();
               }
            } catch (RAException var11) {
               var3 = Utils.consolidateException(var3, var11);
            }
         }

         if (this.matches(var1, 4) && this.bootstrapContext != null) {
            ((WorkManager)this.bootstrapContext.getWorkManager()).resume();
            this.suspendState.resumeWork();
         }

         if (this.suspendState.isAllResumed()) {
            this.state = this.ACTIVATED;
         }

         if (var3 != null) {
            throw var3;
         }
      } finally {
         Utils.stopManagement();
         Debug.exit(this, "resume( " + var1 + ", props )");
      }

   }

   public String toString() {
      return "ModuleName = " + this.moduleName + ", jndiName = " + this.jndiName + ", state = " + this.state + "\nRAInfo = " + this.raInfo;
   }

   synchronized void initialize(RAInstanceManager var1, AuthenticatedSubject var2) throws RAException, RAComplianceException {
      Debug.enter(this, "initialize()");

      try {
         if (this.state.equals(this.NEW)) {
            Debug.println(this, ".initialize() Updating the classloader");
            DeployerUtil.updateClassFinder((GenericClassLoader)this.classloader, this.explodedRar, this.raInfo.isEnableGlobalAccessToClasses(), this.getClassFinders());
            this.raValidationInfo = ((RAComplianceChecker)weblogic.connector.external.RAComplianceChecker.factory.createChecker()).validate(this.vjar.getName(), this.raInfo, (GenericClassLoader)this.classloader);
            Debug.println(this, ".initialize() Register with the RAMAnager");
            RACollectionManager.register(this);

            try {
               Debug.println(this, ".initialize() Creating native lib");
               DeployerUtil.createNativeLibDir(this.vjar, this.raInfo, this.appCtx);
            } catch (DeploymentException var33) {
               String var4 = Debug.getExceptionCreateNativeLib();
               throw new RAException(var4, var33);
            }

            String var3 = this.raInfo.getRAClass();
            if (var3 != null) {
               try {
                  Debug.raLifecycle("Creating the ResourceAdapter JavaBean: " + var3);
                  Class var35 = this.adapterLayer.forName(var3, true, this.classloader, var2);
                  this.resourceAdapter = (ResourceAdapter)this.adapterLayer.newInstance(var35, var2);
                  Utils.setProperties(this, this.resourceAdapter, this.raInfo.getRAConfigProps().values(), this.raValidationInfo.getRAPropSetterTable());
               } catch (ClassNotFoundException var29) {
                  throw new RAException(var29.toString(), var29);
               } catch (InstantiationException var30) {
                  throw new RAException(var30.toString(), var30);
               } catch (IllegalAccessException var31) {
                  throw new RAException(var31.toString(), var31);
               } catch (Throwable var32) {
                  throw new RAException(var32.toString(), var32);
               }
            }

            WorkManagerService var36 = null;
            if (this.resourceAdapter != null) {
               String var6;
               try {
                  String var5 = null;
                  var6 = null;
                  J2EEApplicationRuntimeMBeanImpl var7 = this.appCtx.getRuntime();
                  Debug.work("RAInstanceManager.initialize() Associate the resource adapter with the WorkManagerCollection");
                  if (this.raInfo.getWorkManager() != null) {
                     var5 = this.raInfo.getWorkManager().getName();
                     var36 = this.workMgrCollection.populate(this.moduleName, this.raInfo.getWorkManager());
                  } else {
                     var5 = this.moduleName;
                  }

                  weblogic.work.WorkManager var37 = this.workMgrCollection.get(this.moduleName, var5);
                  this.workManagerRuntime = var7.lookupWorkManagerRuntime(var37);
                  Debug.println(this, ".initialize() Create a new BootstrapContext : " + this.moduleName);
                  this.bootstrapContext = new BootstrapContext(this, this.appCtx, this.moduleName, var37);
                  if (this.isVersioned()) {
                     String var8;
                     if (!Utils.isRAVersionable(this, var1)) {
                        var8 = Debug.getExceptionAdapterNotVersionable();
                        throw new RAException(var8);
                     }

                     if (var1 != null) {
                        var8 = var1.getJndiName();
                        ConnectorLogger.logDeploySideBySide(var8, var1.getVersionId(), this.getVersionId());
                        Debug.raLifecycle("Beginning side-by-side versioning of resource adapter with JNDI name = " + var8 + " by calling init() on new version");
                        this.adapterLayer.init((Suspendable)this.resourceAdapter, var1.getResourceAdapter(), (Properties)null, var2);
                        Debug.raLifecycle("Continuing side-by-side versioning of resource adapter with JNDI name = " + var8 + " by calling startVersioning() on old version");
                        var1.waitingStartVersioningComplete = true;
                        this.adapterLayer.startVersioning((Suspendable)var1.getResourceAdapter(), this.resourceAdapter, (Properties)null, var2);
                        Debug.raLifecycle("Completed side-by-side versioning of resource adapter with JNDI name = " + var8);
                     } else {
                        ConnectorLogger.logSkipSideBySide();
                     }
                  } else {
                     ConnectorLogger.logAppNotSideBySide();
                  }

                  Debug.raLifecycle("Calling start() on the ResourceAdapter bean for " + this.adapterLayer.toString(this.resourceAdapter, var2));
                  this.adapterLayer.start(this.resourceAdapter, this.bootstrapContext, var2);
               } catch (ResourceAdapterInternalException var25) {
                  var6 = Debug.getExceptionStartRA(this.getModuleName(), this.adapterLayer.toString(var25, var2));
                  throw new RAException(var6, var25);
               } catch (WorkException var26) {
                  var6 = Debug.getExceptionCreateBootstrap(this.getModuleName(), this.adapterLayer.toString(var26, var2));
                  throw new RAException(var6, var26);
               } catch (ResourceException var27) {
                  var6 = Debug.getExceptionVersionRA();
                  throw new RAException(var6, var27);
               } catch (Throwable var28) {
                  var6 = Debug.getExceptionStartRA(this.getModuleName(), this.adapterLayer.toString(var28, var2));
                  throw new RAException(var6, var28);
               }
            }

            this.setupWorkManagerRuntime(var36);
            Debug.println(this, ".initialize() Create the RAOutboundManager");
            this.raOutboundManager = new RAOutboundManager(this);
            Debug.println(this, ".initialize() Create the RAInboundManager");
            this.raInboundManager = new RAInboundManager(this);
            this.raInboundManager.setupRuntimes(this.connectorComponentRuntimeMBean);
            this.createAdminObjects(var2);
            RACollectionManager.updateCounts(this);
            this.state = this.INITIALIZED;
         }
      } finally {
         if (this.state != this.INITIALIZED) {
            try {
               this.closeClassFinders();
            } catch (Throwable var24) {
            }

            Debug.println(this, ".initialize() UnRegister with the RAMAnager");
            RACollectionManager.unregister(this);
         }

         Debug.exit(this, "initialize");
      }

   }

   private void createAdminObjects(AuthenticatedSubject var1) throws RAException {
      List var2 = this.raInfo.getAdminObjs();
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            this.createAdminObject((AdminObjInfo)var3.next(), var1);
         }
      }

   }

   private void verifyAdminJNDIAndSaveObj(Object var1, AdminObjInfo var2, String var3, boolean var4) throws RAException {
      String var5 = var2.getKey();
      boolean var6;
      String var7;
      if (var4) {
         Debug.println(this, ".verifyAdminJNDIAndSaveObj() " + this.adapterLayer.toString(var1, kernelId) + " in App-Scoped JNDI Tree with resourceLink '" + var5 + "'");
         var6 = JNDIHandler.verifyResourceLink(var5, this.getAdminObjectContext()) || this.adminObjects != null && this.adminObjects.containsKey(var5);
         if (var6) {
            var7 = Debug.getExceptionCFResourceLinkDuplicate(var5);
            throw new RAException(var7);
         }
      } else {
         Debug.println(this, ".verifyAdminJNDIAndSaveObj() " + this.adapterLayer.toString(var1, kernelId) + " JNDI name '" + var5 + "' and versionId = " + var3);
         var6 = JNDIHandler.isJndiNameBound(var5) || this.adminObjects != null && this.adminObjects.containsKey(var5);
         if (var6) {
            var7 = Debug.getExceptionJndiNameAlreadyBound(var5);
            throw new RAException(var7);
         }
      }

      if (this.adminObjects == null) {
         this.adminObjects = new Hashtable();
      }

      this.adminObjects.put(var5, new AdminObjVersionId(var1, var3, var4, var2));
   }

   private void bindAdminObjects() throws RACommonException {
      if (this.adminObjects != null && this.adminObjects.size() > 0) {
         Iterator var1 = this.adminObjects.keySet().iterator();

         while(var1.hasNext()) {
            String var2 = (String)var1.next();
            this.bindAdminObject(var2);
         }
      }

   }

   private void suspendResourceAdapter(int var1, Properties var2) throws ResourceException {
      AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (this.resourceAdapter != null && this.resourceAdapter instanceof Suspendable && this.adapterLayer.supportsSuspend((Suspendable)this.resourceAdapter, var1, var3)) {
         Debug.raLifecycle("Calling suspend() on the ResourceAdapter JavaBean: " + this.adapterLayer.toString(this.resourceAdapter, var3));
         this.adapterLayer.suspend((Suspendable)this.resourceAdapter, var1, var2, var3);
      } else if (this.resourceAdapter != null) {
         Debug.raLifecycle("Skipping suspend() call ResourceAdapter JavaBean " + this.adapterLayer.toString(this.resourceAdapter, var3) + " which does not support type '" + var1 + "' suspend()");
      }

   }

   private void resumeResourceAdapter(int var1, Properties var2) throws ResourceException {
      AuthenticatedSubject var3 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (this.resourceAdapter != null && this.resourceAdapter instanceof Suspendable && this.adapterLayer.supportsSuspend((Suspendable)this.resourceAdapter, var1, var3)) {
         Debug.raLifecycle("Calling resume() on the ResourceAdapter JavaBean: " + this.resourceAdapter);
         ((Suspendable)this.resourceAdapter).resume(var1, var2);
      } else if (this.resourceAdapter != null) {
         Debug.raLifecycle("Skipping resume() call ResourceAdapter JavaBean " + this.adapterLayer.toString(this.resourceAdapter, var3) + " which does not support type " + var1 + " suspend()/resume()");
      }

   }

   private void putRAintoJNDITree(String var1) throws RAException {
      String var2 = this.getVersionId();
      Debug.deployment("Module '" + this.moduleName + "' binding RA with JNDI name '" + var1 + "' and versionId = " + var2);
      if (var1 != null && !var1.equals("")) {
         JNDIHandler.bindRA(var1, this.resourceAdapter, var2);
      } else if (this.raInboundManager.isInboundRA()) {
         Debug.logNoAdapterJNDInameSetForInboundRA(this.moduleName, this.appCtx.getApplicationName());
      }

   }

   private void removeRAfromJNDITree(RAException var1) {
      Debug.println(this, ".removeRAfromJNDITree() Remove RA from JNDI tree");

      try {
         String var2 = this.getVersionId();
         Debug.deployment("Module '" + this.moduleName + "' unbinding RA with JNDI name '" + this.jndiName + "' and versionId = " + var2);
         JNDIHandler.unbindRA(this.getJndiName(), this.resourceAdapter, var2);
      } catch (Throwable var3) {
         var1.addError(var3);
      }

   }

   private boolean matches(int var1, int var2) {
      return (var1 & var2) > 0;
   }

   private void removeAdminObjsFromJndi(RAException var1) {
      Debug.println(this, ".removeAdminObjsFromJndi() Remove administered objects from JNDI tree");
      if (this.raInfo == null) {
         Debug.throwAssertionError("RAInfo is null");
      }

      if (this.adminObjects != null) {
         Iterator var2 = this.adminObjects.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var3 = (Map.Entry)var2.next();

            try {
               JNDIHandler.unbindAdminObj(((AdminObjVersionId)var3.getValue()).adminInfo, this);
            } catch (Exception var5) {
               var1.add(var1);
            }
         }
      }

   }

   public void removeAdminObject(AdminObjInfo var1) throws RAException {
      try {
         this.adminObjects.remove(var1.getKey());
         JNDIHandler.unbindAdminObj(var1, this);
      } catch (UndeploymentException var3) {
         throw new RAException(var3);
      }
   }

   public void setLateDeploy(boolean var1) {
      this.lateDeploy = var1;
   }

   public RAOutboundManager getRAOutboundManager() {
      return this.raOutboundManager;
   }

   public RAInboundManager getRAInboundManager() {
      return this.raInboundManager;
   }

   public ResourceAdapter getResourceAdapter() {
      return this.resourceAdapter;
   }

   public RAInfo getRAInfo() {
      return this.raInfo;
   }

   public ClassLoader getClassloader() {
      return this.classloader;
   }

   public ConnectorComponentMBean getConnectorComponentMBean() {
      DomainMBean var1 = ManagementService.getRuntimeAccess(kernelId).getDomain();
      ApplicationMBean var2 = var1.lookupApplication(this.applicationName);
      return var2 == null ? null : var2.lookupConnectorComponent(this.connectorComponentName);
   }

   public VirtualJarFile getJarFile() {
      return this.vjar;
   }

   public int getAvailableConnectionPoolsCount() {
      return this.raOutboundManager != null ? this.raOutboundManager.getAvailableConnetionPoolsCount() : 0;
   }

   public ConnectorComponentRuntimeMBean getConnectorComponentRuntimeMBean() {
      return this.connectorComponentRuntimeMBean;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getJndiName() {
      return this.jndiName;
   }

   public boolean isActivated() {
      return this.state.equals(this.ACTIVATED);
   }

   AdapterType getXMLBean(ConnectorDiagnosticImageSource var1) {
      AdapterType var2 = AdapterType.Factory.newInstance();
      var2.setJndiName(this.getRAInfo().getJndiName());
      boolean var3 = var1 != null ? var1.timedout() : false;
      if (var3) {
         return var2;
      } else {
         OutboundAdapterType[] var4 = this.getRAOutboundManager().getXMLBeans(var1);
         var2.setOutboundAdapterArray(var4);
         InboundAdapterType[] var5 = this.getRAInboundManager().getXMLBeans(var1);
         var2.setInboundAdapterArray(var5);
         WorkManagerType var6 = WorkManagerType.Factory.newInstance();
         var6.setWorkManagerName(this.jndiName);
         var2.setWorkManager(var6);
         return var2;
      }
   }

   public String getState() {
      return this.state;
   }

   public int getSuspendedState() {
      return this.suspendState.getSuspendState();
   }

   public String getVersionId() {
      return ApplicationVersionUtils.getVersionId(this.appCtx.getApplicationId());
   }

   public String getActiveVersion() {
      String var1 = null;
      String var2 = null;
      boolean var3 = false;
      var1 = ApplicationVersionUtils.getVersionId(this.appCtx.getApplicationId());
      var3 = var1 != null && var1.length() > 0;
      if (var3) {
         var2 = ApplicationVersionUtils.getActiveVersionId(this.appCtx.getApplicationName());
      }

      return var2;
   }

   public boolean isActiveVersion() {
      String var1 = null;
      boolean var2 = true;
      String var3 = null;
      boolean var4 = false;
      var1 = ApplicationVersionUtils.getVersionId(this.appCtx.getApplicationId());
      var4 = var1 != null && var1.length() > 0;
      if (var4) {
         var3 = ApplicationVersionUtils.getActiveVersionId(this.appCtx.getApplicationName());
         if (var1.equals(var3)) {
            var2 = true;
         }
      }

      return var2;
   }

   public boolean isVersioned() {
      String var1 = null;
      boolean var2 = false;
      var1 = ApplicationVersionUtils.getVersionId(this.appCtx.getApplicationId());
      var2 = var1 != null && var1.length() > 0;
      return var2;
   }

   public ApplicationContextInternal getAppContext() {
      return this.appCtx;
   }

   Context getAdminObjectContext() {
      return this.adminObjectCtx;
   }

   public Context getConnectionFactoryContext() {
      return this.connectionFactoryCtx;
   }

   public BootstrapContext getBootstrapContext() {
      return this.bootstrapContext;
   }

   public AdapterLayer getAdapterLayer() {
      return this.adapterLayer;
   }

   public List getClassFinders() {
      return this.classFinders;
   }

   public void closeClassFinders() {
      Debug.println(this, ".closeClassFinders()");
      Iterator var1 = this.classFinders.iterator();

      while(var1.hasNext()) {
         ClassFinder var2 = (ClassFinder)var1.next();
         var2.close();
      }

   }

   public void setRAInfo(AuthenticatedSubject var1, RAInfo var2) {
      if (!SecurityServiceManager.isKernelIdentity(var1)) {
         throw new SecurityException("KernelId is required to call RAInstanceManager.setRAInfo, Subject '" + (var1 == null ? "<null>" : var1.toString()) + "' is not the kernel identity");
      } else {
         this.raInfo = var2;
      }
   }

   public void createAdminObject(AdminObjInfo var1, AuthenticatedSubject var2) throws RAException {
      String var3 = var1.getAdminObjClass();
      String var4 = var1.getInterface();
      Hashtable var6 = var1.getConfigProps();

      try {
         Class var7 = this.adapterLayer.forName(var3, true, this.classloader, var2);
         Object var8 = this.adapterLayer.newInstance(var7, var2);
         if (var8 instanceof ResourceAdapterAssociation && this.resourceAdapter != null) {
            this.adapterLayer.setResourceAdapter((ResourceAdapterAssociation)var8, this.resourceAdapter, var2);
         }

         if (var6 != null && var6.size() > 0) {
            Utils.setProperties(this, var8, var6.values(), this.raValidationInfo.getAdminPropSetterTable(var4));
         }

         String var5 = var1.getJndiName();
         boolean var9 = var5 == null || var5.length() == 0;
         this.verifyAdminJNDIAndSaveObj(var8, var1, this.getVersionId(), var9);
      } catch (RACommonException var10) {
         throw new RAException(var10.toString(), var10);
      } catch (ClassNotFoundException var11) {
         throw new RAException(var11.toString(), var11);
      } catch (InstantiationException var12) {
         throw new RAException(var12.toString(), var12);
      } catch (IllegalAccessException var13) {
         throw new RAException(var13.toString(), var13);
      } catch (Throwable var14) {
         throw new RAException(var14.toString(), var14);
      }
   }

   public void bindAdminObject(String var1) throws RACommonException {
      AdminObjVersionId var2 = (AdminObjVersionId)this.adminObjects.get(var1);
      if (var2.appScoped) {
         JNDIHandler.bindAppScopedAdminObj(var2.adminObj, var1, this.appCtx, this.adminObjectCtx, this.moduleName);
      } else {
         JNDIHandler.bindAdminObj(var2.adminObj, var1, var2.versionId, this);
      }

   }

   public String getComponentName() {
      return this.connectorComponentName;
   }

   public String getApplicationName() {
      return this.applicationName;
   }

   public void cleanupRuntime() throws ManagementException {
      if (this.connectorComponentRuntimeMBean != null) {
         this.connectorComponentRuntimeMBean.unregister();
         ConnectorService.getConnectorServiceRuntimeMBean().removeConnectorRuntime(this.connectorComponentRuntimeMBean);
      }

   }

   public ComponentRuntimeMBean getRuntime() {
      return this.connectorComponentRuntimeMBean;
   }

   public RAValidationInfo getRAValidationInfo() {
      return this.raValidationInfo;
   }

   private String getRuntimeName() {
      String var1 = ApplicationVersionUtils.replaceDelimiter(this.appCtx.getApplicationId(), '_');
      String var2;
      if (!var1.endsWith(".rar") && !var1.equals(this.connectorComponentName)) {
         var2 = var1 + "_" + this.connectorComponentName;
      } else {
         var2 = this.connectorComponentName;
      }

      if (var2.endsWith(".rar")) {
         var2 = var2.substring(0, var2.length() - 4);
      }

      return var2;
   }

   private void setupWorkManagerRuntime(WorkManagerService var1) throws RAException {
      if (this.workManagerRuntime == null && var1 != null) {
         try {
            this.workManagerRuntime = WorkManagerRuntimeMBeanImpl.getWorkManagerRuntime(var1.getDelegate(), (ApplicationRuntimeMBean)this.connectorComponentRuntimeMBean.getParent(), this.connectorComponentRuntimeMBean);
         } catch (ManagementException var3) {
            throw new RAException(var3);
         }
      }

      if (this.workManagerRuntime != null) {
         this.connectorComponentRuntimeMBean.addWorkManagerRuntime(this.workManagerRuntime);
      }

   }

   public ConnectorComponentRuntimeMBeanImpl setupComponentRuntime() throws RAException {
      ServiceRuntimeMBeanImpl var1 = ConnectorService.getConnectorServiceRuntimeMBean();

      try {
         this.connectorComponentRuntimeMBean = new ConnectorComponentRuntimeMBeanImpl(this.getRuntimeName(), this.componentURI, this, this.appCtx.getRuntime(), var1);
      } catch (ManagementException var3) {
         throw new RAException(var3);
      }

      var1.addConnectorRuntime(this.connectorComponentRuntimeMBean);
      return this.connectorComponentRuntimeMBean;
   }

   public void rebindRA(String var1) throws RAException {
      RAException var2 = new RAException();

      try {
         this.removeRAfromJNDITree(var2);
         this.putRAintoJNDITree(var1);
         this.jndiName = var1;
         if (Debug.isDeploymentEnabled()) {
            Debug.deployment("Module '" + this.moduleName + "' rebinding RA with new JNDI name '" + var1 + "' and versionId = " + this.getVersionId());
         }

      } catch (RAException var4) {
         if (var2.getErrors().hasNext()) {
            var2.add(var4);
            throw var2;
         } else {
            throw var4;
         }
      }
   }

   public boolean isWaitingStartVersioningComplete() {
      return this.waitingStartVersioningComplete;
   }

   public void clearWaitingStartVersioningComplete() {
      this.waitingStartVersioningComplete = false;
   }

   private class SuspendState {
      private int suspendState = 0;

      SuspendState() {
      }

      void suspendInbound() {
         this.suspendState |= 1;
      }

      void resumeInbound() {
         this.suspendState &= -2;
      }

      void suspendOutbound() {
         this.suspendState |= 2;
      }

      void resumeOutbound() {
         this.suspendState &= -3;
      }

      void suspendWork() {
         this.suspendState |= 4;
      }

      void resumeWork() {
         this.suspendState &= -5;
      }

      void suspendAll() {
         this.suspendState = 7;
      }

      void resumeAll() {
         this.suspendState = 0;
      }

      boolean isAllSuspended() {
         return this.suspendState == 7;
      }

      boolean isAllResumed() {
         return this.suspendState == 0;
      }

      boolean isInboundSuspend() {
         return this.suspendState == 1;
      }

      boolean isOutboundSuspended() {
         return this.suspendState == 2;
      }

      boolean isWorkSuspended() {
         return this.suspendState == 4;
      }

      int getSuspendState() {
         return this.suspendState;
      }
   }

   private class AdminObjVersionId {
      Object adminObj;
      String versionId;
      boolean appScoped;
      AdminObjInfo adminInfo;

      private AdminObjVersionId(Object var2, String var3, boolean var4, AdminObjInfo var5) {
         this.adminObj = var2;
         this.versionId = var3;
         this.appScoped = var4;
         this.adminInfo = var5;
      }

      // $FF: synthetic method
      AdminObjVersionId(Object var2, String var3, boolean var4, AdminObjInfo var5, Object var6) {
         this(var2, var3, var4, var5);
      }
   }
}
