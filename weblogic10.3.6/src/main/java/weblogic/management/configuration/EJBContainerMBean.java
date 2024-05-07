package weblogic.management.configuration;

import javax.management.InvalidAttributeValueException;

public interface EJBContainerMBean extends ConfigurationMBean {
   String getJavaCompiler();

   void setJavaCompiler(String var1) throws InvalidAttributeValueException;

   String getJavaCompilerPreClassPath();

   void setJavaCompilerPreClassPath(String var1) throws InvalidAttributeValueException;

   String getJavaCompilerPostClassPath();

   void setJavaCompilerPostClassPath(String var1);

   String getExtraRmicOptions();

   void setExtraRmicOptions(String var1) throws InvalidAttributeValueException;

   boolean getKeepGenerated();

   void setKeepGenerated(boolean var1);

   boolean getForceGeneration();

   void setForceGeneration(boolean var1);

   /** @deprecated */
   String getTmpPath();

   /** @deprecated */
   void setTmpPath(String var1);

   /** @deprecated */
   String getVerboseEJBDeploymentEnabled();

   /** @deprecated */
   void setVerboseEJBDeploymentEnabled(String var1);

   String getExtraEjbcOptions();

   void setExtraEjbcOptions(String var1) throws InvalidAttributeValueException;
}
