package weblogic.deploy.api.model;

import javax.enterprise.deploy.model.DeployableObject;
import javax.enterprise.deploy.shared.ModuleType;
import weblogic.application.compiler.AppMerge;
import weblogic.descriptor.DescriptorBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public interface EditableDeployableObject extends DeployableObject {
   void setVirtualJarFile(VirtualJarFile var1);

   void setClassLoader(GenericClassLoader var1);

   void setResourceFinder(ClassFinder var1);

   void setRootBean(DescriptorBean var1);

   void addRootBean(String var1, DescriptorBean var2, ModuleType var3);

   void setAppMerge(AppMerge var1);

   void setContextRoot(String var1);
}
