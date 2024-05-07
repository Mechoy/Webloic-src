package weblogic.j2ee;

import weblogic.i18n.logging.CatalogMessage;
import weblogic.i18n.logging.MessageLogger;
import weblogic.i18n.logging.MessageLoggerRegistry;
import weblogic.i18n.logging.MessageLoggerRegistryListener;
import weblogic.logging.Loggable;

public class J2EELogger {
   private static final String LOCALIZER_CLASS = "weblogic.j2ee.J2EELogLocalizer";

   private static MessageLogger findMessageLogger() {
      return MessageLoggerRegistry.findMessageLogger(J2EELogger.class.getName());
   }

   public static String logErrorDeployingApplication(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160001", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160001";
   }

   public static Loggable logErrorDeployingApplicationLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160001", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logMBeanCreationFailure(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160032", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160032";
   }

   public static Loggable logMBeanCreationFailureLoggable(String var0, String var1, String var2, Throwable var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160032", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logFailedToUndeployMailSession(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160039", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160039";
   }

   public static Loggable logFailedToUndeployMailSessionLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160039", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logDeployedMailSession(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160040", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160040";
   }

   public static Loggable logDeployedMailSessionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160040", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUndeployedMailSession(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160041", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160041";
   }

   public static Loggable logUndeployedMailSessionLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160041", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logDebug(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160058", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160058";
   }

   public static Loggable logDebugLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160058", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logErrorCheckingWebService(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160069", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160069";
   }

   public static Loggable logErrorCheckingWebServiceLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160069", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidEJBLinkQualificationInEJBDescriptor(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160083", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160083";
   }

   public static Loggable logInvalidEJBLinkQualificationInEJBDescriptorLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("160083", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidEJBLinkQualification(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160084", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160084";
   }

   public static Loggable logInvalidEJBLinkQualificationLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160084", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidQualifiedEJBLinkInEJBDescriptor(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160085", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160085";
   }

   public static Loggable logInvalidQualifiedEJBLinkInEJBDescriptorLoggable(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      return new Loggable("160085", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidQualifiedEJBLink(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160086", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160086";
   }

   public static Loggable logInvalidQualifiedEJBLinkLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("160086", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logEJBLinkInEJBDescriptorPointsToInvalidBean(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160087", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160087";
   }

   public static Loggable logEJBLinkInEJBDescriptorPointsToInvalidBeanLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160087", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logEJBLinkPointsToInvalidBean(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160088", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160088";
   }

   public static Loggable logEJBLinkPointsToInvalidBeanLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160088", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidUnqualifiedEJBLinkInEJBDescriptor(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160089", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160089";
   }

   public static Loggable logInvalidUnqualifiedEJBLinkInEJBDescriptorLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160089", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidUnqualifiedEJBLink(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160090", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160090";
   }

   public static Loggable logInvalidUnqualifiedEJBLinkLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160090", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAmbiguousEJBLinkInEJBDescriptor(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160091", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160091";
   }

   public static Loggable logAmbiguousEJBLinkInEJBDescriptorLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("160091", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAmbiguousEJBLink(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160092", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160092";
   }

   public static Loggable logAmbiguousEJBLinkLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160092", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIncorrectInterfacesForEJBRefTypeInEJBDescriptor(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160093", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160093";
   }

   public static Loggable logIncorrectInterfacesForEJBRefTypeInEJBDescriptorLoggable(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      return new Loggable("160093", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIncorrectInterfacesForEJBRefType(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160094", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160094";
   }

   public static Loggable logIncorrectInterfacesForEJBRefTypeLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("160094", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIncorrectInterfaceNameForEJBRefInEJBDescriptor(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160095", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160095";
   }

   public static Loggable logIncorrectInterfaceNameForEJBRefInEJBDescriptorLoggable(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      return new Loggable("160095", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIncorrectInterfaceNameForEJBRef(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160096", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160096";
   }

   public static Loggable logIncorrectInterfaceNameForEJBRefLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("160096", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logDescriptorUsesInvalidEncoding(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160098", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160098";
   }

   public static Loggable logDescriptorUsesInvalidEncodingLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160098", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logCouldNotDeployRole(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160100", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160100";
   }

   public static Loggable logCouldNotDeployRoleLoggable(String var0, String var1, Throwable var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160100", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnableToResolveEJBLink(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160101", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160101";
   }

   public static Loggable logUnableToResolveEJBLinkLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160101", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logEjbLocalRefNotVisible(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160104", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160104";
   }

   public static Loggable logEjbLocalRefNotVisibleLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160104", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcSourceArgDoesNotExist(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160106", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160106";
   }

   public static Loggable logAppcSourceArgDoesNotExistLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160106", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcSourceFileNotAccessible(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160108", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160108";
   }

   public static Loggable logAppcSourceFileNotAccessibleLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160108", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcErrorCopyingFiles(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160109", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160109";
   }

   public static Loggable logAppcErrorCopyingFilesLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160109", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcCouldNotCreateDirectory(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160110", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160110";
   }

   public static Loggable logAppcCouldNotCreateDirectoryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160110", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcCanNotWriteToDirectory(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160111", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160111";
   }

   public static Loggable logAppcCanNotWriteToDirectoryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160111", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcNoValidModuleFoundInDirectory(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160112", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160112";
   }

   public static Loggable logAppcNoValidModuleFoundInDirectoryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160112", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcJarNotValid(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160113", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160113";
   }

   public static Loggable logAppcJarNotValidLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160113", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcWarNotValid(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160114", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160114";
   }

   public static Loggable logAppcWarNotValidLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160114", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcEarNotValid(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160115", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160115";
   }

   public static Loggable logAppcEarNotValidLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160115", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcUnableToContinueProcessingFile(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160117", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160117";
   }

   public static Loggable logAppcUnableToContinueProcessingFileLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160117", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcErrorAccessingFile(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160118", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160118";
   }

   public static Loggable logAppcErrorAccessingFileLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160118", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcErrorProcessingFile(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160119", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160119";
   }

   public static Loggable logAppcErrorProcessingFileLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160119", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcCantFindDeclaredModule(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160120", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160120";
   }

   public static Loggable logAppcCantFindDeclaredModuleLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160120", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcErrorsEncounteredCompilingModule(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160121", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160121";
   }

   public static Loggable logAppcErrorsEncounteredCompilingModuleLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160121", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcErrorsValidatingEar(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160122", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160122";
   }

   public static Loggable logAppcErrorsValidatingEarLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160122", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcUnableToCreateOutputArchiveRestore(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160123", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160123";
   }

   public static Loggable logAppcUnableToCreateOutputArchiveRestoreLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160123", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcUnableToCreateOutputArchive(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160124", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160124";
   }

   public static Loggable logAppcUnableToCreateOutputArchiveLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160124", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcUnableToDeleteBackupArchive(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160125", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160125";
   }

   public static Loggable logAppcUnableToDeleteBackupArchiveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160125", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcUnableToCreateBackupArchive(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160126", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160126";
   }

   public static Loggable logAppcUnableToCreateBackupArchiveLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160126", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcUnableToDeleteArchive(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160127", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160127";
   }

   public static Loggable logAppcUnableToDeleteArchiveLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160127", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logTooManyArgsForAppc() {
      Object[] var0 = new Object[0];
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160128", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160128";
   }

   public static Loggable logTooManyArgsForAppcLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("160128", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcMissingModuleAltDDFile(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160129", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160129";
   }

   public static Loggable logAppcMissingModuleAltDDFileLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160129", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcMissingApplicationAltDDFile(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160130", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160130";
   }

   public static Loggable logAppcMissingApplicationAltDDFileLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160130", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIncorrectRefTypeForEJBRefInEJBDescriptor(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160132", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160132";
   }

   public static Loggable logIncorrectRefTypeForEJBRefInEJBDescriptorLoggable(String var0, String var1, String var2, String var3, String var4) {
      Object[] var5 = new Object[]{var0, var1, var2, var3, var4};
      return new Loggable("160132", var5, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIncorrectRefTypeForEJBRef(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160133", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160133";
   }

   public static Loggable logIncorrectRefTypeForEJBRefLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160133", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidEntityCacheRefDeclared(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160134", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160134";
   }

   public static Loggable logInvalidEntityCacheRefDeclaredLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160134", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnableToResolveMessageDestinationLink(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160137", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160137";
   }

   public static Loggable logUnableToResolveMessageDestinationLinkLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160137", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logCouldNotSetAppActiveVersionState(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160138", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160138";
   }

   public static Loggable logCouldNotSetAppActiveVersionStateLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160138", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logContextPathSetForNonWarLibRef(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160139", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160139";
   }

   public static Loggable logContextPathSetForNonWarLibRefLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160139", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnresolvedOptionalPackages(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160140", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160140";
   }

   public static Loggable logUnresolvedOptionalPackagesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160140", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logLibraryInitError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160141", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160141";
   }

   public static Loggable logLibraryInitErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160141", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logLibraryWithIllegalSpecVersion(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160142", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160142";
   }

   public static Loggable logLibraryWithIllegalSpecVersionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160142", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logLibraryWithIllegalMBeanSpecVersion(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160143", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160143";
   }

   public static Loggable logLibraryWithIllegalMBeanSpecVersionLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160143", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logLibraryRegistrationError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160144", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160144";
   }

   public static Loggable logLibraryRegistrationErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160144", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logLibraryInfoMismatch(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160145", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160145";
   }

   public static Loggable logLibraryInfoMismatchLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160145", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcLibraryInfoMismatch(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160146", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160146";
   }

   public static Loggable logAppcLibraryInfoMismatchLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160146", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcLibraryRegistrationFailed() {
      Object[] var0 = new Object[0];
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160147", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160147";
   }

   public static Loggable logAppcLibraryRegistrationFailedLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("160147", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcUnreferencedLibraries(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160148", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160148";
   }

   public static Loggable logAppcUnreferencedLibrariesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160148", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnresolvedLibraryReferences(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160149", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160149";
   }

   public static Loggable logUnresolvedLibraryReferencesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160149", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIllegalAppLibSpecVersionRef(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160150", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160150";
   }

   public static Loggable logIllegalAppLibSpecVersionRefLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160150", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logRegisteredLibrary(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160151", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160151";
   }

   public static Loggable logRegisteredLibraryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160151", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnknownLibraryType(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160152", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160152";
   }

   public static Loggable logUnknownLibraryTypeLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160152", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logLibraryCleanupWarning(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160153", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160153";
   }

   public static Loggable logLibraryCleanupWarningLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160153", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logLibraryCleanupError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160154", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160154";
   }

   public static Loggable logLibraryCleanupErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160154", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logCannotFindLibrary(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160155", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160155";
   }

   public static Loggable logCannotFindLibraryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160155", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logCannotProcessLibdir(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160156", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160156";
   }

   public static Loggable logCannotProcessLibdirLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160156", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logLibraryIsNotAppLibrary(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160157", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160157";
   }

   public static Loggable logLibraryIsNotAppLibraryLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160157", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logCannotFindExtensionNameWarning(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160158", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160158";
   }

   public static Loggable logCannotFindExtensionNameWarningLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160158", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIllegalOptPackSpecVersionRefWarning(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160159", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160159";
   }

   public static Loggable logIllegalOptPackSpecVersionRefWarningLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160159", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcErrorParsingEARDescriptors(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160161", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160161";
   }

   public static Loggable logAppcErrorParsingEARDescriptorsLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160161", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcNoApplicationDDFound(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160162", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160162";
   }

   public static Loggable logAppcNoApplicationDDFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160162", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcNoModulesFound(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160163", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160163";
   }

   public static Loggable logAppcNoModulesFoundLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160163", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logErrorImportingLibrary(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160164", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160164";
   }

   public static Loggable logErrorImportingLibraryLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160164", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logSplitDirNotSupportedForLibraries(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160165", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160165";
   }

   public static Loggable logSplitDirNotSupportedForLibrariesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160165", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logValidPlanMerged(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160166", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160166";
   }

   public static Loggable logValidPlanMergedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160166", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logNoEJBDeploymentsFoundForLinkRef(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160167", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160167";
   }

   public static Loggable logNoEJBDeploymentsFoundForLinkRefLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160167", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logDescriptorMergeError(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160168", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160168";
   }

   public static Loggable logDescriptorMergeErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160168", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logDescriptorParseError(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160169", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160169";
   }

   public static Loggable logDescriptorParseErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160169", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logLibraryImport(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160170", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160170";
   }

   public static Loggable logLibraryImportLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160170", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcPlanArgDoesNotExist(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160171", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160171";
   }

   public static Loggable logAppcPlanArgDoesNotExistLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160171", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcPlanArgWrongType() {
      Object[] var0 = new Object[0];
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160172", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160172";
   }

   public static Loggable logAppcPlanArgWrongTypeLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("160172", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcPlanFileNotAccessible(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160173", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160173";
   }

   public static Loggable logAppcPlanFileNotAccessibleLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160173", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcPlanParseError(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160174", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160174";
   }

   public static Loggable logAppcPlanParseErrorLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160174", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidJMSResourceLinkInJ2EEComponent(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160175", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160175";
   }

   public static Loggable logInvalidJMSResourceLinkInJ2EEComponentLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160175", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logJMSResourceSpecifiedInResourceLinkNotFound(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160176", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160176";
   }

   public static Loggable logJMSResourceSpecifiedInResourceLinkNotFoundLoggable(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      return new Loggable("160176", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidApplication(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160177", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160177";
   }

   public static Loggable logInvalidApplicationLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160177", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIgnoringRollbackUpdateError(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160180", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160180";
   }

   public static Loggable logIgnoringRollbackUpdateErrorLoggable(String var0, Throwable var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160180", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIgnoringUndeploymentError(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160181", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160181";
   }

   public static Loggable logIgnoringUndeploymentErrorLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160181", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIgnoringAdminModeErrro(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160182", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160182";
   }

   public static Loggable logIgnoringAdminModeErrroLoggable(Throwable var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160182", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnabletoFindLifecycleJar(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160183", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160183";
   }

   public static Loggable logUnabletoFindLifecycleJarLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160183", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logInvalidJDBCResourceLinkInJ2EEComponent(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160184", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160184";
   }

   public static Loggable logInvalidJDBCResourceLinkInJ2EEComponentLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160184", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logJDBCResourceSpecifiedInResourceLinkNotFound(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160185", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160185";
   }

   public static Loggable logJDBCResourceSpecifiedInResourceLinkNotFoundLoggable(String var0, String var1, String var2, String var3, String var4, String var5) {
      Object[] var6 = new Object[]{var0, var1, var2, var3, var4, var5};
      return new Loggable("160185", var6, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logCompilingEarModule(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160186", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160186";
   }

   public static Loggable logCompilingEarModuleLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160186", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcFailedWithError() {
      Object[] var0 = new Object[0];
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160187", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160187";
   }

   public static Loggable logAppcFailedWithErrorLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("160187", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnresolvedLibraryReferencesWarning(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160188", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160188";
   }

   public static Loggable logUnresolvedLibraryReferencesWarningLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160188", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUrisDidntMatchModules(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160189", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160189";
   }

   public static Loggable logUrisDidntMatchModulesLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160189", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logDeprecatedWeblogicParam(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160191", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160191";
   }

   public static Loggable logDeprecatedWeblogicParamLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160191", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnknownWeblogicParam(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160192", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160192";
   }

   public static Loggable logUnknownWeblogicParamLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160192", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAppcRarNotValid(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160193", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160193";
   }

   public static Loggable logAppcRarNotValidLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160193", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logModulesAlreadyRunningError(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160194", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160194";
   }

   public static Loggable logModulesAlreadyRunningErrorLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160194", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIgnoreAppVersionListenerForNonVersionApp(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160195", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160195";
   }

   public static Loggable logIgnoreAppVersionListenerForNonVersionAppLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160195", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnabletoFindSingletonJar(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160196", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160196";
   }

   public static Loggable logUnabletoFindSingletonJarLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160196", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUnableToValidateDescriptor(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160197", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160197";
   }

   public static Loggable logUnableToValidateDescriptorLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160197", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logIncorrectInterfaceForEJBAnnotationTarget(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160198", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160198";
   }

   public static Loggable logIncorrectInterfaceForEJBAnnotationTargetLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160198", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logFailedToAutoLinkEjbRefMultipleInterfaces(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160199", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160199";
   }

   public static Loggable logFailedToAutoLinkEjbRefMultipleInterfacesLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160199", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logFailedToAutoLinkEjbRefNoMatches(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160200", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160200";
   }

   public static Loggable logFailedToAutoLinkEjbRefNoMatchesLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160200", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logEJBRefTargetDoesNotImplementInterface(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160201", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160201";
   }

   public static Loggable logEJBRefTargetDoesNotImplementInterfaceLoggable(String var0, String var1, String var2, String var3) {
      Object[] var4 = new Object[]{var0, var1, var2, var3};
      return new Loggable("160201", var4, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logPersistenceUnitLogConfigurationSpecified(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160202", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160202";
   }

   public static Loggable logPersistenceUnitLogConfigurationSpecifiedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160202", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logPersistenceUnitIdPropertySpecified(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160203", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160203";
   }

   public static Loggable logPersistenceUnitIdPropertySpecifiedLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160203", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logRunAsPrincipalNotFound(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160204", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160204";
   }

   public static Loggable logRunAsPrincipalNotFoundLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160204", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logAttemptToBumpUpPrivilegesWithRunAs(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160205", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160205";
   }

   public static Loggable logAttemptToBumpUpPrivilegesWithRunAsLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160205", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logOpenJPAPersistenceUnitUsesApplicationJars(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160206", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160206";
   }

   public static Loggable logOpenJPAPersistenceUnitUsesApplicationJarsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160206", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logFailedToCreateEjbRefMultipleInterfaces(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160207", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160207";
   }

   public static Loggable logFailedToCreateEjbRefMultipleInterfacesLoggable(String var0, String var1, String var2) {
      Object[] var3 = new Object[]{var0, var1, var2};
      return new Loggable("160207", var3, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logModuleUriDoesNotExist(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160210", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160210";
   }

   public static Loggable logModuleUriDoesNotExistLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160210", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logOutputLocationExists(String var0) {
      Object[] var1 = new Object[]{var0};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160214", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160214";
   }

   public static Loggable logOutputLocationExistsLoggable(String var0) {
      Object[] var1 = new Object[]{var0};
      return new Loggable("160214", var1, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logFilteringConfigurationIgnored(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160218", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160218";
   }

   public static Loggable logFilteringConfigurationIgnoredLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160218", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logUsingDefaultPersistenceProvider(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160219", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160219";
   }

   public static Loggable logUsingDefaultPersistenceProviderLoggable(String var0, String var1) {
      Object[] var2 = new Object[]{var0, var1};
      return new Loggable("160219", var2, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String logCompilationComplete() {
      Object[] var0 = new Object[0];
      J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger.log(new CatalogMessage("160220", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.class.getClassLoader()));
      return "160220";
   }

   public static Loggable logCompilationCompleteLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("160220", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   public static String getJPA1UnsupportedOperationMsg() {
      Object[] var0 = new Object[0];
      return (new Loggable("160225", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader())).getMessage();
   }

   public static Loggable getJPA1UnsupportedOperationMsgLoggable() {
      Object[] var0 = new Object[0];
      return new Loggable("160225", var0, "weblogic.j2ee.J2EELogLocalizer", J2EELogger.MessageLoggerInitializer.INSTANCE.messageLogger, J2EELogger.class.getClassLoader());
   }

   private static final class MessageLoggerInitializer implements MessageLoggerRegistryListener {
      private static final MessageLoggerInitializer INSTANCE = new MessageLoggerInitializer();
      private MessageLogger messageLogger = J2EELogger.findMessageLogger();

      private MessageLoggerInitializer() {
         MessageLoggerRegistry.addMessageLoggerRegistryListener(this);
      }

      public void messageLoggerRegistryUpdated() {
         this.messageLogger = J2EELogger.findMessageLogger();
      }
   }
}
