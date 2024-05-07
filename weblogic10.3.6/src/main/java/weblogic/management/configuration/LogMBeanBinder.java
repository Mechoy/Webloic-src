package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class LogMBeanBinder extends CommonLogMBeanBinder implements AttributeBinder {
   private LogMBeanImpl bean;

   protected LogMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (LogMBeanImpl)var1;
   }

   public LogMBeanBinder() {
      super(new LogMBeanImpl());
      this.bean = (LogMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("DomainLogBroadcastFilter")) {
                  this.bean.setDomainLogBroadcastFilterAsString((String)var2);
               } else if (var1.equals("DomainLogBroadcastSeverity")) {
                  try {
                     this.bean.setDomainLogBroadcastSeverity((String)var2);
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("DomainLogBroadcasterBufferSize")) {
                  try {
                     this.bean.setDomainLogBroadcasterBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("LogFileFilter")) {
                  this.bean.setLogFileFilterAsString((String)var2);
               } else if (var1.equals("MemoryBufferFilter")) {
                  this.bean.setMemoryBufferFilterAsString((String)var2);
               } else if (var1.equals("MemoryBufferSeverity")) {
                  try {
                     this.bean.setMemoryBufferSeverity((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("MemoryBufferSize")) {
                  try {
                     this.bean.setMemoryBufferSize(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("StdoutFilter")) {
                  this.bean.setStdoutFilterAsString((String)var2);
               } else if (var1.equals("Log4jLoggingEnabled")) {
                  try {
                     this.bean.setLog4jLoggingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("RedirectStderrToServerLogEnabled")) {
                  try {
                     this.bean.setRedirectStderrToServerLogEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("RedirectStdoutToServerLogEnabled")) {
                  try {
                     this.bean.setRedirectStdoutToServerLogEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("ServerLoggingBridgeUseParentLoggersEnabled")) {
                  try {
                     this.bean.setServerLoggingBridgeUseParentLoggersEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var14) {
         System.out.println(var14 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var14;
      } catch (RuntimeException var15) {
         throw var15;
      } catch (Exception var16) {
         if (var16 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var16);
         } else if (var16 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var16.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var16);
         }
      }
   }
}
