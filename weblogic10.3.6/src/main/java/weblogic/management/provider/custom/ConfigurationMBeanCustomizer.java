package weblogic.management.provider.custom;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.Notification;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.mbeans.custom.WebLogic;

public class ConfigurationMBeanCustomizer extends WebLogic implements ConfigurationMBeanCustomized {
   private ConfigurationMBeanCustomized _base;

   public ConfigurationMBeanCustomizer(ConfigurationMBeanCustomized var1) {
      super((WebLogicMBean)var1);
      this._base = var1;
   }

   public ConfigurationMBean getMbean() {
      return this._base.getMbean();
   }

   public void putValue(String var1, Object var2) {
      this._base.putValue(var1, var2);
   }

   public void putValueNotify(String var1, Object var2) {
      this._base.putValueNotify(var1, var2);
   }

   public Object getValue(String var1) {
      return this._base.getValue(var1);
   }

   public boolean isAdmin() {
      return this._base.isAdmin();
   }

   public boolean isConfig() {
      return this._base.isConfig();
   }

   public boolean isEdit() {
      return this._base.isEdit();
   }

   public boolean isRuntime() {
      return this._base.isRuntime();
   }

   public Object clone(ConfigurationMBeanCustomized var1) {
      try {
         ConfigurationMBeanCustomizer var2 = (ConfigurationMBeanCustomizer)super.clone();
         var2._base = var1;
         return var2;
      } catch (CloneNotSupportedException var4) {
         throw new AssertionError(var4);
      }
   }

   public void sendNotification(Notification var1) {
      this._base.sendNotification(var1);
   }

   public void touch() throws ConfigurationException {
      this._base.touch();
   }

   public void freezeCurrentValue(String var1) throws AttributeNotFoundException, MBeanException {
      ((AbstractDescriptorBean)this._base).markSet(var1);
   }

   public void restoreDefaultValue(String var1) throws AttributeNotFoundException {
      ((AbstractDescriptorBean)this._base).unSet(var1);
   }
}
