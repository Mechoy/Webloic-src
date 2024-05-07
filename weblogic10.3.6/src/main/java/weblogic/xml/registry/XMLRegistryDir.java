package weblogic.xml.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.servlet.ConnectionSigner;
import weblogic.management.servlet.FileDistributionServlet;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;

public final class XMLRegistryDir {
   private static final boolean verbose = true;
   private boolean localDir = false;
   private String registryName;
   private URL adminFileServletURL;
   private static AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public XMLRegistryDir(String var1) {
      if (!ManagementService.getRuntimeAccess(KERNEL_ID).isAdminServer() || !ManagementService.getRuntimeAccess(KERNEL_ID).isAdminServerAvailable()) {
         try {
            this.adminFileServletURL = FileDistributionServlet.getURL();
         } catch (MalformedURLException var3) {
            throw new AssertionError(var3);
         }
      }

      this.registryName = var1;
   }

   public InputStream getEntity(String var1) throws XMLRegistryException {
      return this.isLocal() ? this.getLocalEntity(var1) : this.getRemoteEntity(var1);
   }

   boolean isLocal() {
      return this.adminFileServletURL == null;
   }

   private InputStream getLocalEntity(String var1) throws XMLRegistryException {
      DomainMBean var2 = ManagementService.getRuntimeAccess(KERNEL_ID).getDomain();
      String var3 = var2.getRootDirectory();
      File var4 = new File(var3, "xml/registries/" + this.registryName);
      if (!var4.exists()) {
         throw new XMLRegistryException("XML Registry directory " + var4.getAbsolutePath() + " not found");
      } else {
         File var5 = new File(var1);
         if (!var5.isAbsolute()) {
            var5 = new File(var4, var1);
         }

         if (!var5.exists()) {
            throw new XMLRegistryException("Entity " + var1 + " not found in registry entity directory " + var4);
         } else {
            try {
               return new FileInputStream(var5);
            } catch (FileNotFoundException var7) {
               throw new AssertionError();
            }
         }
      }
   }

   private InputStream getRemoteEntity(String var1) throws XMLRegistryException {
      try {
         URLConnection var2 = this.adminFileServletURL.openConnection();
         HttpURLConnection var3 = (HttpURLConnection)var2;
         ConnectionSigner.signConnection(var2, KERNEL_ID);
         var3.setRequestProperty("wl_request_type", "wl_xml_entity_request");
         var3.setRequestProperty("xml-registry-name", this.registryName);
         var3.setRequestProperty("xml-entity-path", var1);
         var3.setRequestProperty("Connection", "Close");
         return var3.getInputStream();
      } catch (IOException var4) {
         throw new XMLRegistryRemoteAccessException("Unable to open url: " + this.adminFileServletURL.toString(), var4);
      }
   }
}
