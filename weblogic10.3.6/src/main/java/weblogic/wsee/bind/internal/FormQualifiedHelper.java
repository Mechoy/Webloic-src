package weblogic.wsee.bind.internal;

import com.bea.staxb.buildtime.internal.bts.ByNameBean;
import com.bea.staxb.buildtime.internal.bts.QNameProperty;
import com.bea.xbean.schema.SchemaTypeImpl;
import com.bea.xml.SchemaProperty;
import com.bea.xml.SchemaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.namespace.QName;

public class FormQualifiedHelper {
   private static final FormQualifiedHelper INSTANCE = new FormQualifiedHelper();

   public static final FormQualifiedHelper getInstance() {
      return INSTANCE;
   }

   private FormQualifiedHelper() {
   }

   public SchemaProperty getElementProperty(SchemaType var1, String var2) {
      QName var3 = new QName(var2);
      SchemaProperty var4 = var1.getElementProperty(var3);
      if (var4 != null) {
         return var4;
      } else {
         var3 = new QName(this.getNamespaceFor(var1), var2);
         var4 = var1.getElementProperty(var3);
         if (var4 != null) {
            return var4;
         } else {
            SchemaProperty[] var5 = var1.getElementProperties();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               var3 = var5[var6].getName();
               if (var3 != null && var3.getLocalPart().equals(var2)) {
                  return var5[var6];
               }
            }

            return null;
         }
      }
   }

   public ArrayList<SchemaProperty> getElementProperties(SchemaType var1, String var2) {
      ArrayList var3 = new ArrayList();
      SchemaProperty[] var4 = var1.getElementProperties();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         QName var6 = var4[var5].getName();
         if (var6 != null && var6.getLocalPart().equals(var2)) {
            var3.add(var4[var5]);
         }
      }

      return var3;
   }

   public SchemaProperty getAttributeProperty(SchemaType var1, String var2) {
      QName var3 = new QName(var2);
      SchemaProperty var4 = var1.getAttributeProperty(var3);
      if (var4 != null) {
         return var4;
      } else {
         var3 = new QName(this.getNamespaceFor(var1), var2);
         var4 = var1.getAttributeProperty(var3);
         if (var4 != null) {
            return var4;
         } else {
            SchemaProperty[] var5 = var1.getAttributeProperties();

            for(int var6 = 0; var6 < var5.length; ++var6) {
               var3 = var5[var6].getName();
               if (var3 != null && var3.getLocalPart().equals(var2)) {
                  return var5[var6];
               }
            }

            return null;
         }
      }
   }

   public QNameProperty getPropertyForElement(ByNameBean var1, String var2) {
      QName var3 = new QName(var2);
      QNameProperty var4 = var1.getPropertyForElement(var3);
      if (var4 != null) {
         return var4;
      } else {
         QName var5 = var1.getName().getXmlName().getQName();
         if (var5 != null) {
            String var6 = var5.getNamespaceURI();
            var3 = new QName(var6, var2);
            var4 = var1.getPropertyForElement(var3);
         }

         if (var4 != null) {
            return var4;
         } else {
            Collection var9 = var1.getProperties();
            if (var9 == null) {
               return null;
            } else {
               Iterator var7 = var9.iterator();

               QNameProperty var8;
               do {
                  if (!var7.hasNext()) {
                     return null;
                  }

                  var8 = (QNameProperty)var7.next();
               } while(var8.isAttribute() || !var2.equals(var8.getQName().getLocalPart()));

               return var8;
            }
         }
      }
   }

   public QNameProperty getPropertyForAttribute(ByNameBean var1, String var2) {
      QName var3 = new QName(var2);
      QNameProperty var4 = var1.getPropertyForAttribute(var3);
      if (var4 != null) {
         return var4;
      } else {
         QName var5 = var1.getName().getXmlName().getQName();
         if (var5 != null) {
            String var6 = var5.getNamespaceURI();
            var3 = new QName(var6, var2);
            var4 = var1.getPropertyForAttribute(var3);
            if (var4 != null) {
               return var4;
            }
         }

         Collection var9 = var1.getProperties();
         if (var9 == null) {
            return null;
         } else {
            Iterator var7 = var9.iterator();

            QNameProperty var8;
            do {
               if (!var7.hasNext()) {
                  return null;
               }

               var8 = (QNameProperty)var7.next();
            } while(!var8.isAttribute() || !var2.equals(var8.getQName().getLocalPart()));

            return var8;
         }
      }
   }

   private String getNamespaceFor(SchemaType var1) {
      QName var2 = var1.getName();
      return var2 != null ? var2.getNamespaceURI() : ((SchemaTypeImpl)var1).getTargetNamespace();
   }
}
