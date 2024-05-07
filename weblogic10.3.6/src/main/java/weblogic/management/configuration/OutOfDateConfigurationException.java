package weblogic.management.configuration;

public final class OutOfDateConfigurationException extends ConfigurationException {
   private static final long serialVersionUID = 5881019814205842701L;
   private Object version;
   private Object currentVersion;

   protected OutOfDateConfigurationException(Object var1, Object var2, String var3) {
      super(var3);
      this.version = var2;
      this.currentVersion = var1;
   }

   public Object getVersion() {
      return this.version;
   }

   public Object getCurrentVersion() {
      return this.currentVersion;
   }
}
