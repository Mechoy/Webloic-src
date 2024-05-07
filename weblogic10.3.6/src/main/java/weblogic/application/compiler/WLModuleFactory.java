package weblogic.application.compiler;

import weblogic.j2ee.descriptor.wl.WeblogicModuleBean;

public interface WLModuleFactory {
   EARModule createModule(WeblogicModuleBean var1);
}
