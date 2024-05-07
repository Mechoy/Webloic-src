package weblogic.diagnostics.accessor;

import java.io.IOException;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import weblogic.diagnostics.debug.DebugLogger;

public class ExportSchemaEntityResolver implements EntityResolver {
   private static final String EXPORT_SCHEMA = "export.xsd";
   private static final DebugLogger accessorDebug = DebugLogger.getDebugLogger("DebugDiagnosticAccessor");

   public InputSource resolveEntity(String var1, String var2) throws SAXException, IOException {
      if (accessorDebug.isDebugEnabled()) {
         accessorDebug.debug("Resolving public Id " + var1);
         accessorDebug.debug("Resolving system Id " + var2);
      }

      return var1 != null && !var2.equals("http://www.bea.com/ns/weblogic/90/diagnostics/accessor/export.xsd export.xsd") ? null : new InputSource(this.getClass().getResourceAsStream("export.xsd"));
   }
}
