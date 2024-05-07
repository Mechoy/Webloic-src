package weblogic.wtc;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class WTCLogger {
   private static final String LOCALIZER_CLASS = "weblogic.wtc.WTCLogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(WTCLogger.class.getName());
   }

   public static String logInfoStartConfigParse() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180000", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180000";
   }

   public static Loggable logInfoStartConfigParseLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180000", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logInfoDoneConfigParse() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180001", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180001";
   }

   public static Loggable logInfoDoneConfigParseLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180001", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIAEcreateSubCntxt(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180002", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180002";
   }

   public static Loggable logIAEcreateSubCntxtLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180002", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logNEcreateSubCntxt(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180003", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180003";
   }

   public static Loggable logNEcreateSubCntxtLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180003", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logNABEtuxConnFactory(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180004", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180004";
   }

   public static Loggable logNABEtuxConnFactoryLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180004", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIAEtuxConnFactory(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180005", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180005";
   }

   public static Loggable logIAEtuxConnFactoryLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180005", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logNEtuxConnFactory(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180006", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180006";
   }

   public static Loggable logNEtuxConnFactoryLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180006", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logSEgetTranMgr(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180007", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180007";
   }

   public static Loggable logSEgetTranMgrLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180007", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorTranId(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180008", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180008";
   }

   public static Loggable logErrorTranIdLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180008", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorExecMBeanDef(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180009", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180009";
   }

   public static Loggable logErrorExecMBeanDefLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180009", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEconstructTDMLocalTD(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180010", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180010";
   }

   public static Loggable logUEconstructTDMLocalTDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180010", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEconstructTDMRemoteTD(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180011", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180011";
   }

   public static Loggable logUEconstructTDMRemoteTDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180011", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadTDMRemoteLTD(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180012", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180012";
   }

   public static Loggable logErrorBadTDMRemoteLTDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180012", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadTDMExportLTD(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180013", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180013";
   }

   public static Loggable logErrorBadTDMExportLTDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180013", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEconstructTDMExport(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180014", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180014";
   }

   public static Loggable logUEconstructTDMExportLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180014", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadTDMImportLTD(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180015", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180015";
   }

   public static Loggable logErrorBadTDMImportLTDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180015", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadTDMImportRTD(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180016", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180016";
   }

   public static Loggable logErrorBadTDMImportRTDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180016", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEconstructTDMImport(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180017", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180017";
   }

   public static Loggable logUEconstructTDMImportLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180017", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorPasswordInfo(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180018", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180018";
   }

   public static Loggable logErrorPasswordInfoLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180018", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadTDMPasswdLTD(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180019", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180019";
   }

   public static Loggable logErrorBadTDMPasswdLTDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180019", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadTDMPasswdRTD(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180020", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180020";
   }

   public static Loggable logErrorBadTDMPasswdRTDLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180020", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEconstructTDMPasswd(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180021", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180021";
   }

   public static Loggable logUEconstructTDMPasswdLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180021", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEconstructTDMResourcesFT(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180022", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180022";
   }

   public static Loggable logUEconstructTDMResourcesFTLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180022", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logXAEcommitXid(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180023", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180023";
   }

   public static Loggable logXAEcommitXidLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180023", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logTPEsendTran(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180024", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180024";
   }

   public static Loggable logTPEsendTranLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180024", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadFmlFldType(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180025", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180025";
   }

   public static Loggable logErrorBadFmlFldTypeLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180025", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logFEbadFMLinData(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180026", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180026";
   }

   public static Loggable logFEbadFMLinDataLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180026", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEbadFMLinData(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180027", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180027";
   }

   public static Loggable logUEbadFMLinDataLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180027", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadFml32FldType(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180028", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180028";
   }

   public static Loggable logErrorBadFml32FldTypeLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180028", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logFEbadFML32inData(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180029", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180029";
   }

   public static Loggable logFEbadFML32inDataLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180029", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEbadFML32inData(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180030", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180030";
   }

   public static Loggable logUEbadFML32inDataLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180030", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNullXmlArg() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180031", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180031";
   }

   public static Loggable logErrorNullXmlArgLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180031", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadFldTblsArg() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180032", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180032";
   }

   public static Loggable logErrorBadFldTblsArgLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180032", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNullFMLarg() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180033", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180033";
   }

   public static Loggable logErrorNullFMLargLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180033", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoXmlDocRoot(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180034", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180034";
   }

   public static Loggable logErrorNoXmlDocRootLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180034", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNullFML32arg() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180035", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180035";
   }

   public static Loggable logErrorNullFML32argLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180035", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logSEbadXml2Parser(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180036", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180036";
   }

   public static Loggable logSEbadXml2ParserLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180036", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIOEbadXml2Parser(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180037", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180037";
   }

   public static Loggable logIOEbadXml2ParserLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180037", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEbadXml2Parser(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180038", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180038";
   }

   public static Loggable logUEbadXml2ParserLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180038", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNullDocFromParser(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180039", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180039";
   }

   public static Loggable logErrorNullDocFromParserLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180039", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoTopElemFromStr(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180040", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180040";
   }

   public static Loggable logErrorNoTopElemFromStrLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180040", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNullTopElemName(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180041", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180041";
   }

   public static Loggable logErrorNullTopElemNameLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180041", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoFMLdata() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180042", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180042";
   }

   public static Loggable logErrorNoFMLdataLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180042", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNullXmlElemName() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180043", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180043";
   }

   public static Loggable logErrorNullXmlElemNameLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180043", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNullXmlElemValue(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180044", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180044";
   }

   public static Loggable logErrorNullXmlElemValueLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180044", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoFML32data() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180045", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180045";
   }

   public static Loggable logErrorNoFML32dataLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180045", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logDebugMsg(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180046", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180046";
   }

   public static Loggable logDebugMsgLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180046", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBNOredirects() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180047", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180047";
   }

   public static Loggable logtBNOredirectsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180047", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBNOTuxedoConnectionFactory() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180048", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180048";
   }

   public static Loggable logtBNOTuxedoConnectionFactoryLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180048", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBNOWLXToptionAvailable() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180049", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180049";
   }

   public static Loggable logtBNOWLXToptionAvailableLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180049", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBInternalTranslationFailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180050", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180050";
   }

   public static Loggable logtBInternalTranslationFailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180050", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logTTEstdSchedule(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180052", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180052";
   }

   public static Loggable logTTEstdScheduleLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180052", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDom80SendTokenCreation(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180053", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180053";
   }

   public static Loggable logErrorDom80SendTokenCreationLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180053", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDom80RecvTokenRead(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180054", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180054";
   }

   public static Loggable logErrorDom80RecvTokenReadLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180054", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorTokenError(int var0, int var1, int var2) {
      Object[] var3 = new Object[]{new Integer(var0), new Integer(var1), new Integer(var2)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180055", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180055";
   }

   public static Loggable logErrorTokenErrorLoggable(int var0, int var1, int var2) {
      Object[] var3 = new Object[]{new Integer(var0), new Integer(var1), new Integer(var2)};
      return new Loggable("180055", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEgssCryptoError1(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180056", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180056";
   }

   public static Loggable logUEgssCryptoError1Loggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180056", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorGssInvRetChallenge() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180057", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180057";
   }

   public static Loggable logErrorGssInvRetChallengeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180057", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIOEgssIOerror(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180058", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180058";
   }

   public static Loggable logIOEgssIOerrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180058", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUEgssCryptoError2(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180059", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180059";
   }

   public static Loggable logUEgssCryptoError2Loggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180059", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logInvHandlerAddrLength(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180063", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180063";
   }

   public static Loggable logInvHandlerAddrLengthLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180063", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorWSRPCRQdescrim(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180067", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180067";
   }

   public static Loggable logErrorWSRPCRQdescrimLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180067", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorWSRPCRQtype(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180068", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180068";
   }

   public static Loggable logErrorWSRPCRQtypeLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180068", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIOEbadTypedBuffer(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180069", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180069";
   }

   public static Loggable logIOEbadTypedBufferLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180069", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnFML32badType(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180070", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180070";
   }

   public static Loggable logWarnFML32badTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180070", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorFML32badField(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180071", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180071";
   }

   public static Loggable logErrorFML32badFieldLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180071", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logDebugTDumpByteStart(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180072", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180072";
   }

   public static Loggable logDebugTDumpByteStartLoggable(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      return new Loggable("180072", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logDebugTDumpByteEnd() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180073", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180073";
   }

   public static Loggable logDebugTDumpByteEndLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180073", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorTpinitBuffer() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180074", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180074";
   }

   public static Loggable logErrorTpinitBufferLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180074", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorRecvSize(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180075", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180075";
   }

   public static Loggable logErrorRecvSizeLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180075", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIOEbadUsrPasswd(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180076", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180076";
   }

   public static Loggable logIOEbadUsrPasswdLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180076", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIOEbadDomSocketClose(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180077", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180077";
   }

   public static Loggable logIOEbadDomSocketCloseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180077", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnOWSAREPLY() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180078", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180078";
   }

   public static Loggable logWarnOWSAREPLYLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180078", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorReadTfmh() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180079", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180079";
   }

   public static Loggable logErrorReadTfmhLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180079", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNullTmmsgWs() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180080", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180080";
   }

   public static Loggable logErrorNullTmmsgWsLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180080", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIOEbadRsessionClose(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180081", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180081";
   }

   public static Loggable logIOEbadRsessionCloseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180081", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIOEbadWscSocketClose(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180082", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180082";
   }

   public static Loggable logIOEbadWscSocketCloseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180082", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorUnknownTcmType(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180083", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180083";
   }

   public static Loggable logErrorUnknownTcmTypeLoggable(int var0) {
      Object[] var1 = new Object[]{new Integer(var0)};
      return new Loggable("180083", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIOEbadTCMwrite(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180084", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180084";
   }

   public static Loggable logIOEbadTCMwriteLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180084", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logIOEbadUnsolAck(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180085", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180085";
   }

   public static Loggable logIOEbadUnsolAckLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180085", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logInfoRemoteDomainConnected(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180086", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180086";
   }

   public static Loggable logInfoRemoteDomainConnectedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180086", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logInfoConnectedToRemoteDomain(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180087", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180087";
   }

   public static Loggable logInfoConnectedToRemoteDomainLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180087", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDom65SendPreAcall(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180088", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180088";
   }

   public static Loggable logErrorDom65SendPreAcallLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180088", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDom65RecvPreAcall(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180089", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180089";
   }

   public static Loggable logErrorDom65RecvPreAcallLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180089", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDomainSecurityFailedRemote(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180090", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180090";
   }

   public static Loggable logErrorDomainSecurityFailedRemoteLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180090", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDomainSecurityFailedLocal(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180091", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180091";
   }

   public static Loggable logErrorDomainSecurityFailedLocalLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180091", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorChallengeFailed(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180092", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180092";
   }

   public static Loggable logErrorChallengeFailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180092", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logNEConfigInfo(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180093", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180093";
   }

   public static Loggable logNEConfigInfoLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180093", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBJMStargetNamefailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180094", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180094";
   }

   public static Loggable logtBJMStargetNamefailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180094", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBJMSsourceNamefailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180095", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180095";
   }

   public static Loggable logtBJMSsourceNamefailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180095", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBJMSerrorDestinationfailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180096", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180096";
   }

   public static Loggable logtBJMSerrorDestinationfailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180096", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBsent2errorDestination() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180097", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180097";
   }

   public static Loggable logtBsent2errorDestinationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180097", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBsent2errorDestinationfailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180098", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180098";
   }

   public static Loggable logtBsent2errorDestinationfailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180098", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logMinEncryptBitsGreaterThanMaxEncryptBits(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180099", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180099";
   }

   public static Loggable logMinEncryptBitsGreaterThanMaxEncryptBitsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180099", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logTPEConfigError(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180100", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180100";
   }

   public static Loggable logTPEConfigErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180100", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDuplicatedLocalDomain(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180102", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180102";
   }

   public static Loggable logErrorDuplicatedLocalDomainLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180102", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDuplicatedRemoteDomain(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180103", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180103";
   }

   public static Loggable logErrorDuplicatedRemoteDomainLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180103", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBunsupportedJMSmsgtype() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180104", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180104";
   }

   public static Loggable logtBunsupportedJMSmsgtypeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180104", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logDroppedMessage() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180105", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180105";
   }

   public static Loggable logDroppedMessageLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180105", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoTransactionManager() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180106", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180106";
   }

   public static Loggable logErrorNoTransactionManagerLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180106", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarningXaRecoverFailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180107", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180107";
   }

   public static Loggable logWarningXaRecoverFailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180107", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarningRecoverRollbackFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180108", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180108";
   }

   public static Loggable logWarningRecoverRollbackFailureLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180108", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBSlashQFML2XMLFailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180109", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180109";
   }

   public static Loggable logtBSlashQFML2XMLFailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180109", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBSlashQFML322XMLFailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180110", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180110";
   }

   public static Loggable logtBSlashQFML322XMLFailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180110", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBInternalFML2XMLFailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180111", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180111";
   }

   public static Loggable logtBInternalFML2XMLFailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180111", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBInternalFML322XMLFailed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180112", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180112";
   }

   public static Loggable logtBInternalFML322XMLFailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180112", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logDumpOneLine(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180113", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180113";
   }

   public static Loggable logDumpOneLineLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180113", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logtBparsefailed(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180115", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180115";
   }

   public static Loggable logtBparsefailedLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180115", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadGetFldTblsType(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180116", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180116";
   }

   public static Loggable logErrorBadGetFldTblsTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180116", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorUnsupportedEncoding(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180117", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180117";
   }

   public static Loggable logErrorUnsupportedEncodingLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180117", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorFileDoesNotExist(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180118", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180118";
   }

   public static Loggable logErrorFileDoesNotExistLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180118", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNotAFile(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180119", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180119";
   }

   public static Loggable logErrorNotAFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180119", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorFileNotReadable(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180120", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180120";
   }

   public static Loggable logErrorFileNotReadableLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180120", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorFileNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180121", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180121";
   }

   public static Loggable logErrorFileNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180121", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorFileSecurity(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180122", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180122";
   }

   public static Loggable logErrorFileSecurityLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180122", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorFileIOError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180123", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180123";
   }

   public static Loggable logErrorFileIOErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180123", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorBadNumberFormat(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180124", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180124";
   }

   public static Loggable logErrorBadNumberFormatLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180124", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorCustomAppKeyClassNotFound(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180132", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180132";
   }

   public static Loggable logErrorCustomAppKeyClassNotFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180132", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorAppKeyInitFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180133", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180133";
   }

   public static Loggable logErrorAppKeyInitFailureLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180133", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorCreateAppKeyClassInstanceFailure(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180134", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180134";
   }

   public static Loggable logErrorCreateAppKeyClassInstanceFailureLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180134", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUndefinedMBeanAttr(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180136", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180136";
   }

   public static Loggable logUndefinedMBeanAttrLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180136", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logInvalidMBeanAttr(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180137", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180137";
   }

   public static Loggable logInvalidMBeanAttrLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180137", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDupImpSvc(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180138", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180138";
   }

   public static Loggable logErrorDupImpSvcLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180138", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logUndefinedMBean(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180139", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180139";
   }

   public static Loggable logUndefinedMBeanLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180139", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logDebugSecurity(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180140", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180140";
   }

   public static Loggable logDebugSecurityLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180140", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorInvalidMagicNumber() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180141", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180141";
   }

   public static Loggable logErrorInvalidMagicNumberLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180141", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorLocalTDomInUse(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180142", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180142";
   }

   public static Loggable logErrorLocalTDomInUseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180142", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoSuchLocalDomain(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180143", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180143";
   }

   public static Loggable logErrorNoSuchLocalDomainLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180143", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorDuplicateRemoteTDom(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180144", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180144";
   }

   public static Loggable logErrorDuplicateRemoteTDomLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180144", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorRemoteTDomInUse(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180145", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180145";
   }

   public static Loggable logErrorRemoteTDomInUseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180145", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoSuchRemoteDomain(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180146", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180146";
   }

   public static Loggable logErrorNoSuchRemoteDomainLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180146", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoSuchImport(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180147", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180147";
   }

   public static Loggable logErrorNoSuchImportLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180147", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoSuchExport(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180148", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180148";
   }

   public static Loggable logErrorNoSuchExportLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180148", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoSuchPassword(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180149", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180149";
   }

   public static Loggable logErrorNoSuchPasswordLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180149", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorIncomingOnly(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180150", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180150";
   }

   public static Loggable logErrorIncomingOnlyLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180150", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorResourceInUse(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180151", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180151";
   }

   public static Loggable logErrorResourceInUseLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180151", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNotificationRegistration() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180152", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180152";
   }

   public static Loggable logErrorNotificationRegistrationLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180152", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnNoValidHostAddress(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180153", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180153";
   }

   public static Loggable logWarnNoValidHostAddressLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180153", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnNoValidListeningAddress(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180154", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180154";
   }

   public static Loggable logWarnNoValidListeningAddressLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180154", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnNoMoreValidRemoteAddress(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180155", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180155";
   }

   public static Loggable logWarnNoMoreValidRemoteAddressLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180155", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logInfoTryNextAddress(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180156", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180156";
   }

   public static Loggable logInfoTryNextAddressLoggable(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      return new Loggable("180156", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnNoMoreAddressToTry(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180157", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180157";
   }

   public static Loggable logWarnNoMoreAddressToTryLoggable(String var0, int var1) {
      Object[] var2 = new Object[]{var0, new Integer(var1)};
      return new Loggable("180157", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnNoMoreValidListeningAddress(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180158", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180158";
   }

   public static Loggable logWarnNoMoreValidListeningAddressLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180158", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logInfoTryNextListeningAddress(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180159", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180159";
   }

   public static Loggable logInfoTryNextListeningAddressLoggable(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return new Loggable("180159", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnNoMoreListeningAddressToTry(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180160", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180160";
   }

   public static Loggable logWarnNoMoreListeningAddressToTryLoggable(String var0, String var1, int var2) {
      Object[] var3 = new Object[]{var0, var1, new Integer(var2)};
      return new Loggable("180160", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorFail2FindImportedQSpace(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180161", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180161";
   }

   public static Loggable logErrorFail2FindImportedQSpaceLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180161", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorTbNoSuchImport(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180162", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180162";
   }

   public static Loggable logErrorTbNoSuchImportLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180162", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorTbUnsupportedBufferType(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180163", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180163";
   }

   public static Loggable logErrorTbUnsupportedBufferTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180163", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorTbJmsSendFailure() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180164", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180164";
   }

   public static Loggable logErrorTbJmsSendFailureLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180164", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logInfoDisconnectNoKeepAliveAck(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180165", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180165";
   }

   public static Loggable logInfoDisconnectNoKeepAliveAckLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180165", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnDisableKeepAlive(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180166", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180166";
   }

   public static Loggable logWarnDisableKeepAliveLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180166", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorUndefinedTDomainSession(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180167", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180167";
   }

   public static Loggable logErrorUndefinedTDomainSessionLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("180167", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorTDomainPassword(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180168", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180168";
   }

   public static Loggable logErrorTDomainPasswordLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180168", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorXAEnd(Exception var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180169", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180169";
   }

   public static Loggable logErrorXAEndLoggable(Exception var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180169", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logViewToXMLException(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180170", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180170";
   }

   public static Loggable logViewToXMLExceptionLoggable(String var0, Exception var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180170", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnNoNullCiphersAllowed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180171", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180171";
   }

   public static Loggable logWarnNoNullCiphersAllowedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180171", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorNoNullCiphersAllowed() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180172", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180172";
   }

   public static Loggable logErrorNoNullCiphersAllowedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180172", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorInvalidPrivateKeyInfo(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180173", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180173";
   }

   public static Loggable logErrorInvalidPrivateKeyInfoLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("180173", var3, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorInvalidPrivateKeyStoreInfo(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180174", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180174";
   }

   public static Loggable logErrorInvalidPrivateKeyStoreInfoLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180174", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorInvalidTrustCertStoreInfo(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180175", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180175";
   }

   public static Loggable logErrorInvalidTrustCertStoreInfoLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180175", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorInvalidTrustCertificate(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180176", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180176";
   }

   public static Loggable logErrorInvalidTrustCertificateLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("180176", var2, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorInvalidServerTrustCertificate(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180177", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180177";
   }

   public static Loggable logErrorInvalidServerTrustCertificateLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180177", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logInfoLLEEncryptBitsDowngrade(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180178", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180178";
   }

   public static Loggable logInfoLLEEncryptBitsDowngradeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180178", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logWarnIgnoreSSLwithSDP(String var0) {
      Object[] var1 = new Object[]{var0};
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180179", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180179";
   }

   public static Loggable logWarnIgnoreSSLwithSDPLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("180179", var1, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   public static String logErrorSdpClassNotFound() {
      Object[] var0 = new Object[0];
      WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("180180", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.class.getClassLoader()));
      return "180180";
   }

   public static Loggable logErrorSdpClassNotFoundLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("180180", var0, "weblogic.wtc.WTCLogLocalizer", WTCLogger.MessageLoggerInitializer.INSTANCE.messageLogger, WTCLogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = WTCLogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = WTCLogger.findMessageLogger();
      }
   }
}
