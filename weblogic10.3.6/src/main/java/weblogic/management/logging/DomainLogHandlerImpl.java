package weblogic.management.logging;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.List;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.diagnostics.snmp.agent.SNMPTrapException;
import weblogic.diagnostics.snmp.agent.SNMPTrapSender;
import weblogic.diagnostics.snmp.agent.SNMPTrapUtil;
import weblogic.diagnostics.snmp.server.ALSBTrapUtil;
import weblogic.i18n.logging.LoggingTextFormatter;
import weblogic.jndi.Environment;
import weblogic.logging.JDKLoggerFactory;
import weblogic.logging.LogEntry;
import weblogic.logging.LogMgmtLogger;
import weblogic.logging.WLLevel;
import weblogic.logging.WLLogRecord;
import weblogic.management.configuration.LogMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class DomainLogHandlerImpl implements DomainLogHandler {
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugDomainLogHandler");
   private static DomainLogHandler singleton = null;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static RuntimeAccess runtimeAccess;
   private static LoggingTextFormatter loggingTextFmt;
   private static boolean loggedSNMPMessage;
   private Logger domainLogger;
   private LogMBean log = null;

   public static synchronized DomainLogHandler getInstance() throws DomainLogHandlerException {
      if (!runtimeAccess.isAdminServer()) {
         throw new DomainLogHandlerException(loggingTextFmt.getDomainLoggerDoesNotExistMsg());
      } else {
         if (singleton == null) {
            try {
               singleton = new DomainLogHandlerImpl();
            } catch (NamingException var1) {
               throw new DomainLogHandlerException(loggingTextFmt.getDomainLoggerDoesNotExistMsg(), var1);
            }
         }

         return singleton;
      }
   }

   private DomainLogHandlerImpl() throws NamingException {
      Environment var1 = new Environment();
      var1.setReplicateBindings(false);
      var1.setCreateIntermediateContexts(true);
      Context var2 = var1.getInitialContext();
      var2.bind("weblogic.logging.DomainLogHandler", this);
      this.log = runtimeAccess.getDomain().getLog();
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Found Domain's LogConfig " + this.log.getObjectName());
      }

      this.initializeDomainLogger();
   }

   public Logger getDomainLogger() {
      return this.domainLogger;
   }

   private void initializeDomainLogger() {
      JDKLoggerFactory var1 = JDKLoggerFactory.getJDKLoggerFactory(this.log);

      try {
         this.domainLogger = var1.createAndInitializeDomainLogger(this.log);
      } catch (IOException var3) {
         LogMgmtLogger.logCannotOpenDomainLogfile(this.log.computeLogFilePath(), var3);
         this.domainLogger = null;
      }

   }

   public void publishLogEntries(LogEntry[] var1) {
      if (var1 != null) {
         WLLogRecord var2 = null;

         for(int var3 = 0; var3 < var1.length; ++var3) {
            LogEntry var4 = var1[var3];
            var2 = new WLLogRecord(WLLevel.getLevel(var4.getSeverity()), var4.getLogMessage());
            var2.setId(var4.getId());
            var2.setLoggerName(var4.getSubsystem());
            var2.setMillis(var4.getTimestamp());
            var2.setParameters(new Object[0]);
            var2.setThrowableWrapper(var4.getThrowableWrapper());
            var2.setMachineName(var4.getMachineName());
            var2.setServerName(var4.getServerName());
            var2.setThreadName(var4.getThreadName());
            var2.setUserId(var4.getUserId());
            var2.setTransactionId(var4.getTransactionId());
            var2.setDiagnosticContextId(var4.getDiagnosticContextId());
            if (DEBUG_LOGGER.isDebugEnabled()) {
               DEBUG_LOGGER.debug("Logging message to domain log " + var2.getLogMessage());
            }

            this.domainLogger.log(var2);
         }

      }
   }

   public void sendTrap(String var1, List<Object[]> var2) throws RemoteException {
      try {
         SNMPTrapSender var3 = SNMPTrapUtil.getInstance().getSNMPTrapSender();
         if (var3 != null) {
            var3.sendTrap(var1, var2);
         }

      } catch (SNMPTrapException var4) {
         throw new RemoteException(var4.getMessage());
      }
   }

   public void sendALAlertTrap(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12) throws RemoteException {
      try {
         ALSBTrapUtil.sendALSBAlert(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
      } catch (Exception var14) {
         if (DEBUG_LOGGER.isDebugEnabled()) {
            DEBUG_LOGGER.debug("Error sending trap to admin server", var14);
         }

         throw new RemoteException(var14.getMessage());
      }
   }

   public void ping() {
   }

   static {
      runtimeAccess = ManagementService.getRuntimeAccess(kernelId);
      loggingTextFmt = LoggingTextFormatter.getInstance();
      loggedSNMPMessage = false;
   }
}
