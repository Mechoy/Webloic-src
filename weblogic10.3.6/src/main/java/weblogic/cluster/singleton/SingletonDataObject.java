package weblogic.cluster.singleton;

import java.util.List;

class SingletonDataObject {
   String name;
   String appName;
   List targets;
   boolean jta;

   public SingletonDataObject(String var1) {
      this.name = var1;
      this.jta = false;
   }

   public SingletonDataObject(String var1, boolean var2) {
      this.name = var1;
      this.jta = var2;
   }

   public SingletonDataObject(String var1, String var2, List var3) {
      this.name = var1;
      this.appName = var2;
      this.targets = var3;
      this.jta = false;
   }

   public boolean isJTA() {
      return this.jta;
   }

   public String getName() {
      return this.name;
   }

   public String getAppName() {
      return this.appName;
   }

   public boolean isAppScopedSingleton() {
      return this.appName != null;
   }

   public List getTargets() {
      return this.targets;
   }
}
