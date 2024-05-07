package weblogic.management.utils;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class LDAPServerMBeanBinder extends SecurityReadOnlyMBeanBinder implements AttributeBinder {
   private LDAPServerMBeanImpl bean;

   protected LDAPServerMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (LDAPServerMBeanImpl)var1;
   }

   public LDAPServerMBeanBinder() {
      super(new LDAPServerMBeanImpl());
      this.bean = (LDAPServerMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("CacheSize")) {
                  try {
                     this.bean.setCacheSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("CacheTTL")) {
                  try {
                     this.bean.setCacheTTL(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("ConnectTimeout")) {
                  try {
                     this.bean.setConnectTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("ConnectionPoolSize")) {
                  try {
                     this.bean.setConnectionPoolSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("ConnectionRetryLimit")) {
                  try {
                     this.bean.setConnectionRetryLimit(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("Credential")) {
                  try {
                     if (this.bean.isCredentialEncryptedSet()) {
                        throw new IllegalArgumentException("Encrypted attribute corresponding to Credential [ LDAPServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                     }

                     this.bean.setCredential((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("CredentialEncrypted")) {
                  if (this.bean.isCredentialEncryptedSet()) {
                     throw new IllegalArgumentException("Unencrypted attribute corresponding to CredentialEncrypted [ LDAPServerMBean ] is also present in the config file. For encrypted properties either encrypted or unencrypted attribute can be present in 8.x config.");
                  }

                  this.bean.setCredentialEncryptedAsString((String)var2);
               } else if (var1.equals("Host")) {
                  try {
                     this.bean.setHost((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("ParallelConnectDelay")) {
                  try {
                     this.bean.setParallelConnectDelay(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("Port")) {
                  try {
                     this.bean.setPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("Principal")) {
                  try {
                     this.bean.setPrincipal((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("ResultsTimeLimit")) {
                  try {
                     this.bean.setResultsTimeLimit(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("BindAnonymouslyOnReferrals")) {
                  try {
                     this.bean.setBindAnonymouslyOnReferrals(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("CacheEnabled")) {
                  try {
                     this.bean.setCacheEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("FollowReferrals")) {
                  try {
                     this.bean.setFollowReferrals(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("SSLEnabled")) {
                  try {
                     this.bean.setSSLEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
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
