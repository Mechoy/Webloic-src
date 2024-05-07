package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class LDAPRealmMBeanBinder extends BasicRealmMBeanBinder implements AttributeBinder {
   private LDAPRealmMBeanImpl bean;

   protected LDAPRealmMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (LDAPRealmMBeanImpl)var1;
   }

   public LDAPRealmMBeanBinder() {
      super(new LDAPRealmMBeanImpl());
      this.bean = (LDAPRealmMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("AuthProtocol")) {
                  try {
                     this.bean.setAuthProtocol((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("Credential")) {
                  try {
                     if (this.bean.isCredentialEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to Credential [ LDAPRealmMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setCredential((String)var2);
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("CredentialEncrypted")) {
                  if (this.bean.isCredentialEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to CredentialEncrypted [ LDAPRealmMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setCredentialEncryptedAsString((String)var2);
               } else if (var1.equals("GroupDN")) {
                  try {
                     this.bean.setGroupDN((String)var2);
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("GroupIsContext")) {
                  try {
                     this.bean.setGroupIsContext(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("GroupNameAttribute")) {
                  try {
                     this.bean.setGroupNameAttribute((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("GroupUsernameAttribute")) {
                  try {
                     this.bean.setGroupUsernameAttribute((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("LDAPURL")) {
                  try {
                     this.bean.setLDAPURL((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("LdapProvider")) {
                  try {
                     this.bean.setLdapProvider((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("Principal")) {
                  try {
                     this.bean.setPrincipal((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else {
                  if (var1.equals("RealmClassName")) {
                     throw new AssertionError("can't set read-only property RealmClassName");
                  }

                  if (var1.equals("SSLEnable")) {
                     try {
                        this.bean.setSSLEnable(Boolean.valueOf((String)var2));
                     } catch (BeanAlreadyExistsException var9) {
                        System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                     }
                  } else if (var1.equals("UserAuthentication")) {
                     try {
                        this.bean.setUserAuthentication((String)var2);
                     } catch (BeanAlreadyExistsException var8) {
                        System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     }
                  } else if (var1.equals("UserDN")) {
                     try {
                        this.bean.setUserDN((String)var2);
                     } catch (BeanAlreadyExistsException var7) {
                        System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     }
                  } else if (var1.equals("UserNameAttribute")) {
                     try {
                        this.bean.setUserNameAttribute((String)var2);
                     } catch (BeanAlreadyExistsException var6) {
                        System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     }
                  } else if (var1.equals("UserPasswordAttribute")) {
                     try {
                        this.bean.setUserPasswordAttribute((String)var2);
                     } catch (BeanAlreadyExistsException var5) {
                        System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                     }
                  } else {
                     var3 = super.bindAttribute(var1, var2);
                  }
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var20) {
         System.out.println(var20 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var20;
      } catch (RuntimeException var21) {
         throw var21;
      } catch (Exception var22) {
         if (var22 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var22);
         } else if (var22 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var22.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var22);
         }
      }
   }
}
