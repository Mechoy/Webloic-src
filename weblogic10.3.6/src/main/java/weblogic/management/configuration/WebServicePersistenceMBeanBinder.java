package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.ReadOnlyMBeanBinder;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class WebServicePersistenceMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private WebServicePersistenceMBeanImpl bean;

   protected WebServicePersistenceMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (WebServicePersistenceMBeanImpl)var1;
   }

   public WebServicePersistenceMBeanBinder() {
      super(new WebServicePersistenceMBeanImpl());
      this.bean = (WebServicePersistenceMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("DefaultLogicalStoreName")) {
                  try {
                     this.bean.setDefaultLogicalStoreName((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("WebServiceLogicalStore")) {
                  try {
                     this.bean.addWebServiceLogicalStore((WebServiceLogicalStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                     this.bean.removeWebServiceLogicalStore((WebServiceLogicalStoreMBean)var6.getExistingBean());
                     this.bean.addWebServiceLogicalStore((WebServiceLogicalStoreMBean)((AbstractDescriptorBean)((WebServiceLogicalStoreMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else if (var1.equals("WebServicePhysicalStore")) {
                  try {
                     this.bean.addWebServicePhysicalStore((WebServicePhysicalStoreMBean)((ReadOnlyMBeanBinder)var2).getBean());
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                     this.bean.removeWebServicePhysicalStore((WebServicePhysicalStoreMBean)var5.getExistingBean());
                     this.bean.addWebServicePhysicalStore((WebServicePhysicalStoreMBean)((AbstractDescriptorBean)((WebServicePhysicalStoreMBean)((ReadOnlyMBeanBinder)var2).getBean()))._cloneIncludingObsolete());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var8) {
         System.out.println(var8 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var8;
      } catch (RuntimeException var9) {
         throw var9;
      } catch (Exception var10) {
         if (var10 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var10);
         } else if (var10 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var10.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var10);
         }
      }
   }
}
