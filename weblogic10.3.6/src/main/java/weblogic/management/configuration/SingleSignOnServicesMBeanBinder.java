package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class SingleSignOnServicesMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private SingleSignOnServicesMBeanImpl bean;

   protected SingleSignOnServicesMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (SingleSignOnServicesMBeanImpl)var1;
   }

   public SingleSignOnServicesMBeanBinder() {
      super(new SingleSignOnServicesMBeanImpl());
      this.bean = (SingleSignOnServicesMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("ArtifactMaxCacheSize")) {
                  try {
                     this.bean.setArtifactMaxCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var48) {
                     System.out.println("Warning: multiple definitions with same name: " + var48.getMessage());
                  }
               } else if (var1.equals("ArtifactTimeout")) {
                  try {
                     this.bean.setArtifactTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var47) {
                     System.out.println("Warning: multiple definitions with same name: " + var47.getMessage());
                  }
               } else if (var1.equals("AuthnRequestMaxCacheSize")) {
                  try {
                     this.bean.setAuthnRequestMaxCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var46) {
                     System.out.println("Warning: multiple definitions with same name: " + var46.getMessage());
                  }
               } else if (var1.equals("AuthnRequestTimeout")) {
                  try {
                     this.bean.setAuthnRequestTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var45) {
                     System.out.println("Warning: multiple definitions with same name: " + var45.getMessage());
                  }
               } else if (var1.equals("BasicAuthPassword")) {
                  try {
                     if (this.bean.isBasicAuthPasswordEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to BasicAuthPassword [ SingleSignOnServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setBasicAuthPassword((String)var2);
                  } catch (BeanAlreadyExistsException var44) {
                     System.out.println("Warning: multiple definitions with same name: " + var44.getMessage());
                  }
               } else if (var1.equals("BasicAuthPasswordEncrypted")) {
                  if (this.bean.isBasicAuthPasswordEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to BasicAuthPasswordEncrypted [ SingleSignOnServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setBasicAuthPasswordEncryptedAsString((String)var2);
               } else if (var1.equals("BasicAuthUsername")) {
                  try {
                     this.bean.setBasicAuthUsername((String)var2);
                  } catch (BeanAlreadyExistsException var43) {
                     System.out.println("Warning: multiple definitions with same name: " + var43.getMessage());
                  }
               } else if (var1.equals("ContactPersonCompany")) {
                  try {
                     this.bean.setContactPersonCompany((String)var2);
                  } catch (BeanAlreadyExistsException var42) {
                     System.out.println("Warning: multiple definitions with same name: " + var42.getMessage());
                  }
               } else if (var1.equals("ContactPersonEmailAddress")) {
                  try {
                     this.bean.setContactPersonEmailAddress((String)var2);
                  } catch (BeanAlreadyExistsException var41) {
                     System.out.println("Warning: multiple definitions with same name: " + var41.getMessage());
                  }
               } else if (var1.equals("ContactPersonGivenName")) {
                  try {
                     this.bean.setContactPersonGivenName((String)var2);
                  } catch (BeanAlreadyExistsException var40) {
                     System.out.println("Warning: multiple definitions with same name: " + var40.getMessage());
                  }
               } else if (var1.equals("ContactPersonSurName")) {
                  try {
                     this.bean.setContactPersonSurName((String)var2);
                  } catch (BeanAlreadyExistsException var39) {
                     System.out.println("Warning: multiple definitions with same name: " + var39.getMessage());
                  }
               } else if (var1.equals("ContactPersonTelephoneNumber")) {
                  try {
                     this.bean.setContactPersonTelephoneNumber((String)var2);
                  } catch (BeanAlreadyExistsException var38) {
                     System.out.println("Warning: multiple definitions with same name: " + var38.getMessage());
                  }
               } else if (var1.equals("ContactPersonType")) {
                  try {
                     this.bean.setContactPersonType((String)var2);
                  } catch (BeanAlreadyExistsException var37) {
                     System.out.println("Warning: multiple definitions with same name: " + var37.getMessage());
                  }
               } else if (var1.equals("DefaultURL")) {
                  try {
                     this.bean.setDefaultURL((String)var2);
                  } catch (BeanAlreadyExistsException var36) {
                     System.out.println("Warning: multiple definitions with same name: " + var36.getMessage());
                  }
               } else if (var1.equals("EntityID")) {
                  try {
                     this.bean.setEntityID((String)var2);
                  } catch (BeanAlreadyExistsException var35) {
                     System.out.println("Warning: multiple definitions with same name: " + var35.getMessage());
                  }
               } else if (var1.equals("ErrorPath")) {
                  try {
                     this.bean.setErrorPath((String)var2);
                  } catch (BeanAlreadyExistsException var34) {
                     System.out.println("Warning: multiple definitions with same name: " + var34.getMessage());
                  }
               } else if (var1.equals("IdentityProviderPreferredBinding")) {
                  try {
                     this.bean.setIdentityProviderPreferredBinding((String)var2);
                  } catch (BeanAlreadyExistsException var33) {
                     System.out.println("Warning: multiple definitions with same name: " + var33.getMessage());
                  }
               } else if (var1.equals("LoginReturnQueryParameter")) {
                  try {
                     this.bean.setLoginReturnQueryParameter((String)var2);
                  } catch (BeanAlreadyExistsException var32) {
                     System.out.println("Warning: multiple definitions with same name: " + var32.getMessage());
                  }
               } else if (var1.equals("LoginURL")) {
                  try {
                     this.bean.setLoginURL((String)var2);
                  } catch (BeanAlreadyExistsException var31) {
                     System.out.println("Warning: multiple definitions with same name: " + var31.getMessage());
                  }
               } else if (var1.equals("OrganizationName")) {
                  try {
                     this.bean.setOrganizationName((String)var2);
                  } catch (BeanAlreadyExistsException var30) {
                     System.out.println("Warning: multiple definitions with same name: " + var30.getMessage());
                  }
               } else if (var1.equals("OrganizationURL")) {
                  try {
                     this.bean.setOrganizationURL((String)var2);
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("PublishedSiteURL")) {
                  try {
                     this.bean.setPublishedSiteURL((String)var2);
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("SSOSigningKeyAlias")) {
                  try {
                     this.bean.setSSOSigningKeyAlias((String)var2);
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("SSOSigningKeyPassPhrase")) {
                  try {
                     if (this.bean.isSSOSigningKeyPassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to SSOSigningKeyPassPhrase [ SingleSignOnServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setSSOSigningKeyPassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("SSOSigningKeyPassPhraseEncrypted")) {
                  if (this.bean.isSSOSigningKeyPassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to SSOSigningKeyPassPhraseEncrypted [ SingleSignOnServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setSSOSigningKeyPassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("ServiceProviderPreferredBinding")) {
                  try {
                     this.bean.setServiceProviderPreferredBinding((String)var2);
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("TransportLayerSecurityKeyAlias")) {
                  try {
                     this.bean.setTransportLayerSecurityKeyAlias((String)var2);
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("TransportLayerSecurityKeyPassPhrase")) {
                  try {
                     if (this.bean.isTransportLayerSecurityKeyPassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to TransportLayerSecurityKeyPassPhrase [ SingleSignOnServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setTransportLayerSecurityKeyPassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("TransportLayerSecurityKeyPassPhraseEncrypted")) {
                  if (this.bean.isTransportLayerSecurityKeyPassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to TransportLayerSecurityKeyPassPhraseEncrypted [ SingleSignOnServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setTransportLayerSecurityKeyPassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("ForceAuthn")) {
                  try {
                     this.bean.setForceAuthn(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("IdentityProviderArtifactBindingEnabled")) {
                  try {
                     this.bean.setIdentityProviderArtifactBindingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("IdentityProviderEnabled")) {
                  try {
                     this.bean.setIdentityProviderEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("IdentityProviderPOSTBindingEnabled")) {
                  try {
                     this.bean.setIdentityProviderPOSTBindingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("IdentityProviderRedirectBindingEnabled")) {
                  try {
                     this.bean.setIdentityProviderRedirectBindingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("POSTOneUseCheckEnabled")) {
                  try {
                     this.bean.setPOSTOneUseCheckEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("Passive")) {
                  try {
                     this.bean.setPassive(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("RecipientCheckEnabled")) {
                  try {
                     this.bean.setRecipientCheckEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("ReplicatedCacheEnabled")) {
                  try {
                     this.bean.setReplicatedCacheEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("ServiceProviderArtifactBindingEnabled")) {
                  try {
                     this.bean.setServiceProviderArtifactBindingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("ServiceProviderEnabled")) {
                  try {
                     this.bean.setServiceProviderEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("ServiceProviderPOSTBindingEnabled")) {
                  try {
                     this.bean.setServiceProviderPOSTBindingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("SignAuthnRequests")) {
                  try {
                     this.bean.setSignAuthnRequests(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("WantArtifactRequestsSigned")) {
                  try {
                     this.bean.setWantArtifactRequestsSigned(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("WantAssertionsSigned")) {
                  try {
                     this.bean.setWantAssertionsSigned(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("WantAuthnRequestsSigned")) {
                  try {
                     this.bean.setWantAuthnRequestsSigned(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("WantBasicAuthClientAuthentication")) {
                  try {
                     this.bean.setWantBasicAuthClientAuthentication(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("WantTransportLayerSecurityClientAuthentication")) {
                  try {
                     this.bean.setWantTransportLayerSecurityClientAuthentication(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var49) {
         System.out.println(var49 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var49;
      } catch (RuntimeException var50) {
         throw var50;
      } catch (Exception var51) {
         if (var51 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var51);
         } else if (var51 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var51.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var51);
         }
      }
   }
}
