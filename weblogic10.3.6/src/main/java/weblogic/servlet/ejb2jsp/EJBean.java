package weblogic.servlet.ejb2jsp;

public class EJBean {
   private String name;
   private String remoteType;
   private String homeType;
   private String jndiName;
   private boolean isEntity;
   private boolean isSession;
   private boolean isStatefulSession;

   public String toString() {
      String var1 = null;
      if (this.isEntity) {
         var1 = "ENTITY";
      } else if (this.isStatefulSession) {
         var1 = "STATEFUL";
      } else {
         var1 = "STATELESS";
      }

      return "[EJBean: name=" + this.name + " rt=" + this.remoteType + " ht=" + this.homeType + " jn=" + this.jndiName + " type=" + var1 + "]";
   }

   public EJBean(String var1, String var2, String var3, String var4, boolean var5, boolean var6, boolean var7) {
      this.name = var1;
      this.remoteType = var2;
      this.homeType = var3;
      this.jndiName = var4;
      this.isEntity = var5;
      this.isSession = var6;
      this.isStatefulSession = var7;
   }

   public String getEJBName() {
      return this.name;
   }

   public String getRemoteInterfaceName() {
      return this.remoteType;
   }

   public String getHomeInterfaceName() {
      return this.homeType;
   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public boolean isEntityBean() {
      return this.isEntity;
   }

   public boolean isSessionBean() {
      return this.isSession;
   }

   public boolean isStatefulSessionBean() {
      return this.isStatefulSession;
   }
}
