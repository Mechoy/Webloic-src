package weblogic.deploy.api.model;

import java.io.IOException;
import javax.enterprise.deploy.model.DDBeanRoot;
import weblogic.descriptor.DescriptorBean;

public interface WebLogicDDBeanRoot extends DDBeanRoot, WebLogicDDBean {
   DescriptorBean getDescriptorBean() throws IOException;
}
