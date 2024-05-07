package weblogic.servlet.internal.session;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.health.HealthState;
import weblogic.j2ee.descriptor.wl.SessionDescriptorBean;
import weblogic.jndi.factories.java.javaURLContextFactory;
import weblogic.logging.Loggable;
import weblogic.management.DeploymentException;
import weblogic.management.configuration.ClusterMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.management.runtime.ServletSessionRuntimeMBean;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.security.SubjectUtils;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.servlet.HTTPLogger;
import weblogic.servlet.SessionCreationException;
import weblogic.servlet.internal.ContextVersionManager;
import weblogic.servlet.internal.NakedTimerListenerBase;
import weblogic.servlet.internal.ServletContextManager;
import weblogic.servlet.internal.ServletRequestImpl;
import weblogic.servlet.internal.ServletResponseImpl;
import weblogic.servlet.internal.WebAppServletContext;
import weblogic.timers.Timer;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public abstract class SessionContext implements HttpSessionContext, SessionConstants {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean DEBUG = false;
   protected final WebAppServletContext servletContext;
   private final ServerRuntimeMBean serverRuntime;
   private final Hashtable openSessions = new Hashtable();
   protected final Hashtable transientData = new Hashtable();
   protected final SessionConfigManager configMgr;
   private static final ClusterMBean cluster;
   private int curOpenSessions = 0;
   private int totalOpenSessions = 0;
   private int maxOpenSessions = 0;
   private SessionInvalidator invalidator = null;
   private static final DebugCategory DEBUG_APP_VERSION;
   protected static final DebugLogger DEBUG_SESSIONS;
   protected static final String COHERENECE_WEB_SESSIONS = "coherence-web-sessions-enabled";
   private static final String COMPONENT = "In-memory Replication - Servlet";
   private static final ServerIdentity currentClusterMember;

   protected SessionContext(WebAppServletContext var1, SessionConfigManager var2) {
      this.configMgr = var2;
      this.servletContext = var1;
      this.serverRuntime = ManagementService.getRuntimeAccess(KERNEL_ID).getServerRuntime();
   }

   public void startTimers() {
      this.initializeInvalidator();
   }

   protected void checkSessionCount() {
      if (this.isServerOverloaded()) {
         throw new SessionCreationException("The server is running low on memory, cannot create new sessions");
      } else if (this.configMgr.getMaxInMemorySessions() > -1 && this.getOpenSessions().size() >= this.configMgr.getMaxInMemorySessions()) {
         throw new SessionCreationException("Cannot create new sessions as the MaxInMemorySessions limit (" + this.configMgr.getMaxInMemorySessions() + ") has been exceeded");
      }
   }

   private boolean isServerOverloaded() {
      HealthState var1 = this.serverRuntime.getHealthState();
      if (var1.getState() == 4 && var1.getReasonCode().length >= 1) {
         for(int var2 = 0; var2 < var1.getReasonCode().length; ++var2) {
            if (var1.getReasonCode()[var2] == "server is low on memory") {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   protected boolean isDebugEnabled() {
      return DEBUG_SESSIONS.isDebugEnabled() || this.getConfigMgr() != null && this.getConfigMgr().isDebugEnabled();
   }

   protected void initializeInvalidator() {
      this.invalidator = new SessionInvalidator();
   }

   public Hashtable getSessionsMap() {
      return this.openSessions;
   }

   public void setSessionsMap(Hashtable var1) {
      if (var1 != null) {
         this.openSessions.putAll(var1);
      }
   }

   public void initialize(WebAppServletContext var1) {
   }

   public static final synchronized SessionContext getInstance(WebAppServletContext var0, SessionDescriptorBean var1) throws DeploymentException {
      SessionConfigManager var2 = new SessionConfigManager(var1, var0);
      WebAppServletContext var3;
      if (var0 != null && var0.getVersionId() != null && var0.getContextManager() != null) {
         var3 = var0.getContextManager().getContext(var0.isAdminMode());
         if (var3 != null && var3.getVersionId() != null && !var2.isSamePersistentStore(var3.getSessionContext().getConfigMgr())) {
            Loggable var19 = HTTPSessionLogger.logIncompatiblePersistentStoreLoggable(var0.getAppDisplayName());
            var19.log();
            throw new DeploymentException(var19.getMessage());
         }
      }

      var3 = null;
      if ("true".equalsIgnoreCase(var0.getInitParameter("coherence-web-sessions-enabled")) || var0.getApplicationContext() != null && "true".equalsIgnoreCase(var0.getApplicationContext().getApplicationParameter("coherence-web-sessions-enabled"))) {
         try {
            Class var18 = var0.getServletClassLoader().loadClass("weblogic.servlet.internal.session.CoherenceWebSessionContextImpl");
            Class[] var25 = new Class[]{var0.getClass(), var2.getClass()};
            Constructor var26 = var18.getConstructor(var25);
            Object[] var23 = new Object[]{var0, var2};
            return (SessionContext)var26.newInstance(var23);
         } catch (ClassNotFoundException var14) {
            throw new DeploymentException("This web-application was configured to use Coherence*Web sessions via the coherence-web-sessions-enabled context-param, but either the Coherence*Web session SPI library  or coherence.jar is not found in the web-application classloader.", var14);
         } catch (Exception var15) {
            throw new DeploymentException("An error occurred instantiating Coherence*Web sessions for this web application", var15);
         }
      } else {
         String var4 = var2.getPersistentStoreType();
         Loggable var5;
         Object var17;
         if ("memory".equals(var4)) {
            var17 = new MemorySessionContext(var0, var2);
            if (DEBUG_SESSIONS.isDebugEnabled()) {
               var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("memory", var0.getAppDisplayName());
               DEBUG_SESSIONS.debug(var5.getMessage());
            }
         } else if ("file".equals(var4)) {
            var17 = new FileSessionContext(var0, var2);
            if (DEBUG_SESSIONS.isDebugEnabled()) {
               var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("file", var0.getAppDisplayName());
               DEBUG_SESSIONS.debug(var5.getMessage());
            }
         } else if ("replicated".equals(var4) && cluster != null && "man".equals(cluster.getClusterType())) {
            TargetValidator.validateTargetting(var0.getWebAppModule(), var4);
            var17 = new MANReplicatedSessionContext(var0, var2);
            if (DEBUG_SESSIONS.isDebugEnabled()) {
               var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("sync-replication-across-cluster", var0.getAppDisplayName());
               DEBUG_SESSIONS.debug(var5.getMessage());
            }
         } else if ("replicated".equals(var4) && cluster != null && "wan".equals(cluster.getClusterType())) {
            if (null == cluster.getDataSourceForSessionPersistence()) {
               var5 = HTTPSessionLogger.logInsufficientConfigurationLoggable(var0.getAppDisplayName());
               var5.log();
               throw new DeploymentException(var5.getMessage());
            }

            TargetValidator.validateTargetting(var0.getWebAppModule(), var4);
            var17 = new WANSessionContext(var0, var2);
            if (DEBUG_SESSIONS.isDebugEnabled()) {
               var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("async-replication-across-cluster", var0.getAppDisplayName());
               DEBUG_SESSIONS.debug(var5.getMessage());
            }
         } else {
            Loggable var6;
            Loggable var7;
            RuntimeAccess var20;
            if ("replicated".equals(var4)) {
               var20 = ManagementService.getRuntimeAccess(KERNEL_ID);
               if (var20.getServer().getCluster() == null) {
                  var6 = HTTPSessionLogger.logClusteringRequiredForReplicationLoggable(var0.getAppDisplayName());
                  var6.log();
                  throw new DeploymentException(var6.getMessage());
               }

               try {
                  TargetValidator.validateTargetting(var0.getWebAppModule(), var4);
                  if (cluster.getPersistSessionsOnShutdown()) {
                     if (null == cluster.getDataSourceForSessionPersistence()) {
                        var6 = HTTPSessionLogger.logInsufficientConfigurationLoggable(var0.getAppDisplayName());
                        var6.log();
                        throw new DeploymentException(var6.getMessage());
                     }

                     var17 = new WANSessionContext(var0, var2);
                  } else {
                     var17 = new ReplicatedSessionContext(var0, var2);
                  }

                  if (DEBUG_SESSIONS.isDebugEnabled()) {
                     var6 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("replicated", var0.getAppDisplayName());
                     DEBUG_SESSIONS.debug(var6.getMessage());
                  }
               } catch (RuntimeException var13) {
                  HTTPSessionLogger.logSessionNotAllowed(var13.getMessage());
                  var17 = new MemorySessionContext(var0, var2);
                  var7 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("memory", var0.getAppDisplayName());
                  DEBUG_SESSIONS.debug(var7.getMessage());
               }
            } else if ("async-replicated".equals(var4) && cluster != null && "man".equals(cluster.getClusterType())) {
               TargetValidator.validateTargetting(var0.getWebAppModule(), var4);
               var17 = new MANAsyncReplicatedSessionContext(var0, var2);
               if (DEBUG_SESSIONS.isDebugEnabled()) {
                  var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("sync-replication-across-cluster", var0.getAppDisplayName());
                  DEBUG_SESSIONS.debug(var5.getMessage());
               }
            } else if ("async-replicated".equals(var4) && cluster != null && "wan".equals(cluster.getClusterType())) {
               if (null == cluster.getDataSourceForSessionPersistence()) {
                  var5 = HTTPSessionLogger.logInsufficientConfigurationLoggable(var0.getAppDisplayName());
                  var5.log();
                  throw new DeploymentException(var5.getMessage());
               }

               TargetValidator.validateTargetting(var0.getWebAppModule(), var4);
               var17 = new WANAsyncSessionContext(var0, var2);
               if (DEBUG_SESSIONS.isDebugEnabled()) {
                  var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("async-replication-across-cluster", var0.getAppDisplayName());
                  DEBUG_SESSIONS.debug(var5.getMessage());
               }
            } else if ("async-replicated".equals(var4)) {
               var20 = ManagementService.getRuntimeAccess(KERNEL_ID);
               if (var20.getServer().getCluster() == null) {
                  var6 = HTTPSessionLogger.logClusteringRequiredForReplicationLoggable(var0.getAppDisplayName());
                  var6.log();
                  throw new DeploymentException(var6.getMessage());
               }

               try {
                  TargetValidator.validateTargetting(var0.getWebAppModule(), var4);
                  if (cluster.getPersistSessionsOnShutdown()) {
                     if (null == cluster.getDataSourceForSessionPersistence()) {
                        var6 = HTTPSessionLogger.logInsufficientConfigurationLoggable(var0.getAppDisplayName());
                        var6.log();
                        throw new DeploymentException(var6.getMessage());
                     }

                     var17 = new WANSessionContext(var0, var2);
                  } else {
                     var17 = new AsyncReplicatedSessionContext(var0, var2);
                  }

                  if (DEBUG_SESSIONS.isDebugEnabled()) {
                     var6 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("async-replicated", var0.getAppDisplayName());
                     DEBUG_SESSIONS.debug(var6.getMessage());
                  }
               } catch (RuntimeException var12) {
                  HTTPSessionLogger.logSessionNotAllowed(var12.getMessage());
                  var17 = new MemorySessionContext(var0, var2);
                  var7 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("memory", var0.getAppDisplayName());
                  DEBUG_SESSIONS.debug(var7.getMessage());
               }
            } else if ("async-replicated-if-clustered".equals(var4)) {
               var20 = ManagementService.getRuntimeAccess(KERNEL_ID);
               if (var20.getServer().getCluster() == null) {
                  var17 = new MemorySessionContext(var0, var2);
                  if (DEBUG_SESSIONS.isDebugEnabled()) {
                     var6 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("memory", var0.getAppDisplayName());
                     DEBUG_SESSIONS.debug(var6.getMessage());
                  }
               } else {
                  try {
                     TargetValidator.validateTargetting(var0.getWebAppModule(), var4);
                     if (cluster.getPersistSessionsOnShutdown()) {
                        if (null == cluster.getDataSourceForSessionPersistence()) {
                           var6 = HTTPSessionLogger.logInsufficientConfigurationLoggable(var0.getAppDisplayName());
                           var6.log();
                           throw new DeploymentException(var6.getMessage());
                        }

                        var17 = new WANSessionContext(var0, var2);
                     } else {
                        var17 = new AsyncReplicatedSessionContext(var0, var2);
                     }

                     if (DEBUG_SESSIONS.isDebugEnabled()) {
                        var6 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("async-replicated", var0.getContextPath());
                        DEBUG_SESSIONS.debug(var6.getMessage());
                     }
                  } catch (RuntimeException var11) {
                     HTTPSessionLogger.logSessionNotAllowed(var11.getMessage());
                     var17 = new MemorySessionContext(var0, var2);
                     var7 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("memory", var0.getAppDisplayName());
                     DEBUG_SESSIONS.debug(var7.getMessage());
                  }
               }
            } else if ("replicated_if_clustered".equals(var4)) {
               var20 = ManagementService.getRuntimeAccess(KERNEL_ID);
               if (var20.getServer().getCluster() == null) {
                  var17 = new MemorySessionContext(var0, var2);
                  if (DEBUG_SESSIONS.isDebugEnabled()) {
                     var6 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("memory", var0.getAppDisplayName());
                     DEBUG_SESSIONS.debug(var6.getMessage());
                  }
               } else {
                  try {
                     TargetValidator.validateTargetting(var0.getWebAppModule(), var4);
                     if ("man".equals(cluster.getClusterType())) {
                        var17 = new MANReplicatedSessionContext(var0, var2);
                        if (DEBUG_SESSIONS.isDebugEnabled()) {
                           var6 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("sync-replication-across-cluster", var0.getAppDisplayName());
                           DEBUG_SESSIONS.debug(var6.getMessage());
                        }
                     } else if (!"wan".equals(cluster.getClusterType()) && !cluster.getPersistSessionsOnShutdown()) {
                        var17 = new ReplicatedSessionContext(var0, var2);
                        if (DEBUG_SESSIONS.isDebugEnabled()) {
                           var6 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("replicated", var0.getContextPath());
                           DEBUG_SESSIONS.debug(var6.getMessage());
                        }
                     } else {
                        if (null == cluster.getDataSourceForSessionPersistence()) {
                           var6 = HTTPSessionLogger.logInsufficientConfigurationLoggable(var0.getAppDisplayName());
                           var6.log();
                           throw new DeploymentException(var6.getMessage());
                        }

                        var17 = new WANSessionContext(var0, var2);
                        if (DEBUG_SESSIONS.isDebugEnabled()) {
                           var6 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("async-replication-across-cluster", var0.getAppDisplayName());
                           DEBUG_SESSIONS.debug(var6.getMessage());
                        }
                     }
                  } catch (RuntimeException var16) {
                     HTTPSessionLogger.logSessionNotAllowed(var16.getMessage());
                     var17 = new MemorySessionContext(var0, var2);
                     var7 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("memory", var0.getAppDisplayName());
                     DEBUG_SESSIONS.debug(var7.getMessage());
                  }
               }
            } else if ("jdbc".equals(var4)) {
               var17 = new JDBCSessionContext(var0, var2);
               if (DEBUG_SESSIONS.isDebugEnabled()) {
                  var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("jdbc", var0.getAppDisplayName());
                  DEBUG_SESSIONS.debug(var5.getMessage());
               }
            } else if ("async-jdbc".equals(var4)) {
               var17 = new AsyncJDBCSessionContext(var0, var2);
               if (DEBUG_SESSIONS.isDebugEnabled()) {
                  var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("async-jdbc", var0.getAppDisplayName());
                  DEBUG_SESSIONS.debug(var5.getMessage());
               }
            } else if ("cookie".equals(var4)) {
               var17 = new CookieSessionContext(var0, var2);
               if (DEBUG_SESSIONS.isDebugEnabled()) {
                  var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("cookie", var0.getAppDisplayName());
                  DEBUG_SESSIONS.debug(var5.getMessage());
               }
            } else {
               if (!"coherence-web".equals(var4)) {
                  var5 = HTTPSessionLogger.logUnknownPeristentTypeLoggable(var4, var0.getAppDisplayName());
                  var5.log();
                  throw new DeploymentException(var5.getMessage());
               }

               try {
                  Class var21 = var0.getServletClassLoader().loadClass("weblogic.servlet.internal.session.CoherenceWebSessionContextImpl");
                  Class[] var24 = new Class[]{var0.getClass(), var2.getClass()};
                  Constructor var22 = var21.getConstructor(var24);
                  Object[] var8 = new Object[]{var0, var2};
                  var17 = (SessionContext)var22.newInstance(var8);
               } catch (ClassNotFoundException var9) {
                  throw new DeploymentException("This web-application was confirgured to use Coherence*Web sessions, but either the Coherence*Web session SPI library is not found in the web-application classloader or the coherence-wls.jar was not found in the weblogic system classloader.", var9);
               } catch (Exception var10) {
                  throw new DeploymentException("An error occured instantaiting Coherence*Web sessions for this web application", var10);
               }

               if (DEBUG_SESSIONS.isDebugEnabled()) {
                  var5 = HTTPSessionLogger.logCreatingSessionContextOfTypeLoggable("coherence-web", var0.getAppDisplayName());
                  DEBUG_SESSIONS.debug(var5.getMessage());
               }
            }
         }

         return (SessionContext)var17;
      }
   }

   public abstract void sync(HttpSession var1);

   public abstract String getPersistentStoreType();

   public abstract HttpSession getNewSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3);

   public abstract SessionData getSessionInternal(String var1, ServletRequestImpl var2, ServletResponseImpl var3);

   boolean invalidateSession(SessionData var1, boolean var2) {
      return this.invalidateSession(var1, var2, true);
   }

   abstract boolean invalidateSession(SessionData var1, boolean var2, boolean var3);

   abstract void unregisterExpiredSessions(ArrayList var1);

   public String[] getIdsInternal() {
      Hashtable var1 = this.getOpenSessions();
      return var1 == null ? new String[0] : (String[])((String[])var1.keySet().toArray(new String[0]));
   }

   public SessionData getSessionInternalForAuthentication(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      return this.getSessionInternal(var1, var2, var3);
   }

   public boolean hasSession(String var1) {
      return this.getSessionInternal(var1, (ServletRequestImpl)null, (ServletResponseImpl)null) != null;
   }

   public HttpSession getSharedSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      if (!this.getConfigMgr().isSessionSharingEnabled()) {
         return null;
      } else {
         WebAppServletContext var4 = this.getServletContext();
         if (var4 == null) {
            return null;
         } else {
            ServletContextManager var5 = var4.getServer().getServletContextManager();
            WebAppServletContext[] var6 = var5.getAllContexts();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               if (var4.getApplicationContext() == var6[var7].getApplicationContext() && var6[var7] != var4) {
                  SessionData var8 = var6[var7].getSessionContext().getSessionInternal(var1, var2, var3);
                  if (var8 != null) {
                     return new SharedSessionData(var8, var4);
                  }
               }
            }

            return null;
         }
      }
   }

   public SessionData getSessionFromOtherContexts(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      WebAppServletContext var4 = this.getServletContext();
      if (var4 != null && var4.getVersionId() != null) {
         ContextVersionManager var5 = var4.getContextManager();
         if (var5 == null) {
            return null;
         } else {
            Iterator var6 = var5.getServletContexts(var2.isAdminChannelRequest());

            while(var6.hasNext()) {
               WebAppServletContext var7 = (WebAppServletContext)var6.next();
               if (var7 != var4) {
                  if (DEBUG_APP_VERSION.isEnabled()) {
                     HTTPLogger.logDebug("Trying to getSessionInternal from another  context=" + var7 + " for id=" + var1);
                  }

                  SessionData var8 = var7.getSessionContext().getSessionInternal(var1, var2, var3);
                  if (var8 != null) {
                     return var8;
                  }
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public abstract int getNonPersistedSessionCount();

   public void storeAttributesInBytes() {
      String[] var1 = this.getIdsInternal();
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length && var1[var2] != null; ++var2) {
            SessionData var3 = this.getSessionInternal(var1[var2], (ServletRequestImpl)null, (ServletResponseImpl)null);
            if (var3 != null) {
               var3.storeAttributesInBytes();
            }
         }

      }
   }

   public void destroy(boolean var1) {
      this.cancelTrigger();
   }

   public void cancelTrigger() {
      if (this.invalidator != null) {
         this.invalidator.stop();
      }

      this.invalidator = null;
   }

   public WebAppServletContext getServletContext() {
      return this.servletContext;
   }

   public abstract int getCurrOpenSessionsCount();

   public int getTotalOpenSessionsCount() {
      return this.totalOpenSessions;
   }

   public int getMaxOpenSessionsCount() {
      return this.maxOpenSessions;
   }

   public void setCurrOpenSessionsCount(int var1) {
      this.curOpenSessions = var1;
   }

   public void setTotalOpenSessionsCount(int var1) {
      this.totalOpenSessions = var1;
   }

   public void setMaxOpenSessionsCount(int var1) {
      this.maxOpenSessions = var1;
   }

   public void incrementOpenSessionsCount() {
      ++this.curOpenSessions;
      ++this.totalOpenSessions;
      if (this.curOpenSessions > this.maxOpenSessions) {
         this.maxOpenSessions = this.curOpenSessions;
      }

   }

   public void decrementOpenSessionsCount() {
      --this.curOpenSessions;
      if (this.curOpenSessions < 0) {
         this.curOpenSessions = 0;
      }

   }

   public void addSession(String var1, Object var2) {
      this.openSessions.put(var1, var2);
      this.servletContext.addSession(var1);
   }

   public void removeSession(String var1) {
      this.openSessions.remove(var1);
      this.servletContext.removeSession(var1);
   }

   public void exit(ServletRequestImpl var1, ServletResponseImpl var2, HttpSession var3) {
   }

   public void enter(ServletRequestImpl var1, ServletResponseImpl var2, HttpSession var3) {
   }

   public Object getOpenSession(String var1) {
      return this.openSessions.get(var1);
   }

   public Hashtable getOpenSessions() {
      return this.openSessions;
   }

   public synchronized ServletSessionRuntimeMBean getServletSessionRuntimeMBean(String var1) {
      SessionData var2 = this.getSessionInternal(var1, (ServletRequestImpl)null, (ServletResponseImpl)null);
      return var2 != null && var2.isValid() ? var2.getServletSessionRuntimeMBean() : null;
   }

   public synchronized ServletSessionRuntimeMBean[] getServletSessionRuntimeMBeans() {
      if (!this.configMgr.isMonitoringEnabled()) {
         return new ServletSessionRuntimeMBean[0];
      } else {
         HashSet var1 = new HashSet();
         String[] var2 = this.getIdsInternal();
         if (var2.length < 1) {
            return new ServletSessionRuntimeMBean[0];
         } else {
            for(int var3 = 0; var3 < var2.length && var2[var3] != null; ++var3) {
               ServletSessionRuntimeMBean var4 = this.getServletSessionRuntimeMBean(var2[var3]);
               if (var4 != null) {
                  var1.add(var4);
               }
            }

            if (var1.isEmpty()) {
               return new ServletSessionRuntimeMBean[0];
            } else {
               ServletSessionRuntimeMBean[] var5 = new ServletSessionRuntimeMBean[var1.size()];
               var1.toArray(var5);
               return var5;
            }
         }
      }
   }

   public synchronized Set getAllServletSessions() {
      String[] var1 = this.getIdsInternal();
      if (var1.length < 1) {
         return new HashSet();
      } else {
         HashSet var2 = new HashSet(var1.length);

         for(int var3 = 0; var3 < var1.length && var1[var3] != null; ++var3) {
            SessionData var4 = this.getSessionInternal(var1[var3], (ServletRequestImpl)null, (ServletResponseImpl)null);
            if (var4 != null && var4.isValid()) {
               SessionInfo var5 = new SessionInfo(var4);
               if (var5.getMonitoringId() != null) {
                  var2.add(var5);
               }
            }
         }

         return var2;
      }
   }

   public String[] getServletSessionsMonitoringIds() {
      String[] var1 = this.getIdsInternal();
      if (var1.length < 1) {
         return new String[0];
      } else {
         HashSet var2 = new HashSet();

         for(int var3 = 0; var3 < var1.length && var1[var3] != null; ++var3) {
            SessionData var4 = this.getSessionInternal(var1[var3], (ServletRequestImpl)null, (ServletResponseImpl)null);
            if (var4 != null && var4.isValid()) {
               String var5 = var4.getMonitoringId();
               if (var5 != null) {
                  var2.add(var5);
               }
            }
         }

         if (var2.isEmpty()) {
            return new String[0];
         } else {
            String[] var6 = new String[var2.size()];
            var2.toArray(var6);
            return var6;
         }
      }
   }

   public void invalidateServletSession(String var1) throws IllegalStateException {
      SessionData var2 = this.getSessionWithMonitoringId(var1);
      if (var2 != null && var2.isValid()) {
         var2.invalidate();
      } else {
         throw new IllegalStateException("Session has been invalidated already");
      }
   }

   private SessionData getSessionWithMonitoringId(String var1) {
      String[] var2 = this.getIdsInternal();
      if (var2.length < 1) {
         return null;
      } else {
         for(int var3 = 0; var3 < var2.length && var2[var3] != null; ++var3) {
            SessionData var4 = this.getSessionInternal(var2[var3], (ServletRequestImpl)null, (ServletResponseImpl)null);
            if (var4 != null && var4.isValid() && var1.equals(var4.getMonitoringId())) {
               return var4;
            }
         }

         return null;
      }
   }

   public long getSessionLastAccessedTime(String var1) throws IllegalStateException {
      SessionData var2 = this.getSessionWithMonitoringId(var1);
      if (var2 != null && var2.isValid()) {
         return var2.getLastAccessedTime();
      } else {
         throw new IllegalStateException("Session has been invalidated already");
      }
   }

   public long getSessionMaxInactiveInterval(String var1) throws IllegalStateException {
      SessionData var2 = this.getSessionWithMonitoringId(var1);
      if (var2 != null && var2.isValid()) {
         return (long)var2.getMaxInactiveInterval();
      } else {
         throw new IllegalStateException("Session has been invalidated already");
      }
   }

   public String getMonitoringId(String var1) {
      SessionData var2 = this.getSessionInternal(var1, (ServletRequestImpl)null, (ServletResponseImpl)null);
      if (var2 != null && var2.isValid()) {
         return var2.getMonitoringId();
      } else {
         throw new IllegalStateException("Session has been invalidated already");
      }
   }

   protected final ServerIdentity getCurrentClusterMember() {
      return currentClusterMember;
   }

   public void notifySessionAttributeChange(HttpSession var1, String var2, Object var3, Object var4) {
      this.servletContext.getEventsManager().notifySessionAttributeChange(var1, var2, var3, var4);
   }

   public static void declareProperties() {
   }

   public void setInvalidationIntervalSecs(int var1) {
      if (this.configMgr.getInvalidationIntervalSecs() != var1) {
         if (this.invalidator != null) {
            this.invalidator.bounce();
         }

      }
   }

   public synchronized HttpSession getSession(String var1) {
      HTTPSessionLogger.logDeprecatedCall("getSession(String id)");
      return null;
   }

   public Enumeration getIds() {
      HTTPSessionLogger.logDeprecatedCall("getIds()");
      return (new Vector()).elements();
   }

   public void deleteInvalidSessions() {
      if (this.invalidator != null) {
         this.invalidator.cleanupExpiredSessions();
      }

   }

   public SessionConfigManager getConfigMgr() {
      return this.configMgr;
   }

   protected void invalidateOrphanedSessions() {
   }

   public String lookupAppVersionIdForSession(String var1, ServletRequestImpl var2, ServletResponseImpl var3) {
      return null;
   }

   private boolean invalidateSession(SessionData var1) {
      InvalidationAction var2 = new InvalidationAction(this, var1);
      AuthenticatedSubject var3 = (AuthenticatedSubject)var1.getInternalAttribute("weblogic.authuser");
      if (var3 == null) {
         var3 = SubjectUtils.getAnonymousSubject();
      }

      Throwable var4 = (Throwable)SecurityServiceManager.runAs(KERNEL_ID, var3, var2);
      if (var4 != null) {
         HTTPSessionLogger.logUnexpectedTimeoutError(var4);
      }

      return var4 == null && var2.isInvalidated();
   }

   static {
      cluster = ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getCluster();
      DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");
      DEBUG_SESSIONS = DebugLogger.getDebugLogger("DebugHttpSessions");
      currentClusterMember = LocalServerIdentity.getIdentity();
   }

   private static class InvalidationAction implements PrivilegedAction {
      private SessionData sess;
      private SessionContext ctx;
      private boolean invalidated = false;

      InvalidationAction(SessionContext var1, SessionData var2) {
         this.ctx = var1;
         this.sess = var2;
      }

      public Object run() {
         try {
            this.invalidated = this.ctx.invalidateSession(this.sess, true);
            return null;
         } catch (Throwable var2) {
            return var2;
         }
      }

      public boolean isInvalidated() {
         return this.invalidated;
      }
   }

   private class SessionInvalidator extends NakedTimerListenerBase {
      private Timer timer;
      private int invalCount;

      private SessionInvalidator() {
         super("ServletSessionTimer", SessionContext.this.servletContext);
         this.invalCount = 0;
         this.start();
      }

      private void start() {
         int var1 = SessionContext.this.configMgr.getInvalidationIntervalSecs();
         if (SessionContext.this.isDebugEnabled()) {
            Loggable var2 = HTTPSessionLogger.logInvalidationIntervalLoggable(var1);
            SessionContext.DEBUG_SESSIONS.debug(var2.getMessage());
         }

         this.timer = this.timerManager.schedule(this, 0L, (long)(var1 * 1000));
      }

      private void stop() {
         this.timer.cancel();
         this.timerManager.stop();
      }

      private void bounce() {
         this.timer.cancel();
         this.start();
      }

      public void timerExpired(Timer var1) {
         if (SessionContext.this.getServletContext().isStarted()) {
            Thread var2 = Thread.currentThread();
            ClassLoader var3 = var2.getContextClassLoader();

            try {
               javaURLContextFactory.pushContext(SessionContext.this.getServletContext().getEnvironmentContext());
               ClassLoader var4 = SessionContext.this.getServletContext().getServletClassLoader();
               var2.setContextClassLoader(var4);
               Throwable var10000 = (Throwable)SecurityServiceManager.runAs(SessionContext.KERNEL_ID, SubjectUtils.getAnonymousSubject(), new PrivilegedAction() {
                  public Object run() {
                     try {
                        SessionInvalidator.this.cleanupExpiredSessions();
                     } catch (Throwable var2) {
                        HTTPSessionLogger.logUnexpectedTimeoutError(var2);
                     }

                     return null;
                  }
               });
            } finally {
               javaURLContextFactory.popContext();
               var2.setContextClassLoader(var3);
            }

         }
      }

      private boolean hasSessionExpired(SessionData var1, long var2) {
         long var4 = Long.MAX_VALUE;
         int var6 = var1.getMaxInactiveInterval();
         var4 = var2 - (long)var6 * 1000L;
         return var6 >= 0 && var1.getLAT() < var4;
      }

      public void cleanupExpiredSessions() {
         try {
            long var1 = System.currentTimeMillis();
            String[] var3 = SessionContext.this.getIdsInternal();
            int var4 = var3.length;
            boolean var5 = false;
            boolean var6 = false;
            ArrayList var7 = new ArrayList();

            for(int var8 = 0; var8 < var4; ++var8) {
               try {
                  if (var3[var8] == null) {
                     break;
                  }

                  SessionData var9 = SessionContext.this.getSessionInternal(var3[var8], (ServletRequestImpl)null, (ServletResponseImpl)null);
                  if (var9 != null) {
                     if (var9.isValid() && !var9.sessionInUse()) {
                        synchronized(var9) {
                           if (var9.isValid() && !var9.sessionInUse() && this.hasSessionExpired(var9, var1)) {
                              if (var9.acquireInvalidationLock()) {
                                 if (SessionContext.this.invalidateSession(var9)) {
                                    var7.add(var9);
                                    if (SessionContext.this.isDebugEnabled()) {
                                       Loggable var11 = HTTPSessionLogger.logTimerInvalidatedSessionLoggable(var3[var8], SessionContext.this.getServletContext().getContextPath());
                                       SessionContext.DEBUG_SESSIONS.debug(var11.getMessage());
                                    }
                                 }
                              }
                           }
                        }
                     }
                  } else {
                     var9 = (SessionData)SessionContext.this.openSessions.get(var3[var8]);
                     if (var9 != null && !var9.sessionInUse()) {
                        synchronized(var9) {
                           if (!var9.sessionInUse() && this.hasSessionExpired(var9, var1)) {
                              SessionContext.this.invalidateSession(var9);
                           }
                        }
                     }
                  }
               } catch (ThreadDeath var16) {
                  throw var16;
               } catch (Throwable var17) {
                  HTTPSessionLogger.logUnexpectedTimeoutError(var17);
               }
            }

            SessionContext.this.unregisterExpiredSessions(var7);
            ++this.invalCount;
            this.invalCount %= 10;
            if (this.invalCount == 9) {
               SessionContext.this.invalidateOrphanedSessions();
            }
         } catch (ThreadDeath var18) {
            throw var18;
         } catch (Throwable var19) {
            HTTPSessionLogger.logUnexpectedTimeoutErrorRaised(var19);
         }

      }

      // $FF: synthetic method
      SessionInvalidator(Object var2) {
         this();
      }
   }
}
