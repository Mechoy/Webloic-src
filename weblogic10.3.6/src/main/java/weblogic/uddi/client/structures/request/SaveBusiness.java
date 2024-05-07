package weblogic.uddi.client.structures.request;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.BusinessEntity;
import weblogic.uddi.client.structures.datatypes.UploadRegister;

public class SaveBusiness extends UpdateRequest {
   private Vector businessEntityVector = new Vector();
   private Vector uploadRegisterVector = new Vector();

   public void addBusinessEntity(BusinessEntity var1) {
      this.uploadRegisterVector.removeAllElements();
      this.businessEntityVector.add(var1);
   }

   public void setBusinessEntityVector(Vector var1) {
      this.uploadRegisterVector.removeAllElements();
      this.businessEntityVector = var1;
   }

   public Vector getBusinessEntityVector() {
      return this.businessEntityVector;
   }

   public void addUploadRegister(UploadRegister var1) {
      this.businessEntityVector.removeAllElements();
      this.uploadRegisterVector.add(var1);
   }

   public void setUploadRegisterVector(Vector var1) {
      this.businessEntityVector.removeAllElements();
      this.uploadRegisterVector = var1;
   }

   public Vector getUploadRegisterVector() {
      return this.uploadRegisterVector;
   }
}
