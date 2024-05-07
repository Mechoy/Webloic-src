package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class WorkManagerBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private WorkManagerBean beanTreeNode;

   public WorkManagerBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (WorkManagerBean)var2;
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
      return this.getName();
   }

   public void initKeyPropertyValue(String var1) {
      this.setName(var1);
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      var1.append("Name: ");
      var1.append(this.beanTreeNode.getName());
      var1.append("\n");
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getName() {
      return this.beanTreeNode.getName();
   }

   public void setName(String var1) {
      this.beanTreeNode.setName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Name", (Object)null, (Object)null));
      this.setModified(true);
   }

   public ResponseTimeRequestClassBean getResponseTimeRequestClass() {
      return this.beanTreeNode.getResponseTimeRequestClass();
   }

   public FairShareRequestClassBean getFairShareRequestClass() {
      return this.beanTreeNode.getFairShareRequestClass();
   }

   public ContextRequestClassBean getContextRequestClass() {
      return this.beanTreeNode.getContextRequestClass();
   }

   public String getRequestClassName() {
      return this.beanTreeNode.getRequestClassName();
   }

   public void setRequestClassName(String var1) {
      this.beanTreeNode.setRequestClassName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RequestClassName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public MinThreadsConstraintBean getMinThreadsConstraint() {
      return this.beanTreeNode.getMinThreadsConstraint();
   }

   public String getMinThreadsConstraintName() {
      return this.beanTreeNode.getMinThreadsConstraintName();
   }

   public void setMinThreadsConstraintName(String var1) {
      this.beanTreeNode.setMinThreadsConstraintName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MinThreadsConstraintName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public MaxThreadsConstraintBean getMaxThreadsConstraint() {
      return this.beanTreeNode.getMaxThreadsConstraint();
   }

   public String getMaxThreadsConstraintName() {
      return this.beanTreeNode.getMaxThreadsConstraintName();
   }

   public void setMaxThreadsConstraintName(String var1) {
      this.beanTreeNode.setMaxThreadsConstraintName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "MaxThreadsConstraintName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public CapacityBean getCapacity() {
      return this.beanTreeNode.getCapacity();
   }

   public String getCapacityName() {
      return this.beanTreeNode.getCapacityName();
   }

   public void setCapacityName(String var1) {
      this.beanTreeNode.setCapacityName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CapacityName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public WorkManagerShutdownTriggerBean getWorkManagerShutdownTrigger() {
      return this.beanTreeNode.getWorkManagerShutdownTrigger();
   }

   public boolean getIgnoreStuckThreads() {
      return this.beanTreeNode.getIgnoreStuckThreads();
   }

   public void setIgnoreStuckThreads(boolean var1) {
      this.beanTreeNode.setIgnoreStuckThreads(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "IgnoreStuckThreads", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getId() {
      return this.beanTreeNode.getId();
   }

   public void setId(String var1) {
      this.beanTreeNode.setId(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Id", (Object)null, (Object)null));
      this.setModified(true);
   }
}
