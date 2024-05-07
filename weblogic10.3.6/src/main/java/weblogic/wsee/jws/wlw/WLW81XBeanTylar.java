package weblogic.wsee.jws.wlw;

import com.bea.staxb.buildtime.internal.bts.BindingFile;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
import com.bea.staxb.buildtime.internal.bts.BindingType;
import com.bea.staxb.buildtime.internal.bts.BindingTypeName;
import com.bea.staxb.buildtime.internal.bts.WrappedArrayType;
import com.bea.staxb.buildtime.internal.joust.CompilingJavaOutputStream;
import com.bea.staxb.buildtime.internal.joust.JavaOutputStream;
import com.bea.staxb.buildtime.internal.tylar.ExplodedTylarImpl;
import com.bea.staxb.buildtime.internal.tylar.Tylar;
import com.bea.staxb.buildtime.internal.tylar.TylarWriter;
import com.bea.util.jam.JamClassLoader;
import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import weblogic.wsee.util.ExceptionUtil;

public class WLW81XBeanTylar implements Tylar, TylarWriter {
   private ExplodedTylarImpl delegate;

   private WLW81XBeanTylar(ExplodedTylarImpl var1) {
      this.delegate = var1;
   }

   public static WLW81XBeanTylar create(File var0) throws IOException {
      CompilingJavaOutputStream var1 = new CompilingJavaOutputStream();
      var1.setSourceDir(var0);
      return new WLW81XBeanTylar(ExplodedTylarImpl.create(var0, var1));
   }

   public BindingFile[] getBindingFiles() {
      return this.delegate.getBindingFiles();
   }

   public SchemaDocument[] getSchemas() {
      return this.delegate.getSchemas();
   }

   public BindingLoader getBindingLoader() {
      return this.delegate.getBindingLoader();
   }

   public SchemaTypeLoader getSchemaTypeLoader() throws IOException, XmlException {
      return this.delegate.getSchemaTypeLoader();
   }

   public JamClassLoader getJamClassLoader() {
      return this.delegate.getJamClassLoader();
   }

   public String getDescription() {
      return this.delegate.getDescription();
   }

   public URL[] getLocations() {
      return this.delegate.getLocations();
   }

   public URL getLocation() {
      return this.delegate.getLocation();
   }

   public void writeBindingFile(BindingFile var1) throws IOException {
      BindingFile var2 = new BindingFile();
      Iterator var3 = var1.bindingTypes().iterator();

      while(var3.hasNext()) {
         BindingType var4 = (BindingType)var3.next();
         if (var4 instanceof WrappedArrayType) {
            BindingTypeName var5 = ((WrappedArrayType)var4).getItemType();
            String var6 = var5.getJavaName().getClassName();
            int var7 = var6.indexOf("[]");
            String var8 = var6;
            if (var7 != -1) {
               var8 = var6.substring(0, var7);
            }

            if (ExceptionUtil.classNameIsSchemaBuiltin(var8)) {
               var2.addBindingType(var4, false, true);
               var2.addBindingType(var4, true, false);
            }
         }
      }

      this.delegate.writeBindingFile(var2);
   }

   public void writeSchema(SchemaDocument var1, String var2, Map var3) throws IOException {
   }

   public void writeSchemaTypeSystem(SchemaTypeSystem var1) throws IOException {
   }

   public JavaOutputStream getJavaOutputStream() {
      return this.delegate.getJavaOutputStream();
   }

   public void close() throws IOException {
      this.delegate.close();
   }
}
