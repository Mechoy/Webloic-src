package weblogic.management.scripting;

public interface NMConstants {
   String NM_HOST = "localhost";
   String NM_NMTYPE = "ssl";
   String PLAIN = "plain";
   String SSL = "ssl";
   String SSH = "ssh";
   String RSH = "rsh";
   String SHELL = "shell";
   String VMM = "vmm-";
   String VMM_SECURE = "vmms-";
   int NM_LISTEN_PORT_PLAIN_SSL = 5556;
   int NM_SSH_PORT = 22;
   int NM_RSH_PORT = 514;
   int NM_VMM_PORT = 8888;
   int NM_VMM_SECURE_PORT = 4443;
   String NM_DOMAIN_NAME = "mydomain";
   String NM_SERVER_NAME = "myserver";
}
