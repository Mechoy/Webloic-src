package weblogic.management.security.pk;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.management.security.ProviderMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class KeyStoreMBeanBinder extends ProviderMBeanBinder implements AttributeBinder {
   private KeyStoreMBeanImpl bean;

   protected KeyStoreMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (KeyStoreMBeanImpl)var1;
   }

   public KeyStoreMBeanBinder() {
      super(new KeyStoreMBeanImpl());
      this.bean = (KeyStoreMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("PrivateKeyStoreLocation")) {
                  try {
                     this.bean.setPrivateKeyStoreLocation((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("PrivateKeyStorePassPhrase")) {
                  try {
                     if (this.bean.isPrivateKeyStorePassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to PrivateKeyStorePassPhrase [ KeyStoreMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setPrivateKeyStorePassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("PrivateKeyStorePassPhraseEncrypted")) {
                  if (this.bean.isPrivateKeyStorePassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to PrivateKeyStorePassPhraseEncrypted [ KeyStoreMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setPrivateKeyStorePassPhraseEncryptedAsString((String)var2);
               } else if (var1.equals("RootCAKeyStoreLocation")) {
                  try {
                     this.bean.setRootCAKeyStoreLocation((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("RootCAKeyStorePassPhrase")) {
                  try {
                     if (this.bean.isRootCAKeyStorePassPhraseEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to RootCAKeyStorePassPhrase [ KeyStoreMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setRootCAKeyStorePassPhrase((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else if (var1.equals("RootCAKeyStorePassPhraseEncrypted")) {
                  if (this.bean.isRootCAKeyStorePassPhraseEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to RootCAKeyStorePassPhraseEncrypted [ KeyStoreMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setRootCAKeyStorePassPhraseEncryptedAsString((String)var2);
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var9) {
         System.out.println(var9 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var9;
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Exception var11) {
         if (var11 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var11);
         } else if (var11 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var11.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var11);
         }
      }
   }
}
