package weblogic.xml.crypto.encrypt.api;

import java.io.InputStream;

public class TBEData extends TBEBase {
   private final InputStream input;

   public TBEData(InputStream var1) {
      this(var1, (String)null, (String)null, (String)null);
   }

   public TBEData(InputStream var1, String var2, String var3) {
      this(var1, (String)null, var2, var3);
   }

   public TBEData(InputStream var1, String var2, String var3, String var4) {
      super(var2, var3, var4);
      this.input = var1;
   }

   public InputStream getInputStream() {
      return this.input;
   }
}
