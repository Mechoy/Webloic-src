package weblogic.management;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.store.SystemProperties;
import weblogic.utils.collections.EnumerationIterator;
import weblogic.utils.collections.PropertiesHelper;

public class SpecialPropertiesHelper {
   private static Set specialProperties = new HashSet();
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationRuntime");
   public static final String PRODUCTION_MODE_ENABLED_PROP = "weblogic.ProductionModeEnabled";
   public static final String MBEAN_AUDITING_ENABLED_PROP = "weblogic.AdministrationMBeanAuditingEnabled";
   public static final String LEGAL_BYPASS_ON_PARSING_PROP = "weblogic.mbeanLegalClause.ByPass";
   public static final String ADMIN_SSLTRUSTCA_PROP = "weblogic.security.SSL.trustedCAKeyStore";
   public static final String SAVE_DOMAIN_INTERVAL_PROP = "weblogic.management.saveDomainMillis";
   static final String WLEC_TIMEOUT_PROP = "weblogic.CORBA.connectionPoolSoTimeout";
   static final String ADMIN_CONFIGFILE_PROP = "weblogic.ConfigFile";
   static final String ADMIN_DEBUGCATEGORY_PROP = "Debug";
   static final String ADMIN_DEBUGFILTER_PROP = "DebugFilter";
   static final String ADMIN_GEN_DEFAULT_CONFIG_PROP = "weblogic.management.GenerateDefaultConfig";
   static final String ADMIN_HOME_PROP = "weblogic.home";
   static final String ADMIN_SSLDEBUG1_PROP = "ssl.debug";
   static final String ADMIN_SSLDEBUG2_PROP = "weblogic.security.SSL.verbose";
   static final String ADMIN_SSLDEBUG3_PROP = "weblogic.security.ssl.verbose";
   static final String ADMIN_SSLHOSTNAMEVER_IGNORE1_PROP = "weblogic.security.SSL.ignoreHostnameVerification";
   static final String ADMIN_SSLHOSTNAMEVER_IGNORE2_PROP = "weblogic.security.SSL.ignoreHostnameVerify";
   static final String ADMIN_SSLHOSTNAMEVER_CLASS_PROP = "weblogic.security.SSL.hostnameVerifier";
   public static final String ADMIN_SSL_MINIMUM_PROTOCOL_VERSION_PROP = "weblogic.security.SSL.minimumProtocolVersion";
   public static final String ADMIN_SSLVERSION_PROP = "weblogic.security.SSL.protocolVersion";
   public static final String ADMIN_SSLENFORCECONSTRAINT_PROP = "weblogic.security.SSL.enforceConstraints";
   public static final String ADMIN_ROLEMAPPERFACTORY_PROP = "weblogic.security.jacc.RoleMapperFactory.provider";
   public static final String ADMIN_ANONYMOUSADMINLOOKUPENABLED_PROP = "weblogic.management.anonymousAdminLookupEnabled";
   public static final String ADMIN_CLEAR_TEXT_CREDENTIAL_ACCESS_ENABLED = "weblogic.management.clearTextCredentialAccessEnabled";
   public static final String ADMIN_IACACHETTL_PROP = "weblogic.security.identityAssertionTTL";
   static final String DOMAIN_PROP = "Domain";
   static final String NAME_PROP = "Name";
   static final String STORE_BOOT_IDENTITY_PROP = "weblogic.system.StoreBootIdentity";
   static final String BOOT_IDENTITY_FILE_PROP = "weblogic.system.BootIdentityFile";
   static final String REMOVE_BOOT_IDENTITY_PROP = "weblogic.system.RemoveBootIdentity";
   static final String NODE_MANAGER_BOOT_PROP = "weblogic.system.NodeManagerBoot";
   static final String FULLY_DELEGATE_AUTH_PROP = "weblogic.security.fullyDelegateAuthorization";
   static final String ADMIN_ANON_USERNAME_PROP = "weblogic.security.anonymousUserName";
   static final String ENTITY_EXPANSION_LIMIT_PROP = "weblogic.apache.xerces.maxentityrefs";
   public static final String ADMIN_HIERARCHY_GROUP_PROP = "weblogic.security.hierarchyGroupMemberships";
   public static final String LDAP_DELEGATE_POOL_SIZE_PROP = "weblogic.security.providers.authentication.LDAPDelegatePoolSize";
   public static final String SECURITY_FW_SUBJECT_MANAGER_CLASS_NAME_PROP = "weblogic.security.SubjectManager";
   public static final String SECURITY_FW_DELEGATE_CLASS_NAME_PROP = "weblogic.security.SecurityServiceManagerDelegate";
   static final String WTC_TRACELEVEL = "weblogic.wtc.TraceLevel";
   static final String WTC_PASSWORDKEY = "weblogic.wtc.PasswordKey";
   static final String CLASSLOADER_PREPROCESSOR = "weblogic.classloader.preprocessor";
   static final String OCI_SELECT_BLOB_CHUNKSIZE = "weblogic.oci.selectBlobChunkSize";
   static final String NUMBER_POSIX_SOCKET_READERS = "weblogic.PosixSocketReaders";
   static final String USE_EXTENTED_SESSION_FORMAT = "weblogic.servlet.useExtendedSessionFormat";
   static final String WEBSERVICE_I18N_CHARSET = "weblogic.webservice.i18n.charset";
   static final String WEBSERVICE_SSL_TRUSTEDCERTFILE = "weblogic.webservice.client.ssl.trustedcertfile";
   static final String NODEMANAGER_SERVICE_ENABLED = "weblogic.nodemanager.ServiceEnabled";
   public static final String WEBSS_VERBOSE = "weblogic.webservice.verbose";
   public static final String DIABLO_BEANTREE_ENABLED = "weblogic.internal.beantree";
   public static final String ADMIN_USERNAME_PROP = "weblogic.management.username";
   public static final String ADMIN_PASSWORD_PROP = "weblogic.management.password";
   public static final String ADMIN_PKPASSWORD_PROP = "weblogic.management.pkpassword";
   public static final String ADMIN_HOST_PROP = "weblogic.management.server";
   public static final String OLD_ADMIN_HOST_PROP = "weblogic.admin.host";
   static final String OPTIONAL_PACKAGE_PROP = "weblogic.application.RequireOptionalPackages";
   static final String J2EE_TMP_DIR = "weblogic.j2ee.application.tmpDir";
   static final String WEBSERVICE_CERTFILE = "webservice.client.ssl.trustedcertfile";
   public static final String ADMIN_ALLOWPWD_ECHO = "weblogic.management.allowPasswordEcho";
   public static final String ADMIN_AUDITLOG_DIR = "weblogic.security.audit.auditLogDir";
   public static final String REGISTER_X509_CERTIFICATE_FACTORY = "weblogic.security.RegisterX509CertificateFactory";
   public static final String MESSAGE_LOG_NON_DURABLE_PROP = "weblogic.jms.message.logging.logNonDurableSubscriber";
   public static final String MESSAGE_LOG_DESTINATIONS_ALL_PROP = "weblogic.jms.message.logging.destinations.all";
   public static final String SECURITY_CHECK_INTERVAL_PROP = "weblogic.jms.securityCheckInterval";
   public static final String MULTICAST_SEND_DELAY_PROP = "weblogic.jms.extensions.multicast.sendDelay";
   public static final String JMX_REMOTE_REQUEST_TIMEOUT = "weblogic.management.jmx.remote.requestTimeout";
   public static final String CONVERT_SECURITY_EXTENSION_SCHEMA = "weblogic.management.convertSecurityExtensionSchema";

