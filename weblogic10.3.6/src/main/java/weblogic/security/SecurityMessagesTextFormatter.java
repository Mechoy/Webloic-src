package weblogic.security;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class SecurityMessagesTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public SecurityMessagesTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.security.SecurityMessagesTextLocalizer", SecurityMessagesTextFormatter.class.getClassLoader());
   }

   public SecurityMessagesTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.security.SecurityMessagesTextLocalizer", SecurityMessagesTextFormatter.class.getClassLoader());
   }

   public static SecurityMessagesTextFormatter getInstance() {
      return new SecurityMessagesTextFormatter();
   }

   public static SecurityMessagesTextFormatter getInstance(Locale var0) {
      return new SecurityMessagesTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String getPasswordPromptMessage() {
      String var1 = "";
      String var2 = "PasswordPromptMessage";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getPasswordPromptMessageRenter() {
      String var1 = "";
      String var2 = "PasswordPromptMessageRenter";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getUsernamePromptMessage() {
      String var1 = "";
      String var2 = "UsernamePromptMessage";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getCallbackWarningMessage() {
      String var1 = "";
      String var2 = "CallbackWarningMessage";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getCallbackErrorMessage() {
      String var1 = "";
      String var2 = "CallbackErrorMessage";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getNewAdministrativeUserPassCreate() {
      String var1 = "";
      String var2 = "getNewAdminstrativeUserPassCreated";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getAdminUserTooShort() {
      String var1 = "";
      String var2 = "getAdminUserTooShort";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getAdminPassTooShort() {
      String var1 = "";
      String var2 = "getAdminPassTooShort";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getPasswordsNoMatch() {
      String var1 = "";
      String var2 = "PassNoMatch";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getPasswordsNoMatchBoom() {
      String var1 = "";
      String var2 = "PassNoMatchBoom";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getTryAgainMessage() {
      String var1 = "";
      String var2 = "TryAgainPassNoMatchBoom";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getFailedCreateAdminUser() {
      String var1 = "";
      String var2 = "failCreateAdminUser";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getSSLClientTrustKeyStoreConfigError() {
      String var1 = "";
      String var2 = "SSLClientTrustKeyStoreConfigError";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getSSLClientTrustKeyStoreSyntax() {
      String var1 = "";
      String var2 = "SSLClientTrustKeyStoreSyntax";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getUserKeyConfigCreatePrompt(String var1, String var2) {
      String var3 = "";
      String var4 = "UserKeyConfigCreatePrompt";
      String var5 = "Security Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getUserKeyConfigCreateAffirmative() {
      String var1 = "";
      String var2 = "UserKeyConfigCreateAffirmative";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getUserKeyConfigCreateNegative() {
      String var1 = "";
      String var2 = "UserKeyConfigCreateNegative";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getUserKeyConfigCreateConfig(String var1, String var2) {
      String var3 = "";
      String var4 = "UserKeyConfigCreateConfirm";
      String var5 = "Security Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getUserKeyConfigCreateFailure() {
      String var1 = "";
      String var2 = "UserKeyConfigCreateFailure";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getUsingExistingKeyFile() {
      String var1 = "";
      String var2 = "UsingExistingKeyFile";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getUserKeyConfigCreateNoPrompt() {
      String var1 = "";
      String var2 = "UserKeyConfigCreateNoPrompt";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getSecurityPre90UpgradeNotPossible(String var1, String var2) {
      String var3 = "";
      String var4 = "SecurityPre90UpgradeNotPossible";
      String var5 = "Security Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getSecurityPre90UpgradeNameSolution() {
      String var1 = "";
      String var2 = "SecurityPre90UpgradeNameSolution";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getSecurityPre90UpgradeMissingObjectNameProblem() {
      String var1 = "";
      String var2 = "SecurityPre90UpgradeMissingObjectNameProblem";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getSecurityPre90UpgradeDuplicateObjectNameProblem(String var1) {
      String var2 = "";
      String var3 = "SecurityPre90UpgradeDuplicateObjectNameProblem";
      String var4 = "Security Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getSecurityPre90UpgradeMissingRealmChildProblem(String var1, String var2) {
      String var3 = "";
      String var4 = "SecurityPre90UpgradeMissingRealmChildProblem";
      String var5 = "Security Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getSecurityPre90UpgradeRealmChildRefersToAnotherRealmProblem(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "SecurityPre90UpgradeRealmChildRefersToAnotherRealmProblem";
      String var6 = "Security Text";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String getSecurityPre90UpgradeRealmChildRefersToNoRealmProblem(String var1, String var2) {
      String var3 = "";
      String var4 = "SecurityPre90UpgradeRealmChildRefersToNoRealmProblem";
      String var5 = "Security Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getSecurityPre90UpgradeUnreferencedRealmChildProblemProblem(String var1) {
      String var2 = "";
      String var3 = "SecurityPre90UpgradeUnreferencedRealmChildProblem";
      String var4 = "Security Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getSecurityPre90UpgradeNonStandardRealmChildObjectNameProblem(String var1, String var2, String var3, String var4) {
      String var5 = "";
      String var6 = "SecurityPre90UpgradeNonStandardRealmChildObjectNameProblem";
      String var7 = "Security Text";
      Object[] var8 = new Object[]{var1, var2, var3, var4};
      String var9 = MessageFormat.format(this.l10n.get(var6), var8);
      if (this.getExtendedFormat()) {
         DateFormat var10 = DateFormat.getDateTimeInstance(2, 1);
         var5 = "<" + var10.format(new Date()) + "><" + var7 + "><" + var6 + "> ";
      }

      return var5 + var9;
   }

   public String getSecurityPre90UpgradeNonStandardRealmObjectNameProblem(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "SecurityPre90UpgradeNonStandardRealmObjectNameProblem";
      String var6 = "Security Text";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String getSecurityPre90UpgradeDuplicateRealmChildDisplayNameProblem(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "SecurityPre90UpgradeDuplicateRealmChildDisplayNameProblem";
      String var6 = "Security Text";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String getSecurityPre90UpgradeDuplicateRealmChildReferencesProblem(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "SecurityPre90UpgradeDuplicateRealmChildReferencesProblem";
      String var6 = "Security Text";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String getSecurityPre90UpgradeDuplicateRealmDisplayNameProblem(String var1) {
      String var2 = "";
      String var3 = "SecurityPre90UpgradeDuplicateRealmDisplayNameProblem";
      String var4 = "Security Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getSecurityPre90UpgradeRealmCertPathBuilderNotChildProblem(String var1, String var2) {
      String var3 = "";
      String var4 = "SecurityPre90UpgradeRealmCertPathBuilderNotChildProblem";
      String var5 = "Security Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getSecurityPre90UpgradeRealmCertPathBuilderNotChildSolution() {
      String var1 = "";
      String var2 = "SecurityPre90UpgradeRealmCertPathBuilderNotChildSolution";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getSecurityPre90UpgradeMultipleDefaultRealmsProblem(String var1, String var2) {
      String var3 = "";
      String var4 = "SecurityPre90UpgradeMultipleDefaultRealmsProblem";
      String var5 = "Security Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getSecurityPre90UpgradeMultipleDefaultRealmsSolution() {
      String var1 = "";
      String var2 = "SecurityPre90UpgradeMultipleDefaultRealmsSolution";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getSecurityPre90UpgradeMissingUserLockoutManagerProblem(String var1) {
      String var2 = "";
      String var3 = "SecurityPre90UpgradeMissingUserLockoutManagerProblem";
      String var4 = "Security Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getSecurityPre90UpgradeMissingUserLockoutManagerSolution() {
      String var1 = "";
      String var2 = "SecurityPre90UpgradeMissingUserLockoutManagerSolution";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getSecurityPre90UpgradeUseDeprecatedWebResourceProblem(String var1) {
      String var2 = "";
      String var3 = "SecurityPre90UpgradeUseDeprecatedWebResourceProblem";
      String var4 = "Security Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getSecurityPre90UpgradeUseDeprecatedWebResourceSolution() {
      String var1 = "";
      String var2 = "SecurityPre90UpgradeUseDeprecatedWebResourceSolution";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getUnknownSecurityProviderTypeError(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "UnknownSecurityProviderTypeError";
      String var6 = "Security Text";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String getEmptyActiveTypeError() {
      String var1 = "";
      String var2 = "EmptyActiveTypeError";
      String var3 = "Security Text";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getSelfSignedCertificateInChainError(String var1) {
      String var2 = "";
      String var3 = "SelfSignedCertificateInChainError";
      String var4 = "Security Text";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCertificateNotSignedByIssuerError(String var1, String var2) {
      String var3 = "";
      String var4 = "CertificateNotSignedByIssuerError";
      String var5 = "Security Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getIssuerDNMismatchError(String var1, String var2) {
      String var3 = "";
      String var4 = "IssuerDNMismatchError";
      String var5 = "Security Text";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }
}
