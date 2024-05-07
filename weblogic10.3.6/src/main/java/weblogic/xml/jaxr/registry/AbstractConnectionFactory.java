package weblogic.xml.jaxr.registry;

import java.util.Properties;
import javax.xml.registry.Connection;
import javax.xml.registry.ConnectionFactory;
import javax.xml.registry.JAXRException;
import weblogic.xml.jaxr.registry.provider.ProviderInfo;

public abstract class AbstractConnectionFactory extends ConnectionFactory {
   private Properties m_properties;

   public void setProperties(Properties var1) throws JAXRException {
      this.m_properties = var1;
   }

   public Properties getProperties() throws JAXRException {
      return this.m_properties;
   }

   public Connection createConnection() throws JAXRException {
      return new ConnectionImpl(this.m_properties, this.getProviderInfo());
   }

   public abstract ProviderInfo getProviderInfo() throws JAXRException;
}
