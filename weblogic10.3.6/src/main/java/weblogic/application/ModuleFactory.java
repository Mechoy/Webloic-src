package weblogic.application;

import weblogic.j2ee.descriptor.ModuleBean;

public interface ModuleFactory {
   Module createModule(ModuleBean var1) throws ModuleException;
}
