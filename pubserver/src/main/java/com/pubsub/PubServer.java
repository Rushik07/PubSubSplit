package com.pubsub;


import com.solace.services.core.model.SolaceServiceCredentials;
import com.solacesystems.jcsmp.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

@SpringBootApplication
@CommonsLog
public class PubServer {

    public static void main(String[] args) {
        SpringApplication.run(PubServer.class, args);
    }

    @Component
    static class Runner implements CommandLineRunner {

        //JCMPFactory is used to obtain instances of messaging system entities. Creating topic as well.
        private final Topic topic = JCSMPFactory.onlyInstance().createTopic("tutorial/topic");

        @Autowired
        private SpringJCSMPFactory solaceFactory;

        // Examples of other beans that can be used together to generate a customized SpringJCSMPFactory
        @Autowired(required=false) private SpringJCSMPFactoryCloudFactory springJCSMPFactoryCloudFactory;
        @Autowired(required=false) private SolaceServiceCredentials solaceServiceCredentials;
        @Autowired(required=false) private JCSMPProperties jcsmpProperties;


        private PubEventHandler pubEventHandler = new PubEventHandler();

        public void run(String... strings) throws Exception {
            final EndpointProperties endpointProps = new EndpointProperties();
            endpointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
            endpointProps.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);
            final JCSMPSession session = solaceFactory.createSession();
            session.isCapable(CapabilityType.ENDPOINT_MANAGEMENT);
            session.isCapable(CapabilityType.QUEUE_SUBSCRIPTIONS);
            Queue queue = JCSMPFactory.onlyInstance().createQueue("DemoQueue");

            //adds a subscription to the appliance.
            session.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);



            // Consumer session is now hooked up and running!

            /** Anonymous inner-class for handling publishing events */
            XMLMessageProducer prod = session.getMessageProducer(pubEventHandler);

            // Publish-only session is now hooked up and running!
            //Creates a message instance tied to that producer.

            TextMessage jcsmpMsg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);

            BufferedReader br = new BufferedReader(new FileReader( "C:\\Users\\Rshkpatel\\Desktop\\demo.txt"));
            String msg;
            while((msg=br.readLine()) != null){
                jcsmpMsg.setText(msg);
                log.info("============= Sending " + msg);
                prod.send(jcsmpMsg, queue);
            }
            br.close();
            //sets message content.
            //jcsmpMsg.setText(msg);

            //sets persistent delivery mode.
            //persistent(Guaranteed message) , sends message even if receiver is offline.
            //keeps copy until successfully deliverd.
            jcsmpMsg.setDeliveryMode(DeliveryMode.PERSISTENT);

            //log.info("============= Sending " + msg);
            //prod.send(jcsmpMsg, topic);

        }
    }

}
