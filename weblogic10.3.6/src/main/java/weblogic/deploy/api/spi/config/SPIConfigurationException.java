package weblogic.deploy.api.spi.config;

import javax.enterprise.deploy.spi.exceptions.ConfigurationException;

public class SPIConfigurationException extends ConfigurationException {
   public SPIConfigurationException(String var1) {
      super(var1);
   }

   public SPIConfigurationException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }
}
