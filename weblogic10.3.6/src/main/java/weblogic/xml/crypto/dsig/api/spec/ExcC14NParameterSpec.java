package weblogic.xml.crypto.dsig.api.spec;

import java.util.List;

public final class ExcC14NParameterSpec implements C14NMethodParameterSpec {
   public static String DEFAULT = "?";
   private List prefixList;

   public ExcC14NParameterSpec() {
   }

   public ExcC14NParameterSpec(List var1) {
      this.prefixList = var1;
   }

   public List getPrefixList() {
      return this.prefixList;
   }
}
