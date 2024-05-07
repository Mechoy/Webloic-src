package weblogic.connector.configuration;

import weblogic.connector.external.RAInfo;

public abstract class Configuration_BaseImpl implements Configuration {
   RAInfo raInfo;

   public Configuration_BaseImpl(RAInfo var1) {
      this.raInfo = var1;
   }
}
