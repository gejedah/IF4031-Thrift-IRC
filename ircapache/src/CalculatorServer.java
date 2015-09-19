import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import praktikum.Prak2Service;

public class CalculatorServer{

    public static CalculatorHandler handler;
    public static Prak2Service.Processor processor;

    public static void main(String[] args){
        try{
            handler = new CalculatorHandler();
            processor = new Prak2Service.Processor(handler);

            Runnable simple = new Runnable(){
                public void run(){
                    simple(processor);
                }
            };

            new Thread(simple).start();

        }catch(Exception x){
            x.printStackTrace();
        }
    }

      public static void simple(Prak2Service.Processor processor){
        try{
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            System.out.println("Starting the simple server...");
            server.serve();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}