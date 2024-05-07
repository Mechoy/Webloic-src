package weblogic.management.descriptors.application.weblogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.application.ModuleMBean;
import weblogic.management.tools.ToXML;

public class WeblogicApplicationMBeanImpl extends XMLElementMBeanDelegate implements WeblogicApplicationMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_moduleProviders = false;
   private List moduleProviders;
   private boolean isSet_parameters = false;
   private List parameters;
   private boolean isSet_encoding = false;
   private String encoding;
   private boolean isSet_modules = false;
   private List modules;
   private boolean isSet_jdbcConnectionPools = false;
   private List jdbcConnectionPools;
   private boolean isSet_ejb = false;
   private EjbMBean ejb;
   private boolean isSet_security = false;
   private SecurityMBean security;
   private boolean isSet_libraries = false;
   private List libraries;
   private boolean isSet_listeners = false;
   private List listeners;
   private boolean isSet_startups = false;
   private List startups;
   private boolean isSet_xml = false;
   private XMLMBean xml;
   private boolean isSet_shutdowns = false;
   private List shutdowns;
   private boolean isSet_version = false;
   private String version;
   private boolean isSet_classloaderStructure = false;
   private ClassloaderStructureMBean classloaderStructure;

   public ModuleProviderMBean[] getModuleProviders() {
      if (this.moduleProviders == null) {
         return new ModuleProviderMBean[0];
      } else {
         ModuleProviderMBean[] var1 = new ModuleProviderMBean[this.moduleProviders.size()];
         var1 = (ModuleProviderMBean[])((ModuleProviderMBean[])this.moduleProviders.toArray(var1));
         return var1;
      }
   }

   public void setModuleProviders(ModuleProviderMBean[] var1) {
      ModuleProviderMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getModuleProviders();
      }

      this.isSet_moduleProviders = true;
      if (this.moduleProviders == null) {
         this.moduleProviders = Collections.synchronizedList(new ArrayList());
      } else {
         this.moduleProviders.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.moduleProviders.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("ModuleProviders", var2, this.getModuleProviders());
      }

   }

   public void addModuleProvider(ModuleProviderMBean var1) {
      this.isSet_moduleProviders = true;
      if (this.moduleProviders == null) {
         this.moduleProviders = Collections.synchronizedList(new ArrayList());
      }

      this.moduleProviders.add(var1);
   }

   public void removeModuleProvider(ModuleProviderMBean var1) {
      if (this.moduleProviders != null) {
         this.moduleProviders.remove(var1);
      }
   }

   public ApplicationParamMBean[] getParameters() {
      if (this.parameters == null) {
         return new ApplicationParamMBean[0];
      } else {
         ApplicationParamMBean[] var1 = new ApplicationParamMBean[this.parameters.size()];
         var1 = (ApplicationParamMBean[])((ApplicationParamMBean[])this.parameters.toArray(var1));
         return var1;
      }
   }

   public void setParameters(ApplicationParamMBean[] var1) {
      ApplicationParamMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getParameters();
      }

      this.isSet_parameters = true;
      if (this.parameters == null) {
         this.parameters = Collections.synchronizedList(new ArrayList());
      } else {
         this.parameters.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.parameters.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Parameters", var2, this.getParameters());
      }

   }

   public void addParameter(ApplicationParamMBean var1) {
      this.isSet_parameters = true;
      if (this.parameters == null) {
         this.parameters = Collections.synchronizedList(new ArrayList());
      }

      this.parameters.add(var1);
   }

   public void removeParameter(ApplicationParamMBean var1) {
      if (this.parameters != null) {
         this.parameters.remove(var1);
      }
   }

   public String getEncoding() {
      return this.encoding;
   }

   public void setEncoding(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.encoding;
      this.encoding = var1;
      this.isSet_encoding = var1 != null;
      this.checkChange("encoding", var2, this.encoding);
   }

   public ModuleMBean[] getModules() {
      if (this.modules == null) {
         return new ModuleMBean[0];
      } else {
         ModuleMBean[] var1 = new ModuleMBean[this.modules.size()];
         var1 = (ModuleMBean[])((ModuleMBean[])this.modules.toArray(var1));
         return var1;
      }
   }

   public void setModules(ModuleMBean[] var1) {
      ModuleMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getModules();
      }

      this.isSet_modules = true;
      if (this.modules == null) {
         this.modules = Collections.synchronizedList(new ArrayList());
      } else {
         this.modules.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.modules.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Modules", var2, this.getModules());
      }

   }

   public void addModule(ModuleMBean var1) {
      this.isSet_modules = true;
      if (this.modules == null) {
         this.modules = Collections.synchronizedList(new ArrayList());
      }

      this.modules.add(var1);
   }

   public void removeModule(ModuleMBean var1) {
      if (this.modules != null) {
         this.modules.remove(var1);
      }
   }

   public JdbcConnectionPoolMBean[] getJdbcConnectionPools() {
      if (this.jdbcConnectionPools == null) {
         return new JdbcConnectionPoolMBean[0];
      } else {
         JdbcConnectionPoolMBean[] var1 = new JdbcConnectionPoolMBean[this.jdbcConnectionPools.size()];
         var1 = (JdbcConnectionPoolMBean[])((JdbcConnectionPoolMBean[])this.jdbcConnectionPools.toArray(var1));
         return var1;
      }
   }

   public void setJdbcConnectionPools(JdbcConnectionPoolMBean[] var1) {
      JdbcConnectionPoolMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getJdbcConnectionPools();
      }

      this.isSet_jdbcConnectionPools = true;
      if (this.jdbcConnectionPools == null) {
         this.jdbcConnectionPools = Collections.synchronizedList(new ArrayList());
      } else {
         this.jdbcConnectionPools.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.jdbcConnectionPools.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("JdbcConnectionPools", var2, this.getJdbcConnectionPools());
      }

   }

   public void addJdbcConnectionPool(JdbcConnectionPoolMBean var1) {
      this.isSet_jdbcConnectionPools = true;
      if (this.jdbcConnectionPools == null) {
         this.jdbcConnectionPools = Collections.synchronizedList(new ArrayList());
      }

      this.jdbcConnectionPools.add(var1);
   }

   public void removeJdbcConnectionPool(JdbcConnectionPoolMBean var1) {
      if (this.jdbcConnectionPools != null) {
         this.jdbcConnectionPools.remove(var1);
      }
   }

   public EjbMBean getEjb() {
      return this.ejb;
   }

   public void setEjb(EjbMBean var1) {
      EjbMBean var2 = this.ejb;
      this.ejb = var1;
      this.isSet_ejb = var1 != null;
      this.checkChange("ejb", var2, this.ejb);
   }

   public SecurityMBean getSecurity() {
      return this.security;
   }

   public void setSecurity(SecurityMBean var1) {
      SecurityMBean var2 = this.security;
      this.security = var1;
      this.isSet_security = var1 != null;
      this.checkChange("security", var2, this.security);
   }

   public LibraryRefMBean[] getLibraries() {
      if (this.libraries == null) {
         return new LibraryRefMBean[0];
      } else {
         LibraryRefMBean[] var1 = new LibraryRefMBean[this.libraries.size()];
         var1 = (LibraryRefMBean[])((LibraryRefMBean[])this.libraries.toArray(var1));
         return var1;
      }
   }

   public void setLibraries(LibraryRefMBean[] var1) {
      LibraryRefMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getLibraries();
      }

      this.isSet_libraries = true;
      if (this.libraries == null) {
         this.libraries = Collections.synchronizedList(new ArrayList());
      } else {
         this.libraries.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.libraries.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Libraries", var2, this.getLibraries());
      }

   }

   public void addLibrary(LibraryRefMBean var1) {
      this.isSet_libraries = true;
      if (this.libraries == null) {
         this.libraries = Collections.synchronizedList(new ArrayList());
      }

      this.libraries.add(var1);
   }

   public void removeLibrary(LibraryRefMBean var1) {
      if (this.libraries != null) {
         this.libraries.remove(var1);
      }
   }

   public ListenerMBean[] getListeners() {
      if (this.listeners == null) {
         return new ListenerMBean[0];
      } else {
         ListenerMBean[] var1 = new ListenerMBean[this.listeners.size()];
         var1 = (ListenerMBean[])((ListenerMBean[])this.listeners.toArray(var1));
         return var1;
      }
   }

   public void setListeners(ListenerMBean[] var1) {
      ListenerMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getListeners();
      }

      this.isSet_listeners = true;
      if (this.listeners == null) {
         this.listeners = Collections.synchronizedList(new ArrayList());
      } else {
         this.listeners.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.listeners.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Listeners", var2, this.getListeners());
      }

   }

   public void addListener(ListenerMBean var1) {
      this.isSet_listeners = true;
      if (this.listeners == null) {
         this.listeners = Collections.synchronizedList(new ArrayList());
      }

      this.listeners.add(var1);
   }

   public void removeListener(ListenerMBean var1) {
      if (this.listeners != null) {
         this.listeners.remove(var1);
      }
   }

   public StartupMBean[] getStartups() {
      if (this.startups == null) {
         return new StartupMBean[0];
      } else {
         StartupMBean[] var1 = new StartupMBean[this.startups.size()];
         var1 = (StartupMBean[])((StartupMBean[])this.startups.toArray(var1));
         return var1;
      }
   }

   public void setStartups(StartupMBean[] var1) {
      StartupMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getStartups();
      }

      this.isSet_startups = true;
      if (this.startups == null) {
         this.startups = Collections.synchronizedList(new ArrayList());
      } else {
         this.startups.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.startups.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Startups", var2, this.getStartups());
      }

   }

   public void addStartup(StartupMBean var1) {
      this.isSet_startups = true;
      if (this.startups == null) {
         this.startups = Collections.synchronizedList(new ArrayList());
      }

      this.startups.add(var1);
   }

   public void removeStartup(StartupMBean var1) {
      if (this.startups != null) {
         this.startups.remove(var1);
      }
   }

   public XMLMBean getXML() {
      return this.xml;
   }

   public void setXML(XMLMBean var1) {
      XMLMBean var2 = this.xml;
      this.xml = var1;
      this.isSet_xml = var1 != null;
      this.checkChange("xml", var2, this.xml);
   }

   public ShutdownMBean[] getShutdowns() {
      if (this.shutdowns == null) {
         return new ShutdownMBean[0];
      } else {
         ShutdownMBean[] var1 = new ShutdownMBean[this.shutdowns.size()];
         var1 = (ShutdownMBean[])((ShutdownMBean[])this.shutdowns.toArray(var1));
         return var1;
      }
   }

   public void setShutdowns(ShutdownMBean[] var1) {
      ShutdownMBean[] var2 = null;
      if (this.changeSupport != null) {
         var2 = this.getShutdowns();
      }

      this.isSet_shutdowns = true;
      if (this.shutdowns == null) {
         this.shutdowns = Collections.synchronizedList(new ArrayList());
      } else {
         this.shutdowns.clear();
      }

      if (null != var1) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            this.shutdowns.add(var1[var3]);
         }
      }

      if (this.changeSupport != null) {
         this.checkChange("Shutdowns", var2, this.getShutdowns());
      }

   }

   public void addShutdown(ShutdownMBean var1) {
      this.isSet_shutdowns = true;
      if (this.shutdowns == null) {
         this.shutdowns = Collections.synchronizedList(new ArrayList());
      }

      this.shutdowns.add(var1);
   }

   public void removeShutdown(ShutdownMBean var1) {
      if (this.shutdowns != null) {
         this.shutdowns.remove(var1);
      }
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.version;
      this.version = var1;
      this.isSet_version = var1 != null;
      this.checkChange("version", var2, this.version);
   }

   public ClassloaderStructureMBean getClassloaderStructure() {
      return this.classloaderStructure;
   }

   public void setClassloaderStructure(ClassloaderStructureMBean var1) {
      ClassloaderStructureMBean var2 = this.classloaderStructure;
      this.classloaderStructure = var1;
      this.isSet_classloaderStructure = var1 != null;
      this.checkChange("classloaderStructure", var2, this.classloaderStructure);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-application");
      var2.append(">\n");
      if (null != this.getEjb()) {
         var2.append(this.getEjb().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getXML()) {
         var2.append(this.getXML().toXML(var1 + 2)).append("\n");
      }

      int var3;
      if (null != this.getJdbcConnectionPools()) {
         for(var3 = 0; var3 < this.getJdbcConnectionPools().length; ++var3) {
            var2.append(this.getJdbcConnectionPools()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getSecurity()) {
         var2.append(this.getSecurity().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getParameters()) {
         for(var3 = 0; var3 < this.getParameters().length; ++var3) {
            var2.append(this.getParameters()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getClassloaderStructure()) {
         var2.append(this.getClassloaderStructure().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getListeners()) {
         for(var3 = 0; var3 < this.getListeners().length; ++var3) {
            var2.append(this.getListeners()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getStartups()) {
         for(var3 = 0; var3 < this.getStartups().length; ++var3) {
            var2.append(this.getStartups()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getShutdowns()) {
         for(var3 = 0; var3 < this.getShutdowns().length; ++var3) {
            var2.append(this.getShutdowns()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getModuleProviders()) {
         for(var3 = 0; var3 < this.getModuleProviders().length; ++var3) {
            var2.append(this.getModuleProviders()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getModules()) {
         for(var3 = 0; var3 < this.getModules().length; ++var3) {
            var2.append(this.getModules()[var3].toXML(var1 + 2));
         }
      }

      if (null != this.getLibraries()) {
         for(var3 = 0; var3 < this.getLibraries().length; ++var3) {
            var2.append(this.getLibraries()[var3].toXML(var1 + 2));
         }
      }

      var2.append(ToXML.indent(var1)).append("</weblogic-application>\n");
      return var2.toString();
   }
}
