package weblogic.wsee.codec;

import java.util.HashMap;
import weblogic.wsee.codec.soap11.SoapCodec;
import weblogic.wsee.codec.soap12.Soap12Codec;
import weblogic.wsee.wsdl.WsdlBinding;

public class CodecFactory {
   private static CodecFactory instance = new CodecFactory();
   private HashMap codecs = new HashMap();

   private CodecFactory() {
      this.codecs.put("SOAP11", new SoapCodec());
      this.codecs.put("SOAP12", new Soap12Codec());
   }

   public static CodecFactory instance() {
      return instance;
   }

   public Codec getCodec(WsdlBinding var1) throws CodecException {
      String var2 = var1.getBindingType();
      Codec var3 = (Codec)this.codecs.get(var2);
      if (var3 == null) {
         throw new CodecException("Unable to find Codec for binding type '" + var2 + "'");
      } else {
         return var3;
      }
   }
}
