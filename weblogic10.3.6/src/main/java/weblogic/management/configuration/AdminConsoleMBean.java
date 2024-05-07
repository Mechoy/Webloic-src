package weblogic.management.configuration;

public interface AdminConsoleMBean extends ConfigurationMBean {
   String getCookieName();

   void setCookieName(String var1);

   int getSessionTimeout();

   void setSessionTimeout(int var1);
}
