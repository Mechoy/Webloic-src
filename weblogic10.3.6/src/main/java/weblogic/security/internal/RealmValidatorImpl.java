package weblogic.security.internal;

import java.util.HashSet;
import weblogic.management.security.ProviderMBean;
import weblogic.management.security.RealmMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBean;
import weblogic.management.security.authentication.AuthenticatorMBean;
import weblogic.management.security.authentication.IdentityAsserterMBean;
import weblogic.management.security.authorization.AuthorizerMBean;
import weblogic.management.security.authorization.DeployableAuthorizerMBean;
import weblogic.management.security.authorization.DeployableRoleMapperMBean;
import weblogic.management.security.authorization.PolicyConsumerMBean;
import weblogic.management.security.authorization.RoleMapperMBean;
import weblogic.management.security.credentials.CredentialMapperMBean;
import weblogic.management.security.pk.CertPathBuilderMBean;
import weblogic.management.security.pk.CertPathProviderMBean;
import weblogic.management.security.pk.CertPathValidatorMBean;
import weblogic.management.security.pk.KeyStoreMBean;
import weblogic.management.utils.ErrorCollectionException;
import weblogic.security.SecurityLogger;
import weblogic.t3.srvr.T3Srvr;

public class RealmValidatorImpl {
   static final String REALM_ADAPTER_ADJUDICATOR = "weblogic.security.providers.realmadapter.AdjudicationProviderImpl";
   static final String REALM_ADAPTER_AUTHORIZER = "weblogic.security.providers.realmadapter.AuthorizationProviderImpl";
   static final String REALM_ADAPTER_AUTHENTICATOR = "weblogic.security.providers.realmadapter.AuthenticationProviderImpl";
   static final String DEFAULT_AUTHORIZER = "weblogic.security.providers.authorization.DefaultAuthorizationProviderImpl";
   static final String DEFAULT_KEYSTORE = "weblogic.security.providers.pk.DefaultKeyStoreProviderImpl";
   static final String SAML_V1_IA = "weblogic.security.providers.saml.SAMLIdentityAsserterProviderImpl";
   static final String SAML_V2_IA = "weblogic.security.providers.saml.SAMLIdentityAsserterV2ProviderImpl";
   static final String SAML_V1_CREDMAP = "weblogic.security.providers.saml.SAMLCredentialMapperProviderImpl";
   static final String SAML_V2_CREDMAP = "weblogic.security.providers.saml.SAMLCredentialMapperV2ProviderImpl";
   private static boolean isBooting = true;

   public void validate(RealmMBean var1) throws ErrorCollectionException {
      ErrorCollectionException var2 = new ErrorCollectionException(SecurityLogger.getInvalidRealmWarning(var1.getName()));
      this.checkAuthenticationProviders(var1, var2);
      this.checkRoleMappers(var1, var2);
      int var3 = this.checkAuthorizers(var1, var2);
      this.checkAdjudicator(var1, var2, var3);
      this.checkCredentialMappers(var1, var2);
      this.checkCompatibilityMode(var1, var2);
      this.checkKeyStoreProviders(var1, var2);
      this.checkCertPathProviders(var1, var2);
      this.checkSAMLProviders(var1, var2);
      if (!var2.isEmpty()) {
         throw var2;
      }
   }

   private void checkAuthenticationProviders(RealmMBean var1, ErrorCollectionException var2) {
      AuthenticationProviderMBean[] var3 = var1.getAuthenticationProviders();
      this.checkHaveAuthenticator(var1, var3, var2);
      this.checkActiveTypesUnique(var1, var3, var2);
   }

   private void checkHaveAuthenticator(RealmMBean var1, AuthenticationProviderMBean[] var2, ErrorCollectionException var3) {
      boolean var4 = false;

      for(int var5 = 0; !var4 && var2 != null && var5 < var2.length; ++var5) {
         if (var2[var5] instanceof AuthenticatorMBean) {
            var4 = true;
         }
      }

      if (!var4) {
         this.addError(var3, SecurityLogger.getInvalidRealmNoAuthenticatorWarning(var1.getName()));
      }

   }

