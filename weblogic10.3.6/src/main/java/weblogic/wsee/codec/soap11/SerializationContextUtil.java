package weblogic.wsee.codec.soap11;

import com.bea.staxb.runtime.MarshalOptions;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.bind.runtime.internal.BaseSerializerContext;

class SerializationContextUtil {
   private static final String ENCODING_STYLE = "http://schemas.xmlsoap.org/soap/encoding/";
   private static final String SOAP12_ENCODING_STYLE = "http://www.w3.org/2003/05/soap-encoding";

   private SerializationContextUtil() {
   }

   static SerializerContext createSerializerContext(RuntimeBindings var0, MessageContext var1, String var2, String var3) {
      MarshalOptions var4 = BaseSerializerContext.createDefaultMarshalOptions();
      if (Boolean.parseBoolean((String)var1.getProperty("weblogic.wsee.marshal.forceIncludeXsiType"))) {
         var4.setForceIncludeXsiType(true);
      }

      if (Boolean.parseBoolean((String)var1.getProperty("weblogic.wsee.marshal.forceOracle1012CompatibleMarshal"))) {
         var4.setForceOracle1012Compatible(true);
         return var0.createSerializerContext(0, var4);
      } else if ("literal".equals(var2)) {
         forceDotNetCompatibleMarshal(var1, var4);
         return var0.createSerializerContext(0, var4);
      } else if ("encoded".equals(var2)) {
         return var3 != null && "http://www.w3.org/2003/05/soap-encoding".equals(var3) ? var0.createSerializerContext(2, var4) : var0.createSerializerContext(1, var4);
      } else {
         throw new AssertionError("unknown encoding: " + var2);
      }
   }

   private static void forceDotNetCompatibleMarshal(MessageContext var0, MarshalOptions var1) {
      Boolean var2 = (Boolean)var0.getProperty("weblogic.wsee.dotnet.compatible.binding");
      if (var2 != null) {
         var1.setForceDotNetCompatibleMarshal(var2);
      } else {
         var1.setForceDotNetCompatibleMarshal(Boolean.parseBoolean(System.getProperty("weblogic.wsee.dotnet.compatible.binding")));
      }

   }
}
