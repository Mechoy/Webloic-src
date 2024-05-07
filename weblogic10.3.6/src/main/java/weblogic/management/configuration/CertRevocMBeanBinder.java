package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class CertRevocMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private CertRevocMBeanImpl bean;

   protected CertRevocMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (CertRevocMBeanImpl)var1;
   }

   public CertRevocMBeanBinder() {
      super(new CertRevocMBeanImpl());
      this.bean = (CertRevocMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("CertRevocCa")) {
                  try {
                     this.bean.addCertRevocCa((CertRevocCaMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                     this.bean.removeCertRevocCa((CertRevocCaMBean)var22.getExistingBean());
                     this.bean.addCertRevocCa((CertRevocCaMBean)((AbstractDescriptorBean)((CertRevocCaMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("CrlCacheRefreshPeriodPercent")) {
                  try {
                     this.bean.setCrlCacheRefreshPeriodPercent(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("CrlCacheType")) {
                  try {
                     this.bean.setCrlCacheType((String)var2);
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("CrlCacheTypeLdapHostname")) {
                  try {
                     this.bean.setCrlCacheTypeLdapHostname((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("CrlCacheTypeLdapPort")) {
                  try {
                     this.bean.setCrlCacheTypeLdapPort(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("CrlCacheTypeLdapSearchTimeout")) {
                  try {
                     this.bean.setCrlCacheTypeLdapSearchTimeout(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("CrlDpDownloadTimeout")) {
                  try {
                     this.bean.setCrlDpDownloadTimeout(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("MethodOrder")) {
                  try {
                     this.bean.setMethodOrder((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("OcspResponseCacheCapacity")) {
                  try {
                     this.bean.setOcspResponseCacheCapacity(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("OcspResponseCacheRefreshPeriodPercent")) {
                  try {
                     this.bean.setOcspResponseCacheRefreshPeriodPercent(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("OcspResponseTimeout")) {
                  try {
                     this.bean.setOcspResponseTimeout(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("OcspTimeTolerance")) {
                  try {
                     this.bean.setOcspTimeTolerance(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("CheckingEnabled")) {
                  try {
                     this.bean.setCheckingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("CrlDpBackgroundDownloadEnabled")) {
                  try {
                     this.bean.setCrlDpBackgroundDownloadEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("CrlDpEnabled")) {
                  try {
                     this.bean.setCrlDpEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("FailOnUnknownRevocStatus")) {
                  try {
                     this.bean.setFailOnUnknownRevocStatus(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("OcspNonceEnabled")) {
                  try {
                     this.bean.setOcspNonceEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("OcspResponseCacheEnabled")) {
                  try {
                     this.bean.setOcspResponseCacheEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var23) {
         System.out.println(var23 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var23;
      } catch (RuntimeException var24) {
         throw var24;
      } catch (Exception var25) {
         if (var25 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var25);
         } else if (var25 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var25.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var25);
         }
      }
   }
}
