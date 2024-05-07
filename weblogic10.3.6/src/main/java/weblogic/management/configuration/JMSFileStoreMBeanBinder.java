package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class JMSFileStoreMBeanBinder extends JMSStoreMBeanBinder implements AttributeBinder {
   private JMSFileStoreMBeanImpl bean;

   protected JMSFileStoreMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (JMSFileStoreMBeanImpl)var1;
   }

   public JMSFileStoreMBeanBinder() {
      super(new JMSFileStoreMBeanImpl());
      this.bean = (JMSFileStoreMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("BlockSize")) {
                  try {
                     this.bean.setBlockSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("CacheDirectory")) {
                  try {
                     this.bean.setCacheDirectory((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("Directory")) {
                  try {
                     this.bean.setDirectory((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("InitialSize")) {
                  try {
                     this.bean.setInitialSize(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("IoBufferSize")) {
                  try {
                     this.bean.setIoBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("MaxFileSize")) {
                  try {
                     this.bean.setMaxFileSize(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("MaxWindowBufferSize")) {
                  try {
                     this.bean.setMaxWindowBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("MinWindowBufferSize")) {
                  try {
                     this.bean.setMinWindowBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("SynchronousWritePolicy")) {
                  try {
                     this.bean.setSynchronousWritePolicy((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("FileLockingEnabled")) {
                  try {
                     this.bean.setFileLockingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var16) {
         System.out.println(var16 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var16;
      } catch (RuntimeException var17) {
         throw var17;
      } catch (Exception var18) {
         if (var18 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var18);
         } else if (var18 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var18.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var18);
         }
      }
   }
}