   private void checkActiveTypesUnique(RealmMBean var1, AuthenticationProviderMBean[] var2, ErrorCollectionException var3) {
      HashSet var4 = new HashSet();

      for(int var5 = 0; var2 != null && var5 < var2.length; ++var5) {
         if (var2[var5] instanceof IdentityAsserterMBean) {
            IdentityAsserterMBean var6 = (IdentityAsserterMBean)var2[var5];
            String[] var7 = var6.getActiveTypes();

            for(int var8 = 0; var7 != null && var8 < var7.length; ++var8) {
               String var9 = var7[var8];
               if (var9 != null && var9.length() > 0) {
                  if (var4.contains(var9)) {
                     this.addError(var3, SecurityLogger.getInvalidRealmMultipleIdentityAssertersForActiveTokenTypeWarning(var1.getName(), var9));
                  } else {
                     var4.add(var9);
                  }
               }
            }
         }
      }

   }

   private void checkRoleMappers(RealmMBean var1, ErrorCollectionException var2) {
      RoleMapperMBean[] var3 = var1.getRoleMappers();
      if (var3 != null && var3.length != 0) {
         boolean var4 = false;
         boolean var5 = false;

         for(int var6 = 0; !var5 && var6 < var3.length; ++var6) {
            if (var3[var6] instanceof DeployableRoleMapperMBean) {
               var4 = true;
               DeployableRoleMapperMBean var7 = (DeployableRoleMapperMBean)var3[var6];
               if (var7.isRoleDeploymentEnabled()) {
                  var5 = true;
               }
            }
         }

         if (!var4) {
            if (!isBooting()) {
               this.addError(var2, SecurityLogger.getInvalidRealmNoDeployableRoleMapperWarning(var1.getName()));
            } else {
               SecurityLogger.logNoDeployableProviderProperlyConfigured(var1.getName(), "DeployableRoleMapper");
            }
         } else if (!var5) {
            if (!isBooting()) {
               this.addError(var2, SecurityLogger.getInvalidRealmNoDeployableRoleMapperEnabledWarning(var1.getName()));
            } else {
               SecurityLogger.logNoDeployableProviderProperlyConfigured(var1.getName(), "DeployableRoleMapper");
            }
         }

      } else {
         this.addError(var2, SecurityLogger.getInvalidRealmNoRoleMapperWarning(var1.getName()));
      }
   }

   private int checkAuthorizers(RealmMBean var1, ErrorCollectionException var2) {
      AuthorizerMBean[] var3 = var1.getAuthorizers();
      if (var3 != null && var3.length != 0) {
         boolean var4 = false;
         boolean var5 = false;

         for(int var6 = 0; !var5 && var6 < var3.length; ++var6) {
            if (var3[var6] instanceof DeployableAuthorizerMBean) {
               var4 = true;
               DeployableAuthorizerMBean var7 = (DeployableAuthorizerMBean)var3[var6];
               if (var7.isPolicyDeploymentEnabled()) {
                  var5 = true;
               }
            }
         }

         if (!var4) {
            if (!isBooting()) {
               this.addError(var2, SecurityLogger.getInvalidRealmNoDeployableAuthorizerWarning(var1.getName()));
            } else {
               SecurityLogger.logNoDeployableProviderProperlyConfigured(var1.getName(), "DeployableAuthorizer");
            }
         } else if (!var5) {
            if (!isBooting()) {
               this.addError(var2, SecurityLogger.getInvalidRealmNoDeployableAuthorizerEnabledWarning(var1.getName()));
            } else {
               SecurityLogger.logNoDeployableProviderProperlyConfigured(var1.getName(), "DeployableAuthorizer");
            }
         }

         if (var1.isDelegateMBeanAuthorization()) {
            boolean var8 = false;

            for(int var9 = 0; !var8 && var9 < var3.length; ++var9) {
               if (var3[var9] instanceof PolicyConsumerMBean) {
                  var8 = true;
               }
            }

            if (!var8) {
               this.addError(var2, SecurityLogger.getInvalidRealmNoMBeanDelegationWarning(var1.getName()));
            }
         }

         return var3.length;
      } else {
         this.addError(var2, SecurityLogger.getInvalidRealmNoAuthorizerWarning(var1.getName()));
         return 0;
      }
   }

