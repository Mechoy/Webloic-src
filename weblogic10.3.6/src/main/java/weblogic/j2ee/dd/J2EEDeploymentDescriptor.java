package weblogic.j2ee.dd;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import weblogic.management.ManagementException;
import weblogic.management.descriptors.DescriptorValidationException;
import weblogic.management.descriptors.TopLevelDescriptorMBean;
import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.descriptors.application.ConnectorModuleMBean;
import weblogic.management.descriptors.application.EJBModuleMBean;
import weblogic.management.descriptors.application.J2EEApplicationDescriptorMBean;
import weblogic.management.descriptors.application.JavaModuleMBean;
import weblogic.management.descriptors.application.ModuleMBean;
import weblogic.management.descriptors.application.SecurityRoleMBean;
import weblogic.management.descriptors.application.WebModuleMBean;
import weblogic.management.descriptors.webapp.UIMBean;
import weblogic.utils.Debug;
import weblogic.utils.application.WarDetector;
import weblogic.utils.io.XMLWriter;
import weblogic.utils.jars.VirtualJarFactory;
import weblogic.utils.jars.VirtualJarFile;

public final class J2EEDeploymentDescriptor extends XMLElementMBeanDelegate implements J2EEApplicationDescriptorMBean, TopLevelDescriptorMBean, UIMBean {
   private static final long serialVersionUID = 5882884876092791011L;
   private static boolean debug = false;
   private String version;
   private String description;
   private String displayName;
   private String largeIconFileName;
   private String smallIconFileName;
   private ArrayList orderedModules;
   private HashMap roleDescriptors;
   private String j2eeAppDescrEncoding;
   private String descriptorVersion;

   public void setEncoding(String var1) {
      String var2 = this.j2eeAppDescrEncoding;
      this.j2eeAppDescrEncoding = var1;
      this.checkChange("encoding", var2, this.j2eeAppDescrEncoding);
   }

   public String getEncoding() {
      return this.j2eeAppDescrEncoding;
   }

   public String getVersion() {
      return this.descriptorVersion;
   }

   public void setVersion(String var1) {
      this.descriptorVersion = var1;
   }

   public J2EEDeploymentDescriptor() {
      this.orderedModules = new ArrayList();
      this.roleDescriptors = new HashMap();
      this.j2eeAppDescrEncoding = null;
      this.descriptorVersion = null;
   }

   public J2EEDeploymentDescriptor(JarFile var1) throws IOException {
      this(VirtualJarFactory.createVirtualJar(var1));
   }

   public J2EEDeploymentDescriptor(VirtualJarFile var1) {
      this.orderedModules = new ArrayList();
      this.roleDescriptors = new HashMap();
      this.j2eeAppDescrEncoding = null;
      this.descriptorVersion = null;
      Iterator var2 = var1.entries();

      while(var2.hasNext()) {
         JarEntry var3 = (JarEntry)var2.next();
         String var4 = var3.getName();
         if (var4.indexOf(47) == var4.lastIndexOf(47)) {
            if (var4.startsWith("/")) {
               var4 = var4.substring(1, var4.length());
            }

            Object var5 = null;
            if (WarDetector.instance.suffixed(var4)) {
               if (debug) {
                  Debug.say("found web app...");
               }

               var5 = new WebModuleDescriptor(var4, (String)null);
            } else if (var4.endsWith(".jar")) {
               var5 = new EJBModuleDescriptor(var4);
            } else {
               String var6 = var4;
               if (!var4.endsWith("/")) {
                  var6 = var4 + "/";
               }

               String var7 = var6 + "META-INF/ejb-jar.xml";
               String var8 = var6 + "WEB-INF/web.xml";
               if (var1.getEntry(var8) != null) {
                  var5 = new WebModuleDescriptor(var4, var4);
               } else if (var1.getEntry(var7) != null) {
                  var5 = new EJBModuleDescriptor(var4);
               }
            }

            if (var5 != null) {
               this.addModuleDescriptor((ModuleDescriptor)var5);
            }
         }
      }

      this.displayName = var1.getName();
      this.description = "Exploded J2EE Application, " + this.displayName;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String var1) {
      if (debug) {
         Debug.say("setDescription(" + var1 + ")");
      }

      String var2 = this.description;
      this.description = var1;
      this.checkChange("description", var2, var1);
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String var1) {
      if (debug) {
         Debug.say("setDisplayName(" + var1 + ")");
      }

      String var2 = this.displayName;
      this.displayName = var1;
      this.checkChange("displayName", var2, var1);
   }

