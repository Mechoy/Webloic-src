package weblogic.cluster;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import weblogic.i18n.Localizer;
import weblogic.i18ntools.L10nLookup;

public class ClusterTextTextFormatter {
   private Localizer l10n;
   private boolean format = false;

   public ClusterTextTextFormatter() {
      this.l10n = L10nLookup.getLocalizer(Locale.getDefault(), "weblogic.cluster.ClusterTextTextLocalizer", ClusterTextTextFormatter.class.getClassLoader());
   }

   public ClusterTextTextFormatter(Locale var1) {
      this.l10n = L10nLookup.getLocalizer(var1, "weblogic.cluster.ClusterTextTextLocalizer", ClusterTextTextFormatter.class.getClassLoader());
   }

   public static ClusterTextTextFormatter getInstance() {
      return new ClusterTextTextFormatter();
   }

   public static ClusterTextTextFormatter getInstance(Locale var0) {
      return new ClusterTextTextFormatter(var0);
   }

   public void setExtendedFormat(boolean var1) {
      this.format = var1;
   }

   public boolean getExtendedFormat() {
      return this.format;
   }

   public String startingClusterService() {
      String var1 = "";
      String var2 = "startingClusterService";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String displayMulticastMonitorMessage(String var1) {
      String var2 = "";
      String var3 = "displayMulticastMonitorMessage";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCannotChangeClusterWhileServerReferredToInMigratableTarget(String var1, String var2) {
      String var3 = "";
      String var4 = "CannotChangeClusterWhileServerReferredToInMigratableTarget";
      String var5 = "Cluster";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getCannotDeleteServerException(String var1) {
      String var2 = "";
      String var3 = "CannotDeleteServerException";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCannotDeleteClusterException(String var1) {
      String var2 = "";
      String var3 = "CannotDeleteClusterException";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCannotDeleteMigratableTargetException(String var1) {
      String var2 = "";
      String var3 = "CannotDeleteMigratableTargetException";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigratableTargetInvViolation_1A(String var1) {
      String var2 = "";
      String var3 = "MigratableTargetInvViolation_1A";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigratableTargetInvViolation_1B(String var1) {
      String var2 = "";
      String var3 = "MigratableTargetInvViolation_1B";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigratableTargetInvViolation_1C(String var1) {
      String var2 = "";
      String var3 = "MigratableTargetInvViolation_1C";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigratableTargetInvViolation_1D(String var1) {
      String var2 = "";
      String var3 = "MigratableTargetInvViolation_1D";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigratableTargetInvViolation_2(String var1) {
      String var2 = "";
      String var3 = "MigratableTargetInvViolation_2";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigratableTargetInvViolation_3(String var1) {
      String var2 = "";
      String var3 = "MigratableTargetInvViolation_3";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigratableTargetInvViolation_4(String var1) {
      String var2 = "";
      String var3 = "MigratableTargetInvViolation_4";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigratableTargetSubSystemName() {
      String var1 = "";
      String var2 = "MigratableTargetSubSystemName";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getCannotSetConstrainedCandidateServersException(String var1) {
      String var2 = "";
      String var3 = "CannotSetConstrainedCandidateServersException";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCannotSetUserPreferredServerException(String var1) {
      String var2 = "";
      String var3 = "CannotSetUserPreferredServerException";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCannotRemoveUserPreferredServerException(String var1) {
      String var2 = "";
      String var3 = "CannotRemoveUserPreferredServerException";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCannotSetClusterException(String var1) {
      String var2 = "";
      String var3 = "CannotSetClusterException";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getAutomaticModeNotSupportedException(String var1) {
      String var2 = "";
      String var3 = "AutomaticModeNotSupportedException";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigrationTaskStatusInProgress() {
      String var1 = "";
      String var2 = "MigrationTaskStatusInProgress";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskStatusDone() {
      String var1 = "";
      String var2 = "MigrationTaskStatusDone";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskStatusFailed() {
      String var1 = "";
      String var2 = "MigrationTaskStatusFailed";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskStatusCanceled() {
      String var1 = "";
      String var2 = "MigrationTaskStatusCanceled";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskStatusQIsTheSourceServerDown() {
      String var1 = "";
      String var2 = "MigrationTaskStatusQIsTheSourceServerDown";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskStatusQIsTheDestinationServerDown() {
      String var1 = "";
      String var2 = "MigrationTaskStatusQIsTheDestinationServerDown";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskTitle(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "MigrationTaskTitle";
      String var6 = "Cluster";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String getMigrationTaskCannotCancelHere() {
      String var1 = "";
      String var2 = "MigrationTaskCannotCancelHere";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskErrorCandidateServerMustNotBeEmpty() {
      String var1 = "";
      String var2 = "MigrationTaskErrorCandidateServerMustNotBeEmpty";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskErrorDestinationMustNotBeCurrentlyActiveServer() {
      String var1 = "";
      String var2 = "MigrationTaskErrorDestinationMustNotBeCurrentlyActiveServer";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskErrorDestinationMustBeMemberOfCandidiateServers() {
      String var1 = "";
      String var2 = "MigrationTaskErrorDestinationMustBeMemberOfCandidiateServers";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskErrorUnableToDetermineListenAddressFromConfig(String var1) {
      String var2 = "";
      String var3 = "MigrationTaskErrorUnableToDetermineListenAddressFromConfig";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigrationTaskLoglineJTAMigrationStarted(String var1, String var2) {
      String var3 = "";
      String var4 = "MigrationTaskLoglineJTAMigrationStarted";
      String var5 = "Cluster";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getMigrationTaskLoglineMigrationStarted(String var1, String var2) {
      String var3 = "";
      String var4 = "MigrationTaskLoglineMigrationStarted";
      String var5 = "Cluster";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getMigrationErrorDestinationNotAmongCandidateServers(String var1, String var2) {
      String var3 = "";
      String var4 = "MigrationErrorDestinationNotAmongCandidateServers";
      String var5 = "Cluster";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getMigrationStarted(String var1, String var2, String var3) {
      String var4 = "";
      String var5 = "MigrationStarted";
      String var6 = "Cluster";
      Object[] var7 = new Object[]{var1, var2, var3};
      String var8 = MessageFormat.format(this.l10n.get(var5), var7);
      if (this.getExtendedFormat()) {
         DateFormat var9 = DateFormat.getDateTimeInstance(2, 1);
         var4 = "<" + var9.format(new Date()) + "><" + var6 + "><" + var5 + "> ";
      }

      return var4 + var8;
   }

   public String getMigrationSucceeded(String var1) {
      String var2 = "";
      String var3 = "MigrationSucceeded";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigrationFailed(String var1, String var2) {
      String var3 = "";
      String var4 = "MigrationFailed";
      String var5 = "Cluster";
      Object[] var6 = new Object[]{var1, var2};
      String var7 = MessageFormat.format(this.l10n.get(var4), var6);
      if (this.getExtendedFormat()) {
         DateFormat var8 = DateFormat.getDateTimeInstance(2, 1);
         var3 = "<" + var8.format(new Date()) + "><" + var5 + "><" + var4 + "> ";
      }

      return var3 + var7;
   }

   public String getMigrationUnknownDestinationServer(String var1) {
      String var2 = "";
      String var3 = "MigrationUnknownDestinationServer";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigrationUnknownMigratableTarget(String var1) {
      String var2 = "";
      String var3 = "MigrationUnknownMigratableTarget";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getMigrationJTAPrefix() {
      String var1 = "";
      String var2 = "MigrationJTAPrefix";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getMigrationTaskUserStopDestinationNotReachable(String var1) {
      String var2 = "";
      String var3 = "MigrationTaskUserStopDestinationNotReachable";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String IncorrectMigratableServerName(String var1) {
      String var2 = "";
      String var3 = "IncorrectMigratableServerName";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String MigratableServerIsNotInCluster(String var1) {
      String var2 = "";
      String var3 = "MigratableServerIsNotInCluster";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCannotEnableAutoMigrationWithoutLeasing(String var1) {
      String var2 = "";
      String var3 = "CannotEnableAutoMigrationWithoutLeasing";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getCannotEnableAutoMigrationWithoutLeasing2() {
      String var1 = "";
      String var2 = "CannotEnableAutoMigrationWithoutLeasing2";
      String var3 = "Cluster";
      Object[] var4 = new Object[0];
      String var5 = MessageFormat.format(this.l10n.get(var2), var4);
      if (this.getExtendedFormat()) {
         DateFormat var6 = DateFormat.getDateTimeInstance(2, 1);
         var1 = "<" + var6.format(new Date()) + "><" + var3 + "><" + var2 + "> ";
      }

      return var1 + var5;
   }

   public String getNodemanagerRequiredOnCandidateServers(String var1) {
      String var2 = "";
      String var3 = "NodemanagerRequiredOnCandidateServers";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getIllegalMigrationPolicy(String var1) {
      String var2 = "";
      String var3 = "IllegalMigrationPolicy";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getIllegalAttemptToSetPostScriptFailure(String var1) {
      String var2 = "";
      String var3 = "IllegalAttemptToSetPostScriptFailure";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getIllegalAttemptToSetRestartOnFailure(String var1) {
      String var2 = "";
      String var3 = "IllegalAttemptToSetRestartOnFailure";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getIllegalAttemptToSetSecondsBetweenRestarts(String var1) {
      String var2 = "";
      String var3 = "IllegalAttemptToSetSecondsBetweenRestarts";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }

   public String getIllegalAttemptToSetNumberOfRestartAttempts(String var1) {
      String var2 = "";
      String var3 = "IllegalAttemptToSetNumberOfRestartAttempts";
      String var4 = "Cluster";
      Object[] var5 = new Object[]{var1};
      String var6 = MessageFormat.format(this.l10n.get(var3), var5);
      if (this.getExtendedFormat()) {
         DateFormat var7 = DateFormat.getDateTimeInstance(2, 1);
         var2 = "<" + var7.format(new Date()) + "><" + var4 + "><" + var3 + "> ";
      }

      return var2 + var6;
   }
}
