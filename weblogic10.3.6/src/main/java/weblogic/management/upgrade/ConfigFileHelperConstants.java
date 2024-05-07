package weblogic.management.upgrade;

public interface ConfigFileHelperConstants {
   String FORCE_IMPLICIT_UPGRADE_IF_NEEDED = "weblogic.ForceImplicitUpgradeIfNeeded";
   boolean FORCE_IMPLICIT_UPGRADE_IF_NEEDED_DEFAULT = false;
   String[] UPGRADE_XSLT_SCRIPTS = new String[]{"weblogic/upgrade/domain/directoryselection/SelectWebLogicVersion.xsl", "weblogic/security/internal/SecurityPre90Upgrade.xsl"};
}
