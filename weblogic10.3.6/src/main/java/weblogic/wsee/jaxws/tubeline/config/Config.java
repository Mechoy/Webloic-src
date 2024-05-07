package weblogic.wsee.jaxws.tubeline.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "",
   propOrder = {"deploymentListeners"}
)
@XmlRootElement(
   name = "config"
)
public class Config {
   @XmlElement(
      name = "deployment-listeners",
      required = true
   )
   protected DeploymentListeners deploymentListeners;

   public DeploymentListeners getDeploymentListeners() {
      return this.deploymentListeners;
   }

   public void setDeploymentListeners(DeploymentListeners var1) {
      this.deploymentListeners = var1;
   }
}
