package weblogic.deploy.api.spi.config.templates;

import java.beans.PropertyChangeEvent;
import java.io.FileNotFoundException;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.DescriptorSupport;
import weblogic.deploy.api.spi.config.DescriptorSupportManager;
import weblogic.j2ee.descriptor.wl.PersistenceUseBean;
import weblogic.j2ee.descriptor.wl.WeblogicEnterpriseBeanBeanDConfig;

public class TypeStorageListener extends ConfigListener {
   public TypeStorageListener(WeblogicEnterpriseBeanBeanDConfig var1) {
      super(var1);
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if (debug) {
         Debug.say("update listener");
      }

      String var2 = var1.getPropertyName();
      if (debug) {
         Debug.say("caught update");
      }

      if ("TypeStorage".equals(var2)) {
         PersistenceUseBean var3 = (PersistenceUseBean)var1.getSource();
         String var4 = (String)var1.getOldValue();
         String var5 = (String)var1.getNewValue();
         if (debug) {
            Debug.say("old: " + var4 + ", new: " + var5);
         }

         if (var4 != null) {
            this.removeDCB(var4);
         }

         if (var5 != null) {
            this.addDCB(var5, var3.getTypeVersion());
         }

      }
   }

   private void addDCB(String var1, String var2) {
      try {
         DDBeanRoot var3 = this.root.getDDBean().getRoot().getDeployableObject().getDDBeanRoot(var1);
         DescriptorSupport var4 = DescriptorSupportManager.getForSecondaryTag("weblogic-rdbms-jar")[0];
         var4.setBaseURI(var1);
         var4.setConfigURI(var1);
         if ("5.1.0".equals(var2)) {
            var4.setBaseNameSpace("http://www.bea.com/ns/weblogic/60");
            var4.setConfigNameSpace("http://www.bea.com/ns/weblogic/60");
            var4.setStandardClassName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBeanImpl");
            var4.setConfigClassName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBeanImpl");
            var4.setDConfigClassName("weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBeanDConfig");
         }

         this.addDCB(var1, var4);
      } catch (FileNotFoundException var5) {
         SPIDeployerLogger.logNoCMPDD(var1);
      } catch (DDBeanCreateException var6) {
         SPIDeployerLogger.logDDCreateError(var1);
      }

   }
}
