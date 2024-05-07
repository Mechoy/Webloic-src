package weblogic.wsee.jaxws.tubeline.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
   name = "deployment-listeners",
   propOrder = {"client", "server"}
)
public class DeploymentListeners {
   protected Listeners client;
   protected Listeners server;

   public Listeners getClient() {
      return this.client;
   }

   public void setClient(Listeners var1) {
      this.client = var1;
   }

   public Listeners getServer() {
      return this.server;
   }

   public void setServer(Listeners var1) {
      this.server = var1;
   }
}
