package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WebAppContainerMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WebAppContainerMBeanImpl bean;

   protected WebAppContainerMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WebAppContainerMBeanImpl)var1;
   }

   public WebAppContainerMBeanBinder() {
      super(new WebAppContainerMBeanImpl());
      this.bean = (WebAppContainerMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("MaxPostSize")) {
                  try {
                     this.bean.setMaxPostSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var28) {
                     System.out.println("Warning: multiple definitions with same name: " + var28.getMessage());
                  }
               } else if (var1.equals("MaxPostTimeSecs")) {
                  try {
                     this.bean.setMaxPostTimeSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var27) {
                     System.out.println("Warning: multiple definitions with same name: " + var27.getMessage());
                  }
               } else if (var1.equals("MimeMappingFile")) {
                  try {
                     this.bean.setMimeMappingFile((String)var2);
                  } catch (BeanAlreadyExistsException var26) {
                     System.out.println("Warning: multiple definitions with same name: " + var26.getMessage());
                  }
               } else if (var1.equals("P3PHeaderValue")) {
                  try {
                     this.bean.setP3PHeaderValue((String)var2);
                  } catch (BeanAlreadyExistsException var25) {
                     System.out.println("Warning: multiple definitions with same name: " + var25.getMessage());
                  }
               } else if (var1.equals("PostTimeoutSecs")) {
                  try {
                     this.bean.setPostTimeoutSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var24) {
                     System.out.println("Warning: multiple definitions with same name: " + var24.getMessage());
                  }
               } else if (var1.equals("ServletReloadCheckSecs")) {
                  try {
                     this.bean.setServletReloadCheckSecs(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var23) {
                     System.out.println("Warning: multiple definitions with same name: " + var23.getMessage());
                  }
               } else if (var1.equals("XPoweredByHeaderLevel")) {
                  try {
                     this.bean.setXPoweredByHeaderLevel((String)var2);
                  } catch (BeanAlreadyExistsException var22) {
                     System.out.println("Warning: multiple definitions with same name: " + var22.getMessage());
                  }
               } else if (var1.equals("AllowAllRoles")) {
                  try {
                     this.bean.setAllowAllRoles(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var21) {
                     System.out.println("Warning: multiple definitions with same name: " + var21.getMessage());
                  }
               } else if (var1.equals("AuthCookieEnabled")) {
                  try {
                     this.bean.setAuthCookieEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var20) {
                     System.out.println("Warning: multiple definitions with same name: " + var20.getMessage());
                  }
               } else if (var1.equals("ChangeSessionIDOnAuthentication")) {
                  try {
                     this.bean.setChangeSessionIDOnAuthentication(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var19) {
                     System.out.println("Warning: multiple definitions with same name: " + var19.getMessage());
                  }
               } else if (var1.equals("ClientCertProxyEnabled")) {
                  try {
                     this.bean.setClientCertProxyEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var18) {
                     System.out.println("Warning: multiple definitions with same name: " + var18.getMessage());
                  }
               } else if (var1.equals("FilterDispatchedRequestsEnabled")) {
                  try {
                     this.bean.setFilterDispatchedRequestsEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("HttpTraceSupportEnabled")) {
                  try {
                     this.bean.setHttpTraceSupportEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("JSPCompilerBackwardsCompatible")) {
                  try {
                     this.bean.setJSPCompilerBackwardsCompatible(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("OptimisticSerialization")) {
                  try {
                     this.bean.setOptimisticSerialization(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("OverloadProtectionEnabled")) {
                  try {
                     this.bean.setOverloadProtectionEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("ReloginEnabled")) {
                  try {
                     this.bean.setReloginEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("RetainOriginalURL")) {
                  try {
                     this.bean.setRetainOriginalURL(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("RtexprvalueJspParamName")) {
                  try {
                     this.bean.setRtexprvalueJspParamName(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("ServletAuthenticationFormURL")) {
                  try {
                     this.bean.setServletAuthenticationFormURL(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("ShowArchivedRealPathEnabled")) {
                  try {
                     this.bean.setShowArchivedRealPathEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("WAPEnabled")) {
                  try {
                     this.bean.setWAPEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("WeblogicPluginEnabled")) {
                  try {
                     this.bean.setWeblogicPluginEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("WorkContextPropagationEnabled")) {
                  try {
                     this.bean.setWorkContextPropagationEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var29) {
         System.out.println(var29 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var29;
      } catch (RuntimeException var30) {
         throw var30;
      } catch (Exception var31) {
         if (var31 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var31);
         } else if (var31 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var31.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var31);
         }
      }
   }
}
