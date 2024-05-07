package weblogic.management.provider.internal;

public class ConfigReaderContext {
   private boolean streamModified;

   public boolean isStreamModifed() {
      return this.streamModified;
   }

   public void setStreamModified(boolean var1) {
      this.streamModified = var1;
   }
}
