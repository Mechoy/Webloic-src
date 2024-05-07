package weblogic.xml.jaxr.registry;

import javax.xml.registry.JAXRException;
import javax.xml.registry.JAXRResponse;
import weblogic.xml.jaxr.registry.infomodel.BaseInfoModelObject;

public class JAXRResponseImpl extends BaseInfoModelObject implements JAXRResponse {
   private String m_requestId;
   private int m_status;

   public JAXRResponseImpl(RegistryServiceImpl var1) {
      super(var1);
   }

   public String getRequestId() throws JAXRException {
      return this.m_requestId;
   }

   public int getStatus() {
      return this.m_status;
   }

   public boolean isAvailable() {
      return this.getStatus() != 3;
   }

   public void setRequestId(String var1) throws JAXRException {
      this.m_requestId = var1;
   }

   public void setStatus(int var1) {
      this.m_status = var1;
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.m_requestId, new Integer(this.m_status)};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"m_requestId", "m_status"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
