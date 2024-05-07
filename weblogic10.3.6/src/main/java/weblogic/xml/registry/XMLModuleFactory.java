package weblogic.xml.registry;

import weblogic.application.Module;
import weblogic.application.ModuleException;
import weblogic.application.WebLogicApplicationModuleFactory;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.XmlBean;

public final class XMLModuleFactory implements WebLogicApplicationModuleFactory {
   public Module[] createModule(WeblogicApplicationBean var1) throws ModuleException {
      XmlBean var2 = var1.getXml();
      return var2 != null ? new Module[]{new XMLModule(var2)} : null;
   }
}
