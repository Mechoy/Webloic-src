package weblogic.wsee.bind.buildtime.internal;

import com.bea.staxb.buildtime.internal.tylar.DefaultTylarLoader;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.xml.XmlException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import weblogic.wsee.util.ClassLoaderUtil;
import weblogic.wsee.util.Verbose;

public abstract class TylarBindingsBuilderBase extends BindingsBuilderBase {
   private static final boolean VERBOSE = Verbose.isVerbose(TylarBindingsBuilderBase.class);

   protected Tylar getBaseTypeLibraries() throws IOException, XmlException {
      return this.mClassLoaders != null && this.mClassLoaders.size() != 0 ? ((DefaultTylarLoader)DefaultTylarLoader.getInstance()).load(this.getTylarUris()) : null;
   }

   private URI[] getTylarUris() throws IOException {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.mClassLoaders.iterator();

      while(var2.hasNext()) {
         ClassLoader var3 = (ClassLoader)var2.next();
         var1.addAll(ClassLoaderUtil.getSourceURIs(var3, "META-INF/binding-file.xml"));
      }

      Verbose.log((Object)("URIs = " + var1));
      return (URI[])var1.toArray(new URI[var1.size()]);
   }
}
