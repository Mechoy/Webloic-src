package weblogic.upgrade.domain;

import weblogic.upgrade.domain.configuration.NodeManagerCredentialsPlugIn;
import weblogic.upgrade.domain.directorybackup.DomainDirectoryBackupPlugIn;
import weblogic.upgrade.domain.directoryselection.DomainDirectorySelectionPlugIn;
import weblogic.upgrade.domain.directoryselection.OptionalGroupsSelectionPlugIn;

public interface DomainPlugInConstants {
   String DOMAIN_DIRECTORY_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".DOMAIN_DIRECTORY_KEY";
   String DOMAIN_CONFIGURATION_SOURCE_FILE = DomainDirectorySelectionPlugIn.class.getName() + ".DOMAIN_CONFIGURATION_SOURCE_FILE";
   String DOMAIN_BEAN_TREE_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".DOMAIN_BEAN_TREE_KEY";
   String DOMAIN_CONFIGURATION_VERSION_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".DOMAIN_CONFIGURATION_VERSION_KEY";
   String DOMAIN_BACKUP_FILE_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".DOMAIN_BACKUP_FILE_KEY";
   String DOMAIN_BACKUP_DIRECTORY_KEY = DomainDirectoryBackupPlugIn.class.getName() + ".DOMAIN_BACKUP_DIRECTORY_KEY";
   String ADMIN_SERVER_URL_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".ADMIN_SERVER_URL_KEY";
   String ADMIN_SERVER_NAME_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".ADMIN_SERVER_NAME_KEY";
   String SERVER_NAMES_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".SERVER_NAMES_KEY";
   String HAS_CONFIGURATION_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".HAS_CONFIGURATION_KEY";
   String PLATFORM_DOMAIN_INFO_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".PLATFORM_DOMAIN_INFO_KEY";
   String PRODUCT_REGISTRY_INFO_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".PRODUCT_REGISTRY_INFO_KEY";
   String OPTIONAL_GROUPS_KEY = OptionalGroupsSelectionPlugIn.class.getName() + ".OPTIONAL_GROUPS_KEY";
   String DOMAIN_DIRECTORY_BACKUP_SELECTED_VALUE = "DOMAIN_DIRECTORY_BACKUP_SELECTED_VALUE";
   String DOMAIN_DIRECTORY_BACKUP_LOG_FILES_INCLUDED_SELECTED_VALUE = "DOMAIN_DIRECTORY_BACKUP_LOG_FILES_INCLUDED_SELECTED_VALUE";
   String CONFIGURATION_ONLY_SELECTED_VALUE = "CONFIGURATION_ONLY_SELECTED_VALUE";
   String SKIP_BACKWARDS_COMPATIBILITY_FLAGS_SELECTED_VALUE = "SKIP_BACKWARDS_COMPATIBILITY_FLAGS_SELECTED_VALUE";
   String DELETE_OLD_FORMAT_JMS_MESSAGES_IN_DB = "DELETE_OLD_FORMAT_JMS_MESSAGES_IN_DB";
   String CLASS_COMPATIBILITY_INSPECTOR_SELECTED_VALUE = "CLASS_COMPATIBILITY_INSPECTOR_SELECTED_VALUE";
   String MIGRATABLE_SERVERS_UPGRADE_SELECTED_VALUE = "MIGRATABLE_SERVERS_UPGRADE_SELECTED_VALUE";
   String SKIP_LOG_FILES_BACKUP_SELECTED_VALUE = "SKIP_LOG_FILES_BACKUP_SELECTED_VALUE";
   String SKIP_LOG_FILES_BACKUP_KEY = DomainDirectoryBackupPlugIn.class.getName() + ".SKIP_LOG_FILES_BACKUP_KEY";
   String NM_CREDENTIALS_USERNAME_KEY = NodeManagerCredentialsPlugIn.class.getName() + ".NM_CREDENTIALS_USERNAME_KEY";
   String NM_CREDENTIALS_PASSWORD_KEY = NodeManagerCredentialsPlugIn.class.getName() + ".NM_CREDENTIALS_PASSWORD_KEY";
   String NM_CREDENTIALS_CHECK_KEY = NodeManagerCredentialsPlugIn.class.getName() + ".NM_CREDENTIALS_CHECK_KEY";
   String SYS_PROP_DO_BACKUP = "weblogic.upgrade.backup";
   String SYS_PROP_ADMIN_SERVER_NAME = "weblogic.upgrade.adminserver";
   String DOMAIN_DIRECTORY_SELECTION_DATE_KEY = DomainDirectorySelectionPlugIn.class.getName() + ".DOMAIN_DIRECTORY_SELECTION_DATE_KEY";
}
