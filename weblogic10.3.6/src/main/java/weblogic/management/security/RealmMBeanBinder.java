package weblogic.management.security;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.management.security.audit.AuditorMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBean;
import weblogic.management.security.authentication.PasswordValidatorMBean;
import weblogic.management.security.authentication.UserLockoutManagerMBean;
import weblogic.management.security.authorization.AdjudicatorMBean;
import weblogic.management.security.authorization.AuthorizerMBean;
import weblogic.management.security.authorization.RoleMapperMBean;
import weblogic.management.security.credentials.CredentialMapperMBean;
import weblogic.management.security.pk.CertPathProviderMBean;
import weblogic.management.security.pk.KeyStoreMBean;
import weblogic.utils.codegen.AttributeBinder;

public class RealmMBeanBinder extends SecurityReadOnlyMBeanBinder implements AttributeBinder {
   private RealmMBeanImpl bean;

   protected RealmMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (RealmMBeanImpl)var1;
   }

   public RealmMBeanBinder() {
      super(new RealmMBeanImpl());
      this.bean = (RealmMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Adjudicator")) {
                  try {
                     this.bean.setAdjudicator((AdjudicatorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var39) {
                     System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                  }
               } else if (var1.equals("Auditor")) {
                  try {
                     this.bean.addAuditor((AuditorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var38) {
                     System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                     this.bean.removeAuditor((AuditorMBean)var38.getExistingBean());
                     this.bean.addAuditor((AuditorMBean)((AbstractDescriptorBean)((AuditorMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("AuthMethods")) {
                  try {
                     this.bean.setAuthMethods((String)var2);
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("AuthenticationProvider")) {
                  try {
                     this.bean.addAuthenticationProvider((AuthenticationProviderMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                     this.bean.removeAuthenticationProvider((AuthenticationProviderMBean)var36.getExistingBean());
                     this.bean.addAuthenticationProvider((AuthenticationProviderMBean)((AbstractDescriptorBean)((AuthenticationProviderMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("Authorizer")) {
                  try {
                     this.bean.addAuthorizer((AuthorizerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                     this.bean.removeAuthorizer((AuthorizerMBean)var35.getExistingBean());
                     this.bean.addAuthorizer((AuthorizerMBean)((AbstractDescriptorBean)((AuthorizerMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("CertPathBuilder")) {
                  this.bean.setCertPathBuilderAsString((String)var2);
               } else if (var1.equals("CertPathProvider")) {
                  try {
                     this.bean.addCertPathProvider((CertPathProviderMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                     this.bean.removeCertPathProvider((CertPathProviderMBean)var34.getExistingBean());
                     this.bean.addCertPathProvider((CertPathProviderMBean)((AbstractDescriptorBean)((CertPathProviderMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else {
                  if (var1.equals("CompatibilityObjectName")) {
                     throw new AssertionError("can't set read-only property CompatibilityObjectName");
                  }

                  if (var1.equals("CredentialMapper")) {
                     try {
                        this.bean.addCredentialMapper((CredentialMapperMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var33) {
                        System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                        this.bean.removeCredentialMapper((CredentialMapperMBean)var33.getExistingBean());
                        this.bean.addCredentialMapper((CredentialMapperMBean)((AbstractDescriptorBean)((CredentialMapperMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("DeployableProviderSynchronizationTimeout")) {
                     try {
                        this.bean.setDeployableProviderSynchronizationTimeout(new Integer((String)var2));
                     } catch (BeanAlreadyExistsException var32) {
                        System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                     }
                  } else if (var1.equals("KeyStore")) {
                     try {
                        this.bean.addKeyStore((KeyStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var31) {
                        System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                        this.bean.removeKeyStore((KeyStoreMBean)var31.getExistingBean());
                        this.bean.addKeyStore((KeyStoreMBean)((AbstractDescriptorBean)((KeyStoreMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("MaxWebLogicPrincipalsInCache")) {
                     try {
                        this.bean.setMaxWebLogicPrincipalsInCache(new Integer((String)var2));
                     } catch (BeanAlreadyExistsException var30) {
                        System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                     }
                  } else if (var1.equals("Name")) {
                     try {
                        this.bean.setName((String)var2);
                     } catch (BeanAlreadyExistsException var29) {
                        System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                     }
                  } else if (var1.equals("PasswordValidator")) {
                     try {
                        this.bean.addPasswordValidator((PasswordValidatorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var28) {
                        System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                        this.bean.removePasswordValidator((PasswordValidatorMBean)var28.getExistingBean());
                        this.bean.addPasswordValidator((PasswordValidatorMBean)((AbstractDescriptorBean)((PasswordValidatorMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("RDBMSSecurityStore")) {
                     try {
                        this.bean.setRDBMSSecurityStore((RDBMSSecurityStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var27) {
                        System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                     }
                  } else if (var1.equals("RoleMapper")) {
                     try {
                        this.bean.addRoleMapper((RoleMapperMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var26) {
                        System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                        this.bean.removeRoleMapper((RoleMapperMBean)var26.getExistingBean());
                        this.bean.addRoleMapper((RoleMapperMBean)((AbstractDescriptorBean)((RoleMapperMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                     }
                  } else if (var1.equals("SecurityDDModel")) {
                     try {
                        this.bean.setSecurityDDModel((String)var2);
                     } catch (BeanAlreadyExistsException var25) {
                        System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                     }
                  } else if (var1.equals("UserLockoutManager")) {
                     try {
                        this.bean.setUserLockoutManager((UserLockoutManagerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var24) {
                        System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                     }
                  } else if (var1.equals("CombinedRoleMappingEnabled")) {
                     try {
                        this.bean.setCombinedRoleMappingEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var23) {
                        System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                     }
                  } else if (var1.equals("DelegateMBeanAuthorization")) {
                     try {
                        this.bean.setDelegateMBeanAuthorization(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var22) {
                        System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                     }
                  } else if (var1.equals("DeployCredentialMappingIgnored")) {
                     this.handleDeprecatedProperty("DeployCredentialMappingIgnored", "9.0.0.0");

                     try {
                        this.bean.setDeployCredentialMappingIgnored(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var21) {
                        System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                     }
                  } else if (var1.equals("DeployPolicyIgnored")) {
                     this.handleDeprecatedProperty("DeployPolicyIgnored", "9.0.0.0");

                     try {
                        this.bean.setDeployPolicyIgnored(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var20) {
                        System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                     }
                  } else if (var1.equals("DeployRoleIgnored")) {
                     this.handleDeprecatedProperty("DeployRoleIgnored", "9.0.0.0");

                     try {
                        this.bean.setDeployRoleIgnored(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var19) {
                        System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                     }
                  } else if (var1.equals("DeployableProviderSynchronizationEnabled")) {
                     try {
                        this.bean.setDeployableProviderSynchronizationEnabled(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var18) {
                        System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                     }
                  } else if (var1.equals("EnableWebLogicPrincipalValidatorCache")) {
                     try {
                        this.bean.setEnableWebLogicPrincipalValidatorCache(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var17) {
                        System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                     }
                  } else if (var1.equals("FullyDelegateAuthorization")) {
                     this.handleDeprecatedProperty("FullyDelegateAuthorization", "9.0.0.0");

                     try {
                        this.bean.setFullyDelegateAuthorization(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var16) {
                        System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                     }
                  } else if (var1.equals("ValidateDDSecurityData")) {
                     try {
                        this.bean.setValidateDDSecurityData(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var15) {
                        System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                     }
                  } else if (this.isInstance(AdjudicatorMBean.class, var1)) {
                     try {
                        this.bean.setAdjudicator((AdjudicatorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var14) {
                        System.out.println("Warning: multiple definitions with same name : " + var14.getMessage());
                        this.bean.setAdjudicator((AdjudicatorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else if (this.isInstance(AuditorMBean.class, var1)) {
                     try {
                        this.bean.addAuditor((AuditorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var13) {
                        System.out.println("Warning: multiple definitions with same name : " + var13.getMessage());
                        this.bean.removeAuditor((AuditorMBean)var13.getExistingBean());
                        this.bean.addAuditor((AuditorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else if (this.isInstance(AuthenticationProviderMBean.class, var1)) {
                     try {
                        this.bean.addAuthenticationProvider((AuthenticationProviderMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var12) {
                        System.out.println("Warning: multiple definitions with same name : " + var12.getMessage());
                        this.bean.removeAuthenticationProvider((AuthenticationProviderMBean)var12.getExistingBean());
                        this.bean.addAuthenticationProvider((AuthenticationProviderMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else if (this.isInstance(AuthorizerMBean.class, var1)) {
                     try {
                        this.bean.addAuthorizer((AuthorizerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var11) {
                        System.out.println("Warning: multiple definitions with same name : " + var11.getMessage());
                        this.bean.removeAuthorizer((AuthorizerMBean)var11.getExistingBean());
                        this.bean.addAuthorizer((AuthorizerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else if (this.isInstance(CertPathProviderMBean.class, var1)) {
                     try {
                        this.bean.addCertPathProvider((CertPathProviderMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var10) {
                        System.out.println("Warning: multiple definitions with same name : " + var10.getMessage());
                        this.bean.removeCertPathProvider((CertPathProviderMBean)var10.getExistingBean());
                        this.bean.addCertPathProvider((CertPathProviderMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else if (this.isInstance(CredentialMapperMBean.class, var1)) {
                     try {
                        this.bean.addCredentialMapper((CredentialMapperMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var9) {
                        System.out.println("Warning: multiple definitions with same name : " + var9.getMessage());
                        this.bean.removeCredentialMapper((CredentialMapperMBean)var9.getExistingBean());
                        this.bean.addCredentialMapper((CredentialMapperMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else if (this.isInstance(KeyStoreMBean.class, var1)) {
                     try {
                        this.bean.addKeyStore((KeyStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var8) {
                        System.out.println("Warning: multiple definitions with same name : " + var8.getMessage());
                        this.bean.removeKeyStore((KeyStoreMBean)var8.getExistingBean());
                        this.bean.addKeyStore((KeyStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else if (this.isInstance(PasswordValidatorMBean.class, var1)) {
                     try {
                        this.bean.addPasswordValidator((PasswordValidatorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var7) {
                        System.out.println("Warning: multiple definitions with same name : " + var7.getMessage());
                        this.bean.removePasswordValidator((PasswordValidatorMBean)var7.getExistingBean());
                        this.bean.addPasswordValidator((PasswordValidatorMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else if (this.isInstance(RoleMapperMBean.class, var1)) {
                     try {
                        this.bean.addRoleMapper((RoleMapperMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var6) {
                        System.out.println("Warning: multiple definitions with same name : " + var6.getMessage());
                        this.bean.removeRoleMapper((RoleMapperMBean)var6.getExistingBean());
                        this.bean.addRoleMapper((RoleMapperMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else if (this.isInstance(UserLockoutManagerMBean.class, var1)) {
                     try {
                        this.bean.setUserLockoutManager((UserLockoutManagerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     } catch (BeanAlreadyExistsException var5) {
                        System.out.println("Warning: multiple definitions with same name : " + var5.getMessage());
                        this.bean.setUserLockoutManager((UserLockoutManagerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                     }
                  } else {
                     var3 = super.bindAttribute(var1, var2);
                  }
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var40) {
         System.out.println(var40 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var40;
      } catch (RuntimeException var41) {
         throw var41;
      } catch (Exception var42) {
         if (var42 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var42);
         } else if (var42 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var42.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var42);
         }
      }
   }
}
