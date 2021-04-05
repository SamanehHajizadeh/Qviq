package com.springboot.Qviq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.*;
import java.util.logging.Logger;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CrulTest {

    //https://medium.com/@prajwalsdharan/curl-in-java-d62a5e4c0f55
    //Curl is a networking tool used to transfer data between a server and the curl client using protocols like HTTP, FTP, TELNET, and SCP.
    private final static Logger logger = Logger.getLogger(Controller.class.getName());
    private final static String crulPathWIN = "C:/Program Files/Git/mingw64/bin";
    private final static String crulPathUBUNTU = "./i";

    @Test
    public void GetData() throws IOException, InterruptedException {
        String command =
                "curl -X GET http://localhost:8080//getLog/1";

        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));

        ProcessBuilder pb = new ProcessBuilder(crulPathUBUNTU);
        Process process = processBuilder.start();

       //we can get the InputStream by accessing it from the Process instance:
        InputStream inputStream = process.getInputStream();

        //When the processing is complete, we can get the exit code with:
        System.out.println("*" + process.isAlive());
        Thread.sleep(20000);

        System.out.println(process.exitValue());
        process.destroy();
    }


    @Test
    public void posTNewData() throws IOException, InterruptedException {
        String command =
                "curl -X POST http://localhost:8080/addNewMessage\n" +
                     /*   "//--header \"Content-Type: application/json\"\n" +*/
                        " -d \"{\n" +
                        "  \\\"name\\\": \\\"Arun\\\",\n" +
                        "  \\\"messageContent\\\": \\\"update Message1\\\"\n" +
                        "}";
        //--header "Content-Type: application/json""


        Process process = Runtime.getRuntime().exec(command);

        //we can get the InputStream by accessing it from the Process instance:
        InputStream inputStream = process.getInputStream();

        //When the processing is complete, we can get the exit code with:
        System.out.println("*" + process.isAlive());
        Thread.sleep(20000);
        System.out.println(process.exitValue());
        int exitCode = process.exitValue();

        process.destroy();
    }

    @Test
    public void PostDataViaJSONFile() throws IOException, InterruptedException {

        String url =
                " curl -vX POST http://localhost:8080/addNewMessage -d @testplace.json \\\n" +
                        "--header \"Content-Type: application/json\"" +
                        "" +
                        "";

        ProcessBuilder pb = new ProcessBuilder(
                "curl",
                "--silent",
                "--location",
                "--request",
                "POST",
                url,
                "--header",
                "Content-Type:application/JSON",
                "--data-urlencode",
                "inputParams=<{\n" +
                        "    \"name\": \"Mahsa\",\n" +
                        "    \"messageContent\" : \"Message for check!\"\n" +
                        "    }\n" +
                        "\n" +
                        "    >");

        // errorstream of the process will be redirected to standard output
        pb.redirectErrorStream(true);
        // start the process
        Process proc = pb.start();
        /* get the inputstream from the process which would get printed on
         * the console / terminal
         */
        InputStream ins = proc.getInputStream();
        // creating a buffered reader
        BufferedReader read = new BufferedReader(new InputStreamReader(ins));
        StringBuilder sb = new StringBuilder();
        read
                .lines()
                .forEach(line -> {
                    logger.info("line>" + line);
                    sb.append(line);
                });
        // close the buffered reader
        read.close();
        /*
         * wait until process completes, this should be always after the
         * input_stream of processbuilder is read to avoid deadlock
         * situations
         */
        proc.waitFor();
        /* exit code can be obtained only after process completes, 0
         * indicates a successful completion
         */
        int exitCode = proc.exitValue();
        logger.info("exit code::" + exitCode);
        // finally destroy the process
        proc.destroy();

    }

}