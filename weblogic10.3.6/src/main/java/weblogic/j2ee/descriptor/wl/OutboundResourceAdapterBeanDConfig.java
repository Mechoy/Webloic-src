package weblogic.j2ee.descriptor.wl;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.config.BasicDConfigBean;
import weblogic.deploy.api.spi.config.BasicDConfigBeanRoot;
import weblogic.descriptor.DescriptorBean;

public class OutboundResourceAdapterBeanDConfig extends BasicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   private OutboundResourceAdapterBean beanTreeNode;
   private List connectionDefinitionGroupsDConfig = new ArrayList();

   public OutboundResourceAdapterBeanDConfig(DDBean var1, DescriptorBean var2, DConfigBean var3) throws ConfigurationException {
      super(var1);
      this.beanTreeNode = (OutboundResourceAdapterBean)var2;
      this.beanTree = var2;
      this.parent = (BasicDConfigBean)var3;
      this.initXpaths();
      this.customInit();
   }

   private void initXpaths() throws ConfigurationException {
      ArrayList var1 = new ArrayList();
      var1.add(this.getParentXpath(this.applyNamespace("connection-definition/connectionfactory-interface")));
      this.xpaths = (String[])((String[])var1.toArray(new String[0]));
   }

   private void customInit() throws ConfigurationException {
   }

   public DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException {
      if (debug) {
         Debug.say("Creating child DCB for <" + var1.getXpath() + ">");
      }

      boolean var3 = false;
      ConnectionDefinitionBeanDConfig var4 = null;
      String var5 = var1.getXpath();
      int var8 = 0;
      if (this.lastElementOf(this.xpaths[var8++]).equals(this.lastElementOf(var5))) {
         ConnectionDefinitionBean var9 = null;
         ConnectionDefinitionBean[] var10 = this.beanTreeNode.getConnectionDefinitionGroups();
         if (var10 == null) {
            this.beanTreeNode.createConnectionDefinitionGroup();
            var10 = this.beanTreeNode.getConnectionDefinitionGroups();
         }

         String var7 = this.lastElementOf(this.applyNamespace("connection-definition/connectionfactory-interface"));
         this.setKeyName(var7);
         String var6 = this.getDDKey(var1, var7);
         if (debug) {
            Debug.say("Using keyName: " + var7 + ", key: " + var6);
         }

         for(int var11 = 0; var11 < var10.length; ++var11) {
            var9 = var10[var11];
            if (this.isMatch((DescriptorBean)var9, var1, var6)) {
               break;
            }

            var9 = null;
         }

         if (var9 == null) {
            if (debug) {
               Debug.say("creating new dcb element");
            }

            var9 = this.beanTreeNode.createConnectionDefinitionGroup();
            var3 = true;
         }

         var4 = new ConnectionDefinitionBeanDConfig(var1, (DescriptorBean)var9, var2);
         ((ConnectionDefinitionBeanDConfig)var4).initKeyPropertyValue(var6);
         if (!var4.hasCustomInit()) {
            var4.setParentPropertyName("ConnectionDefinitionGroups");
         }

         if (debug) {
            Debug.say("dcb dump: " + var4.toString());
         }

         this.connectionDefinitionGroupsDConfig.add(var4);
      } else if (debug) {
         Debug.say("Ignoring " + var1.getXpath());

         for(int var12 = 0; var12 < this.xpaths.length; ++var12) {
            Debug.say("xpaths[" + var12 + "]=" + this.xpaths[var12]);
         }
      }

      if (var4 != null) {
         this.addDConfigBean(var4);
         if (var3) {
            var4.setModified(true);

            Object var13;
            for(var13 = var4; ((BasicDConfigBean)var13).getParent() != null; var13 = ((BasicDConfigBean)var13).getParent()) {
            }

            ((BasicDConfigBeanRoot)var13).registerAsListener(var4.getDescriptorBean());
         }

         this.processDCB(var4, var3);
      }

      return var4;
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

   public ConnectionDefinitionPropertiesBean getDefaultConnectionProperties() {
      return this.beanTreeNode.getDefaultConnectionProperties();
   }

   public ConnectionDefinitionBeanDConfig[] getConnectionDefinitionGroups() {
      return (ConnectionDefinitionBeanDConfig[])((ConnectionDefinitionBeanDConfig[])this.connectionDefinitionGroupsDConfig.toArray(new ConnectionDefinitionBeanDConfig[0]));
   }

   void addConnectionDefinitionBean(ConnectionDefinitionBeanDConfig var1) {
      this.addToList(this.connectionDefinitionGroupsDConfig, "ConnectionDefinitionBean", var1);
   }

   void removeConnectionDefinitionBean(ConnectionDefinitionBeanDConfig var1) {
      this.removeFromList(this.connectionDefinitionGroupsDConfig, "ConnectionDefinitionBean", var1);
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
