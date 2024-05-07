package weblogic.t3.srvr;

import java.util.List;
import weblogic.server.servicegraph.Service;
import weblogic.server.servicegraph.ServiceDependencies;
import weblogic.t3.srvr.servicegroups.ServerServiceDependencies;

public interface ServerServices {
   String STANDBY_STATE = "standby_state";
   String ADMIN_STATE = "admin_state";
   ServiceDependencies WLS_DEPENDENCIES = ServerServiceDependencies.getInstance();
   String[] SERVICE_CLASS_NAMES = WLS_DEPENDENCIES.getServiceClassNames();
   List<Service> services = WLS_DEPENDENCIES.getOrderedServices();
   Service[] serviceArray = new Service[0];
   Service[] ORDERED_SUBSYSTEM_LIST = (Service[])((Service[])services.toArray(serviceArray));
}
