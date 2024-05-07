package weblogic.management.configuration;

public interface RemoteSAFContextMBean extends ConfigurationMBean {
   String getUrl();

   void setUrl(String var1);

   String getProtocol();

   void setProtocol(String var1);

   String getUsername();

   void setUsername(String var1);

   String getPassword();

   void setPassword(String var1);

   String getJndiProperty();

   void setJndiProperty(String var1);

   String getJndiInitialContextFactory();

   void setJndiInitialContextFactory(String var1);
}