   private void checkCredentialMappers(RealmMBean var1, ErrorCollectionException var2) {
      CredentialMapperMBean[] var3 = var1.getCredentialMappers();
      if (var3 == null || var3.length == 0) {
         this.addError(var2, SecurityLogger.getInvalidRealmNoCredentialMapperWarning(var1.getName()));
      }
   }

   private void checkAdjudicator(RealmMBean var1, ErrorCollectionException var2, int var3) {
      if (var1.getAdjudicator() == null && var3 > 1) {
         this.addError(var2, SecurityLogger.getInvalidRealmNoAdjudicatorWarning(var1.getName()));
      }

   }

   private boolean providerIsA(ProviderMBean var1, String var2) {
      return var1 == null ? false : var2.equals(var1.getProviderClassName());
   }

   private int providerCount(ProviderMBean[] var1, String var2) {
      int var3 = 0;

      for(int var4 = 0; var1 != null && var4 < var1.length; ++var4) {
         if (this.providerIsA(var1[var4], var2)) {
            ++var3;
         }
      }

      return var3;
   }

   private void checkCompatibilityMode(RealmMBean var1, ErrorCollectionException var2) {
      AuthorizerMBean[] var3 = var1.getAuthorizers();
      int var4 = this.providerCount(var3, "weblogic.security.providers.realmadapter.AuthorizationProviderImpl");
      int var5 = this.providerCount(var3, "weblogic.security.providers.authorization.DefaultAuthorizationProviderImpl");
      int var6 = this.providerCount(var1.getAuthenticationProviders(), "weblogic.security.providers.realmadapter.AuthenticationProviderImpl");
      if (this.providerIsA(var1.getAdjudicator(), "weblogic.security.providers.realmadapter.AdjudicationProviderImpl")) {
         if (var4 == 0) {
            this.addError(var2, SecurityLogger.getInvalidRealmRealmAdapterNoRealmAdapterAuthorizerWarning(var1.getName()));
         }

         if (var5 == 0) {
            this.addError(var2, SecurityLogger.getInvalidRealmRealmAdapterNoDefaultAuthorizerWarning(var1.getName()));
         }

         if (var5 > 1) {
            this.addError(var2, SecurityLogger.getInvalidRealmRealmAdapterMultipleDefaultAuthorizersWarning(var1.getName()));
         }

         if (var3.length > var4 + var5) {
            this.addError(var2, SecurityLogger.getInvalidRealmRealmAdapterUnsupportedAuthorizerWarning(var1.getName()));
         }
      }

      if (var4 > 1) {
         this.addError(var2, SecurityLogger.getInvalidRealmRealmAdapterMultipleRealmAdapterAuthorizersWarning(var1.getName()));
      }

      if (var6 > 1) {
         this.addError(var2, SecurityLogger.getInvalidRealmRealmAdapterMultipleRealmAdapterAuthenticatorsWarning(var1.getName()));
      }

      if (var4 > 0 && var6 == 0) {
         this.addError(var2, SecurityLogger.getInvalidRealmRealmAdapterNoRealmAdapterAuthenticatorWarning(var1.getName()));
      }

   }

