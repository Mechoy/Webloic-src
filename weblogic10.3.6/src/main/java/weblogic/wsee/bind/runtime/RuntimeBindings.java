package weblogic.wsee.bind.runtime;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.runtime.MarshalOptions;
import com.bea.xml.SchemaType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsType;

public interface RuntimeBindings {
   DeserializerContext createDeserializerContext(int var1, Element var2, boolean var3);

   SerializerContext createSerializerContext(int var1);

   SerializerContext createSerializerContext(int var1, MarshalOptions var2);

   String getJavaType(XmlTypeName var1);

   XmlTypeName getLocalElementType(XmlTypeName var1, String var2);

   QName getLocalElementName(XmlTypeName var1, String var2);

   QName getTypeForElement(QName var1);

   SchemaType getSchemaTypeForXmlTypeName(XmlTypeName var1);

   boolean isSimpleType(QName var1);

   boolean isOptionalLocalElement(XmlTypeName var1, String var2, WsMethod var3, WsType var4);

   int elementIsSingleWildcard(XmlTypeName var1);
}
