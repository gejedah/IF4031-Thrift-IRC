import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import praktikum.Prak2Service;

import java.util.*;

public class CalculatorClient {

    public static void main(String [] args) {
        try {
            TTransport transport;
            transport = new TSocket("localhost", 9090);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Prak2Service.Client client=new Prak2Service.Client(protocol);
            perform(client);
            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }

    private static void perform(Prak2Service.Client client) throws TException
    {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter your command: ");
        String cmd = in.next();
        String[] temp_cmd;
        String nickname=null;
        boolean akun=false;

        temp_cmd = cmd.split("[<>]");
        temp_cmd = Arrays.stream(temp_cmd).filter(s -> (s != null && s.length()>0)).toArray(String[]::new);

        while (temp_cmd[0].equalsIgnoreCase("nick") && !akun){
            if (client.addUser(temp_cmd[1])){
                nickname=temp_cmd[1];
                akun=true;
                System.out.println("Selamat, anda terdaftar sebagai "+ nickname);
            }
            else{
                System.out.println("Nickname yang dimasukkan sudah ada, input nickname baru!");
            }
            System.out.print("Enter your command: ");
            cmd = in.next();
            temp_cmd = cmd.split("[<>]");
            temp_cmd = Arrays.stream(temp_cmd).filter(s -> (s != null && s.length()>0)).toArray(String[]::new);
        }

        while (!akun){
            StringBuilder nick_default=new StringBuilder();
            char ch_random='a';
            Random random_int=new Random();
            while (ch_random <= 'z'){
                nick_default.append(ch_random);
                ch_random+=random_int.nextInt(5);
            }
            nickname=nick_default.toString();
            if (client.addUser(nickname)){
                akun=true;
                System.out.println("Selamat, anda terdaftar sebagai "+ nickname);
            }
        }

        while (!cmd.equalsIgnoreCase("exit")){
            temp_cmd = cmd.split("[<>]");
            temp_cmd = Arrays.stream(temp_cmd).filter(s -> (s != null && s.length()>0)).toArray(String[]::new);
/*            for (int i = 0; i < temp_cmd.length; i++) {
                System.out.println("Element ke-"+ i + " " +temp_cmd[i]);
            }*/
            if (temp_cmd[0].equalsIgnoreCase("nick")){
                System.out.println("Anda sudah memiliki akun "+ nickname + " , pendaftaran gagal");
            }
            else if (temp_cmd[0].equalsIgnoreCase("leave")){
                if (client.leaveChannel(temp_cmd[1],nickname)){
                    System.out.println("Anda berhasil meninggalkan channel "+temp_cmd[1]);
//                    System.out.println("Nickname anda "+ nickname);
                }
                else{
                    System.out.println("Channel tidak ada atau ada belum terdaftar pada channel yang bersangkutan");
//                    System.out.println("Nickname anda "+ nickname);
                }
            }
            else if (temp_cmd[0].equalsIgnoreCase("join")){
                if (client.joinChannel(temp_cmd[1],nickname)){
                    System.out.println("Anda berhasil join dengan channel "+temp_cmd[1]);
//                    System.out.println("Nickname anda "+ nickname);
                }
                else{
                    System.out.println("Kesalahan pada channelname atau anda sudah join dengan channel tersebut");
//                    System.out.println("Nickname anda "+ nickname);
                }
            }
            else if (temp_cmd[0].equalsIgnoreCase("@")){
                client.sendChannel(temp_cmd[1], nickname, temp_cmd[2]);
                System.out.println("Pesan telah berhasil dikirim");
//                System.out.println("Nickname anda " + nickname);
                List<String> new_message = client.recvMessage(nickname);
                if (new_message.size()>0){
                    System.out.println("Message baru adalah:");
                    int iter=new_message.size()-1;
                    while (iter>=0){
                        System.out.println(new_message.get(iter));
                        iter--;
                    }
                }
            }
            else{
                client.sendAllChannel(nickname, temp_cmd[0]);
                System.out.println("Pesan telah berhasil dikirim");
                System.out.println("Nikname anda " + nickname);
                List<String> new_message = new ArrayList<>();
                new_message.addAll(client.recvMessage(nickname));
                if (new_message.size()>0){
                    System.out.println("Message baru adalah:");
                    int iter=new_message.size()-1;
                    while (iter>=0){
                        System.out.println(new_message.get(iter));
                        iter--;
                    }
                }
            }
            System.out.print("Enter your command: ");
            cmd = in.next();
        }
        client.exit(nickname);

    }
}