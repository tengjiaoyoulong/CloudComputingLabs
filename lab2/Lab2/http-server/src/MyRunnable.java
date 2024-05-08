import java.io.IOException;
import java.net.Socket;

 public class MyRunnable implements Runnable{

    Socket socket;

    MyRunnable(Socket socket){
        this.socket=socket;
    }


    @Override
    public void run() {



        MyHandle myHandle = new MyHandle();
        try {

            myHandle.handle(socket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
