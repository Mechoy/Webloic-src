package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class CustomResourceMBeanBinder extends SystemResourceMBeanBinder implements AttributeBinder {
   private CustomResourceMBeanImpl bean;

   protected CustomResourceMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (CustomResourceMBeanImpl)var1;
   }

   public CustomResourceMBeanBinder() {
      super(new CustomResourceMBeanImpl());
      this.bean = (CustomResourceMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("DescriptorBeanClass")) {
                  try {
                     this.bean.setDescriptorBeanClass((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("DescriptorFileName")) {
                  try {
                     this.bean.setDescriptorFileName((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("ResourceClass")) {
                  try {
                     this.bean.setResourceClass((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
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
