package weblogic.ejb.container.cmp.rdbms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.persistence.spi.JarDeployment;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl.WeblogicRdbmsJarBean;

public final class RDBMSDeployment implements JarDeployment {
   private Map m_beans = new HashMap();
   private Map m_dependents = new HashMap();
   private Map m_relations = new HashMap();
   private Set fileNames = new HashSet();
   private Set cmpDescriptors = new HashSet();

   public boolean needToReadFile(String var1) {
      return !this.fileNames.contains(var1);
   }

   public void addFileName(String var1) {
      this.fileNames.add(var1);
   }

   public RDBMSBean getRDBMSBean(String var1) {
      assert this.m_beans.get(var1) != null;

      return (RDBMSBean)this.m_beans.get(var1);
   }

   public void addRdbmsBeans(Map var1) throws RDBMSException {
      Iterator var2 = var1.values().iterator();

      while(var2.hasNext()) {
         RDBMSBean var3 = (RDBMSBean)var2.next();
         if (this.m_beans.containsKey(var3.getEjbName())) {
            String var4 = EJBLogger.logDuplicateBeanOrRelationLoggable("weblogic-rdbms-bean", var3.getEjbName()).getMessage();
            Iterator var5 = this.fileNames.iterator();

            while(var5.hasNext()) {
               var4 = var4 + (String)var5.next();
               if (var5.hasNext()) {
                  var4 = var4 + ", ";
               }
            }

            var4 = var4 + ".";
            throw new RDBMSException(var4);
         }

         this.m_beans.put(var3.getEjbName(), var3);
      }

   }

   public void addRdbmsRelations(Map var1) throws RDBMSException {
      Iterator var2 = var1.values().iterator();

      while(var2.hasNext()) {
         RDBMSRelation var3 = (RDBMSRelation)var2.next();
         if (this.m_relations.containsKey(var3.getName())) {
            String var4 = EJBLogger.logDuplicateBeanOrRelationLoggable("weblogic-rdbms-relation", var3.getName()).getMessage();
            Iterator var5 = this.fileNames.iterator();

            while(var5.hasNext()) {
               var4 = var4 + (String)var5.next();
               if (var5.hasNext()) {
                  var4 = var4 + ", ";
               }
            }

            var4 = var4 + ".";
            throw new RDBMSException(var4);
         }

         this.m_relations.put(var3.getName(), var3);
      }

   }

   public Map getRDBMSBeanMap() {
      return this.m_beans;
   }

   public Map getRDBMSRelationMap() {
      return this.m_relations;
   }

   public Map getRDBMSDependentMap() {
      return null;
   }

   public void addDescriptorMBean(WeblogicRdbmsJarBean var1) {
      this.cmpDescriptors.add(var1);
   }

   public WeblogicRdbmsJarBean getDescriptorMBean(String var1) {
      Iterator var2 = this.cmpDescriptors.iterator();

      while(var2.hasNext()) {
         WeblogicRdbmsJarBean var3 = (WeblogicRdbmsJarBean)var2.next();
         WeblogicRdbmsBeanBean[] var4 = var3.getWeblogicRdbmsBeans();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getEjbName().equals(var1)) {
               return var3;
            }
         }
      }

      return null;
   }

   public WeblogicRdbmsBeanBean getWeblogicRDBMSBeanMBean(String var1) {
      Iterator var2 = this.cmpDescriptors.iterator();

      while(var2.hasNext()) {
         WeblogicRdbmsJarBean var3 = (WeblogicRdbmsJarBean)var2.next();
         WeblogicRdbmsBeanBean[] var4 = var3.getWeblogicRdbmsBeans();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].getEjbName().equals(var1)) {
               return var4[var5];
            }
         }
      }

      return null;
   }

   public WeblogicRdbmsJarBean[] getDescriptorMBeans() {
      return (WeblogicRdbmsJarBean[])((WeblogicRdbmsJarBean[])this.cmpDescriptors.toArray(new WeblogicRdbmsJarBean[this.cmpDescriptors.size()]));
   }
}
