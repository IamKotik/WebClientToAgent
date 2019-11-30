import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.tools.DummyAgent.DummyAgent;

import javax.swing.*;

public class AgentResponse extends Agent {
    @Override
    protected void setup() {
        /*DummyAgent dm = new DummyAgent();
        dm.blockingReceive();*/
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                // получаем сообщение от другого агента
                ACLMessage msg = receive();
                if (msg == null){
                    waitUntilStarted();
                }else {System.out.println("Что-то пошло нее так");
                    JOptionPane.showMessageDialog(null, "Сообщение получено: "+msg.getContent());}
            }
        });
    }

}