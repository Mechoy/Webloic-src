package weblogic.jms.module;

import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.WeblogicModuleFactory;
import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;
import weblogic.jms.common.JMSDebug;

public class JMSModuleFactory implements WeblogicModuleFactory {
   public Module createModule(WeblogicModuleBean var1) throws ModuleException {
      if ("JMS".equals(var1.getType())) {
         if (JMSDebug.JMSModule.isDebugEnabled()) {
            JMSDebug.JMSModule.debug("JMSModuleFactory:createModule(WebLogicModuleBean),moduleURI=" + var1.getPath());
         }

         return new JMSModule(var1.getPath(), var1.getName());
      } else {
         return null;
      }
   }

   public Module createModule(ModuleType var1) throws ModuleException {
      if (JMSDebug.JMSModule.isDebugEnabled()) {
         JMSDebug.JMSModule.debug("JMSModuleFactory:createModule(ModuleType)");
      }

      return null;
   }
}
