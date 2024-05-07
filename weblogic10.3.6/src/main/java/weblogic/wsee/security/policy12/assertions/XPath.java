package weblogic.wsee.security.policy12.assertions;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.util.Verbose;

public class XPath extends SecurityPolicy12Assertion {
   private static final long serialVersionUID = 6198433936359349042L;
   private static boolean verbose = Verbose.isVerbose(XPath.class);
   public static final String XPATH = "XPath";
   String xpathExpr;
   Map<String, String> xpathNamespaces = new HashMap();
   private Element xpathNode;
   private String xpathVersion;
   private String assertionType;

   public String getXPathExpr() {
      return this.xpathExpr;
   }

   private void setXPathExpr(String var1) {
      this.xpathExpr = var1;
      if (!var1.startsWith("/") && var1.indexOf("::") < 0 && Pattern.matches("[a-zA-Z]", var1.substring(0, 1))) {
         Iterator var2 = this.xpathNamespaces.entrySet().iterator();
         String var3 = null;

         label34: {
            Map.Entry var4;
            do {
               if (!var2.hasNext()) {
                  break label34;
               }

               var4 = (Map.Entry)var2.next();
            } while(!((String)var4.getValue()).equals("http://schemas.xmlsoap.org/soap/envelope/") && !((String)var4.getValue()).equals("http://www.w3.org/2003/05/soap-envelope"));

            var3 = (String)var4.getKey();
         }

         if (var3 == null) {
            var3 = "soapenv";
            this.xpathNamespaces.put(var3, "http://schemas.xmlsoap.org/soap/envelope/");
            this.getElementAttrs().put("xmlns:" + var3, "http://schemas.xmlsoap.org/soap/envelope/");
         }

         if (this.assertionType.equals("RequiredElements")) {
            this.xpathExpr = "/" + var3 + ":Envelope/" + var3 + ":Header/" + var1;
         } else {
            this.xpathExpr = "/" + var3 + ":Envelope/" + var1;
         }

         if (verbose) {
            Verbose.log((Object)("Converting relative XPath expression to absolute: " + this.xpathExpr));
         }
      }

   }

   public Map<String, String> getXPathNamespaces() {
      return this.xpathNamespaces;
   }

   public String getXPathVersion() {
      return this.xpathVersion;
   }

   public void setXPathVersion(String var1) {
      this.xpathVersion = var1;
   }

   public boolean getEncryptContentOnly() {
      return this.assertionType.equals("ContentEncryptedElements");
   }

   public QName getName() {
      return new QName(this.getNamespace(), "XPath", "sp");
   }

   public void setAssertion(String var1, String var2, String var3) {
      this.xpathVersion = var1;
      this.assertionType = var3;
      this.setXPathExpr(var2);
   }

   void initAssertion(Element var1) {
      this.xpathNode = var1;
      this.xpathNamespaces = DOMUtils.getNamespaceMapping(this.xpathNode);
      this.assertionType = var1.getParentNode().getLocalName();
      this.setXPathExpr(DOMUtils.getTextContent(var1, true));
   }

   Element serializeAssertion(Document var1, Element var2) {
      weblogic.xml.dom.DOMUtils.addTextData(var2, this.xpathExpr);
      Iterator var3 = this.getElementAttrs().entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         var2.setAttribute((String)var4.getKey(), (String)var4.getValue());
      }

      return var2;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.xpathExpr = var1.readUTF();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeUTF(this.xpathExpr);
   }
}
