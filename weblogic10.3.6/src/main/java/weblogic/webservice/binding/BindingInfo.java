package weblogic.webservice.binding;

/** @deprecated */
public class BindingInfo {
   public static final String SOAP11 = "SOAP1.1";
   public static final String SOAP12 = "SOAP1.2";
   public static final String DEFAULT_TRANSPORT = "http11";
   private static final String TIMEOUT_PROP = "weblogic.webservice.rpc.timeoutsecs";
   private String transport;
   private String address;
   private String type;
   private String acceptCharset;
   private String charset;
   private int timeout;
   private boolean verbose;
   private BindingExtension[] allBindings;

   public BindingInfo() {
      this("http11");
   }

   public BindingInfo(String var1) {
      this.type = "SOAP1.1";
      this.charset = null;
      this.timeout = -1;
      this.allBindings = new BindingExtension[2];
      this.transport = var1;
   }

   public String getTransport() {
      return this.transport;
   }

   public String getAddress() {
      return this.address;
   }

   public void setAddress(String var1) {
      this.address = var1;
   }

   public void setType(String var1) {
      this.type = var1;
   }

   public String getType() {
      return this.type;
   }

   public boolean isSoap12() {
      return "SOAP1.2".equals(this.getType());
   }

   public String getCharset() {
      return this.charset;
   }

   public void setCharset(String var1) {
      this.charset = var1;
   }

   public int getTimeout() {
      String var1 = System.getProperty("weblogic.webservice.rpc.timeoutsecs");
      if (var1 != null) {
         try {
            this.timeout = new Integer(var1);
         } catch (NumberFormatException var3) {
            this.timeout = -1;
         }

         return this.timeout;
      } else {
         return this.timeout;
      }
   }

   public void setTimeout(int var1) {
      this.timeout = var1;
   }

   public String getAcceptCharset() {
      return this.acceptCharset;
   }

   public void setAcceptCharset(String var1) {
      this.acceptCharset = var1;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public BindingExtension getExtension(int var1) {
      if (var1 >= 0 && var1 < 2) {
         return this.allBindings[var1];
      } else {
         throw new IllegalArgumentException("Binding key is out of range");
      }
   }

   public void addExtension(BindingExtension var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("ext cannot be null");
      } else {
         int var2 = var1.getKey();
         if (var2 >= 0 && var2 < 2) {
            this.allBindings[var2] = var1;
         } else {
            throw new IllegalArgumentException("Binding key is out of range");
         }
      }
   }
}
