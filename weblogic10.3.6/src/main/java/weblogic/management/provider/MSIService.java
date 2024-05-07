package weblogic.management.provider;

import java.rmi.UnknownHostException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.naming.NamingException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.Environment;
import weblogic.logging.Loggable;
import weblogic.management.ManagementLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.internal.ConfigLogger;
import weblogic.protocol.ConnectMonitorFactory;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.URLManager;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rmi.extensions.ConnectEvent;
import weblogic.rmi.extensions.ConnectListener;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class MSIService extends AbstractServerService implements ConnectListener {
   private static MSIService singleton;
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   private boolean adminServerAvailable = true;
   private boolean registered = false;
   private String cmdURL = null;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public void stop() throws ServiceFailureException {
   }

   public void halt() throws ServiceFailureException {
   }

   public void start() throws ServiceFailureException {
   }

   public static MSIService getMSIService() {
      return singleton;
   }

   public MSIService() {
      if (singleton != null) {
         throw new AssertionError("MSIService already initialized");
      } else {
         singleton = this;
      }
   }

   public synchronized void doPostParseInitialization(DomainMBean var1) throws ServiceFailureException {
      if (!this.isAdminServerAvailable()) {
         this.cmdURL = ManagementService.getPropertyService(kernelId).getAdminHost();
         String var2 = ManagementService.getPropertyService(kernelId).getServerName();
         ServerMBean var3 = var1.lookupServer(var2);
         if (var3 == null) {
            ServerMBean[] var7 = var1.getServers();
            String var5 = "{";

            for(int var6 = 0; var6 < var7.length; ++var6) {
               if (var6 > 0) {
                  var5 = var5 + ",";
               }

               var5 = var5 + var7[var6].getName();
            }

            var5 = var5 + "}";
            Loggable var8 = ManagementLogger.logServerNameDoesNotExistLoggable(var2, var5);
            var8.log();
            throw new ServiceFailureException(var8.getMessage());
         } else if (!var3.isManagedServerIndependenceEnabled()) {
            Loggable var4 = ConfigLogger.logMSINotEnabledLoggable(var2);
            var4.log();
            throw new ServiceFailureException(var4.getMessage());
         } else {
            ConfigLogger.logStartingIndependentManagerServer();
            ConnectMonitorFactory.getConnectMonitor().addConnectListener(this);
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Added connect listener");
            }

         }
      }
   }

   public synchronized void setAdminServerAvailable(boolean var1) {
      this.adminServerAvailable = var1;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Set admin server available to " + var1);
      }

   }

   public synchronized boolean isAdminServerAvailable() {
      return this.adminServerAvailable;
   }

   public synchronized void registerForReconnectToAdminServer() throws ServiceFailureException {
      if (!this.registered) {
         this.registered = true;
         String[] var1 = this.getAllAdminBinaryURLs();
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            Environment var4 = new Environment();
            var4.setProperty("weblogic.jndi.requestTimeout", Long.getLong("weblogic.jndi.MSIlookupRequestTimeout", 0L));
            var4.setProviderUrl(var1[var3]);
            var2.add(var4);
         }

         if (!var2.isEmpty()) {
            try {
               ConnectMonitorFactory.registerForever((List)var2);
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Registered connect monitor for admin server");
               }
            } catch (NamingException var7) {
               String var8 = ((Environment)var2.get(0)).getProviderUrl();
               if (var2.size() > 1) {
                  var8 = "{ ";

                  Environment var6;
                  for(Iterator var5 = var2.iterator(); var5.hasNext(); var8 = var8 + var6.getProviderUrl() + " ") {
                     var6 = (Environment)var5.next();
                  }

                  var8 = var8 + "}";
               }

               Loggable var9 = ConfigLogger.logErrorConnectingAdminServerForHomeLoggable(var8, var7);
               var9.log();
               throw new ServiceFailureException(var9.getMessage(), var7);
            }
         }

      }
   }

   private String[] getAllAdminBinaryURLs() {
      String var1 = null;
      String var2 = ManagementService.getPropertyService(kernelId).getAdminBinaryURL();
      int var3;
      if (var2 != null && (var3 = var2.indexOf("://")) != -1) {
         var1 = var2.substring(0, var3);
      }

      ManagementService.getPropertyService(kernelId);
      String[] var4 = PropertyService.getAllAdminHttpUrls();
      if (var4 == null) {
         var4 = new String[]{var2};
      } else {
         for(int var5 = 0; var5 < var4.length; ++var5) {
            String var6 = var4[var5];
            String var7 = var6.substring(0, var6.indexOf("://"));
            var4[var5] = var1 != null ? var6.replace(var7 + "://", var1 + "://") : URLManager.normalizeToAdminProtocol(var6);
         }
      }

      return var4;
   }

   public boolean isAdminRequiredButNotSpecifiedOnBoot() {
      if (this.isAdminServerAvailable()) {
         return false;
      } else {
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
         ServerMBean var2 = var1.getDomain().lookupServer(var1.getAdminServerName());
         boolean var3 = ChannelHelper.isAdminChannelEnabled(var2);
         String var4 = this.getAdminURL();
         return !var3 && var4 != null;
      }
   }

   private String getAdminURL() {
      try {
         RuntimeAccess var1 = ManagementService.getRuntimeAccess(kernelId);
         return URLManager.findURL(var1.getAdminServerName(), ProtocolHandlerAdmin.PROTOCOL_ADMIN);
      } catch (UnknownHostException var2) {
         return null;
      }
   }

   public void onConnect(ConnectEvent var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("onConnect event, server = " + var1.getServerName());
      }

      ManagementService.getPropertyService(kernelId).waitForChannelServiceReady();
      RuntimeAccess var2 = ManagementService.getRuntimeAccess(kernelId);
      if (var2 != null) {
         if (var1.getServerName().equals(var2.getAdminServerName())) {
            if (this.isAdminRequiredButNotSpecifiedOnBoot()) {
               String var3 = URLManager.normalizeToHttpProtocol(this.getAdminURL());
               ConfigLogger.logAdminRequiredButNotSpecified(this.cmdURL, var3);
            } else {
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("onConnect setting admin server available.");
               }

               this.setAdminServerAvailable(true);
               ConnectMonitorFactory.getConnectMonitor().removeConnectListener(this);
            }
         }
      }
   }
}
