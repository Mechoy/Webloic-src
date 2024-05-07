package weblogic.wsee.tools.jws.decl.port;

public class JmsPort extends Port {
   private String queue = null;
   private String factory = null;
   static final String PROTOCOL = "jms";

   public JmsPort() {
   }

   public JmsPort(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3);
      this.queue = var4;
   }

   public JmsPort(String var1, String var2, String var3, String var4, String var5) {
      super(var1, var2, var3);
      this.queue = var4;
      this.factory = var5;
   }

   public String getQueue() {
      return this.queue;
   }

   public void setQueue(String var1) {
      this.queue = var1;
   }

   public String getFactory() {
      return this.factory;
   }

   public void setFactory(String var1) {
      this.factory = var1;
   }

   public void setConnectionFactory(String var1) {
      this.factory = var1;
   }

   public String getProtocol() {
      return "jms";
   }

   public String getWsdlTransportNS() {
      return "http://www.openuri.org/2002/04/soap/jms/";
   }

   public String getURI() {
      String var1 = super.getURI();
      if (this.queue == null && this.factory == null) {
         return var1;
      } else if (this.factory == null) {
         return var1 + "?URI=" + this.normalize(this.queue);
      } else {
         return this.queue == null ? var1 + "?FACTORY=" + this.normalize(this.factory) : var1 + "?URI=" + this.normalize(this.queue) + "&FACTORY=" + this.normalize(this.factory);
      }
   }

   private String normalize(String var1) {
      return var1 == null ? "" : var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof JmsPort)) {
         return false;
      } else if (!super.equals(var1)) {
         return false;
      } else {
         JmsPort var2 = (JmsPort)var1;
         if (this.queue != null) {
            if (!this.queue.equals(var2.queue)) {
               return false;
            }
         } else if (var2.queue != null) {
            return false;
         }

         if (this.factory != null) {
            if (!this.factory.equals(var2.factory)) {
               return false;
            }
         } else if (var2.factory != null) {
            return false;
         }

         return true;
      }
   }

   public int hashCode() {
      int var1 = super.hashCode();
      var1 = 29 * var1 + (this.queue != null ? this.queue.hashCode() : 0);
      var1 = 29 * var1 + (this.factory != null ? this.factory.hashCode() : 0);
      return var1;
   }
}
