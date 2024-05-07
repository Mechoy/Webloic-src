package weblogic.wsee.bind.buildtime.internal;

import com.bea.xbean.xb.xsdschema.SchemaDocument;
import com.bea.xml.SchemaTypeSystem;
import com.bea.xml.XmlException;
import java.io.File;
import weblogic.wsee.bind.buildtime.S2JBindingsBuilder;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.wsdl.WsdlDefinitions;

public class XmlBeansBindingsBuilderImpl extends XmlBeansBaseBindingsBuilderImpl implements S2JBindingsBuilder {
   private static final boolean VERBOSE = Verbose.isVerbose(XmlBeansBindingsBuilderImpl.class);

   public XmlBeansBindingsBuilderImpl() {
      if (VERBOSE) {
         Verbose.log((Object)"Constructed a XmlBeansBindingsBuilderImpl");
      }

   }

   protected XmlBeansBaseBuildtimeBindings createBuildtimeBindings(SchemaTypeSystem var1, SchemaDocument[] var2, WsdlDefinitions var3, File var4) throws XmlException {
      return new XmlBeansBuildtimeBindings(var1, var2);
   }

   protected boolean shouldCompile() {
      return true;
   }
}
