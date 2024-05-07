package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.BindingFile;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingType;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.staxb.buildtime.internal.bts.SoapArrayType;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xml.SchemaLocalAttribute;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlObject;
import com.bea.xml.soap.SOAPArrayType;
import com.bea.xml.soap.SchemaWSDLArrayType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.wsee.util.Verbose;

class SoapArrayHelper {
   private static final boolean VERBOSE = Verbose.isVerbose(SoapArrayHelper.class);
   private static final QName SOAPARRAY_TYPE = new QName("http://schemas.xmlsoap.org/soap/encoding/", "arrayType");
   private static String[] PRIMITIVE_TYPES = new String[]{"int", "boolean", "float", "long", "double", "short", "byte", "char"};
   private static String[] BOXED_TYPES = new String[]{"java.lang.Integer", "java.lang.Boolean", "java.lang.Float", "java.lang.Long", "java.lang.Double", "java.lang.Short", "java.lang.Byte", "java.lang.Character"};
   private static String SOAPARRAY_NAME_PREFIX = "ArrayOf";
   private BindingLoader mBindings;
   private SchemaTypeLoader mSchemaTypes;
   private BindingFile mBindingFile = null;

   static boolean isSoapArray(SchemaType var0) {
      while(var0 != null) {
         String var1 = XmlTypeName.forSchemaType(var0).toString();
         if (var1.equals("t=Array@http://schemas.xmlsoap.org/soap/encoding/") || var1.startsWith("t=Array@http://www.w3.org/") && var1.endsWith("/soap-encoding")) {
            return true;
         }

         var0 = var0.getBaseType();
      }

      return false;
   }

   public SoapArrayHelper(SchemaTypeLoader var1, BindingLoader var2, BindingFile var3) {
      this.mBindings = var2;
      this.mSchemaTypes = var1;
      this.mBindingFile = var3;
   }

