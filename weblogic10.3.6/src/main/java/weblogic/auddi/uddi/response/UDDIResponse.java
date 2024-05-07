package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.datastructure.Operator;
import weblogic.auddi.util.PropertyManager;

public abstract class UDDIResponse {
   public static final String GENERIC = "2.0";
   public static final String XMLNS = "urn:uddi-org:api_v2";
   protected Operator m_operator = new Operator(PropertyManager.getRuntimeProperty("auddi.siteoperator"));
   protected boolean m_truncated = false;
   protected boolean m_isTruncatedSet = false;

   public UDDIResponse() {
      this.m_truncated = false;
      this.m_isTruncatedSet = false;
   }

   public void setOperator(Operator var1) {
      this.m_operator = var1;
   }

   public Operator getOperator() {
      return this.m_operator;
   }

   public boolean isTruncated() {
      return this.m_truncated;
   }

   public void setTruncated(boolean var1) {
      this.m_truncated = var1;
      this.m_isTruncatedSet = true;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append(" generic=\"2.0\"");
      if (this.m_operator != null) {
         var1.append(" operator=\"" + this.m_operator.toString() + "\"");
      }

      if (this.m_isTruncatedSet) {
         if (this.isTruncated()) {
            var1.append(" truncated=\"true\"");
         } else {
            var1.append(" truncated=\"false\"");
         }
      }

      var1.append(" xmlns=\"urn:uddi-org:api_v2\"");
      return var1.toString();
   }
}