   public static void configureFromSystemProperties(ServerMBean var0) {
      configureFromSystemProperties(var0, false, false);
   }

   public static void configureFromSystemProperties(ServerMBean var0, boolean var1, boolean var2) {
      Properties var3 = System.getProperties();
      EnumerationIterator var4 = new EnumerationIterator(var3.propertyNames());

      while(true) {
         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (var5.equalsIgnoreCase("weblogic.AdministrationMBeanAuditingEnabled")) {
               ((DomainMBean)var0.getParent()).setAdministrationMBeanAuditingEnabled(Boolean.getBoolean("weblogic.AdministrationMBeanAuditingEnabled"));
            } else {
               String var6 = var5.toLowerCase(Locale.US);
               String var7 = null;
               Object var8 = var0;
               if (!var6.equals("weblogic.class.path") && !specialProperties.contains(var5) && !SystemProperties.isSpecialFileStoreProperty(var6)) {
                  if (var6.startsWith("weblogic.debug.")) {
                     var7 = var5.substring("weblogic.debug.".length());
                     var8 = var0.getServerDebug();
                  } else if (var6.startsWith("weblogic.ssl.")) {
                     var7 = var5.substring("weblogic.ssl.".length());
                     var8 = var0.getSSL();
                  } else if (var6.startsWith("weblogic.log.")) {
                     var7 = var5.substring("weblogic.log.".length());
                     var8 = var0.getLog();
                  } else if (var6.startsWith("weblogic.") && !var6.startsWith("weblogic.cluster.")) {
                     var7 = var5.substring("weblogic.".length());
                     if (var7.equals("Cluster") && var1) {
                        continue;
                     }

                     var8 = var0;
                  } else if (var6.startsWith("weblogic.cluster.") && !var1) {
                     var7 = var5.substring("weblogic.cluster.".length());
                     if (var0.getCluster() == null) {
                        if (var2) {
                           ManagementLogger.logClusterPropertyIgnoreBecauseNoClusterConfigured(var5);
                        }
                        continue;
                     }

                     var8 = var0.getCluster();
                  }

                  if (var7 != null && !specialProperties.contains(var7)) {
                     try {
                        Method[] var9 = var8.getClass().getMethods();
                        boolean var10 = false;

                        for(int var11 = 0; var11 < var9.length; ++var11) {
                           Class[] var12 = var9[var11].getParameterTypes();
                           if (var9[var11].getName().startsWith("set") && var12.length == 1 && var9[var11].getName().substring(3).equals(var7)) {
                              String var13 = (String)var3.get(var5);
                              if (debugLogger.isDebugEnabled()) {
                                 debugLogger.debug("setting from command line [" + var7 + "=" + var13 + "] on " + ((ConfigurationMBean)var8).getType() + "{" + ((ConfigurationMBean)var8).getName() + "}");
                              }

                              Object[] var14 = new Object[1];
                              Class var15 = var12[0];
                              if (var15 == Boolean.TYPE) {
                                 var14[0] = new Boolean(var13);
                              } else if (var15 == Character.TYPE) {
                                 var14[0] = new Character(var13.charAt(0));
                              } else if (var15 == Byte.TYPE) {
                                 var14[0] = new Byte(var13);
                              } else if (var15 == Short.TYPE) {
                                 var14[0] = new Short(var13);
                              } else if (var15 == Integer.TYPE) {
                                 var14[0] = new Integer(var13);
                              } else if (var15 == Long.TYPE) {
                                 var14[0] = new Long(var13);
                              } else if (var15 == Float.TYPE) {
                                 var14[0] = new Float(var13);
                              } else if (var15 == Double.TYPE) {
                                 var14[0] = new Double(var13);
                              } else if (var15 == String.class) {
                                 var14[0] = var13;
                              } else if (var15 == Properties.class) {
                                 var14[0] = PropertiesHelper.parse(var13);
                              } else if (debugLogger.isDebugEnabled()) {
                                 debugLogger.debug("UNKNOWN TYPE: " + var15);
                              }

                              var10 = true;
                              var9[var11].invoke(var8, var14);
                           }
                        }

                        if (!var10 && var2) {
                           ManagementLogger.logUnrecognizedProperty(var5);
                        }
                     } catch (Exception var16) {
                        ManagementLogger.logErrorSettingAttribute(var7, var16);
                     }
                  }
               }
            }
         }

         return;
      }
   }

   static {
      specialProperties.add("weblogic.management.pkpassword");
      specialProperties.add("weblogic.management.password");
      specialProperties.add("weblogic.management.username");
      specialProperties.add("Domain");
      specialProperties.add("Name");
      specialProperties.add("Debug");
      specialProperties.add("DebugFilter");
      specialProperties.add("weblogic.management.server");
      specialProperties.add("weblogic.admin.host");
      specialProperties.add("weblogic.CORBA.connectionPoolSoTimeout");
      specialProperties.add("weblogic.ProductionModeEnabled");
      specialProperties.add("weblogic.AdministrationMBeanAuditingEnabled");
      specialProperties.add("weblogic.management.GenerateDefaultConfig");
      specialProperties.add("weblogic.home");
      specialProperties.add("weblogic.management.saveDomainMillis");
      specialProperties.add("weblogic.debug.DebugScopes");
      specialProperties.add("weblogic.system.StoreBootIdentity");
      specialProperties.add("weblogic.system.BootIdentityFile");
      specialProperties.add("weblogic.system.RemoveBootIdentity");
      specialProperties.add("weblogic.system.NodeManagerBoot");
      specialProperties.add("weblogic.mbeanLegalClause.ByPass");
      specialProperties.add("weblogic.security.fullyDelegateAuthorization");
      specialProperties.add("weblogic.security.anonymousUserName");
      specialProperties.add("ssl.debug");
      specialProperties.add("weblogic.security.SSL.verbose");
      specialProperties.add("weblogic.security.ssl.verbose");
      specialProperties.add("weblogic.security.SSL.ignoreHostnameVerification");
      specialProperties.add("weblogic.security.SSL.ignoreHostnameVerify");
      specialProperties.add("weblogic.security.SSL.hostnameVerifier");
      specialProperties.add("weblogic.security.SSL.trustedCAKeyStore");
      specialProperties.add("weblogic.security.SSL.protocolVersion");
      specialProperties.add("weblogic.security.SSL.enforceConstraints");
      specialProperties.add("weblogic.security.jacc.RoleMapperFactory.provider");
      specialProperties.add("weblogic.management.anonymousAdminLookupEnabled");
      specialProperties.add("weblogic.security.identityAssertionTTL");
      specialProperties.add("weblogic.ConfigFile");
      specialProperties.add("weblogic.wtc.TraceLevel");
      specialProperties.add("weblogic.wtc.PasswordKey");
      specialProperties.add("weblogic.classloader.preprocessor");
      specialProperties.add("weblogic.oci.selectBlobChunkSize");
      specialProperties.add("weblogic.PosixSocketReaders");
      specialProperties.add("weblogic.security.hierarchyGroupMemberships");
      specialProperties.add("weblogic.application.RequireOptionalPackages");
      specialProperties.add("weblogic.management.jmx.remote.requestTimeout");
      specialProperties.add("weblogic.management.convertSecurityExtensionSchema");
      specialProperties.add("weblogic.security.TrustKeyStore");
      specialProperties.add("weblogic.security.CustomTrustKeyStoreFileName");
      specialProperties.add("weblogic.security.CustomTrustKeyStoreType");
      specialProperties.add("weblogic.security.CustomTrustKeyStorePassPhrase");
      specialProperties.add("weblogic.security.JavaStandardTrustKeyStorePassPhrase");
      specialProperties.add("weblogic.nodemanager.ServiceEnabled");
      specialProperties.add("weblogic.apache.xerces.maxentityrefs");
      specialProperties.add("weblogic.servlet.useExtendedSessionFormat");
      specialProperties.add("weblogic.webservice.i18n.charset");
      specialProperties.add("weblogic.webservice.client.ssl.trustedcertfile");
      specialProperties.add("weblogic.security.SubjectManager");
      specialProperties.add("weblogic.security.SecurityServiceManagerDelegate");
      specialProperties.add("weblogic.webservice.verbose");
      specialProperties.add("weblogic.internal.beantree");
      specialProperties.add("weblogic.security.providers.authentication.LDAPDelegatePoolSize");
      specialProperties.add("weblogic.jms.message.logging.destinations.all");
      specialProperties.add("weblogic.jms.message.logging.logNonDurableSubscriber");
      specialProperties.add("weblogic.jms.securityCheckInterval");
      specialProperties.add("weblogic.jms.extensions.multicast.sendDelay");
      SystemProperties.register(specialProperties);
      specialProperties.add("weblogic.messaging.kernel.persistence.InLineBodyThreshold");
      specialProperties.add("weblogic.messaging.kernel.persistence.PageInOnBoot");
      specialProperties.add("weblogic.messaging.kernel.paging.AlwaysUsePagingStore");
      specialProperties.add("weblogic.messaging.kernel.paging.BatchSize");
      specialProperties.add("weblogic.messaging.kernel.paging.PagedMessageThreshold");
      specialProperties.add("weblogic.ForceImplicitUpgradeIfNeeded");
      specialProperties.add("weblogic.diagnostics.instrumentation");
      specialProperties.add("weblogic.j2ee.application.tmpDir");
      specialProperties.add("weblogic.management.allowPasswordEcho");
      specialProperties.add("webservice.client.ssl.trustedcertfile");
      specialProperties.add("weblogic.security.audit.auditLogDir");
      specialProperties.add("weblogic.security.RegisterX509CertificateFactory");
      specialProperties.add("weblogic.management.clearTextCredentialAccessEnabled");
   }
}
