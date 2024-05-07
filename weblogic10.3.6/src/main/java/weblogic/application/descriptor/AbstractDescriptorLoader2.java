package weblogic.application.descriptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.classloaders.Source;
import weblogic.utils.jars.VirtualJarFile;

public class AbstractDescriptorLoader2 {
   protected boolean debug;
   private DescriptorManager dm;
   private DescriptorBean rootBean;
   protected BasicMunger2 munger;
   private File altDD;
   private VirtualJarFile vjar;
   private GenericClassLoader gcl;
   private DeploymentPlanBean plan;
   private File configDir;
   private String moduleName;
   private String documentURI;
   private String namespaceURI;
   private Map elementNameChanges;
   private InputStream inputStream;
   protected String versionInfo;
   private static boolean schemaValidationEnabled = getBooleanProperty("weblogic.descriptor.schemaValidationEnabled", true);
   private boolean validateSchema;
   private List errorHolder;

   private static boolean getBooleanProperty(String var0, boolean var1) {
      String var2 = System.getProperty(var0);
      return var2 != null ? Boolean.parseBoolean(var2) : var1;
   }

   protected AbstractDescriptorLoader2() {
      this.debug = Debug.getCategory("weblogic.descriptor.loader").isEnabled();
      this.elementNameChanges = Collections.EMPTY_MAP;
      this.validateSchema = schemaValidationEnabled;
      this.errorHolder = null;
   }

   public AbstractDescriptorLoader2(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      this();
      this.vjar = var1;
      this.configDir = var2;
      this.plan = var3;
      this.moduleName = var4;
      this.documentURI = var5;
   }

   public AbstractDescriptorLoader2(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      this();
      this.altDD = var1;
      this.configDir = var2;
      this.plan = var3;
      this.moduleName = var4;
      this.documentURI = var5;
   }

   public AbstractDescriptorLoader2(GenericClassLoader var1, String var2) {
      this();
      this.gcl = var1;
      this.documentURI = var2;
   }

   public AbstractDescriptorLoader2(GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      this();
      this.gcl = var1;
      this.configDir = var2;
      this.plan = var3;
      this.moduleName = var4;
      this.documentURI = var5;
   }

   public AbstractDescriptorLoader2(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
      this();
      this.gcl = var2;
      this.dm = var1;
      this.plan = var4;
      this.moduleName = var5;
      this.documentURI = var6;
   }

   public AbstractDescriptorLoader2(DescriptorManager var1, GenericClassLoader var2, String var3) {
      this();
      this.gcl = var2;
      this.dm = var1;
      this.documentURI = var3;
   }

   public AbstractDescriptorLoader2(VirtualJarFile var1, String var2) {
      this();
      this.vjar = var1;
      this.documentURI = var2;
   }

   public AbstractDescriptorLoader2(File var1, String var2) {
      this();
      this.altDD = var1;
      this.documentURI = var2;
   }

   public AbstractDescriptorLoader2(InputStream var1) {
      this(var1, (DescriptorManager)null, (List)null, true);
   }

   public AbstractDescriptorLoader2(InputStream var1, DescriptorManager var2, List var3, boolean var4) {
      this.debug = Debug.getCategory("weblogic.descriptor.loader").isEnabled();
      this.elementNameChanges = Collections.EMPTY_MAP;
      this.validateSchema = schemaValidationEnabled;
      this.errorHolder = null;
      this.inputStream = var1;
      this.dm = var2;
      this.errorHolder = var3;
      this.validateSchema = var4;
   }

   public DeploymentPlanBean getDeploymentPlan() {
      return this.plan;
   }

   public File getConfigDir() {
      return this.configDir;
   }

