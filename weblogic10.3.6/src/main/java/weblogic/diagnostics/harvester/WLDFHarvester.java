package weblogic.diagnostics.harvester;

import com.bea.adaptive.harvester.Harvester;
import java.io.IOException;
import java.util.List;

public interface WLDFHarvester extends Harvester {
   String[] getSupportedNamespaces();

   String getDefaultNamespace();

   void validateNamespace(String var1) throws InvalidHarvesterNamespaceException;

   String[][] getKnownHarvestableTypes(String var1, String var2) throws IOException;

   List<String> getKnownHarvestableInstances(String var1, String var2, String var3) throws IOException;
}
