package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.runtime.MarshalOptions;
import com.bea.xml.SchemaType;
import com.bea.xml.XmlException;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import org.w3c.dom.Element;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.bind.runtime.RuntimeBindingsBuilder;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsType;

public class GenericRuntimeBindingsImpl implements RuntimeBindings {
   private static final boolean verbose = Verbose.isVerbose(GenericRuntimeBindingsImpl.class);
   private RuntimeBindings parent = RuntimeBindingsBuilder.Factory.newInstance().createRuntimeBindings();

   public GenericRuntimeBindingsImpl() throws IOException, XmlException {
   }

   public DeserializerContext createDeserializerContext(int var1, Element var2, boolean var3) {
      return this.parent.createDeserializerContext(var1, var2, var3);
   }

   public SerializerContext createSerializerContext(int var1) {
      return this.parent.createSerializerContext(var1);
   }

   public SerializerContext createSerializerContext(int var1, MarshalOptions var2) {
      return this.parent.createSerializerContext(var1, var2);
   }

   public String getJavaType(XmlTypeName var1) {
      return SOAPElement.class.getName();
   }

   public String getJavaType(QName var1) {
      return SOAPElement.class.getName();
   }

   public QName getLocalElementName(XmlTypeName var1, String var2) {
      return this.parent.getLocalElementName(var1, var2);
   }

   public XmlTypeName getLocalElementType(XmlTypeName var1, String var2) {
      return this.parent.getLocalElementType(var1, var2);
   }

   public QName getTypeForElement(QName var1) {
      return this.parent.getTypeForElement(var1);
   }

   public SchemaType getSchemaTypeForXmlTypeName(XmlTypeName var1) {
      return this.parent.getSchemaTypeForXmlTypeName(var1);
   }

   public int elementIsSingleWildcard(XmlTypeName var1) {
      return this.parent.elementIsSingleWildcard(var1);
   }

   public boolean isSimpleType(QName var1) {
      return this.parent.isSimpleType(var1);
   }

   public boolean isOptionalLocalElement(XmlTypeName var1, String var2, WsMethod var3, WsType var4) {
      return this.parent.isOptionalLocalElement(var1, var2, var3, var4);
   }

   public XmlTypeName getLocalElementName(QName var1, String var2) {
      throw new IllegalStateException();
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   public void toString(ToStringWriter var1) {
      var1.start(this);
      var1.end();
   }
}
