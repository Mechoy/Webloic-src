package weblogic.connector.external;

import java.io.File;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public interface RAComplianceChecker {
   RAComplianceChecker factory = weblogic.connector.external.impl.RAComplianceChecker.factoryHelper;

   RAComplianceChecker createChecker();

   boolean validate(GenericClassLoader var1, VirtualJarFile var2, File var3, File var4, DeploymentPlanBean var5) throws RAComplianceException;
}
