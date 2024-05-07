package weblogic.wsee.bind;

import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import java.io.IOException;
import java.util.ArrayList;

public class BaseTypeLoaderFactory {
   public static SchemaTypeLoader newInstance(Tylar var0) throws IOException, XmlException {
      ArrayList var1 = new ArrayList(2);
      var1.add(XmlBeans.typeLoaderForClassLoader(SchemaDocument.class.getClassLoader()));
      if (var0 != null) {
         var1.add(var0.getSchemaTypeLoader());
      }

      SchemaTypeLoader var2 = XmlBeans.typeLoaderUnion((SchemaTypeLoader[])var1.toArray(new SchemaTypeLoader[0]));
      return var2;
   }
}
