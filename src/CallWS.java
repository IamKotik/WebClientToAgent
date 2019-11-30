import jade.content.OntoACLMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.webservice.dynamicClient.*;
import java.net.URI;
import jade.core.behaviours.OneShotBehaviour;


public class CallWS extends Agent {
    private static ACLMessage msg;
    private static ACLMessage msg1;
    private WSData input = new WSData();
    private WSData s;
    @Override
    protected void setup() {
        addBehaviour(new OneShotBehaviour() {

            @Override
            public void action() {
                try{
                    DynamicClient dc = new DynamicClient();
                    dc.initClient(new URI("https://machinelearningservice.herokuapp.com/soap_service/?wsdl"));
                    //https://api.direct.yandex.com/v5/campaigns?wsdl

                    msg1 = receive();
                    msg = receive();
                    input.setParameter("name1", ""+msg1.getContent());
                    input.setParameter("name2", ""+msg.getContent());
            /*input.setParameter("p1", 8.2f);
            input.setParameter("p2", 8.3f);
            input.setParameter("p3", 8.4f);
            input.setParameter("p4", 8.5f);*/
                    WSData output = dc.invoke("hello", input);
                    s = output;
                    //System.out.println(s.toString().substring(s.toString().indexOf("=")+1,s.toString().lastIndexOf(" ")));
                    String k = s.toString().substring(s.toString().indexOf("=")+1,s.toString().indexOf(" "));
                    ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                    msg2.setContent(k);
                    msg2.addReceiver(new AID("response",AID.ISLOCALNAME));
                    send(msg2);
                    //System.out.println(output);
                } catch(Exception e){
                    e.printStackTrace();
                }

            };
        });


    }


}
