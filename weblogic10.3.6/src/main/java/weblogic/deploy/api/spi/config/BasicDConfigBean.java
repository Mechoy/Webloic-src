package weblogic.deploy.api.spi.config;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import javax.enterprise.deploy.model.DDBean;
import javax.enterprise.deploy.model.XpathEvent;
import javax.enterprise.deploy.spi.DConfigBean;
import javax.enterprise.deploy.spi.exceptions.BeanNotFoundException;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import weblogic.deploy.api.internal.SPIDeployerLogger;
import weblogic.deploy.api.internal.utils.ConfigHelper;
import weblogic.deploy.api.internal.utils.Debug;
import weblogic.deploy.api.spi.WebLogicDConfigBean;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;

public abstract class BasicDConfigBean implements WebLogicDConfigBean {
   private static final boolean debug = Debug.isDebug("config");
   protected boolean modified = false;
   protected BasicDConfigBean parent = null;
   protected List pclList = new ArrayList();
   private DDBean ddbean = null;
   protected String[] xpaths;
   private List childList = new ArrayList();
   private String name = "unknown";
   protected DescriptorBean beanTree;
   private String keyName = null;
   private String parentPropertyName;
   private boolean doNotWriteYet;
   private static final String FROM_DCB = "FROM_DCB";

   public BasicDConfigBean(DDBean var1) {
      ConfigHelper.checkParam("DDBean", var1);
      if (debug) {
         Debug.say("Creating DConfigBean (" + this.getClass().getName() + ") for " + var1.getXpath());
      }

      this.setDDBean(var1);
      this.setName(this.ddbean.getXpath());
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      ConfigHelper.checkParam("PropertyChangeListener", var1);
      this.pclList.add(var1);
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      int var2 = this.pclList.indexOf(var1);
      if (var2 >= 0) {
         this.pclList.remove(var2);
      }

   }

   public String[] getXpaths() {
      return this.xpaths;
   }

   public void removeDConfigBean(DConfigBean var1) throws BeanNotFoundException {
      int var2 = this.childList.indexOf(var1);
      if (var2 >= 0) {
         ((BasicDConfigBean)var1).removeAllChildren();
         this.childList.remove(var2);
      } else {
         String var3 = SPIDeployerLogger.notAChild(((BasicDConfigBean)var1).getName(), this.getName());
         throw new BeanNotFoundException(var3);
      }
   }

   public DDBean getDDBean() {
      return this.ddbean;
   }

   public DConfigBean getDConfigBean(DDBean var1) throws ConfigurationException {
      ConfigHelper.checkParam("DDBean", var1);
      if (debug) {
         Debug.say(this.getName() + " getting DCB for bean: " + var1.getXpath());
      }

      if (this.isEquals(this, var1)) {
         if (debug) {
            Debug.say("Found DCB for bean: " + this.getName());
         }

         return this;
      } else {
         Iterator var2 = this.getChildBeans().iterator();

         DConfigBean var3;
         do {
            if (!var2.hasNext()) {
               if (debug) {
                  Debug.say("No DCB for bean: " + var1.getXpath());
               }

               var3 = this.createDConfigBean(var1, this);
               return var3;
            }

            var3 = (DConfigBean)var2.next();
         } while(!this.isEquals((BasicDConfigBean)var3, var1));

         if (debug) {
            Debug.say("Found DCB for bean: " + this.getName());
         }

         return var3;
      }
   }

   private boolean isEquals(BasicDConfigBean var1, DDBean var2) {
      return var1.getDDBean().equals(var2);
   }

   protected String getKeyName() {
      return this.keyName;
   }

   protected void setKeyName(String var1) {
      this.keyName = var1;
   }

   public void notifyDDChange(XpathEvent var1) {
   }

   public abstract void validate() throws ConfigurationException;

   public boolean isValid() {
      try {
         this.validate();
         return true;
      } catch (ConfigurationException var2) {
         return false;
      }
   }

   protected void firePropertyChange(PropertyChangeEvent var1) {
      Iterator var2 = this.getListeners().iterator();

      while(var2.hasNext()) {
         ((PropertyChangeListener)var2.next()).propertyChange(var1);
      }

   }

   protected abstract DConfigBean createDConfigBean(DDBean var1, DConfigBean var2) throws ConfigurationException;

   protected String getName() {
      return this.name;
   }

   protected void addDConfigBean(DConfigBean var1) {
      this.childList.add(var1);
   }

