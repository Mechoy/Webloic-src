package weblogic.ejb.container.cmp11.rdbms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.persistence.spi.JarDeployment;
import weblogic.ejb20.cmp.rdbms.RDBMSException;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsBeanBean;
import weblogic.j2ee.descriptor.wl60.WeblogicRdbmsJarBean;

public final class RDBMSDeployment implements JarDeployment {
   private Set cmpDescriptors = new HashSet();
   private Set fileNames = new HashSet();
   private Map m_beans = new HashMap();

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

   public WeblogicRdbmsJarBean[] getDescriptorMBeans() {
      return (WeblogicRdbmsJarBean[])((WeblogicRdbmsJarBean[])this.cmpDescriptors.toArray(new WeblogicRdbmsJarBean[this.cmpDescriptors.size()]));
   }

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
}
