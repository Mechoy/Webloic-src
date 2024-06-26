package weblogic.ejb.container.persistence.spi;

import java.util.Map;
import weblogic.ejb.spi.WLDeploymentException;
import weblogic.utils.jars.VirtualJarFile;

public interface CMPDeployer {
   void setup(JarDeployment var1) throws Exception;

   void setCMPBeanDescriptor(CMPBeanDescriptor var1);

   void setBeanMap(Map var1);

   void setRelationships(Relationships var1);

   void setDependentMap(Map var1);

   void setParameters(Map var1) throws Exception;

   void readTypeSpecificData(VirtualJarFile var1, String var2) throws Exception;

   void preCodeGeneration(CMPCodeGenerator var1) throws Exception;

   void postCodeGeneration(CMPCodeGenerator var1) throws Exception;

   void initializePersistenceManager(PersistenceManager var1) throws WLDeploymentException;
}
