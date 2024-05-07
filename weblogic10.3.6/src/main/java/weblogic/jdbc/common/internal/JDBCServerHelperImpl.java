package weblogic.jdbc.common.internal;

import java.security.AccessController;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilderFactory;
import weblogic.application.ApplicationAccess;
import weblogic.common.internal.PeerInfo;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.jdbc.JDBCLogger;
import weblogic.jdbc.rmi.SerialConnection;
import weblogic.jdbc.rmi.internal.ConnectionImpl;
import weblogic.jdbc.rmi.internal.RmiDriverSettings;
import weblogic.jdbc.wrapper.Connection;
import weblogic.jdbc.wrapper.JDBCWrapperFactory;
import weblogic.jndi.Alias;
import weblogic.kernel.KernelStatus;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.rmi.extensions.RemoteHelper;
import weblogic.rmi.extensions.server.RemoteDomainSecurityHelper;
import weblogic.rmi.spi.EndPoint;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.security.service.SecurityServiceManager;
import weblogic.t3.srvr.T3Srvr;

public class JDBCServerHelperImpl extends JDBCHelper {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final boolean remoteEnabled = new Boolean(System.getProperty("weblogic.jdbc.remoteEnabled", "true"));
   private static final UnsupportedOperationException remoteUnsupportedException = new UnsupportedOperationException("Remote JDBC disabled");
   private static ServerMBean localServerMBean = null;

   public Object interopReplace(Connection var1, PeerInfo var2) {
      if (!remoteEnabled) {
         throw remoteUnsupportedException;
      } else {
         RmiDataSource var3 = var1.getRMIDataSource();

         try {
            Properties var4;
            RmiDriverSettings var5;
            if (var3 != null) {
               var4 = var3.getDriverProperties();
               var5 = var3.getDriverSettings();
            } else {
               var4 = new Properties();
               var5 = new RmiDriverSettings();
            }

            ConnectionImpl var6 = (ConnectionImpl)JDBCWrapperFactory.getWrapper("weblogic.jdbc.rmi.internal.ConnectionImpl", var1, true);
            var6.init((java.sql.Connection)var1, var4, var5, var1.getPoolName());

            try {
               var6.addPeerGoneListener();
            } catch (Exception var8) {
               JDBCLogger.logStackTrace(var8);
            }

            return new SerialConnection((java.sql.Connection)var6);
         } catch (Exception var9) {
            JDBCLogger.logStackTrace(var9);
            return this;
         }
      }
   }

   public boolean getAutoConnectionClose() {
      if (KernelStatus.isServer()) {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         ServerMBean var2 = ManagementService.getRuntimeAccess(var1).getServer();
         String var3 = var2.getAutoJDBCConnectionClose();
         return !var3.equalsIgnoreCase("false");
      } else {
         return true;
      }
   }

   public String getDefaultURL() {
      return ChannelHelper.getDefaultURL();
   }

   public String getCurrentApplicationName() {
      return ApplicationAccess.getApplicationAccess().getCurrentApplicationName();
   }

   public String getDomainName() {
      return ManagementService.getRuntimeAccess(KERNEL_ID).getDomainName();
   }

   public String getServerName() {
      return ManagementService.getRuntimeAccess(KERNEL_ID).getServer().getName();
   }

   public boolean isServerShuttingDown() {
      int var1 = T3Srvr.getT3Srvr().getRunState();
      return var1 == 18 || var1 == 7;
   }

   public DocumentBuilderFactory getDocumentBuilderFactory() {
      return DocumentBuilderFactory.newInstance();
   }

   public Object createJNDIAlias(String var1, Object var2) {
      return new Alias(var1);
   }

   public boolean isProductionModeEnabled() {
      return ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().isProductionModeEnabled();
   }

   public String getXAMultiPoolName(JDBCDataSourceBean var1) {
      String var2 = null;
      JDBCSystemResourceMBean[] var3 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().getJDBCSystemResources();

      int var4;
      JDBCDataSourceBean var5;
      for(var4 = 0; var4 < var3.length; ++var4) {
         var5 = var3[var4].getJDBCResource();
         if (var5 != null) {
            String var6 = var5.getJDBCDataSourceParams().getDataSourceList();
            if (var6 != null && var6.contains(var1.getName())) {
               if (JDBCMBeanConverter.getLegacyType(var5) != 0) {
                  var2 = var5.getName();
                  break;
               }

               if (!var5.getJDBCDataSourceParams().getGlobalTransactionsProtocol().equals("None")) {
                  return var5.getName();
               }
            }
         }
      }

      if (var2 == null) {
         return null;
      } else {
         for(var4 = 0; var4 < var3.length; ++var4) {
            var5 = var3[var4].getJDBCResource();
            if (var5 != null && JDBCMBeanConverter.getLegacyType(var5) == 4 && var2.equals(JDBCMBeanConverter.getInternalProperty(var5, "LegacyPoolName"))) {
               return var2;
            }
         }

         return null;
      }
   }

