package weblogic.xml.jaxr.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.xml.registry.BulkResponse;
import javax.xml.registry.BusinessLifeCycleManager;
import javax.xml.registry.BusinessQueryManager;
import javax.xml.registry.CapabilityProfile;
import javax.xml.registry.DeclarativeQueryManager;
import javax.xml.registry.InvalidRequestException;
import javax.xml.registry.JAXRException;
import javax.xml.registry.RegistryService;
import javax.xml.registry.infomodel.ClassificationScheme;
import weblogic.xml.jaxr.registry.provider.RegistryProxy;
import weblogic.xml.jaxr.registry.provider.RegistryProxyFactory;
import weblogic.xml.jaxr.registry.resource.JAXRMessages;
import weblogic.xml.jaxr.registry.util.JAXRLogger;

public class RegistryServiceImpl extends BaseJAXRObject implements RegistryService {
   private ConnectionImpl m_connectionImpl;
   private BusinessLifeCycleManager m_businessLifeCycleManager;
   private BusinessQueryManager m_businessQueryManager;
   private RegistryProxy m_registryProxy;
   private Map m_asynchronousResponses;
   private JAXRLogger m_logger;
   private String m_currentUser;
   private Map m_semanticEquivalences;
   private static int s_sequenceId = 0;

   public RegistryServiceImpl(ConnectionImpl var1) throws JAXRException {
      this.m_connectionImpl = var1;
      this.m_registryProxy = this.createRegistryProxy();
      this.m_businessLifeCycleManager = new BusinessLifeCycleManagerImpl(this);
      this.m_businessQueryManager = new BusinessQueryManagerImpl(this);
      this.m_asynchronousResponses = new HashMap();
      String var2 = var1.getProperties().getProperty("javax.xml.registry.semanticEquivalences");
      this.m_semanticEquivalences = this.getSemanticEquivalences(var2);
   }

   public CapabilityProfile getCapabilityProfile() throws JAXRException {
      return this.m_connectionImpl.getProviderInfo().getCapabilityProfile();
   }

   public BusinessLifeCycleManager getBusinessLifeCycleManager() {
      return this.m_businessLifeCycleManager;
   }

   public BusinessQueryManager getBusinessQueryManager() throws JAXRException {
      return this.m_businessQueryManager;
   }

   public DeclarativeQueryManager getDeclarativeQueryManager() throws JAXRException {
      this.checkCapability(this, 1);
      return null;
   }

   public BulkResponse getBulkResponse(String var1) throws InvalidRequestException, JAXRException {
      BulkResponse var2 = (BulkResponse)this.m_asynchronousResponses.get(var1);
      if (var2 == null) {
         String var3 = JAXRMessages.getMessage("jaxr.provider.invalidRequestId", new Object[]{var1});
         throw new InvalidRequestException(var3);
      } else {
         this.m_asynchronousResponses.remove(var1);
         return var2;
      }
   }

   public ClassificationScheme getDefaultPostalScheme() throws JAXRException {
      Properties var1 = this.m_connectionImpl.getProperties();
      String var2 = var1.getProperty("javax.xml.registry.postalAddressScheme");
      ClassificationScheme var3;
      if (var2 == null) {
         var3 = null;
      } else {
         var3 = this.m_businessQueryManager.findClassificationSchemeByName((Collection)null, var2);
      }

      return var3;
   }

   public String makeRegistrySpecificRequest(String var1) throws JAXRException {
      String var2 = this.m_registryProxy.makeRegistrySpecificRequest(var1);
      return var2;
   }

   public ConnectionImpl getConnectionImpl() {
      return this.m_connectionImpl;
   }

   public void setCredentials(String var1, String var2) throws JAXRException {
      this.m_currentUser = var1;
      this.m_registryProxy.setCredentials(var1, var2);
   }

   public void closeConnection() throws JAXRException {
      if (this.m_registryProxy != null) {
         this.m_registryProxy.closeConnection();
         this.m_registryProxy = null;
      }

   }

   public RegistryProxy getRegistryProxy() throws JAXRException {
      if (this.m_registryProxy == null) {
         String var1 = JAXRMessages.getMessage("jaxr.provider.connection.closed");
         throw new InvalidRequestException(var1);
      } else {
         return this.m_registryProxy;
      }
   }

   public void addBulkResponse(String var1, BulkResponse var2) {
      this.m_asynchronousResponses.put(var1, var2);
   }

   public JAXRLogger getLogger() {
      if (this.m_logger == null) {
         this.m_logger = this.m_connectionImpl.getProviderInfo().getLogger();
      }

      return this.m_logger;
   }

   public String getCurrentUser() {
      return this.m_currentUser;
   }

   public String getSemanticEquivalent(String var1) {
      String var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = (String)this.m_semanticEquivalences.get(var1);
      }

      return var2;
   }

   public int getSequenceId() {
      return s_sequenceId++;
   }

   private Map getSemanticEquivalences(String var1) {
      HashMap var2 = new HashMap();
      if (var1 != null) {
         StringTokenizer var3 = new StringTokenizer(var1, "|");

         while(var3.hasMoreTokens()) {
            String var4 = var3.nextToken();
            StringTokenizer var5 = new StringTokenizer(var4, ",");
            if (var5.countTokens() == 2) {
               String var7 = var5.nextToken().trim();
               int var6 = var7.indexOf("urn:");
               if (var6 >= 0) {
                  var7 = var7.substring(var6 + 4);
               }

               String var8 = var5.nextToken().trim();
               var6 = var8.indexOf("urn:");
               if (var6 >= 0) {
                  var8 = var8.substring(var6 + 4);
               }

               var2.put(var7, var8);
               var2.put(var8, var7);
            }
         }
      }

      return var2;
   }

   private RegistryProxy createRegistryProxy() throws JAXRException {
      RegistryProxyFactory var1 = this.m_connectionImpl.getProviderInfo().getRegistryProxyFactory(this);
      return var1.createRegistryProxy();
   }
}
