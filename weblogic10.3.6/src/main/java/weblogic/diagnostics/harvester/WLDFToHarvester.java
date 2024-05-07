package weblogic.diagnostics.harvester;

import com.bea.adaptive.harvester.WatchedValues;
import java.util.Collection;

public interface WLDFToHarvester {
   int addWatchedValues(WatchedValues var1);

   Collection<WatchedValues.Validation> validateWatchedValues(WatchedValues var1) throws HarvesterException;

   WatchedValues createWatchedValues(String var1);

   void deleteWatchedValues(WatchedValues var1) throws HarvesterException;

   boolean isActivated();
}
