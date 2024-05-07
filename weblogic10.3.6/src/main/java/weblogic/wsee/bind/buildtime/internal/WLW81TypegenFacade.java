package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.internal.bts.ByNameBean;
import com.bea.staxb.buildtime.internal.bts.QNameProperty;
import com.bea.staxb.buildtime.internal.facade.DefaultTypegenFacade;
import com.bea.staxb.buildtime.internal.facade.Java2SchemaContext;
import com.bea.staxb.buildtime.internal.facade.PropgenFacade;
import com.bea.staxb.buildtime.internal.facade.TypegenFacade;
import com.bea.util.jam.JElement;
import com.bea.xbean.xb.xsdschema.Attribute;
import com.bea.xbean.xb.xsdschema.ExtensionType;
import com.bea.xbean.xb.xsdschema.LocalElement;
import com.bea.xbean.xb.xsdschema.TopLevelComplexType;

public class WLW81TypegenFacade extends DefaultTypegenFacade implements TypegenFacade {
   public WLW81TypegenFacade(Java2SchemaContext var1, TopLevelComplexType var2, ExtensionType var3, String var4, ByNameBean var5) {
      super(var1, var2, var3, var4, var5);
   }

   protected PropgenFacade createPropgenFacade(Java2SchemaContext var1, JElement var2, QNameProperty var3, String var4, Attribute var5) {
      return new WLW81PropgenFacade(var1, var2, var3, var4, var5);
   }

   protected PropgenFacade createPropgenFacade(Java2SchemaContext var1, JElement var2, QNameProperty var3, String var4, LocalElement var5) {
      return new WLW81PropgenFacade(var1, var2, var3, var4, var5);
   }
}
