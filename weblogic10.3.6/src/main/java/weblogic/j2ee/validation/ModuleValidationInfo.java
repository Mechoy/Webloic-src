package weblogic.j2ee.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.ejb.spi.EJBValidationInfo;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.j2ee.descriptor.wl.JMSBean;

public final class ModuleValidationInfo {
   private final String moduleURI;
   private Map ejbs = new HashMap();
   private Collection ejbRefs = new ArrayList();
   private Collection dataSourceRefs = new ArrayList();
   private Map cacheRefs = new HashMap();
   private Collection jmsLinkRefs = new ArrayList();
   private Collection jdbcLinkRefs = new ArrayList();
   private String moduleName = null;
   private JMSBean jmsBean = null;
   private JDBCDataSourceBean jdbcDataSourceBean = null;

   public ModuleValidationInfo(String var1) {
      this.moduleURI = var1;
   }

   public String getURI() {
      return this.moduleURI;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public void setModuleName(String var1) {
      this.moduleName = var1;
   }

   public JMSBean getJMSBean() {
      return this.jmsBean;
   }

   public void setJMSBean(JMSBean var1) {
      this.jmsBean = var1;
   }

   public JDBCDataSourceBean getJDBCDataSourceBean() {
      return this.jdbcDataSourceBean;
   }

   public void setJDBCDataSourceBean(JDBCDataSourceBean var1) {
      this.jdbcDataSourceBean = var1;
   }

   public void addEJBValidationInfo(String var1, EJBValidationInfo var2) {
      this.ejbs.put(var1, var2);
   }

   public Map getEJBMap() {
      return this.ejbs;
   }

   public void addEJBRef(String var1, String var2, boolean var3, String var4, String var5, String var6, String var7, boolean var8) {
      this.ejbRefs.add(new EJBRef(var1, var2, var3, var4, var5, var6, var7, var8));
   }

   public void addEJBRef(String var1, boolean var2, String var3, String var4, String var5, String var6, boolean var7) {
      this.addEJBRef((String)null, var1, var2, var3, var4, var5, var6, var7);
   }

   public Collection getEJBRefs() {
      return this.ejbRefs;
   }

   public boolean containsEJB(String var1) {
      return this.ejbs != null ? this.ejbs.containsKey(var1) : false;
   }

   public EJBValidationInfo getEJBValidationInfo(String var1) {
      return (EJBValidationInfo)this.ejbs.get(var1);
   }

   public void addAppScopedCacheReference(String var1, String var2) {
      this.cacheRefs.put(var2, var1);
   }

   public Map getAppScopedCacheReferences() {
      return this.cacheRefs;
   }

   public void addAppScopedDataSourceReference(String var1) {
      this.dataSourceRefs.add(var1);
   }

   public void addJMSLinkRefs(String var1, String var2, String var3, String var4, String var5, boolean var6) {
      this.jmsLinkRefs.add(new JLinkRef(var1, var2, var3, var4, var5, var6));
   }

   public JLinkRef findJMSLinkRef(String var1, String var2, String var3, boolean var4) {
      JLinkRef var5 = null;
      synchronized(this.jmsLinkRefs) {
         Iterator var7 = this.jmsLinkRefs.iterator();

         while(var7.hasNext()) {
            JLinkRef var8 = (JLinkRef)var7.next();
            if (var8.getResRefName().equals(var3) && var8.getAppComponentName().equals(var1) && var8.getAppComponentType().equals(var2) && var8.isEnv() == var4) {
               var5 = var8;
            }
         }

         return var5;
      }
   }

   public Collection getJMSLinkRefs() {
      return this.jmsLinkRefs;
   }

   public void addJDBCLinkRefs(String var1, String var2, String var3, String var4, String var5, boolean var6) {
      this.jdbcLinkRefs.add(new JLinkRef(var1, var2, var3, var4, var5, var6));
   }

   public JLinkRef findJDBCLinkRef(String var1, String var2, String var3, boolean var4) {
      JLinkRef var5 = null;
      synchronized(this.jdbcLinkRefs) {
         Iterator var7 = this.jdbcLinkRefs.iterator();

         while(var7.hasNext()) {
            JLinkRef var8 = (JLinkRef)var7.next();
            if (var8.getResRefName().equals(var3) && var8.getAppComponentName().equals(var1) && var8.getAppComponentType().equals(var2) && var8.isEnv() == var4) {
               var5 = var8;
            }
         }

         return var5;
      }
   }

   public Collection getJDBCLinkRefs() {
      return this.jdbcLinkRefs;
   }

   public class JLinkRef {
      private String appComponentName;
      private String appComponentType;
      private String resRefName;
      private String resLinkName;
      private String resRefType;
      private boolean isEnv;

      public JLinkRef(String var2, String var3, String var4, String var5, String var6, boolean var7) {
         this.appComponentName = var2;
         this.appComponentType = var3;
         this.resRefName = var4;
         this.resLinkName = var5;
         this.resRefType = var6;
         this.isEnv = var7;
      }

      public String getAppComponentName() {
         return this.appComponentName;
      }

      public String getAppComponentType() {
         return this.appComponentType;
      }

      public String getResRefName() {
         return this.resRefName;
      }

      public String getResLinkName() {
         return this.resLinkName;
      }

      public String getResRefType() {
         return this.resRefType;
      }

      public boolean isEnv() {
         return this.isEnv;
      }
   }

   public class EJBRef {
      private String ejbName;
      private String ejbRefName;
      private boolean local;
      private String componentInterfaceName;
      private String homeInterfaceName;
      private String refType;
      private String link;
      private boolean isProvisional;

      public EJBRef(String var2, String var3, boolean var4, String var5, String var6, String var7, String var8, boolean var9) {
         this.ejbName = var2;
         this.ejbRefName = var3;
         this.local = var4;
         this.componentInterfaceName = var5;
         this.homeInterfaceName = var6;
         this.refType = var7;
         this.link = var8;
         this.isProvisional = var9;
      }

      public String getEJBName() {
         return this.ejbName;
      }

      public String getEJBRefName() {
         return this.ejbRefName;
      }

      public String getComponentInterfaceName() {
         return this.componentInterfaceName;
      }

      public String getHomeInterfaceName() {
         return this.homeInterfaceName;
      }

      public String getRefType() {
         return this.refType;
      }

      public boolean isLocal() {
         return this.local;
      }

      public boolean isProvisional() {
         return this.isProvisional;
      }

      public String getEJBLink() {
         return this.link;
      }
   }
}
