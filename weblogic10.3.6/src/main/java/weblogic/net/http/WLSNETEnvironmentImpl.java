package weblogic.net.http;

public class WLSNETEnvironmentImpl extends NETEnvironment {
   public boolean useSunHttpHandler() {
      return System.getProperty("UseSunHttpHandler") != null;
   }
}
