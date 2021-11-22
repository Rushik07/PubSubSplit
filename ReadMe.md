
### Basic View of Project Goals
* To gain knowledge on working of publisher Subscriber model.
* Applying theory into a practical example.

#### Division of above goals into different tasks

### Task 1 - Gaining theory knowledge.
* Solace has a very well-defined doc on the Pub-Sub application.
* Everything is explained in detail.
* Link to the doc from Solace is mentioned below.
  * [Solace Docs](https://docs.solace.com/Solace-PubSub-Platform.htm)

#### Task 1.1 - Understanding about Pub-Sub+ Cloud.
* [Pub-Sub+ cloud](https://docs.solace.com/What-Is-PubSub-Cloud.htm)
* [Evants and Messages](https://docs.solace.com/ConceptMaps/Event-Stream-Maps.htm)
* Types of Messages-
  * [Direct messages](https://docs.solace.com/Basics/Direct-Messages.htm)
  * [Guaranteed messages](https://docs.solace.com/Basics/Guaranteed-Messages.htm)
* We have 2 types of Messsage Delivery Modes:
  * Persistent.
  * Non-Persistent.
  * We are using Persistent mode of delivering messages.

### Task 2 - Pub and Sub "Hello World application".
* Sending "Hello world" message from Publisher through a topic.
* Having Subscriber listening to the topic and printing "Hello world".
* Both Pub and Sub are to be done as a single application.
* We will start and maintain the git process throughout our project.

#### Task 2.1 - Building project.
* Build a spring project with maven dependencies and Lombok.
* Understand basics about Spring, Maven and Lombok with their uses.
#### Task 2.2 - Different Java classes.
* Make main Java class for PubSubApplication for subscribing to the topic and sending a message to the topic.
* Have PubEventHandler class to keep a check if the message was received else throw an error.
* Have DemoMessageConsumer class access the message from the topic and print the message using "log".

### Task 3 - Pub and Sub Split Application.
* Have a different Pub application and Sub application.
* Pub application sending "Hello World" message when PubServer.java main class is up and running.
* Sub application receiving "Hello World" message when SubServer.java main class is up and running.

#### Task 3.1 - Building Project.
* For this project we will make 2 maven modules, one for Pub and another for Sub.
#### Task 3.2 - Pub application.
* We will have PubServer.java as our main class.
  * In this class we will create a session to publish our message.
  * We will create an object and Extend it to PubEventHandler.java
  * Message creation is done here.
* PubEventHandler.java class
  * Here we will send a message to Solace appliance.
  * We will use the XMLMessageProducer interface to achieve the above point.
#### Task 3.3 - Sub application.
* We have SubServer.java as our main class.
  * In this class we will create a session to receive a message from the appliance.
  * And we will create an object of DemoMessageConsumer here.
  * We will start a session and close it once we will receive the required message.
  * We will use Latch countDown to set the time for waiting.
* DemoConsumer.java class
  * this class will hold latch count to 1 and will update it once we receive the message.
  * we use .getText() to receive message.
  * Then return latch which will decrement it.

### Task 4 - Pub-Sub (Split) application using text files.
* In this task are goal is to have a text file  "demo.txt" which will hold data and we need to read data from the file and send it on a topic from the publisher side.
* Subscriber will read the lines in an orderly fashion and write them down in a new file.
* **Note:** Files should maintain the line-by-line ordering of text.
#### Task 4.1 - Pub Server
* We will change our Pubserver.java code by adding a BufferedReader to read content from the demo.txt file.
*



### Task 5 - Adding Queue to Pub-Sub (Split).
* Here are the main goal is to remove the topic solace connection and add a Queue instead.

#### Task 5.1 - Publisher side server.
* We will abstract the Queue method.
* We will then create a queue session to start and close sessions and connect to a solace appliance.
* then we will publish a message on the queue.

#### Task 5.2 - Subscriber side server.
* We will abstract the queue method.
* We will also create a session for the Subscriber side.
* **Note -** On the Subscriber side we will use ConsumerFlowProperties which is used to :
  * Create a flow.
  * Bind to and consume messages from the Queue.
