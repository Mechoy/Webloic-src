package weblogic.wsee.bind.types;

import com.bea.xml.XmlException;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.activation.DataHandler;
import javax.mail.internet.MimeMultipart;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;

public class TypeRegistry {
   private static final boolean verbose = Verbose.isVerbose(TypeRegistry.class);
   private static final String BYTEARRAY_CLASSNAME = byte[].class.getName();
   private static TypeRegistry registry = new TypeRegistry();
   private Map javaTypeMapping = new HashMap();

   private TypeRegistry() {
      if (verbose) {
         Verbose.log((Object)"Creating type registry");
      }

      this.register(Source.class, (QName)null, new SourceTypeMapper());
      this.register(Source[].class, (QName)null, new SourceArrayTypeMapper());
      this.register(DataHandler.class, (QName)null, new DataHandlerTypeMapper());
      this.register(DataHandler[].class, (QName)null, new DataHandlerArrayTypeMapper());
      this.register(Image.class, (QName)null, new ImageTypeMapper());
      this.register(Image[].class, (QName)null, new ImageArrayTypeMapper());
      this.register(MimeMultipart.class, (QName)null, new MimeMultipartTypeMapper());
      this.register(MimeMultipart[].class, (QName)null, new MimeMultipartArrayTypeMapper());
      this.register(SOAPElement.class, (QName)null, new SOAPElementTypeMapper());
      this.register(SOAPElement[].class, (QName)null, new SOAPElementArrayTypeMapper());
   }

   public static TypeRegistry getInstance() {
      return registry;
   }

   public void register(Class var1, QName var2, TypeMapper var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("javaType can not be null");
      } else if (var3 == null) {
         throw new IllegalArgumentException("mapper can not be null");
      } else {
         this.javaTypeMapping.put(var1, var3);
      }
   }

   public void serializeType(SOAPElement var1, Object var2, Class var3, QName var4, QName var5, SerializerContext var6, boolean var7, String var8) throws XmlException {
      TypeMapper var9 = this.getMapper(var3);
      var9.serializeType(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   private TypeMapper getMapper(Class var1) {
      TypeMapper var2 = (TypeMapper)this.javaTypeMapping.get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Unable to find type mapper for class: " + var1);
      } else {
         return var2;
      }
   }

   public Object deserializeType(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException {
      TypeMapper var6 = this.getMapper(var2);
      return var6.deserializeType(var1, var2, var3, var4, var5);
   }

   public boolean isWellKnownType(Class var1, QName var2, boolean var3) {
      if (verbose) {
         Verbose.logArgs("javaType", var1, "xmlType", var2, "useMTOMmessage", var3);
      }

      return var1.getName().equals(BYTEARRAY_CLASSNAME) && !var3 ? false : this.javaTypeMapping.containsKey(var1);
   }

   public boolean isWellKnownElement(Class var1, QName var2, boolean var3) {
      if (verbose) {
         Verbose.logArgs("javaType", var1, "xmlElement", var2, "useMTOMmessage", var3);
      }

      return var1.getName().equals(BYTEARRAY_CLASSNAME) && !var3 ? false : this.javaTypeMapping.containsKey(var1);
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

   public void serializeElement(SOAPElement var1, Object var2, Class var3, QName var4, SerializerContext var5, boolean var6, String var7) throws XmlException, XMLStreamException {
      TypeMapper var8 = this.getMapper(var3);
      if (var8 == null) {
         throw new AssertionError("Mapper can not be null");
      } else {
         var8.serializeElement(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   public Object deserializeElement(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException, XMLStreamException {
      TypeMapper var6 = this.getMapper(var2);
      if (var6 == null) {
         throw new AssertionError("Mapper can not be null");
      } else {
         return var6.deserializeElement(var1, var2, var3, var4, var5);
      }
   }
}
