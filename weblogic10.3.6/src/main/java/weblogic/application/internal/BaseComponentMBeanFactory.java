package weblogic.application.internal;

import javax.management.InvalidAttributeValueException;
import weblogic.application.ComponentMBeanFactory;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.configuration.ApplicationMBean;
import weblogic.management.configuration.ComponentMBean;
import weblogic.utils.Debug;

public abstract class BaseComponentMBeanFactory {
   public boolean needsApplicationPathMunging() {
      return true;
   }

   protected void dumpCompMBeans(ApplicationMBean var1) {
      Debug.say("** Dumping ComponentMBeans for app " + var1.getName());
      ComponentMBean[] var2 = var1.getComponents();
      if (var2 == null) {
         var2 = new ComponentMBean[0];
      }

      Debug.say("** App has " + var2.length + " ComponentMBeans");

      for(int var3 = 0; var3 < var2.length; ++var3) {
         Debug.say("** Type is " + var2[var3].getClass().getName() + " uri is " + var2[var3].getURI());
      }

   }

   protected String removeExtension(String var1) {
      int var2 = var1.lastIndexOf(".");
      return var2 == -1 ? var1 : var1.substring(0, var2);
   }

   protected final ComponentMBean findOrCreateComponentMBean(ComponentMBeanFactory.MBeanFactory var1, ApplicationMBean var2, String var3) {
      return this.findOrCreateComponentMBean(var1, var2, var3, var3);
   }

   protected final ComponentMBean findOrCreateComponentMBean(ComponentMBeanFactory.MBeanFactory var1, ApplicationMBean var2, String var3, String var4) {
      try {
         ComponentMBean var5 = ComponentMBeanHelper.findComponentMBeanByName(var2, var3, var1.getComponentMBeanType());
         if (var5 != null) {
            return var5;
         } else {
            var5 = var1.newCompMBean(var2, var3);
            var5.setApplication(var2);
            var5.setURI(var4);
            return var5;
         }
      } catch (InvalidAttributeValueException var6) {
         throw new AssertionError(var6);
      }
   }

   protected String getCompatibilityName(String var1, AppDeploymentMBean var2) {
      String var3 = var2.getCompatibilityName();
      return var3 == null ? var1 : var3;
   }
}
