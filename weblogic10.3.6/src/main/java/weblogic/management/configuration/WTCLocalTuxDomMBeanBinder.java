package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WTCLocalTuxDomMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WTCLocalTuxDomMBeanImpl bean;

   protected WTCLocalTuxDomMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WTCLocalTuxDomMBeanImpl)var1;
   }

   public WTCLocalTuxDomMBeanBinder() {
      super(new WTCLocalTuxDomMBeanImpl());
      this.bean = (WTCLocalTuxDomMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AccessPoint")) {
                  try {
                     this.bean.setAccessPoint((String)var2);
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("AccessPointId")) {
                  try {
                     this.bean.setAccessPointId((String)var2);
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("BlockTime")) {
                  try {
                     this.bean.setBlockTime(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("CmpLimit")) {
                  try {
                     this.bean.setCmpLimit(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("ConnPrincipalName")) {
                  try {
                     this.bean.setConnPrincipalName((String)var2);
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("ConnectionPolicy")) {
                  try {
                     this.bean.setConnectionPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("IdentityKeyStoreFileName")) {
                  try {
                     this.bean.setIdentityKeyStoreFileName((String)var2);
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("IdentityKeyStorePassPhrase")) {
                  try {
                     if (this.bean.isIdentityKeyStorePassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to IdentityKeyStorePassPhrase [ WTCLocalTuxDomMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setIdentityKeyStorePassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("IdentityKeyStorePassPhraseEncrypted")) {
                  if (this.bean.isIdentityKeyStorePassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to IdentityKeyStorePassPhraseEncrypted [ WTCLocalTuxDomMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setIdentityKeyStorePassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("Interoperate")) {
                  try {
                     this.bean.setInteroperate((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("KeepAlive")) {
                  try {
                     this.bean.setKeepAlive(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("KeepAliveWait")) {
                  try {
                     this.bean.setKeepAliveWait(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("KeyStoresLocation")) {
                  try {
                     this.bean.setKeyStoresLocation((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("MaxEncryptBits")) {
                  try {
                     this.bean.setMaxEncryptBits((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("MaxRetries")) {
                  try {
                     this.bean.setMaxRetries(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("MinEncryptBits")) {
                  try {
                     this.bean.setMinEncryptBits((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("NWAddr")) {
                  try {
                     this.bean.setNWAddr((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("PrivateKeyAlias")) {
                  try {
                     this.bean.setPrivateKeyAlias((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("PrivateKeyPassPhrase")) {
                  try {
                     if (this.bean.isPrivateKeyPassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to PrivateKeyPassPhrase [ WTCLocalTuxDomMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setPrivateKeyPassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("PrivateKeyPassPhraseEncrypted")) {
                  if (this.bean.isPrivateKeyPassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to PrivateKeyPassPhraseEncrypted [ WTCLocalTuxDomMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setPrivateKeyPassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("RetryInterval")) {
                  try {
                     this.bean.setRetryInterval(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("Security")) {
                  try {
                     this.bean.setSecurity((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("TrustKeyStoreFileName")) {
                  try {
                     this.bean.setTrustKeyStoreFileName((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("TrustKeyStorePassPhrase")) {
                  try {
                     if (this.bean.isTrustKeyStorePassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to TrustKeyStorePassPhrase [ WTCLocalTuxDomMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setTrustKeyStorePassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("TrustKeyStorePassPhraseEncrypted")) {
                  if (this.bean.isTrustKeyStorePassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to TrustKeyStorePassPhraseEncrypted [ WTCLocalTuxDomMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setTrustKeyStorePassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("UseSSL")) {
                  try {
                     this.bean.setUseSSL((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var28) {
         System.out.println(var28 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var28;
      } catch (RuntimeException var29) {
         throw var29;
      } catch (Exception var30) {
         if (var30 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var30);
         } else if (var30 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var30.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var30);
         }
      }
   }
}
