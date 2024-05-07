package weblogic.jms.adapter51;

import javax.resource.spi.ConnectionRequestInfo;

public class JMSConnectionRequestInfo implements ConnectionRequestInfo {
   private String name;
   private String user;
   private String password;
   private String url;
   private String icFactory;
   private String selector;
   private String factoryJndi;
   private String destJndi;
   private String destType;
   private boolean durable;
   private String classPath;
   private int type;

   public JMSConnectionRequestInfo(String var1, String var2, int var3) {
      this.type = -1;
      this.user = var1;
      this.password = var2;
      this.type = var3;
   }

   public JMSConnectionRequestInfo(String var1, String var2, int var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10) {
      this(var1, var2, var3);
      this.url = var4;
      this.icFactory = var5;
      this.selector = var6;
      this.factoryJndi = var7;
      this.destJndi = var8;
      this.destType = var9;
      this.classPath = var10;
   }

   public JMSConnectionRequestInfo(String var1, String var2, int var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, boolean var11, String var12) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var12);
      this.name = var10;
      this.durable = var11;
   }

   public String getName() {
      return this.name;
   }

   public String getUser() {
      return this.user;
   }

   public String getPassword() {
      return this.password;
   }

   public int getType() {
      return this.type;
   }

   public String getUrl() {
      return this.url;
   }

   public String getInitialContextFactory() {
      return this.icFactory;
   }

   public String getSelector() {
      return this.selector;
   }

   public String getFactoryJndi() {
      return this.factoryJndi;
   }

   public String getDestJndi() {
      return this.destJndi;
   }

   public String getDestType() {
      return this.destType;
   }

   public boolean getDurability() {
      return this.durable;
   }

   public String getClasspath() {
      return this.classPath;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof JMSConnectionRequestInfo)) {
         return false;
      } else {
         JMSConnectionRequestInfo var2 = (JMSConnectionRequestInfo)var1;
         return this.isEqual(this.user, var2.getUser()) && this.isEqual(this.password, var2.getPassword()) && this.isEqual(this.url, var2.getUrl()) && this.isEqual(this.icFactory, var2.getInitialContextFactory()) && this.isEqual(this.selector, var2.getSelector()) && this.isEqual(this.factoryJndi, var2.getFactoryJndi()) && this.isEqual(this.destJndi, var2.getDestJndi()) && this.isEqual(this.destType, var2.getDestType()) && this.isEqual(this.name, var2.name) && this.isEqual(this.classPath, var2.classPath) && (this.durable && var2.durable || !this.durable && !var2.durable) && this.type == var2.type;
      }
   }

   public int hashCode() {
      String var1 = "";
      if (this.name != null) {
         var1 = var1 + this.name;
      }

      if (this.user != null) {
         var1 = var1 + this.user;
      }

      if (this.password != null) {
         var1 = var1 + this.password;
      }

      var1 = var1 + this.type;
      if (this.url != null) {
         var1 = var1 + this.url;
      }

      if (this.icFactory != null) {
         var1 = var1 + this.icFactory;
      }

      if (this.selector != null) {
         var1 = var1 + this.selector;
      }

      if (this.factoryJndi != null) {
         var1 = var1 + this.factoryJndi;
      }

      if (this.destJndi != null) {
         var1 = var1 + this.destJndi;
      }

      if (this.destType != null) {
         var1 = var1 + this.destType;
      }

      if (this.durable) {
         var1 = var1 + "true";
      } else {
         var1 = var1 + "false";
      }

      if (this.classPath != null) {
         var1 = var1 + this.classPath;
      }

      return var1.hashCode();
   }

   private boolean isEqual(Object var1, Object var2) {
      if (var1 == null) {
         return var2 == null;
      } else {
         return var1.equals(var2);
      }
   }
}