   public String getLargeIconFileName() {
      return this.largeIconFileName;
   }

   public void setLargeIconFileName(String var1) {
      if (debug) {
         Debug.say("setLargeIconFileName(" + var1 + ")");
      }

      String var2 = this.largeIconFileName;
      this.largeIconFileName = var1;
      this.checkChange("largeIconFileName", var2, this.largeIconFileName);
   }

   public String getSmallIconFileName() {
      return this.smallIconFileName;
   }

   public void setSmallIconFileName(String var1) {
      if (debug) {
         Debug.say("setSmallIconFileName(" + var1 + ")");
      }

      String var2 = this.smallIconFileName;
      this.smallIconFileName = var1;
      this.checkChange("smallIconFileName", var2, this.smallIconFileName);
   }

   public void setJ2EEVersion(String var1) {
      String var2 = this.version;
      this.version = var1;
      this.checkChange("j2eeVersion", var2, var1);
   }

   public void addModuleDescriptor(ModuleDescriptor var1) {
      Debug.assertion(var1.getName() != null && var1.getName().length() > 0, var1 != null ? var1.getName() : "<null>");
      this.addModuleDescriptor(var1, var1.getName());
   }

   private void addModuleDescriptor(ModuleDescriptor var1, String var2) {
      this.orderedModules.add(var1);
   }

   public void addModule(ModuleMBean var1) {
      Debug.assertion(var1.getName() != null && var1.getName().length() > 0);
      this.addModuleDescriptor((ModuleDescriptor)var1, var1.getModuleKey());
      Debug.assertion(Arrays.asList((Object[])this.getModules()).contains(var1));
   }

   public void addEJBModule(EJBModuleMBean var1) {
      Debug.assertion(var1.getName() != null && var1.getName().length() > 0);
      this.addModule(var1);
      Debug.assertion(Arrays.asList((Object[])this.getEJBModules()).contains(var1));
   }

   public void addJavaModule(JavaModuleMBean var1) {
      Debug.assertion(var1.getName() != null && var1.getName().length() > 0);
      this.addModule(var1);
      Debug.assertion(Arrays.asList((Object[])this.getJavaModules()).contains(var1));
   }

   public void addWebModule(WebModuleMBean var1) {
      Debug.assertion(var1.getName() != null && var1.getName().length() > 0);
      this.addModule(var1);
      Debug.assertion(Arrays.asList((Object[])this.getWebModules()).contains(var1));
   }

   public void addConnectorModule(ConnectorModuleMBean var1) {
      Debug.assertion(var1.getName() != null && var1.getName().length() > 0);
      this.addModule(var1);
      Debug.assertion(Arrays.asList((Object[])this.getConnectorModules()).contains(var1));
   }

   private void removeModule(ModuleMBean var1) {
      int var2 = this.orderedModules.indexOf(var1);
      if (var2 > -1) {
         this.orderedModules.remove(var2);
      }

   }

   public ModuleDescriptor removeBean(String var1) {
      ModuleDescriptor var2 = this.getModule(var1);
      int var3 = this.orderedModules.indexOf(var2);
      Object var4 = this.orderedModules.remove(var3);
      if (debug) {
         if (var4 != null) {
            Debug.say("FOUND!");
         } else {
            Debug.say("NOT FOUND!");
         }
      }

      return (ModuleDescriptor)var4;
   }

   public void removeEJBModule(EJBModuleMBean var1) {
      this.removeModule(var1);
      Debug.assertion(!Arrays.asList((Object[])this.getEJBModules()).contains(var1));
   }

   public void removeJavaModule(JavaModuleMBean var1) {
      this.removeModule(var1);
      Debug.assertion(!Arrays.asList((Object[])this.getJavaModules()).contains(var1));
   }

   public void removeWebModule(WebModuleMBean var1) {
      this.removeModule(var1);
      Debug.assertion(!Arrays.asList((Object[])this.getWebModules()).contains(var1));
   }

   public void removeConnectorModule(ConnectorModuleMBean var1) {
      this.removeModule(var1);
      Debug.assertion(!Arrays.asList((Object[])this.getConnectorModules()).contains(var1));
   }

   public ModuleDescriptor getModule(String var1) {
      ModuleDescriptor var2 = null;
      Iterator var3 = this.orderedModules.iterator();

      while(var3.hasNext()) {
         ModuleDescriptor var4 = (ModuleDescriptor)var3.next();
         if (var4.getURI().equals(var1)) {
            var2 = var4;
            break;
         }
      }

      return var2;
   }

