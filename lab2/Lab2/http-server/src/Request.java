

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;


//对请求报文进行封装
public class Request {
    private String method;  //请求方式
    private String URI; //请求资源
    private Map<String, List<String>> parameterMapValue;//请求参数

    public static final String CRLF = "\r\n";
    private InputStream inputStream;
    private String requestInfo;     //用来保存请求报文数据

    private List<String> httpList;

    public Request() {
        method = "";
        URI = "";
        parameterMapValue = new HashMap<>();
        requestInfo = "";
        httpList=new ArrayList<String>();
    }

    public Request(InputStream inputStream) {
        this();
        this.inputStream = inputStream;
        try{
            byte []data = new byte[2048];
            int len = inputStream.read(data);
            requestInfo = new String(data,0,len+1);
        }catch (IOException e){
            return;
        }
        parseRequestInfo();

    }

    public String getRequestInfo(){
        return requestInfo;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return URI;
    }

    public Map<String, List<String>> getParameterMapValue() {
        return parameterMapValue;
    }
    //根据参数名称获取对应的多个值
    public String[] getParameters(String parameter){
        List<String> values = null;
        if((values = parameterMapValue.get(parameter)) == null){
            return null;
        }else{
            return values.toArray(new String[0]);
        }

    }

    //根据参数名称获取对应的单个值
    public String getParameter(String parameter){
        String[] values = getParameters(parameter);
        if(values == null)
            return null;
        return values[0];
    }



    //分析请求信息
    private void parseRequestInfo(){
        if((null == requestInfo) || (requestInfo = requestInfo.trim()).equals(""))
            return;
        /**
         * ====================================
         * 从信息的首行分解出：请求方式  请求路径  请求参数（get可能存在）
         *   如：GET /index.html?uname=intputUname&pwd=inputPassword HTTP/1.1
         *
         * 如果为post方式，请求参数在最后正文中
         * ====================================
         */

        String paramString = "";//接收请求参数

        //获取请求方式
        //首部行  GET /index?name=hangover&password=123 HTTP/1.1
        String firstLine = requestInfo.substring(0,requestInfo.indexOf(CRLF));
        int idx = requestInfo.indexOf("/"); // /的位置
            this.method = firstLine.substring(0,idx).trim();
        String urlStr = firstLine.substring(idx,firstLine.indexOf("HTTP")).trim();

        //      POST /index HTTP/1.1
        if(this.method.equals("POST")){
            this.URI = urlStr;
            paramString = requestInfo.substring(requestInfo.lastIndexOf(CRLF)).trim();
        }else if(this.method.equals("GET")){
            //考虑没有参数
            if(urlStr.contains("?")){
                String []urlArray = urlStr.split("\\?");
                this.URI = urlArray[0];
                paramString = urlArray[1];
            }else{
                this.URI = urlStr;
            }
        }
        parseParams(paramString);

    }

    //将请求参数封装到Map中
    //  name=shmily&password=123456
    private void parseParams(String paramString){
        //分割，将字符串转成数组

        StringTokenizer stringTokenizer = new StringTokenizer(paramString, "&");
        while (stringTokenizer.hasMoreTokens()){
            String keyValue = stringTokenizer.nextToken();
            String[] keyValues = keyValue.split("=");

            //System.out.println(keyValue);
            //System.out.println(Arrays.toString(keyValues));


            //考虑name=shmily&password
            if(keyValues.length == 1){
                keyValues = Arrays.copyOf(keyValues,2); //申请两个空间，并复制原来的
                keyValues[1] = null;
            }

            String key = keyValues[0].trim();
            String value = null == keyValues[1] ?null:decode(keyValues[1].trim(),"utf-8");

            //考虑name=shmily&name=hangover
            if(!parameterMapValue.containsKey(key)){
                parameterMapValue.put(key,new ArrayList<>());
            }
            List<String> values = parameterMapValue.get(key);
            //System.out.println(key);
            values.add(value);


        }
    }
    //解决中文
    private String decode(String value,String code){
        try{
            return java.net.URLDecoder.decode(value,code);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return null;
    }


}

