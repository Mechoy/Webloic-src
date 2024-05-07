package weblogic.jms;

import weblogic.server.AbstractServerService;

public final class JMSServicePostDeploymentImpl extends AbstractServerService {
   private JMSService jmsService = JMSService.getJMSService();

   public void start() {
      this.jmsService.postDeploymentStart();
   }

   public void stop() {
      this.jmsService.postDeploymentStop();
   }

   public void halt() {
      this.jmsService.postDeploymentHalt();
   }
}
