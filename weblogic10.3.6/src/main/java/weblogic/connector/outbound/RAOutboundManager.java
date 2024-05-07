package weblogic.connector.outbound;

import com.bea.connector.diagnostic.OutboundAdapterType;
import java.security.AccessController;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.naming.NoPermissionException;
import javax.resource.ResourceException;
import javax.resource.spi.ApplicationServerInternalException;
import javax.resource.spi.ManagedConnectionFactory;
import javax.resource.spi.ResourceAdapterAssociation;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.connector.ConnectorLogger;
import weblogic.connector.common.ConnectorDiagnosticImageSource;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.common.Utils;
import weblogic.connector.deploy.JNDIHandler;
import weblogic.connector.deploy.RAOutboundDeployer;
import weblogic.connector.exception.RAException;
import weblogic.connector.exception.RAInternalException;
import weblogic.connector.exception.RAOutboundException;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.external.impl.OutboundInfoImpl;
import weblogic.connector.monitoring.ConnectorComponentRuntimeMBeanImpl;
import weblogic.connector.monitoring.outbound.ConnectionPoolRuntimeMBeanImpl;
import weblogic.management.DeploymentException;
import weblogic.management.UndeploymentException;
import weblogic.management.runtime.ConnectorConnectionPoolRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.PlatformConstants;

public class RAOutboundManager {
   private boolean isOutsideAppAccessEnabled = false;
   private final byte SUSPEND = 1;
   private final byte RESUME = 2;
   private final byte SHUTDOWN = 3;
   private final byte FORCE_SUSPEND = 4;
   private Hashtable mcfMap = new Hashtable();
   private Hashtable outboundInfoMap = new Hashtable();
   private HashMap<String, OutboundInfo> pendingOutboundInfoMap = new HashMap();
   private Hashtable poolMap = new Hashtable();
   private Hashtable jndiPoolLookupMap = new Hashtable();
   private RAInstanceManager raInstanceMgr;
   private String moduleName;

   public RAOutboundManager(RAInstanceManager var1) throws RAOutboundException {
      Debug.enter(this, "Constructor");

      try {
         this.raInstanceMgr = var1;
         Debug.println(this, "Initializing the outbound manager");
         this.isOutsideAppAccessEnabled = this.raInstanceMgr.getRAInfo().isEnableAccessOutsideApp();
         this.initialize();
      } finally {
         Debug.exit(this, "Constructor");
      }

   }

   public void prepare() throws RAOutboundException {
      Debug.enter(this, "prepare()");

      try {
         Iterator var1 = this.mcfMap.keySet().iterator();
         String var2 = null;
         this.debugModule("Looping through all the outbound connections to prepare the pools");

         while(var1.hasNext()) {
            var2 = (String)var1.next();
            this.preparePool(var2);
         }
      } finally {
         Debug.exit(this, "prepare()");
      }

   }

   public void activate() throws RAOutboundException {
      Debug.enter(this, "activate()");

      try {
         Iterator var1 = this.mcfMap.keySet().iterator();
         String var2 = null;
         this.debugModule("Looping through all the outbound connections to resume the pools and do a JNDI bind");

         while(var1.hasNext()) {
            var2 = (String)var1.next();
            this.activatePool(var2);
         }
      } finally {
         this.debugModule("Done Looping through all the outbound connections to resume the pools and do a JNDI bind");
         Debug.exit(this, "activate()");
      }

   }

