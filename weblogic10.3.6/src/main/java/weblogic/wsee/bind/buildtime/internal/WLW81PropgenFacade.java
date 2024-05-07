package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.internal.bts.BindingType;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.QNameProperty;
import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.staxb.buildtime.internal.facade.DefaultPropgenFacade;
import com.bea.staxb.buildtime.internal.facade.Java2SchemaContext;
import com.bea.staxb.buildtime.internal.facade.PropgenFacade;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JElement;
import com.bea.xbean.xb.xsdschema.Attribute;
import com.bea.xbean.xb.xsdschema.LocalElement;
import javax.xml.namespace.QName;

public class WLW81PropgenFacade extends DefaultPropgenFacade implements PropgenFacade {
   public WLW81PropgenFacade(Java2SchemaContext var1, JElement var2, QNameProperty var3, String var4, LocalElement var5) {
      super(var1, var2, var3, var4, var5);
   }

   public WLW81PropgenFacade(Java2SchemaContext var1, JElement var2, QNameProperty var3, String var4, Attribute var5) {
      super(var1, var2, var3, var4, var5);
   }

   public void setType(JClass var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("null class");
      } else {
         this.assertResolved(var1);
         this.assertNotFinished();
         this.mType = var1;
         if (var1.isArrayType()) {
            JClass var2 = var1.getArrayComponentType();
            this.mBtsProp.setBindingType(this.mJ2sContext.findOrCreateBindingTypeFor(var2));
         }

         if (!this.checkSetType_char(var1)) {
            this.setType(this.getQnameFor(var1));
         }

         BindingType var7 = this.mJ2sContext.findOrCreateBindingTypeFor(var1);
         this.mBtsProp.setBindingType(var7);
         String var3 = this.mBtsProp.getQName().getNamespaceURI();
         BindingTypeName var4 = var7.getName();
         XmlTypeName var5 = var4.getXmlName();
         String var6 = var5.getQName().getNamespaceURI();
         this.mJ2sContext.checkNsForImport(var3, var6);
      }
   }

   protected boolean checkSetType_char(JClass var1) {
      this.assertResolved(var1);
      if (!var1.getQualifiedName().equals(CHAR) && !var1.getQualifiedName().equals(CHARACTER)) {
         return false;
      } else {
         this.setType(new QName(this.mXsTargetNamespace, var1.getSimpleName()));
         return true;
      }
   }
}