   private void checkKeyStoreProviders(RealmMBean var1, ErrorCollectionException var2) {
      if (System.getProperty("weblogic.security.AllowAllKeyStoreProviders") == null) {
         KeyStoreMBean[] var3 = var1.getKeyStores();
         if (var3 != null && var3.length != this.providerCount(var3, "weblogic.security.providers.pk.DefaultKeyStoreProviderImpl")) {
            this.addError(var2, SecurityLogger.getInvalidRealmInvalidKeyStoreProviderWarning(var1.getName()));
         }

         int var4 = 0;
         int var5 = 0;

         for(int var6 = 0; var3 != null && var6 < var3.length; ++var6) {
            KeyStoreMBean var7 = var3[var6];
            String var8 = var7.getRootCAKeyStoreLocation();
            if (var8 != null && var8.length() > 0) {
               ++var4;
            }

            var8 = var7.getPrivateKeyStoreLocation();
            if (var8 != null && var8.length() > 0) {
               ++var5;
            }
         }

         if (var4 > 1) {
            this.addError(var2, SecurityLogger.getInvalidRealmMultipleTrustedCAKeyStoresWarning(var1.getName()));
         }

         if (var5 > 1) {
            this.addError(var2, SecurityLogger.getInvalidRealmMultiplePrivateKeyStoresWarning(var1.getName()));
         }

      }
   }

   private void checkCertPathProviders(RealmMBean var1, ErrorCollectionException var2) {
      CertPathProviderMBean[] var3 = var1.getCertPathProviders();
      if (var3 == null || var3.length < 1) {
         this.addError(var2, SecurityLogger.getInvalidRealmNoCertPathProvidersWarning(var1.getName()));
      }

      CertPathBuilderMBean var4 = var1.getCertPathBuilder();
      if (var4 == null) {
         this.addError(var2, SecurityLogger.getInvalidRealmNoCertPathBuilderWarning(var1.getName()));
      } else {
         if (var4.getRealm() == null || !var4.getRealm().getName().equals(var1.getName())) {
            this.addError(var2, SecurityLogger.getInvalidRealmIllegalCertPathBuilderWarning(var1.getName()));
         }

         boolean var5 = false;

         for(int var6 = 0; !var5 && var3 != null && var6 < var3.length; ++var6) {
            if (var3[var6] instanceof CertPathValidatorMBean) {
               var5 = true;
            }
         }

         if (!var5) {
            this.addError(var2, SecurityLogger.getInvalidRealmNoCertPathValidatorWarning(var1.getName()));
         }

      }
   }

   private void checkSAMLProviders(RealmMBean var1, ErrorCollectionException var2) {
      AuthenticationProviderMBean[] var3 = var1.getAuthenticationProviders();
      int var4 = this.providerCount(var3, "weblogic.security.providers.saml.SAMLIdentityAsserterProviderImpl");
      int var5 = this.providerCount(var3, "weblogic.security.providers.saml.SAMLIdentityAsserterV2ProviderImpl");
      CredentialMapperMBean[] var6 = var1.getCredentialMappers();
      int var7 = this.providerCount(var6, "weblogic.security.providers.saml.SAMLCredentialMapperProviderImpl");
      int var8 = this.providerCount(var6, "weblogic.security.providers.saml.SAMLCredentialMapperV2ProviderImpl");
      if (var4 != 0 || var5 != 0 || var7 != 0 || var8 != 0) {
         if (var4 <= 1 && var5 <= 1 && var7 <= 1 && var8 <= 1) {
            if (var4 > 0 && var5 > 0 || var7 > 0 && var8 > 0 || var4 > 0 && var8 > 0 || var5 > 0 && var7 > 0) {
               this.addError(var2, SecurityLogger.getInvalidRealmSAMLConfigWarning(var1.getName()));
            }
         } else {
            this.addError(var2, SecurityLogger.getInvalidRealmSAMLConfigWarning(var1.getName()));
         }
      }
   }

   private void addError(ErrorCollectionException var1, String var2) {
      var1.add(new Exception(var2));
   }

   private static synchronized boolean isBooting() {
      if (isBooting) {
         if (T3Srvr.getT3Srvr().getRunState() == 2) {
            isBooting = false;
            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }
}
