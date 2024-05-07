package weblogic.management.provider.internal;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import javax.management.JMException;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.diagnostics.image.ImageSource;
import weblogic.diagnostics.image.ImageSourceCreationException;
import weblogic.management.DomainDir;
import weblogic.management.configuration.ConfigurationExtensionMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.ManagementServiceRestricted;
import weblogic.management.provider.RuntimeAccess;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class ConfigImageSource implements ImageSource {
   RuntimeAccess runtimeAccess;
   private boolean imageCreationTimeout = false;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public ConfigImageSource(RuntimeAccess var1) {
      this.runtimeAccess = var1;
   }

   public void createDiagnosticImage(OutputStream var1) throws ImageSourceCreationException {
      try {
         JarOutputStream var2 = new JarOutputStream(var1);
         PrintWriter var3 = new PrintWriter(var2);

         try {
            this.imageCreationTimeout = false;
            DomainMBean var14 = this.runtimeAccess.getDomain();
            Descriptor var13 = var14.getDescriptor();
            writeSystemProperties(var13, var2);
            writeDomainDirectory(var13, var2);
            writeEditLockState(var2);
            writeDescriptorTree(var13, var2);
            this.writeSystemPropertiesSchema(var2);
            this.writeDomainDirectorySchema(var2);
            this.writeEditLockStateSchema(var2);
            var2.flush();
            var2.close();
         } catch (Exception var10) {
            Exception var4 = var10;
            var3.println("Error dumping bean tree " + var10);
            var10.printStackTrace(var3);
            var3.flush();

            for(Throwable var5 = var10.getCause(); var5 != null; var5 = var4.getCause()) {
               var3.println("\nCaused by: \n");
               var5.printStackTrace(var3);
            }

            throw new ImageSourceCreationException(var4);
         } finally {
            var3.flush();
         }

      } catch (IOException var12) {
         throw new ImageSourceCreationException(var12);
      }
   }

   public void timeoutImageCreation() {
      this.imageCreationTimeout = true;
   }

   private static void writeDescriptorTree(Descriptor var0, JarOutputStream var1) throws IOException, JMException {
      var1.putNextEntry(new JarEntry("config/config.xml"));
      DescriptorManagerHelper.saveDescriptor(var0, var1);
      var1.flush();
      Iterator var2 = DescriptorInfoUtils.getDescriptorInfos(var0);

      while(var2 != null && var2.hasNext()) {
         DescriptorInfo var3 = (DescriptorInfo)var2.next();
         Descriptor var4 = var3.getDescriptor();
         ConfigurationExtensionMBean var5 = var3.getConfigurationExtension();
         var1.putNextEntry(new JarEntry("config/" + var5.getDescriptorFileName()));
         DescriptorManager var6 = var3.getDescriptorManager();
         DescriptorBean var7 = var3.getDescriptorBean();
         var6.writeDescriptorBeanAsXML(var7, var1);
         var1.flush();
      }

   }

   private static void writeSystemProperties(Descriptor var0, JarOutputStream var1) throws IOException, JMException {
      var1.putNextEntry(new JarEntry("system-properties.xml"));
      PrintWriter var2 = new PrintWriter(var1);
      Properties var3 = System.getProperties();
      Enumeration var4 = var3.propertyNames();
      var2.println("<!-- System Properties -->");
      var2.println("<system-properties xsi:schemaLocation=\"schema/system-properties.xsd\">");

      while(var4.hasMoreElements()) {
         String var5 = (String)var4.nextElement();
         String var6 = var3.getProperty(var5);

         for(int var7 = 0; var7 < ConfigImageSourceService.PROTECTED.length; ++var7) {
            if (var5.toLowerCase(Locale.US).indexOf(ConfigImageSourceService.PROTECTED[var7]) >= 0) {
               var6 = "********";
            }
         }

         var2.println("  <property>");
         var2.println("    <key>" + var5 + "</key>");
         var2.println("    <val>" + var6 + "</val>");
         var2.println("  </property>");
      }

      var2.println("</system-properties>");
      var2.println("");
      var2.flush();
      var1.flush();
   }

   private static void writeDomainDirectory(Descriptor var0, JarOutputStream var1) throws IOException, JMException {
      var1.putNextEntry(new JarEntry("domain-directory.xml"));
      PrintWriter var2 = new PrintWriter(var1);
      var2.println("<!-- Domain Directory Information -->");
      var2.println("<domain-directory xsi:schemaLocation=\"schema/domain-directory.xsd\">");
      var2.println("  <root-dir>" + DomainDir.getRootDir() + "</root-dir>");
      var2.println("  <bin-dir>" + DomainDir.getBinDir() + "</bin-dir>");
      var2.println("  <config-dir>" + DomainDir.getConfigDir() + "</config-dir>");
      var2.println("  <deployments-dir>" + DomainDir.getDeploymentsDir() + "</deployments-dir>");
      var2.println("  <diagnostic-dir>" + DomainDir.getDiagnosticsDir() + "</diagnostic-dir>");
      var2.println("  <jdbc-dir>" + DomainDir.getJDBCDir() + "</jdbc-dir>");
      var2.println("  <jms-dir>" + DomainDir.getJMSDir() + "</jms-dir>");
      var2.println("  <config-security-dir>" + DomainDir.getConfigSecurityDir() + "</config-security-dir>");
      var2.println("  <config-startup-dir>" + DomainDir.getConfigStartupDir() + "</config-startup-dir>");
      var2.println("  <lib-modules-dir>" + DomainDir.getLibModulesDir() + "</lib-modules-dir>");
      var2.println("  <pending-dir>" + DomainDir.getPendingDir() + "</pending-dir>");
      var2.println("  <security-dir>" + DomainDir.getSecurityDir() + "</security-dir>");
      var2.println("  <servers-dir>" + DomainDir.getServersDir() + "</servers-dir>");
      var2.println("  <local-server-dir>" + DomainDir.getDirForServer(ManagementService.getRuntimeAccess(kernelId).getServerName()) + "</local-server-dir>");
      var2.println("</domain-directory>");
      var2.flush();
      var1.flush();
   }

   private void writeDomainDirectorySchema(JarOutputStream var1) throws IOException {
      var1.putNextEntry(new JarEntry("schema/domain-directory.xsd"));
      PrintWriter var2 = new PrintWriter(var1);
      var2.println("<?xml version=\"1.0\"?>");
      var2.println("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"");
      var2.println(" targetNamespace=\"http://xmlns.oracle.com/weblogic//domain-directory\"");
      var2.println(" xmlns=\"http://http://xmlns.oracle.com/weblogic//domain-directory\"");
      var2.println(" elementFormDefault=\"qualified\">");
      var2.println("   <xs:complexType>");
      var2.println("     <xs:sequence>");
      var2.println("       <xs:element name=\"root-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"bin-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"config-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"deployments-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"diagnostic-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"jdbc-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"jms-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"config-security-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"config-startup-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"config-lib-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"lib-modules-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"pending-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"security-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"servers-dir\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"local-server-dir\" type=\"xs:string\"/>");
      var2.println("     </xs:sequence>");
      var2.println("   </xs:complexType>");
      var2.println("</xs:schema>");
      var2.flush();
      var1.flush();
   }

   private void writeSystemPropertiesSchema(JarOutputStream var1) throws IOException {
      var1.putNextEntry(new JarEntry("schema/system-properties.xsd"));
      PrintWriter var2 = new PrintWriter(var1);
      var2.println("<?xml version=\"1.0\"?>");
      var2.println("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"");
      var2.println(" targetNamespace=\"http://xmlns.oracle.com/weblogic//system-properties\"");
      var2.println(" xmlns=\"http://http://xmlns.oracle.com/weblogic//system-properties\"");
      var2.println(" elementFormDefault=\"qualified\">");
      var2.println(" <xs:element name=\"property\">");
      var2.println("     <xs:complexType>");
      var2.println("       <xs:sequence>");
      var2.println("         <xs:element name=\"key\" type=\"xs:string\"/>");
      var2.println("         <xs:element name=\"val\" type=\"xs:string\"/>");
      var2.println("        </xs:sequence>");
      var2.println("     </xs:complexType>");
      var2.println(" </xs:element>");
      var2.println("</xs:schema>");
      var2.flush();
      var1.flush();
   }

   private static void writeEditLockState(JarOutputStream var0) throws IOException, JMException {
      EditAccess var1 = ManagementServiceRestricted.getEditAccess(kernelId);
      if (var1 != null) {
         var0.putNextEntry(new JarEntry("edit-lock-state.xml"));
         PrintWriter var2 = new PrintWriter(var0);
         var2.println("<!-- Edit Lock Information -->");
         var2.println("<edit-lock xsi:schemaLocation=\"schema/edit-lock-state.xsd\">");
         var2.println("  <editor>" + var1.getEditor() + "</editor>");
         var2.println("  <start-time>" + var1.getEditorStartTime() + "</start-time>");
         var2.println("  <expiration-time>" + var1.getEditorExpirationTime() + "</expiration-time>");
         var2.println("  <exclusive>" + var1.isEditorExclusive() + "</exclusive>");
         var2.println("</edit-lock>");
         var2.flush();
         var0.flush();
      }
   }

   private void writeEditLockStateSchema(JarOutputStream var1) throws IOException {
      var1.putNextEntry(new JarEntry("schema/edit-lock-state.xsd"));
      PrintWriter var2 = new PrintWriter(var1);
      var2.println("<?xml version=\"1.0\"?>");
      var2.println("<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"");
      var2.println(" targetNamespace=\"http://xmlns.oracle.com/weblogic//edit-lock-state\"");
      var2.println(" xmlns=\"http://http://xmlns.oracle.com/weblogic//edit-lock-state\"");
      var2.println(" elementFormDefault=\"qualified\">");
      var2.println("   <xs:complexType>");
      var2.println("     <xs:sequence>");
      var2.println("       <xs:element name=\"editor\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"start-time\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"expiration-time\" type=\"xs:string\"/>");
      var2.println("       <xs:element name=\"exclusive\" type=\"xs:boolean\"/>");
      var2.println("     </xs:sequence>");
      var2.println("   </xs:complexType>");
      var2.println("</xs:schema>");
      var2.flush();
      var1.flush();
   }
}
