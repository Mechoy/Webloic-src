package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class XMLEntityCacheMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private XMLEntityCacheMBeanImpl bean;

   protected XMLEntityCacheMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (XMLEntityCacheMBeanImpl)var1;
   }

   public XMLEntityCacheMBeanBinder() {
      super(new XMLEntityCacheMBeanImpl());
      this.bean = (XMLEntityCacheMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("CacheDiskSize")) {
                  try {
                     this.bean.setCacheDiskSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("CacheLocation")) {
                  try {
                     this.bean.setCacheLocation((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("CacheMemorySize")) {
                  try {
                     this.bean.setCacheMemorySize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("CacheTimeoutInterval")) {
                  try {
                     this.bean.setCacheTimeoutInterval(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("MaxSize")) {
                  try {
                     this.bean.setMaxSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var10) {
         System.out.println(var10 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var10;
      } catch (RuntimeException var11) {
         throw var11;
      } catch (Exception var12) {
         if (var12 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var12);
         } else if (var12 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var12.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var12);
         }
      }
   }
}
