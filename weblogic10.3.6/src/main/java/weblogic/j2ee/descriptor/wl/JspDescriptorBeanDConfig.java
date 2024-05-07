package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.descriptor.DescriptorBean;

public class JspDescriptorBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private JspDescriptorBean beanTreeNode;

   public JspDescriptorBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (JspDescriptorBean)var2;
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

   public boolean isKeepgenerated() {
      return this.beanTreeNode.isKeepgenerated();
   }

   public void setKeepgenerated(boolean var1) {
      this.beanTreeNode.setKeepgenerated(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Keepgenerated", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getPackagePrefix() {
      return this.beanTreeNode.getPackagePrefix();
   }

   public void setPackagePrefix(String var1) {
      this.beanTreeNode.setPackagePrefix(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PackagePrefix", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getSuperClass() {
      return this.beanTreeNode.getSuperClass();
   }

   public void setSuperClass(String var1) {
      this.beanTreeNode.setSuperClass(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "SuperClass", (Object)null, (Object)null));
      this.setModified(true);
   }

   public int getPageCheckSeconds() {
      return this.beanTreeNode.getPageCheckSeconds();
   }

   public void setPageCheckSeconds(int var1) {
      this.beanTreeNode.setPageCheckSeconds(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PageCheckSeconds", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isPageCheckSecondsSet() {
      return this.beanTreeNode.isPageCheckSecondsSet();
   }

   public boolean isPrecompile() {
      return this.beanTreeNode.isPrecompile();
   }

   public void setPrecompile(boolean var1) {
      this.beanTreeNode.setPrecompile(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Precompile", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isPrecompileContinue() {
      return this.beanTreeNode.isPrecompileContinue();
   }

   public void setPrecompileContinue(boolean var1) {
      this.beanTreeNode.setPrecompileContinue(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PrecompileContinue", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isVerbose() {
      return this.beanTreeNode.isVerbose();
   }

   public void setVerbose(boolean var1) {
      this.beanTreeNode.setVerbose(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Verbose", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getWorkingDir() {
      return this.beanTreeNode.getWorkingDir();
   }

   public void setWorkingDir(String var1) {
      this.beanTreeNode.setWorkingDir(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "WorkingDir", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isPrintNulls() {
      return this.beanTreeNode.isPrintNulls();
   }

   public void setPrintNulls(boolean var1) {
      this.beanTreeNode.setPrintNulls(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "PrintNulls", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isBackwardCompatible() {
      return this.beanTreeNode.isBackwardCompatible();
   }

   public void setBackwardCompatible(boolean var1) {
      this.beanTreeNode.setBackwardCompatible(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "BackwardCompatible", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getEncoding() {
      return this.beanTreeNode.getEncoding();
   }

   public void setEncoding(String var1) {
      this.beanTreeNode.setEncoding(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Encoding", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isExactMapping() {
      return this.beanTreeNode.isExactMapping();
   }

   public void setExactMapping(boolean var1) {
      this.beanTreeNode.setExactMapping(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ExactMapping", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getDefaultFileName() {
      return this.beanTreeNode.getDefaultFileName();
   }

   public void setDefaultFileName(String var1) {
      this.beanTreeNode.setDefaultFileName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "DefaultFileName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isRtexprvalueJspParamName() {
      return this.beanTreeNode.isRtexprvalueJspParamName();
   }

   public void setRtexprvalueJspParamName(boolean var1) {
      this.beanTreeNode.setRtexprvalueJspParamName(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "RtexprvalueJspParamName", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isDebug() {
      return this.beanTreeNode.isDebug();
   }

   public void setDebug(boolean var1) {
      this.beanTreeNode.setDebug(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "Debug", (Object)null, (Object)null));
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

   public boolean isCompressHtmlTemplate() {
      return this.beanTreeNode.isCompressHtmlTemplate();
   }

   public void setCompressHtmlTemplate(boolean var1) {
      this.beanTreeNode.setCompressHtmlTemplate(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "CompressHtmlTemplate", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isOptimizeJavaExpression() {
      return this.beanTreeNode.isOptimizeJavaExpression();
   }

   public void setOptimizeJavaExpression(boolean var1) {
      this.beanTreeNode.setOptimizeJavaExpression(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "OptimizeJavaExpression", (Object)null, (Object)null));
      this.setModified(true);
   }

   public String getResourceProviderClass() {
      return this.beanTreeNode.getResourceProviderClass();
   }

   public void setResourceProviderClass(String var1) {
      this.beanTreeNode.setResourceProviderClass(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "ResourceProviderClass", (Object)null, (Object)null));
      this.setModified(true);
   }

   public boolean isStrictStaleCheck() {
      return this.beanTreeNode.isStrictStaleCheck();
   }

   public void setStrictStaleCheck(boolean var1) {
      this.beanTreeNode.setStrictStaleCheck(var1);
      this.firePropertyChange(new PropertyChangeEvent(this, "StrictStaleCheck", (Object)null, (Object)null));
      this.setModified(true);
   }
}
