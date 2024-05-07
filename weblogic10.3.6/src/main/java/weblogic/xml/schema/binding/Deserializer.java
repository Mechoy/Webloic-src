package weblogic.xml.schema.binding;

import javax.xml.rpc.encoding.DeserializerFactory;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;

public interface Deserializer extends javax.xml.rpc.encoding.Deserializer, DeserializerFactory {
   Object deserialize(XMLName var1, XMLInputStream var2, DeserializationContext var3) throws DeserializationException;

   Object deserialize(XMLName var1, Attribute var2, DeserializationContext var3) throws DeserializationException;
}
