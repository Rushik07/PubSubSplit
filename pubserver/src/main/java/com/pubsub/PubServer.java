package com.pubsub;


import com.solace.services.core.model.SolaceServiceCredentials;
import com.solacesystems.jcsmp.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;

@SpringBootApplication
@CommonsLog
public class PubServer {

    public static void main(String[] args) {
        SpringApplication.run(PubServer.class, args);
    }

    @Component
    static class Runner implements CommandLineRunner {

        //JCMPFactory is used to obtain instances of messaging system entities. Creating topic as well.
        private final Topic topic = JCSMPFactory.onlyInstance().createTopic("pub");
        //private final Queue queue = JCSMPFactory.onlyInstance().createQueue("DemoQueue");
        @Autowired
        private SpringJCSMPFactory solaceFactory;

        // Examples of other beans that can be used together to generate a customized SpringJCSMPFactory
        @Autowired(required=false) private SpringJCSMPFactoryCloudFactory springJCSMPFactoryCloudFactory;
        @Autowired(required=false) private SolaceServiceCredentials solaceServiceCredentials;
        @Autowired(required=false) private JCSMPProperties jcsmpProperties;


       private PubEventHandler pubEventHandler = new PubEventHandler();

        public void run(String... strings) throws Exception {


            final JCSMPSession session = solaceFactory.createSession();

            //final EndpointProperties endpointProps = new EndpointProperties();
            //endpointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
            //endpointProps.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);
            session.addSubscription(topic);
            //session.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);

            //adds a subscription to the appliance.
            // Consumer session is now hooked up and running!

            /** Anonymous inner-class for handling publishing events */
            XMLMessageProducer prod = session.getMessageProducer(pubEventHandler);
            // Publish-only session is now hooked up and running!
            //Creates a message instance tied to that producer.

            TextMessage jcsmpMsg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
            byte[] buffer = new byte[1024];
            FileInputStream in = new FileInputStream("C:\\Users\\Rshkpatel\\Desktop\\Sample.txt");
            int readContent= in.read(buffer);
            //String msg;
            while(readContent != -1){
                readContent = in.read(buffer);
                jcsmpMsg.setText(String.valueOf(readContent));
                log.info("============= Sending " + String.valueOf(readContent));
                prod.send(jcsmpMsg, topic);
            }
            in.close();

            //sets persistent delivery mode.
            //persistent(Guaranteed message) , sends message even if receiver is offline.
            //keeps copy until successfully deliverd.
            jcsmpMsg.setDeliveryMode(DeliveryMode.PERSISTENT);

        }
    }

}
