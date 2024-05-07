package weblogic.application;

import weblogic.deploy.container.NonFatalDeploymentException;
import weblogic.j2ee.J2EELogger;
import weblogic.utils.StringUtils;

public class ModuleNotFoundException extends NonFatalDeploymentException {
   static final long serialVersionUID = -9003975495054439803L;

   public ModuleNotFoundException(String var1) {
      super(var1);
   }

   public ModuleNotFoundException(String[] var1) {
      super(J2EELogger.logUrisDidntMatchModulesLoggable(StringUtils.join(var1, ",")).getMessage());
   }
}
