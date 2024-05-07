package weblogic.ejb.container.interfaces;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.ejb.container.internal.EJBComponentRuntimeMBeanImpl;
import weblogic.ejb.container.timer.MDBTimerManagerFactory;
import weblogic.j2ee.descriptor.ActivationConfigBean;
import weblogic.j2ee.descriptor.wl.SecurityPluginBean;
import weblogic.management.configuration.MigratableTargetMBean;
import weblogic.security.subject.AbstractSubject;
import weblogic.work.WorkManager;

public interface MessageDrivenBeanInfo extends BeanInfo {
   String FACTORY = "weblogic.jms.MessageDrivenBeanConnectionFactory";
   String TOPIC_CONNECTION_FACTORY = "weblogic.jms.MessageDrivenBeanConnectionFactory";
   String QUEUE_CONNECTION_FACTORY = "weblogic.jms.MessageDrivenBeanConnectionFactory";
   String TX_TOPIC_CONNECTION_FACTORY = "weblogic.jms.MessageDrivenBeanConnectionFactory";
   String TX_QUEUE_CONNECTION_FACTORY = "weblogic.jms.MessageDrivenBeanConnectionFactory";
   String MESSAGE_DRIVEN_METHOD_NAME = "onMessage";
   String MESSAGE_DRIVEN_METHOD_SIG = "onMessage()";

   boolean isDestinationQueue();

   boolean isDestinationTopic();

   boolean isDurableSubscriber();

   String getName();

   int getAcknowledgeMode();

   boolean isOnMessageTransacted();

   Integer getOnMessageTxIsolationLevel();

   MethodInfo getOnMessageMethodInfo();

   Class getMessageDrivenLocalObjectClass();

   String getMessagingTypeInterfaceName();

   Class getMessagingTypeInterfaceClass();

   Collection getAllMessagingTypeMethodInfos();

   boolean usesBeanManagedTx();

   Context getInitialContext() throws NamingException;

   Context getInitialContext(String var1, String var2) throws NamingException;

   String getProviderURL();

   String getDestinationName();

   String getConnectionFactoryJNDIName();

   String getMessageSelector();

   boolean noLocalMessages();

   int getMaxConcurrentInstances();

   int getJMSPollingIntervalSeconds();

   int getInitSuspendSeconds();

   int getMaxSuspendSeconds();

   SecurityPluginBean getSecurityPlugin();

   boolean isDurableSubscriptionDeletion();

   String getJMSClientID();

   void deployMessageDrivenBeans() throws Exception;

   void updateJMSPollingIntervalSeconds(int var1);

   List getMDManagerList();

   boolean getIsWeblogicJMS();

   ActivationConfigBean getActivationConfigBean();

   String getResourceAdapterJndiName();

   boolean isDeliveryTransacted(Method var1) throws NoSuchMethodException;

   weblogic.ejb.container.internal.MethodDescriptor getMDBMethodDescriptor(Method var1);

   int getMaxMessagesInTransaction();

   boolean getUse81StylePolling();

   boolean getMinimizeAQSessions();

   String getDestinationResourceLink();

   void setEJBComponentRuntime(EJBComponentRuntimeMBeanImpl var1);

   boolean isIndirectlyImplMessageListener();

   String getGeneratedBeanClassName();

   Class getGeneratedBeanClass();

   void reSetUsernameAndPassword();

   boolean getIsRemoteSubjectExists();

   void unRegister();

   int getTopicMessagesDistributionMode();

   int getDistributedDestinationConnection();

   AbstractSubject getSubject();

   boolean isGenerateUniqueJmsClientId();

   MigratableTargetMBean getMtMBean(String var1);

   boolean getIsInactive();

   WorkManager getCustomWorkManager();

   MDBTimerManagerFactory getTimerManagerFactory();
}
