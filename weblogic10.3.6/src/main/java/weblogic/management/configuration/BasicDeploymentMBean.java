package weblogic.management.configuration;

public interface BasicDeploymentMBean extends TargetInfoMBean {
   String getSourcePath();

   /** @deprecated */
   void setSourcePath(String var1);

   SubDeploymentMBean[] getSubDeployments();

   SubDeploymentMBean createSubDeployment(String var1);

   SubDeploymentMBean lookupSubDeployment(String var1);

   void destroySubDeployment(SubDeploymentMBean var1);

   int getDeploymentOrder();

   void setDeploymentOrder(int var1);

   String getDeploymentPrincipalName();

   void setDeploymentPrincipalName(String var1);
}
