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

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessageListener;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@CommonsLog
@Component
//XMLMessageListener is used to receive messages asynchronously.
public class DemoMessageConsumer implements XMLMessageListener {

    //CountDownLatch here is used for thread sychronization.
    private CountDownLatch latch = new CountDownLatch(10);
    private BufferedWriter bw;
    private boolean aborted;
    {
        try {
            bw = new BufferedWriter(new FileWriter("C:\\Users\\Rshkpatel\\Desktop\\Subdemo.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Logging is done here.
    //BytesXMLMessage describe a messages that are sent or received.
    public void onReceive(BytesXMLMessage msg) {
        //TextMessage is used to send a message containing text.
        //Here we can have java code to write msg in a file.

        if (msg instanceof TextMessage) {
            try {
                if(((TextMessage) msg).getText().equals("Error")){
                    log.warn("Error was found.");
                    try {
                        await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    bw.append(((TextMessage) msg).getText());
                    //log.info("appended "+ ((TextMessage) msg).getText());
                    bw.newLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("============= TextMessage received: " + ((TextMessage) msg).getText());
        } else {
            log.info("============= Message received.");
        }

        latch.countDown();
        msg.ackMessage();
    }
    /*public boolean check(String msg){
        if(msg == "101"){
            return true;
        }
        else{
            return false;
        }
    }*/

    public void await() throws InterruptedException {
        latch.await();
        if (aborted) {
            try {
                throw new AbortedException();
            } catch (AbortedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onException(JCSMPException e) {
        log.error("Consumer received exception:", e);
        latch.countDown();// unblock main thread

    }
    public void CloseFile(){
        try {
            bw.close();
            log.info("Subdemo.txt file closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public static class AbortedException extends InterruptedException {
        public AbortedException() {

        }

        public AbortedException(String detailMessage) {
          super(detailMessage);
        }
    }

}