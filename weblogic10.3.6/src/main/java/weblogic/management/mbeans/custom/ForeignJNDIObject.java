package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.ForeignJNDIObjectBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public abstract class ForeignJNDIObject extends ConfigurationMBeanCustomizer {
   private ForeignJNDIObjectBean delegate;

   public ForeignJNDIObject(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(ForeignJNDIObjectBean var1) {
      this.delegate = var1;
   }

   public String getLocalJNDIName() {
      if (this.delegate == null) {
         Object var1 = this.getValue("LocalJNDIName");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getLocalJNDIName();
      }
   }

   public void setLocalJNDIName(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("LocalJNDIName", var1);
      } else {
         this.delegate.setLocalJNDIName(var1);
      }

   }

   public String getRemoteJNDIName() {
      if (this.delegate == null) {
         Object var1 = this.getValue("RemoteJNDIName");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getRemoteJNDIName();
      }
   }

   public void setRemoteJNDIName(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("RemoteJNDIName", var1);
      } else {
         this.delegate.setRemoteJNDIName(var1);
      }

   }
}
