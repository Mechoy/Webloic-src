package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.BeanAlreadyExistsException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.DistributedManagementException;
import weblogic.management.internal.mbean.SecurityReadOnlyMBeanBinder;
import weblogic.utils.codegen.AttributeBinder;

public class VirtualHostMBeanBinder extends WebServerMBeanBinder implements AttributeBinder {
   private VirtualHostMBeanImpl bean;

   protected VirtualHostMBeanBinder(DescriptorBean var1) {
      super(var1);
      this.bean = (VirtualHostMBeanImpl)var1;
   }

   public VirtualHostMBeanBinder() {
      super(new VirtualHostMBeanImpl());
      this.bean = (VirtualHostMBeanImpl)this.getBean();
   }

   public AttributeBinder bindAttribute(String var1, Object var2) throws IllegalArgumentException {
      try {
         Object var3 = this;
         if (!(this instanceof SecurityReadOnlyMBeanBinder) && var2 != null && var2.toString().trim().length() == 0) {
            return this;
         } else {
            if (var1 != null) {
               if (var1.equals("LogFileCount")) {
                  this.handleDeprecatedProperty("LogFileCount", "9.0.0.0 Use getWebServerLog().getFileCount()");

                  try {
                     this.bean.setLogFileCount(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var16) {
                     System.out.println("Warning: multiple definitions with same name: " + var16.getMessage());
                  }
               } else if (var1.equals("LogFileFormat")) {
                  this.handleDeprecatedProperty("LogFileFormat", "9.0.0.0 Use getWebServerLog().getLogFileFormat().");

                  try {
                     this.bean.setLogFileFormat((String)var2);
                  } catch (BeanAlreadyExistsException var15) {
                     System.out.println("Warning: multiple definitions with same name: " + var15.getMessage());
                  }
               } else if (var1.equals("LogFileName")) {
                  this.handleDeprecatedProperty("LogFileName", "9.0.0.0 Use getWebServerLog().getFileName()");

                  try {
                     this.bean.setLogFileName((String)var2);
                  } catch (BeanAlreadyExistsException var14) {
                     System.out.println("Warning: multiple definitions with same name: " + var14.getMessage());
                  }
               } else if (var1.equals("LogRotationPeriodMins")) {
                  this.handleDeprecatedProperty("LogRotationPeriodMins", "9.0.0.0 Use getWebServerLog().getFileTimeSpan() (hours)");

                  try {
                     this.bean.setLogRotationPeriodMins(Integer.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var13) {
                     System.out.println("Warning: multiple definitions with same name: " + var13.getMessage());
                  }
               } else if (var1.equals("LogRotationTimeBegin")) {
                  try {
                     this.bean.setLogRotationTimeBegin((String)var2);
                  } catch (BeanAlreadyExistsException var12) {
                     System.out.println("Warning: multiple definitions with same name: " + var12.getMessage());
                  }
               } else if (var1.equals("LogRotationType")) {
                  this.handleDeprecatedProperty("LogRotationType", "9.0.0.0 Use getWebServerLog().getRotationType()");

                  try {
                     this.bean.setLogRotationType((String)var2);
                  } catch (BeanAlreadyExistsException var11) {
                     System.out.println("Warning: multiple definitions with same name: " + var11.getMessage());
                  }
               } else if (var1.equals("LogTimeInGMT")) {
                  this.handleDeprecatedProperty("LogTimeInGMT", "9.0.0.0 Use getWebServerLog().getLogTimeInGMT().");

                  try {
                     this.bean.setLogTimeInGMT(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var10) {
                     System.out.println("Warning: multiple definitions with same name: " + var10.getMessage());
                  }
               } else if (var1.equals("Name")) {
                  try {
                     this.bean.setName((String)var2);
                  } catch (BeanAlreadyExistsException var9) {
                     System.out.println("Warning: multiple definitions with same name: " + var9.getMessage());
                  }
               } else if (var1.equals("NetworkAccessPoint")) {
                  try {
                     this.bean.setNetworkAccessPoint((String)var2);
                  } catch (BeanAlreadyExistsException var8) {
                     System.out.println("Warning: multiple definitions with same name: " + var8.getMessage());
                  }
               } else if (var1.equals("VirtualHostNames")) {
                  try {
                     this.bean.setVirtualHostNames(this.parseStringArrayInitializer((String)var2));
                  } catch (BeanAlreadyExistsException var7) {
                     System.out.println("Warning: multiple definitions with same name: " + var7.getMessage());
                  }
               } else if (var1.equals("LogFileLimitEnabled")) {
                  this.handleDeprecatedProperty("LogFileLimitEnabled", "9.0.0.0 Use getWebServerLog().getNumberOfFilesLimited()");

                  try {
                     this.bean.setLogFileLimitEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var6) {
                     System.out.println("Warning: multiple definitions with same name: " + var6.getMessage());
                  }
               } else if (var1.equals("LoggingEnabled")) {
                  this.handleDeprecatedProperty("LoggingEnabled", "9.0.0.0 Use getWebServerLog().isLoggingEnabled().");

                  try {
                     this.bean.setLoggingEnabled(Boolean.valueOf((String)var2));
                  } catch (BeanAlreadyExistsException var5) {
                     System.out.println("Warning: multiple definitions with same name: " + var5.getMessage());
                  }
               } else {
                  var3 = super.bindAttribute(var1, var2);
               }
            }

            return (AttributeBinder)var3;
         }
      } catch (ClassCastException var17) {
         System.out.println(var17 + " name: " + var1 + " class: " + var2.getClass().getName());
         throw var17;
      } catch (RuntimeException var18) {
         throw var18;
      } catch (Exception var19) {
         if (var19 instanceof DistributedManagementException) {
            throw new AssertionError("impossible exception: " + var19);
         } else if (var19 instanceof InvalidAttributeValueException) {
            throw new IllegalArgumentException(var19.getMessage());
         } else {
            throw new AssertionError("unexpected exception: " + var19);
         }
      }
   }
}
