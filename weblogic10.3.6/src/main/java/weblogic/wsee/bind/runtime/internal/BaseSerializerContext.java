package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.runtime.MarshalOptions;
import com.bea.staxb.runtime.StaxWriterToNode;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlException;
import java.lang.reflect.InvocationTargetException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.bind.types.TypeRegistry;
import weblogic.wsee.bind.types.XOPByteArrayMarshallerImpl;
import weblogic.wsee.util.Verbose;
import weblogic.xml.dom.DOMStreamWriter;
import weblogic.xml.domimpl.ElementBase;
import weblogic.xml.domimpl.Loader;
import weblogic.xml.domimpl.XMLDomWriter;

public abstract class BaseSerializerContext extends BindingContextImpl implements SerializerContext {
   private Document document;
   private static final boolean mVerbose = Verbose.isVerbose(BaseSerializerContext.class);
   private static final TypeRegistry registry = TypeRegistry.getInstance();
   static final MarshalOptions DEFAULT_MARSHAL_OPTIONS = createDefaultMarshalOptions();
   protected MarshalOptions marshalOptions = null;

   BaseSerializerContext(SchemaTypeLoader var1, MarshalOptions var2) {
      super(var1);
      this.marshalOptions = var2;
   }

   public void setUseMultiRef(boolean var1) {
   }

   public Document getDocument() {
      return this.document;
   }

   public void setDocument(Document var1) {
      this.document = var1;
   }

   public void serializeType(SOAPElement var1, Object var2, Class var3, XmlTypeName var4, QName var5, boolean var6, String var7) throws XMLStreamException, XmlException {
      if (var1 == null) {
         throw new IllegalArgumentException("null parent");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null javaType");
      } else if (var4 == null) {
         throw new IllegalArgumentException("null xmlType");
      } else if (var5 == null) {
         throw new IllegalArgumentException("null name");
      } else {
         if (mVerbose) {
            Verbose.logArgs("xml root", var1.getElementName(), "object", var2, "java Type", var3.getName(), "xml Type", var4, "name", var5, "useMTOMmessage", var6);
         }

         assert this.getDocument() != null;

         if (registry.isWellKnownType(var3, var4.getQName(), var6)) {
            registry.serializeType(var1, var2, var3, var4.getQName(), var5, this, var6, var7);
         } else {
            this.marshalOptions.setMarshaller(new XOPByteArrayMarshallerImpl(this.getMessage()));
            this.internalSerializeType(var1, var2, var5, var4, var3);
         }

      }
   }

   public void serializeElement(SOAPElement var1, Object var2, Class var3, XmlTypeName var4, boolean var5, String var6) throws XMLStreamException, XmlException {
      if (var4 == null) {
         throw new IllegalArgumentException("null xmlElement");
      } else if (!var4.isElement()) {
         throw new IllegalArgumentException(var4 + " must be an element");
      } else {
         if (mVerbose) {
            Verbose.log((Object)("serializing element in " + var1 + "\n" + "object = " + var2 + "\n" + "javaType = " + var3.getName() + "\n" + "xmlElement = " + var4 + "\n" + "useMTOMmessage = " + var4 + "\n"));
         }

         if (registry.isWellKnownElement(var3, var4.getQName(), var5)) {
            registry.serializeElement(var1, var2, var3, var4.getQName(), this, var5, var6);
         } else {
            this.marshalOptions.setMarshaller(new XOPByteArrayMarshallerImpl(this.getMessage()));
            this.internalSerializeElement(var1, var2, var4, var3);
         }

      }
   }

   protected void v91serializeXmlObjects(boolean var1, boolean var2, boolean var3, SOAPElement var4, Class var5, Object var6, QName var7) throws XmlException {
      if (var3) {
         throw new XmlException("  ERROR: " + var5.getName() + ", is an array of XmlBeans.  Arrays of XmlBeans are not supported " + "in 9.1 style XmlBean marshalling");
      } else {
         if (var1 && !var2) {
            SOAPElement var8 = this.createWrappedXBeanTopElement(var4, var7);
            this.serializeXmlObject(var8, var6);
         } else {
            this.serializeXmlObject(var4, var6);
         }

      }
   }

   protected abstract void marshalType(XMLStreamWriter var1, Object var2, QName var3, XmlTypeName var4, Class var5) throws XMLStreamException, XmlException;

   protected abstract void marshalElement(XMLStreamWriter var1, Object var2, XmlTypeName var3, Class var4) throws XMLStreamException, XmlException;

   private void internalSerializeType(SOAPElement var1, Object var2, QName var3, XmlTypeName var4, Class var5) throws XMLStreamException, XmlException {
      XMLStreamWriter var6 = this.getStaxDomWriter(var1);
      this.marshalType(var6, var2, var3, var4, var5);
      var6.close();
   }

   protected XMLStreamWriter getStaxDomWriter(SOAPElement var1) {
      return Loader.createDOMWriter((ElementBase)var1, PushToTopNSHandler.getInstance());
   }

   private void internalSerializeElement(SOAPElement var1, Object var2, XmlTypeName var3, Class var4) throws XMLStreamException, XmlException {
      XMLStreamWriter var5 = this.getStaxDomWriter(var1);
      this.marshalElement(var5, var2, var3, var4);
      var5.close();
   }

   public void serializeXmlObject(SOAPElement var1, Object var2) throws XmlException {
      try {
         Object var3 = (Node)var2.getClass().getMethod("getDomNode").invoke(var2);
         if (((Node)var3).getNodeType() == 9) {
            var3 = ((Document)var3).getDocumentElement();
         }

         var1.appendChild(this.getDocument().importNode((Node)var3, true));
      } catch (NoSuchMethodException var4) {
         throw new XmlException("Unable to find getDomNode() method on XmlObject", var4);
      } catch (IllegalAccessException var5) {
         throw new XmlException("Unable to access getDomNode() method on XmlObject", var5);
      } catch (InvocationTargetException var6) {
         throw new XmlException("Unable to invoke getDomNode() method on XmlObject", var6);
      }
   }

   protected SOAPElement createWrappedXBeanTopElement(SOAPElement var1, QName var2) throws XmlException {
      SOAPElement var3 = null;

      try {
         String var4 = var2.getPrefix();
         if (var4 == null || var4.equals("")) {
            var4 = var1.getPrefix();
         }

         var3 = var1.addChildElement(var2.getLocalPart(), var4, var2.getNamespaceURI());
         return var3;
      } catch (SOAPException var5) {
         throw new XmlException(" could not create child element '" + var2 + "' for Wrapped XMLBean operation '" + var1 + "'  ", var5);
      }
   }

   public static MarshalOptions createDefaultMarshalOptions() {
      MarshalOptions var0 = new MarshalOptions();
      var0.setStaxWriterToNode(new WlsStaxDomWriter());
      return var0;
   }

   private static final class WlsStaxDomWriter implements StaxWriterToNode {
      private WlsStaxDomWriter() {
      }

      public Node getCurrentNode(XMLStreamWriter var1) {
         if (var1 instanceof XMLDomWriter) {
            XMLDomWriter var3 = (XMLDomWriter)var1;
            return var3.getCurrentNode();
         } else if (var1 instanceof DOMStreamWriter) {
            DOMStreamWriter var2 = (DOMStreamWriter)var1;
            return var2.getCurrentNode();
         } else {
            throw new RuntimeException("unexpected writer type: " + var1.getClass().getName());
         }
      }

      // $FF: synthetic method
      WlsStaxDomWriter(Object var1) {
         this();
      }
   }
}
