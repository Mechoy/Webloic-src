package weblogic.xml.crypto.dsig.api.spec;

import java.util.Map;

public final class XPathFilterParameterSpec implements TransformParameterSpec {
   private String xPath;
   private Map namespaceMap;

   public XPathFilterParameterSpec(String var1) {
      this.xPath = var1;
   }

   public XPathFilterParameterSpec(String var1, Map var2) {
      this.xPath = var1;
      this.namespaceMap = var2;
   }

   public Map getNamespaceMap() {
      return this.namespaceMap;
   }

   public String getXPath() {
      return this.xPath;
   }
}
