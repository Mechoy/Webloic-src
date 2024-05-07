package weblogic.wsee.workarea;

import javax.xml.namespace.QName;

public interface WorkAreaConstants {
   String WORK_NS = "http://bea.com/2004/06/soap/workarea/";
   String WORK_PREFIX = "work";
   String XML_TAG_WORK_CONTEXT = "WorkContext";
   String XML_TAG = "work:WorkContext";
   QName WORK_AREA_HEADER = new QName("http://bea.com/2004/06/soap/workarea/", "WorkContext", "work");
   QName[] WORK_HEADERS = new QName[]{WORK_AREA_HEADER};
}
