package weblogic.common;

import weblogic.io.common.IOServicesDef;
import weblogic.jdbc.common.JdbcServicesDef;
import weblogic.time.common.TimeServicesDef;

public interface T3ServicesDef {
   String NAME = "weblogic.common.T3Services";

   AdminServicesDef admin();

   JdbcServicesDef jdbc();

   LogServicesDef log();

   NameServicesDef name();

   IOServicesDef io();

   TimeServicesDef time();
}
