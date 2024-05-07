package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.WildcardUtil;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.BuiltinBindingLoader;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.tylar.CompositeTylar;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.staxb.runtime.BindingContext;
import com.bea.staxb.runtime.BindingContextFactory;
import com.bea.staxb.runtime.EncodingStyle;
import com.bea.staxb.runtime.MarshalOptions;
import com.bea.staxb.runtime.internal.BindingContextFactoryImpl;
import com.bea.xml.SchemaGlobalElement;
import com.bea.xml.SchemaParticle;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlException;
import com.bea.xml.XmlRuntimeException;
import java.io.IOException;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Element;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.RuntimeBindings;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.ToStringWriter;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.ws.WsMethod;
import weblogic.wsee.ws.WsParameterType;
import weblogic.wsee.ws.WsType;
import weblogic.xml.dom.DOMStreamReaderExt;

public class RuntimeBindingsImpl implements RuntimeBindings {
   protected static final boolean mVerbose = Verbose.isVerbose(RuntimeBindingsImpl.class);
   private static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
   private final BindingContext mBindingContext;
   private BindingLoader mBindingLoader;
   private com.bea.xbeanmarshal.runtime.BindingContext mXBeanBindingContext;
   private com.bea.xbeanmarshal.buildtime.internal.bts.BindingLoader mXBeanBindingLoader;
   private final SchemaTypeLoader mSchemaTypeLoader;

   public RuntimeBindingsImpl() {
      this.mBindingContext = this.getDefaultXbeanContext();
      this.mBindingLoader = BuiltinBindingLoader.getInstance();
      this.mXBeanBindingLoader = null;
      this.mSchemaTypeLoader = null;
   }

   public RuntimeBindingsImpl(Tylar[] var1) throws IOException, XmlException {
      if (mVerbose) {
         Verbose.log((Object)("Constructing WlsBindingProvider from: " + var1.length + " tylars"));
      }

      CompositeTylar var2 = new CompositeTylar(var1);
      if (mVerbose) {
      }

      this.mSchemaTypeLoader = var2.getSchemaTypeLoader();
      this.mBindingLoader = var2.getBindingLoader();
      this.mBindingContext = ((BindingContextFactoryImpl)BindingContextFactoryImpl.newInstance()).createBindingContext(var2);
   }

   public void setXmlBeansBindingLoader(com.bea.xbeanmarshal.buildtime.internal.bts.BindingLoader var1) {
      assert var1 != null;

      this.mXBeanBindingLoader = var1;
      com.bea.xbeanmarshal.runtime.internal.BindingContextFactoryImpl var10001 = (com.bea.xbeanmarshal.runtime.internal.BindingContextFactoryImpl)com.bea.xbeanmarshal.runtime.internal.BindingContextFactoryImpl.newInstance();
      this.mXBeanBindingContext = com.bea.xbeanmarshal.runtime.internal.BindingContextFactoryImpl.createBindingContext(var1);
   }

   public DeserializerContext createDeserializerContext(int var1, Element var2, boolean var3) {
      try {
         switch (var1) {
            case 0:
               return new LiteralDeserializerContext(this.mBindingContext, this.mXBeanBindingContext, this.mSchemaTypeLoader, var3);
            case 1:
               return new EncodedDeserializerContext(this.mBindingContext, this.mSchemaTypeLoader, EncodingStyle.SOAP11, var2, var3);
            case 2:
               return new EncodedDeserializerContext(this.mBindingContext, this.mSchemaTypeLoader, EncodingStyle.SOAP12, var2, var3);
            default:
               throw new AssertionError("unknown encoding: " + var1);
         }
      } catch (XmlException var5) {
         throw new XmlRuntimeException(var5);
      }
   }

