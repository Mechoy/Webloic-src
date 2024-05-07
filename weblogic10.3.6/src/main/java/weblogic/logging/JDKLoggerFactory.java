package weblogic.logging;

import com.bea.logging.BaseLogRecord;
import com.bea.logging.BaseLogRecordFactory;
import com.bea.logging.BaseLogger;
import com.bea.logging.BaseLoggerFactory;
import com.bea.logging.DateFormatter;
import com.bea.logging.LogBufferHandler;
import com.bea.logging.LoggingService;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.BeanUpdateFailedException;
import weblogic.descriptor.BeanUpdateListener;
import weblogic.descriptor.BeanUpdateRejectedException;
import weblogic.i18n.logging.LogMessage;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.management.configuration.LogMBean;
import weblogic.management.configuration.ServerMBean;

public class JDKLoggerFactory implements BaseLoggerFactory, BaseLogRecordFactory, BeanUpdateListener {
   protected static final String KERNEL_LOGGER = "com.bea.weblogic.kernel";
   protected static final String BRIDGE_LOGGER = "com.oracle.wls";
   private static final String LOG4J_ADAPTER_FACTORY = "weblogic.logging.log4j.JDKLog4jAdapterFactory";
   private static boolean log4jEnabled = false;
   public static final String DOMAIN_LOGGER = "com.bea.weblogic.domain";
   private Logger bridgeLogger;

   public static JDKLoggerFactory getJDKLoggerFactory(LogMBean var0) {
      boolean var1 = var0.isLog4jLoggingEnabled();
      if (!var1) {
         return new JDKLoggerFactory();
      } else {
         try {
            Class var2 = Class.forName("weblogic.logging.log4j.JDKLog4jAdapterFactory", true, Thread.currentThread().getContextClassLoader());
            JDKLoggerFactory var3 = (JDKLoggerFactory)var2.newInstance();
            log4jEnabled = true;
            return var3;
         } catch (Throwable var4) {
            LogMgmtLogger.logErrorInitializingLog4jLogging(var0.getName(), var4);
            return new JDKLoggerFactory();
         }
      }
   }

   public static boolean isLog4jEnabled() {
      return log4jEnabled;
   }

   public BaseLogger createBaseLogger(String var1) {
      WLLogger var2 = new WLLogger(var1);
      var2.setUseParentHandlers(true);
      return var2;
   }

   public BaseLogRecord createBaseLogRecord(LogMessage var1) {
      return new WLLogRecord(var1);
   }

   public Logger createAndInitializeDomainLogger(LogMBean var1) throws IOException {
      WLLogger var2 = new WLLogger("com.bea.weblogic.domain", true);
      var2.setLevel(WLLevel.getLevel(Severities.severityStringToNum(var1.getLoggerSeverity())));
      FileStreamHandler var3 = new FileStreamHandler(var1);
      LogFileFormatter var4 = new LogFileFormatter();
      DateFormatter var5 = new DateFormatter(var1.getDateFormatPattern());
      var4.setDateFormatter(var5);
      var3.setFormatter(var4);
      var3.setErrorManager(new WLErrorManager(var3));
      var2.setUseParentHandlers(false);
      var2.setLevel(Level.ALL);
      var2.addHandler(var3);
      LogMgmtLogger.logInitializedDomainLogFile(var1.computeLogFilePath());
      return var2;
   }

