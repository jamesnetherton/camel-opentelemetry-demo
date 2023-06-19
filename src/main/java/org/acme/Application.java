package org.acme;

import org.apache.camel.main.Main;

public class Application {
    public static void main(String[] args) throws Exception {
        Main main = new Main(Application.class);
        main.run();
    }
}
