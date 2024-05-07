package weblogic.uddi.client.structures.request;

import java.util.Vector;
import weblogic.uddi.client.structures.datatypes.TModel;
import weblogic.uddi.client.structures.datatypes.UploadRegister;

public class SaveTModel extends UpdateRequest {
   private Vector tModelVector = new Vector();
   private Vector uploadRegisterVector = new Vector();

   public void addTModel(TModel var1) {
      this.uploadRegisterVector.removeAllElements();
      this.tModelVector.add(var1);
   }

   public void setTModelVector(Vector var1) {
      this.uploadRegisterVector.removeAllElements();
      this.tModelVector = var1;
   }

   public Vector getTModelVector() {
      return this.tModelVector;
   }

   public void addUploadRegister(UploadRegister var1) {
      this.tModelVector.removeAllElements();
      this.uploadRegisterVector.add(var1);
   }

   public void setUploadRegisterVector(Vector var1) {
      this.tModelVector.removeAllElements();
      this.uploadRegisterVector = var1;
   }

   public Vector getUploadRegisterVector() {
      return this.uploadRegisterVector;
   }
}