   public void deactivate() throws RAOutboundException {
      Debug.enter(this, "deactivate()");

      try {
         RAOutboundException var1 = null;
         Iterator var2 = this.poolMap.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();

            try {
               this.deactivatePool(var3);
            } catch (RAOutboundException var9) {
               if (var1 == null) {
                  var1 = new RAOutboundException();
               }

               var1.add(var9);
            }
         }

         if (var1 != null) {
            throw var1;
         }
      } finally {
         Debug.exit(this, "deactivate()");
      }

   }

   public void deactivatePool(String var1) throws RAOutboundException {
      OutboundInfo var2 = (OutboundInfo)this.outboundInfoMap.get(var1);
      ConnectionPool var3 = (ConnectionPool)this.poolMap.get(var1);

      String var5;
      try {
         Debug.println("JNDI unbind : " + var1);
         JNDIHandler.unbindConnectionFactory(var2, this, var3.getConnectionFactory());
      } catch (ResourceException var7) {
         var5 = Debug.getExceptionDeactivateException(var1, var7.getMessage(), var7.toString()) + PlatformConstants.EOL;
         throw new RAOutboundException(var5, var7);
      } catch (UndeploymentException var8) {
         var5 = Debug.getExceptionDeactivateException(var1, var8.getMessage(), var8.toString()) + PlatformConstants.EOL;
         throw new RAOutboundException(var5, var8);
      }

      try {
         Debug.println("Suspend the pool");
         var3.suspend();
      } catch (weblogic.common.ResourceException var6) {
         var5 = Debug.getExceptionDeactivateException(var1, var6.getMessage(), var6.toString()) + PlatformConstants.EOL;
         throw new RAOutboundException(var5, var6);
      }
   }

   public void rollback() throws RAOutboundException {
      Debug.enter(this, "rollback()");

      try {
         Iterator var1 = this.poolMap.keySet().iterator();
         RAOutboundException var3 = null;

         while(var1.hasNext()) {
            String var2 = (String)var1.next();

            try {
               this.internalShutdownPool(var2);
            } catch (RAOutboundException var9) {
               if (var3 == null) {
                  var3 = new RAOutboundException();
               }

               var3.add(var9);
            }
         }

         Debug.println("Reset the hashtables");
         this.reset();
         if (var3 != null) {
            throw var3;
         }
      } finally {
         Debug.exit(this, "rollback()");
      }

   }

   public void shutdownPool(String var1) throws RAOutboundException {
      this.internalShutdownPool(var1);
      this.outboundInfoMap.remove(var1);
      this.poolMap.remove(var1);
   }

   private void internalShutdownPool(String var1) throws RAOutboundException {
      OutboundInfo var2 = (OutboundInfo)this.outboundInfoMap.get(var1);
      ConnectionPool var3 = (ConnectionPool)this.poolMap.get(var1);
      if (var2 != null && var3 != null) {
         try {
            Debug.println("Shutdown the pool");
            var3.shutdown();
         } catch (weblogic.common.ResourceException var6) {
            String var5 = Debug.getExceptionShutdownException(var2.getKey(), var6.getMessage(), var6.toString());
            throw new RAOutboundException(var5, var6);
         }
      }
   }

   public void stop() throws RAOutboundException {
      this.suspend();
   }

   public void halt() throws RAOutboundException {
      this.changeStateOfPools((byte)3);
   }

   public void suspend() throws RAOutboundException {
      this.changeStateOfPools((byte)1);
   }

   public void resume() throws RAOutboundException {
      this.changeStateOfPools((byte)2);
   }

   private void initialize() throws RAOutboundException {
      Debug.enter(this, "initialize()");

      try {
         Debug.println("Get OutboundInfos list");
         List var1 = this.raInstanceMgr.getRAInfo().getOutboundInfos();
         Iterator var2 = var1.iterator();
         this.getModuleName();
         Debug.println("OutboundInfos List size : " + var1.size());

         while(var2.hasNext()) {
            OutboundInfo var3 = (OutboundInfo)var2.next();
            this.createConnectionFactory(var3);
         }
      } finally {
         Debug.exit(this, "initialize()");
      }

   }

   public void createConnectionFactory(OutboundInfo var1) throws RAOutboundException {
      String var3 = var1.getJndiName();
      String var4 = var1.getResourceLink();
      boolean var5 = var3 == null || var3.length() == 0;

      String var6;
      String var7;
      try {
         if (!var5 && JNDIHandler.verifyJNDIName(var3)) {
            Debug.logJNDINameAlreadyExists(var3);
            var6 = Debug.getExceptionCFJndiNameDuplicate(var3);
            throw new RAOutboundException(var6);
         }

         if (var5 && JNDIHandler.verifyResourceLink(var4, this.raInstanceMgr.getConnectionFactoryContext())) {
            Debug.logJNDINameAlreadyExists(var4);
            var6 = Debug.getExceptionCFResourceLinkDuplicate(var4);
            throw new RAOutboundException(var6);
         }
      } catch (RAException var8) {
         var7 = Debug.getExceptionJndiVerifyFailed(var1.getKey(), var8.toString());
         throw new RAOutboundException(var7, var8);
      }

      Debug.println(this, ".createConnectionFactory() Initialize MCF");
      ManagedConnectionFactory var2 = this.initializeMCF(var1);
      var6 = ((OutboundInfoImpl)var1).getKey();
      var7 = JNDIHandler.getJndiNameAndVersion(var6, this.raInstanceMgr.getVersionId());
      Debug.println(this, ".createConnectionFactory() Add to the mcfMap : " + var7);
      this.mcfMap.put(var7, var2);
      Debug.println(this, ".createConnectionFactory() Add to the outboundInfoMap : " + var7);
      this.outboundInfoMap.put(var7, var1);
   }

   public OutboundInfo updateOutBoundInfo(String var1, OutboundInfo var2) {
      String var3 = JNDIHandler.getJndiNameAndVersion(var1, this.raInstanceMgr.getVersionId());
      return (OutboundInfo)this.pendingOutboundInfoMap.put(var3, var2);
   }

   private ManagedConnectionFactory initializeMCF(OutboundInfo var1) throws RAOutboundException {
      Debug.enter(this, "initializeMCF(...)");
      Class var2 = null;
      String var4 = var1.getMCFClass();
      AuthenticatedSubject var5 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

      ManagedConnectionFactory var6;
      try {
         String var7;
         try {
            Debug.println("Instantiate the MCF class : " + var4);
            var2 = Class.forName(var4, true, this.raInstanceMgr.getClassloader());
            ManagedConnectionFactory var3 = (ManagedConnectionFactory)this.raInstanceMgr.getAdapterLayer().newInstance(var2, var5);
            if (this.raInstanceMgr.getResourceAdapter() != null && var3 instanceof ResourceAdapterAssociation) {
               try {
                  Debug.println("Set the resource adapter bean in the mcf");
                  this.raInstanceMgr.getAdapterLayer().setResourceAdapter((ResourceAdapterAssociation)var3, this.raInstanceMgr.getResourceAdapter(), var5);
               } catch (Throwable var16) {
                  var7 = Debug.getExceptionSetRAClassFailed(var4, var16.toString());
                  throw new RAOutboundException(var7, var16);
               }
            }

            Debug.println("Configure the MCF class");
            Utils.setProperties(this.raInstanceMgr, var3, var1.getMCFProps().values(), this.raInstanceMgr.getRAValidationInfo().getConnectionFactoryPropSetterTable(var1.getCFInterface()));
            var6 = var3;
         } catch (RAInternalException var17) {
            var7 = Debug.getExceptionMCFUnexpectedException(var2.getName(), var17.toString());
            throw new RAOutboundException(var7, var17);
         } catch (ClassNotFoundException var18) {
            var7 = Debug.getExceptionMCFClassNotFound(var1.getMCFClass(), var18.toString());
            throw new RAOutboundException(var7, var18);
         } catch (InstantiationException var19) {
            var7 = Debug.getExceptionInstantiateMCFFailed(var1.getMCFClass(), var19.toString());
            throw new RAOutboundException(var7, var19);
         } catch (IllegalAccessException var20) {
            var7 = Debug.getExceptionAccessMCFFailed(var1.getMCFClass(), var20.toString());
            throw new RAOutboundException(var7, var20);
         }
      } finally {
         Debug.exit(this, "initializeMCF(...)");
      }

      return var6;
   }

   private boolean fromSameApp(ConnectionPool var1) {
      boolean var2 = true;
      boolean var5 = this.raInstanceMgr.getAppContext().getRuntime().isEAR();
      String var4 = ApplicationAccess.getApplicationAccess().getCurrentApplicationName();
      if (var1 != null && var5) {
         String var3 = this.raInstanceMgr.getApplicationName();
         if (var3 != null & var3.length() != 0 && !var3.equalsIgnoreCase(var4)) {
            var2 = false;
            if (Debug.isConnectionsEnabled()) {
               Debug.connections("For pool '" + var1.getName() + "' a connection " + "request was made from outside the application and is being rejected.");
               Debug.connections("Requesting app = " + var3 + " and current app = " + var4);
            }
         }
      }

      return var2;
   }

   private void changeStateOfPools(byte var1) throws RAOutboundException {
      Debug.enter(this, "changeStateOfPools( " + var1 + " )");

      try {
         Iterator var2 = this.poolMap.values().iterator();
         RAOutboundException var4 = null;

         while(var2.hasNext()) {
            ConnectionPool var3 = (ConnectionPool)var2.next();

            try {
               switch (var1) {
                  case 1:
                     var3.suspend();
                     break;
                  case 2:
                     var3.resume();
                     break;
                  case 3:
                     var3.shutdown();
                     break;
                  case 4:
                     var3.forceSuspend(false);
               }
            } catch (weblogic.common.ResourceException var10) {
               var4 = (RAOutboundException)Utils.consolidateException(new RAOutboundException(), var10);
            }
         }

         if (var4 != null) {
            throw var4;
         }
      } finally {
         Debug.exit(this, "changeStateOfPools( " + var1 + " )");
      }

   }

   private void reset() {
      this.mcfMap.clear();
      this.outboundInfoMap.clear();
      this.poolMap.clear();
   }

   public void setRA(RAInstanceManager var1) {
      this.raInstanceMgr = var1;
   }

   public Object getConnectionFactory(String var1) throws ApplicationServerInternalException, NoPermissionException, ResourceException {
      Debug.enter(this, "getConnectionFactory(...)");

      Object var11;
      try {
         Object var2 = null;

         try {
            ConnectionPool var3 = (ConnectionPool)this.jndiPoolLookupMap.get(var1);
            String var4;
            if (var3 == null) {
               Debug.println("Failed to get the pool for key : " + var1 + " : " + var3);
               var4 = Debug.getExceptionGetConnectionFactoryFailedInternalError(var1);
               throw new ApplicationServerInternalException(var4);
            }

            Debug.println("Got the pool for key : " + var1 + " : " + var3);
            Debug.println("Check if access is allowed");
            if (!this.isOutsideAppAccessEnabled && !this.fromSameApp(var3)) {
               var4 = Debug.getExceptionFailedAccessOutsideApp();
               throw new NoPermissionException(var4);
            }

            Debug.println("Get the ConnectionFactory from the connection pool");
            var2 = var3.getConnectionFactory();
         } catch (ResourceException var9) {
            Debug.logCreateCFforMCFError(var1, var9);
            throw var9;
         }

         var11 = var2;
      } finally {
         Debug.exit(this, "getConnectionFactory(...)");
      }

      return var11;
   }

   public ConnectionPool getConnectionPool(String var1) {
      ConnectionPool var2 = null;
      if (var1 != null) {
         String var3 = JNDIHandler.getJndiNameAndVersion(var1, this.raInstanceMgr.getVersionId());
         var2 = (ConnectionPool)this.poolMap.get(var3);
      }

      return var2;
   }

   public RAInstanceManager getRA() {
      return this.raInstanceMgr;
   }

   public Vector getMCFKeyNames() {
      Vector var1 = new Vector();
      Iterator var2 = this.poolMap.keySet().iterator();
      if (this.poolMap == null) {
         return var1;
      } else {
         while(var2.hasNext()) {
            var1.addElement(var2.next());
         }

         return var1;
      }
   }

   public int getAvailableConnetionPoolsCount() {
      return this.raInstanceMgr.getRAInfo().getOutboundInfos().size();
   }

   public List getConnectionPoolsRuntime() {
      Iterator var1 = this.poolMap.values().iterator();
      ConnectionPool var2 = null;
      Vector var3 = new Vector(10);

      while(var1.hasNext()) {
         var2 = (ConnectionPool)var1.next();
         var3.add(var2.getRuntimeMBean());
      }

      return var3;
   }

   public ConnectorConnectionPoolRuntimeMBean getConnectionPoolRuntime(String var1) {
      String var2 = JNDIHandler.getJndiNameAndVersion(var1, this.raInstanceMgr.getVersionId());
      ConnectionPoolRuntimeMBeanImpl var3 = null;
      ConnectionPool var4 = (ConnectionPool)this.poolMap.get(var2);
      if (var4 != null) {
         var3 = var4.getRuntimeMBean();
      }

      return var3;
   }

   public OutboundAdapterType[] getXMLBeans(ConnectorDiagnosticImageSource var1) {
      boolean var2 = var1 != null ? var1.timedout() : false;
      if (var2) {
         return new OutboundAdapterType[0];
      } else {
         OutboundAdapterType[] var3 = new OutboundAdapterType[this.poolMap.size()];
         Iterator var4 = this.poolMap.values().iterator();

         for(int var5 = 0; var4.hasNext(); ++var5) {
            ConnectionPool var6 = (ConnectionPool)var4.next();
            var3[var5] = var6.getXMLBean(var1);
         }

         return var3;
      }
   }

   public String getModuleName() {
      if (this.moduleName != null) {
         return this.moduleName;
      } else {
         if (this.raInstanceMgr != null) {
            this.moduleName = this.raInstanceMgr.getModuleName();
         }

         return this.moduleName;
      }
   }

   private void debugModule(String var1) {
      if (Debug.isDeploymentEnabled()) {
         Debug.deployment("Module '" + this.getModuleName() + "' " + var1);
      }

   }

   public void preparePool(String var1) throws RAOutboundException {
      ManagedConnectionFactory var2 = (ManagedConnectionFactory)this.mcfMap.get(var1);
      if (var2 == null) {
         String var9 = Debug.getExceptionOutboundPrepareFailed(var1, "ManagedConnectionFactory was not found");
         throw new RAOutboundException(var9);
      } else {
         OutboundInfo var3 = (OutboundInfo)this.outboundInfoMap.get(var1);
         if (var3 == null) {
            String var10 = Debug.getExceptionOutboundPrepareFailed(var1, "Outbound Pool was not found");
            throw new RAOutboundException(var10);
         } else {
            String var5;
            try {
               this.debugModule("Preparing the pool with KEY id:  '" + var1 + "'");
               ConnectionPool var4 = RAOutboundDeployer.prepare(var2, var3, this.raInstanceMgr.getApplicationName(), this.raInstanceMgr.getComponentName(), this);
               var4.setupRuntime((ConnectorComponentRuntimeMBeanImpl)this.raInstanceMgr.getConnectorComponentRuntimeMBean(), this);
               Debug.println("Add to the poolMap : key = " + var1 + " : " + var4);
               this.poolMap.put(var1, var4);
               var5 = var3.getJndiName();
               boolean var6 = var5 == null || var5.length() == 0;
               if (!var6) {
                  var5 = var1;
                  if (JNDIHandler.isJndiNameBound(var1)) {
                     String var7 = Debug.getExceptionJndiNameAlreadyBound(var1);
                     throw new RAOutboundException(var7);
                  }
               } else {
                  var5 = this.raInstanceMgr.getAppContext().getApplicationId() + "/" + var3.getResourceLink();
               }

               Debug.println(this, "Add to the jndiPoolLookupMap : " + var5 + " : " + var4);
               this.jndiPoolLookupMap.put(var5, var4);
            } catch (DeploymentException var8) {
               var5 = Debug.getExceptionOutboundPrepareFailed(var1, var8.toString());
               throw new RAOutboundException(var5, var8);
            }
         }
      }
   }

   public void activatePool(String var1) throws RAOutboundException {
      OutboundInfo var2 = (OutboundInfo)this.outboundInfoMap.get(var1);
      ConnectionPool var3 = (ConnectionPool)this.poolMap.get(var1);

      try {
         String var5;
         try {
            Debug.println("Set the logger");
            var3.setLogger();
            Debug.pooling("Updating the initial capacity of connection pool for module '" + this.moduleName + "' with key:  '" + var1 + "'");
            RAOutboundDeployer.updateInitialCapacity(var3, var2);
            var3.setupForXARecovery();
            this.debugModule("Resuming the pool with name '" + var3.getName() + "'");
            var3.resume();
            boolean var4 = var2.getJndiName() == null || var2.getJndiName().length() == 0;
            if (!var4) {
               this.debugModule("Binding the pool into JNDI with JNDI name '" + var1 + "'");
               JNDIHandler.bindConnectionFactory(var2, this, var3.getConnectionFactory());
            } else {
               var5 = var2.getResourceLink();
               ApplicationContextInternal var6 = this.raInstanceMgr.getAppContext();
               this.debugModule("Binding the pool into App-Scoped JNDI with ResourceLink '" + var5 + "'");
               JNDIHandler.bindAppScopedConnectionFactory(var2, this, var6, this.raInstanceMgr.getConnectionFactoryContext(), this.moduleName);
            }
         } catch (weblogic.common.ResourceException var13) {
            this.debugModule("Failed to activate the pool with key = '" + var1 + "'");
            var5 = Debug.getExceptionResumePoolFailed(var13.toString());
            throw new RAOutboundException(var5, var13);
         } catch (ResourceException var14) {
            var5 = Debug.getFailedToGetCF(var1, var14.toString());
            this.debugModule(var5);
            throw new RAOutboundException(var5);
         } catch (DeploymentException var15) {
            this.debugModule("Failed to bind the pool into JNDI, key = '" + var1 + "'");
            var5 = Debug.getExceptionJndiBindFailed(var1, var15.toString());
            throw new RAOutboundException(var5, var15);
         }
      } finally {
         this.debugModule("Done resuming/binding the pool with key = '" + var1 + "'");
      }

   }

   public boolean resetPool(String var1) throws RAOutboundException {
      return this.internalResetPool(var1, false);
   }

   public void forceResetPool(String var1) throws RAOutboundException {
      this.internalResetPool(var1, true);
   }

   private boolean internalResetPool(String var1, boolean var2) throws RAOutboundException {
      String var3 = JNDIHandler.getJndiNameAndVersion(var1, this.raInstanceMgr.getVersionId());
      ConnectionPool var4 = (ConnectionPool)this.poolMap.get(var3);
      OutboundInfo var5 = (OutboundInfo)this.pendingOutboundInfoMap.get(var3);
      if (var5 == null) {
         var5 = (OutboundInfo)this.outboundInfoMap.get(var3);
      }

      try {
         if (var2) {
            var4.forceSuspend(true);
            Debug.println("ResetConnectionPool# forceSuspend Connection pool : key = " + var1 + " : " + var4);
         } else {
            synchronized(var4) {
               if (var4.getNumReserved() > 0) {
                  if (Debug.isPoolingEnabled()) {
                     Debug.pooling(ConnectorLogger.logCannotResetConnectionPoolInuse(var1));
                  }

                  return false;
               }

               var4.suspend(true);
               Debug.println("ResetConnectionPool# suspend Connection pool : key = " + var1 + " : " + var4);
            }
         }

         var4.shutdown();
         Debug.println("ResetConnectionPool# shutdown Connection pool : key = " + var1 + " : " + var4);
         JNDIHandler.unbindConnectionFactory(var5, this, var4.getConnectionFactory());
         this.createConnectionFactory(var5);
         this.pendingOutboundInfoMap.remove(var3);
         this.preparePool(var3);
         this.activatePool(var3);
         if (Debug.getVerbose()) {
            var4 = (ConnectionPool)this.poolMap.get(var3);
            Debug.println("ResetConnectionPool# activate Connection pool : key = " + var1 + " : " + var4);
         }

         if (Debug.isPoolingEnabled()) {
            Debug.pooling(ConnectorLogger.logConnectionPoolReset(var1));
         }

         return true;
      } catch (weblogic.common.ResourceException var9) {
         throw new RAOutboundException("unable to reset connection pool " + var1, var9);
      } catch (UndeploymentException var10) {
         throw new RAOutboundException("unable to reset connection pool " + var1, var10);
      } catch (ResourceException var11) {
         throw new RAOutboundException("unable to reset connection pool " + var1, var11);
      }
   }
}