   public String getModuleType() {
      return "";
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

   public String getDocumentURI() {
      return this.documentURI;
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   public void setNamespaceURI(String var1) {
      this.namespaceURI = var1;
   }

   public Map getElementNameChanges() {
      return this.elementNameChanges;
   }

   public void setElementNameChanges(Map var1) {
      this.elementNameChanges = var1;
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

   protected DescriptorManager getDescriptorManager() {
      if (this.dm == null) {
         this.dm = AbstractDescriptorLoader2.READONLY_SINGLETON.instance;
      }

      return this.dm;
   }

   public InputStream getInputStream() throws IOException {
      if (this.inputStream != null) {
         return this.inputStream;
      } else if (this.altDD == null) {
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

   public void toXML(PrintStream var1) throws IOException, XMLStreamException {
      this.getMunger().toXML(var1);
   }

   protected XMLStreamReader createXMLStreamReader(InputStream var1) throws XMLStreamException {
      return new BasicMunger2(var1, this);
   }

   protected DescriptorBean createDescriptorBean(InputStream var1) throws IOException, XMLStreamException {
      DescriptorBean var2;
      try {
         this.munger = (BasicMunger2)this.createXMLStreamReader(var1);
         this.versionInfo = this.munger.getVersionInfo();
         if (!this.munger.hasDTD() && !(this.munger instanceof VersionMunger)) {
            var2 = this.getDescriptorBeanFromReader(this.munger);
            return var2;
         }

         var2 = this.getDescriptorBeanFromReader(this.munger.getPlaybackReader());
      } finally {
         if (this.getMunger() != null) {
            this.getMunger().close();
         }

      }

      return var2;
   }

   protected void setValidateSchema(boolean var1) {
      this.validateSchema = var1;
   }

   InputStream getInputStreamFromPlan() throws IOException {
      File var1 = this.getPlanFile();
      if (var1 == null) {
         return null;
      } else {
         return var1.exists() && var1.isFile() ? new FileInputStream(var1) : null;
      }
   }

   public DescriptorBean mergeDescriptors(VirtualJarFile[] var1) throws IOException, XMLStreamException {
      return this._mergeDescriptors(VirtualJarFile.class, var1, (File[])null);
   }

   public DescriptorBean mergeDescriptors(Object[] var1) throws IOException, XMLStreamException {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Source var4 = (Source)var1[var3];
         URL var5 = var4.getURL();
         String var6 = var5.getProtocol();
         if ("file".equals(var6)) {
            var2.add(new File(var5.getPath()));
         }
      }

      File[] var7 = (File[])((File[])var2.toArray(new File[0]));
      return this.mergeDescriptors(var7);
   }

   public DescriptorBean mergeDescriptors(File[] var1) throws IOException, XMLStreamException {
      return this._mergeDescriptors(File.class, (VirtualJarFile[])null, var1);
   }

   private DescriptorBean _mergeDescriptors(Class var1, VirtualJarFile[] var2, File[] var3) throws IOException, XMLStreamException {
      assert var1 != null && (var1 == VirtualJarFile.class || var1 == File.class);

      try {
         BasicMunger2 var4 = null;
         PlaybackReader var5 = null;
         int var6 = 0;
         if (var1 == VirtualJarFile.class) {
            var6 = var2.length;
         } else if (var1 == File.class) {
            var6 = var3.length;
         }

         assert var6 != 0;

         for(int var7 = var6 - 1; var7 > -1; --var7) {
            Constructor var8 = this.getClass().getConstructor(var1, File.class, DeploymentPlanBean.class, String.class, String.class);
            AbstractDescriptorLoader2 var9 = null;
            if (var1 == VirtualJarFile.class) {
               var9 = (AbstractDescriptorLoader2)var8.newInstance(var2[var7], null, null, null, this.getDocumentURI());
            } else {
               var9 = (AbstractDescriptorLoader2)var8.newInstance(var3[var7], null, null, null, this.getDocumentURI());
            }

            var9.setDescriptorManager(this.getDescriptorManager());
            var9.loadDescriptorBean();
            if (var4 == null) {
               var4 = var9.getMunger();
               var5 = var4.getPlaybackReader();
            } else {
               var5 = var4.merge(var9.getMunger(), (DescriptorBean)null);
            }
         }

         DescriptorBean var22;
         if (var4 == null) {
            var22 = this.loadDescriptorBean();
            return var22;
         } else {
            this.loadDescriptorBeanWithoutPlan();
            if (this.getMunger() != null) {
               var5 = var4.merge(this.getMunger(), (DescriptorBean)null);
            }

            this.munger = var4;
            if (this.plan != null) {
               var22 = this.mergeDescriptorBeanWithPlan(this.getDeploymentPlan(), this.getModuleName(), this.getDocumentURI());
               return var22;
            } else {
               var22 = this.getDescriptorBeanFromReader(var5);
               return var22;
            }
         }
      } catch (NoSuchMethodException var17) {
         throw new AssertionError("Descriptor loader subclasses must define a constructor with a \"" + var1.toString().substring(var1.toString().lastIndexOf(".") + 1) + ",File,DeploymentPlanBean,String,String\" signature: " + this.getClass().getName() + ": " + var17);
      } catch (InstantiationException var18) {
         throw new AssertionError("Descriptor loader subclasses must define a constructor with a \"" + var1.toString().substring(var1.toString().lastIndexOf(".") + 1) + ",File,DeploymentPlanBean,String,String\" signature: " + var18);
      } catch (IllegalAccessException var19) {
         throw new AssertionError("Descriptor loader subclasses must define a constructor with a \"" + var1.toString().substring(var1.toString().lastIndexOf(".") + 1) + ",File,DeploymentPlanBean,String,String\" signature: " + var19);
      } catch (InvocationTargetException var20) {
         throw new AssertionError("Descriptor loader subclasses must define a constructor with a \"" + var1.toString().substring(var1.toString().lastIndexOf(".") + 1) + ",File,DeploymentPlanBean,String,String\" signature: " + var20.getTargetException());
      } finally {
         ;
      }
   }

   public DescriptorBean mergeDescriptorBean(AbstractDescriptorLoader2 var1) throws IOException, XMLStreamException {
      return this.mergeDescriptorBean(var1, (DescriptorBean)null);
   }

   public DescriptorBean mergeDescriptorBean(AbstractDescriptorLoader2 var1, DescriptorBean var2) throws IOException, XMLStreamException {
      BasicMunger2 var3 = null;
      DescriptorBean var7;
      if (var2 != null && var2.getDescriptor().isModified()) {
         ByteArrayOutputStream var4 = new ByteArrayOutputStream();
         this.dm.writeDescriptorAsXML(var2.getDescriptor(), var4);
         byte[] var5 = var4.toByteArray();
         ByteArrayInputStream var6 = new ByteArrayInputStream(var5);
         var3 = (BasicMunger2)this.createXMLStreamReader(var6);
         var7 = null;
         if (var3 instanceof VersionMunger) {
            this.getDescriptorBeanFromReader(var3.getPlaybackReader());
         } else {
            this.getDescriptorBeanFromReader(var3);
         }
      }

      BasicMunger2 var12 = var1.getOrCreateMunger();
      BasicMunger2 var13 = this.getOrCreateMunger();
      if (var13 == null && var12 == null && var3 == null) {
         return null;
      } else {
         var12 = this.merge(var3, var12);
         this.munger = this.merge(var13, var12);
         PlaybackReader var14 = this.munger.getPlaybackReader();

         try {
            var7 = this.getDescriptorBeanFromReader(var14);
         } finally {
            this.getMunger().close();
         }

         return var7;
      }
   }

   private BasicMunger2 merge(BasicMunger2 var1, BasicMunger2 var2) throws IOException, XMLStreamException {
      if (var1 != null && var2 != null) {
         var2.merge(var1, (DescriptorBean)null);
         return var2;
      } else if (var2 != null) {
         return var2;
      } else {
         return var1 != null ? var1 : null;
      }
   }

   public void updateDescriptorWithBean(DescriptorBean var1) throws IOException, XMLStreamException {
      BasicMunger2 var2 = null;
      if (var1 != null) {
         ByteArrayOutputStream var3 = new ByteArrayOutputStream();
         this.getDescriptorManager().writeDescriptorAsXML(var1.getDescriptor(), var3);
         var3.flush();
         byte[] var4 = var3.toByteArray();
         ByteArrayInputStream var5 = new ByteArrayInputStream(var4);
         var2 = (BasicMunger2)this.createXMLStreamReader(var5);
         Object var6 = null;
         if (var2 instanceof VersionMunger) {
            this.getDescriptorBeanFromReader(var2.getPlaybackReader());
         } else {
            this.getDescriptorBeanFromReader(var2);
         }
      }

      if (var2 != null) {
         this.munger = var2;
      }

   }

   protected DescriptorBean parseBean() throws XMLStreamException, IOException {
      DescriptorBean var1;
      try {
         var1 = this.getDescriptorBeanFromReader(this.getMunger().getPlaybackReader());
      } finally {
         this.getMunger().close();
      }

      return var1;
   }

   DescriptorBean mergeDescriptorBeanWithPlan(DescriptorBean var1) throws IOException, XMLStreamException {
      PlaybackReader var2 = this.getMunger().mergeDescriptorBeanWithPlan(var1);

      DescriptorBean var3;
      try {
         var3 = this.getDescriptorBeanFromReader(var2);
      } finally {
         this.getMunger().close();
      }

      return var3;
   }

   private DescriptorBean mergeDescriptorBeanWithPlan(DeploymentPlanBean var1, String var2, String var3) throws IOException, XMLStreamException {
      PlaybackReader var4 = this.getMunger().mergeDescriptorBeanWithPlan(var1, var2, var3);

      DescriptorBean var5;
      try {
         var5 = this.getDescriptorBeanFromReader(var4);
      } finally {
         this.getMunger().close();
      }

      return var5;
   }

   protected BasicMunger2 getMunger() {
      return this.munger;
   }

   private BasicMunger2 getOrCreateMunger() throws IOException, XMLStreamException {
      if (this.munger == null) {
         this.loadDescriptorBean();
      }

      return this.munger;
   }

   private DescriptorBean loadDescriptorBeanWithoutPlan() throws IOException, XMLStreamException {
      DescriptorBean var1 = this.rootBean;
      if (var1 != null) {
         return var1;
      } else if (this.munger != null) {
         return this.parseBean();
      } else {
         InputStream var2 = this.getInputStream();
         if (var2 == null) {
            var2 = this.getInputStreamFromPlan();
         }

         if (var2 != null) {
            var1 = this.createDescriptorBean(var2);
         }

         return var1;
      }
   }

   public DescriptorBean loadDescriptorBean() throws IOException, XMLStreamException {
      DescriptorBean var2;
      try {
         DescriptorBean var1 = this.loadDescriptorBeanWithoutPlan();
         if (var1 != null && !this.getMunger().hasDTD() && this.plan != null) {
            var2 = this.mergeDescriptorBeanWithPlan((DescriptorBean)null);
            return var2;
         }

         var2 = var1;
      } finally {
         if (this.getMunger() != null) {
            this.getMunger().close();
         }

      }

      return var2;
   }

   private DescriptorBean getDescriptorBeanFromReader(XMLStreamReader var1) throws IOException {
      try {
         this.rootBean = this.getDescriptorManager().createDescriptor(var1, this.errorHolder, this.validateSchema).getRootBean();
         if (this.versionInfo != null) {
            this.rootBean.getDescriptor().setOriginalVersionInfo(this.versionInfo);
         }

         return this.rootBean;
      } catch (IOException var3) {
         J2EELogger.logUnableToValidateDescriptor(this.moduleName, this.getAbsolutePath(), StackTraceUtils.throwable2StackTrace(var3));
         throw var3;
      }
   }

   public static void main(String[] var0) throws Exception {
      if (var0.length == 0) {
         usage();
         System.exit(-1);
      }

      String var1 = var0[0];
      String var2 = var0.length > 1 && var0[1].endsWith("plan.xml") ? var0[1] : null;
      File var3 = new File(var1);
      File var4 = new File(".");
      DeploymentPlanBean var5 = null;
      String var6 = var0.length > 2 ? var0[2] : null;
      if (var2 != null) {
         if (var6 == null) {
            usage();
            System.exit(-1);
         }

         AbstractDescriptorLoader2 var7 = new AbstractDescriptorLoader2(new File(var2), var2) {
         };
         var5 = (DeploymentPlanBean)var7.loadDescriptorBean();
      }

      TestAbstractDescriptorLoader2 var13 = new TestAbstractDescriptorLoader2(var3, var4, var5, var6, var1);
      int var9 = var5 == null ? 1 : 3;
      File[] var10 = null;
      if (var0.length > var9 && var0[var9].endsWith(".xml")) {
         System.out.print("\nmerged descriptor, Descriptor.toXML():");
         ArrayList var11 = new ArrayList();

         for(int var12 = var9; var12 < var0.length; ++var12) {
            var11.add(new File(var0[var12]));
         }

         var10 = (File[])((File[])var11.toArray(new File[0]));
      }

      DescriptorBean var8;
      if (var10 == null) {
         var8 = var13.loadDescriptorBean();
      } else {
         var8 = var13.mergeDescriptors(var10);
      }

      Descriptor var14 = var8.getDescriptor();
      System.out.print("\nDescriptor.toXML():");
      var14.toXML(System.out);
   }

   private static void usage() {
      System.out.print("java weblogic.application.descriptor.AbstractDescriptorLoader2  <dd-filename> <plan-filename> <module-name>");
   }

   public void setDescriptorManager(DescriptorManager var1) {
      this.dm = var1;
   }

   public static class TestAbstractDescriptorLoader2 extends AbstractDescriptorLoader2 {
      public TestAbstractDescriptorLoader2(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
         super(var1, var2, var3, var4, var5);
      }
   }

   private static class READONLY_SINGLETON {
      static DescriptorManager instance = new DescriptorManager();
   }
}
