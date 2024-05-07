package weblogic.jms.adapter;

import java.util.Enumeration;
import java.util.Properties;
import weblogic.jms.bridge.ConnectionSpec;

public class JMSConnectionSpec implements ConnectionSpec {
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
   private boolean preserveMsgProperty = false;

   public JMSConnectionSpec(String var1, String var2) {
      this.user = var1;
      this.password = var2;
   }

   public JMSConnectionSpec(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, boolean var10, String var11, boolean var12) {
      this.user = var1;
      this.password = var2;
      this.url = var3;
      this.icFactory = var4;
      this.selector = var5;
      this.factoryJndi = var6;
      this.destJndi = var7;
      this.destType = var8;
      this.name = var9;
      this.durable = var10;
      this.classPath = var11;
      this.preserveMsgProperty = var12;
   }

   public JMSConnectionSpec(Properties var1) {
      Enumeration var2 = var1.propertyNames();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         String var4 = var1.getProperty(var3);
         if (var3.equalsIgnoreCase("ConnectionFactoryJNDI")) {
            this.factoryJndi = var4;
         } else if (var3.equalsIgnoreCase("ConnectionFactoryJNDIName")) {
            this.factoryJndi = var4;
         } else if (var3.equalsIgnoreCase("ConnectionURL")) {
            this.url = var4;
         } else if (var3.equalsIgnoreCase("InitialContextFactory")) {
            this.icFactory = var4;
         } else if (var3.equalsIgnoreCase("DestinationJNDI")) {
            this.destJndi = var4;
         } else if (var3.equalsIgnoreCase("DestinationJNDIName")) {
            this.destJndi = var4;
         } else if (var3.equalsIgnoreCase("DestinationType")) {
            this.destType = var4;
         } else if (var3.equalsIgnoreCase("name")) {
            this.name = var4;
         } else if (var3.equalsIgnoreCase("username")) {
            this.user = var4;
         } else if (var3.equalsIgnoreCase("selector")) {
            this.selector = var4;
         } else if (var3.equalsIgnoreCase("password")) {
            this.password = var4;
         } else if (var3.equalsIgnoreCase("classpath")) {
            this.classPath = var4;
         } else if (var3.equalsIgnoreCase("preserveMsgProperty")) {
            if (var4.equals("true")) {
               this.preserveMsgProperty = true;
            } else {
               this.preserveMsgProperty = false;
            }
         } else if (var3.equalsIgnoreCase("durability")) {
            if (var4.equals("true")) {
               this.durable = true;
            } else {
               this.durable = false;
            }
         }
      }

   }

   public String getSelector() {
      return this.selector;
   }

   public String getUrl() {
      return this.url;
   }

   public String getInitialContextFactory() {
      return this.icFactory;
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

   public String getFactoryJndi() {
      return this.factoryJndi;
   }

   public boolean getDurability() {
      return this.durable;
   }

   public boolean getPreserveMsgProperty() {
      return this.preserveMsgProperty;
   }

   public String getDestJndi() {
      return this.destJndi;
   }

   public String getDestType() {
      return this.destType;
   }

   public String getClasspath() {
      return this.classPath;
   }
}
