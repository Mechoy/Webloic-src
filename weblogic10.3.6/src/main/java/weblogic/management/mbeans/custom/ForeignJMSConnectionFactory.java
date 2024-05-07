package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.ForeignConnectionFactoryBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public class ForeignJMSConnectionFactory extends ForeignJNDIObject {
   private ForeignConnectionFactoryBean delegate;

   public ForeignJMSConnectionFactory(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(ForeignConnectionFactoryBean var1) {
      this.delegate = var1;
      super.useDelegates(this.delegate);
   }

   public String getUsername() {
      if (this.delegate == null) {
         Object var1 = this.getValue("Username");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getUsername();
      }
   }

   public void setUsername(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("Username", var1);
      } else {
         this.delegate.setUsername(var1);
      }

   }

   public String getPassword() {
      if (this.delegate == null) {
         Object var1 = this.getValue("Password");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getPassword();
      }
   }

   public void setPassword(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("Password", var1);
      } else {
         this.delegate.setPassword(var1);
      }

   }
}
