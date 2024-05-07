package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WTCRemoteTuxDomMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WTCRemoteTuxDomMBeanImpl bean;

   protected WTCRemoteTuxDomMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WTCRemoteTuxDomMBeanImpl)var1;
   }

   public WTCRemoteTuxDomMBeanBinder() {
      super(new WTCRemoteTuxDomMBeanImpl());
      this.bean = (WTCRemoteTuxDomMBeanImpl)this.getBean();
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
                  } catch (BeanAlreadyExistsException var29) {
                     System.out.println("Warning: multiple definitions with same name: " + var29.getMessage());
                  }
               } else if (var1.equals("AccessPointId")) {
                  try {
                     this.bean.setAccessPointId((String)var2);
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("AclPolicy")) {
                  try {
                     this.bean.setAclPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("AllowAnonymous")) {
                  try {
                     this.bean.setAllowAnonymous(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("AppKey")) {
                  try {
                     this.bean.setAppKey((String)var2);
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
               } else if (var1.equals("CredentialPolicy")) {
                  try {
                     this.bean.setCredentialPolicy((String)var2);
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("CustomAppKeyClass")) {
                  try {
                     this.bean.setCustomAppKeyClass((String)var2);
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("CustomAppKeyClassParam")) {
                  try {
                     this.bean.setCustomAppKeyClassParam((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("DefaultAppKey")) {
                  try {
                     this.bean.setDefaultAppKey(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("FederationName")) {
                  try {
                     this.bean.setFederationName((String)var2);
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("FederationURL")) {
                  try {
                     this.bean.setFederationURL((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("KeepAlive")) {
                  try {
                     this.bean.setKeepAlive(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("KeepAliveWait")) {
                  try {
                     this.bean.setKeepAliveWait(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("LocalAccessPoint")) {
                  try {
                     this.bean.setLocalAccessPoint((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("MaxEncryptBits")) {
                  try {
                     this.bean.setMaxEncryptBits((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("MaxRetries")) {
                  try {
                     this.bean.setMaxRetries(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("MinEncryptBits")) {
                  try {
                     this.bean.setMinEncryptBits((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("NWAddr")) {
                  try {
                     this.bean.setNWAddr((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("RetryInterval")) {
                  try {
                     this.bean.setRetryInterval(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("TpUsrFile")) {
                  try {
                     this.bean.setTpUsrFile((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("TuxedoGidKw")) {
                  try {
                     this.bean.setTuxedoGidKw((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("TuxedoUidKw")) {
                  try {
                     this.bean.setTuxedoUidKw((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var30) {
         System.out.println(var30 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var30;
      } catch (RuntimeException var31) {
         throw var31;
      } catch (Exception var32) {
         if (var32 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var32);
         } else if (var32 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var32.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var32);
         }
      }
   }
}
