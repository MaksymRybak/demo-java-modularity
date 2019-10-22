public class Main {

  public static void main(String... args) throws Exception {
    sun.security.x509.X500Name name = new sun.security.x509.X500Name("CN=user");
  }
}

/*
 * compile with java8 to see java warning, compile with java9 to see java error
 */
