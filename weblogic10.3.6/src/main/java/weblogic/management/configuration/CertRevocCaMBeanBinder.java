package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class CertRevocCaMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private CertRevocCaMBeanImpl bean;

   protected CertRevocCaMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (CertRevocCaMBeanImpl)var1;
   }

   public CertRevocCaMBeanBinder() {
      super(new CertRevocCaMBeanImpl());
      this.bean = (CertRevocCaMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("CrlDpDownloadTimeout")) {
                  try {
                     this.bean.setCrlDpDownloadTimeout(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("CrlDpUrl")) {
                  try {
                     this.bean.setCrlDpUrl((String)var2);
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("CrlDpUrlUsage")) {
                  try {
                     this.bean.setCrlDpUrlUsage((String)var2);
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("DistinguishedName")) {
                  try {
                     this.bean.setDistinguishedName((String)var2);
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("MethodOrder")) {
                  try {
                     this.bean.setMethodOrder((String)var2);
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("OcspResponderCertIssuerName")) {
                  try {
                     this.bean.setOcspResponderCertIssuerName((String)var2);
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("OcspResponderCertSerialNumber")) {
                  try {
                     this.bean.setOcspResponderCertSerialNumber((String)var2);
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("OcspResponderCertSubjectName")) {
                  try {
                     this.bean.setOcspResponderCertSubjectName((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("OcspResponderExplicitTrustMethod")) {
                  try {
                     this.bean.setOcspResponderExplicitTrustMethod((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("OcspResponderUrl")) {
                  try {
                     this.bean.setOcspResponderUrl((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("OcspResponderUrlUsage")) {
                  try {
                     this.bean.setOcspResponderUrlUsage((String)var2);
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
               } else if (var1.equals("CheckingDisabled")) {
                  try {
                     this.bean.setCheckingDisabled(Boolean.valueOf((String)var2));
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