   public Logger createAndInitializeServerLogger(ServerMBean var1) {
      LogMBean var2 = var1.getLog();
      LoggingService var3 = LoggingService.getInstance();
      var3.setBaseLoggerFactory(this);
      var3.setBaseLogRecordFactory(this);
      this.updateLoggerSeverities(var1.getLog());
      var1.getLog().addBeanUpdateListener(this);
      SeverityChangeListener var4 = null;
      ConsoleHandler var5 = new ConsoleHandler(var1.getLog());
      var5.setFormatter(new ConsoleFormatter(var1.getLog()));
      DateFormatter var6 = ((ConsoleFormatter)var5.getFormatter()).getDateFormatter();
      var4 = new SeverityChangeListener(var2, "StdoutSeverity", var5);
      var4.setLevel(var2.getStdoutSeverity());
      var5.setFilter(new LogFilter(var2, "StdoutFilter", var2.getStdoutFilter()));
      Logger var7 = var3.getLogger("");
      var7.addHandler(var5);

      try {
         FileStreamHandler var8 = new FileStreamHandler(var2);
         LogBufferHandler.getInstance().dumpLogBuffer(var8.getRotatingFileOutputStream(), new LogFileFormatter());
         LogFileFormatter var9 = new LogFileFormatter();
         var9.setDateFormatter(var6);
         var8.setFormatter(var9);
         var4 = new SeverityChangeListener(var2, "LogFileSeverity", var8);
         var4.setLevel(var2.getLogFileSeverity());
         var8.setFilter(new LogFilter(var2, "LogFileFilter", var2.getLogFileFilter()));
         var7.addHandler(var8);
         MessageLoggerRegistry.registerMessageLogger("", var3);
         LogMgmtLogger.logServerLogFileOpened(var8.toString());
      } catch (IOException var12) {
         LogMgmtLogger.logErrorOpeningLogFile(var2.computeLogFilePath());
      }

      LogBufferHandler var13 = LogBufferHandler.getInstance();
      var4 = new SeverityChangeListener(var2, "MemoryBufferSeverity", var13);
      var4.setLevel(var2.getMemoryBufferSeverity());
      var13.setFilter(new LogFilter(var2, "MemoryBufferFilter", var2.getMemoryBufferFilter()));
      DomainLogBroadcastHandler var14 = new DomainLogBroadcastHandler();
      var4 = new SeverityChangeListener(var2, "DomainLogBroadcastSeverity", var14);
      var4.setLevel(var2.getDomainLogBroadcastSeverity());
      var14.setFilter(new LogFilter(var2, "DomainLogBroadcastFilter", var2.getDomainLogBroadcastFilter()));
      var7.addHandler(var14);

      try {
         var7.addHandler(new JMXBroadcastHandler());
      } catch (Exception var11) {
      }

      this.initializeBridgeHandler(var2);
      var3.stopUsingPrimordialLogger();
      LogMgmtLogger.logDefaultServerLoggingInitialized();
      return var7;
   }

   protected void initializeBridgeHandler(final LogMBean var1) {
      this.registerBridgeHandler(var1);
      LogManager.getLogManager().addPropertyChangeListener(new PropertyChangeListener() {
         public void propertyChange(PropertyChangeEvent var1x) {
            JDKLoggerFactory.this.registerBridgeHandler(var1);
         }
      });
   }

   protected void registerBridgeHandler(LogMBean var1) {
      this.bridgeLogger = Logger.getLogger("com.oracle.wls");
      Handler[] var2 = this.bridgeLogger.getHandlers();
      if (var2 != null) {
         Handler[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Handler var6 = var3[var5];
            if (var6.getClass().getName().equals(ServerLoggingHandler.class.getName())) {
               return;
            }
         }
      }

      this.bridgeLogger.setUseParentHandlers(var1.isServerLoggingBridgeUseParentLoggersEnabled());
      this.bridgeLogger.addHandler(new ServerLoggingHandler());
   }

   public void activateUpdate(BeanUpdateEvent var1) throws BeanUpdateFailedException {
      LogMBean var2 = (LogMBean)var1.getSourceBean();
      this.updateLoggerSeverities(var2);
   }

   public void prepareUpdate(BeanUpdateEvent var1) throws BeanUpdateRejectedException {
   }

   public void rollbackUpdate(BeanUpdateEvent var1) {
   }

   protected void updateLoggerSeverities(LogMBean var1) {
      LoggingService var2 = LoggingService.getInstance();
      Logger var3 = var2.getLogger("");
      var3.setLevel(WLLevel.getLevel(Severities.severityStringToNum(var1.getLoggerSeverity())));
      Properties var4 = var1.getLoggerSeverityProperties();
      LoggingService.getInstance().setLoggerSeverities(var4);
   }
}
