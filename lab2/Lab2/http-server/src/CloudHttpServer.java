import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudHttpServer {

    public static void main(String[] args) throws IOException {

        int port = 0;
        int threads = 0;

        // 遍历命令行参数
        for (int i = 0; i < args.length; i++) {
            // 如果当前参数是"--ip"，则下一个参数是IP地址
            // 如果当前参数是"--port"，则下一个参数是端口号
            if (args[i].equals("--port") && i < args.length - 1) {
                port = Integer.parseInt(args[i + 1]);
            }
            // 如果当前参数是"--threads"，则下一个参数是线程数
            else if (args[i].equals("--threads") && i < args.length - 1) {
                threads = Integer.parseInt(args[i + 1]);
            }
        }



        //int port=args[0];
        ServerSocket serverSocket=new ServerSocket(port);
        ExecutorService pool = Executors.newFixedThreadPool(threads);//线程池
        while (true){

            Socket accept = serverSocket.accept();
            pool.execute(new MyRunnable(accept));


        }



    }
}
