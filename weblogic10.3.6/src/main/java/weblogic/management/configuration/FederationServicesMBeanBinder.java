package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class FederationServicesMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private FederationServicesMBeanImpl bean;

   protected FederationServicesMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (FederationServicesMBeanImpl)var1;
   }

   public FederationServicesMBeanBinder() {
      super(new FederationServicesMBeanImpl());
      this.bean = (FederationServicesMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AssertionConsumerURIs")) {
                  try {
                     this.bean.setAssertionConsumerURIs(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("AssertionRetrievalURIs")) {
                  try {
                     this.bean.setAssertionRetrievalURIs(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("AssertionStoreClassName")) {
                  try {
                     this.bean.setAssertionStoreClassName((String)var2);
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("AssertionStoreProperties")) {
                  this.bean.setAssertionStorePropertiesAsString((String)var2);
               } else if (var1.equals("IntersiteTransferURIs")) {
                  try {
                     this.bean.setIntersiteTransferURIs(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("SSLClientIdentityAlias")) {
                  try {
                     this.bean.setSSLClientIdentityAlias((String)var2);
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("SSLClientIdentityPassPhrase")) {
                  try {
                     if (this.bean.isSSLClientIdentityPassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to SSLClientIdentityPassPhrase [ FederationServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setSSLClientIdentityPassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("SSLClientIdentityPassPhraseEncrypted")) {
                  if (this.bean.isSSLClientIdentityPassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to SSLClientIdentityPassPhraseEncrypted [ FederationServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setSSLClientIdentityPassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("SigningKeyAlias")) {
                  try {
                     this.bean.setSigningKeyAlias((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("SigningKeyPassPhrase")) {
                  try {
                     if (this.bean.isSigningKeyPassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to SigningKeyPassPhrase [ FederationServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setSigningKeyPassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("SigningKeyPassPhraseEncrypted")) {
                  if (this.bean.isSigningKeyPassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to SigningKeyPassPhraseEncrypted [ FederationServicesMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setSigningKeyPassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("SourceSiteURL")) {
                  try {
                     this.bean.setSourceSiteURL((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("UsedAssertionCacheClassName")) {
                  try {
                     this.bean.setUsedAssertionCacheClassName((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("UsedAssertionCacheProperties")) {
                  this.bean.setUsedAssertionCachePropertiesAsString((String)var2);
               } else if (var1.equals("ACSRequiresSSL")) {
                  try {
                     this.bean.setACSRequiresSSL(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("ARSRequiresSSL")) {
                  try {
                     this.bean.setARSRequiresSSL(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("ARSRequiresTwoWaySSL")) {
                  try {
                     this.bean.setARSRequiresTwoWaySSL(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("DestinationSiteEnabled")) {
                  try {
                     this.bean.setDestinationSiteEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("ITSRequiresSSL")) {
                  try {
                     this.bean.setITSRequiresSSL(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("POSTOneUseCheckEnabled")) {
                  try {
                     this.bean.setPOSTOneUseCheckEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("POSTRecipientCheckEnabled")) {
                  try {
                     this.bean.setPOSTRecipientCheckEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("SourceSiteEnabled")) {
                  try {
                     this.bean.setSourceSiteEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var24) {
         System.out.println(var24 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var24;
      } catch (RuntimeException var25) {
         throw var25;
      } catch (Exception var26) {
         if (var26 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var26);
         } else if (var26 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var26.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var26);
         }
      }
   }
}
