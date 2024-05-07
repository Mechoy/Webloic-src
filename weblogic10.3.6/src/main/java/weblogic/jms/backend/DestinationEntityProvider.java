package weblogic.jms.backend;

import java.security.AccessController;
import java.util.List;
import javax.naming.Context;
import weblogic.application.ApplicationContext;
import weblogic.application.ModuleException;
import weblogic.j2ee.descriptor.wl.DestinationBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.NamedEntityBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.jms.JMSExceptionLogger;
import weblogic.jms.JMSService;
import weblogic.jms.common.EntityName;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.ModuleName;
import weblogic.jms.deployer.BEDeployer;
import weblogic.jms.module.JMSModuleManagedEntity;
import weblogic.jms.module.JMSModuleManagedEntityProvider;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;

public final class DestinationEntityProvider implements JMSModuleManagedEntityProvider {
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public JMSModuleManagedEntity createEntity(ApplicationContext var1, EntityName var2, Context var3, JMSBean var4, NamedEntityBean var5, List var6, DomainMBean var7) throws ModuleException {
      DestinationBean var8 = (DestinationBean)var5;
      String var9 = var6 == null ? var8.getSubDeploymentName() : ((TargetMBean)var6.get(0)).getName();
      return this.internalCreateEntity(var2, var3, false, var4, var5, var9, var2);
   }

   JMSModuleManagedEntity createTemporaryEntity(JMSBean var1, NamedEntityBean var2, String var3, ModuleName var4) throws ModuleException {
      EntityName var5 = new EntityName(var3, (String)null, var2.getName());
      return this.internalCreateEntity(var5, (Context)null, true, var1, var2, var3, var4);
   }

   private JMSModuleManagedEntity internalCreateEntity(EntityName var1, Context var2, boolean var3, JMSBean var4, NamedEntityBean var5, String var6, ModuleName var7) throws ModuleException {
      BEDeployer var8 = JMSService.getJMSService().getBEDeployer();
      BackEnd var9 = var8.findBackEnd(var6);
      if (var9 == null) {
         throw new ModuleException(JMSExceptionLogger.logNoBackEndLoggable(var6, var1.getEntityName(), var1.getFullyQualifiedModuleName()).getMessage());
      } else {
         SecurityServiceManager.pushSubject(KERNEL_ID, KERNEL_ID);
         Object var10 = null;

         try {
            if (var5 instanceof QueueBean) {
               var10 = new BEQueueRuntimeDelegate(var1, var9, var2, var3, var7, var4, (QueueBean)var5);
            } else {
               var10 = new BETopicRuntimeDelegate(var1, var9, var2, var3, var7, var4, (TopicBean)var5);
            }
         } finally {
            SecurityServiceManager.popSubject(KERNEL_ID);
         }

         if (JMSDebug.JMSConfig.isDebugEnabled()) {
            JMSDebug.JMSConfig.debug("Destination " + var1.getEntityName() + " from module " + var1.getFullyQualifiedModuleName() + " successfully created");
         }

         return (JMSModuleManagedEntity)var10;
      }
   }
}
