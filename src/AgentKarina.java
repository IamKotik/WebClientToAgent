import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
public class AgentKarina extends Agent{
    @Override
    protected void setup(){
        addBehaviour(new OneShotBehaviour(){
            @Override
            public void action(){
                //отправляем сообщение другому агенту
                ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
                msg1.setContent("Karina");
                msg1.addReceiver(new AID("callwsagent",AID.ISLOCALNAME));
                send(msg1);
            }

        });
    }
}
