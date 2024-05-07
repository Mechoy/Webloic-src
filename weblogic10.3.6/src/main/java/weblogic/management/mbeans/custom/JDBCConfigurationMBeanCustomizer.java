package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.management.DistributedManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class JDBCConfigurationMBeanCustomizer extends ConfigurationMBeanCustomizer {
   transient JDBCSystemResourceMBean delegate;
   private static final String TARGETS = "Targets";

   public JDBCConfigurationMBeanCustomizer(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void setJDBCSystemResource(JDBCSystemResourceMBean var1) {
      this.delegate = var1;
   }

   public JDBCSystemResourceMBean getJDBCSystemResource() {
      return this.delegate;
   }

   public TargetMBean[] getTargets() {
      if (this.delegate != null) {
         return this.delegate.getTargets();
      } else {
         Object var1 = this.getValue("Targets");
         if (var1 == null) {
            return new TargetMBean[0];
         } else {
            if (!(var1 instanceof TargetMBean)) {
               if (!(var1 instanceof WebLogicMBean[])) {
                  return new TargetMBean[0];
               }

               WebLogicMBean[] var2 = (WebLogicMBean[])((WebLogicMBean[])var1);
               TargetMBean[] var3 = new TargetMBean[var2.length];

               for(int var4 = 0; var4 < var2.length; ++var4) {
                  WebLogicMBean var5 = var2[var4];
                  if (!(var5 instanceof TargetMBean)) {
                     return new TargetMBean[0];
                  }

                  var3[var4] = (TargetMBean)var5;
               }

               var1 = var3;
            }

            return (TargetMBean[])((TargetMBean[])var1);
         }
      }
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      if (this.delegate != null) {
         this.delegate.setTargets(var1);
      }

      this.putValueNotify("Targets", var1);
   }

   private int findTargetIndex(TargetMBean[] var1, TargetMBean var2) {
      if (var2 == null) {
         return -1;
      } else {
         String var3 = var2.getName();
         if (var3 == null) {
            return -1;
         } else {
            for(int var4 = 0; var4 < var1.length; ++var4) {
               TargetMBean var5 = var1[var4];
               if (var5 != null) {
                  String var6 = var5.getName();
                  if (var6 != null && var3.equals(var6)) {
                     return var4;
                  }
               }
            }

            return -1;
         }
      }
   }
}
