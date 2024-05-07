package weblogic.common;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class T3MiscLogger {
   private static final String LOCALIZER_CLASS = "weblogic.common.T3MiscLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(T3MiscLogger.class.getName());
   }

   public static String logDebug(String var0) {
      Object[] var1 = new Object[]{var0};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000700", var1, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000700";
   }

   public static String logMount(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000701", var2, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000701";
   }

   public static String logUnmount(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000702", var2, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000702";
   }

   public static String logGetRoot(Exception var0) {
      Object[] var1 = new Object[]{var0};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000703", var1, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000703";
   }

   public static String logBadCreate(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000704", var2, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000704";
   }

   public static String logCreate(String var0) {
      Object[] var1 = new Object[]{var0};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000705", var1, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000705";
   }

   public static String logFindRemote(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000706", var2, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000706";
   }

   public static String logOpenRemote(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000707", var2, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000707";
   }

   public static String logWriteTimed(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000708", var2, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000708";
   }

   public static String logFlushTimed(int var0, int var1) {
      Object[] var2 = new Object[]{new Integer(var0), new Integer(var1)};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000709", var2, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000709";
   }

   public static String logPastTime(String var0) {
      Object[] var1 = new Object[]{var0};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000710", var1, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000710";
   }

   public static String logThrowable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000711", var1, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000711";
   }

   public static String logExecution(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000712", var2, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000712";
   }

   public static String logCloseException(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      T3MiscLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("000713", var1, "weblogic.common.T3MiscLogLocalizer", T3MiscLogger.class.getClassLoader()));
      return "000713";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = T3MiscLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = T3MiscLogger.findMessageLogger();
      }
   }
}
