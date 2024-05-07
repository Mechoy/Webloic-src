package weblogic.deploy.api.tools.samples;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class J2EEDeployerToolTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public J2EEDeployerToolTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.deploy.api.tools.samples.J2EEDeployerToolTextLocalizer", J2EEDeployerToolTextFormatter.class.getClassLoader());
   }

   public J2EEDeployerToolTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.deploy.api.tools.samples.J2EEDeployerToolTextLocalizer", J2EEDeployerToolTextFormatter.class.getClassLoader());
   }

   public static J2EEDeployerToolTextFormatter getInstance() {
      return new J2EEDeployerToolTextFormatter();
   }

   public static J2EEDeployerToolTextFormatter getInstance(Locale var0) {
      return new J2EEDeployerToolTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String hostOption() {
      String var1 = "";
      String var2 = "260500";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String userOption() {
      String var1 = "";
      String var2 = "260501";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String passwordOption() {
      String var1 = "";
      String var2 = "260502";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String verboseOption() {
      String var1 = "";
      String var2 = "260503";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String quietOption() {
      String var1 = "";
      String var2 = "260504";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String useplanOption() {
      String var1 = "";
      String var2 = "260505";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String saveplanOption() {
      String var1 = "";
      String var2 = "260506";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String targetsOption() {
      String var1 = "";
      String var2 = "260507";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String pluginOption() {
      String var1 = "";
      String var2 = "260508";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String configOption() {
      String var1 = "";
      String var2 = "260509";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String startCommand() {
      String var1 = "";
      String var2 = "260510";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String stopCommand() {
      String var1 = "";
      String var2 = "260511";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String distributeCommand() {
      String var1 = "";
      String var2 = "260512";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String undeployCommand() {
      String var1 = "";
      String var2 = "260513";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String listallCommand() {
      String var1 = "";
      String var2 = "260514";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String liststartedCommand() {
      String var1 = "";
      String var2 = "260515";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String liststoppedCommand() {
      String var1 = "";
      String var2 = "260516";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String fileOrAppNameOption() {
      String var1 = "";
      String var2 = "260517";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String badArchive(String var1, String var2) {
      String var3 = "";
      String var4 = "260518";
      String var5 = "J2EE Deployment Samples";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noConfig(String var1) {
      String var2 = "";
      String var3 = "260519";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String badConfig(String var1, String var2) {
      String var3 = "";
      String var4 = "260520";
      String var5 = "J2EE Deployment Samples";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String badHost() {
      String var1 = "";
      String var2 = "260521";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String badUser() {
      String var1 = "";
      String var2 = "260522";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String badUsePlan(String var1) {
      String var2 = "";
      String var3 = "260523";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String badSavePlan(String var1) {
      String var2 = "";
      String var3 = "260524";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String noCommand() {
      String var1 = "";
      String var2 = "260525";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String noTargets(String var1) {
      String var2 = "";
      String var3 = "260526";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String missingFileOrApp(String var1) {
      String var2 = "";
      String var3 = "260527";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String factoryClassName(String var1) {
      String var2 = "";
      String var3 = "260528";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String nameValue(String var1, String var2) {
      String var3 = "";
      String var4 = "260529";
      String var5 = "J2EE Deployment Samples";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noFactory() {
      String var1 = "";
      String var2 = "260530";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String invalidFactory(String var1) {
      String var2 = "";
      String var3 = "260531";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String invalidFactoryException(String var1, String var2) {
      String var3 = "";
      String var4 = "260532";
      String var5 = "J2EE Deployment Samples";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String registeredFactory(String var1) {
      String var2 = "";
      String var3 = "260533";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String portOption() {
      String var1 = "";
      String var2 = "260534";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String badPort() {
      String var1 = "";
      String var2 = "260535";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String typeOption() {
      String var1 = "";
      String var2 = "260536";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String badType(String var1) {
      String var2 = "";
      String var3 = "260537";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String noType() {
      String var1 = "";
      String var2 = "260538";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String debugOption() {
      String var1 = "";
      String var2 = "260539";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String noSuchTarget(String var1) {
      String var2 = "";
      String var3 = "260540";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String noValidTargets() {
      String var1 = "";
      String var2 = "260541";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String listStarted() {
      String var1 = "";
      String var2 = "260542";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String listStopped() {
      String var1 = "";
      String var2 = "260543";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String listAll() {
      String var1 = "";
      String var2 = "260544";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String registerFactory() {
      String var1 = "";
      String var2 = "260545";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getDM() {
      String var1 = "";
      String var2 = "260546";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String collectTargets() {
      String var1 = "";
      String var2 = "260547";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String listHeader() {
      String var1 = "";
      String var2 = "260548";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String distribute(String var1) {
      String var2 = "";
      String var3 = "260549";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String showStatus(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "260550";
      String var6 = "J2EE Deployment Samples";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String progressTmid(String var1) {
      String var2 = "";
      String var3 = "260551";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String start(String var1) {
      String var2 = "";
      String var3 = "260552";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String noTmidsToDeploy(String var1, String var2) {
      String var3 = "";
      String var4 = "260553";
      String var5 = "J2EE Deployment Samples";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String noTmidsToStop(String var1, String var2) {
      String var3 = "";
      String var4 = "260554";
      String var5 = "J2EE Deployment Samples";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String stop(String var1) {
      String var2 = "";
      String var3 = "260555";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String undeploy(String var1) {
      String var2 = "";
      String var3 = "260556";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String noTmidsToUndeploy(String var1, String var2) {
      String var3 = "";
      String var4 = "260557";
      String var5 = "J2EE Deployment Samples";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String deployCommand() {
      String var1 = "";
      String var2 = "260558";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String deploy(String var1) {
      String var2 = "";
      String var3 = "260559";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String listtargetsCommand() {
      String var1 = "";
      String var2 = "260560";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String unrecognizedArg(String var1) {
      String var2 = "";
      String var3 = "260561";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String listcommandsCommand() {
      String var1 = "";
      String var2 = "260562";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String listtypesCommand() {
      String var1 = "";
      String var2 = "260563";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String remoteOption() {
      String var1 = "";
      String var2 = "260564";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String redeployCommand() {
      String var1 = "";
      String var2 = "260565";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String redeploy(String var1) {
      String var2 = "";
      String var3 = "260566";
      String var4 = "J2EE Deployment Samples";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String noName() {
      String var1 = "";
      String var2 = "260568";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String nameOption() {
      String var1 = "";
      String var2 = "260569";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String onlyRootsAllowed() {
      String var1 = "";
      String var2 = "260570";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String stageOption() {
      String var1 = "";
      String var2 = "260571";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String modulesOption() {
      String var1 = "";
      String var2 = "260572";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String updateCommand() {
      String var1 = "";
      String var2 = "260573";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String missingPlan() {
      String var1 = "";
      String var2 = "260574";
      String var3 = "J2EE Deployment Samples";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String update(String var1, String var2) {
      String var3 = "";
      String var4 = "260575";
      String var5 = "J2EE Deployment Samples";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }
}
