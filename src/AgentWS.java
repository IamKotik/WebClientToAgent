import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.webservice.dynamicClient.DynamicClient;
import jade.webservice.dynamicClient.WSData;

import javax.swing.*;
import java.net.URI;

public class AgentWS extends Agent{
    private String sex, strw1,strsec,age,embarked, sibsp, parch, strw2, strw3, pclass, fare;
    private static ACLMessage msgw1, msgsec, msgw2, msgw3;
    private String[] arrw1, arrw2, arrw3;
    private WSData input = new WSData();
    private MessageTemplate mtw1, mtsec, mtw2, mtw3;
    private WSData s;
    @Override
    protected void setup() {
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                mtw1 = MessageTemplate.MatchConversationId("from-w1-to-ws");
                msgw1 = myAgent.receive(mtw1);
                if (msgw1 != null){
                    strw1 = msgw1.getContent();
                    arrw1=strw1.split(" ");
                    age=arrw1[0];
                    embarked=arrw1[1];
                }
                mtsec = MessageTemplate.MatchConversationId("from-sec-to-ws");
                msgsec = myAgent.receive(mtsec);
                if (msgsec != null){
                    strsec = msgsec.getContent();
                    sex=strsec;
                }
                mtw2 = MessageTemplate.MatchConversationId("from-w2-to-ws");
                msgw2 = myAgent.receive(mtw2);
                if (msgw2 != null){
                    strw2 = msgw2.getContent();
                    arrw2=strw2.split(" ");
                    sibsp=arrw2[0];
                    parch=arrw2[1];
                }
                mtw3 = MessageTemplate.MatchConversationId("from-w3-to-ws");
                msgw3 = myAgent.receive(mtw3);
                if (msgw3 != null){
                    strw3 = msgw3.getContent();
                    arrw3=strw3.split(" ");
                    pclass=arrw3[0];
                    fare=arrw3[1];
                }
                if (age != null && embarked != null && sex!=null && sibsp!=null && parch!=null && fare!=null && pclass != null){
                    System.out.println(age+" "+embarked+" "+sex+" "+sibsp+" "+parch+" "+pclass+" "+fare);
                    //JOptionPane.showMessageDialog(null, "Сообщение получено: AAAAAAAAAA"+mess[1]);
                    try {
                        DynamicClient dc = new DynamicClient();
                        dc.initClient(new URI("https://machinelearningservice.herokuapp.com/soap_service/?wsdl"));

                        String[] services = {"tree", "tree_ensemble", "regression", "neighbors", "ml", "neuron"};
                        input.setParameter("p_class", Float.parseFloat(pclass)); //класс
                        input.setParameter("age", Float.parseFloat(age)); //возраст
                        input.setParameter("sib_sp", Float.parseFloat(sibsp)); //братья/сёстры
                        input.setParameter("par_ch", Float.parseFloat(parch)); //дети/родители
                        input.setParameter("fare", Float.parseFloat(fare)); //стоимость билета
                        switch (sex) {
                            case "male": {
                                input.setParameter("sex_female", 0f); //жен.
                                input.setParameter("sex_male", 1f); //муж.
                                break;
                            }
                            case "female": {
                                input.setParameter("sex_female", 1f); //жен.
                                input.setParameter("sex_male", 0f); //муж.
                                break;
                            }
                        }
                        switch (embarked){
                            case "Q": {
                                input.setParameter("embarked_c", 0f); //место посадки: Шербург
                                input.setParameter("embarked_q", 1f); //место посадки: Квинстаун
                                input.setParameter("embarked_s", 0f); //место посадки: Саутгемптон
                                break;
                            }
                            case "C": {
                                input.setParameter("embarked_c", 1f); //место посадки: Шербург
                                input.setParameter("embarked_q", 0f); //место посадки: Квинстаун
                                input.setParameter("embarked_s", 0f); //место посадки: Саутгемптон
                                break;
                            }
                            case "S": {
                                input.setParameter("embarked_c", 0f); //место посадки: Шербург
                                input.setParameter("embarked_q", 0f); //место посадки: Квинстаун
                                input.setParameter("embarked_s", 1f); //место посадки: Саутгемптон
                                break;
                            }
                        }

                        WSData output = dc.invoke(services[0], input);
                        s = output;
                        String k = s.toString().substring(s.toString().indexOf("=")+1,s.toString().indexOf("Headers"));

                        ACLMessage msgS = new ACLMessage(ACLMessage.INFORM);
                        msgS.setContent(k.trim());
                        msgS.setConversationId("from-ws-to-sec");
                        msgS.addReceiver(new AID("ag",AID.ISLOCALNAME));
                        myAgent.send(msgS);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else block();

            }
        });

    }
    }

