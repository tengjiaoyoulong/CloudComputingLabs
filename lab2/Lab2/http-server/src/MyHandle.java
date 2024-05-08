import java.io.*;
import java.net.Socket;

public class MyHandle {

    public void returnPage(OutputStream outputStream,String filePath,int code) throws IOException {

        Response response = new Response(outputStream);
        // 读取文件内容
        BufferedReader br1 = new BufferedReader(new FileReader(filePath));
        String line;
        int len=0;
        while ((line = br1.readLine()) != null) {
            len++;
        }
        BufferedReader br2 = new BufferedReader(new FileReader(filePath));
        int num=0;
        // 逐行读取文件内容
        while ((line = br2.readLine()) != null) {
            num++;
            if(num<len){

                response.println(line);}
            else {
                response.print(line);
            }
        }

        response.pushToClient(code);


    }

    public void getIndex(Socket socket) throws IOException {

        OutputStream outputStream = socket.getOutputStream();

        //../static/index.html"
        // 指定文件的路径
        String filePath = "../static/index.html";

        returnPage(outputStream,filePath,200);

        socket.close();
    }

    public void get404(Socket socket) throws IOException {

        OutputStream outputStream = socket.getOutputStream();

        String filePath = "../static/404.html";

        returnPage(outputStream,filePath,404);

        socket.close();

    }

    public void get501(Socket socket) throws IOException {

        OutputStream outputStream = socket.getOutputStream();

        String filePath = "../static/501.html";

        returnPage(outputStream,filePath,501);

        socket.close();

    }

    public void getData(Socket socket) throws IOException{

        OutputStream outputStream = socket.getOutputStream();

        String filePath = "../data/data.txt";

        returnPage(outputStream,filePath,200);

        socket.close();

    }

    public void postIdName(Socket socket,Request request) throws IOException {

        OutputStream outputStream = socket.getOutputStream();
        Response response = new Response(outputStream);

        String id=request.getParameter("id");
        String name=request.getParameter("name");

        //System.out.println(request.getRequestInfo());

        if(!id.isEmpty()&&!name.isEmpty()&&id.matches("[0-9]+")&&name.matches("[a-zA-Z0-9]+")){
            response.print("id="+id+"&name="+name);
            response.pushToClient(200);
            socket.close();
        }
        else {
            get404(socket);
        }



    }


    public void handle(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        Request request = new Request(inputStream);

        OutputStream outputStream = socket.getOutputStream();
        Response response = new Response(outputStream);

        //System.out.println(request.getRequestInfo());

        if(request.getMethod().equals("GET")){
            String url = request.getUrl();
            //System.out.println(url);

            if(url.equals("/index.html")||url.equals("/")){

                getIndex(socket);

            }
            else if(url.equals("/404.html")){

                get404(socket);

            }

            else if(url.equals("/501.html")){

                get501(socket);

            }

            else if(url.equals("/api/check")){

              getData(socket);

            }
            else {

                get404(socket);
            }

        }

        else if (request.getMethod().equals("POST")){
            String url = request.getUrl();
            if(url.equals("/api/echo")){

                postIdName(socket,request);

            }
            else {
                get404(socket);
            }
        }

        else {
           get501(socket);
        }
    }
}