   public SerializerContext createSerializerContext(int var1, MarshalOptions var2) {
      try {
         switch (var1) {
            case 0:
               return new LiteralSerializerContext(this.mBindingContext, this.mXBeanBindingContext, this.mSchemaTypeLoader, var2);
            case 1:
               return new EncodedSerializerContext(this.mBindingContext, this.mSchemaTypeLoader, EncodingStyle.SOAP11, var2);
            case 2:
               return new EncodedSerializerContext(this.mBindingContext, this.mSchemaTypeLoader, EncodingStyle.SOAP12, var2);
            default:
               throw new AssertionError("unknown encoding: " + var1);
         }
      } catch (XmlException var4) {
         throw new XmlRuntimeException(var4);
      }
   }

   public SerializerContext createSerializerContext(int var1) {
      return this.createSerializerContext(var1, BaseSerializerContext.DEFAULT_MARSHAL_OPTIONS);
   }

   public int elementIsSingleWildcard(XmlTypeName var1) {
      SchemaGlobalElement var2 = this.mSchemaTypeLoader.findElement(var1.getQName());
      if (var2 == null) {
         throw new IllegalArgumentException("GlobalElement " + var1 + " doesn't exist in the schema.");
      } else {
         SchemaType var3 = var2.getType();
         return WildcardUtil.schemaTypeIsSingleWildcard(var3);
      }
   }

