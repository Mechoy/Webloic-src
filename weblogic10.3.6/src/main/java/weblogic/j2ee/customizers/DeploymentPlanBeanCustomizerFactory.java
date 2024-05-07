package weblogic.j2ee.customizers;

import weblogic.descriptor.beangen.Customizer;
import weblogic.descriptor.beangen.CustomizerFactory;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;

public class DeploymentPlanBeanCustomizerFactory implements CustomizerFactory {
   public Customizer createCustomizer(Object var1) {
      DeploymentPlanBean var2 = (DeploymentPlanBean)var1;
      return new DeploymentPlanBeanCustomizer(var2);
   }
}
