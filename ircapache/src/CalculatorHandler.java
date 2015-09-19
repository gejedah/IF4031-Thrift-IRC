import org.apache.thrift.TException;
import praktikum.*;

import java.sql.Timestamp;
import java.util.*;

public class CalculatorHandler implements Prak2Service.Iface
{
    HashMap dftr_channel;
    HashMap dftr_user;

    public CalculatorHandler(){
        dftr_channel=new HashMap();
        dftr_user=new HashMap();
    }

    @Override
    public boolean addUser(String nickname){
        if (dftr_user.containsKey(nickname)){
            return false;
        }
        else{
            dftr_user.put(nickname, new ArrayList<ctujuh>());
            return true;
        }
    }

    @Override
    public boolean joinChannel(String channelname, String nickname){
        if (dftr_channel.containsKey(channelname)){
            if (((clima) dftr_channel.get(channelname)).anggota_user.contains(nickname)){
                return false;
            }
            else{
                ((clima) dftr_channel.get(channelname)).anggota_user.add(nickname);
//                ((ctujuh) dftr_user.get(nickname)).channel.add(channelname);
                ((ArrayList<ctujuh>) dftr_user.get(nickname)).add(new ctujuh(channelname));
                return true;
            }
        }
        else{
            dftr_channel.put(channelname, new clima());
            ((clima) dftr_channel.get(channelname)).anggota_user.add(nickname);
//            ((ctujuh) dftr_user.get(nickname)).channel.add(channelname);
            ((ArrayList<ctujuh>) dftr_user.get(nickname)).add(new ctujuh(channelname));
            return true;
        }
    }

    @Override
    public boolean leaveChannel(String channelname, String nickname){
        if (dftr_channel.containsKey(channelname)){
            if (((clima) dftr_channel.get(channelname)).anggota_user.contains(nickname)){
                ((clima) dftr_channel.get(channelname)).anggota_user.remove(nickname);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    @Override
    public void exit(String nickname) throws TException{
//        ArrayList<String> temp_list_channel= ((ctujuh) dftr_user.get(nickname)).channel;
        ArrayList<ctujuh> temp_list_channel= (ArrayList<ctujuh>) dftr_user.get(nickname);

        for (int i = 0; i < temp_list_channel.size(); i++) {
            leaveChannel(((ctujuh) temp_list_channel.get(i)).channelname, nickname);
        }
        dftr_user.remove(nickname);
    }

    @Override
    public void sendChannel(String channelname, String nickname, String message) throws TException{
        if (dftr_channel.containsKey(channelname)){
            if (((clima) dftr_channel.get(channelname)).anggota_user.contains(nickname)){
                ((clima) dftr_channel.get(channelname)).messages.add(new cenam(nickname, message));
            }
        }
    }

    @Override
    public void sendAllChannel(String nickname, String message) throws TException{
//        ArrayList<String> temp_list_channel= ((ctujuh) dftr_user.get(nickname)).channel;
        ArrayList<ctujuh> temp_list_channel= (ArrayList<ctujuh>) dftr_user.get(nickname);
        for (int i = 0; i < temp_list_channel.size(); i++) {
            sendChannel(((ctujuh) temp_list_channel.get(i)).channelname, nickname, message);
        }
    }

    @Override
    public List<String> recvMessage(String nickname) throws TException{
//        ArrayList<String> temp_list_channel= ((ctujuh) dftr_user.get(nickname)).channel;
        ArrayList<ctujuh> temp_list_channel= (ArrayList<ctujuh>) dftr_user.get(nickname);
        System.out.println("Jumlah Channel "+temp_list_channel.size());
        List<String> ret_messages=new ArrayList<>();
        Timestamp waktu_terakhir;
        for (int i = 0; i < temp_list_channel.size(); i++) {
//            Timestamp waktu_terakhir= ((Timestamp) ((ctujuh) dftr_user.get(nickname)).waktu_terakhirs.get(temp_list_channel.get(i)));
            waktu_terakhir= temp_list_channel.get(i).waktu_terakhir;
//            ret_messages.addAll(((clima) dftr_channel.get(temp_list_channel.get(i))).getAfterMessages(waktu_terakhir, temp_list_channel.get(i)));
            ret_messages.addAll(((clima) dftr_channel.get(temp_list_channel.get(i).channelname)).getAfterMessages(waktu_terakhir, temp_list_channel.get(i).channelname));
            ((ArrayList<ctujuh>) dftr_user.get(nickname)).get(i).waktu_terakhir=new Timestamp(System.currentTimeMillis());
        }
        return ret_messages;
    }

}