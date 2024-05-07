package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class LogFileMBeanBinder extends ConfigurationMBeanBinder implements AttributeBinder {
   private LogFileMBeanImpl bean;

   protected LogFileMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (LogFileMBeanImpl)var1;
   }

   public LogFileMBeanBinder() {
      super(new LogFileMBeanImpl());
      this.bean = (LogFileMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("BufferSizeKB")) {
                  try {
                     this.bean.setBufferSizeKB(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var17) {
                     System.out.println("Warning: multiple definitions with same name: " + var17.getMessage());
                  }
               } else if (var1.equals("DateFormatPattern")) {
                  try {
                     this.bean.setDateFormatPattern((String)var2);
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("FileCount")) {
                  try {
                     this.bean.setFileCount(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("FileMinSize")) {
                  try {
                     this.bean.setFileMinSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("FileName")) {
                  try {
                     this.bean.setFileName((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("FileTimeSpan")) {
                  try {
                     this.bean.setFileTimeSpan(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("FileTimeSpanFactor")) {
                  try {
                     this.bean.setFileTimeSpanFactor(Long.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("LogFileRotationDir")) {
                  try {
                     this.bean.setLogFileRotationDir((String)var2);
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("RotateLogOnStartup")) {
                  try {
                     this.bean.setRotateLogOnStartup(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("RotationTime")) {
                  try {
                     this.bean.setRotationTime((String)var2);
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("RotationType")) {
                  try {
                     this.bean.setRotationType((String)var2);
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("NumberOfFilesLimited")) {
                  try {
                     this.bean.setNumberOfFilesLimited(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var18) {
         System.out.println(var18 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var18;
      } catch (RuntimeException var19) {
         throw var19;
      } catch (Exception var20) {
         if (var20 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var20);
         } else if (var20 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var20.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var20);
         }
      }
   }
}
