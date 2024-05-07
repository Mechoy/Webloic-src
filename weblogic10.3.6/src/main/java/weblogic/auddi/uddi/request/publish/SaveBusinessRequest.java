package weblogic.auddi.uddi.request.publish;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.BusinessEntities;
import weblogic.auddi.uddi.datastructure.BusinessEntity;

public class SaveBusinessRequest extends UDDIPublishRequest implements Serializable {
   private BusinessEntities businessEntities = null;

   public void addBusinessEntity(BusinessEntity var1) throws UDDIException {
      if (this.businessEntities == null) {
         this.businessEntities = new BusinessEntities();
      }

      this.businessEntities.add(var1);
   }

   public BusinessEntities getBusinessEntities() {
      return this.businessEntities;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<save_business");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      if (this.businessEntities != null) {
         var1.append(this.businessEntities.toXML());
      }

      var1.append("</save_business>");
      return var1.toString();
   }

   protected Object[] getDefiningElements() {
      Object[] var1 = new Object[]{this.businessEntities};
      Object[] var2 = mergeObjectArrays(super.getDefiningElements(), var1);
      return var2;
   }

   protected String[] getVariableNames() {
      String[] var1 = new String[]{"businessEntities"};
      String[] var2 = mergeStringArrays(super.getVariableNames(), var1);
      return var2;
   }
}
