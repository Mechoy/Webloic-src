package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.runtime.NodeFromStreamReader;
import com.bea.staxb.runtime.StreamReaderFromNode;
import com.bea.staxb.runtime.UnmarshalOptions;
import com.bea.xbean.store.Jsr173;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.types.TypeRegistry;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.XBeanUtil;
import weblogic.xml.dom.DOMStreamReaderExt;

abstract class BaseDeserializerContext extends BindingContextImpl implements DeserializerContext {
   private static final boolean mVerbose = Verbose.isVerbose(BaseDeserializerContext.class);
   private static final TypeRegistry registry = TypeRegistry.getInstance();
   protected UnmarshalOptions OPTIONS = null;
   protected MessageContext msgCtx;
   protected static boolean validateXml = false;

   public BaseDeserializerContext(SchemaTypeLoader var1, boolean var2) {
      super(var1);
      validateXml = var2;
      this.OPTIONS = createUnmarshalOptions();
   }

   public Object deserializeType(SOAPElement var1, Class var2, XmlTypeName var3, boolean var4) throws XmlException, XMLStreamException {
      if (var1 == null) {
         throw new IllegalArgumentException("null SOAPElement");
      } else if (var2 == null) {
         throw new IllegalArgumentException("null javaType");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null xmlType");
      } else {
         if (mVerbose) {
            Verbose.log((Object)("XML Beans loaded from: " + whereFrom(Jsr173.class)));
            Verbose.logArgs("xml root", var1.getNodeName(), "javaType", var2.getName(), "xmlType", var3, "isMTOMmessage", var4);
         }

         return registry.isWellKnownType(var2, var3.getQName(), var4) ? registry.deserializeType(var1, var2, var3.getQName(), this, var4) : this.internalDeserializeType(var1, var3, var2);
      }
   }

   public Object deserializeElement(SOAPElement var1, Class var2, XmlTypeName var3, boolean var4) throws XmlException, XMLStreamException {
      if (var1 == null) {
         throw new IllegalArgumentException("null SOAPElement");
      } else if (var2 == null) {
         throw new IllegalArgumentException("null javaType");
      } else if (var3 == null) {
         throw new IllegalArgumentException("null xmlType");
      } else {
         if (mVerbose) {
            Verbose.logArgs("xml root", var1, "javaType", var2.getName(), "xmlElement", var3, "isMTOMmessage", var4);
         }

         return registry.isWellKnownElement(var2, var3.getQName(), var4) ? registry.deserializeElement(var1, var2, var3.getQName(), this, var4) : this.internalDeserializeElement(var1, var3, var2);
      }
   }

   public Object deserializeWrappedElement(SOAPElement var1, Class var2, XmlTypeName var3, boolean var4) throws XmlException, XMLStreamException {
      return this.deserializeType(var1, var2, var3, var4);
   }

   protected abstract Object unmarshalType(XMLStreamReader var1, XmlTypeName var2, Class var3) throws XmlException;

   protected abstract Object unmarshalElement(XMLStreamReader var1, XmlTypeName var2, Class var3) throws XmlException;

   private static String whereFrom(Class var0) {
      String var1 = var0.getName();
      if (!var1.startsWith("/")) {
         var1 = "/" + var1;
      }

      var1 = var1.replace('.', '/');
      var1 = var1 + ".class";
      URL var2 = var0.getResource(var1);
      return var2.getFile();
   }

   private Object internalDeserializeType(SOAPElement var1, XmlTypeName var2, Class var3) throws XmlException, XMLStreamException {
      XMLStreamReader var4 = createReader(var1);
      Object var5 = this.unmarshalType(var4, var2, var3);
      if (mVerbose) {
         Verbose.log((Object)("result = " + var5));
      }

      return var5;
   }

   private Object internalDeserializeElement(SOAPElement var1, XmlTypeName var2, Class var3) throws XmlException, XMLStreamException {
      XMLStreamReader var4 = createReader(var1);
      Object var5 = this.unmarshalElement(var4, var2, var3);
      if (mVerbose) {
         Verbose.log((Object)("result = " + var5));
      }

      return var5;
   }

   private static XMLStreamReader createReader(SOAPElement var0) throws XMLStreamException {
      if (var0 == null) {
         throw new IllegalArgumentException("null SOAPElement");
      } else {
         return new DOMStreamReaderExt(var0);
      }
   }

   protected Object v91deserializeXmlObjects(boolean var1, boolean var2, boolean var3, SOAPElement var4, Class var5, QName var6) throws XmlException {
      if (var3) {
         throw new XmlException("  ERROR: " + var5.getName() + ", is an array of XmlBeans.  Arrays of XmlBeans are not supported " + "in 9.1 style XmlBean marshalling");
      } else {
         Object var7;
         if (var1) {
            if (var2) {
               var7 = this.deserializeXmlObject(var4, var5);
            } else {
               Element var8 = XBeanUtil.createXMLFragmentFromElement(var4);
               var7 = this.deserializeXmlObject(var8, var5);
            }
         } else {
            var7 = this.deserializeXmlObject(var4, var5);
         }

         return var7;
      }
   }

   public Object deserializeXmlObject(Element var1, Class var2) throws XmlException {
      try {
         Class[] var3 = var2.getDeclaredClasses();
         Class var4 = null;

         for(int var5 = 0; var5 < var3.length; ++var5) {
            if (var3[var5].getName().endsWith("Factory")) {
               var4 = var3[var5];
            }
         }

         if (var4 == null) {
            throw new XmlException("Unable to find Factory inner class for class:" + var2);
         } else {
            Method var9 = var4.getDeclaredMethod("parse", Node.class);
            return var9.invoke((Object)null, var1);
         }
      } catch (NoSuchMethodException var6) {
         throw new XmlException("Unable to find parse method on XmlObject", var6);
      } catch (IllegalAccessException var7) {
         throw new XmlException("Unable to access parse method on XmlObject", var7);
      } catch (InvocationTargetException var8) {
         throw new XmlException("Unable to invoke parse method on XmlObject", var8);
      }
   }

   protected static UnmarshalOptions createUnmarshalOptions() {
      UnmarshalOptions var0 = new UnmarshalOptions();
      var0.setValidation(validateXml);
      StreamReaderFromNode var1 = getStreamReaderFromNode();
      var0.setStreamReaderFromNode(var1);
      NodeFromStreamReader var2 = getNodeFromStreamReader();
      var0.setNodeFromStreamReader(var2);
      return var0;
   }

   protected static NodeFromStreamReader getNodeFromStreamReader() {
      return WLSDomNodeFromStreamReader.getInstance();
   }

   protected static StreamReaderFromNode getStreamReaderFromNode() {
      return GenericStreamReaderFromNode.getInstance();
   }

   public MessageContext getMessageContext() {
      return this.msgCtx;
   }

   public void setMessageContext(MessageContext var1) {
      this.msgCtx = var1;
   }
}
