import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

import javax.swing.*;
import java.sql.*;

public class AgentW1 extends Agent{
    private String mess, str1, str2;
    @Override
    protected void setup(){
        addBehaviour(new CyclicBehaviour(){
            @Override
            public void action(){
                ACLMessage msg = myAgent.receive();
                if (msg != null){
                    mess = msg.getContent();
                    //JOptionPane.showMessageDialog(null, "Сообщение получено: "+mess);
                    try {
                        Class.forName("org.postgresql.Driver");
                        Connection con = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "asdrt555");
                        //System.out.println("соединились с бд");
                        PreparedStatement st = con.prepareStatement("SELECT \"Age\" , \"Embarked\" from public.\"ExtraData\" where \"PassengerId\"=?");
                        st.setInt(1, Integer.parseInt(mess));
                        ResultSet rs = st.executeQuery();
                        con.close();
                        //System.out.println(rs.toString());
                        while(rs.next()){
                            str1 = rs.getString(1);
                            str2 = rs.getString(2);
                            //System.out.println(str1+", "+str2);
                        }
                        System.out.println(str1+" "+str2);
                        ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
                        msg1.setContent(str1+" "+str2);
                        msg1.setConversationId("from-w1-to-ws");
                        msg1.addReceiver(new AID("ws",AID.ISLOCALNAME));
                        send(msg1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else block();
            }

        });

    }
}
