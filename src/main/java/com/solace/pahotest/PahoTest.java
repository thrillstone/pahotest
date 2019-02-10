package com.solace.pahotest;

import org.eclipse.paho.client.mqttv3.*;

public class PahoTest {


    public static void main(String args[]) throws Exception {
        MqttClient client = new MqttClient(args[0], "any-name-you-want");
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setUserName("solace-cloud-client");
        connOpts.setPassword(args[1].toCharArray());

        client.setCallback(new MqttCallback() {

            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("\nReceived a Message!" +
                        "\n\tTopic:   " + topic +
                        "\n\tMessage: " + new String(message.getPayload()) +
                        "\n\tQoS:     " + message.getQos() + "\n");
            }

            public void connectionLost(Throwable cause) {
                System.out.println("Connection to Solace broker lost!" + cause.getMessage());
            }

            public void deliveryComplete(IMqttDeliveryToken token) {
            }

        });

        client.connect(connOpts);
        client.subscribe("test/pubsub", 0);
        MqttMessage message = new MqttMessage("Hello world from MQTT!".getBytes());
        message.setQos(0);
        client.publish("test/pubsub", message);

        Thread.sleep(5000);
    }
}
