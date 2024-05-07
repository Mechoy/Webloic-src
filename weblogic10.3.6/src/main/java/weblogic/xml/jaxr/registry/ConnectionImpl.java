package weblogic.xml.jaxr.registry;

import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.xml.registry.Connection;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
import weblogic.xml.jaxr.registry.provider.EnvironmentHelper;
import weblogic.xml.jaxr.registry.provider.ProviderInfo;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;

public class ConnectionImpl extends BaseJAXRObject implements Connection {
   private Properties m_properties;
   private ProviderInfo m_providerInfo;
   private RegistryServiceImpl m_registryService;
   private Set m_credentials;
   private boolean m_isSynchronous = true;
   private URL m_queryURL;
   private URL m_publishURL;
   private boolean m_isClosed = false;
   private Boolean m_isContainer;

   public ConnectionImpl(Properties var1, ProviderInfo var2) throws JAXRException {
      if (var1 == null) {
         this.m_properties = new Properties();
      } else {
         this.m_properties = var1;
      }

      this.m_providerInfo = var2;
      String var3 = this.m_properties.getProperty("javax.xml.registry.queryManagerURL");
      String var4 = this.m_properties.getProperty("javax.xml.registry.lifeCycleManagerURL");

      String var6;
      try {
         this.m_queryURL = new URL(var3);
      } catch (MalformedURLException var8) {
         var6 = JAXRMessages.getMessage("jaxr.provider.invalidQueryURL", new Object[]{var3});
         throw new InvalidRequestException(var6);
      }

      if (var4 != null && var4.length() != 0) {
         try {
            this.m_publishURL = new URL(var4);
         } catch (MalformedURLException var7) {
            var6 = JAXRMessages.getMessage("jaxr.provider.invalidPublishURL", new Object[]{var4});
            throw new InvalidRequestException(var6);
         }
      } else {
         this.m_publishURL = this.m_queryURL;
      }

      String var5 = this.m_properties.getProperty("javax.xml.registry.security.authenticationMethod");
      if (!"CLIENT_CERTIFICATE".equalsIgnoreCase(var5) && !"HTTP_BASIC".equalsIgnoreCase(var5) && !"MS_PASSPORT".equalsIgnoreCase(var5)) {
         if (var5 != null && !"UDDI_GET_AUTHTOKEN".equalsIgnoreCase(var5)) {
            var6 = JAXRMessages.getMessage("jaxr.provider.invalidAuthenticationMethod", new String[]{var5});
            throw new InvalidRequestException(var6);
         }
      } else {
         this.checkCapability(this.m_registryService, 1);
      }

      this.m_registryService = new RegistryServiceImpl(this);
      this.m_isContainer = EnvironmentHelper.getInstance().isJ2EEContainer(this.m_registryService);
   }

   public RegistryService getRegistryService() throws JAXRException {
      if (this.isClosed()) {
         throw new InvalidRequestException(JAXRMessages.getMessage("jaxr.registry.connection.closed"));
      } else {
         return this.m_registryService;
      }
   }

   public void close() throws JAXRException {
      if (!this.isClosed()) {
         this.m_registryService.closeConnection();
         this.m_registryService = null;
         this.m_isClosed = true;
      }

   }

   public boolean isClosed() throws JAXRException {
      return this.m_isClosed;
   }

   public boolean isSynchronous() throws JAXRException {
      if (this.isClosed()) {
         throw new InvalidRequestException(JAXRMessages.getMessage("jaxr.registry.connection.closed"));
      } else if (this.m_isSynchronous) {
         return true;
      } else if (this.m_isContainer != null && this.m_isContainer) {
         this.m_registryService.getLogger().debug("Running in a container: Always behave synchronously!");
         return true;
      } else {
         return false;
      }
   }

   public void setSynchronous(boolean var1) throws JAXRException {
      if (this.isClosed()) {
         throw new InvalidRequestException(JAXRMessages.getMessage("jaxr.registry.connection.closed"));
      } else {
         this.m_isSynchronous = var1;
      }
   }

   public void setCredentials(Set var1) throws JAXRException {
      if (this.isClosed()) {
         throw new InvalidRequestException(JAXRMessages.getMessage("jaxr.registry.connection.closed"));
      } else {
         if (var1 == null) {
            var1 = new HashSet();
         }

         this.m_credentials = (Set)var1;
         String var2 = null;
         String var3 = null;
         Iterator var4 = ((Set)var1).iterator();

         while(var4.hasNext() && var2 == null) {
            Object var5 = var4.next();
            if (var5 instanceof PasswordAuthentication) {
               PasswordAuthentication var6 = (PasswordAuthentication)var5;
               var2 = var6.getUserName();
               var3 = new String(var6.getPassword());
            }
         }

         this.m_registryService.setCredentials(var2, var3);
      }
   }

   public Set getCredentials() throws JAXRException {
      if (this.isClosed()) {
         throw new InvalidRequestException(JAXRMessages.getMessage("jaxr.registry.connection.closed"));
      } else {
         return this.m_credentials;
      }
   }

   public URL getQueryURL() throws InvalidRequestException, JAXRException {
      if (this.isClosed()) {
         throw new InvalidRequestException(JAXRMessages.getMessage("jaxr.registry.connection.closed"));
      } else {
         return this.m_queryURL;
      }
   }

   public URL getPublishURL() throws InvalidRequestException, JAXRException {
      if (this.isClosed()) {
         throw new InvalidRequestException(JAXRMessages.getMessage("jaxr.registry.connection.closed"));
      } else {
         return this.m_publishURL;
      }
   }

   public Integer getMaxRows() {
      String var1 = this.m_properties.getProperty("javax.xml.registry.uddi.maxRows");

      Integer var2;
      try {
         var2 = Integer.valueOf(var1);
      } catch (NumberFormatException var4) {
         var2 = null;
      }

      return var2;
   }

   ProviderInfo getProviderInfo() {
      return this.m_providerInfo;
   }

   Properties getProperties() {
      return this.m_properties;
   }
}
