package weblogic.diagnostics.harvester;

import weblogic.diagnostics.harvester.internal.MetricArchiver;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;

public class HarvesterService extends AbstractServerService {
   public void start() throws ServiceFailureException {
      MetricArchiver.getInstance().finalizeActivation();
   }
}
