package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WebserviceSecurityMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WebserviceSecurityMBeanImpl bean;

   protected WebserviceSecurityMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WebserviceSecurityMBeanImpl)var1;
   }

   public WebserviceSecurityMBeanBinder() {
      super(new WebserviceSecurityMBeanImpl());
      this.bean = (WebserviceSecurityMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("CompatibilityOrderingPreference")) {
                  try {
                     this.bean.setCompatibilityOrderingPreference((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("CompatibilityPreference")) {
                  try {
                     this.bean.setCompatibilityPreference((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("DefaultCredentialProviderSTSURI")) {
                  try {
                     this.bean.setDefaultCredentialProviderSTSURI((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("PolicySelectionPreference")) {
                  try {
                     this.bean.setPolicySelectionPreference((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("WebserviceCredentialProvider")) {
                  try {
                     this.bean.addWebserviceCredentialProvider((WebserviceCredentialProviderMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                     this.bean.removeWebserviceCredentialProvider((WebserviceCredentialProviderMBean)var8.getExistingBean());
                     this.bean.addWebserviceCredentialProvider((WebserviceCredentialProviderMBean)((AbstractDescriptorBean)((WebserviceCredentialProviderMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WebserviceSecurityToken")) {
                  try {
                     this.bean.addWebserviceSecurityToken((WebserviceSecurityTokenMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                     this.bean.removeWebserviceSecurityToken((WebserviceSecurityTokenMBean)var7.getExistingBean());
                     this.bean.addWebserviceSecurityToken((WebserviceSecurityTokenMBean)((AbstractDescriptorBean)((WebserviceSecurityTokenMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WebserviceTimestamp")) {
                  try {
                     this.bean.setWebserviceTimestamp((WebserviceTimestampMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("WebserviceTokenHandler")) {
                  try {
                     this.bean.addWebserviceTokenHandler((WebserviceTokenHandlerMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                     this.bean.removeWebserviceTokenHandler((WebserviceTokenHandlerMBean)var5.getExistingBean());
                     this.bean.addWebserviceTokenHandler((WebserviceTokenHandlerMBean)((AbstractDescriptorBean)((WebserviceTokenHandlerMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var13) {
         System.out.println(var13 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var13;
      } catch (RuntimeException var14) {
         throw var14;
      } catch (Exception var15) {
         if (var15 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var15);
         } else if (var15 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var15.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var15);
         }
      }
   }
}
