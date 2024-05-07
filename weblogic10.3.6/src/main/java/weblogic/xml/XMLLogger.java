package weblogic.xml;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;

public class XMLLogger {
   private static final String LOCALIZER_CLASS = "weblogic.xml.XMLLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(XMLLogger.class.getName());
   }

   public static String logEntityCacheRejection(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130000", var3, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130000";
   }

   public static String logEntityCacheBroken() {
      Object[] var0 = new Object[0];
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130001", var0, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130001";
   }

   public static String logEntityCacheRoundTripFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130002", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130002";
   }

   public static String logXMLRegistryException(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130003", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130003";
   }

   public static String logCacheRejection(String var0, String var1, long var2) {
      Object[] var4 = new Object[]{var0, var1, new Long(var2)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130006", var4, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130006";
   }

   public static String logCacheMemoryPurge(int var0, long var1, long var3) {
      Object[] var5 = new Object[]{new Integer(var0), new Long(var1), new Long(var3)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130007", var5, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130007";
   }

   public static String logCacheDiskPurge(int var0, long var1, long var3) {
      Object[] var5 = new Object[]{new Integer(var0), new Long(var1), new Long(var3)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130008", var5, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130008";
   }

   public static String logCacheDiskRejection(String var0, String var1, long var2) {
      Object[] var4 = new Object[]{var0, var1, new Long(var2)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130009", var4, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130009";
   }

   public static String logCacheEntryAdd(String var0, String var1, long var2, String var4, long var5) {
      Object[] var7 = new Object[]{var0, var1, new Long(var2), var4, new Long(var5)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130010", var7, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130010";
   }

   public static String logCacheEntryDelete(String var0, String var1, long var2, long var4, long var6, long var8) {
      Object[] var10 = new Object[]{var0, var1, new Long(var2), new Long(var4), new Long(var6), new Long(var8)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130011", var10, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130011";
   }

   public static String logCacheEntryPersist(String var0, String var1, long var2, long var4) {
      Object[] var6 = new Object[]{var0, var1, new Long(var2), new Long(var4)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130012", var6, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130012";
   }

   public static String logCacheEntryLoad(String var0, String var1, long var2, long var4) {
      Object[] var6 = new Object[]{var0, var1, new Long(var2), new Long(var4)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130013", var6, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130013";
   }

   public static String logCacheStatisticsCheckpoint() {
      Object[] var0 = new Object[0];
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130014", var0, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130014";
   }

   public static String logCacheCreation(long var0, long var2) {
      Object[] var4 = new Object[]{new Long(var0), new Long(var2)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130015", var4, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130015";
   }

   public static String logCacheLoad(long var0, long var2) {
      Object[] var4 = new Object[]{new Long(var0), new Long(var2)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130016", var4, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130016";
   }

   public static String logCacheClose(long var0) {
      Object[] var2 = new Object[]{new Long(var0)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130017", var2, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130017";
   }

   public static String logCacheCorrupted(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130018", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130018";
   }

   public static String logCacheEntryCorrupted(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130019", var3, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130019";
   }

   public static String logCacheStatisticsCorrupted(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130020", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130020";
   }

   public static String logCacheEntrySaveError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130021", var3, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130021";
   }

   public static String logCacheSaveError(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130022", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130022";
   }

   public static String logCacheStatisticsSaveError(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130023", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130023";
   }

   public static String logCacheOutOfMemoryOnEntryLoad(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130024", var3, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130024";
   }

   public static String logCacheOutOfMemoryOnLoad(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130025", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130025";
   }

   public static String logCacheOutOfMemoryOnStatisticsLoad(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130026", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130026";
   }

   public static String logCacheMemoryWarningExceeds(long var0, long var2) {
      Object[] var4 = new Object[]{new Long(var0), new Long(var2)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130027", var4, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130027";
   }

   public static String logCacheMemoryWarningClose(long var0, long var2) {
      Object[] var4 = new Object[]{new Long(var0), new Long(var2)};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130028", var4, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130028";
   }

   public static String logCacheUnexpectedProblem(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130029", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130029";
   }

   public static String logCacheEntryReadError(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130030", var3, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130030";
   }

   public static String logCacheReadError(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130031", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130031";
   }

   public static String logCacheStatisticsReadError(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130032", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130032";
   }

   public static String logParserConfigurationException(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130033", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130033";
   }

   public static String logSAXException(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130034", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130034";
   }

   public static String logIOException(String var0) {
      Object[] var1 = new Object[]{var0};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130035", var1, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130035";
   }

   public static String logIntializingXMLRegistry() {
      Object[] var0 = new Object[0];
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130036", var0, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130036";
   }

   public static String logStackTrace(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130037", var2, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130037";
   }

   public static String logPropertyNotAccepted(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      XMLLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("130038", var3, "weblogic.xml.XMLLogLocalizer", XMLLogger.class.getClassLoader()));
      return "130038";
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = XMLLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = XMLLogger.findMessageLogger();
      }
   }
}
