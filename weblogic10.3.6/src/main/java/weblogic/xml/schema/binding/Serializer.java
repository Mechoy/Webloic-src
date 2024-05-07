package weblogic.xml.schema.binding;

import javax.xml.rpc.encoding.SerializerFactory;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;

public interface Serializer extends javax.xml.rpc.encoding.Serializer, SerializerFactory {
   void serialize(Object var1, XMLName var2, XMLOutputStream var3, SerializationContext var4) throws SerializationException;
}
