package weblogic.auddi.uddi.request;

import weblogic.auddi.soap.SOAPWrapper;
import weblogic.auddi.uddi.BaseUDDIObject;
import weblogic.auddi.uddi.UDDIException;

public abstract class UDDIRequest extends BaseUDDIObject {
   public static final String GENERIC = "2.0";
   public static final String XMLNS = "urn:uddi-org:api_v2";
   protected int maxRows = -1;
   public static final String WILDCARD_KEY = "00000000-0000-0000-0000-000000000000";
   public static final String WILDCARD_TMODEL_KEY = "uuid:00000000-0000-0000-0000-000000000000";
   private boolean m_isAPI = true;

   public boolean isAPI() {
      return this.m_isAPI;
   }

   public void setAPI(boolean var1) {
      this.m_isAPI = var1;
   }

   public void setMaxRows(int var1) {
      this.maxRows = var1;
   }

   public void setMaxRows(String var1) {
      Integer var2 = new Integer(var1);
      this.maxRows = var2;
   }

   public int getMaxRows() {
      return this.maxRows;
   }

   public String toSOAP() throws UDDIException {
      StringBuffer var1 = new StringBuffer();
      var1.append(SOAPWrapper.makeSOAPString(this.toXML()));
      return var1.toString();
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append(" generic=\"").append("2.0").append("\"");
      if (this.maxRows != -1) {
         var1.append(" maxRows=\"").append(this.maxRows).append("\"");
      }

      var1.append(" xmlns=\"").append("urn:uddi-org:api_v2").append("\"");
      return var1.toString();
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{new Boolean(this.m_isAPI), new Integer(this.maxRows)};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_isAPI", "maxRows"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
