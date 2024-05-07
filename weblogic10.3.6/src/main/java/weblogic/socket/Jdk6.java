package weblogic.socket;

import java.net.NetworkInterface;
import java.net.SocketException;

public class Jdk6 {
   public static int getMTU(NetworkInterface var0) throws SocketException {
      return var0.getMTU();
   }
}
