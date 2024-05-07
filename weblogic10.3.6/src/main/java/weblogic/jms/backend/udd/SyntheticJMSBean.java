package weblogic.jms.backend.udd;

import weblogic.j2ee.descriptor.wl.DestinationKeyBean;
import weblogic.j2ee.descriptor.wl.DistributedQueueBean;
import weblogic.j2ee.descriptor.wl.DistributedTopicBean;
import weblogic.j2ee.descriptor.wl.ForeignServerBean;
import weblogic.j2ee.descriptor.wl.JMSBean;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.j2ee.descriptor.wl.QueueBean;
import weblogic.j2ee.descriptor.wl.QuotaBean;
import weblogic.j2ee.descriptor.wl.SAFErrorHandlingBean;
import weblogic.j2ee.descriptor.wl.SAFImportedDestinationsBean;
import weblogic.j2ee.descriptor.wl.SAFRemoteContextBean;
import weblogic.j2ee.descriptor.wl.TemplateBean;
import weblogic.j2ee.descriptor.wl.TopicBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedQueueBean;
import weblogic.j2ee.descriptor.wl.UniformDistributedTopicBean;

public class SyntheticJMSBean implements JMSBean {
   UDDEntity udd;

   public SyntheticJMSBean(UDDEntity var1) {
      this.udd = var1;
   }

   public JMSConnectionFactoryBean createConnectionFactory(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public DestinationKeyBean createDestinationKey(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public DistributedQueueBean createDistributedQueue(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public DistributedTopicBean createDistributedTopic(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public ForeignServerBean createForeignServer(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public QueueBean createQueue(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public QuotaBean createQuota(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public SAFErrorHandlingBean createSAFErrorHandling(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public SAFImportedDestinationsBean createSAFImportedDestinations(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public SAFRemoteContextBean createSAFRemoteContext(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public TemplateBean createTemplate(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public TopicBean createTopic(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public UniformDistributedQueueBean createUniformDistributedQueue(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public UniformDistributedTopicBean createUniformDistributedTopic(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyConnectionFactory(JMSConnectionFactoryBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyDestinationKey(DestinationKeyBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyDistributedQueue(DistributedQueueBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyDistributedTopic(DistributedTopicBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyForeignServer(ForeignServerBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyQueue(QueueBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyQuota(QuotaBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroySAFErrorHandling(SAFErrorHandlingBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroySAFImportedDestinations(SAFImportedDestinationsBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroySAFRemoteContext(SAFRemoteContextBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyTemplate(TemplateBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyTopic(TopicBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyUniformDistributedQueue(UniformDistributedQueueBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void destroyUniformDistributedTopic(UniformDistributedTopicBean var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public JMSConnectionFactoryBean[] getConnectionFactories() {
      return this.udd.getJMSModuleBean().getConnectionFactories();
   }

   public DestinationKeyBean[] getDestinationKeys() {
      return this.udd.getJMSModuleBean().getDestinationKeys();
   }

   public DistributedQueueBean[] getDistributedQueues() {
      return this.udd.getJMSModuleBean().getDistributedQueues();
   }

   public DistributedTopicBean[] getDistributedTopics() {
      return this.udd.getJMSModuleBean().getDistributedTopics();
   }

   public ForeignServerBean[] getForeignServers() {
      return this.udd.getJMSModuleBean().getForeignServers();
   }

   public QueueBean[] getQueues() {
      return this.udd.getQueues();
   }

   public QuotaBean[] getQuotas() {
      return this.udd.getJMSModuleBean().getQuotas();
   }

   public SAFErrorHandlingBean[] getSAFErrorHandlings() {
      return this.udd.getJMSModuleBean().getSAFErrorHandlings();
   }

   public SAFImportedDestinationsBean[] getSAFImportedDestinations() {
      return this.udd.getJMSModuleBean().getSAFImportedDestinations();
   }

   public SAFRemoteContextBean[] getSAFRemoteContexts() {
      return this.udd.getJMSModuleBean().getSAFRemoteContexts();
   }

   public TemplateBean[] getTemplates() {
      return this.udd.getJMSModuleBean().getTemplates();
   }

   public TopicBean[] getTopics() {
      return this.udd.getTopics();
   }

   public UniformDistributedQueueBean[] getUniformDistributedQueues() {
      return this.udd.getJMSModuleBean().getUniformDistributedQueues();
   }

   public UniformDistributedTopicBean[] getUniformDistributedTopics() {
      return this.udd.getJMSModuleBean().getUniformDistributedTopics();
   }

   public int getVersion() {
      return this.udd.getJMSModuleBean().getVersion();
   }

   public String getNotes() {
      return this.udd.getJMSModuleBean().getNotes();
   }

   public int hashCode() {
      return this.udd.getJMSModuleBean().hashCode();
   }

   public JMSConnectionFactoryBean lookupConnectionFactory(String var1) {
      return this.udd.getJMSModuleBean().lookupConnectionFactory(var1);
   }

   public DestinationKeyBean lookupDestinationKey(String var1) {
      return this.udd.getJMSModuleBean().lookupDestinationKey(var1);
   }

   public DistributedQueueBean lookupDistributedQueue(String var1) {
      return this.udd.getJMSModuleBean().lookupDistributedQueue(var1);
   }

   public DistributedTopicBean lookupDistributedTopic(String var1) {
      return this.udd.getJMSModuleBean().lookupDistributedTopic(var1);
   }

   public ForeignServerBean lookupForeignServer(String var1) {
      return this.udd.getJMSModuleBean().lookupForeignServer(var1);
   }

   public QueueBean lookupQueue(String var1) {
      return this.udd.lookupQueue(var1);
   }

   public QuotaBean lookupQuota(String var1) {
      return this.udd.getJMSModuleBean().lookupQuota(var1);
   }

   public SAFErrorHandlingBean lookupSAFErrorHandling(String var1) {
      return this.udd.getJMSModuleBean().lookupSAFErrorHandling(var1);
   }

   public SAFImportedDestinationsBean lookupSAFImportedDestinations(String var1) {
      return this.udd.getJMSModuleBean().lookupSAFImportedDestinations(var1);
   }

   public SAFRemoteContextBean lookupSAFRemoteContext(String var1) {
      return this.udd.getJMSModuleBean().lookupSAFRemoteContext(var1);
   }

   public TemplateBean lookupTemplate(String var1) {
      return this.udd.getJMSModuleBean().lookupTemplate(var1);
   }

   public TopicBean lookupTopic(String var1) {
      return this.udd.lookupTopic(var1);
   }

   public UniformDistributedQueueBean lookupUniformDistributedQueue(String var1) {
      return this.udd.getJMSModuleBean().lookupUniformDistributedQueue(var1);
   }

   public UniformDistributedTopicBean lookupUniformDistributedTopic(String var1) {
      return this.udd.getJMSModuleBean().lookupUniformDistributedTopic(var1);
   }

   public void setVersion(int var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public void setNotes(String var1) {
      throw new AssertionError("Cannot write to fake bean");
   }

   public boolean isSet(String var1) {
      return true;
   }

   public void unSet(String var1) {
   }
}
