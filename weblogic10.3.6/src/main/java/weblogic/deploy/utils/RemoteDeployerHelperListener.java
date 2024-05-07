package weblogic.deploy.utils;

import weblogic.management.RemoteNotificationListener;

class RemoteDeployerHelperListener extends DeployerHelperListener implements RemoteNotificationListener {
   RemoteDeployerHelperListener(String var1, DeployerHelper var2) {
      super(var1, var2);
   }
}
