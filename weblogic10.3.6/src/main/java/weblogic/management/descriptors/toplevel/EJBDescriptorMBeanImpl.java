package weblogic.management.descriptors.toplevel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.management.descriptors.BaseXMLElementMBeanImpl;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.Encoding;
import weblogic.management.descriptors.XMLElementMBean;
import weblogic.management.descriptors.cmp11.WeblogicRDBMSBeanMBean;
import weblogic.management.descriptors.cmp11.WeblogicRDBMSJarMBean;
import weblogic.management.descriptors.ejb11.EJBJarMBean;
import weblogic.management.descriptors.ejb20.EJBJarMBeanImpl;
import weblogic.management.descriptors.ejb20.EnterpriseBeansMBeanImpl;
import weblogic.management.descriptors.weblogic.WeblogicEJBJarMBean;
import weblogic.management.descriptors.weblogic.WeblogicEJBJarMBeanImpl;
import weblogic.utils.FileUtils;
import weblogic.utils.io.XMLWriter;
import weblogic.utils.jars.RandomAccessJarFile;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public class EJBDescriptorMBeanImpl extends BaseXMLElementMBeanImpl implements EJBDescriptorMBean {
   private static final String EJB11_DT = "<!DOCTYPE ejb-jar PUBLIC '-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN' 'http://java.sun.com/j2ee/dtds/ejb-jar_1_1.dtd'>";
   private static final String EJB20_DT = "<!DOCTYPE ejb-jar PUBLIC '-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN' 'http://java.sun.com/dtd/ejb-jar_2_0.dtd'>";
   private static final String WL_EJB20_DT = "<!DOCTYPE weblogic-ejb-jar PUBLIC '-//BEA Systems, Inc.//DTD WebLogic 8.1.0 EJB//EN' 'http://www.bea.com/servers/wls810/dtd/weblogic-ejb-jar.dtd'>";
   private static final String WL_CMP11_DT = "<!DOCTYPE weblogic-rdbms-jar PUBLIC  '-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB 1.1 RDBMS Persistence//EN' 'http://www.bea.com/servers/wls600/dtd/weblogic-rdbms11-persistence-600.dtd'>";
   private static final String WL_CMP20_DT = "<!DOCTYPE weblogic-rdbms-jar PUBLIC  '-//BEA Systems, Inc.//DTD WebLogic 8.1.0 EJB RDBMS Persistence//EN' 'http://www.bea.com/servers/wls810/dtd/weblogic-rdbms20-persistence-810.dtd'>";
   private String m_jarFileName;
   private EJBJarMBean m_ejbJar;
   private EjbJarBean ejbJarBean;
   private WeblogicEJBJarMBean m_wlJar;
   private Set m_cmpDescs = new HashSet();
   private Set m_cmp20Descs = new HashSet();
   private String persistentDestination = null;
   private String parsingErrorMessage = null;

   public EJBDescriptorMBeanImpl() {
      EJBJarMBeanImpl var1 = new EJBJarMBeanImpl();
      var1.setEnterpriseBeans(new EnterpriseBeansMBeanImpl());
      this.setEJBJarMBean(var1);
      this.setWeblogicEJBJarMBean(new WeblogicEJBJarMBeanImpl());
   }

   private static void ppp(String var0) {
      System.out.println("[EJBDescriptorMBeanImpl] " + var0);
   }

   public String getParsingErrorMessage() {
      return this.parsingErrorMessage;
   }

   public String getJarFileName() {
      return this.m_jarFileName;
   }

   public EjbJarBean getEjbJarBean() {
      return this.ejbJarBean;
   }

   public EJBJarMBean getEJBJarMBean() {
      return this.m_ejbJar;
   }

   public WeblogicEJBJarMBean getWeblogicEJBJarMBean() {
      return this.m_wlJar;
   }

   public WeblogicRDBMSJarMBean[] getWeblogicRDBMSJarMBeans() {
      return (WeblogicRDBMSJarMBean[])((WeblogicRDBMSJarMBean[])this.m_cmpDescs.toArray(new WeblogicRDBMSJarMBean[this.m_cmpDescs.size()]));
   }

   public weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean[] getWeblogicRDBMSJar20MBeans() {
      return (weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean[])((weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean[])this.m_cmp20Descs.toArray(new weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean[this.m_cmp20Descs.size()]));
   }

   public Set getRDBMSDescriptorFileNames() {
      HashSet var1 = new HashSet();
      Map var2 = this.get11RDBMSDescriptorMap();
      var1.addAll(var2.values());
      Map var3 = this.get20RDBMSDescriptorMap();
      var1.addAll(var3.values());
      return var1;
   }

   public Map get11RDBMSDescriptorMap() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.m_cmpDescs.iterator();

      while(var2.hasNext()) {
         WeblogicRDBMSJarMBean var3 = (WeblogicRDBMSJarMBean)var2.next();
         WeblogicRDBMSBeanMBean[] var4 = var3.getWeblogicRDBMSBeans();
         if (var4.length > 0) {
            String var5 = var4[0].getEJBName();
            Object var6 = null;
            if (var6 != null) {
               Object var7 = null;
               if (var7 != null) {
                  var1.put(var3, var7);
               }
            }
         }
      }

      return var1;
   }

   public Map get20RDBMSDescriptorMap() {
      HashMap var1 = new HashMap();
      Iterator var2 = this.m_cmp20Descs.iterator();

      while(var2.hasNext()) {
         weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean var3 = (weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean)var2.next();
         weblogic.management.descriptors.cmp20.WeblogicRDBMSBeanMBean[] var4 = var3.getWeblogicRDBMSBeans();
         if (var4.length > 0) {
            String var5 = var4[0].getEJBName();
            Object var6 = null;
            if (var6 != null) {
               Object var7 = null;
               if (var7 != null) {
                  var1.put(var3, var7);
               }
            }
         }
      }

      return var1;
   }

   public void setParsingErrorMessage(String var1) {
      this.parsingErrorMessage = var1;
   }

   public void setJarFileName(String var1) {
      this.m_jarFileName = var1;
   }

   public void setEjbJarBean(EjbJarBean var1) {
      this.ejbJarBean = var1;
   }

   public void setEJBJarMBean(EJBJarMBean var1) {
      this.m_ejbJar = var1;
   }

   public void setWeblogicEJBJarMBean(WeblogicEJBJarMBean var1) {
      this.m_wlJar = var1;
   }

   public void setWeblogicRDBMSJarMBeans(WeblogicRDBMSJarMBean[] var1) {
      this.m_cmpDescs.clear();
      this.m_cmpDescs.addAll(Arrays.asList((Object[])var1));
   }

   public void setWeblogicRDBMSJar20MBeans(weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean[] var1) {
      this.m_cmp20Descs.clear();
      this.m_cmp20Descs.addAll(Arrays.asList((Object[])var1));
   }

   public void addWeblogicRDBMSJarMBean(WeblogicRDBMSJarMBean var1) {
      this.m_cmpDescs.add(var1);
   }

   public void removeWeblogicRDBMSJarMBean(WeblogicRDBMSJarMBean var1) {
      this.m_cmpDescs.remove(var1);
   }

   public void addWeblogicRDBMSJar20MBean(weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean var1) {
      this.m_cmp20Descs.add(var1);
   }

   public void removeWeblogicRDBMSJar20MBean(weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean var1) {
      this.m_cmp20Descs.remove(var1);
   }

   public void validate() throws DescriptorValidationException {
      String var1 = "tempValidationDir";
      File var2 = new File(var1);

      try {
         var2.mkdir();
         this.persist(var1, (Properties)null);
         VirtualJarFile var3 = VirtualJarFactory.createVirtualJar(var2);
      } catch (Exception var7) {
         throw new DescriptorValidationException("Error during validation", var7);
      } finally {
         FileUtils.remove(var2);
      }

   }

   public void usePersistenceDestination(String var1) {
      this.persistentDestination = var1;
   }

   public void persist(Properties var1) throws IOException {
      this.persist(this.persistentDestination, var1);
   }

   public void persist() throws IOException {
      this.persist(this.persistentDestination, (Properties)null);
   }

   public void persist(String var1, Properties var2) throws IOException {
      if (var1 == null) {
         throw new RuntimeException("No persistentDestination set!");
      } else {
         File var3 = new File(var1);
         if (var3.isDirectory()) {
            this.persistToDirectory(var3, var2);
         } else {
            this.persistToJarFile(var3, var2);
         }

      }
   }

   public void persistEJBJarDescriptor(OutputStream var1, boolean var2) throws IOException {
      if (this.m_ejbJar instanceof weblogic.management.descriptors.ejb20.EJBJarMBean) {
         weblogic.management.descriptors.ejb20.EJBJarMBean var3 = (weblogic.management.descriptors.ejb20.EJBJarMBean)this.m_ejbJar;
         String var4 = var3.getEncoding();
         if (var4 != null) {
            this.persistDescriptor(var1, "<!DOCTYPE ejb-jar PUBLIC '-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN' 'http://java.sun.com/dtd/ejb-jar_2_0.dtd'>", var3, var2, var4);
         } else {
            this.persistDescriptor(var1, "<!DOCTYPE ejb-jar PUBLIC '-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 2.0//EN' 'http://java.sun.com/dtd/ejb-jar_2_0.dtd'>", var3, var2);
         }
      } else {
         String var5 = this.m_ejbJar.getEncoding();
         if (var5 != null) {
            this.persistDescriptor(var1, "<!DOCTYPE ejb-jar PUBLIC '-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN' 'http://java.sun.com/j2ee/dtds/ejb-jar_1_1.dtd'>", this.m_ejbJar, var2, var5);
         } else {
            this.persistDescriptor(var1, "<!DOCTYPE ejb-jar PUBLIC '-//Sun Microsystems, Inc.//DTD Enterprise JavaBeans 1.1//EN' 'http://java.sun.com/j2ee/dtds/ejb-jar_1_1.dtd'>", this.m_ejbJar, var2);
         }
      }

   }

   public void persistWLEJBJarDescriptor(OutputStream var1, boolean var2) throws IOException {
      String var3 = this.m_wlJar.getEncoding();
      if (var3 != null) {
         this.persistDescriptor(var1, "<!DOCTYPE weblogic-ejb-jar PUBLIC '-//BEA Systems, Inc.//DTD WebLogic 8.1.0 EJB//EN' 'http://www.bea.com/servers/wls810/dtd/weblogic-ejb-jar.dtd'>", this.m_wlJar, var2, var3);
      } else {
         this.persistDescriptor(var1, "<!DOCTYPE weblogic-ejb-jar PUBLIC '-//BEA Systems, Inc.//DTD WebLogic 8.1.0 EJB//EN' 'http://www.bea.com/servers/wls810/dtd/weblogic-ejb-jar.dtd'>", this.m_wlJar, var2);
      }

   }

   public void persist11RDBMSJarDescriptor(OutputStream var1, WeblogicRDBMSJarMBean var2, boolean var3) throws IOException {
      String var4 = var2.getEncoding();
      if (var4 != null) {
         this.persistDescriptor(var1, "<!DOCTYPE weblogic-rdbms-jar PUBLIC  '-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB 1.1 RDBMS Persistence//EN' 'http://www.bea.com/servers/wls600/dtd/weblogic-rdbms11-persistence-600.dtd'>", var2, var3, var4);
      } else {
         this.persistDescriptor(var1, "<!DOCTYPE weblogic-rdbms-jar PUBLIC  '-//BEA Systems, Inc.//DTD WebLogic 6.0.0 EJB 1.1 RDBMS Persistence//EN' 'http://www.bea.com/servers/wls600/dtd/weblogic-rdbms11-persistence-600.dtd'>", var2, var3);
      }

   }

   public void persist20RDBMSJarDescriptor(OutputStream var1, weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean var2, boolean var3) throws IOException {
      String var4 = var2.getEncoding();
      if (var4 != null) {
         this.persistDescriptor(var1, "<!DOCTYPE weblogic-rdbms-jar PUBLIC  '-//BEA Systems, Inc.//DTD WebLogic 8.1.0 EJB RDBMS Persistence//EN' 'http://www.bea.com/servers/wls810/dtd/weblogic-rdbms20-persistence-810.dtd'>", var2, var3, var4);
      } else {
         this.persistDescriptor(var1, "<!DOCTYPE weblogic-rdbms-jar PUBLIC  '-//BEA Systems, Inc.//DTD WebLogic 8.1.0 EJB RDBMS Persistence//EN' 'http://www.bea.com/servers/wls810/dtd/weblogic-rdbms20-persistence-810.dtd'>", var2, var3);
      }

   }

   private void persistToDirectory(File var1, Properties var2) throws IOException {
      File var3 = new File(var1 + File.separator + "META-INF");
      var3.mkdirs();
      File var4 = new File(var3, "ejb-jar.xml");
      FileOutputStream var5 = new FileOutputStream(var4);
      this.persistEJBJarDescriptor(var5, true);
      File var6 = new File(var3, "weblogic-ejb-jar.xml");
      FileOutputStream var7 = new FileOutputStream(var6);
      this.persistWLEJBJarDescriptor(var7, true);
      Map var8 = this.get11RDBMSDescriptorMap();
      Iterator var9 = var8.keySet().iterator();

      File var12;
      FileOutputStream var13;
      while(var9.hasNext()) {
         WeblogicRDBMSJarMBean var10 = (WeblogicRDBMSJarMBean)var9.next();
         String var11 = (String)var8.get(var10);
         var12 = new File(var1, var11);
         var12.getParentFile().mkdirs();
         var13 = new FileOutputStream(var12);
         this.persist11RDBMSJarDescriptor(var13, var10, true);
      }

      Map var16 = this.get20RDBMSDescriptorMap();
      Iterator var17 = var16.keySet().iterator();

      while(var17.hasNext()) {
         weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean var18 = (weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean)var17.next();
         String var19 = (String)var16.get(var18);
         File var14 = new File(var1, var19);
         FileOutputStream var15 = new FileOutputStream(var14);
         this.persist20RDBMSJarDescriptor(var15, var18, true);
      }

      if (var2 != null) {
         var12 = new File(var3.getPath(), "_wl_dynamic_change_list.properties");
         var13 = new FileOutputStream(var12);
         var2.store(var13, "Dynamic DD change list");
         var13.close();
      }

   }

   private void persistToJarFile(File var1, Properties var2) throws IOException {
      String var3 = ".";
      RandomAccessJarFile var4 = new RandomAccessJarFile(new File(var3), var1);

      try {
         OutputStream var5 = var4.writeEntry("META-INF/ejb-jar.xml", true);
         this.persistEJBJarDescriptor(var5, true);
         OutputStream var6 = var4.writeEntry("META-INF/weblogic-ejb-jar.xml", true);
         this.persistWLEJBJarDescriptor(var6, true);
         Map var7 = this.get11RDBMSDescriptorMap();
         Iterator var8 = var7.keySet().iterator();

         OutputStream var11;
         while(var8.hasNext()) {
            WeblogicRDBMSJarMBean var9 = (WeblogicRDBMSJarMBean)var8.next();
            String var10 = (String)var7.get(var9);
            var11 = var4.writeEntry(var10, true);
            this.persist11RDBMSJarDescriptor(var11, var9, true);
         }

         Map var17 = this.get20RDBMSDescriptorMap();
         Iterator var18 = var17.keySet().iterator();

         while(var18.hasNext()) {
            weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean var19 = (weblogic.management.descriptors.cmp20.WeblogicRDBMSJarMBean)var18.next();
            String var12 = (String)var17.get(var19);
            OutputStream var13 = var4.writeEntry(var12, true);
            this.persist20RDBMSJarDescriptor(var13, var19, true);
         }

         if (var2 != null) {
            var11 = var4.writeEntry("META-INF/_wl_dynamic_change_list.properties", true);
            var2.store(var11, "Dynamic DD change list");
            var11.close();
         }
      } finally {
         if (var4 != null) {
            var4.close();
         }

      }

   }

   private void persistDescriptor(OutputStream var1, String var2, XMLElementMBean var3, boolean var4) throws IOException {
      XMLWriter var5 = null;

      try {
         var5 = new XMLWriter(var1);
         var5.println(var2 + "\n\n");
         var5.println("<!-- Generated XML! -->\n");
         var5.print(var3.toXML(0));
      } catch (UnsupportedEncodingException var10) {
         var10.printStackTrace();
      } finally {
         if (var5 != null) {
            var5.flush();
            if (var4) {
               var5.close();
            }
         }

      }

   }

   private void persistDescriptor(OutputStream var1, String var2, XMLElementMBean var3, boolean var4, String var5) throws IOException {
      XMLWriter var6 = null;

      try {
         String var7 = Encoding.getIANA2JavaMapping(var5);
         if (var7 == null) {
            var7 = Encoding.getJava2IANAMapping(var5);
         }

         if (var7 == null) {
            if (!Charset.isSupported(var5)) {
               throw new UnsupportedEncodingException("Unsupported encoding " + var5);
            }

            var7 = var5;
         }

         var6 = new XMLWriter(var1, var7);
         var6.println("<?xml version=\"1.0\" encoding=\"" + var5 + "\"?>");
         var6.println(var2 + "\n\n");
         var6.println("<!-- Generated XML! -->\n");
         var6.print(var3.toXML(0));
      } catch (UnsupportedEncodingException var11) {
         var11.printStackTrace();
      } finally {
         if (var6 != null) {
            var6.flush();
            if (var4) {
               var6.close();
            }
         }

      }

   }
}
