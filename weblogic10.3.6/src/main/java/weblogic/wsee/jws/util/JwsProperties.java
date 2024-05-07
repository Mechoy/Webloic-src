package weblogic.wsee.jws.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import weblogic.management.DomainDir;

public class JwsProperties {
   public static final String TESTCONSOLE_PROPNAME = "wlw.testConsole";
   public static final String ITERATIVEDEV_PROPNAME = "wlw.iterativeDev";
   public static final String LOG_ERRORS_TO_CONSOLE_PROPNAME = "wlw.logErrorsToConsole";
   public static final String CONFIG_FILENAME = "jws-config.properties";
   protected static Properties _systemProps = System.getProperties();
   protected static Properties _fileProps;
   protected static Properties _defaultProps;
   protected static Boolean _insertOnCreate;
   public static final String DebugTransactionTimeout = "weblogic.jws.DebugTransactionTimeout";
   public static final String DebugTransactionTimeout_default = "43200";
   public static final String CgDataBlobSize = "weblogic.jws.cgdata.size";
   public static final String CgDataBlobSize_Default = "1M";
   public static final String InternalJMSServer = "weblogic.jws.InternalJMSServer";
   public static final String InternalJMSServer_default = "cgJMSServer";
   public static final String InternalJMSConnFactory = "weblogic.jws.InternalJMSConnFactory";
   public static final String InternalJMSConnFactory_default = "weblogic.jws.jms.QueueConnectionFactory";
   public static final String ConversationDataSource = "weblogic.jws.ConversationDataSource";
   public static final String ConversationDataSource_default = "cgDataSource";
   public static final String JMSControlDataSource = "weblogic.jws.JMSControlDataSource";
   public static final String JMSControlDataSource_default = "cgDataSource";
   public static final String ConversationMaxKeyLength = "weblogic.jws.ConversationMaxKeyLength";
   public static String ConversationMaxKeyLength_default = "768";
   public static final String SybaseConversationMaxKeyLength_default = "300";
   public static final String MSSqlServerConversationMaxKeyLength_default = "550";
   public static final String DB2ConversationMaxKeyLength_default = "600";
   public static final String InsertOnCreate = "weblogic.jws.InsertOnCreate";
   public static final String InsertOnCreate_default = "false";

   protected static void setPropertiesFromConfigFile(File var0) {
      _defaultProps = new Properties();
      _defaultProps.setProperty("weblogic.jws.InternalJMSServer", "cgJMSServer");
      _defaultProps.setProperty("weblogic.jws.InternalJMSConnFactory", "weblogic.jws.jms.QueueConnectionFactory");
      _defaultProps.setProperty("weblogic.jws.ConversationDataSource", "cgDataSource");
      _defaultProps.setProperty("weblogic.jws.cgdata.size", "1M");
      _defaultProps.setProperty("weblogic.jws.JMSControlDataSource", "cgDataSource");
      _defaultProps.setProperty("weblogic.jws.DebugTransactionTimeout", "43200");
      _defaultProps.setProperty("weblogic.jws.InsertOnCreate", "false");
      _fileProps = new Properties(_defaultProps);

      try {
         File var1 = new File(var0, "jws-config.properties");
         loadProps(_fileProps, var1);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   protected static void loadProps(Properties var0, File var1) throws IOException {
      if (var1.exists()) {
         FileInputStream var2 = null;

         try {
            var2 = new FileInputStream(var1);
            var0.load(var2);
         } finally {
            if (var2 != null) {
               var2.close();
            }

         }
      }

   }

   public static String getProperty(String var0) {
      String var1 = getSystemProperty(var0);
      if (var1 == null) {
         var1 = getJwsProperty(var0);
      }

      return var1;
   }

   protected static String getSystemProperty(String var0) {
      return _systemProps.getProperty(var0);
   }

   protected static String getJwsProperty(String var0) {
      return _fileProps != null ? _fileProps.getProperty(var0) : _defaultProps.getProperty(var0);
   }

   public static Boolean getPropertyAsBoolean(String var0) {
      return Boolean.valueOf(getProperty(var0));
   }

   public static boolean iterativeDevDisabled() {
      boolean var0 = false;
      String var1 = getProperty("wlw.iterativeDev");
      if (var1 != null && var1.equalsIgnoreCase("true")) {
         var0 = true;
      }

      return !var0;
   }

   public static String getStackTraceForMessages(Throwable var0) {
      StringWriter var1 = new StringWriter();
      if (iterativeDevDisabled()) {
         var1.write(var0.getMessage());
      } else {
         var0.printStackTrace(new PrintWriter(var1));
      }

      return var1.toString();
   }

   public static boolean testConsoleDisabled() {
      boolean var0 = false;
      String var1 = getProperty("wlw.testConsole");
      if (var1 != null && var1.equalsIgnoreCase("true")) {
         var0 = true;
      }

      return !var0;
   }

   public static synchronized boolean getInsertOnCreate() {
      if (_insertOnCreate == null) {
         _insertOnCreate = getPropertyAsBoolean("weblogic.jws.InsertOnCreate");
      }

      return _insertOnCreate;
   }

   static {
      setPropertiesFromConfigFile(new File(DomainDir.getRootDir()));
   }
}
