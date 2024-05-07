package weblogic.deploy.api.spi.config.templates;

import java.beans.PropertyChangeListener;
import java.io.FileNotFoundException;
import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.spi.DConfigBeanRoot;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.WebLogicDConfigBean;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.deploy.api.spi.config.BasicDConfigBeanRoot;
import weblogic.deploy.api.spi.config.DescriptorSupport;

public abstract class ConfigListener implements PropertyChangeListener {
   protected static final boolean debug = Debug.isDebug("config");
   protected WebLogicDConfigBean dcb;
   protected BasicDConfigBeanRoot root;

   protected ConfigListener(WebLogicDConfigBean var1) {
      this.dcb = var1;

      BasicDConfigBean var2;
      for(var2 = (BasicDConfigBean)var1; var2.getParent() != null; var2 = var2.getParent()) {
      }

      this.root = (BasicDConfigBeanRoot)var2;
      if (debug) {
         Debug.say("root is " + this.root.toString());
      }

   }

   protected void addDCB(String var1, DescriptorSupport var2) {
      try {
         DDBeanRoot var3 = this.root.getDDBean().getRoot().getDeployableObject().getDDBeanRoot(var1);
         if (this.root.getDConfigBean(var3, var2) == null && debug) {
            Debug.say("can't create dcb for dd at " + var1);
         }
      } catch (FileNotFoundException var4) {
         SPIDeployerLogger.logNoDCB(var1, var4.getMessage());
      } catch (DDBeanCreateException var5) {
         SPIDeployerLogger.logDDCreateError(var1);
      } catch (ConfigurationException var6) {
         SPIDeployerLogger.logNoDCB(var1, var6.getMessage());
      }

   }

   protected void removeDCB(String var1) {
      try {
         DDBeanRoot var2 = this.root.getDDBean().getRoot().getDeployableObject().getDDBeanRoot(var1);
         this.root.getDeploymentConfiguration().removeDConfigBean((DConfigBeanRoot)this.root.getDConfigBean(var2));
      } catch (Exception var3) {
         if (debug) {
            var3.printStackTrace();
         }
      }

   }
}
