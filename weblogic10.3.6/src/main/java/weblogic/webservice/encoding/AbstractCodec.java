package weblogic.webservice.encoding;

import java.util.Collections;
import java.util.Iterator;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.encoding.DeserializerFactory;
import javax.xml.rpc.encoding.SerializerFactory;
import weblogic.xml.schema.binding.Deserializer;
import weblogic.xml.schema.binding.Serializer;

/** @deprecated */
public abstract class AbstractCodec implements Serializer, Deserializer, SerializerFactory, DeserializerFactory {
   public String getMechanismType() {
      return "stream";
   }

   public javax.xml.rpc.encoding.Deserializer getDeserializerAs(String var1) throws JAXRPCException {
      if (this.getMechanismType().equals(var1)) {
         return this;
      } else {
         throw new JAXRPCException("unsupported mechanism: " + var1);
      }
   }

   public javax.xml.rpc.encoding.Serializer getSerializerAs(String var1) throws JAXRPCException {
      if (this.getMechanismType().equals(var1)) {
         return this;
      } else {
         throw new JAXRPCException("unsupported mechanism: " + var1);
      }
   }

   public Iterator getSupportedMechanismTypes() {
      return Collections.singletonList("stream").iterator();
   }
}
