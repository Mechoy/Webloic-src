package weblogic.auddi.uddi.request.publish;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.TModel;
import weblogic.auddi.uddi.datastructure.TModels;

public class SaveTModelRequest extends UDDIPublishRequest implements Serializable {
   private TModels tModels = null;

   public void addTModel(TModel var1) throws UDDIException {
      if (this.tModels == null) {
         this.tModels = new TModels();
      }

      this.tModels.add(var1);
   }

   public TModels getTModels() {
      return this.tModels;
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<save_tModel");
      var1.append(super.toXML() + ">");
      if (this.m_authInfo != null) {
         var1.append(this.m_authInfo.toXML());
      }

      if (this.tModels != null) {
         var1.append(this.tModels.toXML());
      }

      var1.append("</save_tModel>");
      return var1.toString();
   }
}
