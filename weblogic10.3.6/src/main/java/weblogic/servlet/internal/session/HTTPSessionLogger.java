package weblogic.servlet.internal.session;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class HTTPSessionLogger {
   private static final String LOCALIZER_CLASS = "weblogic.servlet.internal.session.HTTPSessionLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(HTTPSessionLogger.class.getName());
   }

   public static String logDeprecatedCall(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100000", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100000";
   }

   public static Loggable logDeprecatedCallLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100000", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logUnableToRemoveSession(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100005", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100005";
   }

   public static Loggable logUnableToRemoveSessionLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100005", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logPersistence(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100006", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100006";
   }

   public static Loggable logPersistenceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100006", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logDeleteDirectory(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100007", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100007";
   }

   public static Loggable logDeleteDirectoryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100007", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logUnableToDelete(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100008", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100008";
   }

   public static Loggable logUnableToDeleteLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100008", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logPickledSession(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100010", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100010";
   }

   public static Loggable logPickledSessionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100010", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logErrorSavingSessionData(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100011", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100011";
   }

   public static Loggable logErrorSavingSessionDataLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100011", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logNotInvalidated(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100013", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100013";
   }

   public static Loggable logNotInvalidatedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100013", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logDeletedFile(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100015", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100015";
   }

   public static Loggable logDeletedFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100015", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logTestFailure(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100016", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100016";
   }

   public static Loggable logTestFailureLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100016", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logErrorLoadingSessionData(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100018", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100018";
   }

   public static Loggable logErrorLoadingSessionDataLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100018", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSessionPath(String var0, String var1, boolean var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100019", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100019";
   }

   public static Loggable logSessionPathLoggable(String var0, String var1, boolean var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100019", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logCookieFormatError(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100020", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100020";
   }

   public static Loggable logCookieFormatErrorLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100020", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logInvalidationInterval(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100022", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100022";
   }

   public static Loggable logInvalidationIntervalLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("100022", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logUnexpectedTimeoutError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100025", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100025";
   }

   public static Loggable logUnexpectedTimeoutErrorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100025", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logUnexpectedTimeoutErrorRaised(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100026", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100026";
   }

   public static Loggable logUnexpectedTimeoutErrorRaisedLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100026", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logUnableToDeserializeSessionData(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100028", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100028";
   }

   public static Loggable logUnableToDeserializeSessionDataLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100028", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSessionExpired(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100030", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100030";
   }

   public static Loggable logSessionExpiredLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100030", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logErrorUnregisteringServletSessionRuntime(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100031", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100031";
   }

   public static Loggable logErrorUnregisteringServletSessionRuntimeLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100031", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logErrorCreatingServletSessionRuntimeMBean(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100032", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100032";
   }

   public static Loggable logErrorCreatingServletSessionRuntimeMBeanLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100032", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSessionNotAllowed(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100033", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100033";
   }

   public static Loggable logSessionNotAllowedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100033", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logTimerInvalidatedSession(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100035", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100035";
   }

   public static Loggable logTimerInvalidatedSessionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100035", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logCreatingSessionContextOfType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100037", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100037";
   }

   public static Loggable logCreatingSessionContextOfTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100037", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logUnknownPeristentType(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100038", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100038";
   }

   public static Loggable logUnknownPeristentTypeLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100038", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logClusteringRequiredForReplication(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100039", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100039";
   }

   public static Loggable logClusteringRequiredForReplicationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100039", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logGetAttributeEJBObject(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100040", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100040";
   }

   public static Loggable logGetAttributeEJBObjectLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100040", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSetAttributeEJBObject(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100041", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100041";
   }

   public static Loggable logSetAttributeEJBObjectLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100041", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logErrorReconstructingEJBObject(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100042", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100042";
   }

   public static Loggable logErrorReconstructingEJBObjectLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100042", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logErrorFindingHandle(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100043", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100043";
   }

   public static Loggable logErrorFindingHandleLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100043", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logFoundWLCookie(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100044", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100044";
   }

   public static Loggable logFoundWLCookieLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100044", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logMalformedWLCookie(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100045", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100045";
   }

   public static Loggable logMalformedWLCookieLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100045", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logCreateNewSessionForPath(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100046", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100046";
   }

   public static Loggable logCreateNewSessionForPathLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100046", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logPerformOperation(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100047", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100047";
   }

   public static Loggable logPerformOperationLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100047", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logRetrievedROIDFromSecondary(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100048", var4, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100048";
   }

   public static Loggable logRetrievedROIDFromSecondaryLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("100048", var4, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logErrorGettingSession(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100049", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100049";
   }

   public static Loggable logErrorGettingSessionLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100049", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logBecomePrimary(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100050", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100050";
   }

   public static Loggable logBecomePrimaryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100050", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logBecomeSecondary(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100051", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100051";
   }

   public static Loggable logBecomeSecondaryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100051", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logUnregister(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100052", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100052";
   }

   public static Loggable logUnregisterLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100052", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logFailedToUpdateSecondary(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100053", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100053";
   }

   public static Loggable logFailedToUpdateSecondaryLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100053", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSecondaryNotFound(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100054", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100054";
   }

   public static Loggable logSecondaryNotFoundLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100054", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSessionIDContainsReservedKeyword(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100055", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100055";
   }

   public static Loggable logSessionIDContainsReservedKeywordLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100055", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSessionGotInvalidatedBeforeCreationCouldComplete(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100056", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100056";
   }

   public static Loggable logSessionGotInvalidatedBeforeCreationCouldCompleteLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100056", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logContextNotFound(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100057", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100057";
   }

   public static Loggable logContextNotFoundLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100057", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logPersistentStoreTypeNotReplicated(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100058", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100058";
   }

   public static Loggable logPersistentStoreTypeNotReplicatedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100058", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logUnexpectedErrorCleaningUpSessions(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100059", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100059";
   }

   public static Loggable logUnexpectedErrorCleaningUpSessionsLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100059", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logUnexpectedError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100060", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100060";
   }

   public static Loggable logUnexpectedErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100060", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logTransientMemoryAttributeError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100061", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100061";
   }

   public static Loggable logTransientMemoryAttributeErrorLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100061", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logTransientReplicatedAttributeError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100062", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100062";
   }

   public static Loggable logTransientReplicatedAttributeErrorLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100062", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logTransientFileAttributeError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100063", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100063";
   }

   public static Loggable logTransientFileAttributeErrorLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100063", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logTransientJDBCAttributeError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100064", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100064";
   }

   public static Loggable logTransientJDBCAttributeErrorLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100064", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logAttributeRemovalFailure(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100065", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100065";
   }

   public static Loggable logAttributeRemovalFailureLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100065", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSessionNotFound(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100066", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100066";
   }

   public static Loggable logSessionNotFoundLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100066", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSecondaryIDNotFound(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100067", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100067";
   }

   public static Loggable logSecondaryIDNotFoundLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100067", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logFailedToFindSecondaryInfo(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100068", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100068";
   }

   public static Loggable logFailedToFindSecondaryInfoLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100068", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logGetAttributeEJBHome(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100069", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100069";
   }

   public static Loggable logGetAttributeEJBHomeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100069", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logErrorReconstructingEJBHome(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100070", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100070";
   }

   public static Loggable logErrorReconstructingEJBHomeLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100070", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSetAttributeEJBHome(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100071", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100071";
   }

   public static Loggable logSetAttributeEJBHomeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100071", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logErrorFindingHomeHandle(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100072", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100072";
   }

   public static Loggable logErrorFindingHomeHandleLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100072", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logExceptionSerializingAttributeWrapper(Exception var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100073", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100073";
   }

   public static Loggable logExceptionSerializingAttributeWrapperLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100073", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logObjectNotSerializable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100088", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100088";
   }

   public static Loggable logObjectNotSerializableLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100088", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSessionObjectSize(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100077", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100077";
   }

   public static Loggable logSessionObjectSizeLoggable(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      return new Loggable("100077", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSessionSize(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100078", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100078";
   }

   public static Loggable logSessionSizeLoggable(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      return new Loggable("100078", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logWANSessionConfigurationError() {
      Object[] var0 = new Object[0];
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100079", var0, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100079";
   }

   public static Loggable logWANSessionConfigurationErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("100079", var0, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logIncompatiblePersistentStore(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100081", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100081";
   }

   public static Loggable logIncompatiblePersistentStoreLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100081", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logInsufficientConfiguration(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100082", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100082";
   }

   public static Loggable logInsufficientConfigurationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100082", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logInhomogeneousDeploymentForApp(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100083", var4, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100083";
   }

   public static Loggable logInhomogeneousDeploymentForAppLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("100083", var4, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logInhomogeneousDeploymentForVHost(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100084", var5, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100084";
   }

   public static Loggable logInhomogeneousDeploymentForVHostLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("100084", var5, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logJDBCSessionConcurrentModification(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100087", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100087";
   }

   public static Loggable logJDBCSessionConcurrentModificationLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100087", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logLATUpdateError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100090", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100090";
   }

   public static Loggable logLATUpdateErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("100090", var2, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logAttributeChanged(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100091", var6, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100091";
   }

   public static Loggable logAttributeChangedLoggable(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      return new Loggable("100091", var6, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logDumpSession(String var0) {
      Object[] var1 = new Object[]{var0};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100092", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100092";
   }

   public static Loggable logDumpSessionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("100092", var1, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logDebugSessionEvent(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100093", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100093";
   }

   public static Loggable logDebugSessionEventLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("100093", var3, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logSessionAccessFromNonPrimary(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100094", var4, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100094";
   }

   public static Loggable logSessionAccessFromNonPrimaryLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("100094", var4, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   public static String logConnectionPoolReserveTimeoutSecondsOverride() {
      Object[] var0 = new Object[0];
      HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("100095", var0, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.class.getClassLoader()));
      return "100095";
   }

   public static Loggable logConnectionPoolReserveTimeoutSecondsOverrideLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("100095", var0, "weblogic.servlet.internal.session.HTTPSessionLogLocalizer", HTTPSessionLogger.MessageLoggerInitializer.INSTANCE.messageLogger, HTTPSessionLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = HTTPSessionLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = HTTPSessionLogger.findMessageLogger();
      }
   }
}
