package weblogic.servlet;

import javax.management.ObjectName;
import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class HTTPLogger {
   private static final String LOCALIZER_CLASS = "weblogic.servlet.HTTPLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(HTTPLogger.class.getName());
   }

   public static String logUnableToDeserializeAttribute(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101002", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101002";
   }

   public static Loggable logUnableToDeserializeAttributeLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101002", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNullDocRoot(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101003", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101003";
   }

   public static Loggable logNullDocRootLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101003", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnsafePath(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101005", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101005";
   }

   public static Loggable logUnsafePathLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101005", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnableToGetStream(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101008", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101008";
   }

   public static Loggable logUnableToGetStreamLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101008", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logRootCause(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101017", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101017";
   }

   public static Loggable logRootCauseLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101017", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logIOException(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101019", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101019";
   }

   public static Loggable logIOExceptionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101019", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logException(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101020", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101020";
   }

   public static Loggable logExceptionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101020", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnsupportedErrorCode(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101024", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101024";
   }

   public static Loggable logUnsupportedErrorCodeLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101024", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNullDocumentRoot(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101025", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101025";
   }

   public static Loggable logNullDocumentRootLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101025", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNoDocRoot(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101027", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101027";
   }

   public static Loggable logNoDocRootLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101027", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnableToMakeDirectory(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101029", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101029";
   }

   public static Loggable logUnableToMakeDirectoryLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101029", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logReloadCheckSecondsError(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101040", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101040";
   }

   public static Loggable logReloadCheckSecondsErrorLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101040", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCompareVersion(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101041", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101041";
   }

   public static Loggable logCompareVersionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101041", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101045", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101045";
   }

   public static Loggable logErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101045", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logErrorWithThrowable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101046", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101046";
   }

   public static Loggable logErrorWithThrowableLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101046", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInfo(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101047", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101047";
   }

   public static Loggable logInfoLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101047", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logStarted(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101051", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101051";
   }

   public static Loggable logStartedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101051", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInit(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101052", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101052";
   }

   public static Loggable logInitLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101052", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logLoadingWebApp(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101053", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101053";
   }

   public static Loggable logLoadingWebAppLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101053", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logSetContext(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101054", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101054";
   }

   public static Loggable logSetContextLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101054", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logLoadingFromWAR(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101059", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101059";
   }

   public static Loggable logLoadingFromWARLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101059", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logLoadingFromDir(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101060", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101060";
   }

   public static Loggable logLoadingFromDirLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101060", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnableToFindWebApp(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101061", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101061";
   }

   public static Loggable logUnableToFindWebAppLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101061", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logErrorReadingWebApp(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101062", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101062";
   }

   public static Loggable logErrorReadingWebAppLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101062", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logLoadError(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101064", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101064";
   }

   public static Loggable logLoadErrorLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101064", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNoJNDIContext(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101066", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101066";
   }

   public static Loggable logNoJNDIContextLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101066", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNullURL(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101067", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101067";
   }

   public static Loggable logNullURLLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101067", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logURLParseError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101068", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101068";
   }

   public static Loggable logURLParseErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101068", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnableToBindURL(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101069", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101069";
   }

   public static Loggable logUnableToBindURLLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101069", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logBoundURL(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101070", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101070";
   }

   public static Loggable logBoundURLLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101070", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logShutdown(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101075", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101075";
   }

   public static Loggable logShutdownLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101075", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logConnectionFailure(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101083", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101083";
   }

   public static Loggable logConnectionFailureLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101083", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNoHostInHeader() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101086", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101086";
   }

   public static Loggable logNoHostInHeaderLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101086", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logHostNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101087", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101087";
   }

   public static Loggable logHostNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101087", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDispatchRequest(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101088", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101088";
   }

   public static Loggable logDispatchRequestLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101088", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDispatchError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101093", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101093";
   }

   public static Loggable logDispatchErrorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101093", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logPOSTTimeExceeded(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101095", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101095";
   }

   public static Loggable logPOSTTimeExceededLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("101095", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logPOSTSizeExceeded(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101096", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101096";
   }

   public static Loggable logPOSTSizeExceededLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("101096", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logBadCookieHeader(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101100", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101100";
   }

   public static Loggable logBadCookieHeaderLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101100", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logSessionCreateError(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101101", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101101";
   }

   public static Loggable logSessionCreateErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101101", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101104", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101104";
   }

   public static Loggable logServletFailedLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101104", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNoLocation(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101105", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101105";
   }

   public static Loggable logNoLocationLoggable(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return new Loggable("101105", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnableToServeErrorPage(String var0, String var1, int var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, new Integer(var2), var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101106", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101106";
   }

   public static Loggable logUnableToServeErrorPageLoggable(String var0, String var1, int var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, new Integer(var2), var3};
      return new Loggable("101106", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logSendError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101107", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101107";
   }

   public static Loggable logSendErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101107", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnsupportedEncoding(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101108", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101108";
   }

   public static Loggable logUnsupportedEncodingLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101108", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logPermUnavailable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101122", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101122";
   }

   public static Loggable logPermUnavailableLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101122", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logTimeUnavailable(String var0, String var1, long var2) {
      Object[] var4 = new Object[]{var0, var1, new Long(var2)};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101123", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101123";
   }

   public static Loggable logTimeUnavailableLoggable(String var0, String var1, long var2) {
      Object[] var4 = new Object[]{var0, var1, new Long(var2)};
      return new Loggable("101123", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInstantiateError(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101125", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101125";
   }

   public static Loggable logInstantiateErrorLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101125", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCastingError(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101126", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101126";
   }

   public static Loggable logCastingErrorLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101126", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logHTTPInit() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101128", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101128";
   }

   public static Loggable logHTTPInitLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101128", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logWebInit() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101129", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101129";
   }

   public static Loggable logWebInitLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101129", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInitWeb(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101133", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101133";
   }

   public static Loggable logInitWebLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101133", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDefaultName(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101135", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101135";
   }

   public static Loggable logDefaultNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101135", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logRegisterVirtualHost(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101136", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101136";
   }

   public static Loggable logRegisterVirtualHostLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101136", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInvalidGetParameterInvocation(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101138", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101138";
   }

   public static Loggable logInvalidGetParameterInvocationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101138", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logErrorCreatingServletStub(String var0, String var1, String var2, Object var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101140", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101140";
   }

   public static Loggable logErrorCreatingServletStubLoggable(String var0, String var1, String var2, Object var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("101140", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logErrorUnregisteringServletRuntime(ObjectName var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101142", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101142";
   }

   public static Loggable logErrorUnregisteringServletRuntimeLoggable(ObjectName var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101142", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logErrorUnregisteringWebAppRuntime(ObjectName var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101143", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101143";
   }

   public static Loggable logErrorUnregisteringWebAppRuntimeLoggable(ObjectName var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101143", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNoContext(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101147", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101147";
   }

   public static Loggable logNoContextLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101147", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logIllegalAccessOnInstantiate(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101159", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101159";
   }

   public static Loggable logIllegalAccessOnInstantiateLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101159", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logMalformedWebDescriptor(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101160", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101160";
   }

   public static Loggable logMalformedWebDescriptorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101160", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logListenerFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101162", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101162";
   }

   public static Loggable logListenerFailedLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101162", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCouldNotLoadListener(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101163", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101163";
   }

   public static Loggable logCouldNotLoadListenerLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101163", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNotAListener(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101164", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101164";
   }

   public static Loggable logNotAListenerLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101164", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCouldNotLoadFilter(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101165", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101165";
   }

   public static Loggable logCouldNotLoadFilterLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101165", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logBadSecurityRoleInSRA(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101168", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101168";
   }

   public static Loggable logBadSecurityRoleInSRALoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101168", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101169", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101169";
   }

   public static Loggable logServletNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101169", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletNotFoundForPattern(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101170", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101170";
   }

   public static Loggable logServletNotFoundForPatternLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101170", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNoResourceRefs() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101171", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101171";
   }

   public static Loggable logNoResourceRefsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101171", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNoEjbRefs() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101172", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101172";
   }

   public static Loggable logNoEjbRefsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101172", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logResourceRefNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101173", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101173";
   }

   public static Loggable logResourceRefNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101173", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logEjbRefNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101174", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101174";
   }

   public static Loggable logEjbRefNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101174", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logZipCloseError(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101175", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101175";
   }

   public static Loggable logZipCloseErrorLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101175", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logContextNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101176", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101176";
   }

   public static Loggable logContextNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101176", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logBadSecurityRoleInAC(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101180", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101180";
   }

   public static Loggable logBadSecurityRoleInACLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101180", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInvalidExceptionType(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101188", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101188";
   }

   public static Loggable logInvalidExceptionTypeLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101188", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCouldNotLoadUrlMatchMapClass(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101189", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101189";
   }

   public static Loggable logCouldNotLoadUrlMatchMapClassLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101189", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logPreprocessorNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101194", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101194";
   }

   public static Loggable logPreprocessorNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101194", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logListenerParseException(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101196", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101196";
   }

   public static Loggable logListenerParseExceptionLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101196", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCouldNotDeployRole(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101198", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101198";
   }

   public static Loggable logCouldNotDeployRoleLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101198", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCouldNotDeployPolicy(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101199", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101199";
   }

   public static Loggable logCouldNotDeployPolicyLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101199", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNotCachingTheResponse(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101200", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101200";
   }

   public static Loggable logNotCachingTheResponseLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101200", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logLoadingDescriptors(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101201", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101201";
   }

   public static Loggable logLoadingDescriptorsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101201", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logPreparing(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101202", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101202";
   }

   public static Loggable logPreparingLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101202", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logRollingBack(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101205", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101205";
   }

   public static Loggable logRollingBackLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101205", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logActivating(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101206", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101206";
   }

   public static Loggable logActivatingLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101206", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDeactivating(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101207", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101207";
   }

   public static Loggable logDeactivatingLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101207", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logStarting(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101208", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101208";
   }

   public static Loggable logStartingLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101208", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logReady(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101209", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101209";
   }

   public static Loggable logReadyLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101209", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logPrecompilingJSPs(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101211", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101211";
   }

   public static Loggable logPrecompilingJSPsLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101211", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailureCompilingJSPs(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101212", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101212";
   }

   public static Loggable logFailureCompilingJSPsLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101212", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logBindingResourceReference(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101213", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101213";
   }

   public static Loggable logBindingResourceReferenceLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101213", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logIncludedFileNotFound(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101214", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101214";
   }

   public static Loggable logIncludedFileNotFoundLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101214", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logMalformedRequest(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101215", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101215";
   }

   public static Loggable logMalformedRequestLoggable(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      return new Loggable("101215", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletFailedToPreloadOnStartup(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101216", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101216";
   }

   public static Loggable logServletFailedToPreloadOnStartupLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101216", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logContextAlreadyRegistered(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101217", var6, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101217";
   }

   public static Loggable logContextAlreadyRegisteredLoggable(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      return new Loggable("101217", var6, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNeedServletClassOrJspFile(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101218", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101218";
   }

   public static Loggable logNeedServletClassOrJspFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101218", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logErrorSettingDocumentRoot(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101220", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101220";
   }

   public static Loggable logErrorSettingDocumentRootLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101220", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletNameIsNull(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101221", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101221";
   }

   public static Loggable logServletNameIsNullLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101221", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServerSuspended(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101223", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101223";
   }

   public static Loggable logServerSuspendedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101223", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logAuthFilterInvocationFailed(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101226", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101226";
   }

   public static Loggable logAuthFilterInvocationFailedLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101226", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToUndeploySecurityPolicy(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101228", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101228";
   }

   public static Loggable logFailedToUndeploySecurityPolicyLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101228", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToUndeploySecurityRole(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101229", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101229";
   }

   public static Loggable logFailedToUndeploySecurityRoleLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101229", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logELFLogNotFormattedProperly() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101231", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101231";
   }

   public static Loggable logELFLogNotFormattedProperlyLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101231", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logELFReadHeadersException(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101232", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101232";
   }

   public static Loggable logELFReadHeadersExceptionLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101232", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logELFApplicationFieldFailure(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101234", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101234";
   }

   public static Loggable logELFApplicationFieldFailureLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101234", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logELFApplicationFieldFailureCCE(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101235", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101235";
   }

   public static Loggable logELFApplicationFieldFailureCCELoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101235", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logELFApplicationFieldFormatError(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101236", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101236";
   }

   public static Loggable logELFApplicationFieldFormatErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101236", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logHttpLoggingDisabled(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101237", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101237";
   }

   public static Loggable logHttpLoggingDisabledLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101237", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToRollLogFile(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101242", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101242";
   }

   public static Loggable logFailedToRollLogFileLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101242", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logOldPublicIDWarningWithContext(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101247", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101247";
   }

   public static Loggable logOldPublicIDWarningWithContextLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101247", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logMalformedDescriptorCtx(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101248", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101248";
   }

   public static Loggable logMalformedDescriptorCtxLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("101248", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletClassNotFound(String var0, String var1, String var2, String var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101249", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101249";
   }

   public static Loggable logServletClassNotFoundLoggable(String var0, String var1, String var2, String var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("101249", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletClassDefNotFound(String var0, String var1, String var2, String var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101250", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101250";
   }

   public static Loggable logServletClassDefNotFoundLoggable(String var0, String var1, String var2, String var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("101250", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletUnsatisfiedLink(String var0, String var1, String var2, String var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101251", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101251";
   }

   public static Loggable logServletUnsatisfiedLinkLoggable(String var0, String var1, String var2, String var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("101251", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletVerifyError(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101252", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101252";
   }

   public static Loggable logServletVerifyErrorLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101252", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletClassFormatError(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101253", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101253";
   }

   public static Loggable logServletClassFormatErrorLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101253", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletLinkageError(String var0, String var1, String var2, String var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101254", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101254";
   }

   public static Loggable logServletLinkageErrorLoggable(String var0, String var1, String var2, String var3, Throwable var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("101254", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logRunAsUserCouldNotBeResolved(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101256", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101256";
   }

   public static Loggable logRunAsUserCouldNotBeResolvedLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101256", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logIgnoringClientCert(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101257", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101257";
   }

   public static Loggable logIgnoringClientCertLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101257", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInvalidJspParamName(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101258", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101258";
   }

   public static Loggable logInvalidJspParamNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101258", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logEmptyJspParamName() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101260", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101260";
   }

   public static Loggable logEmptyJspParamNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101260", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInvalidSessionParamName(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101261", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101261";
   }

   public static Loggable logInvalidSessionParamNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101261", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDeprecatedSessionParamName(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101262", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101262";
   }

   public static Loggable logDeprecatedSessionParamNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101262", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logEmptySessionParamName() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101263", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101263";
   }

   public static Loggable logEmptySessionParamNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101263", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCertAuthenticationError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101264", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101264";
   }

   public static Loggable logCertAuthenticationErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101264", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedWhileDestroyingFilter(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101267", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101267";
   }

   public static Loggable logFailedWhileDestroyingFilterLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101267", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServletFailedOnDestroy(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101268", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101268";
   }

   public static Loggable logServletFailedOnDestroyLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101268", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUrlPatternMissingFromWebResource(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101271", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101271";
   }

   public static Loggable logUrlPatternMissingFromWebResourceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101271", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInitialSessionsDuringSuspend(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101275", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101275";
   }

   public static Loggable logInitialSessionsDuringSuspendLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("101275", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logSessionsDuringSuspend(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101276", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101276";
   }

   public static Loggable logSessionsDuringSuspendLoggable(int var0, String var1) {
      Object[] var2 = new Object[]{new Integer(var0), var1};
      return new Loggable("101276", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logSessionListDuringSuspend(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101277", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101277";
   }

   public static Loggable logSessionListDuringSuspendLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101277", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logPrepareToSuspendComplete() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101278", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101278";
   }

   public static Loggable logPrepareToSuspendCompleteLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101278", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logRemoving(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101279", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101279";
   }

   public static Loggable logRemovingLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101279", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToCreateWebServerRuntimeMBean(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101280", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101280";
   }

   public static Loggable logFailedToCreateWebServerRuntimeMBeanLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101280", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInvalidJspServlet(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101282", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101282";
   }

   public static Loggable logInvalidJspServletLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101282", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnableToLoadJspServletClass(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101283", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101283";
   }

   public static Loggable logUnableToLoadJspServletClassLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101283", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNPEDuringServletDestroy(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101287", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101287";
   }

   public static Loggable logNPEDuringServletDestroyLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101287", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToSetContextPath(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101288", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101288";
   }

   public static Loggable logFailedToSetContextPathLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101288", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToBounceClassLoader(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101291", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101291";
   }

   public static Loggable logFailedToBounceClassLoaderLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101291", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDescriptorValidationFailure(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101292", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101292";
   }

   public static Loggable logDescriptorValidationFailureLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101292", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logServerVersionMismatchForJSPisStale(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101295", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101295";
   }

   public static Loggable logServerVersionMismatchForJSPisStaleLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101295", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUnableToLoadDefaultCompilerClass(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101296", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101296";
   }

   public static Loggable logUnableToLoadDefaultCompilerClassLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101296", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInvalidIndexDirectorySortBy(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101297", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101297";
   }

   public static Loggable logInvalidIndexDirectorySortByLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101297", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFoundStarJspUrlPattern(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101299", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101299";
   }

   public static Loggable logFoundStarJspUrlPatternLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101299", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logImplicitMappingForRunAsRole(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101302", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101302";
   }

   public static Loggable logImplicitMappingForRunAsRoleLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("101302", var5, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logImplicitMappingForRunAsRoleToSelf(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101303", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101303";
   }

   public static Loggable logImplicitMappingForRunAsRoleToSelfLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101303", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCreatingImplicitMapForRoles(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101304", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101304";
   }

   public static Loggable logCreatingImplicitMapForRolesLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101304", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logBadErrorPage(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101305", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101305";
   }

   public static Loggable logBadErrorPageLoggable(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return new Loggable("101305", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logCouldNotResolveServletEntity(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101306", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101306";
   }

   public static Loggable logCouldNotResolveServletEntityLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101306", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logErrorResolvingServletEntity(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101307", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101307";
   }

   public static Loggable logErrorResolvingServletEntityLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101307", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDebug(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101308", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101308";
   }

   public static Loggable logDebugLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101308", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToPerformBatchedLATUpdate(String var0, String var1, String var2, int var3, int var4, Throwable var5) {
      Object[] var6 = new Object[]{var0, var1, var2, new Integer(var3), new Integer(var4), var5};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101310", var6, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101310";
   }

   public static Loggable logFailedToPerformBatchedLATUpdateLoggable(String var0, String var1, String var2, int var3, int var4, Throwable var5) {
      Object[] var6 = new Object[]{var0, var1, var2, new Integer(var3), new Integer(var4), var5};
      return new Loggable("101310", var6, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDigestAuthNotSupported(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101317", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101317";
   }

   public static Loggable logDigestAuthNotSupportedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101317", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInvalidAuthMethod(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101318", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101318";
   }

   public static Loggable logInvalidAuthMethodLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101318", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logLoginOrErrorPageMissing(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101319", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101319";
   }

   public static Loggable logLoginOrErrorPageMissingLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101319", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInternalAppWrongPort(String var0, int var1, String var2, String var3, int var4, String var5, String var6) {
      Object[] var7 = new Object[]{var0, new Integer(var1), var2, var3, new Integer(var4), var5, var6};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101320", var7, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101320";
   }

   public static Loggable logInternalAppWrongPortLoggable(String var0, int var1, String var2, String var3, int var4, String var5, String var6) {
      Object[] var7 = new Object[]{var0, new Integer(var1), var2, var3, new Integer(var4), var5, var6};
      return new Loggable("101320", var7, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logMuxableSocketResetException(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101323", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101323";
   }

   public static Loggable logMuxableSocketResetExceptionLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101323", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInvalidVirtualDirectoryPath(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101325", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101325";
   }

   public static Loggable logInvalidVirtualDirectoryPathLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101325", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logUndefinedSecurityRole(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101326", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101326";
   }

   public static Loggable logUndefinedSecurityRoleLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101326", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logMultipleOccurrencesNotAllowed(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101327", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101327";
   }

   public static Loggable logMultipleOccurrencesNotAllowedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101327", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInvalidFilterDispatcher(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101328", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101328";
   }

   public static Loggable logInvalidFilterDispatcherLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101328", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logNonVersionedContextAlreadyRegistered(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101331", var6, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101331";
   }

   public static Loggable logNonVersionedContextAlreadyRegisteredLoggable(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      return new Loggable("101331", var6, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToRegisterPolicyContextHandlers(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101332", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101332";
   }

   public static Loggable logFailedToRegisterPolicyContextHandlersLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101332", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logSecurityException(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101337", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101337";
   }

   public static Loggable logSecurityExceptionLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101337", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDeprecatedContextParamDefaultServlet() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101338", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101338";
   }

   public static Loggable logDeprecatedContextParamDefaultServletLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101338", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDeprecatedContextParam(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101339", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101339";
   }

   public static Loggable logDeprecatedContextParamLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101339", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDeprecatedContextParamClasspath() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101340", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101340";
   }

   public static Loggable logDeprecatedContextParamClasspathLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101340", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logJSPClassUptodate(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101341", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101341";
   }

   public static Loggable logJSPClassUptodateLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101341", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logJSPPrecompileErrors(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101342", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101342";
   }

   public static Loggable logJSPPrecompileErrorsLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101342", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logPrecompilingStaleJsp(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101343", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101343";
   }

   public static Loggable logPrecompilingStaleJspLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101343", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logPrecompilingJspNoClass(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101344", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101344";
   }

   public static Loggable logPrecompilingJspNoClassLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101344", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logELFFieldsChanged(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101345", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101345";
   }

   public static Loggable logELFFieldsChangedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101345", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logVirtualHostNameAlreadyUsed(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101346", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101346";
   }

   public static Loggable logVirtualHostNameAlreadyUsedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101346", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logVirtualHostServerChannelNameAlreadyUsed(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101347", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101347";
   }

   public static Loggable logVirtualHostServerChannelNameAlreadyUsedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101347", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logVirtualHostServerChannelUndefined(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101348", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101348";
   }

   public static Loggable logVirtualHostServerChannelUndefinedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101348", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToSaveWorkContexts(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101349", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101349";
   }

   public static Loggable logFailedToSaveWorkContextsLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101349", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logSessionListDuringGracefulProductionToAdmin(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101350", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101350";
   }

   public static Loggable logSessionListDuringGracefulProductionToAdminLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101350", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logInitialSessionsDuringGracefulProductionToAdmin(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101351", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101351";
   }

   public static Loggable logInitialSessionsDuringGracefulProductionToAdminLoggable(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return new Loggable("101351", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logGracefulProductionToAdminComplete(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101352", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101352";
   }

   public static Loggable logGracefulProductionToAdminCompleteLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101352", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logSessionsDuringGracefulProductionToAdmin(String var0, String var1, int var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, new Integer(var2), var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101354", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101354";
   }

   public static Loggable logSessionsDuringGracefulProductionToAdminLoggable(String var0, String var1, int var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, new Integer(var2), var3};
      return new Loggable("101354", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logWebServicesVersioningNotSupported(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101355", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101355";
   }

   public static Loggable logWebServicesVersioningNotSupportedLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101355", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToLoadNativeIOLibrary(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101356", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101356";
   }

   public static Loggable logFailedToLoadNativeIOLibraryLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101356", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logGracefulProductionToAdminInterrupted(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101357", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101357";
   }

   public static Loggable logGracefulProductionToAdminInterruptedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101357", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logMissingMessageDestinationDescriptor(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101358", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101358";
   }

   public static Loggable logMissingMessageDestinationDescriptorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101358", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logAsyncInitFailed(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101359", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101359";
   }

   public static Loggable logAsyncInitFailedLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101359", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logJSPisStale(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101360", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101360";
   }

   public static Loggable logJSPisStaleLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101360", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logIllegalWebLibSpecVersionRef(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101361", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101361";
   }

   public static Loggable logIllegalWebLibSpecVersionRefLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101361", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToDeserializeAttribute(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101362", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101362";
   }

   public static Loggable logFailedToDeserializeAttributeLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101362", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logIgnoringWeblogicXMLContextRoot(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101363", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101363";
   }

   public static Loggable logIgnoringWeblogicXMLContextRootLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101363", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailureInCompilingJSP(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101364", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101364";
   }

   public static Loggable logFailureInCompilingJSPLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("101364", var4, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logClosingTimeoutSocket() {
      Object[] var0 = new Object[0];
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101366", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101366";
   }

   public static Loggable logClosingTimeoutSocketLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101366", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logExtractionPathTooLong(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101367", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101367";
   }

   public static Loggable logExtractionPathTooLongLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101367", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDeprecatedEncodingPropertyUsed(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101369", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101369";
   }

   public static Loggable logDeprecatedEncodingPropertyUsedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101369", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logLibraryDescriptorMergeFailed(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101370", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101370";
   }

   public static Loggable logLibraryDescriptorMergeFailedLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101370", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logAnnotationProcessingFailed(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101371", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101371";
   }

   public static Loggable logAnnotationProcessingFailedLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101371", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logDependencyInjectionFailed(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101372", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101372";
   }

   public static Loggable logDependencyInjectionFailedLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101372", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logAnnotationsChanged(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101373", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101373";
   }

   public static Loggable logAnnotationsChangedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101373", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFailedToLookupTransaction(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101374", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101374";
   }

   public static Loggable logFailedToLookupTransactionLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101374", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFacesConfigParseException(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101375", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101375";
   }

   public static Loggable logFacesConfigParseExceptionLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("101375", var3, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logBeanELResolverPurgerException(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101376", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101376";
   }

   public static Loggable logBeanELResolverPurgerExceptionLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101376", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logFilteringConfigurationIgnored(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101377", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101377";
   }

   public static Loggable logFilteringConfigurationIgnoredLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("101377", var2, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String getAbstractMethodMsgForOpenJPA() {
      Object[] var0 = new Object[0];
      return (new Loggable("101378", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getAbstractMethodMsgForOpenJPALoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101378", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String getNoSuchMethodMsgForOpenJPA() {
      Object[] var0 = new Object[0];
      return (new Loggable("101379", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getNoSuchMethodMsgForOpenJPALoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("101379", var0, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logGlassfishDescriptorParsed(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101392", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101392";
   }

   public static Loggable logGlassfishDescriptorParsedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101392", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   public static String logGlassfishDescriptorIgnored(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("101393", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.class.getClassLoader()));
      return "101393";
   }

   public static Loggable logGlassfishDescriptorIgnoredLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("101393", var1, "weblogic.servlet.HTTPLogLocalizer", HTTPLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = HTTPLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = HTTPLogger.findMessageLogger();
      }
   }
}
