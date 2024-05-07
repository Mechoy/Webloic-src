package weblogic.j2ee.descriptor.wl60;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class FinderBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private FinderBean beanTreeNode;

   public FinderBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (FinderBean)var2;
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
      return null;
   }

   public void initKeyPropertyValue(String var1) {
   }

   public String getDCBProperties() {
      StringBuffer var1 = new StringBuffer();
      return var1.toString();
   }

   public void validate() throws ConfigurationException {
   }

   public String getFinderName() {
      return this.beanTreeNode.getFinderName();
   }

   public void setFinderName(String var1) {
      this.beanTreeNode.setFinderName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FinderName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String[] getFinderParams() {
      return this.beanTreeNode.getFinderParams();
   }

   public void setFinderParams(String[] var1) {
      this.beanTreeNode.setFinderParams(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FinderParams", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getFinderQuery() {
      return this.beanTreeNode.getFinderQuery();
   }

   public void setFinderQuery(String var1) {
      this.beanTreeNode.setFinderQuery(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FinderQuery", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getFinderSql() {
      return this.beanTreeNode.getFinderSql();
   }

   public void setFinderSql(String var1) {
      this.beanTreeNode.setFinderSql(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FinderSql", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isFindForUpdate() {
      return this.beanTreeNode.isFindForUpdate();
   }

   public void setFindForUpdate(boolean var1) {
      this.beanTreeNode.setFindForUpdate(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FindForUpdate", (Object)null, (Object)null));
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
