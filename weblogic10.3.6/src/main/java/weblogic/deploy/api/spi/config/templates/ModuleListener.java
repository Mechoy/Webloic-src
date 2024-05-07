package weblogic.deploy.api.spi.config.templates;

import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.deploy.api.spi.config.DescriptorSupportManager;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBeanDConfig;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;

public class ModuleListener extends ConfigListener {
   private WeblogicModuleBean wmb = null;

   public ModuleListener(WeblogicApplicationBeanDConfig var1) {
      super(var1);
   }

   public void propertyChange(PropertyChangeEvent var1) {
      String var2 = var1.getPropertyName();
      if ("Path".equals(var2)) {
         this.wmb = (WeblogicModuleBean)var1.getSource();
         if (debug) {
            Debug.say("caught update");
         }

         String var3 = (String)var1.getOldValue();
         String var4 = (String)var1.getNewValue();
         if (debug) {
            Debug.say("old: " + var3 + ", new: " + var4);
         }

         if (var3 != null) {
            this.removeDCB(var3);
         }

         if (var4 != null) {
            this.addDCB(var4, var2);
         }

      }
   }

   private void addDCB(String var1, String var2) {
      try {
         DDBeanRoot var3 = this.root.getDDBean().getRoot().getDeployableObject().getDDBeanRoot(var1);
         DescriptorSupport var4 = null;
         if ("JDBC".equals(this.wmb.getType())) {
            var4 = DescriptorSupportManager.getForTag("jdbc-data-source");
         } else if ("JMS".equals(this.wmb.getType())) {
            var4 = DescriptorSupportManager.getForTag("weblogic-jms");
         } else if ("Interception".equals(this.wmb.getType())) {
            var4 = DescriptorSupportManager.getForTag("weblogic-interception");
         }

         if (var4 != null) {
            var4.setBaseURI(var1);
            var4.setConfigURI(var1);
            this.addDCB(var1, var4);
         }
      } catch (FileNotFoundException var5) {
         SPIDeployerLogger.logMissingDD(var1, var2);
      } catch (DDBeanCreateException var6) {
         SPIDeployerLogger.logDDCreateError(var1);
      }

   }
}
