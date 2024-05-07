package weblogic.wsee.security.wst.binding;

import java.util.Map;
import org.w3c.dom.Element;
import weblogic.xml.dom.marshal.MarshalException;

public class Renewing extends TrustDOMStructure {
   public static final String NAME = "Renewing";
   public static final String ALLOW_ATTR = "Allow";
   public static final String OK_ATTR = "OK";
   private static final boolean DEFAULT_ALLOW = true;
   private static final boolean DEFAULT_OK = false;
   private boolean allow = true;
   private boolean ok = false;

   public Renewing() {
   }

   public Renewing(String var1) {
      this.namespaceUri = var1;
   }

   public void setAllow(boolean var1) {
      this.allow = var1;
   }

   public boolean isAllow() {
      return this.allow;
   }

   public void setOK(boolean var1) {
      this.ok = var1;
   }

   public boolean isOK() {
      return this.ok;
   }

   public void marshalContents(Element var1, Map var2) throws MarshalException {
      if (!this.allow) {
         setAttribute(var1, "Allow", String.valueOf(this.allow));
      }

      if (this.ok) {
         setAttribute(var1, "OK", String.valueOf(this.ok));
      }

   }

   public void unmarshalContents(Element var1) throws MarshalException {
      this.allow = getAttrBoolVal(var1, "Allow", true);
      this.ok = getAttrBoolVal(var1, "OK", false);
   }

   public String getName() {
      return "Renewing";
   }

   private static boolean getAttrBoolVal(Element var0, String var1, boolean var2) {
      String var3 = var0.getAttribute(var1);
      return var3 != null && var3.length() > 0 ? Boolean.valueOf(var3) : var2;
   }
}
