package weblogic.application.descriptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.zip.ZipEntry;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public abstract class AbstractDescriptorLoader {
   private boolean dump = Debug.getCategory("weblogic.application.descriptor").isEnabled();
   private DescriptorManager dm;
   private final XMLInputFactory xiFactory;
   private Descriptor descriptor;
   private Descriptor planMergedDescriptor;
   private File altDD;
   private VirtualJarFile vjar;
   private GenericClassLoader gcl;
   private DeploymentPlanBean plan;
   private String moduleName;
   private File configDir;
   protected boolean debug;
   protected BasicMunger munger;
   private boolean doInit;
   DescriptorBean descriptorBean;

   public AbstractDescriptorLoader(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
      this.dm = AbstractDescriptorLoader.READONLY_SINGLETON.instance;
      this.xiFactory = XMLInputFactory.newInstance();
      this.debug = false;
      this.doInit = true;
      this.vjar = var1;
      this.configDir = var2;
      this.plan = var3;
      this.moduleName = var4;
   }

   public AbstractDescriptorLoader(File var1, File var2, DeploymentPlanBean var3, String var4) {
      this.dm = AbstractDescriptorLoader.READONLY_SINGLETON.instance;
      this.xiFactory = XMLInputFactory.newInstance();
      this.debug = false;
      this.doInit = true;
      this.altDD = var1;
      this.configDir = var2;
      this.plan = var3;
      this.moduleName = var4;
   }

   public AbstractDescriptorLoader(GenericClassLoader var1) {
      this.dm = AbstractDescriptorLoader.READONLY_SINGLETON.instance;
      this.xiFactory = XMLInputFactory.newInstance();
      this.debug = false;
      this.doInit = true;
      this.gcl = var1;
   }

   public AbstractDescriptorLoader(GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4) {
      this.dm = AbstractDescriptorLoader.READONLY_SINGLETON.instance;
      this.xiFactory = XMLInputFactory.newInstance();
      this.debug = false;
      this.doInit = true;
      this.gcl = var1;
      this.configDir = var2;
      this.plan = var3;
      this.moduleName = var4;
   }

   public AbstractDescriptorLoader(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
      this.dm = AbstractDescriptorLoader.READONLY_SINGLETON.instance;
      this.xiFactory = XMLInputFactory.newInstance();
      this.debug = false;
      this.doInit = true;
      this.gcl = var2;
      this.dm = var1;
      this.plan = var4;
      this.moduleName = var5;
   }

   public AbstractDescriptorLoader(DescriptorManager var1, GenericClassLoader var2) {
      this.dm = AbstractDescriptorLoader.READONLY_SINGLETON.instance;
      this.xiFactory = XMLInputFactory.newInstance();
      this.debug = false;
      this.doInit = true;
      this.gcl = var2;
      this.dm = var1;
   }

   public AbstractDescriptorLoader(VirtualJarFile var1) {
      this.dm = AbstractDescriptorLoader.READONLY_SINGLETON.instance;
      this.xiFactory = XMLInputFactory.newInstance();
      this.debug = false;
      this.doInit = true;
      this.vjar = var1;
   }

   public AbstractDescriptorLoader(File var1) {
      this.dm = AbstractDescriptorLoader.READONLY_SINGLETON.instance;
      this.xiFactory = XMLInputFactory.newInstance();
      this.debug = false;
      this.doInit = true;
      this.altDD = var1;
   }

   public DeploymentPlanBean getDeploymentPlan() {
      return this.plan;
   }

   public File getConfigDir() {
      return this.configDir;
   }

   public String getModuleName() {
      return this.moduleName;
   }

   public String getAbsolutePath() {
      if (this.altDD == null) {
         if (this.vjar == null && this.gcl == null) {
            ClassLoader var1 = Thread.currentThread().getContextClassLoader();
            return var1.toString() + "/" + this.getDocumentURI();
         } else {
            return this.vjar == null ? this.gcl.toString() + "/" + this.getDocumentURI() : this.vjar.getName() + "/" + this.getDocumentURI();
         }
      } else {
         return this.altDD.getAbsolutePath();
      }
   }

   public DescriptorBean getRootDescriptorBean() throws IOException, XMLStreamException {
      if (this.doInit) {
         this.descriptor = this.getDescriptor();
         if (this.descriptor == null) {
            DescriptorBean var1 = this.getDescriptorBeanFromPlan();
            if (var1 != null) {
               this.descriptor = var1.getDescriptor();
            }

            return var1;
         }
      }

      return this.descriptor == null ? null : this.descriptor.getRootBean();
   }

   public Descriptor getDescriptor() throws IOException, XMLStreamException {
      if (this.doInit) {
         this.doInit = false;
         this.descriptor = this.createDescriptor();
      }

      return this.descriptor;
   }

   protected void setDescriptor(Descriptor var1) throws IOException, XMLStreamException {
      this.doInit = false;
      this.descriptor = var1;
   }

   public abstract String getDocumentURI();

   public String getNamespaceURI() {
      return null;
   }

   public Map getElementNameChanges() {
      return Collections.EMPTY_MAP;
   }

   public DescriptorBean getDescriptorBeanFromPlan() throws IOException, XMLStreamException {
      File var1 = this.getPlanFile();
      if (var1 == null) {
         return null;
      } else if (var1.exists() && var1.isFile()) {
         FileInputStream var2 = new FileInputStream(var1);

         DescriptorBean var3;
         try {
            var3 = this.createDescriptor(var2).getRootBean();
         } finally {
            try {
               var2.close();
            } catch (Exception var10) {
            }

         }

         return var3;
      } else {
         return null;
      }
   }

   private File getPlanFile() {
      if (this.getDeploymentPlan() != null && this.getConfigDir() != null) {
         ModuleDescriptorBean var1 = this.getDeploymentPlan().findModuleDescriptor(this.getModuleName(), this.getDocumentURI());
         if (var1 == null) {
            return null;
         } else {
            File var2;
            if (this.getDeploymentPlan().rootModule(this.getModuleName())) {
               var2 = this.getConfigDir();
            } else {
               var2 = new File(this.getConfigDir(), this.getModuleName());
            }

            return new File(var2, var1.getUri());
         }
      } else {
         return null;
      }
   }

   protected Descriptor createDescriptor() throws IOException, XMLStreamException {
      InputStream var1 = this.getInputStream();
      if (var1 == null) {
         return null;
      } else {
         Descriptor var2;
         try {
            var2 = this.createDescriptor(var1);
         } finally {
            try {
               var1.close();
            } catch (Exception var9) {
            }

         }

         return var2;
      }
   }

   protected Descriptor createDescriptor(InputStream var1) throws IOException, XMLStreamException {
      Descriptor var3;
      try {
         XMLStreamReader var2 = this.getXMLStreamReader(var1);
         var3 = this.getDescriptorManager().createDescriptor(var2);
      } finally {
         try {
            var1.close();
         } catch (Exception var10) {
         }

      }

      return var3;
   }

   protected Descriptor createDescriptor(InputStream var1, boolean var2) throws IOException, XMLStreamException {
      Descriptor var4;
      try {
         XMLStreamReader var3 = this.getXMLStreamReader(var1);
         var4 = this.getDescriptorManager().createDescriptor(var3, var2);
      } finally {
         try {
            var1.close();
         } catch (Exception var11) {
         }

      }

      return var4;
   }

   protected DescriptorManager getDescriptorManager() {
      return this.dm;
   }

   public InputStream getInputStream() throws IOException {
      if (this.altDD == null) {
         if (this.vjar == null && this.gcl == null) {
            ClassLoader var2 = Thread.currentThread().getContextClassLoader();
            return var2.getResourceAsStream(this.getDocumentURI());
         } else if (this.vjar == null) {
            return this.gcl.getResourceAsStream(this.getDocumentURI());
         } else {
            ZipEntry var1 = this.vjar.getEntry(this.getDocumentURI());
            return var1 == null ? null : this.vjar.getInputStream(var1);
         }
      } else {
         return new FileInputStream(this.altDD);
      }
   }

   protected XMLStreamReader getXMLStreamReader(InputStream var1) throws XMLStreamException {
      if (this.munger == null) {
         this.munger = this.createXMLStreamReader(var1);
      }

      return this.munger;
   }

   protected BasicMunger createXMLStreamReader(InputStream var1) throws XMLStreamException {
      return new BasicMunger(this.createXMLStreamReaderDelegate(var1), this);
   }

   public XMLStreamReader createXMLStreamReaderDelegate(InputStream var1) throws XMLStreamException {
      return this.xiFactory.createXMLStreamReader(var1);
   }

   public BasicMunger createNullMunger() {
      String var1 = this.getMunger().getNamespaceURI();
      ArrayList var2 = new ArrayList();
      this.getMunger().toQueuedEvents(var2);
      byte var3 = 0;

      while(var3 < var2.size() && ((ReaderEvent)((ReaderEvent)var2.get(var3))).getEventType() != 1) {
      }

      String var4 = ((ReaderEvent)((ReaderEvent)var2.get(var3))).getLocalName();
      String var5 = "<ns:" + var4 + " xmlns:ns=\"" + this.getMunger().getNamespaceURI() + "\">" + "</ns:" + var4 + ">";
      XMLStreamReader var6 = null;

      try {
         var6 = this.createXMLStreamReaderDelegate(new ByteArrayInputStream(var5.getBytes()));
      } catch (Exception var8) {
      }

      BasicMunger var7 = new BasicMunger(var6, this);
      var7.setDtdNamespaceURI(this.getMunger().getNamespaceURI());
      return var7;
   }

   public void toXML(PrintStream var1) throws IOException, XMLStreamException {
      if (this.munger == null) {
         this.getRootDescriptorBean();
      }

      this.munger.toXML(var1);
   }

   protected BasicMunger getMergingMunger() throws IOException, XMLStreamException {
      return this.munger;
   }

   public void merge(AbstractDescriptorLoader var1) throws IOException, XMLStreamException {
      BasicMunger var2 = this.getMergingMunger();
      if (var2 == null) {
         this.getRootDescriptorBean();
         var2 = this.getMergingMunger();
      }

      if (var2 != null) {
         var2.merge(var1.getMergingMunger());
      }
   }

   public void mergeDescriptor(AbstractDescriptorLoader var1) throws IOException, XMLStreamException {
      DescriptorBean var2 = this.getMergedDescriptorBean(var1);
      if (var2 != null) {
         this.setDescriptor(var2.getDescriptor());
      }

   }

   public DescriptorBean getMergedDescriptorBean(AbstractDescriptorLoader var1) throws IOException, XMLStreamException {
      DescriptorBean var2 = var1.getRootDescriptorBean();
      if (var2 == null) {
         return null;
      } else {
         this.merge(var1);
         var2 = this.getMergedDescriptorBean();
         if (var2 != null) {
            this.setDescriptor(var2.getDescriptor());
         }

         return var2;
      }
   }

   public DescriptorBean getMergedDescriptorBean() throws IOException, XMLStreamException {
      ArrayList var1 = new ArrayList();

      try {
         Descriptor var3;
         try {
            BasicMunger var2 = this.createNullMunger();
            this.munger.toQueuedEvents(var1);
            var2.setQueuedEvents(var1);
            var2.setForceNoBaseStreamHasNext(true);
            var3 = this.getDescriptorManager().createDescriptor(var2);
            DescriptorBean var4 = var3.getRootBean();
            return var4;
         } catch (Exception var9) {
            var9.printStackTrace();
            var3 = null;
            return var3;
         }
      } finally {
         ;
      }
   }

   public DescriptorBean getPlanMergedDescriptorBean() throws IOException, XMLStreamException {
      if (this.planMergedDescriptor == null) {
         this.planMergedDescriptor = this.getPlanMergedDescriptor();
      }

      if (this.planMergedDescriptor == null) {
         return null;
      } else {
         if (this.debug || this.dump) {
            this.planMergedDescriptor.toXML(System.out);
         }

         return this.planMergedDescriptor.getRootBean();
      }
   }

   public Descriptor getPlanMergedDescriptor() throws IOException, XMLStreamException {
      Object var1 = this.getInputStream();

      try {
         Descriptor var4;
         if (var1 == null) {
            DescriptorBean var2 = this.getRootDescriptorBean();
            File var3;
            if (var2 == null) {
               var3 = null;
               return var3;
            }

            var3 = this.getPlanFile();
            if (var3 == null) {
               var4 = null;
               return var4;
            }

            if (!var3.exists() || !var3.isFile()) {
               var4 = null;
               return var4;
            }

            var1 = new FileInputStream(var3);
         }

         ArrayList var17 = new ArrayList();
         if (this.munger == null) {
            this.getRootDescriptorBean();
         }

         this.munger.mergePlan(var17);
         if (var17.size() == 0) {
            Descriptor var19 = this.getRootDescriptorBean().getDescriptor();
            return var19;
         } else {
            BasicMunger var18 = this.createXMLStreamReader((InputStream)var1);
            var18.setQueuedEvents(var17);
            var4 = this.getDescriptorManager().createDescriptor(var18, false);
            return var4;
         }
      } finally {
         if (var1 != null) {
            try {
               ((InputStream)var1).close();
            } catch (Exception var15) {
            }
         }

      }
   }

   DescriptorBean findOrCreateDescriptorBean(InputStream var1) {
      return this.descriptorBean != null ? this.descriptorBean : null;
   }

   InputStream getInputStreamFromPlan() {
      return null;
   }

   DescriptorBean mergeDescriptorBeanWithPlan() throws IOException {
      if (this.descriptorBean == null) {
         return null;
      } else {
         this.descriptorBean = this.getMunger().mergeDescriptorBeanWithPlan(this);
         return this.descriptorBean;
      }
   }

   BasicMunger getMunger() {
      return this.munger;
   }

   DescriptorBean getMergedDescriptorBean2(AbstractDescriptorLoader var1) {
      return null;
   }

   public DescriptorBean getDescriptorBean() throws IOException {
      if (this.descriptorBean != null) {
         return this.descriptorBean;
      } else {
         InputStream var1 = this.getInputStream();

         DescriptorBean var2;
         try {
            if (this.plan == null) {
               var2 = this.findOrCreateDescriptorBean(var1);
               return var2;
            }

            if (var1 == null) {
               this.findOrCreateDescriptorBean(this.getInputStreamFromPlan());
            } else {
               this.findOrCreateDescriptorBean(var1);
            }

            this.mergeDescriptorBeanWithPlan();
            var2 = this.descriptorBean;
         } finally {
            if (var1 != null) {
               try {
                  var1.close();
               } catch (Exception var10) {
               }
            }

         }

         return var2;
      }
   }

   private static class READONLY_SINGLETON {
      static DescriptorManager instance = new DescriptorManager();
   }
}
