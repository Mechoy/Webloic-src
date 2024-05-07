package weblogic.management.configuration;

public interface SubDeploymentMBean extends TargetInfoMBean {
   String getName();

   SubDeploymentMBean[] getSubDeployments();

   SubDeploymentMBean createSubDeployment(String var1);

   SubDeploymentMBean lookupSubDeployment(String var1);

   void destroySubDeployment(SubDeploymentMBean var1);
}
