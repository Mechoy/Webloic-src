package weblogic.ejb.spi;

import java.io.File;
import java.util.Collection;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.validation.ModuleValidationInfo;
import weblogic.management.configuration.EJBComponentMBean;
import weblogic.utils.ErrorCollectionException;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.compiler.ICompilerFactory;
import weblogic.utils.jars.VirtualJarFile;

public interface EJBC {
   void compileEJB(GenericClassLoader var1, EJBComponentMBean var2, EjbDescriptorBean var3, VirtualJarFile var4) throws ErrorCollectionException;

   void compileEJB(GenericClassLoader var1, EJBComponentMBean var2, VirtualJarFile var3, VersionHelper var4, Collection var5) throws ErrorCollectionException;

   void compileEJB(GenericClassLoader var1, VirtualJarFile var2, File var3, File var4, DeploymentPlanBean var5, ModuleValidationInfo var6) throws ErrorCollectionException;

   void populateValidationInfo(GenericClassLoader var1, VirtualJarFile var2, File var3, ModuleValidationInfo var4) throws ErrorCollectionException;

   void setCompilerFactory(ICompilerFactory var1);
}
