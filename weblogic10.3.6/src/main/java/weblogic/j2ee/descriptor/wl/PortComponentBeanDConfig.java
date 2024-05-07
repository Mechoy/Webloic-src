package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;
import weblogic.j2ee.descriptor.LoginConfigBean;

public class PortComponentBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private PortComponentBean beanTreeNode;

   public PortComponentBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (PortComponentBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      return null;
   }

   public String keyPropertyValue() {
      return this.getPortComponentName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setPortComponentName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("PortComponentName: ");
      var1.append(this.beanTreeNode.getPortComponentName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getPortComponentName() {
      return this.beanTreeNode.getPortComponentName();
   }

   public void setPortComponentName(String var1) {
      this.beanTreeNode.setPortComponentName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PortComponentName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public WebserviceAddressBean getServiceEndpointAddress() {
      return this.beanTreeNode.getServiceEndpointAddress();
   }

   public LoginConfigBean getLoginConfig() {
      return this.beanTreeNode.getLoginConfig();
   }

   public String getTransportGuarantee() {
      return this.beanTreeNode.getTransportGuarantee();
   }

   public void setTransportGuarantee(String var1) {
      this.beanTreeNode.setTransportGuarantee(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TransportGuarantee", (Object)null, (Object)null));
      this.setModified(true);
   }

   public DeploymentListenerListBean getDeploymentListenerList() {
      return this.beanTreeNode.getDeploymentListenerList();
   }

   public WsdlBean getWsdl() {
      return this.beanTreeNode.getWsdl();
   }

   public int getTransactionTimeout() {
      return this.beanTreeNode.getTransactionTimeout();
   }

   public void setTransactionTimeout(int var1) {
      this.beanTreeNode.setTransactionTimeout(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "TransactionTimeout", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getCallbackProtocol() {
      return this.beanTreeNode.getCallbackProtocol();
   }

   public void setCallbackProtocol(String var1) {
      this.beanTreeNode.setCallbackProtocol(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CallbackProtocol", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean getStreamAttachments() {
      return this.beanTreeNode.getStreamAttachments();
   }

   public void setStreamAttachments(boolean var1) {
      this.beanTreeNode.setStreamAttachments(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "StreamAttachments", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isValidateRequest() {
      return this.beanTreeNode.isValidateRequest();
   }

   public void setValidateRequest(boolean var1) {
      this.beanTreeNode.setValidateRequest(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ValidateRequest", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isHttpFlushResponse() {
      return this.beanTreeNode.isHttpFlushResponse();
   }

   public void setHttpFlushResponse(boolean var1) {
      this.beanTreeNode.setHttpFlushResponse(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HttpFlushResponse", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getHttpResponseBuffersize() {
      return this.beanTreeNode.getHttpResponseBuffersize();
   }

   public void setHttpResponseBuffersize(int var1) {
      this.beanTreeNode.setHttpResponseBuffersize(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "HttpResponseBuffersize", (Object)null, (Object)null));
      this.setModified(true);
   }

   public ReliabilityConfigBean getReliabilityConfig() {
      return this.beanTreeNode.getReliabilityConfig();
   }

   public PersistenceConfigBean getPersistenceConfig() {
      return this.beanTreeNode.getPersistenceConfig();
   }

   public BufferingConfigBean getBufferingConfig() {
      return this.beanTreeNode.getBufferingConfig();
   }

   public WSATConfigBean getWSATConfig() {
      return this.beanTreeNode.getWSATConfig();
   }

   public OperationComponentBean[] getOperations() {
      return this.beanTreeNode.getOperations();
   }
}
