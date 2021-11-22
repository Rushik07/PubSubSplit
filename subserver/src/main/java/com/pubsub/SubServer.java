package com.pubsub;

import com.solace.services.core.model.SolaceServiceCredentials;
import com.solacesystems.jcsmp.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@CommonsLog
public class SubServer {

    public static void main(String[] args) {
        SpringApplication.run(SubServer.class, args);
    }

    @Component
    static class Runner implements CommandLineRunner {
        //JCMPFactory is used to obtain instances of messaging system entities. Creating topic as well.
        //private final Topic topic = JCSMPFactory.onlyInstance().createTopic("tutorial/topic");
        private final Queue queue = JCSMPFactory.onlyInstance().createQueue("DemoQueue");
        @Autowired
        private SpringJCSMPFactory solaceFactory;
        // Examples of other beans that can be used together to generate a customized SpringJCSMPFactory
        @Autowired(required=false) private SpringJCSMPFactoryCloudFactory springJCSMPFactoryCloudFactory;
        @Autowired(required=false) private SolaceServiceCredentials solaceServiceCredentials;
        @Autowired(required=false) private JCSMPProperties jcsmpProperties;

        private DemoMessageConsumer msgConsumer = new DemoMessageConsumer();
        public void run(String... strings) throws Exception {

            final JCSMPSession session = solaceFactory.createSession();
            final EndpointProperties endpointProps = new EndpointProperties();
            // set queue permissions to "consume" and access-type to "exclusive"
            endpointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
            endpointProps.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);

            session.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);

            final ConsumerFlowProperties flow_prop = new ConsumerFlowProperties();
            flow_prop.setEndpoint(queue);
            flow_prop.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);

            EndpointProperties endpoint_props = new EndpointProperties();
            endpoint_props.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);
            //provides interface for application to receive messages from appliance.

            //XMLMessageConsumer cons = session.getMessageConsumer(msgConsumer);
            FlowReceiver cons= session.createFlow(msgConsumer, flow_prop);
            //adds a subscription to the appliance.
            //session.addSubscription(topic);
            log.info("Connected. Awaiting message...");
            //start receiving message
            cons.start();
            // Consumer session is now hooked up and running!

            /** Anonymous inner-class for handling publishing events */
            try {
                // block here until message received, and latch will flip.
                msgConsumer.getLatch().await(100, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error("I was awoken while waiting``");
            }
            // Close consumer
            cons.close();
            log.info("Exiting.");
            session.closeSession();
        }
    }

}
