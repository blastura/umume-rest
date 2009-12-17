;; (aj-get-classpath)

(set-variable 'jde-global-classpath
              '("./target/classes"
                "./target/test-classes"
                "./lib/test/junit-4.5.jar"
                "./lib/default/jersey-core-1.0.3.jar"
                "./lib/default/asm-3.1.jar"
                "./lib/default/grizzly-framework-1.9.9.jar"
                "./lib/default/grizzly-http-1.9.9.jar"
                "./lib/default/grizzly-http-servlet-1.9.9.jar"
                "./lib/default/grizzly-portunif-1.9.9.jar"
                "./lib/default/grizzly-rcm-1.9.9.jar"
                "./lib/default/grizzly-servlet-webserver-1.9.9.jar"
                "./lib/default/grizzly-utils-1.9.9.jar"
                "./lib/default/jersey-core-1.0.3.jar"
                "./lib/default/jersey-server-1.0.3.jar"
                "./lib/default/jsr311-api-1.0.jar"
                "./lib/default/logback-classic-0.9.17.jar"
                "./lib/default/logback-core-0.9.17.jar"
                "./lib/default/servlet-api-2.5.jar"
                "./lib/default/slf4j-api-1.5.8.jar"
                "./lib/test/asm-3.1.jar"
                "./lib/test/grizzly-framework-1.9.9.jar"
                "./lib/test/grizzly-http-1.9.9.jar"
                "./lib/test/grizzly-http-servlet-1.9.9.jar"
                "./lib/test/grizzly-portunif-1.9.9.jar"
                "./lib/test/grizzly-rcm-1.9.9.jar"
                "./lib/test/grizzly-servlet-webserver-1.9.9.jar"
                "./lib/test/grizzly-utils-1.9.9.jar"
                "./lib/test/jersey-client-1.0.3.jar"
                "./lib/test/jersey-core-1.0.3.jar"
                "./lib/test/jersey-server-1.0.3.jar"
                "./lib/test/jersey-test-framework-1.0.3.jar"
                "./lib/test/jsr311-api-1.0.jar"
                "./lib/test/junit-4.5.jar"
                "./lib/test/logback-classic-0.9.17.jar"
                "./lib/test/logback-core-0.9.17.jar"
                "./lib/test/servlet-api-2.5.jar"
                "./lib/test/slf4j-api-1.5.8.jar"
                ))
;;(shell-command "find lib -name *.jar")

(set-variable 'jde-sourcepath (mapcar 'jde-normalize-path
                                      (list "./src/main/java"
                                            "./src/test/java")))

(message "Loaded prj.el")
