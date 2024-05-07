package weblogic.wsee.bind.buildtime;

import com.bea.util.jam.JClass;
import com.bea.xml.XmlException;
import java.io.File;
import java.io.IOException;
import javax.xml.namespace.QName;
import weblogic.wsee.bind.buildtime.internal.TylarJ2SBindingsBuilderImpl;

public interface J2SBindingsBuilder {
   void addBaseTypeLibrary(ClassLoader var1);

   void javaTypeToSchemaType(JClass var1, JClass var2, int var3);

   void javaTypeToSchemaElement(JClass var1, JClass var2, QName var3, int var4);

   void wrapJavaTypeToSchemaElement(JClass var1, JClass[] var2, String[] var3, QName var4);

   void setXmlObjectClassLoader(ClassLoader var1);

   ClassLoader getXmlObjectClassLoader();

   BuildtimeBindings createBuildtimeBindings(File var1) throws IOException, XmlException;

   public static class Factory {
      public static J2SBindingsBuilder newInstance() {
         return new TylarJ2SBindingsBuilderImpl();
      }
   }
}
