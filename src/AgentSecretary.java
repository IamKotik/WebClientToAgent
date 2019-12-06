import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import javax.swing.*;
import java.sql.*;

public class AgentSecretary extends Agent {
    String name = "McCoy, Mr. Bernard";
    String sex = "male", str, answer;
    String passid;
    private static ACLMessage msgFWS;
    private MessageTemplate mt;
    private AID[] workers = {new AID("w1", AID.ISLOCALNAME),
            new AID("w2", AID.ISLOCALNAME), new AID("w3", AID.ISLOCALNAME)};
    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                //отправляем сообщение другому агенту
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent(passid);
                while(msg.getContent() == null){
                    try {
                        Class.forName("org.postgresql.Driver");
                        Connection con = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "asdrt555");
                        //System.out.println("соединились с бд");
                        PreparedStatement st = con.prepareStatement("SELECT \"PassengerId\" from public.\"InitialData\" where \"Sex\"=? and \"Name\"=?");
                        st.setString(1, sex);
                        st.setString(2, name);
                        ResultSet rs = st.executeQuery();
                        rs.next();
                        passid = rs.getString(1);
                        msg.setContent(passid);
                        //System.out.println(passid);
                        con.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < workers.length; i++) {
                    msg.addReceiver(workers[i]);
                }
                myAgent.send(msg);
                ACLMessage msgWS = new ACLMessage(ACLMessage.INFORM);
                    msgWS.setContent(sex);
                    msgWS.setConversationId("from-sec-to-ws");
                    msgWS.addReceiver(new AID("ws",AID.ISLOCALNAME));
                    myAgent.send(msgWS);

                mt = MessageTemplate.MatchConversationId("from-ws-to-sec");
                msgFWS = myAgent.receive(mt);
                if (msgFWS != null){
                    str = msgFWS.getContent();
                    answer = str;
                    if(answer.equals("1.0")){
                        JOptionPane.showMessageDialog(null, "Пассажир "+name+": "+answer+" выжил");

                    }else {
                        JOptionPane.showMessageDialog(null, "Пассажир "+name+": "+answer+" не выжил");
                    }
                }else block();
            }
        });
    }
}
