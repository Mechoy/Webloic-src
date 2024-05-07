package weblogic.wsee.bind.tylar;

import com.bea.staxb.buildtime.internal.bts.BindingFile;
import com.bea.staxb.buildtime.internal.bts.BindingLoader;
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
import java.util.Map;

public class CompilingTylar implements Tylar, TylarWriter {
   private ExplodedTylarImpl delegate;

   private CompilingTylar(ExplodedTylarImpl var1) {
      this.delegate = var1;
   }

   public static CompilingTylar create(File var0) throws IOException {
      CompilingJavaOutputStream var1 = new CompilingJavaOutputStream();
      var1.setSourceDir(var0);
      return new CompilingTylar(ExplodedTylarImpl.create(var0, var1, true));
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
      this.delegate.writeBindingFile(var1);
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
