package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class FileT3MBeanBinder extends DeploymentMBeanBinder implements AttributeBinder {
   private FileT3MBeanImpl bean;

   protected FileT3MBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (FileT3MBeanImpl)var1;
   }

   public FileT3MBeanBinder() {
      super(new FileT3MBeanImpl());
      this.bean = (FileT3MBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("Path")) {
                  try {
                     this.bean.setPath((String)var2);
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var6) {
         System.out.println(var6 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var6;
      } catch (RuntimeException var7) {
         throw var7;
      } catch (Exception var8) {
         if (var8 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var8);
         } else if (var8 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var8.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var8);
         }
      }
   }
}
