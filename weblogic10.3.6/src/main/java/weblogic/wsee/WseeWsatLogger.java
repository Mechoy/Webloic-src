package weblogic.wsee;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class WseeWsatLogger {
   private static final String LOCALIZER_CLASS = "weblogic.wsee.WseeWsatLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(WseeWsatLogger.class.getName());
   }

   public static String logErrorPersistingBranchRecord(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224500", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224500";
   }

   public static Loggable logErrorPersistingBranchRecordLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224500", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logErrorDeletingBranchRecord(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224501", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224501";
   }

   public static Loggable logErrorDeletingBranchRecordLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224501", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRegistrationServiceInstantiated(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224502", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224502";
   }

   public static Loggable logRegistrationServiceInstantiatedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224502", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logEnlistResource(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224503", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224503";
   }

   public static Loggable logEnlistResourceLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224503", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRegisterOperationEntered(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224504", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224504";
   }

   public static Loggable logRegisterOperationEnteredLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224504", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRegisterOperationExited(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224505", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224505";
   }

   public static Loggable logRegisterOperationExitedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224505", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logExceptionDuringEnlistResource(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224506", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224506";
   }

   public static Loggable logExceptionDuringEnlistResourceLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224506", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logExceptionDuringRegisterSynchronization(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224507", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224507";
   }

   public static Loggable logExceptionDuringRegisterSynchronizationLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224507", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCoordinatorServiceInstantiated(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224508", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224508";
   }

   public static Loggable logCoordinatorServiceInstantiatedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224508", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPreparedOperationEntered(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224509", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224509";
   }

   public static Loggable logPreparedOperationEnteredLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224509", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPreparedOperation(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224510", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224510";
   }

   public static Loggable logPreparedOperationLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224510", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPreparedOperationExited(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224511", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224511";
   }

   public static Loggable logPreparedOperationExitedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224511", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logAbortedOperationEntered(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224512", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224512";
   }

   public static Loggable logAbortedOperationEnteredLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224512", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logAbortedOperation(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224513", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224513";
   }

   public static Loggable logAbortedOperationLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224513", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logAbortedOperationExited(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224514", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224514";
   }

   public static Loggable logAbortedOperationExitedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224514", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReadOnlyOperationEntered(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224515", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224515";
   }

   public static Loggable logReadOnlyOperationEnteredLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224515", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReadOnlyOperation(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224516", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224516";
   }

   public static Loggable logReadOnlyOperationLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224516", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReadOnlyOperationExited(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224517", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224517";
   }

   public static Loggable logReadOnlyOperationExitedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224517", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCommittedOperationEntered(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224518", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224518";
   }

   public static Loggable logCommittedOperationEnteredLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224518", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCommittedOperation(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224519", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224519";
   }

   public static Loggable logCommittedOperationLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224519", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCommittedOperationExited(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224520", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224520";
   }

   public static Loggable logCommittedOperationExitedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224520", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReplayOperationEntered(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224521", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224521";
   }

   public static Loggable logReplayOperationEnteredLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224521", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReplayOperation(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224522", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224522";
   }

   public static Loggable logReplayOperationLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224522", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReplayOperationSOAPException(Object var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224523", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224523";
   }

   public static Loggable logReplayOperationSOAPExceptionLoggable(Object var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224523", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReplayOperationExited(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224524", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224524";
   }

   public static Loggable logReplayOperationExitedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224524", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRegisterSynchronization(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224525", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224525";
   }

   public static Loggable logRegisterSynchronizationLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224525", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWSATSynchronization(String var0, Object var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224526", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224526";
   }

   public static Loggable logWSATSynchronizationLoggable(String var0, Object var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224526", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionEntered(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224527", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224527";
   }

   public static Loggable logBeforeCompletionEnteredLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224527", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionCommittedBeforeWait(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224528", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224528";
   }

   public static Loggable logBeforeCompletionCommittedBeforeWaitLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224528", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionWaitingForReply(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224529", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224529";
   }

   public static Loggable logBeforeCompletionWaitingForReplyLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224529", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionFinishedWaitingForReply(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224530", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224530";
   }

   public static Loggable logBeforeCompletionFinishedWaitingForReplyLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224530", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionReceivedReplyWithStatus(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224531", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224531";
   }

   public static Loggable logBeforeCompletionReceivedReplyWithStatusLoggable(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224531", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionUnexceptedStatus(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224532", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224532";
   }

   public static Loggable logBeforeCompletionUnexceptedStatusLoggable(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224532", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionInterruptedException(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224533", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224533";
   }

   public static Loggable logBeforeCompletionInterruptedExceptionLoggable(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224533", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionException(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224534", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224534";
   }

   public static Loggable logBeforeCompletionExceptionLoggable(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224534", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionSystemExceptionDuringSetRollbackOnly(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224535", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224535";
   }

   public static Loggable logBeforeCompletionSystemExceptionDuringSetRollbackOnlyLoggable(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224535", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logBeforeCompletionTransactionNullDuringSetRollbackOnly(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224536", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224536";
   }

   public static Loggable logBeforeCompletionTransactionNullDuringSetRollbackOnlyLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224536", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logAfterCompletionStatus(String var0, Object var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224537", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224537";
   }

   public static Loggable logAfterCompletionStatusLoggable(String var0, Object var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224537", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWSATXAResource(String var0, Object var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224538", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224538";
   }

   public static Loggable logWSATXAResourceLoggable(String var0, Object var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224538", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPrepare(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224539", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224539";
   }

   public static Loggable logPrepareLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224539", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPreparedBeforeWait(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224540", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224540";
   }

   public static Loggable logPreparedBeforeWaitLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224540", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPrepareWaitingForReply(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224541", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224541";
   }

   public static Loggable logPrepareWaitingForReplyLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224541", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPrepareFinishedWaitingForReply(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224542", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224542";
   }

   public static Loggable logPrepareFinishedWaitingForReplyLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224542", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPrepareReceivedReplyStatus(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224543", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224543";
   }

   public static Loggable logPrepareReceivedReplyStatusLoggable(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224543", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logFailedStateForPrepare(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224544", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224544";
   }

   public static Loggable logFailedStateForPrepareLoggable(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224544", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logInterruptedExceptionDuringPrepare(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224545", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224545";
   }

   public static Loggable logInterruptedExceptionDuringPrepareLoggable(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224545", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCommit(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224546", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224546";
   }

   public static Loggable logCommitLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224546", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCommitBeforeWait(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224547", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224547";
   }

   public static Loggable logCommitBeforeWaitLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224547", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCommitWaitingForReply(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224548", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224548";
   }

   public static Loggable logCommitWaitingForReplyLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224548", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCommitFinishedWaitingForReply(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224549", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224549";
   }

   public static Loggable logCommitFinishedWaitingForReplyLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224549", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCommitReceivedReplyStatus(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224550", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224550";
   }

   public static Loggable logCommitReceivedReplyStatusLoggable(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224550", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logFailedStateForCommit(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224551", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224551";
   }

   public static Loggable logFailedStateForCommitLoggable(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224551", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logInterruptedExceptionDuringCommit(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224552", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224552";
   }

   public static Loggable logInterruptedExceptionDuringCommitLoggable(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224552", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRollback(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224553", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224553";
   }

   public static Loggable logRollbackLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224553", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRollbackBeforeWait(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224554", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224554";
   }

   public static Loggable logRollbackBeforeWaitLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224554", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRollbackWaitingForReply(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224555", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224555";
   }

   public static Loggable logRollbackWaitingForReplyLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224555", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRollbackFinishedWaitingForReply(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224556", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224556";
   }

   public static Loggable logRollbackFinishedWaitingForReplyLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224556", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRollbackReceivedReplyStatus(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224557", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224557";
   }

   public static Loggable logRollbackReceivedReplyStatusLoggable(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224557", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logFailedStateForRollback(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224558", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224558";
   }

   public static Loggable logFailedStateForRollbackLoggable(String var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224558", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logInterruptedExceptionDuringRollback(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224559", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224559";
   }

   public static Loggable logInterruptedExceptionDuringRollbackLoggable(Exception var0, String var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224559", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logEnterClientSideHandleMessage(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224560", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224560";
   }

   public static Loggable logEnterClientSideHandleMessageLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224560", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logExitClientSideHandleMessage(boolean var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224561", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224561";
   }

   public static Loggable logExitClientSideHandleMessageLoggable(boolean var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224561", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logEnterClientSideHandleFault(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224562", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224562";
   }

   public static Loggable logEnterClientSideHandleFaultLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224562", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logExitClientSideHandleFault(boolean var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224563", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224563";
   }

   public static Loggable logExitClientSideHandleFaultLoggable(boolean var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224563", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logEnterClientSideClose(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224564", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224564";
   }

   public static Loggable logEnterClientSideCloseLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224564", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logExitClientSideClose(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224565", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224565";
   }

   public static Loggable logExitClientSideCloseLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224565", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logOutboundApplicationMessageNoTransaction() {
      Object[] var0 = new Object[0];
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224566", var0, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224566";
   }

   public static Loggable logOutboundApplicationMessageNoTransactionLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("224566", var0, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logOutboundApplicationMessageTransactionBeforeAddingContext(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224567", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224567";
   }

   public static Loggable logOutboundApplicationMessageTransactionBeforeAddingContextLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224567", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logOutboundApplicationMessageTransactionAfterAddingContext(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224568", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224568";
   }

   public static Loggable logOutboundApplicationMessageTransactionAfterAddingContextLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224568", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logInboundApplicationMessage() {
      Object[] var0 = new Object[0];
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224569", var0, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224569";
   }

   public static Loggable logInboundApplicationMessageLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("224569", var0, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWillResumeInClientSideHandler(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224570", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224570";
   }

   public static Loggable logWillResumeInClientSideHandlerLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224570", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logResumedInClientSideHandler(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224571", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224571";
   }

   public static Loggable logResumedInClientSideHandlerLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224571", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logInvalidTransactionExceptionInClientSideHandler(Exception var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224572", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224572";
   }

   public static Loggable logInvalidTransactionExceptionInClientSideHandlerLoggable(Exception var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224572", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logSystemExceptionInClientSideHandler(Exception var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224573", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224573";
   }

   public static Loggable logSystemExceptionInClientSideHandlerLoggable(Exception var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224573", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logSuspendSuccessfulInClientSideHandler(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224574", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224574";
   }

   public static Loggable logSuspendSuccessfulInClientSideHandlerLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224574", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWSATInfoInClientSideHandler(String var0, long var1, Object var3, Object var4) {
      Object[] var5 = new Object[]{var0, new Long(var1), var3, var4};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224575", var5, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224575";
   }

   public static Loggable logWSATInfoInClientSideHandlerLoggable(String var0, long var1, Object var3, Object var4) {
      Object[] var5 = new Object[]{var0, new Long(var1), var3, var4};
      return new Loggable("224575", var5, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logSOAPExceptionCreatingCoordinatorContext(Exception var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224576", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224576";
   }

   public static Loggable logSOAPExceptionCreatingCoordinatorContextLoggable(Exception var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224576", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logAboutToSuspendInClientSideHandler(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224577", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224577";
   }

   public static Loggable logAboutToSuspendInClientSideHandlerLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224577", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logSuspendedInClientSideHandler(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224578", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224578";
   }

   public static Loggable logSuspendedInClientSideHandlerLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224578", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logSystemExceptionDuringSuspend(Exception var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224579", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224579";
   }

   public static Loggable logSystemExceptionDuringSuspendLoggable(Exception var0, Object var1, Object var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224579", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logUnknownParticipantIdentifier(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224580", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224580";
   }

   public static Loggable logUnknownParticipantIdentifierLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224580", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logXidNotInDurableResourceMap(Object var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224581", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224581";
   }

   public static Loggable logXidNotInDurableResourceMapLoggable(Object var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224581", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logXidNotInVolatileResourceMap(Object var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224582", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224582";
   }

   public static Loggable logXidNotInVolatileResourceMapLoggable(Object var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224582", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logDurablePortRemoved(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224583", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224583";
   }

   public static Loggable logDurablePortRemovedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224583", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logDurableXAResourceRemoved(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224584", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224584";
   }

   public static Loggable logDurableXAResourceRemovedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224584", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logVolatilePortRemoved(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224585", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224585";
   }

   public static Loggable logVolatilePortRemovedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224585", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logVolatileSynchronizationRemoved(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224586", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224586";
   }

   public static Loggable logVolatileSynchronizationRemovedLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224586", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logAboutToSendPrepare(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224587", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224587";
   }

   public static Loggable logAboutToSendPrepareLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224587", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPrepareSent(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224588", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224588";
   }

   public static Loggable logPrepareSentLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224588", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logDurableParticipantXAResourcePlacedInCacheFromPrepare(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224589", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224589";
   }

   public static Loggable logDurableParticipantXAResourcePlacedInCacheFromPrepareLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224589", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logAboutToSendCommit(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224590", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224590";
   }

   public static Loggable logAboutToSendCommitLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224590", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCommitSent(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224591", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224591";
   }

   public static Loggable logCommitSentLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224591", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logAboutToSendRollback(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224592", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224592";
   }

   public static Loggable logAboutToSendRollbackLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224592", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRollbackSent(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224593", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224593";
   }

   public static Loggable logRollbackSentLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224593", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRollbackParticipantXAResourcePlacedInCache(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224594", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224594";
   }

   public static Loggable logRollbackParticipantXAResourcePlacedInCacheLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224594", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logAboutToSendPrepareVolatile(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224595", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224595";
   }

   public static Loggable logAboutToSendPrepareVolatileLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224595", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPrepareVolatileSent(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224596", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224596";
   }

   public static Loggable logPrepareVolatileSentLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224596", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logPrepareParticipantSynchronizationPlacedInCache(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224597", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224597";
   }

   public static Loggable logPrepareParticipantSynchronizationPlacedInCacheLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224597", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logVolatileParticipantRetrievedFromCache(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224598", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224598";
   }

   public static Loggable logVolatileParticipantRetrievedFromCacheLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224598", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logVolatileParticipantPortPlacedInCache(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224599", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224599";
   }

   public static Loggable logVolatileParticipantPortPlacedInCacheLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224599", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logDurableParticipantPortRetreivedFromCache(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224600", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224600";
   }

   public static Loggable logDurableParticipantPortRetreivedFromCacheLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224600", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logCannotCreateDurableParticipantPort(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224601", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224601";
   }

   public static Loggable logCannotCreateDurableParticipantPortLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224601", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logDurableParticipantPortPlacedInCache(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224602", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224602";
   }

   public static Loggable logDurableParticipantPortPlacedInCacheLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224602", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logSuccessfullyCreatedParticipantPort(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224603", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224603";
   }

   public static Loggable logSuccessfullyCreatedParticipantPortLoggable(Object var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224603", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReferenceParameter(String var0, String var1, String var2, Object var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224604", var4, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224604";
   }

   public static Loggable logReferenceParameterLoggable(String var0, String var1, String var2, Object var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("224604", var4, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReplyToAddedToHeader(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224605", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224605";
   }

   public static Loggable logReplyToAddedToHeaderLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224605", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWLSWSATTxIdInHeader(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224606", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224606";
   }

   public static Loggable logWLSWSATTxIdInHeaderLoggable(String var0, Object var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("224606", var2, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logRegisterTypeInfo(Object var0, String var1, Object var2, Object var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224607", var4, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224607";
   }

   public static Loggable logRegisterTypeInfoLoggable(Object var0, String var1, Object var2, Object var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("224607", var4, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logEndpointReferenceTypeInfo(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      Object[] var9 = new Object[]{var0, var1, var2, var3, var4, var5, var6, var7, var8};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224608", var9, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224608";
   }

   public static Loggable logEndpointReferenceTypeInfoLoggable(Object var0, Object var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8) {
      Object[] var9 = new Object[]{var0, var1, var2, var3, var4, var5, var6, var7, var8};
      return new Loggable("224608", var9, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logReferenceParameterTypeValueInArray(Object var0, int var1, Object var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224609", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224609";
   }

   public static Loggable logReferenceParameterTypeValueInArrayLoggable(Object var0, int var1, Object var2) {
      Object[] var3 = new Object[]{var0, new Integer(var1), var2};
      return new Loggable("224609", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWSATRoutingInfoFinder(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224610", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224610";
   }

   public static Loggable logWSATRoutingInfoFinderLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224610", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWLSTransactionServicesImpl(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224611", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224611";
   }

   public static Loggable logWLSTransactionServicesImplLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224611", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWSATServerHelper(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224612", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224612";
   }

   public static Loggable logWSATServerHelperLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224612", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWSATParticipant(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224613", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224613";
   }

   public static Loggable logWSATParticipantLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224613", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWSATNoContextHeaderList(Object var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224614", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224614";
   }

   public static Loggable logWSATNoContextHeaderListLoggable(Object var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224614", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logWSATXAResourceInfo(String var0) {
      Object[] var1 = new Object[]{var0};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224615", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224615";
   }

   public static Loggable logWSATXAResourceInfoLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("224615", var1, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   public static String logExceptionWSATXAResourceEnlist(Object var0, Object var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("224616", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.class.getClassLoader()));
      return "224616";
   }

   public static Loggable logExceptionWSATXAResourceEnlistLoggable(Object var0, Object var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("224616", var3, "weblogic.wsee.WseeWsatLogLocalizer", WseeWsatLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WseeWsatLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = WseeWsatLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = WseeWsatLogger.findMessageLogger();
      }
   }
}