   public List getChildBeans() {
      return this.childList;
   }

   private List getListeners() {
      return this.pclList;
   }

   public void removeAllChildren() {
      this.childList = new ArrayList();
   }

   public boolean isModified() {
      return this.modified;
   }

   public void setModified(boolean var1) {
      if (!this.modified) {
         if (debug) {
            Debug.say("modified dcb: " + this.getClass().getName());
         }

         this.modified = true;
         if (var1 && this.parent != null) {
            this.parent.setModified(true);
         }
      }

      if (this.doNotWriteYet) {
         if (debug) {
            Debug.say("set doNotWriteYet to false for dcb " + this);
         }

         this.doNotWriteYet = false;
         ((AbstractDescriptorBean)this.beanTree).setMetaData("FROM_DCB", new Boolean(false));
      }

   }

   public void setUnmodified() {
      this.modified = false;
   }

   protected void addToList(List var1, String var2, Object var3) {
      int var4 = var1.indexOf(var3);
      if (var4 == -1) {
         var1.add(var3);
         this.firePropertyChange(new PropertyChangeEvent(this, var2, (Object)null, (Object)null));
         this.setModified(true);
      }

   }

   protected void removeFromList(List var1, String var2, Object var3) {
      int var4 = var1.indexOf(var3);
      if (var4 != -1) {
         var1.remove(var3);
         this.firePropertyChange(new PropertyChangeEvent(this, var2, (Object)null, (Object)null));
         this.setModified(true);
      }

   }

   protected void replaceList(List var1, String var2, List var3) {
      if (var3 == null) {
         var3 = new ArrayList();
      }

      var1.clear();
      var1.addAll((Collection)var3);
      this.firePropertyChange(new PropertyChangeEvent(this, var2, (Object)null, (Object)null));
      this.setModified(true);
   }

   public String toString() {
      return debug ? this.dumpAll(0) : this.dump(0);
   }

   public DescriptorBean getDescriptorBean() {
      return this.beanTree;
   }

   protected boolean isMatch(DescriptorBean var1, DDBean var2, String var3) {
      String var4 = this.getKeyValue(var1);
      return var4 != null && var4.equals(var3);
   }

   protected String getDDKey(DDBean var1, String var2) {
      if (var2.equals("id")) {
         return var1.getId();
      } else {
         DDBean[] var3 = var1.getChildBean(this.applyNamespace(var2));
         return var3 != null ? ConfigHelper.getText(var3[0]) : null;
      }
   }

   protected String lastElementOf(String var1) {
      int var2 = var1.lastIndexOf("/");
      return var2 == -1 ? var1 : var1.substring(var2 + 1);
   }

   protected String getParentXpath(String var1) {
      if (debug) {
         Debug.say("get parent of " + var1);
      }

      return var1.substring(0, var1.lastIndexOf("/"));
   }

   public String applyNamespace(String var1) {
      BasicDConfigBean var2;
      for(var2 = this; !var2.getDDBean().getXpath().equals("/"); var2 = var2.getParent()) {
      }

      String var3 = ((BasicDConfigBeanRoot)var2).getNamespace();
      if (debug) {
         Debug.say("using " + var3 + " as namespace");
      }

      return ConfigHelper.applyNamespace(var3, var1);
   }

   public BasicDConfigBean getParent() {
      if (debug) {
         Debug.say("Getting parent...");
      }

      return this.parent;
   }

   public String getParentPropertyName() {
      return this.parentPropertyName;
   }

   public void setParentPropertyName(String var1) {
      this.parentPropertyName = var1;
   }

   private String getKeyValue(DescriptorBean var1) {
      if (var1 == null) {
         return null;
      } else {
         String var2 = null;

         try {
            BeanInfo var3 = Introspector.getBeanInfo(var1.getClass());
            PropertyDescriptor[] var4 = var3.getPropertyDescriptors();

            for(int var6 = 0; var6 < var4.length; ++var6) {
               PropertyDescriptor var5 = var4[var6];
               if (Boolean.TRUE.equals(var5.getValue("key"))) {
                  Method var7 = var5.getReadMethod();
                  var2 = (String)var7.invoke(var1);
                  break;
               }
            }
         } catch (Exception var8) {
            SPIDeployerLogger.logBeanError(var1.getClass().getName(), var8);
         }

         return var2;
      }
   }

