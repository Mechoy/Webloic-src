package weblogic.ejb.container.deployer;

import weblogic.ejb.container.internal.MDConnectionManager;

public interface MDBService {
   void addDeployedMDB(MDConnectionManager var1);

   void removeDeployedMDB(MDConnectionManager var1);

   boolean addMDBStarter(EJBDeployer var1);
}
