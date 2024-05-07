package weblogic.deploy.api.spi;

import javax.enterprise.deploy.model.DDBeanRoot;
import javax.enterprise.deploy.spi.DeploymentConfiguration;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.Closable;
import weblogic.deploy.api.internal.utils.InstallDir;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;

public interface WebLogicDeploymentConfiguration extends DeploymentConfiguration, Closable {
   String DEFAULT_APPNAME = "MyApp";

   void restore(DeploymentPlanBean var1) throws ConfigurationException;

   DeploymentPlanBean getPlan();

   String getModuleName(DDBeanRoot var1);

   void addToPlan(WebLogicDConfigBeanRoot var1);

   InstallDir getInstallDir();

   void export(int var1) throws IllegalArgumentException;

   void close();

   boolean isPlanRestored();
}
