package weblogic.xml.crypto.dsig.api.spec;

import java.util.List;

public final class XPathFilter2ParameterSpec implements TransformParameterSpec {
   private List xPathList;

   public XPathFilter2ParameterSpec(List var1) {
      this.xPathList = var1;
   }

   public List getXPathList() {
      return this.xPathList;
   }
}
