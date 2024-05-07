package weblogic.xml.crypto.dsig.api.spec;

import java.util.Map;

public class XPath {
   private String expression;
   private Filter filter;
   private Map namespaceMap;

   public XPath(String var1, Filter var2) {
      this.expression = var1;
      this.filter = var2;
   }

   public XPath(String var1, Filter var2, Map var3) {
      this.expression = var1;
      this.filter = var2;
      this.namespaceMap = var3;
   }

   public String getExpression() {
      return this.expression;
   }

   public Filter getFilter() {
      return this.filter;
   }

   public Map getNamespaceMap() {
      return this.namespaceMap;
   }

   public static class Filter {
      private String filter;
      public static Filter INTERSECT = new Filter("intersect");
      public static Filter SUBTRACT = new Filter("subtract");
      public static Filter UNION = new Filter("union");

      private Filter(String var1) {
         this.filter = var1;
      }

      public String toString() {
         return this.filter;
      }
   }
}