   public void setModules(ModuleMBean[] var1) {
      ModuleMBean[] var2 = this.getModules();
      this.orderedModules.clear();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.addModuleDescriptor((ModuleDescriptor)var1[var3]);
      }

      this.checkChange("modules", var2, var1);
   }

   public ModuleMBean[] getWebModules() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getAllModules().iterator();

      while(true) {
         Object var3;
         do {
            if (!var2.hasNext()) {
               ModuleMBean[] var4 = new ModuleMBean[0];
               return (ModuleMBean[])((ModuleMBean[])var1.toArray(var4));
            }

            var3 = var2.next();
         } while(!(var3 instanceof WebModuleMBean));

         var1.add(var3);
         Debug.assertion(((ModuleMBean)var3).getName() != null && ((ModuleMBean)var3).getName().length() > 0);
      }
   }

   public ModuleMBean[] getEJBModules() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getAllModules().iterator();

      while(true) {
         Object var3;
         do {
            if (!var2.hasNext()) {
               ModuleMBean[] var4 = new ModuleMBean[0];
               return (ModuleMBean[])((ModuleMBean[])var1.toArray(var4));
            }

            var3 = var2.next();
         } while(!(var3 instanceof EJBModuleMBean));

         var1.add(var3);
         Debug.assertion(((ModuleMBean)var3).getName() != null && ((ModuleMBean)var3).getName().length() > 0);
      }
   }

   public ModuleMBean[] getJavaModules() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getAllModules().iterator();

      while(true) {
         Object var3;
         do {
            if (!var2.hasNext()) {
               ModuleMBean[] var4 = new ModuleMBean[0];
               return (ModuleMBean[])((ModuleMBean[])var1.toArray(var4));
            }

            var3 = var2.next();
         } while(!(var3 instanceof JavaModuleMBean));

         var1.add(var3);
         Debug.assertion(((ModuleMBean)var3).getName() != null && ((ModuleMBean)var3).getName().length() > 0);
      }
   }

   public ModuleMBean[] getConnectorModules() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getAllModules().iterator();

      while(true) {
         Object var3;
         do {
            if (!var2.hasNext()) {
               ModuleMBean[] var4 = new ModuleMBean[0];
               return (ModuleMBean[])((ModuleMBean[])var1.toArray(var4));
            }

            var3 = var2.next();
         } while(!(var3 instanceof ConnectorModuleMBean));

         var1.add(var3);
         Debug.assertion(((ModuleMBean)var3).getName() != null && ((ModuleMBean)var3).getName().length() > 0);
      }
   }

   public ModuleMBean[] getModules() {
      ModuleMBean[] var1 = new ModuleMBean[0];
      return (ModuleMBean[])((ModuleMBean[])this.orderedModules.toArray(var1));
   }

   public Collection getAllModules() {
      return this.orderedModules;
   }

   private ModuleMBean[] getModules(Class var1) {
      ModuleMBean[] var2 = new ModuleMBean[0];
      ArrayList var3 = new ArrayList();
      Iterator var4 = this.orderedModules.iterator();

      while(var4.hasNext()) {
         ModuleMBean var5 = (ModuleMBean)var4.next();
         if (debug) {
            Debug.say("module mbean is " + var5 + ", with getClass=" + var5.getClass());
         }

         Debug.assertion(var5.getName() != null);
         if (var5.getClass() == var1) {
            var3.add(var5);
         }
      }

      return (ModuleMBean[])((ModuleMBean[])var3.toArray(var2));
   }

   public SecurityRoleMBean[] getSecurityRoles() {
      SecurityRoleMBean[] var1 = new SecurityRoleMBean[0];
      return (SecurityRoleMBean[])((SecurityRoleMBean[])this.roleDescriptors.values().toArray(var1));
   }

   public void setSecurityRoles(SecurityRoleMBean[] var1) {
      SecurityRoleMBean[] var2 = this.getSecurityRoles();
      this.roleDescriptors = new HashMap();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.roleDescriptors.put(var1[var3].getName(), var1[var3]);
      }

      this.checkChange("securityRoles", var2, var1);
   }

   public RoleDescriptor getRoleDescriptor(String var1) {
      return (RoleDescriptor)this.roleDescriptors.get(var1);
   }

   public Collection getAllRoleDescriptors() {
      return this.roleDescriptors.values();
   }

   public void addRoleDescriptor(RoleDescriptor var1) {
      this.addRoleDescriptor(var1, var1.getName());
   }

   private void addRoleDescriptor(RoleDescriptor var1, String var2) {
      this.roleDescriptors.put(var2, var1);
   }

   public void addSecurityRole(SecurityRoleMBean var1) {
      this.addRoleDescriptor((RoleDescriptor)var1, var1.getRoleName());
      if (debug) {
         Debug.say("add security role ... roles descriptors ...\n" + this.roleDescriptors.values());
      }

   }

   public void removeSecurityRole(SecurityRoleMBean var1) {
      if (this.removeRoleDescriptor(((RoleDescriptor)var1).getRoleName()) == null && debug) {
         Debug.say("REALLY NOT FOUND!");
      }

   }

   public RoleDescriptor removeRoleDescriptor(String var1) {
      RoleDescriptor var2 = (RoleDescriptor)this.roleDescriptors.remove(var1);
      return var2;
   }

   public String toXML(int var1) {
      StringWriter var2 = new StringWriter();
      XMLWriter var3 = new XMLWriter(var2);
      var3.setIndent(var1);
      this.toXML(var3);
      return var2.toString();
   }

   public void toXML(XMLWriter var1) {
      String var2 = this.getEncoding();
      if (var2 != null) {
         var1.println("<?xml version=\"1.0\" encoding=\"" + var2 + "\"?>");
      }

      boolean var3 = true;
      var1.println("<!DOCTYPE application PUBLIC '-//Sun Microsystems, Inc.//DTD J2EE Application 1.3//EN' 'http://java.sun.com/dtd/application_1_3.dtd'>");
      var1.println("<application>");
      var1.incrIndent();
      String var4 = this.getSmallIconFileName();
      String var5 = this.getLargeIconFileName();
      if (var4 != null || var5 != null) {
         var1.println("<icon>");
         var1.incrIndent();
         if (var4 != null) {
            var1.println("<small-icon>" + var4 + "</small-icon>");
         }

         if (var5 != null) {
            var1.println("<large-icon>" + var5 + "</large-icon>");
         }

         var1.println("</icon>");
         var1.decrIndent();
      }

      String var6 = this.getDisplayName();
      if (var6 == null) {
         var6 = "";
      }

      var1.println("<display-name>" + var6 + "</display-name>");
      if (this.getDescription() != null) {
         var1.println("<description>" + this.getDescription() + "</description>");
      }

      if (debug) {
         Debug.say("toXML ... modules ...\n" + this.getAllModules());
      }

      ModuleMBean[] var7 = this.getModules();

      for(int var8 = 0; var8 < var7.length; ++var8) {
         ((ModuleDescriptor)var7[var8]).toXML(var1);
      }

      if (this.roleDescriptors != null) {
         if (debug) {
            Debug.say("toXML ... roles descriptors ...\n" + this.roleDescriptors);
         }

         Iterator var11 = this.roleDescriptors.values().iterator();

         while(var11.hasNext()) {
            SecurityRoleMBean var9 = (SecurityRoleMBean)var11.next();
            int var10 = var1.getIndent();
            var1.printNoIndent(var9.toXML(var10));
         }
      }

      var1.decrIndent();
      var1.println("</application>");
   }

   public String toString() {
      String var1 = "J2EE Deployment Descriptor for " + this.getDisplayName();
      var1 = var1 + "\n Description: " + this.getDescription();
      var1 = var1 + "\n Modules: " + this.orderedModules.size();
      var1 = var1 + "\n Security Roles: " + this.roleDescriptors.size();
      return var1;
   }

   public void validate() throws DescriptorValidationException {
      throw new RuntimeException("NYI");
   }

   public void usePersistenceDestination(String var1) {
      throw new RuntimeException("NYI");
   }

   public void persist() throws IOException {
      throw new RuntimeException("NYI");
   }

   public void persist(Properties var1) throws IOException {
   }

   public void unregister() throws ManagementException {
      super.unregister();
      Iterator var1 = this.getAllRoleDescriptors().iterator();

      while(var1.hasNext()) {
         SecurityRoleMBean var2 = (SecurityRoleMBean)var1.next();
         var2.unregister();
      }

      var1 = this.getAllModules().iterator();

      while(var1.hasNext()) {
         ModuleMBean var3 = (ModuleMBean)var1.next();
         var3.unregister();
      }

   }

   public String toXML() {
      StringWriter var1 = new StringWriter();
      this.toXML(new XMLWriter(var1));
      return var1.toString();
   }

   public void addDescriptorError(String var1) {
   }

   public void removeDescriptorError(String var1) {
   }

   public boolean isValid() {
      return true;
   }

   public String[] getDescriptorErrors() {
      return null;
   }

   public void setDescriptorErrors(String[] var1) {
   }
}
