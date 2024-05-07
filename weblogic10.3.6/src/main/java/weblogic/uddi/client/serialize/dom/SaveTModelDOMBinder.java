package weblogic.uddi.client.serialize.dom;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.uddi.client.structures.datatypes.TModel;
import weblogic.uddi.client.structures.datatypes.UploadRegister;
import weblogic.uddi.client.structures.request.SaveTModel;

public class SaveTModelDOMBinder {
   public static Element toDOM(SaveTModel var0, Document var1) {
      Element var2 = UpdateRequestDOMBinder.toDOM("save_tModel", var0, var1);
      Vector var3 = var0.getTModelVector();
      if (var3 != null) {
         for(int var4 = 0; var4 < var3.size(); ++var4) {
            var2.appendChild(TModelDOMBinder.toDOM((TModel)var3.elementAt(var4), var1));
         }
      }

      Vector var6 = var0.getUploadRegisterVector();
      if (var6 != null) {
         for(int var5 = 0; var5 < var6.size(); ++var5) {
            var2.appendChild(UploadRegisterDOMBinder.toDOM((UploadRegister)var6.elementAt(var5), var1));
         }
      }

      return var2;
   }
}
