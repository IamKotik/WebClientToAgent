import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
public class AgentAlexa extends Agent{
    @Override
    protected void setup(){
        addBehaviour(new OneShotBehaviour(){
            @Override
            public void action(){
                //отправляем сообщение другому агенту
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("Alexa");
                msg.addReceiver(new AID("callwsagent",AID.ISLOCALNAME));
                send(msg);
            }

        });
    }
}
