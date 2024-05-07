package weblogic.jms;

import weblogic.corba.j2ee.naming.ORBHelper;
import weblogic.kernel.KernelStatus;

public class WLSJMSEnvironmentImpl extends JMSEnvironment {
   public boolean isThinClient() {
      return ORBHelper.isThinClient();
   }

   public boolean isServer() {
      return KernelStatus.isServer();
   }
}