   private String dumpAll(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(this.dump(var1));
      Iterator var3 = this.getChildBeans().iterator();

      while(var3.hasNext()) {
         var2.append(((BasicDConfigBean)var3.next()).dumpAll(var1 + 1));
      }

      return var2.toString();
   }

   private String dump(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(this.indenter(var1));
      var2.append("Name: ");
      var2.append(this.getName());
      var2.append("\n");
      var2.append(this.indenter(var1));
      var2.append(this.getDCBProperties());
      return var2.toString();
   }

   protected abstract String getDCBProperties();

   private String indenter(int var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(" ");
      }

      return var2.toString();
   }

   private void setDDBean(DDBean var1) {
      this.ddbean = var1;
   }

   private void setName(String var1) {
      this.name = "DConfigBean." + var1;
   }

   protected String _getKeyValue(String[] var1) {
      if (var1 == null) {
         return null;
      } else {
         StringBuffer var2 = new StringBuffer(var1[0]);

         for(int var3 = 1; var3 < var1.length; ++var3) {
            var2 = var2.append(",").append(var1[var3]);
         }

         return var2.toString();
      }
   }

   protected void processDCB(BasicDConfigBean var1, boolean var2) {
      BasicDConfigBean var3;
      for(var3 = var1; var3.getParent() != null; var3 = var3.getParent()) {
      }

      if (((BasicDConfigBeanRoot)var3).isExternal()) {
         if (var1.beanTree != null && var1.beanTree instanceof AbstractDescriptorBean) {
            AbstractDescriptorBean var4 = (AbstractDescriptorBean)var1.beanTree;
            if (var2) {
               var1.setUnmodified();
               var1.doNotWriteYet = true;
               var4.setMetaData("FROM_DCB", new Boolean(true));
            } else if (var4.getMetaData("FROM_DCB") != null) {
               Boolean var5 = (Boolean)var4.getMetaData("FROM_DCB");
               if (var5) {
                  var1.doNotWriteYet = true;
               }
            }

         }
      }
   }

   void clearUnmodifiedElementsFromDescriptor() {
      if (debug) {
         Debug.say(this.getName() + " clearing unmodified descriptor elements");
      }

      TreeSet var1 = new TreeSet();
      TreeSet var2 = new TreeSet();
      Iterator var3 = this.getChildBeans().iterator();

      while(var3.hasNext()) {
         BasicDConfigBean var4 = (BasicDConfigBean)var3.next();
         if (debug) {
            Debug.say("Checking child dconfig bean: " + var4.getName());
         }

         if (var4.isModified()) {
            var4.clearUnmodifiedElementsFromDescriptor();
            if (var4.getParentPropertyName() != null) {
               var2.add(var4.getParentPropertyName());
            }
         }

         if (var4.getParentPropertyName() != null && var4.doNotWriteYet) {
            if (debug) {
               Debug.say("Child dconfig bean is not modified. Parent property is: " + var4.getParentPropertyName());
            }

            var1.add(var4.getParentPropertyName());
         }
      }

      if (!var1.isEmpty()) {
         String var5;
         Iterator var6;
         if (!var2.isEmpty()) {
            var6 = var2.iterator();

            while(var6.hasNext()) {
               var5 = (String)var6.next();
               var1.remove(var5);
            }
         }

         if (!var1.isEmpty()) {
            var6 = var1.iterator();

            while(var6.hasNext()) {
               var5 = (String)var6.next();
               this.getDescriptorBean().unSet(var5);
            }
         }
      }

   }

   void restoreUnmodifiedElementsToDescriptor() {
      if (debug) {
         Debug.say(this.getName() + " restoring unmodified descriptor elements");
      }

      Iterator var1 = this.getChildBeans().iterator();

      while(var1.hasNext()) {
         BasicDConfigBean var2 = (BasicDConfigBean)var1.next();
         if (debug) {
            Debug.say("Checking child dconfig bean: " + var2.getName());
         }

         if (var2.isModified()) {
            var2.restoreUnmodifiedElementsToDescriptor();
         }

         if (var2.getParentPropertyName() != null && this.getDescriptorBean() instanceof AbstractDescriptorBean && var2.doNotWriteYet) {
            if (debug) {
               Debug.say("Child dconfig bean is not modified. Parent property is: " + var2.getParentPropertyName());
            }

            AbstractDescriptorBean var3 = (AbstractDescriptorBean)this.getDescriptorBean();
            var3.markSet(var2.getParentPropertyName());
         }
      }

   }

   public boolean hasCustomInit() {
      return false;
   }
}
