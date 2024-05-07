package weblogic.jndi.internal;

public class ApplicationNamingInfo {
   private boolean forceCallByReference = false;

   public void setForceCallByReference(boolean var1) {
      this.forceCallByReference = var1;
   }

   public boolean isForceCallByReferenceEnabled() {
      return this.forceCallByReference;
   }
}