   public QName getLocalElementName(XmlTypeName var1, String var2) {
      SchemaGlobalElement var3 = this.mSchemaTypeLoader.findElement(var1.getQName());
      if (var3 == null) {
         throw new IllegalArgumentException("GlobalElement " + var1 + " doesn't exist in the schema.");
      } else {
         SchemaType var4 = var3.getType();
         SchemaProperty[] var5 = var4.getElementProperties();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            String var7 = var5[var6].getName().getLocalPart();
            if (var7.equals(var2)) {
               if (var5[var6].getMaxOccurs() != null && var5[var6].getMaxOccurs().intValue() <= 1) {
                  return var5[var6].getName();
               }

               return var1.getQName();
            }
         }

         throw new IllegalArgumentException("no local element named '" + var2 + "' on global element " + var1);
      }
   }

   public XmlTypeName getLocalElementType(XmlTypeName var1, String var2) {
      SchemaGlobalElement var3 = this.mSchemaTypeLoader.findElement(var1.getQName());
      if (var3 == null) {
         throw new IllegalArgumentException("GlobalElement " + var1 + " doesn't exist in the schema.");
      } else {
         SchemaType var4 = var3.getType();
         SchemaProperty[] var5 = var4.getElementProperties();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            String var7 = var5[var6].getName().getLocalPart();
            if (var7.equals(var2)) {
               if (var5[var6].getMaxOccurs() != null && var5[var6].getMaxOccurs().intValue() <= 1) {
                  return XmlTypeName.forSchemaType(var5[var6].getType());
               }

               return XmlTypeName.forSchemaType(var4);
            }
         }

         throw new IllegalArgumentException("no local element named '" + var2 + "' on global element " + var1);
      }
   }

   public QName getTypeForElement(QName var1) {
      if (var1 == null) {
         throw new IllegalArgumentException(" unexpected: input param elementQName is null");
      } else {
         SchemaGlobalElement var2 = this.mSchemaTypeLoader.findElement(var1);
         if (var2 == null) {
            return null;
         } else {
            SchemaType var3 = var2.getType();
            return var3 == null ? null : var3.getName();
         }
      }
   }

   public SchemaType getSchemaTypeForXmlTypeName(XmlTypeName var1) {
      return var1.findTypeIn(this.mSchemaTypeLoader);
   }

   public boolean isSimpleType(QName var1) {
      if (var1 == null) {
         throw new IllegalArgumentException(" unexpected: input param typeQName is null");
      } else if (var1.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema")) {
         return true;
      } else {
         SchemaType var2 = this.mSchemaTypeLoader.findType(var1);
         return var2.isSimpleType();
      }
   }

   public String getJavaType(XmlTypeName var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null xmlType");
      } else {
         BindingTypeName var2 = this.mBindingLoader.lookupPojoFor(var1);
         if (var2 == null) {
            String var3 = this.lookupXmlObjectFor(var1);
            if (var3 != null) {
               return var3;
            } else {
               if (mVerbose) {
                  Verbose.log((Object)("Unknown xmlType " + var1 + " -- using String instead"));
                  Verbose.log((Object)("Using Binding Provider: " + this.toString()));
               }

               return "java.lang.String";
            }
         } else {
            return var2.getJavaName().toString();
         }
      }
   }

   public boolean isOptionalLocalElement(XmlTypeName var1, String var2, WsMethod var3, WsType var4) {
      SchemaGlobalElement var5 = this.mSchemaTypeLoader.findElement(var1.getQName());
      if (var5 == null) {
         throw new IllegalArgumentException("GlobalElement " + var1 + " doesn't exist in the schema.");
      } else {
         SchemaType var6 = var5.getType();
         int var7 = this.elementIsSingleWildcard(var1);
         if (var7 != 1 && var7 != 2) {
            if (StringUtil.isEmpty(var2) && WildcardUtil.WILDCARD_CLASSNAMES.contains(var4.getJavaType().getCanonicalName())) {
               if (var4 instanceof WsParameterType) {
                  int var13 = -1;
                  Iterator var14 = var3.getParameters();

                  while(var14.hasNext()) {
                     WsType var16 = (WsType)var14.next();
                     if (var16 == var4) {
                        ++var13;
                        break;
                     }

                     if (StringUtil.isEmpty(var16.getName()) && WildcardUtil.WILDCARD_CLASSNAMES.contains(var16.getJavaType().getCanonicalName())) {
                        ++var13;
                     }
                  }

                  if (var13 > -1 && var6.getContentModel() != null) {
                     SchemaParticle[] var15 = var6.getContentModel().getParticleChildren();
                     if (var15 != null) {
                        int var17 = 0;

                        for(int var11 = 0; var11 < var15.length; ++var11) {
                           if (var15[var11].countOfParticleChild() == 0 && var15[var11].getParticleType() == 5) {
                              if (var17 == var13) {
                                 return var15[var11].getIntMinOccurs() < 1;
                              }

                              ++var17;
                           }
                        }
                     }
                  }
               }

               return false;
            }

            SchemaProperty[] var12 = var6.getElementProperties();

            for(int var9 = 0; var9 < var12.length; ++var9) {
               String var10 = var12[var9].getName().getLocalPart();
               if (var10.equals(var2)) {
                  if (var12[var9].getMaxOccurs() != null && var12[var9].getMaxOccurs().intValue() <= 1) {
                     return var12[var9].getMinOccurs() != null && var12[var9].getMinOccurs().intValue() < 1;
                  }

                  return var5.getMinOccurs() != null && var5.getMinOccurs().intValue() < 1;
               }
            }
         } else {
            SchemaParticle var8 = var6.getContentModel();
            if (var8 != null && var8.countOfParticleChild() == 0 && var8.getParticleType() == 5) {
               if (var8.getMinOccurs() != null && var8.getMinOccurs().intValue() < 1) {
                  return true;
               }

               return false;
            }
         }

         return false;
      }
   }

   private void toString(ToStringWriter var1) {
      var1.start(this);
      var1.writeField("binding loader", this.mBindingLoader);
      var1.end();
   }

   public String toString() {
      ToStringWriter var1 = new ToStringWriter();
      this.toString(var1);
      return var1.toString();
   }

   private String lookupXmlObjectFor(XmlTypeName var1) {
      if (this.mSchemaTypeLoader == null) {
         return null;
      } else {
         SchemaType var2 = this.mSchemaTypeLoader.findType(var1.getQName());
         return var2 != null ? var2.getFullJavaName() : null;
      }
   }

   private BindingContext getDefaultXbeanContext() {
      return BindingContextFactory.newInstance().createBindingContext();
   }

   static XMLStreamReader createReader(SOAPElement var0) throws XMLStreamException {
      if (var0 == null) {
         throw new IllegalArgumentException("null SOAPElement");
      } else {
         return new DOMStreamReaderExt(var0);
      }
   }
}
