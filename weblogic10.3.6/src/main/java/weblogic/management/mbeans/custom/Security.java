package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.configuration.SecurityMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public final class Security extends ConfigurationMBeanCustomizer {
   private boolean isInConstructor = true;

   private boolean constructed() {
      return !this.isInConstructor;
   }

   public void _postCreate() {
      this.isInConstructor = false;
   }

   public Security(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   private SecurityConfigurationMBean getSecurityConfiguration() {
      SecurityMBean var1 = (SecurityMBean)this.getMbean();
      DomainMBean var2 = (DomainMBean)((DomainMBean)var1.getParent());
      return var2.getSecurityConfiguration();
   }

   public synchronized byte[] getSalt() {
      return this.getSecurityConfiguration().getSalt();
   }

   public synchronized byte[] getEncryptedSecretKey() {
      return this.getSecurityConfiguration().getEncryptedSecretKey();
   }

   public String getSystemUser() {
      return new String("system");
   }

   public void setSystemUser(String var1) {
   }

   public String getConnectionFilter() {
      return this.constructed() ? this.getSecurityConfiguration().getConnectionFilter() : null;
   }

   public void setConnectionFilter(String var1) throws InvalidAttributeValueException {
      if (this.constructed()) {
         this.getSecurityConfiguration().setConnectionFilter(var1);
      }

   }

   public String[] getConnectionFilterRules() {
      return this.constructed() ? this.getSecurityConfiguration().getConnectionFilterRules() : null;
   }

   public void setConnectionFilterRules(String[] var1) {
      if (this.constructed()) {
         this.getSecurityConfiguration().setConnectionFilterRules(var1);
      }

   }

   public boolean getConnectionLoggerEnabled() {
      return this.constructed() ? this.getSecurityConfiguration().getConnectionLoggerEnabled() : false;
   }

   public void setConnectionLoggerEnabled(boolean var1) throws InvalidAttributeValueException {
      if (this.constructed()) {
         this.getSecurityConfiguration().setConnectionLoggerEnabled(var1);
      }

   }
}
