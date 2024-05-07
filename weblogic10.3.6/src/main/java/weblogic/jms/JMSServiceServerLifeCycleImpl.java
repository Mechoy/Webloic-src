package weblogic.jms;

import javax.jms.JMSException;
import weblogic.application.ApplicationFactoryManager;
import weblogic.jms.interception.service;
import weblogic.jms.module.JMSDeploymentFactory;
import weblogic.jms.module.JMSModuleFactory;
import weblogic.management.ManagementException;
import weblogic.messaging.interception.exceptions.InterceptionServiceException;
import weblogic.server.ActivatedService;
import weblogic.server.ServiceFailureException;

public final class JMSServiceServerLifeCycleImpl extends ActivatedService {
   private transient JMSService jmsService;

   public void stopService() throws ServiceFailureException {
      JMSLogger.logJMSSuspending();
      this.jmsService.stop(false);
   }

   public void haltService() throws ServiceFailureException {
      JMSLogger.logJMSForceSuspending();
      if (this.jmsService != null) {
         this.jmsService.stop(true);
      }

   }

   public boolean startService() throws ServiceFailureException {
      try {
         service.initialize();
      } catch (InterceptionServiceException var4) {
         JMSLogger.logJMSFailedInit();
         throw new ServiceFailureException("JMS service failed in initialization - registering with Interception Service", var4);
      }

      try {
         this.jmsService = JMSService.getService();
         ApplicationFactoryManager var1 = ApplicationFactoryManager.getApplicationFactoryManager();
         var1.addDeploymentFactory(new JMSDeploymentFactory());
         var1.addWblogicModuleFactory(new JMSModuleFactory());
         this.jmsService.start();
         return true;
      } catch (JMSException var2) {
         JMSLogger.logJMSFailedInit();
         throw new ServiceFailureException("JMS service failed in initialization", var2);
      } catch (ManagementException var3) {
         JMSLogger.logJMSFailedInit();
         throw new ServiceFailureException("JMS service failed in initialization", var3);
      }
   }
}
