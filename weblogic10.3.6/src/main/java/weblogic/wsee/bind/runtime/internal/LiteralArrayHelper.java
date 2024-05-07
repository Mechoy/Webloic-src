package weblogic.wsee.bind.runtime.internal;

import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.JavaTypeName;
import com.bea.staxb.buildtime.internal.bts.WrappedArrayType;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeLoader;
import java.math.BigInteger;

public class LiteralArrayHelper {
   private BindingLoader mBindings;
   private SchemaTypeLoader mSchemaTypes;

   public LiteralArrayHelper(SchemaTypeLoader var1, BindingLoader var2) {
      this.mBindings = var2;
      this.mSchemaTypes = var1;
   }

   private void p(String var1) {
      System.out.println(" [LiteralArrayHelper] " + var1);
   }

   private static void pStatic(String var0) {
      System.out.println(" [LiteralArrayHelper] " + var0);
   }

   WrappedArrayType createLiteralArrayType(SchemaType var1) {
      SchemaType var2 = getLiteralArrayItemType(var1);
      XmlTypeName var3 = XmlTypeName.forSchemaType(var2);
      BindingTypeName var4 = this.mBindings.lookupPojoFor(var3);
      if (var4 == null) {
         throw new IllegalStateException("Literal Array " + var1 + " item type " + var3 + " is not found.");
      } else {
         return this.createLiteralArrayType(var4.getJavaName(), var1, true);
      }
   }

   WrappedArrayType createLiteralArrayType(JavaTypeName var1, SchemaType var2) {
      return this.createLiteralArrayType(var1, var2, false);
   }

   private WrappedArrayType createLiteralArrayType(JavaTypeName var1, SchemaType var2, boolean var3) {
      if (var2 == null) {
         throw new IllegalArgumentException("null stype");
      } else {
         SchemaType var4 = getLiteralArrayItemType(var2);
         XmlTypeName var5 = XmlTypeName.forSchemaType(var4);
         JavaTypeName var6 = var1;
         if (!var3) {
            var6 = var1.getArrayTypeMinus1Dim(var1.getArrayDepth());
         }

         BindingTypeName var7 = BindingTypeName.forPair(var6, var5);
         XmlTypeName var8 = XmlTypeName.forSchemaType(var2);
         JavaTypeName var9 = JavaTypeName.forArray(var7.getJavaName(), 1);
         BindingTypeName var10 = BindingTypeName.forPair(var9, var8);
         WrappedArrayType var11 = new WrappedArrayType(var10);
         SchemaProperty[] var12 = var2.getProperties();
         boolean var13 = var12[0].hasNillable() != 0;
         var11.setItemNillable(var13);
         var11.setItemType(var7);
         var11.setItemName(var12[0].getName());
         return var11;
      }
   }

   public static boolean isLiteralArray(SchemaType var0) {
      return getLiteralArrayItemType(var0) != null;
   }

   public static SchemaType getLiteralArrayItemType(SchemaType var0) {
      if (!var0.isSimpleType() && var0.getContentType() != 2) {
         SchemaProperty[] var1 = var0.getProperties();
         if (var1.length == 1 && !var1[0].isAttribute()) {
            BigInteger var2 = var1[0].getMaxOccurs();
            if (var2 != null && var2.compareTo(BigInteger.ONE) <= 0) {
               return null;
            } else {
               SchemaType var3 = var1[0].getType();
               return var1[0].getType();
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }
}
