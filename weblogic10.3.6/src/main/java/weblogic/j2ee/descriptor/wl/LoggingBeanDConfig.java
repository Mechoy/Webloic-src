package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class LoggingBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private LoggingBean beanTreeNode;

   public LoggingBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (LoggingBean)var2;
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

   public String getLogFilename() {
      return this.beanTreeNode.getLogFilename();
   }

   public void setLogFilename(String var1) {
      this.beanTreeNode.setLogFilename(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LogFilename", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isLoggingEnabled() {
      return this.beanTreeNode.isLoggingEnabled();
   }

   public void setLoggingEnabled(boolean var1) {
      this.beanTreeNode.setLoggingEnabled(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LoggingEnabled", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getRotationType() {
      return this.beanTreeNode.getRotationType();
   }

   public void setRotationType(String var1) {
      this.beanTreeNode.setRotationType(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RotationType", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isNumberOfFilesLimited() {
      return this.beanTreeNode.isNumberOfFilesLimited();
   }

   public void setNumberOfFilesLimited(boolean var1) {
      this.beanTreeNode.setNumberOfFilesLimited(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "NumberOfFilesLimited", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getFileCount() {
      return this.beanTreeNode.getFileCount();
   }

   public void setFileCount(int var1) {
      this.beanTreeNode.setFileCount(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FileCount", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getFileSizeLimit() {
      return this.beanTreeNode.getFileSizeLimit();
   }

   public void setFileSizeLimit(int var1) {
      this.beanTreeNode.setFileSizeLimit(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FileSizeLimit", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRotateLogOnStartup() {
      return this.beanTreeNode.isRotateLogOnStartup();
   }

   public void setRotateLogOnStartup(boolean var1) {
      this.beanTreeNode.setRotateLogOnStartup(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RotateLogOnStartup", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getLogFileRotationDir() {
      return this.beanTreeNode.getLogFileRotationDir();
   }

   public void setLogFileRotationDir(String var1) {
      this.beanTreeNode.setLogFileRotationDir(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "LogFileRotationDir", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getRotationTime() {
      return this.beanTreeNode.getRotationTime();
   }

   public void setRotationTime(String var1) {
      this.beanTreeNode.setRotationTime(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RotationTime", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getFileTimeSpan() {
      return this.beanTreeNode.getFileTimeSpan();
   }

   public void setFileTimeSpan(int var1) {
      this.beanTreeNode.setFileTimeSpan(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "FileTimeSpan", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDateFormatPattern() {
      return this.beanTreeNode.getDateFormatPattern();
   }

   public void setDateFormatPattern(String var1) {
      this.beanTreeNode.setDateFormatPattern(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DateFormatPattern", (Object)null, (Object)null));
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
