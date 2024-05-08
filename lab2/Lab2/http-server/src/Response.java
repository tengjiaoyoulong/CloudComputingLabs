


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.cert.CRL;
import java.util.Date;

//封装响应报文
public class Response {

    public static final String CRLF = "\t\n";
    public static final String BLANK = " ";

    //输出流
    private BufferedWriter bufferedWriter;
    //响应头
    private StringBuilder headInfo;
    //响应体
    private StringBuilder content;
    //响应体长度
    private int len = 0;
    //响应类型
    private String contentType = "text/html;charset=utf-8";



    public Response() {
        headInfo = new StringBuilder();
        content = new StringBuilder();
        len = 0;
    }

    public Response(OutputStream outputStream){
        this();
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
    }



    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    //构建响应头
    private void createHeadInfo(int code){
        //构建状态行     HTTP协议版本，状态代码，描述
        headInfo.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);
        switch (code){
            case 200:
                headInfo.append("OK");
                break;
            case 404:
                headInfo.append("Not Found");
                break;
            case 500:
                headInfo.append("Server Error");
                break;

        }
        headInfo.append(CRLF);

        //响应头
        headInfo.append("Server:test Server/0.0.1").append(CRLF);
        headInfo.append("Date").append(new Date()).append(CRLF);
        headInfo.append("Content-type:").append(contentType).append(CRLF);

        //正文长度
        headInfo.append("Content-Length:").append(len);
        headInfo.append("\n\n");

    }
    //构建响应体
    public Response print(String info){
        content.append(info);
        len += info.length();
        return this;
    }

    //构建响应体+回车
    public Response println(String info){
        content.append(info).append("\n");
        len += (info + "\n").getBytes().length;
        return this;
    }



    //推送到客户端
    public void pushToClient(int code) throws IOException{
        createHeadInfo(code);
        bufferedWriter.append(headInfo.toString());
        bufferedWriter.append(content.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}

