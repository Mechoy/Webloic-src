package weblogic.management.j2ee.internal;

public class J2EEModuleMBeanImpl extends J2EEDeployedObjectMBeanImpl {
   protected final String[] javaVMS;

   public J2EEModuleMBeanImpl(String var1, String var2, String var3, ApplicationInfo var4) {
      super(var1, var2, var4);
      this.javaVMS = new String[]{var3};
   }

   public String[] getjavaVMs() {
      return this.javaVMS;
   }
}