   SoapArrayType createSoapArrayType(SchemaType var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null stype");
      } else if (var1.getName() == null) {
         throw new IllegalArgumentException("null stype.getName()");
      } else {
         if (VERBOSE) {
            Verbose.banner("createSoapArrayType for " + var1);
         }

         XmlTypeName var2 = XmlTypeName.forSchemaType(var1);
         XmlTypeName var3 = soapArrayTypeName(var1);
         XmlTypeName var4 = var3.getOuterComponent();
         if (VERBOSE) {
            Verbose.log((Object)("soapArrayTypeName = " + var3));
            Verbose.log((Object)("itemTypeName = " + var4));
         }

         BindingTypeName var5 = this.mBindings.lookupPojoFor(var4);
         if (var5 == null) {
            if (VERBOSE) {
               Verbose.log((Object)("  itemBtn for '" + var4 + "' is NULL create one and add it."));
            }

            QName var6 = var4.getQName();
            SchemaType var7 = this.mSchemaTypes.findType(var6);
            if (var7 == null) {
               throw new IllegalStateException("Array item type is not understood." + var4);
            }

            if (!isSoapArray(var7)) {
               throw new IllegalStateException("Array item type is not understood." + var4);
            }

            SoapArrayType var8 = this.createSoapArrayType(var7);
            var5 = var8.getName();
            this.mBindingFile.addBindingType(var8, false, true);
         }

         if (VERBOSE) {
            Verbose.log((Object)("itemBtn = " + var5));
         }

         JavaTypeName var14 = JavaTypeName.forArray(var5.getJavaName(), 1);
         BindingTypeName var15 = BindingTypeName.forPair(var14, var2);
         SOAPArrayType var16 = getWsdlArrayType(var1);
         BindingType var9 = this.mBindings.getBindingType(var5);
         if (VERBOSE) {
            Verbose.log((Object)("targetJavaName = " + var14));
            Verbose.log((Object)("soapType = " + var16));
            Verbose.log((Object)("targetBtn = " + var15));
            Verbose.log((Object)("itemType = " + var9));
         }

         if (var5 == null) {
            throw new IllegalStateException("null itemBtn");
         } else if (var9 == null) {
            throw new IllegalStateException("null itemType");
         } else {
            SoapArrayType var10 = new SoapArrayType(var15);
            if (var16 != null) {
               var10.setRanks(var16.getDimensions().length);
            } else {
               var10.setRanks(1);
            }

            SchemaProperty[] var11 = var1.getElementProperties();
            boolean var12 = var11.length == 1;
            if (var12) {
               var10.setItemName(var11[0].getName());
               var10.setItemNillable(var11[0].hasNillable() != 0);
            }

            if (var9 != null) {
               if (var12) {
                  if (var10.isItemNillable()) {
                     var9 = this.findBoxedType(var9);
                  }
               } else {
                  JavaTypeName var13 = this.getBoxedName(var9.getName().getJavaName());
                  if (var13 == null) {
                     var10.setItemNillable(true);
                  } else {
                     var10.setItemNillable(false);
                  }
               }

               var10.setItemType(var9.getName());
            }

            return var10;
         }
      }
   }

   List createSoapArrayType(JavaTypeName var1, SchemaType var2) {
      if (VERBOSE) {
         Verbose.banner("createSoapArrayType for " + var2);
      }

      ArrayList var3 = new ArrayList();
      XmlTypeName var4 = XmlTypeName.forSchemaType(var2);
      XmlTypeName var5 = soapArrayTypeName(var2);
      XmlTypeName var6 = var5.getOuterComponent();
      int var7 = var5.getSoapArrayRankAt(1);
      int var8 = var1.getArrayDepth();
      if (VERBOSE) {
         Verbose.log((Object)("schemaType        = " + var2));
         Verbose.log((Object)("array Dimension   = " + var8));
         Verbose.log((Object)("soapArrayTypeName = " + var5));
         Verbose.log((Object)("soapArrayTypeName rank = '" + var7 + "'"));
         Verbose.log((Object)("itemTypeName      = " + var6));
      }

      JavaTypeName var9 = var1.getArrayItemType(var8);
      SOAPArrayType var10 = getWsdlArrayType(var2);
      int[] var11 = var10 == null ? new int[0] : var10.getRanks();
      int var12 = var11.length - 1;
      int var13 = var12 < 0 ? 1 : var11[var12];
      int var14 = var8 - var13;
      if (VERBOSE) {
         Verbose.log((Object)(" soapType from WsdlArrayType was " + (var10 == null ? "" : "not ") + "NULL. \n" + "ranks.length = '" + var11.length + "', rankIndex = '" + var12 + "', arrayRank = '" + var13 + "', subArrayDimension = arrayDimension - arrayRank = '" + var14 + "'."));
      }

      if (var6.getComponentType() == 121) {
         if (VERBOSE) {
            Verbose.log((Object)("\n process itemTypeName '" + var6 + "' as XmlTypName.SOAP_ARRAY type."));
         }

         if (var14 > 0) {
            var9 = var1.getArrayTypeOfDimension(var14);
         }
      } else {
         if (VERBOSE) {
            Verbose.log((Object)("\n process itemTypeName '" + var6 + "' as a NON XmlTypName.SOAP_ARRAY type."));
         }

         JavaTypeName var15 = this.computeItemJavaNameFromSoapArrayRank(var5, var1);
         if (var15 != null) {
            if (VERBOSE) {
               Verbose.log((Object)("check whether we should use the alternate item java name '" + var15 + "' instead."));
            }

            if (var15.getArrayDepth() != var9.getArrayDepth()) {
               var9 = var15;
               if (VERBOSE) {
                  Verbose.log((Object)(" using alternate item java name '" + var15 + "'"));
               }
            } else if (VERBOSE) {
               Verbose.log((Object)(" using default item java name '" + var9 + "'"));
            }
         }

         if (VERBOSE) {
            Verbose.log((Object)(" itemJavaName will be '" + var9 + "'"));
         }
      }

      BindingTypeName var21 = BindingTypeName.forPair(var9, var6);
      BindingTypeName var16 = BindingTypeName.forPair(var1, var4);
      SoapArrayType var17 = new SoapArrayType(var16);
      if (VERBOSE) {
         Verbose.log((Object)("javaName = " + var1));
         Verbose.log((Object)("soapType = " + var10));
         Verbose.log((Object)("targetBtn = " + var16));
         Verbose.log((Object)("itemBtn = " + var21));
      }

      var17.setName(var16);
      var17.setItemType(var21);
      if (var10 != null) {
         var17.setRanks(var10.getDimensions().length);
      } else {
         var17.setRanks(1);
      }

      boolean var18 = false;
      SchemaProperty[] var19 = var2.getElementProperties();
      boolean var20 = var19.length == 1;
      if (var20) {
         var17.setItemName(var19[0].getName());
         var18 = var19[0].hasNillable() != 0;
         var17.setItemNillable(var18);
      }

      var3.add(var17);
      if (var6.getComponentType() == 121 && var14 > 0) {
         --var12;
         this.createSoapArrayType(var3, var6, var9, var11, var12, var18);
      }

      return var3;
   }

   private JavaTypeName computeItemJavaNameFromSoapArrayRank(XmlTypeName var1, JavaTypeName var2) {
      if (var1 == null) {
         return null;
      } else if (var2 == null) {
         return null;
      } else {
         XmlTypeName var3 = var1.getOuterComponent();
         QName var4 = var3.getQName();
         String var5 = var4.getLocalPart();
         int var6 = var1.getSoapArrayRankAt(1);
         int var7 = var2.getArrayDepth();
         if (VERBOSE) {
            Verbose.log((Object)(" computeItemJavaNameFromSoapArrayRank:  arrayTypeName '" + var1 + "', arrayJavaName '" + var2 + "', arrayDimension '" + var7 + "', soapArrayRank '" + var6 + "'"));
         }

         assert var6 > 0;

         if (var6 <= 0) {
            return null;
         } else if (var7 < var6) {
            if (VERBOSE) {
               Verbose.log((Object)(" unexpected condition !   arrayDimension = '" + var7 + "' is less than the soapArrayRank = '" + var6 + "' abandoning alternate java itemArray type computation"));
            }

            return null;
         } else {
            int var8 = var7 - var6;
            JavaTypeName var9 = var2.getArrayItemType(var7);
            String var10 = "";

            for(int var11 = 0; var11 < var8; ++var11) {
               var10 = var10 + "[]";
            }

            String var12 = var9.getClassName() + var10;
            if (VERBOSE) {
               Verbose.log((Object)(" computeJavaNameFromArrayOfItemName   returning JavaTypeName for java class: '" + var12 + "', JTN = '" + JavaTypeName.forString(var12) + "'"));
            }

            return JavaTypeName.forString(var12);
         }
      }
   }

   private String printIntArray(int[] var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append(" int[" + var3 + "] is '" + var1[var3] + "',  ");
      }

      return var2.toString();
   }

   private void createSoapArrayType(List var1, XmlTypeName var2, JavaTypeName var3, int[] var4, int var5, boolean var6) {
      int var7 = var5 < 0 ? 1 : var4[var5];
      int var8 = var3.getArrayDepth() - var7;
      JavaTypeName var9 = var3.getArrayTypeOfDimension(var8);
      XmlTypeName var10 = var2.getOuterComponent();
      if (VERBOSE) {
         Verbose.log((Object)(" createSoapArrayType   typeName is '" + var2 + "' javaName is '" + var3 + "', itemTypeName is '" + var10 + "', itemJavaName is '" + var9 + "', ranks is '" + var7 + "'"));
      }

      BindingTypeName var11 = BindingTypeName.forPair(var3, var2);
      BindingTypeName var12 = BindingTypeName.forPair(var9, var10);
      SoapArrayType var13 = new SoapArrayType(var11);
      var13.setName(var11);
      var13.setItemNillable(var6);
      var13.setItemType(var12);
      var13.setRanks(var7);
      var1.add(var13);
      if (var8 > 0) {
         --var5;
         this.createSoapArrayType(var1, var10, var9, var4, var5, var6);
      }

   }

   void checkArrayItem(SoapArrayType var1) {
      BindingType var2 = this.mBindings.getBindingType(var1.getItemType());
      if (var2 == null) {
         throw new IllegalStateException("For ArrayType '" + var1 + "', Array item type " + var2 + " is not mapped in jaxrpc mapping file.");
      } else {
         if (var1.isItemNillable()) {
            var2 = this.findBoxedType(var2);
            var1.setItemType(var2.getName());
         }

      }
   }

   private JavaTypeName getBoxedName(JavaTypeName var1) {
      for(int var2 = 0; var2 < PRIMITIVE_TYPES.length; ++var2) {
         if (PRIMITIVE_TYPES[var2].equals(var1.toString())) {
            return JavaTypeName.forString(BOXED_TYPES[var2]);
         }
      }

      return null;
   }

   private BindingType findBoxedType(BindingType var1) {
      BindingTypeName var2 = var1.getName();
      JavaTypeName var3 = var2.getJavaName();
      BindingType var4 = null;
      JavaTypeName var5 = this.getBoxedName(var3);
      if (var5 != null) {
         BindingTypeName var6 = BindingTypeName.forPair(var5, var2.getXmlName());
         var4 = this.mBindings.getBindingType(var6);
         if (var4 != null) {
            return var4;
         } else {
            throw new IllegalStateException();
         }
      } else {
         return var1;
      }
   }

   private static XmlTypeName soapArrayTypeName(SchemaType var0) {
      SOAPArrayType var1 = getWsdlArrayType(var0);
      if (var1 != null) {
         if (VERBOSE) {
            Verbose.log((Object)(" wsdlArrayType is not NULL, returning XmlTypeName.forSoapArrayType = '" + XmlTypeName.forSoapArrayType(var1) + "'"));
         }

         return XmlTypeName.forSoapArrayType(var1);
      } else {
         if (VERBOSE) {
            Verbose.log((Object)" wsdlArrayType is NULL, get soapArrayType name by other means.");
         }

         SchemaType var2 = XmlObject.type;
         SchemaProperty[] var3 = var0.getElementProperties();
         if (var3.length == 1) {
            var2 = var3[0].getType();
         }

         return XmlTypeName.forNestedNumber('y', 1, XmlTypeName.forSchemaType(var2));
      }
   }

   private static SOAPArrayType getWsdlArrayType(SchemaType var0) {
      SchemaLocalAttribute var1 = var0.getAttributeModel().getAttribute(SOAPARRAY_TYPE);
      return var1 != null ? ((SchemaWSDLArrayType)var1).getWSDLArrayType() : null;
   }
}