   public List dsToList(String var1) {
      if (var1 == null) {
         return null;
      } else {
         StringTokenizer var2 = new StringTokenizer(var1, ",");
         int var3 = var2.countTokens();
         ArrayList var4 = new ArrayList();

         for(int var5 = 0; var5 < var3; ++var5) {
            var4.add(var2.nextToken());
         }

         return var4;
      }
   }

   public boolean isRACPool(String var1, String var2, String var3) {
      try {
         ConnectionEnv var4 = ConnectionPoolManager.reserve(var1, var2, var3, -2);
         DatabaseMetaData var5 = var4.conn.jconn.getMetaData();
         if (var5 != null) {
            String var6 = var5.getDatabaseProductVersion();
            boolean var7 = false;
            int var9 = var6.indexOf("Real Application Clusters");
            return var9 != -1;
         } else {
            return false;
         }
      } catch (Exception var8) {
         return false;
      }
   }

   public boolean isLLRPool(JDBCDataSourceBean var1) {
      return var1 != null && var1.getJDBCDataSourceParams().getGlobalTransactionsProtocol().equals("LoggingLastResource");
   }

   public boolean isLLRTablePerDataSource(String var1) {
      String var2 = "weblogic.llr.table." + var1;
      String var3 = System.getProperty(var2);
      return var3 != null;
   }

   public String getJDBCLLRTableName(String var1) {
      ServerMBean var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().lookupServer(var1);
      return var2.getJDBCLLRTableName();
   }

   public Boolean isUseFusionForLLR(String var1) {
      ServerMBean var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().lookupServer(var1);
      return var2.isUseFusionForLLR();
   }

   public int getJDBCLLRTableXIDColumnSize(String var1) {
      ServerMBean var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().lookupServer(var1);
      return var2.getJDBCLLRTableXIDColumnSize();
   }

   public int getJDBCLLRTablePoolColumnSize(String var1) {
      ServerMBean var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().lookupServer(var1);
      return var2.getJDBCLLRTablePoolColumnSize();
   }

   public int getJDBCLLRTableRecordColumnSize(String var1) {
      ServerMBean var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().lookupServer(var1);
      return var2.getJDBCLLRTableRecordColumnSize();
   }

   public boolean isJNDIEnabled() {
      return true;
   }

   public static boolean isExalogicOptimizationsEnabled() {
      return ManagementService.getRuntimeAccess(KERNEL_ID).getDomain().isExalogicOptimizationsEnabled();
   }

   public void addRMIContext(Object var1) throws Exception {
      if (rmiSecure()) {
         EndPoint var2 = RemoteHelper.getEndPoint(var1);
         if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
            JdbcDebug.JDBCRMI.debug("setting security context for remote endpoint " + var2);
         }

         AuthenticatedSubject var3 = null;
         var3 = RemoteDomainSecurityHelper.getSubject(var2);
         if (var3 == null) {
            byte var4 = KERNEL_ID.getQOS();
            if (var4 == 103) {
               if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
                  JdbcDebug.JDBCRMI.debug("Admin QOS present");
               }

               var3 = KERNEL_ID;
            } else if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("Not setting security context, no admin channel");
            }
         }

         if (var3 != null) {
            if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("setting security context " + var3);
            }

            SecurityManager.pushSubject(KERNEL_ID, var3);
         }
      }

   }

   public void removeRMIContext(Object var1) throws Exception {
      if (rmiSecure()) {
         AuthenticatedSubject var2 = SecurityServiceManager.getCurrentSubject(KERNEL_ID);
         if (SecurityServiceManager.isKernelIdentity(var2) || RemoteDomainSecurityHelper.getSubject(RemoteHelper.getEndPoint(var1)) != null) {
            if (JdbcDebug.JDBCRMI.isDebugEnabled()) {
               JdbcDebug.JDBCRMI.debug("removing subject " + var2);
            }

            SecurityManager.popSubject(KERNEL_ID);
         }
      }

   }

   public boolean isRMISecure() {
      return rmiSecure();
   }

   public static boolean rmiSecure() {
      ServerMBean var0 = getLocalServerMBean();
      return var0 != null && "Secure".equals(var0.getDataSource().getRmiJDBCSecurity());
   }

   private static ServerMBean getLocalServerMBean() {
      if (localServerMBean == null) {
         RuntimeAccess var0 = ManagementService.getRuntimeAccess(KERNEL_ID);
         if (var0 != null) {
            localServerMBean = var0.getServer();
         }
      }

      return localServerMBean;
   }
}
