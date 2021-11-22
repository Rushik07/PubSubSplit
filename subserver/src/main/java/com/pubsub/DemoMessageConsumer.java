/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.pubsub;

import com.solacesystems.jcsmp.*;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;

import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;

@CommonsLog
//XMLMessageListener is used to receive messages asynchronously.
public class DemoMessageConsumer implements XMLMessageListener {
    //CountDownLatch here is used for thread sychronization.
    private CountDownLatch latch = new CountDownLatch(9);
    // Logging is done here.
    //BytesXMLMessage describe a messages that are sent or received.

    @SneakyThrows
    public void onReceive(BytesXMLMessage msg) {
        //TextMessage is used to send a message containing text.
        //Here we can have java code to write msg in a file.
        if (msg instanceof TextMessage) {
            FileOutputStream fo = new FileOutputStream("C:\\Users\\Rshkpatel\\Desktop\\Subdemo.txt");
            log.info("============= TextMessage received: " + ((TextMessage) msg).getText());
            fo.write(Integer.parseInt(((TextMessage) msg).getText()));
            fo.close();
        } else {
            log.info("============= Message received.");
        }

        latch.countDown();
    }

    public void onException(JCSMPException e) {
        log.info("Consumer received exception:", e);
        latch.countDown(); // unblock main thread
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}