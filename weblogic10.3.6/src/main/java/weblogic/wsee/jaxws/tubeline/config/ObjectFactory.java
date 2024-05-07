package weblogic.wsee.jaxws.tubeline.config;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {
   public Config createConfig() {
      return new Config();
   }

   public DeploymentListeners createDeploymentListeners() {
      return new DeploymentListeners();
   }

   public Listeners createListeners() {
      return new Listeners();
   }

   public AssemblerItem createAssemblerItem() {
      return new AssemblerItem();
   }

   public Listener createListener() {
      return new Listener();
   }

   public List createList() {
      return new List();
   }
}
