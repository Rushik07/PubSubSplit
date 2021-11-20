
### Basic View of Project Goals
* To gain knowledge on working of publisher Subscriber model.
* Applying theory into practical example.

#### Division of above goals into different tasks

### Task 1 - Gaining theroy knowledge.
* Solace has a very well defined doc on Pub Sub application.
* Every thing is explained in detail.
* link to the doc from Solace is mentioned below.
    * [Solace Docs](https://docs.solace.com/Solace-PubSub-Platform.htm)

####Task 1.1 - Understanding about Pub-Sub+ Cloud.
* [Pub-Sub+ cloud](https://docs.solace.com/What-Is-PubSub-Cloud.htm)
* [Evants and Messages](https://docs.solace.com/ConceptMaps/Event-Stream-Maps.htm)
* Types of Messages-
  * [Direct messages](https://docs.solace.com/Basics/Direct-Messages.htm)
  * [Guaranteed messages](https://docs.solace.com/Basics/Guaranteed-Messages.htm)
* We have 2 types of Messsage Delivery Modes:
  * Persistent.
  * Non-Persistent.
  * We are using Persistent mode of delivering messages.

###Task 2 - Pub and Sub "Hello World application".
* Sending "Hello world" message from Publisher through a topic.
* Having Subscriber listening to the topic and printing "Hello world".
* Both Pub and Sub to be done as a single application.
* We will start and maintain git process throughout our project.

####Task 2.1 - Building project.
* Build a spring project with maven dependencies and Lombok.
* Understand basics about Spring, Maven and Lombok with their uses.
####Task 2.2 - Different Java classes.
* Make main Java class for PubSubApplication for subscribing to topic and sending messgage to the topic.
* Have PubEventHandler class to keep check if message was received else throw error.
* Have DemoMessageConsumer class to access the message from the topic and print the message using "log".

###Task 3 - Pub and Sub Split Application.
* Have a diiferent Pub application and Sub application.
* Pub application sending "Hello World" message when PubServer.java main class is up  and running.
* Sub application receiving "Hello World" message when SubServer.java main class is up and running.

####Task 3.1 - Buildng Project.
* For this project we will make 2 maven modules, one for Pub and another for Sub.
####Task 3.2 - Pub application.
* We will have PubServer.java as our main class. 
  * In this class we will creat a session to publish our message.
  * We will create an object and Extend to PubEventHandler.java
  * Message creation is done here.
* PubEventHandler.java class
  * Here we will send message to Solace appliance.
  * We will use XMLMessageProducer interface to achieve above point.
####Task 3.3 - Sub application.
* We have SubServer.java as our main class.
  * In this class we will create a session to receive message from appliance.
  * And we will create object of DemoMessageConsumer here.
  * We will start session and close it once we will receive the required messsage.
  * We will use Latch countDown to set time for waiting.
* DemoConsumer.java class
  * this calss will hold latch count to 1 and will update it once we receive message.
  * we use .getText() to receive message.
  * Then return latch which will decrement it.

###Task 4 - Pub Sub (Split) application using text files.
* In this task are goal is to have a text file  "demo.txt" which will hold data and we need to read data from file and send it on a topic from publisher side.
* Subscriber will read the lines in orderly fashion and write it down in a new file.
* **Note :** Files should maintain line by line ordering of text.



